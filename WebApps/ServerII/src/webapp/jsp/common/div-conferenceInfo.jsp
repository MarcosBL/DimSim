<% response.addHeader("Cache-Control","no-cache, nostore, must-revalidate"); %>
<%@ taglib prefix="ww" uri="/webwork" %>
<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=iso-8859-1">
<html>
<head>
	<title>Meeting Information</title>
  <link rel="stylesheet" href="css/landing_pages.css" type="text/css">
</head>
<body>
<div class="landing_page_body" width="100%">
<div style="margin-left: 10px; margin-top:10px;">
 <div><b>Subject : </b><div><ww:property escape="false" value="info.name"/></div></div>
</div>
<div style="margin-left: 10px; margin-top:10px;">
 <div><b>Key : </b></div><div><ww:property escape="false" value="info.key"/></div>
</div>
<div style="margin-left: 10px; margin-top:10px;">
 <div><b>Organizer : </b></div>
 <div><a href="mailto:<ww:property escape="false" value="info.organizedEmail"/>">
	<ww:property escape="false" value="info.organizer"/></a></div>
</div>
<div style="margin-left: 10px; margin-top:10px;">
 <div><b>Start Time : </b></div>
 <div><ww:property escape="false" value="info.startTime"/></div>
</div>
<div style="margin-left: 10px; margin-top:10px;">
 <div><b>Meeting URL : </b></div>
 <div><ww:property escape="false" value="info.joinURL"/></div>
</div>
</body>
</html>
