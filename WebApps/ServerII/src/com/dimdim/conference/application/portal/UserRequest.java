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

import java.util.UUID;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * This object caches the user information so that it doesn't have to be
 * carried back and forth between the signin page and server.
 */

public class UserRequest
{
	protected	String	uri;
	protected	String	email;
	protected	String	meeting_id;
	protected	String	action;
	protected	UserInfo	userInfo;
	
	public UserRequest()
	{
	}
	public UserRequest(String email, String meeting_id,
				String action, UserInfo userInfo)
	{
		this.uri = UUID.randomUUID().toString();
		this.email = email;
		this.meeting_id = meeting_id;
		this.action = action;
		this.userInfo = userInfo;
	}
	public String getEmail()
	{
		return email;
	}
	public void setEmail(String email)
	{
		this.email = email;
	}
	public String getAction()
	{
		return action;
	}
	public void setAction(String action)
	{
		this.action = action;
	}
	public String getMeeting_id()
	{
		return meeting_id;
	}
	public void setMeeting_id(String meeting_id)
	{
		this.meeting_id = meeting_id;
	}
	public String getUri()
	{
		return uri;
	}
	public void setUri(String uri)
	{
	}
	public UserInfo getUserInfo()
	{
		return userInfo;
	}
	public void setUserInfo(UserInfo userInfo)
	{
		this.userInfo = userInfo;
	}
}

