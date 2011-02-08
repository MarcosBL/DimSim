package com.dimdim.conference.action.portal;

import com.dimdim.conference.application.core.ActiveConference;
import com.dimdim.conference.application.core.ConferenceManager;
import com.dimdim.conference.application.core.NoConferenceByKeyException;
import com.dimdim.conference.application.portal.Message;
import com.dimdim.conference.application.portal.ServerResponse;
import com.opensymphony.xwork.ActionSupport;

public class CloseMeeting extends	ActionSupport{
	protected	String	jsonBuffer = "";
	protected	String	meetingRoomName;
	protected 	String 	presenterPwd = "";
	
	public	String	execute()	throws	Exception
	{
		String	ret = SUCCESS;
		ActiveConference conf = null ;
		
		try
		{
			if(meetingRoomName != null)
			{
				conf = (ActiveConference)ConferenceManager.getManager().getConference(meetingRoomName);
			}
			if(null != conf )
			{
				String requiredPwd = ((ActiveConference)conf).getPreseterPwd();
				if (null != requiredPwd && requiredPwd.length() > 0)
				{
					if(null == presenterPwd || presenterPwd.length() == 0)
					{
						ServerResponse sr = new ServerResponse(false,300,new Message("Presenter password required to perform this action"));
						this.jsonBuffer = sr.toJson();
						ret = ERROR;
						return ret;
					}
					if(!requiredPwd.equals(presenterPwd))
					{
						ServerResponse sr = new ServerResponse(false,300,new Message("Presenter password provided does not match"));
						this.jsonBuffer = sr.toJson();
						ret = ERROR;
						return ret;
					}
				}
				if( null != conf.getHostSession())
				{
					conf.getHostSession().close();
					ServerResponse sr = new ServerResponse(true, 200, new Message("Closed the meeting"));
					this.jsonBuffer = sr.toJson();
				}else{
					ServerResponse sr = new ServerResponse(false,300,new Message("Internal error occured"));
					this.jsonBuffer = sr.toJson();
					ret = ERROR;
				}
			}else{
				ServerResponse sr = new ServerResponse(false,300,new Message("No meeting by key"));
				this.jsonBuffer = sr.toJson();
				//nfe.printStackTrace();
				ret = ERROR;
			}
		}
		catch (NoConferenceByKeyException nfe)
		{
			ServerResponse sr = new ServerResponse(false,300,new Message("No meeting by key"));
			this.jsonBuffer = sr.toJson();
			//nfe.printStackTrace();
			ret = ERROR;
		}
		catch(Exception e)
		{
			ServerResponse sr = new ServerResponse(false,300,new Message("Insufficient data"));
			this.jsonBuffer = sr.toJson();
			e.printStackTrace();
			ret = ERROR;
		}
		
		
		return	ret;
	}

	public String getJsonBuffer() {
		return jsonBuffer;
	}

	public void setJsonBuffer(String jsonBuffer) {
		this.jsonBuffer = jsonBuffer;
	}

	public String getMeetingRoomName() {
		return meetingRoomName;
	}

	public void setMeetingRoomName(String meetingRoomName) {
		this.meetingRoomName = meetingRoomName;
	}

	public String getPresenterPwd() {
		return presenterPwd;
	}

	public void setPresenterPwd(String presenterPwd) {
		this.presenterPwd = presenterPwd;
	}
}
