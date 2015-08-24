package cz.muni.fi.kafka.producer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.stream.Stream;

/**
 * Reads file and send lines to kafka.
 */
public class FromFileProducer {
    
    public static final int DEFAULT_BATCH_SIZE = 5000;

    /**
     * Start producer and send file content to kafka
     *
     * @param args first argument is batch size (default 5000)
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        KafkaProducer<String, String> prod = new KafkaProducer<>(createProducerConfig(args));
        Properties props = getKafkaProperties();
        final String filepath = props.getProperty("file");
        final String topic = props.getProperty("producer.topic");
        try (Stream<String> stream = Files.lines(Paths.get(filepath),Charset.defaultCharset())) {
            stream.forEach(line -> {
                prod.send(new ProducerRecord<>(topic, line));
            });
        }
    }

    /**
     * ProducerConfig properties that are set here.
     *
     * @return
     */
    private static Properties createProducerConfig(String[] args) {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put("producer.type", "async");
        int batchSize = DEFAULT_BATCH_SIZE;
        if (args.length > 0) {
            try {
                batchSize = Integer.parseInt(args[0]);
            } catch (NumberFormatException numberFormatException) {
            }
        }
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, batchSize);
        props.put("partitioner.class", "cz.muni.fi.kafka.producer.SimplePartitioner");
        props.put("request.required.acks",0);
        props.put(ProducerConfig.ACKS_CONFIG, "0");
        return props;
    }

    /**
     * Parsers src/main/resources/kafka.properties file into java.util.Properties object.
     *
     * @return java.util.Properties parsed properties
     */
    public static Properties getKafkaProperties() {
        Properties prop = new Properties();
        try (InputStream input = FromFileProducer.class.getClassLoader().getResourceAsStream("kafka.properties")) {
            prop.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return prop;
    }
}
