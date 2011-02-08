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

import	com.google.gwt.json.client.JSONObject;
//
//import java.util.Date;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 *
 */
public class UIPopoutPanelData	extends	UIObject
{
	private	static	final	String	POPOUT_KEY_WINDOW_ID	=	"windowId";
	private	static	final	String	POPOUT_KEY_PANEL_ID	=	"panelId";
	private	static	final	String	POPOUT_KEY_DATA_TEXT	=	"dataText";
	
	protected	String	windowId;
	protected	String	panelId;
	protected	String	dataText;
	
	public UIPopoutPanelData()
	{
	}
	public	static	UIPopoutPanelData		parseJsonObject(JSONObject reJson)
	{
		UIPopoutPanelData	pcd	=	new	UIPopoutPanelData();
		
		pcd.setWindowId(reJson.get(POPOUT_KEY_WINDOW_ID).isString().stringValue());
		pcd.setPanelId(reJson.get(POPOUT_KEY_PANEL_ID).isString().stringValue());
		pcd.setDataText(reJson.get(POPOUT_KEY_DATA_TEXT).isString().stringValue());
		
		return	pcd;
	}
	public String getDataText()
	{
		return dataText;
	}
	public void setDataText(String dataText)
	{
		this.dataText = dataText;
	}
	public String getPanelId()
	{
		return panelId;
	}
	public void setPanelId(String panelId)
	{
		this.panelId = panelId;
	}
	public String getWindowId()
	{
		return windowId;
	}
	public void setWindowId(String windowId)
	{
		this.windowId = windowId;
	}
	public	String	toString()
	{
		StringBuffer buf = new StringBuffer();
		
		buf.append("windowId:");
		buf.append(windowId);
		buf.append(",panelId:");
		buf.append(panelId);
		buf.append(",dataText:");
		buf.append(dataText);
		
		return	buf.toString();	
	}
	public	String	toJson()
	{
		StringBuffer buf = new StringBuffer();
		
		buf.append("{");
		buf.append("objClass:\""); buf.append("PopoutPanelData"); buf.append("\",");
		buf.append("windowId:\""); buf.append(windowId); buf.append("\",");
		buf.append("panelId:\""); buf.append(panelId); buf.append("\",");
		buf.append("dataText:\""); buf.append(dataText); buf.append("\"");
		buf.append("}");
		
		return	buf.toString();
	}
}
