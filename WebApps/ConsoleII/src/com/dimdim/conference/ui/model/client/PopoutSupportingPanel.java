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

package com.dimdim.conference.ui.model.client;

import com.dimdim.conference.ui.json.client.UIPopoutPanelData;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */

public interface PopoutSupportingPanel
{
	/**
	 * This method must return a unique id that can be used to identify the
	 * panel in console as well as in the popout window.
	 * 
	 * @return the panel identifier.
	 */
	public	String	getPanelId();
	
	/**
	 * This method must return a data string that contains complete current
	 * state of the panel. This string is always produced by the panel in
	 * console and consumed by the readPanelData method on the same panel in
	 * the popout window.
	 * 
	 * @return panel state data.
	 */
	public	String	getPanelData();
	
	/**
	 * This method must initialize the panel in popout window using the data
	 * produced by the getPanelData method.
	 * 
	 * @param dataText is the string produced by the getPanelData method.
	 */
	public	void	readPanelData(String dataText);
	
	/**
	 * This method is called by the console on the panel when the panel is
	 * popped out. The panel is expected to replace its content with something
	 * neutral or a message that informs the user of the state of the panel.
	 */
//	public	void	panelPopedOut();
	
	/**
	 * This method is called by the console on the panel when the panel is
	 * popped back into the console. The panel is expected to show the current
	 * content to the user.
	 */
//	public	void	panelPoppedIn();
	
	/**
	 * The panel when is popped out may have to report the activity in the
	 * popout to the main console. e.g. chat messages sent from the popout
	 * chat box must be reported to the console panel so that when the panel
	 * is popped back in the chat messages will be available. Also there
	 * may be other actions to be taken on the same or other user actions.
	 * It is hence preferable to channel all meeting activity through the
	 * single console window.
	 */
	public	void	setPopoutPanelProxy(PopoutPanelProxy popoutPanelProxy);
	
	/**
	 * Tell the panel where it is located, because only the top level entry
	 * point would know if it is the console or a popout window. These methods
	 * are called by the entry point as soon as the panel is created and passed
	 * to it.
	 */
	public	void	panelInConsole();
	
	public	void	panelInPopout();
	
	/**
	 * Simple location enquiry. This will be required by other panels which use
	 * this panel.
	 */
	public	boolean	isInConsole();
	
	public	boolean	isInPopout();
	
	/**
	 * This method is used to communicate activity in the popout to the panel
	 * in console. intepretation and use of the data in the panel is upto each
	 * individual panel.
	 * 
	 * @param msg
	 */
	public	void	receiveMessageFromPopout(UIPopoutPanelData msg);
}
