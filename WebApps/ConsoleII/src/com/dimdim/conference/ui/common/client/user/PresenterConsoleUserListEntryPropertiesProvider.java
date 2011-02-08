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

import com.dimdim.conference.ui.common.client.UIImages;
import com.dimdim.conference.ui.common.client.UserGlobals;
import com.dimdim.conference.ui.common.client.list.DefaultListEntryPropertiesProvider;
import com.dimdim.conference.ui.common.client.util.DebugPanel;
import com.dimdim.conference.ui.json.client.UIRosterEntry;
import com.dimdim.conference.ui.model.client.ConferenceGlobals;
import com.google.gwt.user.client.ui.Image;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */

public class PresenterConsoleUserListEntryPropertiesProvider extends DefaultListEntryPropertiesProvider
{
	protected	String	photoImageUrl = "images/default_photo_1.jpg";
	protected	UIRosterEntry	me;
	protected	UIRosterEntry	user;
	
	public	PresenterConsoleUserListEntryPropertiesProvider(UIRosterEntry user,UIRosterEntry me)
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
//		return	"presenter-label-background";
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
	 * On the presenter console the presenter doesn't get chat icon for himself.
	 * The icon is there for everyone else and indicates permission and if
	 * enabled kicks off chat.
	 */
	public Image getImage2Url()
	{
		if (!this.me.getUserId().equals(user.getUserId()) && ConferenceGlobals.privateChatEnabled)
		{
			if (this.user.isChatOn())
			{
				return UIImages.getImageBundle(UIImages.defaultSkin).getUserChatEnabledImageUrl();
			}
			else
			{
				return UIImages.getImageBundle(UIImages.defaultSkin).getUserChatDisabledImageUrl();
			}
		}
		return	null;
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
	 * On the presenter console, if the entry is an attendee put the mike icon
	 * which will control the audio permission of the attendee and indicate the
	 * current audio permission.
	 */
	public Image getImage3Url()
	{
		if(ConferenceGlobals.isPresenterAVAudioDisabled()){
			return null;
		}
		else
		{
			/*DebugPanel.getDebugPanel().addDebugMessage("ConferenceGlobals.isPresenterAVAudioDisabled() = "+ConferenceGlobals.isPresenterAVAudioDisabled());
			DebugPanel.getDebugPanel().addDebugMessage("ConferenceGlobals.isPresenterAVVideoOnly() = "+ConferenceGlobals.isPresenterAVVideoOnly());
			DebugPanel.getDebugPanel().addDebugMessage("ConferenceGlobals.isPresenterAVActive() = "+ConferenceGlobals.isPresenterAVAudoVideo());
			DebugPanel.getDebugPanel().addDebugMessage("ConferenceGlobals.isPresenterAVAudioOnly() = "+ConferenceGlobals.isPresenterAVAudioOnly());
			DebugPanel.getDebugPanel().addDebugMessage("this.user.isVideoOn() = "+this.user.isVideoOn());
			DebugPanel.getDebugPanel().addDebugMessage("this.user.isAudioOn() = "+this.user.isAudioOn());
			DebugPanel.getDebugPanel().addDebugMessage("this.user.getDisplayName() = "+this.user.getDisplayName());*/
			if (this.me.getUserId().equals(user.getUserId()))
			{
				if (ConferenceGlobals.isPresenterAVActive()) 
						
					if(ConferenceGlobals.isPresenterAVVideoOnly() 
								|| ConferenceGlobals.isPresenterAVAudoVideo()
					)
					{
						//DebugPanel.getDebugPanel().addDebugMessage("setting presenter audio enabled...cam enabled");
						return UIImages.getImageBundle(UIImages.defaultSkin).getUserCamEnabledImageUrl();
					}else{
						//DebugPanel.getDebugPanel().addDebugMessage("setting presenter audio enabled...audio enabled");
						return UIImages.getImageBundle(UIImages.defaultSkin).getUserAudioEnabledImageUrl();
					}
				
				/*if (ConferenceGlobals.isPresenterAVVideoOnly() || ConferenceGlobals.isPresenterAVAudoVideo())
				{
					//DebugPanel.getDebugPanel().addDebugMessage("setting presenter audio enabled...cam disabled");
					return UIImages.getImageBundle(UIImages.defaultSkin).getUserCamDisabledImageUrl();
				}
				if (ConferenceGlobals.isPresenterAVAudioOnly())
				{
					//DebugPanel.getDebugPanel().addDebugMessage("setting presenter audio enabled...audio disabled");
					return UIImages.getImageBundle(UIImages.defaultSkin).getUserAudioDisabledImageUrl();
				}*/
				//DebugPanel.getDebugPanel().addDebugMessage("setting presenter audio enabled...audio disabled");
				//return UIImages.getImageBundle(UIImages.defaultSkin).getUserAudioDisabledImageUrl();
				return null;
				
			}else{
				if (this.user.isVideoOn())
				{
					return UIImages.getImageBundle(UIImages.defaultSkin).getUserCamEnabledImageUrl();
				}
				if (this.user.isAudioOn())
				{
					return UIImages.getImageBundle(UIImages.defaultSkin).getUserAudioEnabledImageUrl();
				}
				/*if (ConferenceGlobals.isPresenterAVAudioOnly()
						|| "0".equalsIgnoreCase(ConferenceGlobals.userInfoDictionary.getStringValue("max_attendee_videos")))
				{
					return UIImages.getImageBundle(UIImages.defaultSkin).getUserAudioDisabledImageUrl();
				}else{
					return UIImages.getImageBundle(UIImages.defaultSkin).getUserCamDisabledImageUrl();
				}*/
				return null;
			}
		}
		//return null;
	}
	
	public String getImage3Tooltip()
	{
		if(ConferenceGlobals.isPresenterAVAudioDisabled()){
			return null;
		}
		else
		{
			if (this.me.getUserId().equals(user.getUserId()))
			{
				if (ConferenceGlobals.isPresenterAVActive()) 
					
					if(ConferenceGlobals.isPresenterAVVideoOnly() 
								|| ConferenceGlobals.isPresenterAVAudoVideo()
					)
				{
						return ConferenceGlobals.getTooltip("presenter.disable_video");
				}else{
					return ConferenceGlobals.getTooltip("presenter.disable_audio");
				}
				
				if (ConferenceGlobals.isPresenterAVVideoOnly() || ConferenceGlobals.isPresenterAVAudoVideo())
				{
					return ConferenceGlobals.getTooltip("presenter.enable_video");
				}
				if (ConferenceGlobals.isPresenterAVAudioOnly())
				{
					return ConferenceGlobals.getTooltip("presenter.enable_audio");
				}
				
				return ConferenceGlobals.getTooltip("presenter.enable_audio");
				
			}
			else
			{
				if (this.user.isVideoOn())
				{
					return ConferenceGlobals.getTooltip("presenter.disable_video");
				}
				if (this.user.isAudioOn())
				{
					return ConferenceGlobals.getTooltip("presenter.disable_audio");
				}
				if (ConferenceGlobals.isPresenterAVAudioOnly()
						|| "0".equalsIgnoreCase(ConferenceGlobals.userInfoDictionary.getStringValue("max_attendee_videos")))
				{
					return ConferenceGlobals.getTooltip("presenter.enable_audio");
				}
				else
				{
					return ConferenceGlobals.getTooltip("presenter.enable_video");
				}
			}
		}
		//return null;
	}
	/**
	 * On the presenter console, if the entry is an attendee put the 
	 * permissions control image.
	 */
	public Image getImage4Url()
	{
		/*if (!this.me.getUserId().equals(user.getUserId()))
		{
			if(!ConferenceGlobals.privateChatEnabled && ConferenceGlobals.isPresenterAVAudioDisabled())
			{
				return null;
			}
			return UIImages.getPermissionsControlImageUrl();
		}
		return null;*/
		
		return UIImages.getImageBundle(UIImages.defaultSkin).getHideChat();
	}
	public String getImage4Tooltip()
	{				
		/*if (!this.me.getUserId().equals(user.getUserId()))
		{
			if(ConferenceGlobals.isPresenterAVAudioDisabled())
			{
				if(ConferenceGlobals.privateChatEnabled)
				{
					return ConferenceGlobals.getTooltip("presenter.manage_permissions_av_audio_disabled")+" "+
					user.getDisplayName();	
				}
				else
				{
					return "";
				}
				
				
			}
			else {
				if(ConferenceGlobals.privateChatEnabled)
				{
					return ConferenceGlobals.getTooltip("presenter.manage_permissions_av_audio_disabled")+" "+
					user.getDisplayName();	
				}
				else
				{
					if (ConferenceGlobals.isMeetingVideoChat())
					{
						return ConferenceGlobals.getTooltip("presenter.manage_permissions_chat_disabled_video")+" "+
							user.getDisplayName();
					}
					else
					{
						return ConferenceGlobals.getTooltip("presenter.manage_permissions_chat_disabled")+" "+
							user.getDisplayName();
					}
				}
			}			
		}*/
		
		return	ConferenceGlobals.getTooltip("click_menu");
	}
}
