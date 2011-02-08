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

import javax.servlet.http.HttpServletRequest;

import com.dimdim.conference.action.CommonDimDimAction;
import com.dimdim.conference.ConferenceConsoleConstants;
import com.dimdim.conference.UtilMethods;
import com.opensymphony.webwork.interceptor.ServletRequestAware;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * This generic error action is executed on any error. This could be triggered mostly
 * probably and most often by user trying to reload an intermmediate page. It will
 * simply show an error message and if possible a url to rejoin the meeting.
 */

public class ShowErrorAction	extends	CommonDimDimAction
		implements	ServletRequestAware
{
	protected	HttpServletRequest	servletRequest;
	
	protected	String	errorCode = "";
	protected	String	message = "Error";
	
	public	ShowErrorAction()
	{
	}
	public	String	execute()	throws	Exception
	{
		return	SUCCESS;
	}
	public void setServletRequest(HttpServletRequest servletRequest)
	{
		this.servletRequest = servletRequest;
	}
	public String getErrorCode()
	{
		return errorCode;
	}
	public void setErrorCode(String errorCode)
	{
		this.errorCode = errorCode;
	}
	public String getMessage()
	{
		return message;
	}
	public void setMessage(String message)
	{
		this.message = message;
	}
}
