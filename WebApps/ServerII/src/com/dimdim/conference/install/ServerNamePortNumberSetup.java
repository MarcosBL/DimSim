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

package com.dimdim.conference.install;

import java.io.File;
import	java.util.ResourceBundle;

import com.dimdim.util.misc.FileUtil;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * This utility will be run outside the conference server context, so it
 * 
 */

public class ServerNamePortNumberSetup
{
	protected	static	final	String	ServerAddress = "_ServerAddress";
	protected	static	final	String	ServerPortNumber = "_ServerPortNumber";
	
//	protected	ResourceBundle	rb;
	
	protected	String	webappHome;
	protected	String	serverAddress;
	protected	String	serverPortNumber;
	
	public ServerNamePortNumberSetup(String webappHome, String serverAddress, String serverPortNumber)
	{
		this.webappHome = webappHome;
		this.serverAddress = serverAddress;
		this.serverPortNumber = serverPortNumber;
	}
	/*
	public	ServerNamePortNumberSetup()	throws	Exception
	{
		this.rb = ResourceBundle.getBundle("resources.DimDimConference");
		
		this.webappHome = this.rb.getString("dimdim.webappLocalPath");
		this.serverAddress = this.rb.getString("dimdim.serverAddress");
		this.serverPortNumber = this.rb.getString("dimdim.serverPortNumber");
	}
	*/
	public	void	doWork()	throws	Exception
	{
		File webappHomeDir = new File(webappHome);
		File htmlDir = new File(webappHomeDir, "html");
		
		File welcomePage = new File(webappHomeDir, "WelcomePage.html" );
		File createConfPage = new File(htmlDir, "createConf.html" );
		File joinConfPage = new File(htmlDir, "joinConf.html" );
		File startConfPage = new File(htmlDir, "startConf.html" );
		File meetNowPage = new File(htmlDir, "meetNow.html" );
		
		this.rewriteFile(welcomePage);
		this.rewriteFile(createConfPage);
		this.rewriteFile(joinConfPage);
		this.rewriteFile(startConfPage);
		this.rewriteFile(meetNowPage);
	}
	protected	void	rewriteFile(File file)	throws	Exception
	{
		FileUtil fileUtil = new FileUtil();
		String str = fileUtil.readFileContentsAsString(file);
		
		System.out.println("Replacing:"+ServerNamePortNumberSetup.ServerAddress+",with:"+this.serverAddress);
		String s2 = str.replaceAll(ServerNamePortNumberSetup.ServerAddress,this.serverAddress);
		System.out.println("Replacing:"+ServerNamePortNumberSetup.ServerPortNumber+",with:"+this.serverPortNumber);
		String s3 = s2.replaceAll(ServerNamePortNumberSetup.ServerPortNumber,this.serverPortNumber);
		
		fileUtil.writeStringToFile(file, s3, false);
	}
}
