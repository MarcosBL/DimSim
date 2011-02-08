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

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */

public class WhiteboardModuleData
{
	public	static	final	String	SESSION_ATTRIBUTE_NAME	=	"WHITEBOARD_MODULE_DATA";
	
	protected	String	role;
	protected	String	cflag;
	
	protected	int		width = 640;
	protected	int		height = 480;
	protected	String	movieName = "wb";
	protected	String	movieNodeName = "wbWindow";
	protected	String	movieSwfUrl = "wb_sa.swf";
	protected	String	backgroundColor = "transparent";
	
	protected	String	rtmpUrl;
	protected	String	rtmptUrl;
	
	protected	String	confKey;
	protected	String	name;
	
	public	WhiteboardModuleData()
	{
		
	}
	public WhiteboardModuleData(String role, String cflag, int width, int height,
			String movieName, String movieNodeName, String movieSwfUrl,
			String backgroundColor, String rtmpUrl, String rtmptUrl,
			String confKey, String name)
	{
		this.role = role;
		this.cflag = cflag;
		this.width = width;
		this.height = height;
		this.movieName = movieName;
		this.movieNodeName = movieNodeName;
		this.movieSwfUrl = movieSwfUrl;
		this.backgroundColor = backgroundColor;
		this.rtmpUrl = rtmpUrl;
		this.rtmptUrl = rtmptUrl;
		this.confKey = confKey;
		this.name = name;
	}
	public String getRole()
	{
		return role;
	}
	public void setRole(String role)
	{
		this.role = role;
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
