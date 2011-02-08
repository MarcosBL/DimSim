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

import com.dimdim.conference.ui.json.client.UIChatEntry;
import com.dimdim.conference.ui.json.client.UIRosterEntry;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * This listener expect the listening object to be the chat window itself.
 */
public interface ChatListener	extends	UIModelListener
{
	
	public	void	onChatMessage(UIChatEntry chatEntry);
	
	//	Following methods are part of the block user from chats paradigm,
	//	which will be implemented in next phase.
	
//	public	void	onChatPaused();
	
//	public	void	onChatContinued();
	
//	public	void	onUserBlocked(UIRosterEntry user);
	
//	public	void	onUserUnBlocked(UIRosterEntry user);
	
}
