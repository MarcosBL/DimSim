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

package com.dimdim.conference.ui.model.client;

import com.dimdim.conference.ui.json.client.UIClientScreen;
import com.dimdim.conference.ui.json.client.UIResourceObject;
import com.dimdim.conference.ui.json.client.UIRosterEntry;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * The get...URL methods return a complete url which can be used with the
 * JSONurlReader object.
 * 
 * The get....FormURL methods return form url, to which a form must be posted.
 * These are used when the data to be passed could be large or indeterminate.
 * 
 */
public class CommandURLFactory
{
	protected	String	webappRoot = null;
	protected	String	sessionKeyParam = null;
	protected	static	int		counter = 1;
	
	public	CommandURLFactory()
	{
		this.webappRoot = ConferenceGlobals.webappRoot;
		this.sessionKeyParam = "sessionKey="+ConferenceGlobals.sessionKey;
	}
	public String getGetConsoleDataURL()
	{
		return webappRoot+"GetConsolePageData.action?"+this.sessionKeyParam;
	}
	/**
	 * Roster group commands
	 */
	public String getSendInvitationURL(String attendees, String presenters, String message)
	{
		String url = webappRoot+"SendJoinInvitation.action?"+this.sessionKeyParam;
		
		url = url + "&userType="+ConferenceGlobals.userType;
		if (attendees != null && attendees.length() > 0)
		{
			url = url+"&emails="+attendees;
		}
		if (presenters != null && presenters.length() >0)
		{
			url = url+"&presenters="+presenters;
		}
		if (message != null && message.length() > 0)
		{
			url = url+"&message="+message;
		}
		return url;
	}
	public String getSendInvitationURLData(String attendees, String presenters, String message)
	{
		String urlData = "";
		if (attendees != null && attendees.length() > 0)
		{
			urlData = urlData+"emails="+attendees;
		}
		if (presenters != null && presenters.length() >0)
		{
			if (urlData.length() > 0)
			{
				urlData = urlData + "&";
			}
			urlData = urlData+"presenters="+presenters;
		}
		if (message != null && message.length() > 0)
		{
			if (urlData.length() > 0)
			{
				urlData = urlData + "&";
			}
			urlData = urlData+"message="+message;
		}
		return urlData;
	}
	public	String	getReloadConsoleURL()
	{
		return	webappRoot+"ReloadConsole.action?"+this.sessionKeyParam;
	}
	public String getSendFeedbackURL(int rating, String comment, String sender, String toEmail)
	{
		String url = webappRoot+"SendFeedback.action?"+this.sessionKeyParam+
			"&sender="+sender+"&rating="+rating + "&toEmail="+toEmail;
		
		url = url + "&userType="+ConferenceGlobals.userType;
		if (comment != null && (comment=comment.trim()).length() > 0)
		{
			url = url+"&comment="+comment;
		}
		return	url;
	}
	public	String	getMakePresenterURL(UIRosterEntry user)
	{
		return	webappRoot+"MakePresenter.action?"+
			this.sessionKeyParam+"&userId="+user.getUserId();
	}
	public	String	getMakeActivePresenterURL(UIRosterEntry user)
	{
		return	webappRoot+"MakeActivePresenter.action?"+
			this.sessionKeyParam+"&userId="+user.getUserId();
	}
	public	String	getGrantEntryToAttendeeURL(UIRosterEntry user)
	{
		return	webappRoot+"GrantEntry.action?"+
			this.sessionKeyParam+"&userId="+user.getUserId();
	}
	public	String	getDenyEntryToAttendeeURL(UIRosterEntry user)
	{
		return	webappRoot+"DenyEntry.action?"+
			this.sessionKeyParam+"&userId="+user.getUserId();
	}
	public	String	getGrantEntryToAllURL()
	{
		return	webappRoot+"GrantEntryToAll.action?"+this.sessionKeyParam;
	}
	public	String	getEnableLobbyURL()
	{
		return	webappRoot+"EnableLobby.action?"+this.sessionKeyParam;
	}
	public	String	getDisableLobbyURL()
	{
		return	webappRoot+"DisableLobby.action?"+this.sessionKeyParam;
	}
//	public	String	getSendInvitationURL(String email)
//	{
//		return	webappRoot+"SendInvitation.action?email="+email;
//	}
	public	String	getRemoveAttendeeURL(UIRosterEntry user)
	{
		return	webappRoot+"RemoveParticipant.action?"+this.sessionKeyParam+"&userId="+user.getUserId();
	}
	public	String	getLeaveConferenceURL()
	{
		return	webappRoot+"LeaveConference.action?"+sessionKeyParam;
	}
	public	String	getExtendSessionTimeoutURL()
	{
		return	webappRoot+"ExtendSessionTimeout.action?"+sessionKeyParam;
	}
	public	String	getResetSessionTimeoutURL()
	{
		return	webappRoot+"ResetSessionTimeout.action?"+sessionKeyParam;
	}
	public	String	getConsoleURL()
	{
		return	webappRoot+"GetConsole.action?"+sessionKeyParam;
	}
	public	String	getConsoleLoadedURL()
	{
		return	webappRoot+"ConsoleLoaded.action?"+sessionKeyParam;
	}
	public	String	getConsoleReloadedLoadedURL()
	{
		return	webappRoot+"ConsoleReloaded.action?"+sessionKeyParam;
	}
	public	String	getUnloadConsoleURL()
	{
		return	webappRoot+"UnloadConsolePage.action";
	}
	public	String	getCreateChildSessionURL()
	{
		return	webappRoot+"CreateChildSession.action?"+sessionKeyParam;
	}
	public	String	getSetMoodURL(String mood)
	{
		return	webappRoot+"SetMood.action?"+sessionKeyParam+"&mood="+mood;
	}
	public	String	getSetDisplayNameURL(String name)
	{
		return	webappRoot+"SetName.action?"+sessionKeyParam+"&name="+name;
	}
	public	String	getSetProfileOptionsURL(String netProfile, String imgQuality,
				String maxParticipants, String maxMeetingTime,String returnUrl)
	{
		return	webappRoot+"SetProfileOptions.action?"+sessionKeyParam+
			"&netProfile="+netProfile+"&imgQuality="+imgQuality+
			"&maxParticipants="+maxParticipants+"&maxMeetingTime="+maxMeetingTime+
			"&returnUrl="+returnUrl;
	}
	public	String	getPhotoUploadURL()
	{
		return	webappRoot+"UploadPhoto.action?"+sessionKeyParam;
	}
	public	String	getSetPhotoToDefaultURL()
	{
		return	webappRoot+"SetPhotoToDefault.action?"+sessionKeyParam;
	}
	public	String	getSetRoleURL(String role)
	{
		return	webappRoot+"SetRole.action?"+sessionKeyParam+"&role="+role;
	}
	public	String	getSetChatPermissionUrl(String id, boolean enable)
	{
		if (enable)
		{
			return	webappRoot+"SetChatPermission.action?"+sessionKeyParam+"&enableList="+id;
		}
		else
		{
			return	webappRoot+"SetChatPermission.action?"+sessionKeyParam+"&disableList="+id;
		}
	}
	public	String	getSetChatPermissionForAllUrl(boolean enable)
	{
		if (enable)
		{
			return	webappRoot+"SetChatPermission.action?"+sessionKeyParam+"&enableAll=true";
		}
		else
		{
			return	webappRoot+"SetChatPermission.action?"+sessionKeyParam+"&disableAll=true";
		}
	}
	public	String	getSetAudioPermissionUrl(String id, boolean enable)
	{
		if (enable)
		{
			return	webappRoot+"SetAudioPermission.action?"+sessionKeyParam+"&enableList="+id;
		}
		else
		{
			return	webappRoot+"SetAudioPermission.action?"+sessionKeyParam+"&disableList="+id;
		}
	}
	public	String	getSetAudioPermissionForAllUrl(boolean enable)
	{
		if (enable)
		{
			return	webappRoot+"SetAudioPermission.action?"+sessionKeyParam+"&enableAll=true";
		}
		else
		{
			return	webappRoot+"SetAudioPermission.action?"+sessionKeyParam+"&disableAll=true";
		}
	}
	public	String	getSetVideoPermissionUrl(String id, boolean enable)
	{
		if (enable)
		{
			return	webappRoot+"SetVideoPermission.action?"+sessionKeyParam+"&enableList="+id;
		}
		else
		{
			return	webappRoot+"SetVideoPermission.action?"+sessionKeyParam+"&disableList="+id;
		}
	}
	public	String	getSetVideoPermissionForAllUrl(boolean enable)
	{
		if (enable)
		{
			return	webappRoot+"SetVideoPermission.action?"+sessionKeyParam+"&enableAll=true";
		}
		else
		{
			return	webappRoot+"SetVideoPermission.action?"+sessionKeyParam+"&disableAll=true";
		}
	}
	
	/**
	 * Chat Group
	 */
	public	String	getSendChatMessageURL(String userId, String chatText)
	{
		String url = webappRoot+"SendChatMessage.action?"+sessionKeyParam+"&chatText="+chatText;
		if (userId != null)
		{
			url = url+"&userId="+userId;
		}
		return	url;
	}
	public	String	getSendChatMessageURLData(String userId, String chatText)
	{
		String urlData = "chatText="+chatText;
		if (userId != null)
		{
			urlData = urlData+"&userId="+userId;
		}
		return	urlData;
	}
	/**
	 * Resource Group
	 */
	public	String	getGetNewUUIDURL()
	{
		return	webappRoot+"GetNewUUID.action?"+sessionKeyParam;
	}
	public	String	getCreateResourceURL(String name, String type, String mediaId, String appHandle)
	{
		if (mediaId == null)
		{
			mediaId = name;
		}
		String str = webappRoot+"CreateResource.action?"+sessionKeyParam+
				"&name="+name+"&type="+type+"&mediaId="+mediaId;
		if (appHandle != null)
		{
			str += "&appHandle="+appHandle;
		}
		return	str;
	}
	
	public	String	getCreateCobResourceURL(String url , String confAddress)
	{
		String str = webappRoot+"CreateCobResource.action?"+sessionKeyParam+"&url="+url+"&confAddress="+confAddress;
		return	str;
	}
	
	
	
	public	String	getUpdateResourceURL(UIResourceObject res, String type,
				String mediaId, String appHandle)
	{
		String str = webappRoot+"UpdateResource.action?"+sessionKeyParam+
			"&resourceId="+res.getResourceId()+"&name="+
			res.getResourceName()+"&type="+type+"&mediaId="+mediaId;
		if (appHandle != null)
		{
			str += "&appHandle="+appHandle;
		}
		return	str;
	}
	public	String	getCloneResourceURL(UIResourceObject res)
	{
		return	webappRoot+"CloneResource.action?"+sessionKeyParam+"&id="+res.getResourceId();
	}
	public	String	getRenameResourceURL(UIResourceObject res, String name)
	{
		return	webappRoot+"RenameResource.action?"+sessionKeyParam+"&resourceId="+res.getResourceId()+
			"&name="+name;
	}
	public	String	getDeleteResourceURL(UIResourceObject res)
	{
		return	webappRoot+"DeleteResource.action?"+sessionKeyParam+"&resourceId="+res.getResourceId();
	}
	
	public	String	getShowResourceURL(UIResourceObject res)
	{
		return	webappRoot+"ShowResource.action?"+sessionKeyParam+"&id="+res.getResourceId();
	}
	public	String	getClearResourceAnnotationsURL(UIResourceObject res)
	{
		return	webappRoot+"ClearResourceAnnotations.action?id="+res.getResourceId();
	}
	
	/**
	 * Presentations
	 */
	public	String	createSealDMSPresentaion(String pptName,String pptID, int noOfSlides,
			String meetingKey, int width, int height)
	{
		return	webappRoot+"DMSPresentaion.action?pptName="+pptName+
			"&pptID="+pptID+"&noOfSlides="+noOfSlides+
			"&meetingKey="+meetingKey+"&width="+width+"&height="+height;
	}
	
	public	String	getStartPresentationURL(String resourceId,String startSlide)
	{
		return	webappRoot+"ControlPresentation.action?"+sessionKeyParam+
			"&controlAction=start&resourceId="+resourceId+"&slide="+startSlide;
	}
	public	String	getStopPresentationURL(String resourceId)
	{
		return	webappRoot+"ControlPresentation.action?"+sessionKeyParam+
			"&controlAction=stop&resourceId="+resourceId;
	}
	public	String	getSlideChangeURL(String resourceId,int slideIndex)
	{
		return webappRoot+"ControlPresentation.action?"+this.sessionKeyParam+
			"&resourceId="+resourceId+"&controlAction=slide"+"&slide="+slideIndex;
	}
	public	String	getEnableAnnotationsURL(String resourceId)
	{
		return webappRoot+"ControlPresentation.action?"+this.sessionKeyParam+
			"&resourceId="+resourceId+"&controlAction=ann_on";
	}
	public	String	getDisableAnnotationsURL(String resourceId)
	{
		return webappRoot+"ControlPresentation.action?"+this.sessionKeyParam+
			"&resourceId="+resourceId+"&controlAction=ann_off";
	}
	/**
	 * Screen sharing
	 */
	public	String	getStartSharingURL(String resourceId, String mediaId, String appHandle, String presenterId, String presenterPassKey)
	{
	    if (mediaId == null)
		{
			mediaId = resourceId;
		}
	    
		String str = webappRoot+"ScreenShare.action?cmd=start&conferenceKey="+ConferenceGlobals.conferenceKey+
				"&resourceId="+resourceId+"&mediaId="+mediaId+"&presenterId="+presenterId+"&presenterPassKey="+presenterPassKey;
		if (appHandle != null)
		{
			str += "&appHandle="+appHandle;
		}
		return	str;
	}
	
	public	String	getStopSharingURL(String resourceId, String mediaId, String appHandle, String presenterId, String presenterPassKey)
	{
	    if (mediaId == null)
		{
			mediaId = resourceId;
		}
		String str = webappRoot+"ScreenShare.action?cmd=stop&conferenceKey="+ConferenceGlobals.conferenceKey+
				"&resourceId="+resourceId+"&mediaId="+mediaId+"&presenterId="+presenterId+"&presenterPassKey="+presenterPassKey;
		if (appHandle != null)
		{
			str += "&appHandle="+appHandle;
		}
		return	str;
	}
	public	String	getSharedWindowChangedURL(String resourceId)
	{
		return	webappRoot+"SharedWindowChanged.action?"+sessionKeyParam+
			"&resourceId="+resourceId;
	}
	public	String	getSetScreenParamsURL(String confCode, UIClientScreen screen)
	{
		return	webappRoot+"SetScreenParams.action?"+sessionKeyParam+"&confCode="+
			confCode+","+screen.toString();
	}
	
	/**
	 * Video and Audio controls
	 */
	public	String	getStartVideoURL(String streamName, String profile)
	{
		return	webappRoot+"VideoShare.action?"+sessionKeyParam+
			"&cmd=start&streamId="+streamName+"&profile="+profile;
	}
	public	String	getStopVideoURL(String streamName)
	{
		return	webappRoot+"VideoShare.action?"+sessionKeyParam+"&cmd=stop&streamId="+streamName;
	}
	public	String	getStartAudioURL(String streamId)
	{
		return	webappRoot+"AudioShare.action?"+sessionKeyParam+"&cmd=start&streamId="+streamId;
	}
	public	String	getStopAudioURL(String streamId)
	{
		return	webappRoot+"AudioShare.action?"+sessionKeyParam+"&cmd=stop&streamId="+streamId;
	}
	
	/**
	 * Popout control
	 */
	public	String	getPopoutPanelURL(String panelId, String userId)
	{
		String str = webappRoot+"html/popout/PopoutPanel.action?"+sessionKeyParam+
			"&panelId="+panelId+"&cflag="+getGuid();
		if (userId != null)
		{
			str += "&userId="+userId;
		}
		str +="&userType="+ConferenceGlobals.userType;
		return	str;
	}
	public	String	getPopinPanelURL(String panelId, String userId)
	{
		String str = webappRoot+"html/popout/PopinPanel.action?"+sessionKeyParam+
			"&panelId="+panelId;
		if (userId != null)
		{
			str += "&userId="+userId;
		}
		return	str;
	}
	
	/**
	 * Events reading urls
	 */
//	public	String	getGetNextEventURL(String featureId)
//	{
//		return	webappRoot+"GetNextEvent.action?"+sessionKeyParam+"&featureId="+featureId;
//	}
	/**
	 * Slide image get.
	 */
	public	String	getGetSlideImageURL(String ownerId, String presentationId,
			boolean thumbnail, int slideIndex)
	{
		return	webappRoot+"GetSlideImage.action?"+sessionKeyParam+"&ownerId="+ownerId+
			"&presentationId="+presentationId+"&thumbnail="+thumbnail+"&slideIndex="+slideIndex+
			"&flag="+(CommandURLFactory.counter++);
	}
	
	private	native	String	getGuid() /*-{
		return	$wnd.getAGuid();
	}-*/;
}
