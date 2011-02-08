package com.dimdim.conference.action.check;

import com.dimdim.conference.ConferenceConsoleConstants;
import com.dimdim.util.session.UserRequest;
import com.dimdim.util.session.UserSessionDataManager;

public class PubInstall extends	ConferenceCheckAction
{
	protected	String	sessionKey;
	protected	UserRequest userRequest;
	
	protected String doWork() throws Exception
	{
		if (sessionKey != null)
		{
			url = "/"+ConferenceConsoleConstants.getWebappName();
			url += "/ReloadConsole.action";
			url += "?sessionKey="+sessionKey;
			this.servletRequest.setAttribute(UserRequest.MEETING_CONNECT_CONTINUE_URL,url);
	
		}
		return SUCCESS;
    }

	public String getSessionKey() {
		return sessionKey;
	}

	public void setSessionKey(String sessionKey) {
		this.sessionKey = sessionKey;
	}
	
}
