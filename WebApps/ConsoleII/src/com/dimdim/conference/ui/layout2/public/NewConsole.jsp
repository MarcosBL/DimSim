<% response.addHeader("Cache-Control","no-cache, nostore, private, max-age:2"); %>
<%@ taglib prefix="dm" uri="com.dimdim.conferenceConsole.tags" %>
<%@ taglib prefix="ww" uri="/webwork" %>
<%@ page import="com.dimdim.conference.ConferenceConsoleConstants" %>
<%@ page import="com.dimdim.conference.ConferenceConstants" %>
<%@ page import="com.dimdim.conference.model.IConference" %>
<%@ page import="com.dimdim.conference.application.UserSession" %>
<%@ page import="com.dimdim.conference.action.common.ActionRedirectData" %>
<%@ page import="com.dimdim.locale.LocaleResourceFile" %>
<%@ page import="com.dimdim.conference.config.UIParamsConfig" %>
<%@ page import="com.dimdim.util.session.UserRequest" %>
<%@ page import="com.dimdim.util.session.UserSessionDataManager" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%
long tm = System.currentTimeMillis();
//	System.out.println("Loading Console - Time:"+System.currentTimeMillis());
	String browserType = (String)request.getSession().getAttribute("BROWSER_TYPE");
	String osType = (String)request.getSession().getAttribute("OS_TYPE");
	String webappName = ConferenceConsoleConstants.getWebappName();
	String releaseDate = ConferenceConsoleConstants.getReleaseDate();
	String majorVersion = ConferenceConsoleConstants.getMajorVersion();
	String versionNumber = ConferenceConsoleConstants.getVersionNumber();
	String dmsServerAddress = ConferenceConsoleConstants.getDMServerAddress();
	String licenseTag = ConferenceConsoleConstants.getLicenseTag();
	String defTrackBackUrl = ConferenceConsoleConstants.getTrackbackURL();
	String premiumCatalogue = ConferenceConsoleConstants.getPremiumCatalogue();
	String productVersion = ConferenceConsoleConstants.getProductVersion();
	String conferenceName = "Welcome to Web Meeting";
	boolean	isPresenter = false;
	
	int pubMajorVersion = ConferenceConstants.PUBLISHER_CONTROL_MAJOR_VERSION;
	int pubMinorVersion = ConferenceConstants.PUBLISHER_CONTROL_MINOR_VERSION;
	
	//Enumeration enumNames = request.getSession().getAttributeNames();
	//int i = 1;
	//while(enumNames.hasMoreElements()){
	//	String obj = (String)enumNames.nextElement();
	//	System.out.println("index = "+i);
	//	System.out.println("name = "+ obj + "value = "+request.getSession().getAttribute(obj));
	//	
	//}

	String inPopup = (String)request.getParameter("inPopup");
	String reloadConsole = (String)request.getParameter("reloadConsole");

	String sessionKey = (String)request.getAttribute("sessionKey");
	String dataCacheId = (String)request.getAttribute("dataCacheId");
	String pubAvailable = (String)request.getAttribute("pubAvailable");
	String confKey = "";
	String joinUrl = "";
	String skinName = "skin1";
	String userType = ConferenceConsoleConstants.getUserTypeFreeOrPaid();
	


	String topPanelVisible = (String)UIParamsConfig.getUIParamsConfig().getUIParams().get("top_panel_visible");
	String topPanelClassName= "Show";
	if("false".equalsIgnoreCase(topPanelVisible))
	{
		topPanelClassName = "Hide";
	}
	
	//should use publisher_enabled value from user info will have to modify
//	System.out.println("in console.jsp publisher check enabled = :"+(String)request.getAttribute("publisher_enabled"));
	String pubEnabled = (String)request.getAttribute("publisher_enabled");
	//System.out.println("in console.jsp getting userType = :"+(String)request.getAttribute("userType"));
	//System.out.println("in console.jsp getting pubAvailable = :"+(String)request.getAttribute("pubAvailable"));
	//System.out.println("in console.jsp uri = :"+uri);
	boolean publicChat = true;
	userType = (String)request.getAttribute("userType");
	if(null == userType)
	{
	    userType = LocaleResourceFile.FREE;
	}
	if (sessionKey != null)
	{
		UserSession userSession = (UserSession)session.getAttribute(sessionKey);
		if (userSession == null)
		{
	response.sendRedirect("error.action");
		}
		else
		{
	String windowName = userSession.getConference().getConfig().getConferenceKey();
	if (windowName == null)
	{
		response.sendRedirect("error.action");
	}
	else
	{
		isPresenter = userSession.isPresenter();
request.getSession().removeAttribute(ActionRedirectData.SESSION_ATTRIBUTE_NAME);
		confKey = userSession.getConference().getConfig().getConferenceKey();
		UserRequest userRequest = UserSessionDataManager.getDataManager().getUserRequest(userSession.getUri());
		String portalMeetingName = userRequest.getMeetingName();
		System.out.println("Check for portal meeting name to be set to title " + portalMeetingName);
		if(portalMeetingName != null && portalMeetingName.length() > 0)
			conferenceName = portalMeetingName;
		publicChat = userSession.getConference().isPublicChatEnabled();
		joinUrl = userSession.getConference().getJoinUrlEncoded();
	}
		}
	}
	else
	{
	}
	String chatpodClass = "Show";
	if(!publicChat)
	{
		chatpodClass = "Hide";
	}
%>
<%@page import="java.util.Enumeration"%>
<html>
	<head>
		<dm:LocaleEncode/>
		<script type="text/javascript" src="dms_interface.js"></script>
	
		<link href="css/style.css" rel="stylesheet" type="text/css">
		<link href="skin1/styles.css" rel="stylesheet" type="text/css">
		
		<title><%=conferenceName%></title>
		<script language='javascript' src='flash_bridge.js'></script>
		<script language='javascript' src='support_functions.js'></script>
		
		<script langauage='javascript'>
		   	window.page_name = "console";
		   	window.in_popup = "<%=inPopup%>";
		   	window.reloadConsole = "<%=reloadConsole%>";
		   	window.web_app_name = "<%=webappName%>";
		   	window.conf_key = "<%=confKey%>";
		   	window.userType = "<%=userType%>";
		   	window.data_cache_id = "<%=dataCacheId%>";
			window.os_type = "<%=osType%>";
		   	window.trackback_url = "<%=defTrackBackUrl%>";
		   	window.browser_type = "<%=browserType%>";
		   	window.pubEnabled = "<%=pubEnabled%>";
		   	window.pubAvailable = "<%=pubAvailable%>";
		   	var major = <%=pubMajorVersion%>;
			var minor = <%=pubMinorVersion%>;
			window.pubVersion = major+"."+minor;
		   	window.premiumCatalogue = "<%=premiumCatalogue%>";
		   	window.productVersion = "<%=productVersion%>";
		   	window.popout_json_data_array_limit = 24;
		</script>
		<script language="javascript" src="/<%=webappName%>/js/base64.js"></script>
		<script language="javascript" src="/<%=webappName%>/js/popout_support.js"></script>
		
		<script src="client_guid.js" type="text/javascript"></script>
		<!-- <script src="tools_metadata.js" type="text/javascript"></script>
		<script src="ui_resources.js" type="text/javascript"></script> -->
		<script language="javascript">
			function btns_hover(btn_id)
			{
//				alert(btn_id);
				document.getElementById(btn_id).className = 'leftpod_btns_hover';
			}			
	
			function btns_return(btn_id)
			{
//				alert(btn_id);
				document.getElementById(btn_id).className = 'leftpod_btns';
			}			
			
			
			function callFromFlash(streamName,eventName)
			{
				JSInterface.callGWT2('PPT_EVENT',streamName,eventName);
			}
			
			function logMessageFromFlash(streamName,msg)
			{
				JSInterface.callGWT2('PPT_EVENT','FLASH_MESSAGE_LOG',streamName+"::"+msg);
			}
			
			getAGuid = function()
			{
				return	guid6Parts();
			}
			
			function  onCallbackFromFlash(movieListenerId,movie_event)			
			{
				JSInterface.callGWT(movieListenerId,movie_event);
			}
			
			function  setUrlToTopWindow(url)			
			{
				var topWindow = getTopWindow(window);
				topWindow.location = url;
			}
			
			function  getEscapedUrl(url)			
			{
				//alert('got input '+url);
				//alert('after escaping '+unescape(url));
				return unescape(url);
			}
			
			closeMeetingOnHostNavigation = function()
			{
				return false;
			}
			openDimdimWebSite = function()
			{
				window.open("http://www.dimdim.com");
			}
		</script>
</head>
<body>
<div id="pre-loader" align="center"><br>
<br>
<br>
<img src="images/ajax-loader.gif">
<br>
<br>
<br>
<strong style="font-size:18px;"><dm:I18NDisplayString component="console" dictionary="ui_strings" key="loading.message"/></strong>
</div>
<script language="javascript" src="../layout2/com.dimdim.conference.ui.layout2.Layout2.nocache.js"></script>
<% 
//System.out.println("from console jsp...publisher enabled = "+pubEnabled);
//System.out.println("from console jsp...isPresenter = "+isPresenter);
if ("true".equalsIgnoreCase(pubEnabled) && "true".equalsIgnoreCase(pubAvailable)) { 
	//System.out.println("from console jsp...osType = "+osType);
	//System.out.println("from console jsp...browserType = "+browserType);
	if("windows".equalsIgnoreCase(osType))
	{
		if (browserType == null || browserType.equalsIgnoreCase("ie")) {
			//System.out.println("* ********** Opening console in IE"); 
%>
		<object id="dimdimPublisherControl1"  WIDTH=0 HEIGHT=0 classid="CLSID:5100F713-1B48-4A6B-9985-EDDFB7F1C0DF">
    	</object>
	<% 	} else if(browserType.equalsIgnoreCase("firefox")){
		//System.out.println("* ********** Opening console in Firefox"); 
	%>
			<embed id="DimdimDesktopPublisher1" type="application/npdimdim4"  width=0 height=0>
			</embed>
	<% 	} 
	} else if("mac".equalsIgnoreCase(osType)){%>
				<embed id="DimdimDesktopPublisherMac" type="application/wkdimdim" width=1 height=1 hidden="true">
				</embed>
<% 			} 
}
%>
<div id="content_body" class="Hide">
<div id="console_background" class="console-background">
  <div id="background_header" class="background-header">&nbsp;</div>
  <div id="background_middle_1" class="background-middle"></div>
  <div id="logo-image" class="logo-image" class="background-middle"></div>
  <div id="background_middle_2" class="background-middle"></div>
  <div id="background_footer" class="background-footer">&nbsp;</div>
</div>
<div id="MainConsole" class="console-content">
 <table width="100%" border="0" cellspacing="0" cellpadding="0" id="top-panel-table">
  <tr>
   <td width="5px" valign="top"></td>
   <td valign="middle" align="left"><div id="logo_text" class="logo-text"></div></td>
   <td valign="top" align="right">
     <table align="right" class="console-top-panel">
      <tr>
       <td id="meeting_id_block" class="meeting-id-block">
        <table  border="0" cellspacing="0" cellpadding="0">
         <tr>
		  <td align="left"><div id="meetingid_label" class="meetingid-label"></div></td>
		  <td align="left"><div id="meetingid_seperator" class="meetingid-seperator">:</div></td>
		  <!--<td align="left"><div id="meetingid_text" class="meetingid-text"></div></td> -->
		  <td align="left"><div id="copyText" class="meetingid-text"></div></td> 
         </tr>
        </table>
       </td>
       <td id="phone_number_block" class="phone-number-block">
        <table border="0" cellspacing="0" cellpadding="0">
         <tr>
	      <td align="left"><div id="phone_number_label" class="phone-number-label"></div></td>
            <td align="left"><div id="phone_number_seperator" class="phone-number-seperator">:</div></td>
	      <td align="left"><div id="phone_number_text" class="phone-number-text"></div></td>
	      <td align="left"><div id="passcode_label" class="passcode-label"></div></td>
            <td align="left"><div id="passcode_seperator" class="passcode-seperator">:</div></td>
	      <td align="left"><div id="passcode_text" class="passcode-text"></div></td>
	     </tr>
	    </table>
	   </td>
	   <td align="center" id="notifications_image" class="Hide"><div class="notifications_icon"></div><div id="notifications"></div></td>
       <td align="left"><div id="tools_button"></div></td>
	 <td id="clock_and_signout_block" width="260px" align="right">
	  <table>
	    <tr>
	   <td id="clock-image"><img src="images/time.png" alt="Time : "/></td>
         <td><div id="time_meeting"></div></td>
	   <td width="3"></td>
         <td align="right"><div id="signout_link"></div></td>
	    </tr>
	  </table
	 </td>
      </tr>
     </table>
   </td>
  </tr>
 </table>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
 <tr>
  <td id="left_column" valign="top" align="left">
   <div id="left_column_div" class="floating">
    <div id="main_showitems" class="Hide">
     <div class="leftpod_top">
      <div id="show_items"></div>
      <div id="show_items_navigation" class="navigation_arrows"></div>
     </div>
     <div class="leftpod_middle" id="leftpod_middle_items"></div>
     <div class="leftpod_bottom">
	  <table width="100%" border="0" cellpadding="1" cellspacing="0">
	   <tr>
		<td class="Hide">
		 <div class="leftpod_btns" id="items_share_btn" onMouseOver="javascript:btns_hover('items_share_btn');" onMouseOut="javascript:btns_return('items_share_btn');">
		  <div id="share_button_wrapper"></div>
		 </div>
		</td>
		<td>&nbsp;</td>
		<td align="right" class="Hide">
		 <div class="leftpod_btns" id="items_manage_btn" onMouseOver="javascript:btns_hover('items_manage_btn');" onMouseOut="javascript:btns_return('items_manage_btn');">
		  <div id="items_manage"></div>
		 </div>
		</td>
	   </tr>
	  </table>
	 </div>
	 <br>
	</div>
	<div id="main_participants">
	 <div class="leftpod_top">
	  <div id="participants"></div>
	  <div id="participants_navigation" class="navigation_arrows"></div>
	 </div>
	 <div class="leftpod_middle" id="leftpod_middle_users"></div>
	 <div class="leftpod_bottom">
	  <table width="100%" border="0" cellpadding="1" cellspacing="0">
	   <tr>
		<td>
		 <div class="leftpod_btns" id="users_invite_btn" onMouseOver="javascript:btns_hover('users_invite_btn');" onMouseOut="javascript:btns_return('users_invite_btn');">
		  <div id="users_invite"></div>
    	 </div>
    	</td>
		<td align="right">
		 <div class="leftpod_btns" id="participants_manage_btn" onMouseOver="javascript:btns_hover('participants_manage_btn');" onMouseOut="javascript:btns_return('participants_manage_btn');">
		  <div id="participants_manage"></div>
    	 </div>
    	</td>
	   </tr>
	  </table>
	 </div>
	</div>
   </div>
  </td>
  <td align="center" width="5px" style="font-size:5px;">&nbsp;</td>
<% if (browserType.equalsIgnoreCase("ie")) { %>
  <td align="center"><img src="skin1/images/3x2.png" alt=""/></td>
<% } %>
  <td valign="top" align="center" width="100%" style="margin-left:3px;margin-right:3px;">
   <table width="100%" border="0" cellpadding="0" cellspacing="0">
	<tr>
	 <td>
	  <table width="100%" border="0" cellpadding="0" cellspacing="0">
	   <tr>
	 <td width="3"><img src="images/top_leftt_corner.png" /></td>
	 <td align="left" valign="middle" background="images/top_tile.png">                
	  <table border="0" cellspacing="0" cellpadding="0" width="100%">
	   <tr>
		<td align="left" width="90px"><div id="now_sharing" align="left"><strong></strong></div><td>
		<td align="left"><div align="left" id="res_showing"></div></td>
		<td align="right"><div align="right" id="co_browse"></div></td>
	   </tr>
	  </table>
	 </td>
	 <td align="right" valign="middle" background="images/top_tile.png">
	  <table border="0" cellspacing="1" cellpadding="1" id="middle_top_btns">
	   <tr >
		<td align="right"><div id="record_wrapper" ></div></td>
		<td align="right" width="10"></td>
		<td align="right"><div id="full_screen_wrapper" ></div></td>
		<td align="right" width="10"></td>
		<td align="right"><div id="show_chat_wrapper"></div></td>
	   </tr>
	  </table>
	 </td>
	 <td width="3"><img src="images/top_right_corner.png"/></td>
	   </tr>
	  </table>
	 </td>
	</tr>
	<tr>
	  <td>
	    <table width="100%" border="0" cellpadding="0" cellspacing="0">
		<tr>
	 <td width="2px" bgcolor="#C3D9FF">&nbsp;</td>
	 <td align="center"><div id="collab_area"></div></td>
	 <td width="2px" bgcolor="#C3D9FF">&nbsp;</td>
		</tr>
	    </table>
	  </td>
	</tr>
	<tr>
	 <td>
	  <table width="100%" border="0" cellpadding="0" cellspacing="0">
	   <tr>
	 <td style="line-height:9px; font-size:4px;" width="3"><img src="images/bottom_leftcorner.png" alt=""/></td>
	 <td style="line-height:9px; font-size:4px;" align="left" valign="middle" background="images/bottom_tile.png">&nbsp;</td>
	 <td style="line-height:9px; font-size:4px;" width="3"><img src="images/bottom_rightcorner.png" alt=""/></td>
	   </tr>
	  </table>
	 </td>
	</tr>
   </table>
  </td>
  <td align="center" width="5px" style="font-size:5px;">&nbsp;</td>
<!--  <td align="center"><img src="skin1/images/3x2.png" alt=""/></td> -->
  <td align="center" valign="top" id="main_chat_td" class="<%=chatpodClass%>">
   <div id="right_column_div" class="floating">
   <table border="0" cellpadding="0" cellspacing="0" width="100%">
    <tr>
	 <td>
	  <table width="100%" border="0" cellpadding="0" cellspacing="0">
	   <tr>
	 <td width="3"><img src="images/top_leftt_corner.png" alt=""/></td>
	 <td align="left" valign="middle" background="images/top_tile.png"><div id="chat_pod_header"></div></td>
<!--	 <td width="3"><img src="images/top_right_corner.png" alt=""/></td> -->
	   </tr>
	  </table>
	 </td>
    </tr>
    <tr>
	 <td>
	  <table border="0" cellpadding="0" cellspacing="0">
	   <tr>
	 <td width="2px" bgcolor="#C3D9FF">&nbsp;</td>
	 <td align="center" valign="top"><div id="chat_pod_content"></div></td>
<!--	 <td width="2px" bgcolor="#C3D9FF">&nbsp;</td> -->
	   </tr>
	  </table>
	 </td>
    </tr>
    <tr>
	 <td>
	  <table width="100%" border="0" cellpadding="0" cellspacing="0">
	   <tr>
	 <td style="line-height:9px; font-size:4px;" width="3"><img src="images/bottom_leftcorner.png" alt=""/></td>
	 <td style="line-height:9px; font-size:4px;" align="left" valign="middle" background="images/bottom_tile.png">&nbsp;</td>
<!--	 <td style="line-height:9px; font-size:4px;" width="3"><img src="images/bottom_rightcorner.png" alt=""/></td> -->
	   </tr>
	  </table>
	 </td>
    </tr>
   </table>
   </div>
  </td>
 </tr>
</table>
</div>
</div>

<script type="text/javascript">

var dv10 = new SWFObject("clipbd_10.swf", "movie", "60", "17", "7", "#355fab");
	dv10.addParam("allowScriptAccess", "always");
	dv10.addVariable("roomName", window.conf_key);
	dv10.addVariable("meetingUrl", "<%=joinUrl%>");
	dv10.write("copyText");

</script>
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
</body>
	<head>
	<META HTTP-EQUIV="Pragma" CONTENT="no-cache"/>
	<META HTTP-EQUIV="Expires" CONTENT="-1"/>
	</head>
<%
//	System.out.println("Console loading complete - Time:"+System.currentTimeMillis());
%>
</html>
