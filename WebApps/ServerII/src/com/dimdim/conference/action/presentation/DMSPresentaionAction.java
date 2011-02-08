package com.dimdim.conference.action.presentation;

import com.dimdim.conference.action.CommonDimDimAction;
import com.dimdim.conference.application.presentation.PresentationManager;
import com.dimdim.conference.model.Presentation;

public class DMSPresentaionAction extends	CommonDimDimAction	{

	protected	String	pptName;
	protected	String	pptID;
	protected	int		noOfSlides;
	protected	String	meetingKey;
	protected	int		width;
	protected	int		height;
	
	public	String	execute()
	{
		PresentationManager pmgr = PresentationManager.getPresentationManager(meetingKey);
		try
		{
			pmgr.sealDMSPresentation(pptID, pptName, noOfSlides, width, height);
			
//				System.out.println("adding ppt through ppt manager...");
//				pmgr.newPresentation(pptID, pptName);
//				System.out.println("added ppt.. ");
//				Presentation presentation = pmgr.getPresentation(pptID);
//				System.out.println("got ppt from ppt manager .. "+presentation.getPresentationId());
//				presentation.setSlideCount(noOfSlides);
//				
//				pmgr.sealPresentation(presentation.getResourceId(), presentation.getPresentationId());
//				System.out.println("sealed ppt.. ");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		System.out.println("done with changes....");
		return SUCCESS;
	}
	public int getNoOfSlides()
	{
		return noOfSlides;
	}
	public void setNoOfSlides(int noOfSlides)
	{
		this.noOfSlides = noOfSlides;
	}
	public String getPptID()
	{
		return pptID;
	}
	public void setPptID(String pptID)
	{
		this.pptID = pptID;
	}
	public String getPptName()
	{
		return pptName;
	}
	public void setPptName(String pptName)
	{
		this.pptName = pptName;
	}
	public String getMeetingKey()
	{
		return meetingKey;
	}
	public void setMeetingKey(String meetingKey)
	{
		this.meetingKey = meetingKey;
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
