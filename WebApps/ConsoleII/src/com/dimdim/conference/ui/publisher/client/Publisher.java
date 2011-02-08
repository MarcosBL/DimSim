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

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */

public class Publisher implements EntryPoint
{
//	 HorizontalPanel horPanel = new HorizontalPanel();
//	 VerticalPanel verPanel = new VerticalPanel ();
	public	void	onModuleLoad()
	{
		/*
		  //Window.alert("in on module load...");
		 final Button button = new Button("Get Version");
		 final Label label = new Label();
		 final Button getAppButton = new Button("Get Applciations");  
		    button.addClickListener(new ClickListener() {
		      public void onClick(Widget sender) {
		        if (label.getText().equals(""))
		          label.setText( PublisherInterfaceManager.getManager().getPubVersion());
		        else
		          label.setText("");
		      }
		    });
		    
		    getAppButton.addClickListener(new ClickListener() {
		    	public void onClick(Widget sender) {
		    		//ApplicationWindowsListPanel panel = new ApplicationWindowsListPanel(PublisherInterfaceManager.getManager().getApplicationsList(),-1, 200, 200);
		    		//DefaultCommonDialog dialog = new DefaultCommonDialog("Applications", panel, "List Apps");
		    		//dialog.setVisible(true);
		    		//panel.drawDialog();
		    		//verPanel.add(panel);
		    	}
		    });
		    horPanel.add(button);
		    horPanel.add(label);
		    
		    final Button conversionResult = new Button("Get PPT Conversion Result");  
		    conversionResult.addClickListener(new ClickListener() {
		      public void onClick(Widget sender) {
		        Window.alert("conversion result = "+PublisherInterfaceManager.getManager().isPptUploadActive());
		        //Window.alert("in here...conversionResult");
		      }
		    });
		    
		    final Button screencastResult = new Button("Get screen cast result");  
		    screencastResult.addClickListener(new ClickListener() {
		      public void onClick(Widget sender) {
		        Window.alert("screen cast result = "+PublisherInterfaceManager.getManager().isSharingActive());
		    	  //Window.alert("in here...screencastResult");
		      }
		    });
		    
		    verPanel.add(horPanel);
		    verPanel.add(getAppButton);
		    verPanel.add(conversionResult);
		    verPanel.add(screencastResult);
		  
		    RootPanel.get("slot1").add(verPanel);
		*/
	}
}
