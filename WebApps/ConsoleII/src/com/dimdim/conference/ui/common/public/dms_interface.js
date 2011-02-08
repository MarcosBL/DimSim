//
//	All the dms responses to the essentialy required cross domain calls
//	must contain 1 js function as follows:
//	function publishDMSResponse()
//	{
//		JSInterface.callGWT3('<string 1>','<string 2>','<string 3>');
//	}
//

var pollInterval  = 20;
var remoteScriptId = 'jpjp';

function cobCallback(s1)
{
	//alert('inside sampleMethod '+s1 + s2+s3);
	JSInterfaceCobrowse.callGWT4(s1);
}

function dmsReturn(s1,s2,s3,s4)
{
	//alert('inside dms_interface callGWT');
	JSInterface.callGWT3(s1,s2,s3,s4);
}

function onFrameBodyScroll(horScroll, verScroll)
{
	//alert('inside onFrameBodyScroll '+horScroll + verScroll);
	JSInterfaceCobrowse.scroll(horScroll+"", verScroll+"");
}

callDMS = function(url)
{
		// create a new node to host the remote script
		var remoteScript=document.createElement('script');
		remoteScript.setAttribute('id', remoteScriptId);
		remoteScript.setAttribute('name', remoteScriptId);
		remoteScript.setAttribute('type','text/javascript');
		remoteScript.setAttribute('src',url);
		var hd=document.getElementsByTagName('head')[0];
		
		// Gotcha: set attribute and src BEFORE appending, or Safari won't work
		hd.appendChild(remoteScript);
}

//	klick: execute remote function, remove the remote script
//	if exists. The script execution may take some time depending on the
//	connection speed and server speed.
checkDMSResponse = function(code)
{
//	if (publishDMSResponse)
//	{
	var ret = "ok";
	var remove = false;
	//alert('checkDMSResponse --- remoteScriptId '+remoteScriptId);
	var el = document.getElementById(remoteScriptId);
	if (el)
	{
		try
		{
			// getStuff is a function in the remote script
			// which will call doStuffWith() here
			//alert('checkDMSResponse --- bwefore publishDMSResponse el '+el);
			publishDMSResponse();
			remove = true;
		}
		catch(e)
		{
			//alert('checkDMSResponse --- inside exception '+e);
			remove = false;
			JSInterface.callGWT3('DMS',code,'WAIT','WAIT');
		}
	}
	else
	{
		JSInterface.callGWT3('DMS',code,'WAIT','WAIT');
	}
//	if (remove)
//	{
//		try
//		{
//			var removed = el.parentNode.removeChild(el);
//			if (!removed)
//			{
//				ret = "Remove Error";
//				alert("Node not removed");
//			}
//			else
//			{
//				alert("Node removed");
//			}
//			var hd=document.getElementsByTagName('head')[0];
//			hd.removeChild(el);
//		}
//		catch(ee)
//		{
//		}
//	}
//	}
//	else
//	{
//		JSInterface.callGWT3('DMS','na','na','na');
//	}
	return	ret;
}

clearDMSResponse = function()
{
	// and remove it
	removeScript(remoteScriptId);
}

// remove script node
function removeScript(id)
{
	var hd=document.getElementsByTagName('head')[0];
	try
	{
		var el = document.getElementById(remoteScriptId);
		if (el)
		{
//			hd.removeChild(el);
			el.parentNode.removeChild(el);
		}
	}
	catch(e)
	{
	}
}


