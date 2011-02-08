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
import java.util.Vector;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HorizontalPanel;

import com.dimdim.conference.ui.json.client.UIPresentationControlEvent;
import com.dimdim.conference.ui.json.client.UIRosterEntry;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */
public class PPTSharingModel	extends	FeatureModel
{
	public	static	final	String		ModelFeatureId	=	"feature.presentation";
	
	protected	UIRosterEntry	me;
	protected	int		currentSlideIndex = -1;
	protected	Vector	shareControlPanels;
	protected	UIPresentationControlEvent	lastStartEvent;
	
	public	PPTSharingModel(UIRosterEntry me)
	{
		super("feature.presentation");
		
		this.me = me;
		this.shareControlPanels = new Vector();
	}
	public	Vector	getShareControlPanels()
	{
		return	this.shareControlPanels;
	}
	public	void	addShareControlPanel(HorizontalPanel panel)
	{
		this.shareControlPanels.add(panel);
	}
	/**
	 * Specific methods to provide a possibility for the UI design.
	 */
	public	void	startPresentation(String resourceId, int startSlide)
	{
		currentSlideIndex = startSlide;
		//Window.alert("inside pptsharing model... startPresentation slideIndex = "+startSlide);
		String url = this.commandsFactory.getStartPresentationURL(resourceId,startSlide+"");
		this.executeCommand(url);
	}
	public	void	stopPresentation(String resourceId)
	{
		currentSlideIndex = -1;
		//Window.alert("inside pptsharing model... stopPresentation resourceId = "+resourceId);
		String url = this.commandsFactory.getStopPresentationURL(resourceId);
		this.executeCommand(url);
	}
	public	void	changePresentationSlide(String resourceId, int slideIndex)
	{
		currentSlideIndex = slideIndex;
		//Window.alert("inside pptsharing model... changePresentationSlide slideIndex = "+slideIndex);
		String url = this.commandsFactory.getSlideChangeURL(resourceId, slideIndex);
		this.executeCommand(url);
	}
	public	void	EnableAnnotations(String resourceId)
	{
		//Window.alert("inside pptsharing model... changePresentationSlide slideIndex = "+slideIndex);
		String url = this.commandsFactory.getEnableAnnotationsURL(resourceId);
		this.executeCommand(url);
	}
	public	void	DisableAnnotations(String resourceId)
	{
		//Window.alert("inside pptsharing model... changePresentationSlide slideIndex = "+slideIndex);
		String url = this.commandsFactory.getDisableAnnotationsURL(resourceId);
		this.executeCommand(url);
	}
	public	void	onEvent(String eventId, Object data)
	{
//		Window.alert(eventId+":"+data.toString());
//		if (this.me.isHost())
//		{
//			return;
//		}
		if (data != null)
		{
			UIPresentationControlEvent	event = (UIPresentationControlEvent)data;
			if (event.getEventType().equals(UIPresentationControlEvent.START))
			{
				currentSlideIndex = 0;
				this.onStartPPT(event);
				this.lastStartEvent = event;
				this.objects.add(this.lastStartEvent);
			}
			else if (event.getEventType().equals(UIPresentationControlEvent.STOP))
			{
//				Window.alert("Share stopped for resource: "+event.getResourceId());
				currentSlideIndex = -1;
				this.onStopPPT(event);
				this.objects.remove(this.lastStartEvent);
				this.lastStartEvent = null;
			}
			else if (event.getEventType().equals(UIPresentationControlEvent.SLIDE))
			{
				currentSlideIndex = event.getSlide().intValue();
				this.onSlideChanged(event);
			}
			else if (event.getEventType().equals(UIPresentationControlEvent.ANNOTATION_ON))
			{
//				Window.alert("Annotations enabled for resource: "+event.getResourceId());
				this.annotationEnabled(event);
			}
			else if (event.getEventType().equals(UIPresentationControlEvent.ANNOTATION_OFF))
			{
//				Window.alert("Annotations disabled for resource: "+event.getResourceId());
				this.annotationDisabled(event);
			}
		}
	}
	protected	void	onStartPPT(UIPresentationControlEvent event)
	{
		Iterator iter = this.listeners.values().iterator();
		while (iter.hasNext())
		{
//			Window.alert("Share started for resource: "+event.getResourceId());
			((PPTSharingModelListener)iter.next()).startPresentation(event);
		}
	}
	protected	void	onSlideChanged(UIPresentationControlEvent event)
	{
		Iterator iter = this.listeners.values().iterator();
		while (iter.hasNext())
		{
//			Window.alert("Slide changed for resource: "+event.getResourceId());
			((PPTSharingModelListener)iter.next()).slideChanged(event);
		}
	}
	protected	void	annotationEnabled(UIPresentationControlEvent event)
	{
		Iterator iter = this.listeners.values().iterator();
		while (iter.hasNext())
		{
//			Window.alert("Slide changed for resource: "+event.getResourceId());
			((PPTSharingModelListener)iter.next()).annotationsEnabled(event);
		}
	}
	protected	void	annotationDisabled(UIPresentationControlEvent event)
	{
		Iterator iter = this.listeners.values().iterator();
		while (iter.hasNext())
		{
//			Window.alert("Slide changed for resource: "+event.getResourceId());
			((PPTSharingModelListener)iter.next()).annotationsDisabled(event);
		}
	}
	protected	void	onStopPPT(UIPresentationControlEvent event)
	{
		Iterator iter = this.listeners.values().iterator();
		while (iter.hasNext())
		{
			((PPTSharingModelListener)iter.next()).stopPresentation(event);
		}
	}
	public	int	getCurrentSlideIndex()
	{
		if (currentSlideIndex < 0)
		{
			return	0;
		}
		return	currentSlideIndex;
	}
	public	void	setCurrentSlideIndex(int i)
	{
		currentSlideIndex = i;
		if (currentSlideIndex < 0)
		{
			currentSlideIndex =	0;
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
