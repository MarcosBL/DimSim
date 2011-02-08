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
 * A list entry has a very specific structure. Some of the elements of
 * this structure are mandetory and must be used by all lists, e.g.
 * the entry name must be provided for each list entry for the list
 * display to be useful.
 * 
 * Each list entry at maximum has following structure.
 * 
 * <image 1> <name> <image 2> <image 3> <image 4> <image 5> <movie 1> <movie 2>
 */

public class ListEntry implements Comparable
{
	protected	String	id;
	protected	String	name;
	protected	int		displayRank = 4;
	
	protected	Image	image1Url;
	protected	Image	image2Url;
	protected	Image	image3Url;
	protected	Image	image4Url;
	protected	Image	image5Url;
	
	protected	String	image1Tooltip;
	protected	String	image2Tooltip;
	protected	String	image3Tooltip;
	protected	String	image4Tooltip;
	protected	String	image5Tooltip;
	
	protected	ListEntryMovieModel		movie1Model;
	protected	ListEntryMovieModel		movie2Model;
	
	protected	ListEntryChangeListener	changeListener;
	protected	ListEntryShareListener	shareListener;
	
	protected	ListEntryPropertiesProvider	listEntryPropertiesProvider;
	protected	ListEntryControlsProvider	listEntryControlsProvider;
	
	public ListEntry()
	{
	}
	public ListEntry(String id, String name,
			ListEntryControlsProvider	listEntryControlsProvider,
			ListEntryPropertiesProvider	listEntryPropertiesProvider)
	{
		this.id = id;
		this.name = name;
		
		this.listEntryControlsProvider = listEntryControlsProvider;
		this.listEntryPropertiesProvider = listEntryPropertiesProvider;
		
		this.image1Url = this.listEntryPropertiesProvider.getImage1Url();
		this.image2Url = this.listEntryPropertiesProvider.getImage2Url();
		this.image3Url = this.listEntryPropertiesProvider.getImage3Url();
		this.image4Url = this.listEntryPropertiesProvider.getImage4Url();
		this.image5Url = this.listEntryPropertiesProvider.getImage5Url();
		
		this.image1Tooltip = this.listEntryPropertiesProvider.getImage1Tooltip();
		this.image2Tooltip = this.listEntryPropertiesProvider.getImage2Tooltip();
		this.image3Tooltip = this.listEntryPropertiesProvider.getImage3Tooltip();
		this.image4Tooltip = this.listEntryPropertiesProvider.getImage4Tooltip();
		this.image5Tooltip = this.listEntryPropertiesProvider.getImage5Tooltip();
	}
	public	void	refreshEntry(ListEntry entry)
	{
		this.name = entry.getName();
		this.image1Url = entry.getImage1Url();
		this.image2Url = entry.getImage2Url();
		this.image3Url = entry.getImage3Url();
		this.image4Url = entry.getImage4Url();
		this.image5Url = entry.getImage5Url();
		this.movie1Model = entry.getMovie1Model();
		this.movie2Model = entry.getMovie2Model();
		this.changeListener = entry.getChangeListener();
		this.listEntryControlsProvider = entry.getListEntryControlsProvider();
		this.listEntryPropertiesProvider = entry.getListEntryPropertiesProvider();
	}
	public int getDisplayRank()
	{
		return displayRank;
	}
	public void setDisplayRank(int displayRank)
	{
		if (this.displayRank != displayRank)
		{
			int	oldDisplayRank = this.displayRank;
			this.displayRank = displayRank;
			if (this.changeListener != null)
			{
				this.changeListener.displayRankChanged(oldDisplayRank,displayRank);
			}
		}
	}
	public ListEntryControlsProvider getListEntryControlsProvider()
	{
		return listEntryControlsProvider;
	}
	public void setListEntryControlsProvider(
			ListEntryControlsProvider listEntryControlsProvider)
	{
		this.listEntryControlsProvider = listEntryControlsProvider;
	}
	public ListEntryPropertiesProvider getListEntryPropertiesProvider()
	{
		return listEntryPropertiesProvider;
	}
	public void setListEntryPropertiesProvider(
			ListEntryPropertiesProvider listEntryPropertiesProvider)
	{
		this.listEntryPropertiesProvider = listEntryPropertiesProvider;
	}
	public	int	compareTo(Object o)
	{
		ListEntry e = (ListEntry)o;
		
		return	id.compareTo(e.getId());
	}
	public	boolean	equals(Object o)
	{
		return	(this.compareTo(o) == 0);
	}
	public	String		getId()
	{
		return	id;
	}
	public ListEntryChangeListener getChangeListener()
	{
		return changeListener;
	}
	public void setChangeListener(ListEntryChangeListener changeListener)
	{
		this.changeListener = changeListener;
	}
	public Image getImage1Url()
	{
		return image1Url;
	}
	public void setImage1Url(Image image1Url)
	{
		this.image1Url = image1Url;
		this.image1Tooltip = this.listEntryPropertiesProvider.getImage1Tooltip();
		if (this.changeListener != null)
		{
			this.changeListener.image1UrlChanged(image1Url);
		}
	}
	public Image getImage2Url()
	{
		return image2Url;
	}
	public void setImage2Url(Image image2Url)
	{
		this.image2Url = image2Url;
		this.image2Tooltip = this.listEntryPropertiesProvider.getImage2Tooltip();
		if (this.changeListener != null)
		{
			this.changeListener.image2UrlChanged(image2Url);
		}
	}
	public Image getImage3Url()
	{
		return image3Url;
	}
	/*public void setImage3Url(Image image3Url)
	{
		this.image3Url = image3Url;
		this.image3Tooltip = this.listEntryPropertiesProvider.getImage3Tooltip();
		if (this.changeListener != null)
		{
			this.changeListener.image3UrlChanged(image3Url);
		}
	}*/

	public void setImage3Url(Image image3Url, String tooltip)
	{
		this.image3Url = image3Url;
		this.image3Tooltip = tooltip;
		if (this.changeListener != null)
		{
			this.changeListener.image3UrlChanged(image3Url);
		}
	}
	
	/*public void setImage3Url(Image image3Url, boolean rightAlign)
	{
		this.image3Url = image3Url;
		this.image3Tooltip = this.listEntryPropertiesProvider.getImage3Tooltip();
		if (this.changeListener != null)
		{
			this.changeListener.image3UrlChanged(image3Url, rightAlign);
		}
	}*/
	public Image getImage4Url()
	{
		return image4Url;
	}
	public void setImage4Url(Image image4Url)
	{
		this.image4Url = image4Url;
		this.image4Tooltip = this.listEntryPropertiesProvider.getImage4Tooltip();
		if (this.changeListener != null)
		{
			this.changeListener.image4UrlChanged(image4Url);
		}
	}
	public Image getImage5Url()
	{
		return image5Url;
	}
	
	public void setImage5Url(Image image5Url)
	{
		this.image5Url = image5Url;
		this.image5Tooltip = this.listEntryPropertiesProvider.getImage5Tooltip();
		if (this.changeListener != null)
		{
			this.changeListener.image5UrlChanged(image5Url);
		}
	}
	
	public void setImage5Url(Image image5Url, boolean rightAlign)
	{
		this.image5Url = image5Url;
		this.image5Tooltip = this.listEntryPropertiesProvider.getImage5Tooltip();
		if (this.changeListener != null)
		{
			this.changeListener.image5UrlChanged(image5Url, rightAlign);
		}
	}
	
	public ListEntryMovieModel getMovie1Model()
	{
		return movie1Model;
	}
	public void setMovie1Model(ListEntryMovieModel movie1Model)
	{
		if (this.changeListener != null)
		{
			this.changeListener.movieModel1Changed(this.movie1Model,movie1Model);
		}
		this.movie1Model = movie1Model;
	}
	public ListEntryMovieModel getMovie2Model()
	{
		return movie2Model;
	}
	public void setMovie2Model(ListEntryMovieModel movie2Model)
	{
		this.movie2Model = movie2Model;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
		if (this.changeListener != null)
		{
			this.changeListener.nameChanged(name);
		}
	}
	public String getImage1Tooltip()
	{
		return image1Tooltip;
	}
	public void setImage1Tooltip(String image1Tooltip)
	{
		this.image1Tooltip = image1Tooltip;
	}
	public String getImage2Tooltip()
	{
		return image2Tooltip;
	}
	public void setImage2Tooltip(String image2Tooltip)
	{
		this.image2Tooltip = image2Tooltip;
	}
	public String getImage3Tooltip()
	{
		return image3Tooltip;
	}
	public void setImage3Tooltip(String image3Tooltip)
	{
		this.image3Tooltip = image3Tooltip;
	}
	public String getImage4Tooltip()
	{
		return image4Tooltip;
	}
	public void setImage4Tooltip(String image4Tooltip)
	{
		this.image4Tooltip = image4Tooltip;
	}
	public String getImage5Tooltip()
	{
		return image5Tooltip;
	}
	public void setImage5Tooltip(String image5Tooltip)
	{
		this.image5Tooltip = image5Tooltip;
	}
	public ListEntryShareListener getShareListener() {
		return shareListener;
	}
	public void setShareListener(ListEntryShareListener shareListener) {
		this.shareListener = shareListener;
	}
}

