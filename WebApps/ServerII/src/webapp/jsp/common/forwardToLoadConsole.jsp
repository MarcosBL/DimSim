<% response.addHeader("Cache-Control","no-cache, nostore, must-revalidate"); %>
<% response.addHeader("Expires","-1"); %>
<%@ taglib prefix="ww" uri="/webwork" %>
<html>
	<head>
		<meta HTTP-EQUIV="REFRESH" content="0; url=html/layout2/GetFullConsole.action?confKey=<ww:property escape="false" value="confKey"/>&cflag=<ww:property escape="false" value="cflag"/>&uri=<ww:property escape="false" value="uri"/>&pubAvailable=<ww:property escape="false" value="pubAvailable"/>">
	</head>
	<body onload="window.location='html/layout2/GetFullConsole.action?confKey=<ww:property escape="false" value="confKey"/>&cflag=<ww:property escape="false" value="cflag"/>&uri=<ww:property escape="false" value="uri"/>&pubAvailable=<ww:property escape="false" value="pubAvailable"/>'">
		<p>Redirecting to Meeting Console....Please wait or click <a href="html/layout2/GetFullConsole.action?confKey=<ww:property escape="false" value="confKey"/>&cflag=<ww:property escape="false" value="cflag"/>&uri=<ww:property escape="false" value="uri"/>&pubAvailable=<ww:property escape="false" value="pubAvailable"/>">here</a></p>
	</body>
</html>
