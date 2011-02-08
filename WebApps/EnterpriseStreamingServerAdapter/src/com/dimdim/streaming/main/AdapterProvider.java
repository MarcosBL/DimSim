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

package com.dimdim.streaming.main;

import java.util.ResourceBundle;
import java.util.Vector;

import com.dimdim.streaming.StreamingServerAdapter;
import com.dimdim.streaming.StreamingServerAdapterProvider;

import com.dimdim.streaming.enterprise.EnterpriseStreamingServerAdapter;

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

public class AdapterProvider	extends	StreamingServerAdapterProvider
{
	/**
	 * Empty beans constructor is required for instantiation through reflection.
	 *
	 */
	public	AdapterProvider()
	{
		this.essa = new EnterpriseStreamingServerAdapter();
		this.names = new Vector();
		this.names.addElement(new String("EnterpriseStreamingServerAdapter"));
	}
	
	private	EnterpriseStreamingServerAdapter	essa;
	private	Vector	names;
	
	public	StreamingServerAdapter	getAvailableAdapter()
	{
		return	this.essa;
	}
	public	void	initializeProvider(String webappLocalPath,ResourceBundle meetingServerResources)
	{
		this.essa.setWebappLocalPath(webappLocalPath);
	}
	public	StreamingServerAdapter	getAnyAdapter()
	{
		return	this.essa;
	}
	public	Vector getAvailableAdapterNames()
	{
		return	this.names;
	}
	public	StreamingServerAdapter	getAdapter(String name)
	{
		return	this.essa;
	}
}
