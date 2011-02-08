xpidl.exe -m header -I ..\..\..\..\Dependencies\npapi\gecko-sdk\idl nsIDimdimControl.idl
xpidl.exe -m typelib -I ..\..\..\..\Dependencies\npapi\gecko-sdk\idl nsIDimdimControl.idl
xpt_link npDimdimControl.xpt nsIDimdimControl.xpt