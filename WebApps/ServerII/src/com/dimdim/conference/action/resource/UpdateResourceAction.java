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

package com.dimdim.conference.action.resource;

import com.dimdim.conference.ConferenceConstants;
import com.dimdim.conference.action.ConferenceAction;
import com.dimdim.conference.application.presentation.PresentationManager;
import com.dimdim.conference.model.IConference;
import com.dimdim.conference.model.Presentation;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */
public class UpdateResourceAction		extends	ConferenceAction
{
	protected	String	resourceId;
	protected	String	name;
	protected	String	type;
	protected	String	mediaId;
	protected	String	slideCount;
	protected	String	appHandle;
	
	public	UpdateResourceAction()
	{
	}
	public	String	doWork()	throws	Exception
	{
		String	ret = SUCCESS;
		System.out.println("resourceId:"+resourceId);
		System.out.println("name:"+name);
		System.out.println("type:"+type);
		System.out.println("mediaId:"+mediaId);
		System.out.println("slideCount:"+slideCount);
		System.out.println("appHandle:"+appHandle);
		IConference conf = this.userSession.getConference();
			try
			{
				int	s = -1;
				if (slideCount != null)
				{
					s = Integer.parseInt(slideCount);
				}
				//	Update the resource only if the mediaId is valid and present.
				//	For a presentation type resource, the media id must provide the
				//	presentation data. For desktop and video streams the media id is
				//	accepted as is. It is validated by the streaming server.
				if (mediaId != null)
				{
					System.out.println("appHandle:"+appHandle);
					if (this.type.equals(ConferenceConstants.RESOURCE_TYPE_PRESENTATION))
					{
						PresentationManager pmgr = PresentationManager.
								getPresentationManager(conf.getConfig().getConferenceKey());
						if (pmgr != null)
						{
							System.out.println("Found presetation manager");
							Presentation p = pmgr.getPresentation(mediaId);
							if (p!=null)
							{
								System.out.println("Found Presentation");
								s = p.getSlideCount();
								String newName = p.getNiceNameFromOriginalFile();
								if (newName != null)
								{
									name = newName;
								}
								/*
								String fn = p.getOriginalFileName();
								if (fn != null)
								{
									String newName = fn;
									int slash = fn.lastIndexOf("\\");
									if (slash > 0)
									{
										newName = fn.substring(slash+1);
										int dot = newName.lastIndexOf(".");
										if (dot > 0)
										{
											newName = newName.substring(0,dot);
										}
									}
									name = newName;
								}
								*/
							}
						}
					}
				}
				
				System.out.println("name:"+name);
				System.out.println("slideCount:"+s);
				conf.getResourceManager().getResourceRoster().
					updateResourceObject(resourceId,name,type,mediaId,appHandle,s);
			}
			catch(Exception e)
			{
				e.printStackTrace();
				ret = ERROR;
			}
			System.out.println("Returning ::::::::::"+ret);
		return	ret;
	}
	public String getSlideCount()
	{
		return this.slideCount;
	}
	public void setSlideCount(String slideCount)
	{
		this.slideCount = slideCount;
	}
	public String getResourceId()
	{
		return this.resourceId;
	}
	public void setResourceId(String resourceId)
	{
		this.resourceId = resourceId;
	}
	public String getMediaId()
	{
		return this.mediaId;
	}
	public void setMediaId(String mediaId)
	{
		this.mediaId = mediaId;
	}
	public String getName()
	{
		return this.name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public String getType()
	{
		return this.type;
	}
	public void setType(String type)
	{
		this.type = type;
	}
	public String getAppHandle()
	{
		return appHandle;
	}
	public void setAppHandle(String appHandle)
	{
		this.appHandle = appHandle;
	}
}
