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

package com.dimdim.conference;

import	java.io.File;
import	java.io.FileInputStream;
//import	java.io.BufferedInputStream;
import java.io.RandomAccessFile;
//import java.text.DateFormat;
import java.util.ResourceBundle;
import java.util.PropertyResourceBundle;

import com.dimdim.util.misc.FileUtil;
//import com.dimdim.util.misc.TimeZoneOption;
import com.dimdim.util.misc.TimeZonesList;
import com.dimdim.util.misc.EmoticonsList;
import com.dimdim.util.misc.LocaleList;

import com.dimdim.locale.LocaleResourceFile;
import com.dimdim.streaming.StreamingServerAdapterProvider;
import java.util.HashMap;

import java.util.Random;

//import javax.servlet.http.HttpServletRequest;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */
public class ConferenceConsoleConstants
{
	public	static	String	lineSeparator = "\n";
	
	
	static
	{
		try
		{
			ConferenceConsoleConstants.lineSeparator = (String)System.getProperty("line.separator");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		if (ConferenceConsoleConstants.lineSeparator == null)
		{
			ConferenceConsoleConstants.lineSeparator = "\n";
		}
	}
	
	public static final String USER_LOCALE = "SESSION_LOCALE";
	public static final String BROWSER_TYPE = "BROWSER_TYPE";
	public static final String BROWSER_VERSION = "BROWSER_VERSION";
	public static final String BROWSER_TYPE_UNKNOWN = "unknown";
	public static final String BROWSER_TYPE_IE = "ie";
	public static final String BROWSER_TYPE_FIREFOX = "firefox";
	public static final String BROWSER_TYPE_SAFARI = "safari";
	public static final String BROWSER_TYPE_OPERA = "opera";
	
	public static final String BROWSER_VERSION_ALL = "all";
	public static final String BROWSER_VERSION_IE_6_OR_LOWER = "6";
	public static final String BROWSER_VERSION_IE_7 = "7";
	public static final String BROWSER_VERSION_FIREFOX_15_OR_LOWER = "15";
	public static final String BROWSER_VERSION_FIREFOX_2 = "2";
	public static final String BROWSER_VERSION_FIREFOX_3 = "3";
	public static final String BROWSER_VERSION_SAFARI_3 = "3";
	public static final String BROWSER_VERSION_SAFARI_2 = "2";
	public static final String BROWSER_VERSION_UNKOWN="unknown";
	
//	public static final String ACTIVE_USER_SESSION = "ACTIVE_USER_SESSION";
	public static final String ACTIVE_ADMIN_SESSION = "ACTIVE_ADMIN_SESSION";
	public static final String PRESENTATION_SESSION = "PRESENTATION_SESSION";
	public static final String STREAMING_CONTROL_SESSION = "STREAMING_CONTROL_SESSION";
	
	public static final String INVITATION_EMAIL_TEMPLATE = "InvitationTemplate.html";
	
	public static final	String	CHECK_EMAIL = "CHECK_EMAIL";
	public static final String	CHECK_KEY = "CHECK_KEY";
	public static  String	securityKey = "skey";
	public static final String	NO_CHECK = "NO_CHECK";
	
	public static	String	InternationalDialin = "";
	public static   String	InternationalDialinTollFree = "";
	public static   String  LocalDialin = "";
	public static   String	LocalDialinTollFree = "";
	
	//	Constants for communicating errors and messages between components
	//	and to user.
	
	public static final String RETURN_SUCCESS = "success";
	public static final String RETURN_ERROR = "error";
	
	public static final String PRESENTATION_APPLICATION = "PRESENTATION_APPLICATION";
	
	protected	static	String	webappLocalPath = null;
	protected	static	String	webinfDir = null;
	protected	static	String	resourcesDirectory = null;
	protected	static	String	presentationStorageRoot = null;
	protected	static	String	serverIP = null;
	protected	static	String	serverPortNumber = null;
//	protected	static	String	reflectorIP = null;
//	protected	static	String	reflectorPortNumber = null;
	protected	static	String	serverSecurePortNumber = null;
	protected	static	String	serverAddress = null;
	protected	static	String	dMServerAddress = null;
	protected	static	String	dMServerInternalAddress = null;
	protected	static	String	dMServerMboxInternalAddress = null;
	protected	static	String	dMServerMboxExternalAddress = null;
	protected	static	String	serverSecureAddress = null;
	protected	static	String	webappName = null;
	protected	static	String	defaultWebappName = null;
	protected	static	String	invitationEmailTemplateBuffer = null;
//	protected	static	int		maxConcurrentConferences = 50;
//	protected	static	int		maxTotalParticipants = 1000;
	protected	static	int		maxParticipantsPerConference = 50;
	protected	static	int		minParticipantsPerConference = 20;
	protected	static	int		maxMeetingLengthMinutes = 300;
	protected	static	int		maxObjectsInOneEvent = 10;
	protected	static	int		maxAttendeeAudios = 0;
	protected	static	int		maxAttendeeVideos = 0;
	protected	static	boolean	assignMikeOnJoin = false;
//	protected	static	int		presenterDownloadBandwidthRequired = 100;
//	protected	static	int		attendeeDownloadBandwidthRequired = 100;
//	protected	static	int		presenterUploadBandwidthRequired = 100;
//	protected	static	int		attendeeUploadBandwidthRequired = 30;
	protected	static	String	trackbackURL = "/";
	protected	static	String	overrideMaxParticipants = "true";
	protected	static	String	showInviteLinks = "true";
	protected	static	String	videoChatSupported = "false";
	protected	static	String	largeVideoSupported = "false";
	
	protected	static	String	dimdimAdminsListFile = "dimdimAdmins.txt";
	protected	static	String	presenterEmailsListFile = "dimdimPresenters.txt";
	protected	static	String	conferenceSpecsFile = "dimdimConferenceSpecs.dmc";
	
	protected	static	int		presenterSessionTimeout = 10;
	protected	static	int		attendeeSessionTimeout = 10;
	
	protected	static	String	productName = "Dimdim Web Meeting";
//	protected	static	String	productVersion = "1.6.0 Alpha";
	protected	static	String	productVersion = "3.0.0 Beta";
	protected	static	String	productWebSite = "www.dimdim.com";
	
	protected	static	String	releaseDate	= "";
	protected	static	String	majorVersion = "Alpha_1.6.0";
	protected	static	String	versionNumber = "";
	
	protected	static	String	authenticationPolicy = CHECK_EMAIL;
	
	protected	static	String	premiumCatalogue = "";
	protected	static 	String 	userTypeFreeOrPaid = "1";
	protected	static	String	installationId = "_default";
	protected	static	String	installationPrefix = "____";
	
//	protected	static	String	sfxName = "DimdimSetupCreator.exe";
//	protected	static	String	baseNsisExeName = "dimdim-setup.exe";
//	protected	static	String	downloadExeName = "dimdim-publisher-setup.exe";
//	protected	static	String	downloadFirefoxExeName = "dimdim-publisher-setup-firefox.exe";
//	protected	static	String	sfxPathFull = null;
//	protected	static	String	baseNsisExePathFull = null;
//	protected	static	String	downloadExePathFull = null;
//	protected	static	String	downloadFirefoxExePathFull = null;
	
//	protected	static	String	timeZonesListFilePathFull = null;
//	protected	static	TimeZonesList	timeZonesList = null;
	
	protected	static	String	localeListFilePathFull = null;
	protected	static	LocaleList localeList = null;
	
	protected	static	String	propertiesFilePath = null;
	protected	static	String	propertiesDialinFilePath = null;
	protected	static	long	propertiesFileUpdateTime = 0;
	
	protected	static	ResourceBundle	dimdimConferenceProperties;
	protected	static	ResourceBundle	dialinConferenceProperties;
	
	//please make sure that the values here are exactly same as in EnvGlobals in console
	public	static	final	String	OS_TYPE	=	"OS_TYPE";
	public	static	final	String	OS_WINDOWS	=	"windows";
	public	static	final	String	OS_MAC	=	"mac";
	public	static	final	String	OS_UNIX	=	"unix";
	public	static	final	String	OS_LINUX	=	"linux";
	public	static	final	String	OS_UNKNOWN	=	"unknown";
	
//	protected	static	String	avRtmpURL;
//	protected	static	String	shareRtmpURL;
	
//	protected	static	int		maxAudioBroadcasters = 3;
	
	// ADDED FOR EMOTICONS PROPERTIES
	
	protected	static	String	emoticonsListFullPath = null;
	protected	static	EmoticonsList	emoticonsList = null;
	protected	static	String	showRecordLinks = "true";
	protected	static	String	showFullScreenLink = "true";
	//protected	static	String	emoticonsFilePath = null;
	//protected  static  HashMap emoticonsMap = new HashMap();
	
	// ******************************
	
	protected static String copyrightLink = "http://www.dimdim.com/ppolicy.html";
	protected static String trademarkLink = "http://www.dimdim.com/tm.html";
	private static String defaultUrl = "";
	
	public static String getWebappLocalPath()
	{
		return webappLocalPath;
	}
	/**
	 * IMPORTANT: INITIAL WEBAPP SETUP. The initial setup needs to know the
	 * local path. Hence this is called from the first servlet access.
	 * @param webappLocalPath
	 */
	public static void setWebappLocalPath(String webappLocalPath)
	{
		/*
		if (ConferenceConsoleConstants.webappLocalPath == null)
		{
			ConferenceConsoleConstants.webappLocalPath = webappLocalPath;
			try
			{
				ConferenceConsoleConstants.doInit();
			}
			catch(Exception e2)
			{
				e2.printStackTrace();
				try
				{
					Thread.sleep(200);
				}
				catch(InterruptedException ie)
				{
				}
				try
				{
					ConferenceConsoleConstants.doInit();
				}
				catch(Exception e3)
				{
					e3.printStackTrace();
				}
			}
		}
		*/
	}
	public	static	void	initConstants()
	{
		if (ConferenceConsoleConstants.webappLocalPath == null)
		{
//			ConferenceConsoleConstants.readConstants();
		}
	}
	public	static	void	initConstants(String localPath)
	{
//		if (ConferenceConsoleConstants.webappLocalPath == null)
//		{
			ConferenceConsoleConstants.webappLocalPath = localPath;
//			if (ConferenceConsoleConstants.propertiesFilePath == null)
//			{
//				ConferenceConsoleConstants.timeZonesListFilePathFull =
//						(new File( ConferenceConsoleConstants.webappLocalPath, "TimeZonesList.csv")).getAbsolutePath();
//				ConferenceConsoleConstants.timeZonesList =
//						new TimeZonesList(ConferenceConsoleConstants.timeZonesListFilePathFull);
				
				webinfDir  = (new File( ConferenceConsoleConstants.webappLocalPath, "WEB-INF")).getAbsolutePath();
				String classesDir  = (new File( webinfDir, "classes")).getAbsolutePath();
				ConferenceConsoleConstants.resourcesDirectory  = (new File( classesDir, "resources")).getAbsolutePath();
				ConferenceConsoleConstants.propertiesDialinFilePath = 
						(new File(ConferenceConsoleConstants.resourcesDirectory, "dialin.properties")).getAbsolutePath();
//				String confDir  = (new File( resourcesDir, "conf")).getAbsolutePath();
				ConferenceConsoleConstants.propertiesFilePath  =
						(new File( ConferenceConsoleConstants.resourcesDirectory, "dimdim.properties")).getAbsolutePath();
				
				ConferenceConsoleConstants.emoticonsListFullPath =
					(new File( ConferenceConsoleConstants.webappLocalPath, "EmoticonsList.csv")).getAbsolutePath();
				ConferenceConsoleConstants.emoticonsList =
					new EmoticonsList(ConferenceConsoleConstants.emoticonsListFullPath);
//				ConferenceConsoleConstants.emoticonsList.getEmoticonsList();
				
				ConferenceConsoleConstants.localeListFilePathFull =
					(new File( ConferenceConsoleConstants.webappLocalPath, "LocaleEncoding.csv")).getAbsolutePath();
				ConferenceConsoleConstants.localeList =
					new LocaleList(ConferenceConsoleConstants.localeListFilePathFull);
				ConferenceConsoleConstants.localeList.getLocaleEncoding("");
				
				//ConferenceConsoleConstants.emoticonsFilePath  =
					//(new File( ConferenceConsoleConstants.resourcesDirectory, "emoticons.properties")).getAbsolutePath();
//			}
//			ConferenceConsoleConstants.readConstants();
			ConferenceConsoleConstants.reInit();
			
			// ADDED FOR EMOTICONS PROPERTIES
			
			//ConferenceConsoleConstants.loadEmoticons();
			
			// ******************************
//		}
	}
	

	public static void reInit()
	{
//		boolean	updated = false;
//		System.out.println("Checking for properties file updates");
//		System.out.println("  properties file last read on:"+ConferenceConsoleConstants.propertiesFileUpdateTime);
		File propsFile = new File(ConferenceConsoleConstants.propertiesFilePath);
		File propsDialinFile = new File(ConferenceConsoleConstants.propertiesDialinFilePath);
		
		long currentUpdateTime = propsFile.lastModified();
		long currentDialinUpdateTime = propsFile.lastModified();
		
//		System.out.println("  properties file last updated on:"+currentUpdateTime);
		if (currentUpdateTime > ConferenceConsoleConstants.propertiesFileUpdateTime ||
				ConferenceConsoleConstants.propertiesFileUpdateTime == 0 || 
				currentDialinUpdateTime > ConferenceConsoleConstants.propertiesFileUpdateTime 
				|| ConferenceConsoleConstants.propertiesFileUpdateTime == 0)
		{
			System.out.println("******************** Refreshing properties **************");
			try
			{
				FileInputStream fis = new FileInputStream(propsFile);
				PropertyResourceBundle prb = new PropertyResourceBundle(fis);
				ConferenceConsoleConstants.readProperties(prb);
//				ConferenceConsoleConstants.readOptionalProperties(prb);
				
				ConferenceConsoleConstants.dimdimConferenceProperties = prb;
				
				
				FileInputStream fdialin = new FileInputStream(propsDialinFile);
				PropertyResourceBundle prbDialin = new PropertyResourceBundle(fdialin);
				ConferenceConsoleConstants.readDialinProperties(prbDialin);

				ConferenceConsoleConstants.dialinConferenceProperties = prbDialin;
				
				StreamingServerAdapterProvider.getAdapterProvider().initializeProvider(ConferenceConsoleConstants.webappLocalPath,prb);
				try
				{
					fis.close();
				}
				catch(Exception e)
				{
				}
				
				//createPublisherExe();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}
//	protected	synchronized	static	void	readOptionalProperties(ResourceBundle conferenceProps)
//	{
//		try
//		{
//			String s = conferenceProps.getString("maxAudioBroadcasters");
//			ConferenceConsoleConstants.maxAudioBroadcasters = Integer.parseInt(s);
//		}
//		catch(Exception e)
//		{
//			ConferenceConsoleConstants.maxAudioBroadcasters = 3;
//		}
//	}
	
	protected	synchronized	static	void	readDialinProperties(ResourceBundle dialinProps){
		String s = "";
		try{
			s = dialinProps.getString("InternationalDialin");
			ConferenceConsoleConstants.InternationalDialin = s;
		}
		catch (Exception e){
			ConferenceConsoleConstants.InternationalDialin = "";
		}
		try{
			s = dialinProps.getString("InternationalDialinTollFree");
			ConferenceConsoleConstants.InternationalDialinTollFree = s;
		}
		catch (Exception e){
			ConferenceConsoleConstants.InternationalDialinTollFree = "";
		}
		
		try{
			s = dialinProps.getString("LocalDialin");
			ConferenceConsoleConstants.LocalDialin = s;
		}
		catch (Exception e){
			ConferenceConsoleConstants.LocalDialin = "";
		}
		
		try{
			s = dialinProps.getString("LocalDialinTollFree");
			ConferenceConsoleConstants.LocalDialinTollFree = s;
		}
		catch (Exception e){
			ConferenceConsoleConstants.LocalDialinTollFree = "";
		}
		
		System.out.println("******************** Dialin Properties initialization complete **************");
		
	}
	
	protected	synchronized	static	void	readProperties(ResourceBundle conferenceProps)
	{
		try
		{
			String	s = "";
//			ConferenceConsoleConstants.webappLocalPath = conferenceProps.getString("dimdim.webappLocalPath");
			
//			s = conferenceProps.getString("dimdim.maxConcurrentConferences");
//			ConferenceConsoleConstants.maxConcurrentConferences = Integer.parseInt(s);
			
//			s = conferenceProps.getString("dimdim.maxTotalParticipants");
//			ConferenceConsoleConstants.maxTotalParticipants = Integer.parseInt(s);
			try{
				s = conferenceProps.getString("dimdim.maxParticipantsPerConference");
				ConferenceConsoleConstants.maxParticipantsPerConference = Integer.parseInt(s);
			}
			catch (Exception e){
				ConferenceConsoleConstants.maxParticipantsPerConference = 100;
			}
			
			try{
				s = conferenceProps.getString("dimdim.minParticipantsPerConference");
				ConferenceConsoleConstants.minParticipantsPerConference = Integer.parseInt(s);
			}
			catch (Exception e){
				ConferenceConsoleConstants.minParticipantsPerConference = 20;
			}
			if(minParticipantsPerConference > maxParticipantsPerConference){
				minParticipantsPerConference = maxParticipantsPerConference;
			}
			
			try{
				s = conferenceProps.getString("dimdim.maxMeetingLengthMinutes");
				ConferenceConsoleConstants.maxMeetingLengthMinutes = Integer.parseInt(s);
			}
			catch (Exception e){
				ConferenceConsoleConstants.maxMeetingLengthMinutes = 300;
			}
			
			try{
				s = conferenceProps.getString("dimdim.maxObjectsInOneEvent");
				ConferenceConsoleConstants.maxObjectsInOneEvent = Integer.parseInt(s);				
			}
			catch (Exception e){
				ConferenceConsoleConstants.maxObjectsInOneEvent = 25;
			}
			
			try{
				s = conferenceProps.getString("dimdim.maxAttendeeAudios");
				ConferenceConsoleConstants.maxAttendeeAudios = Integer.parseInt(s);
			}
			catch (Exception e){
				ConferenceConsoleConstants.maxAttendeeAudios = 3;
			}
			
			try{
				s = conferenceProps.getString("dimdim.maxAttendeeVideos");
				ConferenceConsoleConstants.maxAttendeeVideos = Integer.parseInt(s);
			}
			catch (Exception e){
				ConferenceConsoleConstants.maxAttendeeVideos = 1;
			}
			
			if(ConferenceConsoleConstants.maxAttendeeAudios > 6){
				ConferenceConsoleConstants.maxAttendeeAudios = 6;
			}
			
//			s = conferenceProps.getString("dimdim.presenterDownloadBandwidthRequired");
//			ConferenceConsoleConstants.presenterDownloadBandwidthRequired = Integer.parseInt(s);
			
//			s = conferenceProps.getString("dimdim.attendeeDownloadBandwidthRequired");
//			ConferenceConsoleConstants.attendeeDownloadBandwidthRequired = Integer.parseInt(s);
			
//			s = conferenceProps.getString("dimdim.presenterUploadBandwidthRequired");
//			ConferenceConsoleConstants.presenterUploadBandwidthRequired = Integer.parseInt(s);
			
//			s = conferenceProps.getString("dimdim.attendeeUploadBandwidthRequired");
//			ConferenceConsoleConstants.attendeeUploadBandwidthRequired = Integer.parseInt(s);
			
			try{
				s = conferenceProps.getString("dimdim.installationId");
				ConferenceConsoleConstants.installationId = s;
			}
			catch(Exception e){
				ConferenceConsoleConstants.installationId = "_default";
			}
			
			try{
				s = conferenceProps.getString("dimdim.installationPrefix");
				ConferenceConsoleConstants.installationPrefix = s;
			}
			catch(Exception e){
				ConferenceConsoleConstants.installationPrefix = "____";
			}
			
			try{
				s = conferenceProps.getString("dimdim.trackbackURL");
				ConferenceConsoleConstants.trackbackURL = s;
			}
			catch(Exception e){
				ConferenceConsoleConstants.trackbackURL = "http://www.dimdim.com";
			}
			
			try{
				s = conferenceProps.getString("dimdim.copyright.policy.link");
				ConferenceConsoleConstants.copyrightLink = s;
			}
			catch (Exception e){
				ConferenceConsoleConstants.copyrightLink = "http://www.dimdim.com/ppolicy.html";
			}
			
			try{
				s = conferenceProps.getString("dimdim.copyright.tm.link.value");
				ConferenceConsoleConstants.trademarkLink = s;
			}
			catch (Exception e){
				ConferenceConsoleConstants.trademarkLink = "http://www.dimdim.com/tm.html";
			}
			
			try{
				s = conferenceProps.getString("dimdim.defaultCollabURL");
				ConferenceConsoleConstants.defaultUrl = s;
			}
			catch (Exception e){
				ConferenceConsoleConstants.defaultUrl = "";
			}
			
			try{
				s = conferenceProps.getString("dimdim.assignMikeOnJoin");
				ConferenceConsoleConstants.assignMikeOnJoin = Boolean.parseBoolean(s);
			}
			catch (Exception e){
				ConferenceConsoleConstants.assignMikeOnJoin = false;
			}
			
			try{						
				s = conferenceProps.getString("dimdim.presenterEmailsFile");
				ConferenceConsoleConstants.presenterEmailsListFile =
					(new File( webinfDir, s)).getAbsolutePath();
			}
			catch (Exception e)
			{
				ConferenceConsoleConstants.presenterEmailsListFile =
					(new File( webinfDir, "dimdimPresenters.txt")).getAbsolutePath();
			}
			
			try
			{
				s = conferenceProps.getString("dimdim.dimdimAdminsFile");
				ConferenceConsoleConstants.dimdimAdminsListFile =
					(new File( webinfDir, s)).getAbsolutePath();
			}
			catch (Exception e){
				ConferenceConsoleConstants.dimdimAdminsListFile =
					(new File( webinfDir, "dimdimAdmins.txt")).getAbsolutePath();
			}
			
			try{
				s = conferenceProps.getString("dimdim.conferenceSpecsFile");
				ConferenceConsoleConstants.conferenceSpecsFile =
					(new File( webappLocalPath, s)).getAbsolutePath();	
			}
			catch(Exception e){
				ConferenceConsoleConstants.conferenceSpecsFile =
					(new File( webappLocalPath, "dimdimConferenceSpecs.dmc")).getAbsolutePath();
			}
						
			try{
				s = conferenceProps.getString("dimdim.presentationStorageRoot");
				ConferenceConsoleConstants.presentationStorageRoot =
					(new File( webappLocalPath,s)).getAbsolutePath();
			}
			catch (Exception e){
				ConferenceConsoleConstants.presentationStorageRoot =
					(new File( webappLocalPath,"presentations")).getAbsolutePath();
			}
			
			try{
				s = conferenceProps.getString("dimdim.webappName");
				ConferenceConsoleConstants.defaultWebappName = s;
			}
			catch (Exception e){
				ConferenceConsoleConstants.defaultWebappName = "dimdim";
			}
			
			try{
				s = conferenceProps.getString("dimdim.authenticationPolicy");
			}
			catch(Exception e){
				s = ConferenceConsoleConstants.NO_CHECK;
			}
				System.out.println("Authentication policy: "+s);
			if (s.equals(ConferenceConsoleConstants.CHECK_EMAIL) ||
					s.equals(ConferenceConsoleConstants.CHECK_KEY) ||
					s.equals(ConferenceConsoleConstants.NO_CHECK))
			{
				ConferenceConsoleConstants.authenticationPolicy = s;
				if (s.equals(ConferenceConsoleConstants.CHECK_KEY))
				{
					try{
						s = conferenceProps.getString("dimdim.keyFile");
						ConferenceConsoleConstants.readKey(s);
					}
					catch(Exception e) {
						System.out.println("Key file not found in dimdim.properties file");
					}
				}
			}
			
//			ConferenceConsoleConstants.avRtmpURL = conferenceProps.getString("dimdim.avRTMPUrl");
//			ConferenceConsoleConstants.shareRtmpURL = conferenceProps.getString("dimdim.shareRTMPUrl");
			ConferenceConsoleConstants.serverIP = conferenceProps.getString("dimdim.serverAddress").trim();
			ConferenceConsoleConstants.serverPortNumber = conferenceProps.getString("dimdim.serverPortNumber").trim();
//			ConferenceConsoleConstants.reflectorIP = conferenceProps.getString("dimdim.reflectorAddress");
//			ConferenceConsoleConstants.reflectorPortNumber = conferenceProps.getString("dimdim.reflectorPort");
			ConferenceConsoleConstants.dMServerAddress = conferenceProps.getString("dimdim.dmsServerAddress").trim();
			ConferenceConsoleConstants.dMServerInternalAddress = conferenceProps.getString("dimdim.dmsServerInternalAddress").trim();
			
			ConferenceConsoleConstants.dMServerMboxInternalAddress = conferenceProps.getString("dimdim.dmsServerMboxInternal").trim();
			ConferenceConsoleConstants.dMServerMboxExternalAddress = conferenceProps.getString("dimdim.dmsServerMboxExternal").trim();
			
			ConferenceConsoleConstants.serverAddress = "http://"+serverIP+":"+serverPortNumber;
			ConferenceConsoleConstants.serverSecurePortNumber = "443";//conferenceProps.getString("dimdim.serverSecurePortNumber");
			ConferenceConsoleConstants.serverSecureAddress = "https://"+serverIP+":"+serverSecurePortNumber;
			
//			if (ConferenceConsoleConstants.downloadExePathFull == null)
//			{
//				String jspDir  = (new File( ConferenceConsoleConstants.webappLocalPath, "jsp")).getAbsolutePath();
//				System.out.println("jspDir:"+jspDir);
//				String activexDir  = (new File( jspDir, "activex")).getAbsolutePath();
//				System.out.println("activexDir:"+activexDir);
//				ConferenceConsoleConstants.sfxPathFull =
//					(new File( activexDir, sfxName)).getAbsolutePath();
//				System.out.println("sfxPathFull:"+sfxPathFull);
//				ConferenceConsoleConstants.baseNsisExePathFull =
//					(new File( activexDir, baseNsisExeName)).getAbsolutePath();
//				System.out.println("baseNsisExePathFull:"+baseNsisExePathFull);
//				ConferenceConsoleConstants.downloadExePathFull =
//					(new File( activexDir, downloadExeName)).getAbsolutePath();
//				System.out.println("downloadExePathFull:"+downloadExePathFull);
//				ConferenceConsoleConstants.downloadFirefoxExePathFull =
//					(new File( activexDir, downloadFirefoxExeName)).getAbsolutePath();
//				System.out.println("downloadExePathFull:"+downloadExePathFull);
//			}
			try{
				ConferenceConsoleConstants.webappName = conferenceProps.getString("dimdim.webappName");
			}
			catch(Exception e){
				ConferenceConsoleConstants.webappName = "dimdim";
			}
			try{
				ConferenceConsoleConstants.overrideMaxParticipants = conferenceProps.getString("override_max_participants");
			}
			catch (Exception e){
				ConferenceConsoleConstants.overrideMaxParticipants = "true";
			}
//			try{
//				ConferenceConsoleConstants.showInviteLinks = conferenceProps.getString("show_invite_links");
//			}
//			catch (Exception e){
//				ConferenceConsoleConstants.showInviteLinks = "true";
//			}
			try{
				ConferenceConsoleConstants.videoChatSupported = conferenceProps.getString("video_chat_supported");	
			}
			catch (Exception e){
				ConferenceConsoleConstants.videoChatSupported = "true";
			}
			
			try{
				ConferenceConsoleConstants.largeVideoSupported = conferenceProps.getString("large_video_supported");
			}
			catch (Exception e){
				ConferenceConsoleConstants.largeVideoSupported = "false";
			}
			/*
			if (ConferenceConsoleConstants.propertiesFilePath == null)
			{
				ConferenceConsoleConstants.timeZonesListFilePathFull =
						(new File( ConferenceConsoleConstants.webappLocalPath, "TimeZonesList.csv")).getAbsolutePath();
				ConferenceConsoleConstants.timeZonesList =
						new TimeZonesList(ConferenceConsoleConstants.timeZonesListFilePathFull);
				
				String webinfDir  = (new File( ConferenceConsoleConstants.webappLocalPath, "WEB-INF")).getAbsolutePath();
				String classesDir  = (new File( webinfDir, "classes")).getAbsolutePath();
				String resourcesDir  = (new File( classesDir, "resources")).getAbsolutePath();
				ConferenceConsoleConstants.propertiesFilePath  =
						(new File( resourcesDir, "DimDimConference.properties")).getAbsolutePath();
			}
			*/
			ConferenceConsoleConstants.propertiesFileUpdateTime =
					(new File(ConferenceConsoleConstants.propertiesFilePath)).lastModified();
			
//			StreamingServerManager.getManager().init(conferenceProps);
			try{
				s = conferenceProps.getString("dimdim.presenterSessionTimeout");
				ConferenceConsoleConstants.presenterSessionTimeout = Integer.parseInt(s);
			}
			catch (Exception e){
				ConferenceConsoleConstants.presenterSessionTimeout = 240;
			}
			
			try{
				s = conferenceProps.getString("dimdim.attendeeSessionTimeout");				
				ConferenceConsoleConstants.attendeeSessionTimeout = Integer.parseInt(s);
			}
			catch (Exception e){
				ConferenceConsoleConstants.attendeeSessionTimeout = 180;
			}
			
			
			//ConferenceConsoleConstants.productName = conferenceProps.getString("dimdim.productName");
			try{
				ConferenceConsoleConstants.productVersion = conferenceProps.getString("dimdim.productVersion");
			}
			catch (Exception e){
				ConferenceConsoleConstants.productVersion = "Dimdim Beta";
			}
			//ConferenceConsoleConstants.productWebSite = conferenceProps.getString("dimdim.productWebSite");
			try{
				s = conferenceProps.getString("dimdim.premiumCatalogue");
				ConferenceConsoleConstants.userTypeFreeOrPaid = s;
			}
			catch (Exception e){
				ConferenceConsoleConstants.userTypeFreeOrPaid = "1";
			}
			try{
				ConferenceConsoleConstants.premiumCatalogue = conferenceProps.getString("dimdim.premiumCatalogue");
			}
			catch (Exception e){
				ConferenceConsoleConstants.premiumCatalogue = "1";
			}
			try{
				ConferenceConsoleConstants.showRecordLinks = conferenceProps.getString("record_meeting_supported");
			}
			catch (Exception e){
				ConferenceConsoleConstants.showRecordLinks = "true";
			}
			try{
				ConferenceConsoleConstants.showFullScreenLink = conferenceProps.getString("full_screen_supported");
			}
			catch (Exception e){
				ConferenceConsoleConstants.showFullScreenLink = "true";
			}
			System.out.println("******************** Properties initialization complete **************");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		//	In deployment a user conference file could be created to override the
		//	defaults.
		/*
		try
		{
			ResourceBundle conferenceProps = ResourceBundle.getBundle("resources.UserConference");
			serverIP = conferenceProps.getString("dimdim.serverAddress");
			serverPortNumber = conferenceProps.getString("dimdim.serverPortNumber");
		}
		catch(Exception e)
		{
			
		}
		*/
	}
	public	static	String	getResourceKeyValue(String key, String defaultValue)
	{
		String s = defaultValue;
		try
		{
			if (ConferenceConsoleConstants.dimdimConferenceProperties != null)
			{
				s = ConferenceConsoleConstants.dimdimConferenceProperties.getString(key);
			}
		}
		catch(Exception e)
		{
			s = defaultValue;
		}
		return	s;
	}
	public	static	ResourceBundle	getDimdimConferenceProperties()
	{
		return	ConferenceConsoleConstants.dimdimConferenceProperties;
	}
//	public	static	TimeZonesList	getTimeZonesList()
//	{
//		return	ConferenceConsoleConstants.timeZonesList;
//	}
	
	public	static	EmoticonsList	getEmoticonsList()
	{
		return	ConferenceConsoleConstants.emoticonsList;
	}
	
	public	static	String	getLocaleEncode(String locale)
	{
		/*
		 String s = localeList.getLocaleEncoding(locale);
	   if (s != null)
	   {
		   return	s;
	   }
	   else
	   {
		   return	"ISO-8859-1";
	   }
	   */
		return "utf-8";
	}
	
//	public	static	void	setServerAddress(String serverAddress)
//	{
//		ConferenceConsoleConstants.serverAddress = serverAddress;
//	}
	public	static	File	getServerLogsDirectory()
	{
		File webappDir = new File(ConferenceConsoleConstants.webappLocalPath);
		File logsDir = new File(webappDir.getParentFile().getParentFile(), "logs");
		return	logsDir;
	}
	public	static	String	getServerAddress()
	{
		return	ConferenceConsoleConstants.serverAddress;
	}
	public	static	String	getResourcesDirectoryPath()
	{
		return	ConferenceConsoleConstants.resourcesDirectory;
	}
	public	static	String	getServerSecureAddress()
	{
		return	ConferenceConsoleConstants.serverSecureAddress;
	}
	public static String getWebappName()
	{
		return webappName;
	}
	public	static	String	getProductName()
	{
		return	ConferenceConsoleConstants.productName;
	}
	public	static	String	getProductVersion()
	{
		return	ConferenceConsoleConstants.productVersion;
	}
	public	static	String	getProductWebSite()
	{
		return	ConferenceConsoleConstants.productWebSite;
	}
//	public static String getAvRtmpURL()
//	{
//		return ConferenceConsoleConstants.avRtmpURL;
//	}
//	public static String getShareRtmpURL()
//	{
//		return ConferenceConsoleConstants.shareRtmpURL;
//	}
//	public static void setWebappName(String webappName)
//	{
//		ConferenceConsoleConstants.webappName = webappName;
//	}
//	public static String getPresentationStorageRoot()
//	{
//		return presentationStorageRoot;
//	}
	public static boolean isSecurityPolicyCheckKey()
	{
		return	ConferenceConsoleConstants.authenticationPolicy.
			equals(ConferenceConsoleConstants.CHECK_KEY);
	}
	public static String getSecurityKey()
	{
		return securityKey;
	}
	public static String getAuthenticationPolicy()
	{
		return authenticationPolicy;
	}
	public static String getLineSeparator()
	{
		return lineSeparator;
	}
//	public static int getMaxConcurrentConferences()
//	{
//		return maxConcurrentConferences;
//	}
	public static int getMaxParticipantsPerConference()
	{
		return maxParticipantsPerConference;
	}
	public static int getMinParticipantsPerConference()
	{
		return minParticipantsPerConference;
	}
//	public static int getMaxTotalParticipants()
//	{
//		return maxTotalParticipants;
//	}
	public static int getMaxMeetingLengthMinutes()
	{
		return maxMeetingLengthMinutes;
	}
	public static int getMaxObjectsInOneEvent()
	{
		return maxObjectsInOneEvent;
	}
	public static int getMaxAttendeeAudios()
	{
		return maxAttendeeAudios;
	}
	public static int getMaxAttendeeVideos()
	{
		return maxAttendeeVideos;
	}
//	public static int getPresenterDownloadBandwidthRequired()
//	{
//		return presenterDownloadBandwidthRequired;
//	}
//	public static int getAttendeeDownloadBandwidthRequired()
//	{
//		return attendeeDownloadBandwidthRequired;
//	}
//	public static int getPresenterUploadBandwidthRequired()
//	{
//		return presenterUploadBandwidthRequired;
//	}
//	public static int getAttendeeUploadBandwidthRequired()
//	{
//		return attendeeUploadBandwidthRequired;
//	}
	public static String getTrackbackURL()
	{
		return trackbackURL;
	}
//	public static boolean getAssignMikeOnJoin()
//	{
//		return assignMikeOnJoin;
//	}
	public static String getDimdimAdminsListFile()
	{
		return dimdimAdminsListFile;
	}
	public static String getPresenterEmailsListFile()
	{
		return presenterEmailsListFile;
	}
	public static String getConferenceSpecsFile()
	{
		return conferenceSpecsFile;
	}
	public static String getReleaseDate()
	{
		return	ConferenceConsoleConstants.releaseDate;
	}
	public static String getMajorVersion()
	{
		return	ConferenceConsoleConstants.majorVersion;
	}
	public static String getVersionNumber()
	{
		return	ConferenceConsoleConstants.versionNumber;
	}
//	public static void setPresentationStorageRoot(String presentationStorageRoot)
//	{
//		ConferenceConsoleConstants.presentationStorageRoot = presentationStorageRoot;
//	}
	public static int getMaxAudioBroadcasters()
	{
		return	ConferenceConsoleConstants.maxAttendeeAudios;
	}
	public static String getFullPhotoFile(String conferenceKey, String userId, int rand_no)
	{
		String photoDirPath = (new File(ConferenceConsoleConstants.presentationStorageRoot)).getAbsolutePath();
		String s2 = conferenceKey+"-"+userId.replaceAll("@","_")+Integer.toString(rand_no)+".jpg";
		String fullPhotoUrl = (new File( photoDirPath, s2)).getAbsolutePath();
		
		return	fullPhotoUrl;
	}
	public static String getPhotoUrl(String conferenceKey, String userId, int rand_no)
	{
		String s2 = conferenceKey+"-"+userId.replaceAll("@","_")+Integer.toString(rand_no)+".jpg";
		String url = "../../presentations/"+s2;
		
		return	url;
	}
	
	public	static	String	getDefaultLogo()
	{
//		return	ConferenceConsoleConstants.serverAddress+"/"+ConferenceConsoleConstants.webappName+
//		"/html/console/images/dimdim-logo.png";
		return	"images/dimdim-logo.png";
	}
	
	public	static	String	getJoinURL(String conferenceKey)
	{
		return	ConferenceConsoleConstants.serverAddress+"/"+ConferenceConsoleConstants.webappName+
				"/GetJoinConferenceForm.action?confKey="+conferenceKey;
	}
	public	static	String	getStartURL(String conferenceKey)
	{
		return	ConferenceConsoleConstants.serverAddress+"/"+ConferenceConsoleConstants.webappName+
				"/GetStartConferenceForm.action?confKey="+conferenceKey;
	}
	protected	static	void	readKey(String keyFile)
	{
//		if (securityKey == null)
//		{
//			StringBuffer buf = new StringBuffer();
			try
			{
				File kf = new File(webinfDir,keyFile);
				/*
				FileInputStream fis = new FileInputStream(kf);
				BufferedInputStream bis = new BufferedInputStream(fis);
				byte[] byteBuf = new byte[256];
				int len=0;
				while((len=bis.read(byteBuf,0,256)) > 0)
				{
					buf.append(new String(byteBuf,0,len));
				}
				try
				{
					bis.close(); fis.close();
				}
				catch(Exception e2)
				{
					
				}
				*/
				FileUtil fileUtil = new FileUtil();
				securityKey = fileUtil.readFileContentsAsString(kf);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
//			securityKey = buf.toString();
//		}
	}
	public	static	String	getInvitationEmailTemplateBuffer()
	{
		if (invitationEmailTemplateBuffer == null)
		{
//			StringBuffer buf = new StringBuffer();
			try
			{
				File templateFile = new File(webappLocalPath,INVITATION_EMAIL_TEMPLATE);
				/*
				FileInputStream fis = new FileInputStream(templateFile);
				BufferedInputStream bis = new BufferedInputStream(fis);
				byte[] byteBuf = new byte[256];
				int len=0;
				while((len=bis.read(byteBuf,0,256)) > 0)
				{
					buf.append(new String(byteBuf,0,len));
				}
				try
				{
					bis.close(); fis.close();
				}
				catch(Exception e2)
				{
					
				}
				*/
				FileUtil fileUtil = new FileUtil();
				invitationEmailTemplateBuffer = fileUtil.readFileContentsAsString(templateFile);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
//			invitationEmailTemplateBuffer = buf.toString();
			
			System.out.println("Invitation Template:"+invitationEmailTemplateBuffer);
		}
		return	invitationEmailTemplateBuffer;
	}
	public static String getServerIP()
	{
		return serverIP;
	}
	public static String getServerPortNumber()
	{
		return serverPortNumber;
	}
	public static String getServerSecurePortNumber()
	{
		return serverSecurePortNumber;
	}
	public static String getDMServerAddress()
	{
		return dMServerAddress;
	}
	public static String getDMServerInternalAddress()
	{
		return dMServerInternalAddress;
	}
	public static String getOverrideMaxParticipants()
	{
		return overrideMaxParticipants;
	}
	
	public static String getshowInviteLinks()
	{
		return showInviteLinks;
	}
	public static String getVideoChatSupported()
	{
		return videoChatSupported;
	}
	public static String getLargeVideoSupported()
	{
		return largeVideoSupported;
	}
	public static int	getPresenterSessionTimeout()
	{
		return	presenterSessionTimeout;
	}
	public static int	getAttendeeSessionTimeout()
	{
		return	attendeeSessionTimeout;
	}
	public static String getCopyrightLink()
	{
		return copyrightLink;
	}
	public static String getTrademarkLink()
	{
		return trademarkLink;
	}
	public static String getLicenseTag()
	{
		return	ConferenceConsoleConstants.getResourceKeyValue("dimdim.license", "");
	}
	public static String getWebInfDirectory()
	{
		return	webinfDir;
	}
	public static String getInstallationId()
	{
		return	ConferenceConsoleConstants.installationId;
	}
	public static String getInstallationPrefix()
	{
		return	ConferenceConsoleConstants.installationPrefix;
	}
//	public static String getReflectorIP()
//	{
//	    return reflectorIP;
//	}
//	public static String getReflectorPortNumber()
//	{
//	    return reflectorPortNumber;
//	}
	public static String getUserTypeFreeOrPaid() {
		if(userTypeFreeOrPaid.equalsIgnoreCase("1"))
		{
			return LocaleResourceFile.PREMIUM;
		}
		else
		{
			return LocaleResourceFile.FREE;
		}
		
	}
	public static String getPremiumCatalogue() {
		return premiumCatalogue;
	}
	
	public static String getDMServerMboxInternalAddress() {
		return dMServerMboxInternalAddress;
	}
	public static String getDMServerMboxExternalAddress() {
		return dMServerMboxExternalAddress;
	}
	public static String getDefaultUrl() {
		return defaultUrl ;
	}
}
