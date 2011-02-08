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

package com.dimdim.conference.action.presentation;

import com.dimdim.conference.ConferenceConstants;
import com.dimdim.conference.action.ConferenceAction;
import com.dimdim.conference.model.IConference;
import com.dimdim.conference.model.IConferenceParticipant;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 *
	public static final String ACTION_PRESENTATION_START = "action.pres.start";
	public static final String ACTION_PRESENTATION_FIRST_SLIDE = "action.pres.first";
	public static final String ACTION_PRESENTATION_NEXT_SLIDE = "action.pres.next";
	public static final String ACTION_PRESENTATION_PREVIOUS_SLIDE = "action.pres.previous";
	public static final String ACTION_PRESENTATION_LAST_SLIDE = "action.pres.last";
	public static final String ACTION_PRESENTATION_SLIDE_BY_NUMBER = "action.pres.slide";
	public static final String ACTION_PRESENTATION_ANNOTATIONS_ON = "action.pres.ann_on";
	public static final String ACTION_PRESENTATION_ANNOTATIONS_OFF = "action.pres.ann_off";
	public static final String ACTION_PRESENTATION_STOP = "action.pres.stop";
 */
public class PresentationControlAction		extends	ConferenceAction
{
	protected	String	resourceId;
//	protected	String	presentationId;
	protected	String	controlAction;
	protected	Integer	slide;
	
	public	PresentationControlAction()
	{
	}
	public	String	doWork()	throws	Exception
	{
		String	ret = SUCCESS;
		IConference conf = this.userSession.getConference();
		IConferenceParticipant user = this.userSession.getUser();
		
		try
		{
			conf.getResourceManager().handlePresentationControlMessage(user,
					this.resourceId,this.controlAction,this.slide,user.getId());
		}
		catch(Exception e)
		{
			e.printStackTrace();
			ret = ERROR;
		}
		return	ret;
	}
	
	public String getControlAction()
	{
		return this.controlAction;
	}
	public void setControlAction(String controlAction)
	{
		this.controlAction = controlAction;
	}
	public String getResourceId()
	{
		return this.resourceId;
	}
	public void setResourceId(String resourceId)
	{
		this.resourceId = resourceId;
	}
	/*
	public String getPresentationId()
	{
		return this.presentationId;
	}
	public void setPresentationId(String presentationId)
	{
		this.presentationId = presentationId;
	}
	*/
	public Integer getSlide()
	{
		return this.slide;
	}
	public void setSlide(Integer slide)
	{
		this.slide = slide;
	}
}
