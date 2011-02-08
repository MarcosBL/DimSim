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

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */

public class PPTModuleData
{
	public	static	final	String	SESSION_ATTRIBUTE_NAME	=	"PPT_MODULE_DATA";
	
	protected	String	action;
	protected	String	cflag;
	
	protected	int		width = 800;
	protected	int		height = 500;
	protected	int		slideWidth = -1;
	protected	int		slideHeight = -1;
	protected	int		aspectRatio = 1334;	//	/1000 is the real value.
	protected	String	movieName = "ppt";
	protected	String	movieNodeName = "pptWindow";
	protected	String	movieSwfUrl = "presentationBroadcaster.swf";
	protected	String	pptUrl = "http://jayantpandit.ca:8080/ppttest/b56379861/";
	protected	int		numberOfSlides = 4;
	protected	int		startSlideIndex = 0;
	protected	String	pptName = "Default";
	protected	String	backgroundColor = "transparent";
	protected	String	meetingId = "global-meeting";
	protected	String	pptType = "preloaded";
	protected	String	pptId = "";
	protected	String	rtmpUrl = "";
	protected	String	rtmptUrl = "";
	protected	String	annotation = "";
	
	public	PPTModuleData()
	{
		
	}
	public PPTModuleData(String action, String cflag, int width, int height, int aspectRatio,
			String movieName, String movieNodeName, String movieSwfUrl,
			String pptUrl, int numberOfSlides, String pptName, String backgroundColor,
			int startSlideIndex,String meetingId, String pptType, String pptId,
			String rtmpUrl, String rtmptUrl, String annotation, int slideWidth, int slideHeight)
	{
		this.action = action;
		this.cflag = cflag;
		this.width = width;
		this.height = height;
		this.slideWidth = slideWidth;
		this.slideHeight = slideHeight;
		this.aspectRatio = aspectRatio;
		this.movieName = movieName;
		this.movieNodeName = movieNodeName;
		this.movieSwfUrl = movieSwfUrl;
		this.pptUrl = pptUrl;
		this.numberOfSlides = numberOfSlides;
		this.pptName = pptName;
		this.backgroundColor = backgroundColor;
		this.startSlideIndex = startSlideIndex;
		this.meetingId = meetingId;
		this.pptType = pptType;
		this.pptId = pptId;
		this.rtmpUrl = rtmpUrl;
		this.rtmptUrl = rtmptUrl;
		this.annotation = annotation;
	}
	public String getAction()
	{
		return action;
	}
	public void setAction(String action)
	{
		this.action = action;
	}
	public String getCflag()
	{
		return cflag;
	}
	public void setCflag(String cflag)
	{
		this.cflag = cflag;
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
	public int getAspectRatio()
	{
		return aspectRatio;
	}
	public void setAspectRatio(int aspectRatio)
	{
		this.aspectRatio = aspectRatio;
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
