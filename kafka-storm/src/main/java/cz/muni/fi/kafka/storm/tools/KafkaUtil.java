package cz.muni.fi.kafka.storm.tools;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class KafkaUtil {
    
    private static final String propertiesFile = "/kafka.properties";
    
    public static Properties loadProperties() {
        Properties properties = new Properties();
        try {
            InputStream inputStream = KafkaUtil.class.getResourceAsStream(propertiesFile);
            properties.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("Properties file is corrupted", e);
        }
        return properties;
    }
}
