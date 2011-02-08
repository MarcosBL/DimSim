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
import com.dimdim.messaging.IEventData;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */

public class TestEvent implements IEvent
{
	private	int	index;
	private	String	buffer;
	private	long	creationTime;
	
	public	TestEvent(int index)
	{
		this.index = index;
		buffer = "This is a test event";
		this.creationTime = System.currentTimeMillis();
	}
	public int getIndex()
	{
		return index;
	}
	public int getPriority()
	{
		return 0;
	}
	public String getSource()
	{
		return "source";
	}
	public String getType()
	{
		return "type";
	}
	public void setIndex(int i)
	{
		index = i;
	}
	public String toJson()
	{
		return "{buffer='"+this.buffer+"::"+this.index+"'}";
	}
	public int compareTo(Object arg0)
	{
		return 0;
	}
	public long getCreationTime()
	{
		return this.creationTime;
	}
	public IEventData getEventData()
	{
		return null;
	}
	public String getInitiatorId()
	{
		return null;
	}
}
