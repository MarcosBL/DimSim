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
 * Part of the DimDim V 2.0 Codebase (http://www.dimdim.com)	          *
 *                                                                        *
 * Copyright (c) 2008 Dimdim Inc. All Rights Reserved.              	  *
 *                                                                        *
 *                                                                        *
 * This code is licensed under the DimDim License                         *
 * For details please visit http://www.dimdim.com/license                 *
 *                                                                        *
 **************************************************************************
 */

package com.dimdim.conference.ui.sharing.client;

import com.dimdim.conference.ui.json.client.UICobrowseControlEvent;
import com.dimdim.conference.ui.json.client.UIPresentationControlEvent;
import com.dimdim.conference.ui.json.client.UIResourceObject;
import com.dimdim.conference.ui.json.client.UIRosterEntry;
import com.dimdim.conference.ui.json.client.UIStreamControlEvent;
import com.dimdim.conference.ui.json.client.UIWhiteboardControlEvent;
import com.dimdim.conference.ui.model.client.ClientModel;
import com.dimdim.conference.ui.model.client.CoBrowseModelListener;
import com.dimdim.conference.ui.model.client.ConferenceGlobals;
import com.dimdim.conference.ui.model.client.PPTSharingModelListener;
import com.dimdim.conference.ui.model.client.SharingModelListener;
import com.dimdim.conference.ui.model.client.WhiteboardModelListener;
import com.dimdim.conference.ui.model.client.dms.CobrowseDMSCaller;
import com.google.gwt.user.client.Window;


/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * This is a purely functional class. It manages the resource sharing panel
 * based on the local and remote events. The local events are simply function
 * calls made from various click listeners. The remove events are various
 * different events from the resource and related sharing models.
 */

public class CollaborationAreaManager implements
		SharingModelListener, PPTSharingModelListener, WhiteboardModelListener, CoBrowseModelListener
{

	protected	static	CollaborationAreaManager	manager;
	
	public	static	void	initManager(UIRosterEntry me, int width, int height, String resNameDivId)
	{
		if (CollaborationAreaManager.manager == null)
		{
			CollaborationAreaManager.manager = new CollaborationAreaManager(me,width,height, resNameDivId);
		}
	}
	public	static	CollaborationAreaManager	getManager()
	{
		return	CollaborationAreaManager.manager;
	}
	
	protected	ResourceSharingPanel	resourceSharingPanel;
	
	protected	UIRosterEntry		me;
	
	public	CollaborationAreaManager(UIRosterEntry me, int width, int height, String resNameDivId)
	{
		this.resourceSharingPanel = new ResourceSharingPanel(me,width,height, resNameDivId);
		
		this.me = me;
		ClientModel.getClientModel().getSharingModel().addListener(this);
		ClientModel.getClientModel().getPPTSharingModel().addListener(this);
		ClientModel.getClientModel().getWhiteboardModel().addListener(this);
		ClientModel.getClientModel().getCobrowseModel().addListener(this);
	}
	public	ResourceSharingPanel	getResourceSharingPanel()
	{
		return	this.resourceSharingPanel;
	}
	
	/**
	 * These method are called on the active presenter console. However the
	 * collaboration area manager does not manage the publisher interface
	 * this is to keep this module independent of the plug-in. The sharing
	 * controller is still seperate and it manages the interaction with
	 * the publisher. This is also because there are timing issues involved
	 * in starting and stopping publisher processes.
	 * 
	 * If the resource being shared is a presentation, powerpoint or pdf, then
	 * the swf movie can generate slide, page change events. Register a listener
	 * to listen to those events.
	 * 
	 * If the console is in full screen mode at this time force an exit out
	 * of it.
	 * 
	 * @param res
	 */
	public	void	onSharingStarted(UIResourceObject res)
	{
//		Window.alert("CollaborationAreaManager::onSharingStarted:"+res);
		this.resourceSharingPanel.onSharingStarted(res);
		
	}
	public	void	onSharingStopped(UIResourceObject res)
	{
//		Window.alert("CollaborationAreaManager::onSharingStopped:"+res);
		if (res != null)
		{
			this.resourceSharingPanel.onSharingStopped(res);
		}
		else
		{
			this.resourceSharingPanel.onSharingStopped(this.resourceSharingPanel.getActiveResource());
		}
	}
	
	/**
	 * Descktop Sharing.
	 */
	public void onChangeShare(UIStreamControlEvent event)
	{
		//	Not used at present.
//		Window.alert("CollaborationAreaManager::onChangeShare:"+event);
	}
	public void onStartSharing(UIStreamControlEvent event)
	{
//		Window.alert("CollaborationAreaManager::onStartSharing:"+event);
		if (!this.me.isActivePresenter())
		{
			UIResourceObject res = ClientModel.getClientModel().getResourceModel().
					findResourceObjectByType(UIResourceObject.RESOURCE_TYPE_DESKTOP);
			if (res != null)
			{
				res.setMediaId(event.getStreamName());
				this.resourceSharingPanel.onSharingStarted(res);
			}
			else
			{
//				Window.alert("Desktop resource not found");
			}
		}
	}
	public void onStopSharing(UIStreamControlEvent event)
	{
//		Window.alert("CollaborationAreaManager::onStopSharing:"+event);
		this.resourceSharingPanel.onSharingStopped(null);
	}
	
	/**
	 * Whiteboard
	 */
	public void onLockWhiteboard(UIWhiteboardControlEvent event)
	{
		//	Defunct - no longer used.
//		Window.alert("CollaborationAreaManager::onLockWhiteboard:"+event);
	}
	public void onUnlockWhiteboard(UIWhiteboardControlEvent event)
	{
		//	Defunct - no longer used
//		Window.alert("CollaborationAreaManager::onUnlockWhiteboard:"+event);
	}
	public void onWhiteboardStarted(UIWhiteboardControlEvent event)
	{
//		Window.alert("CollaborationAreaManager::onWhiteboardStarted:"+event);
		UIResourceObject res = ClientModel.getClientModel().getResourceModel().findResourceObjectByType(UIResourceObject.RESOURCE_TYPE_WHITEBOARD);
		if (res != null)
		{
			this.resourceSharingPanel.onSharingStarted(res);
		}
		else
		{
//			Window.alert("Whiteboard resource not found");
		}
	}
	public void onWhiteboardStopped(UIWhiteboardControlEvent event)
	{
//		Window.alert("CollaborationAreaManager::onWhiteboardStopped:"+event);
		this.resourceSharingPanel.onSharingStopped(null);
	}
	
	/**
	 * Powerpoint presentations and PDF documents sharing
	 */
	public void annotationsDisabled(UIPresentationControlEvent event)
	{
		//	Defunct - never used.
//		Window.alert("CollaborationAreaManager::annotationsDisabled:"+event);
	}
	public void annotationsEnabled(UIPresentationControlEvent event)
	{
		//	Defunct - never used.
//		Window.alert("CollaborationAreaManager::annotationsEnabled:"+event);
	}
	public void slideChanged(UIPresentationControlEvent event)
	{
//		Window.alert("CollaborationAreaManager::slideChanged:"+event);
		UIResourceObject res = this.resourceSharingPanel.getActiveResource();
		if (res != null)
		{
			int showSlide = event.getShowSlide().intValue();
			if (showSlide != res.getLastSlideIndex())
			{
				res.setLastSlideIndex(showSlide);
				this.resourceSharingPanel.onSharingUpdated(res);
			}
		}
		else
		{
			this.startPresentation(event);
		}
	}
	public void startPresentation(UIPresentationControlEvent event)
	{
//		Window.alert("CollaborationAreaManager::startPresentation:"+event);
		String resourceId = event.getResourceId();
		UIResourceObject res = ClientModel.getClientModel().
				getResourceModel().findResourceObject(resourceId);
		if (res != null)
		{
			int showSlide = event.getShowSlide().intValue();
			res.setLastSlideIndex(showSlide);
			
			this.resourceSharingPanel.onSharingStarted(res);
		}
	}
	public void stopPresentation(UIPresentationControlEvent event)
	{
//		Window.alert("CollaborationAreaManager::stopPresentation:"+event);
		this.resourceSharingPanel.onSharingStopped(null);
	}
	public void addResourceSharingCallbacksListener(ResourceSharingCallbacksListener listener)
	{
		this.resourceSharingPanel.addResourceSharingCallbacksListener(listener);
	}
	public	void	roleChanged(boolean activePresenter)
	{
		if (this.resourceSharingPanel.getActiveResource() != null)
		{
			this.onSharingStopped(this.resourceSharingPanel.getActiveResource());
		}
	}
	
	public void onStarted(UICobrowseControlEvent event) {
		//Window.alert("inside cbrowse event on started..");
		String dmsUrl = "http://"+ConferenceGlobals.userInfoDictionary.getStringValue("dms_cob_server_address")+"/cobrowsing";
		UIResourceObject res = ClientModel.getClientModel().getResourceModel().findResourceObject(event.getResourceId());
		//Window.alert("resource object = "+res);
		
		//CobrowseDMSCaller caller = getCobDMSCaller(event, dmsUrl, res);
		//boolean lock = false;
		//if(res.getAnnotation().equals(UIResourceObject.ANNOTATION_ON))
		//{
		//	lock = true;
		//}
		//Window.alert("calling getSyncUrlWithLock  lock = "+lock);
		//caller.getSyncUrlWithLock(lock);
		//this.resourceSharingPanel.onSharingStarted(res);
		
		res.setUrl(ConferenceGlobals.conferenceKeyQualified);
		onSharingStarted(res);
	}
	
	/*private CobrowseDMSCaller getCobDMSCaller(UICobrowseControlEvent event, String dmsUrl, UIResourceObject res) {
		StringBuffer buf = new StringBuffer();
		buf.append(dmsUrl+"/syncToURLResource?");
		buf.append("meetingID="+ConferenceGlobals.getConferenceKey());
		buf.append("&roomID="+ConferenceGlobals.getConferenceKey());
		buf.append("&sessionID=XX");
		buf.append("&resourceID="+res.getResourceId());
		
		//Window.alert("before calling the sync url"+buf.toString());
		CobrowseDMSCaller caller = new CobrowseDMSCaller(buf.toString(),resourceSharingPanel, res);
		return caller;
	}*/
	
	/*public void onStarted(UICobrowseControlEvent event, String newName) {
		//Window.alert("inside cbrowse event on rename and started..");
		String dmsUrl = "http://"+ConferenceGlobals.userInfoDictionary.getStringValue("dms_cob_server_address")+"/cobrowsing";
		UIResourceObject res = ClientModel.getClientModel().getResourceModel().findResourceObject(event.getResourceId());
		res.setResourceName(newName);
		
		CobrowseDMSCaller caller = getCobDMSCaller(event, dmsUrl, res);
		boolean lock = false;
		if(res.getAnnotation().equals(UIResourceObject.ANNOTATION_ON))
		{
			lock = true;
		}
		//Window.alert("calling getSyncUrlWithLock  lock = "+lock);
		caller.getSyncUrlWithLock(lock);
		//this.resourceSharingPanel.onSharingStarted(res);
	}*/
	
	
	/*public void onStartedAndLock(UICobrowseControlEvent event, boolean lock) {
		//Window.alert("inside cbrowse event onStartedAndLock lock = "+lock);
		if (this.resourceSharingPanel.getActiveResource() == null)
		{
			//Window.alert("inside cbrowse event on started..");
			String dmsUrl = "http://"+ConferenceGlobals.userInfoDictionary.getStringValue("dms_cob_server_address")+"/cobrowsing";
			UIResourceObject res = ClientModel.getClientModel().getResourceModel().findResourceObject(event.getResourceId());
			
			CobrowseDMSCaller caller = getCobDMSCaller(event, dmsUrl, res);
			caller.getSyncUrlWithLock(lock);
		}else{
			//Window.alert("just performing lock..."+lock);
			this.resourceSharingPanel.lock(lock);
		}
		//this.resourceSharingPanel.onSharingStarted(res);
	}*/

	public void onStopped(UICobrowseControlEvent event) {
		//Window.alert("inside cbrowse event on stopped..");
		this.resourceSharingPanel.onSharingStopped(null);
	}
	
	public void onLock(UICobrowseControlEvent event) {
		//Window.alert("inside cbrowse event on lock..");
		//UIResourceObject res = ClientModel.getClientModel().getResourceModel().findResourceObject(event.getResourceId());
		if (this.resourceSharingPanel.getActiveResource() != null)
		{
			//Window.alert("active resource is not null so locking it..");
			UIResourceObject res = resourceSharingPanel.getActiveResource();
			res.setAnnotation(UIResourceObject.ANNOTATION_ON);
			this.resourceSharingPanel.lock(true);
		}
	}
	
	public void onUnlock(UICobrowseControlEvent event) {
		//Window.alert("inside cbrowse event on unlock..");
		if (this.resourceSharingPanel.getActiveResource() != null)
		{
			//Window.alert("active resource is not null so un-locking it..");
			UIResourceObject res = resourceSharingPanel.getActiveResource();
			res.setAnnotation(UIResourceObject.ANNOTATION_OFF);
			this.resourceSharingPanel.lock(false);
		}
	}
	
	/*public void onRename(UICobrowseControlEvent event) {
		//Window.alert("inside cbrowse event on unlock..");
		if (this.resourceSharingPanel.getActiveResource() == null)
		{
			onStarted(event, event.getNewName());
		}
		if (this.resourceSharingPanel.getActiveResource() != null)
		{
			//Window.alert("active resource is renaming with "+event.getNewName());
			this.resourceSharingPanel.writeCobResName(event.getNewName());
		}
	}*/
}
