package cz.muni.fi.kafka.producer;

import java.util.*;
import kafka.producer.Partitioner;
import kafka.utils.VerifiableProperties;
 
public class SimplePartitioner implements Partitioner {
    
    private Random rnd;

    public SimplePartitioner (VerifiableProperties props) {
        rnd = new Random();
    }
 
    public int partition(Object key, int a_numPartitions) {
        int partition = rnd.nextInt(16) % a_numPartitions;
        return partition;
    }
 
}
