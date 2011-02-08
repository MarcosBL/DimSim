package com.dimdim.conference.action.common;

import com.dimdim.conference.ConferenceConsoleConstants;
//import com.dimdim.conference.ConferenceConstants;
import com.dimdim.conference.action.CommonDimDimAction;
import com.dimdim.conference.application.presentation.dms.URLHelper;


public class GetDMSUrl extends CommonDimDimAction	{
	
	protected	String		jsonBuffer = "{a:\"b\"}";
	protected	String		meetingId;
	protected	String		userId;
	
	public	String	execute()	throws	Exception
	{
		String dmsUrl = "http://"+ConferenceConsoleConstants.getDMServerMboxExternalAddress()+"/";
		String dmsInternalUrl = "http://"+ConferenceConsoleConstants.getDMServerInternalAddress()+"/";
//		System.out.println(" inside GetDMSUrl action of conf server dmsUrl = "+dmsUrl);
		String currentLogUrl = URLHelper.getLogoUrl(meetingId, userId);
		jsonBuffer = "{dmsUrl:\""+dmsUrl+"\", dmsInternalUrl:\""+dmsInternalUrl+"\", defaultLogoUrl:\""+currentLogUrl+"\" }";
		System.out.println(" inside GetDMSUrl action of conf server returning json buffer = "+jsonBuffer);
		return SUCCESS;
	}
	public String getJsonBuffer() {
		return jsonBuffer;
	}

	public void setJsonBuffer(String jsonBuffer) {
		this.jsonBuffer = jsonBuffer;
	}
	public String getMeetingId() {
		return meetingId;
	}
	public void setMeetingId(String meetingId) {
		this.meetingId = meetingId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}

}
