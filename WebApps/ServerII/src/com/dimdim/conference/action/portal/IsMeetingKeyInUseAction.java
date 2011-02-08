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

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * This action will return the result as 'true'/'false'.
 */

public class IsMeetingKeyInUseAction extends PortalAdapterAction
{
	protected	String	roomName;
	protected	String	sessionKey="dummy";
	
	public	IsMeetingKeyInUseAction()
	{
		System.out.println(".................inside is key in use action constructor....");
	}
	public	String	execute()	throws	Exception
	{
		super.result = "true";
		System.out.println("inside is key in use action.... key = "+roomName);
		if (this.roomName != null)
		{
//		  making the confkey as lower case as this was creating dtp stream problems
		    this.roomName = this.roomName.toLowerCase();
		    this.result = super.isKeyInUse(this.roomName)+"";
		    super.createResultJsonBuffer(true,200,result+"");
		}
		else
		{
			this.result = "true";
			super.createResultJsonBuffer(false,310,"Insufficient data");
		}
		System.out.println("inside is key in use action.... result = "+result);
		return	SUCCESS;
	}
	public String getRoomName() {
		return roomName;
	}
	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}
	public String getKey()
	{
		return roomName;
	}
	public void setKey(String key)
	{
		this.roomName = key;
	}
	public String getSessionKey() {
		return sessionKey;
	}
	public void setSessionKey(String sessionKey) {
		this.sessionKey = sessionKey;
	}


}
