package cz.muni.fi.storm.bolts;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;
import cz.muni.fi.storm.tools.ServiceCounter;
import cz.muni.fi.storm.tools.writers.FileWriter;
import cz.muni.fi.storm.tools.writers.Writer;
import java.util.Map;

public class FileWriterBolt extends BaseRichBolt {

    private Writer fileWriter;
    private ServiceCounter counter;

    @Override
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        String filePath = (String) stormConf.get("fileWriter.filePath");
        this.fileWriter = new FileWriter(filePath);
        
        String broker = (String) stormConf.get("kafkaProducer.broker");
        int port = new Integer(stormConf.get("kafkaProducer.port").toString());
        this.counter = new ServiceCounter(broker, port);
    }

    @Override
    public void execute(Tuple tuple) {
        String flow = tuple.getValue(0).toString();
        fileWriter.send(flow);
        counter.count();
    }
    
    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {}

    @Override
    public void cleanup() {
        fileWriter.close();
        counter.close();
    }
}
