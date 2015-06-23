package cz.muni.fi.kafka.storm;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.message.MessageAndMetadata;

public class KafkaConsumer {
    
    public static void main(String[] args) {
        String topic = "storm-test";
        
        Properties props = new Properties();
        props.put("zookeeper.connect", "localhost:2181");
        props.put("group.id", "myGroup");
        props.put("zookeeper.session.timeout.ms", "6000");
        props.put("zookeeper.sync.time.ms", "2000");
        props.put("auto.commit.interval.ms", "60000");
        ConsumerConfig consumerConfig = new ConsumerConfig(props);
        
        ConsumerConnector consumer = Consumer.createJavaConsumerConnector(consumerConfig);
        Map<String, Integer> topicCount = new HashMap();
        topicCount.put(topic, 3); // number of threats
        Map<String, List<KafkaStream<byte[], byte[]>>> consumerStreams = consumer.createMessageStreams(topicCount);
        List<KafkaStream<byte[], byte[]>> streams = consumerStreams.get(topic);
        for (final KafkaStream stream : streams) {
            ConsumerIterator<byte[], byte[]> ci = stream.iterator();
            while (ci.hasNext()) {
                MessageAndMetadata<byte[], byte[]> mam = ci.next();
                System.out.println(mam.partition() + "  " + new String(mam.message()));
            }
        }
    }
}
