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

import java.util.Vector;
import java.util.HashMap;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * This list model represents and maintains the object lists in dimdim
 * console 2.0. The list is maintained always in the order of entry.
 * 
 * The list model and the list widget treat all entries the same. If any
 * particular entry requires a special treatment, it must be implemented
 * through the list entry implementation.
 * 
 * By default the visible entries are filled in by the newly added entries
 * till the visible max is reached. If the list is placed in a scroll panel
 * the visible entries number is expected to dictate the height of the
 * scroll panel and if the list is placed in a page browser mode the same
 * parameter is expected to dictate the page size.
 */

public class DefaultList
{
	//	-1 means no limit.
	
	protected	int		maxEntries = -1;
	protected	int		maxVisibleEntries = -1;
	protected	int		pageSize = 10;
	
	protected	Vector  entries;
	protected	HashMap	entriesMap;
	protected	Vector	listeners;
	
	protected	ListControlsProvider	listControlsProvider;
	protected	ListPropertiesProvider	listPropertiesProvider;
	
	protected	ListControlPanel		listControlPanel;
	
	public	DefaultList(int maxEntries, int maxVisibleEntries, int pageSize,
		ListControlsProvider listControlsProvider,
		ListPropertiesProvider listPropertiesProvider)
	{
		this.maxEntries = maxEntries;
		this.maxVisibleEntries = maxVisibleEntries;
		this.pageSize = pageSize;
		
		this.entries = new Vector();
		this.entriesMap = new HashMap();
		this.listeners = new Vector();
		
		this.listControlsProvider = listControlsProvider;
		this.listPropertiesProvider = listPropertiesProvider;
	}
	public	void	addListModelListener(ListModelListener lml)
	{
		if (!this.listeners.contains(lml))
		{
			this.listeners.addElement(lml);
		}
	}
	public	void	removeListModelListener(ListModelListener lml)
	{
		this.listeners.removeElement(lml);
	}
	public ListControlsProvider getListControlsProvider()
	{
		return listControlsProvider;
	}
	public void setListControlsProvider(ListControlsProvider listControlsProvider)
	{
		this.listControlsProvider = listControlsProvider;
	}
	public ListPropertiesProvider getListPropertiesProvider()
	{
		return listPropertiesProvider;
	}
	public void setListPropertiesProvider(
			ListPropertiesProvider listPropertiesProvider)
	{
		this.listPropertiesProvider = listPropertiesProvider;
	}
	public int getPageSize()
	{
		return pageSize;
	}
	public void setPageSize(int pageSize)
	{
		this.pageSize = pageSize;
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
	public ListControlPanel getListControlPanel()
	{
		return listControlPanel;
	}
	public void setListControlPanel(ListControlPanel listControlPanel)
	{
		this.listControlPanel = listControlPanel;
	}
	public	void	addEntry(ListEntry entry)
	{
		this.entries.addElement(entry);
		this.entriesMap.put(entry.getId(), entry);
		int size = this.listeners.size();
		for (int i=0; i<size; i++)
		{
			((ListModelListener)this.listeners.elementAt(i)).listEntryAdded(entry);
		}
	}
	public	void	removeEntry(ListEntry entry)
	{
		this.entries.removeElement(entry);
		this.entriesMap.remove(entry.getId());
		int size = this.listeners.size();
		for (int i=0; i<size; i++)
		{
			((ListModelListener)this.listeners.elementAt(i)).listEntryRemoved(entry);
		}
	}
	public	void	removeEntry(String  id)
	{
		ListEntry entry = this.findEntry(id);
		if (entry != null)
		{
			this.entries.removeElement(entry);
			this.entriesMap.remove(id);
			int size = this.listeners.size();
			for (int i=0; i<size; i++)
			{
				((ListModelListener)this.listeners.elementAt(i)).listEntryRemoved(entry);
			}
		}
	}
	public	int	getListSize()
	{
		return	this.entries.size();
	}
	public	ListEntry	getListEntryAt(int i)
	{
		return	(ListEntry)this.entries.get(i);
	}
	public	ListEntry	findEntry(String id)
	{
		ListEntry entry = (ListEntry)this.entriesMap.get(id);
//		int	size = this.entries.size();
//		for (int i=0; i<size; i++)
//		{
//			entry = (ListEntry)this.entries.elementAt(i);
//			if (entry.getId().equals(id))
//			{
//				break;
//			}
//			else
//			{
//				entry = null;
//			}
//		}
		return	entry;
	}
}
