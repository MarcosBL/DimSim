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
 *								                                          *
 * Copyright (c) 2006 Communiva Inc. All Rights Reserved.	              *
 *								                                          *
 *								                                          *
 * This code is licensed under the DimDim License                         *
 * For details please visit http://www.dimdim.com/license		          *
 *									                                      *
 **************************************************************************
 */
/*
 **************************************************************************
 *	File Name  : IConstants.java
 *  Created On : Jun 13, 2006
 *  Created By : Saurav Mohapatra
 **************************************************************************
*/

package com.dimdim.conference.ui.common.client;

import com.dimdim.conference.ui.model.client.ConferenceGlobals;
import com.dimdim.conference.ui.model.client.UIResources;

/**
 * @author Saurav Mohapatra
 * 
 */
public class UIConstants
{
	public	static	int	MY_BROADCASTER_STATE_INACTIVE = 1;
	public	static	int	MY_BROADCASTER_STATE_CONNECTING = 2;
	public	static	int	MY_BROADCASTER_STATE_ACTIVE = 3;
	public	static	int	MY_BROADCASTER_STATE_ERROR = 4;
	
	private	static	UIConstants	uiConstants = null;
	
	public	static	UIConstants	getConstants()
	{
		if (UIConstants.uiConstants == null)
		{
			UIConstants.uiConstants = new UIConstants();
		}
		return	UIConstants.uiConstants;
	}
	
	private	UIResources		uiResources;
	
	private	UIConstants()
	{
		this.uiResources = UIResources.getUIResources();
	}
	/*
	public	static	boolean	isPresenter(UIRosterEntry user)
	{
		return (user.getRole().equals(UIConstants.ROLE_ACTIVE_PRESENTER) || 
				user.getRole().equals(UIConstants.ROLE_PRESENTER));
	}
	public	static	boolean	isActivePresenter(UIRosterEntry user)
	{
		return (user.getRole().equals(UIConstants.ROLE_ACTIVE_PRESENTER));
	}
	public	static	boolean	isAttendee(UIRosterEntry user)
	{
		return (user.getRole().equals(UIConstants.ROLE_ATTENDEE));
	}
	*/
	public	String	getOKLabel()
	{
		return ConferenceGlobals.getDisplayString("common_button.ok.label","OK");
	}
	public	String	getCancelLabel()
	{
		return ConferenceGlobals.getDisplayString("common_button.cancel.label","Cancel");
	}
	public	String	presenterDepartureWarning()
	{
		return ConferenceGlobals.getDisplayString("top_bar.signout.presenter.confirmation_comment",
			"Conference will be closed upon your exit. Are you sure you want to leave");
	}
	public	String	attendeeDepartureWarning()
	{
		return ConferenceGlobals.getDisplayString("top_bar.signout.attendee.confirmation_comment",
			"Are you sure if you want to leave the conference?");
	}
	public	String	attendeeRemoveWarning()
	{
		return ConferenceGlobals.getDisplayString("presenter.remove.attendee.message",
			"You are about to remove USER from this Web Meeting.");
	}
	public	String	getDefaultWorkspaceCaption()
	{
		return ConferenceGlobals.getDisplayString("workspace.default_heading",
			UIConstants.DEFAULT_WORKSPACE_CAPTION);
	}
	public	int	getPopupBoxXPosition()
	{
		return	this.uiResources.getIntResource("comon","popupBoxXPosition",400);
	}
	public	int	getPopupBoxYPosition()
	{
		return	this.uiResources.getIntResource("comon","popupBoxYPosition",200);
	}
	
//	public static final int	LHP_WIDTH	=	185;
//	public static final int	TOP_PANEL_HEIGHT	=	90;
//	public static final int TAB_BORDER_ALLOWANCE = 2;
	
	public static final	String	PPT_PLAYER_MOVIE_ID = "PPT_PLAYER";
	public static final	String	WHITEBOARD_MOVIE_ID = "WHITEBOARD_PLAYER";
	public static final	String	AUDIO_BROADCASTER_PLAYER_MOVIE_ID = "AUDIO_BROADCASTER";
	public static final	String	VIDEO_BROADCASTER_PLAYER_MOVIE_ID = "VIDEO_BROADCASTER";
	
	public static final String RESOURCE_TYPE_DEFAULT = "resource.unassigned";
	public static final String RESOURCE_TYPE_DESKTOP = "resource.screen";
	public static final String RESOURCE_TYPE_WHITEBOARD = "resource.whiteboard";
	public static final String RESOURCE_TYPE_COBROWSE = "resource.cobrowse";
	public static final String RESOURCE_TYPE_PRESENTATION = "resource.ppt";
	public static final String RESOURCE_TYPE_PDF = "resource.pdf";
	public static final String RESOURCE_TYPE_APP_SHARE = "resource.appShare";
	public static final String RESOURCE_TYPE_APP_FRAME = "resource.appframe";
	
	public static final String ROLE_ACTIVE_PRESENTER = "role.activePresenter";
//	public static final String ROLE_ORGANIZER = "role.organizer";
	public static final String ROLE_PRESENTER = "role.presenter";
	public static final String ROLE_ATTENDEE = "role.attendee";
	
	public	static	final	String	WaitingInLobby	=	"waiting_in_lobby";
	
	public static final String DEFAULT_WORKSPACE_ICON = "dimdimWorkspaceDefaultToolbarIcon";
	public static final String PRESENTATION_ICON = "dimdimWorkspacePrsentationSharingToolbarIcon";
	public static final String DESKTOP_ICON = "dimdimWorkspaceDTPSharingToolbarIcon";
	public static final String APP_SHARE_ICON = "dimdimWorkspaceAppSharingToolbarIcon";
	
	public static final String STYLE_NAME_WORKSPACE_TOOLBAR = "dimdimWorkspaceToolbar";
	public static final String STYLE_NAME_WORKSPACE_TOOLBAR_ICON = "dimdimWorkspaceToolbarIcon";
	public static final String STYLE_NAME_WORKSPACE_TOOLBAR_CAPTION = "dimdimWorkspaceToolbarCaption";
	public static final String STYLE_NAME_WORKSPACE_VIEWER = "dimdimWorkspaceViewer";
	public static final String STYLE_NAME_WORKSPACE_SHELL = "dimdimWorkspaceShell";
	public static final String STYLE_NAME_SCROLLER = "dimdimWorkspaceShellScroller";
	
	public static final String PROP_RESOURCE_ID = "resourceId";
	public static final String PROP_CONFERENCE_KEY = "meetingId";
	public static final String PROP_PRESENTATION_ID = "presentationId";
	public static final String PROP_PRESENTATION_TITLE = "presentationTitle";
	public static final String PROP_SLIDE_COUNT = "slideCount";
	public static final String PROP_STREAM_NAME = "streamName";
	public static final String PROP_RTMP_URL = "rtmpUrl";
	public static final String PROP_STREAM_TITLE = "streamTitle";
	public static final String PROP_ROOT_URL = "rootUrl";
	public static final String PROP_PRESENTATER_MODE = "presenterMode";
	public static final String PROP_RESOURCE_OWNER = "resourceOwner";
	
	/**
	 * Following are constant strings, to be replaced soon by resource property.
	 */
	public static final String RESOURCE_TYPE_NICE_NAME_DESKTOP = "Screen Share (Desktop)";
	public static final String RESOURCE_TYPE_NICE_NAME_PRESENTATION = "Presentation";
	public static final String RESOURCE_TYPE_NICE_NAME_APP_SHARE = "Screen Share (Application)";
	public static final String DEFAULT_WORKSPACE_CAPTION = "Web Meeting Collaboration Workspace";
}
