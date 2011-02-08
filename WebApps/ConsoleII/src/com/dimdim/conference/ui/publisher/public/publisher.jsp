<%@ page import="com.dimdim.conference.UtilMethods" %>

<%
String userAgent = request.getHeader("user-agent").toLowerCase();
String browserType = UtilMethods.getBrowserType(userAgent);
//System.out.println("from publisher jsp...browserType = "+browserType);
%>

<html>
	<head>

		<!--                                           -->
		<!-- Any title is fine                         -->
		<!--                                           -->
		<title>Publisher sample code</title>

		<!--                                           -->
		<!-- Use normal html, such as style            -->
		<!--                                           -->
		<style>
			body,td,a,div,.p{font-family:arial,sans-serif}
			div,td{color:#000000}
			a:link,.w,.w a:link{color:#0000cc}
			a:visited{color:#551a8b}
			a:active{color:#ff0000}
		</style>

		<!--                                           -->
		<!-- The module reference below is the link    -->
		<!-- between html and your Web Toolkit module  -->
		<!--                                           -->
		<meta name='gwt:module' content='com.dimdim.conference.ui.publisher.Publisher'>
		<link href="Common_II.css" rel="stylesheet" type="text/css">
		<link href="Console_II.css" rel="stylesheet" type="text/css">
		<link href="User_II.css" rel="stylesheet" type="text/css">
		<link href="Invitations_II.css" rel="stylesheet" type="text/css">
		<link href="Moods_II.css" rel="stylesheet" type="text/css">
		<link href="Lists_II.css" rel="stylesheet" type="text/css">
		<link href="Popups_II.css" rel="stylesheet" type="text/css">
		<link href="Chat_II.css" rel="stylesheet" type="text/css">
		<link href="Discuss_II.css" rel="stylesheet" type="text/css">
		<link href="Tabs_II.css" rel="stylesheet" type="text/css">
		<link href="tk_styles.css" rel="stylesheet" type="text/css">
		<link href="meeting_assistent.css" rel="stylesheet" type="text/css">
		<link href="style/gwl-progressBar.css" rel="stylesheet" type="text/css"> 
		
		<script language='javascript' src='gwt.js'></script>
		<script language='javascript' src='flash_bridge.js'></script>
		<script language='javascript' src='command_support.js'></script>
		<script language='javascript' src='support_functions.js'></script>
		<script src="tools_metadata.js" type="text/javascript"></script>
		<script src="client_guid.js" type="text/javascript"></script>
		<script src="ui_resources.js" type="text/javascript"></script>
		
		<script language="javascript">
		window.browser_type = "<%=browserType%>";
		function getVersion()
				{
			//var version = window.embeds["DimdimDesktopPublisher1"].getVersion();
			var version = document.DimdimDesktopPublisher1.getVersion();
			alert("Version: "+version);
			return version;
		};
		</script>

	</head>

	<!--                                           -->
	<!-- The body can have arbitrary html, or      -->
	<!-- you can leave the body empty if you want  -->
	<!-- to create a completely dynamic ui         -->
	<!--                                           -->
	<body>

		<iframe id="__gwt_historyFrame" style="width:0;height:0;border:0"></iframe>

		<h1>Publisher</h1>

		<p>
			This is an example of a host page for publisher
		</p>
		
		<table align=center>
			<tr>
				<!-- --><td id="slot1"></td> 
			</tr>
		</table>
	</body>
	<% if (browserType == null || browserType.equalsIgnoreCase("ie")) {
		System.out.println("* ********** Opening console in IE"); 
		%>
		 <object id="dimdimPublisherControl1"  WIDTH=0 HEIGHT=0 classid="CLSID:36EFD4D0-4519-4395-8BD2-D92BB9296797">
    		</object>
	<% } else {
	System.out.println("* ********** Opening console in Firefox"); 
	%>
		
	<% } %>
	
	
</html>
