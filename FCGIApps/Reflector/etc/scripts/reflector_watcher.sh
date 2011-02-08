#!/bin/bash
keepRunning=1

while [ $keepRunning -eq 1 ]
do

for FILE in `ls *.pid`
do
INDEX=`echo $FILE | awk '{print substr($1,0,2)}'`
source reflector_daemon_checker.sh $INDEX
done

sleep 3
done
