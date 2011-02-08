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

import java.text.DateFormat;
import java.util.Date;

import com.dimdim.conference.action.CommonDimDimAction;
import com.dimdim.util.misc.StringGenerator;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
Calendar.setup({
inputField : "ACSPMT_V_ARRIVAL",
displayArea : "ArrivalDisplay",
daFormat : "%d %B %Y",
ifFormat : "%Y%m%d",
dateStatusFunc: IsBeforeToday,
showsTime : false,
timeFormat : "24"
});

function IsBeforeToday(MyDate)
{
var Today = new Date();
if (MyDate < Today)
return true;
else
return false;
}

 */
public class GetCreateConferenceFormAction	extends	CommonGetFormAction
{
	private	static	StringGenerator	idGen = new StringGenerator();
	
	protected	String	orgnizerEmail = "";
	protected	String	organizerDisplayName = "";
	protected	String	organizerPasskey = "";
	protected	String	confName	=	"Web Meeting "+(new Date()).toString();
	protected	String	confKey		=	GetCreateConferenceFormAction.idGen.generateRandomString(8,8);
	protected	String	sendEmails = "false";
	protected	String	presenters = "";
	protected	String	attendees = "";
	protected	String	timeStr = "";
	protected	String	timeZone = "";
	
	public	GetCreateConferenceFormAction()
	{
		Date d = new Date();
//		d.setTime(d.getTime()+(1000*60*60*24));
		DateFormat df = DateFormat.getDateInstance(DateFormat.LONG);
		this.timeStr = df.format(d);
//		int space = this.timeStr.lastIndexOf(" ");
//		if (space > 0)
//		{
//			this.timeStr = this.timeStr.substring(0,space);
//		}
		System.out.println("Date:"+this.timeStr);
		confName	=	"Web Meeting "+this.timeStr;
	}
	public	String	execute()	throws	Exception
	{
		this.servletRequest.setAttribute("email",this.orgnizerEmail);
		this.servletRequest.setAttribute("confKey",this.confKey);
		this.servletRequest.setAttribute("action","host");
		
		ActionRedirectData ard = new ActionRedirectData("host",cflag,orgnizerEmail,"",confKey,confName,"","false");
		this.servletRequest.getSession().setAttribute(ActionRedirectData.SESSION_ATTRIBUTE_NAME,ard);
		
		return	super.execute();
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
	public String getAttendees()
	{
		return attendees;
	}
	public void setAttendees(String attendees)
	{
		this.attendees = attendees;
	}
	public String getOrganizerDisplayName()
	{
		return organizerDisplayName;
	}
	public void setOrganizerDisplayName(String organizerDisplayName)
	{
		this.organizerDisplayName = organizerDisplayName;
	}
	public String getOrganizerPasskey()
	{
		return organizerPasskey;
	}
	public void setOrganizerPasskey(String organizerPasskey)
	{
		this.organizerPasskey = organizerPasskey;
	}
	public String getOrgnizerEmail()
	{
		return orgnizerEmail;
	}
	public void setOrgnizerEmail(String orgnizerEmail)
	{
		this.orgnizerEmail = orgnizerEmail;
	}
	public String getPresenters()
	{
		return presenters;
	}
	public void setPresenters(String presenters)
	{
		this.presenters = presenters;
	}
	public String getTimeStr()
	{
		return timeStr;
	}
	public void setTimeStr(String timeStr)
	{
		this.timeStr = timeStr;
	}
	public String getSendEmails()
	{
		return sendEmails;
	}
	public void setSendEmails(String sendEmails)
	{
		this.sendEmails = sendEmails;
	}
	public String getTimeZone()
	{
		return timeZone;
	}
	public void setTimeZone(String timeZone)
	{
		this.timeZone = timeZone;
	}
}
