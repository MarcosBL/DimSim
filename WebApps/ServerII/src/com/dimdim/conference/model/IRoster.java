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
/*
 **************************************************************************
 *	File Name  : IRoster.java
 *  Created On : Apr 11, 2006
 *  Created By : Saurav Mohapatra
 **************************************************************************
*/

package com.dimdim.conference.model;

import java.util.List;

/**
 * @author Saurav Mohapatra
 * @email  Saurav.Mohapatra@communiva.com
 */
public	interface	IRoster
{
	public	void	setClientEventPublisher(IClientEventPublisher cep);
	
	public	IRosterEntry	getRosterEntry(String userId);
	public  IConferenceParticipant	addParticipant(String email, String displayName,
				String password, String role, boolean lobbyEnabled, String userId)	throws	UserInConferenceException;
	
	public	IConferenceParticipant	updateParticipantMood(String email,
				String mood)	throws	UserNotInConferenceException;
	
	public	IConferenceParticipant updateDisplayName(String email, String name)
	throws	UserNotInConferenceException;
	
	public	IConferenceParticipant	updateParticipantPhoto(String email,
				String photo)	throws	UserNotInConferenceException;
//	public	IConferenceParticipant	updateParticipantRole(String email,
//				String role)	throws	UserNotInConferenceException;
	public	void	makeActivePresenter(String email)
				throws	UserNotInConferenceException;
	
	/**
	 * Only the current
	 * @param currentActivePresenter
	 * @param email
	 * @throws UserNotInConferenceException
	 */
	public	void	makePresenter(String email)
				throws	UserNotInConferenceException;
	
	public	IConferenceParticipant	removeParticipant(String email)
				throws	UserNotInConferenceException;
	
	public	String	createParticipantChild(IConferenceParticipant user);
	
	public	IConferenceParticipant		getParticipant(String userId);
	public	List						getParticipants();
	public	List						getParticipants(String role);
	
	public	int		getNumberOfPresenters();
	public	int		getNumberOfParticipants();
	
}
