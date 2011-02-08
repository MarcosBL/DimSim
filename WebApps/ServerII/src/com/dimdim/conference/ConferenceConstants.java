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
 *	File Name  : IConferenceConstants.java
 *  Created On : Apr 11, 2006
 *  Created By : Saurav Mohapatra
 **************************************************************************
*/

package com.dimdim.conference;

/**
 * Each true string for an event is designed as <feature name>.<event name>
 * uniformly. The ui listener needs to implement 'on<event name>(Object data)'
 * method, which will receive the event. Data associated with each event could
 * be different. However it will be a fixed list of supported type. All java
 * primitive types are supported.
 */

/**
 * @author Saurav Mohapatra
 * @email  Saurav.Mohapatra@communiva.com
 */
public interface ConferenceConstants
{
	public static final int MAX_MEETINGS_ON_SERVER = -1;
	//this is inclusive of presenter
	public static final int MAX_PARTICIPANTS_PER_MEETING = -1;
	
	public static final String ROLE_ACTIVE_PRESENTER = "role.activePresenter";
	public static final String ROLE_PRESENTER = "role.presenter";
	public static final String ROLE_ATTENDEE = "role.attendee";
	public static final String ROLE_AUTOMATON = "role.automaton";
	
	public static final String ROLE_PRESENTER_DISPLAY_STRING = "Presenter";
	public static final String ROLE_ATTENDEE_DISPLAY_STRING = "Attendee";

//	public static final String EXTERNAL_RESOURCE_XMPP_SERVER = "externalResource.xmpp";

	/**
	 * Tokens that identify the intended user action to the forms page related
	 * actions.
	 */

	public	static	final	String	ACTION_HOST_MEETING		=	"host";
	public	static	final	String	ACTION_JOIN		=	"join";
	public	static	final	String	ACTION_START_MEETING	=	"start";

	public	static	final	String	ACTION_MEET_NOW		=	"meet_now";
	public	static	final	String	ACTION_SCHEDULE_MEETING		=	"schedule";
	public	static	final	String	ACTION_JOIN_ATTENDEE	=	"join_attendee";
	public	static	final	String	ACTION_JOIN_PRESENTER	=	"join_presenter";

	/**
	 * Following flags identify the target of the message. Each feature has a different
	 * scope and a different set of rules and responses, however the work required for
	 * actions within the feature requires objects close to each other and may in cases
	 * may be interdependent. Hence the handlers are first grouped based on these.
	 */
//	public static final String FEATURE_CONFERENCE_CONTROLLER = "FEATURE_CONFERENCE_CONTROLLER";
	public static final String FEATURE_CONF = "feature.conf";
	public static final String FEATURE_ROSTER = "feature.roster";
	public static final String FEATURE_CHAT = "feature.chat";
	public static final String FEATURE_QUESTION_MANAGER = "feature.question";
	public static final String FEATURE_RESOURCE_MANAGER = "feature.resource";
	public static final String FEATURE_POLL = "feature.poll";
	public static final String FEATURE_PRESENTATION = "feature.presentation";
	public static final String FEATURE_SHARING = "feature.sharing";
	public static final String FEATURE_VIDEO = "feature.video";
	public static final String FEATURE_AUDIO = "feature.audio";
	public static final String FEATURE_SETTINGS = "feature.settings";
	public static final String FEATURE_WHITEBOARD = "feature.wb";
	public static final String FEATURE_COBROWSE = "feature.cb";
	public static final String FEATURE_RECORDING = "feature.recording";

	/**
	 * Action flags. Login and logout are at a different level.
	 */

	public static final String EVENT_CONFERENCE_CLOSED = "conf.closed";
	public static final String EVENT_CONFERENCE_CONSOLE_DATA_SENT = "conf.consoleDataSent";
	public static final String EVENT_CONFERENCE_TIME_WARNING_1 = "conf.timeWarning1";
	public static final String EVENT_CONFERENCE_TIME_WARNING_2 = "conf.timeWarning2";
	public static final String EVENT_CONFERENCE_TIME_WARNING_3 = "conf.timeWarning3";
	public static final String EVENT_CONFERENCE_TIME_EXPIRED = "conf.timeExpired";
	public static final String EVENT_CONFERENCE_ENDED = "conf.ended";
	public static final String EVENT_CONFERENCE_TRACKBACK_URL = "conf.trackbackUrl";
	public static final String EVENT_CONFERENCE_TIME_CHANGE = "conf.timeChange";
	public static final String EVENT_CONFERENCE_PARTICIPANTS_COUNT_CHANGE = "conf.partCountChange";
	public static final String EVENT_CONFERENCE_SESSION_LOST = "conf.serverSessionLost";
	
	public static final String EVENT_CONFERENCE_COMMAND_SUCCESS = "cmd.ok";
	public static final String EVENT_CONFERENCE_NO_KEY = "conf.noKey";
	public static final String EVENT_CONFERENCE_NOT_ACTIVE = "conf.noConf";
	public static final String EVENT_CONFERENCE_ID = "conf.id";
	
	/**
	 * Actions and event codes for various feature groups.
	 */
	/**
	 * Roster
	 */
	public static final String EVENT_PARTICIPANT_USER_ARRIVED = "roster.userArrived";
	public static final String EVENT_PARTICIPANT_ENTRY_GRANTED = "roster.entryGranted";
	public static final String EVENT_PARTICIPANT_ENTRY_DENIED = "roster.entryDenied";
	public static final String EVENT_PARTICIPANTS_LIST = "roster.roster";
	public static final String EVENT_PARTICIPANT_JOINED = "roster.joined";
	public static final String EVENT_PARTICIPANT_REJOINED = "roster.rejoined";
	public static final String EVENT_PARTICIPANT_LEFT = "roster.left";
	public static final String EVENT_PARTICIPANT_REMOVED = "roster.removed";
	public static final String EVENT_PARTICIPANT_STATUS_CHANGE = "roster.entryChanged";
	public static final String EVENT_PARTICIPANT_ROLE_CHANGE = "roster.roleChanged";
	public static final String EVENT_PARTICIPANT_PERMISSION_CHANGE = "roster.permChanged";
	public static final String EVENT_LOBBY_ENABLED = "roster.lobbyEnabled";
	public static final String EVENT_LOBBY_DISABLED = "roster.lobbyDisabled";
	public static final String EVENT_CHAT_DISABLED_FOR_ALL = "roster.disableAllChat";
	public static final String EVENT_CHAT_DISABLED = "roster.disableChat";
	public static final String EVENT_CHAT_ENABLED_FOR_ALL = "roster.enableAllChat";
	public static final String EVENT_CHAT_ENABLED = "roster.enableChat";
	public static final String EVENT_AUDIO_DISABLED_FOR_ALL = "roster.disableAllAudio";
	public static final String EVENT_AUDIO_DISABLED = "roster.disableAudio";
	public static final String EVENT_AUDIO_ENABLED_FOR_ALL = "roster.enableAllAudio";
	public static final String EVENT_AUDIO_ENABLED = "roster.enableAudio";
	public static final String EVENT_VIDEO_DISABLED_FOR_ALL = "roster.disableAllVideo";
	public static final String EVENT_VIDEO_DISABLED = "roster.disableVideo";
	public static final String EVENT_VIDEO_ENABLED_FOR_ALL = "roster.enableAllVideo";
	public static final String EVENT_VIDEO_ENABLED = "roster.enableVideo";
	public static final String EVENT_INVITATION_EMAIL_DISPATCH_SUCCESS = "roster.emailOK";
	public static final String EVENT_INVITATION_EMAIL_DISPATCH_ERROR = "roster.emailError";

	public static final String EVENT_CHAT_INVITATION = "chat.invitation";
	public static final String EVENT_CHAT_INVITATION_DECLINED = "chat.inviteDeclined";
	public static final String EVENT_CHAT_STARTED = "chat.started";
	public static final String EVENT_CHAT_MESSAGE = "chat.message";
	public static final String EVENT_CHAT_ENDED = "chat.ended";

	/**
	 * Resource Management
	 */
	public static final String EVENT_RESOURCE_ADDED = "resource.added";
	public static final String EVENT_RESOURCE_UPDATED = "resource.updated";
	public static final String EVENT_RESOURCE_DELETED = "resource.deleted";
	public static final String EVENT_RESOURCE_SELECTED = "resource.selected";
	public static final String EVENT_RESOURCE_RENAMED = "resource.renamed";
	public static final String EVENT_RESOURCES_LIST = "resources.list";
	public static final String EVENT_ACTIVE_RESOURCE_CHANGED = "resource.activeChanged";
	public static final String EVENT_ANNOTATION_ADDED = "resource.annotationAdded";
	public static final String EVENT_ANNOTATIONS_CLEARED = "resource.annotationsCleared";

	/**
	 * Recording
	 */
	
	public static final String EVENT_RECORDING_CONTROL_START = "start";
	public static final String EVENT_RECORDING_CONTROL_STOP = "stop";
	

	public static final String EVENT_RECORDING_CONTROL = "recording.control";

	/**
	 * Whiteboard
	 */

	public static final String EVENT_WHITEBOARD_CONTROL_CREATE = "c";
	public static final String EVENT_WHITEBOARD_CONTROL_DELETE = "d";
	public static final String EVENT_WHITEBOARD_CONTROL_START = "s";
	public static final String EVENT_WHITEBOARD_CONTROL_STOP = "p";
	public static final String EVENT_WHITEBOARD_CONTROL_LOCK = "l";
	public static final String EVENT_WHITEBOARD_CONTROL_UNLOCK = "u";

	public static final String EVENT_WHITEBOARD_CONTROL = "wb.c";
	
	public static final String EVENT_COBROWSE_CONTROL = "cb.c";
	public static final String EVENT_COBROWSE_CONTROL_START = "s";
	public static final String EVENT_COBROWSE_CONTROL_STOP = "p";
	public static final String EVENT_COBROWSE_CONTROL_SCROLL = "sc";
	public static final String EVENT_COBROWSE_CONTROL_NAVIGATE = "n";
	public static final String EVENT_COBROWSE_CONTROL_RENAME = "r";
	public static final String EVENT_COBROWSE_CONTROL_LOCK = "l";
	public static final String EVENT_COBROWSE_CONTROL_UNLOCK = "u";
		/*
	public static final String ACTION_CREATE_WHITEBOARD = "action.createWhiteboard";
	public static final String ACTION_ADD_ANNOTATION = "action.addAnnotation";
	public static final String ACTION_CLEAR_WHITEBOARD_ANNOTATIONS = "action.clearWBAnnotations";
	public static final String ACTION_SAVE_WHITEBOARD = "action.saveWhiteboard";
	*/
	/**
	 * Presentation
	 */

	public static final String ACTION_PRESENTATION_START = "action.pres.start";
	public static final String ACTION_PRESENTATION_FIRST_SLIDE = "action.pres.first";
	public static final String ACTION_PRESENTATION_NEXT_SLIDE = "action.pres.next";
	public static final String ACTION_PRESENTATION_PREVIOUS_SLIDE = "action.pres.previous";
	public static final String ACTION_PRESENTATION_LAST_SLIDE = "action.pres.last";
	public static final String ACTION_PRESENTATION_SLIDE_BY_NUMBER = "action.pres.slide";
	public static final String ACTION_PRESENTATION_STOP = "action.pres.stop";
	public static final String ACTION_PRESENTATION_ANNOTATIONS_ON = "action.pres.ann_on";
	public static final String ACTION_PRESENTATION_ANNOTATIONS_OFF = "action.pres.ann_off";
	public static final String ACTION_PRESENTATION_CHANGE = "change";

	public static final String ACTION_PRESENTATION_CONTROL = "action.pres.control";
//	public static final String ACTION_PRESENTATION_GET_SLIDE = "action.pres.get";
//	public static final String ACTION_PRESENTATION_SHOW_THUMBNAILS = "action.pres.showThumbnails";
//	public static final String ACTION_PRESENTATION_HIDE__THUMBNAILS = "action.pres.hideThumbnails";

	public static final String EVENT_PRESENTATION_CONTROL = "presentation.control";
	public static final String EVENT_PRESENTATION_STARTED = "presentation.started";
	public static final String EVENT_PRESENTATION_STOPED = "presentation.stoped";

	/**
	 * Streaming related general constants.
	 */
	public static final String STREAM_TYPE_SCREEN_SHARE = "screen";
	public static final String STREAM_TYPE_VIDEO = "video";

	public static final String EVENT_STREAM_CONTROL_START = "start";
	public static final String EVENT_STREAM_CONTROL_STOP = "stop";
	public static final String EVENT_STREAM_CONTROL_VOICE = "voice";

	public static final String EVENT_STREAM_CONTROL = "stream.control";

	/**
	 * Screen sharing
	 */
	public static final String ACTION_SCREEN_SHARING_CONTROL = "action.sharing.control";
	public static final String EVENT_SCREEN_SHARING_CONTROL = "sharing.control";

	public static final String EVENT_SETTINGS_CHANGED = "settings.changed";

	/**
	 * Video
	 */

	public static final String ACTION_VIDEO_START = "action.video.start";
	public static final String ACTION_VIDEO_STOP = "action.video.stop";

	public static final String ACTION_VIDEO_CONTROL = "action.video.control";
	public static final String EVENT_VIDEO_CONTROL = "video.control";

	/**
	 * Audio control for two way audio exchange between attendees.
	 */
	public static final String ACTION_AUDIO_START = "action.audio.start";
	public static final String ACTION_AUDIO_STOP = "action.audio.stop";

	public static final String ACTION_AUDIO_CONTROL = "action.audio.control";
	public static final String EVENT_AUDIO_CONTROL = "audio.control";

	/**
	 * Response codes. These are actions that are generated only in response to
	 * an action initiated by a current user, which will be one of the above
	 * actions. In theory then we will need more than 1 response code for each of
	 * the actions. Wherever specific and detailed data is required to accompany
	 * the response, specific codes are used. For simple string returns generic
	 * codes are used. Each notification handler in the client is expected to
	 * support all generic codes. The responses are always returned within the
	 * same feature scope.
	 */

	public static final String ACTION_RESULT = "action.result";
	public static final String ACTION_RESULT_CODE = "action.result.code";

	public static final String RESPONSE_OK = "response.ok";
	public static final String RESPONSE_ERROR = "response.error";
	public static final String RESPONSE_DATA = "response.data";
	public static final String RESPONSE_UNAUTHORIZED = "response.unahtorized";

	/**
	 * Following are event codes, that the client will receive as a result of an
	 * action by another user. e.g. someone inviting for a chat. Each specfic
	 * event for the client is always within the scope of a feature, so the
	 * handlers must work within that scope.
	 *
	 * Each action always generates and sends a response. Additionaly it may
	 * also send events to the caller as well as other participants of the
	 * conference. e.g. when someone successfuly joins a conference the caller
	 * gets RESPONSE_OK response and everyone else gets PARTICIPANT_JOINED event.
	 */

//	public static final String EVENT_PARTICIPANT_LIST = "roster.list";

	public static final String EVENT_POLL_ANSWER = "resource.pollResultBroadcast";

	/**
	 * Few required constants.
	 */

	public static final String DEFAULT_GLOBAL_QNA_SESSION_ID = "QNA_DEFAULT_GLOBAL_SESSION_ID";

	public static final String WW_TOKEN_SUCCESS = "success";
	public static final String WW_TOKEN_ERROR = "error";
	public static final String WW_TOKEN_IN_CONFERENCE = "in_conference";

	/**
	 *
	 */
	public static final String MSG_FIELD_RESOURCE_POLL_OPTIONS = "msg.resource.pollOptions";

	public static final String RESOURCE_TYPE_UNASSIGNED = "resource.unassigned";
	public static final String RESOURCE_TYPE_PRESENTATION = "resource.ppt";
	public static final String RESOURCE_TYPE_PDF = "resource.pdf";
	public static final String RESOURCE_TYPE_SCREEN_SHARE = "resource.screen";
	public static final String RESOURCE_TYPE_APP_SHARE = "resource.appShare";
	public static final String RESOURCE_TYPE_APP_FRAME = "resource.appframe";

	public static final String RESOURCE_TYPE_POLL = "resource.poll";
	public static final String RESOURCE_TYPE_WHITEBOARD = "resource.whiteboard";
	public static final String RESOURCE_TYPE_COBROWSE = "resource.cobrowse";
	public static final String RESOURCE_TYPE_VIDEO = "resource.video";
	public static final String RESOURCE_TYPE_AUDIO = "resource.audio";

	public static final String MSG_FIELD_PRESENTATION_CONTROL_EVENT = "presentation.control";
	public static final String MSG_FIELD_SHARING_CONTROL_EVENT = "sharing.control";
	public static final String MSG_FIELD_VIDEO_CONTROL_EVENT = "video.control";

	public static final String RESOURCE_NAME_XMPP_SERVER = "resource.xmpp";
	public static final String ATTENDANCE_POLICY_Y_INVITATION_ONLY = "attendance.policy.invitation";

	public static final String STATUS_HOST = "host";
	public static final String STATUS_PREVIOUS_HOST = "previous_host";
	
	public static final String PRESENCE_IN_MEETING = "inmeeting";
	public static final String PRESENCE_OFFLINE = "offline";

	public static final String MOOD_NORMAL = "Normal";
	public static final String MOOD_SPEED_UP = "Speed Up";
	public static final String MOOD_SLOW_DOWN = "Slow Down";
	public static final String MOOD_STEPPING_OUT = "Stepping out";
	public static final String MOOD_QUESTION="Question";
	public static final String MOOD_ON_THE_PHONE = "On the phone";
	public static final String MOOD_BUSY = "Busy";
	public static final String MOOD_UNKNOWN = "Unknown";


	public static final String MSG_ERR_TIMEOUT = "msg.err.timeout";
	public static final String MSG_ERR_NO_SUCH_CONFERENCE = "msg.err.noSuchConference";
	public static final String MSG_ERR_NO_SUCH_FEATURE = "msg.err.noSuchFeature";
	public static final String MSG_ERR_COULD_NOT_CREATE_ROSTER = "msg.err.couldNotCreateRoster";
	public static final String MSG_ERR_NO_SUCH_ACTION = "msg.err.noSuchAction";
	public static final String MSG_ERR_ACTION_IS_NULL = "msg.err.actionIsNull";
	public static final String MSG_ERR_OUT_OF_RANGE = "msg.err.outOfRange";

	/**
	 * Generic error codes. Most of these might never occur at runtime UI would simply
	 * block unavailable functionality. The server however of course checks in order
	 * to protect against erroneous use.
	 */
	public static final String MSG_ERR_IN_CONFERENCE = "msg.err.inConference";
	public static final String MSG_ERR_NOT_IN_CONFERENCE = "msg.err.notInConference";
	public static final String MSG_ERR_LOGIN_FAILURE = "msg.err.loginFailure";
	public static final String MSG_ERR_REQUIRES_LOGIN = "msg.err.requiresLogin";
	public static final String MSG_ERR_NOT_AUTHORIZED = "msg.err.notAuthorized";

	/**
	 * Publisher versions in order to avoid having to track track and maintain in multiple
	 * jsps.
	 */

	public static int PUBLISHER_CONTROL_MAJOR_VERSION	=	4;
	public static int PUBLISHER_CONTROL_MINOR_VERSION	=	0;
	public static int PUBLISHER_CONTROL_BUILD_VERSION	=	0;
	public static int PUBLISHER_CONTROL_BUILD_VERSION_2	=	0;

	public static final String MEETING_AV_TYPE_DISABLED = "disabled";
	public static final String MEETING_AV_TYPE_AV = "av";
	public static final String MEETING_AV_TYPE_AUDIO = "audio";
	//public static final String MEETING_AV_TYPE_VIDEO_CHAT = "videochat";
	public static final String MEETING_AV_TYPE_VIDEO = "video";

}
