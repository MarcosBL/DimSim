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

package com.dimdim.conference.action.whiteboard.ui;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//import com.dimdim.util.misc.IDGenerator;
import com.dimdim.conference.action.CommonDimDimAction;
//import com.dimdim.conference.application.ChildSession;
//import com.dimdim.conference.application.UserSession;
//import com.dimdim.conference.application.UserSessionManager;
//import com.dimdim.conference.model.IConference;
//import com.dimdim.conference.model.IConferenceParticipant;
//import com.dimdim.conference.ConferenceConsoleConstants;
//import com.dimdim.conference.ConferenceConstants;
import com.opensymphony.webwork.interceptor.ServletRequestAware;
import com.opensymphony.webwork.interceptor.ServletResponseAware;

/**
 *	@author Jayant Pandit
 *	@email Jayant.Pandit@communiva.com
 *	
 *	This action is a simple forward to the ppt module page. All the arguments are
 *	required by the page. Out of these, the width, height, movieSwfUrl can be derived
 *	from action (broadcast or play), the movieName and movieNodeName can be derived
 *	from the pptName and the background color can be optional. PPT url and number of
 *	slides must be provided by the caller each time.
 *	
 *	So the calls expected are:
 *	
 *	Broadcaster:
 *	GetPPTModulePage.action?action=broadcast&pptName=<resource id>&pptUrl=<url>&numberOfSlides=<number>
 *	
 *	Player:
 *	GetPPTModulePage.action?action=play&pptName=<resource id>&pptUrl=<url>&numberOfSlides=<number>
 */
public class GetWhiteboardModulePageAction	extends	CommonDimDimAction
		implements	ServletRequestAware, ServletResponseAware 
{
	public	static	final	String	WRITER	=	"writer";
	public	static	final	String	VIEWER	=	"viewer";
	
	protected	HttpServletRequest	servletRequest;
	protected	HttpServletResponse	servletResponse;
	
	protected	int		width = 630;
	protected	int		height = 470;
	protected	String	movieName = "wb";
	protected	String	movieNodeName = "wbWindow";
	protected	String	movieSwfUrl = "swf/wb_sa.swf";
	protected	String	backgroundColor = "white";
	
	protected	String	rtmpUrl;
	protected	String	rtmptUrl;
	
	protected	String	role = GetWhiteboardModulePageAction.WRITER;
	protected	String	confKey;
	protected	String	name;
	
	public	GetWhiteboardModulePageAction()
	{
	}
	/**
	 * The data passed to this action is not required to persist through the session
	 * right now. If such a requirement does come up in future, the object will be
	 * simply attached to either the http session or the user session.
	 */
	public	String	execute()	throws	Exception
	{
		WhiteboardModuleData wmd = new WhiteboardModuleData(role, cflag, width,
			height, movieName, movieNodeName, movieSwfUrl, backgroundColor,
			rtmpUrl, rtmptUrl, confKey, name);
		this.servletRequest.setAttribute(WhiteboardModuleData.SESSION_ATTRIBUTE_NAME,wmd);
		
		return	SUCCESS;
	}
	public HttpServletRequest getServletRequest()
	{
		return this.servletRequest;
	}
	public void setServletRequest(HttpServletRequest servletRequest)
	{
		this.servletRequest = servletRequest;
	}
	public HttpServletResponse getServletResponse()
	{
		return this.servletResponse;
	}
	public void setServletResponse(HttpServletResponse servletResponse)
	{
		this.servletResponse = servletResponse;
	}
	public String getRole()
	{
		return role;
	}
	public void setRole(String role)
	{
		this.role = role;
	}
	public String getBackgroundColor()
	{
		return backgroundColor;
	}
	public void setBackgroundColor(String backgroundColor)
	{
		this.backgroundColor = backgroundColor;
	}
	public int getHeight()
	{
		return height;
	}
	public void setHeight(int height)
	{
		this.height = height;
	}
	public String getMovieName()
	{
		return movieName;
	}
	public void setMovieName(String movieName)
	{
		this.movieName = movieName;
	}
	public String getMovieNodeName()
	{
		return movieNodeName;
	}
	public void setMovieNodeName(String movieNodeName)
	{
		this.movieNodeName = movieNodeName;
	}
	public String getMovieSwfUrl()
	{
		return movieSwfUrl;
	}
	public void setMovieSwfUrl(String movieSwfUrl)
	{
		this.movieSwfUrl = movieSwfUrl;
	}
	public int getWidth()
	{
		return width;
	}
	public void setWidth(int width)
	{
		this.width = width;
	}
	public String getConfKey()
	{
		return confKey;
	}
	public void setConfKey(String confKey)
	{
		this.confKey = confKey;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public String getRtmptUrl()
	{
		return rtmptUrl;
	}
	public void setRtmptUrl(String rtmptUrl)
	{
		this.rtmptUrl = rtmptUrl;
	}
	public String getRtmpUrl()
	{
		return rtmpUrl;
	}
	public void setRtmpUrl(String rtmpUrl)
	{
		this.rtmpUrl = rtmpUrl;
	}
}
