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

import java.util.Iterator;

import com.dimdim.conference.ui.common.client.ResourceGlobals;
import com.dimdim.conference.ui.json.client.UICobrowseControlEvent;
import com.dimdim.conference.ui.json.client.UIResourceObject;
import com.dimdim.conference.ui.json.client.UIRosterEntry;
import com.dimdim.conference.ui.model.client.dms.CobrowseDMSCaller;
import com.google.gwt.user.client.Window;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */

public class CoBrowseModel	extends	FeatureModel 
{
	protected	UIRosterEntry	me;
	public	static	final	String		ModelFeatureId	=	"feature.cb";
//	protected	UIWhiteboardControlEvent	lastEvent;
	
	boolean sharingActive = false;
	protected	UICobrowseControlEvent	lastStartEvent;
	
	public	CoBrowseModel(UIRosterEntry me)
	{
		super(ModelFeatureId);
		
		this.me = me;
	}
	
	public	void	startCobrowse(String streamId, UIResourceObject res , ResourceSharingDisplay resDisplay)
	{
		String dmsUrl = "http://"+ConferenceGlobals.userInfoDictionary.getStringValue("dms_cob_server_address")+"/cobrowsing";
		StringBuffer buf = new StringBuffer();
		
		buf.append(dmsUrl+"/syncToResource?");
		buf.append("dimdimID="+ConferenceGlobals.conferenceKeyQualified);
		buf.append("&roomID="+ConferenceGlobals.conferenceKeyQualified);
		buf.append("&sessionID="+streamId);
		buf.append("&resourceID="+res.getResourceId());

		//Window.alert("inside cob model.. startCobrowse--"+buf.toString());
		
		CobrowseDMSCaller caller = new CobrowseDMSCaller(buf.toString(),resDisplay, res);
		caller.getSyncUrl();
		
		String url = webappRoot+"Cobrowse.action?"+sessionKeyParam+"&cmd=s&si="+streamId+"&ri="+res.getResourceId();
		this.executeCommand(url);
		sharingActive = true;
	}
	
	public	void	syncCobrowse(String streamId, UIResourceObject res , ResourceSharingDisplay resDisplay)
	{
		/*String dmsUrl = "http://"+ConferenceGlobals.userInfoDictionary.getStringValue("dms_cob_server_address")+"/cobrowsing";
		StringBuffer buf = new StringBuffer();
		
		buf.append(dmsUrl+"/syncToResource?");
		buf.append("dimdimID="+ConferenceGlobals.conferenceKeyQualified);
		buf.append("&roomID="+ConferenceGlobals.conferenceKeyQualified);
		buf.append("&sessionID="+streamId);
		buf.append("&resourceID="+res.getResourceId());
		
		Window.alert("inside cob model.. syncCobrowse--"+buf.toString());
		
		CobrowseDMSCaller caller = new CobrowseDMSCaller(buf.toString(),resDisplay, res);
		caller.getSyncUrl();*/
		res.setUrl(ConferenceGlobals.conferenceKeyQualified);
		resDisplay.onSharingStarted(res);
	}
	
	public	void	renameCobResurce(String streamId, UIResourceObject res , ResourceSharingDisplay resDisplay, String newName)
	{
		//cmd = l means lock and cmd = u means unlock
		String cmd = "r";
		String url = webappRoot+"Cobrowse.action?"+sessionKeyParam+"&cmd="+cmd+"&si="+streamId+"&ri="+res.getResourceId()+
		"&newName="+newName;
		this.executeCommand(url);
	}
	
	public	void	lock(String streamId, UIResourceObject res , ResourceSharingDisplay resDisplay, boolean lock)
	{
		//cmd = l means lock and cmd = u means unlock
		String cmd = "l";
		if(!lock)
		{
			cmd = "u";
		}
		String url = webappRoot+"Cobrowse.action?"+sessionKeyParam+"&cmd="+cmd+"&si="+streamId+"&ri="+res.getResourceId();
		this.executeCommand(url);
	}
	
	public	void	navigateTo(String streamId, UIResourceObject res , ResourceSharingDisplay resDisplay, String state)
	{
		/*String dmsUrl = "http://"+ConferenceGlobals.userInfoDictionary.getStringValue("dms_cob_server_address")+"/cobrowsing";
		StringBuffer buf = new StringBuffer();
		if("back".equalsIgnoreCase(state))
		{
			buf.append(dmsUrl+"/navigateBack?");
		}else if("forward".equalsIgnoreCase(state))
		{
			buf.append(dmsUrl+"/navigateForward?");
		}
		buf.append("meetingID="+ConferenceGlobals.getConferenceKey());
		buf.append("&roomID="+ConferenceGlobals.getConferenceKey());
		buf.append("&sessionID="+streamId);
		buf.append("&resourceID="+res.getResourceId());
		if(ResourceGlobals.PreloadedDefaultOwnerId.equalsIgnoreCase(res.getOwnerId()))
		{
			buf.append("&confAddress="+getLocation());
		}
		//Window.alert("inside cob model.."+buf.toString());
		
		CobrowseDMSCaller caller = new CobrowseDMSCaller(buf.toString(),resDisplay, res);
		caller.getSyncUrl();*/
	}
	
	public	void	stopCobrowse( String streamId, UIResourceObject res)
	{
		String url = webappRoot+"Cobrowse.action?"+sessionKeyParam+"&cmd=p&si="+streamId+"&ri="+res.getResourceId();
		this.executeCommand(url);
		sharingActive = false;
	}
	public	void	scroll(String horScroll, String verScroll)
	{
		UIResourceObject res = ConferenceGlobals.getCurrentSharedResource();
		String url = webappRoot+"Cobrowse.action?"+sessionKeyParam+"&cmd=l&si="+res.getStreamName()+"&ri="+res.getResourceId()
						+"&horScroll="+horScroll+"&verScroll="+verScroll;
		this.executeCommand(url);
	}
	public	void	navigate(String streamId, String resId)
	{
		String url = webappRoot+"Cobrowse.action?"+sessionKeyParam+"&cmd=u&si="+streamId+"&ri="+resId;
		this.executeCommand(url);
	}
	public	void	onEvent(String eventId, Object data)
	{
		if (data != null)
		{
			//Window.alert(data.toString());
			UICobrowseControlEvent	event = (UICobrowseControlEvent)data;
			if (event.getEventType().equals(UICobrowseControlEvent.START))
			{
				this.onStart(event);
				this.lastStartEvent = event;
				this.objects.add(this.lastStartEvent);
			}
			else if (event.getEventType().equals(UICobrowseControlEvent.STOP))
			{
				this.onStop(event);
				this.objects.remove(this.lastStartEvent);
				this.lastStartEvent = null;
			}
			else if (event.getEventType().equals(UICobrowseControlEvent.LOCK))
			{
				this.onLock(event);
				//this.objects.add(event);
			}else if (event.getEventType().equals(UICobrowseControlEvent.UNLOCK))
			{
				this.onUnlock(event);
				//this.objects.add(event);
			}
			/*else if (event.getEventType().equals(UICobrowseControlEvent.RENAME))
			{
				this.onRename(event);
				this.objects.add(event);
			}*/
		}
		else
		{
//			Window.alert("No data for stream sharing event");
		}
	}
	protected	String		getPopoutJsonEventName()
	{
		return	"cbce";
	}
	protected	String		getPopoutJsonEventDataType()
	{
		return	"object";
	}
	protected	void	onStart(UICobrowseControlEvent event)
	{
		Iterator iter = this.listeners.values().iterator();
		while (iter.hasNext())
		{
			//Window.alert("insdie on started event...."+event);
			((CoBrowseModelListener)iter.next()).onStarted(event);
		}
	}
	protected	void	onStop(UICobrowseControlEvent event)
	{
		Iterator iter = this.listeners.values().iterator();
		while (iter.hasNext())
		{
			((CoBrowseModelListener)iter.next()).onStopped(event);
		}
	}
	
	
	protected	void	onLock(UICobrowseControlEvent event)
	{
		Iterator iter = this.listeners.values().iterator();
		while (iter.hasNext())
		{
			//here the start event is getting executed when last action is of lock/unlock
			//Window.alert("calling start and lock...."+event);
			//((CoBrowseModelListener)iter.next()).onStartedAndLock(event, true);
			((CoBrowseModelListener)iter.next()).onLock(event);
		}
	}

	/*protected	void	onRename(UICobrowseControlEvent event)
	{
		Iterator iter = this.listeners.values().iterator();
		while (iter.hasNext())
		{
			//here the start event is getting executed when last action is of lock/unlock
			//Window.alert("calling start and lock...."+event);
			((CoBrowseModelListener)iter.next()).onRename(event);
			//((CoBrowseModelListener)iter.next()).onLock(event);
		}
	}*/
	
	protected	void	onUnlock(UICobrowseControlEvent event)
	{
		Iterator iter = this.listeners.values().iterator();
		while (iter.hasNext())
		{
			//Window.alert("calling start and un-lock...."+event );
			//((CoBrowseModelListener)iter.next()).onStartedAndLock(event, false);
			((CoBrowseModelListener)iter.next()).onUnlock(event);
		}
	}
	
	public boolean isSharingActive() {
		return sharingActive;
	}
	
	private native String getLocation() /*-{
		return	(escape($wnd.location));
	}-*/;
}

