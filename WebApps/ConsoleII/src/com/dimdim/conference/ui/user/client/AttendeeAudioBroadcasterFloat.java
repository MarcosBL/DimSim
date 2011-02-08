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
 *								                                          *
 * Copyright (c) 2006 Communiva Inc. All Rights Reserved.	              *
 *								                                          *
 *								                                          *
 * This code is licensed under the DimDim License                         *
 * For details please visit http://www.dimdim.com/license		          *
 *									                                      *
 **************************************************************************
 */

package com.dimdim.conference.ui.user.client;

//import asquare.gwt.tk.client.ui.DropDownPanel;

import pl.rmalinowski.gwt2swf.client.ui.SWFCallableWidget;

import com.bouwkamp.gwt.user.client.ui.RoundedPanel;
import com.dimdim.conference.ui.common.client.LayoutGlobals;
import com.dimdim.conference.ui.common.client.UIGlobals;
import com.dimdim.conference.ui.common.client.data.UIParams;
import com.dimdim.conference.ui.common.client.util.DimdimPopup;
import com.dimdim.conference.ui.common.client.util.DimdimPopupsLayoutManager;

import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.NonModalDraggablePopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.PopupListener;
import com.google.gwt.user.client.WindowResizeListener;
import com.google.gwt.user.client.Window;

import com.dimdim.conference.ui.json.client.UIRosterEntry;
import com.dimdim.conference.ui.model.client.ConferenceGlobals;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * This popup contains a single chat panel. It supports minimize, maximize
 * and popout if popups are not blocked by the browser.
 */

public class AttendeeAudioBroadcasterFloat extends NonModalDraggablePopupPanel implements 
	PopupListener, DimdimPopup
{
	protected	UIRosterEntry	me;
	
	protected	VerticalPanel	outer = new VerticalPanel();
	protected	HorizontalPanel	headerPanel = new HorizontalPanel();
	
//	protected	DmFlashWidget2	movie;
	protected	ScrollPanel	scrollPanel;
	protected	RoundedPanel	avRoundedPanel;
	
	protected	int	floatPositionIndex = -1;
	protected	String movieUrl;
	protected	int	movieWidth;
	protected	int	movieHeight;
	protected	String	sizeFactor;
	
	protected	SWFCallableWidget	movie;
	
	/**
	 * Same chat panel is used for global as well as personal chats. Global
	 * chat is simply identified by using 'other' argument as null.
	 */
	public	AttendeeAudioBroadcasterFloat(UIRosterEntry me, SWFCallableWidget movie,
			int movieWidth, int movieHeight, String sizeFactor, String captionKey, String movieUrl)
	{
		super(false);
		
		this.me = me;
		this.movie = movie;
		this.movieUrl = movieUrl;
		this.movieWidth = movieWidth;
		this.movieHeight = movieHeight;
		this.sizeFactor = sizeFactor;
		
		//	Add header.
		
		caption.setStyleName("common-float-panel-header");
		caption.addStyleName("common-text");
		
		String name = this.me.getDisplayName();
//		String captionKey = "audio_broadcaster_caption";
		String captionSuffix = ConferenceGlobals.uiStringsDictionary.getStringValue(captionKey);
		if (captionSuffix == null || captionSuffix.equals(captionKey))
		{
			captionSuffix = "";
		}
		String s = name;
		if (s.length() > 10)
		{
			s = s.substring(0,10)+"...";
		}
		this.caption.setText(s+" "+captionSuffix);
		
		ScrollPanel s1 = new ScrollPanel();
		headerPanel.setSize(movieWidth+"px", "14px");
		s1.setSize(movieWidth+"px","14px");
		s1.add(headerPanel);
		
//		caption.setWidth(movieWidth+"px");
		headerPanel.add(caption);
		headerPanel.setCellWidth(caption, "100%");
		outer.add(s1);
		
		this.panelHeight = movieHeight+24;
		this.setHeight((movieHeight+24)+"px");
		this.panelWidth = movieWidth+8;
		this.setWidth((movieWidth+8)+"px");
		
		this.scrollPanel = new ScrollPanel();
		this.scrollPanel.setSize(movieWidth+"px", movieHeight+"px");
		this.scrollPanel.add(movie);
		
		outer.setStyleName("common-float-panel-content");
		outer.add(scrollPanel);
		outer.setCellHorizontalAlignment(movie,HorizontalPanel.ALIGN_CENTER);
		outer.setCellVerticalAlignment(movie,VerticalPanel.ALIGN_MIDDLE);
		
		this.avRoundedPanel = new RoundedPanel(this.outer);
		add(this.avRoundedPanel);
		
//		Window.addWindowResizeListener(this);
		DimdimPopupsLayoutManager.getManager(me).dimdimPopupOpened(this);
		
		addPopupListener(this);
	}
	public	String	getPopupId()
	{
		return	this.movieUrl;
	}
	public int getPopupType()
	{
		if (this.movieUrl.indexOf("Broadcaster") > 0)
		{
			if (this.movieUrl.indexOf("avBroadcaster") >= 0)
			{
				return DimdimPopup.VideoBroadcaster;
			}
			else
			{
				return DimdimPopup.AudioBroadcaster;
			}
		}
		else
		{
			return DimdimPopup.VideoPlayer;
		}
//		return DimdimPopup.VideoPlayer;
	}
	public int getPopupWidth()
	{
		return	this.panelWidth;
	}
	public int getPopupHeight()
	{
		return	this.panelHeight;
	}
	public void onPopupClosed(PopupPanel pp, boolean autoClosed)
	{
//		Window.removeWindowResizeListener(this);
		DimdimPopupsLayoutManager.getManager(me).dimdimPopupClosed(this);
	}
	public	int	getFloatPositionIndex()
	{
		return	this.floatPositionIndex;
	}
	public	void	setPosition()
	{
		/*
		if (this.floatPositionIndex == -1)
		{
			this.floatPositionIndex = LayoutGlobals.getAvailableAVFloatSlot();
		}
		*/
		/*
		int left = 5;
		int allowance = LayoutGlobals.getAudioBroadcasterFloatPanelHeight();
		if (ConferenceGlobals.isMeetingVideoChat())
		{
			allowance = LayoutGlobals.getAVPlayerFloatPanelHeight("1");
		}
		if (floatPositionIndex == 2)
		{
			allowance += (LayoutGlobals.getAVPlayerFloatPanelHeight(sizeFactor)+2);
		}
		int top = ConferenceGlobals.getContentHeight() - (allowance+2);
		int footerAllowance = UIParams.getUIParams().getBrowserParamIntValue("av_box_footer_allowance", 0);
		top = top - footerAllowance;
		*/
		/*
		int	left = LayoutGlobals.getAVFloatLeft(this.floatPositionIndex);
		int	top = LayoutGlobals.getAVFloatTop(this.floatPositionIndex);
		if (top < 0)
		{
			top = 0;
		}
		this.setPopupPosition(left, top);
		*/
		DimdimPopupsLayoutManager.getManager(me).repositionPopups();
	}
	public	void	setPositionToCenter()
	{
		int h = this.movieHeight;//LayoutGlobals.getAudioBroadcasterFloatPanelHeight();
		int left = (Window.getClientWidth()/2) - (h/2);
		int top = (Window.getClientHeight()/2) - (h/2);
		this.setPopupPosition(left, top);
	}
	public void onWindowResized (int widthx, int heightx)
	{
//		setPosition();
	}
	public	void	restartAsRecord()
	{
		if (this.movie != null)
		{
//			Window.alert("Calling restartAsRecord");
			this.movie.call("restartAsRecord");
		}
	}
	public	void	restartAsLive()
	{
		if (this.movie != null)
		{
//			Window.alert("Calling restartAsLive");
			this.movie.call("restartAsLive");
		}
	}
}
