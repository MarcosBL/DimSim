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
 * Part of the DimDim V 2.0 Codebase (http://www.dimdim.com)	          *
 *                                                                        *
 * Copyright (c) 2008 Dimdim Inc. All Rights Reserved.              	  *
 *                                                                        *
 *                                                                        *
 * This code is licensed under the DimDim License                         *
 * For details please visit http://www.dimdim.com/license                 *
 *                                                                        *
 **************************************************************************
 */

package com.dimdim.conference.ui.sharing.client;

import com.dimdim.conference.ui.json.client.UIResourceObject;
import com.dimdim.conference.ui.model.client.ConferenceGlobals;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
//import com.google.gwt.user.client.Window;
import pl.rmalinowski.gwt2swf.client.ui.SWFParams;
import pl.rmalinowski.gwt2swf.client.ui.SWFCallableWidget;
import pl.rmalinowski.gwt2swf.client.ui.SWFWidget;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */

public class PPTPlayerPanel extends CollaborationWidgetDisplayPanel
{
	protected	Timer	t;
	
	public	PPTPlayerPanel()
	{
		this.add(this.moviePanel);
		this.setCellHorizontalAlignment(this.moviePanel, HorizontalPanel.ALIGN_CENTER);
		this.setCellVerticalAlignment(this.moviePanel, VerticalPanel.ALIGN_TOP);
	}
	/**
	 * The whiteboard resource never changes. So once this widget is
	 * initialized, it will remain on this single resource.
	 */
	public void refreshWidget(UIResourceObject resource, int width, int height)
	{
//		if (super.requiresRefresh(resource, width, height))
//		{
//			super.refreshWidget(resource,width,height);
		super.refreshWidget(resource, width, height);
		this.setSize(width+"px", height+"px");
		CollaborationResources cr = this.container.getCollaborationesources();
		this.calculateMovieSize(width,height,cr.getPPTPlayerStageAspectRatio(),
				cr.getPPTPlayerMinimumWidth(),cr.getPPTPlayerMinimumHeight());
		this.moviePanel.setSize(movieWidth+"px", movieHeight+"px");
		if (this.movieWidget == null)
		{
//			if (this.movieWidget != null)
//			{
//				this.remove(this.movieWidget);
//				this.movieWidget = null;
//			}
			String fullMovieUrl = this.container.getCollaborationesources().getPPTPlayerMovieURL()+
				"?"+this.getPptPlayerArgs(resource);
			
			SWFParams movieWidgetParams = new SWFParams(fullMovieUrl,"100%","100%");//,this.movieWidth,this.movieHeight);
			movieWidgetParams.setWmode("");
			if(ConferenceGlobals.isBrowserSafari())
			{
				movieWidgetParams.setWmode("opaque");
			}
			String s = null;
			movieWidget = new SWFCallableWidget(movieWidgetParams,"pptMovie","pptMovieDiv",s);
			this.moviePanel.add(movieWidget);
			if (ConferenceGlobals.isBrowserFirefox())
			{
				HTML l = new HTML("<span id=\"forceheight\">&nbsp;</span>");
				this.add(l);
			}
		}
//		}
	}
	public	void	changeSlide(final int newSlideIndex)
	{
//		Window.alert("Changing slide to:"+newSlideIndex);
		if (this.t != null)
		{
			this.t.cancel();
			this.t = null;
		}
		int ret = ((SWFCallableWidget)this.movieWidget).callP1("setSlideIndex", newSlideIndex);
		if (ret == 0)
		{
//			Window.alert("PPT Player movie not fully loaded yet");
			this.t = new Timer()
			{
				public void run()
				{
					((SWFCallableWidget)movieWidget).callP1("setSlideIndex", newSlideIndex);
					t = null;
				}
			};
			this.t.schedule(50);
		}
	}
	private	String	getPptPlayerArgs(UIResourceObject res)
	{
		StringBuffer buf = new StringBuffer();
		
		String rtmpUrl = this.container.getCollaborationesources().getStreamingUrlsTable().getWhiteboardRtmpUrl();
		String rtmptUrl = this.container.getCollaborationesources().getStreamingUrlsTable().getWhiteboardRtmptUrl();
		if (!ConferenceGlobals.whiteboardEnabled || rtmpUrl == null || rtmpUrl.length() == 0)
		{
			rtmpUrl = "disabled";
			rtmptUrl = "disabled";
		}
		String dmsUrl = "http://"+ConferenceGlobals.dmsServerAddress+"/";
		
		buf.append("dmsUrl="+dmsUrl);
		buf.append("&slideCount="+res.getSlideCount());
		buf.append("&pptId="+res.getMediaId());
		buf.append("&pptName="+res.getResourceId());
		buf.append("&startSlide="+res.getLastSlideIndex());
		buf.append("&meetingId="+ConferenceGlobals.conferenceKeyQualified);
		buf.append("&rtmpUrl="+rtmpUrl);
		buf.append("&rtmptUrl="+rtmptUrl);
		buf.append("&ann=ann_on");
		buf.append("&slideWidth="+res.getWidth());
		buf.append("&slideHeight="+res.getHeight());
		buf.append("&t="+System.currentTimeMillis());
		
		return	buf.toString();
	}
	public void widgetHiding()
	{
		try
		{
			this.moviePanel.remove(this.movieWidget);
			this.movieWidget = null;
		}
		catch(Exception e)
		{
			
		}
		super.widgetHiding();
	}
}
