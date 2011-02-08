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
 *	File Name  : ConferenceFeatureConfig.java
 *  Created On : Apr 11, 2006
 *  Created By : Saurav Mohapatra
 **************************************************************************
*/
 
package com.dimdim.conference.config;

import java.util.HashMap;

/**
 * @author Saurav Mohapatra
 * @email  Saurav.Mohapatra@communiva.com
 * ************** MAY NOT BE REQUIRED *******
 */
public class FeatureConfig
{

	private String name; 
	private boolean hasSingletonFeatureObject = false;
	private HashMap featureProps = new HashMap();
	/**
	 * 
	 */
	public FeatureConfig()
	{
		super();
		// TODO Auto-generated constructor stub
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
	 * @return Returns the hasSingletonFeatureObject.
	 */
	public boolean getHasSingletonFeatureObject()
	{
		return hasSingletonFeatureObject;
	}
	/**
	 * @param hasSingletonFeatureObject The hasSingletonFeatureObject to set.
	 */
	public void setHasSingletonFeatureObject(boolean hasSingletonFeatureObject)
	{
		this.hasSingletonFeatureObject = hasSingletonFeatureObject;
	}
	/**
	 * @return Returns the featureProps.
	 */
	public HashMap getFeatureProps()
	{
		return featureProps;
	}
	/**
	 * @param featureProps The featureProps to set.
	 */
	public void setFeatureProps(HashMap featureProps)
	{
		this.featureProps = featureProps;
	}
	
	public void setFeatureProp(String key, String val)
	{
		featureProps.put(key,val);
	}
	public String getFeatureProp(String key)
	{
		return (String)featureProps.get(key);
	}
	

}
