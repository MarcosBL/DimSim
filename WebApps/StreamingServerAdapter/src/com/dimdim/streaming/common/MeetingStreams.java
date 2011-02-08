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

package com.dimdim.streaming.common;

import	java.util.HashMap;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */

public class MeetingStreams
{
	protected	String	meetingKey;
	protected	HashMap	participantStreams;
	protected	int		totalNumberOfStreams;
	
	public	MeetingStreams(String meetingKey)
	{
		this.meetingKey = meetingKey;
		this.participantStreams = new HashMap();
		this.totalNumberOfStreams = 1;
	}
	public	void	addParticipant(String userId)
	{
		if (this.participantStreams.get(userId) == null)
		{
			this.participantStreams.put(userId,new ParticipantStreams(userId));
			this.totalNumberOfStreams++;
		}
	}
	public	void	removeParticipant(String userId)
	{
		if (this.participantStreams.get(userId) != null)
		{
			this.participantStreams.remove(userId);
			this.totalNumberOfStreams--;
		}
	}
	public	int	getTotalNumberOfStreams()
	{
		return	this.totalNumberOfStreams;
	}
}
