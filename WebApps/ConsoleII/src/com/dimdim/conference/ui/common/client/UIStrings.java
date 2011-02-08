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

package com.dimdim.conference.ui.common.client;

import com.dimdim.conference.ui.model.client.ConferenceGlobals;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 *
 * A simple class for dictionary access. Just makes it easier to maintain to
 * have the dictionary resource lookups done from 1 class.
 */

public class UIStrings
{
	public	static	String	getOKLabel()
	{
//		return	"OK";
 	    return ConferenceGlobals.getDisplayString("console.oklabel","OK");

	}
	public	static	String	getCancelLabel()
	{
//		return	"Cancel";
 	    return ConferenceGlobals.getDisplayString("console.cancellabel","Cancel");
	}
	public	static	String	getApplyLabel()
	{
//		return	"Apply";
 	    return ConferenceGlobals.getDisplayString("console.applylabel","Apply");
	}
	public	static	String	getCloseLabel()
	{
//		return	"Close";
 	    return ConferenceGlobals.getDisplayString("console.closelabel","Close");
	}
	public	static	String	getSendLabel()
	{
//		return	"Send";
 	    return ConferenceGlobals.getDisplayString("console.sendlabel","Send");
	}
	
	public	static	String	getMeetingInfoDialogHeader()
	{
//		return	"Dimdim Web Meeting Information";
 	    return ConferenceGlobals.getDisplayString("console.meetinginfo","Web Meeting Information");
	}

	public	static	String	getMeetingInfoSubjectLabel()
	{
//		return	"Subject";
 	    return ConferenceGlobals.getDisplayString("console.subjectlabel","Subject");

	}

	
	public	static	String	getTollInfoLabel()
	{
//		return	"Subject";
 	    return ConferenceGlobals.getDisplayString("console.tolllabel","Phone");

	}
	
	public	static	String	getTollFreeInfoLabel()
	{
//		return	"Subject";
 	    return ConferenceGlobals.getDisplayString("console.tollfreelabel","Local Dialin (Toll Free)");

	}

	
	public	static	String	getInternTollLabel()
	{
//		return	"Subject";
 	    return ConferenceGlobals.getDisplayString("console.internationaltolllabel","International Dialin");

	}

	
	public	static	String	getInternTollFreeLabel()
	{
//		return	"Subject";
 	    return ConferenceGlobals.getDisplayString("console.internationaltollfreelabel","International Dialin (Toll Free)");

	}

	
	public	static	String	getModeratorPasscodeLabel()
	{
//		return	"Subject";
 	    return ConferenceGlobals.getDisplayString("console.moderatorpasscodelabel","Moderator Passcode");

	}

	
	public	static	String	getAttendePasscodeLabel()
	{
//		return	"Subject";
 	    return ConferenceGlobals.getDisplayString("console.attendeepasscodelabel","Passcode");

	}

	
	
	public	static	String	getMeetingIdLabel()
	{
//		return	"Subject";
 	    return ConferenceGlobals.getDisplayString("console.meetingid.label","Room Name ");

	}
	
	public	static	String	getConfIdLabel()
	{
//		return	"Subject";
 	    return ConferenceGlobals.getDisplayString("console.confid.label","Conference ID :");

	}
	
	public	static	String	getDialInfoLabel()
	{
//		return	"Subject";
 	    return ConferenceGlobals.getDisplayString("console.dialinfo.label","Dail-in info :");

	}
	
	public	static	String	getMeetingInfoKeyLabel()
	{
//		return	"Key";
 	    return ConferenceGlobals.getDisplayString("console.keylabel","Room Name");
	}
	public	static	String	getMeetingInfoOrganizerLabel()
	{
//		return	"Organizer";
 	    return ConferenceGlobals.getDisplayString("console.organizerlabel","Organizer");
	}
	public	static	String	getMeetingInfoStartTimeLabel()
	{
//		return	"Start Time";
 	    return ConferenceGlobals.getDisplayString("console.starttimelabel","Start Time");
	}
	public	static	String	getMeetingInfoJoinURLLabel()
	{
//		return	"Join URL";
 	    return ConferenceGlobals.getDisplayString("console.joinurllabel","Join URL");
	}
	public	static	String	getFeedbackDialogHeader()
	{
//		return	"Send Feedback to Dimdim";
 	    return ConferenceGlobals.getDisplayString("console.feedbackhederlabel","Send Feedback to Web Meeting");
	}
	public	static	String	getInvitationsPreviewDialogHeader()
	{
//		return	"Dimdim Meeting Invitations";
 	    return ConferenceGlobals.getDisplayString("console.meetinginvitationlabel","Web Meeting Invitations");
	}
	public	static	String	getSendInvitationsLabel()
	{
//		return	"Send Invitations";
 	    return ConferenceGlobals.getDisplayString("console.meetinginvitationlabel1","Send Invitations");
	}
	public	static	String	getValidEmailRquired()
	{
//		return	"Invite: ";
 	    return ConferenceGlobals.getDisplayString("console.email.error","Please Enter valid email");
	}
	public	static	String	getEmailConfirm()
	{
//		return	"Invite: ";
 	    return ConferenceGlobals.getDisplayString("console.email.confirmation","The email invitation to the Attendee has been send.");
	}
	public	static	String	getInviteLabel()
	{
//		return	"Invite: ";
 	    return ConferenceGlobals.getDisplayString("console.invitelabel","Invite: ");
	}
	public	static	String	getSendInviteLabel()
	{
//		return	"Send Invite";
 	    return ConferenceGlobals.getDisplayString("console.sendinvitelabel","Send Invite");
	}
	public	static	String	getSendInviteLocalLabel()
	{
//		return	"Send Invite";
 	    return ConferenceGlobals.getDisplayString("console.sendinvitelabellocal","Send Invite");
	}
	public	static	String	getPreviewLabel()
	{
//		return	"Preview";
 	    return ConferenceGlobals.getDisplayString("console.previewlabel","Preview");
	}
	public	static	String	getMeetingSettingsDialogHeader()
	{
//		return	"Dimdim Meeting Settings";
 	    return ConferenceGlobals.getDisplayString("console.meetinsettinglabel","Web Meeting Settings");
	}
	public	static	String	getSelectLabel()
	{
//		return	"Select ";
 	    return ConferenceGlobals.getDisplayString("console.selectlabel","Select");
	}
	public	static	String	getEnterURLLabel()
	{
//		return	"Enter url:";
 	    return ConferenceGlobals.getDisplayString("console.urllabel","Enter url:");
	}
	public	static	String	getEnterURLTitle()
	{
//		return	"Enter url:";
 	    return ConferenceGlobals.getDisplayString("console.urltitle","Enter URL");
	}
	public	static	String	getShareLabel()
	{
//		return	"Share";
 	    return ConferenceGlobals.getDisplayString("console.share.button.label","Share");
	}
	public	static	String	getResourceControlDialogHeader()
	{
//		return	"Manage Show Items";
 	    return ConferenceGlobals.getDisplayString("console.showitemlabel","Manage Show Items");
	}
	public	static	String	getDeleteLabel()
	{
//		return	"Delete";
 	    return ConferenceGlobals.getDisplayString("console.deletelabel","Delete");
	}
	public	static	String	getNameLabel()
	{
//		return	"Name";
 	    return ConferenceGlobals.getDisplayString("console.namelabel","Name");
	}
	public	static	String	getTypeLabel()
	{
//		return	"Type";
 	    return ConferenceGlobals.getDisplayString("console.typelabel","Type");
	}
	public	static	String	getDeleteResourceDialogHeader()
	{
//		return	"Delete Item";
 	    return ConferenceGlobals.getDisplayString("console.deleteitemlabel","Delete Item");
	}
	public	static	String	getRenameLabel()
	{
//		return	"Rename";
 	    return ConferenceGlobals.getDisplayString("console.renamelabel","Rename");

	}
	public	static	String	getRenameResourceDialogHeader()
	{
//		return	"Rename Item";
 	    return ConferenceGlobals.getDisplayString("console.renameitemlabel","Rename Item");
	}
	public	static	String	getConfirmDesktopShareDialogHeader()
	{
//		return	"Confirm Desktop Sharing";
 	    return ConferenceGlobals.getDisplayString("console.confirmdtpsharelabel","Confirm Desktop Sharing");
	}
	public	static	String	getConfirmDesktopShareDialogComment1()
	{
//		return	"This will share your desktop with";
 	    return ConferenceGlobals.getDisplayString("console.confirmdtpsharelabel1","This will share your desktop with");
	}
	public	static	String	getConfirmDesktopShareDialogComment2()
	{
//		return	"attendees. Please press ok to confirm";
 	    return ConferenceGlobals.getDisplayString("console.confirmdtpsharelabel2","attendees. Please press ok to confirm");
	}
	public	static	String	getUseExistingLabel()
	{
//		return	"Use Existing ";
 	    return ConferenceGlobals.getDisplayString("console.userexistinglabel","Use Existing ");
	}
	public	static	String	getCreateNewLabel()
	{
//		return	"Create New ";
 	    return ConferenceGlobals.getDisplayString("console.createnewlabel","Create New ");
	}
	public	static	String	getSupportedFileTypesComment()
	{
//		return	"Supported file types: ";
 	    return ConferenceGlobals.getDisplayString("console.filetypelabel","Supported file types:");
	}
	public	static	String	getFileNameRequiredMessage()
	{
//		return	"File name is required";
 	    return ConferenceGlobals.getDisplayString("console.filenamelabel","File name is required");
	}
	public	static	String	getEmailLabel()
	{
//		return	"Email";
 	    return ConferenceGlobals.getDisplayString("console.emaillabel","Email");
	}
	public	static	String	getAcceptLabel()
	{
//		return	"Accept";
 	    return ConferenceGlobals.getDisplayString("console.acceptlabel","Accept");
	}
	public	static	String	getDenyLabel()
	{
//		return	"Deny";
 	    return ConferenceGlobals.getDisplayString("console.denylabel","Deny");
	}
	public	static	String	getResolveLabel()
	{
//		return	"Resolve";
 	    return ConferenceGlobals.getDisplayString("console.resolvelabel","Remove");
	}
	public	static	String	getQuestionsLabel()
	{
//		return	"Questions";
 	    return ConferenceGlobals.getDisplayString("console.questionslabel","Questions");
	}
	public	static	String	getChangePictureDialogHeader()
	{
//		return	"Change Picture";
 	    return ConferenceGlobals.getDisplayString("console.changepicturelabel","Change Picture");
	}
	public	static	String	getUseDefaultPhotoLabel()
	{
//		return	"Use default photo";
 	    return ConferenceGlobals.getDisplayString("console.defaultphotolabel","Use default photo");
	}
	public	static	String	getChangePhotoLabel()
	{
//		return	"Change Photo";
 	    return ConferenceGlobals.getDisplayString("console.changephotolabel","Change Photo");
	}
	public	static	String	getChatPanelInstruction()
	{
//		return	"Type text and press return to send message";
 	    return ConferenceGlobals.getDisplayString("console.typemessagelabel","Type text and press Enter.");
	}
	public	static	String	getChatPanelEmoticonInstruction()
	{
//		return	"Type text and press return to send message";
 	    return ConferenceGlobals.getDisplayString("console.emoticonlabel","Emoticon");
	}
	public	static	String	getChatPermissionRevokedMessage1()
	{
//		return	"Your chat permission has been revoked";
 	    return ConferenceGlobals.getDisplayString("console.chatpermissionrevokelabel","Your chat permission has been revoked");
	}
	public	static	String	getChatPermissionRovokedMessage2()
	{
//		return	"Chat permission has been revoked from ";
 	    return ConferenceGlobals.getDisplayString("console.chatpermissionrevokelabel1","Chat permission has been revoked from ");
	}
	public	static	String	getChatPermissionGrantedMessage1()
	{
//		return	"Chat permission has been granted";
 	    return ConferenceGlobals.getDisplayString("console.chatpermissionlabel","Chat permission has been granted");
	}
	public	static	String	getChatPermissionGrantedMessage2()
	{
//		return	"Chat permission has been granted to ";
 	    return ConferenceGlobals.getDisplayString("console.chatpermissionlabel1","Chat permission has been granted to ");
	}
	public	static	String	getPermissionsControlDialogHeader()
	{
//		return	"Manage Attendee Permissions";
 	    return ConferenceGlobals.getDisplayString("console.manageattendeepermissionlabel","Manage Attendee Permissions");
	}
	public	static	String	getChatLabel()
	{
//		return	"Chat";
 	    return ConferenceGlobals.getDisplayString("console.chatlabel","Chat");
	}
	public	static	String	getAudioLabel()
	{
//		return	"Audio";
	    return ConferenceGlobals.getDisplayString("console.audiolabel","Audio");
	}
	public	static	String	getVideoLabel()
	{
		   return ConferenceGlobals.getDisplayString("console.videolabel","Video");	
	}
	public	static	String	getNoParticipantsMessage()
	{
//		return	"There are no participants";
 	    return ConferenceGlobals.getDisplayString("console.noparticipantlabel","There are no participants");

	}
	public	static	String	getPermissionsLabel()
	{
//		return	"Permissions";

 	    return ConferenceGlobals.getDisplayString("console.permissionslabel","Permissions");
	}
	public	static	String	getChangePictureLabel()
	{
//		return	"Change Picture";
 	    return ConferenceGlobals.getDisplayString("console.changepicturelabel","Change Picture");
	}
	public	static	String	getRemoveLabel()
	{
//		return	"Remove";
 	    return ConferenceGlobals.getDisplayString("console.removelabel","Remove");
	}
	public	static	String	getParticipantsLabel()
	{
//		return	"Participants";

 	    return ConferenceGlobals.getDisplayString("console.participantslabel","Participants");
	}
	public	static	String	getShowItemsLabel()
	{
//		return	"Show Items";
 	    return ConferenceGlobals.getDisplayString("console.showitemslabel","Show Items");
	}
	public	static	String	getPendingTasksLabel()
	{
//		return	"Pending Tasks";
 	    return ConferenceGlobals.getDisplayString("console.pendingtaskslabel","Pending Tasks");
	}
	public	static	String	getAboutDialogHeader()
	{
//		return	"About Dimdim Web Meeting";
 	    return ConferenceGlobals.getDisplayString("console.aboutlabel","About Web Meeting");
	}
	public	static	String	getDimdimWebMeetingLabel()
	{
//		return	"Dimdim Web Meeting";
 	    return ConferenceGlobals.getDisplayString("console.dimdimwebmeetinglabel","Web Meeting");
	}
	public	static	String	getThankyouLabel()
	{
//		return	"Dimdim Web Meeting";
 	    return ConferenceGlobals.getDisplayString("conference_closed_message.header","Thank You");
	}
	public	static	String	getConnectionLostMessage()
	{
//		return	"Console has lost connection to the server. Please connect again";
 	    return ConferenceGlobals.getDisplayString("console.lostconnectionlabel","Console has lost connection to the server. Please connect again");
	}
	public	static	String	getSettingsLabel()
	{
//		return	"Settings";
	    return ConferenceGlobals.getDisplayString("console.settingslabel","Settings");
	}
	public	static	String	getMeetingInfoLabel()
	{
//		return	"Meeting Info";
	    return ConferenceGlobals.getDisplayString("console.meetinginfolabel","Meeting Info");
	}
	public	static	String	getAboutLabel()
	{
//		return	"About";
	    return ConferenceGlobals.getDisplayString("console.aboutlabel","About");
	}
	public	static	String	getAboutLogo()
	{
	    return ConferenceGlobals.getDisplayString("console.aboutlogo","images/dimdim_default_logotype.png");
	}
	public	static	String	getAboutPoweredLogo()
	{
	    return ConferenceGlobals.getDisplayString("console.aboutpoweredlogo","images/dimdim_bubles.png");
	}
	public	static	String	getFeedbackLabel()
	{
//		return	"Feedback";
	    return ConferenceGlobals.getDisplayString("console.feedbacklabel","Feedback");
	}
	public	static	String	getPresenterSignOutLabel()
	{
//		return	"Sign Out";
	    return ConferenceGlobals.getDisplayString("console.signoutlabel.presenter","Sign Out");
	}
	public	static	String	getAttendeeSignOutLabel()
	{
//		return	"Sign Out";
	    return ConferenceGlobals.getDisplayString("console.signoutlabel.attendee","Sign Out");
	}
	public	static	String	getMeetingLobbyLabel()
	{
//		return	"Meeting Lobby";
	    return ConferenceGlobals.getDisplayString("console.meetinglobbylabel","Meeting Waiting Area");
	}
	public	static	String	getLoadingConsoleMessage()
	{
//		return	"Loading Console";
	    return ConferenceGlobals.getDisplayString("console.loadingconsolelabel","Loading Console");
	}
	public	static	String	getSharingAlreadyActiveMessage()
	{
//		return	"Sharing is already active.";
	    return ConferenceGlobals.getDisplayString("console.sharingactivelabel","Sharing is already active.");
	}
	public	static	String	getStopSharingMessage()
	{
//		return	"Please stop the running sharing.";
	    return ConferenceGlobals.getDisplayString("console.stopsharinglabel","Please stop the running sharing.");
	}
	public	static	String	getSharingAlreadyActiveDialogHeader()
	{
//		return	"Sharing Already Active";
	    return ConferenceGlobals.getDisplayString("console.sharingactivelabel","Sharing Already Active");
	}
	public	static	String	getPopoutWorkspaceLinkText()
	{
		return	ConferenceGlobals.getDisplayString("workspace.popout_link_text","Full Screen");
	}
	public	static	String	getPopinWorkspaceLinkText()
	{
		return	ConferenceGlobals.getDisplayString("workspace.popin_link_text","Exit Full Screen");
	}
	public	static	String	getInviteLinkLabel()
	{
		return	ConferenceGlobals.getDisplayString("console.invite_link_text","Invite");
	}
	
	public	static	String	getMoodToolTip(String mood)
	{
		if (mood.equals(UserGlobals.Normal))	{	return	ConferenceGlobals.getDisplayString("attendee_mood.normal","Normal");	}
		else if (mood.equals(UserGlobals.Agree))	{	return	ConferenceGlobals.getDisplayString("attendee_mood.agree","Agree");	}
		else if (mood.equals(UserGlobals.Disagree))	{	return	ConferenceGlobals.getDisplayString("attendee_mood.disagree","Disagree");	}
		else if (mood.equals(UserGlobals.BeRightBack))	{	return	ConferenceGlobals.getDisplayString("attendee_mood.be_right_back","Be Right Back");	}
		else if (mood.equals(UserGlobals.Busy))	{		return	ConferenceGlobals.getDisplayString("attendee_mood.busy","Busy");	}
		else if (mood.equals(UserGlobals.Problem))	{	return	ConferenceGlobals.getDisplayString("attendee_mood.problem","Problem");	}
		else if (mood.equals(UserGlobals.Question))	{	return	ConferenceGlobals.getDisplayString("attendee_mood.question","Question");	}
		
		return	null;
	}
}
