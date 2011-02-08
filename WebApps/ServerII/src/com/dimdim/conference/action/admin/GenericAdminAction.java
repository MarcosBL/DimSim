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
 
package com.dimdim.conference.action.admin;

import com.dimdim.conference.action.CommonDimDimAction;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 *
 */
public class GenericAdminAction	extends	CommonDimDimAction
{
	protected	String	token;
	protected	String	jsonBuffer;
	
	protected	static	int		counter = 0;
	
	public	GenericAdminAction()
	{
		GenericAdminAction.incrementCounter();
	}
	public	String	execute()	throws	Exception
	{
		jsonBuffer = "["+
			"{type:\"event\",eventId:\"chat.message\",dataType:\"object\",featureId:\"feature.chat\","+
			"data:{objClass:\"ChatEntry\",senderId:\"asas@abc.com\",messageText:\"Counter::"+counter+"\","+
			"systemMessage:\"false\",privateMessage:\"false\",data:\"dummy\"}}"+
			"]";
		
		if (token != null)
		{
			return	token;
		}
		else
		{
			return	SUCCESS;
		}
	}
	protected	synchronized	static	void	incrementCounter()
	{
		GenericAdminAction.counter++;
	}
	public String getToken()
	{
		return this.token;
	}
	public void setToken(String token)
	{
		this.token = token;
	}
	public int getCounter()
	{
		return counter;
	}
	public void setCounter(int counter)
	{
		GenericAdminAction.counter = counter;
	}
	public String getJsonBuffer()
	{
		return jsonBuffer;
	}
	public void setJsonBuffer(String jsonBuffer)
	{
		this.jsonBuffer = jsonBuffer;
	}
}
