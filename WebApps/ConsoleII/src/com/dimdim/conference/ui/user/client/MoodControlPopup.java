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

package com.dimdim.conference.ui.user.client;

import java.util.Vector;

import com.dimdim.conference.ui.common.client.UIGlobals;
import com.dimdim.conference.ui.common.client.UIImages;
import com.dimdim.conference.ui.common.client.UserGlobals;
import com.dimdim.conference.ui.common.client.list.ListEntry;
import com.dimdim.conference.ui.json.client.UIRosterEntry;
import com.dimdim.conference.ui.model.client.UserMood;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MouseListenerAdapter;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */

public class MoodControlPopup extends PopupPanel
{
	protected	VerticalPanel		basePanel = new VerticalPanel();
	protected	UserRosterManager	userManager;
	protected	UIRosterEntry		currentUser;
	protected	ListEntry		userListEntry;
//	protected	MoodControlWidget	mcw;
//	protected	MenuBar		commonInvisibleMenuBar;
	
	UserListEntryHoverPopup parentMenu = null;
	
	/*public	MoodControlPopup(UIRosterEntry currentUser, ListEntry userListEntry,
			UserRosterManager userManager, UserListEntryHoverPopup parentMenu)
	{
		this(currentUser, userListEntry, userManager);
		this.parentMenu = parentMenu;
	}*/
	
	public	MoodControlPopup(UIRosterEntry currentUser, ListEntry userListEntry,
			UserRosterManager userManager, UserListEntryHoverPopup parentMenu)
	{
		super(true);
		//pane.add(basePanel);
		this.setStyleName("mood-selection-popup");
		this.currentUser = currentUser;
		this.userListEntry = userListEntry;
		this.userManager = userManager;
		this.parentMenu = parentMenu;
//		this.mcw = mcw;
//		commonInvisibleMenuBar = new MenuBar();
//		basePanel.add(moodsMenu);
		
		Vector moods = UserGlobals.getUserGlobals().getMoods();
		int	numMoods = moods.size();
		UserMood mood = null;
		for (int i=0; i<numMoods; i++)
		{
			mood = (UserMood)moods.elementAt(i);
			if (UIGlobals.isActivePresenter(currentUser))
			{
				if (mood.getMood().equals(UserGlobals.Question))
				{
					continue;
				}
			}
			HorizontalPanel mp = getMoodPanel(mood,false);
			this.basePanel.add(mp);
			this.basePanel.setCellWidth(mp,"100%");
		}
		this.add(basePanel);
//		HorizontalPanel mp = getMoodPanel(UserGlobals.getUserGlobals().getCustomMood(),true);
//		this.basePanel.add(mp);
//		this.basePanel.setCellWidth(mp,"100%");
	}
	protected	HorizontalPanel	getMoodPanel(UserMood mood, boolean custom)
	{
		/*
		FocusPanel	pane = new FocusPanel();
		pane.addMouseListener(new MouseListenerAdapter()
		{
			public void onMouseEnter(Widget sender)
			{
				sender.addStyleName("mood-menu-entry-selected");
			}
			public void onMouseLeave(Widget sender)
			{
				sender.removeStyleName("mood-menu-entry-selected");
			}
		});
		*/
		final	HorizontalPanel moodPanel = new HorizontalPanel();
//		pane.add(moodPanel);
		moodPanel.setStyleName("mood-menu-entry");
		if (!custom)
		{
			moodPanel.addStyleName("mood-menu-entry-border");
		}
		Image moodImage = UIImages.getImageBundle(UIImages.defaultSkin).getNewMoodImageUrl(mood.getMood());
		moodImage.setStyleName(mood.getImageStyleName());
		moodPanel.add(moodImage);
		moodPanel.setCellVerticalAlignment(moodImage,VerticalPanel.ALIGN_MIDDLE);
		
		Label moodLabel = getMoodLabel(mood,custom);
		moodLabel.addStyleName("mood-label");
		moodPanel.add(moodLabel);
		moodPanel.setCellVerticalAlignment(moodLabel,VerticalPanel.ALIGN_MIDDLE);
		moodLabel.addMouseListener(new MouseListenerAdapter()
		{
			public void onMouseEnter(Widget sender)
			{
				moodPanel.addStyleName("mood-menu-entry-selected");
			}
			public void onMouseLeave(Widget sender)
			{
				moodPanel.removeStyleName("mood-menu-entry-selected");
			}
		});
		moodPanel.setTitle(mood.getDisplayLabel());
		return	moodPanel;
	}
	
	protected	Label	getMoodLabel(final UserMood mood, final boolean custom)
	{
		Label moodMenu = new Label(mood.getDisplayLabel());
		moodMenu.setStyleName("common-text");
		moodMenu.addStyleName("common-anchor");
		moodMenu.addClickListener(new ClickListener()
		{
			public	void	onClick(Widget w)
			{
				sendMoodChange(mood.getMood(),custom);
//				//hide();
			}
		});
		/*
		Command cmd = new Command()
		{
			public void execute()
			{
				sendMoodChange(mood.getMood(),custom);
				hide();
			}
		};
		
		MenuItem moodMenuItem = new MenuItem(, cmd);
		moodMenuItem.setStyleName("mood-menu-item");
		moodMenu.addItem(moodMenuItem);
		*/
		
		return	moodMenu;
	}
	/**
	 * The commands associated with the mood change menus
	 * @param mood
	 * @param custom
	 */
	protected	void	sendMoodChange(String mood, boolean custom)
	{
		//	Use the user manager to send out the mood change command to server.
		if (custom)
		{
//			this.mcw.acceptCustomMood();
		}
		else
		{
			this.userManager.changeMood(mood, false);
			this.currentUser.setMood(mood);
			Image moodUrl = UserGlobals.getUserGlobals().getMoodImageUrl(mood);
			this.userListEntry.setImage1Url(moodUrl);
//			this.mcw.acceptStandardMood(mood);
		}
		this.hide();
		if( null != parentMenu)
		{
			//Window.alert("setting parentMenu.setSubMeuOpen(false");
			parentMenu.hide();
		}
//		UIGlobals.closePreviousHoverPopup();
	}


	public void hide()
	{
		
		if( null != parentMenu)
		{
			//Window.alert("setting parentMenu.setSubMeuOpen(false");
			parentMenu.setSubMeuOpen(false);
		}
		super.hide();
	}
}
