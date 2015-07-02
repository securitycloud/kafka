package cz.muni.fi.kafka.storm;

import java.io.File;
import java.util.Properties;
import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

public class KafkaProducer {
    public static void main(String[] args) {
        if (args.length < 2) {
            throw new IllegalArgumentException("Missing argument: input_file batch_size" );
        }
        String inputFile = args[0];
        
        String batchSize = args[1];
        try {
            Integer.parseInt(batchSize);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Argument batch size is not number" );
        }
        
        String topic = "storm-test";
        
        Properties props = new Properties();
        props.put("metadata.broker.list", "localhost:9092");
        props.put("serializer.class", "kafka.serializer.StringEncoder");
        props.put("partitioner.class", "cz.muni.fi.kafka.storm.RoundRobinPartitioner");
        props.put("request.required.acks", "0");
        props.put("producer.type", "async");
        props.put("batch.size", batchSize);

        ProducerConfig config = new ProducerConfig(props);
        Producer<String, String> producer = new Producer<String, String>(config);
        
        System.out.println("1 * = 1 M lines sent");
        FlowSource flowSource = new FileFlowSource(new File(inputFile));
        String flow;
        int count = 0;
        while ((flow = flowSource.nextFlow()) != null) {
            KeyedMessage<String, String> data = new KeyedMessage<String, String>(topic, "", flow);
            producer.send(data);
            if (count == 1000000) {
                count = 0;
                System.out.print("*");
            }
            count++;
        }
        System.out.println();
        producer.close();
    }
}
