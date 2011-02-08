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
/*
 **************************************************************************
 *	File Name  : Mood.java
 *  Created On : Apr 11, 2006
 *  Created By : Saurav Mohapatra
 **************************************************************************
*/
 
package com.dimdim.conference.model;

import	com.dimdim.conference.ConferenceConstants;

/**
 * @author Saurav Mohapatra
 * @email  Saurav.Mohapatra@communiva.com
 */
public class Mood implements Cloneable
{
	
	public	static	final	String	WaitingInLobby	=	"waiting_in_lobby";
	public	static	final	String	Normal	=	"normal";
	public	static	final	String	Question =	"question";
	public	static	final	String	GoFaster =	"go_faster";
	public	static	final	String	GoSlower =	"go_slower";
	public	static	final	String	SpeakLouder =	"speak_louder";
	public	static	final	String	SpeakSofter =	"speak_softer";
	public	static	final	String	ThumbsUp =	"thumbs_up";
	public	static	final	String	ThumbsDown =	"thumbs_down";
	public	static	final	String	SteppedAway =	"stepped_away";
	
	
	/*
	public static final Mood NORMAL = new Mood(ConferenceConstants.MOOD_NORMAL);
	public static final Mood SPEED_UP = new Mood(ConferenceConstants.MOOD_SPEED_UP);
	public static final Mood SLOW_DOWN = new Mood(ConferenceConstants.MOOD_SLOW_DOWN);
	public static final Mood QUESTION = new Mood(ConferenceConstants.MOOD_QUESTION);
	public static final Mood STEPPING_OUT = new Mood(ConferenceConstants.MOOD_STEPPING_OUT);
	public static final Mood BUSY = new Mood(ConferenceConstants.MOOD_BUSY);
	public static final Mood ON_THE_PHONE = new Mood(ConferenceConstants.MOOD_ON_THE_PHONE);
	public static final Mood UNKNOWN = new Mood(ConferenceConstants.MOOD_UNKNOWN);
	
	public static final Mood getMood(String mood)
	{
		Mood newMood = null;
		if(mood.equalsIgnoreCase(ConferenceConstants.MOOD_NORMAL))
		{
			newMood = Mood.NORMAL;
		}
		else if(mood.equalsIgnoreCase(ConferenceConstants.MOOD_SPEED_UP))
		{
			newMood = Mood.SPEED_UP;
		}
		else if(mood.equalsIgnoreCase(ConferenceConstants.MOOD_SLOW_DOWN))
		{
			newMood = Mood.SLOW_DOWN;
		}
		else if(mood.equalsIgnoreCase(ConferenceConstants.MOOD_QUESTION))
		{
			newMood = Mood.QUESTION;
		}
		else if(mood.equalsIgnoreCase(ConferenceConstants.MOOD_STEPPING_OUT))
		{
			newMood = Mood.STEPPING_OUT;
		}
		else if(mood.equalsIgnoreCase(ConferenceConstants.MOOD_BUSY))
		{
			newMood = Mood.BUSY;
		}
		else if(mood.equalsIgnoreCase(ConferenceConstants.MOOD_ON_THE_PHONE))
		{
			newMood = Mood.ON_THE_PHONE;
		}
		else
		{
			newMood = Mood.UNKNOWN;
		}
//		System.out.println("Mood:getMood returning "+newMood+" for "+mood);
		return	newMood;
	}
	public	static	Mood	getDefaultMood()
	{
		return	Mood.NORMAL;
	}
	
	protected String name = null;
	protected String displayName = "";
	protected String description = "";
	
	protected Mood()
	{
		this("","","");
	}
	protected Mood(String name)
	{
		this(name,name,name);
	}
	protected Mood(String name,String displayName)
	{
		this(name,displayName,displayName);
	}
	protected Mood(String name, String displayName, String description)
	{
		setName(name);
		setDisplayName(displayName);
		setDescription(description);
	}
	
	public String getDescription()
	{
		return description;
	}
	
	public void setDescription(String description)
	{
		this.description = description;
	}
	
	public String getDisplayName()
	{
		return displayName;
	}
	
	public void setDisplayName(String displayName)
	{
		this.displayName = displayName;
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public Object clone()
	{
		return new Mood(name,displayName,description);
	}
	
	public String toString()
	{
		return displayName;
	}
	*/
}
