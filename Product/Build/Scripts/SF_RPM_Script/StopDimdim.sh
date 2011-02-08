
#!/bin/bash

echo "*************** Stopping any running Dimdim service ******************"

# stopping confernce server
echo "Stopping Conference server"
echo "Please wait.."
cd /usr/local/dimdim-4.5/ConferenceServer/apache-tomcat-5.5.17/bin/
./dimdim stop

echo "Conference server stopped"

echo ""

# stopping presentation manager
cd /usr/local/dimdim/Mediaserver/mods
./stop_pmgr.sh

echo "presentation manager stooped"

echo ""

# stopping reflector
echo "Reflector stopping"
cd /usr/local/dimdim/DTP3/Reflector
./reflector_stop.sh

echo "Reflector stopped"
echo ""

# stopping nginx
killall -9 nginx
echo "nginx stopped"
echo ""


# stopping Red5
port=`netstat -ntlu | grep "1935" | grep -v grep | sort | awk '{print $2}'`
if [ "$port" ]
then
cd /usr/local/dimdim/red5 
./red5-shutdown.sh
echo "Streaming server stopped"
echo ""
else
echo "Streaming server was not running"
fi

#stopping Cobmanager
cd /usr/local/dimdim/CobrowsingManager/cob
./stopCobServer.sh
echo "Cobmanager stopped"
