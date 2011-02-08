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

import com.google.gwt.user.client.ui.ClickListener;
import com.dimdim.conference.ui.json.client.UIResourceObject;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */

public interface ResourceTypePanelsControlsProvider
{
	public	ClickListener	getNameLabelClickListener(final UIResourceObject resource);
	
	public	ClickListener	getShareLinkClickListener();
	
	public	ClickListener	getShareCobClickListener();
	
	public	ClickListener	getAllLinkClickListener();
	
//	public	ClickListener	getImage5ClickListner(String typeName);
	
	public	ClickListener	getTypePopupHeaderClickListener(String typeName);
	
	public	ClickListener	getTypePopupFooterClickListener(String typeName);
}
