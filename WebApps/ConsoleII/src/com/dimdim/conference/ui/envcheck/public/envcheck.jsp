<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page import="java.util.*" %>
<%
	response.addHeader("Pragma", "no-cache"); 
	response.addHeader("Expires", "-1"); 
	response.addDateHeader("Last-Modified", (new Date()).getTime()); 
%>
<%@ page import="com.dimdim.util.misc.StringGenerator" %>
<%@ page import="com.dimdim.conference.ConferenceConsoleConstants" %>
<%@ page import="com.dimdim.conference.UtilMethods" %>
<%@ page import="com.dimdim.conference.ConferenceConstants" %>

<%@ page import="com.dimdim.util.session.UserRequest" %>
<%@ page import="com.dimdim.util.session.MeetingSettings" %>
<%@ page import="com.dimdim.util.session.UserSessionDataManager" %>

<%@ page import="com.dimdim.conference.action.common.ActionRedirectData" %>
<%@ taglib prefix="dm" uri="com.dimdim.conferenceConsole.tags" %>
<%@ page import="com.dimdim.locale.LocaleResourceFile" %>
<%@ page import="com.dimdim.conference.application.presentation.dms.URLHelper" %>

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
	String userType = ConferenceConsoleConstants.getUserTypeFreeOrPaid();
	String startMeetingOptionAV = ConferenceConsoleConstants.getResourceKeyValue("start_meeting_option_av","1");
	String enableAudioOption = ConferenceConsoleConstants.getResourceKeyValue("start_meeting_option_enable_audio","1");
	String enableAVOption = ConferenceConsoleConstants.getResourceKeyValue("start_meeting_option_enable_av","1");
	//String logoImageUrl = ConferenceConsoleConstants.getResourceKeyValue("start_page_logo_image_url","images/dimdim-logo.png");
	String logoImageWidth = ConferenceConsoleConstants.getResourceKeyValue("start_page_logo_image_width","134");
	String logoImageHeight = ConferenceConsoleConstants.getResourceKeyValue("start_page_logo_image_height","63");
	String licenseTag = ConferenceConsoleConstants.getLicenseTag();	
	
	String pubEnabled = UtilMethods.isPublisherSupportable(osType,browserType,browserVersion)+"";
	
//	MeetingOptions meetingOptions = (MeetingOptions)request.getSession().getAttribute(MeetingOptions.MEETING_OPTIONS_KEY);
//	if (meetingOptions != null)
//	{
//		if (pubEnabled.equalsIgnoreCase("true"))
//		{
//			//	This means that the publisher is supportable, however user may still may
//			//	not be using the publisher. Check the meeting options.
//			pubEnabled = meetingOptions.isScreenShareEnabled()+"";
//		}
//	}
	
	System.out.println("Browser type server knows is:"+browserType);
//	System.out.println("in signin.jsp publisher check enabled = :"+(String)request.getSession().getAttribute("publisher_enabled"));
	String webappName = ConferenceConsoleConstants.getWebappName();
	String releaseDate = ConferenceConsoleConstants.getReleaseDate();
	String majorVersion = ConferenceConsoleConstants.getMajorVersion();
	String versionNumber = ConferenceConsoleConstants.getVersionNumber();
	String serverName = ConferenceConsoleConstants.getServerAddress();
	String serverPort = ConferenceConsoleConstants.getServerPortNumber();
	String actionId = (String)request.getParameter("action");
	String skipAll = (String)request.getParameter("skipAllChecks");
	String uri = (String)request.getParameter("uri");
	String pubInstall = (String)request.getParameter("installPub");
	String confKey = (String)request.getParameter("confKey");
	String userId = (String)request.getParameter("userId");
	
	System.out.println("actionId:"+actionId);
	System.out.println("skipAll:"+skipAll);
	
//	String portalCall = "false";
	
	int maxAttendees = ConferenceConsoleConstants.getMaxParticipantsPerConference();
	int minAttendees = ConferenceConsoleConstants.getMinParticipantsPerConference();
	if (actionId == null)
	{
		actionId = (String)request.getAttribute("action");
	}
	String success_url = (String)request.getAttribute(UserRequest.MEETING_CONNECT_CONTINUE_URL);
	if (uri != null)
	{
		UserRequest ur = UserSessionDataManager.getDataManager().getUserRequest(uri);
//		UserRequest ur = PortalInterface.getPortalInterface().getUserRequest(uri);
		if (ur != null)
		{
			actionId = ur.getAction();
//			portalCall = "true";
			pubEnabled = UtilMethods.isPublisherSupportable(osType,browserType,browserVersion)+"";
			MeetingSettings settings = ur.getMeetingSettings();
			userType = ur.getUserInfo().getUserType();
			if (settings != null)
			{
				pubEnabled = settings.isFeaturePublisher()+"";
			}
			confKey = ur.getConfKey();
			userId = ur.getUserInfo().getUserId();

		}
		else
		{
			System.out.println("Invalid user request id:"+uri);
		}
	}
	
	String customLogo = URLHelper.getLogoUrl(confKey, userId);
	String logoImageUrl = customLogo;
	System.out.println("logoImageUrl is : " + logoImageUrl);

	
	String actionName = "Start Meeting";
	if (actionId != null && actionId.equals("join"))
	{
		actionName = "Join Meeting";
	}
	StringGenerator idGen = new StringGenerator();
	String newKey = idGen.generateRandomString(7,7);
	String cacheBuster = idGen.generateRandomString(40,40);
	int pubMajorVersion = ConferenceConstants.PUBLISHER_CONTROL_MAJOR_VERSION;
	int pubMinorVersion = ConferenceConstants.PUBLISHER_CONTROL_MINOR_VERSION;
	int pubBuildVersion = ConferenceConstants.PUBLISHER_CONTROL_BUILD_VERSION;
	int pubBuildVersion2 = ConferenceConstants.PUBLISHER_CONTROL_BUILD_VERSION_2;	
	
	System.out.println("Continue on success url is:"+success_url);
	String standAloneCheck = (String)request.getAttribute("STAND_ALONE_CHECK");
%>
<html>
<head>	
<dm:LocaleEncode/>
		<!-- 
		<link href="EnvChecks.css" rel="stylesheet" type="text/css">
		<link href="EnvChecks2.css" rel="stylesheet" type="text/css">
		-->
		<!--[if lt IE 7.]>
			<script defer type="text/javascript" src="pngfix.js"></script>
		<![endif]-->
		<link href="style_portal.css"rel="stylesheet" type="text/css" />

  <style type="text/css">@import url(/<%=webappName%>/css/calendar-win2k-1.css);</style>
  <script type="text/javascript" src="/<%=webappName%>/js/calendar.js"></script>
  <script language="JavaScript1.2" type="text/javascript" src="/<%=webappName%>/html/browser_check.js"></script>
  <script language="JavaScript1.2" type="text/javascript" src="swfobject.js"></script>
	<script language="JavaScript1.2" type="text/javascript" src="support_functions.js"></script>
	<script language="JavaScript1.2" type="text/javascript" src="xpi_install_support.js"></script>
	<script language="JavaScript1.2" type="text/javascript" src="/<%=webappName%>/js/client_guid.js"></script>
		<dm:SignInFormsInfo/>
		<script type="text/javascript">
			
			createAndWriteMovieElement = function(element, movieUrl, id2, height, width, version, color)
			{
				var d = new Date();
				var t = d.getTime();
				var so = new SWFObject(movieUrl+"?time="+t, id2, height, width, version, color, true);
//				so.addParam("WMODE","TRANSPARENT");
				so.write(element);
			}
			
			getAGuid = function()
			{
				return	guid6Parts();
			}
			
			reloadWindow = function(url)
			{
				//alert('inside lod url = '+url)
				window.location.href = url+"&cb=<%=newKey%>";
			}
			
			loadURL = function(url)
			{
				//window.location.href = url;
				//alert("window.parent.frames['dimdim_iframe'] = "+window.parent.frames["dimdim_iframe"]);
				//alert("document.getElementById('dimdim_iframe') = "+document.getElementById('dimdim_iframe'));
				//document.getElementById('dimdim_iframe').src = url;
				//frames['iframe_name'].location.href = 'newpage.html';
				//alert('inside load url = '+url);
				<%if (standAloneCheck != null && standAloneCheck.equals("true")) { %>
				window.location.href = "<%=success_url%>";
				<% } else { %>
				window.location.href = url;
				//window.parent.frames["dimdim_iframe"].src = url;
				<% } %>
			}
			
			getCurrentURL = function()
			{
				return window.location.href;
			}
			
		</script>
		<% if("Premium".equalsIgnoreCase(userType)){%>
		<dm:DictionaryObject component="forms" dictionary="ui_strings" userType="Premium"/>
		<dm:DictionaryObject component="forms" dictionary="tooltips" userType="Premium"/>
		<dm:DictionaryObject component="landing_pages" dictionary="ui_strings" userType="Premium"/>
		<%}else{%>
		<dm:DictionaryObject component="forms" dictionary="ui_strings" userType="free"/>
		<dm:DictionaryObject component="forms" dictionary="tooltips" userType="free"/>
		<dm:DictionaryObject component="landing_pages" dictionary="ui_strings" userType="free"/>
		
		<%} %>
		<script langauage='javascript'>
		
			window.webapp_name = "<%=webappName%>";
			window.page_name = "envcheck";
			window.action_id = "<%=actionId%>";
			window.skip_all = "<%=skipAll%>";
			window.conf_key = "<%=confKey%>";
			window.uri = "<%=uri%>";
			window.installPub = "<%=pubInstall%>";
			
			window.browser_type = "<%=browserType%>";
			window.browserVersion = "<%=browserVersion%>";
			window.os_type = "<%=osType%>";
			window.success_url = "<%=success_url%>";
			window.download_bandwidth = -1;
			window.upload_bandwidth = -1;
			
			window.required_publisher_version = "<%=pubMajorVersion%>.<%=pubMinorVersion%>.<%=pubBuildVersion%>.<%=pubBuildVersion2%>";
			window.publisher_availability = "unknown";
			window.publisher_available = "unknown";
			window.publisher_available_version = "unknown";
			window.flash_player_available = "unknown";
			
			window.publisher_majorversion = "<%=pubMajorVersion%>";
			window.publisher_minorversion = "<%=pubMinorVersion%>";
			window.publisher_buildversion = "<%=pubBuildVersion%>";
			window.publisher_build2version = "<%=pubBuildVersion2%>";
			
			window.server_name = "<%=serverName%>";
			window.server_port = "<%=serverPort%>";
			
			window.start_meeting_enable_publisher = "<%=pubEnabled%>";
			
			window.logo_image_url = "<%=logoImageUrl%>";
			window.logo_image_width = "<%=logoImageWidth%>";
			window.logo_image_height = "<%=logoImageHeight%>";			
			
function	requiredFlashPlayerVersionAvailable(status)
{
	window.flash_player_available = status;
}

function	skipFlashCheck()
{
	window.flash_player_available = "skip";
}

var originalURL = window.location.href;;

			function	pageLoadCompleted()
			{
				originalURL = window.location.href;
				var screenHeight = screen.availHeight;
				var screenWidth = screen.availWidth;
				//window.moveTo(0,0);
				//window.resizeTo(screenWidth,screenHeight);
				//continuePostInstallWork();			
			}
			
		</script>
<title>Environment check page</title>

<script language="javascript">

function changeSessionLocale()
{
	var lcSelect = document.getElementById("localeCode");
	var lc = lcSelect[lcSelect.selectedIndex].value;
	var v = guid6Parts();
	window.location = "SetSessionLocale.action?localeCode="+lc+"&cflag="+v;
}

</script>

<script type="text/javascript">

<% if (browserType != null && browserType.equalsIgnoreCase("firefox")) { %>
/*  This function returns an interger with values as:
	1 = no control exists.
	2 = control exists but is lower version
	3 = control exists and is required version or higher
alert("1"+version);
alert("2");
alert("3");
alert("4");
alert(document.DimdimDesktopPublisher1.getVersion());
	alert("Exception");
*/

//function getXControlVersion(version)
//{
//	var ret = 1;
//   try
//   {
//	if ( document.embeds["DimdimDesktopPublisher1"] != null)
//	{
//		ret = 2;
//		window.parent.publisher_available_version = ""+document.DimdimDesktopPublisher1.getVersion();
//		if ( document.DimdimDesktopPublisher1.getVersion() >= version )
//		{
//			ret = 3;
//		}
//		else
//		{
//		}
//	}
//   }
//   catch(e)
//   {
//   }
//  return ret;
//} 
<% } %>



function continuePostInstallWork()
{
//alert("inside continuePostInstallWork...");
	var	major = <%=pubMajorVersion%>;
	var minor = <%=pubMinorVersion%>;
	var build = <%=pubBuildVersion%>;
	var build2 = <%=pubBuildVersion2%>;
	var version = ( build2 % 100 + (build % 100) * 100 + (minor % 100) * 10000 + (major % 100) * 1000000 );
<% 

System.out.println(" in side continue post install work.. pubEnabled = "+pubEnabled);
System.out.println(" in side continue post install work.. browserType = "+browserType);
if ("true".equalsIgnoreCase(pubEnabled) )
{
	if("windows".equalsIgnoreCase(osType))
	{
		if( browserType != null && (browserType.equalsIgnoreCase("ie") || browserType.equalsIgnoreCase("firefox"))) { 
%>
		

	<%   if (browserType.equalsIgnoreCase("ie")) { %>
			if ( DimdimPublisherControl != null && DimdimPublisherControl.getVersion!=null )
			{
				window.publisher_availability = "available";
				window.publisher_available = "true";
			} else 
			{
				window.publisher_availability = "not_available";

			}
	
	<%   } else { /* Firefox way of checking the control existance and version */ %>
			var xControlVersion = checkFirefoxDimdimPublisher(major,minor,build,build2);
			if (xControlVersion == 1)
			{
				window.publisher_availability = "not_available";
			}
			else if (xControlVersion == 2)
			{
				window.publisher_availability = "lower_version_available";
			}
			else if (xControlVersion == 3)
			{
				window.publisher_availability = "available";
				window.publisher_available = "true";
			}
	<%   } %>
	<% }
	}else if("mac".equalsIgnoreCase(osType))
	{%>
		var xControlVersion = checkMacDimdimPublisher(major,minor,build,build2);
			if (xControlVersion == 1)
			{
				window.publisher_availability = "not_available";
			}
			else if (xControlVersion == 2)
			{
				window.publisher_availability = "lower_version_available";
			}
			else if (xControlVersion == 3)
			{
				window.publisher_availability = "available";
				window.publisher_available = "true";
			}	
<%	}
}

%>

//alert('window.parent.publisher_availability = ' +window.parent.publisher_availability);
//alert('window.parent.publisher_available = ' +window.parent.publisher_available);
//alert('window.publisher_availability = ' +window.publisher_availability);
//alert('window.publisher_available = ' +window.publisher_available);
}

 <% if ("true".equalsIgnoreCase(pubEnabled)){ %>
		var installFirefoxPublisher = function()
		{
			InstallTrigger.install({"Dimdim Web Meeting Publisher" : "/<%=webappName%>/jsp/activex/<%=pubMajorVersion%><%=pubMinorVersion%><%=pubBuildVersion%><%=pubBuildVersion2%>dimdim.xpi"});
		}
<%   } %>
		
</script>

</head>
<body>
<% 
	if("true".equalsIgnoreCase(pubEnabled)){
		if (browserType == null || browserType.equalsIgnoreCase("ie")) { %>
	<OBJECT ID="DimdimPublisherControl" WIDTH=0 HEIGHT=0
		   CLASSID="CLSID:5100F713-1B48-4A6B-9985-EDDFB7F1C0DF">		   
	</OBJECT>
	
	<% } else { %>
	<% /*   <object id="DimdimDesktopPublisher1"
		type="application/x-xpinstall"
	    width=0 height=0> 
         <embed id="DimdimDesktopPublisher1"
            type="application/x-xpinstall"
            width=0 height=0>
         </embed>
      </object> */ %>
	
	<% } 
	}//end of pub enabled if%>
<NOSCRIPT>
<p>You need to have certain Javascript/Active-X settings enabled in order to use Dimdim Meeting</p>

<p>To enable these settings in Internet Explorer 5.5 or later please follow either
of the below approach:</p>

<p><b>1) Make the Default Security Setting to Medium:</b></p>
<ul>
<p>- In menu select <i>Tools</i>, then click <i>Internet Options</i>.</p>
<p>- Select the <i>Security</i> tab.</p>
<p>- Select the <i>Default Level - Medium</i>.</p>
</ul>
<p><b>OR</b></p>

<p><b>2) Add the Dimdim Meeting URL as Trusted Site:</b></p>
<ul>
<p>- In menu select <i>Tools</i>, then click <i>Internet Options</i>.</p>
<p>- Select the <i>Security</i> tab. then click <i>Trusted sites</i>.</p>
<p>- Click on <i>Sites</i>i button and add the Dimdim Meeting URL i.e. http://dimdimconference.com.</p>
<p>Note: Please uncheck the "Require server verification (https://) for all sites in the zone</p>

</ul>
<p><b>After enabling these settings simply refresh the page</b></p>
</NOSCRIPT>
<%
	/*	If its a live conference command include the flash tag. */
	/*	These divs can be arranged in any way. They are in a table here just for testing. */
%>
	<table width="1004" border="0" cellspacing="0" cellpadding="0" align="center">
	<tr>    <td align="center">
	<table width="982" border="0" cellspacing="0" cellpadding="0">  
	<tr>    
	<td width="150" align="center">
	<div><a href="http://www.dimdim.com/"><img src="<%=logoImageUrl%>" border=0 width="138" height="58"><div id="logolink"><br />        <br/>  <br /></div></a></div></td>        
	<td align="right" valign="top"><div id="form"></div></td></tr></table>
	</td></tr>
	<tr>
	<td>
	<table width="986" border="0" align="center">
	  <tr>
	    <td width="98" rowspan="2" align="left" valign="top">
	      <img src="images/sidea.png" width="98" height="522px">    </td>
	   <td align="center" valign="top"><br />
	      <div id="chechking_process" style="margin-left:30px;">
	      <div id="envchk_top">
	      <div id="check_header_text" align="left"></div>
	      <div id="check_message_text_1" align="left"></div>
	      </div>
	      
	
	      <div id="envchk_content">
	      
		<div class="checking_boxes" id="os_div">
	        	<table width="95%" border="0" cellspacing="0" cellpadding="2">
	          	<tr>
	            	<td width="70" valign="top" align="left"><br />
	              		<img src="images/meeting-icon.png"/></td>
	            	<td width="266" valign="top" align="left">
	            	<strong class="details"><br />
	              		<div id="os_check_text"></div> <br />
	              	</strong>
			<div id="os_check_message"></div>            </td>
	            	<td width="53" align="center" valign="top"><br />              
	               	<div id="oscheck_mark_done" class=""></div>
            		</td>
	          	</tr>
	          	<!--<tr>
	           	 <td colspan="3" align="left" valign="top"><div id="os_check_imagetxt" class="console-label"> <b>Operating System Check </b></div></td>
	            	</tr> -->
	        	</table>
	      	</div>
	      
	      <div class="checking_boxes" id="browser_div">
	      	      <table width="95%" border="0" cellspacing="0" cellpadding="2">
	      	          <tr>
	      	            <td width="70" align="left" valign="top">
	      	
	      	            <br />
	      	            <img src="images/browser_icons.png" id="text1" title="browser"/></td>
	      	            <td width="266" align="left" valign="top">
	      	            <strong class="details"><br />
	      	              <div id="browser_check_text"></div><br />
	      	              </strong>
	      			<div id="browser_check_message"></div>            
	      		    </td>
	      		    
	      	            <td width="53" align="center" valign="top">
	      	            <br />
	      	            	<div id="browsercheck_mark_done" class=""></div>
            		    </td>
	      	          </tr>
	      	          <!--
	      	          <tr>
	      	            <td colspan="3" align="left" valign="top"><div id="browser_check_imagetxt" class="console-label"><b>Browser Support Check</b></div></td>
	      	            </tr>
	      	            -->
	      	        </table>
	      </div> 
	      <div class="checking_boxes"  id="flash_div">
	      <table width="95%" border="0" cellspacing="0" cellpadding="2">
	          <tr>
	            <td id="skip_flash_check" width="70" align="left" valign="top"><br />
	              <img src="images/checking_flash.png"/></td>
	            <td width="266" align="left" valign="top">
	            <strong class="details"><br />
	            <div id="flash_check_text"></div><br /></strong>
	<div id="flash_check_message" style="position: relative;z-index: 9998;"></div>            </td>
	            <td width="53" align="center" valign="top"><br />              
	               <div id="flashcheck_mark_done" class=""></div>
            		</td>
	          </tr>
	          <!--<tr>
	            <td colspan="3" align="left" valign="top"><div id="flashplayer_check_imagetxt" class="console-label"><b>Flash Player Version Check</b></div></td>
	            </tr> -->
	        </table>
	      </div>
	      
	      
	      <div class="Hide" id="plugin_div"><table width="95%" border="0" cellspacing="0" cellpadding="2">
	          <tr>
	            <td width="19%" align="left" valign="top"><br />
	              <img src="images/plugin_icon.png" /></td>
	            <td width="67%" align="left" valign="top"><strong class="details"><br />
	              <div id="plugin_check_text"></div><br /></strong>
		<div id="plugin_check_message"></div>            </td>
	            <td width="14%" align="center" valign="top"><br />              
	               <div id="plugincheck_mark_done" class=""></div>
            	</td>
	          </tr>
	          <!--<tr>
	            <td colspan="3" align="left" valign="top"><div id="browser_check_imagetxt" class="console-label"><b>Dimdim Plug-in Check</b></div></td>
	            </tr> -->
	        </table>
	      </div>
	      
	      </div><div id="envchk_bottom"></div>
	      </div>
	      
	      </td>
	    <td width="267" rowspan="2" align="right" valign="top">
	      <div align="left" class="sidea">
	<br />
	<br />
	<div id="description_box">
	
	<!-- <div id="page_header_text"><strong>Start a Web Meeting</strong></div>
	          <DIV id="sidebarContent"><br />
	            <div id="page_message_text_1">With Web Meeting you can show presentations, applications and desktop to any other person over the internet. You can chat, show your webcam and talk with others in the meeting. As a presenter only a browser plug in is needed while attendees do not need to install anything.<br />
	</div><br />
	<br />
	
	            <div id="page_message_text_2">To start a meeting, please supply an email and a display name for yourself. You will be able to invite others to the meeting from inside the meeting. You can also change the meeting key to something relevant like 'Sales Meeting'. A meeting can also be scheduled for a future date.</div>
	<div id="page_message_text_3"> </div>
	        </div>
	      </div> -->
	      
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
<% if ( (browserType != null) &&
		( browserType.equalsIgnoreCase("firefox")  || browserType.equalsIgnoreCase("safari") ) ) { %>
<div id="flashcontent2">.</div>
<script type="text/javascript">
try
{
	var so = new SWFObject("flash_version_check.swf", "mymovie2", "1px", "1px", "6", "#336699");
	so.write("flashcontent2");
}
catch(e)
{
	window.flash_player_available = "skip";
}
</script>
<% } %>
		
<%if (licenseTag.length() >0) { %>
<script type="text/javascript">
var pageTracker;
var gaJsHost = (("https:" == document.location.protocol) ? "https://ssl." : "http://www.");
document.write(unescape("%3Cscript src='" + gaJsHost + "google-analytics.com/ga.js' type='text/javascript'%3E%3C/script%3E"));
</script>
<script type="text/javascript">
var trackingSet = 0;
trackAjaxUrl = function(ajaxUrl)
{
  try
  {
    if (trackingSet == 0)
    {
	trackingSet = 1;
pageTracker = _gat._getTracker("<%=licenseTag%>");
pageTracker._initData();
pageTracker._trackPageview();
	trackingSet = 2;
	pageTracker._trackPageview(ajaxUrl);
    }
    else if (trackingSet == 2)
    {
	pageTracker._trackPageview(ajaxUrl);
    }
  }
  catch(eee2)
  {
  }
}
</script>
<% } else { %>
<script type="text/javascript">
trackAjaxUrl = function(ajaxUrl) {  }
</script>
<% } %>
<script >
//alert('before loading the gwt module..');
</script>
<script language="javascript" src="com.dimdim.conference.ui.envcheck.EnvCheck.nocache.js"></script>
<script >
//alert('at the end of page');
</script>
</body>
</html>
