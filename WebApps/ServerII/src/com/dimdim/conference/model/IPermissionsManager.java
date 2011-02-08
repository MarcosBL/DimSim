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
 *	File Name  : IRosterManager.java
 *  Created On : Apr 11, 2006
 *  Created By : Saurav Mohapatra
 **************************************************************************
*/
 
package com.dimdim.conference.model;

import java.util.Vector;

/**
 * @author Saurav Mohapatra
 * @email  Saurav.Mohapatra@communiva.com
 * 
 * This interface defines how various permissions are controlled for a
 * conference. At present the permissions are designed to control sending
 * chat messages, and broadcasting audio and video for an attendee. The
 * presenters always have the authority do these.
 * 
 * The audio and video permissions may be limited to a few number of attendees.
 * In this case, the enables are done only if space is available. Caller must
 * disable a specific attendee before enabling another. The system will never
 * affect a permissions of a specific attendee on its own.
 */
public interface IPermissionsManager
{
	//	Chat Permissions.
	
	public	void	enableSendChatMessageForAll();
	
	public	void	disableSendChatMessageForAll();
	
	public	void	enableSendChatMessageFor(Vector ids);
	
	public	void	disableSendChatMessageFor(Vector ids);
	
	//	Audio Permissions.
	
	public	void	enableAudioFor(String id);
	
	public	void	disableAudioFor(String id);
	
	public	void	enableAudioForAllInList(Vector ids);
	
	public	void	disableAudioForAllInList(Vector ids);
	
	public	void	disableAudioForAll();
	
	//	Video Permissions.
	
	public	void	enableVideoFor(String id);
	
	public	void	disableVideoFor(String id);
	
	public	void	enableVideoForAllInList(Vector ids);
	
	public	void	disableVideoForAllInList(Vector ids);
	
	public	void	disableVideoForAll();
}
