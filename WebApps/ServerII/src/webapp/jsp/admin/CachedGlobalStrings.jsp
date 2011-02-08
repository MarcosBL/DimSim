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
<%@ page import="com.dimdim.data.application.*" %>
<%@ page import="com.dimdim.conference.application.session.AdminSession" %>
<%@ page import="com.dimdim.util.session.*" %>
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
  <tr><td><h3>Cached Global Strings</h3></td></tr>
  <tr><td>
    <table cellmargin="5" cellpadding="5">
<%
	HashMap sessions = UIDataManager.getUIDataManager().getGlobalStrings();
	if (sessions.size() > 0)
	{
%>
		<tr><th><b>String ID</b></th><th><b>Value</b></th><th>Creation Time</th><th></th></tr>
<%
		Iterator ssns = sessions.keySet().iterator();
		while (ssns.hasNext())
		{
			String key = (String)ssns.next();
if (removeKey != null && removeKey.equals(key))
{
	sessions.remove(key);
	break;
}
			try
			{
				if (key != null)
				{
					Object	value = sessions.get(key);
					String s = (String)value;
					if (s.length() > 20)
					{
						s = s.substring(0,20);
					}
%>
					<tr><td><%=key%></td><td><%=s%></td><td></td><td></td></tr>
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
		<tr><td>There is no cached user session information</td></tr>
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
