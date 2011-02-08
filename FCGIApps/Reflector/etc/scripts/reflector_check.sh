#!/bin/bash


for FILE in `ls *.pid`
do
INDEX=`echo $FILE | awk '{print substr($1,0,2)}'`
source reflector_daemon_status.sh $INDEX
done

