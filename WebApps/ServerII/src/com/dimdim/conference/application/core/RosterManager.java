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
 *								                                          *
 * Copyright (c) 2006 Communiva Inc. All Rights Reserved.	              *
 *								                                          *
 *								                                          *
 * This code is licensed under the DimDim License                         *
 * For details please visit http://www.dimdim.com/license		          *
 *									                                      *
 **************************************************************************
 */

package com.dimdim.conference.application.core;

import com.dimdim.conference.model.IRoster;
import com.dimdim.conference.model.IRosterEventListener;
import com.dimdim.conference.model.IRosterManager;
import com.dimdim.conference.model.Conference;
import com.dimdim.conference.model.UserRoster;


/**
 * @author Jayant Pandit
 * @email  Jayant.Pandit@communiva.com
 * 
 */
public class RosterManager extends ConferenceFeatureManager implements IRosterManager
{
	
	protected	Conference				conference;
	
	/**
	 * 
	 */
	public RosterManager(ActiveConference activeConference)
	{
		this.conference = activeConference.getConference();
		this.setClientEventPublisher(activeConference.getClientEventPublisher());
		this.conference.getUserRoster().setClientEventPublisher(activeConference.getClientEventPublisher());
	}
	
	/* (non-Javadoc)
	 * @see com.dimdim.conference.roster.IRosterManager#getRosterObject()
	 */
	public IRoster getRosterObject()
	{
		return this.conference.getUserRoster();
	}
	public	UserRoster	getUserRoster()
	{
		return	conference.getUserRoster();
	}
	/* (non-Javadoc)
	 * @see com.dimdim.conference.roster.IRosterManager#addRosterListener(com.dimdim.conference.roster.IRosterEventListener)
	 */
	public void addRosterListener(IRosterEventListener listener)
	{
//		this.conference.getUserRoster().addRosterEventListener(listener);
	}

	/* (non-Javadoc)
	 * @see com.dimdim.conference.roster.IRosterManager#removeRosterListener(com.dimdim.conference.roster.IRosterEventListener)
	 */
	public void removeRosterListener(IRosterEventListener listener)
	{
//		this.conference.getUserRoster().removeRosterEventListener(listener);
	}
}
