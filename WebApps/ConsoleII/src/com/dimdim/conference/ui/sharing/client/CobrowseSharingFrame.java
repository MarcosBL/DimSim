package com.dimdim.conference.ui.sharing.client;

import com.dimdim.conference.ui.json.client.UIResourceObject;

public class CobrowseSharingFrame  extends	CollaborationWidgetFrame{
	
	public	CobrowseSharingFrame()
	{
	}
	public void refreshWidget(UIResourceObject resource,int width, int height)
	{
		//	Does not use the resource.
		super.refreshWidget(resource, width, height);
		this.setSize(width+"px", height+"px");
	}
}
