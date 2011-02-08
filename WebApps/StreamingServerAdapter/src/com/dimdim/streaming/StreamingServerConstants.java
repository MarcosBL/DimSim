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
 */

public interface StreamingServerConstants
{
	/**
	 * Adapter type
	 */
	public	static	final	String	DIMDIM_RTMPT_TUNNEL	=	"DIMDIM_RTMPT_TUNNEL";
	public	static	final	String	RED5	=	"RED5";
	public	static	final	String	DSS		=	"DSS";
	
	/**
	 * Stream types. This is provided so that if the meeting server chooses
	 * to use different streams for different purposes. This does not mean
	 * that the streaming server will provide different urls for each type.
	 * A particular streaming server may return same url for all types. If
	 * the meeting server however chooses to go to different servers for
	 * different streams that will be fine and the conference start and stop
	 * calls will have to be managed by the meeting server accordingly,
	 * because as yet the streaming server does not have the information and
	 * no other way of getting the information.
	 * 
	 * Whiteboard, powerpoint presentations and annotations are not exactly
	 * stream based applications. However they can be served through an
	 * rtmpt / rtmpt server. The stream tags have essentialy become feature
	 * tags effective now - Jayant, 04/30/07.
	 */
	
	public	static	final	String	AUDIO_STREAM	=	"AUDIO";
	public	static	final	String	AUDIO_VIDEO_STREAM	=	"AV";
	public	static	final	String	SCREEN_SHARE_STREAM	=	"DTP";
	public	static	final	String	WHITEBOARD_STREAM	=	"WB";
	public	static	final	String	ANNOTATIONS_STREAM	=	"ANTN";
	public	static	final	String	POWERPOINT_STREAM	=	"PPT";
	
	public	static	final	String	VIDEO_ONLY_STREAM	=	"VIDEO";
	public	static	final	String	CUSTOM_DATA_STREAM	=	"DATA";
	
	/**
	 * Simple tokens that represent all the stream types.
	 */
	public	static	final	String	ANY_STREAM	=	"ANY";
	public	static	final	String	ALL_STREAMS	=	"ALL";
	
	/**
	 * All involved protocols.
	 */
	public	static	final	String	PROTOCOL_RTMP	=	"rtmp";
	public	static	final	String	PROTOCOL_RTMPT	=	"rtmpt";
	public	static	final	String	PROTOCOL_RTMPS	=	"rtmps";
	public	static	final	String	PROTOCOL_RTMPTS	=	"rtmpts";
	
}
