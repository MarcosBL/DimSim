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
 * Part of the DimDim V 4.0 Codebase (http://www.dimdim.com)	          *
 *                                                                        *
 * Copyright (c) 2008 Communiva Inc. All Rights Reserved.                 *
 *                                                                        *
 *                                                                        *
 * This code is licensed under the DimDim License                         *
 * For details please visit http://www.dimdim.com/license                 *
 *                                                                        *
 **************************************************************************
 */

package com.dimdim.conference.ui.sharing.client;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.dimdim.conference.ui.json.client.UIResourceObject;
import com.dimdim.conference.ui.model.client.ConferenceGlobals;

import pl.rmalinowski.gwt2swf.client.ui.SWFParams;
import pl.rmalinowski.gwt2swf.client.ui.SWFWidget;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * This overall full page panel will simply provide a common banner, lhs
 * and rhs sections page. The lhs will contain all the available tests and
 * results, progress or any associated components for the tests will be
 * displayed in the rhs.
 */

public class WhiteboardPanel extends CollaborationWidgetDisplayPanel
{
	protected	String	role = "A";
	
	public	WhiteboardPanel()
	{
		this.add(this.moviePanel);
		this.setCellHorizontalAlignment(this.moviePanel, HorizontalPanel.ALIGN_CENTER);
		this.setCellVerticalAlignment(this.moviePanel, VerticalPanel.ALIGN_TOP);
	}
	public String getRole()
	{
		return role;
	}
	public void setRole(String role)
	{
		this.role = role;
	}
	/**
	 * The whiteboard resource never changes. So once this widget is
	 * initialized, it will remain on this single resource.
	 */
	public void refreshWidget(UIResourceObject resource, int width, int height)
	{
		super.refreshWidget(resource, width, height);
		this.setSize(width+"px", height+"px");
		CollaborationResources cr = this.container.getCollaborationesources();
		this.calculateMovieSize(width,height,cr.getWhiteboardStageAspectRatio(),
				cr.getWhiteboardMinimumWidth(),cr.getWhiteboardMinimumHeight());
		this.moviePanel.setSize(movieWidth+"px", movieHeight+"px");
//		if (super.requiresRefresh(resource, width, height))
//		{
		if (this.movieWidget == null)
		{
//			if (this.movieWidget != null)
//			{
//				this.remove(this.movieWidget);
//				this.movieWidget = null;
//			}
			String fullMovieUrl = this.container.getCollaborationesources().getWhiteboardMovieURL()+
				"?wbName="+ConferenceGlobals.conferenceKeyQualified+
				"&rtmpUrl="+this.container.getCollaborationesources().getStreamingUrlsTable().getWhiteboardRtmpUrl()+
				"&rtmptUrl="+this.container.getCollaborationesources().getStreamingUrlsTable().getWhiteboardRtmptUrl()+
				"&wbWidth="+movieWidth+"&wbHeight="+movieHeight+"&role="+role;
			
			SWFParams wbWidgetParams = new SWFParams(fullMovieUrl,"100%","100%");//,movieWidth,movieHeight);
			wbWidgetParams.setWmode("");
			if(ConferenceGlobals.isBrowserSafari())
			{
				wbWidgetParams.setWmode("opaque");
			}
			String s = null;
			movieWidget = new SWFWidget(wbWidgetParams,"pptMovie","pptMovieDiv",s);
			this.moviePanel.add(movieWidget);
		}
//		}
	}
	public void widgetHiding()
	{
		try
		{
			this.moviePanel.remove(this.movieWidget);
			this.movieWidget = null;
		}
		catch(Exception e)
		{
			
		}
		super.widgetHiding();
	}
}
