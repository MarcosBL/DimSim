<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@ page import="java.util.*" %>
<%
	response.addHeader("Pragma", "no-cache"); 
	response.addHeader("Expires", "-1"); 
	response.addDateHeader("Last-Modified", (new Date()).getTime()); 
%>
<%@ page import="com.dimdim.util.misc.StringGenerator" %>
<%@ page import="com.dimdim.conference.ConferenceConsoleConstants" %>
<%@ taglib prefix="dm" uri="com.dimdim.conferenceConsole.tags" %>
<%
	String confKey = (String)request.getAttribute("confKey");
	String confName = (String)request.getAttribute("confName");
	String attendeePwd = (String)request.getAttribute("attendeePwd");
	String actionId = (String)request.getParameter("action");
	String startMeetingOptionAV = ConferenceConsoleConstants.getResourceKeyValue("start_meeting_option_av","1");
	String enableAudioOption = ConferenceConsoleConstants.getResourceKeyValue("start_meeting_option_enable_audio","1");
	String enableAVOption = ConferenceConsoleConstants.getResourceKeyValue("start_meeting_option_enable_av","1");
	String logoImageUrl = ConferenceConsoleConstants.getResourceKeyValue("start_page_logo_image_url","images/dimdim-logo.png");
	String logoImageWidth = ConferenceConsoleConstants.getResourceKeyValue("start_page_logo_image_width","138");
	String logoImageHeight = ConferenceConsoleConstants.getResourceKeyValue("start_page_logo_image_height","58");
	String videoChatSupported = ConferenceConsoleConstants.getVideoChatSupported();
	int maxmikes = ConferenceConsoleConstants.getMaxAttendeeAudios();
	
	if (actionId == null)
	{
		actionId = (String)request.getAttribute("action");
	}
	
	System.out.println("action id ="+actionId);
	System.out.println("attendeePwd ="+attendeePwd);
%>	
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
 <link href="style.css" rel="stylesheet" type="text/css">
<title>Welcome to Dimdim</title>
<script language="JavaScript1.2" type="text/javascript" src="js/prototype.js"></script>
<script language="JavaScript1.2" type="text/javascript" src="js/signin.js"></script>
<script language="javascript" type="text/javascript">
function change_tab(tab_id)
{
	document.getElementById('general').className = 'gwt-TabBar';
	document.getElementById('features').className = 'gwt-TabBar';
	document.getElementById('phone').className = 'gwt-TabBar';
	document.getElementById('form_general').className = 'Hide';
	document.getElementById('form_features').className = 'Hide';
	document.getElementById('form_phone').className = 'Hide';

	if(tab_id!='')
	{
		document.getElementById(tab_id).className = 'gwt-TabBarItem-selected';
		document.getElementById('form_'+tab_id).className = 'Show';
		if(tab_id == 'general')
		{
			document.getElementById('confKey').focus();
		}
		else if(tab_id == 'features')
		{
			document.getElementById('presenterAV').focus();
		}
		else
		{
			document.getElementById('internToll').focus();
		}
	}

}

function start_join_meeting(btns_value)
{
	document.getElementById('hostjoin_btnscontainer').className = 'Hide';
	document.getElementById(btns_value).className = 'Show';
	if(btns_value == 'host_container')
	{
		document.getElementById('tabs_pod_top').className = 'Hide';
		document.getElementById('break_line').className = 'Show';
		document.getElementById('confKey').focus();
	}
	else{document.getElementById('confKeyJoin').focus();}
}

</script>


<script langauage='javascript'>
	window.action_id = "<%=actionId%>";
	window.confKey = "<%=confKey%>";
	window.attendeePwd = "<%=attendeePwd%>";
	error4char= '<dm:I18NDisplayString component="forms" dictionary="ui_strings" key="meetingkey.length.message"/>';
</script>		
			
</head>
<body onload="javascript:getDefaults()">
<div id="container_login">
<br />
<br />
<div id="logo_top"></div><br />

<div id="main_content_panel"><br />
<!-- left pod starts here -->
<div id="left_part">
<br />
	<div id="blue_bar">
   <div style="padding:30px;width:300px;">
    <div id="webmeeting_txt">Web Meeting</div>
    <div id="date_time_txt">April 15, 2008</div>
    <script language="javascript" type="text/javascript">
    
function captureKeyEvent(myfield, e)
{
	var keycode;

	//alert('event = '+e);

	if (window.event) keycode = window.event.keyCode;
	else if (e) keycode = e.which;
	//else {
	//	login_mouseout(myfield);
	//	return true;
	//}

	//alert('key code = '+keycode);
	//13 for enter key and 32 for space
	if ( (keycode == 13 || keycode == 32) && (myfield.id == 'startmeeting_btn' || myfield.id == 'joinmeeting_btn'))
	{
		{
			if(myfield.id == 'startmeeting_btn')
			{
				start_join_meeting('host_container');
			}
			else{start_join_meeting('join_container')}
		}
	}	
	else{}
	
	
	if ( (keycode == 13) && (myfield.id == 'join_form' || myfield.id == 'start_form'))
	{
		if(myfield.id == 'join_form')
		{
				joinMeeting();
		}
		else{startMeeting();}
	}
	else{}

}
	
current_date_time();
function current_date_time()
{
	var m_names = new Array("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December");
	var d = new Date();
	var curr_date = d.getDate();
	var curr_month = d.getMonth();
	var curr_year = d.getFullYear();
	var current_date_time = m_names[curr_month] + " " + curr_date + ", " + curr_year
//	$('date_time_txt').update(current_date_time);
	$('date_time_txt').update(current_date_time);
//	alert(m_names[curr_month] + " " + curr_date + ", " + curr_year);
}
    </script>
    </div>
    </div>
<div id="image_people"></div>
<div class="gold_strip"></div>
</div>

<!-- left pod ends here -->


<!-- center pod starts here -->
<div id="center_part">
<div id="tabs_pod_top"></div>
<div id="break_line" class="Hide"><br /></div>
<div id="tabs_pod_middle">

<div id="hostjoin_btnscontainer">
<div class="btns_bg">
<br />
<br />
<br />
<br />
<div id="startmeeting_btn" onclick="javascript:start_join_meeting('host_container')" TABINDEX="40" onkeypress="javascript:captureKeyEvent(this,event);" title="Start Meeting"></div>
</div>

<div class="btns_bg">
<br />
<br />
<br />
<br />
<div id="joinmeeting_btn" onclick="javascript:start_join_meeting('join_container')" TABINDEX="41" onkeypress="javascript:captureKeyEvent(this,event);" title="Join Meeting"></div>
</div>
</div>
<script language="javascript" type="text/javascript">
document.getElementById('startmeeting_btn').focus();
</script>

<div id="host_container" class="Hide">
<div class="host_join_panel">

<div id="tabs_pod">
  <table width="97%" border="0" align="center" cellpadding="0" cellspacing="0">
    <tr>
      
      <td valign="bottom">
      	 <div class="gwt-TabBarItem-selected" id="general" onclick="javascript:change_tab('general')"  TABINDEX="0">
      			<div class="mul_bg">
                	<div class="mul_bg1">
                    	<div class="mul_bg2">
                        	<div id="general_txt"><dm:I18NDisplayString component="forms" dictionary="ui_strings" key="general.label"/></div>	
                        </div>
                    </div>      
			     </div>
           </div>       </td>

		<td valign="bottom">
      	 <div class="gwt-TabBar" id="features" onclick="javascript:change_tab('features')">
      			<div class="mul_bg">
                	<div class="mul_bg1">
                    	<div class="mul_bg2">
                        	<div id="features_txt"><dm:I18NDisplayString component="forms" dictionary="ui_strings" key="features.label"/></div> 
                        </div>
                    </div>      
			     </div>
           </div>       </td>
       
       <td valign="bottom">
      	 <div class="gwt-TabBar" id="phone" onclick="javascript:change_tab('phone')">
      			<div class="mul_bg">
                	<div class="mul_bg1">
                    	<div class="mul_bg2">
                        	<div id="phone_txt"><dm:I18NDisplayString component="forms" dictionary="ui_strings" key="phone.label"/></div>
                        </div>
                    </div>      
			     </div>
           </div>       </td>
       
     
    </tr>
  </table>
</div>
<div id="tabs_pod_top_curves"></div>
<div class="host_join_bg">

<form id="start_form" onkeypress="javascript:captureKeyEvent(this,event);" action="javascript:captureKeyEvent(this,event);" name="start_form">
<div id="form_general" class="Hde" style="padding:10px;">
  <table width="100%" border="0" cellspacing="4" cellpadding="4">
    <tr>
      <td width="30%" align="right"><dm:I18NDisplayString component="forms" dictionary="ui_strings" key="meetingkey.label"/></td>
      <td width="70%" align="left">
        <input type="text" name="confKey" id="confKey" class="TextBox_format" TABINDEX="1"/>       
      </td>
    </tr>
  </table>
  <br />
<div id="startmeeting" align="center" onclick="javascript:startMeeting()" TABINDEX="6" title="Start Meeting"></div>

<span class="advanced_label"><dm:I18NDisplayString component="forms" dictionary="ui_strings" key="advanced.label"/></span>	
  
  <table width="100%" border="0" cellspacing="4" cellpadding="4">
    <tr>
      <td width="30%" align="right"><dm:I18NDisplayString component="forms" dictionary="ui_strings" key="meetingname.label"/></td>
      <td width="70%" align="left">
      <input type="text" name="confName" id="confName" class="TextBox_format" TABINDEX="2" />
	  <input type="text" name="email" id="email" class="Hide"/>
	  <input type="text" name="displayName" id="displayName" class="Hide"/>
      </td>
    </tr>
    <tr>
      <td width="30%" align="right"><dm:I18NDisplayString component="forms" dictionary="ui_strings" key="attendees.label"/></td>
      <td width="70%" align="left">
      <textarea name="attendees" rows="2" id="attendees" TABINDEX="3"></textarea>
	</td>
    </tr>

    <tr>
      <td width="30%" align="right"><dm:I18NDisplayString component="forms" dictionary="ui_strings" key="attendeePwd.label"/></td>
      <td width="70%" align="left">
      <input type="text" name="attendeePwd" id="attendeePwd" class="TextBox_format" TABINDEX="4" />
      </td>
    </tr>
    <tr>
      <td width="30%" align="right">&nbsp;</td>
    <td width="70%" align="left"><span style="color:#225e98;"><dm:I18NDisplayString component="forms" dictionary="ui_strings" key="note"/></span></td>
    </tr>
    
    <tr>
      <td width="30%" align="right"><dm:I18NDisplayString component="forms" dictionary="ui_strings" key="preseterPwd.label"/></td>
      <td width="70%" align="left">
      <input type="text" name="preseterPwd" id="preseterPwd" class="TextBox_format" TABINDEX="5" />
      	</td>
    </tr>
      </table>
      <div id="msg_meetingpwd" align="center" class="red_font"></div>


</div>


<div id="form_features" class="Hide" style="padding:10px;">
  <table width="100%" border="0" cellspacing="3" cellpadding="2">
    <tr >
      <td width="40%" align="right"><dm:I18NDisplayString component="forms" dictionary="ui_strings" key="presenter_av.label"/></td>
      <td width="60%" align="left" colspan="3"> 
<!--  <select name="presenterAV" id="presenterAV" class="TextBox_format" onfocus="javascript:disableMic();" onchange="javascript:disableMic();"> -->
	  <select name="presenterAV" id="presenterAV" class="TextBox_format" onchange="javascript:disableMic();">
      <%if(enableAVOption.equalsIgnoreCase("0") && enableAudioOption.equalsIgnoreCase("0") && videoChatSupported.equalsIgnoreCase("false")) {%>  
	  <option value="disabled"><dm:I18NDisplayString component="forms" dictionary="ui_strings" key="presenter_av.disabled"/></option>                 
      <% }
      else if(enableAVOption.equalsIgnoreCase("0") && enableAudioOption.equalsIgnoreCase("0") && videoChatSupported.equalsIgnoreCase("true")) {%>	  
          <!-- <option value="videochat"><dm:I18NDisplayString component="forms" dictionary="ui_strings" key="presenter_av.videochat"/></option> -->
          <option value="disabled"><dm:I18NDisplayString component="forms" dictionary="ui_strings" key="presenter_av.disabled"/></option>                 
      <% }
      else if(enableAVOption.equalsIgnoreCase("0") && enableAudioOption.equalsIgnoreCase("1") && videoChatSupported.equalsIgnoreCase("false")) {%>	
           <option value="audio"><dm:I18NDisplayString component="forms" dictionary="ui_strings" key="presenter_av.audio"/></option>
           <option value="disabled"><dm:I18NDisplayString component="forms" dictionary="ui_strings" key="presenter_av.disabled"/></option>                 
      <% }
      else if(enableAVOption.equalsIgnoreCase("1") && enableAudioOption.equalsIgnoreCase("0") && videoChatSupported.equalsIgnoreCase("false")) {%>
      	   <option value="av"><dm:I18NDisplayString component="forms" dictionary="ui_strings" key="presenter_av.av"/></option>
           <option value="disabled"><dm:I18NDisplayString component="forms" dictionary="ui_strings" key="presenter_av.disabled"/></option>                 
      <% }
      else if(enableAVOption.equalsIgnoreCase("0") && enableAudioOption.equalsIgnoreCase("1") && videoChatSupported.equalsIgnoreCase("true")) {%>
              <option value="audio"><dm:I18NDisplayString component="forms" dictionary="ui_strings" key="presenter_av.audio"/></option>
              <!-- <option value="videochat"><dm:I18NDisplayString component="forms" dictionary="ui_strings" key="presenter_av.videochat"/></option> -->
              <option value="disabled"><dm:I18NDisplayString component="forms" dictionary="ui_strings" key="presenter_av.disabled"/></option>                 
      <% }
      else if(enableAVOption.equalsIgnoreCase("1") && enableAudioOption.equalsIgnoreCase("0") && videoChatSupported.equalsIgnoreCase("true")) {%>
      	      <option value="av"><dm:I18NDisplayString component="forms" dictionary="ui_strings" key="presenter_av.av"/></option>
              <!-- <option value="videochat"><dm:I18NDisplayString component="forms" dictionary="ui_strings" key="presenter_av.videochat"/></option> -->
              <option value="disabled"><dm:I18NDisplayString component="forms" dictionary="ui_strings" key="presenter_av.disabled"/></option>                 
      <% }
      else if(enableAVOption.equalsIgnoreCase("1") && enableAudioOption.equalsIgnoreCase("1") && videoChatSupported.equalsIgnoreCase("true")) {%>
	      <option value="av"><dm:I18NDisplayString component="forms" dictionary="ui_strings" key="presenter_av.av"/></option>
              <option value="audio"><dm:I18NDisplayString component="forms" dictionary="ui_strings" key="presenter_av.audio"/></option>
              <!--<option value="videochat"><dm:I18NDisplayString component="forms" dictionary="ui_strings" key="presenter_av.videochat"/></option> -->
              <option value="disabled"><dm:I18NDisplayString component="forms" dictionary="ui_strings" key="presenter_av.disabled"/></option>                 
       <% }
       else if(enableAVOption.equalsIgnoreCase("1") && enableAudioOption.equalsIgnoreCase("1") && videoChatSupported.equalsIgnoreCase("false")) {%>
              <option value="av"><dm:I18NDisplayString component="forms" dictionary="ui_strings" key="presenter_av.av"/></option>
              <option value="audio"><dm:I18NDisplayString component="forms" dictionary="ui_strings" key="presenter_av.audio"/></option>            
              <option value="disabled"><dm:I18NDisplayString component="forms" dictionary="ui_strings" key="presenter_av.disabled"/></option>                 
       <% } %>
      
	  <option value="video"><dm:I18NDisplayString component="forms" dictionary="ui_strings" key="presenter_av.videoonly"/></option>
        </select>
      </td>
    </tr>

   
    <script language="javascript" type="text/javascript">
    function on_off_options(id_value)
	{
	//	alert(id_value)
		var options_btns = document.getElementById(id_value);

//			alert(options_btns.options[0].value);

		if(	options_btns.options[0].selected == true)
		{
	//		alert('safasfs');
			options_btns.options[1].selected=true;
			document.getElementById(id_value+'_options').className = 'button-option-disable'
			document.getElementById(id_value+'_options').update(options_btns.options[1].text);
		}
		else
		{
			options_btns.options[0].selected=true;
			document.getElementById(id_value+'_options').className = 'button-option-enable'
			document.getElementById(id_value+'_options').update(options_btns.options[0].text);
		}	
	
	}
	
	function off_on_options(id_value)
	{
	//	alert(id_value)
		var options_btns = document.getElementById(id_value);

//			alert(options_btns.options[0].value);

		if(	options_btns.options[0].selected == true)
		{
//			alert('safasfs');
			options_btns.options[1].selected=true;
			document.getElementById(id_value+'_options').className = 'button-option-enable'
			document.getElementById(id_value+'_options').update(options_btns.options[1].text);
		}
		else
		{
			options_btns.options[0].selected=true;
			document.getElementById(id_value+'_options').className = 'button-option-disable'
			document.getElementById(id_value+'_options').update(options_btns.options[0].text);
		}	
	
	}
    </script>
    <tr >
      <td align="right"><dm:I18NDisplayString component="forms" dictionary="ui_strings" key="private_chat_enabled.label"/></td>
      <td align="left" valign="middle">
      <div id="privateChatEnabled_options" class="button-option-enable" onclick="javascript:on_off_options('privateChatEnabled')" TABINDEX="8"><dm:I18NDisplayString component="forms" dictionary="ui_strings" key="enable.label"/></div>
      <select name="privateChatEnabled" id="privateChatEnabled" class="TextBox_format Hide" >
      <option value="true"><dm:I18NDisplayString component="forms" dictionary="ui_strings" key="enable.label"/></option>
      <option value="false"><dm:I18NDisplayString component="forms" dictionary="ui_strings" key="disable.label"/></option>
                </select></td>
				
	  <td align="right"><dm:I18NDisplayString component="forms" dictionary="ui_strings" key="public_chat_enabled.label"/></td>
      <td align="left">
      <div id="publicChatEnabled_options" class="button-option-enable" onclick="javascript:on_off_options('publicChatEnabled')"><dm:I18NDisplayString component="forms" dictionary="ui_strings" key="enable.label"/></div>
      <select name="publicChatEnabled" id="publicChatEnabled" class="TextBox_format Hide" TABINDEX="9">
      <option value="true"><dm:I18NDisplayString component="forms" dictionary="ui_strings" key="enable.label"/></option>
      <option value="false"><dm:I18NDisplayString component="forms" dictionary="ui_strings" key="disable.label"/></option>
        </select></td>
    </tr>
	
    <tr>
      <td align="right"><dm:I18NDisplayString component="forms" dictionary="ui_strings" key="screen_share_enabled.label"/></td>
      <td align="left">
            <div id="screenShareEnabled_options" class="button-option-enable" onclick="javascript:on_off_options('screenShareEnabled')"><dm:I18NDisplayString component="forms" dictionary="ui_strings" key="enable.label"/></div>
      <select name="screenShareEnabled" id="screenShareEnabled" class="TextBox_format Hide" TABINDEX="10">
      <option value="true"><dm:I18NDisplayString component="forms" dictionary="ui_strings" key="enable.label"/></option>
      <option value="false"><dm:I18NDisplayString component="forms" dictionary="ui_strings" key="disable.label"/></option>
      </select></td>
	  
	        <td align="right"><dm:I18NDisplayString component="forms" dictionary="ui_strings" key="whiteboard_enabled.label"/> </td>
      <td align="left">
                  <div id="whiteboardEnabled_options" class="button-option-enable" onclick="javascript:on_off_options('whiteboardEnabled')"><dm:I18NDisplayString component="forms" dictionary="ui_strings" key="enable.label"/></div>
                  
      <select name="whiteboardEnabled" id="whiteboardEnabled" class="TextBox_format Hide" TABINDEX="11">
      <option value="true"><dm:I18NDisplayString component="forms" dictionary="ui_strings" key="enable.label"/></option>
      <option value="false"><dm:I18NDisplayString component="forms" dictionary="ui_strings" key="disable.label"/></option>
      </select></td>
    </tr>
	
    <tr>
      <td align="right"><dm:I18NDisplayString component="forms" dictionary="ui_strings" key="participant.list.label"/> </td>
      <td align="left">
	     <div id="participantListEnabled_options" class="button-option-enable" onclick="javascript:on_off_options('participantListEnabled')">						         <dm:I18NDisplayString component="forms" dictionary="ui_strings" key="enable.label"/></div>
      <select name="participantListEnabled" id="participantListEnabled" class="TextBox_format Hide" TABINDEX="12">
       <option value="true"><dm:I18NDisplayString component="forms" dictionary="ui_strings" key="enable.label"/></option>
      <option value="false"><dm:I18NDisplayString component="forms" dictionary="ui_strings" key="disable.label"/></option>      
      </select></td>
	   <td align="right"><dm:I18NDisplayString component="forms" dictionary="ui_strings" key="enablelobby.label"/></td>
      <td align="left">
       <div id="lobby_options" class="button-option-disable" onclick="javascript:off_on_options('lobby')">
       <dm:I18NDisplayString component="forms" dictionary="ui_strings" key="disable.label"/></div>
      <select name="lobby" id="lobby" class="TextBox_format Hide" TABINDEX="13">
   		<option value="false" selected="selected"><dm:I18NDisplayString component="forms" dictionary="ui_strings" key="disable.label"/></option>
   		<option value="true"><dm:I18NDisplayString component="forms" dictionary="ui_strings" key="enable.label"/></option>
      </select></td>
    </tr>
	
	<tr>
      <td align="right"><dm:I18NDisplayString component="forms" dictionary="ui_strings" key="mike.assingment.auto"/></td>
      <td align="left">
       <div id="assignMikeOnJoin_options" class="button-option-disable" onclick="javascript:off_on_options('assignMikeOnJoin')">
       <dm:I18NDisplayString component="forms" dictionary="ui_strings" key="disable.label"/></div>
      <select name="assignMikeOnJoin" id="assignMikeOnJoin" class="TextBox_format Hide" TABINDEX="13">
   		<option value="false" selected="selected"><dm:I18NDisplayString component="forms" dictionary="ui_strings" key="disable.label"/></option>
   		<option value="true"><dm:I18NDisplayString component="forms" dictionary="ui_strings" key="enable.label"/></option>
      </select></td>
	  
	   <td align="right"><dm:I18NDisplayString component="forms" dictionary="ui_strings" key="handsfree.auto"/></td>
      <td align="left">
       <div id="handsFreeOnLoad_options" class="button-option-disable" onclick="javascript:off_on_options('handsFreeOnLoad')">
       <dm:I18NDisplayString component="forms" dictionary="ui_strings" key="disable.label"/></div>
      <select name="handsFreeOnLoad" id="handsFreeOnLoad" class="TextBox_format Hide" TABINDEX="13">
   		<option value="false" selected="selected"><dm:I18NDisplayString component="forms" dictionary="ui_strings" key="disable.label"/></option>
   		<option value="true"><dm:I18NDisplayString component="forms" dictionary="ui_strings" key="enable.label"/></option>
      </select></td>
    </tr>
	
	<tr>
      <td align="right"><dm:I18NDisplayString component="forms" dictionary="ui_strings" key="attende.invite.lable"/></td>
      <td align="left">
       <div id="allowAttendeeInvites_options" class="button-option-disable" onclick="javascript:off_on_options('allowAttendeeInvites')">
       <dm:I18NDisplayString component="forms" dictionary="ui_strings" key="disable.label"/></div>
      <select name="allowAttendeeInvites" id="allowAttendeeInvites" class="TextBox_format Hide" TABINDEX="13">
   		<option value="false" selected="selected"><dm:I18NDisplayString component="forms" dictionary="ui_strings" key="disable.label"/></option>
   		<option value="true"><dm:I18NDisplayString component="forms" dictionary="ui_strings" key="enable.label"/></option>
      </select></td>
    </tr>
	
  </table>
<!--  <span style="color:#acacad"><dm:I18NDisplayString component="forms" dictionary="ui_strings" key="advanced.label"/></span>	-->
  <table width="100%" border="0" cellspacing="3" cellpadding="3">
    <tr>
      <td width="40%" align="right"><dm:I18NDisplayString component="forms" dictionary="ui_strings" key="videoquality.label"/></td>
      <td width="60%" align="left">
        <select name="networkProfile" id="networkProfile" class="TextBox_format" TABINDEX="14">
        <option value="1"><dm:I18NDisplayString component="forms" dictionary="ui_strings" key="network1.label"/></option>
        <option value="2" selected="selected"><dm:I18NDisplayString component="forms" dictionary="ui_strings" key="network2.label"/></option>
        <option value="3"><dm:I18NDisplayString component="forms" dictionary="ui_strings" key="network3.label"/></option>
        </select>
      </td>
    </tr>
    <tr>
      <td width="40%" align="right"><dm:I18NDisplayString component="forms" dictionary="ui_strings" key="meetinglen.label"/></td>
      <td width="60%" align="left">
        <table cellpadding="1" cellspacing="0">
	        <tr>
    	    	<td rowspan="1"align="left">
                	<select size="1" name="meetingHours" class="TextBox_format" TABINDEX="15">
                    	<option value="1">1</option>
                        <option value="2">2</option>
                        <option value="3">3</option>
                        <option value="4">4</option>
                        <option value="5">5</option>
                     </select>
                  </td>
                  <td rowspan="1" align="left">
                  	<dm:I18NDisplayString component="forms" dictionary="ui_strings" key="hour.label"/>
                  </td>
                  <td rowspan="1" align="left">
                    <select size="1" name="meetingMinutes" class="TextBox_format" TABINDEX="16">
                    	<option value="0">0</option>
                        <option value="15">15</option>
                        <option value="30">30</option>
                        <option value="45">45</option>
                     </select>
                  </td>
                  <td rowspan="1" align="left">	<dm:I18NDisplayString component="forms" dictionary="ui_strings" key="min.label"/>  </td>
                  </tr></table>                 
      </td>
    </tr>
    <tr>
      <td width="40%" align="right"><dm:I18NDisplayString component="forms" dictionary="ui_strings" key="return_url.label"/></td>
      <td width="60%" align="left">
      <input type="text" name="returnUrl" id="returnUrl" class="TextBox_format" TABINDEX="17" />
      </td>
    </tr>
  </table><br />
<div id="startmeeting" align="center" onclick="javascript:startMeeting()" TABINDEX="18" title="Start Meeting"></div>
</div>


<div id="form_phone" class="Hide" style="padding:10px;">
  <table width="100%" border="0" cellspacing="4" cellpadding="4">
      <tr>
      <td align="right"><dm:I18NDisplayString component="forms" dictionary="ui_strings" key="internToll.label"/></td>
      <td align="left"><input type="text"  maxlength="20" name="internToll" id="internToll" class="TextBox_format" TABINDEX="19"/></td>
    </tr>
    <tr>
      <td align="right"><dm:I18NDisplayString component="forms" dictionary="ui_strings" key="moderatorPassCode.label"/></td>
      <td align="left"><input type="text" maxlength="20" name="moderatorPassCode" id="moderatorPassCode" class="TextBox_format" TABINDEX="20" /></td>
    </tr>
    <tr>
      <td align="right"><dm:I18NDisplayString component="forms" dictionary="ui_strings" key="attendeePasscode.label"/></td>
      <td align="left"><input type="text"  maxlength="20" name="attendeePasscode" id="attendeePasscode" class="TextBox_format" TABINDEX="21"/></td>
    </tr>
    <tr>
      <td align="right"><dm:I18NDisplayString component="forms" dictionary="ui_strings" key="display.phone.label"/></td>
      <td align="left">
      
      <div id="dialInfoVisible_options" class="button-option-enable" onclick="javascript:on_off_options('dialInfoVisible')"><dm:I18NDisplayString component="forms" dictionary="ui_strings" key="enable.label"/></div>
      <select name="dialInfoVisible" id="dialInfoVisible" class="TextBox_format Hide" TABINDEX="22">
      <option value="true"><dm:I18NDisplayString component="forms" dictionary="ui_strings" key="enable.label"/></option>
      <option value="false"><dm:I18NDisplayString component="forms" dictionary="ui_strings" key="disable.label"/></option>
                </select></td>
    </tr>
    </table>
    <!-- 
    <span class="advanced_label"><dm:I18NDisplayString component="forms" dictionary="ui_strings" key="advanced.label"/></span>
     <table width="100%" border="0" cellspacing="4" cellpadding="4">
    <tr>
      <td width="40%" align="right"><dm:I18NDisplayString component="forms" dictionary="ui_strings" key="internTollFree.label"/></td>
      <td width="60%" align="left"><input type="text" name="internTollFree" id="internTollFree" class="TextBox_format" TABINDEX="23"/></td>
    </tr>
    <tr>
      <td width="40%" align="right"><dm:I18NDisplayString component="forms" dictionary="ui_strings" key="toll.label"/></td>
      <td width="60%" align="left">
      <input type="text" name="toll" id="toll" class="TextBox_format" TABINDEX="24"/>      </td>
    </tr>
        <tr>
      <td width="40%" align="right"><dm:I18NDisplayString component="forms" dictionary="ui_strings" key="tollFree.label"/></td>
      <td width="60%" align="left">
        <input type="text" name="tollFree" id="tollFree" class="TextBox_format" TABINDEX="25"/>      </td>
    </tr>

  </table>
  -->
  <br />
<div id="startmeeting" align="center" onclick="javascript:startMeeting()" TABINDEX="26" title="Start Meeting"></div>
</div>

<div id="msg_meeting_errors" align="center" class="red_font"></div>
 <input name="submit" type="button" class="Hide" id="host_hidden" onkeypress="captureKeyEvent(this,event);"/>
</form>


</div>
</div>
</div>

<div id="join_container" class="Hide">
<div id="join_pod" class="host_join_bg"><br />
<div>
<br />
<form id="join_form" onkeypress="javascript:captureKeyEvent(this,event);" action="javascript:captureKeyEvent(this,event);">
<table width="100%" border="0" cellspacing="4" cellpadding="4">
    <tr>
      <td width="40%" align="right"><dm:I18NDisplayString component="forms" dictionary="ui_strings" key="meetingkey.label.attendee"/></td>
      <td width="60%" align="left">
        <input type="text" name="confKey" id="confKeyJoin" class="TextBox_format"  TABINDEX="27"/>      </td>
    </tr>
    <tr>
      <td width="40%" align="right"><dm:I18NDisplayString component="forms" dictionary="ui_strings" key="display.name.label"/></td>
      <td width="60%" align="left">
      <input type="text" name="displayName" id="displayNameJoin"class="TextBox_format"  TABINDEX="28"/>      </td>
   </tr>	
  </table>
  
  <br />
<br />
<br />
<br /><br />
<div id="join_status_pod" >
<div id="space_join"><br /><br /><br /><br /></div>
<div id="msg1" class="Hide"><dm:I18NDisplayString component="forms" dictionary="ui_strings" key="Meeting Key Required"/></div>
<div id="msg2" class="blue_font"></div>
<div id="msg3" class="Hide"><dm:I18NDisplayString component="forms" dictionary="ui_strings" key="attendeePwd.label"/>
    <input type="text" name="attendeePwd" id="attendeePwdJoin"class="TextBox_format"  TABINDEX="29"/>
</div>
<div id="joinmeeting" align="center" onclick="javascript:joinMeeting()"  TABINDEX="30" title="Join Meeting"></div>
</div>
 <input name="submit1" type="button" class="Hide" id="join_hidden" onkeypress="captureKeyEvent(this,event);"/>
  </form>
  <div id="msg_meeting_join_errors" align="center" class="red_font"></div>
  </div>
</div>
</div>


</div>
<div id="tabs_pod_bottom"></div>

</div>

<!-- center pod ends here -->

<div id="right_part"><br />
<div id="green_bar"></div>
<div class="gold_strip"></div>
</div>

</div>

<div id="bottom_white"></div>
</div>

<div></div>
</body>
</html>
