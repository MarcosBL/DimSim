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

package com.dimdim.util.resources;

import	java.io.File;
import	java.util.HashMap;
import	java.util.ResourceBundle;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * This class is required because the java resource bundle does not detect
 * online changes to the base resource file. This class keeps track of the
 * update timestamp of the resource files being read.
 */

public class ResourceFileReader
{
	protected	static	ResourceFileReader	reader;
	
	protected	static	void	initializeResourceFileReader(String rootDirectory)
	{
		ResourceFileReader.reader = new ResourceFileReader(rootDirectory);
	}
	protected	static	ResourceFileReader	createReader()
	{
		return	ResourceFileReader.reader;
	}
	
	protected	String	rootDirectory;
	protected	HashMap	resourceFiles = new HashMap();
	
	private	ResourceFileReader(String rootDirectory)
	{
		this.rootDirectory = rootDirectory;
	}
	
	public	void	addResourceFile(String name, String fileName)
	{
		File fullFilePath = (new File(this.rootDirectory,fileName));
		this.resourceFiles.put(name, new ResourceFile(fullFilePath.getAbsolutePath()));
	}
	public	HashMap	getResources(String name)
	{
		ResourceFile	rf = (ResourceFile)this.resourceFiles.get(name);
		rf.checkAndReadUpdate();
		return	rf.getResources();
	}
}
