
/*
 *    Copyright 2007 Rafal M.Malinowski
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *   
 */

package pl.rmalinowski.gwt2swf.client;

import pl.rmalinowski.gwt2swf.client.ui.SWFCallableWidget;
import pl.rmalinowski.gwt2swf.client.ui.SWFParams;
import pl.rmalinowski.gwt2swf.client.ui.SWFWidget;
import pl.rmalinowski.gwt2swf.client.ui.exceptions.UnsupportedFlashPlayerVersionException;
import pl.rmalinowski.gwt2swf.client.utils.SWFObjectUtil;
import pl.rmalinowski.gwt2swf.client.widgets.SWFPopupPanel;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class GWTFlash2 implements EntryPoint {
	public static native String myMethod(String arg) /*-{
    eval("var myVar = 'arg is " + arg + "';");
    return myVar;
}-*/;
	public static native void alert(String msg) /*-{
	 $wnd.alert(msg);
	 }-*/;

	public static native void hej(String msg) /*-{
	 $wnd.hej(msg);
	 }-*/;

	public static native void sendObj(Object obj) /*-{
	 $wnd.showObj(obj);
	 }-*/;

	
	Button showBtn = new Button("show");
	Button hideBtn = new Button("hide");
	
	public void onModuleLoad4() {
		GWT.log("Starnt", null);
	}
	
	public void onModuleLoad8() {
		SWFObjectUtil.getPlayerVersion();
	}
	
	public void onModuleLoad() {
		GWT.log("Starnt", null);
		String swfFile = "pasek.swf";
		SWFParams desc = new SWFParams(swfFile,new Integer(800),new Integer(600));
		final SWFWidget swfWidget = new SWFWidget(desc);	
		RootPanel.get().add(swfWidget); //Fist you must add swfWidget to RootPanel
		try {
            swfWidget.show();
        } catch (UnsupportedFlashPlayerVersionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }  // And after that you can safety show swf movie
		
	}
	
	public void onModuleLoad5() {
		GWT.log("Starnt", null);
	
		String swfFile = "gwt2swf.swf";
		//String swfFile = "gwt2swf.swf";
//		String swfFile = "getFlashInfo.swf";
		SWFParams desc = new SWFParams(swfFile,new Integer(800),new Integer(600));
		//desc.getVersion().setMajor(7);
		final SWFCallableWidget swfWidget = new SWFCallableWidget(desc);	
		RootPanel.get().add(swfWidget); //Fist you must add swfWidget to RootPanel
		try {
            swfWidget.show();
        } catch (UnsupportedFlashPlayerVersionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }  // And after that you can safety show swf movie
		
		RootPanel.get().add(showBtn);
		
		showBtn.addClickListener(new ClickListener() {

			public void onClick(Widget sender) {
				swfWidget.call("setSWFText");
			}
			
		});
		 
	}
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad2() {
		
		GWT.log("Starnt", null);
	
		
		SWFParams desc = new SWFParams("gwt2swf.swf",new Integer(200),new Integer(200));
		//desc.getVersion().setMajor(7);
		final SWFWidget swfWidget = new SWFWidget(desc);	
		final SWFWidget swfWidget2 = new SWFWidget(desc);	
		final SWFPopupPanel panel = new SWFPopupPanel(swfWidget);
		//swfWidget.initSWF();
		RootPanel.get().add(showBtn);
		RootPanel.get().add(hideBtn);
		
		showBtn.addClickListener(new ClickListener() {

			public void onClick(Widget sender) {
				panel.show();
			}
			
		});
		
		hideBtn.addClickListener(new ClickListener() {
			
			public void onClick(Widget sender) {
				panel.hide();
			}
			
		});
		 RootPanel.get().add(swfWidget2);
		 try {
            swfWidget2.show();
        } catch (UnsupportedFlashPlayerVersionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
		// swfWidget.initSWF();
		
		
		//panel.add(swfWidget);
		
		
		
		
	}
	
	
	
}
