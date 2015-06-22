package cz.muni.fi.kafka.storm;

import kafka.producer.Partitioner;
import kafka.utils.VerifiableProperties;

public class RoundRobinPartitioner implements Partitioner {
    
    int partition = 0;
    
    public RoundRobinPartitioner (VerifiableProperties props) {
 
    }
 
    @Override
    public int partition(Object key, int numberOfPartitions) {
        partition++;
        if (partition == numberOfPartitions) {
            partition = 0;
        }
        return partition;
  }
 
}