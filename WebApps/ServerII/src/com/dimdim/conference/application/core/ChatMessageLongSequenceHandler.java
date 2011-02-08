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

import java.util.StringTokenizer;

/**
 * @author Navin Sharma
 * @email navin@communiva.com
 * 
 */

public class ChatMessageLongSequenceHandler
{
	String chatMessage = null;
	String parsedChatMessage = "";
	
	public	ChatMessageLongSequenceHandler(String chatMessage)
	{
		this.chatMessage = chatMessage;
	}
	public String getParsedChatMessage()
	{
		try
		{		
			String tempMsg = chatMessage;
			tempMsg.trim();
			
			StringTokenizer st = new StringTokenizer(tempMsg);
			while(st.hasMoreTokens())
			{
				String buff = st.nextToken();
				if((buff.indexOf("href=http:") == -1) && (buff.indexOf("src=")== -1) && (buff.indexOf("target=_blank")== -1))
				{
					int buffLen = buff.length();
					int maxAllowedLen = 24;
					if(buffLen > maxAllowedLen)
					{
						int len = 0;
						while(len < buffLen)
						{
							if((buffLen - len) > maxAllowedLen)
							{
								parsedChatMessage +=buff.substring(len,len+maxAllowedLen)+" ";
								len+=maxAllowedLen;
								continue;
							}
							else
							{
								parsedChatMessage +=buff.substring(len,buffLen)+" ";
								break;
							}						
						}
					}
					else
					{
						parsedChatMessage +=buff+" ";
					}
				}
				else
				{
					parsedChatMessage +=buff+" ";
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
}
