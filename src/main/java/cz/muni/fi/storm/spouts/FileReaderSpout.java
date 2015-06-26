package cz.muni.fi.storm.spouts;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import jline.internal.InputStreamReader;

public class FileReaderSpout extends BaseRichSpout {

    private SpoutOutputCollector collector;
    private File fileSource;
    private BufferedReader reader = null;
    private long count;

    public FileReaderSpout(String filePath) {
        fileSource = new File(filePath);
    }
    
    @Override
    public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileSource)));
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("Source file for flows doesnt exist", e);
        }
        this.collector = collector;
    }
    
    @Override
    public void nextTuple() {
        String flow = nextLine();
        if (flow != null) {
            count++;
            //this.collector.emit(new Values(flow), count); // anchoring
            this.collector.emit(new Values(flow)); // without anchoring
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("flow"));
    }
    
    private String nextLine() {
        if (isClosed()) {
            return null;
        }

        String line;

        try {
            line = reader.readLine();
        } catch (IOException e) {
            throw new RuntimeException("Error while reading from file: " + fileSource.getAbsolutePath(), e);
        }

        if (line == null) {
            closeFile();
            return null;
        }

        return line;
    }
	
    private void closeFile() {
        try {
            reader.close();
        } catch (IOException e) {
            throw new RuntimeException("Error closing file: " + fileSource.getAbsolutePath(), e);
        }
        reader = null;
    }
    
    private boolean isClosed() {
        return reader == null;
    }
}
