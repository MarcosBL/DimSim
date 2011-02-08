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
 
package com.dimdim.conference.application.session;

import	java.util.Vector;
import	java.io.*;

import	com.dimdim.conference.ConferenceConsoleConstants;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */
public class AdminSession
{
	protected	Vector	niceNames;
	protected	Vector	logFiles;
	protected	Vector	logFileSizes;
	
//	protected	int	currentLogIndex;
//	protected	int	currentFilePosition;
	
	protected	LogFileBrowseSession	logFileBrowseSession;
	
	public	AdminSession()
	{
		this.readLogFilesList();
//		currentLogIndex = 0;
//		currentFilePosition = 0;
	}
	public	void	readLogFilesList()
	{
		this.niceNames = new Vector();
		this.logFiles = new Vector();
		this.logFileSizes = new Vector();
		try
		{
			File serverLogsDirectory = ConferenceConsoleConstants.getServerLogsDirectory();
			File[] files = serverLogsDirectory.listFiles();
			int	len = files.length;
			for (int i=0; i<len; i++)
			{
				String s = files[i].getAbsolutePath();
				String niceName = s;
				int slash = s.lastIndexOf("\\");
				if (slash > 0)
				{
					niceName = s.substring(slash+1);
				}
				this.niceNames.addElement(niceName);
				this.logFiles.addElement(s);
				this.logFileSizes.addElement(new Long(files[i].length()));
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	public LogFileBrowseSession getLogFileBrowseSession()
	{
		return logFileBrowseSession;
	}
	public Vector getLogFiles()
	{
		return logFiles;
	}
	public void setLogFiles(Vector logFiles)
	{
		this.logFiles = logFiles;
	}
	public	int	getNumberOfLogFiles()
	{
		if (this.logFiles != null)
		{
			return	this.logFiles.size();
		}
		return	0;
	}
	public	String	getNiceName(int i)
	{
		if (this.niceNames != null && i<this.niceNames.size())
		{
			return	(String)this.niceNames.elementAt(i);
		}
		return	null;
	}
	public	String	getLogFileName(int i)
	{
		if (this.logFiles != null && i<this.logFiles.size())
		{
			return	(String)this.logFiles.elementAt(i);
		}
		return	null;
	}
	public	Long	getLogFileSize(int i)
	{
		if (this.logFileSizes != null && i<this.logFileSizes.size())
		{
			return	(Long)this.logFileSizes.elementAt(i);
		}
		return	null;
	}
	public	void	setLogFileBrowser(int fileIndex, String fileName, Long fileSize, int blockIndex)
	{
		if (this.logFileBrowseSession == null || !this.logFileBrowseSession.getLogFileName().equals(fileName))
		{
			//	First call in session. Create the browser, set block index and refresh.
			this.logFileBrowseSession = new LogFileBrowseSession(fileName,fileSize);
			this.logFileBrowseSession.setFileIndex(fileIndex);
			this.logFileBrowseSession.setNiceName(this.getNiceName(fileIndex));
			this.logFileBrowseSession.setCurrentBlockIndex(blockIndex);
			this.logFileBrowseSession.refresh();
		}
		else if (this.logFileBrowseSession.getCurrentBlockIndex() != blockIndex &&
				blockIndex < this.logFileBrowseSession.getNumberOfBlocks())
		{
			this.logFileBrowseSession.setCurrentBlockIndex(blockIndex);
			this.logFileBrowseSession.refresh();
		}
	}
}
