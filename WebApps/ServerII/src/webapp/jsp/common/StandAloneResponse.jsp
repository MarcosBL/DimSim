<%@ page import="java.util.Date" %>
<%@ page import="com.dimdim.conference.ConferenceConsoleConstants" %>
<% response.addHeader("Cache-Control","no-cache, no-store, must-revalidate"); 
	response.addDateHeader("Last-Modified", (new Date()).getTime()); 
	String webappName = ConferenceConsoleConstants.getWebappName();
%>

<%@ taglib prefix="ww" uri="/webwork" %>

<html>
<head>
<link href="/<%=webappName%>/html/envcheck/EnvChecks.css" rel="stylesheet" type="text/css">
<link href="/<%=webappName%>/html/envcheck/EnvChecks2.css" rel="stylesheet" type="text/css">
<!--[if lt IE 7.]>
	<script defer type="text/javascript" src="pngfix.js"></script>
<![endif]-->
<link href="/<%=webappName%>/html/envcheck/style_portal.css"rel="stylesheet" type="text/css" />
<title>Dimdim - Error</title>

<script language="javascript" type="text/javascript">
	var jsonResp = '<ww:property escape="false" value="message"/>';
	function setError(){
		document.getElementById('errorMsg').innerHTML=jsonResp;
	}
</script>

</head>
<body  class="pagebgcolor" onload='setError()'>
<div id="main">
<table width="1004" border="0" cellspacing="0" cellpadding="0" align="center">
  <tr>
    <td><div class="logo" onclick="test()"></div></td>
  </tr>
  <tr>
    <td align="center"><br />
    <div id="cpod"><div id="cpod_middle"><div id="cpod_top"></div><br /><br />
<br />
<br />
<img src="/<%=webappName%>/html/envcheck/images/error.gif">
<br />
<div align="center" id="errorMsg" style="font-family:Arial, Helvetica, sans-serif;font-size:13px;color:#FF0000;font-weight:bold;">
</div>
		       <br />
<br />
<br />
<br /><br /></div><div id="cpod_bottom"></div><br />
</div>
</td></tr>

<tr><td><br />
<br />
<br />
<br />
<br />
<br />
<br />
</td></tr>

</table>
</div>
</body>

</html>