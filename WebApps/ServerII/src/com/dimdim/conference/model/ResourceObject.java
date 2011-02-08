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

import	java.util.ArrayList;
import	java.lang.Comparable;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 *
 */
public class ResourceObject  extends	MeetingEventData	implements IResourceObject
{
	public	static	final	String	ANNOTATION_ON	=	"ann_on";
	public	static	final	String	ANNOTATION_OFF	=	"ann_off";
	
	protected	String	resourceId;
	protected	String	resourceName;
	protected	String	resourceType;
	protected	String	mediaId="";
	protected	Integer	slide = new Integer(0);
	protected	String	appHandle="x";
	protected	ArrayList	pollOptions;
	protected	String	ownerUserId;
	protected	String	annotation = ANNOTATION_OFF;
	protected	Integer	width = new Integer(0);
	protected	Integer	height = new Integer(0);
	protected	Integer	lastSlideIndex = new Integer(0);
	
//	public	ResourceObject()
//	{
//	}
	public ResourceObject(String resourceId, String resourceName,
			String resourceType, String fileId, String ownerUserId)
	{
		super(ownerUserId);
		this.resourceId = resourceId;
		this.resourceName = resourceName;
		this.resourceType = resourceType;
		this.mediaId = fileId;
		this.ownerUserId = ownerUserId;
	}
	public ResourceObject(String resourceId, String resourceName,
			String resourceType, ArrayList pollOptions, String ownerUserId)
	{
		super(ownerUserId);
		this.resourceId = resourceId;
		this.resourceName = resourceName;
		this.resourceType = resourceType;
		this.pollOptions = pollOptions;
		this.ownerUserId = ownerUserId;
	}
//	public	ResourceObject	cloneObject(String resourceId)
//	{
//		ResourceObject res = new ResourceObject();
//		
//		res.setResourceId(resourceId);
//		res.setResourceName(this.getResourceName());
//		res.setResourceType(this.getResourceType());
//		res.setMediaId(this.getMediaId());
//		res.setSlide(this.getSlide());
//		res.setPollOptions(this.getPollOptions());
//		res.setOwnerUserId(this.getOwnerUserId());
//		
//		return	res;
//	}
	public	int	compareTo(Object o)
	{
		if (o instanceof ResourceObject)
		{
			return	this.resourceId.compareTo(((ResourceObject)o).getResourceId());
		}
		else
		{
			return	1;
		}
	}
	
	public	String	toJson()
	{
		StringBuffer	buf = new StringBuffer();
		
		buf.append( "{" );
		buf.append( "\"objClass\":\""); buf.append("ResourceObject"); buf.append("\",");
		buf.append( "\"resourceId\":\""); buf.append(this.resourceId); buf.append("\",");
		buf.append( "\"resourceName\":\""); buf.append(this.resourceName); buf.append("\",");
		buf.append( "\"resourceType\":\""); buf.append(this.resourceType); buf.append("\",");
		buf.append( "\"mediaId\":\""); buf.append(this.mediaId); buf.append("\",");
		buf.append( "\"slide\":\""); buf.append(this.slide); buf.append("\",");
		buf.append( "\"width\":\""); buf.append(this.width); buf.append("\",");
		buf.append( "\"height\":\""); buf.append(this.height); buf.append("\",");
		buf.append( "\"lastSlideIndex\":\""); buf.append(this.lastSlideIndex); buf.append("\",");
		buf.append( "\"ownerId\":\""); buf.append(this.ownerUserId); buf.append("\",");
		buf.append( "\"appHandle\":\""); buf.append(this.appHandle); buf.append("\",");
		buf.append( "\"annotation\":\""); buf.append(this.annotation); buf.append("\",");
		buf.append( "\"data\":\"dummy\"");
		buf.append( "}" );
		
		return	buf.toString();
	}
	
	public String getOwnerUserId()
	{
		return ownerUserId;
	}
	public void setOwnerUserId(String ownerUserId)
	{
		this.ownerUserId = ownerUserId;
	}
	public String getResourceId()
	{
		return resourceId;
	}
	public void setResourceId(String resourceId)
	{
		this.resourceId = resourceId;
	}
	public String getResourceName()
	{
		return resourceName;
	}
	public void setResourceName(String resourceName)
	{
		this.resourceName = resourceName;
	}
	public String getResourceType()
	{
		return resourceType;
	}
	public void setResourceType(String resourceType)
	{
		this.resourceType = resourceType;
	}
	public String getMediaId()
	{
		return mediaId;
	}
	public void setMediaId(String fileId)
	{
		this.mediaId = fileId;
	}
	public ArrayList getPollOptions()
	{
		return pollOptions;
	}
	public void setPollOptions(ArrayList pollOptions)
	{
		this.pollOptions = pollOptions;
	}
	public String getAppHandle()
	{
		return appHandle;
	}
	public void setAppHandle(String appHandle)
	{
		this.appHandle = appHandle;
	}
	public Integer getSlide()
	{
		return slide;
	}
	public void setSlide(Integer slide)
	{
		this.slide = slide;
	}
	public String getAnnotation()
	{
		return annotation;
	}
	public void setAnnotation(String annotation)
	{
		this.annotation = annotation;
	}
	public Integer getHeight()
	{
		return height;
	}
	public void setHeight(Integer height)
	{
		this.height = height;
	}
	public Integer getWidth()
	{
		return width;
	}
	public void setWidth(Integer width)
	{
		this.width = width;
	}
}
