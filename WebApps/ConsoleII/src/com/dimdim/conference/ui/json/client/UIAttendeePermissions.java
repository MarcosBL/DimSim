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

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * The defaults on the object are set
 */
public class UIAttendeePermissions
{
	protected	boolean		currentResourceReviewEnabled = true;
	protected	boolean		askingQuestionsEnabled = true;
	protected	boolean		annotationsEnabled = true;
	protected	boolean		rosterViewEnabled = true;
	protected	boolean		chattingEnabled = false;
	protected	boolean		resourcePanelViewEnabled = true;
	protected	boolean		fileImportEnabled = false;
	
	public UIAttendeePermissions()
	{
	}
	public UIAttendeePermissions(boolean currentResourceReviewEnabled,
			boolean askingQuestionsEnabled, boolean annotationsEnabled,
			boolean rosterViewEnabled, boolean chattingEnabled,
			boolean resourcePanelViewEnabled, boolean fileImportEnabled)
	{
		this.currentResourceReviewEnabled = currentResourceReviewEnabled;
		this.askingQuestionsEnabled = askingQuestionsEnabled;
		this.annotationsEnabled = annotationsEnabled;
		this.rosterViewEnabled = rosterViewEnabled;
		this.chattingEnabled = chattingEnabled;
		this.resourcePanelViewEnabled = resourcePanelViewEnabled;
		this.fileImportEnabled = fileImportEnabled;
	}
	/**
	 * This simple lookup by index just makes the metadata scecifications
	 * easier and trim.
	 */
	public	boolean	getPermissionByIndex(int index)
	{
		if (index == 0)	return	this.currentResourceReviewEnabled;
		else if (index == 1)	return	this.askingQuestionsEnabled;
		else if (index == 2)	return	this.annotationsEnabled;
		else if (index == 3)	return	this.rosterViewEnabled;
		else if (index == 4)	return	this.chattingEnabled;
		else if (index == 5)	return	this.resourcePanelViewEnabled;
		else	return	this.fileImportEnabled;
	}
	
	public	static	UIAttendeePermissions	getPresenterPermissions()
	{
		return	new UIAttendeePermissions(true,true,true,true,true,true,true);
	}
	public	static	UIAttendeePermissions	getActivePresenterPermissions()
	{
		return	new UIAttendeePermissions(true,true,true,true,true,true,true);
	}
	
	public boolean isAnnotationsEnabled()
	{
		return this.annotationsEnabled;
	}
	public void setAnnotationsEnabled(boolean annotationsEnabled)
	{
		this.annotationsEnabled = annotationsEnabled;
	}
	public boolean isAskingQuestionsEnabled()
	{
		return this.askingQuestionsEnabled;
	}
	public void setAskingQuestionsEnabled(boolean askingQuestionsEnabled)
	{
		this.askingQuestionsEnabled = askingQuestionsEnabled;
	}
	public boolean isChattingEnabled()
	{
		return this.chattingEnabled;
	}
	public void setChattingEnabled(boolean chattingEnabled)
	{
		this.chattingEnabled = chattingEnabled;
	}
	public boolean isCurrentResourceReviewEnabled()
	{
		return this.currentResourceReviewEnabled;
	}
	public void setCurrentResourceReviewEnabled(boolean currentResourceReviewEnabled)
	{
		this.currentResourceReviewEnabled = currentResourceReviewEnabled;
	}
	public boolean isFileImportEnabled()
	{
		return this.fileImportEnabled;
	}
	public void setFileImportEnabled(boolean fileImportEnabled)
	{
		this.fileImportEnabled = fileImportEnabled;
	}
	public boolean isResourcePanelViewEnabled()
	{
		return this.resourcePanelViewEnabled;
	}
	public void setResourcePanelViewEnabled(boolean resourcePanelViewEnabled)
	{
		this.resourcePanelViewEnabled = resourcePanelViewEnabled;
	}
	public boolean isRosterViewEnabled()
	{
		return this.rosterViewEnabled;
	}
	public void setRosterViewEnabled(boolean rosterViewEnabled)
	{
		this.rosterViewEnabled = rosterViewEnabled;
	}
}
