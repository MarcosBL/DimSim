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

import com.dimdim.conference.ui.common.client.UIGlobals;
import com.dimdim.conference.ui.common.client.list.DefaultListEntryControlsProvider;
import com.dimdim.conference.ui.common.client.list.ListEntryClickListener;
import com.dimdim.conference.ui.common.client.list.ListEntryControlsProvider;
import com.dimdim.conference.ui.common.client.list.ListEntryMovieModel;
import com.dimdim.conference.ui.common.client.user.NewChatController;
import com.dimdim.conference.ui.common.client.user.UserListEntry;
import com.dimdim.conference.ui.json.client.UIRosterEntry;
import com.dimdim.conference.ui.model.client.ConferenceGlobals;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * On the attendee console, mood change and chat kick off are the only
 * applicable controls.
 */

public class AttendeeConsoleUserListEntryControlsProvider  extends DefaultListEntryControlsProvider
{
	protected	UIRosterEntry		me;
	protected	UIRosterEntry		user;
	protected	boolean				isMe;
	protected	UserRosterManager	userRosterManager;
	protected	ListEntryMovieModel	previousMovie1;
	
	public	AttendeeConsoleUserListEntryControlsProvider(UIRosterEntry user,
			UIRosterEntry me, boolean isMe, UserRosterManager userRosterManager)
	{
		this.me = me;
		this.user = user;
		this.isMe = isMe;
		this.userRosterManager = userRosterManager;
	}
	public ClickListener getNameLabelMouseListener()
	{
		/*ListEntryPanelMouseListener lepml = new ListEntryPanelMouseListener(listEntryPanel.getListEntry())
		{
			protected	void	createPopupPanel()
			{
				theHoverPopup = new UserListEntryHoverPopup(me,
						userRosterManager,listEntryPanel.getListEntry(),getControlsProvider());
			}
		};
		return lepml;*/
		
		ListEntryClickListener lepml = new ListEntryClickListener(listEntryPanel.getListEntry())
		{
			protected	void	createPopupPanel()
			{
				theHoverPopup = new UserListEntryHoverPopup(me,
						userRosterManager,listEntryPanel.getListEntry(),getControlsProvider());
			}
		};
		//Window.alert("returning  click listner from AttendeeConsoleUserListEntryControlsProvider");
		return lepml;
	}
	protected	ListEntryControlsProvider	getControlsProvider()
	{
		return	this;
	}
	public ListEntryMovieModel getPreviousMovie1()
	{
		return previousMovie1;
	}
	public void setPreviousMovie1(ListEntryMovieModel previousMovie1)
	{
		this.previousMovie1 = previousMovie1;
	}
	/**
	 * Image 1 is mood. This listener is for user to change his mood if the
	 * user is himself.
	 */
	public ClickListener getImage1ClickListener()
	{
		/*if (this.isMe)
		{
			return	new ClickListener()
			{
				public	void	onClick(Widget sender)
				{
					MoodControlPopup mcp = new MoodControlPopup(me,
							listEntryPanel.getListEntry(),userRosterManager);
					int left = listEntryPanel.getAbsoluteLeft() + 16;
					int top = listEntryPanel.getAbsoluteTop() + 20;
//					mcp.setPopupPosition(left,top);
					mcp.setDialogPosition(left,top);
					mcp.show();
				}
			};
		}*/
		return	null;
	}
	/**
	 * If a user clicks on anyone except the self entry in the list the chat
	 * session is to be kicked off with that user if the chat permission is on.
	 */
	public	ClickListener getImage2ClickListener()
	{
		return	this.getStartChatClickListener();
	}
	public	ClickListener getNameLabelClickListener()
	{
		//return	this.getStartChatClickListener();
		return null;
	}
	public	ClickListener getStartChatClickListener()
	{
		//Window.alert("inside attendee listener provider...!this.isMe ="+!this.isMe);
		if (!this.isMe && ConferenceGlobals.privateChatEnabled)
		{
			return	new ClickListener()
			{
				public	void	onClick(Widget sender)
				{
					if (me.isChatOn() && user.isChatOn())
					{
						NewChatController.getController().showChatPopupIfPossible(user);
					}
				}
			};
		}
		return	null;
	}
	/**
	 * On the attendee side the image 3 is mike. If the user has audio permission.
	 * This click listener will save and force the reshowing of the audio player movie.
	 * This is to provide a workaround for possible flash errors in displaying of the
	 * movie.
	 */
	public	ClickListener	getImage3ClickListener()
	{
		if (!this.isMe)
		{
			
			if(!ConferenceGlobals.isPresenterAVAudioDisabled() || ConferenceGlobals.isMeetingVideoChat())
			{
				return new ClickListener()
				{
					public	void	onClick(Widget sender)
					{
						UIRosterEntry user = ((UserListEntry)listEntryPanel.getListEntry()).getUser();
						if (user.isAudioOn()) /*||
								(UIGlobals.isActivePresenter(user) &&
										ConferenceGlobals.isPresenterAVAudioOnly() &&
										ConferenceGlobals.isPresenterAVActive()))*/
						{
							ListEntryMovieModel currentMovie = listEntryPanel.getListEntry().getMovie1Model();
							if (currentMovie != null)
							{
								//	This means we have a movie. Save it in this object to be
								//	reinstated on the next click.
								previousMovie1 = currentMovie;
								listEntryPanel.getListEntry().setMovie1Model(null);
							}
							else
							{
								if (previousMovie1 != null)
								{
									ListEntryMovieModel movie = previousMovie1;
									previousMovie1 = null;
									listEntryPanel.getListEntry().setMovie1Model(movie);
								}
							}
						}
						else
						{
							previousMovie1 = null;
						}
					}
				};
			}
			else
				return (ClickListener)null;
		}
		else
		{

			//	For the self entry the listener will show the audio broadcaster.
			if(!ConferenceGlobals.isPresenterAVAudioDisabled() || ConferenceGlobals.isMeetingVideoChat())
			{
				return new ClickListener()
				{
					public	void	onClick(Widget sender)
					{
						if (me.isAudioOn() || me.isVideoOn())
						{
							ActiveAudiosManager.getManager(me).reloadBroadcaster();
						}
					}
				};
			}
			else
				return (ClickListener)null;
		}
	}
}
