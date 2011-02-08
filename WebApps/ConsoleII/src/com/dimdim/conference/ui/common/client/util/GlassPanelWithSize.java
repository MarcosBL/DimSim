package com.dimdim.conference.ui.common.client.util;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupListener;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class GlassPanelWithSize extends PopupPanel implements ClickListener, PopupListener{
	protected	VerticalPanel	basePanel = new VerticalPanel();
	protected	ScrollPanel		scrollPanel = new ScrollPanel();
	int height;
	int width;
	Label l = null;
	
	public	GlassPanelWithSize(int width, int height)
	{
		this.height = height;
		this.width = width;
		
		
		//this.height = 100;
		//this.width = 100;
		
		//Window.alert("inside GlassPanelWithSize width = "+width+"height = "+height);
		
		l = new Label(" Locked....");
		add(basePanel);
		l.addClickListener(this);
		basePanel.add(scrollPanel);
		scrollPanel.add(l);
		
		refreshSize(this.width, this.height);
		basePanel.setStyleName("common-glass-panel-lighter");
		//basePanel.setBorderWidth(1);
		basePanel.setCellHorizontalAlignment(l,HorizontalPanel.ALIGN_CENTER);
		basePanel.setCellVerticalAlignment(l,VerticalPanel.ALIGN_MIDDLE);
		
	}
	public	void	onClick(Widget sender)
	{
		//Window.alert("clicked on the glass panel...");
	}
	
	public	void	show(int x, int y)
	{
		this.setPopupPosition(x, y);
		super.show();
	}
	
	public void onPopupClosed(PopupPanel modalDialog, boolean autoClosed)
	{
		if (this.isVisible())
		{
			hide();
		}
	}
	
	public void hide()
	{
		//Window.alert("inside hide og glass panel..");
		super.hide();
	}
	
	public void refreshSize(int width, int height)
	{
		this.height = height;
		this.width = width;
		basePanel.setSize(this.width+"px",this.height+"px");
		//l.setSize(this.width+"px",this.height+"px");
		scrollPanel.setSize(this.width+"px",this.height+"px");
	}
}
