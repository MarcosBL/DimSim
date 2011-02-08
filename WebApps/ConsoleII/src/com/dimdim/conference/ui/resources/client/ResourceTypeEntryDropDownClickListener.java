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

import com.dimdim.conference.ui.common.client.resource.ResourceList;
import com.dimdim.conference.ui.common.client.util.DmGlassPanel2;
import com.dimdim.conference.ui.json.client.UIResourceObject;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * 
 */
public class ResourceTypeEntryDropDownClickListener implements ClickListener
{
	protected	ResourceTypePanelsControlsProvider rtpcp;
	protected	ResourceTypeListEntry	listEntry;
	protected	ResourceList			resourceList;
	protected	Widget					widget;
	
	public	ResourceTypeEntryDropDownClickListener(ResourceList resourceList,
			ResourceTypeListEntry listEntry,
			ResourceTypePanelsControlsProvider rtpcp, Widget widget)
	{
		this.resourceList = resourceList;
		this.listEntry = listEntry;
		this.rtpcp = rtpcp;
		this.widget = widget;
	}
	public void onClick(Widget sender)
	{
		Widget w = this.widget;
		if (w == null)
		{
			w = sender;
		}
		if (this.listEntry.getResources() != null)
		{
			if (this.listEntry.getResources().size() == 0)
			{
				if (this.listEntry.getTypeName().equals(UIResourceObject.RESOURCE_TYPE_PRESENTATION))
				{
					this.rtpcp.getShareLinkClickListener().onClick(w);
				}
				else if (this.listEntry.getTypeName().equals(UIResourceObject.RESOURCE_TYPE_COBROWSE))
				{
					this.rtpcp.getShareCobClickListener().onClick(w);
				}
			}
			else
			{
				if (this.listEntry.getTypeName().equals(UIResourceObject.RESOURCE_TYPE_PRESENTATION))
				{
					int left = w.getAbsoluteLeft()+8;
					int top = w.getAbsoluteTop();
					ResourceTypeListEntryPopupPanel rtlepp = new
						ResourceTypeListEntryPopupPanel(resourceList,rtpcp);
					rtlepp.paintPanel(this.listEntry.getTypeName());
					DmGlassPanel2 dgp = new DmGlassPanel2(rtlepp);
					dgp.show(left, top);
					rtlepp.popupVisible();
				}
				else if (this.listEntry.getTypeName().equals(UIResourceObject.RESOURCE_TYPE_COBROWSE))
				{
					int left = w.getAbsoluteLeft()+8;
					int top = w.getAbsoluteTop();
					ResourceTypeListEntryPopupPanel rtlepp = new
						ResourceTypeListEntryPopupPanel(resourceList,rtpcp);
					rtlepp.paintPanel(this.listEntry.getTypeName());
					DmGlassPanel2 dgp = new DmGlassPanel2(rtlepp);
					dgp.show(left, top);
					rtlepp.popupVisible();
				}
			}
		}
	}
}
