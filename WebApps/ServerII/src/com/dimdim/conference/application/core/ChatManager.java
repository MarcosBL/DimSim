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

package com.dimdim.conference.application.core;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.dimdim.conference.ConferenceConstants;
import com.dimdim.conference.model.Event;
import com.dimdim.conference.model.ChatEntry;
import com.dimdim.conference.model.IConference;
import com.dimdim.conference.model.IConferenceParticipant;
import com.dimdim.conference.model.IChatManager;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 *
 * This application controls the active resources for the clients. Active
 * resource is simply the resource that is being used at the time. This
 * application employs following concepts and enforces following rules.
 *
 * Primary responsibility of this object is to control the sharing of
 * resources for the system. The resources sharing is strictly only one
 * at a time. Hence all the sharing control requests are handled by this
 * object, which enforces the sharing rules and provides transperent
 * controls to help the presenter.
 *
 * The conference chant permissions controls how the chat manager behaves.
 * Applicable values are:
 *
 * Disable - all attendees disabled
 * Enable - all attendees enabled
 * Check - check individual permissions
 */

public class ChatManager extends ConferenceFeatureManager	implements	IChatManager
{
	public	static	final	String	DISABLE_ALL = "DISABLE_ALL";
	public	static	final	String	ENABLE_ALL	=	"ENABLE_ALL";
	public	static	final	String	CHECK_ATTENDEE	=	"CHECK_ATTENDEE";

	protected	IConference		conference;
	protected	String			conferenceChatPermission;

	public	ChatManager(ActiveConference conference)
	{
		this.conference = conference;
		this.setClientEventPublisher(conference.getClientEventPublisher());
	}
	public	IConference	getConference()
	{
		return	this.conference;
	}
	public String getConferenceChatPermission()
	{
		return conferenceChatPermission;
	}
	public void setConferenceChatPermission(String conferenceChatPermission)
	{
		this.conferenceChatPermission = conferenceChatPermission;
	}
	public	synchronized	void	sendPublicMessage(IConferenceParticipant sender, String message)
	{
		if (sender != null)
		{
			if(message.contains("DimdimChatTraceLog"))
			{
				message+=", Send by Chat Manager-"+Long.toString(System.currentTimeMillis());
			}
			ChatEntry chatEntry = new ChatEntry(sender.getId(),sender.getDisplayName(),message);
			//ChatMessageParser chatMsgParser = new ChatMessageParser(message);
			//ChatEntry chatEntry = new ChatEntry(sender.getId(),sender.getDisplayName(),chatMsgParser.getParsedChatMessage());
			this.dispatchEvent(sender,chatEntry);
		}
	}

	public	synchronized	void	sendPrivateMessage(IConferenceParticipant sender, String receiverId, String message)
	{
		IConferenceParticipant target = this.conference.getParticipant(receiverId);
		if (sender != null && target != null)
		{
			if(message.contains("DimdimChatTraceLog"))
			{
				message+=", Send by Chat Manager-"+Long.toString(System.currentTimeMillis());
			}
			ChatEntry chatEntry = new ChatEntry(sender.getId(),sender.getDisplayName(),message);
			//ChatMessageParser chatMsgParser = new ChatMessageParser(message);
			//ChatEntry chatEntry = new ChatEntry(sender.getId(),sender.getDisplayName(),chatMsgParser.getParsedChatMessage());
			chatEntry.setPrivateMessage("true");
			this.dispatchEventToTarget(target,chatEntry);

			//ChatEntry loopBkChatEntry = new ChatEntry(receiverId,sender.getDisplayName(),chatMsgParser.getParsedChatMessage());
			//loopBkChatEntry.setPrivateMessage("true");
			//this.dispatchEventToTarget(sender,loopBkChatEntry);
		}
	}
	protected	void	dispatchEvent(IConferenceParticipant sender, ChatEntry chatEntry)
	{
		Event event = new Event(ConferenceConstants.FEATURE_CHAT,
				ConferenceConstants.EVENT_CHAT_MESSAGE,
				new Date(), ConferenceConstants.RESPONSE_OK, chatEntry );

		this.getClientEventPublisher().dispatchEventToAllClientsExcept(event,sender);
		//this.getClientEventPublisher().dispatchEventToAllClients(event);
	}
	protected	void	dispatchEventToTarget(IConferenceParticipant target, ChatEntry chatEntry)
	{
		Event event = new Event(ConferenceConstants.FEATURE_CHAT,
				ConferenceConstants.EVENT_CHAT_MESSAGE,
				new Date(), ConferenceConstants.RESPONSE_OK, chatEntry );

		this.getClientEventPublisher().dispatchEventTo(event,target);
	}
}
