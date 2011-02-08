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

package com.dimdim.streaming;

import java.lang.Class;
import java.util.Vector;
import java.util.ResourceBundle;

import com.dimdim.streaming.StreamingServerAdapter;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * This singleton manages the available adapters for streaming servers.
 * The adapter is designed to allow the conference server to work with a
 * single set of interfaces and allow separate development of adapters to
 * different streaming servers.
 * 
 * At any point, in most cases, only a single streaming server adapter is
 * expected to be in use. As the adapter itself represents a cluster of
 * servers. However multiple ones in use at a time is technically not
 * impossible and is considered by the interface as a possibility.
 */

public class StreamingServerAdapterProvider
{
	private	static	StreamingServerAdapterProvider	theProvider;
	
	public	static	StreamingServerAdapterProvider	getAdapterProvider()
	{
		if (StreamingServerAdapterProvider.theProvider == null)
		{
			StreamingServerAdapterProvider.createProvider();
		}
		return	StreamingServerAdapterProvider.theProvider;
	}
	protected	synchronized	static	void	createProvider()
	{
		if (StreamingServerAdapterProvider.theProvider == null)
		{
			try
			{
				Class realProviderClass = Class.
					forName("com.dimdim.streaming.main.AdapterProvider");
				if (realProviderClass != null)
				{
					StreamingServerAdapterProvider.theProvider =
						(StreamingServerAdapterProvider)realProviderClass.newInstance();
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	/**
	 * The meeting server's resource bundle is passed to the streaming server
	 * adapters in case the adapter intends to use any of the properties. In
	 * case of the dimdim tunnel adapter, the adapter would use the server address
	 * and port number.
	 * 
	 * @param meetingServerResources
	 */
	public	void	initializeProvider(String webappLocalPath,ResourceBundle meetingServerResources)
	{
		
	}
	public	StreamingServerAdapterProvider()
	{
	}
	public	StreamingServerAdapter	getAvailableAdapter()
	{
		return	null;
	}
	public	StreamingServerAdapter	getAnyAdapter()
	{
		return	null;
	}
	public	Vector getAvailableAdapterNames()
	{
		return	null;
	}
	public	StreamingServerAdapter	getAdapter(String name)
	{
		return	null;
	}
}
