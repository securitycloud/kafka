<i>Build project:</i> It works in folder <b>kafka/kafka-storm</b> (where pom.xml exist):

        mvn clean package

<i>Default configuration for project:</i> It is localized in file:

        kafka/kafka-storm/src/resources/kafka.properties

Run kafka producer
----------------------------

It fills topic by input data :

        java -cp target/kafka-storm-1.0-SNAPSHOT-jar-with-dependencies.jar cz.muni.fi.kafka.storm.KafkaProducer path/input-file batch-size

Run kafka consumer
----------------------------

It downloads topic to standard output:

        java -cp target/kafka-storm-1.0-SNAPSHOT-jar-with-dependencies.jar cz.muni.fi.kafka.storm.KafkaConsumer