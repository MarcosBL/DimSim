<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
		"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<% response.addHeader("Cache-Control","no-cache, nostore, must-revalidate"); %>
<%@ page import="com.dimdim.util.misc.StringGenerator" %>
<%@ page import="com.dimdim.conference.ConferenceConsoleConstants" %>
<%
	StringGenerator idGen = new StringGenerator();
	String dummyFlag = idGen.generateRandomString(7,7);
	String showFrontPage = ConferenceConsoleConstants.getResourceKeyValue("dimdim.frontpage","true");
	if (showFrontPage.equalsIgnoreCase("true"))
	{
		response.sendRedirect("GetWebAppRoot.action?flag="+dummyFlag);
	}
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title>Dimdim Conference System</title>
<link href="css/signin.css" rel="stylesheet" type="text/css" />
</head>
<body>
<div id="container-signin">
<%	if (showFrontPage.equalsIgnoreCase("true"))	{ %>
	<div id="signin-logo"><h1><a href="#">Dimdim Web Meeting</a></h1></div>
	<div id="pg-header"><h2>Welcome to Dimdim Web Meeting</h2></div>
	<div id="signin-content">
		<p>Welcome to Dimdim Web Meeting System</p>
    <table width="80%" cellpadding="10">
      <tr>
        <td><a href="GetWebAppRoot.action?flag=<%=dummyFlag%>">Conference Webapp Start Page</a></td>
      </tr>
      <br>
    </table>
	</div>
<%	} else { %>
	<h1>Not Found</h1>
	<p>The requested URL is not accessible</p>
<%	} %>
</div>
</body>
</html>
