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
 
package com.dimdim.conference.action.portal;

import	javax.servlet.http.HttpServletRequest;
import com.dimdim.conference.action.HttpAwareConferenceAction;
import com.dimdim.conference.application.core.ConferenceManager;
import com.dimdim.conference.application.core.NoConferenceByKeyException;
import com.dimdim.conference.application.portal.Message;
import com.dimdim.conference.application.portal.ServerResponse;
import com.dimdim.conference.application.presentation.PresentationManager;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 *	
 * The portal actions are for exclusive use of the portal server. This common
 * action provides a common result string, whose value depends on the each
 * specific action. 
 */

public abstract class PortalAdapterAction	extends	HttpAwareConferenceAction
{
	protected	String	result;
	protected	String	secure;
	protected	String	jsonBuffer;
	
	protected	String	meeting_id;
	
	public	PortalAdapterAction()
	{
	}
	/**
	 * The key in use check must be done by both check and start actions because
	 * theorotically speaking, the key could be used between the time of the two
	 * requests.
	 * 
	 * @return
	 */
	protected	boolean	isKeyInUse(String key)
	{
		boolean	b = true;
		try
		{
			//	Make sure the key does not intail references to other existing
			//	directories.
			System.out.println("Checking key validity:"+key);
			PresentationManager.validateKey(key);
			
			System.out.println("Checking key in use:"+key);
			//	Check for existing meetings.
			ConferenceManager confManager = ConferenceManager.getManager();
			System.out.println("Checking key in use conf manager:"+confManager);
			if (confManager.getConferenceIfValid(key) == null)
			{
				System.out.println("Key not in use:"+key);
				b = false;
			}
		}
		catch(NoConferenceByKeyException ncbke)
		{
			b = false;
		}
		catch(Exception e)
		{
			System.out.println("Error:"+e.getMessage());
			result = e.getMessage();
			b = true;
		}
		return	b;
	}
	protected	void	createResultJsonBuffer(boolean result, int code, String message)
	{
		ServerResponse sr = new ServerResponse(result,code,new Message(message));
		this.jsonBuffer = sr.toJson();
	}
	protected	boolean	isSecure()
	{
		return	this.secure != null && this.secure.equalsIgnoreCase("true");
	}
	public String getResult()
	{
		return result;
	}
	public void setResult(String result)
	{
		this.result = result;
	}
	public String getJsonBuffer()
	{
		return jsonBuffer;
	}
	public void setJsonBuffer(String jsonBuffer)
	{
		this.jsonBuffer = jsonBuffer;
	}
	public String getSecure()
	{
		return secure;
	}
	public void setSecure(String secure)
	{
		this.secure = secure;
	}
	public String getMeeting_id()
	{
		return meeting_id;
	}
	public void setMeeting_id(String meeting_id)
	{
		this.meeting_id = meeting_id;
	}
}
