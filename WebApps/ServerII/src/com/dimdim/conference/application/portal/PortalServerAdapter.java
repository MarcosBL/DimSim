/*
 **************************************************************************
 *                                                                        *
 *               DDDDD   iii             DDDDD   iii                      *
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
 * Copyright (c) 2007 Dimdim Inc. All Rights Reserved.                    *
 *                                                                        *
 *                                                                        *
 * This code is licensed under the DimDim License                         *
 * For details please visit http://www.dimdim.com/license                 *
 *                                                                        *
 **************************************************************************
 */

package com.dimdim.conference.application.portal;

//import java.io.RandomAccessFile;
//import	java.io.File;
import java.io.IOException;
import	java.io.InputStream;
import	java.io.BufferedInputStream;
import java.util.ResourceBundle;
import java.util.Vector;

//import org.apache.commons.httpclient.Credentials;
//import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
//import org.apache.commons.httpclient.HttpMethod;
//import org.apache.commons.httpclient.UsernamePasswordCredentials;
//import org.apache.commons.httpclient.NameValuePair;
//import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;
//import org.apache.commons.httpclient.methods.PostMethod;

import com.dimdim.conference.model.IConference;
import com.dimdim.util.timer.TimerService;
import com.dimdim.util.timer.TimerServiceTaskId;
import com.dimdim.util.timer.TimerServiceUser;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * This simple class provides all the http client based interaction from the
 * portal webapp to the conference server webapp. This is required as since
 * they are two different webapps, all the interaction must go through
 * external http calls, even if they are running on the same server.
 * 
 * The terminology of this interface is based on the portal server concepts.
 * It is the responsibility of this adapter to translate the portal concepts
 * to matching conference server concepts as required, e.g. meeting room is
 * identified as confKey on the running conference server. 
 */

public class PortalServerAdapter	implements	TimerServiceUser
{
	private	static	PortalServerAdapter	adapter;
	
	public	static	PortalServerAdapter	getAdapter()
	{
		if (PortalServerAdapter.adapter == null)
		{
			PortalServerAdapter.createAdapter();
		}
		return	PortalServerAdapter.adapter;
	}
	private	synchronized	static	void	createAdapter()
	{
		if (PortalServerAdapter.adapter == null)
		{
			PortalServerAdapter.adapter = new PortalServerAdapter();
		}
	}
	
	protected	HttpClient	httpClient;
	
	protected	boolean		MakeCallbacksForAllMeetings;
	
	protected	String		portalServerHost;
	protected	String		portalServerPortNumber;
	protected	String		portalServerSecurePortNumber;
	protected	String		portalServerWebappName;
	
	protected	String		portalServerInternalHost;
	protected	String		portalServerInternalPortNumber;
	protected	String		portalServerInternalSecurePortNumber;
	
	protected	String		meetingStartedUrl;
	protected	String		meetingClosedUrl;
	protected	String		nonPortalMeetingClosedUrl;
	protected	String		userJoinedUrl;
	protected	String		userLeftUrl;
	protected	String		consoleLoadedUrl;
	
	protected	Vector		urlQueue;
	
	private	PortalServerAdapter()
	{
		this.httpClient = new HttpClient();
		try
		{
			ResourceBundle rb = ResourceBundle.getBundle("resources.portal");
			try
			{
				String s = rb.getString("MakeCallbacksForAllMeetings");
				MakeCallbacksForAllMeetings = Boolean.parseBoolean(s);
			}
			catch(Exception e)
			{
				MakeCallbacksForAllMeetings = false;
			}
			portalServerHost = rb.getString("PortalServerHost");
			portalServerPortNumber = rb.getString("PortalServerPortNumber");
			portalServerSecurePortNumber = rb.getString("PortalServerSecurePortNumber");
			portalServerWebappName = rb.getString("PortalServerWebappName");
			
			portalServerInternalHost = rb.getString("PortalServerInternalHost");
			portalServerInternalPortNumber = rb.getString("PortalServerInternalPortNumber");
			portalServerInternalSecurePortNumber = rb.getString("PortalServerInternalSecurePortNumber");
			
			String baseUrl = "http://"+this.portalServerInternalHost+":"+
				this.portalServerInternalPortNumber+"/"+this.portalServerWebappName+"/";
			this.meetingStartedUrl = baseUrl + getActionName(rb, "MeetingStartedAction", "MeetingStarted.action");
			this.meetingClosedUrl = baseUrl + getActionName(rb, "MeetingClosedAction", "MeetingClosed.action");
			this.nonPortalMeetingClosedUrl = baseUrl + getActionName(rb, "NonPortalMeetingClosedAction", "NonPortalMeetingClosed.action");
			this.userJoinedUrl = baseUrl + getActionName(rb, "UserJoinedAction", "UserJoined.action");
			this.userLeftUrl = baseUrl + getActionName(rb, "UserLeftAction", "UserLeft.action");
			this.consoleLoadedUrl = baseUrl + getActionName(rb, "ConsoleLoadedAction", "ConsoleLoaded.action");
			
			urlQueue = new Vector();
			TimerService.getService().addUser(this);
		}
		catch(Exception e)
		{
//			e.printStackTrace();
		}
	}
	public	String	getPortalServerInternalAddress()
	{
		return	"http://"+this.portalServerInternalHost+":"+
			this.portalServerInternalPortNumber+"/"+this.portalServerWebappName;
	}
	public	String	getJoinMeetingUrl(String confKey)
	{
		String baseUrl = "http://"+this.portalServerHost+":"+
			this.portalServerPortNumber+"/"+this.portalServerWebappName+"/JoinForm.action";
		
		return	baseUrl+"?meetingRoomName="+confKey;
	}
	public	String	getJoinMeetingUrl(String confKey, String attendeePwd)
	{
		String baseUrl = "http://"+this.portalServerHost+":"+
			this.portalServerPortNumber+"/"+this.portalServerWebappName+"/JoinForm.action";
		
		return	baseUrl+"?meetingRoomName="+confKey+"&attendeePwd="+attendeePwd;
	}
	private	String	getActionName(ResourceBundle rb, String key, String defaultAction)
	{
		String ret = defaultAction;
		try
		{
			ret = rb.getString(key);
		}
		catch(Exception e)
		{
			ret = defaultAction;
		}
		return	ret;
	}
	public	void	reportMeetingStarted(String confKey)
	{
		if (this.portalServerHost == null || this.portalServerHost.length() == 0)
		{
			return;
		}
		String meeting_id = PortalInterface.getPortalInterface().getMeetingId(confKey);
		if (meeting_id == null)
		{
			if (MakeCallbacksForAllMeetings)
			{
				meeting_id = confKey;
			}
			else
			{
				return;
			}
		}
		String httpArguments = "meeting_id="+meeting_id;
		this.queuePortalCall(this.meetingStartedUrl, httpArguments);
//		String portalServerResponse = this.getURL_String(this.httpClient,this.meetingStartedUrl,httpArguments);
//		System.out.println("reportMeetingStarted response:"+portalServerResponse);
	}
	public	void	reportMeetingClosed(IConference conf)
	{
		String confKey = conf.getConfig().getConferenceKey();
		long startTime = conf.getStartTimeMillis();
		int noAttendees = conf.getParticipants().size();
		String returnUrl = conf.getReturnUrl();
		if (this.portalServerHost == null || this.portalServerHost.length() == 0)
		{
			return;
		}
		String meeting_id = PortalInterface.getPortalInterface().getMeetingId(confKey);
		if (meeting_id == null)
		{
			String httpArguments = "startTime="+startTime+"&noAttendees="+noAttendees+"&confKey="+confKey+"&returnUrl="+returnUrl;
			this.queuePortalCall(this.nonPortalMeetingClosedUrl, httpArguments);
//			String portalServerResponse = this.getURL_String(this.httpClient,this.nonPortalMeetingClosedUrl, httpArguments);
//			System.out.println("reportMeetingClosed non portal response:"+portalServerResponse);
		}
		else
		{
			String httpArguments = "meeting_id="+meeting_id;
			this.queuePortalCall(this.meetingClosedUrl, httpArguments);
//			String portalServerResponse = this.getURL_String(this.httpClient,this.meetingClosedUrl,httpArguments);
//			System.out.println("reportMeetingClosed response:"+portalServerResponse);
			PortalInterface.getPortalInterface().clearMeeting(confKey);
		}
	}
	public	void	reportUserJoined(String confKey, String email)
	{
		if (this.portalServerHost == null || this.portalServerHost.length() == 0)
		{
			return;
		}
		String meeting_id = PortalInterface.getPortalInterface().getMeetingId(confKey);
		if (meeting_id == null)
		{
			if (MakeCallbacksForAllMeetings)
			{
				meeting_id = confKey;
			}
			else
			{
				return;
			}
		}
		String httpArguments = "meeting_id="+meeting_id+"&email="+email;
		this.queuePortalCall(this.userJoinedUrl, httpArguments);
//		String portalServerResponse = this.getURL_String(this.httpClient,this.userJoinedUrl,httpArguments);
//		System.out.println("isMeetingOccupied response:"+portalServerResponse);
	}
	public	void	reportUserConsoleLoaded(String confKey, String email)
	{
		if (this.portalServerHost == null || this.portalServerHost.length() == 0)
		{
			return;
		}
		String meeting_id = PortalInterface.getPortalInterface().getMeetingId(confKey);
		if (meeting_id == null)
		{
			if (MakeCallbacksForAllMeetings)
			{
				meeting_id = confKey;
			}
			else
			{
				return;
			}
		}
		String httpArguments = "meeting_id="+meeting_id+"&email="+email;
		this.queuePortalCall(this.consoleLoadedUrl, httpArguments);
//		String portalServerResponse = this.getURL_String(this.httpClient,this.userJoinedUrl,httpArguments);
//		System.out.println("isMeetingOccupied response:"+portalServerResponse);
	}
	public	void	reportUserLeft(String confKey, String email)
	{
		System.out.println("report User Left called in conf server");
//		System.out.println(this.userLeftUrl);
		if (this.portalServerHost == null || this.portalServerHost.length() == 0)
		{
			return;
		}
		String meeting_id = PortalInterface.getPortalInterface().getMeetingId(confKey);
/*		if (meeting_id == null)
		{
			if (MakeCallbacksForAllMeetings)
			{
				meeting_id = confKey;
			}
			else
			{
				return;
			}
		}*/
		String httpArguments = "meeting_id="+meeting_id+"&email="+email;
		this.queuePortalCall(this.userLeftUrl, httpArguments);
//		String portalServerResponse = this.getURL_String(this.httpClient,this.userLeftUrl,httpArguments);
//		System.out.println("reportUserLeft response:"+portalServerResponse);
	}
	
	/**
	 * @param url
	 * @param arguments
	 * @return
	 */
	protected	synchronized	String	getURL_String(HttpClient client, String url)
	{
		GetMethod method = null;
        String responseBody = null;
        try
        {
			
			System.out.println("Getting URL:"+url);
//			System.out.println("Posting data:"+args);
	        method = new GetMethod(url);
	//        method.setRequestBody(args);
	        method.setFollowRedirects(true);
	        
	        //execute the method
	        client.setTimeout(2000);
	        System.out.println("Calling url:"+url);
	        StringBuffer buf = new StringBuffer();
            client.executeMethod(method);
            InputStream is = method.getResponseBodyAsStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            byte[] ary = new byte[256];
            int len = 0;
            while ((len=bis.read(ary,0,256))>0)
            {
            	String str = new String(ary,0,len);
//            	System.out.println("Received buffer:"+str);
            	buf.append(str);
            }
            try
            {
            	bis.close(); is.close();
            }
            catch(Exception e)	{	}
            responseBody = buf.toString();
	        System.out.println("Called ----:"+url);
        }
        catch (HttpException he)
        {
        	he.printStackTrace();
        }
        catch (IOException ioe)
        {
        	ioe.printStackTrace();
        }
        //clean up the connection resources
        try
        {
        	method.releaseConnection();
        }
        catch(Exception ee)
        {
        	//	Ignore the exceptions in cleanup.
        }
        return	responseBody;
	}
	
	private	void	queuePortalCall(String url, String args)
	{
		synchronized (this)
		{
			if (this.urlQueue != null)
			{
				this.urlQueue.addElement(url+"?"+args);
			}
		}
	}
	public long getTimerDelay()
	{
		return 60000;
	}
	public void setTimerServiceTaskId(TimerServiceTaskId taskId)
	{
	}
	public boolean timerCall()
	{
		Vector v = null;
		synchronized (this)
		{
			if (this.urlQueue != null && this.urlQueue.size() > 0)
			{
				v = this.urlQueue;
				this.urlQueue = new Vector();
			}
		}
		if (v != null)
		{
			int num = v.size();
			for (int i=0; i<num; i++)
			{
				String	url = (String)v.elementAt(i);
				this.getURL_String(this.httpClient, url);
			}
		}
		return true;
	}
}
