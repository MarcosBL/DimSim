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

import	java.util.Vector;
import	com.dimdim.conference.model.*;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */

public class ParticipantChangeListenerCollection
{
	protected	Vector	listeners = new Vector();
	
	public	ParticipantChangeListenerCollection()
	{
	}
	
	public	synchronized	void	addParticipantChangeListener(IParticipantChangeListener ipcl)
	{
		if (!this.listeners.contains(ipcl))
		{
			this.listeners.addElement(ipcl);
		}
	}
	public	synchronized	void	removeParticipantChangeListener(IParticipantChangeListener ipcl)
	{
		this.listeners.remove(ipcl);
	}
	
	public	void	fireArrivedInLobby(IConferenceParticipant user)
	{
		int size = this.listeners.size();
		for (int i=0; i<size; i++)
		{
			((IParticipantChangeListener)this.listeners.elementAt(i)).arrivedInLobby(user);
		}
	}
	public	void	fireGrantedEntry(IConferenceParticipant user)
	{
		int size = this.listeners.size();
		for (int i=0; i<size; i++)
		{
			((IParticipantChangeListener)this.listeners.elementAt(i)).grantedEntry(user);
		}
	}
	public	void	fireDeniedEntry(IConferenceParticipant user)
	{
		int size = this.listeners.size();
		for (int i=0; i<size; i++)
		{
			((IParticipantChangeListener)this.listeners.elementAt(i)).deniedEntry(user);
		}
	}
	public	void	fireJoinedConference(IConferenceParticipant user)
	{
		int size = this.listeners.size();
		for (int i=0; i<size; i++)
		{
			((IParticipantChangeListener)this.listeners.elementAt(i)).joinedConference(user);
		}
	}
	public	void	fireRejoinedConference(IConferenceParticipant user)
	{
		int size = this.listeners.size();
		for (int i=0; i<size; i++)
		{
			((IParticipantChangeListener)this.listeners.elementAt(i)).rejoinedConference(user);
		}
	}
	public	void	fireReloadedConsole(IConferenceParticipant user)
	{
		int size = this.listeners.size();
		for (int i=0; i<size; i++)
		{
			((IParticipantChangeListener)this.listeners.elementAt(i)).reloadedConsole(user);
		}
	}
	public	void	fireLeftConference(IConferenceParticipant user)
	{
		int size = this.listeners.size();
		for (int i=0; i<size; i++)
		{
			((IParticipantChangeListener)this.listeners.elementAt(i)).leftConference(user);
		}
	}
	public	void	fireRemovedFromConference(IConferenceParticipant user)
	{
		int size = this.listeners.size();
		for (int i=0; i<size; i++)
		{
			((IParticipantChangeListener)this.listeners.elementAt(i)).removedFromConference(user);
		}
	}
	public	void	fireBecamePresenter(IConferenceParticipant user)
	{
		int size = this.listeners.size();
		for (int i=0; i<size; i++)
		{
			((IParticipantChangeListener)this.listeners.elementAt(i)).becamePresenter(user);
		}
	}
	public	void	fireBecameActivePresenter(IConferenceParticipant user)
	{
		int size = this.listeners.size();
		for (int i=0; i<size; i++)
		{
			((IParticipantChangeListener)this.listeners.elementAt(i)).becameActivePresenter(user);
		}
	}
}
