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

import com.dimdim.conference.ui.envcheck.client.EnvGlobals;
import com.dimdim.conference.ui.envcheck.client.command.BrowserTypeCheck;
import com.dimdim.conference.ui.envcheck.client.command.Check;
import com.dimdim.conference.ui.envcheck.client.command.FlashVersionCheck;
import com.dimdim.conference.ui.envcheck.client.command.OSCheck;
import com.dimdim.conference.ui.envcheck.client.command.PublisherCheck;
import com.dimdim.conference.ui.envcheck.client.command.UserCommand;
import com.dimdim.conference.ui.envcheck.client.command.UserCommandFactory;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * This class is responsible for processing each command and the associated
 * data. The processing involves:
 * 
 * 1.	Determining applicable checks for the given command.
 * 2.	Executing those checks with the given data if required. At present
 * 		none of the checkes are required to consider the data associated
 * 		with the command. However it is a good option for say a security,
 * 		authentication or resource availability check.
 */

public class CommandProcessor
{
	public	static	final	int	RESULT_SUCCESS	=	1;
	public	static	final	int	RESULT_FAILURE	=	2;
	public	static	final	int	RESULT_WAITING	=	3;
	
	protected	String		actionId;
	protected	String		browserType;
	protected	String		osType;
	protected	EnvChecksPanel	checksPanel;
	
	protected	OSCheck		osCheck;
	protected	BrowserTypeCheck	browserTypeCheck;
	protected	FlashVersionCheck	flashVersionCheck;
	protected	PublisherCheck		publisherCheck;
	
	protected	UserCommand		currentCommand;
	
	protected	Timer		checkTimer;
	protected	int		currentCheckIndex = 0;
	protected	int		numberOfPassedChecks = 0;
	protected	int		totalNumberOfChecks;
	
	protected	boolean		installReloadReported = false;
	protected	String		submitFormOnLoad;
	
//	protected	VerticalPanel	formContainer;
	
	public	CommandProcessor(String actionId, String browserType, String osType)
	{
		this.actionId = actionId;
		this.browserType = browserType;
		this.osType = osType;
		this.totalNumberOfChecks = EnvGlobals.getTotalNumberOfChecks();
		
		this.osCheck = new OSCheck(getOSType());
		this.browserTypeCheck = new BrowserTypeCheck(getBrowserType());
		this.flashVersionCheck = new FlashVersionCheck(getOSType(),getBrowserType());
		this.flashVersionCheck.setPanelWidth(300);
		this.publisherCheck = new PublisherCheck(getOSType());
		this.publisherCheck.setPanelWidth(300);
		
		this.checksPanel = new EnvChecksPanel(actionId,browserType,osType,300);
		this.currentCommand = UserCommandFactory.getFactory().getUserCommand(actionId);
		
       		this.checkTimer = new Timer()
       		{
       			public	void	run()
       			{
        			try
        			{
        			    DeferredCommand.add(new Command() {
        			        public void execute() {
        			            runCheck();
        			        }
        			      });
        			}
        			catch(Exception e)
        			{
                					
        			}
			}
        		};
		
	}
//	public	void	setFormContainer(VerticalPanel formContainer)
//	{
//		this.formContainer = formContainer;
//	}
	public	void	runCheck() 
	{
		if (numberOfPassedChecks >= this.totalNumberOfChecks)
		{
			//	The checks have already been done.
			this.allChecksSuccessful();
		}
		//	Run the current check. If it is a success, advance to the next
		//	check, if not give message and stop. If at the end of all the
		//	checks, stop and process the form.
		
		//	The result could be success, failure or waiting for user input
		//	in case one is required. Currently user input is required to
		//	accept the cab based active x installation.
		
		//Window.alert("before executing current check in runCheck method");
		int result = executeCurrentCheck();
		if (result == CommandProcessor.RESULT_WAITING)
		{
			//	Do nothing just reschedule the timer
			this.checkTimer.schedule(EnvGlobals.getCheckExecutionInterval());
		}
		else
		{
			/*if(this.currentCheckIndex == PublisherCheck.CheckIndex)
			{
				Window.alert("publisher check got execute....");
				if (result == CommandProcessor.RESULT_SUCCESS)
				{
					Window.alert("success....");
				}else{
					Window.alert("failure...");
				}
			}*/
			
			if (result == CommandProcessor.RESULT_SUCCESS)
			{
				this.numberOfPassedChecks++;
				//Window.alert("Current Check Index:"+this.currentCheckIndex);
				if (this.currentCheckIndex >= this.totalNumberOfChecks)
				{
					//	Check the number of successes and failures and accordingly
					//	give a message or advance to the form.
					if (this.numberOfPassedChecks == this.totalNumberOfChecks)
					{
						this.allChecksSuccessful();
					}
					else
					{
						//	Show a message and ask to take appropriate action.
						//	TODO
						//Window.alert("One of the checks has failed.");
					}
				}
				else
				{
					//	Reschedule the timer for the next check.
					this.checkTimer.schedule(EnvGlobals.getCheckExecutionInterval());
				}
			}
		}
	}
	public	void	allChecksSuccessful()
	{
		String url = this.getSuccessUrl();
		url = checkFireFoxEntityPattern(url);
		
		//Window.alert("All checks successful. Forwarding to :"+url.length());
		if(null != url && url.trim().length() > 0 &&  !url.equals("null"))
		{
		    forwardToSuccessUrl(url);
		}
	}
	private	int	executeCurrentCheck()
	{
		int	result = CommandProcessor.RESULT_FAILURE;
		//Window.alert("inside executeCurrentCheck finding is check applicable");
		if (this.currentCommand.isCheckApplicable(this.currentCheckIndex))
		{
			int	c;
			
			if (this.currentCheckIndex == OSCheck.CheckIndex)
			{
				//Window.alert("the os is = "+getOSType());
				c = this.osCheck.runCheck(this.checksPanel.getOsCheckPanel(),this.currentCommand);
				//Window.alert("check status = "+c);
				if (c == Check.CHECK_SUCCESS)
				{
					result = CommandProcessor.RESULT_SUCCESS;
					this.checksPanel.showOsCheckResult(true);
				}
				else
				{
					this.checksPanel.showOsCheckResult(false);
				}
				//Window.alert("os check done...");
			}
			else if (this.currentCheckIndex == BrowserTypeCheck.CheckIndex)
			{
			    //Window.alert("browser check..browser check");
			    //Window.alert("browser known is "+this.checksPanel.getBrowserTypeCheckPanel());
				c = this.browserTypeCheck.runCheck(this.checksPanel.getBrowserTypeCheckPanel(),
						this.currentCommand);
				if (c == Check.CHECK_SUCCESS)
				{
					result = CommandProcessor.RESULT_SUCCESS;
					this.checksPanel.showBrowserTypeCheckResult(true);
				}
				else
				{
					this.checksPanel.showBrowserTypeCheckResult(false);
				}
			}
			else if (this.currentCheckIndex == FlashVersionCheck.CheckIndex)
			{
				c = this.flashVersionCheck.runCheck(this.checksPanel.getFlashCheckPanel(),
						this.currentCommand);
				if (c == Check.CHECK_SUCCESS)
				{
					result = CommandProcessor.RESULT_SUCCESS;
					this.checksPanel.showFlashCheckResult(true);
				}
				else if (c == Check.CHECK_REPEAT)
				{
					result = CommandProcessor.RESULT_WAITING;
				}
				else
				{
					this.checksPanel.showFlashCheckResult(false);
				}
			}
			else if (this.currentCheckIndex == PublisherCheck.CheckIndex)
			{
				//Window.alert("inside publisher check... "+getPubEnabled());
				//if this feature is disabled in portal or the os found is mac or linux disable publisher check
				String osType = getOSType();
				if("true".equalsIgnoreCase(getPubEnabled()) && !EnvGlobals.OS_LINUX.equals(osType) && !EnvGlobals.OS_UNIX.equals(osType))
				{
					c = this.publisherCheck.runCheck(this.checksPanel.getPublisherCheckPanel(),
							this.currentCommand);
					if (c == Check.CHECK_SUCCESS)
					{
						//Window.alert("publisher result is success");
						setSuccessUrl("&pubAvailable=true");
						//Window.alert("publisher forward url = "+getSuccessUrl());
						result = CommandProcessor.RESULT_SUCCESS;
						this.checksPanel.showPublisherCheckResult(true);
					}
					else if (c == Check.CHECK_REPEAT)
					{
						//Window.alert("publisher result is repeat");
						result = CommandProcessor.RESULT_WAITING;
					}
					else
					{
						//Window.alert("publisher result is fail");
						//if(EnvGlobals.OS_MAC.equals(osType))
						//{
						//	Window.alert("On mac, just saying pub is available this is for testing has to remove..");
						//	setSuccessUrl("&pubAvailable=true");
						//}else{
							setSuccessUrl("&pubAvailable=false");
						//}
						//Window.alert("publisher forward url = "+getSuccessUrl());
						this.checksPanel.showPublisherCheckResult(false);
						result = CommandProcessor.RESULT_SUCCESS;
					}
				}
				else{
					//Window.alert("skipping pub check... os ="+getOSType());
					//publisher is disabled so not checking for it
					result = CommandProcessor.RESULT_SUCCESS;
				}
			}
			else
			{
				//	All the checks have completed. Return failure so that it doesn't
				//	mess the success count.
				result = CommandProcessor.RESULT_FAILURE;
			}
		}
		else
		{
		    //this.checksPanel.showNotApplicableMessage(currentCheckIndex);
		    result = Check.CHECK_SUCCESS;
		}
		if (result != CommandProcessor.RESULT_WAITING)
		{
			this.currentCheckIndex++;
		}
		
		//Window.alert("returng result = "+result);
		return	result;	
	}
	/**
	 * Check the form state with the command and process the new 
	 */
	public void startChecks()
	{
		//	Check if the current command requires any checks at all. The schedule
		//	meeting command does not require any checks and it will be better to
		//	avoid showing the checks panel at all.
	    //Window.alert("start checks...");
		if (this.currentCommand.anyCheckApplicable())
		{
			if (numberOfPassedChecks < this.totalNumberOfChecks)
			{
				this.currentCheckIndex = 0;
//				this.formContainer.remove(0);
				
			    DeferredCommand.add(new Command()
			    {
			        public void execute()
			        {
			        	runCheck();
			        }
			    });
			}
			else
			{
				this.allChecksSuccessful();
			}
		}
		else
		{
			this.allChecksSuccessful();
		}
	}
	protected	String	checkFireFoxEntityPattern(String s)
	{
		String s2 = s;		
		if (getBrowserType().equals("firefox") || getBrowserType().equals("safari"))
		{
			s2 = s.replaceAll("&amp;","&");
		}
		return	s2;
	}
	private native String forwardToSuccessUrl(String url) /*-{
	 return $wnd.loadURL(url);
	}-*/;
	private native String getSuccessUrl() /*-{
	 return $wnd.success_url;
	}-*/;
	private native void setSuccessUrl(String param) /*-{
	 	$wnd.success_url = $wnd.success_url+param;
	}-*/;
	private native String getBrowserType() /*-{
	 return $wnd.browser_type;
	}-*/;
	private native String getOSType() /*-{
		return	$wnd.os_type;
	}-*/;
	private native String getPubEnabled() /*-{
	 return $wnd.start_meeting_enable_publisher;
	}-*/;
	
	
}
