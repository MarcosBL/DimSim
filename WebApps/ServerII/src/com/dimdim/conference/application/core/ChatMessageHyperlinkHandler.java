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

public class ChatMessageHyperlinkHandler
{
	String chatMessage = null;
	String parsedChatMessage = "";
	
	public	ChatMessageHyperlinkHandler(String chatMessage)
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
				if(buff.indexOf("http://") == 0)
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
								String buf = buff.substring(len,len+maxAllowedLen);
								
								String tmpBuff = "";
								//tmpBuff +="<a href=\"";
								tmpBuff +="<a href=";
								tmpBuff +=buff;
								//tmpBuff +="\">"+buf+"</a>";
								tmpBuff +=" target=_blank>"+buf+"</a>";
								
								parsedChatMessage +=tmpBuff+" ";
								
								len+=maxAllowedLen;
								continue;
							}
							else
							{
								String buf =buff.substring(len,buffLen);
								
								String tmpBuff = "";
								//tmpBuff +="<a href=\"";
								tmpBuff +="<a href=";
								tmpBuff +=buff;
								//tmpBuff +="\">"+buf+"</a>";
								tmpBuff +=" target=_blank>"+buf+"</a>";
								
								parsedChatMessage +=tmpBuff+" ";
																
								break;
							}						
						}
					}
					else
					{
						String buf = buff;
						
						String tmpBuff = "";
						//tmpBuff +="<a href=\"";
						tmpBuff +="<a href=";
						tmpBuff +=buff;
						//tmpBuff +="\">"+buf+"</a>";
						tmpBuff +=" target=_blank>"+buf+"</a>";
						
						parsedChatMessage +=tmpBuff+" ";
					}					
				}
				else if(buff.indexOf("https://") == 0)
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
								String buf = buff.substring(len,len+maxAllowedLen);
								
								String tmpBuff = "";
								//tmpBuff +="<a href=\"";
								tmpBuff +="<a href=";
								tmpBuff +=buff;
								//tmpBuff +="\">"+buf+"</a>";
								tmpBuff +=" target=_blank>"+buf+"</a>";
								
								parsedChatMessage +=tmpBuff+" ";
								
								len+=maxAllowedLen;
								continue;
							}
							else
							{
								String buf =buff.substring(len,buffLen);
								
								String tmpBuff = "";
								//tmpBuff +="<a href=\"";
								tmpBuff +="<a href=";
								tmpBuff +=buff;
								//tmpBuff +="\">"+buf+"</a>";
								tmpBuff +=" target=_blank>"+buf+"</a>";
								
								parsedChatMessage +=tmpBuff+" ";
																
								break;
							}						
						}
					}
					else
					{
						String buf = buff;
						
						String tmpBuff = "";
						//tmpBuff +="<a href=\"";
						tmpBuff +="<a href=";
						tmpBuff +=buff;
						//tmpBuff +="\">"+buf+"</a>";
						tmpBuff +=" target=_blank>"+buf+"</a>";
						
						parsedChatMessage +=tmpBuff+" ";
					}	
				}
				else if(buff.indexOf("www.") == 0)
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
								String buf = buff.substring(len,len+maxAllowedLen);
								
								String tmpBuff = "";
								//tmpBuff +="<a href=\"http://";
								tmpBuff +="<a href=http://";
								tmpBuff +=buff;
								//tmpBuff +="\">"+buf+"</a>";
								tmpBuff +=" target=_blank>"+buf+"</a>";
								
								parsedChatMessage +=tmpBuff+" ";
								
								len+=maxAllowedLen;
								continue;
							}
							else
							{
								String buf =buff.substring(len,buffLen);
								
								String tmpBuff = "";
								//tmpBuff +="<a href=\"http://";
								tmpBuff +="<a href=http://";
								tmpBuff +=buff;
								//tmpBuff +="\">"+buf+"</a>";
								tmpBuff +=" target=_blank>"+buf+"</a>";
								
								parsedChatMessage +=tmpBuff+" ";
																
								break;
							}						
						}
					}
					else
					{
						String buf = buff;
						
						String tmpBuff = "";
						//tmpBuff +="<a href=\"http://";
						tmpBuff +="<a href=http://";
						tmpBuff +=buff;
						//tmpBuff +="\">"+buf+"</a>";
						tmpBuff +=" target=_blank>"+buf+"</a>";
						
						parsedChatMessage +=tmpBuff+" ";
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
