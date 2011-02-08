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

import javax.servlet.ServletContext;

import com.dimdim.conference.action.CommonDimDimAction;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */
public class GetJoinConferenceFormAction	extends	CommonGetFormAction
{
	protected	String	email = "";
	protected	String	displayName = "";
	protected	String	confKey = "";
	protected	String	asPresenter = "off";
	protected	String	attendeePwd = null;
	
	public	GetJoinConferenceFormAction()
	{
	}
	public	String	execute()	throws	Exception
	{
		this.servletRequest.setAttribute("email",this.email);
		this.servletRequest.setAttribute("displayName",this.displayName);
		this.servletRequest.setAttribute("confKey",this.confKey);
		this.servletRequest.setAttribute("asPresenter",this.asPresenter);
		this.servletRequest.setAttribute("action","join");
		this.servletRequest.setAttribute("attendeePwd",this.attendeePwd);
		
		ActionRedirectData ard = new ActionRedirectData("join",cflag,email,displayName,confKey,"",asPresenter,"");
		ard.setAttendeePwd(attendeePwd);
		this.servletRequest.getSession().setAttribute(ActionRedirectData.SESSION_ATTRIBUTE_NAME,ard);
		
		return	super.execute();
	}
	public String getConfKey()
	{
		return confKey;
	}
	public void setConfKey(String confKey)
	{
		this.confKey = confKey;
	}
	public String getDisplayName()
	{
		return displayName;
	}
	public void setDisplayName(String displayName)
	{
		this.displayName = displayName;
	}
	public String getEmail()
	{
		return email;
	}
	public void setEmail(String email)
	{
		this.email = email;
	}
	public String getAsPresenter()
	{
		return asPresenter;
	}
	public void setAsPresenter(String asPresenter)
	{
		this.asPresenter = asPresenter;
	}
	public String getAttendeePwd() {
		return attendeePwd;
	}
	public void setAttendeePwd(String attendeePwd) {
		this.attendeePwd = attendeePwd;
	}
	
	public void setMeetingRoomName(String roomName)
	{
		this.confKey = roomName;
	}
	
	public String getMeetingRoomName()
	{
		return confKey;
	}
}
