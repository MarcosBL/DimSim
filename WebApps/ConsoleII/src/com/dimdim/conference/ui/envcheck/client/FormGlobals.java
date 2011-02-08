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

package com.dimdim.conference.ui.envcheck.client;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * A simple static interface for simply organizing the gwt dictionary lookup.
 */

public class FormGlobals
{

	public	static	final	String	emailField = "email";
	public	static	final	String	displayNameField = "displayName";
	public	static	final	String	meetingNameField = "confName";
	public	static	final	String	meetingKeyField = "confKey";
	public	static	final	String	asPresenterCheck = "asPresenter";
	public	static	final	String	startNow = "startNow";
	public	static	final	String	attendees = "attendees";
	public	static	final	String	presenters = "presenters";
	public	static	final	String	hour = "hour";
	public	static	final	String	minute = "minute";
	public	static	final	String	timeAMPM = "timeAMPM";
	public	static	final	String	startTime = "startTime";
	public	static	final	String	timeZone = "timeZone";
	public	static	final	String	timeStr = "timeStr";
	public	static	final	String	securityKey = "securityKey";
	
	public	static	final	String	lobby = "lobby";
	
	//	Network profile values are 1 - dial-up, 2 - cable-dsl and 3 - lan
	public	static	final	String	networkProfile = "networkProfile";
	
	//	Image quality values are low,medium and high
	public	static	final	String	imageQuality = "imageQuality";
	public	static	final	String	meetingTime = "meetingTime";
	public	static	final	String	meetingHours = "meetingHours";
	public	static	final	String	meetingMinutes = "meetingMinutes";
	public	static	final	String	maxParticipants = "maxParticipants";
	public	static	final	String	presenterAV = "presenterAV";
	public	static	final	String	maxAttendeeMikes = "maxAttendeeMikes";
	public	static	final	String	returnUrl = "returnUrl";
	
	public	static	final	String	privateChatEnabled = "privateChatEnabled";
	public	static	final	String	publicChatEnabled = "publicChatEnabled";
	public	static	final	String	screenShareEnabled = "screenShareEnabled";
	public	static	final	String	whiteboardEnabled = "whiteboardEnabled";
	
	public	static	final	String	TEXT_BOX	=	"TEXT_BOX";
	public	static	final	String	DATETIME	=	"DATETIME";
	public	static	final	String	TEXT_AREA	=	"TEXT_AREA";
	public	static	final	String	PASSWORD_TEXT_BOX	=	"PASSWORD_TEXT_BOX";
	public	static	final	String	CHECK_BOX	=	"CHECK_BOX";
	public	static	final	String	LIST_BOX	=	"LIST_BOX";
	
	public	static	String	getFormFieldLabel(String actionId, String fieldId)
	{
		if (fieldId.equals(FormGlobals.emailField))
		{
//			return	"Email: ";
		return	EnvGlobals.getDisplayString("email.label","Email:");

		}
		else if (fieldId.equals(FormGlobals.displayNameField))
		{
//			return	"Name: ";
		return	EnvGlobals.getDisplayString("displayname.label","Name:");
		}
		else if (fieldId.equals(FormGlobals.meetingNameField))
		{
//			return	"Meeting Name: ";
		return	EnvGlobals.getDisplayString("meetingname.label","Meeting Name:");
		}
		else if (fieldId.equals(FormGlobals.meetingKeyField))
		{
//			return	"Meeting Key: ";
		return	EnvGlobals.getDisplayString("meetingkey.label","Meeting Key: ");
		}
		else if (fieldId.equals(FormGlobals.asPresenterCheck))
		{
//			return	"Join As Presenter: ";
		return	EnvGlobals.getDisplayString("presenter.label","Join As Presenter");

		}
		else if (fieldId.equals(FormGlobals.startNow))
		{
//			return	"Start Now: ";
		return	EnvGlobals.getDisplayString("startnow.label" , "Start Now:");
		}
		else if (fieldId.equals(FormGlobals.attendees))
		{
//			return	"Attendees: ";
			return	EnvGlobals.getDisplayString("attendees.label","Attendees:");

		}
		else if (fieldId.equals(FormGlobals.presenters))
		{
//			return	"Presenters: ";
			return	EnvGlobals.getDisplayString("presenters.label","Presenters:");
		}
		else if (fieldId.equals(FormGlobals.hour))
		{
//			return	"Hour: ";
			return	EnvGlobals.getDisplayString("hour.label","Hour:");
		}
		else if (fieldId.equals(FormGlobals.minute))
		{
//			return	"Minute: ";
			return	EnvGlobals.getDisplayString("min.label","Minute:");
		}
		else if (fieldId.equals(FormGlobals.timeAMPM))
		{
//			return	"AM/PM: ";
			return	EnvGlobals.getDisplayString("ampm.label","AM/PM:");
		}
		else if (fieldId.equals(FormGlobals.startTime))
		{
//			return	"Start Time: ";
			return	EnvGlobals.getDisplayString("starttime.label","Start Time:");
		}
		else if (fieldId.equals(FormGlobals.timeStr))
		{
//			return	"Start Date: ";
			return	EnvGlobals.getDisplayString("startdate.label","Start Date:");
		}
		else if (fieldId.equals(FormGlobals.timeZone))
		{
//			return	"Time Zone: ";
			return	EnvGlobals.getDisplayString("timezone.label","Time Zone:");
		}
		else if (fieldId.equals(FormGlobals.lobby))
		{
//			return	"Enable Lobby: ";
			return	EnvGlobals.getDisplayString("enablelobby.label","Enable Waiting Area:");
		}
		else if (fieldId.equals(FormGlobals.networkProfile))
		{
//			return	"Video Quality: ";
			return	EnvGlobals.getDisplayString("videoquality.label","Video Quality:");
		}
		else if (fieldId.equals(FormGlobals.imageQuality))
		{
//			return	"Image Quality: ";
			return	EnvGlobals.getDisplayString("imagequality.label","Image Quality:");
		}
		else if (fieldId.equals(FormGlobals.meetingTime))
		{
//			return	"Meeting Length: ";
			return	EnvGlobals.getDisplayString("meetinglen.label","Meeting Length:");
		}
		else if (fieldId.equals(FormGlobals.maxParticipants))
		{
//			return	"Maximum Participants: ";
			return	EnvGlobals.getDisplayString("maxparticipants.label","Maximum Participants: ");
		}
		else if (fieldId.equals(FormGlobals.presenterAV))
		{
//			return	"Maximum Participants: ";
			return	EnvGlobals.getDisplayString("presenter_av.label","Presenter Audio Video: ");
		}
		else if (fieldId.equals(FormGlobals.privateChatEnabled))
		{
//			return	"Maximum Participants: ";
			return	EnvGlobals.getDisplayString("private_chat_enabled.label","private chat: ");
		}
		else if (fieldId.equals(FormGlobals.publicChatEnabled))
		{
//			return	"Maximum Participants: ";
			return	EnvGlobals.getDisplayString("public_chat_enabled.label","public chat: ");
		}
		else if (fieldId.equals(FormGlobals.screenShareEnabled))
		{
//			return	"Maximum Participants: ";
			return	EnvGlobals.getDisplayString("screen_share_enabled.label","screen share: ");
		}
		else if (fieldId.equals(FormGlobals.whiteboardEnabled))
		{
//			return	"Maximum Participants: ";
			return	EnvGlobals.getDisplayString("whiteboard_enabled.label","whiteboard: ");
		}
		else if (fieldId.equals(FormGlobals.maxAttendeeMikes))
		{
//			return	"Maximum Participants: ";
			return	EnvGlobals.getDisplayString("max_attendee_mikes.label","Attendee Mikes: ");
		}
		else if (fieldId.equals(FormGlobals.returnUrl))
		{
//			return	"Maximum Participants: ";
			return	EnvGlobals.getDisplayString("return_url.label","Return Url: ");
		}
		else if (fieldId.equals(FormGlobals.securityKey))
		{
//			return	"Maximum Participants: ";
			return	EnvGlobals.getDisplayString("security_key.label","Security Key: ");
		}
		return	"";
	}
	public	static	String	getFormFieldType(String actionId, String fieldId)
	{
		if (fieldId.equals(FormGlobals.emailField))
		{
			return	FormGlobals.TEXT_BOX;
		}
		else if (fieldId.equals(FormGlobals.displayNameField))
		{
			return	FormGlobals.TEXT_BOX;
		}
		else if (fieldId.equals(FormGlobals.meetingNameField))
		{
			return	FormGlobals.TEXT_BOX;
		}
		else if (fieldId.equals(FormGlobals.meetingKeyField))
		{
			return	FormGlobals.TEXT_BOX;
		}
		else if (fieldId.equals(FormGlobals.asPresenterCheck))
		{
			return	FormGlobals.CHECK_BOX;
		}
		else if (fieldId.equals(FormGlobals.startNow))
		{
			return	FormGlobals.CHECK_BOX;
		}
		else if (fieldId.equals(FormGlobals.attendees))
		{
			return	FormGlobals.TEXT_AREA;
		}
		else if (fieldId.equals(FormGlobals.presenters))
		{
			return	FormGlobals.TEXT_AREA;
		}
		else if (fieldId.equals(FormGlobals.hour))
		{
			return	FormGlobals.LIST_BOX;
		}
		else if (fieldId.equals(FormGlobals.minute))
		{
			return	FormGlobals.LIST_BOX;
		}
		else if (fieldId.equals(FormGlobals.timeAMPM))
		{
			return	FormGlobals.LIST_BOX;
		}
		else if (fieldId.equals(FormGlobals.startTime))
		{
			return	FormGlobals.TEXT_BOX;
		}
		else if (fieldId.equals(FormGlobals.timeStr))
		{
			return	FormGlobals.TEXT_BOX;
		}
		else if (fieldId.equals(FormGlobals.timeZone))
		{
			return	FormGlobals.LIST_BOX;
		}
		else if (fieldId.equals(FormGlobals.lobby))
		{
			return	FormGlobals.LIST_BOX;
		}
		else if (fieldId.equals(FormGlobals.networkProfile))
		{
			return	FormGlobals.LIST_BOX;
		}
		else if (fieldId.equals(FormGlobals.imageQuality))
		{
			return	FormGlobals.LIST_BOX;
		}
		else if (fieldId.equals(FormGlobals.meetingTime))
		{
			return	FormGlobals.LIST_BOX;
		}
		else if (fieldId.equals(FormGlobals.meetingHours))
		{
			return	FormGlobals.LIST_BOX;
		}
		else if (fieldId.equals(FormGlobals.meetingMinutes))
		{
			return	FormGlobals.LIST_BOX;
		}
		else if (fieldId.equals(FormGlobals.maxParticipants))
		{
			return	FormGlobals.LIST_BOX;
		}
		else if (fieldId.equals(FormGlobals.maxAttendeeMikes))
		{
			return	FormGlobals.LIST_BOX;
		}
		else if (fieldId.equals(FormGlobals.presenterAV))
		{
			return	FormGlobals.LIST_BOX;
		}
		else if (fieldId.equals(FormGlobals.privateChatEnabled))
		{
			return	FormGlobals.LIST_BOX;
		}
		else if (fieldId.equals(FormGlobals.publicChatEnabled))
		{
			return	FormGlobals.LIST_BOX;
		}
		else if (fieldId.equals(FormGlobals.screenShareEnabled))
		{
			return	FormGlobals.LIST_BOX;
		}
		else if (fieldId.equals(FormGlobals.whiteboardEnabled))
		{
			return	FormGlobals.LIST_BOX;
		}
		else if (fieldId.equals(FormGlobals.returnUrl))
		{
			return	FormGlobals.TEXT_BOX;
		}
		else if (fieldId.equals(FormGlobals.securityKey))
		{
			return	FormGlobals.PASSWORD_TEXT_BOX;
		}
		return	"";
	}
	public	static	String	getHoursListSuffix()
	{
//		return	"hours";
		return	EnvGlobals.getDisplayString("hours.label","hours");
	}
	public	static	String	getMinutesListSuffix()
	{
//		return	"minutes";
		return	EnvGlobals.getDisplayString("minutes.label","minutes");
	}
	public	static	String	getActionSubmitButtonText(String actionId)
	{
		if (actionId.equals(EnvGlobals.ACTION_HOST_MEETING))
		{
//			return	"Start";
		return	EnvGlobals.getDisplayString("start.label","Start");
		}
//		else if (actionId.equals(EnvGlobals.ACTION_START_MEETING))
//		{
//			return	"Start";
//		return	EnvGlobals.getDisplayString("start.label","Start");
//		}
		else if (actionId.equals(EnvGlobals.ACTION_JOIN))
		{
//			return	"Join";
		return	EnvGlobals.getDisplayString("join.label ","Join");
		}
//		else if (actionId.equals(EnvGlobals.ACTION_MEET_NOW))
//		{
//			return	"Start";
//		return	EnvGlobals.getDisplayString("start.label","Start");
//		}
//		else if (actionId.equals(EnvGlobals.ACTION_SCHEDULE_MEETING))
//		{
//			return	"Schedule";
//		return	EnvGlobals.getDisplayString("schedule.label","Schedule");
//		}
//		else if (actionId.equals(EnvGlobals.ACTION_JOIN_ATTENDEE))
//		{
//			return	"Join";
//		return	EnvGlobals.getDisplayString("join.label ","Join");
//		}
//		else if (actionId.equals(EnvGlobals.ACTION_JOIN_PRESENTER))
//		{
//			return	"Join";
//		return	EnvGlobals.getDisplayString("join.label ","Join");
//		}
		return	"";
	}
	public	static	String	getFormFieldDefaultValue(String actionId, String fieldId)
	{
		return	"";
	}
	public	static	String	getEmailRequiredMesage()
	{
		return	EnvGlobals.getDisplayString("emailrequired.message","email required");
	}
	public	static	String	getValidEmailRequiredMessage()
	{
//		return	"valid email required";
		return	EnvGlobals.getDisplayString("validemailrequired.message","valid email required");
	}
	public	static	String	getValidEmailRequiredAttendeeMessage()
	{
//		return	"valid email required";
		return	EnvGlobals.getDisplayString("validemailrequired.message.attendee","valid email required for attendees");
	}
	public	static	String	getUserDisplayNameRequiredMessage()
	{
//		return	"user display name required";
		return	EnvGlobals.getDisplayString("displaynamerequired.message","user display name required");
	}
	public	static	String	getMeetingNameRequiredMessage()
	{
//		return	 "meeting name required";
		return	EnvGlobals.getDisplayString("meetingnamerequired.message","meeting name required");
	}
	public	static	String	getMeetingKeyRequiredMessage()
	{
//		return	"meeting key required";
		return	EnvGlobals.getDisplayString("meetingkeyrequired.message","meeting key required");
	}
	public	static	String	getMeetingDateRequiredMessage()
	{
//		return	"Meeting date is required";
		return	EnvGlobals.getDisplayString("meetingdaterequired.message","meeting date required");
	}
	public	static	String	getGeneralTabLabel()
	{
//		return	"General";
		return	EnvGlobals.getDisplayString("general.label","General");
	}
	public	static	String	getOptionsTabLabel()
	{
//		return	"Options";
		return	EnvGlobals.getDisplayString("options.label","Options");
	}
	public	static	String	getFeaturesTabLabel()
	{
//		return	"Options";
		return	EnvGlobals.getDisplayString("features.label","features");
	}
	public	static	String	getTimeAMLabel()
	{
//		return	"AM";
		return	EnvGlobals.getDisplayString("am.label","AM");
	}
	public	static	String	getTimePMLabel()
	{
//		return	"PM";
		return	EnvGlobals.getDisplayString("pm.label","PM");
	}
	public	static	String	getHighLabel()
	{
		return	EnvGlobals.getDisplayString("high.label","High");
	}
	public	static	String	getMediumLabel()
	{
		return	EnvGlobals.getDisplayString("medium.label","Medium");
	}
	public	static	String	getLowLabel()
	{
		return	EnvGlobals.getDisplayString("low.label","Low");
	}
	public	static	String	getDisableLabel()
	{
		return	EnvGlobals.getDisplayString("disable.label","Disable");
	}
	public	static	String	getEnableLabel()
	{
		return	EnvGlobals.getDisplayString("enable.label","Enable");
	}
	public	static	String	getFalseLabel()
	{
		return	EnvGlobals.getDisplayString("false.label","false");
	}
	public	static	String	getTrueLabel()
	{
		return	EnvGlobals.getDisplayString("true.label","true");
	}
	public	static	String	getNetworkOption1Label()
	{
		return	EnvGlobals.getDisplayString("network1.label","Dial-Up");
	}
	public	static	String	getNetworkOption2Label()
	{
		return	EnvGlobals.getDisplayString("network2.label","Cable/DSL");
	}
	public	static	String	getNetworkOption3Label()
	{
		return	EnvGlobals.getDisplayString("network3.label","LAN");
	}
	public	static	String	getAudioVideoOptionLabel()
	{
		return	EnvGlobals.getDisplayString("presenter_av.av","audio / video");
	}
	public	static	String	getAudioOnlyOptionLabel()
	{
		return	EnvGlobals.getDisplayString("presenter_av.audio","audio only");
	}
	public static String getAudioVideoDisabled()
	{
		return	EnvGlobals.getDisplayString("presenter_av.disabled","no audio-video");
	}
	
	public	static	String	getMeetingScheduledMessageHeader()
	{
//		return	"Meeting Scheduled";
		return	EnvGlobals.getDisplayString("meetingscheduled.label","Meeting Scheduled");
	}
	public	static	String	getMeetingScheduledMessagePart1()
	{
//		return	"Meeting is scheduled ";
		return	EnvGlobals.getDisplayString("meetingscheduled1.label","Meeting is scheduled");
	}
	public	static	String	getMeetingScheduledMessagePart2()
	{
//		return	". Email invitations have been dispatched to all participants. "+
//		"Please click on the link in the email 5 minutes before the meeting time to start the meeting.";
		return	EnvGlobals.getDisplayString("emailinvite.message",". Email invitations have been dispatched to all participants. Please click on the link in the email 5 minutes before the meeting time to start the meeting.");

	}
}
