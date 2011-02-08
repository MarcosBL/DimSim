#!/bin/bash
cd /usr/local/dimdim
./StopDimdim.sh

# starting confernce server
echo "Starting Conference server"
cd /usr/local/dimdim-4.5/ConferenceServer/apache-tomcat-5.5.17/bin
./dimdim start

echo "Conference server started"
echo ""



# starting presentation manager
cd /usr/local/dimdim/Mediaserver/mods
./start_pmgr.sh

echo "Starting presentation manager"
echo ""


# starting reflector
cd /usr/local/dimdim/DTP3/Reflector
./reflector_start.sh

echo "Reflector started"
echo ""



# starting nginx
echo "starting nginx"
/usr/local/dimdim/nginx/sbin/nginx
echo ""

# starting Red5
cd /usr/local/dimdim/red5
./red5.sh > /dev/null 2>&1 &
echo "red5 server started"
echo ""
# starting Cobmanager
cd /usr/local/dimdim/CobrowsingManager/cob
./startCobServer.sh
echo "CobManager Started"
