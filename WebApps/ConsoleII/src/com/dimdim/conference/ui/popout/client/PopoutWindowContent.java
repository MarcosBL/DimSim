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

package com.dimdim.conference.ui.popout.client;

import java.util.Vector;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * This interface is required because the popout content panels must be
 * ready to consume the initial data and events before the event reader
 * starts and the initial data is read.
 */

public interface PopoutWindowContent
{
	
	/**
	 * This method is required to create and layout all the visual content
	 * of the popout window, i.e. the panels. The client model is guarranteed
	 * to be created and ready before this method is called, so that the
	 * panels an widgets can reliably fetch the individual models and register
	 * respective listeners.
	 */
	public	void	preparePopoutWindowContent();
	
	/**
	 * As the name suggests this is the simple resize call. This avoids each
	 * specific popout module from having to register their own window resize
	 * handler.
	 * 
	 * @param width is the new width of the window.
	 * @param height is the new height of the window.
	 */
	public	void	resizePopoutWindowContent(int width, int height);
	
	/**
	 * This method provides the popout framework which of the features are used
	 * by the panels in this popout.
	 * 
	 * @return must contain string feature ids. e.g. 'feature.roster' for user
	 * roster data. It may be possible for a popout to be totally independent
	 * and not use any previous data. In such case this method may return null
	 * or an empty Vector.
	 */
	public	Vector	getRequiredFeatureIds();
	
	/**
	 * This method provides the data exported by the console panel to the same
	 * panel in the popout window to be imported. This is required so that the
	 * user can see the exact same state of the panel in the popout as in the
	 * console at the time of popout.
	 * 
	 * @param panelId identifies the panel within the console as well as the
	 * poput window.
	 * @param panelData the data string exported by the panel in console.
	 */
	public	void	initializePanelData(String panelId, String panelData);
	
}
