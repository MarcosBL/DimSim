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

package com.dimdim.conference.action.roster;

import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import com.dimdim.conference.action.ConferenceAction;
import com.dimdim.conference.model.IAudioVideoManager;
import com.dimdim.conference.model.IConference;
import com.dimdim.conference.model.IConferenceParticipant;
import com.dimdim.conference.model.IPermissionsManager;
import com.dimdim.conference.model.UserNotInConferenceException;


/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */
public class MakeActivePresenterAction	extends	ConferenceAction
{
	protected	String	userId;
	
	public	MakeActivePresenterAction()
	{
	}
	public	String	doWork()	throws	Exception
	{
		String	ret = SUCCESS;
		IConference conf = this.userSession.getConference();
		//IConferenceParticipant user = this.userSession.getUser();
		
		try
		{
			if (userId != null)
			{
				conf.getRosterManager().getRosterObject().makeActivePresenter(userId);
				IConferenceParticipant user = conf.getRosterManager().getRosterObject().getParticipant(userId);
				
				if(!user.isHost())
				{
					IAudioVideoManager avManager = conf.getAudioVideoManager();
					IPermissionsManager ipm = conf.getParticipantPermissions();
					
					Vector v = new Vector();
					v.add(userId);
					
					if(conf.getMaxAttendeeVideos() != 0)
					{
//						if user does have an video broad caster already
						if(avManager.getEnabledVideos().containsKey(userId))
						{
							
						}else{
							if(avManager.isVideoBroadcasterAvailable())
							{
							
							}else{
								//here we have to disable someones video and then assign
								Set keySet = avManager.getEnabledVideos().keySet();
								String tempUserId = "";
								for (Iterator iter = keySet.iterator(); iter.hasNext();) {
									tempUserId = (String) iter.next();
								}
								v.clear();
								v.add(tempUserId);
								ipm.disableVideoForAllInList(v);
							}

							//if user has audio enabled disable it and then enable video
							if(avManager.getEnabledAudios().containsKey(userId))
							{
								v.clear();
								v.add(userId);
								ipm.disableAudioForAllInList(v);
							}
	
							v.clear();
							v.add(userId);
							ipm.enableVideoForAllInList(v);
						}
					}else if(conf.getMaxAttendeeMikes() != 0){
						//if user does have an audio broad caster already
						if(avManager.getEnabledAudios().containsKey(userId))
						{
							
						}else{
							if(avManager.isAudioBroadcasterAvailable())
							{
							
							}else{
								//here we have to disable someones audo and then assign
								Set keySet = avManager.getEnabledAudios().keySet();
								String tempUserId = "";
								for (Iterator iter = keySet.iterator(); iter.hasNext();) {
									tempUserId = (String) iter.next();
								}
								v.clear();
								v.add(tempUserId);
								ipm.disableAudioForAllInList(v);
							}
								v.clear();
								v.add(userId);
								ipm.enableAudioForAllInList(v);
							}
					}
					
				}
			}
		}
		catch(UserNotInConferenceException uice)
		{
			ret = ERROR;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			ret = ERROR;
		}
		return	ret;
	}
	public String getUserId()
	{
		return userId;
	}
	public void setUserId(String userId)
	{
		this.userId = userId;
	}
}
