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

import java.io.File;
import java.util.Random;

import com.dimdim.conference.action.ConferenceAction;
import com.dimdim.conference.model.IConference;
import com.dimdim.conference.model.IConferenceParticipant;
import com.dimdim.conference.ConferenceConsoleConstants;
import com.dimdim.util.misc.FileUtil;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */

public class UploadPhotoAction	extends	ConferenceAction
{
	protected static Random generator = new Random(System.currentTimeMillis());
	
	protected	File	photo;
	protected	String	userId;
	
	public	UploadPhotoAction()
	{
		
	}
	protected	String	doWork()
	{
		if (this.photo != null)
		{
			int	rand_no = generator.nextInt();
			
			FileUtil fileUtil = new FileUtil();
			
			System.out.println("Photo upload succeeded ##################: "+
					this.photo.getAbsolutePath());
			
			//	Copy the photo the presentations area of this conference and
			//	attach the url to the user object. Trigger a user changed
			//	event to all.
			
			IConference conf = this.userSession.getConference();
			IConferenceParticipant user = this.userSession.getUser();
			
			String fullPhotoFile = ConferenceConsoleConstants.getFullPhotoFile(conf.getConfig().
					getConferenceKey(),user.getId(),rand_no);
			fileUtil.copyFile(this.photo, new File(fullPhotoFile));
			
			String userPhotoUrl = ConferenceConsoleConstants.getPhotoUrl(conf.getConfig().
					getConferenceKey(),user.getId(),rand_no);
			
			//	Set the photo url in the object exactly same as mood, as it will require
			//	same events to be triggered.
			
			System.out.println("Copying photo to ##################: "+userPhotoUrl);
			try
			{
				conf.getRosterManager().getRosterObject().
					updateParticipantPhoto(user.getId(),userPhotoUrl);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		else
		{
			System.out.println("Photo upload failed ********************* ");
		}
		return	SUCCESS;
	}
	public File getPhoto()
	{
		return photo;
	}
	public void setPhoto(File photo)
	{
		this.photo = photo;
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
