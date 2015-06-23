Run kafka producer
============================

1.) to create topic with partitions try:

        bin/kafka-topics.sh --delete --zookeeper localhost:2181 --topic storm-test
        bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 5 --topic storm-test

2.) to build in folder <b>kafka/kafka-storm</b> (where pom.xml exist) try:

        mvn clean package

3.) to execute jar file try:

        java -jar target/kafka-storm-1.0-SNAPSHOT-jar-with-dependencies.jar path/input-file batch-size


Default topic is <b>storm-test</b>.

Default broker is <b>localhost:9092</b>.
