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

package pl.rmalinowski.gwt2swf.client.widgets;

import pl.rmalinowski.gwt2swf.client.ui.SWFWidget;

import com.google.gwt.user.client.ui.PopupPanel;

public class SWFPopupPanel extends PopupPanel {
	private final SWFWidget widget;

	public SWFPopupPanel(SWFWidget widget) {
		super(true);
		this.widget = widget;
		add(widget);
	}

}
