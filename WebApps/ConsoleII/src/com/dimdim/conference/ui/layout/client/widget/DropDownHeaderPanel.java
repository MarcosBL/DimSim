package com.dimdim.conference.ui.layout.client.widget;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DisclosureEvent;
import com.google.gwt.user.client.ui.DisclosureHandler;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;

public class DropDownHeaderPanel extends Composite implements DisclosureHandler {
	HorizontalPanel basePanel = new HorizontalPanel();
	Label headerLbl = null;
	DisclosurePanel discPanel = null;

	public DropDownHeaderPanel(String header, Composite browseCntrl, DisclosurePanel discPanel )
	{
		this.initWidget(basePanel);
		this.discPanel = discPanel;
		
		headerLbl = new Label(header);
		basePanel.add(headerLbl);
		basePanel.setStyleName("tk-DropDownPanelHeader");
		basePanel.setCellHorizontalAlignment(headerLbl, HorizontalPanel.ALIGN_LEFT);
		
		if(null != browseCntrl)
		{
			HorizontalPanel controlsPanel = new HorizontalPanel();
			controlsPanel.add(browseCntrl);
			//controlsPanel.setBorderWidth(1);
			basePanel.add(controlsPanel);
			basePanel.setCellHorizontalAlignment(controlsPanel, HorizontalPanel.ALIGN_RIGHT);
		}
		
		
	}
	
	public void setText(String text){
		headerLbl.setText(text);
		//basePanel.remove(headerLbl);
		//headerLbl = new Label(text);
		//basePanel.add(headerLbl);
		//basePanel.setCellHorizontalAlignment(headerLbl, HorizontalPanel.ALIGN_LEFT);
	}

	public void onClose(DisclosureEvent event) {
		setStyle();
		
	}

	public void onOpen(DisclosureEvent event) {
		setStyle();
		
	}
	
	private void setStyle() {
	      /*if (discPanel.isOpen()) {
	    	  basePanel.addStyleName("tk-DropDownPanel-open");
	    	  basePanel.sets
	      } else {
	    	  basePanel.addStyleName("tk-DropDownPanel-closed");
	      }*/
	}
}
