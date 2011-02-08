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

import com.dimdim.conference.ui.common.client.UIGlobals;
import com.dimdim.conference.ui.dialogues.client.InvitationPreviewDialog;
import com.dimdim.conference.ui.json.client.UIRosterEntry;
import com.dimdim.conference.ui.model.client.AnalyticsConstants;
import com.dimdim.conference.ui.model.client.ClientModel;
import com.dimdim.conference.ui.model.client.ConferenceGlobals;
import com.dimdim.conference.ui.model.client.UIResources;
import com.google.gwt.user.client.Window;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */

public class InvitationsManager implements IInvitationManager
{
	
	protected	UserRosterManager	userManager;
	
	public	InvitationsManager(UserRosterManager userManager)
	{
		this.userManager = userManager;
	}
	public	void	displayPreviewPanel(String presenterEmails,
			String attendeeEmails)
	{
		InvitationPreviewDialog dlg = new InvitationPreviewDialog(this,presenterEmails, attendeeEmails);
		dlg.drawDialog();
		/*
		UIGlobals.theDialogBox = new InvitationPreviewDialog(this,presenterEmails, attendeeEmails);
		UIGlobals.theDialogBox.setPopupPosition(UIGlobals.getCommonDialogBoxPopupPositionX(),
				UIGlobals.getCommonDialogBoxPopupPositionY());
		UIGlobals.theDialogBox.show();
		*/
	}
	
	public	void	sendInvitations(String presenterEmails,
				String attendeeEmails, String message)
	{
		AnalyticsConstants.reportInvitation();
		this.userManager.sendJoinInvitations(attendeeEmails, presenterEmails, message);
	}
	
	public	void	sendInvitationsLocal(String presenterEmails,
		String attendeeEmails, String message)
	{
	    
	    UIRosterEntry curUser = ClientModel.getClientModel().getRosterModel().getCurrentUser();
	    UIResources  uiResources = UIResources.getUIResources();
	    String	joinURL = uiResources.getConferenceInfo("joinURL");
//	    Window.alert("inside sned local joinURL = "+joinURL);
	    
	    String 	toll = uiResources.getConferenceInfo("toll");
	    String 	tollFree = uiResources.getConferenceInfo("tollFree");
	    String 	internTollFree = uiResources.getConferenceInfo("intl_tollfree");
	    String 	internToll = uiResources.getConferenceInfo("intl_toll");
	    String 	moderatorPassCode = uiResources.getConferenceInfo("mod_pass_code");
	    String 	attendeePasscode = uiResources.getConferenceInfo("att_pass_code");
	    String 	attendeePwd = uiResources.getConferenceInfo("attendee_pass_key");
	    
	    if (UIGlobals.isActivePresenter(curUser))
	    {
        	String emailBody = ConferenceGlobals.getDisplayString("ui_email_instant_body", "Hello! %0a%0a A meeting has started, Please click the link LINK to join the meeting %0a%0a%0a Details of the meeting are %0a Agenda: AGENDA %0a ");
        	
        	emailBody = emailBody.replaceAll("LINK", joinURL);
        	if(attendeePwd.length()>0)
        	{
        		emailBody = emailBody.replaceAll("MEETING_KEY", attendeePwd);
        	}
        	else
        	{
        		emailBody = emailBody.replaceAll("MEETING_KEY", "");
        	}
        	if(tollFree.length()>0)
        	{
        		emailBody = emailBody.replaceAll("LOCAL_DIAL_IN_TOLL_FREE", tollFree);
        	}
        	else
        		emailBody = emailBody.replaceAll("LOCAL_DIAL_IN_TOLL_FREE", "");
        	if(toll.length()>0)
        	{
        		emailBody = emailBody.replaceAll("LOCAL_DIAL_IN", toll);
        	}
        	else
        	{
        		emailBody = emailBody.replaceAll("LOCAL_DIAL_IN", "");
        	}
        	if(internTollFree.length()>0)
        	{
        		emailBody = emailBody.replaceAll("INT_DIAL_IN_TOLL_FREE", internTollFree);
        	}
        	else
        	{
        		emailBody = emailBody.replaceAll("INT_DIAL_IN_TOLL_FREE", "");
        	}
        	if(internToll.length()>0)
        	{
        		emailBody = emailBody.replaceAll("INT_DIAL_IN", internToll);
        	}
        	else
        	{
        		emailBody = emailBody.replaceAll("INT_DIAL_IN", "");
        	}
        	
        	if(attendeePasscode.length()>0)
        	{
        		emailBody = emailBody.replaceAll("ATT_PASS_CODE", attendeePasscode);
        	}
        	else
        	{
        		emailBody = emailBody.replaceAll("ATT_PASS_CODE", "");
        	}        	
        	        	
        	emailBody = emailBody.replaceAll("AGENDA", uiResources.getConferenceInfoAndDecode64("subject"));
        	emailBody = emailBody.replaceAll("USER", curUser.getDisplayName());
        	emailBody = emailBody.replaceAll("MESSAGE", message);
        	
        	emailBody = emailBody.replaceAll("&", "%26");
        	
//        	Window.alert("inside sned emailBody " + emailBody);
        	
        	String mailUrl = "mailto:"+attendeeEmails+"?subject="+ curUser.getDisplayName()+" "+ConferenceGlobals.getDisplayString("ui_email_instant_subject"," has invited you to a Dimdim web meeting")
        	+" " + ConferenceGlobals.conferenceKey+""
        		+"&body="+emailBody;
        	//Window.alert("mail url = "+mailUrl);
        	setLocation(mailUrl);
	    }
	}
	
	public	void	sendFeedback(int rating, String comment, String toEmail)
	{
		this.userManager.sendFeedback(rating,comment, toEmail);
	}
	
	private native void setLocation(String url) /*-{
		//alert('url to set = '+url);
  		$wnd.open(url);
	}-*/;
	

}
