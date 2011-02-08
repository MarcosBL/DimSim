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
<%@ page import="com.dimdim.conference.application.session.*" %>
<%
	LogFileBrowseSession lfbs = null;
	AdminSession adminSession = (AdminSession)session.
		getAttribute(ConferenceConsoleConstants.ACTIVE_ADMIN_SESSION);
	if (adminSession == null)
	{
		response.sendRedirect("AdminRoot.action");
	}
	else
	{
		lfbs = adminSession.getLogFileBrowseSession();
	}
	if (lfbs == null)
	{
		response.sendRedirect("Admin.action?token=logFiles");
	}
	String fileName = lfbs.getNiceName();
	int fileIndex = lfbs.getFileIndex();
	long numberOfBlocks = lfbs.getNumberOfBlocks();
	long currentBlockIndex = lfbs.getCurrentBlockIndex();
	String currentBuffer = lfbs.getCurrentBuffer();
	String tmpFile = lfbs.getTmpTxtFileName();
%>
<html>
<head>
<title>DimDim Web Conference Administration - Log Files Browser</title>
  <link rel="stylesheet" href="css/landing_pages.css" type="text/css">
<script type="text/javascript">
function gotoPage(num)
{
	var url = "BrowseLogFile.action?fileIndex=<%=fileIndex%>&blockIndex="+num;
	document.location = url;
}
function readBlock()
{
	var bi = document.getElementById("blockIndex").value;
	var url = "BrowseLogFile.action?fileIndex=<%=fileIndex%>&blockIndex="+bi;
	document.location = url;
}
function forceRefresh()
{
	document.refresh();
}
</script>
</head>
<body>
<div id="top_black_band" class="top_black_band">
  <div id="top-black-logo"><img src="images/logo.jpg"></div>
</div>
<div id="top_blue_band" class="top_blue_band"></div>
<div id="center_band" class="center_band">
      <form name="readBlockForm" action="BrowseLogFile.action"
		method="post" enctype="application/x-www-urlencoded">
<table width="100%" cellmargin="5" cellpadding="5">
  <tr><td><h3>Server Log Files</h3></td></tr>
  <tr><td>
    <table cellmargin="5" cellpadding="5">
		<tr><th><b>Log File Name</b></th><td><b><%=fileName%></b></td></tr>
		<tr><th><b>Number of pages</b></th><td><b><%=numberOfBlocks%></b></td><th><b>Current Page</b></th><td><b><%=currentBlockIndex%></b></td></tr>
		<tr><th><b>Page --</b></th><td>
<input type"text" name="blockIndex" id="blockIndex" size="10" value="<%=currentBlockIndex+1%>"/>
<button type"button" onclick="javascript:readBlock();">Goto Page</button>
		</td></tr>
	</table>
  </td></tr>
  <tr><td>-----------------------------------------------------</td></tr>
  <tr><td><iframe width="100%" height="400" src="<%=tmpFile%>"/></td></tr>
  <tr><td>-----------------------------------------------------</td></tr>
<br>
</table>
		</form>
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
