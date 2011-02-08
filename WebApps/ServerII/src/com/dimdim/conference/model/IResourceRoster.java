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
 *	File Name  : IRoster.java
 *  Created On : Apr 11, 2006
 *  Created By : Saurav Mohapatra
 **************************************************************************
*/

package com.dimdim.conference.model;

import java.util.List;

/**
 * @author Saurav Mohapatra
 * @email  Saurav.Mohapatra@communiva.com
 */
public	interface	IResourceRoster
{
	
	public	void	setClientEventPublisher(IClientEventPublisher cep);
	
	public	IResourceObject	getActiveResourceObject();
	public	void			setActiveResourceObject(IResourceObject ro);
	
	public	IResourceObject	getResourceObject(String resourceId);
	public	IResourceObject	removeResourceObject(String resourceId);
	
	public  IResourceObject	addResourceObject(String name, String type,
				String ownerId, String mediaId, String appHandle, int slideCount, int width, int height);
	
	public  IResourceObject	addResourceObject(String name, String type,
			String ownerId, String mediaId, String appHandle, int slideCount, int width, int height, String resId);
	
	public  IResourceObject	addResourceObject(String name, String type,
			String ownerId, String mediaId, String resId, String appHandle, int slideCount, int width, int height);
	
	public  IResourceObject	updateResourceObject(String resourceId, String name,
				String type, String mediaId, String appHandle, int slideCount);
	
	public  IResourceObject	renameResourceObject(String resourceId, String name);
	
	public	List		getResources();
	
	public	int		getNumberOfResources();
	
	public	IResourceObject	getWhiteboardResource();
	
}
