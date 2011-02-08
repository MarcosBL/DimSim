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

import asquare.gwt.tk.client.ui.ModalDialog;

import com.dimdim.conference.ui.envcheck.client.EnvGlobals;
import com.dimdim.conference.ui.envcheck.client.layout.CheckPanel;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */

public class FlashVersionCheck	extends	Check
{
	public	static	final	int	CheckIndex	=	2;
	public	static	final	String	CheckName	=	"flash";
	public	static	final	String	reqMajorVer9	=	"9";
	public	static	final	String	reqMajorVer10	=	"10";
	public	static	final	String	reqMajorVer11	=	"11";
	public	static	final	String	reqMinorVer	=	"0";
	
	protected	String	osType;
	protected	String	browserType;
	protected	boolean	installPageLoaded = false;
	protected	boolean	failureMessageShown = false;
	protected	int		counter = 1;
	
	protected	HorizontalPanel	basePanel	=	new	HorizontalPanel();
	protected	DmFlashMovie	playerDetectionMovie;
	protected	ModalDialog		modalDialog;
	protected	DialogBox		dialogBox;
	
//	protected	String			skipCheck = "unknown";
	
	public	FlashVersionCheck(String osType, String browserType)
	{
		this.osType = osType;
		this.browserType = browserType;
	}
	/**
	 * This method should be executed only if the check is applicable
	 * The check process itself considers the command and will return
	 * success or failure. 
	 */
	public	int		runCheck(CheckPanel checkPanel, UserCommand command)
	{
		//	Set the panel text to checking and helptext frame to neutral
		//	check explaination that contains the flash check movie. This
		//	movie page should give sufficient details on how to install
		//	or upgrade flash player. The check itself may fail and will
		//	be retried when the page is reloaded after the flash install
		//	or upgrade.
		if (command.isCheckApplicable(FlashVersionCheck.CheckIndex))
		{
			counter++;
			if (!installPageLoaded)
			{
				installPageLoaded = true;
				String	browserType = this.getBrowserType();
				if (!browserType.equalsIgnoreCase(EnvGlobals.BROWSER_FIREFOX) &&
					!browserType.equalsIgnoreCase(EnvGlobals.BROWSER_SAFARI))
				{
					int frameWidth = 10;
					int frameHeight = 10;
					
					this.basePanel.setSize(frameWidth+"px",
						/*EnvGlobals.getMinimumFrameHeight()*/frameHeight+"px");
					this.playerDetectionMovie = new DmFlashMovie("PlayerDetectionMovie",
						"playerDetectionMovie",
						frameWidth+"",
						frameHeight+"",
						"flash_version_check.swf",
						"white", checkPanel);
					
					checkPanel.setMovie(this.basePanel);
					this.basePanel.add(this.playerDetectionMovie);
					//Window.alert("showing the flash movie...");
					this.playerDetectionMovie.show();
				}
			}
			String flashPlayerFlag = this.getFlashPlayerAvailableFlag();
			if (counter == 5)
			{
				if (!browserType.equalsIgnoreCase(EnvGlobals.BROWSER_FIREFOX) &&
						!browserType.equalsIgnoreCase(EnvGlobals.BROWSER_SAFARI))
				{
			    	checkPanel.setCheckFailed(EnvGlobals.getDisplayString("flashcheckfail.message3","Flash player version 9 or higher is needed to participate in a meeting."));
				}
			}
	//		Window.alert("majorVersion"+ (reqMajorVer.equals(majorVersion)));
	//		Window.alert("minorVersion"+ (reqMinorVer.equals(minorVersion)));		
			if (flashPlayerFlag.equalsIgnoreCase("unknown"))
			{
			    	//Window.alert("returning CHECK_REPEAT");
			    	//checkPanel.setCheckFailed(EnvGlobals.getDisplayString("flashcheckfail.message",
				//	"Adobe Flash Player (version 9 or higher) is needed to participate in a meeting.
				//	<p class=\"common-text\">Click on <a href=http://www.adobe.com/go/getflash/>
				//	this link</a> to download and install."));
				if (!failureMessageShown)
				{
					EnvGlobals.setCheckExecutionInterval(500);
					failureMessageShown = true;
			    	checkPanel.setCheckFailed(EnvGlobals.getDisplayString("flashcheckfail.message","Adobe flash player version 9 or higher is needed to participate in a meeting."));
				}
				//checkPanel.setCheckFailed(getFailureMessage(FlashVersionCheck.CheckIndex,
				//		command.getCommandId(), this.osType));
				//Window.alert("returning CHECK_FAILURE");
				return	Check.CHECK_REPEAT;
			}
			else if (flashPlayerFlag.equalsIgnoreCase("skip"))
			{
				return	Check.CHECK_SUCCESS;
			}
			else
			{
				//	A valid flash version has been found.
				counter = 0;
//				Window.alert("flashPlayerFlag ="+flashPlayerFlag+"=");
//				 String [] temp = null;
//			     temp = flashPlayerFlag.split(",");
//			     String [] temp1 = null;
//			     temp1 = temp[0].split(" ");		     
			  //   int i = flashPlayerFlag.indexOf(' ');
			  //   int j = flashPlayerFlag.indexOf(',');
			  //  String majorVersion = flashPlayerFlag.substring(i, j);
//			    String majorVersion = "0";
//				String minorVersion = "0";
//				if (temp1.length > 1 && temp.length > 1)
//				{
//					majorVersion = temp1[1];
//					minorVersion = temp[1];
//				}
				//Window.alert(majorVersion);
				//Window.alert(minorVersion);
				
//				if (reqMajorVer.equals(majorVersion) && reqMinorVer.equals(minorVersion))
				if (flashPlayerFlag.indexOf(" "+reqMajorVer9+",") >= 0 ||
						flashPlayerFlag.indexOf(" "+reqMajorVer10+",") >= 0 ||
						flashPlayerFlag.indexOf(" "+reqMajorVer11+",") >= 0)
				{
					//checkPanel.setCheckSucceeded(getSuccessMessage(FlashVersionCheck.CheckIndex,
						//	command.getCommandId(), this.osType));
					checkPanel.setCheckSucceeded(getSuccessMessage(FlashVersionCheck.CheckIndex,
							command.getCommandId(), this.osType));
					//Window.alert("returning CHECK_SUCCESS");
					return	Check.CHECK_SUCCESS;
				}
				else 
				{
					if (!failureMessageShown)
					{
						EnvGlobals.setCheckExecutionInterval(500);
						failureMessageShown = true;
				    	checkPanel.setCheckFailed(EnvGlobals.getDisplayString("flashcheckfail.message1","You have Adobe Flash Player version")
				    		+" "+flashPlayerFlag+" "+ EnvGlobals.getDisplayString("flashcheckfail.message2",". version 9 or higher is needed to participate in a meeting."));
				    	
//				    	Button l = new Button("Skip Flash Version Check");
//						l.addClickListener( new ClickListener()
//						{
//							public void onClick(Widget sender)
//							{
//								Window.alert("Skipping flash check");
//								skipCheck = "skip";
//							}
//						} );
//						RootPanel.get("skip_flash_check").add(l);
					}
					//checkPanel.setCheckFailed(getFailureMessage(FlashVersionCheck.CheckIndex,
					//		command.getCommandId(), this.osType));
					//Window.alert("returning CHECK_FAILURE");
					return	Check.CHECK_REPEAT;
				}
			}
		}
		checkPanel.setCheckSucceeded(getSuccessMessage(FlashVersionCheck.CheckIndex,
				command.getCommandId(), this.osType));
		//Window.alert("returning CHECK_SUCCESS");
		return	Check.CHECK_SUCCESS;
	}
	private native String getBrowserType() /*-{
		return $wnd.browser_type;
	}-*/;
	private native String getFlashPlayerAvailableFlag() /*-{
			  	return $wnd.flash_player_available;
	}-*/;
}
