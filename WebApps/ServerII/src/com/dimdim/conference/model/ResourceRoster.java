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
 *								                                          *
 * Copyright (c) 2006 Communiva Inc. All Rights Reserved.	              *
 *								                                          *
 *								                                          *
 * This code is licensed under the DimDim License                         *
 * For details please visit http://www.dimdim.com/license		          *
 *									                                      *
 **************************************************************************
 */

package com.dimdim.conference.model;

import java.util.Date;
import java.util.List;
import java.util.Vector;

import com.dimdim.util.misc.IDGenerator;
import com.dimdim.conference.ConferenceConstants;

/**
 * @author Jayant Pandit
 * @email  Jayant.Pandit@communiva.com
 * 
 * 28-Apr-08 - Got rid of the unused local event listeners. These are actually now
 * 	required for the recording and archiving, however they need to be implemented
 * 	common across all features that are affected by recording feature.
 */

public	class	ResourceRoster	extends	ConferenceFeature	implements	IResourceRoster
{
	
	private	ResourceObject	activeResourceObject;
	
	private	Vector	resources = new Vector();
	
	protected	IDGenerator	idGen = new IDGenerator("r");
	
	/**
	 * 
	 */
	public ResourceRoster()
	{
		super();
	}
	
	public	IResourceObject	getActiveResourceObject()
	{
		return	this.activeResourceObject;
	}
	public	void			setActiveResourceObject(IResourceObject ro)
	{
		this.activeResourceObject = (ResourceObject)ro;
	}
	public	IResourceObject	getResourceObject(String resourceId)
	{
		ResourceObject ro = null;
		int	size = this.resources.size();
		for (int i=0; i<size; i++)
		{
			ro = (ResourceObject)this.resources.get(i);
			if (ro.getResourceId().equals(resourceId))
			{
				break;
			}
			else
			{
				ro = null;
			}
		}
		return	ro;
	}
	public	synchronized	IResourceObject	removeResourceObject(String resourceId)
	{
		ResourceObject ro = null;
		int	size = this.resources.size();
		for (int i=0; i<size; i++)
		{
			ro = (ResourceObject)this.resources.get(i);
			if (ro.getResourceId().equals(resourceId))
			{
				this.resources.remove(ro);
				
				if (this.clientEventPublisher != null)
				{
					Event event = new Event(ConferenceConstants.FEATURE_RESOURCE_MANAGER,
							ConferenceConstants.EVENT_RESOURCE_DELETED,
							new Date(), ConferenceConstants.RESPONSE_OK, ro );
					
					this.clientEventPublisher.dispatchEventToAllClients(event);
				}
				
				break;
			}
			else
			{
				ro = null;
			}
		}
		return	ro;
	}
	public  synchronized	IResourceObject		addResourceObject(String name, String type, String ownerId)
	{
		return	this.addResourceObject(name,type,ownerId,null,null,0,0,0);
	}
	
	public  synchronized	IResourceObject		addResourceObject(String name, String type,
			String ownerId, String mediaId, String appHandle, int slideCount,int width, int height)
	{
		return	this.addResourceObject(name,type,ownerId,mediaId,appHandle,slideCount,width,height, null);
	}
	
	public  synchronized	IResourceObject		addResourceObject(String name, String type,
				String ownerId, String mediaId, String appHandle, int slideCount,int width, int height, String resourceId)
	{
		String id = ""; 
	
		if(resourceId == null)
		{
			id = this.idGen.generate();
		}else{
			id = resourceId;
		}
		
		if (mediaId == null)
		{
			mediaId = "";
		}
		ResourceObject newResource = new ResourceObject(id,name,type,mediaId,ownerId);
		newResource.setSlide(new Integer(slideCount));
		newResource.setWidth(new Integer(width));
		newResource.setHeight(new Integer(height));
		if (appHandle != null)
		{
			newResource.setAppHandle(appHandle);
		}
		this.resources.add(newResource);
		
		if (this.clientEventPublisher != null)
		{
			Event event = new Event(ConferenceConstants.FEATURE_RESOURCE_MANAGER,
					ConferenceConstants.EVENT_RESOURCE_ADDED,
					new Date(), ConferenceConstants.RESPONSE_OK, newResource );
			
			this.clientEventPublisher.dispatchEventToAllClients(event);
		}
		
		return	newResource;
	}
	
	public  synchronized	IResourceObject		addResourceObject(String name, String type,
			String ownerId, String mediaId, String resId, String appHandle, int slideCount,int width, int height)
{
		
	String id = "";
	id = this.idGen.generate();
	if(null != resId && resId.length() > 0)
	{
		id = resId;
	}
	
	if (mediaId == null)
	{
		mediaId = "";
	}
	ResourceObject newResource = new ResourceObject(id,name,type,mediaId,ownerId);
	newResource.setSlide(new Integer(slideCount));
	newResource.setWidth(new Integer(width));
	newResource.setHeight(new Integer(height));
	if (appHandle != null)
	{
		newResource.setAppHandle(appHandle);
	}
	this.resources.add(newResource);
	
	if (this.clientEventPublisher != null)
	{
		Event event = new Event(ConferenceConstants.FEATURE_RESOURCE_MANAGER,
				ConferenceConstants.EVENT_RESOURCE_ADDED,
				new Date(), ConferenceConstants.RESPONSE_OK, newResource );
		
		this.clientEventPublisher.dispatchEventToAllClients(event);
	}
	
	return	newResource;
}
	
	public  synchronized	IResourceObject	updateResourceObject(String resourceId, String name,
				String type, String mediaId, String appHandle, int slideCount)
	{
		ResourceObject ro = (ResourceObject)this.getResourceObject(resourceId);
		if (ro != null)
		{
			if (name != null)
			{
				ro.setResourceName(name);
			}
			if (type != null)
			{
				ro.setResourceType(type);
			}
			if (mediaId != null)
			{
				ro.setMediaId(mediaId);
			}
			if (appHandle != null)
			{
				ro.setAppHandle(appHandle);
			}
			if (slideCount > 0)
			{
				ro.setSlide(new Integer(slideCount));
			}
			
			if (this.clientEventPublisher != null)
			{
				Event event = new Event(ConferenceConstants.FEATURE_RESOURCE_MANAGER,
						ConferenceConstants.EVENT_RESOURCE_UPDATED,
						new Date(), ConferenceConstants.RESPONSE_OK, ro );
				
				this.clientEventPublisher.dispatchEventToAllClients(event);
			}
		}
		return	ro;
	}
	public  synchronized	IResourceObject	renameResourceObject(String resourceId, String name)
	{
		ResourceObject ro = (ResourceObject)this.getResourceObject(resourceId);
		if (ro != null)
		{
			ro.setResourceName(name);
			
			if (this.clientEventPublisher != null)
			{
				Event event = new Event(ConferenceConstants.FEATURE_RESOURCE_MANAGER,
						ConferenceConstants.EVENT_RESOURCE_RENAMED,
						new Date(), ConferenceConstants.RESPONSE_OK, ro );
				
				this.clientEventPublisher.dispatchEventToAllClients(event);
			}
		}
		return	ro;
	}
	public	List		getResources()
	{
		return	this.resources;
	}
	public	int		getNumberOfResources()
	{
		return	this.resources.size();
	}
	public	IResourceObject	getWhiteboardResource()
	{
		ResourceObject ro = null;
		int	size = this.resources.size();
		for (int i=0; i<size; i++)
		{
			ro = (ResourceObject)this.resources.get(i);
			if (ro.getResourceType().equalsIgnoreCase(ConferenceConstants.RESOURCE_TYPE_WHITEBOARD))
			{
				break;
			}
			else
			{
				ro = null;
			}
		}
		return	ro;
	}
}
