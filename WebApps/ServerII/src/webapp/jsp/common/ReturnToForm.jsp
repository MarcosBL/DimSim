<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page import="com.dimdim.conference.ConferenceConsoleConstants" %>
<%@ page import="com.dimdim.util.misc.StringGenerator" %>
<%
	StringGenerator idGen = new StringGenerator();
	String webappName = ConferenceConsoleConstants.getWebappName();
	String message = (String)request.getParameter("message");
	String cacheBuster = idGen.generateRandomString(40,40);
	if (message == null)
	{
		message = "Error";
	}
	
	String url = "/"+webappName+"/html/envcheck/GetFormsPage.action?message="+message+"&cflag="+cacheBuster;
	response.sendRedirect(url);
%>
