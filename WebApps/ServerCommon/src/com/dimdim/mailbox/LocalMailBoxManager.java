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
 * Part of the DimDim V 2.0 Codebase (http://www.dimdim.com)	          *
 *                                                                        *
 * Copyright (c) 2008 Dimdim Inc. All Rights Reserved.              	  *
 *                                                                        *
 *                                                                        *
 * This code is licensed under the DimDim License                         *
 * For details please visit http://www.dimdim.com/license                 *
 *                                                                        *
 **************************************************************************
 */

package com.dimdim.mailbox;

import java.io.*;

/**
 * A MailBox Manager class that helps navigate the directory tree
 * and auto creates the dir structure
 * 
 * @author Saurav Mohapatra
 * @email saurav@dimdim.com
 */
public class LocalMailBoxManager 
{
//	private static LocalMailBoxManager sInstance = null;
//	private String mboxRoot = null;
//	private	String installationRoot = null;
	private static final String[] types = new String[]{ "PPT","AV","CONF","MISC","DTP" };
	
//	public static synchronized LocalMailBoxManager getInstance()
//	{
//		if(sInstance == null)
//		{
//			sInstance = new LocalMailBoxManager();
//		}
//		return sInstance;
//	}
	
	private	String	mboxRoot;
	private	String	installationId;
	private	String	installationRoot;
	
	public	LocalMailBoxManager(String mboxRoot, String installationId)	throws	Exception
	{
		this.mboxRoot = mboxRoot;
		this.installationId = installationId;
		this.init(mboxRoot, true);
		this.getInstallationDirectory(installationId, true);
	}
	public	String	getMboxRoot()
	{
		return	this.mboxRoot;
	}
	public	String	getInstallationRoot()
	{
		return	this.installationRoot;
	}
	public	String	getInstallationId()
	{
		return	this.installationId;
	}
	/**
	 * initialize the mailbox manager
	 * @param mboxRoot - mailbox root
	 * @param forceCreate - if true then the directory is created if does not exist
	 * @throws Exception
	 */
	private	void init(String mboxRoot, boolean forceCreate) throws Exception
	{
		File dir = new File(mboxRoot);
		if(dir.exists())
		{
			if(!dir.isDirectory())
			{
				throw new Exception(mboxRoot+" is not a directory!");
			}
		}
		else if(forceCreate)
		{
			dir.mkdirs();
		}
		else
		{
			throw new Exception(mboxRoot+" does not exist!");
		}
		this.mboxRoot = mboxRoot;
	}
	/**
	 * get the absolute path to the installation directory
	 * @param dimdimId
	 * @param forceCreate
	 * @return
	 * @throws Exception
	 */
	private String getInstallationDirectory(String installationId, boolean forceCreate) throws Exception
	{
		if(mboxRoot == null)
		{
			throw new Exception("Mailbox Manager not initialized");
		}
		else
		{
			File dir = new File(mboxRoot,installationId);
			if(dir.exists())
			{
				if(!dir.isDirectory())
				{
					throw new Exception(dir+" is not a directory!");
				}
			}
			else if(forceCreate)
			{
				createMailbox(dir);
			}
			else
			{
				throw new Exception(dir+" does not exist!");
			}
			this.installationRoot = dir.getAbsolutePath();
			return dir.getAbsolutePath();
		}
	}
	/**
	 * get the absolute path to the mailbox directory
	 * @param dimdimId
	 * @param forceCreate
	 * @return
	 * @throws Exception
	 */
	public String getMailboxDirectory(String dimdimId, boolean forceCreate) throws Exception
	{
		if(installationRoot == null)
		{
			throw new Exception("Mailbox Manager not initialized");
		}
		else
		{
			if (dimdimId.length() < 4)
			{
				throw new Exception("Dimdim ID must be at least 4 characters long");
			}
			if (dimdimId.indexOf("/") >= 0)
			{
				throw new Exception("Dimdim ID must not contain / character");
			}
			if (dimdimId.indexOf("____") >= 0)
			{
				throw new Exception("Dimdim ID must not contain ____ character sequence");
			}
			String l1 = dimdimId.substring(0,1);
			if (l1.equals("."))
			{
				throw new Exception("Dimdim ID must not start with a .");
			}
			String levelRoot = getRoomLevelDirectory(installationRoot,l1,forceCreate);
			if (dimdimId.length() > 1)
			{
				String l2 = dimdimId.substring(0, 2);
				levelRoot = getRoomLevelDirectory(levelRoot,l2,forceCreate);
			}
			if (dimdimId.length() > 2)
			{
				String l3 = dimdimId.substring(0, 3);
				levelRoot = getRoomLevelDirectory(levelRoot,l3,forceCreate);
			}
			File dir = new File(levelRoot,dimdimId);
			if(dir.exists())
			{
				if(!dir.isDirectory())
				{
					throw new Exception(dir+" is not a directory!");
				}
			}
			else if(forceCreate)
			{
				createMailbox(dir);
			}
			else
			{
				throw new Exception(dir+" does not exist!");
			}
			return dir.getAbsolutePath();
		}
	}
	private	String getRoomLevelDirectory(String baseDirectory, String dirName, boolean forceCreate) throws Exception
	{
		File dir = new File(baseDirectory,dirName);
		if(dir.exists())
		{
			if(!dir.isDirectory())
			{
				throw new Exception(dir+" is not a directory!");
			}
		}
		else if(forceCreate)
		{
			createMailbox(dir);
		}
		else
		{
			throw new Exception(dir+" does not exist!");
		}
		return dir.getAbsolutePath();
	}
	/**
	 * get abs. path to room directory
	 * @param dimdimId
	 * @param roomName
	 * @param forceCreate
	 * @return
	 * @throws Exception
	 */
	public String getRoomDirectory(String dimdimId, String roomName, boolean forceCreate) throws Exception
	{
		File dir = new File(getMailboxDirectory(dimdimId,forceCreate),roomName);
		
		if(dir.exists())
		{
			if(!dir.isDirectory())
			{
				throw new Exception(dir+" is not a directory!");
			}
		}
		else if(forceCreate)
		{
			createRoom(dir);
		}
		else
		{
			throw new Exception(dir+" does not exist!");
		}
		return dir.getAbsolutePath();
	}
	public	String	getStaticFilesDirectory(String dimdimId, String roomName, boolean forceCreate) throws Exception
	{
		File dir = new File(getRoomDirectory(dimdimId,roomName,forceCreate),"_staticfiles");
		
		if(dir.exists())
		{
			if(!dir.isDirectory())
			{
				throw new Exception(dir+" is not a directory!");
			}
		}
		else if(forceCreate)
		{
			createRoom(dir);
		}
		else
		{
			throw new Exception(dir+" does not exist!");
		}
		return dir.getAbsolutePath();
	}
	/**
	 * get the session directory and create it, if non-existant
	 * @param dimdimId
	 * @param roomName
	 * @param sessionId
	 * @return
	 */
	String getSessionDirectory(String dimdimId, String roomName, String sessionId) throws Exception
	{
		return getSessionDirectory(dimdimId, roomName, sessionId, true);
	}
	/**
	 * get abs. path to session directory
	 * @param dimdimId
	 * @param roomName
	 * @param sessionId
	 * @param forceCreate
	 * @return
	 * @throws Exception
	 */
	public String getSessionDirectory(String dimdimId, String roomName, String sessionId, boolean forceCreate) throws Exception
	{
		File dir = new File(getRoomDirectory(dimdimId,roomName, forceCreate),sessionId);
		
		if(dir.exists())
		{
			if(!dir.isDirectory())
			{
				throw new Exception(dir+" is not a directory!");
			}
		}
		else if(forceCreate)
		{
			createSession(dir);
		}
		else
		{
			throw new Exception(dir+" does not exist!");
		}
		return dir.getAbsolutePath();
	}
	/**
	 * Get the path to the Inbox directory for a particular type
	 * @param dimdimId
	 * @param roomName
	 * @param sessionId
	 * @param type : defaults to type "MISC"
	 * @param forceCreate
	 * @return
	 * @throws Exception
	 */
	public String getInboxDirectoryByType(String dimdimId, String roomName, String sessionId, String type, boolean forceCreate) throws Exception
	{
		if(type != null)
		{
			int index = -1;
			for(int i = 0; i < types.length; i++)
			{
				if(type.equalsIgnoreCase(types[i]))
				{
					index = i;
					break;
				}
			}
			if(index == -1)
			{
				throw new Exception("invalid type "+type+" supplied!");
			}
		}
		else
		{
			type = "MISC";
		}
		String sessionDir = getSessionDirectory(dimdimId, roomName, sessionId, forceCreate);
		File inboxDir = new File(sessionDir, "Inbox");
		if(inboxDir.exists())
		{
			if(!inboxDir.isDirectory())
			{
				throw new Exception(inboxDir+" is not a directory!");
			}
		}
		else if(forceCreate)
		{
			if(!inboxDir.mkdir())
			{
				throw new Exception("failed to create "+inboxDir);
			}
		}
		else
		{
			throw new Exception(inboxDir+" does not exist!");
		}
		
		File dir = new File(inboxDir,type.toUpperCase());
		if(dir.exists())
		{
			if(!dir.isDirectory())
			{
				throw new Exception(dir+" is not a directory!");
			}
		}
		else if(forceCreate)
		{
			if(!dir.mkdir())
			{
				throw new Exception("failed to create "+dir);
			}
		}
		else
		{
			throw new Exception(dir+" does not exist!");
		}
		return dir.getAbsolutePath();
		
	}
	private void createMailbox(File dir) throws Exception
	{
		if(!dir.mkdir())
		{
			throw new Exception("Failed to create directory : "+dir);
		}
		File defaultRoomDir = new File(dir,"_default");
		createRoom(defaultRoomDir);
	}
	private void createRoom(File dir) throws Exception
	{
		if(!dir.mkdir())
		{
			throw new Exception("Failed to create directory : "+dir);
		}		
	}
	private void createSession(File dir) throws Exception 
	{
		if(!dir.mkdir())
		{
			throw new Exception("Failed to create directory : "+dir);
		}
		else
		{
			File inbox = new File(dir,"Inbox");
			inbox.mkdir();
			
			File outbox = new File(dir,"Output");
			outbox.mkdir();
			
			File tmp = new File(dir,"Temp");
			tmp.mkdir();
			
			for(int i = 0; i < types.length; i++)
			{
				File tdir = new File(inbox,types[i]);
				tdir.mkdir();
			}
		}
	}
}
