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

package com.dimdim.conference.ui.model.client;

import	java.util.Vector;
import	com.google.gwt.user.client.Window;
import	com.dimdim.conference.ui.json.client.UIServerResponse;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * Objects of this class receive events from the local handler. This is
 * expected to be done for the purpose of forwarding these events to an
 * external object or component, such as a poped out window or windows.
 * The manner of use of the proxy objects is left to the higher level
 * user of the events.
 * 
 * Each events proxy has an event filter based on feature id. The filter
 * specifies the features which the user of the proxy is interested in.
 * The proxy is given to the local handler, which forwards all the events
 * to each proxy. The proxy forwards only the events for the specified
 * features to the final consumer. Since the consumers are expected to be
 * external, each proxy provides for only one consumer.
 */

public class EventsJsonProxy
{
	protected	EventsJsonConsumer	eventsConsumer;
	
	protected	Vector		featureIds;
	
	public	EventsJsonProxy(Vector featureIds, EventsJsonConsumer eventsConsumer)
	{
		this.eventsConsumer = eventsConsumer;
		this.featureIds = featureIds;
	}
	public	void	receiveEvent(UIServerResponse event)
	{
		//	A minor safety precaution.
		if (this.eventsConsumer != null)
		{
			if (this.featureIds == null || this.featureIds.contains(event.getFeatureId()))
			{
//				Window.alert("Forwarding event to proxy:"+event.toString());
				this.eventsConsumer.receiveEventText(event.getEventText());
//				this.eventsConsumer.receiveEvent(event);
				
				//	INCOMPLETE - forward the event text as well. This needs
				//	to be made available by the json parser in the event object.
			}
		}
	}
	public EventsJsonConsumer getEventsConsumer()
	{
		return eventsConsumer;
	}
	public void setEventsConsumer(EventsJsonConsumer eventsConsumer)
	{
		this.eventsConsumer = eventsConsumer;
	}
	public Vector getFeatureIds()
	{
		return featureIds;
	}
	public void setFeatureIds(Vector featureIds)
	{
		this.featureIds = featureIds;
	}
	public	void	addFeatureId(String featureId)
	{
		if (!this.featureIds.contains(featureId))
		{
			this.featureIds.addElement(featureId);
		}
	}
	public	void	removeFeatureId(String featureId)
	{
		this.featureIds.remove(featureId);
	}
}

