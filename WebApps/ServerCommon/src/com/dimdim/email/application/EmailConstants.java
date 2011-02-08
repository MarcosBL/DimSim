package com.dimdim.email.application;

import java.io.File;
import java.io.FileInputStream;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

public class EmailConstants {

	public static final String EVENT_INVITATION_EMAIL_DISPATCH_ERROR = "roster.emailError";
	public static final String EVENT_INVITATION_EMAIL_DISPATCH_SUCCESS = "roster.emailOK";
	public static final String RESPONSE_OK = "response.ok";
	public static final String	lineSeparator = "\n";
	
	public static final String FEATURE_CONF = "feature.conf";
	public static final String FEATURE_ROSTER = "feature.roster";
	public static final String FEATURE_CHAT = "feature.chat";
	public static final String FEATURE_QUESTION_MANAGER = "feature.question";
	public static final String FEATURE_RESOURCE_MANAGER = "feature.resource";
	public static final String FEATURE_POLL = "feature.poll";
	public static final String FEATURE_PRESENTATION = "feature.presentation";
	public static final String FEATURE_SHARING = "feature.sharing";
	public static final String FEATURE_VIDEO = "feature.video";
	public static final String FEATURE_AUDIO = "feature.audio";
	public static final String FEATURE_SETTINGS = "feature.settings";
	public static final String FEATURE_WHITEBOARD = "feature.wb";
	
	private static String resourcesDirectory = null;
	private static String propertiesFilePath = null;
	private static PropertyResourceBundle prb = null;
	private static String serverIP;
	private static String serverPortNumber;
	private static String serverAddress;
	private static String serverSecurePortNumber;
	private static String serverSecureAddress;
	private static String webappName;
	
	public static final String WW_TOKEN_SUCCESS = "success";
	public static final String WW_TOKEN_ERROR = "error";
	public static final String WW_TOKEN_IN_CONFERENCE = "in_conference";
	
	public	static	ResourceBundle	getDimdimProperties()
	{
		return	prb;
	}
	
	public	static	void	initConstants(String localPath){
		
		resourcesDirectory = localPath + File.separator + "WEB-INF" +  File.separator +"classes"
								+ File.separator + "resources";
		
		propertiesFilePath  =	(new File( resourcesDirectory, "portal.properties")).getAbsolutePath();
		
		File propsFile = new File(propertiesFilePath);
		try
		{
			FileInputStream fis = new FileInputStream(propsFile);
			prb = new PropertyResourceBundle(fis);
			readProperties(prb);
			try
			{
				fis.close();
			}
			catch(Exception e)
			{
			}
		}
		catch(Exception e){
				e.printStackTrace();
		}
	}
	
	protected	synchronized	static	void	readProperties(ResourceBundle conferenceProps)
	{
		try
		{
			serverIP = conferenceProps.getString("dimdim.serverAddress");
			serverPortNumber = conferenceProps.getString("dimdim.serverPortNumber");
			serverAddress = "http://"+serverIP+":"+serverPortNumber;
			serverSecurePortNumber = conferenceProps.getString("dimdim.serverSecurePortNumber");
			serverSecureAddress = "http://"+serverIP+":"+serverSecurePortNumber;
			webappName = conferenceProps.getString("dimdim.webappName");
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public	static	String	getServerAddress()
	{
		return serverAddress;
	}
	
	public	static	String	getWebappName()
	{
		return webappName;
	}

}
