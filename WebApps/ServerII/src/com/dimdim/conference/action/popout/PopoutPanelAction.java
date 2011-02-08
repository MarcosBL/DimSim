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

package com.dimdim.conference.action.popout;

import com.dimdim.conference.ConferenceConsoleConstants;
import com.dimdim.conference.action.HttpAwareConferenceAction;
import com.dimdim.conference.application.UserSession;
import com.dimdim.conference.model.IConference;
import com.dimdim.conference.model.IConferenceParticipant;
import com.dimdim.data.application.UIDataManager;
import com.dimdim.locale.LocaleResourceFile;
import com.dimdim.util.session.UserInfo;
import com.dimdim.util.session.UserRequest;
import com.dimdim.util.session.UserSessionDataManager;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * Return of the popout panel action depends on the panel being poped out,
 * as the page to display in the poped out window depends on it. In case
 * of popin there is no difference. The client will close the poped out
 * window on closing.
 * 
 * Supported panel ids at present are:
 * 
 * WORKSPACE
 * DEBUG
 * PRIVATE_CHAT
 */

public class PopoutPanelAction	extends	HttpAwareConferenceAction
{
	public	static	final	String	PANEL_ID_ATTRIBUTE	=	"PANEL_ID";
	public	static	final	String	USER_ID_ATTRIBUTE	=	"USER_ID";
	
	protected	String	panelId;
	protected	String	userId;
	protected	String	userType = ConferenceConsoleConstants.getUserTypeFreeOrPaid();
	
	public	PopoutPanelAction()
	{
		
	}
	public	String	doWork()	throws	Exception
	{
		String	ret = SUCCESS;
		
		IConference conf = this.userSession.getConference();
		IConferenceParticipant user = this.userSession.getUser();
		try
		{
			if (panelId != null)
			{
				//	INCOMPLETE.
				ret = panelId;
				this.servletRequest.setAttribute("sessionKey",this.sessionKey);
				
				String browserType = (String)session.get(ConferenceConsoleConstants.BROWSER_TYPE);
				String browserVersion = (String)session.get(ConferenceConsoleConstants.BROWSER_VERSION);
				
				String userInfo = this.userSession.getUserInfo(sessionKey,
						this.servletRequest.getLocale(),
						browserType, browserVersion,
						servletRequest.getRequestURL().toString().startsWith("htts"));
				String	dataCacheId = this.userSession.getDataCacheId();
//				session.put("user_info", userInfo);
				UIDataManager.getUIDataManager().addSessionDataBuffer("user_info"+dataCacheId, userInfo);
				this.servletRequest.setAttribute("dataCacheId", dataCacheId);
				
//				setting up uer type which will be used for resource bundles 
				this.servletRequest.setAttribute("userType",userType);
				
				
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			ret = ERROR;
		}
		return	ret;
	}
	public String getPanelId()
	{
		return panelId;
	}
	public void setPanelId(String panelId)
	{
		this.panelId = panelId;
	}
	public String getUserId()
	{
		return userId;
	}
	public void setUserId(String userId)
	{
		this.userId = userId;
	}
	public String getUserType()
	{
	    return userType;
	}
	public void setUserType(String userType)
	{
	    if(LocaleResourceFile.PREMIUM.equals(userType)
		    || LocaleResourceFile.PREMIUM.equals(userType))
	    this.userType = userType;
	}
}
