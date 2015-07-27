All scripts run in folder <b>storm</b> (where <b>pom.xml</b> exist).

Configuration
============================

Default configuration for zookeeper:

        config/zoo.cfg

Default configuration for storm:

        config/storm.yaml

Default configuration for scripts:

        scripts/setenv.sh

Default configuration for this project:

        src/main/resources/storm.properties

Prepare Storm on cluster
============================

1.) <i>Clean all PCs in cluster:</i> kill all java programs on work PCs.
Clean work directory on all PCs in cluster.

        scripts/clean/clean-cluster.sh

2.) <i>Install all PCs in cluster:</i> copy zookeeper, storm and configured them.
Download and compile kafka-storm on kafka PCs. Download and compile project to nimbus.

        scripts/install/install-cluster.sh

3.) <i>Start all PCs in cluster:</i> start zookeeper, nimbus, ui and supervisors.

        scripts/start/start-cluster.sh

Run Storm on cluster
============================

<i>Run test on cluster:</i> <b>ReadWrite test</b> open testing kafka topics, start topology for actual test
and begin sent testing data to topology. If readWrite test finish producing data, then it kill topology.
<b>Read test</b> open testing kafka topic only on kafka-consumer (topic on kafka-producer must exist and filled)
and start topology for actual test. Read test wait 7 minutes and kill topology.

All topologies are sent delay between every millionth flow in ms to kafka-consumer topic <b>storm-service</b>.
Default kafka topic is <b>storm-test</b>.

a) For topology KafkaConsumerSpout -> KafkaProducerBolt:

        scripts/run/run-test-read.sh TopologyKafkaKafka number_of_computers partitions batch_size
        scripts/run/run-test-readwrite.sh TopologyKafkaKafka number_of_computers partitions batch_size

b) For topology KafkaConsumerSpout -> FilterBolt -> KafkaProducerBolt:

        scripts/run/run-test-read.sh TopologyKafkaFilterKafka number_of_computers partitions batch_size
        scripts/run/run-test-readwrite.sh TopologyKafkaFilterKafka number_of_computers partitions batch_size

z) For topology FileReaderSpout -> FileWriterBolt:

        scripts/run/run-test-read.sh TopologyFileFile number_of_computers partitions batch_size
        scripts/run/run-test-readwrite.sh TopologyFileFile number_of_computers partitions batch_size
