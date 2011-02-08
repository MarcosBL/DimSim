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

import java.util.Iterator;

import com.dimdim.conference.ui.json.client.UIRosterEntry;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * These events are purely local for a single client. These are for mainly
 * comunication and code orgnization simplification. These may seem to
 * overlap with some of the evets from other model, but there is no
 * connection between this model and any other model. Actualy there is no
 * similar connection between any other two feature models.
 */

public class LocalEventsModel	extends	FeatureModel
{
	public	static	final	String		ModelFeatureId	=	"feature.localEvent";
	
	protected	UIRosterEntry	me;
	
	public	LocalEventsModel(UIRosterEntry me)
	{
		super("feature.localEvent");
		
		this.me = me;
	}
	/**
	 * This event notifies the listeners that a new presentation has started
	 * on both presenter and attendee consoles. The signature contains the
	 * available and required height and width so that the listener parent
	 * panels can accordingly either resize or hide other components to make
	 * the presentation visible. This signature could later be extended. At
	 * present it contains only the information required.
	 */
	public	void	pptPresentationStarted(int availableWidth,
				int requiredWidth, int availableHeight, int requiredHeight)
	{
		Iterator iter = this.listeners.values().iterator();
		while (iter.hasNext())
		{
			((LocalEventsModelListener)iter.next()).pptPresentationStarted(
					availableWidth,requiredWidth,availableHeight,requiredHeight);
		}
	}
	public	void	pptPresentationStopped()
	{
		Iterator iter = this.listeners.values().iterator();
		while (iter.hasNext())
		{
			((LocalEventsModelListener)iter.next()).pptPresentationStopped();
		}
	}
	public	void	screenShareStarted(int availableWidth,
				int requiredWidth, int availableHeight, int requiredHeight)
	{
		Iterator iter = this.listeners.values().iterator();
		while (iter.hasNext())
		{
			((LocalEventsModelListener)iter.next()).screenShareStarted(
					availableWidth,requiredWidth,availableHeight,requiredHeight);
		}
	}
	public	void	screenShareStopped()
	{
		Iterator iter = this.listeners.values().iterator();
		while (iter.hasNext())
		{
			((LocalEventsModelListener)iter.next()).screenShareStopped();
		}
	}
	public	void	whiteboardStarted(int availableWidth,
				int requiredWidth, int availableHeight, int requiredHeight)
	{
		Iterator iter = this.listeners.values().iterator();
		while (iter.hasNext())
		{
			((LocalEventsModelListener)iter.next()).whiteboardStarted(
					availableWidth,requiredWidth,availableHeight,requiredHeight);
		}
	}
	public	void	whiteboardStopped()
	{
		Iterator iter = this.listeners.values().iterator();
		while (iter.hasNext())
		{
			((LocalEventsModelListener)iter.next()).whiteboardStopped();
		}
	}
}

