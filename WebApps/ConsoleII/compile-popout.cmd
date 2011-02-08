
@java -Xms512M -Xmx512M -cp "%~dp0/src;../../ThirdPartyPackages/GWT144/gwt-user.jar;../../ThirdPartyPackages/GWT144/gwt-dev-windows.jar;../../ThirdPartyPackages/GWT144/gwt-widgets-0.1.5.jar;../../ThirdPartyPackages/GWT144/gwttk-SNAPSHOT.jar;../../ThirdPartyPackages/GWT144/gwt2swf.jar;../../ThirdPartyPackages/UICommon/dimdim_ui_common.jar" com.google.gwt.dev.GWTCompiler -out "%~dp0/www" %* com.dimdim.conference.ui.workspacepopout.WorkspacePopout
xcopy /E /R /Y www\com.dimdim.conference.ui.workspacepopout.WorkspacePopout\* "E:\DilipMyInstalls\apache-tomcat-5.5.17\webapps\dimdim\html\popout"
