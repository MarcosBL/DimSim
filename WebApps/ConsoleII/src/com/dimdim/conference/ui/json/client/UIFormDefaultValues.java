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
 
package com.dimdim.conference.ui.json.client;

import com.google.gwt.user.client.Window;
import	com.google.gwt.json.client.JSONObject;
//
//import java.util.Date;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */
public class UIFormDefaultValues
{
	private	static	final	String	FORM_KEY_ACTION_ID	=	"actionId";
	private	static	final	String	FORM_KEY_EMAIL	=	"email";
	private	static	final	String	FORM_KEY_DISPLAY_NAME	=	"displayName";
	private	static	final	String	FORM_KEY_CONF_KEY	=	"confKey";
	private	static	final	String	FORM_KEY_CONF_NAME	=	"confName";
	private	static 	final	String	FORM_KEY_TOLL = "toll";
	private	static 	final	String	FORM_KEY_TOLL_FREE = "tollFree";
	private	static 	final	String	FORM_KEY_INTERN_TOLL = "internToll";
	private	static 	final	String	FORM_KEY_INTERN_TOLL_FREE = "internTollFree";
	private	static 	final	String	FORM_KEY_MODERATOR_PASSCODE = "moderatorPassCode";
	private	static 	final	String	FORM_KEY_ATTENDEE_PASSCODE = "attendeePasscode";
	
	protected	String	actionId;
	protected	String	email;
	protected	String	displayName;
	protected	String	confKey;
	protected	String	confName;
	protected	String	tollFree;
	protected	String	toll;
	protected	String	internTollFree;
	protected	String	internToll;
	protected	String	moderatorPassCode;
	protected	String	attendeePasscode;
 	
	
	public UIFormDefaultValues()
	{
	}
	public	static	UIFormDefaultValues		parseJsonObject(JSONObject reJson)
	{
		UIFormDefaultValues	fdv	=	new	UIFormDefaultValues();
		
		fdv.setActionId(reJson.get(FORM_KEY_ACTION_ID).isString().stringValue());
		fdv.setEmail(reJson.get(FORM_KEY_EMAIL).isString().stringValue());
		fdv.setDisplayName(reJson.get(FORM_KEY_DISPLAY_NAME).isString().stringValue());
		fdv.setConfKey(reJson.get(FORM_KEY_CONF_KEY).isString().stringValue());
		fdv.setConfName(reJson.get(FORM_KEY_CONF_NAME).isString().stringValue());
		fdv.setToll(reJson.get(FORM_KEY_TOLL).isString().stringValue());
		fdv.setTollFree(reJson.get(FORM_KEY_TOLL_FREE).isString().stringValue());
		fdv.setInternToll(reJson.get(FORM_KEY_INTERN_TOLL).isString().stringValue());
		fdv.setInternTollFree(reJson.get(FORM_KEY_INTERN_TOLL_FREE).isString().stringValue());
		fdv.setModeratorPassCode(reJson.get(FORM_KEY_MODERATOR_PASSCODE).isString().stringValue());
		fdv.setAttendeePasscode(reJson.get(FORM_KEY_ATTENDEE_PASSCODE).isString().stringValue());		
		return	fdv;
	}
	public	String	toString()
	{
		StringBuffer buf = new StringBuffer();
		
		buf.append("actionId:");
		buf.append(actionId);
		buf.append(",email:");
		buf.append(email);
		buf.append(",displayName:");
		buf.append(displayName);
		buf.append(",confKey:");
		buf.append(confKey);
		buf.append(",confName:");
		buf.append(confName);
		buf.append(",toll:");
		buf.append(toll);
		buf.append(",tollFree:");
		buf.append(tollFree);
		buf.append(",internTollFree:");
		buf.append(internTollFree);
		buf.append(",internToll:");
		buf.append(internToll);
		buf.append(",moderatorPassCode:");
		buf.append(moderatorPassCode);
		buf.append(",attendeePasscode:");
		buf.append(attendeePasscode);
		
		return	buf.toString();	
	}
	public String getActionId()
	{
		return actionId;
	}
	public void setActionId(String actionId)
	{
		this.actionId = actionId;
	}
	public String getConfKey()
	{
		return confKey;
	}
	public void setConfKey(String confKey)
	{
		this.confKey = confKey;
	}
	public String getConfName()
	{
		return confName;
	}
	public void setConfName(String confName)
	{
		this.confName = confName;
	}
	public String getDisplayName()
	{
		return displayName;
	}
	public void setDisplayName(String displayName)
	{
		this.displayName = displayName;
	}
	public String getEmail()
	{
		return email;
	}
	public void setEmail(String email)
	{
		this.email = email;
	}
	public String getAttendeePasscode() {
		return attendeePasscode;
	}
	public void setAttendeePasscode(String attendeePasscode) {
		this.attendeePasscode = attendeePasscode;
	}
	public String getInternToll() {
		return internToll;
	}
	public void setInternToll(String internToll) {
		this.internToll = internToll;
	}
	public String getInternTollFree() {
		return internTollFree;
	}
	public void setInternTollFree(String internTollFree) {
		this.internTollFree = internTollFree;
	}
	public String getModeratorPassCode() {
		return moderatorPassCode;
	}
	public void setModeratorPassCode(String moderatorPassCode) {
		this.moderatorPassCode = moderatorPassCode;
	}
	public String getToll() {
		return toll;
	}
	public void setToll(String toll) {
		this.toll = toll;
	}
	public String getTollFree() {
		return tollFree;
	}
	public void setTollFree(String tollFree) {
		this.tollFree = tollFree;
	}
}
