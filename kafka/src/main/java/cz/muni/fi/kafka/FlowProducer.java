package cz.muni.fi.kafka;

import java.io.File;
import java.util.Properties;
import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

public class FlowProducer {
    public static void main(String[] args) {
        if (args.length < 2) {
            //throw new IllegalArgumentException("Missing argument: input_file or name_of_topic." );
        }
        String inputFile = "/mnt/data/radozaj/Masarykova univerzita/Magisterske studium/diplomovka/out1k"; //args[0];
        String topic = "securitycloud-testing-data"; //args[1];

        Properties props = new Properties();
        props.put("metadata.broker.list", "localhost:9092");
        props.put("serializer.class", "kafka.serializer.StringEncoder");
        //props.put("partitioner.class", "cz.muni.fi.kafka.SimplePartitioner");
        props.put("request.required.acks", "1");

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
