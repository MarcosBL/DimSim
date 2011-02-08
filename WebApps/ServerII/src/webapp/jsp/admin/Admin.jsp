<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN">
<% response.addHeader("Cache-Control","no-cache, nostore, must-revalidate, private"); %>
<%@ taglib prefix="dm" uri="com.dimdim.conferenceConsole.tags" %>
<%@ taglib prefix="ww" uri="/webwork" %>
<%@ page import="com.dimdim.conference.*" %>
<%@ page import="com.dimdim.conference.model.*" %>
<%@ page import="com.dimdim.conference.application.*" %>
<%
%>
<html>
	<head>
		<meta name='gwt:module' content='com.dimdim.conference.ui.admin.Admin'>
		<link rel=stylesheet href="console.css">
		<link rel=stylesheet href="tool_common.css">
		<link rel=stylesheet href="tool_buttons.css">
		<link rel=stylesheet href="popup_styles.css">
		<link rel=stylesheet href="Navigator.css">
		<link rel=stylesheet href="Chat.css">
		<link rel=stylesheet href="RosterControl.css">
		<script language='javascript' src='gwt.js'></script>
		<script src="ui_resources.js" type="text/javascript"></script>
	</head>
	<body>
		<div name="admin" id="admin"></div>
	</body>
</html>
