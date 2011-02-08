<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page import="com.dimdim.util.misc.StringGenerator" %>
<%@ page import="com.dimdim.conference.ConferenceConsoleConstants" %>
<%@ page import="com.dimdim.conference.ConferenceConstants" %>
<%@ page import="java.util.Date" %>
<%
	response.addHeader("Pragma", "no-cache"); 
	response.addHeader("Expires", "-1"); 
	response.addDateHeader("Last-Modified", (new Date()).getTime()); 
%>
<%@ taglib prefix="dm" uri="com.dimdim.conferenceConsole.tags" %>
<dm:SetSessionLocale/>
<%
	String browserType = (String)request.getSession().getAttribute("BROWSER_TYPE");
	String webappName = ConferenceConsoleConstants.getWebappName();
	StringGenerator idGen = new StringGenerator();
	String dummyFlag = idGen.generateRandomString(7,7);
	String checkObject = (String)request.getParameter("checkObject");
	String submitFormOnLoad = (String)request.getParameter("submitFormOnLoad");
	String freshInstall = (String)request.getParameter("freshInstall");
	String serverURL = ConferenceConsoleConstants.getServerAddress();
	if (freshInstall == null)
	{
		freshInstall = "true";
	}
	if (freshInstall.equals("true"))
	{
		submitFormOnLoad = null;
	}
	else
	{
		// this upgrade option needs to be varified.
		submitFormOnLoad = null;
	}
	int pubMajorVersion = ConferenceConstants.PUBLISHER_CONTROL_MAJOR_VERSION;
	int pubMinorVersion = ConferenceConstants.PUBLISHER_CONTROL_MINOR_VERSION;
	int pubBuildVersion = ConferenceConstants.PUBLISHER_CONTROL_BUILD_VERSION;
	int pubBuildVersion2 = ConferenceConstants.PUBLISHER_CONTROL_BUILD_VERSION_2;	
%>
<html>
<head>
<title><dm:I18NDisplayString component="landing_pages" dictionary="ui_strings"
  	key="install_x_control_page.title" /></title>
  <link rel="stylesheet" href="css/landing_pages.css" type="text/css">

<STYLE type=text/css>
BODY {
	
	background: #E8EEF7;
	background-color: #E8EEF7;
}


table
{
	margin: 0px;
	padding: 0px;
	border-spacing: 0px;
}

td
{
	padding: 0px;
	margin: 0px;
}

.left-side
{
	width: 20px;
	height:40px;
	background-image: url(images/red-btn-left.png);
	cursor: default;
	cursor: pointer;
	position:relative;
	left:5px;
}

img
{
	padding: 0px;
	margin: 0px;
	position:relative;
	top:15px;
}

.button1
{
	width: 100px;
	height:40px;
	line-height: 40px;
	vertical-align: middle;
	text-align: center;
	font-family: Lucinda Grande, Verdana, Trebuchet MS, Arial, sans-serif, Tahoma;
	font-size: 12px;
	font-weight: bold;
	background-image: url(images/red-btn-tile.png);
	background-repeat: repeat-x;
	background-color: transparent;
	color: white;
	border-style: hidden;
	cursor: default;
	cursor: pointer;
	border: 0px;
	margin: 0px;
	padding: 0px;
	top:0px;
}

.right-side
{
	width: 20px;
	height:40px;
	background-image: url(images/red-btn-right.png);
	cursor: default;
	cursor: pointer;
	position:relative;
	right:5px;
}


</STYLE>

<script type="text/javascript">
			
window.publisher_available = "unknown";
window.publisher_availability = "unknown";
			
<% if (browserType != null && browserType.equalsIgnoreCase("firefox")) { %>
/*  This function returns an interger with values as:
	1 = no control exists.
	2 = control exists but is lower version
	3 = control exists and is required version or higher
alert("1"+version);
alert("2");
alert("3");
alert("4");
alert(document.DimdimDesktopPublisher1.getVersion());
	alert("Exception");
*/
function getXControlVersion(version)
{
	var ret = 1;
   try
   {
	if ( document.embeds["DimdimDesktopPublisher1"] != null)
	{
		ret = 2;
		window.parent.publisher_available_version = ""+document.DimdimDesktopPublisher1.getVersion();
		if ( document.DimdimDesktopPublisher1.getVersion() >= version )
		{
			ret = 3;
		}
		else
		{
		}
	}
   }
   catch(e)
   {
   }
  return ret;
}
<% } %>
 


function updateText()
{ 
  var ni = document.getElementById('myDiv');
  var newdiv = document.createElement('div');
  var divIdName = 'myDiv1'
  newdiv.setAttribute('id',divIdName);
  newdiv.innerHTML = 'The installation is going on please wait.You would be taken to <br>the meeting as soon as the installation is over';
  ni.appendChild(newdiv);
}

function checkPassed()
{	
	window.parent.publisher_availability = "available";
	window.parent.publisher_available = "true";
}
</script>
</head>
<body>
<div id="top_blue_band" class="top_blue_band"></div>
<div id="center_band" class="center_band">
<% if (submitFormOnLoad != null && submitFormOnLoad.equals("true")) { %>
<table width="100%">
  <tr>
    <td width="5%">
    </td>
    <td width="95%">Dimdim Web Meeting Publisher is being downloaded. This may take a few moments.
    </td>
  <tr>
  <tr>
    <td></td>
    <td><img src="waiting.gif"/></td>
  <tr>
</table>
<% } else { %>
<table width="100%">
  <tr>
    <td width="25%">
    </td>
    <td width="75%">
      <form name="dummy" action="" method="post" enctype="application/x-www-urlencoded">      
        <table border="0" cellpadding="0" cellspacing="0" width="586">        
          <tbody>
            <tr>
              <td>
              <table class="landing_page_body" border="0" cellpadding="0" cellspacing="0" width="586">
                <tbody>
                  <tr>
                    <td><img src="images/placeholder.gif" height="5" width="586"></td>
                  </tr>

                  <tr>
                    <td class="dark_blue"><img src="images/placeholder.gif" height="1" width="586"></td>
                  </tr>

                  <tr>
                    <td><img src="images/placeholder.gif" height="20" width="586"></td>
                  </tr>

		    <tr>			
			<% if (freshInstall.equalsIgnoreCase("true")) { %>						
			<p><font size="2" face="Verdana">
			Please check the top of your browser page for a message which states that Firefox has blocked the installation. If you see a yellow bar similar to the <a href="images/ffstuckpage.JPG" target="_blank">image</a>, 			
			please continue with the steps given below, otherwise please refresh the browser.
			<br>
			<br>
			<font size="2" face="Verdana">								
				Firefox has blocked the installation of the  Screencaster Plug-in. Please follow the steps given below. Please add the 
				Dimdim Web Meeting Hostname/IP Address in the allowed list and proceed with the Plug-in Installation.
				<br>
				<img src="images/one.JPG"> <b>Click on Edit Options</b> <a href="images/ffeditoption.JPG" target="_blank">View image</a>
				<br>
             		&nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp Click on the Edit Options to allow Dimdim Meeting Server machine to install add-ons.  
				<br>
				<img src="images/two.JPG"> <b>Press on Allow button</b> <a href="images/clickallow.JPG" target="_blank">View image</a>
				<br>
             		&nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp Verify IP address/Host name of Dimdim Meeting Server and click Allow.
				<br>
				<img src="images/three.JPG"> <b>Dimdim Meeting Server status should be Allow</b> <a href="images/allowedsite.JPG" target="_blank">View image</a>
				<br>
             		&nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp The status of Dimdim Meeting Server should be "Allow". 
				<br>	
				<br>							
			<b>Now please close this window and either restart or refresh the browser.</b>
			<% } else { %>	
			
			<p><font size="2" face="Verdana">
			<p><font size="2" face="Verdana">
			Please check the top of your browser page for a message which states that Firefox has blocked the installation. If you see a yellow bar similar to the <a href="images/ffstuckpage.JPG" target="_blank">image</a>, 			
			please continue with the steps given below, otherwise please refresh the browser.
			<br>
			<br>
			<font size="2" face="Verdana">								
				Firefox has blocked the installation of the  Screencaster Plug-in. Please follow the steps given below. Please add the 
				Dimdim Web Meeting Hostname/IP Address in the allowed list and proceed with the Plug-in Installation.
				<br>
				<img src="images/one.JPG"> <b>Click on Edit Options</b> <a href="images/ffeditoption.JPG" target="_blank">View image</a>
				<br>
             		&nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp Click on the Edit Options to allow Dimdim Meeting Server machine to install add-ons.  
				<br>
				<img src="images/two.JPG"> <b>Press on Allow button</b> <a href="images/clickallow.JPG" target="_blank">View image</a>
				<br>
             		&nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp Verify IP address/Host name of Dimdim Meeting Server and click Allow.
				<br>
				<img src="images/three.JPG"> <b>Dimdim Meeting Server status should be Allow</b> <a href="images/allowedsite.JPG" target="_blank">View image</a>
				<br>
             		&nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp The status of Dimdim Meeting Server should be "Allow". 
				<br>	
				<br>							
			<b>Now please close this window and either restart or refresh the browser.</b>
			<%} %>	
			<br><br><br><div id="myDiv"> </div>			
		    </tr>                    
		    <tr>
                    <td><img src="images/placeholder.gif" height="20" width="556"></td>
                  </tr>
                </tbody>
              </table>
              </td>
            </tr>
          </tbody>
        </table>
      </form>
    </td>
  </tr>
</table>
<% } %>
</div>
</body>
</html>
