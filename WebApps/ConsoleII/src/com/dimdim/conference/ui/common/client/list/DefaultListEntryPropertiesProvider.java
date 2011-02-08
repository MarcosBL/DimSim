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

package com.dimdim.conference.ui.common.client.list;

import com.google.gwt.user.client.ui.Image;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */

public class DefaultListEntryPropertiesProvider	implements	ListEntryPropertiesProvider
{
	public	DefaultListEntryPropertiesProvider()
	{
	}
	public	String	getId()
	{
		return	"";
	}
	public int getNameLabelWidth()
	{
		return 20;
	}
	public String getImage1Tooltip()
	{
		return null;
	}
	public Image getImage1Url()
	{
		return null;
	}
	public String getImage2Tooltip()
	{
		return null;
	}
	public Image getImage2Url()
	{
		return null;
	}
	public String getImage3Tooltip()
	{
		return null;
	}
	public Image getImage3Url()
	{
		return null;
	}
	public String getImage4Tooltip()
	{
		return null;
	}
	public Image getImage4Url()
	{
		return null;
	}
	public String getImage5Tooltip()
	{
		return null;
	}
	public Image getImage5Url()
	{
		return null;
	}
	public int getImageHeightPX()
	{
		return 16;
	}
	public int getImageWidthPX()
	{
		return 16;
	}
	public String getListEntryPanelBackgroundStyle()
	{
		return "list-entry-panel-background";
	}
	public String getListEntryPanelStyle()
	{
		return null;
	}
	public ListEntryMovieModel getMovie1Model()
	{
		return null;
	}
	public ListEntryMovieModel getMovie2Model()
	{
		return null;
	}
	public String getNameLabelStyle()
	{
		return "list-entry-label";
	}
}
