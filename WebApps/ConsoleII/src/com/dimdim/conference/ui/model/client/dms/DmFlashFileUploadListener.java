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
 * Copyright (c) 2007 Dimdim Inc. All Rights Reserved.                 *
 *                                                                        *
 *                                                                        *
 * This code is licensed under the DimDim License                         *
 * For details please visit http://www.dimdim.com/license                 *
 *                                                                        *
 **************************************************************************
 */

package com.dimdim.conference.ui.model.client.dms;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 *	
 * This interface is used by the progress check url reader to analyse
 * the buffer returned by the server. User needs to know if the returned
 * buffer indicates the action still in progress, completed or canceled
 * so that appropriate action on the client can be taken.
 * 
 * This interface only accepts the text buffer, which must be a valid
 * json buffer. 
 */

public interface DmFlashFileUploadListener
{
	public	void	fileUploadCompleted(String fileName);
	
	public	void	fileSelected(String fileName, int fileSize);
	
	public	void	uploadProgress(String fileName, int bytesUploaded);
}
