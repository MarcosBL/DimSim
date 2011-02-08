package com.dimdim.conference.action.portal;


import com.dimdim.conference.application.core.ActiveConference;
import com.dimdim.conference.application.core.ConferenceManager;
import com.dimdim.conference.application.core.NoConferenceByKeyException;
import com.dimdim.conference.application.portal.Message;
import com.dimdim.conference.application.portal.ServerResponse;
import com.opensymphony.xwork.ActionSupport;

public class EditSettings extends	ActionSupport{

	protected	String	networkProfile;
	protected	String	imgQuality;
	protected	Integer	maxParticipants;
	protected	Integer	maxMeetingTime;
	protected	Integer	maxAttendeeMikes;
	protected	String	returnUrl;
	protected	String	meetingRoomName;
	protected	boolean	lobby = false;
	protected 	String 	presenterPwd = "";
	protected	String	jsonBuffer = "Setting changed succesfully";
	
	public	EditSettings()
	{
		System.out.println(".................inside EditSettings action constructor....");
	}
	
	public	String	execute()	throws	Exception
	{
		String	ret = SUCCESS;
		ActiveConference conf = null ;
		
		System.out.println("Net Profile:"+networkProfile+", Image Quality: "+imgQuality+", max participants:"+maxParticipants+", max meeting time:"+maxMeetingTime);
		try
		{
			if(meetingRoomName != null)
			{
				conf = (ActiveConference)ConferenceManager.getManager().getConference(meetingRoomName);
			}
			if(null != conf)
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
				if (networkProfile == null)
					{
						networkProfile = conf.getOrganizer().getNetProfile();
					}
				if(imgQuality != null)
					{
						imgQuality = conf.getOrganizer().getImgQuality();
					}
				
				if(maxParticipants == null)
				{
					maxParticipants = new Integer(conf.getMaxParticipants());
				}
				if(maxMeetingTime == null)
				{
					maxMeetingTime = new Integer(conf.getMaxMeetingTime());
				}
						
				
				if (maxAttendeeMikes == null)
				{
					maxAttendeeMikes = new Integer(conf.getMaxAttendeeMikes());
				}
				if (returnUrl == null || returnUrl.length() == 0 || returnUrl.equalsIgnoreCase("null"))
				{
					returnUrl = conf.getReturnUrl();
				}
				
				conf.setConferenceOptions(lobby, networkProfile, imgQuality,maxMeetingTime,maxParticipants,null,maxAttendeeMikes,returnUrl, true);
				
				
			}else{
				ServerResponse sr = new ServerResponse(false,300,new Message("No meeting by key"));
				this.jsonBuffer = sr.toJson();
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
		if(ret.equals(SUCCESS))
		{
			ServerResponse sr = new ServerResponse(true, 200,new Message("Setting changed succesfully"));
			this.jsonBuffer = sr.toJson();
		}
		return	ret;
	}

	public String getImgQuality() {
		return imgQuality;
	}

	public void setImgQuality(String imgQuality) {
		this.imgQuality = imgQuality;
	}

	public Integer getMaxMeetingTime() {
		return maxMeetingTime;
	}

	public void setMaxMeetingTime(Integer maxMeetingTime) {
		this.maxMeetingTime = maxMeetingTime;
	}

	public Integer getMaxParticipants() {
		return maxParticipants;
	}

	public void setMaxParticipants(Integer maxParticipants) {
		this.maxParticipants = maxParticipants;
	}


	public String getReturnUrl() {
		return returnUrl;
	}

	public void setReturnUrl(String returnUrl) {
		this.returnUrl = returnUrl;
	}

	
	public String getPresenterPwd() {
		return presenterPwd;
	}

	public void setPresenterPwd(String preseterPwd) {
		this.presenterPwd = preseterPwd;
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

	public boolean isLobby() {
		return lobby;
	}

	public void setLobby(boolean lobby) {
		this.lobby = lobby;
	}

	public String getNetworkProfile() {
		return networkProfile;
	}

	public void setNetworkProfile(String networkProfile) {
		this.networkProfile = networkProfile;
	}

	
}
