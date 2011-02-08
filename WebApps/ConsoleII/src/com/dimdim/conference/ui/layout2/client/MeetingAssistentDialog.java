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

package com.dimdim.conference.ui.layout2.client;

//import com.google.gwt.user.client.Command;
//import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.DOM;
//import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.DockPanel;
import org.gwtwidgets.client.ui.PNGImage;
import asquare.gwt.tk.client.ui.ModalDialog;

import com.dimdim.conference.ui.common.client.util.DebugPanel;
import com.dimdim.conference.ui.common.client.util.DmGlassPanel;
import com.dimdim.conference.ui.dialogues.client.common.ImageButtonPanel2;
import com.dimdim.conference.ui.json.client.UIResourceObject;
import com.dimdim.conference.ui.model.client.ClientModel;
import com.dimdim.conference.ui.model.client.ConferenceGlobals;
import com.dimdim.conference.ui.model.client.ResourceModel;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */

public class MeetingAssistentDialog extends ModalDialog implements ClickListener
{
	protected	boolean		dialogDrawn = false;
	
	protected	DockPanel		headerPanel1;
	protected	PNGImage	closeButton;
	protected	ResourceModel rm = null;
	protected	ImageButtonPanel2 desktopButton = null;
	protected	ImageButtonPanel2 whiteboardButton = null;
	
	protected	ResourceRoster resRoster = null;
	protected	UIResourceObject resource = null;
	protected	ClickListener	shareClickListener = null;
	
	public	MeetingAssistentDialog(ResourceRoster resRoster, ClickListener shareClickListener)
	{
		this.shareClickListener = shareClickListener;
		this.resRoster = resRoster;
		add(getContent());
	}
	public	void	onClick(final Widget sender)
	{
		if(sender == desktopButton.getLine1() || sender == whiteboardButton.getLine1())
		{
			rm = ClientModel.getClientModel().getResourceModel();
			
			if(sender == desktopButton.getLine1())
			{
				resource = rm.findResourceObjectByType(UIResourceObject.RESOURCE_TYPE_DESKTOP);
			}
			else if(sender == whiteboardButton.getLine1())
			{
				resource = rm.findResourceObjectByType(UIResourceObject.RESOURCE_TYPE_WHITEBOARD);
			}
			if(resource != null)
			{
				hide();
				resRoster.getResourceManager().getSharingController().startSharingIfNotActive(resource);
			}
			else
			{
			}
		}
		else
		{
			hide();
		}
	}
	
	protected Widget getContent()
	{
		VerticalPanel	basePanel = new VerticalPanel();
		basePanel.setStyleName("meeting-assistent-panel");
		
		headerPanel1 = new DockPanel();
		headerPanel1.setStyleName("meeting-assistent-header-1");
		closeButton = new PNGImage("images/assistent/close.png",16,16);
		closeButton.addStyleName("anchor-cursor");
		headerPanel1.add(closeButton,DockPanel.EAST);
		headerPanel1.setCellHorizontalAlignment(closeButton,HorizontalPanel.ALIGN_RIGHT);
		headerPanel1.setCellVerticalAlignment(closeButton,VerticalPanel.ALIGN_TOP);
		closeButton.addClickListener(this);
		
		Label filler1 = new Label(" ");
		HorizontalPanel filler1Panel = new HorizontalPanel();
		filler1Panel.add(filler1);
		headerPanel1.add(filler1Panel,DockPanel.CENTER);
		headerPanel1.setCellWidth(filler1Panel,"100%");
		
		basePanel.add(headerPanel1);
		basePanel.setCellHorizontalAlignment(headerPanel1,HorizontalPanel.ALIGN_RIGHT);
		basePanel.setCellVerticalAlignment(headerPanel1,VerticalPanel.ALIGN_TOP);
		basePanel.setCellWidth(headerPanel1,"100%");
		
		Label label = new Label(ConferenceGlobals.getDisplayString("meeting.assistant.title","What would you like to do with Web Meeting today?"));
		label.setStyleName("meeting-assistent-header-2");
		HorizontalPanel labelPanel = new HorizontalPanel();
		labelPanel.add(label);
		labelPanel.setCellHorizontalAlignment(label,HorizontalPanel.ALIGN_CENTER);
		basePanel.add(labelPanel);
		basePanel.setCellHorizontalAlignment(labelPanel,HorizontalPanel.ALIGN_CENTER);
		basePanel.setCellVerticalAlignment(labelPanel,VerticalPanel.ALIGN_TOP);
		basePanel.setCellWidth(labelPanel,"100%");
		
		String desktopButtonColor = "gray";
		if (ConferenceGlobals.publisherEnabled)
		{
			desktopButtonColor = "red";
		}
		desktopButton = new ImageButtonPanel2(ConferenceGlobals.getDisplayString("meeting.assistant.desktop","Share Desktop Screen"),null,
				desktopButtonColor);
		basePanel.add(desktopButton);
		basePanel.setCellHorizontalAlignment(desktopButton,HorizontalPanel.ALIGN_RIGHT);
		basePanel.setCellVerticalAlignment(desktopButton,VerticalPanel.ALIGN_MIDDLE);
		desktopButton.addClickListener(this);
		
		String whiteboardButtonColor = "gray";
		if (ConferenceGlobals.whiteboardEnabled)
		{
			whiteboardButtonColor = "green";
		}
		whiteboardButton = new ImageButtonPanel2(ConferenceGlobals.getDisplayString("meeting.assistant.whiteboard","Share Whiteboard"),null,
				whiteboardButtonColor);
		basePanel.add(whiteboardButton);
		basePanel.setCellHorizontalAlignment(whiteboardButton,HorizontalPanel.ALIGN_RIGHT);
		basePanel.setCellVerticalAlignment(whiteboardButton,VerticalPanel.ALIGN_MIDDLE);
		whiteboardButton.addClickListener(this);
		
		String pptButtonColor = "gray";
		if (ConferenceGlobals.docEnabled)
		{
			pptButtonColor = "blue";
		}
		ImageButtonPanel2 pptButton = new ImageButtonPanel2(ConferenceGlobals.getDisplayString("meeting.assistant.presentation","Share a Presentation"),null,
				pptButtonColor);
		basePanel.add(pptButton);
		basePanel.setCellHorizontalAlignment(pptButton,HorizontalPanel.ALIGN_RIGHT);
		basePanel.setCellVerticalAlignment(pptButton,VerticalPanel.ALIGN_MIDDLE);
//		pptButton.addClickListener(middlePanel.getShareButtonListener());
		if (ConferenceGlobals.docEnabled)
		{
			pptButton.addClickListener(this);
			pptButton.addClickListener(this.shareClickListener);
		}

		Label label2 = new Label(" ");
		basePanel.add(label2);
		basePanel.setCellHorizontalAlignment(label2,HorizontalPanel.ALIGN_RIGHT);
		basePanel.setCellVerticalAlignment(label2,VerticalPanel.ALIGN_MIDDLE);
		
		ScrollPanel sPanel = new ScrollPanel();
		sPanel.setSize("550px", "390px");
		sPanel.add(basePanel);
		
		return sPanel;
	}
	public	void	showMeetingAssistent()
	{
		if (!this.dialogDrawn)
		{
			this.dialogDrawn = true;
		}
		DmGlassPanel dgp = new DmGlassPanel(this);
		dgp.show();
	}
}
