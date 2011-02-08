package com.dimdim.conference.ui.resources.client;

import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.Widget;

public class ResourceHoverStyler implements MouseListener
{
	protected	String	hoverStyle = "common-list-entry-label-hover";
	protected	String	normalStyle = "common-list-entry-label";
	
	protected	Widget	widget;
	
	public ResourceHoverStyler()
	{
		
	}
	public ResourceHoverStyler(Widget widget)
	{
		this.widget = widget;
	}
	public void onMouseDown(Widget sender, int x, int y)
	{
	}
	public void onMouseEnter(Widget sender)
	{
		if (this.widget != null)
		{
			widget.removeStyleName(normalStyle);
			widget.addStyleName(hoverStyle);
		}
		else if(sender != null)
		{
			sender.removeStyleName(normalStyle);
			sender.addStyleName(hoverStyle);
		}
	}
	public void onMouseLeave(Widget sender)
	{
		if (this.widget != null)
		{
			widget.removeStyleName(hoverStyle);
			widget.addStyleName(normalStyle);
		}
		else if(sender != null)
		{
			sender.removeStyleName(hoverStyle);
			sender.addStyleName(normalStyle);
		}
	}
	public void onMouseMove(Widget sender, int x, int y)
	{
	}
	public void onMouseUp(Widget sender, int x, int y)
	{
	}
}
