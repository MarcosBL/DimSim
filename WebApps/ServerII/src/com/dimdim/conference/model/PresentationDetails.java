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

import java.util.Map;

/**
 * @author Saurav Mohapatra
 * @email Saurav.Mohapatra@communiva.com
 *
 */
public class PresentationDetails 
{
//	private String meetingId;
//	private String resourceId;
//	private String presentationId;
	private String originalFileName;
	private String slideCount;
	
	public PresentationDetails()
	{
		
	}
	public String toString()
	{
//		return ""+originalFileName+" ["+meetingId+"/"+presentationId+"] slide count:"+slideCount;
		return ""+originalFileName+" [] slide count:"+slideCount;
	}
//	public String getResourceId()
//	{
//		return this.resourceId;
//	}
//	public void setResourceId(String resourceId)
//	{
//		this.resourceId = resourceId;
//	}
	public void setSlideCount(String slideCount)
	{
		this.slideCount = slideCount;
	}
//	public String getMeetingId() {
//		return meetingId;
//	}
//	public void setMeetingId(String meetingId) {
//		this.meetingId = meetingId;
//	}
	public String getOriginalFileName() {
		return originalFileName;
	}
	public void setOriginalFileName(String originalFileName) {
		this.originalFileName = originalFileName;
	}
//	public String getPresentationId() {
//		return presentationId;
//	}
//	public void setPresentationId(String presentationId) {
//		this.presentationId = presentationId;
//	}
	public int getSlideCount() {
		return Integer.parseInt(slideCount);
	}
	public void setSlideCount(int count) {
		this.slideCount = (new Integer(count)).toString();
	}
	public void load(Map props) throws Exception
	{
//		meetingId = (String)props.get("meetingId");
//		resourceId = (String)props.get("resourceId");
//		presentationId = (String)props.get("presentationId");
		originalFileName = (String)props.get("originalFileName");
		slideCount= (String)props.get("slideCount");
	}
	public void save(Map props) throws Exception
	{
//		props.put("meetingId",meetingId);
//		props.put("resourceId",resourceId);
//		props.put("presentationId",presentationId);
		props.put("originalFileName",originalFileName);
		props.put("slideCount",slideCount);
	}
}
