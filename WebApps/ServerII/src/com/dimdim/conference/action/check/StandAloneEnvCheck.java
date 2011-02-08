package com.dimdim.conference.action.check;

import java.util.Locale;

import com.dimdim.conference.ConferenceConsoleConstants;
//import com.dimdim.conference.ConferenceConstants;
import com.dimdim.conference.UtilMethods;
//import com.dimdim.conference.application.UserManager;
//import com.dimdim.conference.application.UserSession;
//import com.dimdim.conference.application.core.ConferenceManager;
//import com.dimdim.conference.application.core.NoConferenceByKeyException;
//import com.dimdim.conference.application.core.UserNotAuthorizedToStartConference;
//import com.dimdim.conference.model.IConference;
//import com.dimdim.conference.model.IConferenceParticipant;
//import com.dimdim.conference.model.MeetingOptions;
//import com.dimdim.locale.LocaleManager;
//import com.dimdim.locale.LocaleResourceFile;
//import com.dimdim.util.session.MeetingSettings;
//import com.dimdim.util.session.UserInfo;
import com.dimdim.util.session.UserRequest;
import com.dimdim.util.session.UserSessionDataManager;
import java.util.UUID;

/**
 * This action is 
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 *
 */
public class StandAloneEnvCheck extends	ConferenceCheckAction
{
	public	static	final	String	HOST_ACTION = "host";
	public	static	final	String	JOIN_ACTION = "join";
	
	protected	String	action;
	
	protected	String	osType;
	protected	String	browserType;
	
	protected	String	uri;
	protected	String	lc;
	
	protected	UserRequest userRequest;
	
	String userType = ConferenceConsoleConstants.getUserTypeFreeOrPaid();
	
	public	StandAloneEnvCheck()
	{
	}
	public	String	doWork()	throws	Exception
	{
		String	ret = SUCCESS;
		readOsAndBrowserInfo();
		if (this.action  == null)
		{
			this.action = StandAloneEnvCheck.HOST_ACTION;
		}
		if (this.action.equals(StandAloneEnvCheck.HOST_ACTION))
		{
			ret = checkAction(action, true);
		}
		else
		{
			ret = checkAction(action, false);
		}
		
		return	ret;
	}
	protected	void	readOsAndBrowserInfo()
	{
//		if (this.osType == null || this.browserType == null)
//		{
		String userAgent = UtilMethods.findUserAgent(this.servletRequest);
//    		String userAgent = this.servletRequest.getHeader("user-agent");
//    		if (userAgent != null)
//    		{
//    			userAgent = userAgent.toLowerCase();
//    		}
//    		else
//    		{
//    			userAgent = UtilMethods.findUserAgent(this.servletRequest);
//    		}
		UtilMethods.setSessionParameters(this.session, userAgent);
		this.osType = UtilMethods.getOsType(userAgent);
		this.browserType = UtilMethods.getBrowserType(userAgent);
//			if (userAgent != null)
//			{
//				userAgent = userAgent.toLowerCase();
//				this.osType = UtilMethods.getOsType(userAgent);
//				this.browserType = UtilMethods.getBrowserType(userAgent);
//			}
//			else
//			{
//				System.out.println("Error: user agent header not available to detect os and browser type");
//			}
//		}
	}
	protected	String	checkAction(String action, boolean pub)	throws	Exception
	{
		String	ret = SUCCESS;
		
		String meetingId = "";
		String defaultUrl = "";
		String feedbackEmail = "";
		String locale = "en_US";
		int participantLimit = 20;
		boolean featurePpt = true;
		boolean assistantEnabled = true;
		
		this.uri = UserSessionDataManager.getDataManager().saveStartnewMeetingRequestData(
				action,"", "", "", "", "", meetingId,"", "","", "",1,1,participantLimit,
				1,1,false,true,assistantEnabled,true,true,featurePpt,pub,true,
				defaultUrl, "",feedbackEmail, "", locale, userType, null, null, null, null, null, null, false, null, null,
				false, false, true, true, true, true, "");
		
		this.userRequest = UserSessionDataManager.getDataManager().getUserRequest(this.uri);
		
		resultCode = SUCCESS;
		message = "";
		if (url == null || url.length() == 0)
		{	
			url = "http://www.dimdim.com/";
		}
		
		this.servletRequest.setAttribute(UserRequest.MEETING_CONNECT_CONTINUE_URL,url);
		this.servletRequest.setAttribute("STAND_ALONE_CHECK", "true");
		this.userRequest.setUrl(url);
		
		return	ret;
	}
	public String getAction()
	{
		return action;
	}
	public void setAction(String action)
	{
		this.action = action;
	}
	public String getUri()
	{
		return uri;
	}
	public void setUri(String uri)
	{
		this.uri = uri;
	}
	public UserRequest getUserRequest()
	{
		return userRequest;
	}
	public void setUserRequest(UserRequest userRequest)
	{
		this.userRequest = userRequest;
	}
	public String getUserType()
	{
		return userType;
	}
	public void setUserType(String userType)
	{
		this.userType = userType;
	}
}
