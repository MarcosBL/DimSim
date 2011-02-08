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

package com.dimdim.conference.action.portal;

import com.dimdim.conference.ConferenceConsoleConstants;
import com.dimdim.conference.application.core.ActiveConference;
import com.dimdim.conference.application.core.ConferenceManager;
import com.dimdim.conference.application.portal.PortalInterface;
import com.dimdim.conference.application.portal.UserInfo;
import com.dimdim.conference.application.portal.UserRequest;
import com.dimdim.conference.model.IConference;
import com.dimdim.conference.model.IConferenceParticipant;
import com.dimdim.locale.LocaleResourceFile;
import com.dimdim.util.misc.Base64;
import com.dimdim.util.session.UserSessionDataManager;


/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */

public class JoinMeetingAction extends PortalAdapterAction
{
	//	Input parameters that will be posted to the action by the portal's
	//	start new meeting form.
	
	protected	String	email;
	protected	String	displayName;
	protected	String	key;
	protected	String	password;
	protected	String	userType = LocaleResourceFile.FREE;
	private String userId;
	
	/**
	 * 
	 */
	protected	String	attendeePwd;
	//	The return url. If the meeting can be started.
	
	protected	String		url = "";
	
	public	JoinMeetingAction()
	{
		
	}
	public	String	execute()	throws	Exception
	{
		String	ret = SUCCESS;
		
		System.out.println("Received join meeting from portal:"+key+","+displayName+","+attendeePwd);
		if (key != null) //&& email != null && displayName != null)
		{
//			  making the confkey as lower case as this was creating dtp stream problems
		    this.key = this.key.toLowerCase();

			//	Check if conference exists.
			if (displayName != null)
			{
				displayName = (String)(Base64.decodeToObject(displayName));
			}
			
			if(email != null && email.length() > 0)
			{
				
			}
			else
			{
				email ="";
			}
			System.out.println("Received join meeting from portal:"+key+","+displayName+","+attendeePwd);
			if (super.isKeyInUse(key))
			{
				
//				Check currently running conferences.
				ConferenceManager confManager = ConferenceManager.getManager();
				IConference conf = confManager.getConferenceIfValid(this.key);
				if (conf != null)
				{
					IConferenceParticipant user = conf.getParticipant(this.email);
					//if user in meeting send false1 as error code to portal
					if (user != null)
					{
						this.result = "false1";
						System.out.println("User already in meeting...");
						super.createResultJsonBuffer(false,300,"User already in meeting");
					}
					else
					{
						String requiredPwd = ((ActiveConference)conf).getAttendeePwd();
						if (null != requiredPwd && requiredPwd.length() > 0)
						{
							if(null == attendeePwd || attendeePwd.length() == 0)
							{
								this.result = "false2";
								System.out.println("This meeting needs a password");
								super.createResultJsonBuffer(false, 300, "This meeting needs a password");
							}
							else if(!requiredPwd.equals(attendeePwd))
							{
								this.result = "false3";
								System.out.println("The password provided does not match");
								super.createResultJsonBuffer(false, 300, "The password provided does not match");
							}
						}
						if(requiredPwd.equalsIgnoreCase(""))
						{

							//						Form the start form url and return to be submitted immediately.
													this.url = ConferenceConsoleConstants.getServerAddress();
													if (this.isSecure())
													{
														this.url = ConferenceConsoleConstants.getServerSecureAddress();
													}
							//						this.url = this.url+
							//							"/"+ConferenceConsoleConstants.getWebappName()+"/html/signin/signin.action?"+
							//							"action=join&confKey="+key+"&email="+email+
							//							"&displayName="+displayName+"&submitFormOnLoad=true";
													
							//						UserInfo ui = new UserInfo(email,displayName,key,key);
							//						UserRequest  ur = new UserRequest(email,meeting_id,"join",ui);
													
													String uri = UserSessionDataManager.getDataManager().
														saveJoinMeetingRequestData("join", email, displayName, userId, key, "en_US", userType, attendeePwd);
													
						/*							this.url = this.url+
														"/"+ConferenceConsoleConstants.getWebappName()+
														"/html/envcheck/connect.action?uri="+uri+"&attendeePwd="+attendeePwd;
						*/	
													this.url = this.url+
													"/"+ConferenceConsoleConstants.getWebappName()+
													"/html/envcheck/connect.action?uri="+uri;
													//							"action=join&meeting_id="+meeting_id+"&email="+email;
													
													this.result = checkFireFoxEntityPattern(url);
													System.out.println("Returning url:"+url);
													super.createResultJsonBuffer(true,200,url);
													
							//						PortalInterface.getPortalInterface().saveUserInfo(meeting_id,new UserInfo(email,key,displayName,key));
							//						PortalInterface.getPortalInterface().saveUserRequest(ur);
												
							
						}
						else if(attendeePwd.equalsIgnoreCase(requiredPwd))
						{
	//						Form the start form url and return to be submitted immediately.
							this.url = ConferenceConsoleConstants.getServerAddress();
							if (this.isSecure())
							{
								this.url = ConferenceConsoleConstants.getServerSecureAddress();
							}
	//						this.url = this.url+
	//							"/"+ConferenceConsoleConstants.getWebappName()+"/html/signin/signin.action?"+
	//							"action=join&confKey="+key+"&email="+email+
	//							"&displayName="+displayName+"&submitFormOnLoad=true";
							
	//						UserInfo ui = new UserInfo(email,displayName,key,key);
	//						UserRequest  ur = new UserRequest(email,meeting_id,"join",ui);
							
							String uri = UserSessionDataManager.getDataManager().
								saveJoinMeetingRequestData("join", email, displayName, userId, key, "en_US", userType, attendeePwd);
							
/*							this.url = this.url+
								"/"+ConferenceConsoleConstants.getWebappName()+
								"/html/envcheck/connect.action?uri="+uri+"&attendeePwd="+attendeePwd;
*/	
							this.url = this.url+
							"/"+ConferenceConsoleConstants.getWebappName()+
							"/html/envcheck/connect.action?uri="+uri;
							//							"action=join&meeting_id="+meeting_id+"&email="+email;
							
							this.result = checkFireFoxEntityPattern(url);
							System.out.println("Returning url:"+url);
							super.createResultJsonBuffer(true,200,url);
							
	//						PortalInterface.getPortalInterface().saveUserInfo(meeting_id,new UserInfo(email,key,displayName,key));
	//						PortalInterface.getPortalInterface().saveUserRequest(ur);
						}
					}
					}
			}
			else
			{
				//if no meeting exists return false as error code to portal
				this.result = "false";
				System.out.println("No meeting by key.");
				super.createResultJsonBuffer(false,300,"No meeting by key");
			}
		}
		else
		{
			this.result = "false";
			System.out.println("Invalid data");
			super.createResultJsonBuffer(false,310,"Insufficient data");
		}
		
		System.out.println("from join meeting action of conf server returning json buffer "+jsonBuffer);
		
		return	ret;
	}
	
	protected	String	checkFireFoxEntityPattern(String s)
	{
		String s2 = s;		
		s2 = s.replaceAll("&amp;","&");

		return	s2;
	}

	
	public String getDisplayName()
	{
		return displayName;
	}
	public void setDisplayName(String displayName)
	{
		this.displayName = displayName;
	}
	public String getKey()
	{
		return key;
	}
	public void setKey(String key)
	{
		this.key = key;
	}
	public String getEmail()
	{
		return email;
	}
	public void setEmail(String email)
	{
		this.email = email;
	}
	public String getPassword()
	{
		return password;
	}
	public void setPassword(String password)
	{
		this.password = password;
	}
	public String getUrl()
	{
		return url;
	}
	public void setUrl(String url)
	{
		this.url = url;
	}
	public String getUserType()
	{
	    return userType;
	}
	public void setUserType(String userType)
	{
	    this.userType = userType;
	}
	public String getAttendeePwd() {
		return attendeePwd;
	}
	public void setAttendeePwd(String attendeePwd) {
		this.attendeePwd = attendeePwd;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
}
