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
 * Copyright (c) 2007 Dimdim Inc. All Rights Reserved.              	  *
 *                                                                        *
 *                                                                        *
 * This code is licensed under the DimDim License                         *
 * For details please visit http://www.dimdim.com/license                 *
 *                                                                        *
 **************************************************************************
 */

package com.dimdim.test.application;

import com.dimdim.messaging.MessageEngineFactory;
import com.dimdim.messaging.IMessageEngine;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * Each meeting is a single message engine. The meeting itself does not
 * represent any functionality. It simply creates and manages the message
 * engine. The participants represent the functionality.
 * 
 */

public class TestMeeting
{
	protected	int		index;
	protected	IMessageEngine	messageEngine;
	
	public	TestMeeting(int index)
	{
		this.index = index;
		this.messageEngine = MessageEngineFactory.getFactory(25).getMessageEngine();
//		this.messageEngine = new SmallMessageEngine();
	}
	public	void	addParticipant(TestParticipant participant)
	{
		participant.setMessageEngine(this.messageEngine);
		this.messageEngine.addEventReceiver(participant.getTestEventReceiver());
	}
	public	void	removeParticipant(TestParticipant participant)
	{
		this.messageEngine.removeEventReceiver(participant.getTestEventReceiver());
	}
}
