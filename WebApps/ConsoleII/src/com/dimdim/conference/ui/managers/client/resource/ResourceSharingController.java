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

package com.dimdim.conference.ui.managers.client.resource;

import com.dimdim.conference.ui.common.client.ResourceGlobals;
import com.dimdim.conference.ui.common.client.UIConstants;
import com.dimdim.conference.ui.common.client.UIGlobals;
import com.dimdim.conference.ui.common.client.UIStrings;
import com.dimdim.conference.ui.common.client.layout.DefaultCommonDialog;
import com.dimdim.conference.ui.common.client.resource.ResourceCallbacks;
import com.dimdim.conference.ui.common.client.resource.ResourceList;
import com.dimdim.conference.ui.common.client.user.UserCallbacks;
import com.dimdim.conference.ui.common.client.util.CommonUserInformationDialog;
import com.dimdim.conference.ui.common.client.util.ConfirmationDialog;
import com.dimdim.conference.ui.common.client.util.ConfirmationListener;
import com.dimdim.conference.ui.json.client.UIResourceObject;
import com.dimdim.conference.ui.json.client.UIRosterEntry;
import com.dimdim.conference.ui.model.client.ClientModel;
import com.dimdim.conference.ui.model.client.CommandExecProgressListener;
import com.dimdim.conference.ui.model.client.ConferenceGlobals;
import com.dimdim.conference.ui.model.client.ResourceSharingDisplay;
import com.dimdim.conference.ui.publisher.client.PublisherInterfaceManager;
import com.dimdim.conference.ui.publisher.client.WaitAndContinueData;
import com.dimdim.conference.ui.publisher.client.WaitAndContinueUserListener;
import com.dimdim.conference.ui.resources.client.SelectFileDialogue;
import com.dimdim.conference.ui.sharing.client.ResourceSharingCallbacksListener;
import com.dimdim.conference.ui.user.client.ActivePresenterAVManager;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.PopupListener;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * This object controls resource sharing kickoff on the presenter console.
 */

public class ResourceSharingController implements WaitAndContinueUserListener,
	/*ConfirmationListener,*/ ResourceCallbacks, ResourceSharingCallbacksListener
{
	public	static	final	String	DESKTOP	=	"DESKTOP";
	public	static	final	String	WHITEBOARD	=	"WHITEBOARD";
	public	static	final	String	COBROWSE	=	"COBROWSE";
	public	static	final	String	SELECT_POWERPOINT_DMS	=	"SELECT_POWERPOINT_DMS";
	public	static	final	String	SHARE_PPT_PRESENTATION	=	"SHARE_PPT_PRESENTATION";
	
	protected	UIRosterEntry	me;
	protected	ResourceManager	resourceManager;
	protected	ResourceList			resourceList;
	
	protected	UIResourceObject		currentSharedResource;
	
	protected	WaitAndContinueData		publisherData;
	
	protected	int		pcounter = 0;
	
	protected	Timer	appShareTimer;
	protected	WaitAndContinueData	nextStepData;
	
//	protected	ConfirmationDialog		desktopShareMessageDialog;
	
//	double appHandle = -1;
	
	private ApplicationShareListener appShareListener;
	private	PopupListener		pendingShareListener;
	private	ResourceSharingDisplay	resourceSharingDisplay;
	private	boolean		transparentRestart = false;
	private	boolean		recordingOn = false;
	private	String		recordingFlags = "";
	private DTPRestartListener dtpRestartListener;

	private SelectFileDialogue safd = null;
	
	//	0 - neutral. No dtp process in progress. A non zero flag means that an information
	//		dialog is on display and is waiting for a response from the publisher interface.
	//	1 - dtp start sequence in progress.
	//	2 - dtp stop sequence in progress.
	//	3 - dtp stop sequence in progress, with required start after stop.
	//		this means a forced restart because of a recording event.
	private	int		dtpSharingProcessFlag = 0;
	//Timer pollForUrl = null;
	/**
	 * 
	 * @param me
	 */
	public	ResourceSharingController(UIRosterEntry me, ResourceManager resourceManager)
	{
		this.me = me;
		this.resourceManager = resourceManager;
		
		this.publisherData = new WaitAndContinueData();
		
		String dtpRtmpUrl = UIGlobals.getStreamingUrlsTable().getDtpRtmpUrl();
		String dtpRtmptUrl = UIGlobals.getStreamingUrlsTable().getDtpRtmptUrl();
		this.publisherData.setRtmpUrl(dtpRtmpUrl);
//		this.publisherData.setRtmptUrl(dtpRtmptUrl);
//		this.publisherData.setStr3(ConferenceGlobals.conferenceKey+"_sh");
		this.publisherData.setUserId(me.getUserId());
		this.publisherData.setConferenceKey(ConferenceGlobals.conferenceKeyQualified);
		this.publisherData.setRecordingFlags("");
		
		//	Recording flags. pattern is
		//	~/dimdim_id/room_id/meeting_id
		//	
		//	At the time of the 4.0 development dimdim_id and room_id are same as
		//	confKey. Only question is whether recording is supported or not.
		//	
		if (ConferenceGlobals.recordingEnabled)
		{
			recordingFlags = "~"+ConferenceGlobals.conferenceKeyQualified+"/"+
				ConferenceGlobals.roomId+"/"+ConferenceGlobals.conferenceId;
		}
//		this.publisherData.setStr6("");
		
		appShareListener = new ApplicationShareListener(this);
		pendingShareListener = new PopupListener()
		{
			public void onPopupClosed(PopupPanel sender, boolean autoClosed)
			{
				startPendingShare();
			}
		};
		
		/*pollForUrl = new Timer() {
		      public void run() {
		    	String confUrl = getLocation();
		    	//Window.alert("the url "+confUrl);
		    	int index = confUrl.indexOf("%23");
		    	if(index != -1 )
		    	{
		    		String url = confUrl.substring(index+3);
		    		//Window.alert("the url "+url);
		    		//Window.alert("after escaping url "+unescape(url));
	    			writeCobResName(unescape(url));
		      	}
		      }
		    };*/

		// Schedule the timer to run once in 5 seconds.
		
	}
	public ResourceSharingDisplay getResourceSharingDisplay()
	{
		return resourceSharingDisplay;
	}
	public void setResourceSharingDisplay(
			ResourceSharingDisplay resourceSharingDisplay)
	{
		this.resourceSharingDisplay = resourceSharingDisplay;
	}
	/**
	 * This method is called from the resource list entry click listener.
	 * If sharing is active for the same object it will be stopped, otherwise
	 * the call will be forwarded to start sharing.
	 * 
	 * @param res
	 */
	public	void	toggleSharing(UIResourceObject res)
	{
		//Window.alert("ResourceSharingController::toggleSharing:"+res);
		//Window.alert("ResourceSharingController::currentSharedResource:"+currentSharedResource);
		if (this.currentSharedResource != null &&
				this.currentSharedResource.getResourceId().equals(res.getResourceId()))
		{
			//	The resource is already being shared. Stop the sharing.
			String newKey = getShareKey(res);
			stopSharing(newKey,res);
		}
		else
		{
			startSharingIfNotActive(res);
		}
	}
	public	void	startSharingIfNotActive(UIResourceObject res)
	{
		if(res.getResourceType().equals(UIConstants.RESOURCE_TYPE_DESKTOP))
		{
			if(!"true".equals(ConferenceGlobals.getPubAvailable()))
			{
				installPub();
				return;
			}
		}
			this.publisherData.setResourceId(res.getResourceId());
//			this.publisherData.setStr3(res.getMediaId());
			String s = res.getAppHandle();
			if (s != null && s.length() > 0 && !s.equals("x"))
			{
				int i = (new Integer(s)).intValue();
				this.publisherData.setInt1(i);
			}
			else
			{
				this.publisherData.setInt1(0);
			}
			String currentKey = getShareKey(this.currentSharedResource);
			String newKey = getShareKey(res);
			startShare(newKey,currentKey,res);
	}
	
	private void  installPub()
	{
		if(ConferenceGlobals.isPubSupportable())
		{
			PubInstallListener listener = new PubInstallListener(resourceManager.getUcb(),me);
			String title = ConferenceGlobals.getDisplayString("ondemand.pub.dialog.header","Screencaster Install");
			String description = ConferenceGlobals.getDisplayString("ondemand.pub.dialog.desc","Dimdim Screencaster Plug-in VERSION needs to be installed for the Desktop to be shared");
			description = description.replaceAll("VERSION", ConferenceGlobals.getPubVersion());
			ConfirmationDialog confirmPubInstall = new ConfirmationDialog(title, description, title, listener);
			confirmPubInstall.drawDialog();
			confirmPubInstall.setButtonLabels(UIStrings.getOKLabel(), UIStrings.getCancelLabel());
		}
		else
		{
			String title = ConferenceGlobals.getDisplayString("ondemand.pub.no.support.header","Info");
			String description = ConferenceGlobals.getDisplayString("ondemand.pub.no.support.desc","Dimdim Screencaster Plug-in is not supported");
			DefaultCommonDialog.showMessage(title, description); 
		}
	}
	
	private	String	getShareKey(UIResourceObject res)
	{
		if (res == null)
		{
			return	"";
		}
		if (res.getResourceType().equals(UIConstants.RESOURCE_TYPE_DESKTOP))
		{
			res.setMediaId("0");
			return	ResourceSharingController.DESKTOP;
		}
		else if (res.getResourceType().equals(UIConstants.RESOURCE_TYPE_WHITEBOARD))
		{
			return	ResourceSharingController.WHITEBOARD;
		}
		else if (res.getResourceType().equals(UIConstants.RESOURCE_TYPE_PRESENTATION))
		{
			return	ResourceSharingController.SHARE_PPT_PRESENTATION;
		}else if (res.getResourceType().equals(UIConstants.RESOURCE_TYPE_COBROWSE))
		{
			return	ResourceSharingController.COBROWSE;
		}
		return	"";
	}
	/**
	 * This method returns when the actual share or selection process is
	 * kicked off. The sharing may involve multiple steps and each step has
	 * to be considered as sharing active because the console must consider
	 * it as a single unit of operation.
	 * 
	 * @param shareKey
	 */
	private	void	startShare(String newShareKey, String currentShareKey, final UIResourceObject res)
	{
		//Window.alert("inside startshare of res sharing controller... res ="+res);
		if (newShareKey.equals(ResourceSharingController.DESKTOP))
		{
			if (this.isShareDesktopActive())
			{
				this.showWaitingForPubResponse(ConferenceGlobals.getDisplayString("stopping.desktop.header","Stopping Desktop Sharing"));
				this.dtpSharingProcessFlag = 2;
				stopDTPAppShare();
			}
			else
			{
				CommonUserInformationDialog cuid = CommonUserInformationDialog.getCommonUserInformationDialog(
						UIStrings.getConfirmDesktopShareDialogHeader(),
						UIStrings.getConfirmDesktopShareDialogComment1()+" "+
						UIStrings.getConfirmDesktopShareDialogComment2());
				ClickListener okListener = new ClickListener()
				{
					public void onClick(Widget sender)
					{
						onOK();
					}
				};
				ClickListener cancelListener = new ClickListener()
				{
					public void onClick(Widget sender)
					{
						CommonUserInformationDialog.hideCommonUserInformationDialog();
					}
				};
		    	cuid.drawDialog();
		    	cuid.addOKClickListener(okListener);
		    	cuid.addCancelClickListener(cancelListener);
		    	cuid.setButtonLabels(UIStrings.getOKLabel(),UIStrings.getCancelLabel());
				
//			    	desktopShareMessageDialog = new ConfirmationDialog(
//						UIStrings.getConfirmDesktopShareDialogHeader(),
//						UIStrings.getConfirmDesktopShareDialogComment1()+" "+
//						UIStrings.getConfirmDesktopShareDialogComment2(),
//						"default-message",this);
//			    	desktopShareMessageDialog.setHideOnOK(false);
//			    	desktopShareMessageDialog.drawDialog();
//			    	desktopShareMessageDialog.setButtonLabels(UIStrings.getOKLabel(),UIStrings.getCancelLabel());
			}
		}else if (newShareKey.equals(ResourceSharingController.COBROWSE))
		{
			boolean	startNow = true;
			if(currentShareKey.equals(ResourceSharingController.SHARE_PPT_PRESENTATION))
			{
				stopPPTPresentation();
			}else if(currentShareKey.equals(ResourceSharingController.COBROWSE))
			{
				stopCobrowse(this.currentSharedResource);
			}else if (currentShareKey.equals(ResourceSharingController.WHITEBOARD))
			{
				stopWhiteboardShare();
			}
			
			if (isShareDesktopActive())
			{
				//stop desktop share
				appShareTimer = new Timer()
				{
					public void run()
					{
						startCobrowse(res);
					}
				};
				startNow = false;
				this.dtpSharingProcessFlag = 2;
				stopDTPAppShare();
				PublisherInterfaceManager.getManager().setDesktopSharingActive(false);
			}
			
			if (startNow)
			{
				startCobrowse(res);
			}
			
		}
		else if (newShareKey.equals(ResourceSharingController.WHITEBOARD))
		{
				boolean	startNow = true;
				if(currentShareKey.equals(ResourceSharingController.SHARE_PPT_PRESENTATION))
				{
					stopPPTPresentation();
				}
				if(currentShareKey.equals(ResourceSharingController.COBROWSE))
				{
					stopCobrowse(res);
				}
				if (isShareDesktopActive())
				{
					appShareTimer = new Timer()
					{
						public void run()
						{
							startWhiteboardShare(res);
						}
					};
					startNow = false;
					this.dtpSharingProcessFlag = 2;
					stopDTPAppShare();
					PublisherInterfaceManager.getManager().setDesktopSharingActive(false);
				}
				
				if (startNow)
				{
					this.startWhiteboardShare(res);
				}
				
				
		}
		else if (newShareKey.equals(ResourceSharingController.SHARE_PPT_PRESENTATION))
		{
			boolean	startNow = true;
			
			if(currentShareKey.equals(ResourceSharingController.SHARE_PPT_PRESENTATION))
			{
			    	//Window.alert("stop presentation...");
				stopPPTPresentation();
			}
			if(currentShareKey.equals(ResourceSharingController.COBROWSE))
			{
				stopCobrowse(res);
			}
			if (currentShareKey.equals(ResourceSharingController.WHITEBOARD))
			{
				stopWhiteboardShare();
			}
			
			if (isShareDesktopActive())
			{
				appShareTimer = new Timer()
				{
					public void run()
					{
						startPPTSharing(res);
					}
				};
				startNow = false;
				this.dtpSharingProcessFlag = 2;
				stopDTPAppShare();
				PublisherInterfaceManager.getManager().setDesktopSharingActive(false);
			}
			if (startNow)
			{
			    	//Window.alert("starting the share...");
			    	//Window.alert("ConferenceGlobals.getCurrentSharedResource() = "+ConferenceGlobals.getCurrentSharedResource());
				startPPTSharing(res);
			}
			
		}
		else
		{
		}
	}
	private void startCobrowse(UIResourceObject res) {
		//Window.alert("start co browse ...res="+res);
		//if (this.resourceSharingDisplay != null)
		//{
			//this.resourceSharingDisplay.onSharingStarted(res);
		this.currentSharedResource = res;
		//}
		ClientModel.getClientModel().getCobrowseModel().startCobrowse("xx", res, resourceSharingDisplay);
		ConferenceGlobals.setCurrentSharedResource(res);
		this.resourceList.showResourceBeingShared(ConferenceGlobals.getCurrentSharedResource());
		//pollForUrl.scheduleRepeating(5000);
	}
	
	public void changeCobrowseName(String newName) {
		ClientModel.getClientModel().getCobrowseModel().renameCobResurce("xx", this.currentSharedResource, resourceSharingDisplay, newName);
	}
	public void syncCobrowse() {
		//Window.alert("inside sync cobrowse... "+currentSharedResource);
		//Window.alert("inside sync cobrowse... "+currentSharedResource.getResourceType());
		
		resourceSharingDisplay.syncCobUrl();
				
	}
	
	public void lock(boolean lock) {
		if(null != this.currentSharedResource && 
				this.currentSharedResource.getResourceType().equals(UIConstants.RESOURCE_TYPE_COBROWSE))
		{
			if(lock)
			{
				this.currentSharedResource.setAnnotation(UIResourceObject.ANNOTATION_ON);
			}else{
				this.currentSharedResource.setAnnotation(UIResourceObject.ANNOTATION_OFF);
			}
			ClientModel.getClientModel().getCobrowseModel().lock("xx", this.currentSharedResource, resourceSharingDisplay, lock);
		}
				
	}
	
	public void navigateTo(String state) {
		//Window.alert("inside sync cobrowse... "+currentSharedResource);
		//Window.alert("inside sync cobrowse... "+currentSharedResource.getResourceType());
		
		resourceSharingDisplay.navigateTo(state);
				
	}
	
	public void writeCobResName(String url)
	{
		resourceSharingDisplay.writeCobResName(url);
	}
	
	private void stopCobrowse(UIResourceObject res) {
		//Window.alert("stop co brose ...res="+res);
		if (this.resourceSharingDisplay != null)
		{
			this.resourceSharingDisplay.onSharingStopped(this.currentSharedResource);
		}
		this.currentSharedResource = null;
		ClientModel.getClientModel().getCobrowseModel().stopCobrowse("xx", res);
		ConferenceGlobals.setCurrentSharedResource(res);
		if (null != this.resourceList)
	    {
			this.resourceList.showResourceSharingStopped();
	    }
		ConferenceGlobals.setCurrentSharedResource(null);
		//pollForUrl.cancel();
	}
	
	private	void	stopSharing(String shareKey, UIResourceObject res)
	{
		if (shareKey.equals(ResourceSharingController.DESKTOP))
		{
			if (this.isShareDesktopActive())
			{
				this.showWaitingForPubResponse("Stopping Desktop Sharing");
				this.dtpSharingProcessFlag = 2;
				stopDTPAppShare();
			}
		}
		else if (shareKey.equals(ResourceSharingController.WHITEBOARD))
		{
			this.stopWhiteboardShare();
		}
		else if (shareKey.equals(ResourceSharingController.SHARE_PPT_PRESENTATION))
		{
			stopPPTPresentation();
		}
		else if (shareKey.equals(ResourceSharingController.COBROWSE))
		{
			this.stopCobrowse(res);
		}
		else
		{
		}
	}
	
	/**
	 * Specific methods for kicking off resource sharing.
	 * 
	 * @param res
	 */
	private void	startDesktopShare()
	{
		this.showWaitingForPubResponse(ConferenceGlobals.getDisplayString("starting.desktop.header","Starting Computer Screen Sharing"));
		if (this.recordingOn)
		{
			this.publisherData.setRecordingFlags(recordingFlags);
		}
		else
		{
			this.publisherData.setRecordingFlags("");
		}
		this.publisherData.setCode(ResourceSharingController.DESKTOP);
		this.publisherData.setRtmpStreamId(ConferenceGlobals.getDTPStreamId());
//		appHandle = 0;
		PublisherInterfaceManager.getManager().startDesktopShare(this.publisherData,appShareListener);
	}
	private	void	startPPTSharing(UIResourceObject res)
	{
		if (this.resourceSharingDisplay != null)
		{
			this.resourceSharingDisplay.onSharingStarted(res);
			this.currentSharedResource = res;
		}
		ConferenceGlobals.setCurrentSharedResource(res);
		this.resourceList.showResourceBeingShared(ConferenceGlobals.getCurrentSharedResource());
		ClientModel.getClientModel().getPPTSharingModel().startPresentation(res.getResourceId(),res.getLastSlideIndex());
	}
	private	void	stopPPTPresentation()
	{
		if (this.resourceSharingDisplay != null)
		{
			this.resourceSharingDisplay.onSharingStopped(this.currentSharedResource);
		}
		ClientModel.getClientModel().getPPTSharingModel().stopPresentation(currentSharedResource.getResourceId());
		this.currentSharedResource = null;
		if (null != this.resourceList)
	    {
		    this.resourceList.showResourceSharingStopped();
	    }
		ConferenceGlobals.setCurrentSharedResource(null);
	}
	private	void	startWhiteboardShare(UIResourceObject res)
	{
		if (this.resourceSharingDisplay != null)
		{
			this.resourceSharingDisplay.onSharingStarted(res);
			this.currentSharedResource = res;
		}
			ClientModel.getClientModel().getWhiteboardModel().startWhiteboard("xx");
			ConferenceGlobals.setCurrentSharedResource(res);
			this.resourceList.showResourceBeingShared(ConferenceGlobals.getCurrentSharedResource());
	}
	private	void	stopWhiteboardShare()
	{
		if (this.resourceSharingDisplay != null)
		{
			this.resourceSharingDisplay.onSharingStopped(this.currentSharedResource);
		}
		this.currentSharedResource = null;
		
		ClientModel.getClientModel().getWhiteboardModel().stopWhiteboard("xx");
		this.resourceList.showResourceSharingStopped();
	}
	private void stopDTPAppShare()
	{
		if (!this.transparentRestart)
		{
			this.currentSharedResource = null;
			this.resourceList.showResourceSharingStopped();
		}
		this.showWaitingForPubResponse(ConferenceGlobals.getDisplayString("stopping.desktop.header","Stopping Computer Screen Sharing"));
		PublisherInterfaceManager.getManager().stopDTPAndAppShare();
	}
	/*private	void	startDTPAppShare()
	{
		onOK();
	}*/
	public	void	onOK()
	{
		CommonUserInformationDialog cuid = CommonUserInformationDialog.getCommonUserInformationDialog();
		if (cuid != null)
		{
	    	cuid.setButtonLabels(UIStrings.getOKLabel(),UIStrings.getCancelLabel());
			cuid.hideOKButton();
			cuid.hideCancelButton();
		}
		this.dtpSharingProcessFlag = 1;
//		if (this.desktopShareMessageDialog != null)
//		{
//			this.desktopShareMessageDialog.hideOKButton();
//			this.desktopShareMessageDialog.hideCancelButton();
//		}
		
		UIResourceObject desktop = ResourceGlobals.getResourceGlobals().getDesktopResource();
		desktop.setMediaId("0");
		
		this.publisherData.setHttpUrl(ConferenceGlobals.baseWebappURL+"ScreenShare.action");
		this.publisherData.setResourceId(desktop.getResourceId());
//		this.publisherData.setStr3(desktop.getMediaId());
		this.publisherData.setCode(ResourceSharingController.DESKTOP);
		this.publisherData.setInt1(0);
		nextStepData = new WaitAndContinueData(this.publisherData);
		
		if(this.getShareKey(this.currentSharedResource).equals(ResourceSharingController.SHARE_PPT_PRESENTATION))
		{
			stopPPTPresentation();
		}
		
		if (this.getShareKey(this.currentSharedResource).equals(ResourceSharingController.WHITEBOARD))
		{
			stopWhiteboardShare();
		}
		if (this.getShareKey(this.currentSharedResource).equals(ResourceSharingController.COBROWSE))
		{
			stopCobrowse(this.currentSharedResource);
		}
		
		startDesktopShare();
	}
	public	void	onCancel()
	{
	}
	public	void	showFileSelector(SelectFileDialogue safd)
	{
		this.safd = safd;
		if (safd != null)
		{
			safd.drawDialog();
		}
	}
	
	public	void	continueAppControlShare(String applicationName, final String fileName)
	{
	}
	public	void	cancelAppControlShare()
	{
	}
	/**
	 * The callback return could be either from sharing or application selector.
	 * If the application selector returned a valid windows handle, start a share
	 * for that application. If not do nothing, as the user must have cancelled
	 * out of the application selector.
	 */
	public	void	continueWork(WaitAndContinueData listenerData)
	{
	}
	
	/**
	 * This method is called after a ppt is uploaded to DMS
	 * @param pptID
	 */
	public	void	continueWorkAfterDMS(String pptID)
	{
	      //Window.alert("inside .... continueWorkAfterDMS pptID = "+pptID);
	      resourceManager.createNewResource("Presentation "+(pcounter++),
				UIConstants.RESOURCE_TYPE_PRESENTATION, pptID, "x");
	}
	
	/**
	 * This method is called to add cobrowse res
	 * @param pptID
	 */
	public	void	addCobRes(String url, CommandExecProgressListener listener)
	{
	      //Window.alert("inside .... addCobRes getLocation = "+getLocation());
		resourceManager.setProgressListener(listener);
	    resourceManager.createNewCobResource(url, getLocation());
	}
	
	protected   boolean isShareDesktopActive()
	{
		return	PublisherInterfaceManager.getManager().isDesktopSharingActive();
	}
	
	public void onPptBroadcasterStopEvent()
	{
		this.stopSharing();
	}
//	public	void	stopRunningShares()
//	{
//		
//	}
	/**
	 * Methods to show and hide the wait panel for the pub response.
	 */
	public	void	showWaitingForPubResponse(String message)
	{
		String waitAMoment = ConferenceGlobals.getDisplayString("stopping.desktop.desc","This may take a moment. Please wait.");
		CommonUserInformationDialog cuid = CommonUserInformationDialog.getCommonUserInformationDialog();
		if (cuid == null)
		{
			cuid = CommonUserInformationDialog.getCommonUserInformationDialog(message,waitAMoment);
	    	cuid.drawDialog();
		}
		else
		{
			cuid.setMessage(message,waitAMoment);
		}
    	cuid.setButtonLabels(UIStrings.getOKLabel(),UIStrings.getCancelLabel());
		cuid.hideOKButton();
		cuid.hideCancelButton();
		if (this.pendingShareListener != null)
		{
			cuid.addPopupListener(this.pendingShareListener);
		}
//		if (desktopShareMessageDialog != null)
//		{
//			desktopShareMessageDialog.setMessage(message);
//		}
//		else if (DefaultCommonDialog.getDialog() != null)
//		{
//			DefaultCommonDialog.getDialog().setMessageText(message);
//		}
//		else
//		{
//			DefaultCommonDialog.showMessage(message, ConferenceGlobals.getDisplayString("stopping.desktop.desc","This may take a moment. Please wait.")
//					,this.pendingShareListener);
//			DefaultCommonDialog.getDialog().hideCloseButton();
//		}
	}
	public	void	pubResponseReceived(boolean error, String header, String message)
	{
		CommonUserInformationDialog cuid = CommonUserInformationDialog.getCommonUserInformationDialog();
		if (cuid == null)
		{
			cuid = CommonUserInformationDialog.getCommonUserInformationDialog(header,message);
	    	cuid.drawDialog();
		}
		else
		{
			cuid.setMessage(header,message);
		}
    	cuid.hideOKButton();
    	cuid.setButtonLabels(UIStrings.getOKLabel(),UIStrings.getCancelLabel());
		if (this.dtpSharingProcessFlag == 1)
		{
			//	Start sequence in progress. it has finished with a message from the pub
			this.dtpSharingProcessFlag = 0;
			if (error)
			{
				cuid.setMessage(header,message);
				cuid.showCancelButton();
			}
			else
			{
				UIResourceObject desktop = ResourceGlobals.getResourceGlobals().getDesktopResource();
				ConferenceGlobals.setCurrentSharedResource(desktop);
				this.resourceList.showResourceBeingShared(desktop);
				this.currentSharedResource = desktop;
				if (this.dtpRestartListener != null)
				{
					this.dtpRestartListener.dtpRestarted(recordingOn, true);
				}
				else
				{
					CommonUserInformationDialog.hideCommonUserInformationDialog();
				}
			}
//			desktopShareMessageDialog = null;
		}
		else
		{
			//	Stop in progress with or without a required immediate start.
			if (error)
			{
				this.resourceList.showResourceSharingStopped();
				cuid.setMessage(header,message);
				cuid.setButtonLabels(UIStrings.getOKLabel(),UIStrings.getOKLabel());
				cuid.hideOKButton();
				this.currentSharedResource = null;
				if (this.transparentRestart)
				{
					this.transparentRestart = false;
					if (this.dtpRestartListener != null)
					{
						this.dtpRestartListener.dtpRestarted(recordingOn, false);
					}
				}
//				if (DefaultCommonDialog.getDialog() != null)
//				{
//					DefaultCommonDialog.getDialog().setMessageText(message);
//				}
//				else
//				{
//					DefaultCommonDialog.showMessage(header, message, this.pendingShareListener);
//				}
				this.dtpSharingProcessFlag = 0;
			}
			else if (this.transparentRestart)
			{
//				DefaultCommonDialog.hideMessageBox();
				this.transparentRestart = false;
				this.dtpSharingProcessFlag = 1;
				this.startDesktopShare();
			}
			else
			{
				//if (this.currentSharedResource == null)
				//{
				//here we have to set the sharing resource to be null
				if (this.dtpSharingProcessFlag == 2 || this.dtpSharingProcessFlag == 0)
				{
					this.currentSharedResource = null;
					this.resourceList.showResourceSharingStopped();
				}
				this.dtpSharingProcessFlag = 0;
				CommonUserInformationDialog.hideCommonUserInformationDialog();
				//}
//				DefaultCommonDialog.hideMessageBox();
			}
		}
	}
	public ResourceList getResourceList()
	{
		return resourceList;
	}
	public void setResourceList(ResourceList resourceList)
	{
		this.resourceList = resourceList;
	}
	private	void	startPendingShare()
	{
		if (this.appShareTimer != null)
		{
			this.appShareTimer.schedule(2);
			Timer t = new Timer()
			{
				public void run()
				{
					appShareTimer = null;
				}
			};
			t.schedule(10);
		}
	}
	public String nowSharing()
	{
		return null;
	}
	public void stopSharing()
	{
		if (this.currentSharedResource != null )
		{
			//	The resource is already being shared. Stop the sharing.
			String newKey = getShareKey(currentSharedResource);
			stopSharing(newKey,currentSharedResource);
		}
		//Also try to stop upload if any
		if(safd != null)
		{
			safd.cancelUpload();
			safd = null;
		}
	}
	//	Following entire sequence is required for transperent restart of the dtp
	//	when recording is switched on or off. This must be done for both cases
	//	because a recorded file must be for the recording start - stop boundary
	//	only. Hence when the recording is started current dtp stream is stopped and
	//	a new one is created, which in turn is stopped when recording is stopped.
	public	boolean	isDTPSharingActive()
	{
		return	isShareDesktopActive();
	}
	public	void	restartDTPwithRecord(DTPRestartListener dtpRestartListener)
	{
		this.publisherData.setRecordingFlags(recordingFlags);
		this.dtpRestartListener = dtpRestartListener;
		restartDTP();
	}
	public	void	restartDTPwithoutRecord(DTPRestartListener dtpRestartListener)
	{
		this.publisherData.setRecordingFlags("");
		this.dtpRestartListener = dtpRestartListener;
		restartDTP();
	}
	public	void	removeDTPRestartListener()
	{
		this.dtpRestartListener = null;
	}
	private	void	restartDTP()
	{
		if (this.isShareDesktopActive())
		{
			this.transparentRestart = true;
			this.dtpSharingProcessFlag = 3;
			this.stopDTPAppShare();
		}
	}
	public boolean isRecordingOn()
	{
		return recordingOn;
	}
	public void setRecordingOn(boolean recordingOn)
	{
		this.recordingOn = recordingOn;
	}
	
	class PubInstallListener implements ConfirmationListener
	{
		UserCallbacks ucb = null;
		UIRosterEntry me = null;
		public PubInstallListener(UserCallbacks ucb, UIRosterEntry me)
		{
			this.ucb = ucb;
			this.me = me;
		}
		public void onCancel()
		{
		}
		public void onOK()
		{
			if(null != ucb)
			{
				ucb.removeWindowListener();	
			}
			ActivePresenterAVManager.getPresenterAVManager(me).stopPresenterAV();
			String url = ConferenceGlobals.baseWebappURL+"html/envcheck/publisherinstall.action?sessionKey="+ConferenceGlobals.sessionKey;
			
			//Window.alert("redirect the browser to get pub installed url = "+url);
			setLocation(url);
		}
	}
	
	private native void setLocation(String url) /*-{
		$wnd.location = url;
	}-*/;
	
	private native String unescape(String url) /*-{
		return	$wnd.getEscapedUrl(url);
	}-*/;
	
	private native String getLocation() /*-{
		return	(escape($wnd.location));
	}-*/;
}
