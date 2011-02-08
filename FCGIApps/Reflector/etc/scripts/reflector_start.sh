#!/bin/bash
export LD_LIBRARY_PATH=`pwd`/libs:$LD_LIBRARY_PATH
source reflector_stop.sh
source reflector_daemon_spawn.sh 00 > /dev/null
source reflector_daemon_spawn.sh 01 > /dev/null
source reflector_daemon_spawn.sh 02 > /dev/null
source reflector_daemon_spawn.sh 03 > /dev/null
source reflector_daemon_spawn.sh 04 > /dev/null
source reflector_daemon_spawn.sh 05 > /dev/null
source reflector_daemon_spawn.sh 06 > /dev/null
source reflector_daemon_spawn.sh 07 > /dev/null
source reflector_daemon_spawn.sh 08 > /dev/null
source reflector_daemon_spawn.sh 09 > /dev/null
#touch nohup.out
nohup `pwd`/reflector_watcher.sh & > /dev/null
clear
echo spawn complete. checking status...
source ./reflector_check.sh
