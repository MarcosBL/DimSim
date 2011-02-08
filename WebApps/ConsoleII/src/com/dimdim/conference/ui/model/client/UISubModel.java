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

import	java.util.HashMap;

import com.dimdim.conference.ui.json.client.JSONurlReader;
import com.dimdim.conference.ui.json.client.JSONurlReaderCallback;
import com.dimdim.conference.ui.json.client.UIServerResponse;
import com.google.gwt.user.client.ResponseTextHandler;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */
public class UISubModel	implements	ResponseTextHandler
{
	protected	CommandURLFactory	commandsFactory = new CommandURLFactory();
	protected	HashMap	listeners;
	
	public	UISubModel()
	{
		this.listeners = new HashMap();
	}
	
	public	void	addListener(UIModelListener listener)
	{
		this.listeners.put(listener,listener);
	}
	public	void	removeListener(UIModelListener listener)
	{
		this.listeners.remove(listener);
	}
	public	void	onCompletion(String text)
	{
		
	}
//	protected	void	executeCommand(String url)
//	{
//		JSONurlReader reader = new JSONurlReader(url,this);
//		reader.doReadURL();
//	}
//	protected	Object	executeCommandSync(String url, long timeout)
//	{
//		JSONurlReader reader = new JSONurlReader(url,this);
//		return	reader.doReadURLSync(timeout);
//	}
	public	void	urlReadingComplete(UIServerResponse response)
	{
//		lastCommandResponse = response;
	}
	
}
