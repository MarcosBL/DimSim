<% response.addHeader("Cache-Control","no-cache, nostore, private, max-age:20"); %>
<%@ taglib prefix="dm" uri="com.dimdim.conferenceConsole.tags" %>
<%@ page import="com.dimdim.conference.ConferenceConsoleConstants" %>
<%@ page import="com.dimdim.conference.application.UserSession" %>
<%@ page import="com.dimdim.util.session.UserInfo" %>
<%@ page import="com.dimdim.util.session.UserSessionDataManager" %>
<%@ page import="com.dimdim.util.session.UserRequest" %>
<%@ page import="com.dimdim.locale.LocaleResourceFile" %>
<%
	long tm = System.currentTimeMillis();
	String browserType = (String)request.getSession().getAttribute("BROWSER_TYPE");
	String webappName = ConferenceConsoleConstants.getWebappName();
	String licenseTag = ConferenceConsoleConstants.getLicenseTag();
	String sessionKey = (String)request.getAttribute("sessionKey");
	String confKey = "";
	String userType = ConferenceConsoleConstants.getUserTypeFreeOrPaid();
	boolean publicChat = true;
	UserSession userSession = (UserSession)session.getAttribute(sessionKey);
	String dataCacheId = userSession.getDataCacheId();
	
	if (userSession != null)
	{
		confKey = userSession.getConference().getConfig().getConferenceKey();
		publicChat = userSession.getConference().isPublicChatEnabled();
	}
	
	System.out.println("in workspace popout getting userType = :"+(String)request.getAttribute("userType"));
		userType = (String)request.getAttribute("userType");
		if(null == userType)
		{
		    userType = LocaleResourceFile.FREE;
	}
	System.out.println("in workspace popout userType:="+userType);
	String chatpodClass = "Show";
	if(!publicChat)
	{
		chatpodClass = "Hide";
	}
%>
<html>
	<head>
		<dm:LocaleEncode/>
		<script type="text/javascript" src="dms_interface.js"></script>
		<title>Workspace Full Page Popout</title>
		<link href="css/style.css" rel="stylesheet" type="text/css">
		<link href="skin1/styles.css" rel="stylesheet" type="text/css">

		<!-- 
		<link href="Common_II.css" rel="stylesheet" type="text/css">
		<link href="Console_II.css" rel="stylesheet" type="text/css">
		<link href="User_II.css" rel="stylesheet" type="text/css">
		<link href="Invitations_II.css" rel="stylesheet" type="text/css">
		<link href="Moods_II.css" rel="stylesheet" type="text/css">
		<link href="Lists_II.css" rel="stylesheet" type="text/css">
		<link href="Popups_II.css" rel="stylesheet" type="text/css">
		<link href="Chat_II.css" rel="stylesheet" type="text/css">
		<link href="Discuss_II.css" rel="stylesheet" type="text/css">
		<link href="Tabs_II.css" rel="stylesheet" type="text/css">
		-->
		<script language="javascript" src="/<%=webappName%>/js/base64.js"></script>
		<script language="javascript" src="/<%=webappName%>/js/popout_support.js"></script>
		<script langauage='javascript'>
		   window.page_name = "workspace_popout";
		   window.in_popup = "true";
		   window.web_app_name = "<%=webappName%>";
		   window.conf_key = "<%=confKey%>";
		   window.userType = "<%=userType%>";
		   window.current_slide = "";
		   window.data_cache_id = "<%=dataCacheId%>";
		</script>
		<script language="javascript" src="flash/swfobject.js"></script>
		<script language="javascript">
			
			getAGuid = function()
			{
				return	guid6Parts();
			}
			
			function	pageLoadCompleted()
			{
				var screenHeight = screen.availHeight;
				var screenWidth = screen.availWidth;
				window.moveTo(0,0);
				window.resizeTo(screenWidth,screenHeight);
			}
			
			function callFromFlash(streamName,eventName)
			{
				window.opener.callFromFlash(streamName,eventName);
				if (eventName == 'stop')
				{
					window.close();
				}
				else
				{
					window.current_slide = eventName+"";
				}
			}
			
			openDimdimWebSite = function()
			{
				window.open("http://www.dimdim.com");
			}
		</script>
</head>
<body onLoad="javascript:pageLoadCompleted()">
<div id="pre-loader" align="center"><br>
<br>
<br>
<img src="images/ajax-loader.gif">
<br>
<br>
<br>
<strong style="font-size:18px;"><dm:I18NDisplayString component="console" dictionary="ui_strings" key="loading.message"/></strong>
</div>
<script language="javascript" src="com.dimdim.conference.ui.workspacepopout.WorkspacePopout.nocache.js"></script>
<div id="content_body" class="Hide">
<div id="console_background" class="console-background">
  <div id="background_header" class="background-header">&nbsp;</div>
  <div id="background_middle_1" class="background-middle"></div>
  <div id="background_footer" class="background-footer">&nbsp;</div>
</div>
<div id="MainPopout" class="console-content">
 <table width="100%" border="0" cellspacing="1" cellpadding="1">
  <tr >
  <td align="center" width="5px" style="font-size:5px;">&nbsp;</td>
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
</html>
