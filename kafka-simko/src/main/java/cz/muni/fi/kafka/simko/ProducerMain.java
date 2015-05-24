package cz.muni.fi.kafka.simko;

import java.io.*;
import java.util.*;

import jdk.nashorn.internal.parser.JSONParser;
import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

public class ProducerMain {
    public static void main(String[] args) {

        Properties props = new Properties();
        props.put("metadata.broker.list", "localhost:9092");
        props.put("serializer.class", "kafka.serializer.StringEncoder");
      //  props.put("partitioner.class", "cz.muni.fi.kafka.SimplePartitioner");
        props.put("request.required.acks", "1");

        ProducerConfig config = new ProducerConfig(props);

        Producer<String, String> producer = new Producer<String, String>(config);
        BufferedReader bufferedReader=null;


        try {
             bufferedReader = new BufferedReader(new FileReader(new File("/root/megaOut")));
            try {
                String line = bufferedReader.readLine();
                while(line!= null){
                    //String string = line;
                    //JSONParser jsonParser = new JSONParser();
                    //JSONObject flow = (JSONObject) jsonParser.parse(line);
                    KeyedMessage<String,String> data= new KeyedMessage<String, String>("securitycloud-testing-data",line);
                    producer.send(data);
                    line=bufferedReader.readLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }finally{
            producer.close();
            try {
                if (bufferedReader != null) bufferedReader.close();
            }catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
