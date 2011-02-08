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

package com.dimdim.conference.application.core;

import java.util.Date;

import com.dimdim.conference.ConferenceConstants;
import com.dimdim.conference.model.Event;
import com.dimdim.conference.model.IConference;
import com.dimdim.conference.model.IConferenceParticipant;
import com.dimdim.conference.model.SettingsEvent;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * This application controls the active resources for the clients. Active
 * resource is simply the resource that is being used at the time. This
 * application employs following concepts and enforces following rules.
 * 
 * Primary responsibility of this object is to control the sharing of
 * resources for the system. The resources sharing is strictly only one
 * at a time. Hence all the sharing control requests are handled by this
 * object, which enforces the sharing rules and provides transperent
 * controls to help the presenter.
 * 
 * The video is always presented in the size determined by the system
 * hence the related parameters in the event can be ignored.
 */

public class CurrentSettingsManager extends ConferenceFeatureManager
{
	protected	ActiveConference		conference;
	
	public	CurrentSettingsManager(ActiveConference conference)
	{
		this.conference = conference;
		this.setClientEventPublisher(conference.getClientEventPublisher());
	}
	public	void	lobbySettingChanged()
	{
		boolean	lobbyEnabled = conference.isLobbyEnabled();
		SettingsEvent se = new SettingsEvent(SettingsEvent.CHANGE,lobbyEnabled,
				0, 0, "unknown", "unknown");
		
		this.dispatchStreamControlEvent(se);
	}
	protected	void	dispatchStreamControlEvent(SettingsEvent se)
	{
		Event event = new Event(ConferenceConstants.FEATURE_SETTINGS,
				ConferenceConstants.EVENT_SETTINGS_CHANGED, new Date(),
				ConferenceConstants.RESPONSE_OK, se );
		
		this.getClientEventPublisher().dispatchEventToAllClients(event);
	}
}
