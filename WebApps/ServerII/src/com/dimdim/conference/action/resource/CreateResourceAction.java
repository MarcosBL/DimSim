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

import com.dimdim.conference.action.ConferenceAction;
import com.dimdim.conference.model.IConference;
import com.dimdim.conference.model.IConferenceParticipant;
import com.dimdim.conference.application.presentation.PresentationManager;
import com.dimdim.conference.model.Presentation;
import com.dimdim.conference.ConferenceConstants;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
	public static final String RESOURCE_TYPE_PRESENTATION = "resource.ppt";
	public static final String RESOURCE_TYPE_POLL = "resource.poll";
	public static final String RESOURCE_TYPE_WHITEBOARD = "resource.whiteboard";
	public static final String RESOURCE_TYPE_SCREEN_SHARE = "resource.screen";
	public static final String RESOURCE_TYPE_VIDEO = "resource.video";
	public static final String RESOURCE_TYPE_AUDIO = "resource.audio";
 */
public class CreateResourceAction		extends	ConferenceAction
{
	protected	String	name;
	protected	String	type;
	protected	String	mediaId;
	protected	String	slideCount;
	protected	String	appHandle;
	
	public	CreateResourceAction()
	{
	}
	public	String	doWork()	throws	Exception
	{
		String	ret = SUCCESS;
		IConference conf = this.userSession.getConference();
		IConferenceParticipant user = this.userSession.getUser();
		
			try
			{
				int	s = 0;
				int	width = 640;
				int	height = 480;
				if (slideCount != null)
				{
					s = Integer.parseInt(slideCount);
				}
				//	Create the new resource only if the mediaId is valid and present.
				//	For a presentation type resource, the media id must provide the
				//	presentation data. For desktop and video streams the media id is
				//	accepted as is. It is validated by the streaming server.
				if (mediaId != null)
				{
					if (this.type.equals(ConferenceConstants.RESOURCE_TYPE_PRESENTATION))
					{
						PresentationManager pmgr = PresentationManager.
							getPresentationManager(conf.getConfig().getConferenceKey());
						if (pmgr != null)
						{
							Presentation p = pmgr.getPresentation(mediaId);
							if (p!=null)
							{
								s = p.getSlideCount();
								if (p.getWidth() != -1)
								{
									width = p.getWidth();
								}
								if (p.getHeight() != -1)
								{
									height = p.getHeight();
								}
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
				
				conf.getResourceManager().getResourceRoster().addResourceObject(name,
						type,user.getId(),mediaId,appHandle,s,width,height);
			}
			catch(Exception e)
			{
				e.printStackTrace();
				ret = ERROR;
			}
			System.out.println("Returning ::::::::::"+ret);
		return	ret;
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
	public String getSlideCount()
	{
		return this.slideCount;
	}
	public void setSlideCount(String slideCount)
	{
		this.slideCount = slideCount;
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
