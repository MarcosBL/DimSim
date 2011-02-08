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
 
package com.dimdim.conference.action.recording;

import com.dimdim.conference.action.ConferenceAction;
import com.dimdim.conference.model.IConference;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 *
 */
public class SetRecordingStopOptionsAction		extends	ConferenceAction
{
	protected	boolean saveRecording;
	protected	boolean uploadRecordingToBlipTV;
	protected	String title;
	protected	String description;
	protected	String category;
	protected	String keywords;
	
	public	SetRecordingStopOptionsAction()
	{
	}
	public	String	doWork()	throws	Exception
	{
		String	ret = SUCCESS;
		IConference conf = this.userSession.getConference();
//		IConferenceParticipant user = this.userSession.getUser();
		
		try
		{
			conf.getRecordingManager().SetRecordingStopOptions(saveRecording,
					uploadRecordingToBlipTV, title, description, category, keywords);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			ret = ERROR;
		}
		return	ret;
	}
	public String getCategory()
	{
		return category;
	}
	public void setCategory(String category)
	{
		this.category = category;
	}
	public String getDescription()
	{
		return description;
	}
	public void setDescription(String description)
	{
		this.description = description;
	}
	public String getKeywords()
	{
		return keywords;
	}
	public void setKeywords(String keywords)
	{
		this.keywords = keywords;
	}
	public boolean isSaveRecording()
	{
		return saveRecording;
	}
	public void setSaveRecording(boolean saveRecording)
	{
		this.saveRecording = saveRecording;
	}
	public String getTitle()
	{
		return title;
	}
	public void setTitle(String title)
	{
		this.title = title;
	}
	public boolean isUploadRecordingToBlipTV()
	{
		return uploadRecordingToBlipTV;
	}
	public void setUploadRecordingToBlipTV(boolean uploadRecordingToBlipTV)
	{
		this.uploadRecordingToBlipTV = uploadRecordingToBlipTV;
	}
}
