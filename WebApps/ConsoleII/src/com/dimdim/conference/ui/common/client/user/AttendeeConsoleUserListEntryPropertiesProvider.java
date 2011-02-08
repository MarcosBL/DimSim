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

package com.dimdim.conference.ui.common.client.user;

import com.dimdim.conference.ui.common.client.UIGlobals;
import com.dimdim.conference.ui.common.client.UIImages;
import com.dimdim.conference.ui.common.client.UserGlobals;
import com.dimdim.conference.ui.common.client.list.DefaultListEntryPropertiesProvider;
import com.dimdim.conference.ui.common.client.list.ListEntryMovieModel;
import com.dimdim.conference.ui.common.client.util.DebugPanel;
import com.dimdim.conference.ui.json.client.UIRosterEntry;
import com.dimdim.conference.ui.model.client.ConferenceGlobals;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Image;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */

public class AttendeeConsoleUserListEntryPropertiesProvider extends DefaultListEntryPropertiesProvider
{
	protected	String	photoImageUrl = "images/default_photo_1.jpg";
	protected	UIRosterEntry	user;
	protected	UIRosterEntry	me;
	
	public	AttendeeConsoleUserListEntryPropertiesProvider(UIRosterEntry user,UIRosterEntry me)
	{
		this.user = user;
		this.me = me;
	}
	public int getNameLabelWidth()
	{
		return 9;
	}
	/**
	 * For the user list entry, type is role and type2 is the flag if user
	 * already has the presenter active x control installed.
	 */
	public String getListEntryPanelBackgroundStyle()
	{
		if (user.isHost())
		{
			return	"host-list-entry-panel-background";
		}else if (user.isActivePresenter())
		{
			return	"active-presenter-list-entry-panel-background";
		}
		else if (user.getUserId().equals(me.getUserId()))
		{
			return	"self-list-entry-panel-background";
		}
		else
		{
			return "user-list-entry-panel-background";
		}
	}
//	public String getNameLabelStyle()
//	{
//		return	"attendee-label-background";
//	}
	public Image getImage1Url()
	{
		return UserGlobals.getUserGlobals().getMoodImageUrl(user.getMood());
	}
	public String getImage1Tooltip()
	{
		if (!this.me.getUserId().equals(user.getUserId()))
		{
			return ConferenceGlobals.getDisplayString("attendee_mood."+user.getMood(),"Mood");
		}
		else
		{
			//return ConferenceGlobals.getTooltip("user.change_mood");
			return null;
		}
		
	}
	/**
	 * On attendee console everyone including the self entry will get chat
	 * icon. Self entry will not have a kick off listener and only indicate
	 * the permission.
	 */
	public Image getImage2Url()
	{
		if( ConferenceGlobals.privateChatEnabled)
		{
			//Window.alert("in attendee prop provider user = "+user+" user.isChatOn() = "+user.isChatOn());
			if (this.user.isChatOn() )
			{
				return UIImages.getImageBundle(UIImages.defaultSkin).getUserChatEnabledImageUrl();
			}
			else
			{
				return UIImages.getImageBundle(UIImages.defaultSkin).getUserChatDisabledImageUrl();
			}
		}
		return null;
	}
	public String getImage2Tooltip()
	{
		if (!this.me.getUserId().equals(user.getUserId()))
		{
			return ConferenceGlobals.getTooltip("user.start_chat")+" "+user.getDisplayName();
		}
		return	null;
	}
	/**
	 * On Attendee console the active presenter gets an audio icon if the meeting
	 * is in audio mode. The player only comes on when the presenter is in audio
	 * only mode and is speaking.
	 * 
	 * For other attendees the mike icon is provided only when the attendee's
	 * audio permission is on.
	 */
	public Image getImage3Url()
	{
		//DebugPanel.getDebugPanel().addDebugMessage("inside getImage3Url = "+user);
		//DebugPanel.getDebugPanel().addDebugMessage("max attendee videos = "+ConferenceGlobals.userInfoDictionary.getStringValue("max_attendee_videos"));
		if(ConferenceGlobals.isPresenterAVAudioDisabled())
		{
			return null;
		}
		else
		{
		if (!user.isHost())
		{
			if (ConferenceGlobals.isPresenterAVAudioOnly() 
					|| "0".equalsIgnoreCase(ConferenceGlobals.userInfoDictionary.getStringValue("max_attendee_videos")))
			{
				/*if (this.user.isAudioOn())
				{
					if (user.getUserId().equals(me.getUserId()))
					{
						return UIImages.getImageBundle(UIImages.defaultSkin).getUserAudioEnabledImageUrl();
					}
					else
					{
						return UIImages.getImageBundle(UIImages.defaultSkin).getSpeakerOKImageUrl();
					}
				}else
				{*/
					/*if (user.getUserId().equals(me.getUserId()))
					{
						return UIImages.getImageBundle(UIImages.defaultSkin).getUserAudioDisabledImageUrl();
					}
					else
					{	
						return UIImages.getImageBundle(UIImages.defaultSkin).getSpeakerDisabledImageUrl();
					}*/
					return null;
				//}
			}else if (ConferenceGlobals.isPresenterAVVideoOnly() || ConferenceGlobals.isPresenterAVAudoVideo()){
				/*if (this.user.isAudioOn())
				{
					if (user.getUserId().equals(me.getUserId()))
					{
						return UIImages.getImageBundle(UIImages.defaultSkin).getUserAudioEnabledImageUrl();
					}
					else
					{
						return UIImages.getImageBundle(UIImages.defaultSkin).getSpeakerOKImageUrl();
					}
				}else if (this.user.isVideoOn())
				{
					if (user.getUserId().equals(me.getUserId()))
					{
						return UIImages.getImageBundle(UIImages.defaultSkin).getUserCamEnabledImageUrl();
					}
					else
					{
						return UIImages.getImageBundle(UIImages.defaultSkin).getTvOKImageUrl();
					}
				}else{*/
					/*if (user.getUserId().equals(me.getUserId()))
					{
						return UIImages.getImageBundle(UIImages.defaultSkin).getUserCamDisabledImageUrl();
					}
					else
					{
						return UIImages.getImageBundle(UIImages.defaultSkin).getTvDisabledImageUrl();
					}*/
					return null;
				//}
			}
			else
			{
				//return UIImages.getImageBundle(UIImages.defaultSkin).getSpeakerDisabledImageUrl();
				return null;
			}
		}
		else
		{
			if (this.user.isVideoOn())
			{
				return UIImages.getImageBundle(UIImages.defaultSkin).getUserCamEnabledImageUrl();
			}else if (this.user.isAudioOn())
			{
				return UIImages.getImageBundle(UIImages.defaultSkin).getSpeakerOKImageUrl();
			}
			
			/*if (ConferenceGlobals.isPresenterAVAudioOnly())
			{
				return UIImages.getImageBundle(UIImages.defaultSkin).getSpeakerDisabledImageUrl();
			}else if (ConferenceGlobals.isPresenterAVVideoOnly() || ConferenceGlobals.isPresenterAVAudoVideo()){
				return UIImages.getImageBundle(UIImages.defaultSkin).getTvDisabledImageUrl();
			}*/
		}
		return	null;
		}
	}
	public String getImage3Tooltip()
	{
		if(ConferenceGlobals.isPresenterAVAudioDisabled())
		{
			return null;
		}
		else
		{
//			if (!this.me.getUserId().equals(user.getUserId()))
			//{
			if (ConferenceGlobals.isPresenterAVAudioOnly()
					|| "0".equalsIgnoreCase(ConferenceGlobals.userInfoDictionary.getStringValue("max_attendee_videos")))
			{
				if (this.user.isAudioOn())
				{
					return ConferenceGlobals.getTooltip("presenter.disable_audio");
				}
				else
				{
					return null;
					//return ConferenceGlobals.getTooltip("presenter.enable_audio");
				
				}
			}else if (ConferenceGlobals.isPresenterAVVideoOnly() || ConferenceGlobals.isPresenterAVAudoVideo())
			{
				if (this.user.isVideoOn())
				{
					return ConferenceGlobals.getTooltip("presenter.disable_video");
				}
				else
				{
					return null;
					//return ConferenceGlobals.getTooltip("presenter.enable_video");
				}
			}
			return null;
			//}
			//else
			//{
			//	return ConferenceGlobals.getTooltip("presenter.restart_broadcaster");
			//}
		}
	}
	public ListEntryMovieModel getMovie1Model()
	{
		return null;
	}

	
	public Image getImage4Url()
	{
		return UIImages.getImageBundle(UIImages.defaultSkin).getHideChat();
	}
	public String getImage4Tooltip()
	{				
		return	ConferenceGlobals.getTooltip("click_menu");
	}
}
