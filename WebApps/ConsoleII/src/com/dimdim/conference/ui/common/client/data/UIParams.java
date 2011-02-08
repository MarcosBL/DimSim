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
 * These parameters are read from a file on server side, through the resource
 * dictionary writing tag that adds the resource file to the console data
 * page.
 */

public class UIParams extends StringsTable
{
	protected	static	UIParams	uiParams;
	
	public	static	UIParams	getUIParams()
	{
		if (UIParams.uiParams == null)
		{
			UIParams.uiParams = new UIParams();
		}
		return	UIParams.uiParams;
	}
	
	public	static	final	String	uiParamsTableName	=	"ui_params";
	
	public	static	final	String	maxParticipantsForAnyMeeting	=	"max_participants";
	public	static	final	String	maxMeetingTimeForAnyMeeting	=	"max_meeting_time";
	public	static	final	String	initialEventPollTimeMillis	=	"initial_event_poll_time";
	public	static	final	String	regularEventPollTimeMillis	=	"regular_event_poll_time";
	public	static	final	String	maxEventPollFailures	=	"max_event_poll_failures";
	
	public	UIParams()
	{
		super(UIParams.uiParamsTableName,
			new String[] {
				UIParams.maxParticipantsForAnyMeeting,
				UIParams.maxMeetingTimeForAnyMeeting,
				UIParams.initialEventPollTimeMillis,
				UIParams.regularEventPollTimeMillis,
				UIParams.maxEventPollFailures
		});
		
		this.maxParticipants = (new Integer((String)this.fields.get(UIParams.maxParticipantsForAnyMeeting))).intValue();
		this.maxMeetingTime = (new Integer((String)this.fields.get(UIParams.maxMeetingTimeForAnyMeeting))).intValue();
		this.initialEventPollTime = (new Integer((String)this.fields.get(UIParams.initialEventPollTimeMillis))).intValue();
		this.regularEventPollTime = (new Integer((String)this.fields.get(UIParams.regularEventPollTimeMillis))).intValue();
		this.maxEventFailures = (new Integer((String)this.fields.get(UIParams.maxEventPollFailures))).intValue();
	}
	
	protected	int	maxParticipants;
	protected	int	maxMeetingTime;
	protected	int	initialEventPollTime;
	protected	int	regularEventPollTime;
	protected	int	maxEventFailures;
	
	/**
	 * These following parameters are in a resource file, however they are
	 * sensitive parameters and significantly affect the behaviour of the console.
	 * These are in resource file for faster finer tuning than.
	 */
	public	int	getMaxParticipantsForAnyMeeting()
	{
		return	maxParticipants;//50;
	}
	public	int	getMaxMeetingTimeForAnyMeeting()
	{
		return	maxMeetingTime;//300;
	}
	public	int	getInitialEventPollDelayMillis()
	{
		return	initialEventPollTime;//1000;
	}
	public	int	getRegularEventPollIntervalMillis()
	{
		return	regularEventPollTime;//100;
	}
	public	int	getMaxEventPollErrors()
	{
		return	maxEventFailures;//300;
	}
	
	/**
	 * Generic method for accessing any arbitrary parameter by name.
	 */
	public	String	getStringParamValue(String name, String defaultValue)
	{
		String s = defaultValue;
		try
		{
			s = super.getFieldValue(UIParams.uiParamsTableName,name);
			if (s == null || s.equals(name))
			{
				s = defaultValue;
			}
		}
		catch(Exception e)
		{
			s = defaultValue;
		}
		return	s;
	}
	public	int	getIntParamValue(String name, int defaultValue)
	{
		int	v = defaultValue;
		try
		{
			String s = super.getFieldValue(UIParams.uiParamsTableName,name);
			if (s != null && !s.equals(name))
			{
				v = (new Integer(s)).intValue();
			}
		}
		catch(Exception e)
		{
			v = defaultValue;
		}
		return	v;
	}
	public	int	getBrowserParamIntValue(String paramName, int defaultValue)
	{
		String name = ConferenceGlobals.browserType+"_"+ConferenceGlobals.browserVersion+"_"+paramName;
		int v = this.getIntParamValue(name,-1);
		if (v == -1)
		{
			name = paramName;
			v = this.getIntParamValue(name, defaultValue);
		}
		return	v;
	}
	public	int	getWindowWidthAllowance()
	{
		return	getBrowserParamIntValue("window_width_allowance",0);
	}
	public	int	getWindowHeightAllowance()
	{
		return	getBrowserParamIntValue("window_height_allowance",0);
	}
	public	int	getMarginAllowance()
	{
		return	getBrowserParamIntValue("margin_allowance",0);
	}
	public	int	getChatTextAreaHeight()
	{
		return	getBrowserParamIntValue("chat_textarea_height", 60);
	}
//	public	int	getPrivateChatScrollPanelHeight()
//	{
//		return	getBrowserParamIntValue("private_chat_scrollpanel_height",225);
//	}
//	public	int	getPrivateChatScrollPanelWidth()
//	{
//		return	getBrowserParamIntValue("private_chat_scrollpanel_width",248);
//	}
//	public	int	getPublicChatWidthAllowance()
//	{
//		return	getBrowserParamIntValue("public_chat_width_allowance",28);
//	}
//	public	int	getPublicChatHeightAllowance()
//	{
//		return	getBrowserParamIntValue("public_chat_height_allowance",40);
//	}
}

