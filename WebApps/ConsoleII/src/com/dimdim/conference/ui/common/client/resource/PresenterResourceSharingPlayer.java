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

package com.dimdim.conference.ui.common.client.resource;

import java.util.Vector;
import com.google.gwt.user.client.ui.Frame;
import com.dimdim.conference.ui.json.client.UIRosterEntry;
import com.dimdim.conference.ui.json.client.UIPresentationControlEvent;
import com.dimdim.conference.ui.model.client.ConferenceGlobals;
import com.dimdim.conference.ui.common.client.LayoutGlobals;
import com.dimdim.conference.ui.common.client.UIGlobals;
import com.dimdim.conference.ui.common.client.util.DebugPanel;
import com.dimdim.conference.ui.common.client.util.SimpleStringParser;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */

public class PresenterResourceSharingPlayer //extends ResourceSharingPlayer implements ResourcePresentationMovieContainer
{
	/*
	protected	String	resourceId;
//	protected	String	pptUrl;
//	protected	String	pptName;
//	protected	String	pptUrl;
//	protected	int		numSlides = 0;
	
	public	PresenterResourceSharingPlayer(UIRosterEntry me, int width, int height)
	{
		super(me,width,height);
		this.pptMaxWidth = 770;//750;
		this.pptMaxHeight = 512; //495;
		this.pptAspectRatio = 1504;
		this.pptMaxWidthLarge = 924;
		this.pptMaxHeightLarge = 615;
	}
//	public void showPresentation(String swfUrl, int width, int height)
//	{
//		if (this.movie != null)
//		{
//			scroller.remove(movie);
//		}
//		this.shareMovie = true;
//	}
	public	void	showPresentation(String resourceId, String mediaId,
			String pptName, String pptUrl, int numberOfSlides, int lastSlideIndex,
			int slideWidth, int slideHeight, String annotation)
	{
//		Window.alert("Presenter ppt player:"+1);
		this.showPresentation(resourceId,mediaId,pptName,numberOfSlides,
				this.lastKnownWidth,this.lastKnownHeight,lastSlideIndex,false,false,annotation,slideWidth,slideHeight);
		
		this.localEventsModel.pptPresentationStarted(this.lastKnownWidth,
				this.pptMaxWidth,this.lastKnownHeight,this.pptMaxHeight);
	}
	private	void	showPresentation(String resourceId, String pptId, String pptName,
			int numberOfSlides, int width, int height, int startSlideIndex,
			boolean popinShow, boolean onResize,String annotation, int slideWidth, int slideHeight)
	{
//		Window.alert("1");
		if (this.movie != null)
		{
			moviePanel.remove(movie);
			movie = null;
		}
		if (this.waitPageFrame != null)
		{
			moviePanel.remove(this.waitPageFrame);
			this.waitPageFrame = null;
		}
		if (this.pptFrame == null)
		{
			this.pptFrame = new Frame();
		}
		this.resourceId = resourceId;
		this.pptName = pptName;
		this.pptId = pptId;
//		this.pptUrl = pptUrl;
		this.numSlides = numberOfSlides;
		this.slideWidth = slideWidth;
		this.slideHeight = slideHeight;
		this.frameActive = true;
		this.shareMovie = true;
		this.annotation = annotation;
		
		if (!popinShow && !onResize)
		{
			this.setFrameSize(this.lastKnownWidth,this.lastKnownHeight);
			this.pptFrame.setStyleName("ppt-broadcaster-frame");
			this.scroller.remove(moviePanel);
			this.scroller.add(this.pptFrame);
		}
		this.setFrameSize(width,height);
//		Window.alert("2");
//		this.pptFrame.setStyleName("ppt-broadcaster-frame");
//		if (!popinShow)
//		{
//			Window.alert("2");
//			this.scroller.remove(this.moviePanel);
//			this.scroller.add(this.pptFrame);
//		}
//		Window.alert("3");
		String rtmpUrl = "disabled";
		String rtmptUrl = "disabled";
		if (LayoutGlobals.isWhiteboardSupported() && ConferenceGlobals.whiteboardEnabled)
		{
			rtmpUrl = UIGlobals.getStreamingUrlsTable().getWhiteboardRtmpUrl();
			rtmptUrl = UIGlobals.getStreamingUrlsTable().getWhiteboardRtmptUrl();
		}
		String dmsUrl = "http://"+ConferenceGlobals.dmsServerAddress+"/";
		
		String url = "/"+ConferenceGlobals.getWebappName()+
			"/html/ppt/GetPPTModulePage.action?action=broadcast"+
			"&pptUrl="+dmsUrl+
			"&width="+this.pptFrameWidth+
			"&height="+this.pptFrameHeight+
			"&slideWidth="+this.slideWidth+
			"&slideHeight="+this.slideHeight+
			"&numberOfSlides="+numberOfSlides+
			"&inPopout="+(isInPopout()?1:0)+
			"&startSlideIndex="+startSlideIndex+
			"&meetingId="+ConferenceGlobals.getConferenceKey()+
			"&rtmpUrl="+rtmpUrl+
			"&rtmptUrl="+rtmptUrl+
//			"&pptType=preloaded"+
			"&pptId="+pptId+
			"&pptName="+resourceId+
			"&annotation="+annotation+
//			"&inPopout="+(isInPopout()?1:0)+
			"&t="+System.currentTimeMillis();
//		Window.alert(url);
		DebugPanel.getDebugPanel().addDebugMessage(url);
//		this.pptFrame.setUrl("http://jp2.dimdim.com:8080");
		this.pptFrame.setUrl(url);
//		Window.alert("5");
		
//		Window.alert("5");
//		this.addPptSharingControl(true, showSlide, numSlides);
//		Window.alert("6");
//		this.localEventsModel.pptPresentationStarted(this.lastKnownWidth,
//					this.pptMaxWidth,this.lastKnownHeight,this.pptMaxHeight);
//		Window.alert("7");
	}
	public void onTabChange(boolean active)
	{
//		Window.alert("on tab change");
//		super.onTabChange(active);
		this.frameActive = active;
		if (frameActive && resourceId != null)
		{
			this.showPresentation(this.resourceId, this.pptId, this.pptName, this.numSlides,
					this.lastKnownWidth, this.lastKnownHeight, this.pptSharingModel.getCurrentSlideIndex(),
					true,false,annotation,this.slideWidth,this.slideHeight);
		}
	}
	public void panelPoppedIn()
	{
//		Window.alert("on panel popin");
		super.panelPoppedIn();
		if (this.frameActive && resourceId != null)
		{
			int currentSlideIndex = this.pptSharingModel.getCurrentSlideIndex();
			this.showPresentation(this.resourceId, this.pptId, this.pptName, this.numSlides,
				this.lastKnownWidth, this.lastKnownHeight, currentSlideIndex,true,false,annotation,this.slideWidth,this.slideHeight);
		}
	}
	protected	void	refitPptMovieToFrame()
	{
//		Window.alert("Refitting the ppt broadcaster to the new frame size");
		int currentSlideIndex = this.pptSharingModel.getCurrentSlideIndex();
		if (this.getPageName().equals("workspace_popout"))
		{
			String	s = this.getCurrentSlide();
			if (s != null && s.length() > 0)
			{
				currentSlideIndex = (new Integer(s)).intValue();
			}
		}
//		Window.alert("------------------"+currentSlideIndex);
		this.showPresentation(this.resourceId, this.pptId, this.pptName, this.numSlides,
				this.lastKnownWidth, this.lastKnownHeight, currentSlideIndex,false,true,annotation,this.slideWidth,this.slideHeight);
	}
	public void stopPresentation()
	{
		this.numSlides = 0;
		this.scroller.remove(this.pptFrame);
		this.scroller.add(this.moviePanel);
		addWaitPage(this.lastKnownWidth, this.lastKnownHeight);
		if (this.resourceId != null)
		{
			this.pptSharingModel.stopPresentation(this.resourceId);
			this.resourceId = null;
		}
		this.shareMovie = false;
//		this.removePptSharingControl();
	}
	public void setAnnotationStatus(String annotation)
	{
		if (annotation.equalsIgnoreCase(UIPresentationControlEvent.ANNOTATION_ON))
		{
			this.annotation = annotation;
		}
	}
	public String getPanelData()
	{
		String str = "no_data";
		if (this.inConsole)
		{
			if (this.pptName != null && 
					this.numSlides > 0 && this.lastKnownWidth > 0 && this.lastKnownHeight > 0)
			{
				str = this.encodeBase64(this.pptName)+" "+this.pptId+" "+this.numSlides+" "+
						this.lastKnownWidth+" "+this.lastKnownHeight+" "+
						this.pptSharingModel.getCurrentSlideIndex()+" "+this.annotation+" "+
						this.slideWidth+" "+this.slideHeight;
			}
		}
		return str;
	}
	public void readPanelData(String dataText)
	{
		if (this.inPopout)
		{
			if (!dataText.equals("no_data"))
			{
				SimpleStringParser ssp = new SimpleStringParser();
				Vector v = ssp.tokenizeString(dataText);
				if (v != null && v.size() > 4)
				{
					this.pptName = this.decodeBase64(((String)v.elementAt(0)));
					this.resourceId = this.pptName;
					this.pptId = (String)v.elementAt(1);
//					this.pptUrl = this.decodeBase64(((String)v.elementAt(2)));
					this.numSlides = (new Integer((String)v.elementAt(2))).intValue();
//					this.lastKnownWidth = (new Integer((String)v.elementAt(3))).intValue();
//					this.lastKnownHeight = (new Integer((String)v.elementAt(4))).intValue();
					int startSlideIndex = (new Integer((String)v.elementAt(5))).intValue();
					this.pptSharingModel.setCurrentSlideIndex(startSlideIndex);
					this.annotation = this.decodeBase64(((String)v.elementAt(6)));
					this.slideWidth = (new Integer((String)v.elementAt(7))).intValue();
					this.slideHeight = (new Integer((String)v.elementAt(8))).intValue();
					
					this.showPresentation(this.resourceId, this.pptId, this.pptName, this.numSlides,
							this.lastKnownWidth, this.lastKnownHeight, startSlideIndex,false,false,annotation,this.slideWidth,this.slideHeight);
					
					this.localEventsModel.pptPresentationStarted(this.lastKnownWidth,
							this.pptMaxWidth,this.lastKnownHeight,this.pptMaxHeight);
				}
				else
				{
				}
			}
		}
	}
	private	native	String	getPageName()/*-{
		return	$wnd.page_name;
	}-*--/;
	private	native	String	getCurrentSlide()/*-{
		return	$wnd.current_slide;
	}-*--/;
	*/
}

