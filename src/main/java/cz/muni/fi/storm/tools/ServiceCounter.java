package cz.muni.fi.storm.tools;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.OutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import java.util.Map;

public class ServiceCounter {

    private static final String streamIdForService = "service";
    private final boolean isSpout;
    private final OutputCollector boltCollector;
    private final SpoutOutputCollector spoutCollector;
    private long countToEmit;
    private long messagesPerPartition;
    private long cleanUpEveryFlows;
    private boolean isFirstPassed = false;
    private long totalCount = 0;
    
    public ServiceCounter(OutputCollector boltCollector, Map conf) {
        this.boltCollector = boltCollector;
        this.spoutCollector = null;
        this.isSpout = false;
        setup(conf);
    }
    
    public ServiceCounter(SpoutOutputCollector spoutCollector, Map conf) {
        this.boltCollector = null;
        this.spoutCollector = spoutCollector;
        this.isSpout = true;
        setup(conf);
    }
    
    private void setup(Map conf) {
        this.messagesPerPartition = new Long(conf.get("countWindow.messagesPerPartition").toString());
        this.countToEmit = new Long(conf.get("countWindow.messagesPerWindow").toString());
        this.cleanUpEveryFlows = new Long(conf.get("countWindow.messagesPerWindow").toString());
    }
    
    public void count() {
        begin();
        
        totalCount++;
        //if (totalCount % countToEmit == 0) {
        //    emit(countToEmit);
        //}
    }
    
    private void begin() {
        if (!isFirstPassed) {
            emit(0);
            isFirstPassed = true;
        }
    }
    
    private void emit(Object message) {
        if (isSpout) {
            spoutCollector.emit(streamIdForService, new Values(message));
        } else {
            boltCollector.emit(streamIdForService, new Values(message));
        }
    }
    
    public boolean isEnd() {
        return totalCount % messagesPerPartition == 0;
    }
    
    public boolean isTimeToClean() {
        return totalCount % cleanUpEveryFlows == 0;
    }
    
    public static void declareServiceStream(OutputFieldsDeclarer declarer) {
        declarer.declareStream(streamIdForService, new Fields("count"));
    }
    
    public static String getStreamIdForService() {
        return streamIdForService;
    }
}
