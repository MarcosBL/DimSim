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

package com.dimdim.conference.model;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * This interface is notified of the state and status changes in a meeting
 * participant.
 */

public interface IParticipantChangeListener
{
	
	public	void	arrivedInLobby(IConferenceParticipant user);
	
	public	void	grantedEntry(IConferenceParticipant user);
	
	public	void	deniedEntry(IConferenceParticipant user);
	
	public	void	joinedConference(IConferenceParticipant user);
	
	public	void	rejoinedConference(IConferenceParticipant user);
	
	public	void	reloadedConsole(IConferenceParticipant user);
	
	public	void	leftConference(IConferenceParticipant user);
	
	public	void	removedFromConference(IConferenceParticipant user);
	
	public	void	becameAttendee(IConferenceParticipant user);
	
	public	void	becamePresenter(IConferenceParticipant user);
	
	public	void	becameActivePresenter(IConferenceParticipant user);
	
}
