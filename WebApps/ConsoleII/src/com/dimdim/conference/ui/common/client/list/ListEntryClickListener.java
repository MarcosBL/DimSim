package com.dimdim.conference.ui.common.client.list;

import com.dimdim.conference.ui.common.client.UIGlobals;
import com.dimdim.conference.ui.common.client.util.DmGlassPanel2;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.NonModalPopupPanel;
import com.google.gwt.user.client.ui.Widget;

public  abstract class ListEntryClickListener implements ClickListener{

//	protected	static	PopupPanel	listEntryFocusPopup = null;
	
	//	This panel is required simply to get its current width and height so that
	//	the hover popup can be displayed at proper position.
	
    protected	ListEntry					listEntry;
//	protected	ListEntryPropertiesProvider	listEntryPropertiesProvider;
//	protected	ListEntryControlsProvider	listEntryControlsProvider;
	
	protected	NonModalPopupPanel		theHoverPopup = null;
//	protected	boolean		secondLevelPopup = false;
	
	public ListEntryClickListener(ListEntry listEntry)
//			ListEntryPropertiesProvider listEntryPropertiesProvider,
//			ListEntryControlsProvider listEntryControlsProvider)
	{
		this.listEntry = listEntry;
//		this.listEntryPropertiesProvider = listEntryPropertiesProvider;
//		this.listEntryControlsProvider = listEntryControlsProvider;
	}

	public void onClick(Widget sender)
	{
		try
		{
			UIGlobals.closePreviousHoverPopup();
			this.createPopupPanel();
			int left = sender.getAbsoluteLeft()+UIGlobals.getHoverPopupWidthClearance();
			int top = sender.getAbsoluteTop()-UIGlobals.getHoverPopupHeightClearance();
			
			UIGlobals.setLastPopupPosition(left,top);
			//Window.alert("inside ListEntryClickListener on click");
			if(this.theHoverPopup != null)
			{
				DmGlassPanel2 dgp = new DmGlassPanel2(theHoverPopup);
				dgp.show(left, top);
//				this.theHoverPopup.setPopupPosition(left, top);
				//Window.alert("theHoverPopup is not null so hidiing all other popups");
//				UIGlobals.showHoverPopup(this.theHoverPopup);
			
				((ListEntryHoverPopupPanel)theHoverPopup).popupVisible();
			}
			
			
//			this.theHoverPopup.popupVisible();
//			Window.alert("8");
//			UIGlobals.consoleMiddleLeftHoverPopup = this.theHoverPopup;
//			this.theHoverPopup.showHoverPopup();
//			Window.alert("9");
//			this.theHoverPopup.setVisible(true);
			
		}
		catch(Exception e)
		{
//			Window.alert(e.getMessage());
		}
	}
	
	
	protected	abstract	void	createPopupPanel();
	
}
