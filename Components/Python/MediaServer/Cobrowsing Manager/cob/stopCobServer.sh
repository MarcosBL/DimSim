if [ -f '/var/run/cob.pid' ]
then
kill `cat /var/run/cob.pid`
fi