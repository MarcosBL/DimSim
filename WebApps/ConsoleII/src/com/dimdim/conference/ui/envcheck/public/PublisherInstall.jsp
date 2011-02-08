<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page import="com.dimdim.util.misc.StringGenerator" %>
<%@ page import="com.dimdim.conference.ConferenceConsoleConstants" %>
<%@ page import="com.dimdim.conference.ConferenceConstants" %>
<%@ page import="com.dimdim.util.session.UserRequest" %>
<%@ page import="com.dimdim.conference.UtilMethods" %>
<%@ page import="java.util.Date" %>
<%@ page import="com.dimdim.util.session.UserRequest" %>
<%@ page import="com.dimdim.util.session.MeetingSettings" %>
<%@ page import="com.dimdim.util.session.UserSessionDataManager" %>
<%
	response.addHeader("Pragma", "no-cache"); 
	response.addHeader("Expires", "-1"); 
	response.addDateHeader("Last-Modified", (new Date()).getTime()); 
%>
<%@ taglib prefix="dm" uri="com.dimdim.conferenceConsole.tags" %>
<dm:SetSessionLocale/>
<%
	String userAgent = request.getHeader("user-agent");
	if (userAgent != null)
	{
		userAgent = userAgent.toLowerCase();
	}
	else
	{
		userAgent = UtilMethods.findUserAgent(request);
	}
	String osType = UtilMethods.getOsType(userAgent);
	String browserType = UtilMethods.getBrowserType(userAgent);
	String browserVersion = UtilMethods.getBrowserVersion(userAgent);
	
	System.out.println("from pub installer browserType:"+browserType);
	System.out.println("from pub installer osType is:"+osType);
	System.out.println("from pub installer browserVersion:"+browserVersion);
	String webappName = ConferenceConsoleConstants.getWebappName();
	StringGenerator idGen = new StringGenerator();
	String dummyFlag = idGen.generateRandomString(7,7);
	String checkObject = (String)request.getParameter("checkObject");
	String submitFormOnLoad = (String)request.getParameter("submitFormOnLoad");
	String freshInstall = (String)request.getParameter("freshInstall");
	String serverURL = ConferenceConsoleConstants.getServerAddress();
	String uri = (String)request.getParameter("uri");
	String userType = ConferenceConsoleConstants.getUserTypeFreeOrPaid();
	
//	if (uri != null)
//	{
//		UserRequest ur = UserSessionDataManager.getDataManager().getUserRequest(uri);
//		UserRequest ur = PortalInterface.getPortalInterface().getUserRequest(uri);
//		if (ur != null)
//		{
//			userType = ur.getUserInfo().getUserType();
//		}
//		else
//		{
//			System.out.println("Invalid user request id:"+uri);
//		}
//	}
	
	String success_url = (String)request.getAttribute(UserRequest.MEETING_CONNECT_CONTINUE_URL);
	System.out.println("from pub installer Continue on success url is:"+success_url);
	
	if (freshInstall == null)
	{
		freshInstall = "true";
	}
	if (freshInstall.equals("true"))
	{
		submitFormOnLoad = null;
	}
	else
	{
		// this upgrade option needs to be varified.
		submitFormOnLoad = null;
	}
	int pubMajorVersion = ConferenceConstants.PUBLISHER_CONTROL_MAJOR_VERSION;
	int pubMinorVersion = ConferenceConstants.PUBLISHER_CONTROL_MINOR_VERSION;
	int pubBuildVersion = ConferenceConstants.PUBLISHER_CONTROL_BUILD_VERSION;
	int pubBuildVersion2 = ConferenceConstants.PUBLISHER_CONTROL_BUILD_VERSION_2;
	//System.out.println("inside pub installer pubBuildVersion = "+pubBuildVersion);
%>
<html>
<head>
<% if("Premium".equalsIgnoreCase(userType)){%>
<title><dm:I18NDisplayString component="landing_pages" dictionary="ui_strings" key="install_x_control_page.title" userType="Premium"/></title>
<%}else{%>
<title><dm:I18NDisplayString component="landing_pages" dictionary="ui_strings" key="install_x_control_page.title" userType="free"/></title>		
<%} %>
  <link href="style_portal.css"rel="stylesheet" type="text/css" />
  <!--[if lt IE 7.]>
  	<script defer type="text/javascript" src="pngfix.js"></script>
  <![endif]-->
<script language="JavaScript1.2" type="text/javascript" src="xpi_install_support.js"></script>
<script type="text/javascript">		
window.publisher_available = "unknown";
window.publisher_availability = "unknown";
window.success_url = "<%=success_url%>";


var checkDone=false;


function launchURL()
{	
	//alert('inside launch url....');
	
	var iframe1; 
	iframe1 = document.createElement("iframe");
	iframe1.setAttribute("src", "<%=serverURL%>" + "/" + "<%=webappName%>" + "/" + "jsp/activex/" + "Screencaster_IE_"+ "<%=pubMajorVersion%>" + "<%=pubMinorVersion%>" + "<%=pubBuildVersion%>" + "<%=pubBuildVersion2%>" + ".exe");
	iframe1.setAttribute("id", "1");
	iframe1.setAttribute("id", "1");
	iframe1.setAttribute("scrolling", "no");
	iframe1.setAttribute("frameBorder", "0");
	iframe1.setAttribute("height","0");
	iframe1.setAttribute("width","0");
	
	if(document.body)
	{
		document.body.appendChild(iframe1);
	}
	iframe1=null;
	timer = setInterval ( "checkIE()", 5000 );
	
}

function updateText()
{ 
	var divtag = document.getElementById('install_button');
	//alert('changing button to upgrade divtag = '+divtag);
	divtag.innerHTML = "<Strong>Upgrade</Strong>";
	
}

function checkPassed()
{	
	//alert('publisher installed....success_url ='+window.success_url);
	checkDone = true;
	clearInterval(timer);
	//window.parent.publisher_availability = "available";
	//window.parent.publisher_available = "true";
	if(null != window.success_url && 'null' != window.success_url){
		window.location.href = window.success_url;
	}
}


function initialCheck()
{	
	//alert('inside on load...');
	//window.parent.publisher_availability = "available";
	//window.parent.publisher_available = "true";
	<% if("windows".equalsIgnoreCase(osType)){%>
			checkForWindows();
	<%} else if("windows".equalsIgnoreCase(osType)) {%> 
			checkForMac();
	<%	}%> 
	

}

function checkForMac()
{

		var major = <%=pubMajorVersion%>;
		var minor = <%=pubMinorVersion%>;
		var build = <%=pubBuildVersion%>;
		var build2 = <%=pubBuildVersion2%>;		
		var version = ( build2 % 100 + (build % 100) * 100 + (minor % 100) * 100000 + (major % 100) * 1000000 )+"";
		var requiredVersionCheck = version.substring(0,2);
		//alert(requiredVersionCheck);
	
		var xControlVersion = checkMacDimdimPublisher(major,minor,build,build2);
		//alert('browser is ff...xControlVersion = '+xControlVersion);
		if (xControlVersion == 1)
		{
			//alert('mac plugin not installed....');
		}
		else if (xControlVersion == 2)
		{
			//alert('mac plugin prev installed....');
			updateText();

		}
		else if (xControlVersion == 3)
		{
			//alert('mac plugin installed....');
			checkPassed();
		}
}

function checkForWindows()
{
<% if (browserType != null && (browserType.equalsIgnoreCase("ie") || browserType.equalsIgnoreCase("firefox"))) { %>
		var major = <%=pubMajorVersion%>;
		var minor = <%=pubMinorVersion%>;
		var build = <%=pubBuildVersion%>;
		var build2 = <%=pubBuildVersion2%>;		
		var version = ( build2 % 100 + (build % 100) * 100 + (minor % 100) * 100000 + (major % 100) * 1000000 )+"";
		var requiredVersionCheck = version.substring(0,2);
		//alert(requiredVersionCheck);
	<%   if (browserType.equalsIgnoreCase("ie")) { %>
		if ( DimdimPublisher != null && DimdimPublisher.getVersion!=null )
		{
			var pubVersion = DimdimPublisher.getVersion()+"";
			if (pubVersion.substring(0,2) == requiredVersionCheck)
			{
				checkPassed();
				//alert('ie plugin installed....');
			}
			else
			{
				//alert('ie plugin not installed....');
				//alert('upgrade....');
				//updateText();
			}
		}else 
			{
				//alert('ie pub unavailabel..');
			}
	

	<% } else { /* Firefox way of checking the control existance and version */ %>
		//var xControlVersion = getXControlVersion(version);
		var xControlVersion = checkFirefoxDimdimPublisher(major,minor,build,build2);
		//alert('browser is ff...xControlVersion = '+xControlVersion);
		if (xControlVersion == 1)
		{
			//alert('ff plugin not installed....');
		}
		else if (xControlVersion == 2)
		{
			//alert('ff plugin prev installed....');
			updateText();

		}
		else if (xControlVersion == 3)
		{
			//alert('ff plugin installed....');
			checkPassed();
		}
	<% } 
	}%>
}
function checkInstallerIE()
{
	<% if (browserType != null && (browserType.equalsIgnoreCase("ie") || browserType.equalsIgnoreCase("firefox"))) { %>
		var major = <%=pubMajorVersion%>;
		var minor = <%=pubMinorVersion%>;
		var build = <%=pubBuildVersion%>;
		var build2 = <%=pubBuildVersion2%>;		
		var version = ( build2 % 100 + (build % 100) * 100 + (minor % 100) * 100000 + (major % 100) * 1000000 )+"";
		var requiredVersionCheck = version.substring(0,2);
	<%}%>

	try
	{
		var pubVersion = DimdimDesktopPublisher1.getVersion()+"";
		//alert("inside checkInstallerIE pubVersion = "+pubVersion);
		//alert("inside checkInstallerIE requiredVersionCheck = "+requiredVersionCheck);

		if (pubVersion.substring(0,2) == requiredVersionCheck)
		{
			//alert('inside checkInstallerIE check passed...');
			//parent.checkPassed();
			checkPassed();

		}
		else
		{
			//alert('inside checkInstallerIE check failed...');
			//parent.checkFailed();

		}
	}
	catch(err)
	{
		//alert('inside checkInstallerIE check failed...err='+err);
		//parent.checkFailed();

	}
}

function checkIE()
{
	var d = document.getElementById('pubobj');
	//alert('inside checIE...'+d);
  	d.innerHTML = '<object id="DimdimDesktopPublisher1" classid="CLSID:5100F713-1B48-4A6B-9985-EDDFB7F1C0DF" height="0" width="0">';
  	
	try
	{
		var obj = new ActiveXObject("xDimdimControl_40.xpublisher.1");
		obj = null;
		//alert('calling check installer...');
		checkInstallerIE();
	}
	catch(e)
	{
		//alert('inside check e = '+e);
		//parent.checkFailed();
	}
}

function continueWorkForWindows()
{
<% if (browserType != null && (browserType.equalsIgnoreCase("ie") || browserType.equalsIgnoreCase("firefox"))) { %>
		var	major = <%=pubMajorVersion%>;
		var minor = <%=pubMinorVersion%>;
		var build = <%=pubBuildVersion%>;
		var build2 = <%=pubBuildVersion2%>;		
		var version = ( build2 % 100 + (build % 100) * 100 + (minor % 100) * 100000 + (major % 100) * 1000000 )+"";
		var requiredVersionCheck = version.substring(0,2);
		
<%   if (browserType.equalsIgnoreCase("ie")) { %>
<%  //if (submitFormOnLoad != null && submitFormOnLoad.equals("true")) { %>
		if ( DimdimPublisher != null &&
		 		DimdimPublisher.getVersion!=null )
		{

			try
			{
				var pubVersion = DimdimPublisher.getVersion()+"";
				if (pubVersion.substring(0,2) == requiredVersionCheck)
				{
					//alert('pub version is not as expected');
					window.publisher_available = "false";
					launchURL();
				}
				else
				{
					//alert('check passed');
					window.publisher_available = "true";
					checkPassed();
				}
			}
			catch(e)
			{
				//alert('exception occured');
				window.publisher_available = "false";
				launchURL();
			}
		}
		else
		{
			//alert('pub is not there at all');
			window.publisher_available = "false";
			launchURL();
		}
		
<% //} %>
<% } else { /* Firefox way of checking the control existance and version */ %>
		//var xControlVersion = getXControlVersion(version);
	var installUrl = "<%=serverURL%>" + "/" + "<%=webappName%>" + "/" + "jsp/activex/" + "Screencaster_FF_"+<%=pubMajorVersion%>+"."+<%=pubMinorVersion%>+".exe";
	
	if (!checkDone)
	{
		var xControlVersion = checkFirefoxDimdimPublisher(major,minor,build,build2);
		//alert('browser is ff...xControlVersion = '+xControlVersion);
		if (xControlVersion == 1)
		{
			window.publisher_availability = "not_available";
			//InstallTrigger.install({" Screencaster Plug-in" : "/<%=webappName%>/jsp/activex/<%=pubMajorVersion%><%=pubMinorVersion%><%=pubBuildVersion%><%=pubBuildVersion2%>dimdim.xpi"});
			launchWindowsFFURL(major,minor,build,build2, installUrl);
		}
		else if (xControlVersion == 2)
		{
			window.publisher_availability = "lower_version_available";
			//InstallTrigger.install({" Screencaster Plug-in" : "/<%=webappName%>/jsp/activex/<%=pubMajorVersion%><%=pubMinorVersion%><%=pubBuildVersion%><%=pubBuildVersion2%>dimdim.xpi"});
			launchWindowsFFURL(major,minor,build,build2, installUrl);
		}
		else if (xControlVersion == 3)
		{
			window.publisher_availability = "available";
			window.publisher_available = "true";
			checkPassed();
		}
	}
<%   } %>
<% } %>
}

function continueWorkForMac(){

	var major = <%=pubMajorVersion%>;
	var minor = <%=pubMinorVersion%>;
	var build = <%=pubBuildVersion%>;
	var build2 = <%=pubBuildVersion2%>;	
	var installUrl = "<%=serverURL%>" + "/" + "<%=webappName%>" + "/" + "jsp/activex/" + "Screencaster_"+<%=pubMajorVersion%>+"."+<%=pubMinorVersion%>+".dmg";
	
	launchMacURL(major,minor,build,build2, installUrl);
}

function continueWork()
{
<% if("windows".equalsIgnoreCase(osType)){%>
			continueWorkForWindows();
<%} else if("mac".equalsIgnoreCase(osType)) {%> 
			continueWorkForMac();
<%	}%> 
	

}
</script>

</head>
<!-- <body onLoad="javascript:continueWork()"> -->
<body onLoad="javascript:initialCheck()">
<% if ((browserType == null || browserType.equalsIgnoreCase("ie")) ) { %>
	<OBJECT ID="DimdimPublisher" WIDTH=0 HEIGHT=0
		   CLASSID="CLSID:5100F713-1B48-4A6B-9985-EDDFB7F1C0DF">		   
	</OBJECT>
	
	
<% }  %>

<NOSCRIPT><% if("Premium".equalsIgnoreCase(userType)){%>
<dm:I18NDisplayString component="common" dictionary="ui_strings" key="all_pages.no_javascript_comment" userType="Premium"/>
<%}else{%>
<dm:I18NDisplayString component="common" dictionary="ui_strings" key="all_pages.no_javascript_comment" userType="free"/>
<%} %>
</NOSCRIPT>

<table width="1004" border="0" cellspacing="0" cellpadding="0" align="center">
<!-- <tr>    <td align="center">
<table width="982" border="0" cellspacing="0" cellpadding="0">  
<td width="150" align="center">
<div class="logo"><a href="http://www.dimdim.com/"><div id="logolink"><br />        <br />  <br /></div></a></div></td>        
<td align="right" valign="top"><div id="form"></div></td></tr></table>

</td></tr>  -->
<tr>
<td>

<div id="pubobj" >
</div>

<table width="982" border="0" align="left" cellpadding="0" cellspacing="0">
      <tr>
    <td width="50" align="left" valign="top">
      <img src="images/sidea.png" height="522" width="50">    </td>
<br />
<td align="center" valign="top"><br />
      <div id="chechking_process" style="margin-left:30px;">
      <div id="envchk_top"></div>      
      <div id="envchk_content">
      <div align="left" id="plugin_header_text"><strong>
      <% if("Premium".equalsIgnoreCase(userType)){%>
      <dm:I18NDisplayString component="landing_pages" dictionary="ui_strings" key="install_x_control_page.heading1" userType="Premium"/>
      <%} else { %>
            <dm:I18NDisplayString component="landing_pages" dictionary="ui_strings" key="install_x_control_page.heading1" userType="free" />
      <%} %>
      </strong></div>
<br /><br />
<div id="plugin_installbox"><br />
  <br />
  <br />
  <div id="publusher_version"><strong>
  <% if("Premium".equalsIgnoreCase(userType)){%>
  <dm:I18NDisplayString component="landing_pages" dictionary="ui_strings" key="install_x_control_page.title" userType="Premium"/>
  <%} else {%>
  <dm:I18NDisplayString component="landing_pages" dictionary="ui_strings" key="install_x_control_page.title" userType="free"/>
  <%} %>
  </strong></div>
  <br />
<br />
<br />
<br />

<% if ((browserType != null) && browserType.equalsIgnoreCase("firefox")){ %>
  	<div id="publusher_text1">
  	<% if("Premium".equalsIgnoreCase(userType)){%>
  	<dm:I18NDisplayString component="landing_pages" dictionary="ui_strings" key="install_x_control_page.text1" userType="Premium"/>
  	<%} else{ %>
  	<dm:I18NDisplayString component="landing_pages" dictionary="ui_strings" key="install_x_control_page.text1" userType="free"/>
  	<%} %>
  	</div>
  	<div id="publusher_text2">
 	<% if("Premium".equalsIgnoreCase(userType)){%>
  	<dm:I18NDisplayString component="landing_pages" dictionary="ui_strings" key="install_x_control_page.text2" />
  	<%} else{ %>
  	  	<dm:I18NDisplayString component="landing_pages" dictionary="ui_strings" key="install_x_control_page.text2" />
  	<%} %>
  	</div>
  	  	
<%} else if((browserType == null) || browserType.equalsIgnoreCase("ie")){%>
	<div id="publusher_text1">
	<% if("Premium".equalsIgnoreCase(userType)){%>
	<dm:I18NDisplayString component="landing_pages" dictionary="ui_strings" key="install_x_control_page.text.ie14" userType="Premium"/>
	<%} else { %>
	<dm:I18NDisplayString component="landing_pages" dictionary="ui_strings" key="install_x_control_page.text.ie14" userType="free"/>
	<%} %>
	</div>
<%}%>

  <br />
<br />
  <div id="publusher_text3">
  <% if("Premium".equalsIgnoreCase(userType)){%>
  <dm:I18NDisplayString component="landing_pages" dictionary="ui_strings" key="install_x_control_page.text3" userType="Premium"/>
  <%} else{ %>
  <dm:I18NDisplayString component="landing_pages" dictionary="ui_strings" key="install_x_control_page.text3" userType="free"/>
  <%} %>
  </div><br />
  <br>
<div id="install_button" class="Install_redbtn" align="center" onClick="javascript:continueWork();"><strong>Install</strong>
</div>
<!-- <div id="install_button" align="right" onClick="continueWork();"></div> -->
      </div><br>
<br>
<br>
      
            </div>
<div id="envchk_bottom"></div>
      </div></td>

      
      <td width="317" rowspan="2" align="right" valign="top">
 <% if ((browserType != null) && browserType.equalsIgnoreCase("firefox")){ 
 
 if(osType.equalsIgnoreCase("windows") || osType.equalsIgnoreCase("linux")){%>
<div align="left" class="sidea">
<div id="page_header_text"><strong>
<% if("Premium".equalsIgnoreCase(userType)){%>
<dm:I18NDisplayString component="landing_pages" dictionary="ui_strings" key="install_x_control_page.text4" userType="Premium"/>
<%} else{ %>
<dm:I18NDisplayString component="landing_pages" dictionary="ui_strings" key="install_x_control_page.text4" userType="free"/>
<%} %>
</strong></div>



          <DIV id="sidebarContent"><br />
            <div id="page_message_text_1">
            <% if("Premium".equalsIgnoreCase(userType)){%>
            <dm:I18NDisplayString component="landing_pages" dictionary="ui_strings" key="install_x_control_page.text5" userType="Premium"/>
            <%}else{ %>
            <dm:I18NDisplayString component="landing_pages" dictionary="ui_strings" key="install_x_control_page.text5" userType="free"/>            
            <%} %>
            <br />
		</div>
            <br />
		<br />

            <div id="page_message_text_2">
            <% if("Premium".equalsIgnoreCase(userType)){%>
            <dm:I18NDisplayString component="landing_pages" dictionary="ui_strings" key="install_x_control_page.text6" userType="Premium"/>
            <%}else{ %>
            <dm:I18NDisplayString component="landing_pages" dictionary="ui_strings" key="install_x_control_page.text6" userType="free"/>
            <%} %>
            </div>

	<table width="100%" border="0" cellspacing="0" cellpadding="1" style="font-size:12px">
	    <tr>
	      <td>&nbsp;</td>
	      <td>&nbsp;</td>
	    </tr>
	    <tr>
	      <td width="12%"><strong><img src="images/01.png" width="31" height="31"></strong></td>
	      <td width="88%"><div id="page_message_text_3"><strong>
	      <% if("Premium".equalsIgnoreCase(userType)){%>
	      <dm:I18NDisplayString component="landing_pages" dictionary="ui_strings" key="install_x_control_page.text7" userType="Premium"/>
	      <%}else{ %>
	      <dm:I18NDisplayString component="landing_pages" dictionary="ui_strings" key="install_x_control_page.text7" userType="free"/>
	      <%} %>
	      </strong> </div></td>
	    </tr>
	    <tr>
	      <td colspan="2"><div id="page_message_text_4"><strong>
	      <% if("Premium".equalsIgnoreCase(userType)){%>
	      <dm:I18NDisplayString component="landing_pages" dictionary="ui_strings" key="install_x_control_page.text8" userType="Premium"/>
	      <%}else{ %>
	      <dm:I18NDisplayString component="landing_pages" dictionary="ui_strings" key="install_x_control_page.text8" userType="free"/>	      
	      <%} %>
	      </div></td>
	      </tr>

	    <tr>
	      <td>&nbsp;</td>
	      <td>&nbsp;</td>
	    </tr>
	    <tr>
	      <td><strong><img src="images/02.png" alt="" width="31" height="31"></strong></td>
	      <td><div id="page_message_text_5"><strong><strong>
	      <% if("Premium".equalsIgnoreCase(userType)){%>
	      <dm:I18NDisplayString component="landing_pages" dictionary="ui_strings" key="install_x_control_page.text9" userType="Premium"/>
	      <%}else{ %>
	      <dm:I18NDisplayString component="landing_pages" dictionary="ui_strings" key="install_x_control_page.text9" userType="free"/>
	      <%} %>
	      </strong> </div></td>
	    </tr>
	    <tr>
	      <td colspan="2"><div id="page_message_text_6"><strong>
	      <% if("Premium".equalsIgnoreCase(userType)){%>
	      <dm:I18NDisplayString component="landing_pages" dictionary="ui_strings" key="install_x_control_page.text10" userType="Premium"/>
	      <%}else{ %>
	      <dm:I18NDisplayString component="landing_pages" dictionary="ui_strings" key="install_x_control_page.text10" userType="free"/>
	      <%} %>
	      </div></td>
	      </tr>
	    <tr>
	      <td>&nbsp;</td>
	      <td>&nbsp;</td>
	    </tr>
	    <tr>
	      <td><strong><img src="images/03.png" alt="" width="31" height="31"></strong></td>
	      <td><div id="page_message_text_7"><strong>
	      <% if("Premium".equalsIgnoreCase(userType)){%>
	      <dm:I18NDisplayString component="landing_pages" dictionary="ui_strings" key="install_x_control_page.text11" userType="Premium"/>
	      <%}else{ %>
	      <dm:I18NDisplayString component="landing_pages" dictionary="ui_strings" key="install_x_control_page.text11" userType="free"/>	      
	      <%} %>
	      </strong> </div></td>
	    </tr>
	    <tr>
	      <td colspan="2"><div id="page_message_text_8">
	      <% if("Premium".equalsIgnoreCase(userType)){%>
	      <dm:I18NDisplayString component="landing_pages" dictionary="ui_strings" key="install_x_control_page.text12" userType="Premium"/>
	      <%} else{ %>
	      <dm:I18NDisplayString component="landing_pages" dictionary="ui_strings" key="install_x_control_page.text12" userType="free"/>
	      <%} %>
	      </div></td>
	      </tr>
	    <tr>
	      <td colspan="2"><div id="page_message_text_9"><strong>
	      <% if("Premium".equalsIgnoreCase(userType)){%>
	      <dm:I18NDisplayString component="landing_pages" dictionary="ui_strings" key="install_x_control_page.text13" userType="Premium"/>
	      <%} else { %>
	      <dm:I18NDisplayString component="landing_pages" dictionary="ui_strings" key="install_x_control_page.text13" userType="free"/>	      
	      <%} %>
	      </strong></div><br></td>
	      </tr>
	  </table>
	<div id="page_message_text_3"> </div>
        </div>
      </div>
      <%}
      else if(osType.equalsIgnoreCase("mac")){%>
      	<div align="left" class="sidea">
	<div id="page_header_text"><strong>
	<% if("Premium".equalsIgnoreCase(userType)){%>
	<dm:I18NDisplayString component="landing_pages" dictionary="ui_strings" key="install_x_control_page_mac.text4" userType="Premium"/>
	<%} else{ %>
	<dm:I18NDisplayString component="landing_pages" dictionary="ui_strings" key="install_x_control_page_mac.text4" userType="free"/>
	<%} %>
	</strong></div>
	
	
	
	          <DIV id="sidebarContent"><br />
	            <br />
			<br />
	
	            <div id="page_message_text_2">
	            <% if("Premium".equalsIgnoreCase(userType)){%>
	            <dm:I18NDisplayString component="landing_pages" dictionary="ui_strings" key="install_x_control_page_mac.text6" userType="Premium"/>
	            <%}else{ %>
	            <dm:I18NDisplayString component="landing_pages" dictionary="ui_strings" key="install_x_control_page_mac.text6" userType="free"/>
	            <%} %>
	            </div>
	
		<table width="100%" border="0" cellspacing="0" cellpadding="1" style="font-size:12px">
		    <tr>
		      <td>&nbsp;</td>
		      <td>&nbsp;</td>
		    </tr>
		    <tr>
		      <td width="12%"><strong><img src="images/01.png" width="31" height="31"></strong></td>
		      <td width="88%"><div id="page_message_text_3">
		      <% if("Premium".equalsIgnoreCase(userType)){%>
		      <dm:I18NDisplayString component="landing_pages" dictionary="ui_strings" key="install_x_control_page_mac.text7" userType="Premium"/>
		      <%}else{ %>
		      <dm:I18NDisplayString component="landing_pages" dictionary="ui_strings" key="install_x_control_page_mac.text7" userType="free"/>
		      <%} %>
		      </div></td>
		    </tr>
		    <tr>
		      <td>&nbsp;</td>
		      <td>&nbsp;</td>
		    </tr>
		    <tr>
		      <td><strong><img src="images/02.png" alt="" width="31" height="31"></strong></td>
		      <td><div id="page_message_text_5">
		      <% if("Premium".equalsIgnoreCase(userType)){%>
		      <dm:I18NDisplayString component="landing_pages" dictionary="ui_strings" key="install_x_control_page_mac.text8" userType="Premium"/>
		      <%}else{ %>
		      <dm:I18NDisplayString component="landing_pages" dictionary="ui_strings" key="install_x_control_page_mac.text8" userType="free"/>
		      <%} %>
		       </div></td>
		    </tr>
		    <tr>
		      <td>&nbsp;</td>
		      <td>&nbsp;</td>
		    </tr>
		    <tr>
		      <td><strong><img src="images/03.png" alt="" width="31" height="31"></strong></td>
		      <td><div id="page_message_text_7">
		      <% if("Premium".equalsIgnoreCase(userType)){%>
		      <dm:I18NDisplayString component="landing_pages" dictionary="ui_strings" key="install_x_control_page_mac.text9" userType="Premium"/>
		      <%}else{ %>
		      <dm:I18NDisplayString component="landing_pages" dictionary="ui_strings" key="install_x_control_page_mac.text9" userType="free"/>	      
		      <%} %>
		      </div></td>
		    </tr>
	    
		  </table>
		<div id="page_message_text_3"> </div>
	        </div>
	      </div>

<%      } 
 }
 else if(osType.equalsIgnoreCase("mac") && browserType.equalsIgnoreCase("safari")){%>
 	<div align="left" class="sidea">
<div id="page_header_text"><strong>
<% if("Premium".equalsIgnoreCase(userType)){%>
<dm:I18NDisplayString component="landing_pages" dictionary="ui_strings" key="install_x_control_page_mac_safari.text4" userType="Premium"/>
<%} else{ %>
<dm:I18NDisplayString component="landing_pages" dictionary="ui_strings" key="install_x_control_page_mac_safari.text4" userType="free"/>
<%} %>
</strong></div>



         <DIV id="sidebarContent"><br />
           <br />
		<br />

           <div id="page_message_text_2">
           <% if("Premium".equalsIgnoreCase(userType)){%>
           <dm:I18NDisplayString component="landing_pages" dictionary="ui_strings" key="install_x_control_page_mac_safari.text6" userType="Premium"/>
           <%}else{ %>
           <dm:I18NDisplayString component="landing_pages" dictionary="ui_strings" key="install_x_control_page_mac_safari.text6" userType="free"/>
           <%} %>
           </div>

	<table width="100%" border="0" cellspacing="0" cellpadding="1" style="font-size:12px">
	    <tr>
	      <td>&nbsp;</td>
	      <td>&nbsp;</td>
	    </tr>
	    <tr>
	      <td width="12%"><strong><img src="images/01.png" width="31" height="31"></strong></td>
	      <td width="88%"><div id="page_message_text_3">
	      <% if("Premium".equalsIgnoreCase(userType)){%>
	      <dm:I18NDisplayString component="landing_pages" dictionary="ui_strings" key="install_x_control_page_mac_safari.text7" userType="Premium"/>
	      <%}else{ %>
	      <dm:I18NDisplayString component="landing_pages" dictionary="ui_strings" key="install_x_control_page_mac_safari.text7" userType="free"/>
	      <%} %>
	      </div></td>
	    </tr>

	    <tr>
	      <td>&nbsp;</td>
	      <td>&nbsp;</td>
	    </tr>
	    <tr>
	      <td><strong><img src="images/02.png" alt="" width="31" height="31"></strong></td>
	      <td><div id="page_message_text_5">
	      <% if("Premium".equalsIgnoreCase(userType)){%>
	      <dm:I18NDisplayString component="landing_pages" dictionary="ui_strings" key="install_x_control_page_mac_safari.text8" userType="Premium"/>
	      <%}else{ %>
	      <dm:I18NDisplayString component="landing_pages" dictionary="ui_strings" key="install_x_control_page_mac_safari.text8" userType="free"/>
	      <%} %>
	       </div></td>
	    </tr>
	
	    <tr>
	      <td>&nbsp;</td>
	      <td>&nbsp;</td>
	    </tr>
	    <tr>
	      <td><strong><img src="images/03.png" alt="" width="31" height="31"></strong></td>
	      <td><div id="page_message_text_7">
	      <% if("Premium".equalsIgnoreCase(userType)){%>
	      <dm:I18NDisplayString component="landing_pages" dictionary="ui_strings" key="install_x_control_page_mac_safari.text9" userType="Premium"/>
	      <%}else{ %>
	      <dm:I18NDisplayString component="landing_pages" dictionary="ui_strings" key="install_x_control_page_mac_safari.text9" userType="free"/>	      
	      <%} %>
	      </div></td>
	    </tr>
	    
	  </table>
	<div id="page_message_text_3"> </div>
       </div>
     </div>

<%        } 
 
 else if((browserType == null) || browserType.equalsIgnoreCase("ie")){%>
      
      <div align="left" class="sidea">
      <div id="page_header_text"><strong>
      <% if("Premium".equalsIgnoreCase(userType)){%>
      <dm:I18NDisplayString component="landing_pages" dictionary="ui_strings" key="install_x_control_page.text.ie4" userType="Premium"/>
      <%} else{ %>
       <dm:I18NDisplayString component="landing_pages" dictionary="ui_strings" key="install_x_control_page.text.ie4" userType="free"/>     
      <%} %>
      </strong></div>
      
      
      
                <DIV id="sidebarContent"><br />
                  <div id="page_message_text_1">
                  <% if("Premium".equalsIgnoreCase(userType)){%>
                  <dm:I18NDisplayString component="landing_pages" dictionary="ui_strings" key="install_x_control_page.text.ie5" userType="Premium"/>
                  <%} else { %>
                  <dm:I18NDisplayString component="landing_pages" dictionary="ui_strings" key="install_x_control_page.text.ie5" userType="free"/>                 
                  <%} %> 
                  <br />
                  
      		</div>
                  <br />
      		<br />
      
                  <div id="page_message_text_2">
                  <% if("Premium".equalsIgnoreCase(userType)){%>
                  <dm:I18NDisplayString component="landing_pages" dictionary="ui_strings" key="install_x_control_page.text.ie6" userType="Premium"/>
                  <%} else { %>
                   <dm:I18NDisplayString component="landing_pages" dictionary="ui_strings" key="install_x_control_page.text.ie6" userType="free"/>              
                  <%} %>
                  </div>
      
      	<table width="100%" border="0" cellspacing="0" cellpadding="1" style="font-size:12px">
      	    <tr>
      	      <td>&nbsp;</td>
      	      <td>&nbsp;</td>
      	    </tr>
      	    <tr>
      	      <td width="12%"><strong><img src="images/01.png" width="31" height="31"></strong></td>
      	      <td width="88%"><div id="page_message_text_3"><strong>
      	      <% if("Premium".equalsIgnoreCase(userType)){%>
      	      <dm:I18NDisplayString component="landing_pages" dictionary="ui_strings" key="install_x_control_page.text.ie7" userType="Premium"/>
      	      <%} else { %>
      	      <dm:I18NDisplayString component="landing_pages" dictionary="ui_strings" key="install_x_control_page.text.ie7" userType="free"/>      	      
      	      <%} %>
      	      </strong> </div></td>
      	    </tr>
      	    <tr>
      	      <td colspan="2"><div id="page_message_text_4"><strong>
      	      <% if("Premium".equalsIgnoreCase(userType)){%>
      	      <dm:I18NDisplayString component="landing_pages" dictionary="ui_strings" key="install_x_control_page.text.ie8" userType="Premium"/>
      	      <%} else { %>
      	      <dm:I18NDisplayString component="landing_pages" dictionary="ui_strings" key="install_x_control_page.text.ie8" userType="free"/>      	      
      	      <%} %>
      	      </div></td>
      	      </tr>
      
      	    <tr>
      	      <td>&nbsp;</td>
      	      <td>&nbsp;</td>
      	    </tr>
      	    <tr>
      	      <td><strong><img src="images/02.png" alt="" width="31" height="31"></strong></td>
      	      <td><div id="page_message_text_5"><strong><strong>
      	      <% if("Premium".equalsIgnoreCase(userType)){%>
      	      <dm:I18NDisplayString component="landing_pages" dictionary="ui_strings" key="install_x_control_page.text.ie9" userType="Premium"/>
      	      <% } else {  %>
      	      <dm:I18NDisplayString component="landing_pages" dictionary="ui_strings" key="install_x_control_page.text.ie9" userType="free"/>      	      
      	      <%} %>
      	      </strong> </div></td>
      	    </tr>
      	    <tr>
      	      <td colspan="2"><div id="page_message_text_6"><strong>
      	      <% if("Premium".equalsIgnoreCase(userType)){%>
      	      <dm:I18NDisplayString component="landing_pages" dictionary="ui_strings" key="install_x_control_page.text.ie10" userType="Premium"/>
      	      <%}else {%>
      	      <dm:I18NDisplayString component="landing_pages" dictionary="ui_strings" key="install_x_control_page.text.ie10" userType="free"/>      	      
      	      <%} %>
      	      </div></td>
      	      </tr>
      	    <tr>
      	      <td>&nbsp;</td>
      	      <td>&nbsp;</td>
      	    </tr>
      	    <tr>
      	      <td><strong><img src="images/03.png" alt="" width="31" height="31"></strong></td>
      	      <td><div id="page_message_text_7"><strong>
      	      <% if("Premium".equalsIgnoreCase(userType)){%>
      	      <dm:I18NDisplayString component="landing_pages" dictionary="ui_strings" key="install_x_control_page.text.ie11" userType="Premium"/>
      	      <% } else{ %>
      	      <dm:I18NDisplayString component="landing_pages" dictionary="ui_strings" key="install_x_control_page.text.ie11" userType="free"/>      	      
      	      <%} %>
      	      </strong> </div></td>
      	    </tr>
      	    <tr>
      	      <td colspan="2"><div id="page_message_text_8">
      	      <% if("Premium".equalsIgnoreCase(userType)){%>
      	      <dm:I18NDisplayString component="landing_pages" dictionary="ui_strings" key="install_x_control_page.text.ie12" userType="Premium"/>
      	      <%} else { %>
      	      <dm:I18NDisplayString component="landing_pages" dictionary="ui_strings" key="install_x_control_page.text.ie12" userType="free"/>      	      
      	      <%} %>
      	      </div></td>
      	      </tr>
      	    <tr>
      	      <td colspan="2"><div id="page_message_text_9"><strong>
      	      <% if("Premium".equalsIgnoreCase(userType)){%>
      	      <dm:I18NDisplayString component="landing_pages" dictionary="ui_strings" key="install_x_control_page.text.ie13" userType="Premium"/>
      	      <%} else { %>
      	      <dm:I18NDisplayString component="landing_pages" dictionary="ui_strings" key="install_x_control_page.text.ie13" userType="free"/>
      	      <%} %>
      	      </strong></div><br></td>
      	      </tr>
      	  </table>
      	<div id="page_message_text_3"> </div>
              </div>
      </div>
      <%}%>
      </td>
      <td width="50" rowspan="3" align="right" valign="top"><img src="images/side.png" width="50" height="522" /></td>
    </tr>
</table>


</td>
</tr>    
<!--
	<script language="javascript" type="text/javascript" src="footer.js"></script>
	-->
</table>
</body>
</html>
