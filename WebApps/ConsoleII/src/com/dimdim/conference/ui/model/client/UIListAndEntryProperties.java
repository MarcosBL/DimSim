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

import com.dimdim.conference.ui.common.client.UIImages;
import com.google.gwt.user.client.ui.Image;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * This interface exists to avoid duplication of properties in list and list
 * entry objects. The calls accept an optional list entry object so that values
 * could be cofigurable based on the attributes of the list entry object.
 */

public class UIListAndEntryProperties
{
	protected	String	entryPanelStyleName;
	protected	String	selectedEntryPanelStyleName;
	protected	Image	baseImageUrl;
	protected	String	baseLabelStyleName;
	protected	String	baseImageStyleName;
	protected	String	selectedLabelStyleName;
	protected	String	selectedImageStyleName;
	protected	String	hoverLabelStyleName;
	protected	String	hoverImageStyleName;
	protected	String	entryPanelBackgroundStyleName;
	
	private	UIListAndEntryProperties()
	{
		this.entryPanelStyleName = "dm-user-panel";
		this.selectedEntryPanelStyleName = "dm-user-panel-selected";
		this.baseImageUrl = UIImages.getImageBundle(UIImages.defaultSkin).getPresenter();
		this.baseLabelStyleName = "dm-users-list-label";
		this.baseImageStyleName = "dm-user-role-image";
//		this.selectedLabelStyleName = selectedLabelStyleName;
		this.selectedImageStyleName = "dm-user-role-image";
//		this.hoverLabelStyleName = hoverLabelStyleName;
		this.hoverImageStyleName = "dm-user-role-image";
	}
	/*
	public UIListAndEntryProperties(String entryPanelStyleName,
			String selectedEntryPanelStyleName, String baseImageUrl,
			String baseLabelStyleName, String baseImageStyleName,
			String selectedLabelStyleName, String selectedImageStyleName,
			String hoverLabelStyleName, String hoverImageStyleName)
	{
		this.entryPanelStyleName = entryPanelStyleName;
		this.selectedEntryPanelStyleName = selectedEntryPanelStyleName;
		this.baseImageUrl = baseImageUrl;
		this.baseLabelStyleName = baseLabelStyleName;
		this.baseImageStyleName = baseImageStyleName;
		this.selectedLabelStyleName = selectedLabelStyleName;
		this.selectedImageStyleName = selectedImageStyleName;
		this.hoverLabelStyleName = hoverLabelStyleName;
		this.hoverImageStyleName = hoverImageStyleName;
	}
	*/
	public String getEntryPanelStyleName(UIListEntry listEntry)
	{
		return this.entryPanelStyleName;
	}
	public String getSelectedEntryPanelStyleName(UIListEntry listEntry)
	{
		return this.selectedEntryPanelStyleName;
	}
	public String getBaseImageStyleName(UIListEntry listEntry)
	{
		return baseImageStyleName;
	}
	public Image getBaseImageUrl(UIListEntry listEntry)
	{
		return baseImageUrl;
	}
	public String getBaseLabelStyleName(UIListEntry listEntry)
	{
		return baseLabelStyleName;
	}
	public String getHoverImageStyleName(UIListEntry listEntry)
	{
		return hoverImageStyleName;
	}
	public String getHoverLabelStyleName(UIListEntry listEntry)
	{
		return hoverLabelStyleName;
	}
	public String getSelectedImageStyleName(UIListEntry listEntry)
	{
		return selectedImageStyleName;
	}
	public String getSelectedLabelStyleName(UIListEntry listEntry)
	{
		return selectedLabelStyleName;
	}
	public String getEntryPanelBackgroundStyleName(UIListEntry listEntry)
	{
		return this.entryPanelBackgroundStyleName;
	}
}
