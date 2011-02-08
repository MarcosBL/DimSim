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

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * The adapter is designed to a possible superset of conference server
 * requirements. The adapter is designed to allow a conference server to use
 * any number of streaming servers at a time. The conference server treats
 * the adapter as a cluster of streaming servers, where each one is a distinct
 * physical server.
 * 
 * Each server adapter provides each conference its own stream server.
 * The conference is expected to provide the participants join and leave
 * activities to this server. This is to allow the streaming server to
 * take any appropriate actions depending on the load, which may include not
 * accepting any more streams.
 * 
 * Alternatively this work could be done by the adapter itself. These two
 * patterns are mutually exclusive. The work is either done by the meeting
 * server or the adapter.
 */

public interface StreamingServerAdapter	extends	StreamingServerConstants
{
	/**
	 * Refresh is for forcing the adapter to reread the properties file. This
	 * is more complicated for the streaming servers than meeting server because
	 * the parameters directly affect the adapter. We can not simply delete any
	 * one of the servers because it might be active and may be required to
	 * accept additional connections. Also the refresh needs to be on a timer
	 * so that it can be automatic.
	 */
//	public	void	refresh();
	
	/**
	 * A few simple inquiries. Not in active use at this time.
	 */
	public	String	getName();
	
	public	String	getAdapterType();
	
	/**
	 * This method is expected to return 1 if cluster is not in use.
	 * @return
	 */
	public	int		getNumberOfServers();
	
	public	StreamingServer	getAvailableServer(String streamType);
	
	/**
	 * This method implementation is essentially expected to return a server
	 * for stream type 'ANY'.
	 * 
	 * @return
	 */
	
	public	StreamingServer	getAvailableServer();
	
}

