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

package com.dimdim.conference.model;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */
public class PresentationControlEvent  extends	MeetingEventData	implements IJsonSerializable
{
	public	static	String	START	=	"start";
	public	static	String	STOP	=	"stop";
	public	static	String	SLIDE	=	"slide";
	public	static	String	ANNOTATION_ON	=	"ann_on";
	public	static	String	ANNOTATION_OFF	=	"ann_off";
	
	protected	String	conferenceKey;
	protected	String	userId;
	protected	String	resourceId;
	protected	String	presentationId;
	protected	String	eventType;
	protected	Integer	slide;
	protected	Integer	showSlide;
	protected	String	annotation;
	
	public	static	PresentationControlEvent
		createStartEvent(String confKey, String presenterId, String resId, String presId, int slideCount, int showSlide,String annotation)
	{
		return	new	PresentationControlEvent(confKey, presenterId, resId, presId,
				PresentationControlEvent.START, new Integer(slideCount), new Integer(showSlide),annotation);
	}
	public	static	PresentationControlEvent
		createStopEvent(String confKey, String presenterId, String resId, String presId)
	{
		return	new	PresentationControlEvent(confKey, presenterId, resId, presId,
				PresentationControlEvent.STOP, new Integer(0),ANNOTATION_OFF);
	}
	public	static	PresentationControlEvent
		createShowSlideEvent(String confKey, String presenterId, String resId, String presId, int slideIndex,String annotation)
	{
		return	new	PresentationControlEvent(confKey, presenterId, resId, presId,
				PresentationControlEvent.SLIDE, new Integer(slideIndex),
				new Integer(slideIndex),annotation);
	}
	public	static	PresentationControlEvent
		createAnnotationEvent(String confKey, String presenterId, String resId, String presId, int slideIndex,
				String userId, boolean annotationsOn)
	{
		String	eventType = (annotationsOn)?PresentationControlEvent.ANNOTATION_ON:
			PresentationControlEvent.ANNOTATION_OFF;
		return	new	PresentationControlEvent(confKey, presenterId, resId, presId,
				eventType, new Integer(slideIndex), new Integer(slideIndex),eventType);
	}
//	public PresentationControlEvent()
//	{
//	}
	public PresentationControlEvent(String conferenceKey, String presenterId, String resourceId, String presentationId,
			String eventType, Integer slide, String annotation)
	{
		this(conferenceKey,presenterId,resourceId,presentationId,eventType,slide,new Integer(0),annotation);
	}
	public PresentationControlEvent(String conferenceKey, String presenterId, String resourceId,
			String presentationId, String eventType, Integer slide, Integer showSlide, String annotation)
	{
		super(presenterId);
		this.conferenceKey = conferenceKey;
		this.resourceId = resourceId;
		this.presentationId = presentationId;
		this.eventType = eventType;
		this.slide = slide;
		this.showSlide = showSlide;
		this.annotation = annotation;
	}
	
	public	String	toJson()
	{
		StringBuffer	buf = new StringBuffer();
		
		buf.append( "{" );
		buf.append( "\"objClass\":\""); buf.append("PresentationControlEvent"); buf.append("\",");
		buf.append( "\"conferenceKey\":\""); buf.append(this.conferenceKey); buf.append("\",");
		buf.append( "\"resourceId\":\""); buf.append(this.resourceId); buf.append("\",");
		buf.append( "\"presentationId\":\""); buf.append(this.presentationId); buf.append("\",");
		buf.append( "\"eventType\":\""); buf.append(this.eventType); buf.append("\",");
		buf.append( "\"slide\":\""); buf.append(this.slide); buf.append("\",");
		buf.append( "\"showSlide\":\""); buf.append(this.showSlide); buf.append("\",");
		buf.append( "\"annotation\":\""); buf.append(this.annotation); buf.append("\",");
		buf.append( "\"data\":\"dummy\"");
		buf.append( "}" );
		
		return	buf.toString();
	}
	
	public String getConferenceKey()
	{
		return conferenceKey;
	}
	public void setConferenceKey(String conferenceKey)
	{
		this.conferenceKey = conferenceKey;
	}
	public String getEventType()
	{
		return eventType;
	}
	public void setEventType(String eventType)
	{
		this.eventType = eventType;
	}
	public String getPresentationId()
	{
		return presentationId;
	}
	public void setPresentationId(String presentationId)
	{
		this.presentationId = presentationId;
	}
	public Integer getSlide()
	{
		return slide;
	}
	public void setSlide(Integer slide)
	{
		this.slide = slide;
	}
	public String getResourceId()
	{
		return resourceId;
	}
	public void setResourceId(String resourceId)
	{
		this.resourceId = resourceId;
	}
	public Integer getShowSlide()
	{
		return showSlide;
	}
	public void setShowSlide(Integer showSlide)
	{
		this.showSlide = showSlide;
	}
	public String getAnnotation()
	{
		return annotation;
	}
	public void setAnnotation(String annotation)
	{
		this.annotation = annotation;
	}
}
