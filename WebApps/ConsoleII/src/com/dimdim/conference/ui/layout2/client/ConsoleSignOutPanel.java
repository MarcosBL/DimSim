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
 *								                                          *
 * Copyright (c) 2006 Communiva Inc. All Rights Reserved.	              *
 *								                                          *
 *								                                          *
 * This code is licensed under the DimDim License                         *
 * For details please visit http://www.dimdim.com/license		          *
 *									                                      *
 **************************************************************************
 */

package com.dimdim.conference.ui.layout2.client;

import com.dimdim.conference.ui.common.client.UIConstants;
import com.dimdim.conference.ui.common.client.UIGlobals;
import com.dimdim.conference.ui.common.client.UIStrings;
import com.dimdim.conference.ui.common.client.data.UIParams;
import com.dimdim.conference.ui.common.client.util.ConfirmationDialog;
import com.dimdim.conference.ui.common.client.util.ConfirmationListener;
import com.dimdim.conference.ui.json.client.UIRosterEntry;
import com.dimdim.conference.ui.model.client.ClientModel;
import com.dimdim.conference.ui.model.client.ConferenceGlobals;
import com.dimdim.conference.ui.model.client.RosterModelAdapter;
import com.dimdim.conference.ui.user.client.UserRosterManager;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.WindowCloseListener;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;


public class ConsoleSignOutPanel extends Composite	implements ClickListener
{
	
//	protected final DockPanel topPanel = new DockPanel();

//	protected final VerticalPanel links = new VerticalPanel();
//	protected final HorizontalPanel linksLine1 = new HorizontalPanel();
//	protected final HTML heading = new HTML("");
	
//	private	String	aboutHeading = UIStrings.getAboutDialogHeader();
//	protected	Label	nameLabel = null;
//	protected	Label	settingsLabel = null;
//	protected	Label	assistantLabel = null;
//	protected	Label	meetingLabel = null;
	protected	Label	logoutLabel = new Label("");
//	protected	Label	aboutLabel = null;
//	protected	Label	feedbackLabel = null;
	
	protected	MainLayout		console;
	protected	UIRosterEntry	currentUser;
	protected	RosterModelAdapter	rosterModelListener;
	protected	UserRosterManager	userManager;
	
	
	public ConsoleSignOutPanel(MainLayout console, UIRosterEntry currentUser)
	{
		this.console = console;
		this.currentUser = currentUser;
		userManager = new UserRosterManager(currentUser);
		this.initWidget(logoutLabel);
		
		UIParams uiParams = UIParams.getUIParams();
		if("true".equalsIgnoreCase(uiParams.getStringParamValue("top_links_sign_out", "true")))
		{
			if (UIGlobals.isPresenter(this.currentUser))
			{
				logoutLabel.setText(UIStrings.getPresenterSignOutLabel());
			}
			else
			{
				logoutLabel.setText(UIStrings.getAttendeeSignOutLabel());
			}
		
			logoutLabel.setTitle(ConferenceGlobals.getTooltip("signout_link"));
			logoutLabel.setWordWrap(false);
			logoutLabel.addClickListener(this);
			logoutLabel.setStyleName("console-top-panel-link");
			logoutLabel.addStyleName("signout-link");
		}
	}
	public void onClick(Widget sender)
	{
		if (sender == this.logoutLabel)
		{
			signout();
		}
	}
	public void signout()
	{
		final WindowCloseListener wcl = console.getFullPanel().getMiddlePanel();
		ConfirmationListener listener = new ConfirmationListener()
		{
			public void onCancel()
			{
			}
			public void onOK()
			{
				Window.removeWindowCloseListener(wcl);
				console.getFullPanel().getMiddlePanel().onSignout();
			}
		};
		String s = UIConstants.getConstants().attendeeDepartureWarning();
		if  (this.currentUser.isHost())
		{
			s = UIConstants.getConstants().presenterDepartureWarning();
		}
		ConfirmationDialog dlg = new ConfirmationDialog(
				"Warning",s+" "+ConferenceGlobals.getDisplayString("top_bar.signout.press.yes", "Please press Yes to confirm."),
				"default-message",listener);
		dlg.drawDialog();
	}
}
