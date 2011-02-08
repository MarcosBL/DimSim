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
 * This object provides the flags to control an attendee's access to various
 * features. This object could be expanded later. It contains only the
 * permissions that are in use at present.
 * 
 * In all cases the permission values '0' denotes disabled and '1' enabled.
 * Default is all disabled.
 */

public class ParticipantPermissions implements	IJsonSerializable
{
	protected	String	chat;
	protected	String	audio;
	protected	String	video;
	
	public ParticipantPermissions()
	{
		this.chat = "1";
		this.audio = "0";
		this.video = "0";
	}
	public ParticipantPermissions(ParticipantPermissions defaultPerms)
	{
		this.chat = defaultPerms.getChat();
		this.audio = defaultPerms.getAudio();
		this.video = defaultPerms.getVideo();
	}
	public	String	toJson()
	{
		StringBuffer	buf = new StringBuffer();
		
		buf.append( "{" );
		buf.append( "\"objClass\":\""); buf.append("ParticipantPermissions"); buf.append("\",");
		buf.append( "\"chat\":\""); buf.append(this.chat); buf.append("\",");
		buf.append( "\"audio\":\""); buf.append(this.audio); buf.append("\",");
		buf.append( "\"video\":\""); buf.append(this.video); buf.append("\",");
		buf.append( "\"data\":\"dummy\"");
		buf.append( "}" );
		
		return	buf.toString();
	}
	public String getAudio()
	{
		return audio;
	}
	public void setAudio(String audio)
	{
		this.audio = audio;
	}
	public String getChat()
	{
		return chat;
	}
	public void setChat(String chat)
	{
		this.chat = chat;
	}
	public String getVideo()
	{
		return video;
	}
	public void setVideo(String video)
	{
		this.video = video;
	}
	public	void	setChatOn()
	{
		this.chat = "1";
	}
	public	void	setChatOff()
	{
		this.chat = "0";
	}
	public	void	setAudioOn()
	{
		this.audio = "1";
	}
	public	void	setAudioOff()
	{
		this.audio = "0";
	}
	public	void	setVideoOn()
	{
		this.video = "1";
	}
	public	void	setVideoOff()
	{
		this.video = "0";
	}
	public	boolean isVideoOn()
	{
		return "1".equalsIgnoreCase(video);
	}
	public	boolean isAudoOn()
	{
		return "1".equalsIgnoreCase(audio);
	}
}

