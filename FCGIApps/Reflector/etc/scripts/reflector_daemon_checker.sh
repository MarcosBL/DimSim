#!/bin/bash
EXENAME=`pwd`/dimdimReflector
PID=`cat $1.pid`
CHECKEDPID=`ps -ef | grep $EXENAME | grep $PID | grep -v grep | awk '{print $2}'`
if [ "$CHECKEDPID" != "$PID" ]
then
echo "`date` - $PID has died. restarting..."
source ./reflector_daemon_spawn.sh $1 
fi
