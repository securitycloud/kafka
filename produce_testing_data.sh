#!/bin/bash -e
# Licensed to the Apache Software Foundation (ASF) under one
# or more contributor license agreements.  See the NOTICE file
# distributed with this work for additional information
# regarding copyright ownership.  The ASF licenses this file
# to you under the Apache License, Version 2.0 (the
# "License"); you may not use this file except in compliance
# with the License.  You may obtain a copy of the License at
#
#   http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing,
# software distributed under the License is distributed on an
# "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
# KIND, either express or implied.  See the License for the
# specific language governing permissions and limitations
# under the License.

# This script will generate wikipedia-raw data to Kafka

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
BASE_DIR=$(dirname $DIR)

#ZOOKEEPER=localhost:2181
#KAFKA_BROKER=localhost:9092

ZOOKEEPER=10.16.31.201:2181
KAFKA_BROKER=10.16.31.201:9092

# overwritten options
while getopts "z:b:" option
do
  case ${option} in 
    z) ZOOKEEPER="${OPTARG}";;
    b) KAFKA_BROKER="${OPTARG}";;
  esac
done
echo "Using ${ZOOKEEPER} as the zookeeper. You can overwrite it with '-z yourlocation'"
echo "Using ${KAFKA_BROKER} as the kafka broker. You can overwrite it with '-b yourlocation'"

# check if the topic exists. if not, create the topic
EXIST=$(/root/kafka/kafka_2.11-0.8.2.1/bin/kafka-topics.sh --describe --topic securitycloud-testing-data --zookeeper $ZOOKEEPER)
if [ -z "$EXIST" ]
  then
   /root/kafka/kafka_2.11-0.8.2.1/bin/kafka-topics.sh --create --zookeeper $ZOOKEEPER --topic securitycloud-testing-data --partition 12 --replication-factor 1
fi

# produce data ten times
for var in 0 1 2 3 4 5 6 7 8 9
do 
  /root/kafka/kafka_2.11-0.8.2.1/bin/kafka-console-producer.sh < /root/dataset_json.json --batch-size 10000 --topic securitycloud-testing-data --broker $KAFKA_BROKER
done

