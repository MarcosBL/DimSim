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

package com.dimdim.conference.ui.envcheck.client;

import com.google.gwt.i18n.client.Dictionary;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */

public class EnvGlobals
{
	
	public	static	final	String	ACTION_HOST_MEETING		=	"host";
	public	static	final	String	ACTION_HOST_REJOIN		=	"hostrejoin";
	public	static	final	String	ACTION_JOIN		=	"join";
//	public	static	final	String	ACTION_START_MEETING	=	"start";
	
//	public	static	final	String	ACTION_MEET_NOW		=	"meet_now";
//	public	static	final	String	ACTION_SCHEDULE_MEETING		=	"schedule";
//	public	static	final	String	ACTION_JOIN_ATTENDEE	=	"join_attendee";
//	public	static	final	String	ACTION_JOIN_PRESENTER	=	"join_presenter";
	
	public	static	final	String	BROWSER_IE	=	"ie";
	public	static	final	String	BROWSER_FIREFOX	=	"firefox";
	public	static	final	String	BROWSER_SAFARI	=	"safari";
	public	static	final	String	BROWSER_OPERA	=	"opera";
	
	public	static	final	String	OS_WINDOWS	=	"windows";
	public	static	final	String	OS_MAC	=	"mac";
	public	static	final	String	OS_UNIX	=	"unix";
	public	static	final	String	OS_LINUX	=	"linux";
	
	protected	static	Dictionary	uiStringsDictionary;
	protected	static	Dictionary	tooltipsDictionary;
	protected	static	String		originalURL;
	
	protected	static	int		checkExecInterval = 20;
	
	public	static	Dictionary	getUIStringsDictionary()
	{
		if (EnvGlobals.uiStringsDictionary == null)
		{
			EnvGlobals.uiStringsDictionary = Dictionary.getDictionary("forms_ui_strings");
		}
		return	EnvGlobals.uiStringsDictionary;
	}
	public	static	Dictionary	getTooltipsDictionary()
	{
		if (EnvGlobals.tooltipsDictionary == null)
		{
			EnvGlobals.tooltipsDictionary = Dictionary.getDictionary("forms_tooltips");
		}
		return	EnvGlobals.tooltipsDictionary;
	}
	public static String getOriginalURL()
	{
		return originalURL;
	}
	public static void setOriginalURL(String originalURL)
	{
		EnvGlobals.originalURL = originalURL;
	}
	
	public	static	String	getActionName(String actionId)
	{
//		if (actionId.equals(EnvGlobals.ACTION_MEET_NOW))
//		{
//			return	"Start New Meeting";
//		return	EnvGlobals.getDisplayString("startnewmeeting.label","Start New Meeting");
//		}
//		else if (actionId.equals(EnvGlobals.ACTION_START_MEETING))
//		{
//			return	"Start Scheduled Meeting";
//		return	EnvGlobals.getDisplayString("startscheduledmeeting.label","Start Scheduled Meeting");
//		}
//		else if (actionId.equals(EnvGlobals.ACTION_JOIN_ATTENDEE))
//		{
//			return	"Join Meeting As Attendee";
//		return	EnvGlobals.getDisplayString("joinmeeting.label","Join Meeting As Attendee");
//		}
//		else if (actionId.equals(EnvGlobals.ACTION_JOIN_PRESENTER))
//		{
//			return	"Join Meeting As Presenter";
//		return	EnvGlobals.getDisplayString("joinmeeting1.label","Join Meeting As Presenter");
//		}
		if (actionId.equals(EnvGlobals.ACTION_HOST_MEETING))
		{
//			return	"Start Meeting";
		return	EnvGlobals.getDisplayString("startmeeting.label","Start Meeting");
		}
		else if (actionId.equals(EnvGlobals.ACTION_JOIN))
		{
//			return	"Join Meeting";
		return	EnvGlobals.getDisplayString("joinmeeting2.label","Join Meeting");
		}
//		else if (actionId.equals(EnvGlobals.ACTION_SCHEDULE_MEETING))
//		{
//			return	"Schedule Meeting";
//		return	EnvGlobals.getDisplayString("schudulemeeting.label","Schedule Meeting");
//		}
//		return	"Join Meeting As Attendee";
		return	EnvGlobals.getDisplayString("joinmeeting.label","Join Meeting As Attendee");
	}
	public	static	String	getRepeatCheckUrl(String actionId)
	{
		if (actionId.equals(EnvGlobals.ACTION_HOST_MEETING))
		{
			return	"/"+EnvGlobals.getWebappName()+"/RepeatStartNewConferenceCheck.action";
		}
		else
		{
			return	"/"+EnvGlobals.getWebappName()+"/RepeatStartNewConferenceCheck.action";
//			return	"/"+EnvGlobals.getWebappName()+"/RepeatJoinConferenceCheck.action";
		}
	}
	public	static	String	getActionUrl(String actionId)
	{
		if (actionId.equals(EnvGlobals.ACTION_HOST_MEETING))
		{
			return	"/"+EnvGlobals.getWebappName()+"/StartNewConferenceCheck.action";
		}
//		else if (actionId.equals(EnvGlobals.ACTION_START_MEETING))
//		{
//			return	"/"+EnvGlobals.getWebappName()+"/StartConferenceCheck.action";
//		}
		else if (actionId.equals(EnvGlobals.ACTION_JOIN))
		{
			return	"/"+EnvGlobals.getWebappName()+"/JoinConferenceCheck.action";
		}
//		else if (actionId.equals(EnvGlobals.ACTION_MEET_NOW))
//		{
//			return	"/"+EnvGlobals.getWebappName()+"/StartNewConferenceCheck.action";
//		}
//		else if (actionId.equals(EnvGlobals.ACTION_SCHEDULE_MEETING))
//		{
//			return	"/"+EnvGlobals.getWebappName()+"/CreateConferenceAJAX.action";
//		}
//		else if (actionId.equals(EnvGlobals.ACTION_JOIN_ATTENDEE))
//		{
//			return	"/"+EnvGlobals.getWebappName()+"/JoinConferenceCheck.action";
//		}
//		else if (actionId.equals(EnvGlobals.ACTION_JOIN_PRESENTER))
//		{
//			return	"/"+EnvGlobals.getWebappName()+"/JoinConferenceCheck.action";
//		}
		return	"";
	}
	/**
	 * For most part all the fields are called the same thing on same form.
	 * Action id is passed to this method just in case some form wants to
	 * differ.
	 * 
	 * @param actionId
	 * @param fieldId
	 * @return
	 */
	public	static	int	getTotalNumberOfChecks()
	{
		return	4;
	}
	public	static	String	getOSName(String osId)
	{
		if (osId.equals(EnvGlobals.OS_LINUX))
		{
//			return	"Linux";
			return	EnvGlobals.getDisplayString("linux.label","Linux");
		}
		else if (osId.equals(EnvGlobals.OS_MAC))
		{
//			return	"Macintosh";
			return	EnvGlobals.getDisplayString("macintosh.label","Macintosh");
		}
		else if (osId.equals(EnvGlobals.OS_UNIX))
		{
//			return	"Unix";
			return	EnvGlobals.getDisplayString("unix.label","Unix");
		}
		else if (osId.equals(EnvGlobals.OS_WINDOWS))
		{
//			return	"Windows 2000/XP/2003";
			return	EnvGlobals.getDisplayString("windows.label","Windows 2000/XP/2003");
		}
//		return	"Unknown";
		return	EnvGlobals.getDisplayString("unknown.label","Unknown");
	}
	public	static	String	getBrowserName(String browserId)
	{
		if (browserId.equalsIgnoreCase(EnvGlobals.BROWSER_FIREFOX))
		{
//			return	"Mozilla firefox 1.5";
		    return EnvGlobals.getDisplayString("ff15.label","Mozilla firefox ");
		}
		else if (browserId.equalsIgnoreCase(EnvGlobals.BROWSER_IE))
		{
//			return	"Internet Explorer 6";
		    return EnvGlobals.getDisplayString("ie6.label","Internet Explorer ");
		}
		else if(browserId.equalsIgnoreCase(EnvGlobals.BROWSER_SAFARI))
		{
		    return EnvGlobals.getDisplayString("safari.label","Safari");
		}
		else if(browserId.equalsIgnoreCase(EnvGlobals.BROWSER_OPERA))
		{
		    return EnvGlobals.getDisplayString("opera.label","Opera");
		}
//		return	"Unknown";
		return EnvGlobals.getDisplayString("unknown.label","Unknown");
	}
	public	static	String	getActionComment()
	{
//		return	"Checking environment for: ";
		return EnvGlobals.getDisplayString("checkenv.label","Checking environment for");
	}
	public	static	String	getSkipChecksComment()
	{
//		return	"Skip checks";
		return EnvGlobals.getDisplayString("skipcheckenv.label","Skip checks");
	}
	public	static	String	getChangeLocaleComment()
	{
//		return	"Change Locale: ";
		return EnvGlobals.getDisplayString("changelocale.label","Change Locale@:");
	}
	public	static	int	getMaximumListOptionValueLength()
	{
		return	20;
	}
	/*
	public	static	int	getMinimumPageWidth()
	{
		return	700;
	}
	public	static	int	getMinimumPageHeight()
	{
		return	500;
	}
	public	static	int	getMinimumRHPWidth()
	{
		return	250;
	}
	public	static	int	getMaximumRHPWidth()
	{
		return	500;
	}
	public	static	int	getMinimumFormWidth()
	{
		return	380;
	}
	public	static	int	getMaximumFormWidth()
	{
		return	600;
	}
	public	static	int	getMinimumFrameWidth()
	{
		return	400;
	}
	public	static	int	getMaximumFrameWidth()
	{
		return	500;
	}
	public	static	int	getMinimumFrameHeight()
	{
		return	400;
	}
	*/
	public	static	int	getCheckExecutionInterval()
	{
		return	checkExecInterval;
	}
	public	static	void	setCheckExecutionInterval(int i)
	{
		checkExecInterval = i;
	}
	/**
	 * These two percentages do not add upto 100 because some clearance is required
	 * for the float styles.
	 * @return
	 */
	public	static	int	getFormFieldLabelWidthPercentage()
	{
		return	30;
	}
	public	static	int	getFormFieldValueWidthPercentage()
	{
		return	63;
	}
//	public	static	int	getMinimumDownloadBandwidth()
//	{
//		return	100;
//	}
	public	static	String	getEmptyCheckboxUrl()
	{
		return	"images/checkbox-off.png";
	}
	public	static	String	getSuccessCheckboxUrl()
	{
		return	"images/checkbox-on.png";
	}
	public	static	String	getFailureCheckboxUrl()
	{
		return	"images/checkbox-off.png";
	}
	public	static	String	getPlaceholderImageUrl()
	{
		return	"images/placeholder.gif";
	}
	public	static	String	getCheckDescriptionText(int checkIndex)
	{
		switch(checkIndex)
		{
			case 0:	return	EnvGlobals.getDisplayString("oscheck.label","Operating System Check"); //"Operating System Check";
			case 1:	return	EnvGlobals.getDisplayString("browsercheck.label","Browser Support Check"); //"Browser Support Check";
			case 2:	return	EnvGlobals.getDisplayString("flashcheck.label","Flash Player Version Check"); //"Flash Player Version Check";
			case 3:	return	EnvGlobals.getDisplayString("bwcheck.label","Bandwidth Check"); // "Bandwidth Check";
			case 4:	return	EnvGlobals.getDisplayString("pubcheck.label","Publisher Availability Check"); //"Publisher Availability Check";
		}
//		return	"Publisher Availability Check";
		return EnvGlobals.getDisplayString("pubcheck1.label","Publisher Availability Check");
	}
	public	static	String	getCheckInProgressText(int checkIndex, String actionId, String osType,
			String requiredPublisherVersion, String availablePublisherVersion,
			String publisherAvailability, int int2)
	{
		switch(checkIndex)
		{
		case 0:	return	EnvGlobals.getDisplayString("oscheckprogess.label","Operating System Check in progress…");
		case 1:	return	EnvGlobals.getDisplayString("browsercheckprogess.label","Browser Support Check in progress…");
		case 2:	return	EnvGlobals.getDisplayString("flashcheckprogess.label","Flash Player Version Check in progress ..");
		case 3:	return	EnvGlobals.getDisplayString("browsercheckprogess.label","Measing download and upload bandwidths ..");
		case 4:	return	EnvGlobals.getDisplayString("pubcheckprogess.label","Publisher Availability Check in progress..");
		}
		return EnvGlobals.getDisplayString("checkdimdimpub.label","Checking web meeting publisher");
	}
	
	public static String getCheckFFAllowSiteText()
	{
		return EnvGlobals.getDisplayString("pubffallowtext.label","If you are not able to proceed further please click here");
	}
	
	public static String getCheckIEAllowSiteText()
	{
		return EnvGlobals.getDisplayString("pubieallowtext.label","If you are not able to proceed further please refresh this page");
	}
	
//panel.setCheckSucceeded(EnvGlobals.getCheckSucceededText(PublisherCheck.CheckIndex,command.getCommandId(), 
//	this.osType, "2.0.3.6", 0, 0));	
	
	public	static	String	getCheckSucceededText(int checkIndex, String actionId,
				String str1, String str2, int int1, int int2)
	{
		String actionName = EnvGlobals.getActionName(actionId);
		String osName = "unknown";
		if (str1 != null)
		{
			osName = EnvGlobals.getOSName(str1);
		}
		String browserName = "unknown";
		//Window.alert("string1 = "+str1);
		if (str1 != null)
		{
			browserName = EnvGlobals.getBrowserName(str1);
		}
		//Window.alert("browserName = "+browserName);
		switch(checkIndex)
		{
			case 0:
				if (actionId.equals(EnvGlobals.ACTION_JOIN))
				{
//					return	"You are running "+osName+". You can participate in dimdim meeting.";
					return EnvGlobals.getDisplayString("oscheck1.message","You are running ") + osName + EnvGlobals.getDisplayString("oscheck2.message",".You can participate in web meeting.");
				}
				else
				{
//					return	"A new Meeting can be started on Windows 2000/XP/2003. Your machine satisfies this requirement.";
					return EnvGlobals.getDisplayString("meetingoscheck.message","A new Meeting can be started on Windows XP/Vista/2003/2000, Mac and Linux. Your machine satisfies this requirement.");

				}
			case 1:				
				if (actionId.equals(EnvGlobals.ACTION_JOIN))
				{
//					return	"You can participant in dimdim meeting using "+browserName+" browser.";
					return EnvGlobals.getDisplayString("browsercheck1.message","You can participant in web meeting using ") + browserName + " "+ getBrowserVersion();
				}
//				else if(actionId.equals(EnvGlobals.ACTION_START_MEETING) || actionId.equals(EnvGlobals.ACTION_HOST_MEETING) || actionId.equals(EnvGlobals.ACTION_MEET_NOW))
				else if(actionId.equals(EnvGlobals.ACTION_HOST_MEETING))
				{
//					return	"A new Meeting can be started using internet Explorer 6/7 or FireFox 1.5. Your machine satisfies this requirement.";					
					return EnvGlobals.getDisplayString("meetingpresenterbrowsercheck.message","You have browser ")+ browserName + " "+ getBrowserVersion();
				}
				else
				{
//					return	"A new Meeting can be started using internet Explorer 6/7 or FireFox 1.5. Your machine satisfies this requirement.";					
					return EnvGlobals.getDisplayString("meetingbrowsercheck.message","The version of the browser you are using is not supported. Please upgrade to the latest browser. Dimdim supports Firefox 1.5 onwards for Attendees on all platforms, IE 6 onwards for attendees on Windows and Safari 2 onwards for attendees on MAC.");
				}
			case 2:	return EnvGlobals.getDisplayString("meetingflashcheck.message","Adobe Flash Player (version 9 or higher) is needed to participate in a meeting. Your machine satisfies this requirement."); //return	"Adobe Flash Player (version 8 or higher) is needed to participate in a meeting. Your machine satisfies this requirement.";
			
			case 3:
				if (actionId.equals(EnvGlobals.ACTION_JOIN))
				{
//					return	"You have Dimdim Web Meeting Publisher version "+str2+" on this computer. You can "+actionName;

					return EnvGlobals.getDisplayString("pubversioncheck1.message","You have Web Meeting Publisher version ") + str2 + 
					EnvGlobals.getDisplayString("pubversioncheck2.message"," on this computer. You can ") +actionName;
				}
				else
				{
//					return	"Dimdim Publisher version "+str2+" is required to be installed to start a meeting. Your machine satisfies this requirement.";
					return EnvGlobals.getDisplayString("pubchecksucceed1.message","Web Meeting Publisher version ") + str2 +" ";

				}
				
			case 4:
				String attendee1 = EnvGlobals.getDisplayString("attendeebwcheck.message1","");
				String presenter1 = EnvGlobals.getDisplayString("presenterbwcheck.message1","");
				String msg2 = EnvGlobals.getDisplayString("bwcheck.message2","");
//				String failmsg3 = EnvGlobals.getDisplayString("bwcheckfail.message3","");
				String successmsg3 = EnvGlobals.getDisplayString("bwchecksuccess.message3","");
				String requiredBWs = str1;
				String availableBWs = "("+int1+"/"+int2+")";
				if (actionId.equals(EnvGlobals.ACTION_JOIN))
				{
					return attendee1+requiredBWs+msg2+availableBWs+successmsg3; 
				}
				else
				{
					return presenter1+requiredBWs+msg2+availableBWs+successmsg3; 
				}
			
		}
//		return	"Dimdim publisher is available";
		return EnvGlobals.getDisplayString("dimdimpubavailable.message","Web Meeting publisher is available");
	}
	public	static	String	getCheckFailedText(int checkIndex, String actionId, String osType, String browserType,
				String publisherVersion, int downloadBandwidth,int uploadBandwidth)
	{
		String osName = "unknown";
		if (osType != null)
		{
			osName = EnvGlobals.getOSName(osType);
		}
		String actionName = EnvGlobals.getActionName(actionId);
		String browserName = "unknown";
		if (browserType != null)
		{
			browserName = EnvGlobals.getBrowserName(browserType);
		}
		switch(checkIndex)
		{
			case 0:
				if (actionId.equals(EnvGlobals.ACTION_JOIN))
				{
//					return	"You can join a meeting on Windows 2000/XP/2003/Linux/Mac. Your machine does not satisfy this requirement.";

					return EnvGlobals.getDisplayString("attendeeoscheckfail.message","You can join a meeting on Windows XP/Vista/2003/2000, Mac and Linux. Your machine does not satisfy this requirement.");
				}
				else
				{
//					return	"A new Meeting can be started on Windows 2000/XP/2003. Your machine does not satisfy this requirement.";

					return EnvGlobals.getDisplayString("presenteroscheckfail.message","A new Meeting can be started on Windows XP/Vista/2003/2000, Mac and Linux. Your machine does not satisfy this requirement.");
				}
			case 1:
				if (actionId.equals(EnvGlobals.ACTION_JOIN))
				{
//					return	"You can participant in a meeting using internet Explorer 6/7 or FireFox 1.5. Your machine does not satisfy this requirement.";
					return EnvGlobals.getDisplayString("attendeebrowsercheckfail.message","You can participant in a meeting using internet Explorer 6/7 or FireFox 1.5. Your machine does not satisfy this requirement.");

				}
				else
				{
//					return	"A new Meeting can be started using internet Explorer 6/7 or FireFox 1.5. Your machine does not satisfy this requirement.";
					return EnvGlobals.getDisplayString("presenterbrowsercheckfail.message","A new Meeting can be started using internet Explorer 6/7 or FireFox 1.5. Your machine does not satisfy this requirement.");
				}
			case 2:	return EnvGlobals.getDisplayString("flashcheckfail.message","Adobe Flash Player (version 9 or higher) is needed to participate in a meeting. Your machine satisfies this requirement.");	//return	"Adobe Flash Player (version 8 or higher) is needed to participate in a meeting. Your machine satisfies this requirement.";
			case 3:
				String attendee1 = EnvGlobals.getDisplayString("attendeebwcheck.message1","");
				String presenter1 = EnvGlobals.getDisplayString("presenterbwcheck.message1","");
				String msg2 = EnvGlobals.getDisplayString("bwcheck.message2","");
				String failmsg3 = EnvGlobals.getDisplayString("bwcheckfail.message3","");
//				String successmsg3 = EnvGlobals.getDisplayString("bwchecksuccess.message3","");
				String requiredBWs = osType;
				String availableBWs = "("+downloadBandwidth+"/"+uploadBandwidth+")";
				if (actionId.equals(EnvGlobals.ACTION_JOIN))
				{
					return attendee1+requiredBWs+msg2+availableBWs+failmsg3; 
				}
				else
				{
					return presenter1+requiredBWs+msg2+availableBWs+failmsg3; 
				}
			case 4:
				if (actionId.equals(EnvGlobals.ACTION_JOIN))
				{
//					return	"You have Dimdim Web Meeting Publisher version "+publisherVersion+" on this computer. You can "+actionName;
					return EnvGlobals.getDisplayString("pubversioncheck1.message","You have Dimdim Web Meeting Publisher version ") + publisherVersion + 
						EnvGlobals.getDisplayString("pubversioncheck2.message"," on this computer. You can ") +actionName;

				}
				else
				{
//					return	"Dimdim Publisher version "+publisherVersion+
//												" is required to be installed to start a meeting. Your machine does not satisfy this requirement.";
					return "Publisher installation on your machine has failed. This could have happened if you clicked refresh during the installation, Please close all browsers and start the meeting again.";
					/*return EnvGlobals.getDisplayString("pubcheckfail1.message","Dimdim Publisher version ") + publisherVersion +
					EnvGlobals.getDisplayString("pubcheckfail1.message"," is required to be installed to start a meeting. Your machine does not satisfy this requirement.");
*/					

				}
		}
//		return	"Not Applicable";
		return EnvGlobals.getDisplayString("checknotavailable.message","Not Applicable");
	}
	public	static	String	getPublisherInstallRequiredComment(String requiredVersion)
	{
//		return	"Dimdim web conference publisher version "+requiredVersion+
//			" is required. Starting the installation process. This might take a few minutes";
		return EnvGlobals.getDisplayString("pubversionverifyfail1.message","Dimdim web meeting publisher version ") + requiredVersion + " "+
			EnvGlobals.getDisplayString("pubversionverifyfail2.message"," is required. Starting the installation process. This might take a few minutes.");

	}
	public	static	String	getPublisherUpgradeRequiredComment(String requiredVersion, String availableVersion)
	{
//		return	"Dimdim web conference publisher version "+requiredVersion+
//			" is required. This computer has version "+availableVersion+
//			". Starting the installation process. This might take a few minutes";

		return EnvGlobals.getDisplayString("pubversionupgrade1.message","Dimdim web conference publisher version ") + requiredVersion + 
			EnvGlobals.getDisplayString("pubversionupgrade2.message","  is required. This computer has version ") + availableVersion +
			EnvGlobals.getDisplayString("pubversionupgrade3	.message",". Starting the installation process. This might take a few minutes");
	}
	public	static	String	getFirefoxAcceptComment()
	{
//		return	"Please accept the firefox extension installation by clicking on 'Install Now'.";

		return EnvGlobals.getDisplayString("ffinstall1.message","Please accept the firefox extension installation by clicking on 'Install Now'.");
	}
	public	static	String	getFirefoxRestartComment()
	{
//		return	"After the plugin installation is completed please restart all instances of firefox browsers and reconnect to dimdim";
		return EnvGlobals.getDisplayString("ffinstall2.message","After the plugin installation is completed please restart all instances of firefox browsers and reconnect to dimdim");
	}
	public	static	String	getFirefoxInstallerPopupHeading()
	{
//		return	"Dimdim Web Conference Publisher Installer";
		return EnvGlobals.getDisplayString("pubinstall.message","Web Meeting Publisher Installer");

	}
//	public	static	String	getLHPFrameUrl(String actionId)
//	{
//		Window.alert(actionId);
//		if (actionId == null || actionId.equals(EnvGlobals.ACTION_JOIN))
//		{
//			return	"join_meeting.html";
//		}
//		return	"start_meeting.html";
//	}
//	public	static	String	getHelptextUrl(int checkIndex)
//	{
//		switch(checkIndex)
//		{
//			case 0:	return	"OSCheckHelpText.html";
//			case 1:	return	"BrowserCheckHelpText.html";
//			case 2:	return	"FlashCheckHelpText.html";
//			case 3:	return	"BandwidthTest.html";
//			case 4:	return	"PublisherCheckHelpText.html";
//		}
//		return	"PublisherCheckHelpText.html";
//	}
	public	static	String	getTooltip(String key)
	{
		String tooltip = "";
		if (EnvGlobals.getTooltipsDictionary() != null)
		{
			try
			{
				String s = EnvGlobals.getTooltipsDictionary().get(convertToJsIdString(key));
				if (s != null)
				{
					tooltip = s;
				}
			}
			catch(Exception e)
			{
				//Window.alert(e.getMessage());
			}
		}
		return	tooltip;
	}
	public	static	String	getDisplayString(String key, String defaultValue)
	{
		String ds = defaultValue;
		if (EnvGlobals.getUIStringsDictionary() != null)
		{
			try
			{
				String s = EnvGlobals.getUIStringsDictionary().get(convertToJsIdString(key));
				if (s != null)
				{
					ds = s;
				}
			}
			catch(Exception e)
			{
				//Window.alert(e.getMessage());
			}
		}
		return	ds;
	}
	protected	static	String	convertToJsIdString(String str)
	{
		String s1 = str;
		int dot = s1.indexOf(".");
		while (dot > 0)
		{
			s1 = s1.substring(0,dot)+"$"+s1.substring(dot+1);
			dot = s1.indexOf(".");
		}
		return	s1;
	}
	public	static	boolean	isPublisherSupportable()
	{
		if (!EnvGlobals.isOsWindows() && !EnvGlobals.isOsMac())
		{
			//	Publisher is not supported on non windows operating systems.
			return	false;
		}
		else
		{
			//	On windows it is only supported on ie and firefox.
			return	(isBrowserIE() || isBrowserFirefox() || isBrowserSafari());
		}
	}
	public	static	boolean	isBrowserIE()
	{
		return	EnvGlobals.getBrowserType().equals("ie");
	}
	public	static	boolean	isBrowserFirefox()
	{
		return	EnvGlobals.getBrowserType().equals("firefox");
	}
	public	static	boolean	isBrowserSafari()
	{
		return	EnvGlobals.getBrowserType().equals("safari");
	}
	public	static	boolean	isOsMac()
	{
		return	EnvGlobals.getOsType().equals("mac");
	}
	public	static	boolean	isOsLinux()
	{
		return	EnvGlobals.getOsType().equals("linux");
	}
	public	static	boolean	isOsWindows()
	{
		return	EnvGlobals.getOsType().equals("windows");
	}
	public	static native String getWebappName() /*-{
		return ($wnd.webapp_name);
	}-*/;
	private static native String getBrowserType() /*-{
	  	return $wnd.browser_type;
	}-*/;
	private static native String getBrowserVersion() /*-{
  		return $wnd.browserVersion;
	}-*/;
	private static native String getOsType() /*-{
	  	return $wnd.os_type;
	}-*/;
}
