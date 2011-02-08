package com.dimdim.conference.ui.common.client.list;

import java.util.Vector;

import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * This class makes the resource panel in such a way that the entries are alphabetically sorted
 * @author dilip
 *
 */
public class SortedListPanel extends ListPanel{

	public SortedListPanel(DefaultList list) {
		super(list);
	}

	/*public	void	moveEntryPanelSection(ListEntryPanel listEntryPanel, int oldRank, int newRank)
	{
		Window.alert("Old rank:"+oldRank+", new rank:"+newRank);
		VerticalPanel sectionPanel = getSectionPanel(oldRank);
		//Window.alert("Old section:"+sectionPanel.toString());
		boolean reDrawRequired = false;
		if (sectionPanel == this.scrollableEntriesPanel)
		{
			this.scrollablePanels.remove(listEntryPanel);
			this.scrollableEntriesPanel.remove(listEntryPanel);
			//Window.alert("removing from scrollable entries");
		}
		if (listEntryPanel.isPanelVisible())
		{
			//Window.alert("removing from just sction panel");
			sectionPanel.remove(listEntryPanel);
			reDrawRequired = true;
		}
		sectionPanel = getSectionPanel(newRank);
		addEntryPanelToSection(sectionPanel,listEntryPanel);
		
		Window.alert("after addEntryPanelToSection!@#@#");
		this.browseControl.numberOfPanelsChanged();
		//Window.alert("1");
		if (reDrawRequired)
		{
			//Window.alert("2");
			this.browseControl.showCurrentPage();
		}
		//Window.alert("3");
	}*/
	
	/*public	void	addEntryPanel(ListEntry listEntry)
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
	}*/
	
	/*private	VerticalPanel	getSectionPanel(int rank)
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
	}*/
	
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
				//Window.alert("inside addEntryPanelToSection adding section panel ...");
				sectionPanel.add(listEntryPanel);
				sectionPanel.setCellHorizontalAlignment(listEntryPanel, HasAlignment.ALIGN_LEFT);
				sectionPanel.setCellVerticalAlignment(listEntryPanel, HasAlignment.ALIGN_MIDDLE);
				sectionPanel.setCellWidth(listEntryPanel, "100%");
				
				listEntryPanel.setPanelVisible(true);
			}
		else
		{
			//Window.alert("adding to scrollable panels so let us try with index..scrollableEntriesPanel.getWidgetCount() = "+scrollableEntriesPanel.getWidgetCount());
			int index = getIndex(sectionPanel, listEntryPanel);
			//Window.alert("adding to scrollable at index = "+index);
			//this.scrollablePanels.add(listEntryPanel);
			//this.scrollablePanels.add(listEntryPanel);
			//add in sorted order
			addToVector(scrollablePanels, listEntryPanel);
			
			//Window.alert("scrollablePanels = "+scrollablePanels);
			//this.scrollablePanels.insertElementAt(listEntryPanel, index);
			
			int	i = this.getNumberOfScrollableEntries();
			//Window.alert("i = "+i);
			if (this.scrollableEntriesPanel.getWidgetCount() < i)
			{
				//Window.alert("inside addEntryPanelToSection inserting into sectionPanel at index = "+index);
				sectionPanel.insert(listEntryPanel, index);
				sectionPanel.setCellHorizontalAlignment(listEntryPanel, HasAlignment.ALIGN_LEFT);
				sectionPanel.setCellVerticalAlignment(listEntryPanel, HasAlignment.ALIGN_MIDDLE);
				sectionPanel.setCellWidth(listEntryPanel, "100%");
				listEntryPanel.setPanelVisible(true);
			}
			else
			{
				//Window.alert("calling number of panel changed");
				this.browseControl.numberOfPanelsChanged();
				this.browseControl.setControlsVisibility();
				this.browseControl.showCurrentPage();
			}
			
		}
		
//		listEntryPanel.setVisible(false);
		//Window.alert("addEntryPanelToSection--3");
	}

	private void addToVector(Vector scrollablePanels, ListEntryPanel listEntryPanel) {
		int count = scrollablePanels.size();
		String tempEntryName = null;
		String currEntryName = null;
		ListEntryPanel tempEntry = null;
		boolean foundGreaterEntry = false;
		
		currEntryName = listEntryPanel.getNameLabel().getText();
		if(count == 0)
		{
			//Window.alert("adding currEntryName "+currEntryName + " adding at the end");
			scrollablePanels.add(listEntryPanel);
			return;
		}
		
		for(int i = 0; i < count ; i++)
		{
			tempEntry = (ListEntryPanel)scrollablePanels.get(i);
			tempEntryName = tempEntry.getNameLabel().getText();
			
			
			tempEntryName = tempEntryName.toLowerCase();
			currEntryName = currEntryName.toLowerCase();
			//Window.alert("currEntryName = "+currEntryName + " tempEntryName = "+tempEntryName);
			//Window.alert("the result of coparision = "+tempEntryName.compareTo(currEntryName));
			if(tempEntryName.compareTo(currEntryName ) < 0)
			{
				//Window.alert("inside continue");
				continue;
			}else{
				foundGreaterEntry = true;
				//Window.alert("adding currEntryName "+currEntryName + " to index in vector = "+i);
				scrollablePanels.insertElementAt(listEntryPanel, i);
				break;
			}
		}
		
		if(!foundGreaterEntry)
		{
			//Window.alert("adding currEntryName "+currEntryName + " adding at the end");
			scrollablePanels.add(listEntryPanel);
		}
		
	}

	private int getIndex(VerticalPanel sectionPanel, ListEntryPanel listEntryPanel) {
		int entryCount = sectionPanel.getWidgetCount();
		ListEntryPanel tempEntry = null;
		String tempEntryName = null;
		String currEntryName = null;
		int indexToInsert = 0;
		int i = 0;
		boolean foundGreaterEntry = false;
		
		if(entryCount == 0)
		{
			//Window.alert("returning index = "+indexToInsert);
			return indexToInsert;
		}
		for ( i = 0; i < entryCount; i++) {
			tempEntry = (ListEntryPanel)sectionPanel.getWidget(i);
			tempEntryName = tempEntry.getNameLabel().getText();
			currEntryName = listEntryPanel.getNameLabel().getText();
			
			tempEntryName = tempEntryName.toLowerCase();
			currEntryName = currEntryName.toLowerCase();
			
			//Window.alert("tempEntryName = "+tempEntryName);
			//Window.alert("currEntryName = "+currEntryName);
			//Window.alert("the result of coparision = "+tempEntryName.compareTo(currEntryName));
			if(tempEntryName.compareTo(currEntryName ) < 0)
			{
				//Window.alert("inside continue");
				continue;
			}else{
				foundGreaterEntry = true;
				//Window.alert("inside break");
				break;
			}
		}
		
		
		indexToInsert = i;
		//Window.alert("tempEntryName = "+ tempEntryName + " currEntryName = "+currEntryName);
		//Window.alert("indexToInsert = "+ indexToInsert + " foundGreaterEntry = "+foundGreaterEntry);
		/*if(foundGreaterEntry && indexToInsert != 0)
		{
			//this means we found an entry greater then this so returning a lesser index 
			indexToInsert += 1;
		}*/
		
		//Window.alert("returning index = "+indexToInsert);
		return indexToInsert;
	}
	
	protected	void	showPageSection1(VerticalPanel sectionPanel, int start, int num, boolean hideAllFirst)
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
				//Window.alert(e.getMessage());
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
				
				int index = getIndex(sectionPanel, lep);
				//Window.alert("Settng panel visible index = "+index +" for entry ="+lep.getNameLabel().getText());
				lep.setPanelVisible(true);
				this.scrollableEntriesPanel.insert(lep, index);
				//this.scrollableEntriesPanel.sdf(lep);
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
