// JavaScript Document

function getDefaults(){
	//alert("getdefualts window.action_id = "+window.attendeePwd);
	if("join" == window.action_id)
	{
		start_join_meeting('join_container');
		if('null' != window.confKey)
		{
			$("confKeyJoin").value = window.confKey;
		}
		if('null' != window.attendeePwd)
		{
			$("attendeePwdJoin").value = window.attendeePwd;
		}
	}else{
		var url = 'GetFormDefaults.action';
		var params = 'actionId=host';

		var myAjax = new Ajax.Request(
				url,
				{
					method: 'get',
					parameters: params,
					onComplete: fillDefaults
				});
	}
		
}

function disableMic(){

	//var presenterAV=document.getElementById("presenterAV").value;
	//alert(presenterAV);
	//if(presenterAV == "disabled")
	//  document.getElementById('maxmikes').disabled = true;
	//else
	//  document.getElementById('maxmikes').disabled = false;
	//if(presenterAV == "videochat")
	//{
//	  document.getElementById('msg_meeting_errors').innerHTML = 'You have selected video chat. This will allow 2 users to share video only, no additional mic can be shared.';
	 // alert("You have selected video chat. This will allow 2 users to share video only, no additional mic can be shared.");	  
	//}
	
}

function fillDefaults(originalRequest)
{
	//alert("Comes inside show response register"+originalRequest);
	//alert('originalRequest = '+originalRequest.responseText);
	//alert('originalRequest = '+originalRequest);
	
	var data = originalRequest.responseText;
	//alert('data = '+data);
	var respObject = eval('(' + data + ')');
	
	//alert(' data.confkey = '+respObject.confKey);
	//alert(' data.email = '+respObject.email);
	$("confKey").value = respObject.confKey;
	$("confName").value = respObject.confName;
	$("email").value = respObject.email;
	$("displayName").value = respObject.displayName;
	
	$("toll").value = respObject.toll;
	$("tollFree").value = respObject.tollFree;
	$("internToll").value = respObject.internToll;
	$("internTollFree").value = respObject.internTollFree;
	$("moderatorPassCode").value = respObject.moderatorPassCode;
	$("attendeePasscode").value = respObject.attendeePasscode;
	$("returnUrl").value = respObject.returnUrl;
}


function startMeeting()
{
	var url = '../../StartNewConferenceCheck.action';
	var params = $('start_form').serialize();
	var roomname = $("confKey").value;	
	var hostkey = $("preseterPwd").value;	
	var meetingkey = $("attendeePwd").value;	
	if(roomname!='')
	{
		if(alphanumeric(roomname))
		{
			if(roomname.length >= 4)
			{
			if(hostkey!='')
			{
				if(alphanumeric(hostkey))
				{
					
				}
				else{
//					alert('Please check host key');
					document.getElementById('msg_meeting_errors').innerHTML = 'Please check host key';
					document.getElementById('preseterPwd').focus();
					return;
					}
				
			}
			
			if(meetingkey!='')
			{				
				if(alphanumeric(meetingkey))
				{
					
				}
				else{
//						alert('Please check meeting key');
					  document.getElementById('msg_meeting_errors').innerHTML = 'Please check meeting key';
						document.getElementById('attendeePwd').focus();
						return;
					}
			}
			
// validation for phone tab starts here

			var phones = new Array("internToll","moderatorPassCode","attendeePasscode");
//			alert(phones.length);
			for(var i=0;i<phones.length;i++)
			{
				if(alphanumeric_phone($(phones[i]).value))
				{
//					alert($(phones[i]).value);
				}
				else					
				{
					alert('please check ' + phones[i])
					$(phones[i]).focus();
					return;
				}
			}					
			
// validation for phone tab ends here			


			var myAjax = new Ajax.Request(
								url,
								{
								method: 'get',
								parameters: params,
								onComplete: redirectToUrl
							});	
			}else{
				document.getElementById('msg_meeting_errors').innerHTML = error4char;
				document.getElementById('confKey').focus();
				}
		}
		else{
//				alert('Please check room name');				
				document.getElementById('msg_meeting_errors').innerHTML = 'Room Name can contain only Alpha numeric, @, _ , $ characters';
				document.getElementById('confKey').focus();
			}
	}
	else{
//		alert('Please Enter room name');
	  document.getElementById('msg_meeting_errors').innerHTML = 'Please Enter Room Name';
		document.getElementById('confKey').focus();
		}

}


function alphanumeric(feild_value)
{
	var numaric = feild_value;
	for(var j=0; j<numaric.length; j++)
		{
		  var alphaa = numaric.charAt(j);
		  var rn = alphaa.charCodeAt(0);
	//	  alert(numaric.charAt(0).charCodeAt(0));
			if(numaric.charAt(0).charCodeAt(0) != 46)
			{
				  if((rn > 47 && rn<58) || (rn > 64 && rn<91) || (rn > 96 && rn<123) || rn==64 || rn==95 || rn==36 || rn==46)
				  {
							  
				  }
				  else	{ return false;	}
			}
		    else	{ return false;	}
		}
 return true;
}


function alphanumeric_phone(feild_value)
{
	var numaric = feild_value;
	for(var j=0; j<numaric.length; j++)
		{
		  var alphaa = numaric.charAt(j);
		  var rn = alphaa.charCodeAt(0);
		  if((rn > 47 && rn<58) || (rn > 64 && rn<91) || (rn > 96 && rn<123) || rn==35 || rn==43 || rn==45 || rn==32)
		  {
		  
		  }
	      else	{ return false;	}
		}
 return true;
}


function redirectToUrl(originalRequest)
{
//	alert('originalRequest = '+originalRequest);
	var data = originalRequest.responseText;
//	alert('data = '+data);
	var respObject = eval('(' + data + ')');
	var metting_msg = respObject.message
//	alert(metting_msg);
	if('success' == respObject.result)
	{
		$('msg_meetingpwd').update('&nbsp;');
		window.location=respObject.url;
	}
	else{	$('msg_meetingpwd').update(metting_msg);}
}


function joinMeeting(){
	var url = '../../JoinConferenceCheck.action';
	var params = $('join_form').serialize();
	var roomname = $("confKeyJoin").value;	
	var meetingkey = $("attendeePwdJoin").value;	
	if(roomname != '')
	{
		if(alphanumeric(roomname))
		{
			if(meetingkey!='')
			{
				if(alphanumeric(meetingkey))
				{
					
				}
				else
				{
//					alert('Please check meeting key');
				  document.getElementById('msg_meeting_join_errors').innerHTML = 'Please check meeting key';
					document.getElementById('attendeePwdJoin').focus();
					return;
				}
				
			}
			else{}
		}
		else{	
//				alert('Please check room name');
			  document.getElementById('msg_meeting_join_errors').innerHTML = 'Room Name can contain only Alpha numeric, @, _ , $ characters';
				document.getElementById('confKeyJoin').focus();
				return;}
				
				
		var myAjax = new Ajax.Request(
					url,
					{
						method: 'get',
						parameters: params,
						onComplete: joinMeetingHandler
					});
	}
	else{
	  document.getElementById('msg_meeting_join_errors').innerHTML = 'Please enter Room Name';
	//	alert('Please enter room name')
		}
}

function joinMeetingHandler(originalRequest)
{
	//alert('originalRequest = '+originalRequest);
	var data = originalRequest.responseText;
	//	alert('data = '+data);
	var respObject = eval('(' + data + ')');
	if('PASSWORD_REQUIRED'== respObject.result)
	{
		$('space_join').className='Hide';
		$('msg2').update(respObject.message);
		$('msg1').className='Show red_font';
		$('msg3').className='Show';
				
	}else if('success'== respObject.result){
	//	alert('set the browser url to this:'+respObject.url);
		window.location=respObject.url;
	}else{
		$('space_join').className='Hide';
		$('msg2').update('<br><br>'+respObject.message); 
	}
}