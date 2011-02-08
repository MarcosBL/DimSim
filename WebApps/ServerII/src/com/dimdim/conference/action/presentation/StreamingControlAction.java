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

package com.dimdim.conference.action.presentation;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimdim.conference.model.IConference;
import com.dimdim.conference.model.IConferenceParticipant;
import com.dimdim.conference.application.core.ConferenceManager;
import com.dimdim.conference.application.core.NoConferenceByKeyException;
import com.dimdim.conference.action.CommonDimDimAction;
import com.opensymphony.webwork.interceptor.ServletRequestAware;
import com.opensymphony.webwork.interceptor.ServletResponseAware;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */
public	abstract class StreamingControlAction	extends	CommonDimDimAction
	implements	ServletRequestAware, ServletResponseAware 
{
	protected	String	cmd;
	protected	String	conferenceKey;
	protected	String	resourceId;
	protected	String	mediaId;
	protected	String	presenterId;
	protected	String	presenterPassKey;
	protected	String	appName;
	protected	String	appHandle;
	
	protected	HttpServletRequest	servletRequest;
	protected	HttpServletResponse	servletResponse;
	
	public	StreamingControlAction()
	{
	}
	public	String	execute()	throws	Exception
	{
		System.out.println("Received from the stream publisher:");
		System.out.println("Stream Control --- cm -"+cmd+"-");
		System.out.println("Stream Control --- conferenceKey -"+conferenceKey+"-");
		System.out.println("Stream Control --- resourceId -"+resourceId+"-");
		System.out.println("Stream Control --- mediaId -"+mediaId+"-");
		System.out.println("Stream Control --- presenterId -"+presenterId+"-");
		System.out.println("Stream Control --- presenterPassKey -"+presenterPassKey+"-");
		System.out.println("Stream Control --- appName -"+appName+"-");
		long t = System.currentTimeMillis();
		System.out.println("Stream Control --- appHandle -"+appHandle+"-"+t);
		
		try
		{
			IConference conf = ConferenceManager.getManager().getConference(conferenceKey);
			IConferenceParticipant user = conf.getParticipant(presenterId);
			
			if (user != null)
			{
				conf.getResourceManager().handleScreenStreamControlMessage(user, resourceId,
					mediaId, cmd, presenterId, presenterPassKey, appName, appHandle);
			}
		}
		catch(NoConferenceByKeyException ncbke)
		{
			
		}
		
		System.out.println("Stream Control call complete in -"+(System.currentTimeMillis()-t));
		
		return	SUCCESS;
	}
	protected	abstract	String	getActionCode();
	
	public String getCmd()
	{
		return this.cmd;
	}
	public void setCmd(String cmd)
	{
		this.cmd = cmd;
	}
	public String getConferenceKey()
	{
		return this.conferenceKey;
	}
	public void setConferenceKey(String conferenceKey)
	{
		this.conferenceKey = conferenceKey;
	}
	public String getMediaId()
	{
		return this.mediaId;
	}
	public void setMediaId(String mediaId)
	{
		this.mediaId = mediaId;
	}
	public String getPresenterId()
	{
		return this.presenterId;
	}
	public void setPresenterId(String presenterId)
	{
		this.presenterId = presenterId;
	}
	public String getPresenterPassKey()
	{
		return this.presenterPassKey;
	}
	public void setPresenterPassKey(String presenterPassKey)
	{
		this.presenterPassKey = presenterPassKey;
	}
	public String getResourceId()
	{
		return this.resourceId;
	}
	public void setResourceId(String resourceId)
	{
		this.resourceId = resourceId;
	}
	public String getAppHandle()
	{
		return appHandle;
	}
	public void setAppHandle(String appHandle)
	{
		this.appHandle = appHandle;
	}
	public String getAppName()
	{
		return appName;
	}
	public void setAppName(String appName)
	{
		this.appName = appName;
	}
	public HttpServletRequest getServletRequest()
	{
		return servletRequest;
	}
	public void setServletRequest(HttpServletRequest servletRequest)
	{
		this.servletRequest = servletRequest;
	}
	public HttpServletResponse getServletResponse()
	{
		return servletResponse;
	}
	public void setServletResponse(HttpServletResponse servletResponse)
	{
		this.servletResponse = servletResponse;
	}
}
