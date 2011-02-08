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
 *                                                                        *
 * Copyright (c) 2006 Communiva Inc. All Rights Reserved.                 *
 *                                                                        *
 *                                                                        *
 * This code is licensed under the DimDim License                         *
 * For details please visit http://www.dimdim.com/license                 *
 *                                                                        *
 **************************************************************************
 */

package com.dimdim.conference.ui.envcheck.client.main;

import java.util.HashMap;
import com.google.gwt.user.client.Timer;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.i18n.client.Dictionary;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.HistoryListener;
import com.google.gwt.user.client.WindowResizeListener;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import com.dimdim.conference.ui.envcheck.client.EnvGlobals;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */

public class EnvCheck implements EntryPoint, WindowResizeListener, HistoryListener
{
	
	public void onHistoryChanged(String historyToken)
	{
		
	}

	//protected	int	windowWidth;
	//protected	int	windowHeight;
	
	protected	CommandProcessor	commandProcessor;
	
	protected	Dictionary	fields = null;
	protected	HashMap		arguments = new	HashMap();
	
	public void onModuleLoad()
	{
		//Window.alert("inside module load...");
		EnvGlobals.setOriginalURL(this.getCurrentURL());
		
		//windowWidth = Window.getClientWidth();
		//windowHeight = Window.getClientHeight();
		
		String actionId = getActionId();
		String browserType = getBrowserType();
		String osType = getOSType();
		
		final CommandProcessor cp = new CommandProcessor(actionId,browserType,osType);
		//Window.alert("created command processor");
//		this.fullPage = new SignInFullPage();
//		cp.setFormContainer(this.fullPage.getFormContainerPanel());
		
//		RootPanel.get().clear();
//		RootPanel.get().add(this.fullPage);

		RootPanel.get("check_header_text").add(new HTML( EnvGlobals.getDisplayString("checkpanel.title","<strong>The checking of essentials...</strong>")));
		RootPanel.get("check_message_text_1").add(new Label( EnvGlobals.getDisplayString("checkpanel.text","This may take a few moments, thanks for your patience")));
		
		changeUIForAction();
		
		if(null != skipCheck() && "true".equals(skipCheck())){
		    //Window.alert("ignoring checks...");
		    cp.allChecksSuccessful();
		}
		else{
		    	//Window.alert("starting timer...");
        		Timer t = new Timer()
        		{
        			public void run()
        			{
        				cp.startChecks();
        			}
        		};
        		t.schedule(500);
		}
		
		Window.addWindowResizeListener(this);
		History.addHistoryListener(this);
	}
	
	private void fillTextPresenter()
	{
	    String text = EnvGlobals.getDisplayString("oscheck.label","Operating systems Check");
	    Label lbl = new Label(text);
	    //lbl.setStyleName("console-label");
	    RootPanel.get("os_check_text").add(lbl);
	    
	    text = EnvGlobals.getDisplayString("browsercheck.label","Browser Support Check");
	    lbl = new Label(text);
	    //lbl.setStyleName("console-label");
	    RootPanel.get("browser_check_text").add(lbl);
	    
	    text = EnvGlobals.getDisplayString("flashcheck.label","Flash Player version check");
	    lbl = new Label(text);
	    //lbl.setStyleName("console-label");
	    RootPanel.get("flash_check_text").add(lbl);
	    
	    String osType = getOSType();
	    if("true".equalsIgnoreCase(getPubEnabled()) && !EnvGlobals.OS_MAC.equals(osType)
			&& !EnvGlobals.OS_LINUX.equals(osType) && !EnvGlobals.OS_UNIX.equals(osType))
	    {
        	    text = EnvGlobals.getDisplayString("pubcheck.label","Plugin Check");
        	    lbl = new Label(text);
        	    //lbl.setStyleName("console-label");
        	    RootPanel.get("plugin_check_text").add(lbl);
	    }
	    
	    VerticalPanel description = new VerticalPanel();
	    Label header = new Label(EnvGlobals.getDisplayString("desc.label","Description"));
	    header.setStyleName("subheadders");
	    description.add(header);
	    HTML seperator1 = new HTML("&nbsp;");
	    description.add(seperator1);
	    description.setCellWidth(seperator1,"100%");
	    
	    Label check1 = new Label(EnvGlobals.getDisplayString("oscheck.desc"," 1. A Meeting can be started Windows XP/Vista/2003/2000, Mac and Linux."));
	    Label check2 = new Label(EnvGlobals.getDisplayString("browsercheck.desc","2. A Meeting can be started on Firefox 1.5 onwards and IE 6 onwards."));
	    //Window.alert("flsh desc...."+EnvGlobals.getDisplayString("flashcheck.desc","3. A Meeeting can be started on Adobe Flash Player (version 9 or higher)."));
	    HTML check3 = new HTML(EnvGlobals.getDisplayString("flashcheck.desc","3. A Meeeting can be started on Adobe Flash Player (version 9 or higher)."));
	    Label check4 = new Label(EnvGlobals.getDisplayString("pubcheck.desc","4. Dimdim Publisher is required to use screen share."));
	    
	    check1.setStyleName("console-label");
	    check2.setStyleName("console-label");
	    check3.setStyleName("console-label");
	    check4.setStyleName("console-label");
	    
	    description.add(check1);
	    HTML seperator2 = new HTML("&nbsp;");
	    description.add(seperator2);
	    description.setCellWidth(seperator2,"100%");
	    
	    description.add(check2);
	    HTML seperator3 = new HTML("&nbsp;");
	    description.add(seperator3);
	    description.setCellWidth(seperator3,"100%");
	    
	    description.add(check3);
	    HTML seperator4 = new HTML("&nbsp;");
	    description.add(seperator4);
	    description.setCellWidth(seperator4,"100%");
	    
	    description.add(check4);
	    
	    RootPanel.get("description_box").add(description);
	    //Window.alert("after fillTextPresenter...");
	}
	
	private void fillTextAttendee()
	{
	    String text = EnvGlobals.getDisplayString("browsercheck.label","Browser Support Check");
	    Label lbl = new Label(text);
	    //lbl.setStyleName("console-label");
	    RootPanel.get("browser_check_text").add(lbl);
	    
	    text = EnvGlobals.getDisplayString("flashcheck.label","Flash Player version check");
	    lbl = new Label(text);
	    //lbl.setStyleName("console-label");
	    RootPanel.get("flash_check_text").add(lbl);
	    
	    VerticalPanel description = new VerticalPanel();
	    Label header = new Label(EnvGlobals.getDisplayString("desc.label","Description"));
	    header.setStyleName("subheadders");
	    description.add(header);
	    HTML seperator1 = new HTML("&nbsp;");
	    description.add(seperator1);
	    description.setCellWidth(seperator1,"100%");
	    
	    Label check2 = new Label(EnvGlobals.getDisplayString("browsercheck.att.desc"," A Meeting can be joined on Firefox 1.5 onwards and IE 6 onwards."));
	    HTML check3 = new HTML(EnvGlobals.getDisplayString("flashcheck.att.desc","A Meeeting can be joined on Adobe Flash Player (version 8 or higher)."));
	    
	    check2.setStyleName("console-label");
	    check3.setStyleName("console-label");
	    
	    description.add(check2);
	    HTML seperator3 = new HTML("&nbsp;");
	    description.add(seperator3);
	    description.setCellWidth(seperator3,"100%");
	    
	    description.add(check3);
	    HTML seperator4 = new HTML("&nbsp;");
	    description.add(seperator4);
	    description.setCellWidth(seperator4,"100%");
	    
	    RootPanel.get("description_box").add(description);
	    //Window.alert("after fillTextAttendee...");
	}
	
	private void changeUIForAction(){
	    //if for join hide few things
		RootPanel.get("plugin_div").setVisible(false);
	    if(EnvGlobals.ACTION_JOIN.equals(getActionId()))
	    {
        	//Window.alert("before making hidden");
        	//RootPanel.get("os_div").setVisible(false);
        	//RootPanel.get("plugin_div").setVisible(false);
//        	/Window.alert("after making hidden");
	    	
	    	//now using preseter stuff for both
        	//fillTextAttendee();
	    	fillTextPresenter();
	    }else{
	    	fillTextPresenter();
	    }
	    //if("true".equalsIgnoreCase(getPubEnabled()) && !EnvGlobals.OS_MAC.equals(getOSType())
		//	&& !EnvGlobals.OS_LINUX.equals(getOSType()) && !EnvGlobals.OS_UNIX.equals(getOSType()))
	    //{
		//RootPanel.get("plugin_div").setVisible(true);
	    //}else{
		//Window.alert("hiding publisher....");
		//RootPanel.get("plugin_div").setVisible(false);
	    //}
	}
	
	public void onWindowResized(int arg0, int arg1)
	{
		//windowWidth = Window.getClientWidth();
		//windowHeight = Window.getClientHeight();
	}
	private native String getCurrentURL() /*-{
		return ($wnd.getCurrentURL());
	}-*/;
	private native String getActionId() /*-{
		return ($wnd.action_id);
	}-*/;
	private native String getBrowserType() /*-{
		return ($wnd.browser_type);
	}-*/;
	private native String getOSType() /*-{
		return ($wnd.os_type);
	}-*/;
	private native String skipCheck() /*-{
	 return $wnd.skip_all;
	}-*/;
	
	private native String getPubEnabled() /*-{
	 return $wnd.start_meeting_enable_publisher;
	}-*/;
}

