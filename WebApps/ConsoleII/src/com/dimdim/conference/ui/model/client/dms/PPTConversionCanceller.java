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
public class PPTConversionCanceller implements ProgressCheckListener
{
	protected	String	dmsUrl;
	protected	String	pptId;
	protected   String  meetingID;
	protected	String	docType;
	protected	ProgressCheckUrlReader	checkUrlReader;
	
	public	PPTConversionCanceller(String dmsUrl, String pptId,String meetingID,String docType)
	{
		this.dmsUrl = dmsUrl;
		this.pptId = pptId;
		this.meetingID = meetingID;
		this.docType = docType;
		
		String checkUrl = this.dmsUrl+"/cancelDocumentConversion2?docID="+this.pptId+"&meetingID="+this.meetingID+"&docType="+this.docType;
		this.checkUrlReader = new ProgressCheckUrlReader(checkUrl, 10, this);
	}
	public	void	cancelConversion()
	{
		this.checkUrlReader.startCheck();
	}
	public ProgressCheckResponse analyzeResponseText(String buffer)
	{
		return	null;
	}
	public	void	processResponse(ProgressCheckResponse resp)
	{
		
	}
	public void onInterfaceResponse(String message)
	{
//		this.progressCheckListener.onInterfaceResponse(message);
	}
//	public String getUserMessage()
//	{
//		return null;
//	}
//	public boolean isActionCancelled()
//	{
//		return false;
//	}
//	public boolean isActionComplete()
//	{
//		return true;
//	}
//	public void showUserMessage()
//	{
//		//	not required yet.
//	}
}
