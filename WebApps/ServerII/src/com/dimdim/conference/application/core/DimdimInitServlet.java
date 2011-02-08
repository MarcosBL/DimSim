package com.dimdim.conference.application.core;

import java.io.File;
import java.util.Vector;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import com.dimdim.conference.ConferenceConsoleConstants;
import com.dimdim.conference.application.UserSessionManager;
import com.dimdim.conference.application.presentation.PresentationManager;
//import com.dimdim.conference.application.presentation.dms.DMSPresentationManager;
import com.dimdim.conference.db.ConferenceDB;
import com.dimdim.conference.install.ServerNamePortNumberSetup;
import com.dimdim.locale.LocaleManager;
import com.dimdim.util.misc.Emoticon;

import com.dimdim.data.application.UIDataManager;

/**
 * Servlet implementation class for Servlet: PresentationUploadServlet
 *
 */
public class DimdimInitServlet extends javax.servlet.http.HttpServlet
{
	protected	static	boolean	initialized = false;
	
	public void init(ServletConfig servletConfig) throws ServletException
	{
		super.init(servletConfig);
		if (DimdimInitServlet.initialized)
		{
			return;
		}
		DimdimInitServlet.initialized = true;
		ServletContext sc = servletConfig.getServletContext();
		String path = sc.getRealPath("/");
		System.out.println("************************************ REAL PATH: "+path);
		try
		{
			System.out.println("Initializing ConferenceConsoleConstants");
			ConferenceConsoleConstants.initConstants(path);
			UIDataManager.initUIDataManager(path);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		try
		{
			System.out.println("Initializing ConferenceDB");
			ConferenceDB.initDB(ConferenceConsoleConstants.getWebappLocalPath());
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		try
		{
			System.out.println("Initializing PresentationManager");
			PresentationManager.initializePresentationManager();
			
//			System.out.println("Initializing DMSPresentationManager");
//			DMSPresentationManager.initializePresentationManager();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		try
		{
			System.out.println("Initializing UserSessionManager");
			UserSessionManager.getManager();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		try
		{
			System.out.println("Initializing LocaleManager");
			File dataDir = new File(ConferenceConsoleConstants.getWebappLocalPath(),"data");
			LocaleManager.initManager(dataDir.getAbsolutePath());
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		try
		{
			System.out.println("Initializing Short Cut Files, if required");
			ServerNamePortNumberSetup snpns = new ServerNamePortNumberSetup(
					ConferenceConsoleConstants.getWebappLocalPath(),
					ConferenceConsoleConstants.getServerIP(),
					ConferenceConsoleConstants.getServerPortNumber());
			snpns.doWork();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		try
		{
			this.initGlobalsStrings();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	private	void	initGlobalsStrings()
	{
		StringBuffer buf = new StringBuffer();
		buf.append("{");
		
		String webappName = ConferenceConsoleConstants.getWebappName();
		Vector emoticonsList = ConferenceConsoleConstants.getEmoticonsList().getEmoticonsList();
		int	len = emoticonsList.size();
		buf.append("num:\"");
		buf.append(len+"");
		buf.append("\",");
		for (int i=0; i<len; i++)
		{
			Emoticon emt = (Emoticon)emoticonsList.elementAt(i);
			String name = emt.getName();
			String value = emt.getValue();
			
			buf.append("t"+i+"_name:\"");
			buf.append(name);
			buf.append("\",");
			
			buf.append("t"+i+"_value:\"");
			buf.append("/"+webappName+value);
			buf.append("\"");
			if (i<(len-1))
			{
				buf.append(",");
			}
		}
		buf.append("}");
		
		UIDataManager.getUIDataManager().addGlobalsString("emoticons", buf.toString());
	}
}