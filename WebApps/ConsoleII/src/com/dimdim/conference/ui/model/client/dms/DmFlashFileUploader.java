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

package com.dimdim.conference.ui.model.client.dms;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.Element;

import com.dimdim.conference.ui.model.client.JSCallbackListener;
import com.dimdim.conference.ui.model.client.JSInterface;

import com.dimdim.conference.ui.model.client.dms.PPTConversionProgressChecker;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * This is a wrapper widget on the swfupload. This is seperate from the movie
 * widget that uses the swfobject because the upload movie has much more
 * extensive javascript interface. It may also alway require an independet
 * page of its own.
 * 
 * The upload movie does not contain any visual elements. Primary function of
 * this widget is to process and manage the javascript callbacks from the movie.
 * This panel will show the user the selected files, upload progress numbers
 * and the final result.
 * 
 * This widget must always be loaded in its own frame because of the heavy use
 * of external interface. We have seen a few problems earlier because of hide
 * and show or add and remove of widgets stopping the external interface from
 * working.
 */

public class DmFlashFileUploader	implements	JSCallbackListener
{
	protected	String	name	=	"FILE_UPLOADER";
	
	protected	DmFlashFileUploadListener	fileUploadListener;
	
	public DmFlashFileUploader(DmFlashFileUploadListener fileUploadListener)
	{
		this.fileUploadListener = fileUploadListener;
		
		JSInterface.addCallbackListener(this);
	}
	public String getListenerName()
	{
		return this.name;
	}
	public void handleCallFromJS(String data)
	{
//		Window.alert("CallFromJS:"+data);
	}
	public void handleCallFromJS2(String data1, String data2)
	{
		if (data1 != null && this.fileUploadListener != null)
		{
			if (data1.equals("FILE_COMPLETED"))
			{
				this.fileUploadListener.fileUploadCompleted(data2);
			}
		}
//		Window.alert("CallFromJS:"+data1+"-"+data2);
	}
	public void handleCallFromJS3(String data1, String data2, String data3)
	{
		if (data1 != null && this.fileUploadListener != null)
		{
			if (data1.equals("QUEUED"))
			{
				this.fileUploadListener.fileSelected(data2,(new Integer(data3)).intValue());
			}
			else if (data1.equals("PROGRESS"))
			{
				this.fileUploadListener.uploadProgress(data2,(new Integer(data3)).intValue());
			}
		}
//		Window.alert("CallFromJS:"+data1+"-"+data2+"-"+data3);
	}
}
