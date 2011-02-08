#!/usr/bin/perl
$ipaddress = $ARGV[0];
$nginxPort = $ARGV[1];
$internalIP = $ARGV[2];
$Red5InstallRoot = $ARGV[4];
$port = "80";
$Red5 = "/usr/local/dimdim/red5/webapps";
$mailbox = "/usr/local/dimdim/mailbox/dimdimMailBoxRoot";
$dimdimwebapp = "dimdim";
if ($#ARGV+1<2)
{
print "\n Try to use";
print "\n Config-ipaddress.pl externalIP portNumner internalIP ";
print "\n Config-ipaddress.pl 216.X.X.X 80 192.168.X.X ";
print "\n Last parameter is optional";
print "\n Terminating....\n";
exit(0);
}
else
{
print "\n Please wait.........";

}


sub trim()
{
	#my $para = shift;
	print LOG "\tTrimming the first and last empty spaces\n";
        my $string = shift;
        $string =~ s/^\s+//;
        $string =~ s/\s+$_[0]//;
        return $string;
}

if($ARGV[1])
{
	configuredimdimserverPortNumber($nginxPort);
	
}
else
{
	configuredimdimserverPortNumber($port);
	
}
if($ARGV[4])
{
	configureRed5Installation($Red5InstallRoot);
}
else
{
	configureRed5Installation($Red5);
}
if($ARGV[2])
{
 print  "@ARGV[2]";
	configuredmsserverInternal($internalIP);
	configuredmsServerMboxInternal($internalIP);
	configuredmsCobAddressInternalAddress($internalIP);	
}
else
{
	 configuredmsserverInternal($ipaddress);
	 configuredmsServerMboxInternal($ipaddress);
	 configuredmsCobAddressInternalAddress($ipaddress);	
	 
}
#print LOG $ipaddress; 



configuredimdimserver($ipaddress);
configuredmsserver($ipaddress);
configuredmsServerMboxExternal($ipaddress);


configurestreamingserverRTMP($ipaddress);
configurestreamingserverRTMPT($ipaddress);
configurereflector1($ipaddress);
configurereflector01($ipaddress);
configurereflector2($ipaddress);
configurereflector02($ipaddress);
configurereflector3($ipaddress);
configurereflector03($ipaddress);
configurereflector4($ipaddress);
configurereflector04($ipaddress);
configurereflector5($ipaddress);
configurereflector05($ipaddress);
configurereflector6($ipaddress);
configurereflector06($ipaddress);
configurereflector7($ipaddress);
configurereflector07($ipaddress);
configurereflector8($ipaddress);
configurereflector08($ipaddress);
configurereflector9($ipaddress);
configurereflector09($ipaddress);
configurereflector10($ipaddress);
configurereflector010($ipaddress);

configureNginxPort($nginxPort);
configureNginxServer($ipaddress);
configureCobserver($ipaddress);
configureCobosconfig($ipaddress);
configureScreenShareini();
configuredimdiminstallationid();
configuredmsCobAddress($ipaddress);
configureMediaServer($nginxPort);
sub configuredmsCobAddress()
{
      my $string = "dimdim.dmsCobAddress=";
      my $temp = "/usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/dimdim.properties";
      my $output="/usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/dimdim.properties.temp";
      $temp1 = "dimdim.dmsCobAddress=".$_[0].":".$nginxPort;
        open(IN,"<$temp") or die $!;
        open (OUT,">$output") or die $!;
        while (<IN>)
        {
            if ($_ !~/$string/)
            {
               print OUT $_;
            }
            else
            {
                print OUT $temp1."\n";
            }

        }
        close IN;
        close OUT;
	`rm -rf /usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/dimdim.properties`;
        `mv /usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/dimdim.properties.temp /usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/dimdim.properties`;
         `dos2unix /usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/dimdim.properties`;
        return 'true';
	
	
}


sub configuredmsCobAddressInternalAddress()
{
      my $string = "dimdim.dmsCobAddressInternalAddress=";
      my $temp = "/usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/dimdim.properties";
      my $output="/usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/dimdim.properties.temp";
      $temp1 = "dimdim.dmsCobAddressInternalAddress=".$_[0].":".$nginxPort;
      open(IN,"<$temp") or die $!;
        open (OUT,">$output") or die $!;
        while (<IN>)
        {
            if ($_ !~/$string/)
            {
               print OUT $_;
            }
            else
            {
                print OUT $temp1."\n";
            }

        }
        close IN;
        close OUT;
	`rm -rf /usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/dimdim.properties`;
        `mv /usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/dimdim.properties.temp /usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/dimdim.properties`;
         `dos2unix /usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/dimdim.properties`;
        return 'true';
	
	
}


sub configureScreenShareini()
{
	my $string = "mailboxRoot=";
        my $temp = "/usr/local/dimdim/DTP3/Reflector/etc/cfg/dimdimScreenShare.ini";
        my $output = "/usr/local/dimdim/DTP3/Reflector/etc/cfg/dimdimScreenShare.ini.temp";
        $temp1 = "mailboxRoot="."/usr/local/dimdim/mailbox/dimdimMailBoxRoot";
        open(IN,"<$temp") or die $!;
        open (OUT,">$output") or die $!;
        while (<IN>)
        {
            if ($_ !~/$string/)
            {
               print OUT $_;
            }
            else
            {
                print OUT $temp1."\n";
            }

        }
        close IN;
        close OUT;
        `rm -rf /usr/local/dimdim/DTP3/Reflector/etc/cfg/dimdimScreenShare.ini`;
        `mv /usr/local/dimdim/DTP3/Reflector/etc/cfg/dimdimScreenShare.ini.temp /usr/local/dimdim/DTP3/Reflector/etc/cfg/dimdimScreenShare.ini`;
         `dos2unix /usr/local/dimdim/DTP3/Reflector/etc/cfg/dimdimScreenShare.ini`;
        return 'true';
}
sub configuredimdiminstallationid()
{
	my $string = "dimdim.installationId=";
        my $temp = "/usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/dimdim.properties";
        my $output = "/usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/dimdim.properties.temp";
        $temp1 = "dimdim.installationId="."dimdim-v4.5";
        open(IN,"<$temp") or die $!;
        open (OUT,">$output") or die $!;
        while (<IN>)
        {
            if ($_ !~/$string/)
            {
               print OUT $_;
            }
            else
            {
                print OUT $temp1."\n";
            }

        }
        close IN;
        close OUT;
        `rm -rf /usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/dimdim.properties`;
        `mv /usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/dimdim.properties.temp /usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/dimdim.properties`;
         `dos2unix /usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/dimdim.properties`;
        return 'true';
}


sub configureCobserver()
{
	my $string = "dimdim.dmsCobAddress=";
        my $temp = "/usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/dimdim.properties";
        my $output = "/usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/dimdim.properties.temp";
        $temp1 = "dimdim.dmsCobAddress=".$_[0];
        open(IN,"<$temp") or die $!;
        open (OUT,">$output") or die $!;
        while (<IN>)
        {
            if ($_ !~/$string/)
            {
               print OUT $_;
            }
            else
            {
                print OUT $temp1."\n";
            }

        }
        close IN;
        close OUT;
        `rm -rf /usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/dimdim.properties`;
        `mv /usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/dimdim.properties.temp /usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/dimdim.properties`;
         `dos2unix /usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/dimdim.properties`;
        return 'true';
}


sub configureCobosconfig()
{
	my $string = "hostString=";
        my $temp = "/usr/local/dimdim/CobrowsingManager/cob/toolkit/osconfig.py";
        my $output = "/usr/local/dimdim/CobrowsingManager/cob/toolkit/osconfig.py.temp";
        $temp1 = "hostString="."\'".$_[0].":".$nginxPort."\'";
        open(IN,"<$temp") or die $!;
        open (OUT,">$output") or die $!;
        while (<IN>)
        {
            if ($_ !~/$string/)
            {
               print OUT $_;
            }
            else
            {
                print OUT $temp1."\n";
            }

        }
        close IN;
        close OUT;
        `rm -rf /usr/local/dimdim/CobrowsingManager/cob/toolkit/osconfig.py`;
        `mv /usr/local/dimdim/CobrowsingManager/cob/toolkit/osconfig.py.temp /usr/local/dimdim/CobrowsingManager/cob/toolkit/osconfig.py`;
         `dos2unix /usr/local/dimdim/CobrowsingManager/cob/toolkit/osconfig.py`;
        return 'true';
}


sub configureNginxServer()
{
		my $string = "server_name";
        my $temp = "/usr/local/dimdim/nginx/conf/nginx.conf";
        my $output = "/usr/local/dimdim/nginx/conf/nginx.conf.temp";
        $temp1 = "server_name  ".$_[0].";";
        open(IN,"<$temp") or die $!;
        open (OUT,">$output") or die $!;
        while (<IN>)
        {

            if ($_ !~/$string/)
            {
               print OUT $_;
            }
            else
            {
                print OUT $temp1."\n";
            }


        }
        close IN;
        close OUT;
        system(`rm -rf /usr/local/dimdim/nginx/conf/nginx.conf`);
        system(`mv /usr/local/dimdim/nginx/conf/nginx.conf.temp /usr/local/dimdim/nginx/conf/nginx.conf`);
        system(`dos2unix /usr/local/dimdim/nginx/conf/nginx.conf`);
        return true;
}
sub configureNginxPort()
{
		my $string = "        listen       ";
        my $temp = "/usr/local/dimdim/nginx/conf/nginx.conf";
        my $output = "/usr/local/dimdim/nginx/conf/nginx.conf.temp";
        $temp1 = "        listen       ".$_[0].";";
        open(IN,"<$temp") or die $!;
        open (OUT,">$output") or die $!;
        while (<IN>)
        {

            if ($_ !~/$string/)
            {
               print OUT $_;
            }
            else
            {
                print OUT $temp1."\n";
            }


        }
        close IN;
        close OUT;
        system(`rm -rf /usr/local/dimdim/nginx/conf/nginx.conf`);
        system(`mv /usr/local/dimdim/nginx/conf/nginx.conf.temp /usr/local/dimdim/nginx/conf/nginx.conf`);
        system(`dos2unix /usr/local/dimdim/nginx/conf/nginx.conf`);
        return true;
}


sub configureRed5Installation()
{
		#my $para = shift;
		my $string = "streaming_server.1.av_application_streams_directory=";
        my $temp = "/usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties";
        my $output = "/usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties.temp";
        $temp1 = "streaming_server.1.av_application_streams_directory=".$_[0]."/dimdimPublisher/streams";
        open(IN,"<$temp") or die $!;
        open (OUT,">$output") or die $!;
        while (<IN>)
        {
            if ($_ !~/$string/)
            {
               print OUT $_;
            }
            else
            {
                print OUT $temp1."\n";
            }

        }
        close IN;
        close OUT;
        `rm -rf /usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties`;
        `mv /usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties.temp /usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties`;
	`dos2unix /usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties`;

        return 'true';
}
sub configuredimdimserverPortNumber()
{
		#my $para = shift;
		my $string = "dimdim.serverPortNumber=";
        my $temp = "/usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/dimdim.properties";
        my $output = "/usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/dimdim.properties.temp";
        $temp1 = "dimdim.serverPortNumber=".$_[0];
        open(IN,"<$temp") or die $!;
        open (OUT,">$output") or die $!;
        while (<IN>)
        {
            if ($_ !~/$string/)
            {
               print OUT $_;
            }
            else
            {
                print OUT $temp1."\n";
            }

        }
        close IN;
        close OUT;
        `rm -rf /usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/dimdim.properties`;
        `mv /usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/dimdim.properties.temp /usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/dimdim.properties`;
         `dos2unix /usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/dimdim.properties`;
        return 'true';
}


sub configuredmsServerMboxExternal()
{
        my $string = "dimdim.dmsServerMboxExternal=";
        my $temp = "/usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/dimdim.properties";
        my $output = "/usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/dimdim.properties.temp";
        $temp1 = "dimdim.dmsServerMboxExternal=".$_[0];
        open(IN,"<$temp") or die $!;
        open (OUT,">$output") or die $!;
        while (<IN>)
        {
            if ($_ !~/$string/)
            {
               print OUT $_;
            }
            else
            {
                print OUT $temp1."\n";
            }

        }
        close IN;
        close OUT;
        `rm -rf /usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/dimdim.properties`;
        `mv /usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/dimdim.properties.temp /usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/dimdim.properties`;
         `dos2unix /usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/dimdim.properties`;
        return 'true';


}
sub configuredmsServerMboxInternal()
{
        my $string = "dimdim.dmsServerMboxInternal=";
        my $temp = "/usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/dimdim.properties";
        my $output = "/usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/dimdim.properties.temp";
        $temp1 = "dimdim.dmsServerMboxInternal=".$_[0];
        open(IN,"<$temp") or die $!;
        open (OUT,">$output") or die $!;
        while (<IN>)
        {
            if ($_ !~/$string/)
            {
               print OUT $_;
            }
            else
            {
                print OUT $temp1."\n";
            }

        }
        close IN;
        close OUT;
        `rm -rf /usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/dimdim.properties`;
        `mv /usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/dimdim.properties.temp /usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/dimdim.properties`;
         `dos2unix /usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/dimdim.properties`;
        return 'true';


}

sub configuredimdimserver()
{
        my $string = "dimdim.serverAddress=";
        my $temp = "/usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/dimdim.properties";
        my $output = "/usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/dimdim.properties.temp";
        $temp1 = "dimdim.serverAddress=".$_[0];
        open(IN,"<$temp") or die $!;
        open (OUT,">$output") or die $!;
        while (<IN>)
        {
            if ($_ !~/$string/)
            {
               print OUT $_;
            }
            else
            {
                print OUT $temp1."\n";
            }

        }
        close IN;
        close OUT;
        `rm -rf /usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/dimdim.properties`;
        `mv /usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/dimdim.properties.temp /usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/dimdim.properties`;
	 `dos2unix /usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/dimdim.properties`;
        return 'true';


}
sub configuredmsserver()
{
        my $string = "dimdim.dmsServerAddress=";
        my $temp = "/usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/dimdim.properties";
        my $output = "/usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/dimdim.properties.temp";
        $temp1 = "dimdim.dmsServerAddress=".$_[0].":".$nginxPort."/pmgr";
        open(IN,"<$temp") or die $!;
        open (OUT,">$output") or die $!;
        while (<IN>)
        {
            if ($_ !~/$string/)
            {
               print OUT $_;
            }
            else
            {
                print OUT $temp1."\n";
            }

        }
        close IN;
        close OUT;
        `rm -rf /usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/dimdim.properties`;
        `mv /usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/dimdim.properties.temp /usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/dimdim.properties`;
	`dos2unix /usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/dimdim.properties`;
        return 'true';
}
sub configuredmsserverInternal()
{
        my $string = "dimdim.dmsServerInternalAddress=";
        my $temp = "/usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/dimdim.properties";
        my $output = "/usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/dimdim.properties.temp";
        $temp1 = "dimdim.dmsServerInternalAddress=".$_[0].":".$nginxPort."/pmgr";
#        $temp1 = "dimdim.dmsServerInternalAddress=localhost:80";
        open(IN,"<$temp") or die $!;
        open (OUT,">$output") or die $!;
        while (<IN>)
        {
            if ($_ !~/$string/)
            {
               print OUT $_;
            }
            else
            {
                print OUT $temp1."\n";
            }

        }
        close IN;
        close OUT;
        `rm -rf /usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/dimdim.properties`;
        `mv /usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/dimdim.properties.temp /usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/dimdim.properties`;
	`dos2unix /usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/dimdim.properties`;
        return 'true';


}
sub configureMbox()
{	
	my $string = "dimdim.dmsServerMbox=";
        my $temp = "/usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/dimdim.properties";
        my $output = "/usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/dimdim.properties.temp";
        $temp1 = "dimdim.dmsServerMbox=".$_[0];
#        $temp1 = "dimdim.dmsServerInternalAddress=localhost:80";
        open(IN,"<$temp") or die $!;
        open (OUT,">$output") or die $!;
        while (<IN>)
        {
            if ($_ !~/$string/)
            {
               print OUT $_;
            }
            else
            {
                print OUT $temp1."\n";
            }

        }
        close IN;
        close OUT;
        `rm -rf /usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/dimdim.properties`;
        `mv /usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/dimdim.properties.temp /usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/dimdim.properties`;
	`dos2unix /usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/dimdim.properties`;
        return 'true';
}
sub configurestreamingserverRTMP()
{
        my $string = "streaming_server.1.rtmp_url=rtmp://";
        my $temp = "/usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties";
        my $output = "/usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties.temp";
        $temp1 = "streaming_server.1.rtmp_url=rtmp://".$_[0].":1935/";
        open(IN,"<$temp") or die $!;
        open (OUT,">$output") or die $!;
        while (<IN>)
        {
            if ($_ !~/$string/)
            {
               print OUT $_;
            }
            else
            {
                print OUT $temp1."\n";
            }

        }
        close IN;
        close OUT;
        `rm -rf /usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties`;
        `mv /usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties.temp /usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties`;
	`dos2unix /usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties`;

        return 'true';
}

sub configurestreamingserverRTMPT()
{
        my $string = "streaming_server.1.rtmpt_url=rtmpt://";
        my $temp = "/usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties";
        my $output = "/usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties.temp";
        $temp1 = "streaming_server.1.rtmpt_url=rtmpt://".$_[0].":".$nginxPort."/";
        open(IN,"<$temp") or die $!;
        open (OUT,">$output") or die $!;
        while (<IN>)
        {
            if ($_ !~/$string/)
            {
               print OUT $_;
            }
            else
            {
                print OUT $temp1."\n";
            }

        }
        close IN;
        close OUT;
        `rm -rf /usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties`;
        `mv /usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties.temp /usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties`;
	`dos2unix /usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties`;
        return 'true';
}
sub configurereflector1()
{

	my $string = "streaming_server.2.rtmp_url=http://";
	my $temp = "/usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties";
	my $output = "/usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties.temp";
	$temp1 = "streaming_server.2.rtmp_url=http://".$_[0].":".$nginxPort."/screenshare0/";
	open(IN,"<$temp") or die $!;
	open (OUT,">$output") or die $!;
	while (<IN>)
	{
	    if ($_ !~/$string/)
	    {	
	       print OUT $_;
	    }
	    else
	    {
	        print OUT $temp1."\n";
	    }

	}
	close IN;
	close OUT;
	`rm -rf /usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties`;
	`mv /usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties.temp /usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties`;
	`dos2unix /usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties`;
	return true;
}
sub configurereflector01()
{
        my $string = "streaming_server.2.rtmpt_url=http://";
        my $temp = "/usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties";
        my $output = "/usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties.temp";
        $temp1 = "streaming_server.2.rtmpt_url=http://".$_[0].":".$nginxPort."/screenshare0/";
        open(IN,"<$temp") or die $!;
        open (OUT,">$output") or die $!;
        while (<IN>)
        {

            if ($_ !~/$string/)
            {
               print OUT $_;
            }
            else
            {
                print OUT $temp1."\n";
            }


        }
        close IN;
        close OUT;
	system(`rm -rf /usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties`);
        system(`mv /usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties.temp /usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties`);
	system(`dos2unix /usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties`);
        return true;
}

sub configurereflector2()
{

        my $string = "streaming_server.3.rtmp_url=http://";
        my $temp = "/usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties";
        my $output = "/usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties.temp";
        $temp1 = "streaming_server.3.rtmp_url=http://".$_[0].":".$nginxPort."/screenshare1/";
        open(IN,"<$temp") or die $!;
        open (OUT,">$output") or die $!;
        while (<IN>)
        {
            if ($_ !~/$string/)
            {
               print OUT $_;
            }
            else
            {
                print OUT $temp1."\n";
            }

        }
        close IN;
        close OUT;
        `rm -rf /usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties`;
        `mv /usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties.temp /usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties`;
        `dos2unix /usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties`;
        return true;
}
sub configurereflector02()
{
        my $string = "streaming_server.3.rtmpt_url=http://";
        my $temp = "/usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties";
        my $output = "/usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties.temp";
        $temp1 = "streaming_server.3.rtmpt_url=http://".$_[0].":".$nginxPort."/screenshare1/";
        open(IN,"<$temp") or die $!;
        open (OUT,">$output") or die $!;
        while (<IN>)
        {

            if ($_ !~/$string/)
            {
               print OUT $_;
            }
            else
            {
                print OUT $temp1."\n";
            }


        }
        close IN;
        close OUT;
        system(`rm -rf /usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties`);
        system(`mv /usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties.temp /usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties`);
        system(`dos2unix /usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties`);
        return true;
}
sub configurereflector3()
{

        my $string = "streaming_server.4.rtmp_url=http://";
        my $temp = "/usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties";
        my $output = "/usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties.temp";
        $temp1 = "streaming_server.4.rtmp_url=http://".$_[0].":".$nginxPort."/screenshare2/";
        open(IN,"<$temp") or die $!;
        open (OUT,">$output") or die $!;
        while (<IN>)
        {
            if ($_ !~/$string/)
            {
               print OUT $_;
            }
            else
            {
                print OUT $temp1."\n";
            }

        }
        close IN;
        close OUT;
        `rm -rf /usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties`;
        `mv /usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties.temp /usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties`;
        `dos2unix /usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties`;
        return true;
}
sub configurereflector03()
{
        my $string = "streaming_server.4.rtmpt_url=http://";
        my $temp = "/usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties";
        my $output = "/usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties.temp";
        $temp1 = "streaming_server.4.rtmpt_url=http://".$_[0].":".$nginxPort."/screenshare2/";
        open(IN,"<$temp") or die $!;
        open (OUT,">$output") or die $!;
        while (<IN>)
        {

            if ($_ !~/$string/)
            {
               print OUT $_;
            }
            else
            {
                print OUT $temp1."\n";
            }


        }
        close IN;
        close OUT;
        system(`rm -rf /usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties`);
        system(`mv /usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties.temp /usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties`);
        system(`dos2unix /usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties`);
        return true;
}
sub configurereflector4()
{

        my $string = "streaming_server.5.rtmp_url=http://";
        my $temp = "/usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties";
        my $output = "/usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties.temp";
        $temp1 = "streaming_server.5.rtmp_url=http://".$_[0].":".$nginxPort."/screenshare3/";
        open(IN,"<$temp") or die $!;
        open (OUT,">$output") or die $!;
        while (<IN>)
        {
            if ($_ !~/$string/)
            {
               print OUT $_;
            }
            else
            {
                print OUT $temp1."\n";
            }

        }
        close IN;
        close OUT;
        `rm -rf /usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties`;
        `mv /usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties.temp /usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties`;
        `dos2unix /usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties`;
        return true;
}
sub configurereflector04()
{
        my $string = "streaming_server.5.rtmpt_url=http://";
        my $temp = "/usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties";
        my $output = "/usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties.temp";
        $temp1 = "streaming_server.5.rtmpt_url=http://".$_[0].":".$nginxPort."/screenshare3/";
        open(IN,"<$temp") or die $!;
        open (OUT,">$output") or die $!;
        while (<IN>)
        {

            if ($_ !~/$string/)
            {
               print OUT $_;
            }
            else
            {
                print OUT $temp1."\n";
            }


        }
        close IN;
        close OUT;
        system(`rm -rf /usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties`);
        system(`mv /usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties.temp /usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties`);
        system(`dos2unix /usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties`);
        return true;
}
sub configurereflector5()
{

        my $string = "streaming_server.6.rtmp_url=http://";
        my $temp = "/usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties";
        my $output = "/usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties.temp";
        $temp1 = "streaming_server.6.rtmp_url=http://".$_[0].":".$nginxPort."/screenshare4/";
        open(IN,"<$temp") or die $!;
        open (OUT,">$output") or die $!;
        while (<IN>)
        {
            if ($_ !~/$string/)
            {
               print OUT $_;
            }
            else
            {
                print OUT $temp1."\n";
            }

        }
        close IN;
        close OUT;
        `rm -rf /usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties`;
        `mv /usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties.temp /usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties`;
        `dos2unix /usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties`;
        return true;
}
sub configurereflector05()
{
        my $string = "streaming_server.6.rtmpt_url=http://";
        my $temp = "/usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties";
        my $output = "/usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties.temp";
        $temp1 = "streaming_server.6.rtmpt_url=http://".$_[0].":".$nginxPort."/screenshare4/";
        open(IN,"<$temp") or die $!;
        open (OUT,">$output") or die $!;
        while (<IN>)
        {

            if ($_ !~/$string/)
            {
               print OUT $_;
            }
            else
            {
                print OUT $temp1."\n";
            }


        }
        close IN;
        close OUT;
        system(`rm -rf /usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties`);
        system(`mv /usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties.temp /usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties`);
        system(`dos2unix /usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties`);
        return true;
}
sub configurereflector6()
{

        my $string = "streaming_server.7.rtmp_url=http://";
        my $temp = "/usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties";
        my $output = "/usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties.temp";
        $temp1 = "streaming_server.7.rtmp_url=http://".$_[0].":".$nginxPort."/screenshare5/";
        open(IN,"<$temp") or die $!;
        open (OUT,">$output") or die $!;
        while (<IN>)
        {
            if ($_ !~/$string/)
            {
               print OUT $_;
            }
            else
            {
                print OUT $temp1."\n";
            }

        }
        close IN;
        close OUT;
        `rm -rf /usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties`;
        `mv /usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties.temp /usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties`;
        `dos2unix /usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties`;
        return true;
}
sub configurereflector06()
{
        my $string = "streaming_server.7.rtmpt_url=http://";
        my $temp = "/usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties";
        my $output = "/usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties.temp";
        $temp1 = "streaming_server.7.rtmpt_url=http://".$_[0].":".$nginxPort."/screenshare5/";
        open(IN,"<$temp") or die $!;
        open (OUT,">$output") or die $!;
        while (<IN>)
        {

            if ($_ !~/$string/)
            {
               print OUT $_;
            }
            else
            {
                print OUT $temp1."\n";
            }


        }
        close IN;
        close OUT;
        system(`rm -rf /usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties`);
        system(`mv /usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties.temp /usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties`);
        system(`dos2unix /usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties`);
        return true;
}
sub configurereflector7()
{

        my $string = "streaming_server.8.rtmp_url=http://";
        my $temp = "/usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties";
        my $output = "/usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties.temp";
        $temp1 = "streaming_server.8.rtmp_url=http://".$_[0].":".$nginxPort."/screenshare6/";
        open(IN,"<$temp") or die $!;
        open (OUT,">$output") or die $!;
        while (<IN>)
        {
            if ($_ !~/$string/)
            {
               print OUT $_;
            }
            else
            {
                print OUT $temp1."\n";
            }

        }
        close IN;
        close OUT;
        `rm -rf /usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties`;
        `mv /usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties.temp /usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties`;
        `dos2unix /usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties`;
        return true;
}
sub configurereflector07()
{
        my $string = "streaming_server.8.rtmpt_url=http://";
        my $temp = "/usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties";
        my $output = "/usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties.temp";
        $temp1 = "streaming_server.8.rtmpt_url=http://".$_[0].":".$nginxPort."/screenshare6/";
        open(IN,"<$temp") or die $!;
        open (OUT,">$output") or die $!;
        while (<IN>)
        {

            if ($_ !~/$string/)
            {
               print OUT $_;
            }
            else
            {
                print OUT $temp1."\n";
            }


        }
        close IN;
        close OUT;
        system(`rm -rf /usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties`);
        system(`mv /usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties.temp /usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties`);
        system(`dos2unix /usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties`);
        return true;
}
sub configurereflector8()
{

        my $string = "streaming_server.9.rtmp_url=http://";
        my $temp = "/usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties";
        my $output = "/usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties.temp";
        $temp1 = "streaming_server.9.rtmp_url=http://".$_[0].":".$nginxPort."/screenshare7/";
        open(IN,"<$temp") or die $!;
        open (OUT,">$output") or die $!;
        while (<IN>)
        {
            if ($_ !~/$string/)
            {
               print OUT $_;
            }
            else
            {
                print OUT $temp1."\n";
            }

        }
        close IN;
        close OUT;
        `rm -rf /usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties`;
        `mv /usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties.temp /usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties`;
        `dos2unix /usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties`;
        return true;
}
sub configurereflector08()
{
        my $string = "streaming_server.9.rtmpt_url=http://";
        my $temp = "/usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties";
        my $output = "/usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties.temp";
        $temp1 = "streaming_server.9.rtmpt_url=http://".$_[0].":80/screenshare7/";
        open(IN,"<$temp") or die $!;
        open (OUT,">$output") or die $!;
        while (<IN>)
        {

            if ($_ !~/$string/)
            {
               print OUT $_;
            }
            else
            {
                print OUT $temp1."\n";
            }


        }
        close IN;
        close OUT;
        system(`rm -rf /usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties`);
        system(`mv /usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties.temp /usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties`);
        system(`dos2unix /usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties`);
        return true;
}
sub configurereflector9()
{

        my $string = "streaming_server.10.rtmp_url=http://";
        my $temp = "/usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties";
        my $output = "/usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties.temp";
        $temp1 = "streaming_server.10.rtmp_url=http://".$_[0].":".$nginxPort."/screenshare8/";
        open(IN,"<$temp") or die $!;
        open (OUT,">$output") or die $!;
        while (<IN>)
        {
            if ($_ !~/$string/)
            {
               print OUT $_;
            }
            else
            {
                print OUT $temp1."\n";
            }

        }
        close IN;
        close OUT;
        `rm -rf /usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties`;
        `mv /usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties.temp /usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties`;
        `dos2unix /usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties`;
        return true;
}
sub configurereflector09()
{
        my $string = "streaming_server.10.rtmpt_url=http://";
        my $temp = "/usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties";
        my $output = "/usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties.temp";
        $temp1 = "streaming_server.10.rtmpt_url=http://".$_[0].":".$nginxPort."/screenshare8/";
        open(IN,"<$temp") or die $!;
        open (OUT,">$output") or die $!;
        while (<IN>)
        {

            if ($_ !~/$string/)
            {
               print OUT $_;
            }
            else
            {
                print OUT $temp1."\n";
            }


        }
        close IN;
        close OUT;
        system(`rm -rf /usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties`);
        system(`mv /usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties.temp /usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties`);
        system(`dos2unix /usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties`);
        return true;
}sub configurereflector10()
{

        my $string = "streaming_server.11.rtmp_url=http://";
        my $temp = "/usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties";
        my $output = "/usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties.temp";
        $temp1 = "streaming_server.11.rtmp_url=http://".$_[0].":".$nginxPort."/screenshare9/";
        open(IN,"<$temp") or die $!;
        open (OUT,">$output") or die $!;
        while (<IN>)
        {
            if ($_ !~/$string/)
            {
               print OUT $_;
            }
            else
            {
                print OUT $temp1."\n";
            }

        }
        close IN;
        close OUT;
        `rm -rf /usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties`;
        `mv /usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties.temp /usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties`;
        `dos2unix /usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties`;
        return true;
}
sub configurereflector010()
{
        my $string = "streaming_server.11.rtmpt_url=http://";
        my $temp = "/usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties";
        my $output = "/usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties.temp";
        $temp1 = "streaming_server.11.rtmpt_url=http://".$_[0].":".$nginxPort."/screenshare9/";
        open(IN,"<$temp") or die $!;
        open (OUT,">$output") or die $!;
        while (<IN>)
        {

            if ($_ !~/$string/)
            {
               print OUT $_;
            }
            else
            {
                print OUT $temp1."\n";
            }


        }
        close IN;
        close OUT;
        system(`rm -rf /usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties`);
        system(`mv /usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties.temp /usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties`);
        system(`dos2unix /usr/local/dimdim/ConferenceServer/apache-tomcat-5.5.17/webapps/dimdim/WEB-INF/classes/resources/streaming.properties`);
        return true;
}

sub configureMediaServer()
{
                my $string = "MailBoxIP=";
        my $temp = "/usr/local/dimdim/Mediaserver/mods/toolkit/osconfig.py";
        my $output = "/usr/local/dimdim/Mediaserver/mods/toolkit/osconfig.py.temp";
        $temp1 = "MailBoxIP="."\'localhost".":".$_[0]."\'";
        my $count=1;
        open(IN,"<$temp") or die $!;
        open (OUT,">$output") or die $!;
        while (<IN>)
        {
        if ($_ !~/$string/)
                {
                print OUT $_;
                }
        else
                {
                print OUT $temp1."\n";
                }
        }
        close IN;
        close OUT;
        system(`rm -rf /usr/local/dimdim/Mediaserver/mods/toolkit/osconfig.py`);
        system(`mv /usr/local/dimdim/Mediaserver/mods/toolkit/osconfig.py.temp /usr/local/dimdim/Mediaserver/mods/toolkit/osconfig.py`);
        system(`dos2unix /usr/local/dimdim/Mediaserver/mods/toolkit/osconfig.py`);
        return true;
}
