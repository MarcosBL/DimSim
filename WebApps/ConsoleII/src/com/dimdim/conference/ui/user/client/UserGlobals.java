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

import com.dimdim.conference.ui.common.client.UIConstants;
import com.dimdim.conference.ui.common.client.UIImages;
import com.dimdim.conference.ui.json.client.UIRosterEntry;
import com.dimdim.conference.ui.model.client.ConferenceGlobals;
import com.dimdim.conference.ui.model.client.UserMood;
import com.google.gwt.user.client.ui.Image;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * This is a simple helper class that is expected to provide simple methods
 * that are required by many widgets through out the user management widgets.
 */

public class UserGlobals
{
	protected	static	UserGlobals	globalConstants = null;
	
	public	static	UserGlobals	getUserGlobals()
	{
		if (UserGlobals.globalConstants == null)
		{
			UserGlobals.globalConstants = new UserGlobals();
		}
		return	UserGlobals.globalConstants;
	}
	
	protected	Vector		moods;
	protected	UserMood	customMood;
	protected	String		defaultPhotoUrl;
	
	public	static	final	String	WaitingInLobby	=	"waiting_in_lobby";
	public	static	final	String	Normal	=	"normal";
	public	static	final	String	Question =	"question";
	public	static	final	String	GoFaster =	"go_faster";
	public	static	final	String	GoSlower =	"go_slower";
	public	static	final	String	SpeakLouder =	"speak_louder";
	public	static	final	String	SpeakSofter =	"speak_softer";
	public	static	final	String	ThumbsUp =	"thumbs_up";
	public	static	final	String	ThumbsDown =	"thumbs_down";
	public	static	final	String	SteppedAway =	"stepped_away";
	
	public	UserGlobals()
	{
		this.moods = new Vector();
		
		//	Available Moods
		
		this.moods.addElement(new UserMood("normal",
				ConferenceGlobals.getDisplayString("attendee_mood.normal","Normal"),
				"mood-available-label", "mood-image"));
		this.moods.addElement(new UserMood("question",
				ConferenceGlobals.getDisplayString("attendee_mood.question","Question"),
				"mood-available-label", "mood-image"));
		this.moods.addElement(new UserMood("go_faster",
				ConferenceGlobals.getDisplayString("attendee_mood.go_faster","Go Faster"),
				"mood-available-label", "mood-image"));
		this.moods.addElement(new UserMood("go_slower",
				ConferenceGlobals.getDisplayString("attendee_mood.go_slower","Go Slower"),
				"mood-available-label",  "mood-image"));
		this.moods.addElement(new UserMood("speak_louder",
				ConferenceGlobals.getDisplayString("attendee_mood.speak_louder","Speak Louder"),
				"mood-available-label", "mood-image"));
		this.moods.addElement(new UserMood("speak_softer",
				ConferenceGlobals.getDisplayString("attendee_mood.speak_softer","Speak Softer"),
				"mood-available-label",  "mood-image"));
		this.moods.addElement(new UserMood("thumbs_up",
				ConferenceGlobals.getDisplayString("attendee_mood.thumbs_up","Thumbs Up"),
				"mood-available-label", "mood-image"));
		this.moods.addElement(new UserMood("thumbs_down",
				ConferenceGlobals.getDisplayString("attendee_mood.thumbs_down","Thumbs Down"),
				"mood-available-label", "mood-image"));
		this.moods.addElement(new UserMood("stepped_away",
				ConferenceGlobals.getDisplayString("attendee_mood.stepped_away","Stepped Away"),
				"mood-available-label",  "mood-image"));
		
		//	Custom Mood
		
		this.customMood = new UserMood("custom", "Custom message",
				"mood-unknown-label", "mood-image");
		
//		this.defaultPhotoUrl = "images/default_photo_1.jpg";
		this.defaultPhotoUrl = "../../presentations/global-meeting/default_photo_1.jpg";
	}
	public	String	getChangePictureComment1()
	{
		return	"Please select a jpg image of";
	}
	public	String	getChangePictureComment2()
	{
		return	"size 64 by 64 pixels.";
	}
	public Image getRoleImageUrl(String role)
	{
		Image imageUrl = null;
		if (role == null || role.equals(UIConstants.ROLE_ACTIVE_PRESENTER))
		{
			imageUrl = UIImages.getImageBundle(UIImages.defaultSkin).getPresenter();
		}
		else if (role.equals(UIConstants.ROLE_PRESENTER))
		{
			imageUrl = UIImages.getImageBundle(UIImages.defaultSkin).getPresenter();
		}
		else if (role.equals(UIConstants.ROLE_ATTENDEE))
		{
			imageUrl = UIImages.getImageBundle(UIImages.defaultSkin).getAttendee();
		}
		else
		{
			imageUrl = UIImages.getImageBundle(UIImages.defaultSkin).getAttendee();
		}
		return imageUrl;
	}
	public	Vector	getMoods()
	{
		return	this.moods;
	}
	public	UserMood	getCustomMood()
	{
		return	this.customMood;
	}
	public	String	getPhotoUrl(UIRosterEntry user)
	{
		String photoUrl = user.getPhotoUrl();
		if (this.isPhotoUrlDefault(photoUrl))
		{
			return	this.getDefaultPhotoUrl();
		}
		return	photoUrl;
	}
	public	boolean	isPhotoUrlDefault(String url)
	{
		if (url == null || url.length() == 0 || url.equals(this.defaultPhotoUrl))
		{
			return	true;
		}
		return	false;
	}
	public	String	getDefaultPhotoUrl()
	{
		return	this.defaultPhotoUrl;
	}
	public	int	getMaxVisibleParticipants()
	{
		return	1;
//		return	ConferenceGlobals.getLayoutParameterAsInt("participants_list.max_visible_length",5);
	}
	public	int	getMaxVisibleParticipantsInLobby()
	{
		return	ConferenceGlobals.getLayoutParameterAsInt("lobby_list.max_visible_length",5);
	}
	public	boolean	isMoodNormal(String mood)
	{
		boolean	b = false;
		if (mood.equals(UserGlobals.Normal))
		{
			b = true;
		}
		return	b;
	}
	public	boolean	isMoodStandard(String mood)
	{
		boolean	b = false;
		if (mood.equals(UserGlobals.WaitingInLobby) ||
			mood.equals(UserGlobals.Normal) ||
			mood.equals(UserGlobals.Question) ||
			mood.equals(UserGlobals.GoFaster) ||
			mood.equals(UserGlobals.GoSlower) ||
			mood.equals(UserGlobals.SpeakLouder) ||
			mood.equals(UserGlobals.SpeakSofter) ||
			mood.equals(UserGlobals.ThumbsUp) ||
			mood.equals(UserGlobals.ThumbsDown) ||
			mood.equals(UserGlobals.SteppedAway))
		{
			b = true;
		}
		return	b;
	}
	/**
	 * The argument string must be the value used by server to store the
	 * mood. Not the display string.
	 * 
	 * @param mood
	 * @return
	 */
	public	Image	getMoodImageUrl(String mood)
	{
		return	UIImages.getImageBundle(UIImages.defaultSkin).getNewMoodImageUrl(mood);
	}
	public	String	getMoodStyleName(String mood)
	{
		if (mood.equals(UserGlobals.WaitingInLobby))	{	return	"mood-available";	}
		else if (mood.equals(UserGlobals.Normal))	{	return	"mood-available";	}
		else if (mood.equals(UserGlobals.Question))	{	return	"mood-question";	}
		else if (mood.equals(UserGlobals.GoFaster))	{	return	"mood-go-fast";	}
		else if (mood.equals(UserGlobals.GoSlower))	{	return	"mood-go-slow";	}
		else if (mood.equals(UserGlobals.SpeakLouder))	{	return	"mood-speak-louder";	}
		else if (mood.equals(UserGlobals.SpeakSofter))	{	return	"mood-speak-softer";	}
		else if (mood.equals(UserGlobals.ThumbsUp))	{	return	"mood-thumbs-up";	}
		else if (mood.equals(UserGlobals.ThumbsDown))	{	return	"mood-thumbs-down";	}
		else if (mood.equals(UserGlobals.SteppedAway))	{	return	"mood-stepped-out";	}
		
		return	"mood-custom-message";
	}
	public	String	getPublisherAVSWFFileName()
	{
		return	"";
	}
	public	String	getAttendeeAVSWFFileName()
	{
		return	"";
	}
	/*public	String	getLobbyTaskHeading()
	{
//		return	"Waiting in lobby";
		return ConferenceGlobals.getDisplayString("makepresentor.activepresentorcomment16","Waiting in waiting area");	
	}*/
	public	String	getLobbyTaskMessage(String displayName)
	{
		return	displayName+" has arrived in lobby. Please click on accept link below to allow "+
			displayName+" entry into this meeting or click on deny in order to prevent entry into this meeting";
	}
	/*public	String	getQuestionTaskHeading()
	{
//		return	"Question";
		return ConferenceGlobals.getDisplayString("makepresentor.activepresentorcomment15","Question");
	}*/
	public	String	getQuestionTaskMessage(String displayName)
	{
		return	displayName+" wants to ask a question. Please answer it.";
	}
	/*public	String	getUserAVButtonEnableLabelText()
	{
		//return	"Share Stage";
		return ConferenceGlobals.getDisplayString("makepresentor.activepresentorcomment14","Share Stage");		
	}*/
	/*public	String	getUserAVButtonDisableLabelText()
	{
//		return	"Take Stage";
		return ConferenceGlobals.getDisplayString("makepresentor.activepresentorcomment13","Take Stage");		
	}*/
	public	String	getYouAreRemovedHeading()
	{
		//return	"Removed From Meeting";
		return ConferenceGlobals.getDisplayString("presenter.removed.attendee.heading","Removed From Meeting");		
	}
	public	String	getYouAreRemovedMessage()
	{
//		return	"You have been removed from this conference.";
		return ConferenceGlobals.getDisplayString("presenter.removed.attendee.message","You have been removed from this conference.");		
	}
	/*public	String	getMakePresenterConfirmationHeading()
	{
//		return	"Make Presenter";
		return ConferenceGlobals.getDisplayString("makepresentor.activepresentorcomment10","Make Presenter");		
	}*/
	/*public	String	getMakePresenterConfirmationMessage()
	{
//		return	"This will revoke the sharing priviledges from this presenter. Are you sure you want to proceed?";
		return ConferenceGlobals.getDisplayString("makepresentor.activepresentorcomment9","This will revoke the sharing priviledges from this presenter. Are you sure you want to proceed?");
	}*/
	/*public	String	getMakeActivePresenterConfirmationHeading()
	{
//		return	"Make Active Presenter";
		return ConferenceGlobals.getDisplayString("makepresentor.activepresentorcomment8","Make Active Presenter");		
	}*/
	/*public	String	getMakeActivePresenterRequirementHeading()
	{
//		return	"Sharing Active";
		return ConferenceGlobals.getDisplayString("makepresentor.activepresentorcomment7","Sharing Active");
	}*/
	/*public	String	getMakeActivePresenterRequirementMessage()
	{
//		return	"Current sharing and presenter audio-video broadcast must be stopped before changing the sharing control.";
		return ConferenceGlobals.getDisplayString("makepresentor.activepresentorcomment6","Current sharing and presenter audio-video broadcast must be stopped before changing the sharing control");		

	}*/
	/*public	String	getMakeActivePresenterConfirmationMessage()
	{
//		return	"This will grant the sharing priviledges to this presenter and remove them from you. Are you sure you want to proceed?";
		return ConferenceGlobals.getDisplayString("makepresentor.activepresentorcomment5","This will grant the sharing priviledges to this presenter and remove them from you. Are you sure you want to proceed?");		
		
	}*/
	/*public	String	getYouAreActivePresenterHeading()
	{
//		return	"Active Presenter";
		return ConferenceGlobals.getDisplayString("makepresentor.activepresentorcomment4","Active Presenter");	
	}*/
	/*public	String	getYouAreActivePresenterMessage()
	{
//		return	"You have been made active presenter by the organizer. You may now upload presentations and share your desktop with others participants.";
		return ConferenceGlobals.getDisplayString("makepresentor.activepresentorcomment3","You have been made active presenter by the organizer. You may now upload presentations and share your desktop with others participants.");	
	}*/
	
	/*public	String	getOnlyActivePresenterCanShareHeading()
	{
//		return	"Active Presenter Only";
		return ConferenceGlobals.getDisplayString("makepresentor.activepresentorcomment2","Active Presenter Only");	
	}*/
	/*public	String	getOnlyActivePresenterCanShareMessage()
	{
//		return	"Only the active presenter can upload presentations and share desktop. If you wish to do so please request the current active presenter to pass control to you.";
		return ConferenceGlobals.getDisplayString("makepresentor.activepresentorcomment1","Only the active presenter can upload presentations and share desktop. If you wish to do so please request the current active presenter to pass control to you.");	
	}*/
	public	String	getTimeWarningMessageHeading()
	{
		return ConferenceGlobals.getDisplayString("conference_time_warning_heading","Time Warning");
	}
	public	String	getTimeWarning1Message()
	{
		return ConferenceGlobals.getDisplayString("conference_time_warning_1","Time Warning 1");
	}
	public	String	getTimeWarning2Message()
	{
		return ConferenceGlobals.getDisplayString("conference_time_warning_2","Time Warning 2");
	}
	public	String	getTimeWarning3Message()
	{
		return ConferenceGlobals.getDisplayString("conference_time_warning_3","Time Warning 3");
	}
	public	String	getTimeWarning4Message()
	{
		return ConferenceGlobals.getDisplayString("conference_time_warning_4","Time Warning 4");
	}
	public	String	getTimeWarning5Message()
	{
		return ConferenceGlobals.getDisplayString("conference_time_warning_5","Time Warning 5");
	}
	public	String	getTimeExpiredMessage()
	{
		return ConferenceGlobals.getDisplayString("conference_time_expired","Time Expired");
	}
}

