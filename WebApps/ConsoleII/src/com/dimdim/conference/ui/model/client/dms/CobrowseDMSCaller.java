package com.dimdim.conference.ui.model.client.dms;

import com.dimdim.conference.ui.common.client.common.TimeSensitiveInfo;
import com.dimdim.conference.ui.json.client.UIResourceObject;
import com.dimdim.conference.ui.model.client.ConferenceGlobals;
import com.dimdim.conference.ui.model.client.ResourceSharingDisplay;
import com.dimdim.conference.ui.model.client.helper.CobrowseCallInterface;
import com.dimdim.conference.ui.model.client.helper.FlashXmlCallListener;

public class CobrowseDMSCaller implements	FlashXmlCallListener{

	String url = "";
	public	static	final	String	COBRO_SYNC = "COBRO_SYNC";
	ResourceSharingDisplay resDisplay = null;
	UIResourceObject res = null;
	boolean lock = false;
	
	public CobrowseDMSCaller(String url, ResourceSharingDisplay resDisplay, UIResourceObject res)
	{
		this.url = url;
		this.resDisplay = resDisplay;
		this.res = res;
	}
	
	public void onCompletion(String result, String responseText) {
		CobrowseCallInterface.getInterface().removeUrlListener(COBRO_SYNC);
		//Window.alert("response = "+responseText);
		//Window.alert("onCompletion responseText = "+responseText);
		if("SUCCESS".equalsIgnoreCase(result))
		{
			res.setUrl(responseText);
			resDisplay.onSharingStarted(res);
			
			if(this.lock)
			{
				//Window.alert("inside CobrowseDMSCaller locking after showing the cob");
				resDisplay.lock(true);
			}
		}else{
			String info = ConferenceGlobals.getDisplayString("cob.res.timeout.error","Could not contact server, Please try again.");
			TimeSensitiveInfo tfd = new TimeSensitiveInfo(info, 2000, false);
			tfd.setCaption(ConferenceGlobals.getDisplayString("error.label","Error"), false);
			//tfd.add(hp);
			//Window.alert("got a dialog...drawing it..");
			tfd.show();
		}
		
	}

	public	void	getSyncUrlWithLock(boolean lock)
	{
		this.lock = lock;
		CobrowseCallInterface.getInterface().addUrlListener(COBRO_SYNC,this);
		//Window.alert("in cobrowse caller "+FlashXmlCallInterface.getInterface().getUrlListeners().get(COBRO_SYNC));
		CobrowseCallInterface.getInterface().callXmlUrlInFlash(COBRO_SYNC, url,10000);
	}
	
	public	void	getSyncUrl()
	{
//		this.timer.schedule(this.delayInterval);
		//String dmsUrl = "http://192.168.1.39:81/cobrowsing";
		//StringBuffer buf = new StringBuffer();
		//Window.alert("url = "+url);
		CobrowseCallInterface.getInterface().addUrlListener(COBRO_SYNC,this);
		//Window.alert("in cobrowse caller "+FlashXmlCallInterface.getInterface().getUrlListeners().get(COBRO_SYNC));
		CobrowseCallInterface.getInterface().callXmlUrlInFlash(COBRO_SYNC, url,10000);
	}
}
