Run kafka producer
============================

1.) to build in folder <b>kafka/kafka-storm</b> (where pom.xml exist) try:

        mvn clean package

2.) to execute jar file try:

        java -jar target/kafka-storm-1.0-SNAPSHOT-jar-with-dependencies.jar path/input-file batch-size


Default topic is <b>storm-test</b>.

Default broker is <b>localhost:9092</b>.
