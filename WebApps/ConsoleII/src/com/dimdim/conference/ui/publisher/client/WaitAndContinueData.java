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

package com.dimdim.conference.ui.publisher.client;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * Most common configuration for the data object for resource share kickoffs.
 * 
 * code : code for the callback to identify the call.
 * 
 * str1 : http url for screen share
 * str2 : rtmp url
 * str3 : rtmp stream name / presentation id
 * str4 : presenter id
 * str5 : conf key
 * str6 : recording flags
 * str7 : resource id
 * str8 : v 2.0 application exe name
 * str9 : v 2.0 application file name / application name back from selector
 * 
 * int1 : application's window handle
 */

public class WaitAndContinueData
{
	protected	String	code = "";
	protected	String	httpUrl = "";
	protected	String	rtmpUrl = "";
//	protected	String	rtmptUrl = "";
	protected	String	rtmpStreamId = "";
//	protected	String	str3 = "";
	protected	String	userId = "";
	protected	String	conferenceKey = "";
	protected	String	recordingFlags = "";
	protected	String	resourceId = "";
//	protected	String	str8 = "";
//	protected	String	str9 = "";
	protected	int		int1 = 0;
	protected	int		int2 = 0;
	protected	boolean	bool1;
	protected	boolean	bool2;
	protected	double appHandle = -1;
	
	public WaitAndContinueData()
	{
	}
	public WaitAndContinueData(WaitAndContinueData wacd)
	{
		this.code = wacd.getCode();
		this.httpUrl = wacd.getHttpUrl();
		this.rtmpUrl = wacd.getRtmpUrl();
//		this.rtmptUrl = wacd.getRtmptUrl();
		this.rtmpStreamId = wacd.getRtmpStreamId();
//		this.str3 = wacd.getStr3();
		this.userId = wacd.getUserId();
		this.conferenceKey = wacd.getConferenceKey();
		this.recordingFlags = wacd.getRecordingFlags();
		this.resourceId = wacd.getResourceId();
//		this.str8 = wacd.getStr8();
//		this.str9 = wacd.getStr9();
		this.int1 = wacd.getInt1();
		this.int2 = wacd.getInt2();
		this.bool1 = wacd.isBool1();
		this.bool2 = wacd.isBool2();
		this.appHandle = wacd.getAppHandle();
	}
	public	String	toString()
	{
		return	code+":"+httpUrl+":"+rtmpUrl+":"+userId+":"+conferenceKey+
			":"+recordingFlags+":"+resourceId+":"+int1+":"+int2+":"+bool1+":"+bool2;
	}
	public boolean isBool1()
	{
		return bool1;
	}
	public void setBool1(boolean bool1)
	{
		this.bool1 = bool1;
	}
	public boolean isBool2()
	{
		return bool2;
	}
	public void setBool2(boolean bool2)
	{
		this.bool2 = bool2;
	}
	public int getInt1()
	{
		return int1;
	}
	public void setInt1(int int1)
	{
		this.int1 = int1;
	}
	public int getInt2()
	{
		return int2;
	}
	public void setInt2(int int2)
	{
		this.int2 = int2;
	}
	public String getHttpUrl()
	{
		return httpUrl;
	}
	public void setHttpUrl(String str1)
	{
		this.httpUrl = str1;
	}
	public String getRtmpUrl()
	{
		return rtmpUrl;
	}
	public void setRtmpUrl(String str2)
	{
		this.rtmpUrl = str2;
	}
//	public String getRtmptUrl()
//	{
//		return rtmptUrl;
//	}
//	public void setRtmptUrl(String rtmptUrl)
//	{
//		this.rtmptUrl = rtmptUrl;
//	}
//	public String getStr3()
//	{
//		return str3;
//	}
//	public void setStr3(String str3)
//	{
//		this.str3 = str3;
//	}
	public String getUserId()
	{
		return userId;
	}
	public void setUserId(String str4)
	{
		this.userId = str4;
	}
	public String getConferenceKey()
	{
		return conferenceKey;
	}
	public void setConferenceKey(String str5)
	{
		this.conferenceKey = str5;
	}
	public String getRecordingFlags()
	{
		return recordingFlags;
	}
	public void setRecordingFlags(String str6)
	{
		this.recordingFlags = str6;
	}
	public String getResourceId()
	{
		return resourceId;
	}
	public void setResourceId(String str7)
	{
		this.resourceId = str7;
	}
	public String getCode()
	{
		return code;
	}
	public void setCode(String code)
	{
		this.code = code;
	}
//	public String getStr8()
//	{
//		return str8;
//	}
//	public void setStr8(String str8)
//	{
//		this.str8 = str8;
//	}
//	public String getStr9()
//	{
//		return str9;
//	}
//	public void setStr9(String str9)
//	{
//		this.str9 = str9;
//	}
	public String getRtmpStreamId()
	{
		return rtmpStreamId;
	}
	public void setRtmpStreamId(String rtmpStreamId)
	{
		this.rtmpStreamId = rtmpStreamId;
	}
	public double getAppHandle()
	{
	    return appHandle;
	}
	public void setAppHandle(double appHandle)
	{
	    this.appHandle = appHandle;
	}
}
