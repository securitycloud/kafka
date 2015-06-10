package cz.muni.fi.kafka.rado;

import java.io.File;
import java.util.Properties;
import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

public class FlowProducer {
    public static void main(String[] args) {
        if (args.length < 1) {
            throw new IllegalArgumentException("Missing argument: input_file [name_of_topic]" );
        }
        String inputFile = args[0];
        
        String topic;
        if (args.length >= 2) {
             topic = args[1];
        } else {
            topic = "securitycloud-testing-data";
        }
        
        Properties props = new Properties();
        props.put("metadata.broker.list", "localhost:9092");
        props.put("serializer.class", "kafka.serializer.StringEncoder");
        props.put("request.required.acks", "1");
        props.put("producer.type", "async");

        ProducerConfig config = new ProducerConfig(props);
        Producer<String, String> producer = new Producer<String, String>(config);
        
        FlowSource flowSource = new FileFlowSource(new File(inputFile));
        String flow;
        while ((flow = flowSource.nextFlow()) != null) {
            KeyedMessage<String, String> data = new KeyedMessage<String, String>(topic, flow);
            producer.send(data);
        }
        producer.close();
    }
}
