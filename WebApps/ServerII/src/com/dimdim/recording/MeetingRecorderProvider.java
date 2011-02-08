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

package com.dimdim.recording;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */

public interface MeetingRecorderProvider
{
	
	public	void	initialiseProvider(String installationId, String installationPrefix,
			String templatesRootDirectory, String storageRootDirectory, String recordingServerAddress,
			String portalServerAddress, String portalCallbackAction, String otherCallBackUrl)	throws	Exception;
	
	public	MeetingRecorder		getMeetingRecorder(String dimdimId, String roomId,
			String confKey, String meetingId, String avApplicationStreamsDirectory,
			String locale)	throws	Exception;
	
	public	void				meetingClosed(String dimdimId, String roomId,
			String confKey, String meetingId);
}
