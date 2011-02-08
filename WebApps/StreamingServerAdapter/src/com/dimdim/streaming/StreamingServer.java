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
 * Start of each meeting needs to be reported to a streaming server, which
 * is expected to return an instance of this manager. This manager will be
 * provided all the meeting activities which may affect the streaming server
 * load for the meeting.
 * 
 * Implementation of this interface is not expected to remember the meeting key
 * and organizer id or any other information passed to it. All the provided
 * information is for the use by the streaming server internally for any
 * reason of its own, such as accounting, performance or logging.
 */

public interface StreamingServer	extends	StreamingServerConstants
{
	/**
	 * This method does not have to be directly used. The getUrl method will call
	 * it anyway before generating the url.
	 * 
	 * @param streamType
	 * @return true or false as the protocol is supported or not.
	 * @throws InvalidStreamType
	 */
//	public	boolean		isStreamTypeSupported(String streamType)	throws	InvalidStreamType;
	
	/**
	 * The conference server is expected to get the urls for the required
	 * stream types and provide them to all clients to be used as and when
	 * required. Only restriction on these urls is that they remain valid and
	 * functional during the lifetime of the meeting.
	 * 
	 * @param streamType
	 * @return
	 * @throws InvalidStreamType
	 * @throws StreamTypeNotSupported
	 */
//	public	String		getUrl(String streamType)	throws	InvalidStreamType, StreamTypeNotSupported;
	
	public	String		getRTMPUrl(String meetingKey);
	
	public	String		getRTMPTUrl(String meetingKey);
	
	//	This method is required to provide the location of the av application's streams
	//	directory on the streaming server. Under this directory the server is expected
	//	to create a directory for each meetingKey, where all the recorded streams for that
	//	key are kept. The location provided by this method is appended with the meeting
	//	key to get the full path of the recorded stream's directory.
	
	public	String		getAVApplicationStreamsDirectory();
	
	public	int			getMaxStreams();
	
	public	int			getAvailableStreams();
	
	public	boolean		isStreamAvailable();
	
	/**
	 * The stream types are defined in the constants interface. This should really be
	 * an enum.
	 * 
	 * @param streamType
	 * @return
	 */
	public	boolean		supportsStreamType(String streamType);
	
	/**
	 * This interface
	 */
	public	void	meetingStarted(String meetingKey,
			int maxExpectedParticipants,
			int maxExpectedMeetingLengthMinutes);
	
	public	void	participantJoined(String meetingKey, String participantId);
	
	public	void	participantLeft(String meetingKey, String participantId);
	
	/**
	 * This call is provided so that the streaming server can keep track of the
	 * number of streams and the number of available streams.
	 */
	public	void	meetingClosed(String meetingKey);
	
	public	void	broadcasterStarted(String meetingKey, String participantId, String streamId);
	
	public	void	broadcasterStopped(String meetingKey, String participantId, String streamId);
}

