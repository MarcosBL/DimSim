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

package com.dimdim.conference.ui.json.client;

import	com.google.gwt.json.client.JSONObject;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */
public class UIPresentationControlEvent extends UIObject
{
	public	static	final	String	START	=	"start";
	public	static	final	String	STOP	=	"stop";
	public	static	final	String	SLIDE	=	"slide";
	public	static	final	String	ANNOTATION_ON	=	"ann_on";
	public	static	final	String	ANNOTATION_OFF	=	"ann_off";
	
	protected	static	String	KEY_CONFERENCE_KEY = "conferenceKey";
	protected	static	String	KEY_RESOURCE_ID = "resourceId";
	protected	static	String	KEY_PRESENTATION_ID = "presentationId";
	protected	static	String	KEY_EVENT_TYPE = "eventType";
	protected	static	String	KEY_SLIDE = "slide";
	protected	static	String	KEY_SHOW_SLIDE = "showSlide";
	protected	static	String	KEY_ANNOTATION = "annotation";
	
	protected	String	conferenceKey;
	protected	String	resourceId;
	protected	String	presentationId;
	protected	String	eventType;
	protected	Integer	slide;
	protected	Integer	showSlide;
	protected	String	annotation = ANNOTATION_OFF;
	
	protected UIPresentationControlEvent()
	{
	}
	protected UIPresentationControlEvent(String conferenceKey, String resourceId,
			String presentationId, String eventType, Integer slide, Integer showSlide)
	{
		this.conferenceKey = conferenceKey;
		this.resourceId = resourceId;
		this.presentationId = presentationId;
		this.eventType = eventType;
		this.slide = slide;
		this.showSlide = showSlide;
	}
	public	static	UIPresentationControlEvent	parseJsonObject(JSONObject pceJson)
	{
		UIPresentationControlEvent pce = new UIPresentationControlEvent();
		
		pce.setConferenceKey(pceJson.get(KEY_CONFERENCE_KEY).isString().stringValue());
		pce.setResourceId(pceJson.get(KEY_RESOURCE_ID).isString().stringValue());
		pce.setPresentationId(pceJson.get(KEY_PRESENTATION_ID).isString().stringValue());
		pce.setEventType(pceJson.get(KEY_EVENT_TYPE).isString().stringValue());
		pce.setSlide(new Integer(pceJson.get(KEY_SLIDE).isString().stringValue()));
		pce.setShowSlide(new Integer(pceJson.get(KEY_SHOW_SLIDE).isString().stringValue()));
		pce.setAnnotation(pceJson.get(KEY_ANNOTATION).isString().stringValue());
		
		return	pce;
	}
	public	String	toJson()
	{
		StringBuffer buf = new StringBuffer("");
		
		buf.append("{");
		buf.append("objClass:\""); buf.append("PresentationControlEvent"); buf.append("\",");
		buf.append("conferenceKey:\""); buf.append(conferenceKey); buf.append("\",");
		buf.append("resourceId:\""); buf.append(resourceId); buf.append("\",");
		buf.append("presentationId:\""); buf.append(presentationId); buf.append("\",");
		buf.append("eventType:\""); buf.append(eventType); buf.append("\",");
		buf.append("slide:\""); buf.append(slide); buf.append("\",");
		buf.append("showSlide:\""); buf.append(showSlide); buf.append("\",");
		buf.append("annotation:\""); buf.append(annotation); buf.append("\",");
		buf.append("data:\""); buf.append("dummy"); buf.append("\"");
		buf.append("}");
		
		return	buf.toString();
	}
	public String toString()
	{
		return	toJson();
	}
	public String getConferenceKey()
	{
		return this.conferenceKey;
	}
	public void setConferenceKey(String conferenceKey)
	{
		this.conferenceKey = conferenceKey;
	}
	public String getEventType()
	{
		return this.eventType;
	}
	public void setEventType(String eventType)
	{
		this.eventType = eventType;
	}
	public String getPresentationId()
	{
		return this.presentationId;
	}
	public void setPresentationId(String presentationId)
	{
		this.presentationId = presentationId;
	}
	public Integer getSlide()
	{
		return this.slide;
	}
	public void setSlide(Integer slide)
	{
		this.slide = slide;
	}
	public String getResourceId()
	{
		return this.resourceId;
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
