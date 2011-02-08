<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page import="java.util.Date" %>
<%
	response.addHeader("Pragma", "no-cache"); 
	response.addHeader("Expires", "-1"); 
	response.addDateHeader("Last-Modified", (new Date()).getTime()); 
%>
<%@ taglib prefix="ww" uri="/webwork" %>
<%@ page import="java.util.*" %>
<%@ page import="com.dimdim.conference.ConferenceConsoleConstants" %>
<%@ page import="com.dimdim.conference.model.*" %>
<%@ page import="com.dimdim.conference.application.*" %>
<%@ page import="com.dimdim.conference.application.core.*" %>
<%@ page import="com.dimdim.conference.application.session.AdminSession" %>
<%
	AdminSession adminSession = (AdminSession)session.
		getAttribute(ConferenceConsoleConstants.ACTIVE_ADMIN_SESSION);
	if (adminSession == null)
	{
		response.sendRedirect("AdminRoot.action");
	}
	String removeKey = request.getParameter("removeKey");
%>
<html>
<head>
<title>DimDim Web Conference Administration</title>
  <link rel="stylesheet" href="css/landing_pages.css" type="text/css">

</head>
<body>
<div id="top_black_band" class="top_black_band">
  <div id="top-black-logo"><img src="images/logo.jpg"></div>
</div>
<div id="top_blue_band" class="top_blue_band"></div>
<div id="center_band" class="center_band">
<table width="100%" cellmargin="5" cellpadding="5">
  <tr><td><h3>Active User Sessions</h3></td></tr>
  <tr><td>
    <table cellmargin="5" cellpadding="5">
<%
	UserSessionManager usm = UserSessionManager.getManager();
	if (usm.getNumberOfActiveSessions() > 0)
	{
%>
		<tr><th><b>Session Key</b></th><th><b>Name</b></th><th><b>Meeting</b></th><th><b>Start Time</b></th><th><b>Last Access Time</b></th><th> </th></tr>
<%
		Iterator ssns = usm.sessions();
		while (ssns.hasNext())
		{
			DimDimSession dds = (DimDimSession)ssns.next();
			try
			{
				if (dds != null)
				{
					String key = dds.getSessionKey();
if (removeKey != null && removeKey.equals(key))
{
	dds.close();
	break;
}
					String startTime  = dds.getCreationDate().toString();
					Date lat = new Date();
					lat.setTime(dds.getLastAccessTime());
					String lastAccessTime = lat.toString();
					String confKey = "";
					String userName = "";
					String remove = "";

long it = System.currentTimeMillis() - dds.getLastAccessTime();
if (it > (60*60*1000))
{
	remove = "<a href=\"/dimdim/jsp/admin/ActiveUserSessions.jsp?removeKey="+key+"\">remove</a>";
}
					try
					{
						confKey = ((UserSession)dds).getConference().getConfig().getConferenceKey();
						userName = ((UserSession)dds).getUser().getDisplayName();
					}
					catch(Throwable t)
					{
						
					}
%>
					<tr><td><%=key%></td><td><%=userName%></td><td><%=confKey%></td><td><%=startTime%></td><td><%=lastAccessTime%></td><td><%=remove%></td></tr>
<%
				}
				else
				{
				}
			}
			catch(Throwable tt)
			{
				
			}
		}
	}
	else
	{
%>
		<tr><td>There are no active user sessions</td></tr>
<%
	}
%>
  <tr><td><h3>-------------------</h3></td></tr>
</table>
</div>
<div id="bottom_blue_band_floating" style="padding: 8px; font-size: 11; color: white;" class="bottom_blue_band_floating">
Copyright 2006 Dimdim inc.
</div>
<div id="bottom_black_band_floating" style="padding-left: 8px; padding-top: 40px; font-size: 11; " class="bottom_black_band_floating">
<a href="http://www.dimdim.com/index.php?option=com_wrapper&Itemid=78" style="color: white;">Dimdim Privacy Policy</a>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
<a href="http://www.dimdim.com/index.php?option=com_wrapper&Itemid=77" style="color: white;">Dimdim Trademark and Copyright Policy</a>
</div>
</body>
</html>
