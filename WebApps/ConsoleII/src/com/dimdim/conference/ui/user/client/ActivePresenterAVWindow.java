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

import com.bouwkamp.gwt.user.client.ui.RoundedPanel;
import com.dimdim.conference.ui.common.client.LayoutGlobals;
//import com.dimdim.conference.ui.common.client.UIGlobals;
import com.dimdim.conference.ui.common.client.UIImages;
//import com.dimdim.conference.ui.common.client.data.UIParams;
import com.dimdim.conference.ui.json.client.UIRosterEntry;
//import com.dimdim.conference.ui.model.client.ConferenceGlobals;
import com.google.gwt.user.client.Window;
//import com.google.gwt.user.client.WindowResizeListener;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.NonModalDraggablePopupPanel;
import com.google.gwt.user.client.ui.PopupListener;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
//import com.dimdim.conference.ui.common.client.user.NewChatPopup;
import com.dimdim.conference.ui.common.client.util.DimdimPopup;
import com.dimdim.conference.ui.common.client.util.DimdimPopupsLayoutManager;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * This popup contains a single chat panel. It supports minimize, maximize
 * and popout if popups are not blocked by the browser.
 */

public class ActivePresenterAVWindow extends NonModalDraggablePopupPanel implements 
	PopupListener, DimdimPopup
{
	protected	UIRosterEntry	me;
	
	protected	VerticalPanel	outer = new VerticalPanel();
	protected	HorizontalPanel	header	=	new	HorizontalPanel();
	protected	HorizontalPanel	headerPanel = new HorizontalPanel();
	
//	protected	ActivePresenterAVManager	avManager;
	protected	ScrollPanel	scrollPanel;
	protected	AVWindow	avWindow;
	
//	protected	DropDownPanel	avDropDown;
	protected	RoundedPanel	avRoundedPanel;
	
	protected	int	floatPositionIndex = -1;
	
	protected	String	movieUrl;
	protected	int		movieWidth;
	protected	int		movieHeight;
	protected	String	suffix;
	protected	String	sizeFactor;
	
	/**
	 * Same chat panel is used for global as well as personal chats. Global
	 * chat is simply identified by using 'other' argument as null.
	 */
	public	ActivePresenterAVWindow(UIRosterEntry me, String captionText,
			String movieUrl, int movieWidth, int movieHeight, String suffix,
			String sizeFactor, ClickListener presenterSizeControl)
	{
		super(false);
//		Window.alert("ActivePresenterAVWindow::ActivePresenterAVWindow");
		
		this.me = me;
		this.movieUrl = movieUrl;
		this.movieWidth = movieWidth;
		this.movieHeight = movieHeight;
		this.suffix = suffix;
		this.sizeFactor = sizeFactor;
		//	Add header.
		
		caption.setStyleName("common-float-panel-header");
		caption.addStyleName("common-text");
		
		this.caption.setText(captionText);
		
		ScrollPanel s1 = new ScrollPanel();
		headerPanel.setSize(movieWidth+"px", "14px");
		s1.setSize(movieWidth+"px","14px");
		s1.add(headerPanel);
		
		if (presenterSizeControl != null)
		{
			//	This is a broadcaster on presenter console. Add the size
			//	change image in the header.
			header.add(caption);
			header.setCellWidth(caption, "100%");
			header.setCellHorizontalAlignment(caption, HorizontalPanel.ALIGN_LEFT);
			
			Image image =this.getHeaderImageUrl();
			header.add(image);
			image.addStyleName("anchor-cursor");
			header.setCellHorizontalAlignment(image, HorizontalPanel.ALIGN_RIGHT);
			image.addClickListener(presenterSizeControl);
			headerPanel.add(header);
			
			header.setWidth(movieWidth+"px");
//			outer.setCellWidth(header, "100%");
		}
		else
		{
//			caption.setWidth(movieWidth+"px");
			headerPanel.add(caption);
			header.setCellWidth(caption, "100%");
		}
		outer.add(s1);
		
		avWindow = new AVWindow("AVPlayer"+suffix,"AV"+suffix,
				movieUrl,movieWidth,movieHeight);
		this.panelHeight = movieHeight+24;
		this.setHeight((movieHeight+24)+"px");
		this.panelWidth = movieWidth+8;
		this.setWidth((movieWidth+8)+"px");
		
		this.scrollPanel = new ScrollPanel();
		this.scrollPanel.setSize(movieWidth+"px", movieHeight+"px");
		this.scrollPanel.add(avWindow);
		outer.setStyleName("common-float-panel-content");
		outer.add(scrollPanel);
		outer.setCellHorizontalAlignment(avWindow,HorizontalPanel.ALIGN_CENTER);
		outer.setCellVerticalAlignment(avWindow,VerticalPanel.ALIGN_MIDDLE);
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
	}
	public int getPopupWidth()
	{
		return	this.panelWidth;
	}
	public int getPopupHeight()
	{
		return	this.panelHeight;
	}
	public boolean stopPresenterAudioVideo()
	{
		return	this.avWindow.stopAV("AV"+this.suffix);
	}
	public void startPresenterAudioVideo()
	{
		this.avWindow.startMovie();
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
		if (!sizeFactor.equals("1"))
		{
			this.setPositionToCenter();
			return;
		}
		/*
		if (this.floatPositionIndex == -1)
		{
			this.floatPositionIndex = LayoutGlobals.getAvailableAVFloatSlot();
		}
		*/
		/*
		int left = 5;
		int allowance = LayoutGlobals.getAVPlayerFloatPanelHeight(sizeFactor);
//		if (avManager.isAudioOnly())
//		{
//			allowance = LayoutGlobals.getAudioBroadcasterFloatPanelHeight();
//		}
		if (floatPositionIndex == 2)
		{
			if (ConferenceGlobals.isMeetingVideoChat())
			{
				allowance += (LayoutGlobals.getAVPlayerFloatPanelHeight("1")+2);
			}
			else
			{
				allowance += (LayoutGlobals.getAudioBroadcasterFloatPanelHeight()+2);
			}
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
	public	void	setPositionToWorkspaceCorner()
	{
//		int h = LayoutGlobals.getAVPlayerFloatPanelHeight(sizeFactor);
		int left = 268;
		int top = 101;
//		if (Window.getClientHeight() < h+111)
//		{
//			top = 0;
//		}
		this.setPopupPosition(left, top);
	}
	public	void	setPositionToCenter()
	{
		int h = LayoutGlobals.getAVPlayerFloatPanelHeight(sizeFactor);
		int left = (Window.getClientWidth()/2) - (h/2);
		int top = (Window.getClientHeight()/2) - (h/2);
		if (top<0)
		{
			top = 0;
		}
		else
		{
			if (top < 101)
			{
				top = 101;
			}
		}
		this.setPopupPosition(left, top);
	}
	public void onWindowResized (int widthx, int heightx)
	{
		if (!this.sizeFactor.equals("1"))
		{
//			this.setPositionToWorkspaceCorner();
		}
		else
		{
//			setPosition();
		}
	}
	private	Image	getHeaderImageUrl()
	{
		if (this.sizeFactor.equals("1"))
		{
			return	UIImages.getImageBundle(UIImages.defaultSkin).getMaximizeVideo();
		}
		return	UIImages.getImageBundle(UIImages.defaultSkin).getMinimizeVideo();
	}
	public	void	restartAsRecord()
	{
		if (this.avWindow != null)
		{
			this.avWindow.restartAsRecord();
		}
	}
	public	void	restartAsLive()
	{
		if (this.avWindow != null)
		{
			this.avWindow.restartAsLive();
		}
	}
}
