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
 
package com.dimdim.conference.ui.model.client;

import java.util.Iterator;

import com.dimdim.conference.ui.json.client.UIResourceObject;
import com.dimdim.conference.ui.json.client.UIRosterEntry;
import com.dimdim.conference.ui.json.client.UIServerResponse;
import com.google.gwt.user.client.Window;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * The client state model is responsible for maintenning the state of the
 * client as the current user's actions as well as the events coming from
 * the server. All functional points must check if the functionality is
 * applicable for the current state.
 */
public class ClientStateModel	extends	FeatureModel
{
	public	static	final	Integer		ModelIndex	=	new Integer(1);
	
	public static final String CONFERENCE_ACTIVE = "conf.active";
	public static final String CONFERENCE_CLOSED = "conf.closed";
	public static final String CONFERENCE_ENDED = "conf.ended";
	
	public static final String EVENT_NO_CONFERENCE_KEY = "conf.noKey";
	public static final String EVENT_NO_CONFERENCE_FOR_KEY = "conf.noConf";
	public static final String EVENT_CONFERENCE_ID = "conf.id";
	
	public static final String SERVER_CONNECTION_LOST = "conf.serverConnectionLost";
	
	public static final String PRESENTATION_ATTENDING = "presentation.attending";
	public static final String PRESENTATION_PERSENTING = "presentation.presenting";
	
	public static final String SCREEN_SHARE_RECEIVING = "screen.receiving";
	public static final String SCREEN_SHARE_PUBLISHING = "screen.publishing";
	
	protected	String	currentState = ClientStateModel.CONFERENCE_ACTIVE;
	
	public	ClientStateModel()
	{
		super("feature.conf");
		currentState = ClientStateModel.CONFERENCE_ACTIVE;
	}
	
	public	void	onEvent(String eventId, Object data)
	{
//		Window.alert("ClientStateModel:onEvent:: -"+eventId+"-, data: -"+data+"-");
		String eventMethod = eventId;
		int index = eventId.lastIndexOf(".");
		if (index > 0)
		{
			eventMethod = eventId.substring(index+1);
		}
//		Window.alert("triggering event:"+eventMethod+":"+data);
//		actOnEvent(eventMethod,data);
		if (eventMethod.equalsIgnoreCase("closed"))
		{
			onclosed();
		}
		else if (eventMethod.equalsIgnoreCase("consoleDataSent"))
		{
			onconsoleDataSent();
		}
		else if (eventMethod.equalsIgnoreCase("serverConnectionLost"))
		{
			String message = "";
			if (data != null)
			{
				message = data.toString();
			}
			onserverConnectionLost(message);
		}
		else if (eventMethod.equalsIgnoreCase("serverSessionLost"))
		{
			//	This event is defunct, it should never be received now.
			String message = "";
			if (data != null)
			{
				message = data.toString();
			}
			onserverSessionLost(message);
		}
		else if (eventMethod.equalsIgnoreCase("noKey"))
		{
			//	This is an invalid data call. it should never happen
			//	in field.
			String message = "";
			if (data != null)
			{
				message = data.toString();
			}
			onMissingConferenceKey(message);
		}
		else if (eventMethod.equalsIgnoreCase("noConf"))
		{
			String message = "";
			if (data != null)
			{
				message = data.toString();
			}
			onNoConferenceForKey(message);
		}
		else if (eventMethod.equalsIgnoreCase("id"))
		{
			String message = "";
			if (data != null)
			{
				message = data.toString();
			}
			onConferenceId(message);
		}
		else if (eventMethod.equalsIgnoreCase("timeWarning1"))
		{
			onTimeWarning1();
		}
		else if (eventMethod.equalsIgnoreCase("timeWarning2"))
		{
			onTimeWarning2();
		}
		else if (eventMethod.equalsIgnoreCase("timeWarning3"))
		{
			onTimeWarning3();
		}
		else if (eventMethod.equalsIgnoreCase("timeExpired"))
		{
			onTimeExpired();
		}
		else if (eventMethod.equalsIgnoreCase("noPresenter"))
		{
			onnoPresenter();
		}
		else if (eventMethod.equalsIgnoreCase("trackbackUrl"))
		{
			String url = "/";
			if (data != null)
			{
				url = data.toString();
			}
			ontrackbackUrl(url);
		}
		else if (eventMethod.equalsIgnoreCase("timeChange"))
		{
			String time = "";
			if (data != null)
			{
				time = data.toString();
			}
			onTimeChange(time);
		}
		
		else if (eventMethod.equalsIgnoreCase("partCountChange"))
		{
			String count = "";
			if (data != null)
			{
				count = data.toString();
			}
			onPartCountChange(count);
		}
	}
	
	protected	void	onclosed()
	{
		this.currentState = ClientStateModel.CONFERENCE_CLOSED;
		Iterator iter = this.listeners.values().iterator();
		while (iter.hasNext())
		{
			((ClientStateModelListener)iter.next()).onConferenceClosed();
		}
	}
	protected	void	onconsoleDataSent()
	{
		Iterator iter = this.listeners.values().iterator();
		while (iter.hasNext())
		{
			((ClientStateModelListener)iter.next()).onConsoleDataSent();
		}
	}
	protected	void	onTimeWarning1()
	{
		Iterator iter = this.listeners.values().iterator();
		while (iter.hasNext())
		{
			((ClientStateModelListener)iter.next()).onTimeWarning1();
		}
	}
	protected	void	onTimeWarning2()
	{
		Iterator iter = this.listeners.values().iterator();
		while (iter.hasNext())
		{
			((ClientStateModelListener)iter.next()).onTimeWarning2();
		}
	}
	protected	void	onTimeWarning3()
	{
		Iterator iter = this.listeners.values().iterator();
		while (iter.hasNext())
		{
			((ClientStateModelListener)iter.next()).onTimeWarning3();
		}
	}
	protected	void	onTimeExpired()
	{
		Iterator iter = this.listeners.values().iterator();
		while (iter.hasNext())
		{
			((ClientStateModelListener)iter.next()).onTimeExpired();
		}
	}
	protected	void	onserverConnectionLost(String message)
	{
		this.currentState = ClientStateModel.CONFERENCE_CLOSED;
		Iterator iter = this.listeners.values().iterator();
		while (iter.hasNext())
		{
			((ClientStateModelListener)iter.next()).onServerConnectionLost(message);
		}
	}
	protected	void	onserverSessionLost(String message)
	{
		this.currentState = ClientStateModel.CONFERENCE_CLOSED;
		Iterator iter = this.listeners.values().iterator();
		while (iter.hasNext())
		{
			((ClientStateModelListener)iter.next()).onServerSessionLost(message);
		}
	}
	protected	void	onMissingConferenceKey(String message)
	{
		Iterator iter = this.listeners.values().iterator();
		while (iter.hasNext())
		{
			((ClientStateModelListener)iter.next()).onMissingConferenceKey();
		}
	}
	protected	void	onNoConferenceForKey(String message)
	{
		this.currentState = ClientStateModel.CONFERENCE_CLOSED;
		Iterator iter = this.listeners.values().iterator();
		while (iter.hasNext())
		{
			((ClientStateModelListener)iter.next()).onNoConferenceForKey();
		}
	}
	protected	void	onConferenceId(String message)
	{
		this.currentState = ClientStateModel.CONFERENCE_CLOSED;
		Iterator iter = this.listeners.values().iterator();
		while (iter.hasNext())
		{
			((ClientStateModelListener)iter.next()).onConferenceId(message);
		}
	}
	protected	void	onnoPresenter()
	{
		this.currentState = ClientStateModel.CONFERENCE_CLOSED;
		Iterator iter = this.listeners.values().iterator();
		while (iter.hasNext())
		{
			((ClientStateModelListener)iter.next()).onConferenceClosed();
		}
	}
	protected	void	ontrackbackUrl(String url)
	{
		Iterator iter = this.listeners.values().iterator();
		while (iter.hasNext())
		{
			((ClientStateModelListener)iter.next()).onTrackbackUrlChanged(url);
		}
	}
	
	protected	void	onTimeChange(String time)
	{
		Iterator iter = this.listeners.values().iterator();
		while (iter.hasNext())
		{
			((ClientStateModelListener)iter.next()).onTimeChanged(time);
		}
	}
	
	protected	void	onPartCountChange(String count)
	{
		Iterator iter = this.listeners.values().iterator();
		while (iter.hasNext())
		{
			((ClientStateModelListener)iter.next()).onPartCountChanged(count);
		}
	}
	
	public	boolean	isConferenceActive()
	{
//		Window.alert("Conference State:"+this.currentState);
		return	(this.currentState.equals(ClientStateModel.CONFERENCE_ACTIVE));
	}
	public	void	setConferenceInactive()
	{
//		Window.alert("Setting conference inactive:");
		this.currentState = ClientStateModel.CONFERENCE_CLOSED;
	}
}
