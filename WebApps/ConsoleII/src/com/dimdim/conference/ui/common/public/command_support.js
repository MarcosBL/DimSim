/**
 *	This function is not required to traverse the frame heirarchy
 *	at present. It can be added easily enough if required. The get
 *	top window function is available in the flash_bridge.js.
 *	
 *		var w = getTopWindow(window);
 */

clickOnNodeWithId = function(nodeId)
{
		var d = window.document;
		var ec = d.getElementById(nodeId);
		if (ec)
		{
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
		}
		else
		{
			alert("Node "+nodeId+" not found");
		}
}

