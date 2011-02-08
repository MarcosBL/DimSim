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

package com.dimdim.conference.ui.common.client;

import java.util.Date;

import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HoverPopupPanel;

import com.dimdim.conference.ui.common.client.data.StreamingUrlsTable;
import com.dimdim.conference.ui.json.client.UIRosterEntry;
import com.dimdim.conference.ui.model.client.ConferenceGlobals;
import com.dimdim.conference.ui.model.client.ClientModel;
import com.dimdim.conference.ui.model.client.RosterModel;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 *
 */
public class UIGlobals
{
	//	A global table for use by different components as required.

//	protected	static	final	HashMap	table = new HashMap();
	protected	static	Date	consoleStartDateTime = new Date();
	protected	static	long	consoleStartTime = System.currentTimeMillis();
	protected	static	String	debugFlag = null;
	protected	static	boolean	show_debug_popups;
	protected	static	StreamingUrlsTable	streamingUrlsTable;
	
	public	static	StreamingUrlsTable	getStreamingUrlsTable()
	{
		if (UIGlobals.streamingUrlsTable == null)
		{
			UIGlobals.streamingUrlsTable = new StreamingUrlsTable();
		}
		return	UIGlobals.streamingUrlsTable;
	}
//	public	HashMap	getTable()
//	{
//		return	UIGlobals.table;
//	}
	public	static	String	getConsoleStartDate_str()
	{
		return	UIGlobals.consoleStartDateTime.toString();
	}
	public	static	String	getConsoleEndDate_str()
	{
		long t = UIGlobals.consoleStartTime + (ConferenceGlobals.currentMaxMeetingTime*60*1000);
		Date d = new Date();
		d.setTime(t);
		
		return	d.toString();
	}
	public	static	long	getConsoleStartTime()
	{
		return	UIGlobals.consoleStartTime;
	}
	public	static	String	getWaitMovieUrl()
	{
		return	"swf/please_wait.swf";
	}
	public	static	String	getPlaceholderMovieUrl()
	{
//		if (ConferenceGlobals.isBrowserIE())
//		{
//			return	UIGlobals.getNewWaitMovieUrl();
//		}
//		else
//		{
			return	UIGlobals.getNewWaitMovieUrl();
//		}
	}
	public	static	String	getNewWaitMovieUrl()
	{
		return	"swf/placeholder.swf";
	}
	public	static	boolean	showDebugPopups()
	{
		if (UIGlobals.debugFlag == null)
		{
			UIGlobals.show_debug_popups = false;
			UIGlobals.debugFlag = ConferenceGlobals.getDisplayString("show_debug_popup","false");
			if (UIGlobals.debugFlag != null && UIGlobals.debugFlag.equalsIgnoreCase("true"))
			{
				UIGlobals.show_debug_popups = true;
			}
		}
		return	UIGlobals.show_debug_popups;
	}
	public	static	boolean	isPresenter(UIRosterEntry user)
	{
		return (user.getRole().equals(UIConstants.ROLE_ACTIVE_PRESENTER) ||
				user.getRole().equals(UIConstants.ROLE_PRESENTER));
	}
	public	static	boolean	isOnlyPresenter(UIRosterEntry user)
	{
		return user.getRole().equals(UIConstants.ROLE_PRESENTER);
	}
	public	static	boolean	isOrganizer(UIRosterEntry user)
	{
		return user.isHost();//(user.getUserId().equals(ConferenceGlobals.organizerEmail));
	}
	public	static	boolean	isActivePresenter(UIRosterEntry user)
	{
		return (user.getRole().equals(UIConstants.ROLE_ACTIVE_PRESENTER));
	}
	public	static	boolean	isAttendee(UIRosterEntry user)
	{
		return (user.getRole().equals(UIConstants.ROLE_ATTENDEE));
	}
	public	static	boolean	isInLobby(UIRosterEntry user)
	{
		return	user.getMood().equals(UIConstants.WaitingInLobby);
	}
	protected	static	UIRosterEntry	me;
	public	static	boolean	amInLobby()
	{
		if (UIGlobals.me == null)
		{
			UIGlobals.me = ClientModel.getClientModel().getRosterModel().getCurrentUser();
		}
		return	UIGlobals.isInLobby(UIGlobals.me);
	}
	
	/**
	 * The list entries hover popup. There needs to be only a single
	 * one at a time.
	 */
	public	static	HoverPopupPanel	consoleMiddleLeftHoverPopup = null;
	public	static	void	showHoverPopup(HoverPopupPanel popup)
	{
		UIGlobals.closePreviousHoverPopup();
		UIGlobals.consoleMiddleLeftHoverPopup = popup;
		UIGlobals.consoleMiddleLeftHoverPopup.showHoverPopup();
	}
	public	static	void	closePreviousHoverPopup()
	{
		if (UIGlobals.consoleMiddleLeftHoverPopup != null)
		{
			UIGlobals.consoleMiddleLeftHoverPopup.hideHoverPopup();
			UIGlobals.consoleMiddleLeftHoverPopup = null;
		}
	}
	public	static	DialogBox	theDialogBox = null;

	public	static	int	getCommonDialogBoxPopupPositionX()
	{
		return	161;
	}
	public	static	int	getCommonDialogBoxPopupPositionY()
	{
		return	155;
	}
	

	public	static	String	getInvitePresentersComment()
	{
//		return	"To invite other people as presenters, "+
//			"please type their email addresses here seperated by semicolon:";
		

		return ConferenceGlobals.getDisplayString("presenterinvite.comment1","To invite other people as presenters, "+
				"please type their email addresses here seperated by semicolon:");	
		
	}
	public	static	String	getSettingsMainComment()
	{
		//return	"Change following settings for your meeting. These will be effective only "+
		//	"for the duration of your meeting";
		return ConferenceGlobals.getDisplayString("meetingsetting.maincomment","Change following settings for your meeting. These will be effective only"+
				"for the duration of your meeting");
	}
	public	static	String	getMeetingLobbySettingComment()
	{
		//return	"Meeting Lobby:";
		return ConferenceGlobals.getDisplayString("meetingsetting.option1comment","Meeting Waiting Area:");
		
	}
	public	static	String	getEnableLobbyButtonTagText()
	{
//		return	"enable";
		return ConferenceGlobals.getDisplayString("meetingsetting.option1enable","enable");	
	}
	public	static	String	getDisableLobbyButtonTagText()
	{
//		return	"disable";
		return ConferenceGlobals.getDisplayString("meetingsetting.option1disable","disable");		
	}
	
	public	static	String	getMouseEnableSettingComment()
	{
		//return	"Meeting Lobby:";
		return ConferenceGlobals.getDisplayString("mousesetting.optioncomment","Mouse Tracking:");
		
	}
	public	static	String	getMouseEnableButtonTagText()
	{
//		return	"enable";
		return ConferenceGlobals.getDisplayString("mousesetting.option1enable","enable");	
	}
	public	static	String	getMouseDisableButtonTagText()
	{
//		return	"disable";
		return ConferenceGlobals.getDisplayString("mousesetting.option1disable","disable");		
	}

	
	public	static	String	getMaxAttendeesSettingComment()
	{
//		return	"Maximum number of participants:";
		return ConferenceGlobals.getDisplayString("meetingsetting.maxattendee_comment","Maximum number of participants:");
	}
	public	static	String	getCurrentMeetingLengthComment()
	{
		return ConferenceGlobals.getDisplayString("meetingsetting.current_meeting_legnth_comment","Current time for meeting(minutes)");				
	}
	public	static	String	getMaxTimeSettingComment()
	{
//		return	"Maximum time for meeting(minutes)";
		return ConferenceGlobals.getDisplayString("meetingsetting.maxtime_comment","Maximum time for meeting(minutes)");				
	}
	public	static	String	getMeetingHoursSuffix()
	{
//		return	"Maximum time for meeting(minutes)";
		return ConferenceGlobals.getDisplayString("meetingsetting.hours_suffix","Maximum time for meeting(minutes)");				
	}
	public	static	String	getMeetingMinutesSuffix()
	{
//		return	"Maximum time for meeting(minutes)";
		return ConferenceGlobals.getDisplayString("meetingsetting.minutes_suffix","Maximum time for meeting(minutes)");				
	}
	public	static	String	getNetworkProfileSettingComment()
	{
//		return	"Network Profile:";
		return ConferenceGlobals.getDisplayString("meetingsetting.option2comment","Network Setting:");		
		
	}
	public	static	String	getReturnURL()
	{
	//	return	"High";
		return ConferenceGlobals.getDisplayString("return_url.label","Return Url: ");		
	}
	public	static	String	getNetworkProfile1ButtonTagText()
	{
//		return	"dial-up";
		return ConferenceGlobals.getDisplayString("meetingsetting.option2dialup","Low");
	}
	public	static	String	getNetworkProfile2ButtonTagText()
	{
//		return	"cable-dsl";
		return ConferenceGlobals.getDisplayString("meetingsetting.option2cabledsl","Medium");	
	}
	public	static	String	getNetworkProfile3ButtonTagText()
	{
//		return	"lan";
		return ConferenceGlobals.getDisplayString("meetingsetting.option2lan","High");
	}
	public	static	String	getImageQualitySettingComment()
	{
//		return	"Image Quality:";
		return ConferenceGlobals.getDisplayString("meetingsetting.option3comment","Image Quality:");
	}
	public	static	String	getImageQualityLowButtonTagText()
	{
	//	return	"Low";
		return ConferenceGlobals.getDisplayString("meetingsetting.option3low","Low");		
	}
	public	static	String	getImageQualityMediumButtonTagText()
	{
	//	return	"Medium";
		return ConferenceGlobals.getDisplayString("meetingsetting.option3medium","Medium");		
	}
	public	static	String	getImageQualityHighButtonTagText()
	{
	//	return	"High";
		return ConferenceGlobals.getDisplayString("meetingsetting.option3high","High");		
	}
	public	static	String	getYesLabelText()
	{
		return	"Yes";
	}
	public	static	String	getNoLabelText()
	{
		return	"No";
	}
	public	static	String	getRateYourExperienceComment()
	{
		//return	"Please rate your dimdim meeting experience:";
		return ConferenceGlobals.getDisplayString("experience.comment1","Please rate your dimdim meeting experience:");
	}
	public	static	String	getExperienceRateLabelText(int i)
	{
		if (i == 0)
		{
			//return	"Poor";
			return ConferenceGlobals.getDisplayString("experience.poor","Poor");
		}
		else if (i == 1)
		{
			//return	"Fair";
			return ConferenceGlobals.getDisplayString("experience.fair","Fair");
		}
		else if (i == 2)
		{
			//return	"Neutral";
			return ConferenceGlobals.getDisplayString("experience.neutral","Neutral");
		}
		else if (i == 3)
		{
			//return	"Good";
			return ConferenceGlobals.getDisplayString("experience.good","Good");
		}
		//return	"Excellent";
		    return ConferenceGlobals.getDisplayString("experience.excellent","Excellent");	
	}
	public	static	String	getInviteAttendeesComment()
	{
//		return	"To invite other people as attendees, "+
//			"please type their email addresses here seperated by semicolon:";
		
		return ConferenceGlobals.getDisplayString("presenterinvite.comment2","To invite other people as attendees, "+
				"please type their email addresses here seperated by semicolon:");	
	}
	public	static	String	getAddPersonalMessageComment()
	{
		//return	"Enter an optional personal message:";
	    return ConferenceGlobals.getDisplayString("presenterinvite.comment3","Enter an optional personal message:");
		
	}
	public	static	String	getFeedbackMessageComment()
	{
		//return	"Please enter any comments you may have about your meeting experience";
		return ConferenceGlobals.getDisplayString("experience.comment2","Please enter any comments you may have about your meeting experience");
	}
	public	static	String	getDefaultInvitationPersonalMessage()
	{
//		return	"Please join my meeting";
		return ConferenceGlobals.getDisplayString("presenterinvite.comment4","Please join my meeting");
	}
	protected	static	int	lastPopupLeftPosition	=	0;
	protected	static	int	lastPopupTopPosition	=	0;

	public	static	void	setLastPopupPosition(int left, int top)
	{
		UIGlobals.lastPopupLeftPosition = left;
		UIGlobals.lastPopupTopPosition = top;
	}
	public	static	int	getLastPopupLeftPosition()
	{
		return	UIGlobals.lastPopupLeftPosition;
	}
	public	static	int	getLastPopupTopPosition()
	{
		return	UIGlobals.lastPopupTopPosition;
	}
	public	static	String	getVideoEnabledMessageTitle()
	{
		return	"Video Enabled";
	}
	
	public	static	String	getSelfVideoEnabledMessage()
	{
//		return	"You have been granted permission to transmit video and audio for this meeting."+
//			" Please change to Conference tab to manage the controls";

		return	ConferenceGlobals.getDisplayString("meetingsetting.videocomment1","You have been granted permission to transmit video and audio for this meeting. Please change to Conference tab to manage the controls");

	}
	
	public	static	String	getUserVideoEnabledMessage(String userName)
	{
//		return	userName+" has been granted permission to transmit video and audio for this meeting."+
//			" Please change to Conference tab to view this user's video.";

		return	userName + ConferenceGlobals.getDisplayString("meetingsetting.videocomment2","has been granted permission to transmit video and audio for this meeting. Please change to Conference tab to view this user's video.");
	}
	
	private	static	RosterModel	rosterModel;
	
	public static String getActivePresenterNetworkProfile()
	{
		if (UIGlobals.rosterModel == null)
		{
			UIGlobals.rosterModel = ClientModel.getClientModel().getRosterModel();
		}
		return UIGlobals.rosterModel.getCurrentActivePresenter().getNetProfile();
	}
	
	public	static	String	getDefaultAudioNetworkProfile()
	{
		return	"3";
	}
	public	static	int	getAVBroadcasterWidth(String sizeFactor)
	{
		if (sizeFactor.equals("4"))
		{
			return	816;
		}
		else if (sizeFactor.equals("25"))
		{
			return	528;
		}
		return	240;
	}
	public	static	int	getAVBroadcasterHeight(String sizeFactor)
	{
		if (sizeFactor.equals("4"))
		{
			return	652;
		}
		else if (sizeFactor.equals("25"))
		{
			return	436;
		}
		return	220;
	}
	public	static	int	getAudioBroadcasterWidth()
	{
		return	240;
	}
	public	static	int	getAudioBroadcasterHeight()
	{
		return	180;
	}
//	public	static	int	getAVPlayerWidth()
//	{
//		return	240;
//	}
//	public	static	int	getAVPlayerHeight()
//	{
//		return	220;
//	}
	public	static	int	getAudioPlayerWidth()
	{
		return	70;
	}
	public	static	int	getAudioPlayerHeight()
	{
		return	18;
	}
	public	static	int	getHoverPopupWidthClearance()
	{
		return	240;
	}
	public	static	int	getHoverPopupHeightClearance()
	{
		return	0;
	}
	public	static	int	getHoverPopupShowDelayMillis()
	{
		return	250;
	}
	public	static	String	getSWFVersionNumber()
	{
		return	"2.0.0.1";
	}
	public	static	int	getHoverInitialShowTime()
	{
		return	2000;
	}
	public	static	int	getHoverResponseRequiredShowTime()
	{
		return	600;
	}
	public	static	int	getHoverPostMouseOutShowTime()
	{
		return	20;
	}
	public	static	String	getMakeActivePresenterLinkText()
	{
//		return	"Make Presenter";
		return ConferenceGlobals.getDisplayString("console.makepresenterlabel","Make Presenter");		
	}
	public	static	String	getTakeActivePresenterControlLinkText()
	{
//		return	"Take Control";
		return ConferenceGlobals.getDisplayString("console.takecontrollabel","Take Control");		
	}
	public	static	String	getReturnActivePresenterControlLinkText()
	{
//		return	"Return Control";
		return ConferenceGlobals.getDisplayString("console.returncontrollabel","Return Control");		
	}
	/**
	 * Console labels and strings
	 */
	
	public	static	String	getScreenTabLabel()
	{
		return	ConferenceGlobals.getDisplayString("screen_tab.label","Screen");
	}
	public	static	String	getDocumentTabLabel()
	{
		return	ConferenceGlobals.getDisplayString("documents_tab.label","Documents");
	}
	public	static	String	getCollaborationTabLabel()
	{
		return	ConferenceGlobals.getDisplayString("collaboration_tab.label","Collaboration");
	}
	public	static	String	getDiscussTabLabel()
	{
//		return	"Discuss";
		return	ConferenceGlobals.getDisplayString("console.discusslabel","Chat With All");		
	}
	public	static	String	getDiscussSubTabComment()
	{
//		return	"Discuss about this meeting";
		return	ConferenceGlobals.getDisplayString("console.discussmeetinglabel","Discuss about this meeting");		
	}
	public	static	String	getAssistantSubTabLabel()
	{
//		return	"Discuss about this meeting";
		return	ConferenceGlobals.getDisplayString("console.assistantlabel","Assistant");		
	}
	public	static	String	getDesktopSubTabLabel()
	{
//		return	"Desktop";
		return	ConferenceGlobals.getDisplayString("console.desktoplabel","Desktop");		
	}
	public	static	String	getApplicationSubTabLabel()
	{
//		return	"Application";
		return	ConferenceGlobals.getDisplayString("console.applicationlabel","Application");		
	}
	public	static	String	getExcelSubTabLabel()
	{
//		return	"Excel";
		return	ConferenceGlobals.getDisplayString("console.excellabel","Excel");	
	}
	public	static	String	getWordSubTabLabel()
	{
//		return	"Word";
		return	ConferenceGlobals.getDisplayString("console.wordlabel","Word");
	}
	public	static	String	getPdfSubTabLabel()
	{
//		return	"Pdf";
		return	ConferenceGlobals.getDisplayString("console.pdflabel","Pdf");		
	}
	public	static	String	getBrowserSubTabLabel()
	{
//		return	"Browser";
		return	ConferenceGlobals.getDisplayString("console.browserlabel","Browser");		
	}
	public	static	String	getPowerpoinSubTabLabel()
	{
//		return	"Powerpoint";
		return	ConferenceGlobals.getDisplayString("console.powerpointlabel","Powerpoint");		
	}
	public	static	String	getPowerpoinXSubTabLabel()
	{
//		return	"Powerpoint";
		return	ConferenceGlobals.getDisplayString("console.powerpoint2007label","Powerpoint 2007");		
	}
	public	static	String	getListPanelAllLinkLabel(UIRosterEntry user)
	{
		if (UIGlobals.isActivePresenter(user))
		{
//			return	"Manage";
			return	ConferenceGlobals.getDisplayString("console.managelabel","Manage");			
		}
//		return	"All";
		return	ConferenceGlobals.getDisplayString("console.alllabel","Show All");		
	}
	
	public	static	String	getListPanelManegeLinkLabel(UIRosterEntry user)
	{
		return	ConferenceGlobals.getDisplayString("console.managelabel","Manage");			
	}
	
	public	static	String	getListPanelShareLinkLabel(UIRosterEntry user)
	{
		//if (UIGlobals.isPresenter(user))
		//{
//			return	"Manage";
			return	ConferenceGlobals.getDisplayString("console.sharelabel","Share...");			
		//}
//		return	"All";
		//return	ConferenceGlobals.getDisplayString("console.alllabel","Show All");		
	}
	public	static	String	getNowSharingText()
	{
//		return	"Sharing desktop";
		return	ConferenceGlobals.getDisplayString("console.nowsharing","Now Sharing:");	
	}
	public	static	String	getDTPSharingWorkspaceHeaderText()
	{
//		return	"Sharing desktop";
		return	ConferenceGlobals.getDisplayString("console.sharingdesktoplabel","Sharing desktop");	
	}
	public	static	String	getAppSharingWorkspaceHeaderText(String appName)
	{
//		return	"Sharing application "+appName;
		return	ConferenceGlobals.getDisplayString("console.sharingapplicationlabel","Sharing application ") +appName ;		
	}
	public	static	String	getPptSharingWorkspaceHeaderText(String pptName)
	{
//		return	"Sharing presentation "+pptName;
		return	ConferenceGlobals.getDisplayString("console.sharingpresentationlabel","Sharing presentation ") +pptName ;		
	}
	public	static	String	getWorkspaceHeaderText()
	{
//		return	"Dimdim Collaboration Workspace";
		return	ConferenceGlobals.getDisplayString("console.dimdimcollaborationworkspacelabel","Web Meeting Collaboration Workspace");		
	}
	public	static	String	getFeedbackConfirmation()
	{
//		return	"Dimdim Collaboration Workspace";
		return	ConferenceGlobals.getDisplayString("console.feedbackconfirmation","The Feedback has been send to the Web Meeting Team");		
	}
	public	static	String	getSettingChangeConfirmation()
	{
		return	ConferenceGlobals.getDisplayString("console.settingchangeconfirmation","The Changes in the setting has been made");		
	}
	
	public	static	String	getChangeConfirmForCurrMeeting()
	{
		return	ConferenceGlobals.getDisplayString("console.settingchangeconfirmForCurrMeeting","The Changes in the setting has been made");		
	}
	public	static	String	getPubSettingChangeConfirmation()
	{
		return	ConferenceGlobals.getDisplayString("console.settingchangeconfirmation","The Changes in the setting has been made. The changes will take effect the next time you start sharing.");		
	}
	public	static	String	getWhiteboardTabLabel()
	{
		return	ConferenceGlobals.getDisplayString("whiteboard_tab.label","Whiteboard");
	}
	public	static	String	getWhiteboardLockLabel()
	{
		return	ConferenceGlobals.getDisplayString("whiteboard_lock.label","Lock");
	}
	public	static	String	getWhiteboardUnlockLabel()
	{
		return	ConferenceGlobals.getDisplayString("whiteboard_unlock.label","Unlock");
	}
}
