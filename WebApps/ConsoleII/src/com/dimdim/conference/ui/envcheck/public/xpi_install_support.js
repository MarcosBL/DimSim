

var timer;
/**
 *	This function checks for dimdim publisher firefox plugin and returns 1 if found
 *	and 0 if not found.
 */
 
		function checkInstallation()
		{
			var bFound = 0;
			if (navigator.plugins && navigator.plugins.length > 0)
			{
				//alert('navigator.plugins.length ='+navigator.plugins.length);
				for (i = 0; i < navigator.plugins.length; i++)
				{
					plugin = navigator.plugins[i];
					if (plugin.name == "Dimdim Web Meeting Screencaster Firefox Plugin")
					{
						bFound = 1;
						break;
					}
				}
				//alert('bFound = '+bFound)
				return bFound;
			}
		}
	
	
/**
 *	This method returns:
 *	
 *	1 = Publisher does not exists.
 *	2 = Publisher exists but is lower version
 *	3 = Publisher exists and is required version or higher
 */
 
		function checkFirefoxDimdimPublisher(major,minor,build,build2)
		{
			var ret = 1;
			if ((navigator.userAgent.indexOf("Firefox")!=-1) && (navigator.userAgent.indexOf("Gecko")!=-1))
			{
				var requiredVersion = ( (build2 % 100) + (build % 100) * 100 + (minor % 100) * 10000 + (major % 100) * 1000000);

				if (checkInstallation() == 1)
				{

				//	The control exists. Now the install process for new install and upgrade are different
				//	So forward the user accordingly.
					ret = 2;
					for (i = 0; i < navigator.plugins.length; i++)
					{
						plugin = navigator.plugins[i];
						if (plugin.name == "Dimdim Web Meeting Screencaster Firefox Plugin")
						{
							var stringarray = plugin.description.split(".");
							var installedVersion = (stringarray[3] % 100) + ((stringarray[2] % 100) * 100) + ((stringarray[1] % 100) * 10000) + ((stringarray[0] % 100) * 1000000);
							//alert('installedVersion ='+installedVersion +'length of installed version = '+installedVersion.length);
							//alert('requiredVersion ='+requiredVersion);
							if (installedVersion >= requiredVersion)
							{
								ret = 3;
							}
							else
							{
								ret = 2;
							}
							break;
						}
					}
				}
				else
				{
					ret = 1;
				}
				//alert("inside checkFirefoxDimdimPublisher return value = "+ret);
				return	ret;
			}
		}
		
		
		function checkMacInstallation()
		{
			navigator.plugins.refresh(false);
			if (navigator.plugins && navigator.plugins.length > 0)
			{
				var bFound = 0;
				for (i = 0; i < navigator.plugins.length; i++)
				{
					plugin = navigator.plugins[i];
					//alert('plugin.name = '+plugin.name);
					if (plugin.name.indexOf("Dimdim Publisher")!=-1 || plugin.name.indexOf("Web Meeting")!=-1)
					{
						//alert('plugin.description '+plugin.description);
						bFound = 1;
						break;
					}
				}

				return bFound;
			}
		}
		
		
		/**
 *	This method returns:
 *	
 *	1 = Publisher does not exists.
 *	2 = Publisher exists but is lower version
 *	3 = Publisher exists and is required version or higher
 */
 
		function checkMacDimdimPublisher(major,minor,build,build2)
		{
			//alert('checkMacDimdimPublisher');
			var ret = 1;
			//alert('navigator.userAgent = '+navigator.userAgent);
			//if ((navigator.userAgent.indexOf("Firefox")!=-1) && (navigator.userAgent.indexOf("Gecko")!=-1))
			//{
				var requiredVersion = ( (build2 % 100) + (build % 100) * 100 + (minor % 100) * 10000 + (major % 100) * 1000000);

				if (checkMacInstallation() == 1)
				{

				//	The control exists. Now the install process for new install and upgrade are different
				//	So forward the user accordingly.
					//alert('found..');
					ret = 2;
					for (i = 0; i < navigator.plugins.length; i++)
					{
						plugin = navigator.plugins[i];
						if (plugin.name.indexOf("Dimdim Publisher")!=-1 || plugin.name.indexOf("Web Meeting")!=-1)
						{
							var stringarray = plugin.description.split(".");
							//alert('description ='+stringarray);
							var installedVersion = (stringarray[3] % 100) + ((stringarray[2] % 100) * 100) + ((stringarray[1] % 100) * 10000) + ((stringarray[0] % 100) * 1000000);
							//alert('installedVersion ='+installedVersion +'length of installed version = '+installedVersion.length);
							//alert('requiredVersion ='+requiredVersion);
							if (installedVersion >= requiredVersion)
							{
								ret = 3;
							}
							else
							{
								ret = 2;
							}
							break;
						}
					}
				}
				else
				{
					//alert('did not find the plugin');
					ret = 1;
				}
				//alert('returning = '+ret);
				return	ret;
			//}
		}

function launchMacURL(major, minor, build, build2, url)
{	
	//alert('inside launch url....');
	
	var iframe1; 
	var installUrl = url;
	var requiredVersion = ( (build2 % 100) + (build % 100) * 100 + (minor % 100) * 10000 + (major % 100) * 1000000);
	//alert('inside mac....install..installUrl = '+installUrl);	
	iframe1 = document.createElement("iframe");
	iframe1.setAttribute("src", installUrl);
	iframe1.setAttribute("id", "1");
	iframe1.setAttribute("id", "1");
	iframe1.setAttribute("scrolling", "no");
	iframe1.setAttribute("frameBorder", "0");
	iframe1.setAttribute("height","0");
	iframe1.setAttribute("width","0");
	
	if(document.body)
	{
		document.body.appendChild(iframe1);
	}
	iframe1=null;
	timer = setInterval ( "checkMac("+requiredVersion+")", 500 );
	
}

function checkMac(requiredVersion)
{
//in mac refreshing has to be done so that the installed plugin is reflected in navigator
	navigator.plugins.refresh(false);
	var d = document.getElementById('pubobj');
	//alert('inside checMac...'+d);
  	try
	{
		for (i = 0; i < navigator.plugins.length; i++)
		{
			plugin = navigator.plugins[i];
			if (plugin.name.indexOf("Dimdim Publisher")!=-1 || plugin.name.indexOf("Web Meeting")!=-1)
			{
				var stringarray = plugin.description.split(".");
				//alert('description ='+stringarray);
				var installedVersion = (stringarray[3] % 100) + ((stringarray[2] % 100) * 100) + ((stringarray[1] % 100) * 10000) + ((stringarray[0] % 100) * 1000000);
				//alert('installedVersion ='+installedVersion +'length of installed version = '+installedVersion.length);
				//alert('requiredVersion ='+requiredVersion);
				if (installedVersion >= requiredVersion)
				{
					navigator.plugins.refresh(false);
					checkPassedMac();
				}
			}
		}
	}
	catch(e)
	{
		//alert('inside checkMac = '+e);
		//parent.checkFailed();
	}
}

function checkPassedMac()
{	
	//alert('publisher installed....success_url ='+window.success_url);
	checkDone = true;
	clearInterval(timer);
	//window.parent.publisher_availability = "available";
	//window.parent.publisher_available = "true";
	if(null != window.success_url && 'null' != window.success_url){
		window.location.href = window.success_url;
	}
}

function launchWindowsFFURL(major, minor, build, build2, url)
{	
	//alert('inside launchWindowsFFURL....');
	
	var iframe1; 
	var installUrl = url;
	var requiredVersion = ( (build2 % 100) + (build % 100) * 100 + (minor % 100) * 10000 + (major % 100) * 1000000);
	//alert('inside windows FF..installUrl = '+installUrl);	
	iframe1 = document.createElement("iframe");
	iframe1.setAttribute("src", installUrl);
	iframe1.setAttribute("id", "1");
	iframe1.setAttribute("id", "1");
	iframe1.setAttribute("scrolling", "no");
	iframe1.setAttribute("frameBorder", "0");
	iframe1.setAttribute("height","0");
	iframe1.setAttribute("width","0");
	
	if(document.body)
	{
		document.body.appendChild(iframe1);
	}
	iframe1=null;
	timer = setInterval ( "checkWindowsFF("+requiredVersion+")", 500 );
	
}

function checkWindowsFF(requiredVersion)
{
//in mac refreshing has to be done so that the installed plugin is reflected in navigator
	//alert('just before calling refresh...');
	navigator.plugins.refresh(false);
	//alert('just before calling refresh...');
  	try
	{
		for (i = 0; i < navigator.plugins.length; i++)
		{
			plugin = navigator.plugins[i];
			if (plugin.name.indexOf("Dimdim Web Meeting Screencaster Firefox Plugin")!=-1 )
			{
				var stringarray = plugin.description.split(".");
				//alert('description ='+stringarray);
				var installedVersion = (stringarray[3] % 100) + ((stringarray[2] % 100) * 100) + ((stringarray[1] % 100) * 10000) + ((stringarray[0] % 100) * 1000000);
				//alert('installedVersion ='+installedVersion +'length of installed version = '+installedVersion.length);
				//alert('requiredVersion ='+requiredVersion);
				if (installedVersion >= requiredVersion)
				{
					navigator.plugins.refresh(false);
					checkPassedWindowsFF();
				}
			}
		}
	}
	catch(e)
	{
		//alert('inside checkFF = '+e);
		//parent.checkFailed();
	}
}

function checkPassedWindowsFF()
{	
	//alert('publisher installed....success_url ='+window.success_url);
	checkDone = true;
	clearInterval(timer);
	//window.parent.publisher_availability = "available";
	//window.parent.publisher_available = "true";
	
	if(null != window.success_url && 'null' != window.success_url){
		window.location.href = window.success_url;
	}
}
