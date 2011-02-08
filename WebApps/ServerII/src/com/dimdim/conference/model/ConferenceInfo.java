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

package com.dimdim.conference.model;

import java.io.UnsupportedEncodingException;
import	java.util.Date;

/**
 * @author Saurav Mohapatra
 * @email  Saurav.Mohapatra@communiva.com
 */
public class ConferenceInfo implements IJsonSerializable
{
	private String	name;
	private String	key;
	private Integer	numParticipants;
	private Integer	numResources;
	private String	startTime;
	private	Date	startDate;
	private String	organizerEmail;
	private String	organizer;
	private	String	joinURL = "the join url";
	private	String	startURL = "the start url";
	
	/**
	 * 
	 */
	public ConferenceInfo()
	{
		super();
	}
	public	ConferenceInfo(String name, String key, int numParticipants, int numResources,
				Date startDate, String startTime, String organizer, String organizerEmail)
	{
		this.name = name;
		this.key = key;
		this.numParticipants = new Integer(numParticipants);
		this.numResources = new Integer(numResources);
		this.startDate = startDate;
		this.startTime = startTime;
		this.organizer = organizer;
		this.organizerEmail = organizerEmail;
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
		buf.append( "organizer:\""); buf.append(this.organizer); buf.append("\",");
		buf.append( "organizerEmail:\""); buf.append(this.organizerEmail); buf.append("\",");
		buf.append( "joinURL:\""); buf.append(this.joinURL); buf.append("\",");
		buf.append( "startURL:\""); buf.append(this.startURL); buf.append("\"");
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
		this.name = name;
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
	public Date getStartDate()
	{
		return startDate;
	}
	public void setStartDate(Date startDate)
	{
		this.startDate = startDate;
	}
	public String getStartURL()
	{
		return startURL;
	}
	public void setStartURL(String startURL)
	{
		this.startURL = startURL;
	}
}
