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

import java.util.Vector;

import com.dimdim.conference.ui.common.client.UIGlobals;
import com.dimdim.conference.ui.common.client.util.DebugPanel;
import com.dimdim.conference.ui.common.client.util.FlashCallbackHandler;
import com.dimdim.conference.ui.common.client.util.FlashStreamHandler;
import com.dimdim.conference.ui.common.client.util.GlassPanelWithSize;
import com.dimdim.conference.ui.json.client.ResponseAndEventReader;
import com.dimdim.conference.ui.json.client.UIPopoutPanelData;
import com.dimdim.conference.ui.json.client.UIResourceObject;
import com.dimdim.conference.ui.json.client.UIRosterEntry;
import com.dimdim.conference.ui.model.client.ClientModel;
import com.dimdim.conference.ui.model.client.ConferenceGlobals;
import com.dimdim.conference.ui.model.client.PPTSharingModel;
import com.dimdim.conference.ui.model.client.PopoutPanelProxy;
import com.dimdim.conference.ui.model.client.PopoutSupportingPanel;
import com.dimdim.conference.ui.model.client.ResourceSharingDisplay;
import com.dimdim.conference.ui.panels.client.CobrowseWidget;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * This panel simply switches between various shares. Only one of the sharing
 * panels can be active at a given time.
 * 
 * This panel manages the resize and redisplay of panels based on the browser
 * type, because ie does not stop the movies from playing, where as firefox
 * and safari so.
 * 
 * The width and height does not need to be exact. The margin and padding
 * treatment that causes misalignmens in firefox and safari
 * 
 * 
 */

public class ResourceSharingPanel extends	HorizontalPanel	implements
	CollaborationWidgetContainer, FlashStreamHandler, PopoutSupportingPanel, ResourceSharingDisplay
{

	protected	UIRosterEntry			me;
	
	protected	ScrollPanel				scrollPanel;
	protected	DTPPlayerFrame			dtpPlayerFrame;
	protected	PPTBroadcasterPanel		pptBroadcasterPanel;
	protected	PPTPlayerPanel			pptPlayerPanel;
	protected	WaitingPageFrame		waitingPageFrame;
	protected	WhiteboardPanel			whiteboardPanel;
	protected	CobrowseSharingFrame	coBrowseFrame;
	
	protected	int		lastKnownWidth;
	protected	int		lastKnownHeight;
	
	protected	Widget				activeCollaborationWidget;
	protected	UIResourceObject	activeResource;
	
	protected	CollaborationResources	collaborationesources;
//	protected	ResourceSharingCallbacksListener	resourceSharingCallbacksListener;
	private		PPTSharingModel		pptSharingModel;
	
	private	Vector	resourceSharingCallbacksListeners = new Vector();
	
	String resNameDivId = "";
	GlassPanelWithSize glassPane = null;
	boolean isLocked = false;
	private CobrowseWidget cobWidget;
	
	protected	SharingStatusListener	sharingStatusListener;
	
	public	ResourceSharingPanel(UIRosterEntry me, int lastKnownWidth, int lastKnownHeight, String resNameDivId)
	{
		this.me = me;
		this.lastKnownWidth = lastKnownWidth;
		this.lastKnownHeight = lastKnownHeight;
		this.resNameDivId = resNameDivId;
		
		this.collaborationesources = new CollaborationResources();
		this.scrollPanel = new ScrollPanel();
		this.waitingPageFrame = new WaitingPageFrame();
		this.waitingPageFrame.setContainer(this);
		
		this.coBrowseFrame = new CobrowseSharingFrame();
		this.coBrowseFrame.setContainer(this);
		
		this.pptSharingModel = ClientModel.getClientModel().getPPTSharingModel();
		
		//	By default the panel points to the waiting page.
		this.add(this.scrollPanel);
		this.scrollPanel.add(this.waitingPageFrame);
		
		String url = getParamAddedUrl();
		this.waitingPageFrame.refreshWidget(null,lastKnownWidth,lastKnownHeight, url);
		this.activeCollaborationWidget = this.waitingPageFrame;
		writeResName(false, null, null);
	}
	public SharingStatusListener getSharingStatusListener()
	{
		return sharingStatusListener;
	}
	public void setSharingStatusListener(SharingStatusListener sharingStatusListener)
	{
		this.sharingStatusListener = sharingStatusListener;
	}
	private String getParamAddedUrl() {
		String url = waitingPageFrame.container.getCollaborationesources().getWaitingPageURL();
		String paramToAdd = "";
		if(url.indexOf('?') != -1)
		{
			paramToAdd = "&";
		}else{
			paramToAdd = "?";
		}
		if(this.me.isHost())
		{
			url = url+paramToAdd+"role=host";
		}else{
			url = url+paramToAdd+"role=attendee";
		}
		url = url + "&cflag="+Random.nextInt();
		return url;
	}
	
	public	void	resizePanel()
	{
		this.resizePanel(this.lastKnownWidth, this.lastKnownHeight);
	}
	public	void	resizePanel(int newWidth, int newHeight)
	{
		//Window.alert("Resizing resource sharing panel to: "+newWidth+"--"+newHeight);
		this.lastKnownWidth = newWidth;
		this.lastKnownHeight = newHeight;
		this.scrollPanel.setSize(newWidth+"px", newHeight+"px");
		((CollaborationWidget)this.activeCollaborationWidget).refreshWidget(this.activeResource,newWidth,newHeight);
		if(glassPane != null)
		{
			glassPane.refreshSize(this.lastKnownWidth, this.lastKnownHeight);
		}
	}
	public	boolean	isSharingActive()
	{
		return	this.activeResource != null;
	}
	public CollaborationResources getCollaborationesources()
	{
		return collaborationesources;
	}
	public int getContainerHeight()
	{
		return lastKnownHeight;
	}
	public int getContainerWidth()
	{
		return lastKnownWidth;
	}
	public UIResourceObject getActiveResource()
	{
		return activeResource;
	}
	public void addResourceSharingCallbacksListener(ResourceSharingCallbacksListener res)
	{
		this.resourceSharingCallbacksListeners.addElement(res);
	}
	/**
	 * 
	 * @param res
	 */
	public	void	onSharingStarted(UIResourceObject res)
	{
		//Window.alert("inside ResourceSharingPanel ResourceSharingPanel::onSharingStarted:"+res);
		//Window.alert("inside ResourceSharingPanel activeResource = "+this.activeResource);
		if (this.activeResource == null)
		{
			this.activeResource = res;
			if (res.getResourceType().equals(UIResourceObject.RESOURCE_TYPE_DESKTOP))
			{
				//Window.alert("Starting desktop share");
				if (this.dtpPlayerFrame == null)
				{
					this.dtpPlayerFrame = new DTPPlayerFrame();
					this.dtpPlayerFrame.setContainer(this);
				}
				this.scrollPanel.remove(this.activeCollaborationWidget);
				this.scrollPanel.add(this.dtpPlayerFrame);
				this.activeCollaborationWidget = this.dtpPlayerFrame;
				this.dtpPlayerFrame.refreshWidget(res,this.lastKnownWidth,this.lastKnownHeight);
				writeResName(true, null, null);
			}
			else if (res.getResourceType().equals(UIResourceObject.RESOURCE_TYPE_PRESENTATION))
			{
				//Window.alert("Starting ppt share");
				if (this.me.isActivePresenter())
				{
					if (this.pptBroadcasterPanel == null)
					{
						this.pptBroadcasterPanel = new PPTBroadcasterPanel();
						this.pptBroadcasterPanel.setContainer(this);
					}
					this.scrollPanel.remove(this.activeCollaborationWidget);
					this.scrollPanel.add(this.pptBroadcasterPanel);
					this.activeCollaborationWidget = this.pptBroadcasterPanel;
					this.pptBroadcasterPanel.refreshWidget(res,this.lastKnownWidth,this.lastKnownHeight);
					
					FlashCallbackHandler.getHandler().addStreamHandler(this);
//					this.pptSharingModel.startPresentation(res.getResourceId(),res.getLastSlideIndex());
				}
				else
				{
					if (this.pptPlayerPanel == null)
					{
						this.pptPlayerPanel = new PPTPlayerPanel();
						this.pptPlayerPanel.setContainer(this);
					}
					this.scrollPanel.remove(this.activeCollaborationWidget);
					this.scrollPanel.add(this.pptPlayerPanel);
					this.activeCollaborationWidget = this.pptPlayerPanel;
					this.pptPlayerPanel.refreshWidget(res,this.lastKnownWidth,this.lastKnownHeight);
				}
				String resourceName = res.getResourceName();//this.getResourceName(res.getResourceId());
				writeResName(false, null, resourceName);
			}
			else if (res.getResourceType().equals(UIResourceObject.RESOURCE_TYPE_WHITEBOARD))
			{
				//	If the whiteboard panel was never initialized, initialize it.
				if (this.whiteboardPanel == null)
				{
					this.whiteboardPanel = new WhiteboardPanel();
					this.whiteboardPanel.setContainer(this);
				}
				if (this.me.isActivePresenter())
				{
					this.whiteboardPanel.setRole("P");
				}
				else
				{
					this.whiteboardPanel.setRole("A");
				}
				this.scrollPanel.remove(this.activeCollaborationWidget);
				this.scrollPanel.add(this.whiteboardPanel);
				this.activeCollaborationWidget = this.whiteboardPanel;
				this.whiteboardPanel.refreshWidget(res,this.lastKnownWidth,this.lastKnownHeight);
				writeResName(false, UIResourceObject.RESOURCE_TYPE_WHITEBOARD, null);
			}else if(res.getResourceType().equals(UIResourceObject.RESOURCE_TYPE_COBROWSE)){
				//Adding co browse url
				try
				{
					this.scrollPanel.remove(this.activeCollaborationWidget);
				}
				catch(Throwable t)
				{
	
				}
				this.scrollPanel.add(this.coBrowseFrame);
				this.activeCollaborationWidget = this.coBrowseFrame;
				syncCobUrl(res);
			}
			if (this.sharingStatusListener != null)
			{
				this.sharingStatusListener.resourceSharingStarted(res);
			}
		}
		else
		{
			//	Active sharing must be explicitly stopped by the controller before
			//	starting another sharing. This level cannot implement switchover
			//	transperancy because some of the elements may involve timing issues
			//	and only one object in the system can be given the responsibility
			//	of managing switchover.
//			Window.alert("Current sharing must be stopped");
			writeResName(false, null, null);
		}
	}
	
	public void syncCobUrl(){
		
		//Window.alert("inside sync cobrowse... "+this.activeResource);
		if(this.activeResource != null)
		{
			UIResourceObject currentRes = this.activeResource;
			this.activeResource = null;
			ClientModel.getClientModel().getCobrowseModel().syncCobrowse("xx", currentRes, this);
		}
	}
	
	public void navigateTo(String state){
		
		//Window.alert("inside sync cobrowse... "+this.activeResource);
		if(this.activeResource != null)
		{
			UIResourceObject currentRes = this.activeResource;
			this.activeResource = null;
			ClientModel.getClientModel().getCobrowseModel().navigateTo("xx", currentRes, this, state);
		}
	}
	
	public void lock(boolean lock) {
		//Window.alert("inside resource sharing panel lock the collab area lock = "+lock +"and isLocked = "+isLocked);
		//coBrowseFrame.lock(lock);
		removeGlassPane();

		if(this.isVisible())
		{
			if(lock)
			{
				//Window.alert("inside res sharing panel ... locking");
				//Window.alert("this.lastKnownWidth = "+this.lastKnownWidth+" this.lastKnownHeight ="+this.lastKnownHeight);
				//Window.alert("this.coBrowseFrame.getAbsoluteLeft() = "+this.coBrowseFrame.getAbsoluteLeft()+" this.coBrowseFrame.getAbsoluteTop() ="+this.coBrowseFrame.getAbsoluteTop());
				DebugPanel.getDebugPanel().addDebugMessage("this.coBrowseFrame.getAbsoluteLeft() = "+this.coBrowseFrame.getAbsoluteLeft()+" this.coBrowseFrame.getAbsoluteTop() ="+this.coBrowseFrame.getAbsoluteTop());
				//add glass panel only if he is not active presenter
				if(!this.me.isActivePresenter())
				{
					glassPane = new GlassPanelWithSize(this.lastKnownWidth, this.lastKnownHeight);
					glassPane.show(this.coBrowseFrame.getAbsoluteLeft(), this.coBrowseFrame.getAbsoluteTop());
				}

				isLocked = true;
			}else{
				isLocked = false;
			}
		}
		
		if(null != cobWidget)
		{
			//Window.alert("inside resource sharing panel changing the lock icon"+isLocked);
			cobWidget.setLockIcon(isLocked);
		}
	}

	private void removeGlassPane()
	{
		if(glassPane != null)
		{
			glassPane.hide();
		}
		glassPane = null;
	}
	
	public void setVisible(boolean visbility)
	{
		//Window.alert("setting res players visibility.."+visbility);
		super.setVisible(visbility);
		setLockMask();
	}
	
	private void setLockMask() {
		//if the res being shared in of cob and 
		//set the locking mask here...
		if(this.getActiveResource() != null)
		{
			if(UIResourceObject.RESOURCE_TYPE_COBROWSE.equals(this.getActiveResource().getResourceType()) )
			{
				//if ann_on it means res is locked..
				if(this.getActiveResource().getAnnotation().equals(UIResourceObject.ANNOTATION_ON))
				{
					//Window.alert("locking it up");
					this.lock(true);
				}else{
					//Window.alert("unlocking it..");
					this.lock(false);
				}
			}
		}
	}
	
	private void syncCobUrl(UIResourceObject res) {
		String url = "http://"+ConferenceGlobals.userInfoDictionary.getStringValue("dms_cob_server_address")+"/content/"+res.getUrl();
		//String url = ConferenceGlobals.baseWebappURL+"cob/"+ConferenceGlobals.conferenceKey+"/"+res.getResourceId();
		//Window.alert("inside sync cobrowse... "+this.activeResource);
		if(UIGlobals.isActivePresenter(me))
		{
			url += "/p_cache.html";
		}else{
			url += "/a_cache.html";
		}
		//Window.alert("setting the url to "+url);
		this.coBrowseFrame.setUrl(url+"?cflag="+Random.nextInt());
		this.resizePanel();
		//writeResName(false, null, res.getResourceName());
		writeCobResName(res.getResourceName());
		setLockMask();
	}
	
	protected	String	getResourceName(String resourceId)
	{
		String name = "";
		UIResourceObject res = ClientModel.getClientModel().
				getResourceModel().findResourceObject(resourceId);
		if (res != null)
		{
			name = res.getResourceName();
		}
		return	name;
	}
	public	void	onSharingStopped(UIResourceObject res)
	{
//		Window.alert("ResourceSharingPanel::onSharingStopped:"+res);
		if (activeResource != null)
		{
			((CollaborationWidget)this.activeCollaborationWidget).widgetHiding();
			if (activeResource.getResourceType().equals(UIResourceObject.RESOURCE_TYPE_PRESENTATION))
			{
				if (this.me.isActivePresenter())
				{
					FlashCallbackHandler.getHandler().removeStreamHandler(activeResource.getResourceId());
//					this.pptSharingModel.stopPresentation(res.getResourceId());
				}
			}else if (activeResource.getResourceType().equals(UIResourceObject.RESOURCE_TYPE_COBROWSE))
			{
				String url = getParamAddedUrl();
				coBrowseFrame.setUrl(url);
			}
			this.restoreWaitPage();
			this.activeResource = null;
			if (this.sharingStatusListener != null)
			{
				this.sharingStatusListener.resourceSharingStopped();
			}
		}
		else
		{
			//	This might be a redundant duplicate call or second event.
			//	Should never happen.
		}
		writeResName(false, null, null);
		
		//removing the glas panel
		lock(false);
	}
	/**
	 * The sharing panels do not accept any specific updates during their lifetime,
	 * except the ppt player panel, which must change the slides.
	 * 
	 * @param res
	 */
	public	void	onSharingUpdated(UIResourceObject res)
	{
		if (this.activeResource != null && this.pptPlayerPanel != null &&
				activeResource.getResourceType().
					equals(UIResourceObject.RESOURCE_TYPE_PRESENTATION))
		{
			if (!this.me.isActivePresenter())
			{
				this.pptPlayerPanel.changeSlide(this.activeResource.getLastSlideIndex());
			}
			else
			{
//				Window.alert("On the presenter console");
			}
		}
		else
		{
//			Window.alert("No active resource");
		}
	}
	protected	void	restoreWaitPage()
	{
		try
		{
			this.scrollPanel.remove(this.activeCollaborationWidget);
		}
		catch(Throwable t)
		{
			
		}
		this.scrollPanel.add(this.waitingPageFrame);
		this.activeCollaborationWidget = this.waitingPageFrame;
		this.resizePanel();
	}
	
	/**
	 * Interface to listen to the events generated by the slide change within the
	 * ppt broadcaster.
	 */
	public String getStreamName()
	{
		if (this.activeResource != null)
		{
			return	this.activeResource.getResourceId();
		}
		return	"PPT";
	}
	public void handleEvent(String eventName)
	{
//		Window.alert("Received event from flash: "+eventName);
		if (eventName.startsWith("stop"))
		{
			int size = this.resourceSharingCallbacksListeners.size();
			for (int i=0; i<size; i++)
			{
				((ResourceSharingCallbacksListener)
						this.resourceSharingCallbacksListeners.elementAt(i)).onPptBroadcasterStopEvent();
			}
		}
		else if (eventName.startsWith(UIResourceObject.ANNOTATION_ON))
		{
			//	Obsolete event
		}
		else if (eventName.startsWith(UIResourceObject.ANNOTATION_OFF))
		{
			//	Obsolete event
		}
		else if (this.activeResource != null)
		{
			int	slide = (new Integer(eventName)).intValue();
			this.activeResource.setLastSlideIndex(slide);
			this.pptSharingModel.changePresentationSlide(this.activeResource.getResourceId(),slide);
		}
	}
	
	public void writeCobResName(String url)
	{
		
		/*if(this.me.isActivePresenter())
		{
			writeResName(false, null, "");
			//write in the text box
			if(cobWidget != null)
			{
				cobWidget.setTextBoxValue(url);
			}
		}else{*/
			//Window.alert("write the res name.."+url);
		writeResName(false, null, url);
		//}
	}
	private void writeResName(boolean dtpShare, String appName, String pptName)
	{
		RootPanel rp = RootPanel.get(resNameDivId);
		//Window.alert("resNameDivId = "+ resNameDivId);
		if(null != rp)
		{
			Label headerLabel = new Label();;
			if (headerLabel != null)
			{
				if (dtpShare)
				{
					//	Presenter is sharing desktop.
					headerLabel.setText(UIGlobals.getDesktopSubTabLabel());
				}
				else if (appName != null && UIResourceObject.RESOURCE_TYPE_WHITEBOARD.equals(appName))
				{
					//	Presenter is sharing a specific application
					headerLabel.setText(UIGlobals.getWhiteboardTabLabel());
					
				}
				else if (appName != null)
				{
					//	Presenter is sharing a whiteboard
					headerLabel.setText(appName);
				}
				else if (pptName != null)
				{
					//	Presenter is sharing a powerpoint presentation
					headerLabel.setText(pptName);
				}
				else
				{
					//	No sharing is in progress at present.
					headerLabel.setText(UIGlobals.getWorkspaceHeaderText());
				}
			}
			rp.clear();
			//Window.alert("adding label text = "+headerLabel.getText());
			rp.add(headerLabel);
		}
		else
		{
			//Window.alert("div tag is null...");
		}
	}
	
	/**
	 * Popout support. Same panel is used in the popout. These implementations allow
	 * user to move the panel from main console to popout and back.
	 */
	
	protected	boolean		inConsole = true;
	protected	boolean		inPopout = false;
	protected	boolean		consolePanelPoppedOut = false;
	
	protected	PopoutPanelProxy	popoutPanelProxy;
	protected	ResponseAndEventReader	jsonReader = new ResponseAndEventReader();

	
	public String getPanelId()
	{
		return "panel.resourceSharingPlanel";
	}
	public void panelPopedOut()
	{
		this.consolePanelPoppedOut = true;
	}
	public void setPopoutPanelProxy(PopoutPanelProxy popoutPanelProxy)
	{
		this.popoutPanelProxy = popoutPanelProxy;
	}
	public void panelInConsole()
	{
		this.inConsole = true;
		this.inPopout = false;
	}
	public void panelInPopout()
	{
		this.inConsole = false;
		this.inPopout = true;
	}
	public	boolean	isInConsole()
	{
		return	this.inConsole;
	}
	public	boolean	isInPopout()
	{
		return	this.inPopout;
	}
	public void receiveMessageFromPopout(UIPopoutPanelData msg)
	{
	}
	public String getPanelData()
	{
		//	A simple safety precaution.
//		Window.alert("Popout asking for panel data");
		String str = "no_data";
		if (this.inConsole)
		{
//			Window.alert("We are in console");
			if (this.activeResource != null)
			{
				str = this.activeResource.toJson();
//				Window.alert("Sending data: "+str);
			}
			else
			{
//				Window.alert("Now stored event to send");
			}
		}
		else
		{
//			Window.alert("We are in popout");
		}
		return this.encodeBase64(str);
	}
	public void readPanelData(String dataText)
	{
		//	A simple safety precaution.
//		Window.alert("In popped out resource player: ");
//		if (this.inPopout)
//		{
			String s = this.decodeBase64(dataText);
//			Window.alert("In popped out resource player: "+s);
			if (!s.equals("no_data"))
			{
//				Window.alert("In popped out resource player: "+s);
				try
				{
					JSONValue jsonObject = JSONParser.parse(s);
					if (jsonObject != null)
					{
//						Window.alert("-"+jsonObject+"-");
						JSONObject jObj = jsonObject.isObject();
						if (jObj != null)
						{
							UIResourceObject res = (UIResourceObject)jsonReader.readObject(jObj);
							if (res != null)
							{
								this.activeResource = null;
								this.onSharingStarted(res);
							}
							else
							{
//								Window.alert("JSON parsing error");
							}
						}
					}
				}
				catch(Exception e)
				{
//					Window.alert(e.getMessage());
				}
			}
//		}
	}
	protected	native	String	encodeBase64(String s) /*-{
		return $wnd.Base64.encode(s);
	}-*/;
	protected	native	String	decodeBase64(String s) /*-{
		return $wnd.Base64.decode(s);
	}-*/;
	public void setCobWidget(CobrowseWidget cobWidget) {
		this.cobWidget = cobWidget;
		
	}
	public	CollaborationWidgetDisplayPanel	getMoviePanel()
	{
		CollaborationWidgetDisplayPanel panel = null;
		try
		{
			panel = (CollaborationWidgetDisplayPanel)this.activeCollaborationWidget;
		}
		catch(Exception e)
		{
		}
		return	panel;
	}
}
