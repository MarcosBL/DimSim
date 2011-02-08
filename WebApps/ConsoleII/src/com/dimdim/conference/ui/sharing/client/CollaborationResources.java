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
 * Copyright (c) 2008 Dimdim Inc. All Rights Reserved.              	  *
 *                                                                        *
 *                                                                        *
 * This code is licensed under the DimDim License                         *
 * For details please visit http://www.dimdim.com/license                 *
 *                                                                        *
 **************************************************************************
 */

package com.dimdim.conference.ui.sharing.client;

import com.dimdim.conference.ui.model.client.ConferenceGlobals;
import com.dimdim.conference.ui.common.client.data.StreamingUrlsTable;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */

public class CollaborationResources
{
	protected	StreamingUrlsTable	streamingUrlsTable;
	
	String waitPageUrl = ConferenceGlobals.baseWebappURL+"share_wait/share_wait.html";
	
	public	CollaborationResources()
	{
		this.streamingUrlsTable = new StreamingUrlsTable();
	}
	public	String	getConferenceKey()
	{
		return	ConferenceGlobals.conferenceKey;
	}
	public	String	getWhiteboardMovieURL()
	{
		return	"swf/wb_sa.swf";
	}
	public	String	getPPTBroadcasterMovieURL()
	{
		return	"swf/dmsPresentationBroadcaster.swf";
	}
	public	String	getPPTPlayerMovieURL()
	{
		return	"swf/dmsPresentationPlayer.swf";
	}
	public	String	getWaitingPageURL()
	{
		String temp = ConferenceGlobals.defaultUrl; 
		if(null != temp && temp.length() > 0)
		{
			waitPageUrl = temp;
		}
		return	waitPageUrl;
	}
	//	The aspect ratios are Number.round( width * 100 / height ) Since they
	//	do not change
	public	int	getWhiteboardStageAspectRatio()
	{
		return	1226;
	}
	public	int	getWhiteboardMinimumWidth()
	{
		return	436;
	}
	public	int	getWhiteboardMinimumHeight()
	{
		return	348;
	}
	public	int	getPPTBroadcasterStageAspectRatio()
	{
		return	1455;
	}
	public	int	getPPTBroadcasterMinimumWidth()
	{
		return	436;
	}
	public	int	getPPTBroadcasterMinimumHeight()
	{
		return	348;
	}
	public	int	getPPTPlayerStageAspectRatio()
	{
		return	1243;
	}
	public	int	getPPTPlayerMinimumWidth()
	{
		return	436;
	}
	public	int	getPPTPlayerMinimumHeight()
	{
		return	348;
	}
	public	StreamingUrlsTable	getStreamingUrlsTable()
	{
		return	this.streamingUrlsTable;
	}
}
