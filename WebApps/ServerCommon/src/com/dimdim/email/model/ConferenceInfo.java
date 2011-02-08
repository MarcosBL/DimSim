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
 *								                                          *
 * Copyright (c) 2006 Communiva Inc. All Rights Reserved.	              *
 *								                                          *
 *								                                          *
 * This code is licensed under the DimDim License                         *
 * For details please visit http://www.dimdim.com/license		          *
 *									                                      *
 **************************************************************************
 */

package com.dimdim.email.model;

import java.io.UnsupportedEncodingException;
import	java.util.Date;


/**
 * @author Saurav Mohapatra
 * @email  Saurav.Mohapatra@communiva.com
 */
public class ConferenceInfo //implements IJsonSerializable
{
	private String	name;
	private String	key;
	private Integer	numParticipants;
	private Integer	numResources;
	private String	startTime;
	private String	endTime ;
	private String	organizerEmail;
	private String	organizer;
	private String  agenda;
	private	String	joinURL = "the join url";
	private	String	startURL = "the start url";
	private String  meetingID;
	private String	recurrenceType;
	private String	recurrenceTypeConst = "DAILY";
	
	protected	String	internToll;
	protected	String	internTollFree;
	protected	String	toll;
	protected	String	tollFree;
	protected	String	moderatorPassCode;
	protected	String	attendeePasscode;
	protected	String	host_key;
	protected	String	meeting_key;

	
	/**
	 *
	 */
	public ConferenceInfo()
	{
		super();
	}
	public	ConferenceInfo(String name, String key, int numParticipants, int numResources,
				String startTime, String recurrenceType, String organizer, String organizerEmail, 
				String toll, String tollFree, String internToll, String internTollFree, String moderatorPassCode, String attendeePasscode)
	{
		this.name = name;
		if(null == this.name)
		{
			this.name = "";
		}
		this.key = key;
		this.numParticipants = new Integer(numParticipants);
		this.numResources = new Integer(numResources);
		this.startTime = startTime;
		this.organizer = organizer;
		this.organizerEmail = organizerEmail;
		this.recurrenceType = recurrenceType;
		this.toll = toll;
		this.tollFree = tollFree;
		this.internToll = internToll;
		this.internTollFree = internTollFree;
		this.moderatorPassCode = moderatorPassCode;
		this.attendeePasscode = attendeePasscode;
	}
	public	String	toJson()
	{
		StringBuffer	buf = new StringBuffer();

		buf.append( "{" );
		buf.append( "objClass:\""); buf.append("ConferenceInfo"); buf.append("\",");
		buf.append( "name:\""); buf.append(this.name); buf.append("\",");
		buf.append( "key:\""); buf.append(this.key); buf.append("\",");
		buf.append( "numParticipants:\""); buf.append(this.numParticipants); buf.append("\",");
		buf.append( "numResources:\""); buf.append(this.numResources); buf.append("\",");
		buf.append( "startTime:\""); buf.append(this.startTime); buf.append("\",");
		buf.append( "endTime:\""); buf.append(this.endTime); buf.append("\",");
		buf.append( "organizer:\""); buf.append(this.organizer); buf.append("\",");
		buf.append( "organizerEmail:\""); buf.append(this.organizerEmail); buf.append("\",");
		buf.append( "joinURL:\""); buf.append(this.joinURL); buf.append("\",");
		buf.append( "startURL:\""); buf.append(this.startURL); buf.append("\"");
		buf.append( "toll:\""); buf.append(this.toll); buf.append("\"");
		buf.append( "tollFree:\""); buf.append(this.tollFree); buf.append("\"");
		buf.append( "internToll:\""); buf.append(this.internToll); buf.append("\"");
		buf.append( "internTollFree:\""); buf.append(this.internTollFree); buf.append("\"");
		buf.append( "moderatorPassCode:\""); buf.append(this.moderatorPassCode); buf.append("\"");
		buf.append( "attendeePasscode:\""); buf.append(this.attendeePasscode); buf.append("\"");
		buf.append( "}" );

		return	buf.toString();
	}
	public String getKey()
	{
		return key;
	}
	public void setKey(String key)
	{
		this.key = key;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		if(null == name)
		{
			this.name = "";
		}else{
			this.name = name;
		}
	}
	public Integer getNumParticipants()
	{
		return numParticipants;
	}
	public void setNumParticipants(Integer numParticipants)
	{
		this.numParticipants = numParticipants;
	}
	public Integer getNumResources()
	{
		return numResources;
	}
	public void setNumResources(Integer numResources)
	{
		this.numResources = numResources;
	}
	public String getJoinURL()
	{
		return joinURL;
	}
	public void setJoinURL(String joinURL)
	{
		this.joinURL = joinURL;
	}
	
	public String getOrganizer()
	{
		return organizer;
	}
	
	public String getOrganizerUTF8()
	{
	    String xyz = "";
	    if(null != organizer)
	    {
		try
		{
		    xyz = new String(organizer.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e)
		{
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}
	    }
		return xyz;
	}
	public void setOrganizer(String organizer)
	{
		this.organizer = organizer;
	}
	public String getStartTime()
	{
		return startTime;
	}
	public void setStartTime(String startTime)
	{
		this.startTime = startTime;
	}
	public String getOrganizerEmail()
	{
		return organizerEmail;
	}
	public void setOrganizerEmail(String organizerEmail)
	{
		this.organizerEmail = organizerEmail;
	}

	public String getStartURL()
	{
		return startURL;
	}
	public void setStartURL(String startURL)
	{
		this.startURL = startURL;
	}
	public String getAgenda()
	{
		return agenda;
	}
	public void setAgenda(String agenda)
	{
		this.agenda = agenda;
	}
	public String getMeetingID()
	{
		return meetingID;
	}
	public void setMeetingID(String meetingID)
	{
		this.meetingID = meetingID;
	}
	public String getRecurrenceType()
	{
		return recurrenceType;
	}
	public void setRecurrenceType(String recurrenceType)
	{
		this.recurrenceType = recurrenceType;
	}
	public String getEndTime() {
		return endTime;
	}
	
	public String getEndTimeForMail() {
		if (null != endTime && endTime.length() > 0){
			return endTime;
		}else{
			return "Not Applicable";
		}
	}
	
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getRecurrenceTypeConst()
	{
	    return recurrenceTypeConst;
	}
	public void setRecurrenceTypeConst(String recurrenceTypeConst)
	{
	    this.recurrenceTypeConst = recurrenceTypeConst;
	}
	public String getModeratorPassCode() {
		return moderatorPassCode;
	}
	public void setModeratorPassCode(String moderatorPassCode) {
		this.moderatorPassCode = moderatorPassCode;
	}
	public String getHost_key() {
		return host_key;
	}
	public void setHost_key(String host_key) {
		this.host_key = host_key;
	}
	
	public String getMeeting_key() {
		return meeting_key;
	}
	public void setMeeting_key(String meeting_key) {
		this.meeting_key = meeting_key;
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
