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

package com.dimdim.conference.ui.model.client;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */

public class UserMood
{
	protected	String		mood;
	protected	String		displayLabel;
	protected	String		moodStyleName;
	//protected	String		imageUrl;
	protected	String		imageStyleName;
	
	public UserMood()
	{
	}
	public UserMood(String mood, String displayLabel,
			String moodStyleName, String imageStyleName)
	{
		this.mood = mood;
		this.displayLabel = displayLabel;
		this.moodStyleName = moodStyleName;
		//this.imageUrl = imageUrl;
		this.imageStyleName = imageStyleName;
	}
	
	public String getDisplayLabel()
	{
		return displayLabel;
	}
	public void setDisplayLabel(String displayLabel)
	{
		this.displayLabel = displayLabel;
	}
	public String getImageStyleName()
	{
		return imageStyleName;
	}
	public void setImageStyleName(String imageStyleName)
	{
		this.imageStyleName = imageStyleName;
	}
	/*public String getImageUrl()
	{
		return imageUrl;
	}
	public void setImageUrl(String imageUrl)
	{
		this.imageUrl = imageUrl;
	}*/
	public String getMood()
	{
		return mood;
	}
	public void setMood(String mood)
	{
		this.mood = mood;
	}
	public String getMoodStyleName()
	{
		return moodStyleName;
	}
	public void setMoodStyleName(String moodStyleName)
	{
		this.moodStyleName = moodStyleName;
	}
}

