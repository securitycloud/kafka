#!/bin/bash

. scripts/setenv.sh

if [ -z "$1" ] 
then
    echo -e $ERR You must specify Topology $OFF
    exit 1;
fi
TOPOLOGY=$1

if [ -z "$2" ] 
then
    echo -e $ERR You must specify Number of computers $OFF
    exit 2;
fi
COMPUTERS=$2

if [ -z "$3" ] 
then
    echo -e $ERR You must specify Number of partitions $OFF
    exit 3;
fi
PARTITIONS=$3

if [ -z "$4" ] 
then
    echo -e $ERR You must specify Batch size $OFF
    exit 4;
fi
BATCH_SIZE=$4


echo -e $LOG Recreating output topic $TESTING_TOPIC with 1 partitions on $KAFKA_CONSUMER $OFF
scripts/run-topic.sh $TESTING_TOPIC 1 $KAFKA_CONSUMER

STORM_EXE=$WRK/storm/bin/storm
STORM_JAR=$WRK/project/target/storm-1.0-SNAPSHOT-jar-with-dependencies.jar

echo -e $LOG Logging info to service topic: $SERVICE_TOPIC $OFF
ssh root@$KAFKA_CONSUMER "
    echo Type=read, Topology=$TOPOLOGY, Computers=$COMPUTERS, Partitions=$PARTITIONS, BatchSize=$BATCH_SIZE |
        $KAFKA_INSTALL/bin/kafka-console-producer.sh --topic $SERVICE_TOPIC --broker-list localhost:9092
"

echo -e $LOG Running topology $TOPOLOGY on $COMPUTERS computers $OFF
ssh root@$SRV_NIMBUS "
    $STORM_EXE jar $STORM_JAR cz.muni.fi.storm.$TOPOLOGY $COMPUTERS $KAFKA_PRODUCER $KAFKA_CONSUMER true
"

sleep 420

scripts/kill-topology.sh $TOPOLOGY