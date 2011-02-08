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

package com.dimdim.conference.application.core;

//import java.util.Calendar;
import java.io.File;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.ResourceBundle;
import java.util.Locale;

import com.dimdim.conference.ConferenceConsoleConstants;
import com.dimdim.conference.application.portal.PortalServerAdapter;
import com.dimdim.conference.application.presentation.PresentationManager;
//import com.dimdim.conference.application.presentation.dms.DMSPresentationManager;
import com.dimdim.conference.config.ConferenceConfig;
import com.dimdim.conference.db.ConferenceDB;
import com.dimdim.conference.model.IConference;
//import com.dimdim.conference.model.IConferenceStateListener;
import com.dimdim.locale.LocaleManager;
import com.dimdim.util.timer.TimerService;
import com.dimdim.util.timer.TimerServiceTaskId;
import com.dimdim.util.timer.TimerServiceUser;

import com.dimdim.recording.MeetingRecorder;
import com.dimdim.recording.MeetingRecorderProvider;
//import com.dimdim.conference.application.portal.PortalServerAdapter;

import com.dimdim.mailbox.LocalDMSManager;

/**
 * @author	Jayant Pandit
 * @email	Jayant.Pandit@communiva.com
 * 
 * This object manages the running active conferences. It also manages the
 * scheduled conferences by supporing number of queries related to the same.
 */

public class ConferenceManager	//implements	IConferenceStateListener, ActiveConferenceStateChangeListener
{
	protected	static	ConferenceManager	theManager;
	
	public	static	ConferenceManager	getManager()
	{
		if (ConferenceManager.theManager == null)
		{
			ConferenceManager.createManager();
		}
		ConferenceManager.theManager.getMeetingRecorderProvider();
		return	ConferenceManager.theManager;
	}
	protected	synchronized	static	void	createManager()
	{
		if (ConferenceManager.theManager == null)
		{
			ConferenceManager.theManager = new ConferenceManager();
		}
	}
	
	/**
	 * A simple table of all currently running conferences.
	 */
	
	protected	TreeMap		conferences;
	protected	boolean		processStopped = false;
	protected	MeetingRecorderProvider	recordingAdapter;
	protected	transient	TimerServiceTaskId	recordingInitialized;
	
	public ConferenceManager()
	{
		this.conferences = new TreeMap();
	}
	private	void	initTimers()
	{
		//	Create a continuous task for properties and locale files refreshes.
		TimerServiceUser tsu = new TimerServiceUser()
		{
			public long getTimerDelay()
			{
				return  300000;
			}
			public boolean timerCall()
			{
				System.out.println("Checking the config file updates ..........");
				try
				{
					if (!isProcessStopped())
					{
						ConferenceConsoleConstants.reInit();
						ConferenceDB.getDB().rereadUserFiles();
						LocaleManager.getManager().refresh();
					}
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
				return true;
			}
			public void setTimerServiceTaskId(TimerServiceTaskId taskId)
			{
			}
		};
		TimerService.getService().addUser(tsu);
		
		Thread shutdownListener = new Thread()
		{
			public	void	run()
			{
				ConferenceManager.getManager().setProcessStopped(true);
				TimerService.stopService();
			}
		};
		Runtime.getRuntime().addShutdownHook(shutdownListener);
	}
	private	void	getMeetingRecorderProvider()
	{
		if (this.recordingInitialized == null)
		{
			System.out.println("The recorder provider not yet initialized. -- Initializing.......");
			initTimers();
			
			System.out.println("Initializing Local Mailbox Reader ........");
			String localDMSMailbox = ConferenceConsoleConstants.getResourceKeyValue("dms.localMailboxDirectory", "");
			if (localDMSMailbox != null && localDMSMailbox.length() > 0)
			{
				System.out.println("Local Mailbox Reader Available ........ ");
				LocalDMSManager.initLocalDMSManager(localDMSMailbox, ConferenceConsoleConstants.getInstallationId());
			}
			
		try
		{
			ResourceBundle recordingResources = ResourceBundle.getBundle("resources.recording");
			if (recordingResources != null)
			{
				String	recorderProviderClassName = recordingResources.getString("RecordingAdapterClass");
				if (recorderProviderClassName != null)
				{
					Class rpc = Class.forName(recorderProviderClassName);
					this.recordingAdapter = (MeetingRecorderProvider)rpc.newInstance();
					
					String recordingsRootDirectory = recordingResources.getString("RecordingsRootDirectory");
					String RecordingServerAddress = recordingResources.getString("RecordingServerAddress");
//					String FMSServerInstallationDirectory = recordingResources.getString("FMSServerInstallationDirectory");
//					String FMSAVApplicationName = recordingResources.getString("FMSAVApplicationName");
					String portalServerAddress = PortalServerAdapter.getAdapter().getPortalServerInternalAddress();
					String portalServerAction = recordingResources.getString("PortalServerCallbackAction");
					String otherCallBackURL = recordingResources.getString("OtherCallBackUrl");
					if (this.recordingAdapter != null && recordingsRootDirectory != null)
					{
						String webinfDirectory = ConferenceConsoleConstants.getWebInfDirectory();
						String templatesRootDirectory = (new File(webinfDirectory,"recording")).getAbsolutePath();
						this.recordingAdapter.initialiseProvider(
								ConferenceConsoleConstants.getInstallationId(),
								ConferenceConsoleConstants.getInstallationPrefix(),
								templatesRootDirectory,recordingsRootDirectory,RecordingServerAddress,
								portalServerAddress, portalServerAction, otherCallBackURL);
					}
					else
					{
						System.out.println("Recording adapter already created or mailbox root not available");
					}
				}
				else
				{
					System.out.println("Recorder provider class not available");
				}
			}
			else
			{
				System.out.println("No resource bundle available for recording");
			}
		}
		catch(Throwable t)
		{
			t.printStackTrace();
		}
		this.recordingInitialized = new TimerServiceTaskId();
		}
		else
		{
		}
	}
	public boolean isProcessStopped()
	{
		return processStopped;
	}
	public void setProcessStopped(boolean processStopped)
	{
		this.processStopped = processStopped;
	}
	public	synchronized	IConference		createConference(String email,
				String displayName, String userId, String confName, String confKey, String confId, Locale locale)
		throws	KeyInUseException, StreamingServerTooBusy, Exception
	{
		if (this.conferences.get(confKey) != null)
		{
			throw	new	KeyInUseException();
		}
		ConferenceConfig cc = new ConferenceConfig();
		
		cc.setConferenceOwnerId(email);
		cc.setConferenceName(confName);
		cc.setConferenceKey(confKey);
		
		ActiveConference	conference = new ActiveConference(email, displayName, userId, cc, confId);
		this.conferences.put(confKey,conference);
		conference.setConferenceManager(this);
//		conference.addStateListener(this);
		
//		PresentationManager.initPresentationManager();
		PresentationManager.addPresentationManager(confKey);
//				ConferenceConsoleConstants.getPresentationStorageRoot());
		
		if (this.recordingAdapter != null)
		{
			System.out.println("Recording is enabled. Adding meeting recorder.");
			MeetingRecorder recorder = this.recordingAdapter.
				getMeetingRecorder(confKey,"_default",confKey, conference.getConferenceId(),
						conference.getStreamingServer().getAVApplicationStreamsDirectory(),locale.toString());
			conference.addMeetingRecorder(recorder);
		}
//		DMSPresentationManager.addPresentationManager(confKey,
//				ConferenceConsoleConstants.getPresentationStorageRoot());
		
		PortalServerAdapter.getAdapter().reportMeetingStarted(confKey);
		
		return	conference;
	}
	public	IConference	getConference(String confKey)	throws	NoConferenceByKeyException
	{
		IConference conf = (IConference)this.conferences.get(confKey);
		if (conf == null)
		{
			throw	new	NoConferenceByKeyException();
		}
		return	conf;
	}
	/**
	 * This method checks if the available conference is really valid. This is a guard
	 * against a possible bug in close, where the closed conferences are not getting
	 * removed from cache. This method is used by start and join sequence. This method
	 * will remove the conference from the cache immediately 
	 * 
	 * @param confKey
	 * @return
	 * @throws NoConferenceByKeyException
	 */
	public	IConference	getConferenceIfValid(String confKey)	throws	NoConferenceByKeyException
	{
		IConference conf = (IConference)this.conferences.get(confKey);
		if (conf == null)
		{
			throw	new	NoConferenceByKeyException();
		}
		else
		{
			//	Check if the conference is valid. This check currently only involves making
			//	sure that there is at least 1 presenter in the roster.
			try
			{
				int numPresenters = conf.getRosterManager().getRosterObject().getNumberOfPresenters();
				if (numPresenters == 0)
				{
					//if clean cache is successful then throw no conf key else it might be too new
					if(cleanConfCache(conf))
					{
						System.out.println("inside getConferenceIfValid throwing NoConferenceByKeyException");
						throw	new	NoConferenceByKeyException();
					}
				}
			}
			catch(Exception e)
			{
				//if clean cache is successful then throw no conf key else it might be too new
				System.out.println("Exception: "+e.getMessage());
//				e.printStackTrace();
//				if(cleanConfCache(conf))
//				{
//					System.out.println("inside getConferenceIfValid throwing NoConferenceByKeyException");
//					throw	new	NoConferenceByKeyException();
//				}
			}
		}
		
		return	conf;
	}
	
	/**
	 * This method cleans the conf cache of entrieas that are stale
	 * meetings with 0 participants and older then 3 minutes are purged, less then 3 minutes are too new to be purged
+	 * @param conf
	 * @return
	 */
	private boolean cleanConfCache(IConference conf)
	{
		long difference = System.currentTimeMillis() - conf.getStartTimeMillis();
		System.out.println("inside cleanConfCache diff is  = "+difference);
//		this is to avoid deletion of fresh meetings where console is not yet displayed
		if(difference > ConferenceConsoleConstants.getPresenterSessionTimeout()*1000)
		{
			System.out.println("--------------- deleting this conf ------------conf key = "+conf.getConfig().getConferenceKey());
			closeConference(conf);
			return true;
		}
		return false;
	}
	
	private	synchronized	void	closeConference(IConference conf)
	{
		try
		{
			System.out.println("ConferenceManager::closeConference");
			String confKey = conf.getConfig().getConferenceKey();
			if (this.recordingAdapter != null)
			{
				this.recordingAdapter.meetingClosed(confKey,"_default",confKey, conf.getConferenceId());
			}
			this.conferences.remove(confKey);
			conf.getStreamingServer().conferenceClosed();
			PortalServerAdapter.getAdapter().reportMeetingClosed(conf);
			try
			{
				PresentationManager.removePresentationManager(confKey,true);
//				DMSPresentationManager.removePresentationManager(confKey);
			}
			catch(Exception e)
			{
			}
			
			OtherCallbacksHandler.meetingClosed(confKey,"_default",confKey, conf.getConferenceId());
		}
		catch(Exception e)
		{
			//	Ignore. This is a forced close.
		}
	}
	public	void	conferenceStateChanged(IConference conference, int oldState, int newState)
	{
		System.out.println("ConferenceManager::conferenceStateChanged: oldState:"+oldState+": newState:"+newState);
		if (newState == IConference.CONF_STATE_CLOSED)
		{
			this.closeConference((ActiveConference)conference);
		}
	}
	
	/**
	 * Active conference state change listener implementation. Only significant
	 * action at present is cleaning up the presentations uploaded for the
	 * conference.
	 */
	
//	public	void	conferenceActive(ActiveConference conference)
//	{
//		
//	}
//	public	void	conferencePassive(ActiveConference conference)
//	{
//		
//	}
//	public	void	conferenceClosed(ActiveConference conference)
//	{
//		
//	}
//	public	void	conferenceRemoved(ActiveConference conference)
//	{
//		this.closeConference(conference);
//		/**
//		String confKey = conference.getConfig().getConferenceKey();
//		this.conferences.remove(confKey);
//		try
//		{
//			PresentationManager.removePresentationManager(confKey,true);
//		}
//		catch(Exception e)
//		{
//		}
//		conference.getStreamingServer().conferenceClosed();
//		PortalServerAdapter.getAdapter().reportMeetingClosed(confKey);
//		**/
//	}
	
	/**
	 * These methods are for admin interface support.
	 * @return
	 */
	public	int	getNumberOfActiveConferences()
	{
		return	this.conferences.size();
	}
	public	Iterator	keys()
	{
		return	this.conferences.keySet().iterator();
	}
	public	IConference	getConferenceReturnNull(String key)
	{
		IConference conf = null;
		try
		{
			conf = this.getConferenceIfValid(key);
		}
		catch(Exception e)
		{
//			e.printStackTrace();
		}
		return	conf;
	}
}
