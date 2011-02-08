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
 * Part of the DimDim V 2.0 Codebase (http://www.dimdim.com)	          *
 *                                                                        *
 * Copyright (c) 2008 Dimdim Inc. All Rights Reserved.                 	  *
 *                                                                        *
 *                                                                        *
 * This code is licensed under the Dimdim License                         *
 * For details please 									                  *
 * 	visit http://www.dimdim.com/opensource/dimdim_license.html			  *
 *                                                                        *
 **************************************************************************
 */

package com.dimdim.red5.module.dimdimPublisher;

/**
 * @author Rajesh Dharmalingam
 * @email rajesh@dimdim.com
 *
 */

import org.red5.server.api.so.ISharedObject;
import org.red5.server.api.IScope;

public class DimdimMeeting
{
	protected	String	prefix;
	protected	String	name;
	protected	boolean	isPersistent;
	
	protected	int		connectsCount;
	
	protected	IScope			roomScope;
	protected	ISharedObject	rosterSharedObject;
	protected	Application		publisher;
	protected	long			lastConnectTime = System.currentTimeMillis();
	
	public DimdimMeeting(Application publisher, IScope roomScope)
	{
		this.publisher = publisher;
		this.roomScope = roomScope;
		this.isPersistent = publisher.isPersistent;
		
		this.name = roomScope.getName();
		this.prefix = "dimdimPublisher."+name;
		String rosterSharedObjectName = prefix + ".roster";
		
		this.rosterSharedObject = publisher.getSharedObject(roomScope, rosterSharedObjectName, isPersistent);
		
		this.connectsCount = this.getRosterCount();
	}
	public	int	getRosterCount()
	{
		int	i = 0;
		if (this.rosterSharedObject.hasAttribute(""))
		{
			i = ((Integer)this.rosterSharedObject.getIntAttribute("")).intValue();
		}
		return	i;
	}
	public	void	setRosterCount(int i)
	{
		this.rosterSharedObject.setAttribute("", new Integer(i));
	}
	public	void	participantJoined()
	{
		this.setRosterCount(++this.connectsCount);
	}
	public	boolean	isActive()
	{
		return	(System.currentTimeMillis() - this.lastConnectTime)<(5*60*60*1000);
	}
}

