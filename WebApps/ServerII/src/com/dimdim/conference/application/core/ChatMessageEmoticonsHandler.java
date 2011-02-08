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
import com.dimdim.conference.ConferenceConsoleConstants;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;
/**
 * @author Navin Sharma
 * @email navin@communiva.com
 * 
 */

public class ChatMessageEmoticonsHandler
{
	String chatMessage = null;
	String parsedChatMessage = "";
	
	public	ChatMessageEmoticonsHandler(String chatMessage)
	{
		this.chatMessage = chatMessage;
	}
	public String getParsedChatMessage()
	{
		HashMap emoticonsMap = new HashMap();//ConferenceConsoleConstants.getEmoticonsMap();
		if(emoticonsMap.isEmpty())
		{
			return this.chatMessage;
		}
		
		String tempMsg = chatMessage;
		tempMsg.trim();
		
		StringTokenizer st = new StringTokenizer(tempMsg);
		while(st.hasMoreTokens())
		{
			String buff = st.nextToken();
			
			boolean isEmoticonPresent = false;
			
			Iterator argNames = emoticonsMap.keySet().iterator();
			
			while (argNames.hasNext())
			{
				String name = (String)argNames.next();
				String value = (String)emoticonsMap.get(name);
				
				if(buff.compareTo(name)==0)
				{
					//String tmpBuff = "<img src=\""+value+"\">";
					String tmpBuff = "<img src="+value+">";
					parsedChatMessage +=tmpBuff+" ";
					isEmoticonPresent = true;
					break;
				}
				
			}
			if(isEmoticonPresent == false)
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
}
