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
 * Part of the DimDim V 4.0 Codebase (http://www.dimdim.com)	          *
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

import java.io.File;
import java.util.StringTokenizer;

/**
 * A MailBox Manager class that helps navigate the directory tree
 * and auto creates the dir structure
 * 
 * @author Jayant Pandit
 * @email jayant@dimdim.com
 */
public class LocalDMSManager
{
	protected	static	LocalDMSManager	localDMSManager;
	
	public	static	void	initLocalDMSManager(String dmsMailBoxRoot, String installationId)
	{
		if (LocalDMSManager.localDMSManager == null)
		{
			if (dmsMailBoxRoot != null && installationId != null)
			{
				LocalDMSManager.createLocalDMSManager(dmsMailBoxRoot, installationId);
			}
			else
			{
				//	DMS is a shared service and remote.
			}
		}
	}
	public	static	boolean	isDMSLocal()
	{
		return	LocalDMSManager.localDMSManager != null;
	}
	public	static	LocalDMSManager	getLocalDMSManager()
	{
		return	LocalDMSManager.localDMSManager;
	}
	private	static	synchronized	void	createLocalDMSManager(String dmsMailBoxRoot, String installationId)
	{
		if (LocalDMSManager.localDMSManager == null)
		{
			LocalDMSManager.localDMSManager = new LocalDMSManager(dmsMailBoxRoot, installationId);
		}
	}
	
	private	LocalMailBoxManager	localMailBoxManager;
//	private	LocalFileUtil		localFileUtil;
	
	private	LocalDMSManager(String dmsMailBoxRoot, String installationId)
	{
//		this.localFileUtil = new LocalFileUtil();
		try
		{
			this.localMailBoxManager = new LocalMailBoxManager(dmsMailBoxRoot, installationId);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	//	These methods return exactly the same string as the respective web services.
	public	String	getLogoUrl(String dimdimId, String roomId)
	{
		String logoUrl = null;
		if (this.localMailBoxManager != null)
		{
			try
			{
				String staticFilesDirectory = this.localMailBoxManager.getStaticFilesDirectory(dimdimId, roomId, false);
				System.out.println("Static files directory:"+staticFilesDirectory.toString());
				File staticDir = new File(staticFilesDirectory);
				File[] files = staticDir.listFiles();
				if (files != null && files.length > 0)
				{
					System.out.println("Available files:"+files);
					String forSlash = "/";
					String backSlash = "\\\\";
					for (int i=0; i<files.length; i++)
					{
						String s = files[i].getName();
						System.out.println("Testing file:"+s);
						if (s.startsWith("dimdim_logo"))
						{
							logoUrl = files[i].getAbsolutePath().substring(this.localMailBoxManager.getMboxRoot().length()+1);
							System.out.println("Received logo url:"+logoUrl);
							logoUrl = logoUrl.replaceAll(backSlash, forSlash);
							System.out.println("Received logo url:"+logoUrl);
							break;
						}
					}
				}
				else
				{
					System.out.println("No static files");
				}
			}
			catch(Exception e)
			{
				System.out.println("Exception while getting the static files directory:"+e.getMessage());
				logoUrl = null;
			}
		}
		else
		{
			System.out.println("Mailbox not local -- this should never happen");
		}
		return	logoUrl;
	}
	//	The preloaded documents are kept in the 'Preloaded' directory under the room
	//	directory. Under this directory each of the presentations reside as a directory,
	//	with same structure as any presentation uploaded during the meeting.
	//	This method must return either null or a string with following format.
	//	{docs:[<single ppt object 1>,<single ppt object 2>,...]}
	/*	The preloaded presentations are not right now supported on the hosted version.
	 * 	When that support comes in, this code will be useful.
	public	String	getPreloadedPresentations(String dimdimId, String roomId)
	{
		String buf = null;
		if (this.localMailBoxManager != null)
		{
			StringBuffer singlePPTs = new StringBuffer();
			try
			{
				String globalMeetingDirectory = this.localMailBoxManager.getMailboxDirectory("global-meeting", true);
				String s = this.getPreloadedPPTs(globalMeetingDirectory);
				if (s != null && s.length() > 0)
				{
					singlePPTs.append(s);
				}
			}
			catch(Exception e)
			{
				System.out.println("Exception while reading global preloaded presentations:"+e.getMessage());
//				e.printStackTrace();
			}
			try
			{
				String meetingDirectory = this.localMailBoxManager.getMailboxDirectory(dimdimId, false);
				String s = this.getPreloadedPPTs(meetingDirectory);
				if (s != null && s.length() > 0)
				{
					if (singlePPTs.length() > 0)
					{
						singlePPTs.append(",");
					}
					singlePPTs.append(s);
				}
			}
			catch(Exception e)
			{
				System.out.println("Exception while reading meeting preloaded presentations:"+e.getMessage());
//				e.printStackTrace();
			}
			
			if (singlePPTs.length() > 0)
			{
				buf = "{docs:["+singlePPTs.toString()+"]}";
			}
		}
		return	buf;
	}
	private	String	getPreloadedPPTs(String directory)
	{
		String list = null;
		try
		{
			StringBuffer buf = new StringBuffer();
			File pptsDir = new File(directory,"Preloaded");
			if (pptsDir.exists())
			{
				File[] dirs = pptsDir.listFiles();
				if (dirs != null && dirs.length > 0)
				{
					System.out.println("Available ppts:"+dirs);
					for (int i=0; i<dirs.length; i++)
					{
						String pptiStr = this.getSinglePPTString(dirs[i]);
						if (pptiStr != null && pptiStr.length() > 0)
						{
							if (buf.length() > 0)
							{
								buf.append(",");
							}
							buf.append(pptiStr);
						}
						else
						{
							System.out.println("Error while readin ppt:"+dirs[i]);
						}
					}
				}
				else
				{
					System.out.println("Preloaded empty directory:"+directory);
				}
			}
			else
			{
				System.out.println("Preloaded ppts do not exist in directory:"+directory);
			}
			if (buf.length() > 0)
			{
				list = buf.toString();
			}
		}
		catch(Exception e)
		{
			System.out.println("Exception while reading preloaded ppts from directory:"+directory);
		}
		return	list;
	}
	//	This method must return an object json string of format:
	//	{docName:"<name>",noOfPages:<number of pages>,docID:"<doc id>",width:<width>,height:<height>}
	private	String	getSinglePPTString(File pptDirectory)
	{
		String str = null;
		try
		{
			File pptData = new File(pptDirectory,"documentData.txt");
			if (pptData.exists())
			{
				StringBuffer buf = new StringBuffer();
				String s = this.localFileUtil.readFileContentsAsString(pptData);
				if (s != null && s.length() > 0)
				{
					System.out.println("PPT Data: "+s);
					StringTokenizer parser = new StringTokenizer(s,",");
					while (parser.hasMoreTokens())
					{
						String tkn = parser.nextToken();
						int i = tkn.indexOf("=");
						String name = tkn.substring(0,i);
						String value = tkn.substring(i+1);
						if (buf.length() > 0)
						{
							buf.append(",");
						}
						else
						{
							buf.append("{");
						}
						if (name.equals("docName"))
						{
							byte[] bytes = Base64.decode(value);
							String base64DecodedFileName = new String(bytes);
							value = base64DecodedFileName;
						}
						if (name.equals("docID") || name.equals("docName"))
						{
							buf.append(name);
							buf.append(":\"");
							buf.append(value);
							buf.append("\"");
						}
						else
						{
							buf.append(name);
							buf.append(":");
							buf.append(value);
						}
					}
					buf.append("}");
					str = buf.toString();
					
					//	TODO copy the directory to slidedeck.
				}
				else
				{
					System.out.println("Error while reading file: "+pptData);
				}
			}
			else
			{
				System.out.println("No available PPT data in directory:"+pptDirectory);
			}
		}
		catch(Exception e)
		{
			System.out.println("Exception while reading preloaded ppt data from directory:"+pptDirectory);
		}
		return	str;
	}
	*/
}
