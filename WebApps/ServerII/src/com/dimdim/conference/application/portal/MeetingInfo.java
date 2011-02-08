package com.dimdim.conference.application.portal;

public class MeetingInfo implements JsonSerializable{

	String subject = "";
	String meetingRoomName = "";
	String organizer = "";
	String startTime = "";
	
	String phone = "";
	String passCode = "";
	
	String joinUrl;
	String attendedCount;
	
	public MeetingInfo()
	{
		
	}

	public MeetingInfo(String subject, String meetingRoomName, String preseterPwd, 
			String organizer, String startTime, String phone, String passCode, 
			String joinUrl, String attendedCount) {
		super();
		this.subject = subject;
		this.meetingRoomName = meetingRoomName;
		this.organizer = organizer;
		this.startTime = startTime;
		this.phone = phone;
		this.passCode = passCode;
		this.joinUrl = joinUrl;
		this.attendedCount = attendedCount;
	}

	
	public String getObjectClass() {
		return "MeetingInfo";
	}

	public String toJson() {
		
		StringBuffer buf = new StringBuffer();
		
		buf.append("{subject:\"");
		buf.append(subject);
		buf.append("\",meetingRoomName:\"");
		buf.append(meetingRoomName);
		buf.append("\"");
		buf.append(",organizer:\"");
		buf.append(organizer);
		buf.append("\"");
		buf.append(",startTime:");
		buf.append(startTime);
		
		buf.append(",phone:\"");
		buf.append(phone);
		buf.append("\"");
		
		buf.append(",passCode:\"");
		buf.append(passCode);
		buf.append("\"");
		
		buf.append(",joinUrl:\"");
		buf.append(joinUrl);
		buf.append("\"");
		
		buf.append(",attendedCount:");
		buf.append(attendedCount);
		
		buf.append("}");
		
		return	buf.toString();
	}
	
	public String getJoinUrl() {
		return joinUrl;
	}

	public void setJoinUrl(String joinUrl) {
		this.joinUrl = joinUrl;
	}

	public String getMeetingRoomName() {
		return meetingRoomName;
	}

	public void setMeetingRoomName(String meetingRoomName) {
		this.meetingRoomName = meetingRoomName;
	}

	public String getOrganizer() {
		return organizer;
	}

	public void setOrganizer(String organizer) {
		this.organizer = organizer;
	}

	public String getPassCode() {
		return passCode;
	}

	public void setPassCode(String passCode) {
		this.passCode = passCode;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getAttendedCount() {
		return attendedCount;
	}

	public void setAttendedCount(String attendedCount) {
		this.attendedCount = attendedCount;
	}

	
	
	
}
