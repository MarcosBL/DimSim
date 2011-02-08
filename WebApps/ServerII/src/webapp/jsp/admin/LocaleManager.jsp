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
<%@ page import="com.dimdim.locale.*" %>
<%@ page import="com.dimdim.conference.db.*" %>
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
  <tr><td><h3>Supported Locales</h3></td></tr>
  <tr><td>
    <table cellmargin="5" cellpadding="5">
<%
	LocaleManager lm = LocaleManager.getManager();
	Vector v = lm.getSupportedLocales();
	if (v != null && v.size() > 0)
	{
%>
		<tr><th><b>Locale</b></th></tr>
		<tr><td>--------------------------------------------------------------------------</td></tr>
<%
		for (int i=0; i<v.size(); i++)
		{
			Locale l = (Locale)v.elementAt(i);
%>
				<tr><td><%=l.toString()%></td></tr>
<%
		}
	}
	else
	{
%>
		<tr><td>There are no supported locales</td></tr>
<%
	}
%>
	</table>
  </td></tr>
  <tr><td><h3>-----------------------------------------------------</h3></td></tr>
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
