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

package com.dimdim.conference.config;

import	com.dimdim.conference.ConferenceConstants;
import	com.dimdim.conference.model.IJsonSerializable;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */
public class PermissionsConfig	implements	IJsonSerializable
{
	protected	boolean		currentResourceReviewEnabled = true;
	protected	boolean		askingQuestionsEnabled = true;
	protected	boolean		annotationsEnabled = true;
	protected	boolean		rosterViewEnabled = true;
	protected	boolean		chattingEnabled = false;
	protected	boolean		resourcePanelViewEnabled = true;
	protected	boolean		fileImportEnabled = false;
	protected	boolean		meetingLobbyEnabled = false;
	
	public PermissionsConfig()
	{
	}
	public PermissionsConfig(boolean currentResourceReviewEnabled,
			boolean askingQuestionsEnabled, boolean annotationsEnabled,
			boolean rosterViewEnabled, boolean chattingEnabled,
			boolean resourcePanelViewEnabled, boolean fileImportEnabled,
			boolean meetingLobbyEnabled)
	{
		this.currentResourceReviewEnabled = currentResourceReviewEnabled;
		this.askingQuestionsEnabled = askingQuestionsEnabled;
		this.annotationsEnabled = annotationsEnabled;
		this.rosterViewEnabled = rosterViewEnabled;
		this.chattingEnabled = chattingEnabled;
		this.resourcePanelViewEnabled = resourcePanelViewEnabled;
		this.fileImportEnabled = fileImportEnabled;
		this.meetingLobbyEnabled = meetingLobbyEnabled;
	}
	public	String	toJson()
	{
		StringBuffer	buf = new StringBuffer();
		
		buf.append( "{" );
		buf.append( "currentResourceReviewEnabled:\""); buf.append(this.currentResourceReviewEnabled); buf.append("\",");
		buf.append( "askingQuestionsEnabled:\""); buf.append(this.askingQuestionsEnabled); buf.append("\",");
		buf.append( "annotationsEnabled:\""); buf.append(this.annotationsEnabled); buf.append("\",");
		buf.append( "rosterViewEnabled:\""); buf.append(this.rosterViewEnabled); buf.append("\",");
		buf.append( "chattingEnabled:\""); buf.append(this.chattingEnabled); buf.append("\",");
		buf.append( "resourcePanelViewEnabled:\""); buf.append(this.resourcePanelViewEnabled); buf.append("\",");
		buf.append( "fileImportEnabled:\""); buf.append(this.fileImportEnabled); buf.append("\",");
		buf.append( "meetingLobbyEnabled:\""); buf.append(this.meetingLobbyEnabled); buf.append("\"");
		buf.append( "}" );
		
		return	buf.toString();
	}
	public boolean isAnnotationsEnabled()
	{
		return annotationsEnabled;
	}
	public void setAnnotationsEnabled(boolean annotationsEnabled)
	{
		this.annotationsEnabled = annotationsEnabled;
	}
	public boolean isAskingQuestionsEnabled()
	{
		return askingQuestionsEnabled;
	}
	public void setAskingQuestionsEnabled(boolean askingQuestionsEnabled)
	{
		this.askingQuestionsEnabled = askingQuestionsEnabled;
	}
	public boolean isChattingEnabled()
	{
		return chattingEnabled;
	}
	public void setChattingEnabled(boolean chattingEnabled)
	{
		this.chattingEnabled = chattingEnabled;
	}
	public boolean isCurrentResourceReviewEnabled()
	{
		return currentResourceReviewEnabled;
	}
	public void setCurrentResourceReviewEnabled(boolean currentResourceReviewEnabled)
	{
		this.currentResourceReviewEnabled = currentResourceReviewEnabled;
	}
	public boolean isFileImportEnabled()
	{
		return fileImportEnabled;
	}
	public void setFileImportEnabled(boolean fileImportEnabled)
	{
		this.fileImportEnabled = fileImportEnabled;
	}
	public boolean isResourcePanelViewEnabled()
	{
		return resourcePanelViewEnabled;
	}
	public void setResourcePanelViewEnabled(boolean resourcePanelViewEnabled)
	{
		this.resourcePanelViewEnabled = resourcePanelViewEnabled;
	}
	public boolean isRosterViewEnabled()
	{
		return rosterViewEnabled;
	}
	public void setRosterViewEnabled(boolean rosterViewEnabled)
	{
		this.rosterViewEnabled = rosterViewEnabled;
	}
	public	void	setPermission(String flag)
	{
		/*
		if (flag.equals(ConferenceConstants.ACTION_ENABLE_CURRENT_RESOURCE_VIEW))
		{
			this.currentResourceReviewEnabled = true;
		}
		else if (flag.equals(ConferenceConstants.ACTION_ENABLE_ASKING_QUESTIONS))
		{
			this.askingQuestionsEnabled = true;
		}
		else if (flag.equals(ConferenceConstants.ACTION_ENABLE_ANNOTATION))
		{
			this.annotationsEnabled = true;
		}
		else if (flag.equals(ConferenceConstants.ACTION_ALLOW_ROSTER_VIEW))
		{
			this.rosterViewEnabled = true;
		}
		else if (flag.equals(ConferenceConstants.ACTION_ALLOW_CHAT))
		{
			this.chattingEnabled = true;
		}
		else if (flag.equals(ConferenceConstants.ACTION_ALLOW_RESOURCE_PANEL_VIEW))
		{
			this.resourcePanelViewEnabled = true;
		}
		else if (flag.equals(ConferenceConstants.ACTION_ALLOW_RESOURCE_IMPORT))
		{
			this.fileImportEnabled = true;
		}
		else if (flag.equals(ConferenceConstants.ACTION_DISABLE_CURRENT_RESOURCE_VIEW))
		{
			this.currentResourceReviewEnabled = false;
		}
		else if (flag.equals(ConferenceConstants.ACTION_DISABLE_ASKING_QUESTIONS))
		{
			this.askingQuestionsEnabled = false;
		}
		else if (flag.equals(ConferenceConstants.ACTION_DISABLE_ANNOTATION))
		{
			this.annotationsEnabled = false;
		}
		else if (flag.equals(ConferenceConstants.ACTION_BLOCK_ROSTER_VIEW))
		{
			this.rosterViewEnabled = false;
		}
		else if (flag.equals(ConferenceConstants.ACTION_DISALLOW_CHAT))
		{
			this.chattingEnabled = false;
		}
		else if (flag.equals(ConferenceConstants.ACTION_BLOCK_RESOURCE_PANEL_VIEW))
		{
			this.resourcePanelViewEnabled = false;
		}
		else if (flag.equals(ConferenceConstants.ACTION_BLOCK_RESOURCE_IMPORT))
		{
			this.fileImportEnabled = false;
		}
		*/
	}
}
