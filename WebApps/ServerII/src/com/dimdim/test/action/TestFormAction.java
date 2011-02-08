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
 * Copyright (c) 2006 Communiva Inc. All Rights Reserved.                 *
 *                                                                        *
 *                                                                        *
 * This code is licensed under the DimDim License                         *
 * For details please visit http://www.dimdim.com/license                 *
 *                                                                        *
 **************************************************************************
 */
 
package com.dimdim.test.action;

import javax.servlet.http.HttpServletRequest;

import com.dimdim.conference.action.CommonDimDimAction;
import com.dimdim.conference.ConferenceConsoleConstants;
import com.opensymphony.webwork.interceptor.ServletRequestAware;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * This action simply returns the home page for the tests kick off. Objective
 * of this and associated actions is to provide a simple set of pages and actions
 * which can be recorded in sequence and played back by an automated tool.
 * 
 * This is to facilitate load testing. Load testing of the server by using the
 * interactive console itself has been difficult to impossible, most probably
 * because of the dynamic js on the client and the ajax calls. Instead if we
 * get a sequence which is similar to the conventional browsing, i.e. each click
 * returns a full page. Then the record and playback will be easier and more
 * reliable.
 * 
 * The Start or join sequence is almost same except for action.
 * 
 * 	--	connect
 * 	--	create and start	/	join
 * 	--	get console page
 * 	--	get data -	ui_strings
 * 	--	get	data -	tooltips
 * 	--	get data -	default layout
 * 	--	get data -	emoticons
 * 	--	get	data -	session data
 * 	--	console loaded
 * 	--	get events
 * 	--		Repeat get events indefinately at minimum. Action on the part of
 * 	--		the attendee or presenter means calling some specific commands.
 */

public class TestFormAction	extends	CommonDimDimAction
		implements	ServletRequestAware
{
	protected	HttpServletRequest	servletRequest;
	
	protected	String	url;
	protected	String	token;
	protected	String	infoBuffer="";
	
	protected	String	action;
	protected	String	email;
	protected	String	confKey;
	protected	String	confName;
	
	public	TestFormAction()
	{
	}
	public	String	execute()	throws	Exception
	{
		if (action.equals("host"))
		{
			this.url = "/"+ConferenceConsoleConstants.getWebappName()+
				"/test/testconnect.action?action=host&email="+email+
				"&displayName="+email+"&confKey="+confKey+"&confName="+confName+
				"&osType=windows&browserType=ie&attendees=&lobby=false"+
				"&networkProfile=2&imageQuality=2&meetingHours=1&meetingMinutes=0"+
				"&presenterAV=audio&maxAttendeeMikes=2&returnUrl=/"+
				"&privateChatEnabled=true&publicChatEnabled=true"+
				"&screenShareEnabled=true&maxParticipants=20&whiteboardEnabled=true";
		}
		else
		{
			this.url = "/"+ConferenceConsoleConstants.getWebappName()+
				"/test/testconnect.action?action=join&email="+email+
				"&displayName="+email+"&confKey="+confKey+
				"&osType=windows&browserType=ie";
		}
		
		if (token != null)
		{
			return	token;
		}
		return	SUCCESS;
	}
	public String getToken()
	{
		return this.token;
	}
	public void setToken(String token)
	{
		this.token = token;
	}
	public HttpServletRequest getServletRequest()
	{
		return servletRequest;
	}
	public void setServletRequest(HttpServletRequest servletRequest)
	{
		this.servletRequest = servletRequest;
	}
	public String getInfoBuffer()
	{
		return infoBuffer;
	}
	public void setInfoBuffer(String infoBuffer)
	{
		this.infoBuffer = infoBuffer;
	}
	public String getAction()
	{
		return action;
	}
	public void setAction(String action)
	{
		this.action = action;
	}
	public String getConfKey()
	{
		return confKey;
	}
	public void setConfKey(String confKey)
	{
		this.confKey = confKey;
	}
	public String getConfName()
	{
		return confName;
	}
	public void setConfName(String confName)
	{
		this.confName = confName;
	}
	public String getUrl()
	{
		return url;
	}
	public void setUrl(String url)
	{
		this.url = url;
	}
	public String getEmail()
	{
		return email;
	}
	public void setEmail(String email)
	{
		this.email = email;
	}
}
