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

package com.dimdim.conference.application.presentation;

import com.dimdim.conference.ConferenceConstants;
//import com.dimdim.conference.application.presentation.dms.DMSPresentationManager;
import com.dimdim.conference.application.presentation.dms.DMSResource;
import com.dimdim.conference.application.presentation.dms.DMSResourceSet;
import com.dimdim.conference.application.presentation.dms.URLHelper;
import com.dimdim.conference.model.IConference;
import com.dimdim.conference.model.Presentation;
import com.dimdim.util.misc.StringGenerator;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * This simple utility class does the work of attaching default presentations
 * subject to availability and include / exclude specifications from the
 * show_items.properties file.
 */

public class DefaultPresentationsManager
{
	private	static	StringGenerator	idGen = new StringGenerator();
	
	public	DefaultPresentationsManager()
	{
	}
	public	void	addDefaultItemsToMeeting(IConference conf, String desktopResName, String whiteboardResName,
												String coBrowseResName)
	{
		String confKey = conf.getConfig().getConferenceKey();
		PresentationManager pm = PresentationManager.getPresentationManager(confKey);
		if (pm != null)
		{
			Presentation[] gps = pm.getPresentations();
			for (int i=0; i<gps.length; i++)
			{
				Presentation p = gps[i];
				String id = p.getPresentationId();
				System.out.println("adding presentation with id "+id + "to meeting id = "+p.getMeetingId());
				if (p.getMeetingId().equals(confKey))
				{
					//System.out.println("adding this"); 
					String name = p.getNiceNameFromOriginalFile();
					conf.getResourceManager().getResourceRoster().addResourceObject(name,
						ConferenceConstants.RESOURCE_TYPE_PRESENTATION,
						PresentationManager.PreloadedDefaultOwnerId,id,null,
						p.getSlideCount(),p.getWidth(),p.getHeight());
				}
			}
		}
		DMSResourceSet resSet = URLHelper.getResourceSet(conf, desktopResName, whiteboardResName, coBrowseResName);
		for(int i = 0; i < resSet.getResources().size(); i++)
		{
			DMSResource resource = (DMSResource)resSet.getResources().get(i);
			if(resource.isEnabled())
			{
				if(resource.getResourceType().equals(ConferenceConstants.RESOURCE_TYPE_COBROWSE))
				{
					conf.getResourceManager().getResourceRoster().addResourceObject(resource.getResourceName(),
							resource.getResourceType(),
							PresentationManager.PreloadedDefaultOwnerId, resource.getMediaId(), resource.getResId(), resource.getAppHandle(),0,0,0);
				}else{
					String id = DefaultPresentationsManager.idGen.generateRandomString(9,9);
					conf.getResourceManager().getResourceRoster().addResourceObject(resource.getResourceName(),
						resource.getResourceType(),
						PresentationManager.SystemDefaultOwnerId,id,null,0,0,0);
				}
			}
		}
	}
}

