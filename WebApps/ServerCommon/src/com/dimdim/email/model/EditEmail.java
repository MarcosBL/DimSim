package com.dimdim.email.model;

import java.util.List;
import java.util.Locale;

import com.dimdim.locale.ILocaleManager;

public class EditEmail extends JoinInvitationEmail{

	public EditEmail(ConferenceInfo conferenceInfo, List presenters, List attendees, String message, boolean scheduledConference, 
		Locale locale, ILocaleManager localeManager, String role) {
		super(conferenceInfo, presenters, attendees, message, scheduledConference,
				locale, localeManager, role);
		
		subject = conferenceInfo.getOrganizerUTF8();
		subject += " "+localeManager.getResourceKeyValue("email","ui_strings",this.locale,"subject.edit", role);
		
	}

	
}
