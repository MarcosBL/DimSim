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

package com.dimdim.conference.action.ui.ppt;

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
public class GetPPTModulePageAction	extends	CommonDimDimAction
		implements	ServletRequestAware, ServletResponseAware 
{
	public	static	final	String	BROADCAST	=	"broadcast";
	public	static	final	String	PLAY	=	"play";
	
	protected	HttpServletRequest	servletRequest;
	protected	HttpServletResponse	servletResponse;
	
	protected	String	action = "play";
	
	protected	int		width = -1;
	protected	int		height = -1;
	protected	int		slideWidth = -1;
	protected	int		slideHeight = -1;
	protected	int		aspectRatio = 1334;
	protected	String	movieName = "ppt";
	protected	String	movieNodeName = "pptWindow";
	protected	String	movieSwfUrl = "";
	protected	String	pptUrl = "";
	protected	int		numberOfSlides = 4;
	protected	int		startSlideIndex = 0;
	protected	String	pptName = "Default";
	protected	String	backgroundColor = "white";
	protected	int		inPopout = 0;
	protected	String	meetingId = "global-meeting";
	protected	String	pptType = "preloaded";
	protected	String	pptId = "";
	protected	String	rtmpUrl = "";
	protected	String	rtmptUrl = "";
	protected	String	annotation = "";
	
	public	GetPPTModulePageAction()
	{
	}
	/**
	 * The data passed to this action is not required to persist through the session
	 * right now. If such a requirement does come up in future, the object will be
	 * simply attached to either the http session or the user session.
	 * 
	 * Sizing policy change update: June 24th / 07. Now the ppt will be shown in
	 * two fixed sizes. One, Normal size in console and other Large size in popout.
	 * Parameter inPopout is added to convay that information. The names of the
	 * swf movies are appended with the width and height numbers in that order to
	 * get the name of the movie. The movies are exported to specific width and
	 * height numbers because, the buttons and text elements do not look good
	 * when simply stretched in html.
	 */
	public	String	execute()	throws	Exception
	{
//		if (width == -1 || height == -1)
//		{
			if (action.equals("broadcast"))
			{
//				aspectRatio = 1504;	//	770*1000 / 514 for the no header, no borders version
				aspectRatio = 1472;	//	770*1000 / 534 for old version with header above the slide
				if (inPopout == 0)
				{
//					width = 770;//750;
//					height = 512; //495;
				}
				else
				{
//					width = 924;
//					height = 615;
				}
			}
			else
			{
//				aspectRatio = 1334;	//	660*1000 / 495 for the no header, no borders version
				aspectRatio = 1243;	//	659*1000 / 517 for old version with header above the slide
				if (inPopout == 0)
				{
//					width = 660;
//					height = 495;
				}
				else
				{
//					width = 924;
//					height = 693;
				}
			}
//		}
		if (action.equals("broadcast"))
		{
			movieSwfUrl = "swf/dmsPresentationBroadcaster.swf";
		}
		else
		{
			movieSwfUrl = "swf/dmsPresentationPlayer.swf";
		}
		this.movieName = this.pptName+"Name";
		this.movieNodeName = this.pptName+"Window";
		
		PPTModuleData pmd = new PPTModuleData(action,cflag,width,height,aspectRatio,movieName,
				movieNodeName, movieSwfUrl, pptUrl, numberOfSlides,
				pptName, backgroundColor, startSlideIndex, meetingId, pptType, pptId,
				rtmpUrl,rtmptUrl,annotation,slideWidth,slideHeight);
		this.servletRequest.setAttribute(PPTModuleData.SESSION_ATTRIBUTE_NAME,pmd);
		
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
	public String getAction()
	{
		return action;
	}
	public void setAction(String action)
	{
		this.action = action;
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
	public int getNumberOfSlides()
	{
		return numberOfSlides;
	}
	public void setNumberOfSlides(int numberOfSlides)
	{
		this.numberOfSlides = numberOfSlides;
	}
	public String getPptName()
	{
		return pptName;
	}
	public void setPptName(String pptName)
	{
		this.pptName = pptName;
	}
	public String getPptUrl()
	{
		return pptUrl;
	}
	public void setPptUrl(String pptUrl)
	{
		this.pptUrl = pptUrl;
	}
	public int getWidth()
	{
		return width;
	}
	public void setWidth(int width)
	{
		this.width = width;
	}
	public int getStartSlideIndex()
	{
		return startSlideIndex;
	}
	public void setStartSlideIndex(int startSlideIndex)
	{
		this.startSlideIndex = startSlideIndex;
	}
	public int getInPopout()
	{
		return inPopout;
	}
	public void setInPopout(int inPopout)
	{
		this.inPopout = inPopout;
	}
	public String getMeetingId()
	{
		return meetingId;
	}
	public void setMeetingId(String meetingId)
	{
		this.meetingId = meetingId;
	}
	public String getPptType()
	{
		return pptType;
	}
	public void setPptType(String pptType)
	{
		this.pptType = pptType;
	}
	public String getPptId()
	{
		return pptId;
	}
	public void setPptId(String pptId)
	{
		this.pptId = pptId;
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
	public String getAnnotation()
	{
		return annotation;
	}
	public void setAnnotation(String annotation)
	{
		this.annotation = annotation;
	}
	public int getSlideHeight()
	{
		return slideHeight;
	}
	public void setSlideHeight(int slideHeight)
	{
		this.slideHeight = slideHeight;
	}
	public int getSlideWidth()
	{
		return slideWidth;
	}
	public void setSlideWidth(int slideWidth)
	{
		this.slideWidth = slideWidth;
	}
}
