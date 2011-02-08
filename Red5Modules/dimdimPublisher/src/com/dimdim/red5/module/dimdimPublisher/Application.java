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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import org.red5.server.adapter.ApplicationAdapter;
import org.red5.server.api.IClient;
import org.red5.server.api.IConnection;
import org.red5.server.api.IScope;

public class Application extends ApplicationAdapter {

	protected	boolean	isPersistent = true;
	protected	HashMap	meetings = new HashMap();
	protected	IScope	appScope = null;
	
//	protected ISharedObject roster;
//	protected String prefix;
//	protected int connectsCount;
//	protected String name;

	public boolean appStart(IScope app)
	{
		System.out.println("## App Start ##, scope: "+app);
		if (!super.appStart(app))
		{
			System.out.println("Unable to start the application");
			return false;
		}
		this.appScope = app;
//		Object handler = new com.dimdim.red5.module.dimdimPublisher.SOHandler();
//		app.registerServiceHandler("dimdimPublisher", handler);
		return true;
	}
	public boolean appConnect(IConnection conn, Object[] params)
	{
		System.out.println("## App Connect ## connection: "+conn+", params:"+params);
		if (!super.appConnect(conn, params))
		{
			System.out.println("app connect failed");
			return false;
		}
//		System.out.println("param values if any in appConnect : " + (String)params[0]);
		return true;
	}
	public void appLeave(IClient client, IScope scope)
	{
		System.out.println("## appLeave ## client :"+client.getId()+", scope " + scope.getName());
	}
	public void appDisconnect(IConnection conn)
	{
		System.out.println("## appDisconnect : connection:"+conn);
	}
	public boolean roomStart(IScope room)
	{
		System.out.println("## roomStart ## scope:"+room);		
		if (!super.roomStart(room))
		{
			System.out.println("Unable to start Room.");
			return false;
		}
		String meetingName = room.getName();
		DimdimMeeting dimdimMeeting = (DimdimMeeting)this.meetings.get(meetingName);
		if (dimdimMeeting != null)
		{
			//	Close the previous board. However there is no specific process for this.
		}
		System.out.println("Creating a new whiteboard: "+meetingName);
		dimdimMeeting = new DimdimMeeting(this, room);
		this.meetings.put(meetingName, dimdimMeeting);
		
//		initialize(room);
		return true;
	}
	public boolean roomJoin(IClient client, IScope room)
	{
		System.out.println("## roomJoin ## client: "+client+", room: "+room);		
		if (!super.roomJoin(client, room))
		{
			System.out.println("Room join is unsuccessful");
			return false;
		}
		String roomName = room.getName();
		DimdimMeeting dimdimMeeting = (DimdimMeeting)this.meetings.get(roomName);
		if (dimdimMeeting != null)
		{
			System.out.println("User joined meeting.");
			dimdimMeeting.participantJoined();
		}
		else
		{
			System.out.println("No meeting by name:"+roomName);
		}
		return true;
	}
	public void roomDisconnect(IConnection conn)
	{
		System.out.println("## roomDisconnect ## connection: "+conn);
		this.cleanOldMeetings();
	}
	public boolean connect(IConnection conn, IScope scope, Object[] params)
	{
		System.out.println("## connect ## Connection: "+conn+", scope: "+scope+", params: "+params);
//		scope.setPersistent(isPersistent);
//		roomStart(scope);
		return true;
	}
	public boolean start(IScope scope)
	{
		System.out.println("## start ## scope:"+scope);
		if (!super.start(scope))
		{
			System.out.println("Not able to start the module.");
			return false;
		}
		return true;
	}
	public boolean join(IClient client, IScope scope)
	{
		System.out.println("## join ## client:"+client+", scope:"+scope);
//		roomJoin(client, scope);
		return true;
	}
	private	synchronized	void	cleanOldMeetings()
	{
		Vector v = new Vector();
		Iterator keys = this.meetings.keySet().iterator();
		while (keys.hasNext())
		{
			String	name = (String)keys.next();
			DimdimMeeting meetingWhiteboard = (DimdimMeeting)this.meetings.get(name);
			if (!meetingWhiteboard.isActive())
			{
				v.addElement(name);
			}
		}
		int	size = v.size();
		for (int i=0; i<size; i++)
		{
			String name = (String)v.elementAt(i);
			System.out.println("#### Removing meeting on timeout: "+name);
			this.meetings.remove(name);
		}
	}
	/*
	public void initialize(IScope room){

		String meetingName = room.getName();
		System.out.println("meeting name is " + meetingName);

		DimdimMeeting meeting = new DimdimMeeting(this);
		room.setAttribute(meetingName, meeting);
		this.meetings.put(meetingName, meetings);

		this.createSharedObject(room, "dimdimPublisher", true);
		ISharedObject sharedObject = this.getSharedObject(room,
				"dimdimPublisher", false);
//		sharedObject.addSharedObjectListener(new SOEventListener());

		sharedObject.registerServiceHandler(new SOHandler());
		this.name = room.getName();
		this.prefix = "dimdimPublisher." + room.getName();

		this.connectsCount = 0;
		String rosterName = prefix + ".roster";

		roster = getSharedObject(room, rosterName, true);
		if (roster == null)
		{
			this.createSharedObject(room, rosterName, isPersistent);
			roster = this.getSharedObject(room, rosterName, isPersistent);
		}
	
	}
	public boolean roomConnect(IConnection conn, Object[] params) {
		System.out
				.println("################# Room Connect called ###############");
		if (!super.roomConnect(conn, params)) {
			System.out.println("not able to connect to the room");
			return false;
		}
		return false;
	}
	*/
}