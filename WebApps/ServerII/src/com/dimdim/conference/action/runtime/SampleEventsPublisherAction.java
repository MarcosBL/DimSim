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

package com.dimdim.conference.action.runtime;

import com.dimdim.conference.ConferenceConsoleConstants;
import com.dimdim.conference.application.UserSession;
import com.opensymphony.xwork.ActionSupport;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */
public class SampleEventsPublisherAction	extends	ActionSupport
{
	protected	String	jsonBuffer;
	protected	static	int	index = 0;
	protected	static	String[]	buffers = new String[]
	{
		"{\"featureId\":\"feature.roster\",\"eventId\":\"participant.joined\",\"time\":\"Mon May 22 23:51:54 EDT 2006\",\"from\":\"feature.conf\",\"data\":{\"userId\":\"John.A.Smith@communiva.com\",\"displayName\":\"John A Smith\",\"presence\":\"inmeeting\",\"mood\":\"normal\",\"role\":\"role.presenter\"}}",
		"{featureId:\"feature.roster\",eventId:\"participant.left\",time:\"Mon May 22 23:51:54 EDT 2006\",from:\"feature.conf\",data:{userId:\"John.A.Smith@communiva.com\",displayName:\"John A Smith\",presence:\"inmeeting\",mood:\"normal\",role:\"role.presenter\"}}",
		"{featureId:\"feature.roster\",eventId:\"participant.joined\",time:\"Mon May 22 23:51:54 EDT 2006\",from:\"feature.conf\",data:{userId:\"John.A.Smith@communiva.com\",displayName:\"John A Smith\",presence:\"inmeeting\",mood:\"normal\",role:\"role.presenter\"}}",
		"{featureId:\"feature.roster\",eventId:\"participant.joined\",time:\"Mon May 22 23:51:54 EDT 2006\",from:\"feature.conf\",data:{userId:\"John.B.Smith@communiva.com\",displayName:\"John B Smith\",presence:\"inmeeting\",mood:\"normal\",role:\"role.presenter\"}}",
		"{featureId:\"feature.roster\",eventId:\"participant.joined\",time:\"Mon May 22 23:51:54 EDT 2006\",from:\"feature.conf\",data:{userId:\"John.C.Smith@communiva.com\",displayName:\"John C Smith\",presence:\"inmeeting\",mood:\"normal\",role:\"role.presenter\"}}",
		"{featureId:\"feature.roster\",eventId:\"participant.left\",time:\"Mon May 22 23:51:54 EDT 2006\",from:\"feature.conf\",data:{userId:\"John.B.Smith@communiva.com\",displayName:\"John B Smith\",presence:\"inmeeting\",mood:\"normal\",role:\"role.presenter\"}}",
		"{featureId:\"feature.roster\",eventId:\"participant.joined\",time:\"Mon May 22 23:51:57 EDT 2006\",from:\"feature.conf\",data:{userId:\"John.D.Smith@communiva.com\",displayName:\"John D Smith\",presence:\"inmeeting\",mood:\"normal\",role:\"role.attendee\"}}"
	};
	
	public	SampleEventsPublisherAction()
	{
	}
	public	String	execute()	throws	Exception
	{
		SampleEventsPublisherAction.index++;
		if (SampleEventsPublisherAction.index >= SampleEventsPublisherAction.buffers.length)
		{
			SampleEventsPublisherAction.index = 0;
		}
		this.jsonBuffer = SampleEventsPublisherAction.buffers[SampleEventsPublisherAction.index];
		return	SUCCESS;
	}
	public String getJsonBuffer()
	{
		return this.jsonBuffer;
	}
	public void setJsonBuffer(String jsonBuffer)
	{
		this.jsonBuffer = jsonBuffer;
	}
}
