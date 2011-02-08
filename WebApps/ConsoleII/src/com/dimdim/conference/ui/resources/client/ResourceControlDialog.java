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

package com.dimdim.conference.ui.resources.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import com.dimdim.conference.ui.common.client.ResourceGlobals;
import com.dimdim.conference.ui.common.client.UIConstants;
import com.dimdim.conference.ui.common.client.UIImages;
import com.dimdim.conference.ui.common.client.UIStrings;
import com.dimdim.conference.ui.common.client.list.DefaultList;
import com.dimdim.conference.ui.common.client.list.ListEntryPanel;
import com.dimdim.conference.ui.common.client.resource.ResourceListEntry;
import com.dimdim.conference.ui.common.client.util.CommonModalDialog;
import com.dimdim.conference.ui.common.client.util.FixedLengthLabel;
import com.dimdim.conference.ui.json.client.UIResourceObject;
import com.dimdim.conference.ui.managers.client.resource.ResourceManager;
import com.dimdim.conference.ui.model.client.ClientModel;
import com.dimdim.conference.ui.model.client.ConferenceGlobals;
import com.dimdim.conference.ui.model.client.ResourceModel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */

public class ResourceControlDialog	extends	CommonModalDialog implements ClickListener
{
	protected	Button	deleteButton;
	protected	Label	deleteHeader;
//	protected	Button	deleteAllButton;
	
	protected	DefaultList	listModel;
//	protected	ResourceListAndEntryProperties	resourceListAndEntryProperties;
	protected	ResourceManager		resourceManager;
	protected	boolean		allDeleteChecked = false;
	protected	int			checkedCount = 0;
	protected	VerticalPanel table;
	protected	String		typeName;
	
//	protected	UIListEntryManager		listEntryManager;
	protected	boolean		allowResourceControl = false;
	
	protected	HashMap	checkBoxes = new HashMap();
	protected	HashMap	checkBoxes2 = new HashMap();
	protected	HashMap	rows = new HashMap();
	
	public	ResourceControlDialog(ResourceManager resourceManager,
			DefaultList listModel, boolean allowResourceControl)
	{
		this(resourceManager, listModel, allowResourceControl, null);
	}
	public	ResourceControlDialog(ResourceManager resourceManager,
			DefaultList listModel, boolean allowResourceControl, String typeName)
	{
		super(UIStrings.getResourceControlDialogHeader());
		this.resourceManager = resourceManager;
		this.listModel = listModel;
		this.typeName = typeName;
//		this.listEntryManager = listEntryManager;
//		this.resourceListAndEntryProperties = resourceListAndEntryProperties;
		this.allowResourceControl = allowResourceControl;
		this.addStyleName("resource-control-dialog-box");
	}
	protected	Widget	getContent()
	{
		table = new VerticalPanel();
		Widget	content  = table;
//		table.setStyleName("list-control-table");
		UIResourceObject currentActiveResource = ConferenceGlobals.getCurrentSharedResource();
		
		HorizontalPanel header = new HorizontalPanel();
		header.add(createLabel(".","resource-image-header"));
		header.add(createLabel(UIStrings.getNameLabel(),"resource-name-header"));
		header.add(createLabel(UIStrings.getTypeLabel(),"resource-type-header"));
		if (this.allowResourceControl)
		{
			deleteHeader = createLabel(UIStrings.getDeleteLabel(),"resource-delete-button-header");
			deleteHeader.addStyleName("common-anchor-default-color");
			deleteHeader.addClickListener(this);
			header.add(deleteHeader);
		}
		header.addStyleName("common-dialog-row");
		table.add(header);
		
		int	size = this.listModel.getListSize();
		if (size > 5)
		{
			VerticalPanel vp = new VerticalPanel();
			ScrollPanel scroller = new ScrollPanel();
			scroller.setStyleName("resource-control-dialog-height");
			scroller.add(table);
			vp.add(scroller);
			content = vp;
		}
		int i=0;
		
		Vector sortedList = getSortedList(listModel);
		for (i=0; i<size; i++)
		{
			ResourceListEntry rle = (ResourceListEntry)sortedList.get(i);
			UIResourceObject res = rle.getResource();
			if (this.typeName != null && !res.getResourceType().equals(this.typeName))
			{
				continue;
			}
//			UIListEntryPanelMouseAndClickListener mcl = new UIListEntryPanelMouseAndClickListener(
//					rle,this.resourceListAndEntryProperties,this.listEntryManager);
//			mcl.setSecondLevelPopup(true);
			
			HorizontalPanel row = new HorizontalPanel();
			
			HorizontalPanel hp1 = new HorizontalPanel();
			Image image = getImage(res);
			hp1.add(image);
			hp1.setCellHorizontalAlignment(image,HorizontalPanel.ALIGN_CENTER);
			hp1.setCellVerticalAlignment(image,VerticalPanel.ALIGN_MIDDLE);
			hp1.addStyleName("resource-image");
			row.add(hp1);
			row.setCellHorizontalAlignment(hp1,HorizontalPanel.ALIGN_CENTER);
			row.setCellVerticalAlignment(hp1,VerticalPanel.ALIGN_MIDDLE);
			
//			table.setWidget((i+1), 0, image);
			Label nameLabel = createTextHTML(res.getResourceName(),"resource-name",23);
//			nameLabel.addStyleName("common-anchor");
//			nameLabel.addClickListener(mcl);
//			nameLabel.addMouseListener(mcl);
			row.add(nameLabel);
			
			row.add(createTextHTML(ResourceGlobals.getResourceGlobals().
					getResourceTypeNiceName(res),"resource-type"));
			
			if (this.allowResourceControl)
			{
				HorizontalPanel hp2 = new HorizontalPanel();
				CheckBox cb = new CheckBox();
				hp2.add(cb);
				hp2.setStyleName("resource-delete-button");
				hp2.setCellHorizontalAlignment(cb,HorizontalPanel.ALIGN_LEFT);
				hp2.setCellVerticalAlignment(cb,VerticalPanel.ALIGN_MIDDLE);
				row.add(hp2);
				row.setCellHorizontalAlignment(hp2,HorizontalPanel.ALIGN_LEFT);
				row.setCellVerticalAlignment(hp2,VerticalPanel.ALIGN_MIDDLE);
				cb.addClickListener(this);
				this.checkBoxes.put(res.getResourceId(),cb);
				this.checkBoxes2.put(cb,cb);
				if (!ResourceGlobals.allowDelete(res) ||
						(currentActiveResource != null &&
							currentActiveResource.getResourceId().equals(res.getResourceId())))
				{
					cb.setEnabled(false);
				}
			}
			row.addStyleName("common-dialog-row");
			table.add(row);
			this.rows.put(res.getResourceId(),row);
		}
		return	content;
	}
	
	private Vector getSortedList(DefaultList listModel) {
		Vector returnVector = new Vector();
		int	size = this.listModel.getListSize();
		ResourceListEntry rle = null;
		ResourceListEntry rleDesktop = null;
		ResourceListEntry rleWhiteBoard = null;
		for(int i=0; i<size; i++)
		{
			rle = (ResourceListEntry)listModel.getListEntryAt(i);
			if(rle.getResource().equals(ResourceGlobals.getResourceGlobals().getDesktopResource()))
			{
				rleDesktop = rle;
				continue;
			}else if(rle.getResource().equals(ResourceGlobals.getResourceGlobals().getWhiteboardResource())){
				rleWhiteBoard = rle;
				continue;
			}else{
				addToVector(returnVector, rle);
			}
		}
		if(rleDesktop != null)
		{
			returnVector.insertElementAt(rleDesktop, 0);
		}
		if(rleWhiteBoard != null)
		{
			returnVector.insertElementAt(rleWhiteBoard, 1);
		}
		
		return returnVector;
	}
	
	private void addToVector(Vector resourceVector, ResourceListEntry listEntry) {
		int count = resourceVector.size();
		String tempEntryName = null;
		String currEntryName = null;
		ResourceListEntry tempEntry = null;
		boolean foundGreaterEntry = false;
		
		currEntryName = listEntry.getResource().getResourceName();
		if(count == 0)
		{
			//Window.alert("adding currEntryName "+currEntryName + " adding at the end");
			resourceVector.add(listEntry);
			return;
		}
		
		for(int i = 0; i < count ; i++)
		{
			tempEntry = (ResourceListEntry)resourceVector.get(i);
			tempEntryName = tempEntry.getResource().getResourceName();
			
			
			tempEntryName = tempEntryName.toLowerCase();
			currEntryName = currEntryName.toLowerCase();
			//Window.alert("currEntryName = "+currEntryName + " tempEntryName = "+tempEntryName);
			//Window.alert("the result of coparision = "+tempEntryName.compareTo(currEntryName));
			if(tempEntryName.compareTo(currEntryName ) < 0)
			{
				//Window.alert("inside continue");
				continue;
			}else{
				foundGreaterEntry = true;
				//Window.alert("adding currEntryName "+currEntryName + " to index in vector = "+i);
				resourceVector.insertElementAt(listEntry, i);
				break;
			}
		}
		
		if(!foundGreaterEntry)
		{
			//Window.alert("adding currEntryName "+currEntryName + " adding at the end");
			resourceVector.add(listEntry);
		}
		
	}
	
	private Image getImage(UIResourceObject res)
	{
		String type = res.getResourceType();
		Image image = null;
		if (type == null || type.equals(UIConstants.RESOURCE_TYPE_DEFAULT))
		{
			image = UIImages.getImageBundle(UIImages.defaultSkin).getDesktopShareItemImageUrl();
		}
		else if (type.equals(UIConstants.RESOURCE_TYPE_WHITEBOARD))
		{
			image = UIImages.getImageBundle(UIImages.defaultSkin).getWhiteboardShareItemImageUrl();
		}
		else if (type.equals(UIConstants.RESOURCE_TYPE_DESKTOP))
		{
			image = UIImages.getImageBundle(UIImages.defaultSkin).getDesktopShareItemImageUrl();
		}
		else if (type.equals(UIConstants.RESOURCE_TYPE_PRESENTATION))
		{
			image = UIImages.getImageBundle(UIImages.defaultSkin).getPowerpointItemImageUrl();
		}
		else if (type.equals(UIConstants.RESOURCE_TYPE_APP_SHARE))
		{
			image = UIImages.getImageBundle(UIImages.defaultSkin).getApplicationShareItemImageUrl();
		}else if (type.equals(UIConstants.RESOURCE_TYPE_COBROWSE))
		{
			image = UIImages.getImageBundle(UIImages.defaultSkin).getShareIcon();
		}
		return image;
	}
	
	protected	Label	createLabel(String labelText, String styleName)
	{
		Label html = new Label(labelText);
		html.setStyleName("common-table-header");
		if (styleName != null)
		{
			html.addStyleName(styleName);
		}
		return	html;
	}
	protected	Label	createTextHTML(String text, String styleName)
	{
		Label html = new Label(text);
		html.setStyleName("common-table-text");
		if (styleName != null)
		{
			html.addStyleName(styleName);
		}
		return	html;
	}
	protected	Label	createTextHTML(String text, String styleName, int length)
	{
		FixedLengthLabel html = new FixedLengthLabel(text, length);
		html.setStyleName("common-table-text");
		if (styleName != null)
		{
			html.addStyleName(styleName);
		}
		html.setTitle(text);
		return	html;
	}
	protected	Vector	getFooterButtons()
	{
		Vector v = new Vector();
		if (this.allowResourceControl)
		{	
			deleteButton = new Button();
			deleteButton.setText(UIStrings.getOKLabel());
			deleteButton.setStyleName("dm-popup-close-button");
			deleteButton.addClickListener(this);
			v.addElement(deleteButton);
//			deleteButton.setEnabled(false);
		}
		return	v;
	}
	public	void	resourceRemoved(UIResourceObject res)
	{
		Widget w = (Widget)rows.get(res.getResourceId());
		if (w != null)
		{
			this.table.remove(w);
			this.rows.remove(res.getResourceId());
		}
	}
	public	void	onClick(Widget w)
	{
		if (w == deleteButton)
		{
//			this.disableAllButtons();
			this.resourceManager.setProgressListener(this);
			
			ResourceModel rm = ClientModel.getClientModel().getResourceModel();
			ArrayList resources = rm.getResourceList();
			int	size = resources.size();
			for (int i=0; i<size; i++)
			{
				UIResourceObject res = (UIResourceObject)resources.get(i);
				CheckBox cb = (CheckBox)this.checkBoxes.get(res.getResourceId());
				if (cb != null && cb.isChecked())
				{
					this.resourceManager.deleteResource(res);
//					resourceRemoved(res);
				}
			}
			this.checkedCount = 0;
			hide();
		}
		else if (w == deleteHeader)
		{
			allDeleteChecked = !allDeleteChecked;
			Iterator iter = this.checkBoxes.values().iterator();
			while (iter.hasNext())
			{
				CheckBox cb = (CheckBox)iter.next();
				if (cb.isEnabled())
				{
					cb.setChecked(allDeleteChecked);
					if (allDeleteChecked)
					{
						this.checkedCount++;
					}
					else
					{
						this.checkedCount--;
					}
				}
			}
		}
		else
		{
			CheckBox cb = (CheckBox)(this.checkBoxes2.get(w));
			if (cb.isChecked())
			{
				this.checkedCount++;
			}
			else
			{
				this.checkedCount--;
			}
		}
		if (this.checkedCount > 0)
		{
//			this.deleteButton.setEnabled(true);
		}
		else
		{
//			this.deleteButton.setEnabled(false);
		}
	}
}
