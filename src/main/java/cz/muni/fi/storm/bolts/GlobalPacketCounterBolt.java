package cz.muni.fi.storm.bolts;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import cz.muni.fi.storm.tools.TupleUtils;
import cz.muni.fi.storm.tools.pojo.PacketCount;
import cz.muni.fi.storm.tools.writers.KafkaProducer;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class GlobalPacketCounterBolt extends BaseRichBolt {

    private ObjectMapper mapper;
    private HashMap<String, Long> totalCounter;
    private int numberOfComputers;
    private int numberOfActualReceives;
    private KafkaProducer kafkaProducer;

    public GlobalPacketCounterBolt(int numberOfComputers) {
        this.numberOfComputers = numberOfComputers;
    }

    @Override
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        this.mapper = new ObjectMapper();
        this.totalCounter = new HashMap<String, Long>();
        this.numberOfActualReceives = 0;
        
        String broker = (String) stormConf.get("kafkaProducer.broker");
        int port = new Integer(stormConf.get("kafkaProducer.port").toString());
        String topic = (String) stormConf.get("kafkaProducer.topic");
        kafkaProducer = new KafkaProducer(broker, port, topic);
    }

    @Override
    public void execute(Tuple tuple) {
        if (TupleUtils.isEndOfWindow(tuple)) {
            numberOfActualReceives++ ;
            if (numberOfActualReceives == numberOfComputers) {
                for (String ip : totalCounter.keySet()) {
                    long packets = totalCounter.get(ip);
                    PacketCount packetCount = new PacketCount();
                    packetCount.setDst_ip_addr(ip);
                    packetCount.setPackets(packets);
                    try {
                        String packetCountJson = new ObjectMapper().writer()
                                .withDefaultPrettyPrinter().writeValueAsString(packetCount);
                        kafkaProducer.send(packetCountJson);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException("Can not create JSON from PacketCount", e);
                    }
                }
            }
        } else {
            String packetCountJson = tuple.getString(0);

            try {
                PacketCount packetCount = mapper.readValue(packetCountJson, PacketCount.class);
                String ip = packetCount.getDst_ip_addr();
                long packets = packetCount.getPackets() + totalCounter.get(ip);
                totalCounter.put(ip, packets);

            } catch (IOException e) {
                throw new RuntimeException("Excepted JSON format of PacetCount", e);
            }
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {}
    
    @Override
    public void cleanup() {
        kafkaProducer.close();
    }
}
