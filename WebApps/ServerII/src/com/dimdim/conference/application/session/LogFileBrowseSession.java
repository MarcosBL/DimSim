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

import	java.io.*;

import	com.dimdim.conference.ConferenceConsoleConstants;
import com.dimdim.util.misc.StringGenerator;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * This browser simply reads the given file in chunks of 20kb.
 */
public class LogFileBrowseSession
{
	private	static	StringGenerator	idGen = new StringGenerator();
	private	static	String	tmpFileName1 = null;
	
	protected	String	logFileName;
	protected	String	niceName;
	protected	int		fileIndex;
	protected	long	fileSize;
	protected	long	readBlockSize = 10 * 1024;
	protected	long	currentBlockIndex = 0;
	protected	long	numberOfBlocks;
	protected	String	currentBuffer = "";
	protected	String	tmpTxtFileName = null;
	
	public	LogFileBrowseSession(String fileName, Long fileSize)
	{
		this.logFileName = fileName;
		this.fileSize = fileSize.longValue();
		this.numberOfBlocks = this.fileSize/this.readBlockSize +1;
	}
	public	void	refresh()
	{
		try
		{
			if (LogFileBrowseSession.tmpFileName1 != null)
			{
				File f = new File(LogFileBrowseSession.tmpFileName1);
				f.delete();
			}
		}
		catch(Exception e)
		{
			
		}
		try
		{
			File f = new File(logFileName);
			long currentStartPosition = this.currentBlockIndex * this.readBlockSize;
//			System.out.println("file length:"+f.length());
//			System.out.println("current start position:"+currentStartPosition);
			if (f.length() > currentStartPosition)
			{
				LogFileBrowseSession.tmpFileName1 = LogFileBrowseSession.idGen.generateRandomString(9,9);
				LogFileBrowseSession.tmpFileName1 += ".txt";
				
				String tName = ConferenceConsoleConstants.getWebappLocalPath()+tmpFileName1;
//				System.out.println("Writing tmp file:"+tmpFileName);
				this.tmpTxtFileName = "/"+ConferenceConsoleConstants.getWebappName()+"/"+tmpFileName1;
				LogFileBrowseSession.tmpFileName1 = tName;
				
				//	Just in case the file was truncated for some reason, somehow.
				StringBuffer buf = new StringBuffer();
				RandomAccessFile raf = new RandomAccessFile(f, "r");
				RandomAccessFile raf2 = new RandomAccessFile(tName,"rw");
//				String header = "<%response.addHeader(\"Pragma\",\"no-cache\");"+ 
//					"response.addHeader(\"Expires\",\"-1\");";
//					"response.addDateHeader(\"Last-Modified\",(new Date()).getTime());%>";
				raf2.seek(0);
//				raf2.write(header.getBytes());
				raf.seek(currentStartPosition);
				//	Read 10 1 k blocks.
				byte[] byteBuf = new byte[1024];
				for (int i=0; i<10; i++)
				{
					int len=raf.read(byteBuf,0,1024);
					if (len > 0)
					{
						String s = new String(byteBuf,0,len);
						raf2.write(byteBuf,0,len);
//						System.out.println("Read:"+s);
						buf.append(s);
					}
					else
					{
						break;
					}
				}
				try
				{
					raf.close(); raf2.close();
				}
				catch(Exception e2)
				{
					
				}
				this.currentBuffer = buf.toString();
			}
			else
			{
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	public String getNiceName()
	{
		return niceName;
	}
	public void setNiceName(String niceName)
	{
		this.niceName = niceName;
	}
	public int getFileIndex()
	{
		return fileIndex;
	}
	public void setFileIndex(int fileIndex)
	{
		this.fileIndex = fileIndex;
	}
	public long getCurrentBlockIndex()
	{
		return currentBlockIndex;
	}
	public void setCurrentBlockIndex(long currentBlockIndex)
	{
		this.currentBlockIndex = currentBlockIndex;
	}
	public String getCurrentBuffer()
	{
		return currentBuffer;
	}
	public void setCurrentBuffer(String currentBuffer)
	{
		this.currentBuffer = currentBuffer;
	}
	public long getFileSize()
	{
		return fileSize;
	}
	public void setFileSize(long fileSize)
	{
		this.fileSize = fileSize;
	}
	public String getLogFileName()
	{
		return logFileName;
	}
	public void setLogFileName(String logFileName)
	{
		this.logFileName = logFileName;
	}
	public long getNumberOfBlocks()
	{
		return numberOfBlocks;
	}
	public void setNumberOfBlocks(long numberOfBlocks)
	{
		this.numberOfBlocks = numberOfBlocks;
	}
	public long getReadBlockSize()
	{
		return readBlockSize;
	}
	public void setReadBlockSize(long readBlockSize)
	{
		this.readBlockSize = readBlockSize;
	}
	public String getTmpTxtFileName()
	{
		return tmpTxtFileName;
	}
	public void setTmpTxtFileName(String tmpTxtFileName)
	{
		this.tmpTxtFileName = tmpTxtFileName;
	}
}
