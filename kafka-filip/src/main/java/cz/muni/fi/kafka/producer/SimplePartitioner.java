package cz.muni.fi.kafka.producer;

import java.util.*;
import kafka.producer.Partitioner;
import kafka.utils.VerifiableProperties;
 
public class SimplePartitioner implements Partitioner {
    
    private static volatile int counter = 0;
    private static final Object counterLock = new Object();

    public SimplePartitioner (VerifiableProperties props) {
        
    }
 
    public int partition(Object key, int a_numPartitions) {
        synchronized (counterLock) {
            counter = (counter + 1) % a_numPartitions;
            return counter;
        }
    }
 
}
