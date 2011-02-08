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

import com.google.gwt.user.client.Window;
import	com.dimdim.conference.ui.model.client.helper.ProgressCheckUrlReader;
import	com.dimdim.conference.ui.model.client.helper.ProgressCheckListener;
import	com.dimdim.conference.ui.model.client.helper.ProgressCheckResponse;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 *
 */
public class PPTConversionProgressChecker implements ProgressCheckListener
{
	protected	String	dmsUrl;
	protected	String	meetingId;
	protected	String	pptId;
//	protected	boolean	checkStopped = false;
	
//	protected	String	lastCheckResponse = "";
	
	protected	ProgressCheckUrlReader	checkUrlReader;
	protected	PPTConversionProgressListener	progressListener;
	
	public	PPTConversionProgressChecker(String dmsUrl,
				String meetingId, String pptId, int intervalMS, PPTConversionProgressListener progressListener)
	{
		this.dmsUrl = dmsUrl;
		this.meetingId = meetingId;
		this.pptId = pptId;
		this.progressListener = progressListener;
		
		//Window.alert("inside PPTConversionProgressChecker pptId = "+pptId);
		String checkUrl = this.dmsUrl+"/getDocumentStatus2?docID="+this.pptId+"&meetingID="+this.meetingId;
		this.checkUrlReader = new ProgressCheckUrlReader(checkUrl, intervalMS, this);
	}
	public	void	startCheck()
	{
//		this.checkStopped = false;
		this.checkUrlReader.startCheck();
	}
	public	void	stopCheck()
	{
//		this.checkStopped = true;
		this.checkUrlReader.stopCheck();
	}
	public ProgressCheckResponse analyzeResponseText(String buffer)
	{
//		this.lastCheckResponse = buffer;
//		Window.alert("Progress check return:"+buffer);
		ProgressCheckResponse ret = this.progressListener.conversionCheckReturn(buffer);
		
		return ret;
	}
	public	void	processResponse(ProgressCheckResponse resp)
	{
		if (resp.isActionComplete())
		{
			this.progressListener.conversionComplete(resp);
		}
		else if (resp.isActionCancelled())
		{
			this.progressListener.conversionCancelled();
		}
	}
	public void onInterfaceResponse(String message)
	{
//		this.progressCheckListener.onInterfaceResponse(message);
	}
//	public String getUserMessage()
//	{
//		return this.lastCheckResponse;
//	}
//	public boolean isActionCancelled()
//	{
//		return this.lastCheckResponse.indexOf("conversionCancelled") > 0 || this.checkStopped;
//	}
//	public boolean isActionComplete()
//	{
//		return this.lastCheckResponse.indexOf("conversionComplete:\"True\"") > 0;
//	}
//	public void showUserMessage()
//	{
//		//	not required yet.
//	}
}
