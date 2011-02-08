package com.dimdim.conference.action.portal;

import com.dimdim.conference.application.core.ActiveConference;
import com.dimdim.conference.application.core.ConferenceManager;
import com.dimdim.conference.application.core.NoConferenceByKeyException;
import com.dimdim.conference.application.portal.MeetingInfo;
import com.dimdim.conference.application.portal.Message;
import com.dimdim.conference.application.portal.ServerResponse;
import com.opensymphony.xwork.ActionSupport;

public class GetMeetingInfo extends	ActionSupport{

	protected	String	jsonBuffer = "";
	protected	String	meetingRoomName;
	
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
			if(null != conf)
			{
				MeetingInfo mi = new MeetingInfo();
				setValues(mi, conf);
				ServerResponse sr = new ServerResponse(true, 200, mi);
				this.jsonBuffer = sr.toJson();
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

	private void setValues(MeetingInfo mi, ActiveConference conf) {
		mi.setAttendedCount(conf.getRosterManager().getRosterObject().getNumberOfParticipants()+"");
		mi.setOrganizer(conf.getOrganizer().getDisplayName());
		mi.setJoinUrl(conf.getJoinUrl());
		mi.setMeetingRoomName(conf.getConferenceKey());
		mi.setPassCode(conf.getAttendeePasscode());
		mi.setPhone(conf.getInternToll());
		//mi.setPreseterPwd(conf.getPreseterPwd());
		mi.setStartTime(conf.getStartTimeMillis()+"");
		mi.setSubject(conf.getConfig().getConferenceName());
		
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

}
