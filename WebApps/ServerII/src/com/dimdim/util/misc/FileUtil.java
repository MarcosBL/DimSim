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

package com.dimdim.util.misc;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import	java.util.ResourceBundle;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */

public class FileUtil
{
	public	FileUtil()
	{
		
	}
	public	String	readFileContentsAsString(String fileName)	throws	Exception
	{
		return	this.readFileContentsAsString(new File(fileName));
	}
	public	String	readFileContentsAsString(File file)	throws	Exception
	{
		String str = null;
		try
		{
			StringBuffer buf = new StringBuffer();
			FileInputStream fis = new FileInputStream(file);
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
			str = buf.toString();
		}
		catch(Exception e)
		{
//			e.printStackTrace();
		}
//		System.out.println("Read file:"+file.getAbsolutePath()+", to:"+str);
		return	str;
	}
	public	void	writeStringToFile(String fileName, String str, boolean append)	throws	Exception
	{
		this.writeStringToFile(new File(fileName), str, append);
	}
	public	void	writeStringToFile(File file, String str, boolean append)	throws	Exception
	{
		FileOutputStream fos = new FileOutputStream(file, append);
		BufferedOutputStream bos = new BufferedOutputStream(fos);
		
		bos.write(str.getBytes());
		bos.flush();
		
		try
		{
			bos.close(); fos.close();
		}
		catch(Exception e)
		{
		}
	}
	public	ResourceBundle	readFileAdResourceBundle(String fileName)	throws	Exception
	{
		return	this.readFileAdResourceBundle(new File(fileName));
	}
	public	ResourceBundle	readFileAdResourceBundle(File file)	throws	Exception
	{
		return	null;
	}
	public	void	copyFile(File source, File target)
	{
		try
		{
			FileInputStream fis = new FileInputStream(source);
			BufferedInputStream bis = new BufferedInputStream(fis);
			
			FileOutputStream fos = new FileOutputStream(target, false);
			BufferedOutputStream bos = new BufferedOutputStream(fos);
			
			byte[] byteBuf = new byte[256];
			int len=0;
			while((len=bis.read(byteBuf,0,256)) > 0)
			{
				bos.write(byteBuf,0,len);
				bos.flush();
			}
			
			try
			{
				bis.close(); fis.close();
			}
			catch(Exception e2)
			{
				
			}
			try
			{
				bos.close(); fos.close();
			}
			catch(Exception e3)
			{
				
			}
		}
		catch(Exception e)
		{
//			e.printStackTrace();
		}
	}
}
