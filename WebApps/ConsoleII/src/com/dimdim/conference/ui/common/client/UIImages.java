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

package com.dimdim.conference.ui.common.client;

import com.dimdim.conference.ui.imageBundles.client.DefaultSkinImpl;
import com.dimdim.conference.ui.imageBundles.client.SkinBundle;
import com.dimdim.conference.ui.model.client.ConferenceGlobals;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ImageBundle;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * A simple static interface for the urls of the images so that any changes
 * can be made at 1 place.
 */

public class UIImages
{
	/**
	 * User float images.
	 */
	
	SkinBundle imageBundle = null;
	String skinName = "";
	
	public static String defaultSkin ="default";
	static UIImages uiImages = null;
	
	public static UIImages getImageBundle(String skinName){
		SkinBundle imageBundle = null;
		
//		if image bundle is not null and does not use the given skin then create a new one
		if(null != uiImages && skinName.equals(uiImages.skinName))
		{
			uiImages = new UIImages(imageBundle, skinName);
			return uiImages;
		}
		
		if(defaultSkin.equals(skinName))
		{
			imageBundle= (DefaultSkinImpl) GWT.create(DefaultSkinImpl.class);
		}
		
		if(imageBundle == null)
		{
			imageBundle= (DefaultSkinImpl) GWT.create(DefaultSkinImpl.class);
		}
		return uiImages;
	}
	
	public UIImages(SkinBundle imageBundle, String skinName)
	{
		this.imageBundle = imageBundle;
		this.skinName = skinName;
	}
	
	public	Image	getNewMoodImageUrl(String mood)
	{
		if (mood.equals(UserGlobals.WaitingInLobby))	{	return	imageBundle.waitingLobby().createImage();	}
		else if (mood.equals(UserGlobals.Normal))	{	return	imageBundle.normal().createImage();	}
		else if (mood.equals(UserGlobals.GoFaster))	{	return	imageBundle.goFast().createImage();	}
		else if (mood.equals(UserGlobals.GoSlower))	{	return	imageBundle.goSlow().createImage();	}
		else if (mood.equals(UserGlobals.SpeakLouder))	{	return	imageBundle.louder().createImage();	}
		else if (mood.equals(UserGlobals.SpeakSofter))	{	return	imageBundle.softer().createImage();	}
		else if (mood.equals(UserGlobals.ThumbsUp))	{	return	imageBundle.thumbsUp().createImage();	}
		else if (mood.equals(UserGlobals.ThumbsDown))	{	return	imageBundle.thumbsDown().createImage();	}
		else if (mood.equals(UserGlobals.SteppedAway))	{	return	imageBundle.steppedOut().createImage();	}
		else if (mood.equals(UserGlobals.Agree))	{	return	imageBundle.agree().createImage();	}
		else if (mood.equals(UserGlobals.Disagree))	{	return	imageBundle.disagree().createImage();	}
		else if (mood.equals(UserGlobals.BeRightBack))	{	return	imageBundle.berightBack().createImage();	}
		else if (mood.equals(UserGlobals.Busy))	{	return	imageBundle.busy().createImage();	}
		else if (mood.equals(UserGlobals.Problem))	{	return	imageBundle.problem().createImage();	}
		else if (mood.equals(UserGlobals.Question))	{	return	imageBundle.question().createImage();	}
		
		return	imageBundle.customMessage().createImage();
	}
	
	public	Image	getUserChatEnabledImageUrl()
	{
		return	imageBundle.chat().createImage();
	}
	public	Image	getUserChatDisabledImageUrl()
	{
		return	imageBundle.chatDisabled().createImage();
	}
	public	Image	getChatControlImageUrl()
	{
		return	imageBundle.chat().createImage();
	}
	
	public	Image	getUserAudioEnabledImageUrl()
	{
		return	imageBundle.mic().createImage();
	}
	public	Image	getUserCamEnabledImageUrl()
	{
		return	imageBundle.camera().createImage();
	}
	
	public	Image	getUserAudioDisabledImageUrl()
	{
		return	imageBundle.micDiabled().createImage();
	}
	public	Image	getUserCamDisabledImageUrl()
	{
		return	imageBundle.cameraDisabled().createImage();
	}

	public	Image	getPermissionsControlImageUrl()
	{
		return	imageBundle.permissions().createImage();
	}
	
	public	Image	getSpeakerOKImageUrl()
	{
		return	imageBundle.speaker().createImage();
	}
	public	Image	getTvOKImageUrl()
	{
		return	imageBundle.tv().createImage();
	}
	public	Image	getSpeakerDisabledImageUrl()
	{
		return	imageBundle.speakerDisabled().createImage();
	}
	public	Image	getTvDisabledImageUrl()
	{
		return	imageBundle.tvDisabled().createImage();
	}
	
	public	Image	getGrantEntryTaskImageUrl()
	{
		return	imageBundle.grantEntry().createImage();
	}
	public	Image	getDenyEntryTaskImageUrl()
	{
		return	imageBundle.denyEntry().createImage();
	}
	
	public	Image	getCancelQuestionTaskImageUrl()
	{
		return	imageBundle.remove().createImage();
	}
	
	public	Image	getRightTriangleUrl()
	{
		return	imageBundle.rightTriangle().createImage();
	}
	
	public	Image	getEmailTaskImage1Url()
	{
		return	imageBundle.eamilTask().createImage();
	}
	public	Image	getCancelEmailTaskImageUrl()
	{
		return	imageBundle.remove().createImage();
	}
	
	/**
	 * Resource float icons
	 */
	public	Image	getDesktopShareItemImageUrl()
	{
		return	imageBundle.desktop().createImage();
	}
	public	Image	getWhiteboardShareItemImageUrl()
	{
		return	imageBundle.whiteboard().createImage();
	}
	public	Image	getPowerpointItemImageUrl()
	{
		return	imageBundle.ppt().createImage();
	}
	public	Image	getApplicationShareItemImageUrl()
	{
		return	imageBundle.applicationIcon().createImage();
	}
	public	Image	getShareControlImage()
	{
		return	imageBundle.shareControl().createImage();
	}
	public	Image	getDeleteControlImage()
	{
		return	imageBundle.remove().createImage();
	}
	public	Image	getRenameControlImage()
	{
		return	imageBundle.rename().createImage();
	}
	public	Image	getActiveShareItemImageUrl()
	{
		return	imageBundle.activeShare().createImage();
	}
	
	public	Image	getNextGreyed()
	{
		return	imageBundle.nextGreyed().createImage();
	}
	public	Image	getNext()
	{
		return	imageBundle.next().createImage();
	}
	public	Image	getPrev()
	{
		return	imageBundle.previous().createImage();
	}
	public	Image	getPrevGreyed()
	{
		return	imageBundle.prevGreyed().createImage();
	}
	
	public	Image	getNoMessage()
	{
		return	imageBundle.noIncoming().createImage();
	}
	
	public	Image	getNewMessage()
	{
		return	imageBundle.newIncoming().createImage();
	}
	
	public	Image	getMinimize()
	{
		return	imageBundle.minimize().createImage();
	}
	
	public	Image	getMaximize()
	{
		return	imageBundle.maximize().createImage();
	}
	
	public	Image	getClose()
	{
		return	imageBundle.close().createImage();
	}
	
	public	Image	getCloseDialog()
	{
		return	imageBundle.commonDialogClose().createImage();
	}
	
	public	Image	getMaximizeVideo()
	{
		return	imageBundle.maximizeVideo().createImage();
	}
	
	public	Image	getMinimizeVideo()
	{
		return	imageBundle.minimizeVideo().createImage();
	}
	
	public	Image	getPresenter()
	{
		return	imageBundle.presenter().createImage();
	}
	
	public	Image	getAttendee()
	{
		return	imageBundle.attendee().createImage();
	}
	
	/*public	Image	getWaiting()
	{
		return	imageBundle.waiting().createImage();
	}*/
	
	public	Image	getPopIn()
	{
		return	imageBundle.popIn().createImage();
	}
	
	public	Image	getFullScreen()
	{
		return	imageBundle.fullScreen().createImage();
	}
	
	public	Image	getRecord()
	{
		return	imageBundle.record().createImage();
	}
	
	public	Image	getRecordOff()
	{
		return	imageBundle.recordOff().createImage();
	}
	
	public	Image	getHideChat()
	{
		return	imageBundle.hideChat().createImage();
	}
	public	Image	getShowChat()
	{
		return	imageBundle.showChat().createImage();
	}
	
	public	Image	getShareIcon()
	{
		return	imageBundle.shareIcon().createImage();
	}
	public	Image	getChangeImage()
	{
		return	imageBundle.changeImage().createImage();
	}
	public	Image	getChangeDisplayName()
	{
		return	imageBundle.changeDisplayName().createImage();
	}
	public	Image	getEndMeeting()
	{
		return	imageBundle.endMeeting().createImage();
	}
	public	Image	getGiveRights()
	{
		return	imageBundle.giveRights().createImage();
	}
	public	Image	getInvite()
	{
		return	imageBundle.invite().createImage();
	}
	public	Image	getManage()
	{
		return	imageBundle.manage().createImage();
	}
	public	Image	getRestartAv()
	{
		return	imageBundle.restartAv().createImage();
	}
	public	Image	getSendEmail()
	{
		return	imageBundle.sendEmail().createImage();
	}
	public	Image	getPublicChatCloseImage()
	{
		return	imageBundle.publicChatClose().createImage();
	}
}

