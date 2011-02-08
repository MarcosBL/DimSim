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

import java.util.HashMap;
import java.util.Iterator;

import	com.dimdim.conference.ui.json.client.UIRosterEntry;
import	com.dimdim.conference.ui.json.client.UIStreamControlEvent;
import	com.dimdim.conference.ui.json.client.JSONurlReader;
import com.google.gwt.user.client.Window;

import com.google.gwt.user.client.ui.HorizontalPanel;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * The sharing process is distributed over the gwt based html / js content and
 * flash movies content. Sometimes the controls may have to be shared and
 * cross communicated. The control panels maintained here are simple containers
 * which the console layout is responsible for placing in the layout and the
 * resource sharing players and controllers are responsible for populating the
 * area with appropriate labels, images and links. The resource types are
 * defined only as needed.
 * 
 * The layout is responsible for creating and placing the panels in the page.
 * Sharing controllers will add their own controls to these panels if the
 * panels are available. This allows the layout to put the controls in multiple
 * places, and the share controls do not have to worry about the placement
 * of the controls as long as they stay within the specified size. 
 */

public class SharingModel	extends	FeatureModel
{
	public	static	final	String		ModelFeatureId	=	"feature.sharing";
	
	public	static	final	String		pptShare = "ppt";
	
	protected	UIRosterEntry	me;
	protected	HashMap			shareControlPanels;
	protected	UIStreamControlEvent	lastStartEvent;
	
	public	SharingModel(UIRosterEntry me)
	{
		super("feature.sharing");
		
		this.me = me;
		shareControlPanels = new HashMap();
	}
	public	void	setPptShareControlPanel(HorizontalPanel panel)
	{
		this.addShareControlPanel(SharingModel.pptShare,panel);
	}
	public	HorizontalPanel	getPptShareControlPanel()
	{
		return	getShareControlPanelFor(SharingModel.pptShare);
	}
	private	void	addShareControlPanel(String shareType, HorizontalPanel panel)
	{
		shareControlPanels.put(shareType,panel);
	}
	private	HorizontalPanel	getShareControlPanelFor(String resourceType)
	{
		return	(HorizontalPanel)this.shareControlPanels.get(resourceType);
	}
	/*
	public	void	startSharePublish(String mediaId)
	{
		String url = this.commandsFactory.getStartVideoURL(ConferenceGlobals.conferenceKey,
				this.me.getUserId(),mediaId);
		this.executeCommand(url);
	}
	public	void	stopSharePublish(String mediaId)
	{
		String url = this.commandsFactory.getStopVideoURL(ConferenceGlobals.conferenceKey,
				this.me.getUserId(),mediaId);
		this.executeCommand(url);
	}
	*/
	public	void	sharedWindowChanged(String resourceId)
	{
		String url = this.commandsFactory.getSharedWindowChangedURL(resourceId);
		this.executeCommand(url);
	}
	public	void	onEvent(String eventId, Object data)
	{
//		Window.alert(eventId);
		if (data != null)
		{
//			Window.alert(data.toString());
			UIStreamControlEvent	event = (UIStreamControlEvent)data;
			if (event.getEventType().equals(UIStreamControlEvent.START))
			{
				this.onStartStream(event);
				this.lastStartEvent = event;
				this.objects.add(this.lastStartEvent);
			}
			else if (event.getEventType().equals(UIStreamControlEvent.STOP))
			{
				this.onStopStream(event);
				this.objects.remove(this.lastStartEvent);
				this.lastStartEvent = null;
			}
			else if (event.getEventType().equals(UIStreamControlEvent.CHANGE))
			{
				this.onChangeShare(event);
				if (this.lastStartEvent != null)
				{
					this.lastStartEvent.setResourceId(event.getResourceId());
				}
			}
		}
		else
		{
//			Window.alert("No data for stream sharing event");
		}
	}
	protected	void	onStartStream(UIStreamControlEvent event)
	{
		Iterator iter = this.listeners.values().iterator();
		while (iter.hasNext())
		{
			((SharingModelListener)iter.next()).onStartSharing(event);
		}
	}
	protected	void	onStopStream(UIStreamControlEvent event)
	{
		Iterator iter = this.listeners.values().iterator();
		while (iter.hasNext())
		{
			((SharingModelListener)iter.next()).onStopSharing(event);
		}
	}
	protected	void	onChangeShare(UIStreamControlEvent event)
	{
		Iterator iter = this.listeners.values().iterator();
		while (iter.hasNext())
		{
			((SharingModelListener)iter.next()).onChangeShare(event);
		}
	}
	/**
	 * Popout support
	 */
	protected	String		getPopoutJsonEventName()
	{
		return	"sharing.control";
	}
	protected	String		getPopoutJsonEventDataType()
	{
		return	"object";
	}
}

