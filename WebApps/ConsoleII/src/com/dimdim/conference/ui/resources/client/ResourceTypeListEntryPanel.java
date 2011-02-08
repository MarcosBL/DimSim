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

//import com.dimdim.conference.ui.common.client.data.UIParams;
import com.dimdim.conference.ui.common.client.util.FixedLengthLabel;
import com.dimdim.conference.ui.common.client.list.ListEntryShareListener;
import com.dimdim.conference.ui.common.client.resource.ResourceList;
//import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * 
 */
public class ResourceTypeListEntryPanel	extends	Composite implements MouseListener, ListEntryShareListener
{
	protected	ResourceTypeListPanel	listPanel;
	protected	ResourceTypeListEntry	listEntry;
	protected	boolean					isSharing = false;
	
	protected	HorizontalPanel	rootPanel = new HorizontalPanel();
	protected	FocusPanel		focusPanel = new FocusPanel();
	protected	DockPanel		basePanel = new DockPanel();
	protected	HTML			basePanelFiller = new HTML("&nbsp;");
	
	protected	HorizontalPanel	image1Panel = new HorizontalPanel();
	protected	HorizontalPanel	namePanel = new HorizontalPanel();
	protected	HorizontalPanel	image5Panel = new HorizontalPanel();
	
	protected	Image				image1;
	protected	FixedLengthLabel	nameLabel;
	protected	Image				image5;
	
	protected	static	String	backgroundStyle = "list-entry-panel-background";
	protected	static	String	hoverStyle = "list-entry-panel-hover";
	protected	static	String	hoverSharingStyle = "list-entry-panel-share-hover";
	protected	static	String	hoverSharingStopStyle = "list-entry-panel-stop-hover";
	protected	static	String	shareStyle = "list-entry-panel-share";
	protected	static	String	nameLabelStyle = "list-name-label";
	
	protected	static	int		nameLabelWidth = 26;
	
	public ResourceTypeListEntryPanel(ResourceList resourceList, ResourceTypeListEntry listEntry, ResourceTypePanelsControlsProvider rtepcp)
	{
		initWidget(rootPanel);
		
		this.rootPanel.setStyleName("list-entry-panel");
		this.rootPanel.addStyleName("resource-type-list-entry-panel");
		
		this.listEntry = listEntry;
		
		this.nameLabel = new FixedLengthLabel("", nameLabelWidth);
		this.nameLabel.setWordWrap(false);
		this.nameLabel.setStyleName("common-text");
		this.nameLabel.addStyleName(nameLabelStyle);
		
		this.addStyleName(backgroundStyle);
		
		this.image1Panel.setStyleName("list-entry-panel-image-panel");
		this.basePanel.add(this.image1Panel,DockPanel.WEST);
		this.basePanel.setCellHorizontalAlignment(this.image1Panel,HorizontalPanel.ALIGN_LEFT);
		this.basePanel.setCellVerticalAlignment(this.image1Panel,VerticalPanel.ALIGN_MIDDLE);
		
		image1 = this.setImage(this.basePanel,image1,listEntry.getSlot1Image(),null,image1Panel,null);
		
		this.basePanel.add(this.nameLabel,DockPanel.WEST);
		this.basePanel.setCellVerticalAlignment(this.nameLabel, VerticalPanel.ALIGN_MIDDLE);
		
		this.nameLabel.setText(listEntry.getDisplayName(false));
		this.nameLabel.setTitle(listEntry.getDisplayName(false));
		this.image5Panel.setStyleName("list-entry-panel-image-panel");
		this.basePanel.add(this.image5Panel,DockPanel.EAST);
		this.basePanel.setCellHorizontalAlignment(this.image5Panel,HorizontalPanel.ALIGN_RIGHT);
		this.basePanel.setCellVerticalAlignment(this.image5Panel,VerticalPanel.ALIGN_MIDDLE);
		
		image5 = this.setImage(this.basePanel,image5,listEntry.getSlot5Image(),null,image5Panel,null);
		ClickListener ml = new ResourceTypeEntryDropDownClickListener(resourceList,listEntry,rtepcp,image5);
		
		ClickListener cl = rtepcp.getNameLabelClickListener(listEntry.getResourceObject());
		if (cl != null)
		{
			//this.focusPanel.addStyleName("anchor-cursor");
			this.focusPanel.addClickListener(cl);
		}
		else
		{
			this.focusPanel.addClickListener(ml);
		}
		
		this.basePanel.add(this.basePanelFiller,DockPanel.CENTER);
		this.basePanel.setCellWidth(this.basePanelFiller,"100%");
		
		this.focusPanel.add(this.basePanel);
//		this.focusPanel.addMouseListener(new ResourceHoverStyler());
		this.focusPanel.addMouseListener(this);
		this.rootPanel.add(this.focusPanel);
		this.rootPanel.setCellHorizontalAlignment(this.focusPanel,HorizontalPanel.ALIGN_LEFT);
		this.rootPanel.setCellVerticalAlignment(this.focusPanel,VerticalPanel.ALIGN_MIDDLE);
		this.rootPanel.setCellHeight(this.focusPanel, "100%");
		this.rootPanel.setCellWidth(this.focusPanel, "100%");
		this.focusPanel.setTitle(listEntry.getDisplayName(false));
		
		if (listEntry.hasMultipleInstances())
		{
			this.resourceCountChanged(listEntry.getResources().size(), listEntry.getDisplayName(false));
		}
	}
	public ResourceTypeListPanel getListPanel()
	{
		return listPanel;
	}
	public void setListPanel(ResourceTypeListPanel listPanel)
	{
		this.listPanel = listPanel;
	}
	protected	Image	setImage(DockPanel imagePanel, Image currentImage,
			Image newImage, ClickListener clickListener, HorizontalPanel subPanel, String tooltip)
	{
		return	this.setImage(imagePanel,currentImage,newImage,clickListener,subPanel,tooltip, false);
	}
	
	protected	Image	setImage(DockPanel imagePanel, Image currentImage,
			Image newImage, ClickListener clickListener, HorizontalPanel subPanel, String tooltip, boolean rightAlign)
	{
		Image image2 = currentImage;
		if (currentImage != null)
		{
			if (subPanel != null)
			{
				//Window.alert("subpanel = is not null so removing..");
				subPanel.remove(currentImage);
				image2 = null;
			}
			else if(imagePanel != null)
			{
				imagePanel.remove(currentImage);
				image2 = null;
			}
		}
		if (newImage != null )
		{
			{
				image2 = newImage;
				image2.addStyleName("list-entry-panel-image");
				if (subPanel != null)
				{
					subPanel.add(image2);
					subPanel.setCellHorizontalAlignment(image2,HorizontalPanel.ALIGN_CENTER);
					subPanel.setCellVerticalAlignment(image2,VerticalPanel.ALIGN_MIDDLE);
				}
				else
				{
					if(rightAlign)
					{
						imagePanel.add(image2,DockPanel.EAST);
						imagePanel.setCellHorizontalAlignment(image2,HorizontalPanel.ALIGN_RIGHT);
						imagePanel.setCellVerticalAlignment(image2,VerticalPanel.ALIGN_MIDDLE);
					}
					else
					{
						imagePanel.add(image2,DockPanel.WEST);
						imagePanel.setCellHorizontalAlignment(image2,HorizontalPanel.ALIGN_CENTER);
						imagePanel.setCellVerticalAlignment(image2,VerticalPanel.ALIGN_MIDDLE);
					}
				}
				if (clickListener != null)
				{
					image2.addClickListener(clickListener);
					//image2.addStyleName("anchor-cursor");
				}
				if (tooltip != null)
				{
					image2.setTitle(tooltip);
				}
			}
		}
		return	image2;
	}
	public FixedLengthLabel getNameLabel()
	{
		return nameLabel;
	}
	public ResourceTypeListEntry	getListEntry()
	{
		return	this.listEntry;
	}
	public void onStartShare()
	{
		removeAllStyles();
		this.addStyleName(shareStyle);
		isSharing = true;
		this.nameLabel.setText(listEntry.getDisplayName(true));
		this.nameLabel.setTitle(listEntry.getDisplayName(true));
	}
	public void onStopShare()
	{
		removeAllStyles();
		if(null != backgroundStyle)
		{
			this.addStyleName(backgroundStyle);
		}
		isSharing = false;
		this.nameLabel.setText(listEntry.getDisplayName(false));
		this.nameLabel.setTitle(listEntry.getDisplayName(false));
	}
	public void resourceCountChanged(int newCount, String newName)
	{
		if (newCount == 0)
		{
			this.image5Panel.setVisible(false);
			this.nameLabel.setText(newName);
			this.nameLabel.setTitle(newName);
		}
		else
		{
			this.image5Panel.setVisible(true);
			this.nameLabel.setText(newName+" ("+newCount+")");
			this.nameLabel.setTitle(newName+" ("+newCount+")");
		}
	}
//	private void nameChanged(String newName)
//	{
//		this.nameLabel.setText(newName);
//		this.nameLabel.setTitle(newName);
//	}
	public void onMouseDown(Widget sender, int x, int y)
	{
	}
	public void onMouseEnter(Widget sender)
	{
		removeAllStyles();
		if(isSharing)
		{
			this.addStyleName(hoverSharingStyle);
		}
		else
		{
			this.addStyleName(hoverSharingStopStyle);
		}
	}
	public void onMouseLeave(Widget sender)
	{
		removeAllStyles();
		if(isSharing)
		{
			this.addStyleName(shareStyle);
		}
		else
		{
			this.addStyleName(backgroundStyle);
		}
	}
	private void removeAllStyles()
	{
		this.removeStyleName(backgroundStyle);
		this.removeStyleName(shareStyle);
		this.removeStyleName(hoverStyle);
		this.removeStyleName(hoverSharingStopStyle);
		this.removeStyleName(hoverSharingStyle);
	}
	public void onMouseMove(Widget sender, int x, int y)
	{
	}
	public void onMouseUp(Widget sender, int x, int y)
	{
	}
}

