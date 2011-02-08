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

package com.dimdim.conference.ui.model.client;

import	java.util.HashMap;
import	com.dimdim.conference.ui.json.client.*;
import com.google.gwt.user.client.Window;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * This class, maintains the client data model using many ther smaller model
 * classes for individual features and their significant components.
 * 
 * The client model contains following sub models:
 * 
 * Roster, always available. May have minimum of 1 entry for the self.
 * Resources, always available but may be empty
 * Question Manager, if active
 * Any number of running Chats, if active
 * Current Presentation, if active
 * Current Screen Sharing, if active,
 * Current Poll, if active,
 * Any number of Videos, if active. Some of the conferneces may restrict the
 * 		active videos to 1.
 * 
 * Five specific models and their companion listeners.
 * 
 * UserModel, RosterModel, ResourceModel, QuestionManagerModel, ChatModel
 * 
 * RosterModelListener,
 * UserModelListener,
 * ResourceModelListener,
 * QuestionManagerListener,
 * ChatModelListener
 * 
 * The model also maintains a tools registry for managing functionality.
 * Each tool represents functions that may be purely local or server
 * calls or both. The functionality available to user at any point
 * depends on the user's role and current permissions.
 * 
 */
public class ClientModel
{
	
	private	static	ClientModel	clientModel = null;
	
	public	static	ClientModel	getClientModel()
	{
		return	ClientModel.clientModel;
	}
	public	static	void	createClientModel()
	{
		ClientModel.clientModel = new ClientModel();
	}
	
//	protected	HashMap		featureModels;
	
	protected	RosterModel				roster;
//	protected	UserModel				localUser;
	protected	ChatManagerModel		chatManager;
	protected	ResourceModel			resources;
	protected	AVModel					avModel;
	protected	AudioModel				audioModel;
	protected	SharingModel			sharingModel;
	protected	PPTSharingModel			pptSharingModel;
	protected	ClientStateModel		clientStateModel;
	protected	PopoutModel			popoutModel;
	protected	SettingsModel		settingsModel;
	protected	WhiteboardModel		whiteboardModel;
	protected	RecordingModel		recordingModel;
	protected	CoBrowseModel		cobrowseModel;
	
	protected	LocalEventsModel	localEventsModel;
	
	/**
	 * The arraylist contains DimDimFeatureModel for each active chat.
	 * When chats are closed, the model listeners are sent the stopped
	 * event and the model is removed from this list.
	 */
//	protected	ArrayList		chats;
	
	/**
	 * Respective presentation, poll and share objects may exist in the
	 * resources model. A seperate feature model is constructed for them
	 * only when the said feature is activated by a presenter.
	 */
//	protected	DimDimFeatureModel		activePresentation;
//	protected	DimDimFeatureModel		activePoll;
//	protected	DimDimFeatureModel		activeSharing;
	
	private	ClientModel()
	{
//		this.featureModels = new HashMap();
		this.roster = new RosterModel();
//		this.localUser = new UserModel();
//		this.roster.setLocalUserModel(this.localUser);
//		this.featureModels.put(RosterModel.ModelIndex,this.roster);
		
//		Window.alert("c-2");
		this.resources = new ResourceModel();
//		Window.alert("c-3");
		this.chatManager = new ChatManagerModel(this.roster.getCurrentUser());
//		Window.alert("c-4");
		this.clientStateModel = new ClientStateModel();
//		Window.alert("c-5");
		this.avModel = new AVModel(this.roster.getCurrentUser());
//		Window.alert("c-6");
		this.audioModel = new AudioModel(this.roster.getCurrentUser());
//		Window.alert("c-7");
		this.sharingModel = new SharingModel(this.roster.getCurrentUser());
//		Window.alert("c-8");
		this.pptSharingModel = new PPTSharingModel(this.roster.getCurrentUser());
//		Window.alert("c-9");
		this.popoutModel = new PopoutModel(this.roster.getCurrentUser());
//		Window.alert("c-10");
		this.settingsModel = new SettingsModel(this.roster.getCurrentUser());
//		Window.alert("c-11");
		this.whiteboardModel = new WhiteboardModel(this.roster.getCurrentUser());
		this.recordingModel = new RecordingModel(this.roster.getCurrentUser());
//		Window.alert("c-12");
		this.localEventsModel = new LocalEventsModel(this.roster.getCurrentUser());
		
		this.cobrowseModel = new CoBrowseModel(this.roster.getCurrentUser());
	}
	
	public	RosterModel	getRosterModel()
	{
		return	this.roster;
	}
//	public	UserModel	getLocalUserModel()
//	{
//		return	this.localUser;
//	}
	public	ChatManagerModel	getChatManagerModel()
	{
		return	this.chatManager;
	}
	public	ResourceModel	getResourceModel()
	{
		return	this.resources;
	}
	public	AVModel		getAVModel()
	{
		return	this.avModel;
	}
	public	AudioModel	getAudioModel()
	{
		return	this.audioModel;
	}
	public	SharingModel		getSharingModel()
	{
		return	this.sharingModel;
	}
	public	PPTSharingModel		getPPTSharingModel()
	{
		return	this.pptSharingModel;
	}
	public	WhiteboardModel		getWhiteboardModel()
	{
		return	this.whiteboardModel;
	}
	public	RecordingModel		getRecordingModel()
	{
		return	this.recordingModel;
	}
	public	PopoutModel		getPopoutModel()
	{
		return	this.popoutModel;
	}
	public	SettingsModel		getSettingsModel()
	{
		return	this.settingsModel;
	}
	public	String	getConferenceKey()
	{
		return	this.getConferenceName();
	}
	
	public	void	leaveConference()
	{
		this.closePopouts();
		this.roster.leaveConference();
	}
	private native void closePopouts() /*-{
		$wnd.handleConsoleClosedForPopouts();
	}-*/;
	public ClientStateModel getClientStateModel()
	{
		return clientStateModel;
	}
	public LocalEventsModel getLocalEventsModel()
	{
		return localEventsModel;
	}
	/**
	 * Some of the manager objects can not be created up front. Since they must
	 * be created as and when required.
	 * @param manager
	 */
//	public	void	addManager(FeatureModel manager)
//	{
//		this.featureModels.put(manager.getModelIndex(),manager);
//	}
	/**
	 * This method allows the tool clicks to delegate the functional call to
	 * the client model. The client model will associate the call to various
	 * selections or require user to provide additional data.
	 */
//	public	void	onToolClick(int managerOrModelIndex, int functionIndex)
//	{
//		FeatureModel manager = (FeatureModel)this.featureModels.get(new Integer(managerOrModelIndex));
//		if (manager != null)
//		{
//			
//		}
//		return;
//	}
    private String getConferenceName()
    {
		return	ConferenceGlobals.userInfoDictionary.getStringValue("conference_key");
	}
//    private native String getLocationURL()/*-{
//    	return	($wnd.location);
//    }-*/;
	public CoBrowseModel getCobrowseModel() {
		return cobrowseModel;
	}
}
