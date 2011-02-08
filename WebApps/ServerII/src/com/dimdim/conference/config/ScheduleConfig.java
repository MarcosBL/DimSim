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
 *	File Name  : ConferenceScheduleConfig.java
 *  Created On : Apr 11, 2006
 *  Created By : Saurav Mohapatra
 **************************************************************************
*/
 
package com.dimdim.conference.config;

/**
 * @author Saurav Mohapatra
 * @email  Saurav.Mohapatra@communiva.com
 */
public class ScheduleConfig
{

	boolean isFlexible = true;
	long    totalDurationMs = 0;
	long    maxLobbyTimeAtStartMs = 0;
	long    maxLobbyTimeAtEndMs = 0;
	
	/**
	 * 
	 */
	public ScheduleConfig()
	{
		super();
	}
	/**
	 * @return Returns the isFlexible.
	 */
	public boolean getIsFlexible()
	{
		return isFlexible;
	}
	/**
	 * @param isFlexible The isFlexible to set.
	 */
	public void setIsFlexible(boolean isFlexible)
	{
		this.isFlexible = isFlexible;
	}
	/**
	 * @return Returns the maxLobbyTimeAtEndMs.
	 */
	public long getMaxLobbyTimeAtEndMs()
	{
		return maxLobbyTimeAtEndMs;
	}
	/**
	 * @param maxLobbyTimeAtEndMs The maxLobbyTimeAtEndMs to set.
	 */
	public void setMaxLobbyTimeAtEndMs(long maxLobbyTimeAtEndMs)
	{
		this.maxLobbyTimeAtEndMs = maxLobbyTimeAtEndMs;
	}
	/**
	 * @return Returns the maxLobbyTimeAtStartMs.
	 */
	public long getMaxLobbyTimeAtStartMs()
	{
		return maxLobbyTimeAtStartMs;
	}
	/**
	 * @param maxLobbyTimeAtStartMs The maxLobbyTimeAtStartMs to set.
	 */
	public void setMaxLobbyTimeAtStartMs(long maxLobbyTimeAtStartMs)
	{
		this.maxLobbyTimeAtStartMs = maxLobbyTimeAtStartMs;
	}
	/**
	 * @return Returns the totalDurationMs.
	 */
	public long getTotalDurationMs()
	{
		return totalDurationMs;
	}
	/**
	 * @param totalDurationMs The totalDurationMs to set.
	 */
	public void setTotalDurationMs(long totalDurationMs)
	{
		this.totalDurationMs = totalDurationMs;
	}

}
