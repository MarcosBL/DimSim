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

package com.dimdim.conference.ui.sharing.client;

import	com.dimdim.conference.ui.json.client.UIResourceObject;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */

public interface CollaborationWidget
{
	/**
	 * This reference allows the widget access to the container. The container
	 * hence will be responsible for providing any resources and properties
	 * which may be required by this widget to work.
	 * 
	 * @param container
	 */
	public	void	setContainer(CollaborationWidgetContainer container);
	
	/**
	 * The container must guarrantee that the refresh will be called only when
	 * widget is current and active. Attempt to do any layout, javascript or
	 * html / dhtml work while the widget is hidden may be undefined in some
	 * browser. This is a reasonable safety precaution.
	 * 
	 * This method is called when the shared resource changes. Even though
	 * for the overall display there will be only one active shared resource
	 * at a time, each collaboration widget is specific to at least one type
	 * of resource. It will be efficient for each widget to remember the
	 * previously shared resource of its type so that the switchover is as
	 * fast as possible. However this method will always refresh the widget
	 * is called. It is the caller's, i.e. the collaboration area manager,
	 * responsibility to decide whether a forced refresh is required or not.
	 */
	public	void	refreshWidget(UIResourceObject resource,int newWidth, int newHeight);
	
	/**
	 * Each widget will be required to keep track of the previous used resource
	 * so as to make refresh fast and effecient. However whether a forced
	 * refresh is required or not depends on many factors. Hen
	 */
	public	UIResourceObject	getCurrentResource();
	
	/**
	 * This method is called by the container before closing or removing the
	 * widget from the container. Deactivating essentially means that widget
	 * is being removed. The widget must be activated again in order to display
	 * again.
	 */
//	public	void	widgetDeactivating();
	
	/**
	 * This method is called by the container before hiding the widget. This
	 * means that the sharing is switching between two resources.
	 */
	public	void	widgetHiding();
	
	/**
	 * This method is called by the container when the widget is redisplayed
	 * without any change in the resource being displayed. 
	 */
//	public	void	widgetShowing();
}

