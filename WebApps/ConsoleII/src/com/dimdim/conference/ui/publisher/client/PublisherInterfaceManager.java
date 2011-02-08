/*
 **************************************************************************
 *                                                                        *
 *               DD  DD      mm mm mmmm  DD  DD      mm mm mmmm           *
 *               DD   DD iii mmm  mm  mm DD   DD iii mmm  mm  mm          *
 *               DD   DD iii mmm  mm  mm DD   DD iii mmm  mm  mm          *
 *               DDDDDD  iii mmm  mm  mm DDDDDD  iii mmm  mm  mm          *
 *                                                                        *
 **************************************************************************
 **************************************************************************
 *                                                                        *
 * Part of the DimDim V 1.0 Codebase (http://www.dimdim.com)	          *
 *                                                                        *
 * Copyright (c) 2006 Communiva Inc. All Rights Reserved.                 *
 *                                                                        *
 *                                                                        *
 * This code is licensed under the DimDim License                         *
 * For details please visit http://www.dimdim.com/license                 *
 *                                                                        *
 **************************************************************************
 */

package com.dimdim.conference.ui.publisher.client;

import com.dimdim.conference.ui.common.client.layout.DefaultCommonDialog;
import com.dimdim.conference.ui.model.client.ConferenceGlobals;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */

public class PublisherInterfaceManager
{
    private static String sampleAppString = "{windowList:["
	    + "{handle:264776,caption:'Java - PublisherInterfaceManager.java - Eclipse SDK'},"
	    + "{handle:330770,caption:'C:\\\\WINDOWS\\\\system32\\\\cmd.exe - python interface.py'},"
	    + "{handle:35851974,caption:'Command Prompt'},"
	    + "{handle:35851974,caption:'E:\\\\DilipWorkArea\\\\DimDimCodeBase'},"
	    + "{handle:50139340,caption:'activesalesforce-1.1.6'}]}";

    private static PublisherInterfaceManager manager;
    //this is the id that should be used in all perform action methods
    private static String publisherId = "";

    public static PublisherInterfaceManager getManager()
    {
		if (PublisherInterfaceManager.manager == null)
		{
		    PublisherInterfaceManager.manager = new PublisherInterfaceManager();
		    publisherId = ConferenceGlobals.getClientGUID();
		}
		return PublisherInterfaceManager.manager;
    }

    private WaitAndContinueWaiter sharingWaiter = null;

    private boolean desktopSharingActive = false;

    //	This flag is required because even if the pub is enabled and installed,
    //	a particular meeting may never use it. At the end of the meeting the
    //	publisher should be closed only if it was ever activated.
    
    private	boolean	screenSharePluginActivated = false;
    
    private PublisherInterfaceManager()
    {

    }
    /**
         * @return
         */
    public int isSharingActive()
    {
		String tempString = null;
		Element elem = null;
		
		elem = getPubElement();
		 //Window.alert("inside pub intf mngr's isSharingActive -- ");
		if(null != elem)
		{
			tempString = PublisherMethodHandler.getScreencastResult(elem);
		}else{
			//Window.alert("isSharingActive publisher not found..");
		}
		
		JSONValue jVal = JSONParser.parse(tempString);
		JSONObject jObj = jVal.isObject();
		String returnVal = jObj.get("screencastResult").isString().stringValue();
		
	//    DebugPanel.getDebugPanel().addDebugMessage( "Is SharingActive : " + returnVal);
	    
		return Integer.parseInt(returnVal);
    }

    /**
         * @return
         */
    /**
         * @return
         */
    public boolean isDesktopSharingActive()
    {
    	return desktopSharingActive;
    }

    /**
         * @return
         */
    public void setDesktopSharingActive(boolean desktopSharingActive)
    {
    	this.desktopSharingActive = desktopSharingActive;
    }

    /**
         * V 1.0 Desktop Share.
         */
    public void startDesktopShare(WaitAndContinueData data,
	    ApplicationShareInterface consoleCallback)//, ApplicationWindowsListPanel appPanel)
    {
		String httpUrl = data.getHttpUrl();
		String rtmpUrl = data.getRtmpUrl();
//		String rtmptUrl = data.getRtmptUrl();
		String rtmpStream = data.getRtmpStreamId();
		String presenterId = data.getUserId();
		String confKey = data.getConferenceKey();
		String recordingFlags = data.getRecordingFlags();
		String resourceId = data.getResourceId();
		int returnVal = -1;
	
		try
		{
		    this.sharingWaiter = new WaitAndContinueWaiter(this);
		    Element elem = null;
		    
		    elem = getPubElement();
		    //Window.alert("inside pub intf mngr's startDesktopShare -- ");
		    if (null != elem)
		    {
					returnVal = PublisherMethodHandler.runDesktopShare(elem, confKey, resourceId, presenterId,
					recordingFlags, rtmpUrl, rtmpUrl, rtmpStream,
					httpUrl, true, publisherId);
		    }
		    else
		    {
		    	//Window.alert("startDesktopShare publisher not found..");
		    }
		    
			screenSharePluginActivated = true;
		    if(returnVal == 1)
		    {
	        	    this.desktopSharingActive = true;
	        	    data.setAppHandle(0);
	        	    this.sharingWaiter.waitForScreenShareCompletion(consoleCallback,
	        		    elem, data, this);//, appPanel);
		    }
		    else
		    {
				 DefaultCommonDialog.showMessage(
					ConferenceGlobals.getDisplayString("publisher_error.header","Application Share Error"), 
					ConferenceGlobals.getDisplayString("publisher_error."+23,"Unknown Error"));
		    }
		}
		catch (Exception e)
		{
		    showErrorMessage(e.getMessage());
		}
    }

    /**
         * V 1.0 Application Share.
         */
    public JSONArray getApplicationsList()
    {
		 JSONArray jarray = null;
		return jarray;
    }
    
    public int isDriverPresent()
    {
    	//Window.alert("calling ... isDriverPresent ConferenceGlobals.publisherEnabled = "+ConferenceGlobals.publisherEnabled);
    	if (!ConferenceGlobals.publisherEnabled)
    	{
    		return	0;
    	}
		int returnVal = 1;
		try
		{
		    Element elem = null;
		    //Window.alert("inside isDriverPresent before getting element = ");
		    elem = getPubElement();
		    //Window.alert("publisher version = "+PublisherMethodHandler.getVersion(elem));
		    //Window.alert("inside isDriverPresent element = "+elem);
		    if (null != elem)
		    {
		    	PublisherMethodHandler.checkDriver(elem);
		    }
		    //else
		    //{
		    	//Window.alert("isDriverPresent publisher not found..");
		    //}
		    //Window.alert("return val of command = "+returnVal);
		}
		catch (Exception e)
		{
		    //showErrorMessage(e.getMessage());
		}
		screenSharePluginActivated = true;
		return returnVal;
    }
    
    public void setNetworkProfileValue(String networkProfileVal)
    {
    	if (!ConferenceGlobals.publisherEnabled && 
    			!"true".equalsIgnoreCase(ConferenceGlobals.getPubAvailable()) )
    	{
    		return;
    	}
	
	int netWorkVal = 0;
//	int returnVal = 0;
	
	if("1".equals(networkProfileVal)){
	    netWorkVal = 2;
	}else if("2".equals(networkProfileVal)){
	    netWorkVal = 1;
	}else if("3".equals(networkProfileVal)){
	    netWorkVal = 0;
	}
	
	//Window.alert("inside pub intf mngr's setNetworkProfileValue -- "+netWorkVal);
	
	try
	{
	    Element elem = null;
	    
	    elem = getPubElement();
	    if (null != elem)
	    {
	    	PublisherMethodHandler.setNetworkProfile(elem, netWorkVal);
	    } else
	    {
	    	//Window.alert("setNetworkProfileValue publisher not found..");
	    }
	    //Window.alert("return val of command = "+returnVal);
	} catch (Exception e)
	{
	    showErrorMessage(e.getMessage());
	}
    }


    public void stopDTPAndAppShare()
    {
    	if (!ConferenceGlobals.publisherEnabled)
    	{
    		return;
    	}
		if (this.desktopSharingActive)
		{
			//Window.alert("inside pub intf mngr's stopDTPAndAppShare -- ");
		    try
		    {
		    	Element elem = null;
		    	elem = getPubElement();
		    	if (null != elem)
		    	{
		    		PublisherMethodHandler.stopDTPAndApplicationShare(elem, publisherId);
		    	} else
		    	{
		    		//Window.alert("stopDTPAndAppShare publisher not found..");
		    	}
		    } catch (Exception e)
		    	{
		    	showErrorMessage(e.getMessage());
		    	}
		}
    }
    public void closePublisher()
    {
    	if (!ConferenceGlobals.publisherEnabled)
    	{
    		return;
    	}
    	if (screenSharePluginActivated)
    	{
    		//Window.alert("inside pub intf mngr's closePublisher -- ");
		 	Element elem = null;
		 	elem = getPubElement();
			if (null != elem)
			{
				PublisherMethodHandler.closePublisher(elem, publisherId);
			} else
			{
				//Window.alert(" closePublisher publisher not found..");
			}
			screenSharePluginActivated = false;
    	}
    }
    
    private void showErrorMessage(String message)
    {
    	String messageHeader = ConferenceGlobals.getDisplayString(
		"publisher_error.header", "publisher error");
    	DefaultCommonDialog.showMessage(messageHeader, message);
    }

    private Element getPubElement() {
		Element elem = null;
		if(ConferenceGlobals.OS_WINDOWS.equals(ConferenceGlobals.getOSType()))
		{
			if (ConferenceGlobals.isBrowserIE())
			{
				elem = getWindowsIEPublisher();
				//Window.alert("returning ie on windows elemenet = "+elem);
			}
			else
			{
				elem = getFirefoxWindows();
			}
			if("undefined".equals(elem) || "null".equals(elem))
			{
				elem = null;
			}
		}else if (ConferenceGlobals.OS_MAC.equals(ConferenceGlobals.getOSType()))
		{
			elem = getMacPublisherElement();
			
		}
		return elem;
	}
    
    private Element getFirefoxWindows()
    {
    	return DOM.getElementById("DimdimDesktopPublisher1");
    }
    
    private native Element getWindowsIEPublisher()/*-{ 
    	if($wnd.dimdimPublisherControl1 != null )
    		{
				return $wnd.dimdimPublisherControl1;
			}else{
				return null;
			}
	}-*/;
    
    
    private Element getMacPublisherElement()
    {
    	Element elem = DOM.getElementById("DimdimDesktopPublisherMac");
    	return getMacPubControl(elem);
    }
    
    private native Element getMacPubControl(Element elem)/*-{ 
		return elem.dimdimCtrl;
	}-*/;
    
}
