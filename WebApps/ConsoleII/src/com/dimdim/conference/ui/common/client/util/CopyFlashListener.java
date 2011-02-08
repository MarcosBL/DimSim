package com.dimdim.conference.ui.common.client.util;

import com.dimdim.conference.ui.common.client.common.TimeSensitiveInfo;
import com.dimdim.conference.ui.model.client.ConferenceGlobals;
import com.dimdim.conference.ui.model.client.JSCallbackListener;
import com.dimdim.conference.ui.model.client.UIResources;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;


public class CopyFlashListener implements	JSCallbackListener{

	protected	static	CopyFlashListener	copyListener;
	
	public String getListenerName() {
		return "COPY_MOVIE_EVENT";
	}

	public void handleCallFromJS(String data) {
		//Window.alert("handleCallFromJS = "+data);
		
	}

	public void handleCallFromJS2(String data1, String data2) {
		//Window.alert("handleCallFromJS2 = "+data1 + "  "+data2);
		showDialog(data2);
	}

	public void handleCallFromJS3(String data1, String data2, String data3) {
		//Window.alert("handleCallFromJS3 = "+data1 + " "+data2);
		
	}
	
	
	public	static	CopyFlashListener	getListener()
	{
		if (CopyFlashListener.copyListener == null)
		{
			CopyFlashListener.copyListener = new CopyFlashListener();
		}
		//Window.alert("inside copyListener "+copyListener.getListenerName());
		return	CopyFlashListener.copyListener;
	}
	
	public void showDialog(String data2) {
		
		String info = ConferenceGlobals.getDisplayString("console.copy.url.success","URL successfully copied");
		UIResources  uiResources = UIResources.getUIResources();
		String	joinURL = uiResources.getConferenceInfo("joinURL");
		joinURL = decodeUrl(joinURL);
		String urlWithSpace = getSpacedUrl(joinURL);
		info = info.replaceAll("XXX", urlWithSpace);
		
		//Window.alert(retVal+"");
		if("1".equalsIgnoreCase(data2))
		{
			//Window.alert("text successfully copied");
			
		}else{
			//Window.alert("text could not be copied");
			info = ConferenceGlobals.getDisplayString("console.copy.url.fail","URL could not be  copied");
		}
		TimeSensitiveInfo tfd = new TimeSensitiveInfo(info, 2000, false);
	    //dlg = new TimeSensitiveInfo("Attempting to Start Desktop Share...");
	    tfd.setCaption(ConferenceGlobals.getDisplayString("console.copy.url.header","Info..."), false);
	    //tfd.add(hp);
	    //Window.alert("got a dialog...drawing it..");
	    tfd.show();
	    
	}
	
	private String  getSpacedUrl(String url) {
		int length = url.length();
		StringBuffer returnString = new StringBuffer();
		int i = 0;
		for(; i < length+30 ; )
		{
			if( (i + 30) < length)
			{
				returnString.append(url.substring(i, i+30));
				returnString.append(" ");
			}else{
				returnString.append(url.substring(i, length));
			}
			i += 30;
		}
		return returnString.toString();
	}
	
	private native String decodeUrl(String url)
	/*-{
	 return unescape(url);
	}-*/;
}
