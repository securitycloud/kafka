#!/bin/bash

. scripts/setenv.sh

if [ -z "$1" ] 
then
    echo "You must specify server"
    exit 1;
fi

SERVER=$1

# RECREATE WORKING DIRECTORY
ssh root@$SERVER "
    rm -rf $WRK
    mkdir -p $WRK
"