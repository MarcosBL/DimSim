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

import java.net.URLEncoder;

import org.json.JSONException;

import com.dimdim.conference.ConferenceConsoleConstants;
import com.dimdim.conference.ConferenceConstants;
import com.dimdim.conference.action.ConferenceAction;
import com.dimdim.conference.application.presentation.dms.CobURLHelper;
import com.dimdim.conference.model.IConference;
import com.dimdim.conference.model.IConferenceParticipant;
import com.dimdim.conference.model.IResourceObject;
import com.dimdim.util.misc.IDGenerator;

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
public class CreateCobResourceAction		extends	ConferenceAction
{
	protected	String	url;
	protected	String confAddress;
	public	CreateCobResourceAction()
	{
	}
	public	String	doWork()	throws	Exception
	{
		String	ret = SUCCESS;
		
		String resDisplayName = "test";
		
		IConference conf = this.userSession.getConference();
		IConferenceParticipant user = this.userSession.getUser();
		
			try
			{
				int	s = 0;
				int	width = 640;
				int	height = 480;
				
				//	Create the new resource only if the url is valid and present.
				if (url != null)
				{
					resDisplayName = url;
				}
				
				IDGenerator	idGen = new IDGenerator("r");
				String cobResourceId = idGen.generate();

				String urlToSend  = URLEncoder.encode(url, "utf-8");
				String confAddressToSend = URLEncoder.encode(confAddress, "utf-8");
				String dimdimIdWithInstallId = ConferenceConsoleConstants.getInstallationId()
												+ ConferenceConsoleConstants.getInstallationPrefix() 
												+ conf.getConferenceInfo().getKey();
				String args = "dimdimID="+dimdimIdWithInstallId+"&roomID="+conf.getConferenceInfo().getKey()
				+"&sessionID="+conf.getConferenceInfo().getKey()+"&encodedURL="+urlToSend
				+"&resourceID="+cobResourceId+"&resourceType=regular"+"&cflag="+System.currentTimeMillis();
				
				String retVal = "false";
				try{
					retVal = CobURLHelper.callCacheCob(args);
				}catch (Exception e) {
					// ugly, but has to be done for now
					try
					{
						retVal = CobURLHelper.callCacheCob(args);
					}
					catch(Exception ee)
					{
						retVal = "false";
					}
				}
				if("false".equalsIgnoreCase(retVal))
				{
					ret = ERROR;
					System.out.println("could not create cob res.."+cobResourceId);
				}else{
					IResourceObject res = conf.getResourceManager().getResourceRoster().addResourceObject(resDisplayName,
						ConferenceConstants.RESOURCE_TYPE_COBROWSE, user.getId(), "11", null, s,width,height, cobResourceId);
					System.out.println("created cob res.."+cobResourceId);
				}
				
			}
			catch(Exception e)
			{
				e.printStackTrace();
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
	public String getConfAddress() {
		return confAddress;
	}
	public void setConfAddress(String confAddress) {
		this.confAddress = confAddress;
	}
	
	
}
