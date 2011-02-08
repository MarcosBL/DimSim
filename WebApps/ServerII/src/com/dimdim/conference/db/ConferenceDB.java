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

package com.dimdim.conference.db;

import	java.io.*;

import	java.util.Date;
import	java.util.List;
import	java.util.Vector;
import	java.util.TreeMap;
import java.util.Iterator;
import com.dimdim.conference.ConferenceConsoleConstants;

/**
 * 
 */

public	class	ConferenceDB
{
	private	static	ConferenceDB	theDB;
	
	public	static	ConferenceDB	getDB()
	{
//		if (ConferenceDB.theDB == null)
//		{
//			ConferenceDB.initDB(ConferenceConsoleConstants.getWebappLocalPath());
//		}
		return	ConferenceDB.theDB;
	}
	
	/**
	 * This method initializes the conference db. The DB is initialized on
	 * the first access. For performance reason, the data is read from the
	 * files or base db only and and when required.
	 */
	public	synchronized	static	void	initDB(String webappBaseRootPath)
	{
		if (ConferenceDB.theDB == null)
		{
			ConferenceDB.CreateDB(webappBaseRootPath);
		}
	}
	protected	synchronized	static	void	CreateDB(String webappBaseRootPath)
	{
		if (ConferenceDB.theDB == null)
		{
			ConferenceDB.theDB = new ConferenceDB(webappBaseRootPath);
		}
	}
	/**
	 * 
	 */
	protected	String	rootPath;
	protected	long	usersFileUpdateTime = 0;
	protected	File	usersFile;
	protected	long	adminsFileUpdateTime = 0;
	protected	File	adminsFile;
	protected	File	conferenceSpecsFile;
	
	protected	String	securityPolicy;
	protected	TreeMap	conferenceSpecs;
	protected	TreeMap	users;
	protected	TreeMap	admins;
	
	private	ConferenceDB(String rootPath)
	{
		this.rootPath = rootPath;
//		File rootDir = new File(rootPath);
		usersFile = new File(ConferenceConsoleConstants.getPresenterEmailsListFile());
		adminsFile = new File(ConferenceConsoleConstants.getDimdimAdminsListFile());
		conferenceSpecsFile = new File(ConferenceConsoleConstants.getConferenceSpecsFile());
		
		this.readConferenceUsers();
		this.readConferenceAdmins();
		this.readConferenceSpecs();
	}
	public	void	rereadUserFiles()
	{
//		System.out.println("Checking for users and admin file updates");
//		System.out.println("  Users file last read on:"+this.usersFileUpdateTime);
//		System.out.println("  Admins file last read on:"+this.adminsFileUpdateTime);
		long currentUpdateTime = this.usersFile.lastModified();
//		System.out.println("  Users file last modified on:"+currentUpdateTime);
		if (currentUpdateTime > this.usersFileUpdateTime)
		{
//			System.out.println("******************** Refreshing users file **************");
			this.readConferenceUsers();
		}
		currentUpdateTime = this.adminsFile.lastModified();
//		System.out.println("  Admins file last modified on:"+currentUpdateTime);
		if (currentUpdateTime > this.adminsFileUpdateTime)
		{
//			System.out.println("******************** Refreshing admins file **************");
			this.readConferenceAdmins();
		}
	}
	public	synchronized	ConferenceUser	addUser(String userName, String password)
		throws	UserExistsException
	{
		ConferenceUser user = new ConferenceUser(userName,password);
		this.users.put(userName.toLowerCase(),user);
		this.saveConferenceUsers();
		
		return	user;
	}
	public	synchronized	ConferenceSpec	addConferenceSpec(String organizerEmail,
			String organizerDisplayName, String name, String description,
			String key, Date startTime, String timeZone, List presenters, List attendees)
		throws	InvalidSpecException
	{
		ConferenceSpec cs = new ConferenceSpec(organizerEmail, organizerDisplayName,
				name,description,key,startTime,timeZone,presenters,attendees);
		this.conferenceSpecs.put(key,cs);
		this.saveConferenceSpecs();
		
		return	cs;
	}
	public	ConferenceSpec	getConferenceSpec(String key)
	{
		return	(ConferenceSpec)this.conferenceSpecs.get(key);
	}
	public	ConferenceAdmin	getConferenceAdmin(String name)
	{
		return	(ConferenceAdmin)this.admins.get(name);
	}
	public	ConferenceUser	getConferenceUser(String email)
	{
		return	(ConferenceUser)this.users.get(email.toLowerCase());
	}
	public TreeMap getConferenceSpecs()
	{
		return conferenceSpecs;
	}
	public TreeMap getUsers()
	{
		return users;
	}
	public	void	saveConferenceUsers()
	{
		try
		{
		}
		catch(Exception e)
		{
		}
	}
	protected	synchronized	void	readConferenceAdmins()
	{
		this.admins = new TreeMap();
		try
		{
			RandomAccessFile raf = new RandomAccessFile(this.adminsFile,"r");
			String line = raf.readLine();
			while (line != null)
			{
				line = line.trim();
				if (line.length() > 0)
				{
					int comma = line.indexOf(",");
					if (comma > 0 && comma < line.length())
					{
						String name = line.substring(0,comma);
						String password = line.substring(comma+1);
//						System.out.println("Admin name:"+name+", password: *******");
						this.admins.put(name,new ConferenceAdmin(name,password));
					}
				}
				line = raf.readLine();
			}
			raf.close();
			this.adminsFileUpdateTime = this.adminsFile.lastModified();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	protected	synchronized	void	readConferenceUsers()
	{
		this.users = new TreeMap();
		try
		{
			RandomAccessFile raf = new RandomAccessFile(this.usersFile,"r");
			String line = raf.readLine();
			while (line != null)
			{
				line = line.trim();
//				if (line.indexOf(ConferenceConsoleConstants.lineSeparator) > 0)
//				{
//					line = line.substring(0,line.length()-1);
//				}
//				System.out.println("User email:"+line);
				this.users.put(line.toLowerCase(),new ConferenceUser(line,line));
				line = raf.readLine();
			}
			raf.close();
			this.usersFileUpdateTime = this.usersFile.lastModified();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	public	void	saveConferenceSpecs()
	{
		try
		{
			this.trimConferenceSpecs();
			
			FileOutputStream fos = new FileOutputStream(this.conferenceSpecsFile);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			
			oos.writeObject(this.conferenceSpecs);
			
			try { oos.close(); fos.close(); } catch(Exception e2) { }
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	public	void	trimConferenceSpecs()
	{
		try
		{
			Vector v = new Vector();
			Iterator iter = this.conferenceSpecs.keySet().iterator();
			while (iter.hasNext())
			{
				String key = (String)iter.next();
				ConferenceSpec cs = (ConferenceSpec)this.conferenceSpecs.get(key);
				if (cs.isMoreThanDayOld())
				{
					v.addElement(key);
				}
			}
			int size = v.size();
			for (int i=0; i<size; i++)
			{
				this.conferenceSpecs.remove((String)(v.elementAt(i)));
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	public	void	readConferenceSpecs()
	{
		try
		{
			if (this.conferenceSpecsFile.exists())
			{
				FileInputStream fis = new FileInputStream(this.conferenceSpecsFile);
				ObjectInputStream ois = new ObjectInputStream(fis);
				
				this.conferenceSpecs = (TreeMap)ois.readObject();
				
				try { ois.close(); fis.close(); } catch(Exception e2) { }
			}
			else
			{
				this.conferenceSpecs = new TreeMap();
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			this.conferenceSpecs = new TreeMap();
		}
	}
}
