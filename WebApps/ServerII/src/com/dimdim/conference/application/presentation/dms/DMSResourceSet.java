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
 * Copyright (c) 2007 Dimdim Inc. All Rights Reserved.                 *
 *                                                                        *
 *                                                                        *
 * This code is licensed under the DimDim License                         *
 * For details please visit http://www.dimdim.com/license                 *
 *                                                                        *
 **************************************************************************
 */

package com.dimdim.conference.application.presentation.dms;

import java.util.ArrayList;

public class DMSResourceSet
{
	protected	ArrayList	resources = new ArrayList();
	
	public ArrayList getResources()
	{
		return resources;
	}
	public void addResource(String name, String type, boolean isEnabled)
	{
		DMSResource res = new DMSResource(name, type, isEnabled);
		resources.add(res);
	}
	public void addResource(String name, String type, boolean isEnabled, String resId)
	{
		DMSResource res = new DMSResource(name, type, isEnabled, resId);
		resources.add(res);
	}
}
