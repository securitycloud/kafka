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

    public static void main(String[] args) throws IOException {
        KafkaProducer<String, String> prod = new KafkaProducer<>(createProducerConfig());
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
    private static Properties createProducerConfig() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
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
