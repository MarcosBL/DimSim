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

package com.dimdim.conference.ui.envcheck.client.main;

import com.dimdim.conference.ui.envcheck.client.layout.CheckPanel;
import	com.dimdim.conference.ui.envcheck.client.command.OSCheck;
import	com.dimdim.conference.ui.envcheck.client.command.BrowserTypeCheck;
import	com.dimdim.conference.ui.envcheck.client.command.FlashVersionCheck;
import	com.dimdim.conference.ui.envcheck.client.command.PublisherCheck;
import com.google.gwt.user.client.Window;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */

public class EnvChecksPanel
{
	
	protected	CheckPanel	osCheckPanel;
	protected	CheckPanel	browserTypeCheckPanel;
	protected	CheckPanel	flashCheckPanel;
	protected	CheckPanel	publisherCheckPanel;
	
	protected	String	actionId;
	protected	String	browserType;
	protected	String	osType;
	
	protected	int		checkPanelWidth;
	
	public	EnvChecksPanel(String actionId, String browserType, String osType,
				int	checkPanelWidth)
	{
		this.actionId = actionId;
		this.browserType = browserType;
		this.osType = osType;
		this.checkPanelWidth = checkPanelWidth;
		
		this.osCheckPanel = new CheckPanel(OSCheck.CheckIndex,OSCheck.CheckName);
		this.browserTypeCheckPanel = new CheckPanel(BrowserTypeCheck.CheckIndex,BrowserTypeCheck.CheckName);
		this.flashCheckPanel = new CheckPanel(FlashVersionCheck.CheckIndex,FlashVersionCheck.CheckName);
		this.publisherCheckPanel = new CheckPanel(PublisherCheck.CheckIndex,PublisherCheck.CheckName);
		//Window.alert("env check panel created..");
	}
	public CheckPanel getOsCheckPanel()
	{
		return osCheckPanel;
	}
	public	void	showOsCheckResult(boolean result)
	{
//		this.checksSummaryPanel.setOSCheckResult(result);
	}
	public CheckPanel getBrowserTypeCheckPanel()
	{
		return browserTypeCheckPanel;
	}
	public	void	showBrowserTypeCheckResult(boolean result)
	{
//		this.checksSummaryPanel.setBrowserTypeCheckResult(result);
	}
	public CheckPanel getFlashCheckPanel()
	{
		return flashCheckPanel;
	}
	public	void	showFlashCheckResult(boolean result)
	{
//		this.checksSummaryPanel.setFlashCheckResult(result);
	}
	public CheckPanel getPublisherCheckPanel()
	{
		return publisherCheckPanel;
	}
	public	void	showPublisherCheckResult(boolean result)
	{
//		this.checksSummaryPanel.setPublisherCheckResult(result);
	}
	
	public void showNotApplicableMessage(int index){
	    
	    if(index == PublisherCheck.CheckIndex)
	    {
		this.publisherCheckPanel.setCheckNotApplicable();
	    }
	    else if(index == FlashVersionCheck.CheckIndex)
	    {
		this.flashCheckPanel.setCheckNotApplicable();
	    }
	    else if(index == OSCheck.CheckIndex)
	    {
		this.osCheckPanel.setCheckNotApplicable();
	    }
	}
}
