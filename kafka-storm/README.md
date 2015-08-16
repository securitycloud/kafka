<i>Build project:</i> It works in folder <b>kafka/kafka-storm</b> (where pom.xml exist):

        mvn clean package

Run kafka producer
----------------------------

It fills topic <b>storm-test</b> on localhost by input data :

        java -cp target/kafka-storm-1.0-SNAPSHOT-jar-with-dependencies.jar cz.muni.fi.kafka.storm.KafkaProducer path/input-file batch-size

Run kafka consumer
----------------------------

It downloads topic <b>storm-service</b> on localhost to standard output:

        java -cp target/kafka-storm-1.0-SNAPSHOT-jar-with-dependencies.jar cz.muni.fi.kafka.storm.KafkaConsumer