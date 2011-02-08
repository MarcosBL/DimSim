package com.dimdim.conference.ui.user.client;

import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.Widget;

public class HoverStyler implements MouseListener{

	String hoverSharingStyle = "user-menu-entry-hover";
	MoodListener moodListener = null;
	protected	Widget	w1 = null;
	protected	Widget	w2 = null;
	public HoverStyler(MoodListener moodListener, Widget w1, Widget w2)
	{
		this.w1 = w1;
		this.w2 = w2;
		this.moodListener = moodListener;
	}
	public HoverStyler(MoodListener moodListener)
	{
		this.moodListener = moodListener;
	}
	public void onMouseDown(Widget sender, int x, int y)
	{
	}
	public void onMouseEnter(Widget sender)
	{
		if(w1 != null || w2 != null)
		{
			if (w1 != null)
			{
				w1.removeStyleName("user-menu-entry-enabled");
				w1.addStyleName(hoverSharingStyle);
			}
			if (w2 != null)
			{
				w2.removeStyleName("user-menu-entry-enabled");
				w2.addStyleName(hoverSharingStyle);
			}
		}
		else if(sender != null)
		{
			sender.removeStyleName("user-menu-entry-enabled");
			sender.addStyleName(hoverSharingStyle);
		}
		if(moodListener != null && moodListener.mcp != null)
		{
			moodListener.mcp.hide();
		}
	}
	public void onMouseLeave(Widget sender)
	{
		if(w1 != null || w2 != null)
		{
			if (w1 != null)
			{
				w1.removeStyleName(hoverSharingStyle);
				w1.addStyleName("user-menu-entry-enabled");
			}
			if (w2 != null)
			{
				w2.removeStyleName(hoverSharingStyle);
				w2.addStyleName("user-menu-entry-enabled");
			}
		}
		else if(sender != null)
		{
			sender.removeStyleName(hoverSharingStyle);
			sender.addStyleName("user-menu-entry-enabled");
		}
	}
	public void onMouseMove(Widget sender, int x, int y)
	{
	}
	public void onMouseUp(Widget sender, int x, int y)
	{
	}
}
