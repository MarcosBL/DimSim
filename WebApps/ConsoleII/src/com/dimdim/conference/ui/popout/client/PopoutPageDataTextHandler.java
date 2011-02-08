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

package com.dimdim.conference.ui.popout.client;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ResponseTextHandler;

import com.dimdim.conference.ui.json.client.ResponseAndEventReader;
import com.dimdim.conference.ui.json.client.JSONurlReaderCallback;
//import com.dimdim.conference.ui.json.client.JSONValue;
//import com.dimdim.conference.ui.json.client.JSONParser;
import com.dimdim.conference.ui.json.client.UIServerResponse;

import com.dimdim.conference.ui.model.client.RosterModel;
import com.dimdim.conference.ui.model.client.ResourceModel;

public	class	PopoutPageDataTextHandler
{
	protected	String		windowId;
	protected	PopoutWindowContent		popoutWindowContent;
//	protected	ResponseAndEventReader	jsonReader;
	protected	PopoutPageEventTextHandler	eventsTextHandler;
	
	protected	int		featureModelsCount = -1;
	protected	int		panelsCount = -1;
	
	protected	int		featureDataRead = 0;
	protected	int		panelsDataRead = 0;
	
	protected	String	dataCode = null;
	
	public	PopoutPageDataTextHandler(String windowId,
				PopoutWindowContent popoutWindowContent,
				PopoutPageEventTextHandler	eventsTextHandler)
	{
		this.windowId = windowId;
		this.popoutWindowContent = popoutWindowContent;
		this.eventsTextHandler = eventsTextHandler;
//		this.jsonReader = new ResponseAndEventReader();
	}
	public	int	onCompletion(String data)
	{
		int ret = 0;
		if (featureModelsCount == -1)
		{
			featureModelsCount = (new Integer(data)).intValue();
//			Window.alert(featureModelsCount+"");
		}
		else if (panelsCount == -1)
		{
			panelsCount = (new Integer(data)).intValue();
//			Window.alert(panelsCount+"");
		}
		else if (dataCode == null || data.startsWith("feature") || data.startsWith("panel"))
		{
			dataCode = data;
//			Window.alert(dataCode+"");
		}
		else
		{
			ret = onCompletion(dataCode,data);
			dataCode = null;
		}
		reportDataReceived();
		return	ret;
	}
	private	int	onCompletion(String dataCode, String dataText)
	{
		try
		{
			if (dataCode != null && dataCode.length() != 0 &&
					dataText != null && dataText.length() != 0)
			{
//				Window.alert("Reading data:--"+dataText+"-- for code: --"+dataCode+"--");
				if (dataCode.startsWith("feature"))
				{
					//	This is a data buffer for a feature model. Pass it on
					//	to the respective feature. This list is specific and
					//	can be handled right here.
					this.readFeatureModelData(dataCode,dataText);
					this.featureDataRead++;
				}
				else if (dataCode.startsWith("panel"))
				{
					this.readPanelData(dataCode,dataText);
				}
				dataCode = null;
			}
			else
			{
			}
		}
		catch (Exception e)
		{
//			Window.alert(e.getMessage());
		}
		
		return	this.isDataTransferComplete();
	}
	protected	void	reportDataReceived()
	{
		String popoutReceivedDataMessage = "{objClass:\"PopoutPanelData\",windowId:\""+
			this.windowId+"\",panelId:\""+this.windowId+"\",dataText:\"POPOUT_DATA_RECEIVED\"}";
		reportPopoutReceivedDataToConsole(popoutReceivedDataMessage);
	}
	private native void reportPopoutReceivedDataToConsole(String msg) /*-{
		$wnd.sendMessageFromPopoutToConsole(msg);
	}-*/;
	/**
	 * This method returns 0 false, 1 true.
	 * @return
	 */
	protected	int		isDataTransferComplete()
	{
		int ret = 0;
		if (this.featureModelsCount == this.featureDataRead &&
				this.panelsCount == this.panelsDataRead)
		{
			ret = 1;
		}
		return ret;
	}
	protected	void	readFeatureModelData(String dataCode, String dataText)
	{
		this.eventsTextHandler.onCompletion(dataText);
	}
	protected	void	readPanelData(String dataCode, String dataText)
	{
		this.popoutWindowContent.initializePanelData(dataCode,dataText);
	}
}
