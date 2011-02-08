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

package com.dimdim.conference.ui.common.client.user;

import com.dimdim.conference.ui.common.client.UIImages;
import com.dimdim.conference.ui.common.client.LayoutGlobals;
import com.dimdim.conference.ui.common.client.UserGlobals;
import com.dimdim.conference.ui.common.client.data.UIParams;
import com.dimdim.conference.ui.common.client.util.FixedLengthLabel;
import com.dimdim.conference.ui.common.client.util.DimdimPopup;
import com.dimdim.conference.ui.common.client.util.DimdimPopupsLayoutManager;
import com.dimdim.conference.ui.json.client.UIRosterEntry;
import com.dimdim.conference.ui.model.client.ClientModel;
import com.dimdim.conference.ui.model.client.ConferenceGlobals;
import com.dimdim.conference.ui.model.client.RosterModel;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Timer;
//import com.google.gwt.user.client.Window;
//import com.google.gwt.user.client.WindowResizeListener;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.FocusListener;
//import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.NonModalDraggablePopupPanel;
import com.google.gwt.user.client.ui.PopupListener;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * This popup contains a single chat panel. It supports minimize, maximize
 * and popout if popups are not blocked by the browser.
 */

public class NewChatPopup extends NonModalDraggablePopupPanel implements FocusListener,
	ClickListener, PopupListener, DimdimPopup, NewChatUnreadMessageListener
{
	public	static	int	privateChatPanelHeight = 225;
	public	static	int	privateChatPanelHeaderHeight = 23;
	
	protected	UIRosterEntry	me;
	protected	UIRosterEntry	other;
	
	protected	NewChatPanel	chatPanel;
	protected	int	index = 1;
	protected	boolean	active = false;
	
	protected	DockPanel	outer = new DockPanel();
	protected	DockPanel	body = new DockPanel();
	
	public	DockPanel	header;
	protected	Label	popoutLink;
	protected 	Label   nameLabel;
	protected	HorizontalPanel	pane = new HorizontalPanel();
//	protected	HorizontalPanel	scrollPanel = new HorizontalPanel();;
	protected	ScrollPanel		scWrapperPanel = new ScrollPanel();;
	protected	HorizontalPanel	hzWrapperPanel = new HorizontalPanel();;
	
	protected	Image		closeImage;
	protected	Image		minimizeImage;
	protected	Image		maximizeImage = null;
	protected   Timer toggle;
	protected	String	toId;
	
	protected	KeyboardListener	keyboardListener;
	
	protected	boolean		hasMessage	=	false;
	protected	boolean		textBoxHasFocus = false;
	protected	boolean		minimized	=	false;
	protected	boolean		hasUnreadMessage	=	false;	
	protected HorizontalPanel headerButtons = null;
	protected Image image = UIImages.getImageBundle(UIImages.defaultSkin).getNewMessage();
	protected Image imageChange = UIImages.getImageBundle(UIImages.defaultSkin).getNoMessage();
	
	
	boolean checkImageFirstTime;
	boolean checkMinimizedMessageAgain = false;
	protected static int counter = 0;
	protected	RosterModel		rosterModel;
	
	/**
	 * Same chat panel is used for global as well as personal chats. Global
	 * chat is simply identified by using 'other' argument as null.
	 */
	public	NewChatPopup(NewChatPanel chatPanel, int index)
	{
		super(false);
		this.index = index;
		this.chatPanel = chatPanel;
		this.me = this.chatPanel.getMe();
		this.other = this.chatPanel.getOther();
		this.rosterModel = ClientModel.getClientModel().getRosterModel();
		//	Add header.
		
		header = new DockPanel();
		header.setStyleName("dm-user-info-header");
		header.setWidth("248px");
		header.setSpacing(4);

		String role = this.me.getRole();
		if (other != null)
		{
			role = other.getRole();
			toId = other.getUserId();
		}
		Image image = UserGlobals.getUserGlobals().getRoleImageUrl(role);
		header.add(image,DockPanel.WEST);
		header.setCellVerticalAlignment(image,VerticalPanel.ALIGN_MIDDLE);
		
		String name = this.me.getDisplayName();
		if (other != null)
		{
			name = other.getDisplayName();
		}
		
		name = ConferenceGlobals.getDisplayString("console.private.chat.with","Private chat with")+" " + name; 
		
		nameLabel = new FixedLengthLabel(name,27);
		nameLabel.setStyleName("dm-popup-header-label");
		nameLabel.addStyleName("draggable-panel-header");
		nameLabel.addClickListener(this);
		nameLabel.setWordWrap(false);
		nameLabel.addMouseListener(this);
		
		header.add(nameLabel,DockPanel.WEST);
		header.setCellVerticalAlignment(nameLabel,VerticalPanel.ALIGN_MIDDLE);
		
		HTML filler = new HTML("");
		header.add(filler,DockPanel.CENTER);
		header.setCellVerticalAlignment(filler,VerticalPanel.ALIGN_MIDDLE);
		
		headerButtons = new HorizontalPanel();
		headerButtons.setStyleName("chat-header-buttons-panel");
		
		minimizeImage = UIImages.getImageBundle(UIImages.defaultSkin).getMinimize();
		//minimizeImage.setStyleName("minimize-chat-popup");
		minimizeImage.addStyleName("chat-header-button");
		headerButtons.add(minimizeImage);
		minimizeImage.addClickListener(this);
		
		closeImage = UIImages.getImageBundle(UIImages.defaultSkin).getClose();
		//closeImage.setStyleName("close-chat-popup");
		closeImage.addStyleName("chat-header-button");
		headerButtons.add(closeImage);
		closeImage.addClickListener(this);
		
		this.setImage();
		
		header.add(headerButtons,DockPanel.EAST);
		header.setCellWidth(filler,"100%");
		//	Assemble the overall chat panel.
		
		add(pane);
		this.setHeight(NewChatPopup.privateChatPanelHeight+"px");
		pane.setWidth("100%");
		if (ConferenceGlobals.isBrowserFirefox())
		{
			pane.add(scWrapperPanel);
			scWrapperPanel.setSize("248px",NewChatPopup.privateChatPanelHeight+"px");
			scWrapperPanel.add(outer);
			scWrapperPanel.setAlwaysShowScrollBars(false);
		}
		else
		{
			pane.add(hzWrapperPanel);
			hzWrapperPanel.setSize("248px",NewChatPopup.privateChatPanelHeight+"px");
			hzWrapperPanel.add(outer);
		}
		
		outer.add(header,DockPanel.NORTH);
		
		outer.add(this.chatPanel,DockPanel.CENTER);
		outer.setCellWidth(chatPanel,"100%");
		
		setStyleName("dm-chat-popup");
//		Window.addWindowResizeListener(this);
		
		addPopupListener(this);
		chatPanel.setUnreadMessaeListener(this);
		
		this.setHeight();
	}
	public	UIRosterEntry	getOther()
	{
		return	this.other;
	}
	public	NewChatPanel	getChatPanel()
	{
		return	this.chatPanel;
	}
	
	private void setImage(){
//		Window.alert("is minimized : " + this.minimized);
		if(this.minimized)
		{
			maximizeImage = UIImages.getImageBundle(UIImages.defaultSkin).getMaximize();
			//maximizeImage.setStyleName("maximize-chat-popup");
			maximizeImage.addStyleName("chat-header-button");
			maximizeImage.addClickListener(this);
			this.headerButtons.remove(minimizeImage);
			headerButtons.remove(closeImage);
			this.headerButtons.add(maximizeImage);			
			headerButtons.add(closeImage);
		}
		else
		{
			if(maximizeImage != null)
				this.headerButtons.remove(maximizeImage);
			headerButtons.remove(closeImage);
			this.headerButtons.add(minimizeImage);
			headerButtons.add(closeImage);
		}
	}
	
	private	void	setHeight()
	{
		if (this.minimized)
		{
			if (ConferenceGlobals.isBrowserIE())
			{
				this.hzWrapperPanel.setHeight("25px");
				this.setHeight("25px");
			}
			else if (ConferenceGlobals.isBrowserFirefox())
			{
				this.scWrapperPanel.setHeight("24px");
				this.setHeight("24px");
			}
			else
			{
				this.hzWrapperPanel.setHeight("24px");
				this.setHeight("24px");
			}
		}
		else
		{
			if (ConferenceGlobals.isBrowserFirefox())
			{
				this.scWrapperPanel.setHeight(NewChatPopup.privateChatPanelHeight+"px");
			}
			else
			{
				this.hzWrapperPanel.setHeight(NewChatPopup.privateChatPanelHeight+"px");
			}
			this.setHeight(NewChatPopup.privateChatPanelHeight+"px");
			this.chatPanel.setScrollPanelHeight(NewChatPopup.privateChatPanelHeight-
					(UIParams.getUIParams().getChatTextAreaHeight()+
						NewChatPopup.privateChatPanelHeaderHeight));
		}
	}
	public void onPopupClosed(PopupPanel pp, boolean autoClosed)
	{
		DimdimPopupsLayoutManager.getManager(me).dimdimPopupClosed(this);
//		Window.removeWindowResizeListener(this);
		if (this.index != -1)
		{
			//	-1 means that the chat is hidden at the time of close.
			//	Will happen if a user leaves while the chat is hidden.
			NewChatController.getController().setChatPopupIndexFree(this.index);
		}
	}
	public	String	getPopupId()
	{
		if (this.other == null)
		{
			return	this.me.getUserId();
		}
		return	this.other.getUserId();
	}
	public int getPopupType()
	{
		return DimdimPopup.PrivateChatBox;
	}
	public int getPopupWidth()
	{
		return	248;
	}
	public int getPopupHeight()
	{
		if (this.minimized)
		{
			return	25;
		}
		return NewChatPopup.privateChatPanelHeight;
	}
	public	void	setPosition()
	{
//		int left = LayoutGlobals.getPrivateChatLeft(index);//(index*257);
//		int top = getTopPosition();
//		this.setPopupPosition(left, top);
		DimdimPopupsLayoutManager.getManager(me).repositionPopups();
	}
//	private	int	getTopPosition()
//	{
//		if (this.minimized)
//		{
//			return	ConferenceGlobals.getContentHeight() - (31);
//		}
//		else
//		{
//			return	ConferenceGlobals.getContentHeight() - (NewChatPopup.privateChatPanelHeight+15);
//		}
//	}
	public void onFocus(Widget w)
	{
		this.textBoxHasFocus = true;
	}
	public void onLostFocus(Widget sender)
	{
		this.textBoxHasFocus = false;
	}
	/**
	 * This method will be used by the controller to display all chat panels.
	 * Resetting of the index is particularly critical when redisplaying a
	 * chat panel. There may be another chat panel in its previous position.
	 */
	public	int	getIndex()
	{
		return	this.index;
	}
	public	void	setIndex(int index)
	{
		this.index = index;
	}
	public	void	onShow()
	{
		if (this.minimized)
		{
			this.maximize();
		}
		DimdimPopupsLayoutManager.getManager(me).dimdimPopupOpened(this);
	}
	public	void	userLeft()
	{
		NewChatController.getController().closeChat(this);
	}
	public void hasNewUnreadMessage()
	{
		if (this.minimized)
		{			
			counter++;
			hasNewUnreadHiddenMessage();			
		}	
		else
			this.header.addStyleName("chat-header-message-unread");

		hasUnreadMessage = true;
	
	}
	public void seenUnreadMessage()
	{
		counter = 1;	
		
		headerButtons.remove(image);	
		headerButtons.remove(imageChange);	
		
		this.header.removeStyleName("chat-header-message-unread");
		hasUnreadMessage = false;
	}
	
	public void hasNewUnreadHiddenMessage()
	{ 
		image.setStyleName("blink-chat-popup");		 
		imageChange.setStyleName("blink-chat-popup");
		image.addClickListener(this);
		imageChange.addClickListener(this);
		
		if(counter > 1)
		{
			headerButtons.remove(image);
			headerButtons.remove(imageChange);
		}
				
		checkImageFirstTime = true;
		
		if (this.toggle == null)
		{
			this.toggle = new Timer() 
			{
				boolean unread = true;
				
				public void run() 
				{					
					if(unread)
					{
						if(checkImageFirstTime != true)
						{
							headerButtons.remove(imageChange);
							headerButtons.insert(image, 0);
						}
						else
						{
							headerButtons.insert(image, 0);
						}
						unread = false;
					}
					else
					{						
						checkImageFirstTime = false;
						headerButtons.remove(image);
						headerButtons.insert(imageChange, 0);
						unread = true;
					}										
				}
			};
		}
	    this.toggle.scheduleRepeating(1000);
	  hasUnreadMessage = true;
	}
	
	private	void	minimize()
	{
		this.minimized = true;
		this.setHeight();
		this.setImage();
		this.chatPanel.setVisible(false);
		this.setPosition();
		//this.minimizeImage = UIImages.getImageBundle(UIImages.defaultSkin).getMaximize();
	}
	private	void	maximize()
	{
		this.minimized = false;
		this.setHeight();
		this.setImage();
		this.chatPanel.setVisible(true);
		this.chatPanel.onShow();
		this.setPosition();
		//this.minimizeImage = UIImages.getImageBundle(UIImages.defaultSkin).getMinimize();
		
		headerButtons.remove(image);
		headerButtons.remove(imageChange);	
				
		if (this.toggle != null)
		{
			this.toggle.cancel();
			this.toggle = null;
		}
	}
		public void onClick(Widget sender)
		{
			if (sender == closeImage)
			{
				if (!minimized)
				{
					this.minimize();
				}
				NewChatController.getController().closeChat(this);
			}
			else if (sender == minimizeImage)
			{
				if (minimized)
				{
					this.maximize();
				}
				else
				{
					this.minimize();
				}
			}
			else if (sender == maximizeImage){
				if(minimized)
				{
					this.maximize();					
				}
				else
				{
					this.maximize();
				}
			}
			else if (sender == popoutLink)
			{
			}
			else if (sender == nameLabel)
			{	
				if(this.minimized)
				this.maximize();	
			}	
			else if (sender == image)
			{	
				if(this.minimized)
				this.maximize();	
			}
			else if (sender == imageChange)
			{	
				if(this.minimized)
				this.maximize();	
			}
		}
        public void onWindowResized (int widthx, int heightx)
        {
    		setPosition();
        }
        public void onMouseDown(Widget sender, int x, int y) {
     	   
    	  	dragging = true;
    	    DOM.setCapture(nameLabel.getElement());
    	    dragStartX = x;
    	    dragStartY = y;
    	  }
        public void onMouseUp(Widget sender, int x, int y) {
            dragging = false;
            DOM.releaseCapture(nameLabel.getElement());
          }
        
		public void refreshName() {
			String name = this.rosterModel.findRosterEntry(me.getUserId()).getDisplayName();
			if (other != null)
			{
				name = this.rosterModel.findRosterEntry(other.getUserId()).getDisplayName();
			}
			
			name = ConferenceGlobals.getDisplayString("console.private.chat.with","Private chat with")+" " + name;
			nameLabel.setText(name);
			
		}
}
