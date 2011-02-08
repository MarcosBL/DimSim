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

import	com.dimdim.conference.ui.json.client.UIStreamControlEvent;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */

public interface AVModelListener	extends	FeatureModelListener
{
	
	public	void	onStartVideo(String conferenceKey,
		String resourceId, String streamType, String streamName, String profile, String sizeFactor);
	
//	public	void	onStartVideo(UIStreamControlEvent event);
	
	public	void	onStopVideo(String conferenceKey,
			String resourceId, String streamType, String streamName);
	
	public	void	onStartAudio(UIStreamControlEvent event);
	
	public	void	onStopAudio(UIStreamControlEvent event);
	
}
