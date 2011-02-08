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
 *	File Name  : Presence.java
 *  Created On : Apr 11, 2006
 *  Created By : Saurav Mohapatra
 **************************************************************************
*/

package com.dimdim.conference.model;

import com.dimdim.conference.ConferenceConstants;

/**
 * @author Saurav Mohapatra
 * @email  Saurav.Mohapatra@communiva.com
 */
public class Presence implements Cloneable
{
	public static final Presence IN_MEETING = new Presence(ConferenceConstants.PRESENCE_IN_MEETING, ConferenceConstants.PRESENCE_IN_MEETING,"",false);;
	public static final Presence OFFLINE = new Presence(ConferenceConstants.PRESENCE_OFFLINE, ConferenceConstants.PRESENCE_OFFLINE,"",true);
	private String name = null;
	private String niceName = null;
	private String extendedMessage = null;
	private boolean isAbsent = true;

	/**
	 * 
	 */
	public Presence()
	{
		this("","","",true);
	}
	
	/**
	 * 
	 */
	public Presence(String name, String niceName, String extendedMessage, boolean isAbsent)
	{
		super();
		setName(name);
		setNiceName(niceName);
		setExtendedMessage(extendedMessage);
		setIsAbsent(isAbsent);
	}


	/**
	 * @return Returns the extendedMessage.
	 */
	public String getExtendedMessage()
	{
		return extendedMessage;
	}

	/**
	 * @param extendedMessage The extendedMessage to set.
	 */
	public void setExtendedMessage(String extendedMessage)
	{
		this.extendedMessage = extendedMessage;
	}

	/**
	 * @return Returns the isAbsent.
	 */
	public boolean getIsAbsent()
	{
		return isAbsent;
	}

	/**
	 * @param isAbsent The isAbsent to set.
	 */
	public void setIsAbsent(boolean isAbsent)
	{
		this.isAbsent = isAbsent;
	}

	/**
	 * @return Returns the name.
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @param name The name to set.
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * @return Returns the niceName.
	 */
	public String getNiceName()
	{
		return niceName;
	}

	/**
	 * @param niceName The niceName to set.
	 */
	public void setNiceName(String niceName)
	{
		this.niceName = niceName;
	}

	public static Presence getPresence(String presence)
	{
		if(presence != null && presence.equals(ConferenceConstants.PRESENCE_IN_MEETING))
		{
			return (Presence)IN_MEETING.clone();
		}
		else
		{
			return (Presence)OFFLINE.clone();
		}
	}
	public Object clone()
	{
		Presence presence = new Presence(name, niceName, extendedMessage, isAbsent);
		return presence;
	}
	
	public String toString()
	{
		return niceName;
	}

}
