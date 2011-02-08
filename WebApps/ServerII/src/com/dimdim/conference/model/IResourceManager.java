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

package com.dimdim.conference.model;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 *
 */
public interface IResourceManager
{
	
	public	IResourceRoster		getResourceRoster();
	
//	public  void	addResourceEventListener(IResourceEventListener listener);
//	public  void	removeResourceEventListener(IResourceEventListener listener);
	
	/**
	 * Following methods provide the action classes more direct interface to
	 * handle the controls for various resources. These are clearly seperated
	 * for specific types of resources. The resources under control at present
	 * are powerpoint presentations and desktop and application window streams.
	 */
	
	public	void	handlePresentationControlMessage(IConferenceParticipant presenter,
			String resourceId, String controlAction, Integer slide, String userId);
	
	public	void	handleScreenStreamControlMessage(IConferenceParticipant presenter,
			String resourceId, String mediaId, String controlAction,
			String presenterId, String presenterPassKey,
			String	appName, String appHandle);
	
	public	void	handleWhiteboardControlMessage(IConferenceParticipant presenter,
			String command);
	
	public	void	handleCobrowseControlMessage(IConferenceParticipant presenter,
			String command, String resourceId, String horScroll, String verScroll, String newName);
	
}
