<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page import="java.util.Date" %>
<%
	response.addHeader("Pragma", "no-cache"); 
	response.addHeader("Expires", "-1"); 
	response.addDateHeader("Last-Modified", (new Date()).getTime()); 
%>
<%@ taglib prefix="ww" uri="/webwork" %>
<%@ taglib prefix="dm" uri="com.dimdim.conferenceConsole.tags" %>
<%@ page import="com.dimdim.util.misc.StringGenerator" %>
<%@ page import="com.dimdim.conference.ConferenceConsoleConstants" %>
<%@ page import="com.dimdim.conference.model.*" %>
<%@ page import="com.dimdim.conference.application.*" %>
<%@ page import="com.dimdim.conference.application.session.AdminSession" %>
<html>
<head>
<title>DimDim Web Conference Administration</title>
  <link rel="stylesheet" href="css/landing_pages.css" type="text/css">
  <script language="JavaScript1.2" type="text/javascript" src="js/dimdim.js"></script>

<script type="text/javascript">

if (document.layers)
{
	NS4 = true;
	document.captureEvents(Event.KEYPRESS);
	document.onKeyPress = checkEnter;
}
else
{
	NS4 = false;
}

function checkEnter(event)
{
	var code = 0;
	if (NS4) {
		code = event.which;
	}
	else
	{
		code = event.keyCode;
	}

	if (code==13) {
		document.adminForm.submit();
	}
}

function submitAdminForm()
{
	document.adminForm.submit();
}

</script>

</head>
<body>
<dm:SetSessionLocale/>
<div id="top_black_band" class="top_black_band">
  <div id="top-black-logo"><img src="images/logo.jpg"></div>
</div>
<div id="top_blue_band" class="top_blue_band"></div>
<div id="center_band" class="center_band">
<table width="100%">
  <tr>
    <td width="25%">
    </td>
    <td width="75%">
      <form name="adminForm" action="AdminRoot.action"
		method="post" enctype="application/x-www-urlencoded">
        <table border="0" cellpadding="0" cellspacing="0">
            <tr>
              <td>
              <table class="landing_page_body" border="0" cellpadding="0" cellspacing="0">
                  <tr>
                    <td><img src="images/placeholder.gif" height="5"></td>
                  </tr>

                  <tr>
                    <td class="heading">Conference Server Administration</td>
                  </tr>

                  <tr>
                    <td class="dark_blue"><img src="images/placeholder.gif" height="1"></td>
                  </tr>

                  <tr>
                    <td><img src="images/placeholder.gif" height="20"></td>
                  </tr>

                  <tr>
                    <td class="regular_text">Login  ...</td>
                  </tr>

                  <tr>
                    <td><img src="images/placeholder.gif" height="20"></td>
                  </tr>

                  <tr>
                    <td>
<div>
<table style="height: 80px;" border="0" cellpadding="0" cellspacing="0">
    <tr>
	<td width="30%" class="regular_text" align="right"><label for="name">Name:</label></td>
	<td width="5%"><img src="images/placeholder.gif" height="1" width="2"></td>
	<td width="65%"><input class="input_field" size="24" name="name" id="name"
		value="<ww:property value="name"/>" onkeypress="checkEnter(event)"
		maxlength="80"><span class="message_field"><ww:fielderror><ww:param>name</ww:param></ww:fielderror></span></td>
    </tr>
    <tr>
	<td class="regular_text" align="right"><label for="displayName">Password:</label></td>
	<td><img src="images/placeholder.gif" height="1" width="2"></td>
	<td><input type="password" class="input_field" size="24" name="password" id="password"
		value="<ww:property value="password"/>" onkeypress="checkEnter(event)"
		maxlength="80"><span class="message_field"><ww:fielderror><ww:param>password</ww:param></ww:fielderror></span></td>
    </tr>
    </tr>
    <tr>
	<td class="regular_text" align="right"><label for="displayName">Locale:</label></td>
	<td><img src="images/placeholder.gif" height="1" width="2"></td>
	<td><select class="input_field" name="sessionLocale">
		<dm:SupportedLocalesOptionsList/>
		</select></td>
    </tr>
</table>
</div>
                    </td>
                  </tr>

                  <tr>
                    <td width="70%">
                    <table style="width: 407px; height: 110px;" border="0" cellpadding="0" cellspacing="0">
                      <tbody>
                        <tr>
                          <td class="regular_text" width="130">&nbsp;</td>
                          <td><img src="images/placeholder.gif" height="1" width="5"></td>
                          <td class="regular_text">
                          <button type="button" name="continue" class="normal_button"
						onclick="javascript:submitAdminForm();"
						onmouseover="this.className='highlighted_button'"
						onmouseout="this.className='normal_button'" title="Start">
					<img src="images/placeholder.gif" height="1" width="70"><br>Login</button></td>
                        </tr>
                      </tbody>
                    </table>
                    </td>
                  </tr>

                  <tr>
                    <td><img src="images/placeholder.gif" height="20"></td>
                  </tr>
              </table>
              </td>
            </tr>
        </table>
      </form>
    </td>
  </tr>
</table>
</div>
<div id="bottom_blue_band" style="padding: 8px; font-size: 11; color: white;" class="bottom_blue_band">
Copyright 2006 Dimdim inc.
</div>
<div id="bottom_black_band" style="padding-left: 8px; padding-top: 40px; font-size: 11; " class="bottom_black_band">
<a href="http://www.dimdim.com/index.php?option=com_wrapper&Itemid=78" style="color: white;">Dimdim Privacy Policy</a>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
<a href="http://www.dimdim.com/index.php?option=com_wrapper&Itemid=77" style="color: white;">Dimdim Trademark and Copyright Policy</a>
</div>
		<script src="http://www.google-analytics.com/urchin.js" type="text/javascript"></script>
		<script type="text/javascript">
			_uacct="UA-441834-1";
			urchinTracker();
		</script>
</body>
</html>
