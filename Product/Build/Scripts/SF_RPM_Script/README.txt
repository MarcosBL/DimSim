This file will guide you how to build Dimdim Web Meeting Server
***************************************************************



You need to have rpmbuild, jdk 1.6 and  ant-1.7 installed.

You need to setup Build environment(Specially for Reflector) before building Dimdim Web meeting Server.

Reflector: It is a part of Dimdim 4.5. It use for DTP(desktop sharing).

Dependencies:
To build reflector you need to have corona-1.0.2 and fcgi-2.4.0 installed.


Setting environment:
go to 
	cd v4.1/FCGIApps/Reflector
	vi compile_all.sh

Specify corona and fcgi installed path like

	export FCGI_DIR=/usr/local/Code/Dependencies/Reflector/fcgi
	export CORONA_DIR=/usr/local/Code/Dependencies/Reflector/corona

Now make a directory structure above to v4.1 as shown below
	
	<ParentDirectory>
		v4.1
		Dependencies
			Reflector
				corona_fcgi_libs
	

Now do 
	cp /path/to/corona/lib/*so* <ParentDirectory>Dependencies/Reflector/corona_fcgi_libs/
	cp /path/to/fcgi/lib/*so* <ParentDirectory>Dependencies/Reflector/corona_fcgi_libs/


Now try to run Build script
	cd <ParentDirectory>/v4.1/Product/Build/Scripts/SF_RPM_Script/
	ant -f Dimdim_SF_32_v4.5_build.xml build

You can find RPM under RPMS/<architecture> directory
If you build on 32 bit machine, RPM will be under
	/usr/src/redhat/RPMS/i386/dimdim-4.5-1.i386.rpm

If you build on 64 bit machine, RPM will be under
	/usr/src/redhat/RPMS/x86_64/dimdim-4.5-1.x86_64.rpm


