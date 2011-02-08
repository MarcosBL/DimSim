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
import com.dimdim.conference.UtilMethods;
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

public class TestHomeAction	extends	CommonDimDimAction
		implements	ServletRequestAware
{
	protected	HttpServletRequest	servletRequest;
	
	protected	String	token;
	protected	String	infoBuffer="";
	
	public	TestHomeAction()
	{
	}
	public	String	execute()	throws	Exception
	{
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
}
