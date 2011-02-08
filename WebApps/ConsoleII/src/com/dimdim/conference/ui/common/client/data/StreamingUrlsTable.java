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

package com.dimdim.conference.ui.common.client.data;

import com.dimdim.conference.ui.model.client.ConferenceGlobals;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */

public class StreamingUrlsTable	extends	StringsTable
{
	public	static	final	String	stremingUrlsTableName	=	"streaming_urls";
	
	public	static	final	String	dtpRtmpUrl	=	"dtp_rtmp_url";
	public	static	final	String	dtpRtmptUrl	=	"dtp_rtmpt_url";
	public	static	final	String	avRtmpUrl	=	"av_rtmp_url";
	public	static	final	String	avRtmptUrl	=	"av_rtmpt_url";
	public	static	final	String	audioRtmpUrl	=	"audio_rtmp_url";
	public	static	final	String	audioRtmptUrl	=	"audio_rtmpt_url";
	public	static	final	String	whiteboardRtmpUrl	=	"whiteboard_rtmp_url";
	public	static	final	String	whiteboardRtmptUrl	=	"whiteboard_rtmpt_url";
	
	protected	String	dtpProtocol;
	protected	String	dtpHost;
	protected	String	dtpPort = "80";
	protected	String	dtpShareApp;
	
	public	StreamingUrlsTable()
	{
		super( StreamingUrlsTable.stremingUrlsTableName,
				new String[]{
					StreamingUrlsTable.dtpRtmpUrl,
					StreamingUrlsTable.dtpRtmptUrl,
					StreamingUrlsTable.avRtmpUrl,
					StreamingUrlsTable.avRtmptUrl,
					StreamingUrlsTable.audioRtmpUrl,
					StreamingUrlsTable.audioRtmptUrl,
					StreamingUrlsTable.whiteboardRtmpUrl,
					StreamingUrlsTable.whiteboardRtmptUrl
				});
		
		String	dtpShareUrl = (String)super.fields.get(StreamingUrlsTable.dtpRtmpUrl);
		int	index1 = dtpShareUrl.indexOf("://");
		this.dtpProtocol = dtpShareUrl.substring(0, index1);
		String	serverAddress = dtpShareUrl.substring(index1+3);
		this.dtpHost = serverAddress;
		this.dtpPort = "80";
		
		int	index2 = serverAddress.indexOf("/");
		this.dtpHost = serverAddress.substring(0, index2);
		this.dtpShareApp = serverAddress.substring(index2+1);
		
		int	index3 = dtpHost.indexOf(":");
		if (index3 > 0)
		{
			this.dtpPort = dtpHost.substring(index3+1);
			this.dtpHost = dtpHost.substring(0, index3);
		}
		
		int	index4 = dtpShareApp.indexOf("/");
		if (index4 > 0)
		{
			this.dtpShareApp = dtpShareApp.substring(0, index4);
		}
	}
	public String getDtpHost()
	{
		return dtpHost;
	}
	public String getDtpPort()
	{
		return dtpPort;
	}
	public String getDtpProtocol()
	{
		return dtpProtocol;
	}
	public String getDtpShareApp()
	{
		return dtpShareApp;
	}
	public	String	getDtpRtmpUrl()
	{
		return	(String)super.fields.get(StreamingUrlsTable.dtpRtmpUrl);
//			this.getDTPStreamingApplicationName()+"/"+ConferenceGlobals.conferenceKey;
	}
	public	String	getDtpRtmptUrl()
	{
		return	(String)super.fields.get(StreamingUrlsTable.dtpRtmptUrl);
//			this.getDTPStreamingApplicationName()+"/"+ConferenceGlobals.conferenceKey;
	}
	public	String	getAvRtmpUrl()
	{
		return	(String)super.fields.get(StreamingUrlsTable.avRtmpUrl)+
			this.getAVStreamingApplicationName()+"/"+ConferenceGlobals.conferenceKeyQualified;
	}
	public	String	getAvRtmptUrl()
	{
		return	(String)super.fields.get(StreamingUrlsTable.avRtmptUrl)+
			this.getAVStreamingApplicationName()+"/"+ConferenceGlobals.conferenceKeyQualified;
	}
	public	String	getAvTestRtmpUrl()
	{
		return	(String)super.fields.get(StreamingUrlsTable.avRtmpUrl)+
			this.getAVStreamingTestApplicationName()+"/"+ConferenceGlobals.conferenceKeyQualified;
	}
	public	String	getAvTestRtmptUrl()
	{
		return	(String)super.fields.get(StreamingUrlsTable.avRtmptUrl)+
			this.getAVStreamingTestApplicationName()+"/"+ConferenceGlobals.conferenceKeyQualified;
	}
	public	String	getAudioRtmpUrl()
	{
		return	(String)super.fields.get(StreamingUrlsTable.audioRtmpUrl)+
			this.getAVStreamingApplicationName()+"/"+ConferenceGlobals.conferenceKeyQualified;
	}
	public	String	getAudioRtmptUrl()
	{
		return	(String)super.fields.get(StreamingUrlsTable.audioRtmptUrl)+
			this.getAVStreamingApplicationName()+"/"+ConferenceGlobals.conferenceKeyQualified;
	}
	public	String	getWhiteboardRtmpUrl()
	{
		return	(String)super.fields.get(StreamingUrlsTable.whiteboardRtmpUrl);
	}
	public	String	getWhiteboardRtmptUrl()
	{
		return	(String)super.fields.get(StreamingUrlsTable.whiteboardRtmptUrl);
	}
	public	String	getAVStreamingApplicationName()
	{
		return	"dimdimPublisher";
	}
	public	String	getAVStreamingTestApplicationName()
	{
		return	"avtest";
	}
	public	String	getDTPStreamingApplicationName()
	{
		return	"dimdimDTP";
	}
}
