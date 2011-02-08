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

//import com.dimdim.conference.ui.json.client.UIAttendeePermissions;
//import	com.dimdim.conference.ui.json.client.UIRosterEntry;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */
public interface ClientStateModelListener	extends	FeatureModelListener
{
	
	public	void	onConferenceClosed();
	
	public	void	onConsoleDataSent();
	
	public	void	closeConsole();
	
	public	void	onServerConnectionLost(String message);
	
	public	void	onServerSessionLost(String message);
	
	//	This event is raised when there is no key in the server call.
	
	public	void	onMissingConferenceKey();
	
	//	This event is raised by server when there is no conference by
	//	the given key. Which would mean that the console is trying to
	//	poll for events on conference which is finished.
	
	public	void	onNoConferenceForKey();
	
	//	This event is raised by server when the event poll find the
	//	conference but not the session. Which means that a console
	//	from a previous meeting on the same key is trying to reconnect
	//	to the meeting.
	
	public	void	onConferenceId(String conferenceId);
	
	public	void	onTimeWarning1();
	
	public	void	onTimeWarning2();
	
	public	void	onTimeWarning3();
	
	public	void	onTimeExpired();
	
	public	void	onTrackbackUrlChanged(String url);
	
	public	void	onTimeChanged(String time);
	
	public	void	onPartCountChanged(String count);
}
