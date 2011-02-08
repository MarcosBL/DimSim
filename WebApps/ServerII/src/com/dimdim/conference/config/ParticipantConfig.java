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
/*
 **************************************************************************
 *	File Name  : ConferenceParticipantConfig.java
 *  Created On : Apr 11, 2006
 *  Created By : Saurav Mohapatra
 **************************************************************************
*/
 
package com.dimdim.conference.config;

import com.dimdim.conference.ConferenceConstants;

/**
 * @author Saurav Mohapatra
 * @email  Saurav.Mohapatra@communiva.com
 * ************** MUST NOT BE REQUIRED, REDUNDANT *******
 */
public class ParticipantConfig
{
	private String clientId = null;
	private String emailId = null;
	private String conferencePassword = null;
	private String displayName = "";
	private String role = null;
	private String jabberIdName = null;
	private String xmppPassword = null;
	private String defaultJID = null;
	
	/**
	 * 
	 */
	public ParticipantConfig()
	{
		super();
	}

	/**
	 * @return Returns the displayName.
	 */
	public String getDisplayName()
	{
		return displayName;
	}

	/**
	 * @param displayName The displayName to set.
	 */
	public void setDisplayName(String displayName)
	{
		this.displayName = displayName;
	}

	/**
	 * @return Returns the emailId.
	 */
	public String getEmailId()
	{
		return emailId;
	}

	/**
	 * @param emailId The emailId to set.
	 */
	public void setEmailId(String emailId)
	{
		this.emailId = emailId;
	}

	/**
	 * @return Returns the jabberIdName.
	 */
	public String getJabberIdName()
	{
		return jabberIdName;
	}

	/**
	 * @param jabberIdName The jabberIdName to set.
	 */
	public void setJabberIdName(String jabberIdName)
	{
		this.jabberIdName = jabberIdName;
	}

	/**
	 * @return Returns the role.
	 */
	public String getRole()
	{
		return role;
	}
	/**
	 * @param role The role to set.
	 */
	public void setRole(String role)
	{
		this.role = role;
	}
	
	public boolean isPresenter()
	{
		return role != null && (role.equals(ConferenceConstants.ROLE_PRESENTER)
				|| role.equals(ConferenceConstants.ROLE_ACTIVE_PRESENTER));
	}
	
	public boolean isAttendee()
	{
		return role != null && role.equals(ConferenceConstants.ROLE_ATTENDEE);
	}
	
	public boolean isAutomaton()
	{
		return role != null && role.equals(ConferenceConstants.ROLE_AUTOMATON);
	}

	/**
	 * @return Returns the xmppPassword.
	 */
	public String getXmppPassword()
	{
		return xmppPassword;
	}

	/**
	 * @param xmppPassword The xmppPassword to set.
	 */
	public void setXmppPassword(String xmppPassword)
	{
		this.xmppPassword = xmppPassword;
	}

	public String getConferencePassword()
	{
		return conferencePassword;
	}

	public void setConferencePassword(String conferencePassword)
	{
		this.conferencePassword = conferencePassword;
	}

	public String getDefaultJID()
	{
		return defaultJID;
	}

	public void setDefaultJID(String defaultJID)
	{
		this.defaultJID = defaultJID;
	}

	public String getClientId()
	{
		return clientId;
	}

	public void setClientId(String clientId)
	{
		this.clientId = clientId;
	}

}
