%define debug_package %{nil}
Summary: Creation of rpm package for Dimdim
Name: dimdim
Version: 4.5
Release: 1 
License: Other
Group: Application/Internet 
Source: dimdim-4.5.tar.gz
URL: http://sourceforge.net/project/showfiles.php?group_id=176809/dimdim-4.5.tar.gz
Buildroot: /usr/src/redhat/myroot
Requires: sed

%description
Dimdim is an open source web conferencing product with features like Application, Desktop and Presentation sharing with A/V streaming and chat. No installation is needed on the Attendee side and all features are available through a web browser.

%prep
%setup

%build

%install
   echo "Dimdim Components Installation"
   mkdir -p $RPM_BUILD_ROOT/usr/local
   cp -r $RPM_BUILD_DIR/dimdim-4.5 $RPM_BUILD_ROOT/usr/local
%clean

%preun
echo "Stopping Dimdim Components..."
#/usr/local/dimdim-4.5/ConferenceServer/apache-tomcat-5.5.17/bin/dimdim stop
stopDimdim
sed -i "s/\(source \/usr\/local\/dimdim-4.5\/Dimdim_env_set.sh\)\(.*\)$/\1/g" /root/.bashrc
sed -i "/source \/usr\/local\/dimdim-4.5\/Dimdim_env_set.sh/s/source \/usr\/local\/dimdim-4.5\/Dimdim_env_set.sh//" /root/.bashrc
sed -i "s/\(startDimdim\)\(.*\)$/\1/g" /etc/rc.local
sed -i "/startDimdim/s/startDimdim//" /etc/rc.local
rm -rf /usr/bin/spawn-fcgi
rm -rf /usr/lib/python2.4/site-packages/unohelper.py
rm -rf /usr/lib/python2.4/site-packages/uno.py
rm -rf /usr/lib/python2.4/site-packages/unohelper.pyc
rm -rf /usr/lib/python2.4/site-packages/uno.pyc
rm -rf /usr/local/dimdim
rm -rf /usr/bin/startDimdim
rm -rf /usr/bin/stopDimdim
rm -rf /usr/bin/confDimdim
rm -rf /usr/bin/soffice.bin
rm -rf /etc/logrotate-dimdim.conf

echo "Uninstalling Dimdim Components..."

%postun
rm -rf /usr/local/dimdim-4.5

%pre
echo "Checking preinstall checks"
OSCHECK=`cat /etc/issue |grep Cent | awk '{print $3}'`
DIRCHECK=`find / -name dimdim | grep /usr/local`
if [ 5.2 == $OSCHECK ]; then
echo `cat /etc/issue |grep Cent`
else
echo "Error, OS check failed"
echo "Exit Status..."
exit 1
fi
if [ "$DIRCHECK" ]; then
echo "Found previous version of dimdim, Please remove any previous version of Dimdim"
echo "Exit Status...."
exit 1
else
echo "Success..."
fi
#if [ "$(ls -A '/usr/local/dimdim')" ]; then
 #    echo "Remove /usr/local/dimdim directory"
	# echo "Exit status ..."
	 #exit 1
#else
#	echo "Success, Procceding Installation"
 #   echo "Checking Environment.."
#fi
echo "Please wait..."
cherry=`find / -name cherrypy | grep site-packages | grep lib/python2.4`
cherryeg=`find / -name CherryPy*egg* | grep site-packages | grep lib/python2.4 | grep 3.1 | grep egg`
flup=`find / -name flup-1.0-py2.4.egg | grep site-packages | grep python`
pycurl=`find / -name pycurl* | grep site-packages`
demjson=`find / -name demjson*py | grep site-packages | grep python`
demjsonegg=`find / -name demjson*egg* | grep site-packages | grep lib/python2.4 | grep 1.3 | grep egg`
OpenOffice=`find / -name soffice.bin | grep openoffice | grep program | grep org3`
STATUS="success"
if [ "$cherry" -o "$cherryeg" ]; then
OSCHECK=`cat /etc/issue |grep Cent | awk '{print $1}'`
else
echo "Error, Cherrypy-3.1 is not installed"
STATUS="error"
fi

if [ "$flup" ]; then
OSCHECK=`cat /etc/issue |grep Cent | awk '{print $1}'`
else
echo "Error, Flup-1.0 is not installed"
STATUS="error"
fi

if [ "$pycurl" ]; then
OSCHECK=`cat /etc/issue |grep Cent | awk '{print $1}'`
else
echo "Error, pycurl is not installed"
#STATUS="error"
fi

if [ "$demjson" -o "$demjsonegg" ]; then
OSCHECK=`cat /etc/issue |grep Cent | awk '{print $1}'`
else
echo "Error, demjson is not installed"
STATUS="error"
fi

if [ "$OpenOffice" ]; then
OSCHECK=`cat /etc/issue |grep Cent | awk '{print $1}'`
else
echo "Error, OpenOffice 3.0 is not installed"
STATUS="error"
fi
if [ "error" == $STATUS ]; then
echo "Exit Status.."
exit 1
else
echo "Checking Ports.."
fi
test=` netstat -ntlu | grep ":80 " | grep LISTEN | grep -v grep | wc -l`

if [ "$test" != "1" ]; then
echo "Port 80 is free"
else
echo "Error: Port 80 is not free. Dimdim needs port 80 to be free. Please stop any other server running on port 80"
echo "Exit Status.."
exit 1
fi

test1=` netstat -ntlu | grep ":1935 " | grep LISTEN | grep -v grep | wc -l`

if [ "$test1" != "1" ]; then
echo "Port 1935 is free"
else
echo "Error: Port 1935 is not free. Dimdim needs port 1935 to be free. Please stop any other server running on port 1935"
echo "Exit Status.."
exit 1
fi

test2=` netstat -ntlu | grep ":40000 " | grep LISTEN | grep -v grep | wc -l`

if [ "$test2" != "1" ]; then
echo "Port 40000 is free"
else
echo "Error: Port 40000 is not free. Dimdim needs port 40000 to be free. Please stop any other server running on port 40000"
echo "Exit Status.."
exit 1
fi

test3=` netstat -ntlu | grep ":40001 " | grep LISTEN | grep -v grep | wc -l`

if [ "$test3" != "1" ]; then
echo "Port 40001 is free"
else
echo "Error: Port 40001 is not free. Dimdim needs port 40001 to be free. Please stop any other server running on port 40001"
echo "Exit Status.."
exit 1
fi
test4=` netstat -ntlu | grep ":40002 " | grep LISTEN | grep -v grep | wc -l`

if [ "$test4" != "1" ]; then
echo "Port 40002 is free"
else
echo "Error: Port 40002 is not free. Dimdim needs port 40001 to be free. Please stop any other server running on port 40001"
echo "Exit Status.."
exit 1
fi
test6=` netstat -ntlu | grep ":40005 " | grep LISTEN | grep -v grep | wc -l`

if [ "$test6" != "1" ]; then
echo "Port 40005 is free"
else
echo "Error: Port 40005 is not free. Dimdim needs port 40005 to be free. Please stop any other server running on port 40005"
echo "Exit Status.."
exit 1
fi

%post
  # setting locale to C 
  echo "Successfully installed Dimdim Components"
  LANG=C
  export LANG

# path of java 

JAVA_HOME_BIN_VAR=` rpm -ql --filesbypkg \`rpm -qa | grep jre\`| grep "/bin/java$" |grep -v "^jre" | cut -f2`
if [ ${#JAVA_HOME_BIN_VAR} != 0 ]; then
for i in $JAVA_HOME_BIN_VAR; do
JAVA_HOME_BIN="$i"
done
else
JAVA_HOME_BIN_VAR=`find / -name java | grep 1.6 | grep "/bin/java$" |grep -v "^jre"|cut -f2`
for i in $JAVA_HOME_BIN_VAR; do
JAVA_HOME_BIN="$i"
done
fi
# getting JAVA_HOME path
JAVA_HOME_VAR=`rpm -ql --filesbypkg \`rpm -qa | grep jre\`| grep "/bin/java$" |grep -v "^jre" | cut -f2 | rev | cut -f3-111 -d'/'|rev` 
if [ ${#JAVA_HOME_VAR} != 0 ]; then
for j in $JAVA_HOME_VAR; do
JAVA_HOME="$j"
done
else
JAVA_HOME_VAR=`find / -name java | grep 1.6 | grep "/bin/java$" |grep -v "^jre"|cut -f2 | rev | cut -f3-111 -d'/'|rev`
for j in $JAVA_HOME_VAR; do
JAVA_HOME="$j"
done
fi

   #OO_PATH=`ls \`which soffice\` -l |  cut -f2 -d'>' | sed 's/ //' | sed 's/soffice\///'`
   #echo "OO Path is " $OO_PATH

   #OO_PATH=`ls \`which soffice\` -l |  cut -f2 -d'>' | sed 's/ //' | sed 's/soffice//'`
   #echo $OO_PATH

# Getting IP address - removing loopback addresses. for a multi IP machine - the first IP Address will be taken
   ipaddr=`ifconfig | grep "inet addr" | grep -v "127.0.0." | cut -f2 -d':' | cut -f1 -d' '|head -1`

   echo "=========================================="
   echo "Post Installation ..."   
   echo "=========================================="

   echo "Configuring the Dimdim Conference Server Settings"

# setting JAVA_HOME in /root/.bashrc
touch /usr/local/dimdim-4.5/Dimdim_env_set.sh
echo '#!/bin/bash' >> /usr/local/dimdim-4.5/Dimdim_env_set.sh
   echo JAVA_HOME=$JAVA_HOME >> /usr/local/dimdim-4.5/Dimdim_env_set.sh
   echo export JAVA_HOME >> /usr/local/dimdim-4.5/Dimdim_env_set.sh
   echo PATH=$JAVA_HOME/bin:'$PATH' >> /usr/local/dimdim-4.5/Dimdim_env_set.sh
   echo export PATH >> /usr/local/dimdim-4.5/Dimdim_env_set.sh
   OOSoffice=`find / -name soffice.bin | grep openoffice | grep program | grep org3`
   UNOPATH=`find / -name program | grep openoffice | grep basis3.0`
   echo LD_LIBRARY_PATH='$LD_LIBRARY_PATH':$UNOPATH:/usr/local/dimdim-4.5/ThirdPartyPackages/swftools/libs:/usr/local/dimdim-4.5/ThirdPartyPackages/swftools/swffonts/fonts:/usr/local/dimdim-4.5/ThirdPartyPackages/swftools/swffonts/swfs >> /usr/local/dimdim-4.5/Dimdim_env_set.sh
   echo PATH=/usr/local/dimdim-4.5/ThirdPartyPackages/swftools:'$PATH'>> /usr/local/dimdim-4.5/Dimdim_env_set.sh
   echo export PATH >> /usr/local/dimdim-4.5/Dimdim_env_set.sh
   echo export LD_LIBRARY_PATH >> /usr/local/dimdim-4.5/Dimdim_env_set.sh
echo source /usr/local/dimdim-4.5/Dimdim_env_set.sh >>  /root/.bashrc 
echo startDimdim >> /etc/rc.local
chmod +x /usr/local/dimdim-4.5/Dimdim_env_set.sh
. /root/.bashrc
   touch /etc/logrotate-dimdim.conf
   echo "#include /etc/logrotate.d" >> /etc/logrotate-dimdim.conf
   echo "\"/usr/local/dimdim/nginx/logs/access.log\"  /usr/local/dimdim/nginx/logs/error.log {" >> /etc/logrotate-dimdim.conf
   echo "daily" >> /etc/logrotate-dimdim.conf
   echo "rotate 15" >> /etc/logrotate-dimdim.conf
   echo "size=25M" >> /etc/logrotate-dimdim.conf
   echo "nomail" >> /etc/logrotate-dimdim.conf
   echo "copytruncate" >> /etc/logrotate-dimdim.conf
   echo "compress" >> /etc/logrotate-dimdim.conf
   echo "lastaction" >> /etc/logrotate-dimdim.conf
   echo "kill -USR1 \`cat /usr/local/dimdim-4.5/nginx/logs/nginx.pid\`" >> /etc/logrotate-dimdim.conf
   echo "endscript" >> /etc/logrotate-dimdim.conf
   echo "}" >> /etc/logrotate-dimdim.conf

   chmod +x /etc/logrotate-dimdim.conf
. /root/.bashrc 
# Creating soft link for start script
  ln -s /usr/local/dimdim-4.5/StartDimdim.sh /usr/bin/startDimdim
  chmod +x /usr/bin/startDimdim

# Creating soft link for stop script
  ln -s /usr/local/dimdim-4.5/StopDimdim.sh /usr/bin/stopDimdim
  chmod +x /usr/bin/stopDimdim

  #echo "Default Open office installation path is /opt/openoffice.org2.4/program/"
  ln -s $OOSoffice /usr/bin/soffice.bin
  chmod +x /usr/bin/soffice.bin

# Creating soft link for version changes of the rpm folder
  ln -s /usr/local/dimdim-4.5/ /usr/local/dimdim
  

  OOPATH=`echo $OO_PATH | sed 's|\/|\\\/|g'`
  
  sed -i "s|DIMDIM_JAVA_HOME/bin/java|$JAVA_HOME_BIN|" /usr/local/dimdim-4.5/ConferenceServer/apache-tomcat-5.5.17/conf/wrapper.conf 
sed -i "s|DIMDIM_PORT_NUMBER|40000|" /usr/local/dimdim-4.5/ConferenceServer/apache-tomcat-5.5.17/conf/server.xml
  echo "Successfully configured the java path in wrapper.conf"

   sed -i "s/\(dimdim.serverAddress=\)\(.*\)$/\1/g" /usr/local/dimdim-4.5/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/dimdim.properties
   sed -i "/dimdim.serverAddress=/s/dimdim.serverAddress=/dimdim.serverAddress=$ipaddr/" /usr/local/dimdim-4.5/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/dimdim.properties
   
   sed -i "s/\(dimdim.serverPortNumber=\)\(.*\)$/\1/g" /usr/local/dimdim-4.5/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/dimdim.properties
   sed -i "/dimdim.serverPortNumber=/s/dimdim.serverPortNumber=/dimdim.serverPortNumber=80/g" /usr/local/dimdim-4.5/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/dimdim.properties

   sed -i "s/\(dimdim.dmsServerAddress=\)\(.*\)$/\1/g" /usr/local/dimdim-4.5/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/dimdim.properties
   sed -i "/dimdim.dmsServerAddress=/s/dimdim.dmsServerAddress=/dimdim.dmsServerAddress=$ipaddr:80\/pmgr/g" /usr/local/dimdim-4.5/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/dimdim.properties
  
  sed -i "s/\(dimdim.dmsServerInternalAddress=\)\(.*\)$/\1/g" /usr/local/dimdim-4.5/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/dimdim.properties
   sed -i "/dimdim.dmsServerInternalAddress=/s/dimdim.dmsServerInternalAddress=/dimdim.dmsServerInternalAddress=localhost:80\/pmgr/g" /usr/local/dimdim-4.5/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/dimdim.properties
   
   sed -i "s/\(dimdim.dmsServerMboxInternal=\)\(.*\)$/\1/g" /usr/local/dimdim-4.5/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/dimdim.properties
   sed -i "/dimdim.dmsServerMboxInternal=/s/dimdim.dmsServerMboxInternal=/dimdim.dmsServerMboxInternal=localhost/g" /usr/local/dimdim-4.5/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/dimdim.properties
   
   sed -i "s/\(dimdim.dmsServerMboxExternal=\)\(.*\)$/\1/g" /usr/local/dimdim-4.5/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/dimdim.properties
   sed -i "/dimdim.dmsServerMboxExternal=/s/dimdim.dmsServerMboxExternal=/dimdim.dmsServerMboxExternal=$ipaddr/" /usr/local/dimdim-4.5/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/dimdim.properties

sed -i "s/\(dimdim.defaultCollabURL=\)\(.*\)$/\1/g" /usr/local/dimdim-4.5/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/dimdim.properties
   sed -i "/dimdim.defaultCollabURL=/s/dimdim.defaultCollabURL=/dimdim.defaultCollabURL=/" /usr/local/dimdim-4.5/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/dimdim.properties   

   sed -i "s/\(streaming_server.1.rtmp_url=\)\(.*\)$/\1/g" /usr/local/dimdim-4.5/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties
  sed -i "/streaming_server.1.rtmp_url=/s/streaming_server.1.rtmp_url=/streaming_server.1.rtmp_url=rtmp:\/\/$ipaddr:1935\//g" /usr/local/dimdim-4.5/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties

  sed -i "s/\(streaming_server.1.rtmpt_url=\)\(.*\)$/\1/g" /usr/local/dimdim-4.5/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties
  sed -i "/streaming_server.1.rtmpt_url=/s/streaming_server.1.rtmpt_url=/streaming_server.1.rtmpt_url=rtmpt:\/\/$ipaddr:80\//g" /usr/local/dimdim-4.5/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties
############################################
   sed -i "s/\(streaming_server.2.rtmp_url=\)\(.*\)$/\1/g" /usr/local/dimdim-4.5/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties
  sed -i "/streaming_server.2.rtmp_url=/s/streaming_server.2.rtmp_url=/streaming_server.2.rtmp_url=http:\/\/$ipaddr\/screenshare0\//g" /usr/local/dimdim-4.5/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties 

  sed -i "s/\(streaming_server.2.rtmpt_url=\)\(.*\)$/\1/g" /usr/local/dimdim-4.5/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties
  sed -i "/streaming_server.2.rtmpt_url=/s/streaming_server.2.rtmpt_url=/streaming_server.2.rtmpt_url=http:\/\/$ipaddr\/screenshare0\//g" /usr/local/dimdim-4.5/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties
############################################
sed -i "s/\(streaming_server.3.rtmp_url=\)\(.*\)$/\1/g" /usr/local/dimdim-4.5/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties
  sed -i "/streaming_server.3.rtmp_url=/s/streaming_server.3.rtmp_url=/streaming_server.3.rtmp_url=http:\/\/$ipaddr\/screenshare1\//g" /usr/local/dimdim-4.5/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties 

  sed -i "s/\(streaming_server.3.rtmpt_url=\)\(.*\)$/\1/g" /usr/local/dimdim-4.5/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties
  sed -i "/streaming_server.3.rtmpt_url=/s/streaming_server.3.rtmpt_url=/streaming_server.3.rtmpt_url=http:\/\/$ipaddr\/screenshare1\//g" /usr/local/dimdim-4.5/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties
###############################################
sed -i "s/\(streaming_server.4.rtmp_url=\)\(.*\)$/\1/g" /usr/local/dimdim-4.5/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties
  sed -i "/streaming_server.4.rtmp_url=/s/streaming_server.4.rtmp_url=/streaming_server.4.rtmp_url=http:\/\/$ipaddr\/screenshare2\//g" /usr/local/dimdim-4.5/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties 

  sed -i "s/\(streaming_server.4.rtmpt_url=\)\(.*\)$/\1/g" /usr/local/dimdim-4.5/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties
  sed -i "/streaming_server.4.rtmpt_url=/s/streaming_server.4.rtmpt_url=/streaming_server.4.rtmpt_url=http:\/\/$ipaddr\/screenshare2\//g" /usr/local/dimdim-4.5/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties
################################################
sed -i "s/\(streaming_server.5.rtmp_url=\)\(.*\)$/\1/g" /usr/local/dimdim-4.5/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties
  sed -i "/streaming_server.5.rtmp_url=/s/streaming_server.5.rtmp_url=/streaming_server.5.rtmp_url=http:\/\/$ipaddr\/screenshare3\//g" /usr/local/dimdim-4.5/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties 

  sed -i "s/\(streaming_server.5.rtmpt_url=\)\(.*\)$/\1/g" /usr/local/dimdim-4.5/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties
  sed -i "/streaming_server.5.rtmpt_url=/s/streaming_server.5.rtmpt_url=/streaming_server.5.rtmpt_url=http:\/\/$ipaddr\/screenshare3\//g" /usr/local/dimdim-4.5/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties
####################################################
sed -i "s/\(streaming_server.6.rtmp_url=\)\(.*\)$/\1/g" /usr/local/dimdim-4.5/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties
  sed -i "/streaming_server.6.rtmp_url=/s/streaming_server.6.rtmp_url=/streaming_server.6.rtmp_url=http:\/\/$ipaddr\/screenshare4\//g" /usr/local/dimdim-4.5/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties 

  sed -i "s/\(streaming_server.6.rtmpt_url=\)\(.*\)$/\1/g" /usr/local/dimdim-4.5/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties
  sed -i "/streaming_server.6.rtmpt_url=/s/streaming_server.6.rtmpt_url=/streaming_server.6.rtmpt_url=http:\/\/$ipaddr\/screenshare4\//g" /usr/local/dimdim-4.5/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties  
####################################################  
sed -i "s/\(streaming_server.7.rtmp_url=\)\(.*\)$/\1/g" /usr/local/dimdim-4.5/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties
  sed -i "/streaming_server.7.rtmp_url=/s/streaming_server.7.rtmp_url=/streaming_server.7.rtmp_url=http:\/\/$ipaddr\/screenshare5\//g" /usr/local/dimdim-4.5/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties 

  sed -i "s/\(streaming_server.7.rtmpt_url=\)\(.*\)$/\1/g" /usr/local/dimdim-4.5/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties
  sed -i "/streaming_server.7.rtmpt_url=/s/streaming_server.7.rtmpt_url=/streaming_server.7.rtmpt_url=http:\/\/$ipaddr\/screenshare5\//g" /usr/local/dimdim-4.5/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties
###############################################################
sed -i "s/\(streaming_server.8.rtmp_url=\)\(.*\)$/\1/g" /usr/local/dimdim-4.5/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties
  sed -i "/streaming_server.8.rtmp_url=/s/streaming_server.8.rtmp_url=/streaming_server.8.rtmp_url=http:\/\/$ipaddr\/screenshare6\//g" /usr/local/dimdim-4.5/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties 

  sed -i "s/\(streaming_server.8.rtmpt_url=\)\(.*\)$/\1/g" /usr/local/dimdim-4.5/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties
  sed -i "/streaming_server.8.rtmpt_url=/s/streaming_server.8.rtmpt_url=/streaming_server.8.rtmpt_url=http:\/\/$ipaddr\/screenshare6\//g" /usr/local/dimdim-4.5/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties
###############################################################
sed -i "s/\(streaming_server.9.rtmp_url=\)\(.*\)$/\1/g" /usr/local/dimdim-4.5/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties
  sed -i "/streaming_server.9.rtmp_url=/s/streaming_server.9.rtmp_url=/streaming_server.9.rtmp_url=http:\/\/$ipaddr\/screenshare7\//g" /usr/local/dimdim-4.5/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties 

  sed -i "s/\(streaming_server.9.rtmpt_url=\)\(.*\)$/\1/g" /usr/local/dimdim-4.5/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties
  sed -i "/streaming_server.9.rtmpt_url=/s/streaming_server.9.rtmpt_url=/streaming_server.9.rtmpt_url=http:\/\/$ipaddr\/screenshare7\//g" /usr/local/dimdim-4.5/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties
###############################################################
sed -i "s/\(streaming_server.10.rtmp_url=\)\(.*\)$/\1/g" /usr/local/dimdim-4.5/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties
  sed -i "/streaming_server.10.rtmp_url=/s/streaming_server.10.rtmp_url=/streaming_server.10.rtmp_url=http:\/\/$ipaddr\/screenshare8\//g" /usr/local/dimdim-4.5/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties 

  sed -i "s/\(streaming_server.10.rtmpt_url=\)\(.*\)$/\1/g" /usr/local/dimdim-4.5/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties
  sed -i "/streaming_server.10.rtmpt_url=/s/streaming_server.10.rtmpt_url=/streaming_server.10.rtmpt_url=http:\/\/$ipaddr\/screenshare8\//g" /usr/local/dimdim-4.5/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties
###############################################################
sed -i "s/\(streaming_server.11.rtmp_url=\)\(.*\)$/\1/g" /usr/local/dimdim-4.5/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties
  sed -i "/streaming_server.11.rtmp_url=/s/streaming_server.11.rtmp_url=/streaming_server.11.rtmp_url=http:\/\/$ipaddr\/screenshare9\//g" /usr/local/dimdim-4.5/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties 

  sed -i "s/\(streaming_server.11.rtmpt_url=\)\(.*\)$/\1/g" /usr/local/dimdim-4.5/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties
  sed -i "/streaming_server.11.rtmpt_url=/s/streaming_server.11.rtmpt_url=/streaming_server.11.rtmpt_url=http:\/\/$ipaddr\/screenshare9\//g" /usr/local/dimdim-4.5/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties
###############################################################

sed -i "s/\(RecordingServerAddress=\)\(.*\)$/\1/g" /usr/local/dimdim-4.5/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/recording.properties
  sed -i "/RecordingServerAddress=/s/RecordingServerAddress=/RecordingServerAddress=http:\/\/$ipaddr\/rad\//g" /usr/local/dimdim-4.5/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/recording.properties

#######################################################

  
 sed -i "s/\(        server_name  \)\(.*\)$/\1/g" /usr/local/dimdim-4.5/nginx/conf/nginx.conf
 
 sed -i "/        server_name/s/        server_name/        server_name $ipaddr;/g" /usr/local/dimdim-4.5/nginx/conf/nginx.conf
 
#################################################
cd /usr/local/dimdim-4.5/
tar xzf CobrowsingManager.tar.gz > temp.out 2>&1 &
wait
rm -rf temp.out
cd /usr/local/dimdim-4.5/
rm -rf CobrowsingManager.tar.gz
sed -i "s/\(hostString=\)\(.*\)$/\1/" /usr/local/dimdim-4.5/CobrowsingManager/cob/toolkit/osconfig.py
sed -i "/hostString=/s/hostString=/hostString=\'$ipaddr\'/g" /usr/local/dimdim-4.5/CobrowsingManager/cob/toolkit/osconfig.py
####################################################
sed -i "s/\(dimdim.dmsCobAddress=\)\(.*\)$/\1/g" /usr/local/dimdim-4.5/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/dimdim.properties
sed -i "/dimdim.dmsCobAddress=/s/dimdim.dmsCobAddress=/dimdim.dmsCobAddress=$ipaddr/g" /usr/local/dimdim-4.5/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/dimdim.properties
##################################################
  
cd /usr/local/dimdim-4.5/ThirdPartyPackages/
tar xzf swftools.tar.gz > temp.out 2>&1 &
wait
rm -rf temp.out
rm -rf swftools.tar.gz
cd /usr/local/dimdim-4.5/
rm -rf CobrowsingManager.tgz
  chmod +x /usr/local/dimdim-4.5/ConferenceServer/apache-tomcat-5.5.17/bin/*
  chmod +x /usr/local/dimdim-4.5/CobrowsingManager/cob/*
  chmod +x /usr/local/dimdim-4.5/Mediaserver/mods/*
  chmod +x /usr/local/dimdim-4.5/Mediaserver/www/pages/*
  chmod +x /usr/local/dimdim-4.5/DTP3/Reflector/*
  chmod +x /usr/local/dimdim-4.5/nginx/sbin/*
  chmod +x /usr/local/dimdim-4.5/*
  chmod +x /usr/local/dimdim-4.5/ThirdPartyPackages/swftools/libs/*
  chmod +x /usr/local/dimdim-4.5/ThirdPartyPackages/swftools/swffonts/fonts/*
  chmod +x /usr/local/dimdim-4.5/ThirdPartyPackages/swftools/swffonts/swfs/*
  chmod +x /usr/local/dimdim-4.5/ThirdPartyPackages/swftools/*	
  chmod +x /usr/local/dimdim-4.5/DTP3/Reflector/libs/*
  chmod +x /usr/local/dimdim-4.5/DTP3/Reflector/etc/scripts/*
  chmod +x /usr/local/dimdim-4.5/DTP3/Reflector/etc/cfg/*
  chmod +x /usr/local/dimdim-4.5/DTP3/Reflector/etc/htdocs/*
chmod +x /usr/local/dimdim-4.5/red5/*
cp /usr/local/dimdim-4.5/DTP3/Reflector/etc/tools/spawn-fcgi-32bit /usr/local/dimdim-4.5/DTP3/Reflector/spawn-fcgi
  chmod +x /usr/local/dimdim-4.5/DTP3/Reflector/*
  mv /usr/local/dimdim-4.5/uno*py /usr/lib/python2.4/site-packages/

ln -s /usr/local/dimdim-4.5/DTP3/Reflector/spawn-fcgi /usr/bin/spawn-fcgi
  #chmod +x /usr/local/dimdim-4.5/DTP3/Reflector/etc/htdocs/screensharestatic/*
   #sed -i "s|_ServerAddress|$ipaddr|" /usr/local/dimdim-4.5/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/dimdim.properties    echo "Successfully configured IP address of this server in dimdim.properties."

  echo "=========================================="
  echo  "1. To start or stop Dimdim, do startDimdim and stopDimdim"
  echo  "2. It is necessary that the dimdim conference server be stopped and re-started before and after changing the ipaddress."
  echo "=========================================="

  /usr/local/dimdim-4.5/Mediaserver/mods/StartOO.sh /dev/null 2>&1&

  echo "=========================================="
  echo " To Start a meeting, use the URL: http://$ipaddr/dimdim/"  
  echo "=========================================="

#dos2unix /usr/local/dimdim-4.5/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/*.* > /dev/null 2>&1 &
#dos2unix /usr/local/dimdim-4.5/CobrowsingManager/cob/toolkit/osconfig.py > /dev/null 2>&1 &
cd /usr/local/dimdim-4.5
./Config-ipaddress.pl $ipaddr 80 > /dev/null 2>&1 &

%files
/usr/local/dimdim-4.5/*



