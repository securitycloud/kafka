Run kafka producer
============================

1.) to build in folder <b>kafka/kafka-rado</b> try:

        mvn clean compile assembly:single

2.) to execute jar file try:

        java -jar target/kafka-rado-1.0-SNAPSHOT-jar-with-dependencies.jar path/input-file 

Default topic is <b>securitycloud-testing-data</b>.

Default broker is <b>localhost:9092</b>.
