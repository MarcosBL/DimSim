package com.dimdim.conference.ui.layout.client.widget;

import com.dimdim.conference.ui.common.client.util.DebugPanel;
import com.dimdim.conference.ui.json.client.UIRosterEntry;
import com.dimdim.conference.ui.layout.client.main.NewLayout;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.WindowResizeListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ConsoleFullPanel	{

//	protected VerticalPanel fullPanel = new VerticalPanel();
//	protected HorizontalPanel leftPanel = null;
//	protected HorizontalPanel rightPanel = null;
	
	private NewLayout consoleLayout;
	private UIRosterEntry currentUser;
	NewMiddlePanel nmp = null;
	NewTopPanel ntp = null;
	
	public ConsoleFullPanel(NewLayout consoleLayout, UIRosterEntry currentUser)
	{
		this.consoleLayout = consoleLayout;
		this.currentUser = currentUser;
//		this.initWidget(fullPanel);
//		fullPanel.setStyleName("console-page");
//		fullPanel.setWidth("100%");
//		fullPanel.setHeight("100%");
		
		this.consoleLayout = consoleLayout;
		this.currentUser = currentUser;
		
		ntp = new NewTopPanel(consoleLayout, currentUser);
//		fullPanel.add(ntp);
		
		nmp = new NewMiddlePanel(consoleLayout, currentUser);
//		fullPanel.add(nmp);
		
//		fullPanel.setCellHorizontalAlignment(ntp,HorizontalPanel.ALIGN_LEFT);
//		fullPanel.setCellVerticalAlignment(ntp,VerticalPanel.ALIGN_TOP);
		
//		fullPanel.setCellHorizontalAlignment(nmp,HorizontalPanel.ALIGN_LEFT);
		//fullPanel.setCellVerticalAlignment(nmp,VerticalPanel.ALIGN_BOTTOM);
//		fullPanel.setSize("100%", "100%");
		
		//onWindowResized(740, 700);

	}

	public void resizePanel(int width, int height) {
		//Window.alert("inside onWindowResized prevWidth = "+width +"prevHeight ="+height );
		//DebugPanel.getDebugPanel().addDebugMessage("console full panel width:"+width+", height:"+height);
		nmp.resizePanel(width, height);
		
	}
	public	void	divShown(String divId)
	{
		if (this.nmp != null)
		{
			this.nmp.divShown(divId);
		}
	}
	public	void	divHidden(String divId)
	{
		if (this.nmp != null)
		{
			this.nmp.divHidden(divId);
		}
	}

	public NewMiddlePanel getMiddlePanel() {
		return nmp;
	}

	public NewTopPanel getTopPanel() {
		return ntp;
	}
}
