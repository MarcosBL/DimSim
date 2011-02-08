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

package com.dimdim.conference.ui.envcheck.client.command;

import asquare.gwt.tk.client.ui.DropDownPanel;

import com.bouwkamp.gwt.user.client.ui.RoundedPanel;
import com.dimdim.conference.ui.envcheck.client.layout.CheckPanel;
import com.google.gwt.user.client.Window;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 *
 */

public class PublisherCheck	extends	Check
{
	public	static	final	int	CheckIndex	=	3;
	public	static	final	String	CheckName	=	"plugin";

	protected	String	osType;
	protected	boolean	checkDone = false;
//	protected	boolean	installPageLoaded = false;
//	protected	CommonMessagePopup	framePopup;
//	protected	ModalDialog		modalDialog;
//	protected	String	publisherAvailability;

	protected	DropDownPanel	osDropDown;
	protected	RoundedPanel	osCheckPanelRounded;

	public	PublisherCheck(String osType)
	{
		this.osType = osType;
	}
	public	int		runCheck(CheckPanel panel, UserCommand command)
	{
		//Window.alert("Running publisher check");
		String	browserType = this.getBrowserType();
		if (command.isCheckApplicable(PublisherCheck.CheckIndex))
		{
			if (browserType == "unknown")
			{
			    panel.setCheckFailed(getSuccessMessage(PublisherCheck.CheckIndex,
					command.getCommandId(), this.osType));
			    
				return Check.CHECK_FAILURE;
			}else
			{
				//Window.alert("just before calling continuePostInstallWork");
				continuePostInstallWork();
				String publisherAvailability = this.getPublisherAvailability();
				//Window.alert("publisherAvailability = "+publisherAvailability);
				if (publisherAvailability.equals("available"))
				{
				    //Window.alert("Running publisher check success message = "+getSuccessMessage(PublisherCheck.CheckIndex,
				    //		command.getCommandId(), this.osType));
				    panel.setCheckSucceeded(getSuccessMessage(PublisherCheck.CheckIndex,
						command.getCommandId(), this.osType, getRequiredPubVersion()));
				    return	Check.CHECK_SUCCESS;
				}
				else
				{
					if (!checkDone && "true".equalsIgnoreCase(getPubInstall()) )
					{
						checkDone = true;
						String freshInstall = "false";
						if (publisherAvailability == null || publisherAvailability.equalsIgnoreCase("not_available"))
						{
							freshInstall = "true";
						}
						String url = "/"+this.getWebappName()+
							"/html/envcheck/publisherinstall.action?uri="+getUri()+"&freshInstall="+freshInstall;
						forwardToSuccessUrl(url);
					}
				}
				return Check.CHECK_FAILURE;
			}
		}
		else
		{
		    //Window.alert("this check is not applicable");
		    //panel.setCheckNotApplicable();
		    return	Check.CHECK_SUCCESS;
		}
	}
	private native String forwardToSuccessUrl(String url) /*-{
		return $wnd.loadURL(url);
	}-*/;
	private	native String getWebappName() /*-{
		return $wnd.webapp_name;
	}-*/;
	private	native String getPubInstall() /*-{
		return $wnd.installPub;
	}-*/;
	private	native String getUri() /*-{
		return $wnd.uri;
	}-*/;
	private native String getPublisherAvailability() /*-{
		return $wnd.publisher_availability;
	}-*/;
	
	//calling this js again was necessary coz in ff some timing issues were observed
	private native void continuePostInstallWork() /*-{
		$wnd.continuePostInstallWork();
	}-*/;
	private native String getBrowserType() /*-{
		return $wnd.browser_type;
	}-*/;
	
	private native String getRequiredPubVersion() /*-{
		return $wnd.publisher_majorversion+"."+$wnd.publisher_minorversion;
	}-*/;
	

	/**
	 * This method return frist 3 charecters of version like x.y
	 * @return
	 */
	private native String getRequiredPublisherVersion() /*-{
		var requiredVersion = $wnd.required_publisher_version+"";
		var requiredVersionCheck = requiredVersion.substring(0,3);
	  	return requiredVersionCheck+" ";
	}-*/;
	private native String getAvailablePublisherVersion() /*-{
		return $wnd.publisher_available_version;
	}-*/;
	private native String startFirefoxPublisherInstall() /*-{
		return $wnd.installFirefoxPublisher();
	}-*/;	
}
