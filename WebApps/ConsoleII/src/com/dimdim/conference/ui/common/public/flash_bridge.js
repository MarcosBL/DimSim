
window.streamName_from_flash = "";
window.eventName_from_flash = "";

function callFromFlash(streamName,eventName)
{
	JSInterface.callGWT2('PPT_EVENT',streamName,eventName);
/*	
	alert("PPT EVENT:"+streamName+"--"+eventName);
		var w = getTopWindow(window);
		var d = w.document;
		w.streamName_from_flash = streamName+"";
		w.eventName_from_flash = eventName+"";
		var ec = d.getElementById("flash_bridge");
		var isIE = navigator.appName.indexOf("Microsoft") != -1;
		if (isIE)
		{
			ec.fireEvent("onclick");
		}
		else
		{
			var evt = document.createEvent("MouseEvents");
			evt.initMouseEvent("click", true, false, window,
					0, 0, 0, 0, 0, false, false, false, false, 0, null);
			var ret = ec.dispatchEvent(evt);
		}
*/
}

function logMessageFromFlash(streamName,msg)
{
	JSInterface.callGWT2('PPT_EVENT','FLASH_MESSAGE_LOG',streamName+"::"+msg);
}

function showCopyMessage(s1)
{
	//alert('inside flash bridge showCopyMessage '+s1);
	try{
		//JSInterfaceCobrowse.init();
		JSInterface.callGWT2('COPY_MOVIE_EVENT','FLASH_MESSAGE_LOG',+s1);
	}catch(e){
		alert(e);
	}
}

/**
 *	This returns the top most window, This is currently used by console 
 *	to set the return url to to the top most window
 */
function getTopWindow(w)
{
	if (w.parent == w)
	{
		return w;
	}
	else
	{
		return getTopWindow(w.parent);
	}
}

/**
 *	This mean that the external interface registration from within the
 *	flash movie control is complete.
 */
 
function flashMovieLoaded()
{
}

