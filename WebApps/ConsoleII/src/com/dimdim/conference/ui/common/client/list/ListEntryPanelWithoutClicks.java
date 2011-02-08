package com.dimdim.conference.ui.common.client.list;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ListEntryPanelWithoutClicks extends ListEntryPanel{

	public ListEntryPanelWithoutClicks(ListEntry listEntry) {
		super(listEntry);
	}
	
	
	protected	Image	setImage(DockPanel imagePanel, Image image,
			Image imageUrl, ClickListener clickListener, HorizontalPanel subPanel, String tooltip)
	{
		return	this.setImage(imagePanel,image,imageUrl,clickListener,subPanel,tooltip, false);
	}
	
	protected	Image	setImage(DockPanel imagePanel, Image image,
			Image imageUrl, ClickListener clickListener, HorizontalPanel subPanel, String tooltip, boolean rightAlign)
	{
		Image image2 = image;
		if (image != null)
		{
			//Window.alert("prev image = is not null");
			if (subPanel != null)
			{
				//Window.alert("subpanel = is not null so removing..");
				subPanel.remove(image);
				image2 = null;
			}else if(imagePanel != null){
				imagePanel.remove(image);
				image2 = null;
			}

			
		}
		if (imageUrl != null )
		{
			/*
			if (imageUrl.endsWith("xxx"))
			{
				Image image = new Image(imageUrl);
				image.addStyleName("list-entry-panel-image");
				imagePanel.add(image,DockPanel.WEST);
				imagePanel.setCellHorizontalAlignment(image,HorizontalPanel.ALIGN_CENTER);
				imagePanel.setCellVerticalAlignment(image,VerticalPanel.ALIGN_MIDDLE);
				if (clickListener != null)
				{
					image.addClickListener(clickListener);
				}
			}
			else
			*/
//			else
			{
				image2 = imageUrl;
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
					}else
					{
						imagePanel.add(image2,DockPanel.WEST);
						imagePanel.setCellHorizontalAlignment(image2,HorizontalPanel.ALIGN_CENTER);
						imagePanel.setCellVerticalAlignment(image2,VerticalPanel.ALIGN_MIDDLE);
					}
				}
				/*if (clickListener != null)
				{
					image2.addClickListener(clickListener);
					image2.addStyleName("anchor-cursor");
				}*/
				if (tooltip != null)
				{
					image2.setTitle(tooltip);
				}
			}
		}
		return	image2;
	}
	
}
