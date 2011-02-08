package com.dimdim.conference.ui.common.client.util;

import com.dimdim.conference.ui.common.client.common.TimeSensitiveInfo;
import com.dimdim.conference.ui.model.client.ConferenceGlobals;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Widget;

public class CopyToClipBoard implements ClickListener
{

	String textBoxToCopy = null;
	public CopyToClipBoard(String copyText)
	{
		textBoxToCopy = copyText;
	}
	public void onClick(Widget sender) {
		//Window.alert("text to be copied text = "+textBoxToCopy.getText());
		String info = ConferenceGlobals.getDisplayString("console.copy.url.success","URL successfully copied");
		String urlWithSpace = getSpacedUrl(textBoxToCopy);
		info = info.replaceAll("XXX", urlWithSpace);
		int retVal = coyToClipBoard(textBoxToCopy) ;
		//Window.alert(retVal+"");
		if(1 == retVal)
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
	
public static native int coyToClipBoard(String copyText) /*-{
	return $wnd.copy(copyText);
}-*/;

}
