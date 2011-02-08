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

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 *
 */
public class UIClientScreen
{
	protected	Integer		height;
	protected	Integer		width;
	protected	String		resolution;
	
	public UIClientScreen()
	{
	}
	
	public Integer getHeight()
	{
		return this.height;
	}
	public void setHeight(Integer height)
	{
		this.height = height;
	}
	public String getResolution()
	{
		return this.resolution;
	}
	public void setResolution(String resolution)
	{
		this.resolution = resolution;
	}
	public Integer getWidth()
	{
		return this.width;
	}
	public void setWidth(Integer width)
	{
		this.width = width;
	}
}
