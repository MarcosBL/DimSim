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
import java.io.File;

/**
 * @author Saurav Mohapatra
 * @email Saurav.Mohapatra@communiva.com
 *
 */
public class Presentation 	implements	Comparable
{
	private String meetingId = "";
	private String resourceId = "";
	private String presentationId = "";
	private String originalFileName = "";
	private int    slideCount = 0;
	private String slidePrefix = "slide";
	private String slideSuffix = "jpg";
	private boolean sealed = false;
	private	int		width;
	private	int		height;
	
	public Presentation()
	{
		
	}
	public PresentationDetails getDetails()
	{
		PresentationDetails details = new PresentationDetails();
		extractDetails(details);
		return details;
	}
	public	int	compareTo(Object obj)
	{
		if (obj != null && obj instanceof Presentation)
		{
			String fileName = ((Presentation)obj).getOriginalFileName();
			if (fileName != null && this.originalFileName != null)
			{
				return	this.originalFileName.compareTo(fileName);
			}
		}
		return	1;
	}
	public void extractDetails(PresentationDetails details)
	{
//		details.setMeetingId(meetingId);
//		details.setResourceId(resourceId);
//		details.setPresentationId(presentationId);
		details.setOriginalFileName(originalFileName);
		details.setSlideCount(slideCount);
	}
//	public void load(Map props) throws Exception
//	{
//		meetingId = (String)props.get("meetingId");
//		resourceId = (String)props.get("resourceId");
//		presentationId = (String)props.get("presentationId");
//		originalFileName = (String)props.get("originalFileName");
//		Integer icount = (Integer)props.get("slideCount");
//		slideCount=icount.intValue();
//		slidePrefix = (String)props.get("slidePrefix");
//		slideSuffix = (String)props.get("slideSuffix");
//	}
//	public void save(Map props) throws Exception
//	{
//		props.put("meetingId",meetingId);
//		props.put("resourceId",resourceId);
//		props.put("presentationId",presentationId);
//		props.put("originalFileName",originalFileName);
//		props.put("slideCount",new Integer(slideCount));
//		props.put("slidePrefix",slidePrefix);
//		props.put("slideSuffix",slideSuffix);
//	}
	public	String	getNiceNameFromOriginalFile()
	{
		String	name = null;
			String fn = this.getOriginalFileName();
			if (fn != null)
			{
				String newName = fn;
				int slash = fn.lastIndexOf("\\");
				if (slash > 0)
				{
					newName = fn.substring(slash+1);
					int dot = newName.lastIndexOf(".");
					if (dot > 0)
					{
						newName = newName.substring(0,dot);
					}
				}
				name = newName;
			}
		return	name;
	}

	public String getResourceId()
	{
		return this.resourceId;
	}
	public void setResourceId(String resourceId)
	{
		this.resourceId = resourceId;
	}
	public String getMeetingId()
	{
		return meetingId;
	}
	public void setMeetingId(String meetingId)
	{
		this.meetingId = meetingId;
	}
	public String getPresentationId()
	{
		return presentationId;
	}
	public void setPresentationId(String presentationId)
	{
		this.presentationId = presentationId;
	}
	public int getSlideCount()
	{
		return slideCount;
	}
	public void setSlideCount(int slideCount)
	{
		this.slideCount = slideCount;
	}
	public String getSlidePrefix()
	{
		return slidePrefix;
	}
	public void setSlidePrefix(String slidePrefix)
	{
		this.slidePrefix = slidePrefix;
	}
	public String getSlideSuffix()
	{
		return slideSuffix;
	}
	public void setSlideSuffix(String slideSuffix)
	{
		this.slideSuffix = slideSuffix;
	}
	public String toString()
	{
		return ""+originalFileName +"("+meetingId+"/"+presentationId+") Slide Count = "+slideCount;
	}
	public String getOriginalFileName()
	{
		return originalFileName;
	}
	public void setOriginalFileName(String originalFileName)
	{
		this.originalFileName = originalFileName;
	}
	public boolean isSealed()
	{
		return sealed;
	}
	public void setSealed(boolean sealed)
	{
		this.sealed = sealed;
	}
	public void resetDefaults() 
	{
		setSlideCount(0);
		setSlidePrefix("slide");
		setSlideSuffix("jpg");
		setSealed(false);
	}
	public int getHeight()
	{
		return height;
	}
	public void setHeight(int height)
	{
		this.height = height;
	}
	public int getWidth()
	{
		return width;
	}
	public void setWidth(int width)
	{
		this.width = width;
	}
}
