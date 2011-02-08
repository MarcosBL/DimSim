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

package com.dimdim.red5.module.whiteboard;

/**
 * @author Rajesh Dharmalingam
 * @email rajesh@dimdim.com
 *
 */

import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import org.red5.server.adapter.ApplicationAdapter;
import org.red5.server.api.IClient;
import org.red5.server.api.IConnection;
import org.red5.server.api.IScope;


public class Whiteboard extends ApplicationAdapter 
{
	protected boolean isPersistent = true;
	protected HashMap whiteboards = new HashMap();
	protected IScope appScope = null;
	
	public boolean appStart(IScope app)
	{
		System.out.println("AppStart called for : " + app.getName());
		
		if (!super.appStart(app))
		{
			log.error("Unable to start the application");
			return false;
		}
		
		this.appScope = app;
		return true;
	}
	public boolean appJoin(IClient client, IScope scope)
	{
		System.out.println("## appJoin ## client: "+client.getId()+", scope: "+scope.getName());
		return true;
	}
	public boolean connect(IConnection conn, IScope scope, Object[] params)
	{
		System.out.println("## connect ## IConnection: "+conn+", scope: "+scope.getName()+", params: "+params);
		return true;
	}
	public boolean start(IScope scope)
	{
		System.out.println("## start ## "+scope.getName());
		if (!super.start(scope))
		{
			System.out.println("Not able to start the module.");
			return false;
		}
		System.out.println("returning true from start");
		return true;
	}
	public boolean join(IClient client, IScope scope)
	{
		System.out.println("## join ## client: "+client.getId()+", scope: "+scope.getName());
		return true;
	}
	public boolean roomStart(IScope room)
	{
		System.out.println("## roomStart ## " + room.getName());
		
		if (!super.roomStart(room))
		{
			System.out.println("Unable to start " + room.getName()
					+ " for the application");
			return false;
		}
		
		String	meetingName = room.getName();
		MeetingWhiteboard meetingWhiteboard = (MeetingWhiteboard)this.whiteboards.get(meetingName);
		if (meetingWhiteboard != null)
		{
			//	Close the previous board. However there is no specific process for this.
		}
		System.out.println("Creating a new whiteboard: "+meetingName);
		meetingWhiteboard = new MeetingWhiteboard(this, room);
		this.whiteboards.put(meetingName, meetingWhiteboard);
		
		return true;
	}
	public boolean roomConnect(IConnection conn, Object[] params)
	{
		System.out.println("## roomConnect ## IConnection: "+conn+", params:"+params);
		if (!super.roomConnect(conn, params))
		{
			System.out.println("#### Unable to connect from "+conn.getScope().getName());
			return false;
		}
		return true;
	}
	public boolean roomJoin(IClient client, IScope scope)
	{
		System.out.println("## roomJoin ## Client: "+client.getId()+", Scope "+scope.getName());
		return true;
	}
	public void appLeave(IClient client, IScope scope)
	{
		System.out.println("## appLeave ## Client: "+client.getId()+", Scope "+scope.getName());
	}
	public void roomLeave(IClient client, IScope room)
	{
		System.out.println("## roomLeave ## Client: "+client.getId()+", Scope "+scope.getName());
	}
	public void roomDisconnect(IConnection conn)
	{
		System.out.println("## roomDisconnect ## IConnection: "+conn);
	}
	public void appDisconnect(IConnection conn)
	{
		System.out.println("## appDisconnect ## IConnection: "+conn);
		this.cleanOldWhiteboard();
	}
	public void appStop(IScope app)
	{
		System.out.println("## appStop ## Application:" + app.getName());
	}
	private	synchronized	void	cleanOldWhiteboard()
	{
		Vector v = new Vector();
		Iterator keys = this.whiteboards.keySet().iterator();
		while (keys.hasNext())
		{
			String	name = (String)keys.next();
			MeetingWhiteboard meetingWhiteboard = (MeetingWhiteboard)this.whiteboards.get(name);
			if (!meetingWhiteboard.isActive())
			{
				v.addElement(name);
			}
		}
		int	size = v.size();
		for (int i=0; i<size; i++)
		{
			String name = (String)v.elementAt(i);
			System.out.println("#### Removing whiteboard on timeout: "+name);
			this.whiteboards.remove(name);
		}
	}
}
