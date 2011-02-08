<% response.addHeader("Cache-Control","no-cache, nostore, must-revalidate"); %>
<%@ taglib prefix="ww" uri="/webwork" %>
<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=iso-8859-1">
<html>
<head>
	<title>Meeting Information</title>
  <link rel="stylesheet" href="css/landing_pages.css" type="text/css">
</head>
<body>
<div class="landing_page_body">
<table width="100%">
  <tr>
    <td>
	<table width="100%">
	<tr>
		<td width="25%"><b>Subject : </b></td>
		<td width="75%"><ww:property escape="false" value="info.name"/></td>
	</tr>
	<tr>
		<td><b>Key : </b></td>
		<td><ww:property escape="false" value="info.key"/></td>
	</tr>
	<tr>
		<td><b>Organizer : </b></td>
		<td><a href="mailto:<ww:property escape="false" value="info.organizedEmail"/>"><ww:property escape="false" value="info.organizer"/></a></td>
	</tr>
	<tr>
		<td><span style="word-wrap: normal"><b>Start Time : </b></span></td>
		<td><ww:property escape="false" value="info.startTime"/></td>
	</tr>
	<tr>
		<td><span style="word-wrap: break-word"><b>Meeting URL : </b></span></td>
		<td><span  style="word-wrap: break-word"><ww:property escape="false" value="info.joinURL"/></span></td>
	</tr>
	</table>
    </td>
    <td width="75%">&nbsp;</td>
  </tr>
</table>
</div>
</body>
</html>
