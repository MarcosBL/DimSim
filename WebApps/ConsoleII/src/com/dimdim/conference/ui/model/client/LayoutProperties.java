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
 * Part of the DimDim V 2.0 Codebase (http://www.dimdim.com)	          *
 *                                                                        *
 * Copyright (c) 2008 Dimdim Inc. All Rights Reserved.              	  *
 *                                                                        *
 *                                                                        *
 * This code is licensed under the DimDim License                         *
 * For details please visit http://www.dimdim.com/license                 *
 *                                                                        *
 **************************************************************************
 */

package com.dimdim.conference.ui.model.client;

import	java.util.Vector;
import	java.util.HashMap;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * At present there is only 1 layout.
 * The html divisions in the console 4.0 layout are:
 * 
 * image - the logo image.
 * dial-in-panel - the dialin panal.
 * top-links - the right hand top links.
 * clock-image - clock image
 * time_meeting - time
 * notifications_image - notifications
 * 
 * main_showitems - items box
 * main_participants - participants box
 * 
 */

public class LayoutProperties
{
	public	static	final	String	CONSOLE_4_LAYOUT	=	"CONSOLE_4_LAYOUT";
	
	protected	static	LayoutProperties	console4Layout;
	
	/**
	 * Since there is only 1 layout right now the name is ignored.
	 * 
	 * @param name
	 * @return
	 */
	public	static	LayoutProperties	getLayout(String name)
	{
		if (LayoutProperties.console4Layout == null)
		{
			LayoutProperties.console4Layout = new LayoutProperties(LayoutProperties.CONSOLE_4_LAYOUT);
		}
		return	LayoutProperties.console4Layout;
	}
	
	protected	String		layoutId;
	protected	Vector		divIds;
	protected	HashMap		normalViewVisibilityStyleMap;
	protected	HashMap		fullScreenViewVisibilityStyleMap;
	
	protected	LayoutProperties(String layoutId)
	{
		this.layoutId = layoutId;
		this.divIds = new Vector();
		this.normalViewVisibilityStyleMap = new HashMap();
		this.fullScreenViewVisibilityStyleMap = new HashMap();
		if (layoutId.equals(LayoutProperties.CONSOLE_4_LAYOUT))
		{
			this.divIds.addElement("image");
			this.normalViewVisibilityStyleMap.put("image", "Show");
			this.fullScreenViewVisibilityStyleMap.put("image", "Hide");
			
			this.divIds.addElement("dial-in-panel");
			this.normalViewVisibilityStyleMap.put("dial-in-panel", "Show");
			this.fullScreenViewVisibilityStyleMap.put("dial-in-panel", "Hide");
			
			this.divIds.addElement("top_links");
			this.normalViewVisibilityStyleMap.put("top_links", "Show");
			this.fullScreenViewVisibilityStyleMap.put("top_links", "Hide");
			
			this.divIds.addElement("clock-image");
			this.normalViewVisibilityStyleMap.put("clock-image", "Show");
			this.fullScreenViewVisibilityStyleMap.put("clock-image", "Hide");
			
			this.divIds.addElement("time_meeting");
			this.normalViewVisibilityStyleMap.put("time_meeting", "Show");
			this.fullScreenViewVisibilityStyleMap.put("time_meeting", "Hide");
			
			this.divIds.addElement("notifications_image");
			this.normalViewVisibilityStyleMap.put("notifications_image", "Show");
			this.fullScreenViewVisibilityStyleMap.put("notifications_image", "Hide");
			
			this.divIds.addElement("left_column");
			this.normalViewVisibilityStyleMap.put("left_column", "Show");
			this.fullScreenViewVisibilityStyleMap.put("left_column", "Hide");
		}
	}
	public	Vector	getDivIds()
	{
		return	this.divIds;
	}
	public	String	getNormalViewStyle(String divId)
	{
		return	(String)this.normalViewVisibilityStyleMap.get(divId);
	}
	public	String	getFullScreenViewStyle(String divId)
	{
		return	(String)this.fullScreenViewVisibilityStyleMap.get(divId);
	}
	public	void	setDivHiddenInNormalView(String divId)
	{
		this.normalViewVisibilityStyleMap.put(divId, "Hide");
	}
	public	void	setDivHiddenInFulLScreenView(String divId)
	{
		this.fullScreenViewVisibilityStyleMap.put(divId, "Hide");
	}
	public	void	setDivVisibleInNormalView(String divId)
	{
		this.normalViewVisibilityStyleMap.put(divId, "Show");
	}
	public	void	setDivVisibleInFulLScreenView(String divId)
	{
		this.fullScreenViewVisibilityStyleMap.put(divId, "Show");
	}
}
