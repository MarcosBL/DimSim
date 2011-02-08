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

package com.dimdim.conference.action.admin;

import java.util.Vector;
import java.io.*;

import com.dimdim.conference.ConferenceConsoleConstants;
import com.dimdim.conference.application.session.AdminSession;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */
public class BrowseLogFileAction	extends	AdminAction
{
	protected	int		fileIndex;
	protected	int		blockIndex;
	
	//	This parameter helps the action browse the file by blocks of lines.
	protected	String	cmd;	//	"first", "next", "previous", "last"
	
	public	BrowseLogFileAction()
	{
	}
	public	String	execute()	throws	Exception
	{
		String ret = LOGIN;
		this.adminSession = (AdminSession)this.session.get(ConferenceConsoleConstants.ACTIVE_ADMIN_SESSION);
		if (this.adminSession != null)
		{
			ret = SUCCESS;
			String fileName = adminSession.getLogFileName(fileIndex);
			Long fileSize = adminSession.getLogFileSize(fileIndex);
			
			if (fileName != null && fileSize != null)
			{
				adminSession.setLogFileBrowser(fileIndex,fileName,fileSize,blockIndex);
			}
		}
		return	ret;
	}
	public String getCmd()
	{
		return cmd;
	}
	public void setCmd(String cmd)
	{
		this.cmd = cmd;
	}
	public int getFileIndex()
	{
		return fileIndex;
	}
	public void setFileIndex(int fileIndex)
	{
		this.fileIndex = fileIndex;
	}
	public int getBlockIndex()
	{
		return blockIndex;
	}
	public void setBlockIndex(int blockIndex)
	{
		this.blockIndex = blockIndex;
	}
}
