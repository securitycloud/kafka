package cz.muni.fi.storm.bolts;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;
import cz.muni.fi.storm.tools.ServiceCounter;
import cz.muni.fi.storm.tools.writers.KafkaProducer;
import java.util.Map;

public class KafkaBolt extends BaseRichBolt {

    private KafkaProducer kafkaProducer;
    private ServiceCounter counter;

    @Override
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        String broker = (String) stormConf.get("kafkaProducer.broker");
        int port = new Integer(stormConf.get("kafkaProducer.port").toString());
        String topic = (String) stormConf.get("kafkaProducer.topic");
        int totalTasks = context.getComponentTasks(context.getThisComponentId()).size();
        
        this.kafkaProducer = new KafkaProducer(broker, port, topic);
        this.counter = new ServiceCounter(collector, stormConf);
    }

    @Override
    public void execute(Tuple tuple) {
        kafkaProducer.send(tuple.getValue(0).toString());
        counter.count();
    }
    
    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        ServiceCounter.declareServiceStream(declarer);
    }

    @Override
    public void cleanup() {
        kafkaProducer.close();
    }
}