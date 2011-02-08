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

import java.util.Date;
import com.dimdim.conference.ConferenceConstants;
import com.dimdim.conference.model.Event;
import com.dimdim.conference.model.IResourceObject;
import com.dimdim.conference.model.IConferenceParticipant;
import com.dimdim.conference.model.PresentationControlEvent;
import com.dimdim.conference.model.ResourceObject;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * This class manages the current active presentation for the conference.
 * It handles all control messages. Each active resource manager will always
 * 
	public static final String ACTION_PRESENTATION_START = "action.pres.start";
	public static final String ACTION_PRESENTATION_FIRST_SLIDE = "action.pres.first";
	public static final String ACTION_PRESENTATION_NEXT_SLIDE = "action.pres.next";
	public static final String ACTION_PRESENTATION_PREVIOUS_SLIDE = "action.pres.previous";
	public static final String ACTION_PRESENTATION_LAST_SLIDE = "action.pres.last";
	public static final String ACTION_PRESENTATION_STOP = "action.pres.stop";
 */
public class ActivePresentationManager
{
	protected	ActiveResourceManager	activeResourceManager;
	
	protected	String	presentationId;
	protected	int		totalSlideCount;
	protected	int		currentSlideNumber = 0;
	
	public	ActivePresentationManager(ActiveResourceManager arm)
	{
		this.currentSlideNumber = 0;
		this.activeResourceManager = arm;
	}
	
	protected	void	startPresentation(IConferenceParticipant presenter,
			IResourceObject pres, int startSlide)
	{
		this.presentationId = pres.getMediaId();
		this.totalSlideCount = pres.getSlide().intValue();
		this.currentSlideNumber = startSlide;
		String annotation = ((ResourceObject)pres).getAnnotation();
		
		/**
		 * The presentation kick off is done from selection of the resource.
		 * Raise a resource selected event.
		 */
		Event event = new Event(ConferenceConstants.FEATURE_RESOURCE_MANAGER,
				ConferenceConstants.EVENT_RESOURCE_SELECTED,
				new Date(), ConferenceConstants.RESPONSE_OK, pres );
		this.activeResourceManager.getClientEventPublisher().dispatchEventToAllClientsExcept(event,presenter);
		
		PresentationControlEvent pce = PresentationControlEvent.createStartEvent(
			this.activeResourceManager.getConference().getConfig().getConferenceKey(),
			presenter.getId(),
			pres.getResourceId(),this.presentationId,this.totalSlideCount,startSlide,annotation);
		
		this.dispatchPresentationControlEvent(presenter, pce);
		
		PresentationControlEvent pce2 = PresentationControlEvent.createShowSlideEvent(
				this.activeResourceManager.getConference().getConfig().getConferenceKey(),
				presenter.getId(),
				pres.getResourceId(),this.presentationId,this.currentSlideNumber,annotation);
		
		this.dispatchPresentationControlEvent(presenter, pce2);
	}
	protected	void	stopPresentation(IConferenceParticipant presenter, IResourceObject pres)
	{
		PresentationControlEvent pce = PresentationControlEvent.createStopEvent(
			this.activeResourceManager.getConference().getConfig().getConferenceKey(),
			presenter.getId(),
			pres.getResourceId(),this.presentationId);
		
		this.dispatchPresentationControlEvent(presenter, pce);
	}
	protected	void	changeAnnotations(IConferenceParticipant presenter, IResourceObject ro,
					String userId, boolean annotationOn)
	{
		((ResourceObject)ro).setAnnotation((annotationOn)?ResourceObject.ANNOTATION_ON:
			ResourceObject.ANNOTATION_OFF);
		PresentationControlEvent pce = PresentationControlEvent.createAnnotationEvent(
				this.activeResourceManager.getConference().getConfig().getConferenceKey(),
				presenter.getId(),
				ro.getResourceId(),this.presentationId,this.currentSlideNumber,userId,annotationOn);
		
		this.dispatchPresentationControlEvent(presenter, pce, false);
	}
	protected	void	showSlide(IConferenceParticipant presenter, IResourceObject ro, String slideType, int slideNumber)
	{
		int	newSlideNumber = -1;
		if (this.presentationId != null)
		{
			if (slideType.equals(ConferenceConstants.ACTION_PRESENTATION_FIRST_SLIDE))
			{
				newSlideNumber = 0;
			}
			else if (slideType.equals(ConferenceConstants.ACTION_PRESENTATION_NEXT_SLIDE))
			{
				if (this.currentSlideNumber < this.totalSlideCount-1)
				{
					newSlideNumber = this.currentSlideNumber+1;
				}
			}
			else if (slideType.equals(ConferenceConstants.ACTION_PRESENTATION_PREVIOUS_SLIDE))
			{
				if (this.currentSlideNumber > 0)
				{
					newSlideNumber = this.currentSlideNumber-1;
				}
			}
			else if (slideType.equals(ConferenceConstants.ACTION_PRESENTATION_LAST_SLIDE))
			{
				newSlideNumber = this.totalSlideCount-1;
			}
			else if (slideType.equals(ConferenceConstants.ACTION_PRESENTATION_SLIDE_BY_NUMBER))
			{
				if (slideNumber >=0 && slideNumber <this.totalSlideCount)
				{
					newSlideNumber = slideNumber;
				}
			}
			if (newSlideNumber != this.currentSlideNumber)
			{
				this.currentSlideNumber = newSlideNumber;
				
				PresentationControlEvent pce = PresentationControlEvent.createShowSlideEvent(
						this.activeResourceManager.getConference().getConfig().getConferenceKey(),
						presenter.getId(),
						ro.getResourceId(),this.presentationId,this.currentSlideNumber,
						((ResourceObject)ro).getAnnotation());
				
				this.dispatchPresentationControlEvent(presenter, pce);
			}
		}
	}
	protected	void	dispatchPresentationControlEvent(IConferenceParticipant presenter,
			PresentationControlEvent pce)
	{
		this.dispatchPresentationControlEvent(presenter, pce, true);
	}
	protected	void	dispatchPresentationControlEvent(IConferenceParticipant presenter,
			PresentationControlEvent pce, boolean markControlMessage)
	{
		Event event = new Event(ConferenceConstants.FEATURE_PRESENTATION,
				ConferenceConstants.EVENT_PRESENTATION_CONTROL, new Date(),
				ConferenceConstants.RESPONSE_OK, pce );
		
		this.activeResourceManager.getClientEventPublisher().dispatchEventToAllClientsExcept(event,presenter);
		if (markControlMessage)
		{
			this.activeResourceManager.setLastControlMessage(event);
		}
	}
}
