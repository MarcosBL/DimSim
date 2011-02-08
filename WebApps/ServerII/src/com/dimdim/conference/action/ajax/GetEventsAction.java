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
 
package com.dimdim.conference.action.ajax;

import java.util.Date;
//import	java.util.Vector;
//import	java.util.Iterator;

//import com.dimdim.conference.ConferenceConstants;
import com.dimdim.conference.ConferenceConstants;
import com.dimdim.conference.action.HttpAwareConferenceAction;

import com.dimdim.conference.model.Event;
import	com.dimdim.conference.model.IConferenceParticipant;
import	com.dimdim.conference.model.IEventsProvider;
//import	com.dimdim.conference.model.Event;
//import	com.dimdim.conference.model.IJsonSerializable;

//import org.mortbay.util.ajax.Continuation;
//import org.mortbay.util.ajax.ContinuationSupport;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */
public class GetEventsAction	extends	HttpAwareConferenceAction
{
//	protected	String		featureId;
	protected	String		jsonBuffer = "[{a:\"b\"}]";
	
	public	GetEventsAction()
	{
	}
	public	String	doWork()	throws	Exception
	{
		String	ret = SUCCESS;
//		Vector ss = null;
		IConferenceParticipant user = this.userSession.getUser();
		if (user != null)
		{
//			System.out.println("GetEventsAction -- poll from: "+System.currentTimeMillis()+"\t::"+user.getId());
				IEventsProvider eventsProvider = user.getEventsProvider();
				/*
				{
					ss = eventsProvider.drainAllAvailableEvents();
				}
				if (ss != null && ss.size() >0)
				{
					this.jsonBuffer = makeJsonBuffer(ss);
					System.out.println("Found events:::::"+System.currentTimeMillis()+"::::::::::"+this.jsonBuffer);
					ret = SUCCESS;
				}
				*/
				//
			String buf = eventsProvider.drainAllAvailableEventsBuffer();
			if (buf != null)
			{
				this.jsonBuffer = buf;
//				System.out.println("Found events:::::"+System.currentTimeMillis()+"::::::::::"+this.jsonBuffer);
			}
				//
		}
		else
		{
//			System.out.println("GetEventsAction -- Error:: no user session");
//			setupLoginResponse();
			ret = ERROR;
		}
		return	ret;
	}
	/*
	protected	String	makeJsonBuffer(Vector ss)
	{
		StringBuffer buf = new StringBuffer();
		Iterator iter = ss.iterator();
		int	i=0;
		buf.append("[");
		while (iter.hasNext())
		{
			IJsonSerializable obj = (IJsonSerializable)iter.next();
			if (i>0)
			{
				buf.append(",");
			}
			buf.append(obj.toJson());
			i++;
		}
		buf.append("]");
		return	buf.toString();
	}
	*/
	/**
	 * This method will kick in when the client attempts to continue to
	 * call for events after a logout or a session timeout on server side.
	 * The login response for an event poll will send the conference closed
	 * event to the console. This will force the user to close the console.
	 * This event could be different from the conference closed event.
	 */
//	protected	void	setupLoginResponse()
//	{
//		Event event = new Event(ConferenceConstants.FEATURE_CONF,
//				"conf.serverSessionLost",
//				new Date(), ConferenceConstants.RESPONSE_OK, "This user session has expired. Please join the meeting again." );
//		
//		StringBuffer buf = new StringBuffer();
//		buf.append("[");
//		buf.append(event.toJson());
//		buf.append("]");
//		
//		this.jsonBuffer = buf.toString();
//	}
//	public String getFeatureId()
//	{
//		return this.featureId;
//	}
//	public void setFeatureId(String featureId)
//	{
//		this.featureId = featureId;
//	}
	public String getJsonBuffer()
	{
		return this.jsonBuffer;
	}
	public void setJsonBuffer(String jsonBuffer)
	{
		this.jsonBuffer = jsonBuffer;
	}
	protected	void	setupErrorResponse(String eventId, String eventMessage)
	{
		this.resultEvent = new Event(ConferenceConstants.FEATURE_CONF,eventId,new Date(),ConferenceConstants.RESPONSE_OK,eventMessage);
		StringBuffer buf = new StringBuffer();
		buf.append("[");
		buf.append(resultEvent.toJson());
		buf.append("]");
		this.jsonBuffer = buf.toString();
	}
}
