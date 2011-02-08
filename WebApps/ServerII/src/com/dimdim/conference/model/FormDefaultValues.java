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

package com.dimdim.conference.model;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */

public class FormDefaultValues implements IJsonSerializable
{
	protected	String	actionId = "join";
	protected	String	email = "";
	protected	String	displayName = "";
	protected	String	confKey = "";
	protected	String	confName = "";
	protected	String	tollFree = "";
	protected	String	toll = "";
	protected	String	internTollFree = "";
	protected	String	internToll = "";
	protected	String	moderatorPassCode = "";
	protected	String	attendeePasscode = "";
	protected	String	returnUrl = "";
	
	public	FormDefaultValues()
	{
	}
	public	FormDefaultValues(String actionId, String email,
				String displayName, String confKey, String confName, 
				String toll, String tollFree, String internToll, String internTollFree,
				String moderatorPassCode, String attendeePasscode, String returnUrl)
	{
		this.actionId = actionId;
		this.email = email;
		this.displayName = displayName;
		this.confKey = confKey;
		this.confName = confName;
		this.toll = toll;
		this.tollFree = tollFree;
		this.internToll = internToll;
		this.internTollFree = internTollFree;
		this.moderatorPassCode = moderatorPassCode;
		this.attendeePasscode = attendeePasscode;
		this.returnUrl = returnUrl;
	}
	public String toJson()
	{
		StringBuffer	buf = new StringBuffer();
		
		buf.append( "{" );
		buf.append( "\"objClass\":\""); buf.append("FormDefaultValues"); buf.append("\",");
		buf.append( "\"actionId\":\""); buf.append(this.actionId); buf.append("\",");
		buf.append( "\"email\":\""); buf.append(this.email); buf.append("\",");
		buf.append( "\"displayName\":\""); buf.append(this.displayName); buf.append("\",");
		buf.append( "\"confKey\":\""); buf.append(this.confKey); buf.append("\",");
		buf.append( "\"confName\":\""); buf.append(this.confName); buf.append("\",");
		buf.append( "\"toll\":\""); buf.append(this.toll); buf.append("\",");
		buf.append( "\"tollFree\":\""); buf.append(this.tollFree); buf.append("\",");
		buf.append( "\"internToll\":\""); buf.append(this.internToll); buf.append("\",");
		buf.append( "\"internTollFree\":\""); buf.append(this.internTollFree); buf.append("\",");
		buf.append( "\"moderatorPassCode\":\""); buf.append(this.moderatorPassCode); buf.append("\",");
		buf.append( "\"attendeePasscode\":\""); buf.append(this.attendeePasscode); buf.append("\",");
		buf.append( "\"returnUrl\":\""); buf.append(this.returnUrl); buf.append("\",");
		buf.append( "\"data\":\"dummy\"");
		buf.append( "}" );
		
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
	public String getAttendeePasscode() {
		return attendeePasscode;
	}
	public void setAttendeePasscode(String attendeePasscode) {
		this.attendeePasscode = attendeePasscode;
	}
	public String getModeratorPassCode() {
		return moderatorPassCode;
	}
	public void setModeratorPassCode(String moderatorPassCode) {
		this.moderatorPassCode = moderatorPassCode;
	}
	public String getReturnUrl() {
		return returnUrl;
	}
	public void setReturnUrl(String returnUrl) {
		this.returnUrl = returnUrl;
	}
}
