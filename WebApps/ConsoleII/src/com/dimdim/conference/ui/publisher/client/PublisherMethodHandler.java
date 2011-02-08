package com.dimdim.conference.ui.publisher.client;

import com.google.gwt.user.client.Element;

public class PublisherMethodHandler {

	public static native String getScreencastResult(Element elem) /*-{
     //alert("result = "+$wnd.dimdimPublisherControl1.getProperty("name=\"screencastResult\""));
     	return  elem.getProperty("name=\"screencastResult\"");
    }-*/;
	 
	public static native void closePublisher(Element elem, String publisherId) /*-{
	   		elem.performAction("operation=mint&action=exit"+"&reg="+publisherId);
	}-*/;
	 
	public static native int runDesktopShare(Element elem, String confKey,
			    String resourceId, String presenterId, String recordingFlags,
			    String rtmpUrl, String rtmptUrl, String rtmpStream, String httpUrl,
			    boolean wholeDesktop, String publisherId)/*-{ 

		    var shareWholeDesktop = 0;
			var returnVal = -1;
		     if (elem != null)
		     {
		     	if (wholeDesktop)
		     	{
		     		shareWholeDesktop = 1;
		     	}

		     	var screenURL = rtmpUrl+confKey+"/"+rtmpStream+"/"+recordingFlags;
		     	//alert("screen url  = "+"{screenURL:\""+screenURL+"\"}");
		     	elem.setProperty("{screenURL:\""+screenURL+"\"}");
		     
		     	var inputStr = "operation=screencast&action=share&handle=0";
		     	returnVal = elem.performAction(inputStr+"&reg="+publisherId);
		     	return returnVal;
		     }

	}-*/;
	 
	public static native void stopDTPAndApplicationShare(Element elem, String publisherId)/*-{ 

     	if (elem != null)
     	{
     		elem.performAction("operation=screencast&action=stop"+"&reg="+publisherId);
     	}			   
 	}-*/;

    public static native int checkDriver(Element elem)/*-{ 

		if (elem != null)
		{
			return elem.performAction("operation=mint&action=checkDriver");
		}			   
	}-*/;
    
    public static native String getVersion(Element elem)/*-{ 

		if (elem != null)
		{
			return elem.getVersion();
		}			   
	}-*/;
    
    public static native int setNetworkProfile(Element elem, int netWorkVal)/*-{ 

		if (elem != null)
		{
			var operationString =  "{BWProfile:\""+netWorkVal+"\"}";
			//alert('set prof = '+operationString);
			return elem.setProperty(operationString);
		}			   
	}-*/;
}
