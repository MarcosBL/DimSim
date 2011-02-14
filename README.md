#DimSim Web Conferencing Server

Written By: Matthew Wilmott mattwilmott@gmail.com
Created: 08 Feb 2011
Last Modifeid: 14 Feb 2011

This is a fork of the recently acquired DimDim software. It is based on the last
publicly released version, 4.5.

DimSim is a free web conferencing service where you can share your desktop, show 
slides, collaborate, chat, talk and broadcast via webcam with absolutely no 
download required for attendees. Dimdim is100% browser-based and is your easy, 
open and free web meeting alternative.

## Current State
 Broken - Working on breaking apart the components, modifying the build scripts and generally tidying the code base.

The webapp component can be built currently and imported into an existing Tomcat installation for testing. Basic chat functionality exists.

## Initial Plans

* Annotate as many functions as possible. 
* Update Documentation as it was very poor in the original product. 
* Remove dependency on rpmbuild and Centos oriented build sequences 
* Split modules into logical folder structures 
* Refactor the code base
* Update libraries
* Update build scripts

  
## Dependencies

TODO - Incomplete at present including versions of already identified deps
I have just listed here all the dependancies needed to compile. At present
they are all contained in the repository. 

Some of these will be need to be culled from the distribution channel. Ideally
every binary needs to be removed and replaced with an Ant/Ivy or Maven script
to pull in deps and compile.

Priority is to determine which packages are actually needed and which can be
updated to their latest versions.

* rpmbuild
* jdk 1.6 +
* ant-1.7
* Red5 - Java based Flash Server - http://www.red5.org
* ffmpeg - http://ffmpeg.org
* swftools - http://www.swftools.org
* Python
* nginx
* Apache Tomcat
* libcurl
* npapi
* gcc-c++
* pcre-devel
* libjpeg-devel
* zlib-devel
* libpng-devel
* fastcgi
* corona - http://sourceforge.net/corona
* common-attributes-api
* common-beanutils
* common-cli
* common-collections
* commons-dbcp
* commons-digester
* commons-el
* commons-fileupload
* commons-io
* commons-lang
* commons-logging
* commons-pool-1.3
* GWT - Google Web Toolkit - Versions 1.4.4 and 1.5.0 ???
* commons-codec-1.3
* commons-httpclient-3.1
* commons-logging-1.1
* ical4j-1.0-beta4
* javamail and activation
* gwttk-server-0.2.3
* jsp-api
* servlet-api
* asm
* cglib-nodep
* dom
* freemarker
* ivy-1.1
* ognl
* oscore
* portlet-api
* rife-continuations
* sitemesh
* spring-beans
* spring-context
* spring-core
* spring-mock
* spring-web
* velocity-dep
* webwork-nostatic-2.2.2
* xwork-tiger
* xwork



## Overview

NOTE: Taken from the DimDim integration document in Documentation folder

The major components of DimSim Server are

* DimSim Conference Server (DCS)
* DimSim Media Server (DMS)
* DimSim Screenshare Server (Reflector)
* DimSim Streaming Server (DSS)

These components are accessed via an nginx based reverse proxy and thus from an 
external perspective all of them are available at a single HTTP port end point 
with the URL patterns serving as the differentiator.

This reduces the complexity of the port architecture and also allows for easier 
firewall management policies when deploying DimSim Web Meeting Server.

* DimSim Conference Server (DCS) provides the infrastructure for conferences
(along with attendees and hosts) to interact using a messaging infrastructure that 
is responsible for state management of the conference itself along with its 
participants.

* DimSim Media Server (DMS) is responsible for handling varied media resources 
like powerpoint presentations, pdf files etc. and also handles pre-uploaded content 
for conferences in future. In conjunction with portal it also provides a mailbox 
system namespaced by DimSim user id and meeting id to provide dynamic as well as 
static content required for personalization/branding of DimSim Web Meeting.

* Dimdim Screenshare Server (Reflector) provides a robust scalable screen share 
protocol server that runs a modified VNC protocol over HTTP/S by leveraging the 
FastCGI protocol. It can support multiple screens each with one presenter and 
multiple viewers.

* DimSim Streaming Server (DSS) provides live streaming capabilities for Audio and 
Video using RTMP or RTMPT (RTMP over HTTP/S) and whiteboard features. It is 
usually commoditized by using either Flash Media Server (FMS) or Wowza Media 
Server (WMS) or Red5 Open source media server.

###Port Architecture
One of the central design motifs of the original DimDim was to lower the number of
ports that have to be opened up for the DimDim infrastructure to function. Because
of the wonderful reverse-proxy capabilities of nginx web server, it is possible to
run DimDim with only one HTTP port open to the world at large. However it is
recommended to open up TCP/1935 for direct RTMP access for optimized performance 
of live A/V streaming and Whiteboard features. Future versions of DimSim will 
hopefully continue this philosophy.




## Getting Started
TODO - Update build scripts etc and write new documentation for make/install.

For now the original process is:

v4.1/Product/Build/Scripts/SF_RPM_Script/README.txt

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

* New DimSim Build script

	     ant -f DimSim_x86_build.xml

This will build all components bar the reflector at this stage.
You can copy the Product/bin/ConferenceServer_Template/linuxdist/DimSimConf-4.5/
ConferenceServer/apache-tomcat-5.5.17/webapps directory to your own Tomcat 
installation to test.

* Old DimDim rpm build script
Tries building all components including the Reflector and creates an RPM. Testing
indicates it is likely to fail however.

	ant -f Dimdim_SF_32_v4.5_build.xml build

	You can find RPM under RPMS/<architecture> directory
	If you build on 32 bit machine, RPM will be under
		/usr/src/redhat/RPMS/i386/dimdim-4.5-1.i386.rpm

	If you build on 64 bit machine, RPM will be under
		/usr/src/redhat/RPMS/x86_64/dimdim-4.5-1.x86_64.rpm

### Project Setup
TODO

### Development Process
TODO

### Testing

### Deployment

### Runtime Environment

## Limitations
TODO - Plenty at present

### Incomplete Features
TODO - Again plenty

## Warnings
This software is by no means production ready. It's currently only a direct
import of the DimDim source as of January 6th 2011. The source is unorganised and
lacks documentation. 

## Contributions/Contacts
Many thanks to:

* Matthew Wilmott - Owner/Maintainer - mattwilmott@gmail.com

## License
TODO - High Priority

