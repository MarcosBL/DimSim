#!/bin/bash
export LD_LIBRARY_PATH=`pwd`/libs:$LID_LIBRARY_PATH
./spawn-fcgi -f `pwd`/dimdimReflector -s /var/run/dimdim-screenshare-fcgi-$1.socket -P $1.pid 
