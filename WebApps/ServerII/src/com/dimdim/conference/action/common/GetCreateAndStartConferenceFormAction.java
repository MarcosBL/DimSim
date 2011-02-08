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

package com.dimdim.conference.action.common;

import java.util.Date;

import com.dimdim.conference.action.CommonDimDimAction;
import com.dimdim.util.misc.StringGenerator;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */
public class GetCreateAndStartConferenceFormAction	extends	CommonGetFormAction
{
	private	static	StringGenerator	idGen = new StringGenerator();
	
	protected	String	email = "admin@dimdim.com";
	protected	String	confName	=	"Web Meeting "+(new Date()).toString();
	protected	String	confKey		=	GetCreateAndStartConferenceFormAction.idGen.generateRandomString(7,7);
	
	public	GetCreateAndStartConferenceFormAction()
	{
	}
	public	String	execute()	throws	Exception
	{
		this.servletRequest.setAttribute("email",this.email);
		this.servletRequest.setAttribute("confKey",this.confKey);
		this.servletRequest.setAttribute("action","host");
		
		ActionRedirectData ard = new ActionRedirectData("host",cflag,email,"",confKey,confName,"","");
		this.servletRequest.getSession().setAttribute(ActionRedirectData.SESSION_ATTRIBUTE_NAME,ard);
		
		return	super.execute();
	}
	public String getEmail()
	{
		return this.email;
	}
	public void setEmail(String email)
	{
		this.email = email;
	}
	public String getConfKey()
	{
		return this.confKey;
	}
	public void setConfKey(String confKey)
	{
		this.confKey = confKey;
	}
	public String getConfName()
	{
		return this.confName;
	}
	public void setConfName(String confName)
	{
		this.confName = confName;
	}
}
