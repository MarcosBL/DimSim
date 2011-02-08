#!/bin/bash
source ./reflector_status.sh
echo killing reflector processes...
source ./supakilla `pwd`/reflector_watcher.sh
source ./supakilla `pwd`/dimdimReflector
echo killed
source ./reflector_status.sh
rm -rf *.pid

