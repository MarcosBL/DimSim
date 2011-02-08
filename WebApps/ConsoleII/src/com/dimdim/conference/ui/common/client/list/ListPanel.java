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
 *								                                          *
 * Copyright (c) 2006 Communiva Inc. All Rights Reserved.	              *
 *								                                          *
 *								                                          *
 * This code is licensed under the DimDim License                         *
 * For details please visit http://www.dimdim.com/license		          *
 *									                                      *
 **************************************************************************
 */

package com.dimdim.conference.ui.common.client.list;

import java.util.HashMap;
import java.util.Vector;

import com.dimdim.conference.ui.common.client.user.UserListEntry;
import com.dimdim.conference.ui.common.client.user.UserListEntryWithClicks;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * @author Jayant Pandit
 * 
 * The list panel never modifies the list model itself on its own. The model
 * is always modified by the controllers, the list managers.
 * 
 * Max visible entries for a given list does not change over the lifetime
 * of the list panel.
 */

public class ListPanel	extends	Composite implements ListModelListener
{
	protected	DefaultList				list;
	protected	ListControlsProvider	listControlsProvider;
	protected	ListPropertiesProvider	listPropertiesProvider;
	
	protected	HashMap		listEntriesTable;
	
	protected	Vector		scrollablePanels;
	
	/**
	 * Basic parameters.
	 */
	
	protected	VerticalPanel	basePanel = new VerticalPanel();
	
	protected	VerticalPanel	firstEntryPanel = new VerticalPanel();
	protected	VerticalPanel	secondEntryPanel = new VerticalPanel();
	protected	VerticalPanel	stickyEntriesPanel = new VerticalPanel();
	protected	VerticalPanel	scrollableEntriesPanel = new VerticalPanel();
	
//	protected	ListEntryPanel	firstListEntryPanel;
//	protected	ListEntryPanel	secondListEntryPanel;
	
//	protected	Vector			istEntryPanels;
	
	/**
	 * Page browsing support parameters.
	 */
	
	protected	DefaultListBrowseControl	browseControl;
	
	public ListPanel(DefaultList list)
	{
		initWidget(basePanel);
		this.setStyleName("list-panel");
		
		this.list = list;
		this.listControlsProvider = list.getListControlsProvider();
		this.listPropertiesProvider = list.getListPropertiesProvider();
		this.listControlsProvider.setListPanel(this);
		
		this.listEntriesTable = new HashMap();
		this.scrollablePanels = new Vector();
		
//		this.listEntryPanels = new Vector();
		
		//	Add up all the panels.
		
		this.firstEntryPanel.setStyleName("list-section-container");
		this.secondEntryPanel.setStyleName("list-section-container");
		this.stickyEntriesPanel.setStyleName("list-section-container");
		this.scrollableEntriesPanel.setStyleName("list-section-container");
		
		this.basePanel.add(this.firstEntryPanel);
		this.basePanel.add(this.secondEntryPanel);
		this.basePanel.add(this.stickyEntriesPanel);
		this.basePanel.add(this.scrollableEntriesPanel);
		
		this.browseControl = new DefaultListBrowseControl(this);
		
		int size = list.getListSize();
		for (int i=0; (i<size && i<this.list.getMaxVisibleEntries()); i++)
		{
			this.addEntryPanel(list.getListEntryAt(i));
		}
		
		list.addListModelListener(this);
	}
	public DefaultList getList()
	{
		return list;
	}
	public	int	getNumberOfFixedEntries()
	{
		return	this.firstEntryPanel.getWidgetCount() +
				this.secondEntryPanel.getWidgetCount() +
				this.stickyEntriesPanel.getWidgetCount();
	}
	public	int	getTotalNumberOfEntries()
	{
		return	this.listEntriesTable.size();
	}
	public	int	getNumberOfScrollableEntries()
	{
		return	this.list.getMaxVisibleEntries() - this.getNumberOfFixedEntries();
	}
//	public	Vector	getUIListEntryPanels()
//	{
//		return	this.listEntryPanels;
//	}
	/**
	 * The list entry needs to tell the list in which display panel the entry should
	 * go. This is done through the display rank. 1 for first, 2 for second, 3 for
	 * third and sticky and 4 for the rest. This way if the need be the same can be
	 * implemented for the resource list should be need ever arise. Also the 
	 * 
	 * @param listEntry
	 */
	public	void	addEntryPanel(ListEntry listEntry)
	{
		//Window.alert("adding panel for entry:"+listEntry.id);
		//Window.alert("adding panel for entry:"+listEntry);
		ListEntryPanel listEntryPanel = null;
		if(listEntry instanceof UserListEntry && ! (listEntry instanceof UserListEntryWithClicks))
		{
			//Window.alert("adding instanceof UserListEntry");
			listEntryPanel = new ListEntryPanelWithoutClicks(listEntry);
		}else
		{
			//Window.alert("not an instanceof UserListEntry");
			listEntryPanel = new ListEntryPanel(listEntry);	
		}
		
		listEntryPanel.setListPanel(this);
		
		this.listEntriesTable.put(listEntry.getId(),listEntryPanel);
//		this.listEntryPanels.addElement(listEntryPanel);
		
		VerticalPanel sectionPanel = getSectionPanel(listEntry.getDisplayRank());
		if (sectionPanel != this.firstEntryPanel || sectionPanel.getWidgetCount() > 0)
		{
			listEntryPanel.addStyleName("list-entry-panel-gap");
		}
		this.addEntryPanelToSection(sectionPanel,listEntryPanel);
	}
	/**
	 * If the panel is being added to the first or second section, then add straight away.
	 * For sticky entries add to the 
	 * @param sectionPanel
	 * @param listEntryPanel
	 */
	protected	void	addEntryPanelToSection(VerticalPanel sectionPanel, ListEntryPanel listEntryPanel)
	{
		//Window.alert("addEntryPanelToSection--1");
		if (sectionPanel != this.scrollableEntriesPanel)
		{
			sectionPanel.add(listEntryPanel);
			sectionPanel.setCellHorizontalAlignment(listEntryPanel, HasAlignment.ALIGN_LEFT);
			sectionPanel.setCellVerticalAlignment(listEntryPanel, HasAlignment.ALIGN_MIDDLE);
			sectionPanel.setCellWidth(listEntryPanel, "100%");
			
			listEntryPanel.setPanelVisible(true);
		}
		else
		{
			this.scrollablePanels.add(listEntryPanel);
			int	i = this.getNumberOfScrollableEntries();
			if (this.scrollableEntriesPanel.getWidgetCount() < i)
			{
				sectionPanel.add(listEntryPanel);
				sectionPanel.setCellHorizontalAlignment(listEntryPanel, HasAlignment.ALIGN_LEFT);
				sectionPanel.setCellVerticalAlignment(listEntryPanel, HasAlignment.ALIGN_MIDDLE);
				sectionPanel.setCellWidth(listEntryPanel, "100%");
				
				listEntryPanel.setPanelVisible(true);
			}
			else
			{
				this.browseControl.numberOfPanelsChanged();
			}
		}
//		listEntryPanel.setVisible(false);
//		Window.alert("addEntryPanelToSection--3");
	}
	public	void	moveEntryPanelSection(ListEntryPanel listEntryPanel, int oldRank, int newRank)
	{
//		Window.alert("Old rank:"+oldRank+", new rank:"+newRank);
		VerticalPanel sectionPanel = getSectionPanel(oldRank);
//		Window.alert("Old section:"+sectionPanel.toString());
		boolean reDrawRequired = false;
		if (sectionPanel == this.scrollableEntriesPanel)
		{
			this.scrollablePanels.remove(listEntryPanel);
		}
		if (listEntryPanel.isPanelVisible())
		{
			sectionPanel.remove(listEntryPanel);
			reDrawRequired = true;
		}
		sectionPanel = getSectionPanel(newRank);
		addEntryPanelToSection(sectionPanel,listEntryPanel);
		this.browseControl.numberOfPanelsChanged();
		if (reDrawRequired)
		{
			this.browseControl.showCurrentPage();
		}
	}
	private	VerticalPanel	getSectionPanel(int rank)
	{
		VerticalPanel sectionPanel = this.scrollableEntriesPanel;
		if (rank == 1)
		{
//			Window.alert("Returning panel 1");
			sectionPanel = this.firstEntryPanel;
		}
		else if (rank == 2)
		{
//			Window.alert("Returning panel 2");
			sectionPanel = this.secondEntryPanel;
		}
		else if (rank == 3)
		{
//			Window.alert("Returning panel 3");
			sectionPanel = this.stickyEntriesPanel;
		}
//		Window.alert("Returning panel 4");
		return	sectionPanel;
	}
	public	void	removeEntryPanel(ListEntry listEntry)
	{
		ListEntryPanel listEntryPanel = (ListEntryPanel)this.listEntriesTable.get(listEntry.getId());
		if (listEntryPanel != null)
		{
			VerticalPanel sectionPanel = getSectionPanel(listEntry.getDisplayRank());
			if (sectionPanel == this.firstEntryPanel || sectionPanel == this.secondEntryPanel)
			{
				sectionPanel.remove(listEntryPanel);
				this.listEntriesTable.remove(listEntry.getId());
				this.browseControl.numberOfPanelsChanged();
			}
			else
			{
//				Window.alert("1");
				this.listEntriesTable.remove(listEntry.getId());
//				Window.alert("2");
				if (sectionPanel == this.scrollableEntriesPanel)
				{
//					Window.alert("3");
					this.scrollablePanels.remove(listEntryPanel);
				}
				this.browseControl.numberOfPanelsChanged();
//				Window.alert("4");
				if (listEntryPanel.isPanelVisible())
				{
					sectionPanel.remove(listEntryPanel);
					//	The panel is currently visible so some additional movement in the
					//	scroll might be required.
					this.browseControl.showCurrentPage();
				}
			}
		}
	}
	public	ListEntry	findEntry(String id)
	{
		return	((ListEntryPanel)this.listEntriesTable.get(id)).getListEntry();
	}
	public	ListEntryPanel	findEntryPanel(String id)
	{
		return	(ListEntryPanel)(this.listEntriesTable.get(id));
	}
	public void listEntryAdded(ListEntry newEntry)
	{
		this.addEntryPanel(newEntry);
	}
	public void listEntryRemoved(ListEntry removedEntry)
	{
		this.removeEntryPanel(removedEntry);
	}
	public void listEntryRenamed(ListEntry renamedEntry)
	{
		ListEntryPanel entryPanel = this.findEntryPanel(renamedEntry.getId());
		if (entryPanel != null)
		{
			entryPanel.getNameLabel().setText(renamedEntry.getName());
		}
	}
	public void listEntrySelected(ListEntry listEntry)
	{
	}
	/**
	 * Page browsing interface.
	 */
	public	DefaultListBrowseControl	getListBrowseControl()
	{
		return	this.browseControl;
	}
	public	int	getNumberOfAvailableScrollableEntries()
	{
		return	this.scrollablePanels.size();
	}
	public	void	showPage(int firstScrollableEntryIndex, int numberOfEntries)
	{
//		this.showPageSection(this.firstEntryPanel,0,10,false);
//		this.showPageSection(this.secondEntryPanel,0,10,false);
//		this.showPageSection(this.stickyEntriesPanel,0,10,false);
		this.showPageSection(this.scrollableEntriesPanel,
				firstScrollableEntryIndex, numberOfEntries,true);
	}
	protected	void	showPageSection(VerticalPanel sectionPanel, int start, int num, boolean hideAllFirst)
	{
//		Window.alert("Showing page:"+start+"-"+num);
		/*
		if (hideAllFirst)
		{
			for (int i=0; i<size; i++)
			{
				Widget w = sectionPanel.getWidget(i);
//				Window.alert("Showing widget:"+w);
				w.removeStyleName("list-entry-panel-gap");
				w.setVisible(false);
			}
		}
		*/
		//	First remove the entries from the scrollable section.
		int size = sectionPanel.getWidgetCount();
//		Window.alert("Number of visible panels -- "+size);
		for (int i=size-1; i>=0; i--)
		{
			try
			{
				sectionPanel.remove(i);
			}
			catch(Exception e)
			{
				Window.alert(e.getMessage());
			}
		}
		
		//	Now add the panels from the panels vector into the display panel
		//	Here i<size
//		int maxScrollable = this.getNumberOfScrollableEntries();
		size = this.scrollablePanels.size();
		for (int i=0; i<size; i++)
		{
//			Window.alert("Panel -- "+i);
			ListEntryPanel lep = (ListEntryPanel)this.scrollablePanels.elementAt(i);
			if (i<start)
			{
//				Window.alert("Setting panel non visible");
				lep.setPanelVisible(false);
			}
			else if (i < start+num)
			{
//				Window.alert("Settng panel visible");
				lep.setPanelVisible(true);
				this.scrollableEntriesPanel.add(lep);
				this.scrollableEntriesPanel.setCellWidth(lep,"100%");
			}
			else
			{
//				Window.alert("Setting panel non visible");
				lep.setPanelVisible(false);
			}
		}
		/*
		for (int i=start; i<start+maxScrollable && i<start+num && i<size; i++)
		{
			ListEntryPanel lep = (ListEntryPanel)this.scrollablePanels.elementAt(i);
//			Window.alert("Showing widget:"+w);
//			w.addStyleName("list-entry-panel-gap");
			this.scrollableEntriesPanel.add(lep);
			this.scrollableEntriesPanel.setCellWidth(lep,"100%");
		}
		*/
	}
}
