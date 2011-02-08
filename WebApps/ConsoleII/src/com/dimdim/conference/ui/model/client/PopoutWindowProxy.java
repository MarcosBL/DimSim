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

import java.util.Vector;
import com.google.gwt.user.client.Window;
import com.dimdim.conference.ui.json.client.UIPopoutPanelData;
import com.dimdim.conference.ui.json.client.UIServerResponse;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * Objects of this class represent a poped out window in the main console
 * window. This object contains and maintains the event proxy for the poped
 * out window. The panelId uniquely identifies the popped out window. This
 * proxy is used only on the cosole side.
 */

public class PopoutWindowProxy implements EventsJsonConsumer
{
	protected	String			windowId;
	protected	PopoutPanelSet	popoutPanelSet;
	
	protected	Vector		dataBlocks;
	protected	int			nextDataBlock;
	
	public	PopoutWindowProxy(PopoutPanelSet popoutPanelSet)
	{
		this.windowId = popoutPanelSet.getPanelSetId();
		this.popoutPanelSet = popoutPanelSet;
		this.dataBlocks = new Vector();
		
		PopoutCallbackReader.getReader().addPopoutWindowProxy(this);
	}
	public String getWindowId()
	{
		return windowId;
	}
	public void setWindowId(String windowId)
	{
		this.windowId = windowId;
	}
	
	/**
	 * This method will enqueue the data for the features and the panels
	 * to the window. This means that this method must be called only
	 * from the console and when the popout window creation is completed.
	 * 
	 * 1. Get the feature models list and send the number of features
	 * 2. Get the number of panels and send to popout window.
	 * 3. Get the data for each of the feature models and send.
	 * 4. Get the data for each of the panels and send to popout window.
	 */
	public void panelPoppedOut()
	{
		dataBlocks = new Vector();
		nextDataBlock = 0;
		
		Vector	featureModels = this.popoutPanelSet.getFeatureModelIds();
		int	numFeatureModels = featureModels.size();
		//Window.alert("Feature Models:"+featureModels.toString());
		this.dataBlocks.addElement(numFeatureModels+"");
		
		Vector	panelIds = this.popoutPanelSet.getPanelIds();
		int numPanels = panelIds.size();
//		Window.alert("Number of pabels:"+numPanels);
		this.dataBlocks.addElement(numPanels+"");
		
		for (int i=0; i<numFeatureModels; i++)
		{
			String featureModelId = (String)featureModels.elementAt(i);
			String data = this.getFeatureModelData(featureModelId);
//			Window.alert("Feature:"+featureModelId+", data:"+data);
			if (data != null)
			{
				this.dataBlocks.addElement(featureModelId);
				this.dataBlocks.addElement(data);
			}
		}
		for (int i=0; i<numPanels; i++)
		{
			String panelId = (String)panelIds.elementAt(i);
			String data = this.getPanelData(panelId);
			if (data != null)
			{
				this.dataBlocks.addElement(panelId);
				this.dataBlocks.addElement(data);
			}
		}
	}
	protected	String	getFeatureModelData(String id)
	{
		String data = null;
		//Window.alert("inside getFeatureModelData id = "+id);
		if (id.equals(RosterModel.ModelFeatureId))
		{
			data = ClientModel.getClientModel().getRosterModel().getPopoutJsonData();
		}
		else if (id.equals(ResourceModel.ModelFeatureId))
		{
			data = ClientModel.getClientModel().getResourceModel().getPopoutJsonData();
		}
		else if (id.equals(SharingModel.ModelFeatureId))
		{
			data = ClientModel.getClientModel().getSharingModel().getPopoutJsonData();
		}
		else if (id.equals(PPTSharingModel.ModelFeatureId))
		{
			data = ClientModel.getClientModel().getPPTSharingModel().getPopoutJsonData();
		}
		else if (id.equals(WhiteboardModel.ModelFeatureId))
		{
			data = ClientModel.getClientModel().getWhiteboardModel().getPopoutJsonData();
		}
		else if (id.equals(CoBrowseModel.ModelFeatureId))
		{
			data = ClientModel.getClientModel().getCobrowseModel().getPopoutJsonData();
		}
		else if (id.equals(RecordingModel.ModelFeatureId))
		{
			data = ClientModel.getClientModel().getRecordingModel().getPopoutJsonData();
		}
		//Window.alert("inside getFeatureModelData returning data ="+data);
		return	data;
	}
	protected	String	getPanelData(String panelId)
	{
		String data = null;
		PopoutSupportingPanel psp = this.popoutPanelSet.getPanel(panelId);
		if (psp != null)
		{
			data = psp.getPanelData();
		}
		return	data;
	}
	protected	void	receivePanelDataFromPopout(UIPopoutPanelData ppd)
	{
		PopoutSupportingPanel psp = this.popoutPanelSet.getPanel(ppd.getPanelId());
		if (psp != null)
		{
			psp.receiveMessageFromPopout(ppd);
		}
		else
		{
//			Window.alert("Panel not found");
		}
	}
	/**
	 * This method must be called when the popin of the panel back into
	 * console page is done. This method will call popin on the panel
	 * set so that the console panels can continue to display the data
	 * and any other content as expected.
	 */
//	public void panelPoppedIn()
//	{
//		this.popoutPanelSet.panelSetPopedIn();
//	}
	/**
	 * This method is called when the popout window is fully loaded. This
	 * message is sent by the popout module upon completion of the module
	 * load. 
	 */
	public void popoutWindowLoaded()
	{
//		Window.alert("PopoutWindowProxy:PopoutWindowLoaded");
		this.popoutPanelSet.popoutWindowLoaded();
		sendNextDataBlock();
	}
	public void popoutWindowClosed()
	{
		this.popoutPanelSet.popoutWindowClosed();
	}
	public void popoutDataTransferReceived()
	{
		//	Last data sent was received. Now send the next data.
		sendNextDataBlock();
	}
	protected	void	sendNextDataBlock()
	{
//		Window.alert("Sending next data block");
		if (this.nextDataBlock < this.dataBlocks.size())
		{
			String s = (String)this.dataBlocks.elementAt(this.nextDataBlock);
			this.nextDataBlock++;
//			Window.alert("Sending data string:"+s);
			sendDataString(s);
		}
	}
	public void receiveEvent(UIServerResponse event)
	{
		//	WARNING - not used.
//		this.enqueueEvent(windowId,event);
	}
	public void receiveEventText(String eventText)
	{
		this.sendEventTextToPopoutJS(windowId,eventText);
	}
	
//	private native void enqueueEvent(String panelId, UIServerResponse event) /*-{
//		$wnd.enqueueEvent(panelId,event);
//	}-*/;
	private native void sendEventTextToPopoutJS(String windowId, String eventText) /*-{
		$wnd.sendEventToPopout(windowId,eventText);
	}-*/;
	private native void sendDataString(String s) /*-{
		$wnd.sendDataObjectToPopout(s);
	}-*/;
}
