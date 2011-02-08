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
 
package com.dimdim.conference.action.chat;

//import java.text.DateFormat;
//import java.text.SimpleDateFormat;
//import java.util.Date;

import com.dimdim.conference.action.HttpAwareConferenceAction;
import com.dimdim.conference.model.IConference;
import com.dimdim.conference.model.IConferenceParticipant;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 *
 */
public class SendChatMessageAction	extends	HttpAwareConferenceAction
{
	protected	String	userId;
	protected	String	chatText;
	
	public	SendChatMessageAction()
	{
	}
	public	String	doWork()	throws	Exception
	{
		String	ret = SUCCESS;
		IConference conf = this.userSession.getConference();
		IConferenceParticipant user = this.userSession.getUser();

//		System.out.println("Received chat message: userId:"+userId+", text:"+chatText);
		try
		{
			String msg = this.servletRequest.getParameter("chatText");
			if (msg != null)
			{
				if(msg.contains("DimdimChatTraceLog"))
				{
					msg+=", Received by Server-"+Long.toString(System.currentTimeMillis());
				}
				
				if (userId != null)
				{
					conf.getChatManager().sendPrivateMessage(user,userId,msg);
				}
				else
				{
					conf.getChatManager().sendPublicMessage(user,msg);
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			ret = ERROR;
		}
		return	ret;
	}
	
	// TO BE REMOVED (FOR CHAT DELAY TESTING)
	/*
	private String getDateTime() 
	{
		 //DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		DateFormat dateFormat = new SimpleDateFormat("EEE d MMM yyyy HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }
	*/
	// *********************
	public String getChatText()
	{
		return this.chatText;
	}
	public void setChatText(String chatText)
	{
		this.chatText = chatText;
	}
	public String getUserId()
	{
		return userId;
	}
	public void setUserId(String userId)
	{
		this.userId = userId;
	}
}
