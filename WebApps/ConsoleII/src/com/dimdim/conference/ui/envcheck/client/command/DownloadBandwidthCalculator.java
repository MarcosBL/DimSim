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

public class DownloadBandwidthCalculator //implements ResponseTextHandler
{
	/*
	protected	BandwidthCheck		bandwidthCheck;
	protected	JSONurlReader		urlReader;
	protected	long		startTime;
	protected	int		kbs;
	
	protected	double	rate1 = -1;
	protected	double	rate2 = -1;
	protected	double	rate3 = -1;
	protected	double	downloadBandwidth = 0;
	
	public	DownloadBandwidthCalculator(BandwidthCheck bandwidthCheck)
	{
		this.bandwidthCheck = bandwidthCheck;
	}
	public	void	calculateDownloadBandwidth(int kbs)
	{
		this.kbs = kbs;
		String url = "TestDownloadBandwidth.action?size="+kbs;
		urlReader = new JSONurlReader(url,this.bandwidthCheck.getConfKey(),this);
		this.startTime = System.currentTimeMillis();
		urlReader.doReadURL();
	}
	public void onCompletion(String text)
	{
		long endTime = System.currentTimeMillis();
//		Window.alert("Start Time:"+startTime+", End Time:"+endTime+", Elpased Time:"+(endTime-startTime));
//		if (text != null && text.length() > 0)
//		{
//			Window.alert("Received String Size:"+text.length());
			double db = (text.length()*8)/(endTime-startTime);
			if (rate1 == -1)
			{
				this.rate1 = db;
				this.startTime = System.currentTimeMillis();
				this.urlReader.doReadURL();
			}
			else if (this.rate2 == -1)
			{
				this.rate2 = db;
				this.startTime = System.currentTimeMillis();
				this.urlReader.doReadURL();
			}
			else if (this.rate3 == -1)
			{
				this.rate3 = db;
				calculateAndReportDownloadBandwidth();
			}
			else
			{
				calculateAndReportDownloadBandwidth();
			}
//			Window.alert("Download Bandwidth:"+db+" kbps");
//		}
	}
	private	void	calculateAndReportDownloadBandwidth()
	{
		this.downloadBandwidth = (rate1+rate2+rate3)/3;
		this.bandwidthCheck.setDownloadBandwidth(this.downloadBandwidth);
	}
	*/
}
