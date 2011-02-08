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

import com.dimdim.conference.ui.common.client.UIGlobals;
import com.dimdim.conference.ui.common.client.UIImages;
import com.dimdim.conference.ui.common.client.UserGlobals;
import com.dimdim.conference.ui.common.client.layout.DefaultCommonDialog;
import com.dimdim.conference.ui.common.client.list.DefaultList;
import com.dimdim.conference.ui.common.client.list.ListControlsProvider;
import com.dimdim.conference.ui.common.client.list.ListEntry;
import com.dimdim.conference.ui.common.client.list.ListEntryMovieModel;
import com.dimdim.conference.ui.common.client.list.ListPropertiesProvider;
import com.dimdim.conference.ui.common.client.resource.ResourceCallbacks;
import com.dimdim.conference.ui.common.client.user.UserCallbacks;
import com.dimdim.conference.ui.common.client.user.UserListEntry;
import com.dimdim.conference.ui.common.client.util.ConfirmationDialog;
import com.dimdim.conference.ui.common.client.util.DebugPanel;
import com.dimdim.conference.ui.json.client.UIEmailAttemptResult;
import com.dimdim.conference.ui.json.client.UIRosterEntry;
import com.dimdim.conference.ui.json.client.UIStreamControlEvent;
import com.dimdim.conference.ui.model.client.AVModelListener;
import com.dimdim.conference.ui.model.client.AudioModelListener;
import com.dimdim.conference.ui.model.client.ClientModel;
import com.dimdim.conference.ui.model.client.ConferenceGlobals;
import com.dimdim.conference.ui.model.client.RosterModel;
import com.dimdim.conference.ui.model.client.RosterModelListener;
import com.dimdim.conference.ui.model.client.SettingsModel;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */

public class UserList extends DefaultList implements RosterModelListener, AudioModelListener, AVModelListener
{
	private String rtmpUrl = null;
	private String rtmptUrl = null;
	private String siteName = null;
	protected	UIRosterEntry	me;
	ResourceCallbacks resCallBacks = null;
	private UserCallbacks userCallbacks;
	
	public UserList(UIRosterEntry me, ListControlsProvider listControlsProvider,
			ListPropertiesProvider listPropertiesProvider, UserCallbacks userCallBacks)
	{
		super(-1,UserGlobals.getUserGlobals().getMaxVisibleParticipants(),10,
				listControlsProvider,listPropertiesProvider);
		
		this.me = me;
		this.rtmpUrl = UIGlobals.getStreamingUrlsTable().getAudioRtmpUrl();
		this.rtmptUrl = UIGlobals.getStreamingUrlsTable().getAudioRtmptUrl();
		this.siteName = ConferenceGlobals.serverAddress;
		this.userCallbacks = userCallBacks;
		
		ClientModel.getClientModel().getRosterModel().addListener(this);
		ClientModel.getClientModel().getAVModel().addListener(this);
		ClientModel.getClientModel().getAudioModel().addListener(this);
	}
	public void consoleClosing()
	{
		int size = this.getListSize();
		for (int i=0; i<size; i++)
		{
			ListEntry listEntry = this.getListEntryAt(i);
			listEntry.setMovie1Model(null);
		}
	}
	/**
	 * We may have to add and remove the mike icon.
	 */
	/*public void onPermissionsChanged(UIRosterEntry user)
	{
		//Window.alert("in on Permissions changed for:"+user.getUserId());
		ListEntry userEntry = this.findEntry(user.getUserId());
		if (userEntry != null)
		{
			this.setSpeakerPanelPosition(userEntry,user);
			if(ConferenceGlobals.privateChatEnabled)
			{
				this.setChatImage(userEntry,user);
			}
			if(!ConferenceGlobals.isPresenterAVAudioDisabled())
			{
				this.setMikeImage(userEntry,user);
			}
		}
	}*/
	
	public	void	onChatPermissionsChanged(UIRosterEntry user)
	{
		//Window.alert("in onChatPermissionsChanged for:"+user.getUserId());
		ListEntry userEntry = this.findEntry(user.getUserId());
		if (userEntry != null)
		{
			if(ConferenceGlobals.privateChatEnabled)
			{
				this.setChatImage(userEntry,user);
			}
		}
	}
	public	void	onAudioVidoPermissionsChanged(UIRosterEntry user)
	{
		//Window.alert("in onAudioPermissionsChanged for:"+user.getUserId());
		ListEntry userEntry = this.findEntry(user.getUserId());
		if (userEntry != null)
		{
			this.setSpeakerPanelPosition(userEntry,user);
			if(!ConferenceGlobals.isPresenterAVAudioDisabled())
			{
				this.setMikeImage(userEntry,user);
			}
		}
	}
	/**
	 * On presenter console indicate the audio permission through the disabled
	 * mike and on attendee console, remove the mike when audio is disabled.
	 * Image 3.
	 * @param user
	 */
	protected	void	setMikeImage(ListEntry userEntry, UIRosterEntry user)
	{
		//DebugPanel.getDebugPanel().addDebugMessage("#### inside setMikeImage = "+user);
		if (userEntry != null)
		{
			if (me.isHost())
			{
				if (user.isAudioOn())
				{
					//DebugPanel.getDebugPanel().addDebugMessage("#### host audio enabled...me is host and audio on");
					userEntry.setImage3Url(UIImages.getImageBundle(UIImages.defaultSkin).getUserAudioEnabledImageUrl()
							,ConferenceGlobals.getTooltip("presenter.disable_audio"));
				}
				/*else
				{
					//DebugPanel.getDebugPanel().addDebugMessage("#### host audio disabled...me is host and audio off");
					userEntry.setImage3Url(UIImages.getImageBundle(UIImages.defaultSkin).getUserAudioDisabledImageUrl()
							,ConferenceGlobals.getTooltip("presenter.enable_audio"));
				}*/
				else if (user.isVideoOn())
				{
					//DebugPanel.getDebugPanel().addDebugMessage("#### host video enabled...me is host and video on");
					userEntry.setImage3Url(UIImages.getImageBundle(UIImages.defaultSkin).getUserCamEnabledImageUrl()
							,ConferenceGlobals.getTooltip("presenter.disable_video"));
				}else{
					userEntry.setImage3Url(null, null);
				}
				
			}
			else
			{
				/*if (user.isVideoOn())
				{
					if (user.getUserId().equals(me.getUserId()))
					{
						//DebugPanel.getDebugPanel().addDebugMessage("#### user cam enabled, this is me but video is on");
						userEntry.setImage3Url(UIImages.getImageBundle(UIImages.defaultSkin).getUserCamEnabledImageUrl()
								,ConferenceGlobals.getTooltip("presenter.disable_video"));
					}
					else
					{
						//DebugPanel.getDebugPanel().addDebugMessage("#### user tv enabled, this is mot me but video is on");
						userEntry.setImage3Url(UIImages.getImageBundle(UIImages.defaultSkin).getTvOKImageUrl()
								,null);
					}
				}
				else if (user.isAudioOn())
				{
					if (user.getUserId().equals(me.getUserId()))
					{
						//DebugPanel.getDebugPanel().addDebugMessage("#### user audio enabled...me is not host and audio on");
						userEntry.setImage3Url(UIImages.getImageBundle(UIImages.defaultSkin).getUserAudioEnabledImageUrl()
								,ConferenceGlobals.getTooltip("presenter.disable_audio"));
					}
					else
					{
						//DebugPanel.getDebugPanel().addDebugMessage("#### user speaker, this is not me but audio is on");
						userEntry.setImage3Url(UIImages.getImageBundle(UIImages.defaultSkin).getSpeakerOKImageUrl()
								,null);
					}
				} 
				else*/
				{
					//DebugPanel.getDebugPanel().addDebugMessage("#### audio is not on setting it to null");
					userEntry.setImage3Url(null, null);
				}
			}
		}
	}
	/**
	 * On all consoles replace the chat image per enabled / disabled.
	 * Chat is image 2.
	 * @param user
	 */
	protected	void	setChatImage(ListEntry userEntry, UIRosterEntry user)
	{
		//if (!UIGlobals.isActivePresenter(user))
		//{
			if (userEntry != null)
			{
				if (user.isChatOn())
				{
					userEntry.setImage2Url(UIImages.getImageBundle(UIImages.defaultSkin).getUserChatEnabledImageUrl());
				}
				else
				{
					userEntry.setImage2Url(UIImages.getImageBundle(UIImages.defaultSkin).getUserChatDisabledImageUrl());
				}
			}
		//}
	}
	/**
	 * On all consoles, move the user's panel to the top of the list.
	 * @param user
	 */
	protected	void	setSpeakerPanelPosition(ListEntry userEntry, UIRosterEntry user)
	{
		if (!user.getUserId().equals(this.me.getUserId())
					&& !UIGlobals.isActivePresenter(user))
		{
			if (userEntry != null)
			{
				if (user.isAudioOn() || user.isVideoOn())
				{
					userEntry.setDisplayRank(3);
				}
				else
				{
					userEntry.setDisplayRank(4);
				}
			}
		}
	}
	public void onUserChanged(UIRosterEntry user)
	{
		
		ListEntry userEntry = this.findEntry(user.getUserId());
//		Window.alert("inside UserList onUserChanged "+userEntry.getName());
		if (userEntry != null)
		{
			String mood = user.getMood();
			Image moodImageUrl = UserGlobals.getUserGlobals().getMoodImageUrl(mood);
			userEntry.setImage1Url(moodImageUrl);
			userEntry.setName(user.getDisplayName());
		}
		else
		{
			//Window.alert("User not found:"+user.getUserId());
		}
		//Window.alert("calling user call back for change of name");
		userCallbacks.onDisplayNameChanged();
	}
	/**
	 * If the current console is attendee and the joining user is the presenter
	 * and is in audio only mode, then the audio player needs to be added to
	 * the user panel. This is done through setting the movie1 model.
	 */
	public void onUserJoined(UIRosterEntry newUser)
	{
		if (UIGlobals.isInLobby(newUser))
		{
			return;
		}
		if (this.findEntry(newUser.getUserId()) == null)
		{
			ListEntry listEntry = new UserListEntry(newUser,
					this.listControlsProvider.getListEntryControlsProvider(newUser),
					this.listPropertiesProvider.getListEntryPropertiesProvider(newUser));
			
			if (newUser.getUserId().equals(this.me.getUserId()))
			{
				ActivePresenterAVManager.getPresenterAVManager(me).setMyListEntry(listEntry);
				ActiveAudiosManager.getManager(me).setMyListEntry(listEntry);
				if (UIGlobals.isActivePresenter(newUser))
				{
					//persisting default setting into the file so that settings in file and UI are in sync
					SettingsModel currentSettings = ClientModel.getClientModel().getSettingsModel();
					RosterModel rosterModel = ClientModel.getClientModel().getRosterModel();
					
//					now publisher related setttings will not be updated from console
					//PublisherInterfaceManager pubInterface = PublisherInterfaceManager.getManager();
					//pubInterface.setNetworkProfileValue(rosterModel.getCurrentUser().getNetProfile());
					
					//if(currentSettings.isMouseTrackEnabled()){
					//	pubInterface.setMouseVisible("1");
					//}else{
					//	pubInterface.setMouseVisible("0");
					//}
					//pubInterface.setImageProfileValue(rosterModel.getCurrentUser().getImgQuality());
					
				}
			}
			
			if (newUser.isActivePresenter())
			{
				//first make the host 1
				//Window.alert("isActivePresenter Changing the display rank of user entry "+newUser.getDisplayName() +" to 1");
				listEntry.setDisplayRank(1);
			}
			else if (newUser.isHost())
			{
				//Window.alert(" isHost Changing the display rank of user entry "+newUser.getDisplayName() +" to 1");
				listEntry.setDisplayRank(1);
			}
			else if (newUser.getUserId().equals(this.me.getUserId()))
			{
				//Window.alert(" me Changing the display rank of user entry "+newUser.getDisplayName() +" to 2");
				listEntry.setDisplayRank(2);
			}
			else if (newUser.isAudioOn())
			{
				listEntry.setDisplayRank(3);
			}else if (newUser.isVideoOn())
			{
				listEntry.setDisplayRank(3);
			}
			
			this.addEntry(listEntry);
		}
	}
	/**
	 * 
	 */
	public void onUserRejoined(UIRosterEntry newUser)
	{
		if (this.findEntry(newUser.getUserId()) == null)
		{
			ListEntry listEntry = new UserListEntry(newUser,
					this.listControlsProvider.getListEntryControlsProvider(newUser),
					this.listPropertiesProvider.getListEntryPropertiesProvider(newUser));
			
			if (newUser.getUserId().equals(this.me.getUserId()))
			{
				ActivePresenterAVManager.getPresenterAVManager(me).setMyListEntry(listEntry);
				ActiveAudiosManager.getManager(me).setMyListEntry(listEntry);
				if (UIGlobals.isActivePresenter(newUser))
				{
					//persisting default setting into the file so that settings in file and UI are in sync
					SettingsModel currentSettings = ClientModel.getClientModel().getSettingsModel();
					RosterModel rosterModel = ClientModel.getClientModel().getRosterModel();
					
//					now publisher related setttings will not be updated from console
					//PublisherInterfaceManager pubInterface = PublisherInterfaceManager.getManager();
					//pubInterface.setNetworkProfileValue(rosterModel.getCurrentUser().getNetProfile());
					
					//if(currentSettings.isMouseTrackEnabled()){
					//	pubInterface.setMouseVisible("1");
					//}else{
					//	pubInterface.setMouseVisible("0");
					//}
					//pubInterface.setImageProfileValue(rosterModel.getCurrentUser().getImgQuality());
					
				}
			}
			if (newUser.isActivePresenter())
			{
				//first make the host 1
				listEntry.setDisplayRank(1);
			}
			else if (newUser.isHost())
			{
				//Window.alert("Changing the display rank of user entry "+user.getDisplayName() +" to 1");
				listEntry.setDisplayRank(1);
			}
			else if (newUser.getUserId().equals(this.me.getUserId()))
			{
				listEntry.setDisplayRank(2);
			}
			else if (newUser.isAudioOn() || newUser.isVideoOn())
			{
				listEntry.setDisplayRank(3);
			}
			
			this.addEntry(listEntry);
		}
	}
	protected ListEntryMovieModel getAudioPlayerMovieModel(String streamId,
			String streamName,String profile, String color)
	{
		StringBuffer buf = new StringBuffer("swf/audioPlayer.swf");
		buf.append("?");
		buf.append(this.rtmpUrl);
		buf.append("$");
		buf.append(streamName);
		buf.append("$");
		buf.append(profile);
		buf.append("$");
		buf.append("dimdim");
		buf.append("$");
		buf.append(this.siteName);
		buf.append("$");
		buf.append(this.rtmptUrl);
		String movieUrl = buf.toString();
		
		String movieId = "m"+ConferenceGlobals.getClientGUID();
		
		return new ListEntryMovieModel(movieId+"_id",movieId,UIGlobals.getAudioPlayerWidth(),
				UIGlobals.getAudioPlayerHeight(),movieUrl,"#fff");
	}
	public void onUserLeft(UIRosterEntry user)
	{
		this.removeEntry(user.getUserId());
	}
	public void onUserRemoved(UIRosterEntry user)
	{
		this.removeEntry(user.getUserId());
	}
	public void onUserRoleChanged(UIRosterEntry user)
	{
		//DebugPanel.getDebugPanel().addDebugMessage("inside role changed data ="+user+" ---partListEnabled="+ConferenceGlobals.partListEnabled);
		//DebugPanel.getDebugPanel().addDebugMessage("inside role changed data ="+user);
		if (ConferenceGlobals.getClientLayout() != null)
		{
//			Window.alert("1");
			ConferenceGlobals.getClientLayout().closePopout();
		}
		handlePartListEnabled(user);
		
		ListEntry listEntry = this.findEntry(user.getUserId());
		
		if (user.isActivePresenter())
		{
			//DebugPanel.getDebugPanel().addDebugMessage("user is active presenter..");
			//first make the host 1
			if(user.getUserId().equals(me.getUserId()))
			{
				if(!me.isHost())
				{
					//DebugPanel.getDebugPanel().addDebugMessage("me is not host...");
					String msg = ConferenceGlobals.getDisplayString("make.presenter.desc","The host of this meeting has now made you the presenter.");
					Window.alert(msg);
//					Label lbl = new Label(msg);
//					lbl.setStyleName("common-text");
//					String header = ConferenceGlobals.getDisplayString("remove.presenter.heading","Info");
//					DefaultCommonDialog confirmPubInstall = new DefaultCommonDialog(header, lbl, header);
//					confirmPubInstall.drawDialog();
				}
			}
			listEntry.setDisplayRank(1);
		}
		else if(user.isPresenter())
		{
			DebugPanel.getDebugPanel().addDebugMessage("user.isPresenter()");
			if(user.getUserId().equals(me.getUserId()))
			{
				//DebugPanel.getDebugPanel().addDebugMessage("5");
				if(!me.isHost())
				{
					//DebugPanel.getDebugPanel().addDebugMessage("6");
					String msg = ConferenceGlobals.getDisplayString("remove.presenter.desc","The Host has taken the control back");
					Window.alert(msg);
//					Label lbl = new Label(msg);
//					lbl.setStyleName("common-text");
//					String header = ConferenceGlobals.getDisplayString("remove.presenter.heading","Info");
//					DefaultCommonDialog confirmPubInstall = new DefaultCommonDialog(header, lbl, header);
//					confirmPubInstall.drawDialog();
				}
				if(resCallBacks != null)
				{
					//DebugPanel.getDebugPanel().addDebugMessage("Stopping the share....");
	//				stop current share if any is active
					resCallBacks.stopSharing();
				}
				//it is me so display rank is 2
				listEntry.setDisplayRank(2);
			}
			else
			{
				//means this is not me so changing the display rank
				//Window.alert("Changing the display rank of user entry "+user.getDisplayName() +" to 4");
				//DebugPanel.getDebugPanel().addDebugMessage("Changing the display rank of user entry "+user.getDisplayName() +" to 4");
				listEntry.setDisplayRank(3);
				
			}
		}
		if (user.isHost())
		{
			//Window.alert("Changing the display rank of user entry "+user.getDisplayName() +" to 1");
//			Window.alert("3");
			listEntry.setDisplayRank(1);
		}
		
	}
	
	private void handlePartListEnabled(UIRosterEntry user) {
		if(!ConferenceGlobals.partListEnabled )
		{
//			Window.alert("me is = "+me+" user = "+user);
			//DebugPanel.getDebugPanel().addDebugMessage(" handlePartListEnabled ---> me is = "+me+" user = "+user);
			if(me.isHost())
			{
				return;
			}
			
			//boolean userAlreadyPresent = true;
			ListEntry listEntry = this.findEntry(user.getUserId());
			if(listEntry == null)
			{
				//userAlreadyPresent = false;
				//user.setChat("0");
				listEntry = new UserListEntry(user,
					this.listControlsProvider.getListEntryControlsProvider(user),
					this.listPropertiesProvider.getListEntryPropertiesProvider(user));
			}
			//DebugPanel.getDebugPanel().addDebugMessage("userAlreadyPresent = "+userAlreadyPresent);
//			Window.alert("userAlreadyPresent = "+userAlreadyPresent);
			if (user.isActivePresenter())
			{
				//DebugPanel.getDebugPanel().addDebugMessage("active presenter...user = "+user );
				//DebugPanel.getDebugPanel().addDebugMessage("!user.isHost() = "+!user.isHost()+" !user.equals(me) ="+!user.equals(me));
				
				ListEntry entry = null;
				int	size = this.entries.size();
				int i = 0;
				//for (int i=0; i<size; i++)
				
				while(i < this.entries.size())
				{
					entry = (ListEntry)this.entries.elementAt(i);
					//DebugPanel.getDebugPanel().addDebugMessage(" entry,,, "+entry.getId());
					if(entry.getId().equals(me.getUserId()))
					{
						//DebugPanel.getDebugPanel().addDebugMessage(" not removing entry,,, coz self "+entry.getId());
						i++;
						continue;
					}
					if(entry instanceof UserListEntry) 
					{
						UserListEntry userListEntry = (UserListEntry) entry;
						if(userListEntry.getUser().isHost())
						{
							//DebugPanel.getDebugPanel().addDebugMessage(" not removing entry,,,coz host "+entry.getId());
							i++;
							continue;
						}
					}
					//DebugPanel.getDebugPanel().addDebugMessage("removing entry,,, "+entry.getName());
					this.removeEntry(entry);
				}
				
				//if(!user.isHost() && !user.getUserId().equals(me.getUserId()) && !userAlreadyPresent)
				if(!user.isHost() && !user.getUserId().equals(me.getUserId()) )
				{
					//DebugPanel.getDebugPanel().addDebugMessage("adding list entry ="+listEntry);
					this.addEntry(listEntry);
					RosterModel rosterModel = ClientModel.getClientModel().getRosterModel();
					rosterModel.addToRoster(user);
				}
				
			}/*else if(user.isPresenter()){
				//Window.alert("non active presenter...user = "+user );
				//DebugPanel.getDebugPanel().addDebugMessage("non active presenter...user = "+user );
				//DebugPanel.getDebugPanel().addDebugMessage("!user.isHost() = "+!user.isHost()+" !user.equals(me) ="+!user.equals(me));
				if(!user.isHost() && !user.getUserId().equals(me.getUserId()) && userAlreadyPresent)
				{
					//Window.alert("removing list entry ="+listEntry);
					this.removeEntry(listEntry);
				}
			}*/
		}
		//DebugPanel.getDebugPanel().addDebugMessage("end oif handlePartListEnabled###########");
	}
	public void onStartAudio(UIStreamControlEvent event)
	{
		//Window.alert("inside user list onStartAudio");
		DebugPanel.getDebugPanel().addDebugMessage("inside user list onStartAudio event = "+event);
		String userId = event.getResourceId();
		if (!userId.equals(this.me.getUserId()))
		{
			//Window.alert("Starting Audio Player for:"+userId);
			String streamName = event.getStreamName();
			String profile = event.getProfile();
			ListEntry listEntry = this.findEntry(userId);
			
			if (listEntry != null)
			{
				//DebugPanel.getDebugPanel().addDebugMessage("user list ewntry not null...");
				String color = "#E4E4E4";
				UIRosterEntry user = ((UserListEntry)listEntry).getUser();
				//DebugPanel.getDebugPanel().addDebugMessage("the user is "+user);
				//for the host audio = on is not being set, so over-riding it here
				//if(user.isHost())
				//{
					setAudioVideoState(user, event);
				//}
				if (UIGlobals.isActivePresenter(user))
				{
					ConferenceGlobals.setPresenterAVActive(true);
					color = "#C2D3A2";
				}
				
				//if (!UIGlobals.isActivePresenter(me))
				//{
				if(event.isAudioEvent())
				{
					//DebugPanel.getDebugPanel().addDebugMessage("setting speaker icon");
					listEntry.setImage3Url(UIImages.getImageBundle(UIImages.defaultSkin).getSpeakerOKImageUrl()
							,null);
				}else if(event.isVideoEvent())
				{
					//DebugPanel.getDebugPanel().addDebugMessage("setting tv icon");
					listEntry.setImage3Url(UIImages.getImageBundle(UIImages.defaultSkin).getTvOKImageUrl()
							,null);
				}else{
					//DebugPanel.getDebugPanel().addDebugMessage("setting null");
					listEntry.setImage3Url(null, null);
				}
					//setMikeImage(listEntry, user);
					
				//}
				//if (!ConferenceGlobals.isMeetingVideoChat())
				if( event.isAudioEvent() && !event.isVideoEvent() )
				{
					//DebugPanel.getDebugPanel().addDebugMessage("adding audio player against user "+user);
					listEntry.setMovie1Model(getAudioPlayerMovieModel(streamName,streamName,profile,color));
				}
			}
			else
			{
//				Window.alert("List entry panel not ready for user:"+userId);
			}
		}
		else
		{
//			ActivePresenterAVManager.getPresenterAVManager(me).setBroadcasterActive();
//			ActiveAudiosManager.getManager(me).setBroadcasterActive();
		}
	}
	private void setAudioVideoState(UIRosterEntry user, UIStreamControlEvent event) {
		if(event.isAudioEvent())
		{
			if(UIStreamControlEvent.STOP.equalsIgnoreCase(event.getEventType()))
			{
				user.setAudio("0");
			}else if(UIStreamControlEvent.START.equalsIgnoreCase(event.getEventType()))
			{
				user.setAudio("1");
			}
		}else if(event.isVideoEvent())
		{
			if(UIStreamControlEvent.STOP.equalsIgnoreCase(event.getEventType()))
			{
				user.setVideo("0");
			}else if(UIStreamControlEvent.START.equalsIgnoreCase(event.getEventType()))
			{
				user.setVideo("1");
			}
		}
	}
	public void onStopAudio(UIStreamControlEvent event)
	{
		String speakerId = event.getResourceId();
		if (!speakerId.equals(me.getUserId()))
		{
			ListEntry listEntry = this.findEntry(speakerId);
			if (listEntry != null)
			{
				listEntry.setMovie1Model(null);
				UIRosterEntry user = ((UserListEntry)listEntry).getUser();
				setAudioVideoState(user, event);
				/*if (!me.isHost())
				{
					if (event.isAudioEvent())
					{
						listEntry.setImage3Url(UIImages.getImageBundle(UIImages.defaultSkin).getSpeakerDisabledImageUrl()
								,ConferenceGlobals.getTooltip("attendee.mike_silent"));
					}
					else if(event.isVideoEvent())
					{
						//DebugPanel.getDebugPanel().addDebugMessage("onStopAudio setting tv icon");
						listEntry.setImage3Url(UIImages.getImageBundle(UIImages.defaultSkin).getTvDisabledImageUrl()
								,ConferenceGlobals.getTooltip("attendee.mike_silent"));
					}else{
						//DebugPanel.getDebugPanel().addDebugMessage("setting null");
						listEntry.setImage3Url(null, null);
					}
				}*/
				listEntry.setImage3Url(null, null);
				if (UIGlobals.isActivePresenter(user))
				{
					ConferenceGlobals.setPresenterAVActive(false);
				}
			}
		}
		else
		{
//			Window.alert("Setting broadcaster inactive on stop audio event");
//			ActivePresenterAVManager.getPresenterAVManager(me).setBroadcasterInactiveIfNotConnecting();
//			ActiveAudiosManager.getManager(me).setBroadcasterInactiveIfNotConnecting();
		}
	}
	public void onStartVideo(String conferenceKey, String resourceId,
			String streamType, String streamName, String profile, String sizeFactor)
	{
		DebugPanel.getDebugPanel().addDebugMessage("------inside onStartVideo event = "+streamName);
		if (!ConferenceGlobals.isPresenterAVAudioDisabled())
		{
			String speakerId = resourceId/*ClientModel.getClientModel().
				getRosterModel().getCurrentActivePresenter().getUserId()*/;
			if (!speakerId.equals(me.getUserId()))
			{
				ListEntry listEntry = this.findEntry(speakerId);
				if (listEntry != null)
				{
					String color = "#E4E4E4";
					UIRosterEntry user = ((UserListEntry)listEntry).getUser();
					//DebugPanel.getDebugPanel().addDebugMessage("user = "+user);
					user.setVideo("1");
					if (UIGlobals.isActivePresenter(user))
					{
						ConferenceGlobals.setPresenterAVActive(true);
						color = "#C2D3A2";
					}
					
					listEntry.setImage3Url(UIImages.getImageBundle(UIImages.defaultSkin).getTvOKImageUrl(), null);
					//DebugPanel.getDebugPanel().addDebugMessage(" not adding the audio player for video start..");
					//listEntry.setMovie1Model(getAudioPlayerMovieModel(resourceId,streamName,profile,color));
				}
				else
				{
	//				Window.alert("User not found to add player:"+resourceId);
				}
			}
			else
			{
//				ActivePresenterAVManager.getPresenterAVManager(me).setBroadcasterActive();
			}
		}
		
		ConferenceGlobals.setPresenterAVActive(true);
	}
	public void onStopVideo(String conferenceKey, String resourceId,
			String streamType, String streamName)
	{
		if (ConferenceGlobals.isPresenterAVAudioDisabled())
		{
			String speakerId = resourceId/*ClientModel.getClientModel().
				getRosterModel().getCurrentActivePresenter().getUserId()*/;
			if (!speakerId.equals(me.getUserId()))
			{
				ListEntry listEntry = this.findEntry(speakerId);
				if (listEntry != null)
				{
					UIRosterEntry user = ((UserListEntry)listEntry).getUser();
					user.setVideo("0");
					
					listEntry.setImage3Url(UIImages.getImageBundle(UIImages.defaultSkin).getTvDisabledImageUrl()
							,ConferenceGlobals.getTooltip("attendee.mike_silent"));
					listEntry.setMovie1Model(null);
				}
				else
				{
	//				Window.alert("User not found to add player:"+resourceId);
				}
			}
			else
			{
//				Window.alert("Setting broadcaster inactive on stop video event");
//				ActivePresenterAVManager.getPresenterAVManager(me).setBroadcasterInactiveIfNotConnecting();
			}
		}
		ConferenceGlobals.setPresenterAVActive(false);
	}
	
	/**
	 * No op implementations.
	 */
	public void onEntryDenied(UIRosterEntry newUser)
	{
		if (newUser.getUserId().equals(this.me.getUserId()))
		{
			DefaultCommonDialog.hideMessageBox();
		}
	}
	public void onEntryGranted(UIRosterEntry newUser)
	{
		if (newUser.getUserId().equals(this.me.getUserId()))
		{
			DefaultCommonDialog.hideMessageBox();
		}
	}
	public void onUserArrived(UIRosterEntry newUser)
	{
	}
	public void onEmailError(UIEmailAttemptResult emailResult)
	{
	}
	public void onEmailOK(UIEmailAttemptResult emailResult)
	{
	}
	public ResourceCallbacks getResCallBacks() {
		return resCallBacks;
	}
	public void setResCallBacks(ResourceCallbacks resCallBacks) {
		this.resCallBacks = resCallBacks;
	}
}
