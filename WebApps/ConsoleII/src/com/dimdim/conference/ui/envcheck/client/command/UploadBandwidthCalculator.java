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

package com.dimdim.conference.ui.envcheck.client.command;
/*
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ResponseTextHandler;
import com.dimdim.conference.ui.json.client.JSONurlReader;
*/
/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */

public class UploadBandwidthCalculator //implements ResponseTextHandler
{
	/*
	private	static final String	FRAGMENT = "abcdefdhijklmnopabcdefdhijklmnopabcdefdhijklmnopabcdefdhijklmnop";
	
	protected	BandwidthCheck		bandwidthCheck;
	protected	JSONurlReader		urlReader;
	protected	long		startTime;
	
	protected	int		kbs;
	protected	String	testData;
	
	protected	double	rate1 = -1;
	protected	double	rate2 = -1;
	protected	double	rate3 = -1;
	protected	double	uploadBandwidth = 0;
	
	public	UploadBandwidthCalculator(BandwidthCheck bandwidthCheck)
	{
		this.bandwidthCheck = bandwidthCheck;
	}
	public	void	calculateUploadBandwidth(int kbs)
	{
		this.kbs = kbs;
		StringBuffer buf = new StringBuffer("dataBuffer=");
		//	1 KB is 16 fragments of the 64 characters string.
		int num = kbs*16;
		for (int i=0; i<num; i++)
		{
			buf.append(FRAGMENT);
		}
		String url = "TestUploadBandwidth.action?size="+kbs;
		urlReader = new JSONurlReader(url,this.bandwidthCheck.getConfKey(),this);
		testData = buf.toString();
		
		this.startTime = System.currentTimeMillis();
		urlReader.doPostURL(testData);
	}
	public void onCompletion(String text)
	{
		long endTime = System.currentTimeMillis();
//		Window.alert("Start Time:"+startTime+", End Time:"+endTime+", Elpased Time:"+(endTime-startTime));
//		if (text != null && text.length() > 0)
//		{
//			Window.alert("Received String Size:"+text.length());
			double db = (kbs*16*64*8)/(endTime-startTime);
			if (rate1 == -1)
			{
				this.rate1 = db;
				this.startTime = System.currentTimeMillis();
				this.urlReader.doPostURL(testData);
			}
			else if (this.rate2 == -1)
			{
				this.rate2 = db;
				this.startTime = System.currentTimeMillis();
				this.urlReader.doPostURL(testData);
			}
			else if (this.rate3 == -1)
			{
				this.rate3 = db;
				calculateAndReportUploadBandwidth();
			}
			else
			{
				calculateAndReportUploadBandwidth();
			}
//			Window.alert("Upload Bandwidth:"+db+" kbps");
//		}
	}
	private	void	calculateAndReportUploadBandwidth()
	{
		this.uploadBandwidth = (rate1+rate2+rate3)/3;
		this.bandwidthCheck.setUploadBandwidth(this.uploadBandwidth);
	}
	*/
}
