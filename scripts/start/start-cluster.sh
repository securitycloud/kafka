#!/bin/bash

CUR_DIR=`dirname $0`
. $CUR_DIR/../setenv.sh

echo -e $LOG Starting zookeeper on $SRV_ZK $OFF
$CUR_DIR/start-zk.sh $SRV_ZK

sleep 5
echo -e $LOG Starting nimbus and ui on $SRV_NIMBUS $OFF
$CUR_DIR/start-nimbus.sh $SRV_NIMBUS


for i in "${SRV_SUPERVISOR[@]}"
do
    echo -e $LOG Starting supervisor on $i $OFF
    $CUR_DIR/start-supervisor.sh $i
done


