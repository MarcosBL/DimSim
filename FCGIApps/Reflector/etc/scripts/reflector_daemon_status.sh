#!/bin/bash
EXENAME=`pwd`/dimdimReflector
PID=`cat $1.pid`
CHECKEDPID=`ps -ef | grep $EXENAME | grep $PID | grep -v grep | awk '{print $2}'`
if [ "$CHECKEDPID" == "$PID" ]
then
echo $PID is alive
else
echo "$PID has died"
fi
