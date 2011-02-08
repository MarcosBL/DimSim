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

package com.dimdim.conference.ui.model.client;

import java.util.Vector;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * This list model represents the object lists as they are used in dimdim
 * console 2.0. The list is maintained always in the order of entry. This
 * is mainly because gwt does not yet support ordered sets, lists and maps.
 * In fat program, the list would be maintained ordered based on the list
 * entry comparator.
 * 
 * The list model and the list widget treat all entries the same. If any
 * particular entry requires a special treatment, it must be implemented
 * through the list entry implementation.
 * 
 * By default the visible entries are filled in by the newly added entries
 * till the visible max is reached. Number of visible entries can be
 * reduced by the user below the max, even if more entries are available.
 * User is also allowed to set lower number of entries visible, for any
 * reason.
 */

public class UIListModel
{
	//	-1 would mean no limit.
	
	protected	int	maxEntries = -1;
	protected	int	maxVisibleEntries = -1;
	protected	boolean		scrollerEnabled = false;
	
	protected	Vector  entries;
	protected	Vector	properties;
	
	private	UIListModel(int maxEntries, int maxVisibleEntries)
	{
		this.maxEntries = maxEntries;
		this.maxVisibleEntries = maxVisibleEntries;
		this.entries = new Vector();
		this.properties = new Vector();
	}
	public boolean isScrollerEnabled()
	{
		return scrollerEnabled;
	}
	public void setScrollerEnabled(boolean scrollerEnabled)
	{
		this.scrollerEnabled = scrollerEnabled;
	}
	public	Vector	getEntries()
	{
		return	this.entries;
	}
	public int getMaxEntries()
	{
		return maxEntries;
	}
	public void setMaxEntries(int maxEntries)
	{
		this.maxEntries = maxEntries;
	}
	public int getMaxVisibleEntries()
	{
		return maxVisibleEntries;
	}
	public void setMaxVisibleEntries(int maxVisibleEntries)
	{
		this.maxVisibleEntries = maxVisibleEntries;
	}
//	public Vector getVisibleEntries()
//	{
//		return visibleEntries;
//	}
	public	void	addEntry(UIListEntry entry)
	{
		this.entries.addElement(entry);
	}
	public	void	removeEntry(UIListEntry entry)
	{
		this.entries.removeElement(entry);
	}
	public	int	getListSize()
	{
		return	this.entries.size();
	}
	public	UIListEntry	getListEntryAt(int i)
	{
		return	(UIListEntry)this.entries.get(i);
	}
//	public	int	getVisibleListSize()
//	{
//		return	this.visibleEntries.size();
//	}
//	public	int	setEntryVisible(UIListEntry entry)
//	{
//		if (this.visibleEntries.size() <= this.maxVisibleEntries)
//		{
//			this.visibleEntries.addElement(entry);
//			return	1;
//		}
//		return	-1;
//	}
//	public	void	setEntryInvisible(UIListEntry entry)
//	{
//		this.visibleEntries.remove(entry);
//	}
	public	UIListEntry	findEntry(String id)
	{
		UIListEntry entry = null;
		int	size = this.entries.size();
		for (int i=0; i<size; i++)
		{
			entry = (UIListEntry)this.entries.elementAt(i);
			if (entry.getId().equals(id))
			{
				break;
			}
			else
			{
				entry = null;
			}
		}
		return	entry;
	}
	/**
	 * These properties are additional properties for the list entry,
	 * if available, so that they can be displayed during the list
	 * control process.
	 * 
	 * @return
	 */
	public	Vector	getPropertiesList()
	{
		return	properties;
	}
}
