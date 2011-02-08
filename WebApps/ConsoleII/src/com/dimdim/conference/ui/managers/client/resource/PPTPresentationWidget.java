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
/*
 **************************************************************************
 *	File Name  : AVWindow.java
 *  Created On : Jun 30, 2006
 *  Created By : Saurav Mohapatra
 **************************************************************************
 */

package com.dimdim.conference.ui.managers.client.resource;

//import java.util.Vector;

import com.dimdim.conference.ui.common.client.ResourceGlobals;
import com.dimdim.conference.ui.common.client.util.FlashCallbackHandler;
import com.dimdim.conference.ui.common.client.util.FlashStreamHandler;
import com.dimdim.conference.ui.json.client.UIPresentationControlEvent;
import com.dimdim.conference.ui.json.client.UIResourceObject;
import com.dimdim.conference.ui.json.client.UIRosterEntry;
import com.dimdim.conference.ui.model.client.ClientModel;
import com.dimdim.conference.ui.model.client.ConferenceGlobals;
import com.dimdim.conference.ui.model.client.PPTSharingModel;
import com.dimdim.conference.ui.model.client.PPTSharingModelListener;

/**
 * @author Jayant Pandit
 * 
 */
public class PPTPresentationWidget implements PPTSharingModelListener, FlashStreamHandler
{
	private	UIRosterEntry		me;
	private	UIResourceObject	currentPPT;
	private	PPTSharingModel		pptSharingModel;
	private	ResourceSharingController	rsc;
	
	private	boolean		sharing = false;
	
	private PPTPresentationWidget(ResourceSharingController rsc, UIRosterEntry me)
	{
//		this.discussionWidget = discussionWidget;
		this.rsc = rsc;
		this.me = me;
		this.pptSharingModel = ClientModel.getClientModel().getPPTSharingModel();
		this.pptSharingModel.addListener(this);
	}
	/*
	public UIResourceObject getCurrentPPT()
	{
		return currentPPT;
	}
	public void setCurrentPPT(UIResourceObject currentPPT)
	{
		this.sharing = false;
		this.currentPPT = currentPPT;
		this.startPPTPresentation(currentPPT);
	}
	*/
	public	void	startPPTPresentation(UIResourceObject ppt)
	{
		this.sharing = false;
		this.currentPPT = ppt;
		ConferenceGlobals.setCurrentSharedResource(ppt);
		FlashCallbackHandler.getHandler().addStreamHandler(this);
	}
	public	void	stopPPTPresentation()
	{
		if (currentPPT != null)
		{
			FlashCallbackHandler.getHandler().removeStreamHandler(this.currentPPT.getResourceId());
		}
	}
	public	String	getPptReadingUrl()
	{
		String confKey = ConferenceGlobals.conferenceKey;
		String altConfKey = ResourceGlobals.getMeetingIdIfDefaultResource(currentPPT);
		if (altConfKey != null)
		{
			confKey = altConfKey;
		}
		String pptReadingUrl = ConferenceGlobals.webappRoot+"presentations/"+
			confKey+"/"+this.currentPPT.getMediaId()+"/";
		
		return	pptReadingUrl;
	}
	public	int	getSlideCount()
	{
		return	this.currentPPT.getSlideCount().intValue();
	}
	public	String	getMovieUrl()
	{
		/*
		String confKey = ConferenceGlobals.conferenceKey;
		if (this.currentPPT.getOwnerId().equalsIgnoreCase("SYSTEM"))
		{
			confKey = "global-meeting";
		}
		String pptReadingUrl = ConferenceGlobals.webappRoot+"presentations/"+
			confKey+"/"+this.currentPPT.getMediaId()+"/";
		*/
//		Window.alert(pptReadingUrl);
		
		return "swf/presentationBroadcaster.swf?"+this.getPptReadingUrl()+"$"+
				this.currentPPT.getSlideCount()+"$"+this.currentPPT.getResourceId()+"$0";
	}
	public	String	getStreamName()
	{
		if (currentPPT != null)
		{
			return	this.currentPPT.getResourceId();
		}
		return	"PPT";
	}
	/**
	 * For this movie only 2 events are expected which are the slide number
	 * and stop. On slide number if the presentation is not being shared then
	 * start sharing it with others at that slide, otherwise simply change the
	 * slide.   
	 */
	public	void	handleEvent(String eventName)
	{
		if (eventName.startsWith("stop"))
		{
//			Window.alert("Received stop presentation from flash:"+eventName);
			this.pptSharingModel.stopPresentation(this.currentPPT.getResourceId());
			FlashCallbackHandler.getHandler().removeStreamHandler(this.currentPPT.getResourceId());
			this.sharing = false;
			ConferenceGlobals.setCurrentSharedResource(null);
//			this.rsc.stopPPTPresentation();
		}
		else if (eventName.startsWith(UIResourceObject.ANNOTATION_ON))
		{
//			Window.alert("Switching on the annotations for resource: "+this.currentPPT.getResourceName());
			if (sharing)
			{
				this.currentPPT.setAnnotation(UIResourceObject.ANNOTATION_ON);
				this.pptSharingModel.EnableAnnotations(this.currentPPT.getResourceId());
//				this.rsc.pptPresentatinMovieContainer.setAnnotationStatus(UIResourceObject.ANNOTATION_ON);
			}
		}
		else if (eventName.startsWith(UIResourceObject.ANNOTATION_OFF))
		{
//			Window.alert("Switching off the annotations for resource: "+this.currentPPT.getResourceName());
			//	At present the disable annotations scenario is not very clear. So only the
			//	enable event is used.
		}
		else
		{
//			Window.alert("Received event from flash:"+eventName);
			if (!sharing)
			{
				this.sharing = true;
				int	slide = (new Integer(eventName)).intValue();
				this.currentPPT.setLastSlideIndex(slide);
				this.pptSharingModel.startPresentation(this.currentPPT.getResourceId(),slide);
			}
//			else
			{
				int	slide = (new Integer(eventName)).intValue();
				this.currentPPT.setLastSlideIndex(slide);
				this.pptSharingModel.changePresentationSlide(this.currentPPT.getResourceId(),slide);
				this.showSlideChanged(slide);
			}
		}
	}
	protected	void	showSlideChanged(int currentSlide)
	{
//		Vector	v = this.pptSharingModel.getShareControlPanels();
//		int size = v.size();
//		for (int i=0; i<size; i++)
//		{
//			HorizontalPanel pptControlPanel = (HorizontalPanel)v.elementAt(i);
//			if (pptControlPanel != null && pptControlPanel.getWidgetCount() > 0)
//			{
//				PptShareControl pptShareControl = (PptShareControl)pptControlPanel.getWidget(0);
//				pptShareControl.setCurrentSlide(currentSlide+1);
//			}
//		}
	}
	public void annotationsDisabled(UIPresentationControlEvent event)
	{
//		Implementation not required because the real action is taken by the ppt player.
	}
	public void annotationsEnabled(UIPresentationControlEvent event)
	{
//		Implementation not required because the real action is taken by the ppt player.
	}
	public	void	registerPptShareControls()
	{
//		Vector	v = this.pptSharingModel.getShareControlPanels();
//		int size = v.size();
//		for (int i=0; i<size; i++)
//		{
//			HorizontalPanel pptControlPanel = (HorizontalPanel)v.elementAt(i);
//			if (pptControlPanel != null && pptControlPanel.getWidgetCount() > 0)
//			{
//				PptShareControl pptShareControl = (PptShareControl)pptControlPanel.getWidget(0);
//				pptShareControl.getStopControl().addClickListener(new ClickListener()
//				{
//					public	void	onClick(Widget sender)
//					{
//						handleEvent("stop");
//					}
//				});
//			}
//		}
	}
	public void slideChanged(UIPresentationControlEvent event)
	{
	}
	public void startPresentation(UIPresentationControlEvent event)
	{
	}
	public void stopPresentation(UIPresentationControlEvent event)
	{
	}
}
