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

package com.dimdim.conference.model;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * This interface, though called IStreamingServer represents all the streaming
 * resources required by a single meeting and may encapsulate any number of
 * real streaming servers. Conference started call is not strictly required
 * because the object is constructed only when a meeting is started. It is
 * provided for consistancy and possible use by any specific implementation.
 */

public interface IStreamingServer
{
	public	String	getDtpRtmpUrl();
	
	public	String	getDtpRtmptUrl();
	
	public	String	getAvRtmpUrl();
	
	public	String	getAvRtmptUrl();
	
	public	String	getAudioRtmpUrl();
	
	public	String	getAudioRtmptUrl();
	
	public	String	getAVApplicationStreamsDirectory();
	
	public	String	getWhiteboardRtmpUrl();
	
	public	String	getWhiteboardRtmptUrl();
	
	public	void	conferenceStarted(int maxParticipants, int maxDurationMinutes);
	
	public	void	conferenceClosed();
}
