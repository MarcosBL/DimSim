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

package com.dimdim.conference.application;

import	java.util.HashMap;
import	java.util.ResourceBundle;
import	java.util.Vector;
import	java.util.StringTokenizer;

import	com.dimdim.util.misc.StringGenerator;
import com.dimdim.conference.ConferenceConsoleConstants;
import	com.dimdim.conference.ConferenceConstants;

import	com.dimdim.conference.model.IConferenceParticipant;
import	com.dimdim.conference.model.Participant;
import com.dimdim.conference.application.core.UserNotAuthorizedToStartConference;
import com.dimdim.conference.db.ConferenceDB;
import com.dimdim.conference.db.ConferenceUser;
import com.dimdim.conference.db.ConferenceSpec;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * Main purpose of the user manager is to create new users as people
 * unregistered to this server are invited to conferences. This will be
 * done when new sessions are created in the server. If a login doesn't
 * exist in the wildfire database, it will be created only if its an
 * attempt to connect to a conference. If its a direct login attempt the
 * user must be in the db. We will differentiate between the registered
 * and automaticaly created users by their group memberships.
 */
public class UserManager
{
//	public static final String KEY_RTMP_PROTOCOL = "rtmp.protocol";
//	public static final String KEY_RTMP_SERVER = "rtmp.server";
//	public static final String KEY_RTMP_PORT = "rtmp.port";
//	public static final String KEY_RTMP_APPLICATION = "rtmp.application";
	
//	public static final String KEY_PRESENTATION_GLOBAL_NAME_LIST = "presentation.global.name.list";
//	public static final String KEY_PRESENTATION_GLOBAL_ID_LIST = "presentation.global.id.list";
//	public static final String KEY_PRESENTATION_STORAGE_ROOT = "presentation.storage.root";
	
	protected	static	UserManager	theManager	=	null;
	
	public	static	UserManager	getManager()
	{
		if (UserManager.theManager == null)
		{
			UserManager.createManager();
		}
		return	UserManager.theManager;
	}
	protected	synchronized	static	void	createManager()
	{
		if (UserManager.theManager == null)
		{
			UserManager.theManager = new UserManager();
		}
	}
	
//	protected	String	presentationStorageRoot;
//	protected	Vector	presentationGlobalNameList;
//	protected	Vector	presentationGlobalIdList;
	
//	protected	String	rtmpURL;
	
	protected	String	emailServer;
	protected	String	emailUser;
	protected	String	emailPassword;
	protected	String	emailSender;
	
	protected	StringGenerator		idGen = new StringGenerator();
	
	protected	UserManager()
	{
		//	Initialize the client manager
		try
		{
			ResourceBundle conferenceProps = ResourceBundle.getBundle("resources.dimdim");
//			String rtmpProtocol = conferenceProps.getString(KEY_RTMP_PROTOCOL);
//			String rtmpServerHost = conferenceProps.getString(KEY_RTMP_SERVER);
//			String rtmpPortStr = conferenceProps.getString(KEY_RTMP_PORT);
//			String rtmpApplication = conferenceProps.getString(KEY_RTMP_APPLICATION);
			
//			this.rtmpURL = rtmpProtocol+"://"+rtmpServerHost+":"+rtmpPortStr+"/"+rtmpApplication;
			
//			ResourceBundle conf = ResourceBundle.getBundle("resources.ConferenceSpace");
//			this.presentationStorageRoot = conferenceProps.getString(KEY_PRESENTATION_STORAGE_ROOT);
//			this.presentationStorageRoot = ConferenceConsoleConstants.getPresentationStorageRoot();
//			String presentationGlobalNameList_str = conferenceProps.getString(KEY_PRESENTATION_GLOBAL_NAME_LIST);
//			this.presentationGlobalNameList = parseCSVLine(presentationGlobalNameList_str);
//			String presentationGlobalIdList_str = conferenceProps.getString(KEY_PRESENTATION_GLOBAL_ID_LIST);
//			this.presentationGlobalIdList = parseCSVLine(presentationGlobalIdList_str);
			
//			ResourceBundle email = ResourceBundle.getBundle("resources.EmailServer");
			this.emailServer = conferenceProps.getString("email.server");
			this.emailUser = conferenceProps.getString("email.user");
			this.emailPassword = conferenceProps.getString("email.password");
			this.emailSender = conferenceProps.getString("email.sender");
		}
		catch(Exception e)
		{
			//	Any exception at this point is a show stopper.
			e.printStackTrace();
		}
	}
	public	void	authenticateOrganizer(String email, String securityKey)
		throws	UserNotAuthorizedToStartConference
	{
		this.authenticatePresenter(email, securityKey);
	}
	public	void	authenticatePresenter(String email, String securityKey)
		throws	UserNotAuthorizedToStartConference
	{
		String authPolicy = ConferenceConsoleConstants.getAuthenticationPolicy();
//		System.out.println("Authentication Policy is:"+authPolicy);
		if (authPolicy.equals(ConferenceConsoleConstants.CHECK_EMAIL))
		{
			ConferenceDB db = ConferenceDB.getDB();
			if (email == null || db.getConferenceUser(email.toLowerCase()) == null)
			{
				throw	new	UserNotAuthorizedToStartConference();
			}
		}
		else if (authPolicy.equals(ConferenceConsoleConstants.CHECK_KEY))
		{
			System.out.println("Security key must be: -"+ConferenceConsoleConstants.getSecurityKey()+"-");
			if (securityKey == null ||
					!securityKey.equals(ConferenceConsoleConstants.getSecurityKey()))
			{
				throw	new	UserNotAuthorizedToStartConference();
			}
		}
	}
	public	void	authenticateAttendee(String email, String securityKey)
		throws	UserNotAuthorizedToStartConference
	{
		String authPolicy = ConferenceConsoleConstants.getAuthenticationPolicy();
//		System.out.println("Authentication Policy is:"+authPolicy);
		if (authPolicy.equals(ConferenceConsoleConstants.CHECK_KEY))
		{
			if (securityKey == null ||
					!securityKey.equals(ConferenceConsoleConstants.getSecurityKey()))
			{
				throw	new	UserNotAuthorizedToStartConference();
			}
		}
	}
	/*
	public	Participant	createActivePresenter(String email, String displayName)
	{
		String	password = this.idGen.generateRandomString(7,7);
		Participant user = new Participant(email,
				displayName,password,ConferenceConstants.ROLE_ACTIVE_PRESENTER);
		
		return	user;
	}
	
	public	Participant	createAttendee(String email, String displayName)
	{
		String	password = this.idGen.generateRandomString(7,7);
		Participant user = new Participant(email,
				displayName,password,ConferenceConstants.ROLE_ATTENDEE);
		
		return	user;
	}
	*/
	protected	Vector	parseCSVLine(String line)
	{
		Vector v = new Vector();
		StringTokenizer parser = new StringTokenizer(line,",");
		while (parser.hasMoreTokens())
		{
			v.add(parser.nextToken());
		}
		return	v;
	}
//	public Vector getPresentationGlobalNameList()
//	{
//		return this.presentationGlobalNameList;
//	}
//	public Vector getPresentationGlobalIdList()
//	{
//		return this.presentationGlobalIdList;
//	}
//	public String getPresentationStorageRoot()
//	{
//		return this.presentationStorageRoot;
//	}
//	public String getRtmpURL()
//	{
//		return this.rtmpURL;
//	}
	public String getEmailPassword()
	{
		return emailPassword;
	}
	public String getEmailServer()
	{
		return emailServer;
	}
	public String getEmailUser()
	{
		return emailUser;
	}
	public String getEmailSender()
	{
		return emailSender;
	}
}
