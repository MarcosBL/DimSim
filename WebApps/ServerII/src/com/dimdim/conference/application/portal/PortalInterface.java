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

package com.dimdim.conference.application.portal;

import java.util.HashMap;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */

public class PortalInterface
{
	private	static	PortalInterface		portalInterface;
	
	public	static	PortalInterface	getPortalInterface()
	{
		if (PortalInterface.portalInterface == null)
		{
			PortalInterface.createPortalInterface();
		}
		return	PortalInterface.portalInterface;
	}
	private	synchronized	static	void	createPortalInterface()
	{
		if (PortalInterface.portalInterface == null)
		{
			PortalInterface.portalInterface = new PortalInterface();
		}
	}
	
//	protected	HashMap	settings;
	protected	HashMap	meeting_ids;
//	protected	HashMap	userInfos;
//	protected	HashMap	userRequests;
	
	private	PortalInterface()
	{
//		this.settings = new HashMap();
		this.meeting_ids = new HashMap();
//		this.userInfos = new HashMap();
//		this.userRequests = new HashMap();
	}
//	public	MeetingSettings	getMeetingSettings(String confKey)
//	{
//		return	(MeetingSettings)this.settings.get(confKey);
//	}
//	public	void	saveMeetingSettings(String confKey, MeetingSettings meetingSettings)
//	{
//		this.settings.put(confKey,meetingSettings);
//	}
	public	String	getMeetingId(String confKey)
	{
		return	(String)this.meeting_ids.get(confKey);
	}
	public	void	saveMeetingId(String confKey, String meeting_id)
	{
		this.meeting_ids.put(confKey,meeting_id);
	}
//	public	UserInfo	getUserInfo(String meeting_id, String email)
//	{
//		return	(UserInfo)this.userInfos.get(meeting_id+"_"+email);
//	}
//	public	void	saveUserInfo(String meeting_id, UserInfo userInfo)
//	{
//		this.userInfos.put(meeting_id+"_"+userInfo.getEmail(),userInfo);
//	}
	public	void	clearMeeting(String confKey)
	{
//		this.settings.remove(confKey);
		this.meeting_ids.remove(confKey);
//		this.userInfos.remove(confKey);
	}
	public	void	saveUserRequest(UserRequest ur)
	{
//		this.userRequests.put(ur.getUri(),ur);
	}
//	public	UserRequest	getUserRequest(String uri)
//	{
//		return	(UserRequest)this.userRequests.get(uri);
//	}
	public	void	clearUserRequest(String uri)
	{
//		this.userRequests.remove(uri);
	}
}

