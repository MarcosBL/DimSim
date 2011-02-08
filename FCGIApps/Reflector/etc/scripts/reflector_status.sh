#!/bin/bash
MAXPCOUNT=10
ZERO=0
PCOUNT=`source reflector_check.sh | grep -c alive`
echo  "Active FCGI Processes : $PCOUNT (of $MAXPCOUNT)"



