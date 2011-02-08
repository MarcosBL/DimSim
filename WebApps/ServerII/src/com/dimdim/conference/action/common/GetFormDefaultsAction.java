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
 
package com.dimdim.conference.action.common;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import com.dimdim.conference.ConferenceConstants;
import com.dimdim.conference.ConferenceConsoleConstants;
import com.dimdim.conference.action.CommonDimDimAction;

import	com.dimdim.conference.model.FormDefaultValues;
import com.dimdim.util.misc.StringGenerator;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * As the name suggests, this action is intended to provide the ui with the
 * default values for a form. The forms are identified by the action id.
 * Supported action ids at present are 'start' and 'join'.
 * 
 * Default values for start form as as current, while join does not have any
 * at present. The action however is still provided so as to keep the functional
 * flow consistent and uniform.
 */
public class GetFormDefaultsAction	extends	CommonDimDimAction
{
	private	static	StringGenerator	idGen = new StringGenerator();
	
	protected	String		actionId;
	protected	String		jsonBuffer = "{a:\"b\"}";
	
	public	GetFormDefaultsAction()
	{
	}
	public	String	execute()	throws	Exception
	{
		String	ret = SUCCESS;
		FormDefaultValues fdv = new FormDefaultValues();
		if (this.actionId != null)
		{
			if (actionId.equalsIgnoreCase(ConferenceConstants.ACTION_HOST_MEETING))
			{
				String confKey = GetFormDefaultsAction.idGen.generateRandomString(7,7);
				DateFormat dateFormater = DateFormat.getDateTimeInstance(DateFormat.SHORT,
						DateFormat.SHORT,Locale.US);
				String confName = "Web Meeting "+dateFormater.format(new Date());
				
				fdv.setActionId(actionId);
				String email = ConferenceConsoleConstants.getResourceKeyValue("start_meeting_user_email","admin@dimdim.com");
				fdv.setEmail(email);
				String displayName = ConferenceConsoleConstants.getResourceKeyValue("start_meeting_user_name","Admin");
				fdv.setDisplayName(displayName);
				fdv.setConfKey(confKey);
				String confName2 = ConferenceConsoleConstants.getResourceKeyValue("start_meeting_meeting_name",confName);
				fdv.setConfName(confName2);
				String toll = ConferenceConsoleConstants.LocalDialin;
				fdv.setToll(toll);
				String tollFree = ConferenceConsoleConstants.LocalDialinTollFree;
				fdv.setTollFree(tollFree);
				String internToll = ConferenceConsoleConstants.InternationalDialin;
				fdv.setInternToll(internToll);
				String internTollFree = ConferenceConsoleConstants.InternationalDialinTollFree;
				fdv.setInternTollFree(internTollFree);
				String moderatorPassCode = generateModeratorPassCode(confKey);
				fdv.setModeratorPassCode(moderatorPassCode);
				String attendeePasscode = generateModeratorPassCode(confKey);
				fdv.setAttendeePasscode(attendeePasscode);
				String returnUrl = ConferenceConsoleConstants.getTrackbackURL();
				fdv.setReturnUrl(returnUrl);
			}
		}
		this.jsonBuffer = fdv.toJson();
		return	ret;
	}
	
	private String generateModeratorPassCode(String passcode){

		 //String userid = "as12##something";
		 String userid = passcode;
		 
		 String trimmedUserid = userid;
		 if(userid.length() > 6)
		 {
			 trimmedUserid=userid.substring(0, 6);
		 }
		 else if(userid.length() == 4)
		 {
			 trimmedUserid = userid+"00";
		 }
		 else if(userid.length() == 5)
		 {
			 trimmedUserid = userid+"0";
		 }
		
//		 System.out.println("userid is : " + userid);
//		 System.out.println("trimmedUserid is : " + trimmedUserid);
		 
		 String moderatorPasscode = "";
		 
		 trimmedUserid = trimmedUserid.toLowerCase();
		 
		 for(int i=0; i < 6; i++){
//			 System.out.println(trimmedUserid.charAt(i));
			 int ascii = (int)trimmedUserid.charAt(i);
//			 System.out.println("ascii value is : " + ascii);
			 if(ascii >= 97 && ascii <=99)
			 {
				 moderatorPasscode = moderatorPasscode+2;
			 }
			 else if(ascii >=100 && ascii <=102)
			 {
				 moderatorPasscode = moderatorPasscode+3;
			 }
			 else if(ascii >=103 && ascii <=105)
			 {
				 moderatorPasscode = moderatorPasscode+4;
			 }
			 else if(ascii >=106 && ascii <=108)
			 {
				 moderatorPasscode = moderatorPasscode+5;
			 }
			 else if(ascii >=109 && ascii <=111)
			 {
				 moderatorPasscode = moderatorPasscode+6;
			 }
			 else if(ascii >=112 && ascii <=114)
			 {
				 moderatorPasscode = moderatorPasscode+7;
			 }
			 else if(ascii >=115 && ascii <=117)
			 {
				 moderatorPasscode = moderatorPasscode+8;
			 }
			 else if(ascii >=118 && ascii <=120)
			 {
				 moderatorPasscode = moderatorPasscode+9;
			 }
			 else if(ascii >=121 && ascii <=123)
			 {
				 moderatorPasscode = moderatorPasscode+0;
			 }
			 else if(ascii >= 48 && ascii <= 57)
			 {
				 moderatorPasscode = moderatorPasscode+trimmedUserid.charAt(i);
			 }
			 else
			 {
				 moderatorPasscode = moderatorPasscode+0;
			 }
		 }
		 
		 System.out.println("moderator Passcode is : " + moderatorPasscode);
		 
	 
		return moderatorPasscode;
	}
	
	public String getActionId()
	{
		return actionId;
	}
	public void setActionId(String actionId)
	{
		this.actionId = actionId;
	}
	public String getJsonBuffer()
	{
		return this.jsonBuffer;
	}
	public void setJsonBuffer(String jsonBuffer)
	{
		this.jsonBuffer = jsonBuffer;
	}
}
