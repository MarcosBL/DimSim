//============================================================================
// Name        : GenLightyConfig.cpp
// Author      : 
// Version     :
// Copyright   : Your copyright notice
// Description : Hello World in C++, Ansi-style
//============================================================================

#include <iostream>
using namespace std;

void printConfigBlock(const char* dirName, const char* endPoint, int index)
{
	if(index == 0)
	{
		std::cout<<"    \"/"<<endPoint<<"/\" =>"<<std::endl;
		std::cout<<"    ((	\"bin-path\" => \""<<dirName<<"/dimdimReflector\","<<std::endl;
		std::cout<<"        \"socket\" => \"/var/run/dimdim-fcgi-screenshare-"<<index<<"-socket\","<<std::endl;
		std::cout<<"        \"check-local\" => \"disable\","<<std::endl;
		std::cout<<"        \"min-procs\" => 1,"<<std::endl;
		std::cout<<"        \"max-procs\" => 1"<<std::endl;
		std::cout<<"    )),"<<std::endl;
	}
	std::cout<<"    \"/"<<endPoint<<index<<"/\" =>"<<std::endl;
	std::cout<<"    ((	\"bin-path\" => \""<<dirName<<"/dimdimReflector\","<<std::endl;
	std::cout<<"        \"socket\" => \"/var/run/dimdim-fcgi-screenshare-"<<index<<"-socket\","<<std::endl;
	std::cout<<"        \"check-local\" => \"disable\","<<std::endl;
	std::cout<<"        \"min-procs\" => 1,"<<std::endl;
	std::cout<<"        \"max-procs\" => 1"<<std::endl;
	std::cout<<"    ))";
}
void printConfig(const char* dirName, int count)
{

	std::cout<<"fastcgi.server = ("<<std::endl;
	char buf[64];
	for(int i = 0; i < count; i++)
	{
	
		printConfigBlock(dirName, "screenshare", i);
		std::cout<<","<<std::endl;
		printConfigBlock(dirName, "screenshareviewer",i);
		std::cout<<","<<std::endl;
		printConfigBlock(dirName, "screenshareadmin", i);
		std::cout<<","<<std::endl;
		printConfigBlock(dirName, "screensharequery", i);
		std::cout<<","<<std::endl;
		printConfigBlock(dirName, "screensharestatus", i);
		std::cout<<","<<std::endl;
	}
	std::cout<<")"<<std::endl;
}

int main(int argc, char** argv) 
{
	char* dirName = "DIMDIM_REFLECTOR_DIR";
	int count = 10;
	if(argc > 1)
	{
		dirName = argv[1];
	}
	if(argc > 2)
	{
		count = atoi(argv[2]);
	}
	std::cout<<"#"<<std::endl;
	std::cout<<"# generating for "<<count<<" fcgi instances"<<std::endl;
	std::cout<<"#"<<std::endl;
	::printConfig(dirName,count);
	
	return 0;
}
