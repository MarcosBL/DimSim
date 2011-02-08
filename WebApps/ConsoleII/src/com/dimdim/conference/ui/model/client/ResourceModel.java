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

import java.util.ArrayList;
import java.util.Iterator;

import com.dimdim.conference.ui.common.client.UIConstants;
import	com.dimdim.conference.ui.json.client.UIResourceObject;
import com.dimdim.conference.ui.json.client.UIRosterEntry;

import com.google.gwt.user.client.Window;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */
public class ResourceModel	extends	FeatureModel
{
//	public	static	final	Integer		ModelIndex	=	new Integer(7);
	public	static	final	String		ModelFeatureId	=	"feature.resource";
	
	protected	UIResourceObject	activeResource;
	
	public	ResourceModel()
	{
		super("feature.resource");
	}
	public	ArrayList	getResourceList()
	{
		return	this.objects;
	}
	/**
	 * Specific methods to provide a possibility for the UI design.
	 */
//	public	void	startPresentation(String resourceId, int startSlide)
//	{
//		String url = this.commandsFactory.getStartPresentationURL(resourceId,startSlide+"");
//		this.executeCommand(url);
//	}
//	public	void	stopPresentation(String resourceId)
//	{
//		String url = this.commandsFactory.getStopPresentationURL(resourceId);
//		this.executeCommand(url);
//	}
//	public	void	changePresentationSlide(String resourceId, int slideIndex)
//	{
//		String url = this.commandsFactory.getSlideChangeURL(resourceId, slideIndex);
//		this.executeCommand(url);
//	}
	/**
	 * Generic methods that match the conference package level action
	 * directly.
	 */
	public	void	createResource(String name, String type, String mediaId, String appHandle)
	{
		String url = this.commandsFactory.getCreateResourceURL(name,type,mediaId,appHandle);
//		Window.alert("invoking url = "+url);
		this.executeCommand(url);
	}
	
	public	void	createCobResource(String url, String confAddress)
	{
		String urlToExec = this.commandsFactory.getCreateCobResourceURL(url, confAddress);
//		Window.alert("invoking url = "+url);
		this.executeCommand(urlToExec);
	}
	
	public	void	updateResource(UIResourceObject res, String type, String mediaId, String appHandle)
	{
		String url = this.commandsFactory.getUpdateResourceURL(res,type,mediaId,appHandle);
		this.executeCommand(url);
	}
	public	void	cloneResource(UIResourceObject resource)
	{
		String url = this.commandsFactory.getCloneResourceURL(resource);
		this.executeCommand(url);
	}
	public	void	renameResource(UIResourceObject res, String name)
	{
		String	url = this.commandsFactory.getRenameResourceURL(res, name);
		this.executeCommand(url);
	}
	public	void	deleteResource(UIResourceObject res)
	{
		String	url = this.commandsFactory.getDeleteResourceURL(res);
		this.executeCommand(url);
	}
	public	void	showResource(UIResourceObject res)
	{
		String	url = this.commandsFactory.getShowResourceURL(res);
		this.executeCommand(url);
	}
	public	void	clearAnnotations(UIResourceObject res)
	{
		String	url = this.commandsFactory.getClearResourceAnnotationsURL(res);
		this.executeCommand(url);
	}
	
	public	void	getStartStreamURL(String name, String type, String mediaId, String appHandle)
	{
		String url = this.commandsFactory.getCreateResourceURL(name,type,mediaId,appHandle);
		//Window.alert("invoking url = "+url);
		this.executeCommand(url);
	}
	/*
	public static final String ACTION_IMPORT_RESOURCE = "action.importResource";
	*/
	
	public	void	onEvent(String eventId, Object data)
	{
//		Window.alert("Received event:"+data.toString());
		String eventMethod = eventId;
		int index = eventId.lastIndexOf(".");
		if (index > 0)
		{
			eventMethod = eventId.substring(index+1);
		}
//		Window.alert("triggering event:"+eventMethod);
//		actOnEvent(eventMethod,data);
		if (eventMethod.equalsIgnoreCase("added"))
		{
			onadded(data);
		}
		else if (eventMethod.equalsIgnoreCase("deleted"))
		{
			onremoved(data);
		}
		else if (eventMethod.equalsIgnoreCase("list"))
		{
			onresourceList(data);
		}
		else if (eventMethod.equalsIgnoreCase("updated"))
		{
			onupdated(data);
		}
		else if (eventMethod.equalsIgnoreCase("renamed"))
		{
			onrenamed(data);
		}
		else if (eventMethod.equalsIgnoreCase("selected"))
		{
			onselected(data);
		}
//		else if (eventMethod.equalsIgnoreCase("annotationAdded"))
//		{
//			onannotationAdded(data);
//		}
//		else if (eventMethod.equalsIgnoreCase("annotationsCleared"))
//		{
//			onannotationsCleared(data);
//		}
		// Window.alert("Trigger worked?");
	}
	public	void	onadded(Object data)
	{
//		Window.alert("Received event:"+data.toString());
		UIResourceObject newRes = (UIResourceObject)data;
		this.objects.add(newRes);
		Iterator iter = this.listeners.values().iterator();
		while (iter.hasNext())
		{
			((ResourceModelListener)iter.next()).onResourceAdded(newRes);
		}
	}
	public	void	onresourceList(Object data)
	{
		ArrayList oldResources = (ArrayList)data;
		int	size = oldResources.size();
		UIResourceObject resAtZero = null;
		for (int i=0; i<size; i++)
		{
			UIResourceObject tempResource = (UIResourceObject)oldResources.get(i);
			if(tempResource.getResourceType().equals(UIConstants.RESOURCE_TYPE_DESKTOP)
					|| tempResource.getResourceType().equals(UIConstants.RESOURCE_TYPE_WHITEBOARD))
			{
				resAtZero = (UIResourceObject)oldResources.get(0);
				oldResources.remove(i);
				oldResources.add(0, tempResource);
			}
		}
		for (int i=0; i<size; i++)
		{
			UIResourceObject oldResource = (UIResourceObject)oldResources.get(i);
			onadded(oldResource);
		}
		onlistloaded();
	}
	public	void	onlistloaded()
	{
//		Window.alert("Received event:"+data.toString());
		Iterator iter = this.listeners.values().iterator();
		while (iter.hasNext())
		{
			((ResourceModelListener)iter.next()).onResourceRosterLoaded();
		}
	}
	public	void	onremoved(Object data)
	{
		UIResourceObject res = (UIResourceObject)data;
		UIResourceObject ourEntry = this.findResourceObject(res.getResourceId());
		if (ourEntry != null)
		{
			this.objects.remove(ourEntry);
			Iterator iter = this.listeners.values().iterator();
			while (iter.hasNext())
			{
				((ResourceModelListener)iter.next()).onResourceRemoved(ourEntry);
			}
		}
	}
	public	void	onupdated(Object data)
	{
		UIResourceObject res = (UIResourceObject)data;
		UIResourceObject ourEntry = this.findResourceObject(res.getResourceId());
		if (ourEntry != null)
		{
			ourEntry.refreshWithChanges(res);
			Iterator iter = this.listeners.values().iterator();
			while (iter.hasNext())
			{
				((ResourceModelListener)iter.next()).onResourceUpdated(ourEntry);
			}
		}
	}
	public	void	onrenamed(Object data)
	{
		UIResourceObject res = (UIResourceObject)data;
		UIResourceObject ourEntry = this.findResourceObject(res.getResourceId());
		if (ourEntry != null)
		{
			ourEntry.refreshWithChanges(res);
			Iterator iter = this.listeners.values().iterator();
			while (iter.hasNext())
			{
				((ResourceModelListener)iter.next()).onResourceRenamed(ourEntry);
			}
		}
	}
	public	void	onselected(Object data)
	{
		UIResourceObject res = (UIResourceObject)data;
		UIResourceObject ourEntry = this.findResourceObject(res.getResourceId());
		if (ourEntry != null)
		{
//			UIResourceObject currentActive = this.activeResource;
			//	If the current active resource is same as new one do nothing.
			if (this.activeResource == null ||
					!this.activeResource.getResourceId().equals(res.getResourceId()))
			{
				this.activeResource = res;
				Iterator iter = this.listeners.values().iterator();
				while (iter.hasNext())
				{
					((ResourceModelListener)iter.next()).onResourceSelected(this.activeResource);
				}
			}
		}
	}
	public	void	setCurrentResourceUnselected()
	{
		this.activeResource = null;
	}
//	public	void	onannotationAdded(Object data)
//	{
////		String str = (UIResourceObject)data;
//		if (data != null)
//		{
//			Iterator iter = this.listeners.values().iterator();
//			while (iter.hasNext())
//			{
////				((ResourceModelListener)iter.next()).onAnnotationAdded(data);
//			}
//		}
//	}
//	public	void	onannotationsCleared(Object data)
//	{
////		String str = (UIResourceObject)data;
//		if (data != null)
//		{
//			Iterator iter = this.listeners.values().iterator();
//			while (iter.hasNext())
//			{
////				((ResourceModelListener)iter.next()).onAnnotationsCleared(data);
//			}
//		}
//	}
	protected	String		getPopoutJsonEventName()
	{
		return	"resources.list";
	}
	public	UIResourceObject	findResourceObject(String resourceId)
	{
		UIResourceObject res = null;
		int	size = this.objects.size();
		for (int i=0; i<size; i++)
		{
			res = (UIResourceObject)this.objects.get(i);
			if (res.getResourceId().equals(resourceId))
			{
				break;
			}
			else
			{
				res = null;
			}
		}
		return	res;
	}
	public	UIResourceObject	findResourceObject(String name,String type)
	{
	    //Window.alert("inside .... findResourceObject name = "+name);
		UIResourceObject res = null;
		int	size = this.objects.size();
		for (int i=0; i<size; i++)
		{
			res = (UIResourceObject)this.objects.get(i);
			if (res.getResourceName().equalsIgnoreCase(name) &&
					res.getResourceType().equalsIgnoreCase(type))
			{
				break;
			}
			else
			{
				res = null;
			}
		}
		//Window.alert("resource taht is returning after finding is "+res);
		return	res;
	}
	public	UIResourceObject	findResourceObjectByType(String type)
	{
	    //Window.alert("inside .... findResourceObject name = "+name);
		UIResourceObject res = null;
		int	size = this.objects.size();
		for (int i=0; i<size; i++)
		{
			res = (UIResourceObject)this.objects.get(i);
			if (res.getResourceType().equalsIgnoreCase(type))
			{
				break;
			}
			else
			{
				res = null;
			}
		}
		//Window.alert("resource taht is returning after finding is "+res);
		return	res;
	}
	
//	public	UIResourceObject findResourceObjectByMediaID(String mediaId)
//	{
//		UIResourceObject res = null;
//		int	size = this.objects.size();
//		for (int i=0; i<size; i++)
//		{
//			res = (UIResourceObject)this.objects.get(i);
//			if (res.getMediaId().equals(mediaId))
//			{
//				break;
//			}
//			else
//			{
//				res = null;
//			}
//		}
//		return	res;
//	}
}
