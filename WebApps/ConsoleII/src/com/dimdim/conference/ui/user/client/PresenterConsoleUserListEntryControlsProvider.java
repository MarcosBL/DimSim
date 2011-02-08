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

import com.dimdim.conference.ui.common.client.layout.DefaultCommonDialog;
import com.dimdim.conference.ui.common.client.list.DefaultListEntryControlsProvider;
import com.dimdim.conference.ui.common.client.list.ListEntryClickListener;
import com.dimdim.conference.ui.common.client.list.ListEntryControlsProvider;
import com.dimdim.conference.ui.common.client.user.NewChatController;
import com.dimdim.conference.ui.json.client.UIRosterEntry;
import com.dimdim.conference.ui.model.client.ConferenceGlobals;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * On the presenter console applicable controls are mood change for self,
 * chat kickoff with others, audio permission toggle and permission change
 * popup display.
 */

public class PresenterConsoleUserListEntryControlsProvider  extends DefaultListEntryControlsProvider
{
	protected	UIRosterEntry		me;
	protected	UIRosterEntry		user;
	protected	boolean				isMe;
	protected	UserRosterManager	userRosterManager;
	
	public	PresenterConsoleUserListEntryControlsProvider(UIRosterEntry user,
			UIRosterEntry me, boolean isMe, UserRosterManager userRosterManager)
	{
		this.me = me;
		this.user = user;
		this.isMe = isMe;
		this.userRosterManager = userRosterManager;
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
		if (!this.isMe && ConferenceGlobals.privateChatEnabled)
		{
			//Window.alert("returning click listener");
			return	new ClickListener()
			{
				public	void	onClick(Widget sender)
				{
					//Window.alert("clicked on chat icon me chat = "+me.isChatOn()+" user caht = "+user.isChatOn() );
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
	 * Image 3 for user is mike control. Enable and disable the audio permission.
	 * Enable only if audio slots are available.
	 */
	public ClickListener getImage3ClickListener()
	{
		if (!this.isMe)
		{
/*			Window.alert("1");
			Window.alert(ConferenceGlobals.isPresenterAVAudioDisabled()+"");
			Window.alert(ConferenceGlobals.isMeetingVideoChat()+"");
*/
			if(!ConferenceGlobals.isPresenterAVAudioDisabled() || ConferenceGlobals.isMeetingVideoChat())
			{
				return	new ClickListener()
				{
					public	void	onClick(Widget sender)
					{
						if (user.isAudioOn())
						{
							userRosterManager.disableAudioFor(user.getUserId());
						}
						else
						{
							if (userRosterManager.canEnableAudioFor(user.getUserId()))
							{
								userRosterManager.enableAudioFor(user.getUserId());
							}
							else
							{
								int max = userRosterManager.getMaximumAttendeeAudios();
								DefaultCommonDialog dlg = DefaultCommonDialog.createDialog(
										"Audio Permissions",
										"This meeting allows maximum "+max+" attendees to broadcast audio.");
								dlg.drawDialog();
							}
						}
					}
				};
			}
			else
				return (ClickListener)null;
		}
		else
		{
/*			Window.alert("2");
			Window.alert(ConferenceGlobals.isPresenterAVAudioDisabled()+"");
			Window.alert(ConferenceGlobals.isMeetingVideoChat()+"");
*/

			if(!ConferenceGlobals.isPresenterAVAudioDisabled() || ConferenceGlobals.isMeetingVideoChat())
			{
				return	new	ClickListener()
				{
					public	void	onClick(Widget sender)
					{
						userRosterManager.getPresenterAVManager().reloadPresenterAV();
					}
				};
			}
			else
				return (ClickListener)null;
		}
//		return	null;
	}
	/**
	 * Image 4 for user is permisions control. Throw a dialog box with chat,
	 * audio permissions and remove control.
	 */
	/*public ClickListener getImage4ClickListener()
	{
		if (!this.isMe)
		{
			return	new ClickListener()
			{
				public	void	onClick(Widget sender)
				{
//					DialogsTracker.showDialog(new SingleAttendeePermissionsControlDialog(
//							userRosterManager, listEntryPanel.getListEntry()));
					SingleAttendeePermissionsControlDialog dlg = 
						new SingleAttendeePermissionsControlDialog(userRosterManager,
								listEntryPanel.getListEntry());
					dlg.setDialogPosition(listEntryPanel.getAbsoluteLeft()+50,
							listEntryPanel.getAbsoluteTop()+30);
					dlg.drawDialog();
//					dlg.show();
				}
			};
		}
		return	null;
	}*/
	
	public ClickListener getImage4ClickListener()
	{
		if (!this.isMe)
		{
/*			Window.alert("1");
			Window.alert(ConferenceGlobals.isPresenterAVAudioDisabled()+"");
			Window.alert(ConferenceGlobals.isMeetingVideoChat()+"");
*/
			if(!ConferenceGlobals.isPresenterAVAudioDisabled() || ConferenceGlobals.isMeetingVideoChat())
			{
				return	new ClickListener()
				{
					public	void	onClick(Widget sender)
					{
						if (user.isVideoOn())
						{
							userRosterManager.disableVideoFor(user.getUserId());
						}
						else
						{
							if (userRosterManager.canEnableVideoFor(user.getUserId()))
							{
								userRosterManager.enableVideoFor(user.getUserId());
							}
							else
							{
								int max = userRosterManager.getMaximumAttendeeVideos();
								DefaultCommonDialog dlg = DefaultCommonDialog.createDialog(
										"Video Permissions",
										"This meeting allows maximum "+max+" attendees to broadcast Video .");
								dlg.drawDialog();
							}
						}
					}
				};
			}
			else
				return (ClickListener)null;
		}
		else
		{
/*			Window.alert("2");
			Window.alert(ConferenceGlobals.isPresenterAVAudioDisabled()+"");
			Window.alert(ConferenceGlobals.isMeetingVideoChat()+"");
*/

			if(!ConferenceGlobals.isPresenterAVAudioDisabled() || ConferenceGlobals.isMeetingVideoChat())
			{
				return	new	ClickListener()
				{
					public	void	onClick(Widget sender)
					{
						userRosterManager.getPresenterAVManager().reloadPresenterAV();
					}
				};
			}
			else
				return (ClickListener)null;
		}
	}
	
	public ClickListener getNameLabelMouseListener()
	{
		/*ListEntryPanelMouseListener lepml = new ListEntryPanelMouseListener(listEntryPanel.getListEntry()){
			protected	void	createPopupPanel()
			{
				theHoverPopup = new UserListEntryHoverPopup(me,
						userRosterManager,listEntryPanel.getListEntry(),getControlsProvider());
			}
		};*/
		
		ListEntryClickListener lepml = new ListEntryClickListener(listEntryPanel.getListEntry()){
			protected	void	createPopupPanel()
			{
				theHoverPopup = new UserListEntryHoverPopup(me,
						userRosterManager,listEntryPanel.getListEntry(),getControlsProvider());
				//theHoverPopup = new UserListMenu();
			}
		};
		//Window.alert("returning  click listner from PresenterConsoleUserListEntryControlsProvider");
		return lepml;
	}
	protected	ListEntryControlsProvider	getControlsProvider()
	{
		return	this;
	}
}
