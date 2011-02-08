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
 * Copyright (c) 2007 Dimdim Inc. All Rights Reserved.              	  *
 *                                                                        *
 *                                                                        *
 * This code is licensed under the DimDim License                         *
 * For details please visit http://www.dimdim.com/license                 *
 *                                                                        *
 **************************************************************************
 */

package com.dimdim.conference.application.presentation.dms;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */

public class DMSResource
{
	protected	String	resourceName = null;
	protected	String	resourceType = null;
	String mediaId = null;
	String resId = null; 
	String appHandle = null;
	
	protected	boolean	isEnabled = false;
	
	public DMSResource(String name, String resourceType, boolean isEnabled)
	{
		this.resourceName = name;
		this.resourceType = resourceType;
		this.isEnabled = isEnabled;
	}
	
	public DMSResource(String name, String resourceType, boolean isEnabled, String resId)
	{
		this.resourceName = name;
		this.resourceType = resourceType;
		this.isEnabled = isEnabled;
		this.resId = resId;
	}
	
	public String getResourceName()
	{
		return resourceName;
	}
	public void setResourceName(String resourceName)
	{
		this.resourceName = resourceName;
	}
	public String getResourceType()
	{
		return resourceType;
	}
	public void setResourceType(String resourceType)
	{
		this.resourceType = resourceType;
	}
	public boolean isEnabled()
	{
		return isEnabled;
	}
	public void setEnabled(boolean isEnabled)
	{
		this.isEnabled = isEnabled;
	}
	public String getAppHandle() {
		return appHandle;
	}
	public void setAppHandle(String appHandle) {
		this.appHandle = appHandle;
	}
	public String getMediaId() {
		return mediaId;
	}
	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}
	public String getResId() {
		return resId;
	}
	public void setResId(String resId) {
		this.resId = resId;
	}
}
