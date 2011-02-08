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
 * Part of the DimDim V 2.0 Codebase (http://www.dimdim.com)	          *
 *                                                                        *
 * Copyright (c) 2007 Dimdim Inc. All Rights Reserved.              	  *
 *                                                                        *
 *                                                                        *
 * This code is licensed under the DimDim License                         *
 * For details please visit http://www.dimdim.com/license                 *
 *                                                                        *
 **************************************************************************
 */

package com.dimdim.conference.ui.publisher.client;

import java.util.HashMap;

import org.gwtwidgets.client.ui.PNGImage;

import com.dimdim.conference.ui.common.client.common.TimeAndFocusSensitiveModalDialog;
import com.dimdim.conference.ui.common.client.common.TimeSensitiveInfo;
import com.dimdim.conference.ui.common.client.layout.WorkspaceTabPanelContent;
import com.dimdim.conference.ui.json.client.UIRosterEntry;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SourcesTableEvents;
import com.google.gwt.user.client.ui.TableListener;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */

public class ApplicationWindowsListPanel //extends WorkspaceTabPanelContent implements TableListener
{
	/*
	//protected	ScrollPanel	rootPanel = new ScrollPanel();
	
	//protected	GridPanel	gridPanel = null;
	JSONArray jarray  = null;
	protected HashMap appsMap = new HashMap();
	double sharedAppHandle = -1;
	
	ScrollPanel scroller = null; 
	FlexTable table = null;//new FlexTable();
	String sharedIconLoc = "images/appShared.gif";
	String notSharedIconLoc = "images/appNotShared.png";
	String applicationIconLoc = "images/application.png";
	
	int width;
	int height;

	private ApplicationWindowsListPanelListener appListListener;
	private TimeAndFocusSensitiveModalDialog tfd = null;
	//private TimeSensitiveInfo dlg = null;

	public ApplicationWindowsListPanel(UIRosterEntry me, JSONArray jarray, double sharedAppHandle, int width, int height)
	{
	    	super(me, width, height);
		//initWidget(basePanel);
	    	
		//super("Applications");
		//this.rootPanel.setStyleName("list-entry-panel");
		this.resourceSharingPlayer = null;
		this.jarray = jarray;
		this.sharedAppHandle = sharedAppHandle;
		this.height = height;
		this.width = width;
		
		createAppTable(jarray, sharedAppHandle);
		basePanel.setHeight(this.height+"px");
		basePanel.setWidth(this.width+"px");
	}

	public	Widget	getTabContent(String tabName){
	    if (tabName.equalsIgnoreCase(WorkspaceTabPanelContent.ApplicationTab))
	    {
    		//Window.alert("returning apppanel...");
    		return this;
	    }
	    else {
		//Window.alert("returning null...");
		return null;
	    }
	}
	
	public void workspaceResized(int newWorkspaceWidth, int newWorkspaceHeight)
	{
		this.width = newWorkspaceWidth;
		this.height = newWorkspaceHeight;
		//Window.alert("inside app panel Resizing workspace tab content:"+width+"--"+height);
		
		basePanel.setHeight(this.height+"px");
		basePanel.setWidth(this.width+"px");
		table.setWidth((width-40)+"px");
		//table.setHeight((height-20)+"px");
		scroller.setHeight(height+"px");
		scroller.setWidth(width+"px");
	}
	
	private void createAppTable(JSONArray jarray, double sharedAppHandle)
	{
	    String appName = null;
	    double appHandle = -1;
	    JSONValue tempValue = null;
	    String shareStatusImgLoc = notSharedIconLoc;
	    String shareAction = "Share";
	    //String imageLoc = "images/appNotShared.png";
	    PNGImage image = null;
	    int  size = jarray.size();
	    //gridPanel = new GridPanel(rootPanel, 1, 4, false);
	    //Window.alert("inside applciation panel share app handle = "+sharedAppHandle);
	    if(scroller != null)
	    {
		basePanel.remove(scroller);
	    }
	    table = new FlexTable();    
	    table.setStyleName("console-apptable");
	    //table.setWidth((width-20)+"px");
	    //table.setHeight((height-20)+"px");
	    
	    scroller = new ScrollPanel();
	    
	    scroller.add(table);
	    
	    table.setText(0, 0, " ");
	    table.setHTML(0, 1, "<b> Available Screens</b>");
	    table.setHTML(0, 2, "<b> Status</b>");
	    //table.setHTML(0, 3, "<b> Action</b>");
	    
	    table.getCellFormatter().setAlignment(0, 1, HorizontalPanel.ALIGN_CENTER, VerticalPanel.ALIGN_MIDDLE);
	    table.getCellFormatter().setAlignment(0, 2, HorizontalPanel.ALIGN_CENTER, VerticalPanel.ALIGN_MIDDLE);
	    //table.getCellFormatter().setAlignment(0, 3, HorizontalPanel.ALIGN_CENTER, VerticalPanel.ALIGN_MIDDLE);
	    
	    table.getRowFormatter().addStyleName(0, "console-applist-header-row");
	    
	    table.getColumnFormatter().setWidth(0, "70px");
	    //table.getColumnFormatter().setWidth(1, "80%");
	    table.getColumnFormatter().setWidth(2, "70px");
	    //table.getColumnFormatter().setWidth(2, (width-500)+"px");
	    

	    ApplicationWindowItem desktopItem = new ApplicationWindowItem(0, "desktop");
	    
	    table.setWidget(1, 0, new PNGImage(applicationIconLoc, 16, 16));
	    table.setWidget(1, 1, desktopItem);
	    if(sharedAppHandle == 0){
	    	table.setWidget(1, 2, new PNGImage(sharedIconLoc, 16, 16));
	    	desktopItem.setShared(true);
	    	//table.setText(1, 3, "Stop");
	    }else{
	    	table.setWidget(1, 2, new PNGImage(notSharedIconLoc, 16, 16));
	    	//table.setText(1, 3, "Share");
	    }
	    table.getCellFormatter().setAlignment(1, 0, HorizontalPanel.ALIGN_CENTER, VerticalPanel.ALIGN_MIDDLE);
	    table.getCellFormatter().setAlignment(1, 2, HorizontalPanel.ALIGN_CENTER, VerticalPanel.ALIGN_MIDDLE);
	    //table.getCellFormatter().setAlignment(1, 3, HorizontalPanel.ALIGN_CENTER, VerticalPanel.ALIGN_MIDDLE);
	    
	    //table.getCellFormatter().addStyleName(1, 3, "console-share-action");
	    table.getCellFormatter().addStyleName(1, 1, "console-share-action");
	    table.getRowFormatter().addStyleName(1, "console-applist-odd-row");

	    for (int i=0; i<size; i++)
	    {
	    	JSONValue arrayMember = jarray.get(i);
	    	//Window.alert("Reading array member:"+arrayMember);
	    	JSONObject data = arrayMember.isObject();
	    	//Window.alert("Read array member:"+data);
	    	tempValue = data.get("handle");
	    	appHandle = tempValue.isNumber().getValue();
	    	//Window.alert("appHandle :"+appHandle );
	    	
	    	tempValue = data.get("caption");
	    	appName = tempValue.isString().stringValue();
	    	//Window.alert("appName :"+appName );
	    	
	    	ApplicationWindowItem appItem = new ApplicationWindowItem(appHandle, appName);
	    	//there was some problem in comparing with doubles so comparing them as strings
	    	if(String.valueOf(sharedAppHandle).equals(String.valueOf(appHandle))){
	    		//Window.alert("inside shared....");
	    	    	shareStatusImgLoc = sharedIconLoc;
	    		//imageLoc = "images/appShared.gif";
	    		shareAction = "Stop";
	    		appItem.setShared(true);
	    	}else{
	    		//Window.alert("inside NOT shared....");
	    	    	shareStatusImgLoc = notSharedIconLoc;
	    		//imageLoc = "images/appNotShared.png";
	    		shareAction = "Share";
	    	}
	    	
	    	image = new PNGImage(shareStatusImgLoc, 16, 16);
	    	image.setTitle(shareAction);
	    	table.setWidget(i+2, 0, new PNGImage(applicationIconLoc, 16, 16));
	    	table.setWidget(i+2, 1, appItem);
	    	table.setWidget(i+2, 2, image);
	    	//table.setText(i+2, 3, shareAction);
	    	appsMap.put(Double.toString(appItem.getAppHandle()), appItem.getAppName());
	    	table.getCellFormatter().setAlignment(i+2, 0, HorizontalPanel.ALIGN_CENTER, VerticalPanel.ALIGN_MIDDLE);
	    	table.getCellFormatter().setAlignment(i+2, 1, HorizontalPanel.ALIGN_LEFT, VerticalPanel.ALIGN_MIDDLE);
	    	table.getCellFormatter().setAlignment(i+2, 2, HorizontalPanel.ALIGN_CENTER, VerticalPanel.ALIGN_MIDDLE);
	    	//table.getCellFormatter().setAlignment(i+2, 3, HorizontalPanel.ALIGN_CENTER, VerticalPanel.ALIGN_MIDDLE);
	    	//table.getCellFormatter().addStyleName(i+2, 3, "console-share-action");
	    	table.getCellFormatter().addStyleName(i+2, 1, "console-share-action");
	    	
	    	if((i+2)%2 == 0)
	    	{
	    	    table.getRowFormatter().addStyleName(i+2, "console-applist-even-row");
	    		
	    	}else{
	    	    table.getRowFormatter().addStyleName(i+2, "console-applist-odd-row");
	    	}
	    	//rootPanel.add(appItem);
	    }
	    table.addTableListener(this);
	    
	    workspaceResized(width, height);
	    basePanel.add(scroller);
	}

	public String getAppNameFromHandle(double handle){
		return (String)appsMap.get(Double.toString(handle));
	}
	/*protected Widget getContent() {
		String appName = null;
		double appHandle = -1;
		JSONValue tempValue = null;
		int  size = jarray.size();
		for (int i=0; i<size; i++)
		{
			JSONValue arrayMember = jarray.get(i);
			//Window.alert("Reading array member:"+arrayMember);
			JSONObject data = arrayMember.isObject();
			//Window.alert("Read array member:"+data);
			tempValue = data.get("handle");
			appHandle = tempValue.isNumber().getValue();
			//Window.alert("appHandle :"+appHandle );
			
			tempValue = data.get("caption");
			appName = tempValue.isString().stringValue();
			//Window.alert("appName :"+appName );
			
			ApplicationWindowItem appItem = new ApplicationWindowItem(appHandle, appName);
			rootPanel.add(appItem);
		}
		return rootPanel;
	}*--/

	/*protected Vector getFooterButtons() {
		// TODO Auto-generated method stub
		return null;
	}*--/
	
	public void addApplicationWindowsListPanelListener(ApplicationWindowsListPanelListener appListListener){
		this.appListListener = appListListener;
	}

	public void onCellClicked(SourcesTableEvents arg0, int row, int col) {
		
	    if(col == 1){
		ApplicationWindowItem appItem = (ApplicationWindowItem)table.getWidget(row, 1);
	    
		//Window.alert("place holder ....inside cell clicked appItem.isShared() = "+appItem.isShared());
		
		
		//show this only application not for desktop
		/*if(!appItem.isShared()  )
		{
		    if(appItem.getAppHandle() != 0){
			Window.alert("have to show start share");
			tfd = new TimeAndFocusSensitiveModalDialog();
			tfd.setCaption("Attempting to Start Share...", false);
			tfd.show();
		    }
		}else{
		    Window.alert("have to show stop share");
		    tfd = new TimeAndFocusSensitiveModalDialog();
		    tfd.setCaption("Attempting to Stop Share...", false);
		    tfd.show();
		}*--/
		
		if(null != appListListener){
			appListListener.applicationWindowSelected(appItem);	
		}
		
		if(appItem.isShared()){
		    //Window.alert("inside panel appitem current state is shared sharedAppHandle = "+sharedAppHandle);
		    table.setWidget(row, 2, new PNGImage(sharedIconLoc, 16, 16));
		    //table.setText(row, 3, "Stop");
		    //sharedAppHandle = appItem.appHandle;
		    makeAllAppUnshare();
		}else{
		    //Window.alert("inside panel appitem current state is stopped  sharedAppHandle = "+sharedAppHandle);
		    table.setWidget(row, 2, new PNGImage(notSharedIconLoc, 16, 16));
		   // table.setText(row, 3, "Share");
		    //sharedAppHandle = -1;
		    makeAllAppUnshare();
		}
	    }//ignore clicks on other columns
		
	}

	private void makeAllAppUnshare()
	{
	    int count = table.getRowCount();
	    for(int i = 1;  i < count; i++ )
	    {
		
		ApplicationWindowItem appItem = (ApplicationWindowItem)table.getWidget(i, 1);
		if (sharedAppHandle != appItem.getAppHandle()){
		    table.setWidget(i, 2, new PNGImage(notSharedIconLoc, 16, 16));
		    //table.setText(i, 3, "Share");
		    //break;
		}
	    }
	    
	}

	public void refresh()
	{
//	    createAppTable(PublisherInterfaceManager.getManager().getApplicationsList(), -1);
	    
	}
	
	public void refresh(double appHandle)
	{
	    //Window.alert("inside apppanle refresh... apphandle = "+appHandle);
	    closePopup();
	    sharedAppHandle = appHandle;
//	    createAppTable(PublisherInterfaceManager.getManager().getApplicationsList(), appHandle);
	    
	}

	public void closePopup(){
	    if(null != tfd)
	    {
		//Window.alert("do u weant to close..?");
		tfd.closePopup();
		tfd.hide();
		//dlg.hide();
	    }
	    tfd = null;
	}
	
	public  void showDeskTopPopup(){
	    HorizontalPanel hp = new HorizontalPanel();
	    Label label = new Label("Attempting to Start Desktop Share...");
	    label.setStyleName("common-text");
	    label.setWordWrap(true);
	    hp.add(label);
	    
	    //dlg = new DefaultCommonDialog("Info", label, "default-message");
	    tfd = new TimeSensitiveInfo("Attempting to Start Desktop Share...");
	    //dlg = new TimeSensitiveInfo("Attempting to Start Desktop Share...");
	    tfd.setCaption("Info...", false);
	    //tfd.add(hp);
	    //Window.alert("got a dialog...drawing it..");
	    tfd.show();
	    //Window.alert("got a dialog...drawing it..");
	    //dlg.drawDialog();
	}
	
	public void showAppStartPopup(){
	   //tfd = new TimeAndFocusSensitiveModalDialog();
	   // tfd.setCaption("Attempting to Start Screen Share...", false);
	   // tfd.show();
	    
	    Label label = new Label("Attempting to Start Screen Share...");
	    label.setStyleName("common-text");
	    label.setWordWrap(true);
	    tfd = new TimeSensitiveInfo("Attempting to Start Screen Share...");
	    //dlg = new TimeSensitiveInfo("Attempting to Start Screen Share...");
	    tfd.setCaption("Info...", false);
	    //tfd.add(label);
	    //Window.alert("got a dialog...drawing it..");
	    tfd.show();
	    //dlg = new DefaultCommonDialog("Info", label, "default-message");
	    //Window.alert("got a dialog...drawing it..");
	    //dlg.drawDialog();
	}
	
	public void showAppStopPopup(){
	    //tfd = new TimeAndFocusSensitiveModalDialog();
	    //tfd.setCaption("Attempting to Stop Screen Share...", false);
	    //tfd.show();
	}
	
	public double getSharedAppHandle()
	{
	    return sharedAppHandle;
	}
	*/
}
