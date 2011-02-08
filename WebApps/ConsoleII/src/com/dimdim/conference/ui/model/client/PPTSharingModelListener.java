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

import com.dimdim.conference.ui.json.client.UIPresentationControlEvent;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * This interface must be implemented by the presentation player component
 * which will change the state of the ui presentation on events raised by
 * the active presenter.
 */
public interface PPTSharingModelListener	extends	FeatureModelListener
{
	/**
	 * This event is raised whenever the presenter starts a new presentation.
	 */
	public	void	startPresentation(UIPresentationControlEvent event);
	
	/**
	 * This event is raised whenever the presenter stops the presentation.
	 */
	public	void	stopPresentation(UIPresentationControlEvent event);
	
	/**
	 * This event is raied when the current slide is changed. It could be a
	 * next, previous navigation or a jump to any particulat slide.
	 */
	public	void	slideChanged(UIPresentationControlEvent event);
	
	/**
	 * This event is raied when a user, presenter or attendee, enables
	 * annotations on their ppt broadcaster or viewer, resp.
	 */
	public	void	annotationsEnabled(UIPresentationControlEvent event);
	
	/**
	 * This event is raied when a user, presenter or attendee, disables
	 * annotations on their ppt broadcaster or viewer, resp.
	 */
	public	void	annotationsDisabled(UIPresentationControlEvent event);
	
}
