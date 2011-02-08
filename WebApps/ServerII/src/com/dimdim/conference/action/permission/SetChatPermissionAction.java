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
 
package com.dimdim.conference.action.permission;

import java.util.StringTokenizer;
import java.util.Vector;

import com.dimdim.conference.action.ConferenceAction;
import com.dimdim.conference.model.IConference;
import com.dimdim.conference.model.IConferenceParticipant;
import com.dimdim.conference.model.IPermissionsManager;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * All the parameters are processed in strict order, hence if multiple are
 * specified, the last one processed will remain in effect.
 * 
 * 1.	enable all - if specified, everything else is ignored
 * 2.	disable all - if specified, the enable and disable lists are ignored
 * 3.	enable list - if specified, disable list is processed
 * 4.	disable list - if specified, which may contradict an entry in the
 * 			enable list. Caller is responsible for avoiding multiple
 * 			inclusions.
 * 		3 and 4 do not affect the permissions of other attendees not mentioned
 * 		in the list.
 */
public class SetChatPermissionAction	extends	ConferenceAction
{
	protected	String	enableAll;	//	true / null
	protected	String	disableAll;	//	true / null
	protected	String	enableList;	//	';' seperated list of attendee ids.
	protected	String	disableList;	//	';' seperated list of attendee ids.
	
	public	SetChatPermissionAction()
	{
	}
	public	String	doWork()	throws	Exception
	{
		String	ret = SUCCESS;
		IConference conf = this.userSession.getConference();
		IConferenceParticipant user = this.userSession.getUser();
		if (user.isPresenter())
		{
			IPermissionsManager ipm = conf.getParticipantPermissions();
			try
			{
				if (enableAll != null)
				{
					boolean b = Boolean.parseBoolean(enableAll);
					if (b)
					{
						System.out.println("Enabling chats permission for all attendees");
						ipm.enableSendChatMessageForAll();
					}
				}
				else if (disableAll != null)
				{
					boolean b = Boolean.parseBoolean(disableAll);
					if (b)
					{
						System.out.println("Disabling chats permission for all attendees");
						ipm.disableSendChatMessageForAll();
					}
				}
				else
				{
					if (enableList != null)
					{
						System.out.println("Enabling chats permission for: "+enableList);
						Vector v = this.parseStringToVector(enableList);
						ipm.enableSendChatMessageFor(v);
					}
					if (disableList != null)
					{
						System.out.println("Disabling chats permission for: "+disableList);
						Vector v = this.parseStringToVector(disableList);
						ipm.disableSendChatMessageFor(v);
					}
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
				ret = ERROR;
			}
		}
		return	ret;
	}
	protected	Vector	parseStringToVector(String str)
	{
		Vector v = new Vector();
		StringTokenizer parser = new StringTokenizer(str, ";");
		while (parser.hasMoreTokens())
		{
			v.add(parser.nextToken());
		}
		return	v;
	}
	public String getDisableAll()
	{
		return disableAll;
	}
	public void setDisableAll(String disableAll)
	{
		this.disableAll = disableAll;
	}
	public String getDisableList()
	{
		return disableList;
	}
	public void setDisableList(String disableList)
	{
		this.disableList = disableList;
	}
	public String getEnableAll()
	{
		return enableAll;
	}
	public void setEnableAll(String enableAll)
	{
		this.enableAll = enableAll;
	}
	public String getEnableList()
	{
		return enableList;
	}
	public void setEnableList(String enableList)
	{
		this.enableList = enableList;
	}
}
