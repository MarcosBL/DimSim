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
 
package com.dimdim.conference.action.presentation;

import com.dimdim.conference.ConferenceConstants;
import com.dimdim.conference.action.ConferenceAction;
import com.dimdim.conference.application.core.ConferenceManager;
import com.dimdim.conference.model.IConference;
import com.dimdim.conference.application.core.NoConferenceByKeyException;
import com.dimdim.conference.model.IConferenceParticipant;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 *
 */
public class AudioStreamingFromFlashAction		extends	ConferenceAction
{
//	protected	String	confKey;
	protected	String	userId;
	protected	String	cmd;
	protected	String	streamId;
	
	public	AudioStreamingFromFlashAction()
	{
	}
	public	String	execute()	throws	Exception
	{
		String	ret = SUCCESS;
		IConference conf = null;
		IConferenceParticipant user = null;
		
		System.out.println("Audio control event: -"+cmd+"--"+streamId+"--"+confKey+"--"+userId);
		try
		{
			conf = ConferenceManager.getManager().getConference(confKey);
		}
		catch(NoConferenceByKeyException ee)
		{
			//	Conference not found. This should not happen
		}
		catch(Exception e)
		{
			//	Unexpected exception.
			e.printStackTrace();
		}
		if (conf != null)
		{
			user = conf.getParticipant(userId);
		}
		try
		{
			if (cmd != null && streamId != null && user != null)
			{
				if (cmd.equals(ConferenceConstants.EVENT_STREAM_CONTROL_START))
				{
					conf.getAudioVideoManager().startAudioStreaming(user,streamId);
				}
				else if (cmd.equals(ConferenceConstants.EVENT_STREAM_CONTROL_STOP))
				{
					conf.getAudioVideoManager().stopAudioStreaming(user,streamId);
				}
				else if (cmd.equals(ConferenceConstants.EVENT_STREAM_CONTROL_VOICE))
				{
					conf.getAudioVideoManager().audioVoiceStarted(user,streamId);
				}
				else
				{
					conf.getAudioVideoManager().audioControlCommand(user,streamId,cmd);
				}
			}
			else
			{
				System.out.println("###### Unable to raise audio control event");
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			ret = ERROR;
		}
		return	ret;
	}
	public String getCmd()
	{
		return cmd;
	}
	public void setCmd(String cmd)
	{
		this.cmd = cmd;
	}
	public String getStreamId()
	{
		return streamId;
	}
	public void setStreamId(String streamId)
	{
		this.streamId = streamId;
	}
//	public String getConfKey()
//	{
//		return confKey;
//	}
//	public void setConfKey(String confKey)
//	{
//		this.confKey = confKey;
//	}
	public String getUserId()
	{
		return userId;
	}
	public void setUserId(String userId)
	{
		this.userId = userId;
	}
}
