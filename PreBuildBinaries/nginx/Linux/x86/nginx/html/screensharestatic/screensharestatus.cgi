#!/bin/bash
LIGHTY_PID=`ps -ef | grep "sbin/lighttpd -f lighttpd.dimdim.cfg" | grep -v grep | awk '{print $2}'`
DDSS_PIDS=`ps -ef | grep dimdimReflector | grep -v grep | awk '{print $2}'`

echo Content-type: text/plain
echo ""

#
#TODO: count pids and compare against pre-configured FCGI process count and add MINOR/MAJOR
# 
if [ "$LIGHTY_PID" ] 
then
echo "<NORMAL> DIMDIM REFLECTOR IS UP"
else
echo "<CRITICAL> DIMDIM REFLECTOR IS DOWN"
fi