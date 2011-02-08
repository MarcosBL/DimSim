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

import java.net.URLDecoder;
import java.net.URLEncoder;

import com.dimdim.conference.ConferenceConsoleConstants;
import com.dimdim.conference.ConferenceConstants;
import com.dimdim.conference.application.core.ConferenceManager;
import com.dimdim.conference.application.core.RosterManager;
import com.dimdim.conference.model.IConference;
import com.dimdim.conference.model.IConferenceParticipant;
import com.dimdim.conference.model.IResourceObject;
import com.opensymphony.xwork.ActionSupport;


public class BookmarkCobResource		extends	ActionSupport
{
	protected	String	url;
	protected	String	dimdimID;
	protected	String	resourceID;
	protected	String	jsonBuffer;
	
	public	BookmarkCobResource()
	{
		System.out.println("inside creating BookmarkCobResource..");
	}
	public	String	execute()	throws	Exception
	{
		String	ret = SUCCESS;
		
		String resDisplayName = "test";
		String confKey = dimdimID;
		
		if (dimdimID != null && dimdimID.indexOf(ConferenceConsoleConstants.getInstallationPrefix()) != -1)
		{
			int confeKeyIndex = dimdimID.indexOf(ConferenceConsoleConstants.getInstallationPrefix())+
								ConferenceConsoleConstants.getInstallationPrefix().length();
			confKey = dimdimID.substring(confeKeyIndex, dimdimID.length());
		}
		 
		ConferenceManager confManager = ConferenceManager.getManager();
		IConference conf = confManager.getConferenceIfValid(confKey);
		RosterManager rm = (RosterManager)conf.getRosterManager();
		IConferenceParticipant activePresenter = rm.getUserRoster().getActivePresenter();
		
			try
			{
				int	s = 0;
				int	width = 640;
				int	height = 480;
				
				//	Create the new resource only if the url is valid and present.
				if (url != null)
				{
					String urlUnescaped  = URLDecoder.decode(url, "utf-8");
					resDisplayName = urlUnescaped;
				}
				
				String cobResourceId = resourceID;
				
				IResourceObject res = conf.getResourceManager().getResourceRoster().addResourceObject(resDisplayName,
							ConferenceConstants.RESOURCE_TYPE_COBROWSE, activePresenter.getId(), "11", null, s,width,height, cobResourceId);
				
				System.out.println("created cob res.."+res);
				jsonBuffer = "dimdimsl_cacheAdvisory.resourceBookmarked(true)";
				
			}
			catch(Exception e)
			{
				e.printStackTrace();
				jsonBuffer = "dimdimsl_cacheAdvisory.resourceBookmarked(false)";
				ret = ERROR;
			}
			System.out.println("Returning ::::::::::"+ret);
		return	ret;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getJsonBuffer() {
		return jsonBuffer;
	}
	public void setJsonBuffer(String jsonBuffer) {
		this.jsonBuffer = jsonBuffer;
	}
	public String getDimdimID() {
		return dimdimID;
	}
	public void setDimdimID(String dimdimID) {
		this.dimdimID = dimdimID;
	}
	public String getResourceID() {
		return resourceID;
	}
	public void setResourceID(String resourceID) {
		this.resourceID = resourceID;
	}
	
}
