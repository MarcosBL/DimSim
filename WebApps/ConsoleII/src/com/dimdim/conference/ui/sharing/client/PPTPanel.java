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

package com.dimdim.conference.ui.sharing.client;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;

import com.dimdim.conference.ui.model.client.JSCallbackListener;
import com.dimdim.conference.ui.model.client.JSInterface;
import com.dimdim.conference.ui.json.client.UIPresentationControlEvent;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * This overall full page panel will simply provide a common banner, lhs
 * and rhs sections page. The lhs will contain all the available tests and
 * results, progress or any associated components for the tests will be
 * displayed in the rhs.
 */

public class PPTPanel	extends	Composite //implements JSCallbackListener
{
	/*
	protected	VerticalPanel	basePanel	=	new	VerticalPanel();
//	protected	DmFlashWidget2	pptMovie	=	null;
	
	protected	int		width = 800;
	protected	int		height = 500;
	protected	int		slideWidth = 800;
	protected	int		slideHeight = 500;
	protected	String	movieName = "ppt";
	protected	String	movieNodeName = "pptWindow";
	protected	String	movieSwfUrl = "";
	protected	String	pptUrl = "";
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
	
//	?$27$ppt1$Jayant", "avWindow", "700", "600", "8", "transparent"
//	public	PPTPanel()
//	{
//		this.initMovie();
//	}
	public PPTPanel(int width, int height, int aspectRatio,
			String movieName, String movieNodeName,
			String movieSwfUrl, String pptUrl, int numberOfSlides,
			String pptName, String backgroundColor, int startSlideIndex,
			String meetingId, String pptType, String pptId,
			String rtmpUrl, String rtmptUrl, String annotation, int slideWidth, int slideHeight)
	{
		initWidget(this.basePanel);
		this.width = width;
		this.height = height;
		this.slideWidth = slideWidth;
		this.slideHeight = slideHeight;
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
		
		int frameAspectRatio = this.width*1000 /this.height;
		if (frameAspectRatio == aspectRatio)
		{
			//	Exact size. Will have to be very lucky to get here.
		}
		else if (frameAspectRatio < aspectRatio)
		{
			//	Available height is larger than required to match the full
			//	width. Keep the width same and reduce the movie height to
			//	scale.
			this.height = (this.width*1000)/aspectRatio;
		}
		else
		{
			//	Available width is larger than required to match the full
			//	height. Keep the height same and reduce the width.
			this.width = (this.height*aspectRatio)/1000;
		}
		
//		Window.alert(this.width+"--"+this.height);
		
		JSInterface.addCallbackListener(this);
	}
	public	void	initMovie()
	{
//		Window.alert("PPTPanel:initMovie() - 1");
		this.setSize((width)+"px",(height)+"px");
//		this.setStyleName("ppt-module-base-panel");
		
//		Window.alert("PPTPanel:initMovie() - 2");
		String fullMovieUrl = this.movieSwfUrl+
			"?wbUrl=swf/wb.swf&dmsUrl="+this.pptUrl+
			"&slideCount="+this.numberOfSlides+
			"&slideWidth="+this.slideWidth+
			"&slideHeight="+this.slideHeight+
			"&pptId="+this.pptId+
			"&pptName="+this.pptName+
			"&startSlide="+this.startSlideIndex+
			"&meetingId="+this.meetingId+
			"&rtmpUrl="+this.rtmpUrl+
			"&rtmptUrl="+this.rtmptUrl+
			"&ann="+this.annotation+
			"&cflag="+System.currentTimeMillis();
		
//		this.pptMovie = new DmFlashWidget2(movieName,movieNodeName,
//					width+"px",
//					height+"px",
//					fullMovieUrl,
//					backgroundColor);
//		basePanel.add(pptMovie);
//		basePanel.setCellHorizontalAlignment(pptMovie, HorizontalPanel.ALIGN_CENTER);
//		basePanel.setCellVerticalAlignment(pptMovie, VerticalPanel.ALIGN_TOP);
//		pptMovie.show();
//		pptMovie.setStyleName("ppt-module-movie");
//		Window.alert("PPTPanel:initMovie() - 3");
	}
	public int getHeight()
	{
		return height;
	}
	public void setHeight(int height)
	{
		this.height = height;
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
	public String getBackgroundColor()
	{
		return backgroundColor;
	}
	public void setBackgroundColor(String backgroundColor)
	{
		this.backgroundColor = backgroundColor;
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
	public String getListenerName()
	{
		return "PPT_PLAYER";
	}
	//
	//	There is only 1 call required and used right now, which is slide changed.
	//
	public void handleCallFromJS(String data)
	{
		if (data.equals(UIPresentationControlEvent.ANNOTATION_ON) ||
					data.equals(UIPresentationControlEvent.ANNOTATION_ON))
		{
//			this.pptMovie.sendAnnotationMessage(movieNodeName,data);
		}
		else
		{
//			this.pptMovie.setSlide(movieNodeName,(new Integer(data)).intValue());
		}
	}
	public void handleCallFromJS2(String data1, String data2)
	{
		//	Not required.
	}
	public void handleCallFromJS3(String data1, String data2, String data3)
	{
		//	Not required.
	}
	*/
}
