1. Extract wintools.zip and use wintools\buildtools\windows\install.bat

	These tools are required to compile IDL files and create the corresponding header and 
	component files.

2. Set DimDimCodeBase\v2.0\Dependencies\npapi\gecko-sdk\bin to ENV Variables

2. Before compiling npDimdimControl, run compileIDL.bat. compileIDL.bat is available in
	DimdimCodeBase\v2.0\Components\cpp\Publisher4.0\npDimdimControl\

	compileIDL uses the buildtools installed above in step 1.

3. Make sure you use compileIDL.bat everytime you change nsIDimdimControl.idl. If this .idl file
is unchanged, there is no need to run the batch file.


Mail support@dimdim.com for any queries