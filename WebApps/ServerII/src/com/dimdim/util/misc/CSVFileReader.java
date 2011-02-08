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

import	java.io.File;
import	java.io.RandomAccessFile;
import	java.util.Vector;
import	java.util.StringTokenizer;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 *
 */
public class CSVFileReader
{
	protected	File		file;
//	protected	String		fileName;
	protected	RandomAccessFile	raf;
	protected	String	seperator = ",";
	
	public	CSVFileReader(File file)	throws	Exception
	{
		this(file,",");
	}
	public	CSVFileReader(File file, String seperator)	throws	Exception
	{
		this.file = file;
		this.seperator = seperator;
		this.raf = new RandomAccessFile(file,"r");
	}
	public	CSVFileReader(String fileName)	throws	Exception
	{
		this(fileName,",");
	}
	public	CSVFileReader(String fileName, String seperator)	throws	Exception
	{
		this.file = new File(fileName);
		this.seperator = seperator;
		this.raf = new RandomAccessFile(file,"r");
	}
	public	void	reset()	throws	Exception
	{
		this.raf = new RandomAccessFile(file,"r");
	}
	public	Vector	getNextLine()
	{
		Vector v = null;
		if (this.raf != null)
		{
			try
			{
				String line = raf.readLine();
				if (line != null)
				{
					v = this.parseCSVLine(line);
				}
				else
				{
					this.raf = null;
				}
			}
			catch(Exception e)
			{
				try
				{
					raf.close();
				}
				catch(Exception e2)
				{
					
				}
				raf = null;
				this.file = null;
			}
		}
		
		return	v;
	}
	public	void	close()
	{
		if (this.raf != null)
		{
			try
			{
				this.raf.close();
				this.raf = null;
				this.file = null;
			}
			catch(Exception e)
			{
				this.raf = null;
				this.file = null;
			}
		}
	}
	protected	Vector	parseCSVLine(String line)
	{
		Vector v = new Vector();
		StringTokenizer parser = new StringTokenizer(line,this.seperator);
		while (parser.hasMoreTokens())
		{
			v.add(parser.nextToken());
		}
		return	v;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		try
		{
			CSVFileReader reader = new CSVFileReader("d:\\temp\\test1.csv");
			Vector v = null;
			while ((v=reader.getNextLine()) != null)
			{
				int	size = v.size();
				for (int i=0; i<size; i++)
				{
					System.out.print("-"+v.elementAt(i)+"-");
				}
				System.out.println("");
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

}
