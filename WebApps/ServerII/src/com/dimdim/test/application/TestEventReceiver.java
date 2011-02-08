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

import com.dimdim.messaging.IEvent;
import com.dimdim.messaging.IEventFilter;
import com.dimdim.messaging.IEventReceiver;
import com.dimdim.messaging.IEventsBufferProvider;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */

public class TestEventReceiver implements IEventReceiver
{
	private	int	index;
	private	IEventsBufferProvider	bufferProvider;
	
	public	TestEventReceiver(int index)
	{
		this.index = index;
	}
	public int compareTo(Object arg0)
	{
		return 0;
	}
	public String getId()
	{
		return this.index+"";
	}
	public int getIndex()
	{
		return this.index;
	}
	public void receiveEvent(IEvent event)
	{
//		System.out.println("THIS SHOULD NEVER HAPPEN");
	}
	public void removeEventFilter()
	{
	}
	public void setEventFilter(IEventFilter eventFilter)
	{
	}
	public void setEventsBufferProvider(IEventsBufferProvider iebf)
	{
		this.bufferProvider = iebf;
	}
	public	String	getAvailableData()
	{
		if (this.bufferProvider != null)
		{
			return	this.bufferProvider.drainAllAvailableEvents();
		}
		return	"";
	}
}
