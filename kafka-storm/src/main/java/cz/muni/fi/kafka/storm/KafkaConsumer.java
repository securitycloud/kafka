package cz.muni.fi.kafka.storm;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import kafka.api.FetchRequest;
import kafka.api.FetchRequestBuilder;
import kafka.api.PartitionOffsetRequestInfo;
import kafka.common.TopicAndPartition;
import kafka.javaapi.FetchResponse;
import kafka.javaapi.OffsetResponse;
import kafka.javaapi.consumer.SimpleConsumer;
import kafka.javaapi.message.ByteBufferMessageSet;
import kafka.message.MessageAndOffset;

public class KafkaConsumer {

    private static final String clientName = "service";
    private static final String broker = "localhost";
    private static final int port = 9092;
    private static final String topic = "storm-service";
    private static final int partition = 0;

    public static void main(String[] args) {

        // Prepare kafka consumer
        SimpleConsumer consumer = null;
        try {
             consumer = new SimpleConsumer(broker, port, 100000, 64 * 1024, clientName);


            // Get offset from beginning
            Map<TopicAndPartition, PartitionOffsetRequestInfo> requestInfo =
                    new HashMap<TopicAndPartition, PartitionOffsetRequestInfo>();
            TopicAndPartition topicAndPartition = new TopicAndPartition(topic, partition);
            PartitionOffsetRequestInfo partitionOffsetRequestInfo =
                    new PartitionOffsetRequestInfo(kafka.api.OffsetRequest.EarliestTime(), 1);
            requestInfo.put(topicAndPartition, partitionOffsetRequestInfo);
            kafka.javaapi.OffsetRequest request = new kafka.javaapi.OffsetRequest(
                    requestInfo, kafka.api.OffsetRequest.CurrentVersion(), clientName);
            OffsetResponse response = consumer.getOffsetsBefore(request);
            if (response.hasError()) {
                short errorCode = response.errorCode(topic, partition);
                System.err.println("ERROR " + errorCode + " during get offset from beginning");
            }
            long[] offsets = response.offsets(topic, partition);
            long readOffset = offsets[0];

            
            // Fetch request and response
            FetchRequestBuilder builder = new FetchRequestBuilder().clientId(clientName);
            builder.addFetch(topic, partition, readOffset, 100000);
            FetchRequest req = builder.build();
            FetchResponse fetchResponse = consumer.fetch(req);
            if (fetchResponse.hasError()) {
                short errorCode = response.errorCode(topic, partition);
                System.err.println("ERROR " + errorCode + " during fetch response");
            }
            List<ByteBufferMessageSet> listByteBufferMessageSets = new ArrayList<ByteBufferMessageSet>();
            ByteBufferMessageSet byteBufferMessageSet = fetchResponse.messageSet(topic, partition);
            listByteBufferMessageSets.add(byteBufferMessageSet);
            for (MessageAndOffset messageAndOffset : listByteBufferMessageSets.get(0)) {
                long currentOffset = messageAndOffset.offset();
                if (currentOffset < readOffset) {
                    System.err.println("ERROR: read offset is history");
                }
                
                
                // Parse message
                readOffset = messageAndOffset.nextOffset();
                ByteBuffer payload = messageAndOffset.message().payload();
                byte[] bytes = new byte[payload.limit()];
                payload.get(bytes);
                try {
                    System.out.println(new String(bytes, "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    System.err.println("ERROR: could not create string in UTF-8");
                }
            }
        } finally {
            if (consumer != null) {
                consumer.close();
            }
        }
    }
}
