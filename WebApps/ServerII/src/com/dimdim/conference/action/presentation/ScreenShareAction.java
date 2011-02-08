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
 
package com.dimdim.conference.action.presentation;

import com.dimdim.conference.ConferenceConstants;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 *
 */
public class ScreenShareAction	extends	StreamingControlAction
{
	/*
	protected	String	cmd;
	protected	String	conferenceKey;
	protected	String	resourceId;
	protected	String	mediaId;
	protected	String	presenterId;
	protected	String	presenterPassKey;
	*/
	public	ScreenShareAction()
	{
	}
	protected	String	getActionCode()
	{
		return	ConferenceConstants.ACTION_SCREEN_SHARING_CONTROL;
	}
	/*
	public	String	execute()	throws	Exception
	{
		System.out.println("Received from the stream publisher:");
		System.out.println("Stream Control --- cm -"+cmd+"-");
		System.out.println("Stream Control --- conferenceKey -"+conferenceKey+"-");
		System.out.println("Stream Control --- resourceId -"+resourceId+"-");
		System.out.println("Stream Control --- mediaId -"+mediaId+"-");
		System.out.println("Stream Control --- presenterId -"+presenterId+"-");
		System.out.println("Stream Control --- presenterPassKey -"+presenterPassKey+"-");
		
		userSession = (UserSession)session.
			get(ConferenceConsoleConstants.ACTIVE_USER_SESSION);
		if (userSession == null)
		{
			userSession = new UserSession(null);
			this.session.put(ConferenceConsoleConstants.ACTIVE_USER_SESSION,userSession);
		}
		{
			String ret = doWork();
			if (this.resultCode == null)
			{
				//	If the individual action has not set the result code
				this.resultCode = ret;
			}
			if (this.resultEvent == null)
			{
				setupErrorResponse(IConferenceConstants.MSG_ERR_NO_SUCH_ACTION);
			}
			return	ret;
		}
	}
	public	String	doWork()
	{
		ConferenceAdminClient adminClient = UserManager.getManager().
			getConferenceAdminClient(this.presenterId,this.presenterPassKey);
		String	str	= NONE;
		try
		{
//			public	static	ConferenceMessage	createMessage_ControlResource(String jabberId, String confKey, String userId,
//					String controlAction, String resourceId, String presentationId, String eventType, Integer slide)
			String jabberId = adminClient.getJabberId();
			
			ConferenceMessage msg = ConferenceMessageCreateHelper.createMessage_ControlResource(
					jabberId, conferenceKey, presenterId,
					IConferenceConstants.ACTION_SCREEN_SHARING_CONTROL,
					resourceId, this.mediaId, cmd, new Integer(0));
			adminClient.sendMessage(msg);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			str = ERROR;
		}
		return	str;
	}
	public String getCmd()
	{
		return this.cmd;
	}
	public void setCmd(String cmd)
	{
		this.cmd = cmd;
	}
	public String getConferenceKey()
	{
		return this.conferenceKey;
	}
	public void setConferenceKey(String conferenceKey)
	{
		this.conferenceKey = conferenceKey;
	}
	public String getMediaId()
	{
		return this.mediaId;
	}
	public void setMediaId(String mediaId)
	{
		this.mediaId = mediaId;
	}
	public String getPresenterId()
	{
		return this.presenterId;
	}
	public void setPresenterId(String presenterId)
	{
		this.presenterId = presenterId;
	}
	public String getPresenterPassKey()
	{
		return this.presenterPassKey;
	}
	public void setPresenterPassKey(String presenterPassKey)
	{
		this.presenterPassKey = presenterPassKey;
	}
	public String getResourceId()
	{
		return this.resourceId;
	}
	public void setResourceId(String resourceId)
	{
		this.resourceId = resourceId;
	}
	*/
}
