<% response.addHeader("Cache-Control","no-cache, nostore, must-revalidate"); %>
<%@ taglib prefix="ww" uri="/webwork" %>
<%
	
%>
<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=iso-8859-1">
<html>
<head>
	<title>Meeting Information</title>
  <link rel="stylesheet" href="css/landing_pages.css" type="text/css">
</head>
<body>
<div class="landing_page_body" width="100%">
<div style="margin-left: 10px; margin-top:10px;">
  <b>Subject : </b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<ww:property escape="false" value="info.name"/>
</div>
<div style="margin-left: 10px; margin-top:10px;">
  <b>Key : </b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<ww:property escape="false" value="info.key"/>
</div>
<div style="margin-left: 10px; margin-top:10px;">
  <b>Organizer : </b>&nbsp;&nbsp;&nbsp;<a href="mailto:<ww:property escape="false" value="info.organizerEmail"/>?subject=<ww:property escape="false" value="info.name"/>">
	<ww:property escape="false" value="info.organizer"/></a>
</div>
<div style="margin-left: 10px; margin-top:10px;">
  <b>Start Time : </b>&nbsp;&nbsp;<ww:property escape="false" value="localeDate"/>
</div>
<div style="margin-left: 10px; margin-top:10px;">
  <b>Meeting URL : </b>&nbsp;<ww:property escape="false" value="info.joinURL"/>
</div>
</body>
</html>
