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

import pl.rmalinowski.gwt2swf.client.ui.SWFParams;
import pl.rmalinowski.gwt2swf.client.ui.SWFWidget;
import pl.rmalinowski.gwt2swf.client.ui.exceptions.UnsupportedFlashPlayerVersionException;
import pl.rmalinowski.gwt2swf.client.utils.PlayerVersion;
import pl.rmalinowski.gwt2swf.client.utils.SWFObjectUtil;
import pl.rmalinowski.gwt2swf.client.widgets.SWFPopupPanel;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author rmalinowski
 * 
 */
public class GWTFlash implements EntryPoint {
	Button showBtn = new Button("show");
	Button hideBtn = new Button("hide");

	Panel panel = new VerticalPanel();

	public void onModuleLoad() {
		PlayerVersion minPlayerVersion = new PlayerVersion(7, 0, 14);

		boolean versionIsValid = SWFObjectUtil
				.isVersionIsValid(minPlayerVersion);
		if (!versionIsValid) {
			if (SWFObjectUtil.getPlayerVersion().toString().equals("0.0.0")) {
				Window.alert("flash player not installed");
			} else {
				Window
						.alert("not valid version of installed flash player, needed version min: "
								+ minPlayerVersion.toString());
			}
		}

		String swfFile = "pasek.swf";
		SWFParams desc = new SWFParams(swfFile, new Integer(800), new Integer(
				600));
		// desc.setVersion(new PlayerVersion(12));
		final SWFWidget swfWidget = new SWFWidget(desc);

		// panel.add(swfWidget);

		RootPanel.get().add(showBtn);
		RootPanel.get().add(hideBtn);

		try {
			RootPanel.get().add(panel); // Fist you must add swfWidget to
		} catch (UnsupportedFlashPlayerVersionException e) {
			GWT.log("UnsupportedFlashPlayerVersionException", e);
		}
		final SWFPopupPanel popupPanel = new SWFPopupPanel(swfWidget);
		showBtn.addClickListener(new ClickListener() {

			public void onClick(Widget sender) {
				popupPanel.show();
				// panel.add(swfWidget);
				// swfWidget.setVisible(true);
			}

		});

		hideBtn.addClickListener(new ClickListener() {

			public void onClick(Widget sender) {
				// panel.remove(swfWidget);
				// swfWidget.setVisible(false);
				popupPanel.hide();
			}

		});
	}

}
