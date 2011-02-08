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

import com.dimdim.messaging.IMessageEngine;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */

public class TestParticipant implements Runnable
{
	private	int	index;
	private	TestEventReceiver	testEventReceiver;
	private	int	counter;
	private	IMessageEngine	messageEngine;
	
	public	TestParticipant(int index)
	{
		this.index = index;
		this.testEventReceiver = new TestEventReceiver(index);
		counter = 0;
	}
	public TestEventReceiver getTestEventReceiver()
	{
		return testEventReceiver;
	}
	public void run()
	{
		//	Create and dispatch 1 event and read all available data.
		System.out.println("Participant:"+index+", round:"+counter);
		if (counter%5 == 0)
		{
			TestEvent event = new TestEvent(this.index*10000+counter);
			this.messageEngine.dispatchEvent(event);
		}
		String buffer = this.testEventReceiver.getAvailableData();
		System.out.println("Available data:"+buffer);
	}
	public IMessageEngine getMessageEngine()
	{
		return messageEngine;
	}
	public void setMessageEngine(IMessageEngine messageEngine)
	{
		this.messageEngine = messageEngine;
	}
}
