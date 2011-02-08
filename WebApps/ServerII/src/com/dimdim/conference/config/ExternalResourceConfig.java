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
 *								                                          *
 * Copyright (c) 2006 Communiva Inc. All Rights Reserved.	              *
 *								                                          *
 *								                                          *
 * This code is licensed under the DimDim License                         *
 * For details please visit http://www.dimdim.com/license		          *
 *									                                      *
 **************************************************************************
 */
/*
 **************************************************************************
 *	File Name  : ConferenceExternalResourceConfig.java
 *  Created On : Apr 11, 2006
 *  Created By : Saurav Mohapatra
 **************************************************************************
*/
 
package com.dimdim.conference.config;

import java.util.HashMap;
/**
 * @author Saurav Mohapatra
 * @email  Saurav.Mohapatra@communiva.com
 */
public class ExternalResourceConfig
{

	private String name = null;
	private String host = "";
	private int    port = 0;
	private String path = "";
	private HashMap resourceProps = new HashMap();
	public ExternalResourceConfig()
	{
		super();
	}
	/**
	 * @return Returns the host.
	 */
	public String getHost()
	{
		return host;
	}
	/**
	 * @param host The host to set.
	 */
	public void setHost(String host)
	{
		this.host = host;
	}
	/**
	 * @return Returns the name.
	 */
	public String getName()
	{
		return name;
	}
	/**
	 * @param name The name to set.
	 */
	public void setName(String name)
	{
		this.name = name;
	}
	/**
	 * @return Returns the path.
	 */
	public String getPath()
	{
		return path;
	}
	/**
	 * @param path The path to set.
	 */
	public void setPath(String path)
	{
		this.path = path;
	}
	/**
	 * @return Returns the port.
	 */
	public int getPort()
	{
		return port;
	}
	/**
	 * @param port The port to set.
	 */
	public void setPort(int port)
	{
		this.port = port;
	}
	/**
	 * @return Returns the resourceProps.
	 */
	public HashMap getResourceProps()
	{
		return resourceProps;
	}
	/**
	 * @param resourceProps The resourceProps to set.
	 */
	public void setResourceProps(HashMap resourceProps)
	{
		this.resourceProps = resourceProps;
	}
	
	public void setResourceProp(String key, String val)
	{
		resourceProps.put(key,val);
	}
	public String getResourceProp(String key)
	{
		return (String)resourceProps.get(key);
	}

}
