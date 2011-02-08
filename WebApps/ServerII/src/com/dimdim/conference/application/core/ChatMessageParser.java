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

/**
 * @author Navin Sharma
 * @email navin@communiva.com
 * 
 * This is responsible for parsing chat message and performing different actions which are as follows:
 * (1) Breaking up long words
 * (2) Handling emoticons
 * (3) Hyperlink addition
 */

public class ChatMessageParser 
{
	String chatMessage = null;
	String parsedChatMessage = "";
	
	public	ChatMessageParser(String chatMessage)
	{
		this.chatMessage = chatMessage;
	}
	public String getParsedChatMessage()
	{
		try
		{
			String hyperLinkParsedMessage = "";
			hyperLinkParsedMessage = handleHyperlink(this.chatMessage);
			
			if(hyperLinkParsedMessage.length() > 0)
			{
				String emoticonsParsedMessage = "";
				emoticonsParsedMessage = handleEmoticons(hyperLinkParsedMessage);
				
				if(emoticonsParsedMessage.length() > 0)
				{
					String longWordParsedMessage = "";
					longWordParsedMessage = handleLongWords(emoticonsParsedMessage);
					
					if(longWordParsedMessage.length() > 0)
					{
						parsedChatMessage = longWordParsedMessage;
					}
				}
			}
			if(parsedChatMessage.length() > 0)
			{
				return parsedChatMessage;
			}
			else
			{
				return chatMessage;
			}
		}
		catch(Exception e)
		{
			return this.chatMessage;
		}
	}
	public String handleLongWords(String message)
	{
		ChatMessageLongSequenceHandler chatMsgLSeqHandler = new ChatMessageLongSequenceHandler(message);
		return chatMsgLSeqHandler.getParsedChatMessage();		
	}
	public String handleEmoticons(String message)
	{
		ChatMessageEmoticonsHandler chatMsgEmoticonHandler = new ChatMessageEmoticonsHandler(message);
		return chatMsgEmoticonHandler.getParsedChatMessage();		
	}
	public String handleHyperlink(String message)
	{
		ChatMessageHyperlinkHandler chatMsgHyperLinkHandler = new ChatMessageHyperlinkHandler(message);
		return chatMsgHyperLinkHandler.getParsedChatMessage();		
	}
}
