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

package com.dimdim.conference.ui.user.client;

import com.dimdim.conference.ui.common.client.UIGlobals;
import com.dimdim.conference.ui.common.client.UIImages;
import com.dimdim.conference.ui.common.client.UIStrings;
import com.dimdim.conference.ui.common.client.UserGlobals;
import com.dimdim.conference.ui.common.client.layout.DefaultCommonDialog;
import com.dimdim.conference.ui.common.client.list.ListEntry;
import com.dimdim.conference.ui.common.client.list.ListEntryControlsProvider;
import com.dimdim.conference.ui.common.client.list.ListEntryHoverPopupPanel;
import com.dimdim.conference.ui.common.client.user.UserCallbacks;
import com.dimdim.conference.ui.common.client.user.UserListEntry;
import com.dimdim.conference.ui.common.client.util.ConfirmationDialog;
import com.dimdim.conference.ui.common.client.util.ConfirmationListener;
import com.dimdim.conference.ui.common.client.util.DebugPanel;
import com.dimdim.conference.ui.dialogues.client.FeedbackDialog;
import com.dimdim.conference.ui.dialogues.client.InvitationPreviewDialog;
import com.dimdim.conference.ui.dialogues.client.common.CommonConsoleHelpers;
import com.dimdim.conference.ui.json.client.UIRosterEntry;
import com.dimdim.conference.ui.model.client.ClientModel;
import com.dimdim.conference.ui.model.client.ConferenceGlobals;
import com.dimdim.conference.ui.model.client.UIResources;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * The buttons are being created in the specific class because the 
 */

public class UserListEntryHoverPopup extends ListEntryHoverPopupPanel //implements ClickListener
{
	protected	UIRosterEntry	me;
	protected	ListEntry		userEntry;
	protected	UserRosterManager		userManager;
	protected	ListEntryControlsProvider	controlsProvider;
	
	protected 	Image[] attendeeImagesArray = {	UIImages.getImageBundle(UIImages.defaultSkin).getSendEmail(), 
												UIImages.getImageBundle(UIImages.defaultSkin).getChangeImage(),
												UIImages.getImageBundle(UIImages.defaultSkin).getRestartAv(), 
												UIImages.getImageBundle(UIImages.defaultSkin).getNewMoodImageUrl(UserGlobals.Normal),
												UIImages.getImageBundle(UIImages.defaultSkin).getSendEmail(), 
												UIImages.getImageBundle(UIImages.defaultSkin).getChangeDisplayName(),
												UIImages.getImageBundle(UIImages.defaultSkin).getEndMeeting(),
											};
	protected 	String[] attendeeSelfTextArray = {
			ConferenceGlobals.getDisplayString("usermenu.invite.label", "Invite Others to this Meeting")
			, ConferenceGlobals.getDisplayString("usermenu.picture.label","Set Picture")
			, ConferenceGlobals.getDisplayString("usermenu.restart.label","Restart A/V broadcaster")
			, ConferenceGlobals.getDisplayString("usermenu.mood.label","Set Mood>>")
			, ConferenceGlobals.getDisplayString("usermenu.feedback.label","Provide Feedback")
			, ConferenceGlobals.getDisplayString("usermenu.display.name.label","Set Display Name")
			, ConferenceGlobals.getDisplayString("usermenu.leave.label","Leave Meeting...")
			};
	;
	
	protected 	String[] presenterSelfTextArray = {
			ConferenceGlobals.getDisplayString("usermenu.invite.label", "Invite Others to this Meeting")
			,ConferenceGlobals.getDisplayString("usermenu.invite1.label","Invite...")
			,ConferenceGlobals.getDisplayString("usermenu.restart.label","Restart A/V broadcaster")
			,ConferenceGlobals.getDisplayString("usermenu.picture.label","Set Picture")
			,ConferenceGlobals.getDisplayString("usermenu.mood.label","Set Mood>>")
			,ConferenceGlobals.getDisplayString("usermenu.display.name.label","Set Display Name")
			,ConferenceGlobals.getDisplayString("usermenu.take.control.label","Take Control Back")
			,ConferenceGlobals.getDisplayString("usermenu.end.label","End Meeting...")
			};
	
	protected 	String[] attendeeOnAnotherTextArray = {ConferenceGlobals.getDisplayString("usermenu.chat.label","Chat Privately...")};
	/*protected 	Image[] presenterImagesArray = {UIImages.getImageBundle(UIImages.defaultSkin).getGiveRights(), 
												UIImages.getImageBundle(UIImages.defaultSkin).getUserAudioEnabledImageUrl(), 
												UIImages.getImageBundle(UIImages.defaultSkin).getUserChatEnabledImageUrl(),
												UIImages.getImageBundle(UIImages.defaultSkin).getUserAudioDisabledImageUrl(),
												UIImages.getImageBundle(UIImages.defaultSkin).getRecord(),
												};
	*/
	protected 	String[] presenterTextArray = {
			ConferenceGlobals.getDisplayString("usermenu.make.presenter.label","Make Presenter")
			,ConferenceGlobals.getDisplayString("usermenu.give.mic.label","Assign Microphone")
			,ConferenceGlobals.getDisplayString("usermenu.chat.label","Chat Privately...")
			,ConferenceGlobals.getDisplayString("usermenu.disable.chat.label","Disable Private Chat")
			,ConferenceGlobals.getDisplayString("usermenu.remove.label","Remove From Meeting")
			,ConferenceGlobals.getDisplayString("usermenu.give.camera.label","Assign Camera")
			};
	
//	protected	Label	changePictureLink = null;
	MoodListener moodListener = null;
	ClickListener changePicListener = null;
	ClickListener feedbackListener = null;
	ClickListener sendInviteListener = null;
	ClickListener makePresenterListener = null;
	ClickListener signoutListener = null;
	ClickListener changeDisplayListener = null;
	
	ClickListener recordListener = null;
	
	protected	boolean	allowInvites;

	public	UserListEntryHoverPopup(final UIRosterEntry me,
				final UserRosterManager userManager, final ListEntry userEntry,
				ListEntryControlsProvider controlsProvider)
	{
		this.userManager = userManager;
		this.me = me;
		this.userEntry = userEntry;
		this.controlsProvider = controlsProvider;
		
		allowInvites = this.me.isHost() ||
			UIResources.getUIResources().getConferenceInfo("allow_attendee_invites").equals("true");
		
		createListeners();
		writeHeader3();
		
		Widget content = getContent(userEntry);
		this.contentPanel.add(content);
		this.contentPanel.setCellWidth(content,"100%");
		//Widget links = getLinks();
		//this.linksPanel.add(links);
		//this.linksPanel.setCellWidth(links,"100%");
	}
	
	public void setSubMeuOpen(boolean isOpen)
	{
		subMenuOpen = isOpen;
	}
	
	private void createListeners(){
		moodListener = new MoodListener(this);

		changePicListener =  new ClickListener()
		{
			
			public	void	onClick(Widget sender)
			{
				ChangePictureDialog dlg = new ChangePictureDialog(userManager,me);
				dlg.drawDialog();
				hide();
			}
		};
		
		changeDisplayListener = new ClickListener()
		{
			public	void	onClick(Widget sender)
			{
				SetDisplayNamePopup dlg = new SetDisplayNamePopup(userManager, me, userEntry);
				dlg.drawDialog();
			}
		};
		
		sendInviteListener = new ClickListener()
		{
			public	void	onClick(Widget sender)
			{
			    	InvitationsManager invitationsManager;
			    	invitationsManager = new InvitationsManager(userManager);
			    	CommonConsoleHelpers.setInvitationsManager(invitationsManager);
			    	
			    	InvitationPreviewDialog dlg = new InvitationPreviewDialog(invitationsManager,"", "");
			    	dlg.drawDialog();
				//SendInvitationDialog dlg = new SendInvitationDialog(userManager,me);
				//dlg.drawDialog();
//				dlg.show();
			}
		};
		
		feedbackListener = new ClickListener()
		{
			public	void	onClick(Widget sender)
			{
					FeedbackDialog dlg = new FeedbackDialog(userManager);
					dlg.drawDialog();
			}
		};
		
		if(null != controlsProvider.getUserSignoutListener())
		{
			signoutListener = new ClickListener()
			{
				public	void	onClick(Widget sender)
				{
					controlsProvider.getUserSignoutListener().userClickedOnSignout();
				}
			};
		}
		if (ConferenceGlobals.recordingEnabled)
		{
			if(null != controlsProvider.getUserSignoutListener())
			{
				recordListener = controlsProvider.getUserSignoutListener().getRecordListener();
			}
		}
	}
	
	protected	Widget	getContent(ListEntry listEntry)
	{
		//UserListEntry ule = (UserListEntry)listEntry;
//		UIRosterEntry user = ((UserListEntry)listEntry).getUser();
		final UIRosterEntry otherUser = ((UserListEntry)userEntry).getUser();
		
	    
//	    DockPanel basePanel = new DockPanel();
//	    basePanel.setSize("100%","100%");
	    VerticalPanel userPanel = new VerticalPanel();
	    userPanel.setStyleName("user-popup-data-panel");
	    
//		VerticalPanel row = new VerticalPanel();
		
		//Window.alert("getImage3 = "+presenterImagesArray[0]);
		//Window.alert("getImage2 = "+presenterImagesArray[1]);
		//Window.alert("getImage4 = "+presenterImagesArray[2]);
		
		if (UIGlobals.isOrganizer(me))
		{
			//this means hover is on some atendee
			if(!me.getUserId().equals(otherUser.getUserId()))
			{
				presOnAttendees(userPanel, otherUser);
			}else{
				//this means hover is on self
				presOnSelf(userPanel);
			}
		}else{
			if(me.getUserId().equals(otherUser.getUserId()))
			{
				attendeeOnSelf(userPanel);
			}else{
				attendeeOnAnother(userPanel, otherUser);
			}
		}
		
//	    userPanel.add(row);
//	    userPanel.setCellWidth(row,"100%");
//	    userPanel.add(email);
	    
	    //String currentPhotoUrl = this.me.getPhotoUrl();
	    
	    /*Image photoImage = new Image(currentPhotoUrl);
		photoImage.setStyleName("change-photo-picture");
		userPanel.add(photoImage);
		userPanel.setCellVerticalAlignment(photoImage,VerticalPanel.ALIGN_TOP);
		userPanel.setCellHorizontalAlignment(photoImage,HorizontalPanel.ALIGN_RIGHT);*/
	    
//	    basePanel.add(userPanel,DockPanel.WEST);
	    
//		Image photoImage = new Image(UserGlobals.getUserGlobals().getPhotoUrl(otherUser));
//		photoImage.setSize("64px","64px");
//		basePanel.add(photoImage,DockPanel.EAST);
		
//	    basePanel.add(linksPanel,DockPanel.SOUTH);
//	    basePanel.add(filler2,DockPanel.CENTER);
	    
//	    basePanel.setCellHorizontalAlignment(userPanel,HorizontalPanel.ALIGN_LEFT);
//	    basePanel.setCellVerticalAlignment(userPanel,VerticalPanel.ALIGN_TOP);
	    //basePanel.setCellVerticalAlignment(photoImage,VerticalPanel.ALIGN_TOP);
//	    basePanel.setCellWidth(userPanel,"100%");
	    
//	    basePanel.setCellVerticalAlignment(linksPanel,VerticalPanel.ALIGN_BOTTOM);
//	    basePanel.setCellWidth(filler2,"100%");
//	    basePanel.setCellHeight(filler2,"100%");
		return	userPanel;
	}
	
	private void attendeeOnAnother(VerticalPanel row, final UIRosterEntry otherUser) {
		if(otherUser.isChatOn() && me.isChatOn())
		{
			addControl(row, attendeeOnAnotherTextArray[0], controlsProvider.getStartChatClickListener(), true);
		}else{
			addControl(row, attendeeOnAnotherTextArray[0], null, true);
		}
	}
	
	private void presOnAttendees(VerticalPanel row, final UIRosterEntry otherUser) {
		if(otherUser.isChatOn() && me.isChatOn())
		{
			addControl(row, presenterTextArray[2], controlsProvider.getStartChatClickListener(), true);
		}else{
			addControl(row, presenterTextArray[2], null, true);
		}
		//if particpant list is turned off do not add this option
		if(ConferenceGlobals.partListEnabled)
		{
			addAudioMenu(row, otherUser);
			addVideoMenu(row, otherUser);
		}
		
		if(UIGlobals.isOrganizer(me) && !UIGlobals.isActivePresenter(otherUser))
		{
			makePresenterListener = new ClickListener()
			{
				public void onClick(Widget sender)
				{
					if (controlsProvider.getUserSignoutListener().isRecordingInProgress())
					{
						//	Recording must be stopped before switching presenters.
						DefaultCommonDialog.showMessage(ConferenceGlobals.getDisplayString("recording.progress.heading","Recording in progress")
								, ConferenceGlobals.getDisplayString("recording.progress.desc","Recording must be stopped before changing the presenter control.")
								);
					}
					else
					{
						if (ConferenceGlobals.getClientLayout() != null)
						{
							ConferenceGlobals.getClientLayout().closePopout();
						}
						userManager.makeActivePresenter(otherUser);
					}
				}
			};
		}
		
		addControl(row, presenterTextArray[0], makePresenterListener, true);
		
		
		/*HTML line1 = new HTML("&nbsp;");
		line1.setStyleName("section-break");
		row.add(line1);*/

		
		/*ClickListener disableChat = null;
		if(otherUser.isChatOn())
		{
			disableChat = new ClickListener()
			{
				public void onClick(Widget sender) {
					userManager.disableChatFor(otherUser.getUserId());
				}
			};
		}
		
		addControl(row, UIImages.getImageBundle(UIImages.defaultSkin).getUserChatDisabledImageUrl(), presenterTextArray[3], disableChat, true);*/
		
		ClickListener removeUser = new ClickListener()
		{

			public void onClick(Widget sender) {
				userManager.removeUser(otherUser);
			}
			
		};
		addControl(row, presenterTextArray[4], removeUser, true);
		/*HTML line2 = new HTML("&nbsp;");
		line2.setStyleName("section-break");
		row.add(line2);
		addControl(row, null, presenterTextArray[5], null, true);*/
	}

	private void addAudioMenu(VerticalPanel row, final UIRosterEntry otherUser) {
		//DebugPanel.getDebugPanel().addDebugMessage("ConferenceGlobals.isPresenterAVAudioDisabled() = "+ConferenceGlobals.isPresenterAVAudioDisabled());
		//DebugPanel.getDebugPanel().addDebugMessage("ConferenceGlobals.isPresenterAVVideoOnly() = "+ConferenceGlobals.isPresenterAVVideoOnly());
		//DebugPanel.getDebugPanel().addDebugMessage("ConferenceGlobals.isPresenterAVActive() = "+ConferenceGlobals.isPresenterAVActive());
		//DebugPanel.getDebugPanel().addDebugMessage("ConferenceGlobals.isPresenterAVAudioOnly() = "+ConferenceGlobals.isPresenterAVAudioOnly());
		if(ConferenceGlobals.isPresenterAVAudioDisabled()
				|| ConferenceGlobals.isPresenterAVVideoOnly())
		{
			return;
		}
		//not giving this option if video on
		if (otherUser.isVideoOn())
		{
			return;
		}
		if (otherUser.isAudioOn())
		{
				addAudioControl(row, ConferenceGlobals.getDisplayString("usermenu.disable.audio.label","Disable Audio"),
						userManager.getAvailableAttendeeAudios(), 
						controlsProvider.getImage3ClickListener(), true);
		}
		else
		{
				addAudioControl(row, presenterTextArray[1],
						userManager.getAvailableAttendeeAudios(), 
							controlsProvider.getImage3ClickListener(), true);
		}
	}
	
	private void addVideoMenu(VerticalPanel row, final UIRosterEntry otherUser) {
		if(	ConferenceGlobals.isPresenterAVAudioDisabled() 
				|| ConferenceGlobals.isPresenterAVAudioOnly()
				|| !ConferenceGlobals.isMeetingVideoChat()
			)
		{
			return;
		}
		
		if (otherUser.isAudioOn())
		{
			return;
		}
		
		if (otherUser.isVideoOn())
		{
				addVideoControl(row, ConferenceGlobals.getDisplayString("usermenu.disable.video.label","Disable Camera"),
					userManager.getAvailableAttendeeVideos(), 
					controlsProvider.getImage4ClickListener(), true);

		}
		else
		{
				addVideoControl(row, presenterTextArray[5],
						userManager.getAvailableAttendeeVideos(), 
							controlsProvider.getImage4ClickListener(), true);

		}
	}
	
	private void presOnSelf(VerticalPanel row) {
		//addControl(row, UIImages.getImageBundle(UIImages.defaultSkin).getInvite(), presenterSelfTextArray[0], sendInviteListener, true);
//		if("true".equalsIgnoreCase(ConferenceGlobals.getShowInviteLinks()))
		if (this.allowInvites)
		{
			addControl(row, presenterSelfTextArray[1], sendInviteListener, true);
		}
		
		if(UIGlobals.isOrganizer(me) && !UIGlobals.isActivePresenter(me))
		{
				makePresenterListener = new ClickListener()
				{
					public void onClick(Widget sender)
					{
						if (controlsProvider.getUserSignoutListener().isRecordingInProgress())
						{
							//	Recording must be stopped before switching presenters.
							DefaultCommonDialog.showMessage(
									ConferenceGlobals.getDisplayString("recording.progress.heading","Recording in progress")
									, ConferenceGlobals.getDisplayString("recording.progress.desc","Recording must be stopped before changing the presenter control.")
									);
						}
						else
						{
							if (ConferenceGlobals.getClientLayout() != null)
							{
								ConferenceGlobals.getClientLayout().closePopout();
							}
							userManager.makeActivePresenter(me);
						}
					}
				};
		}
		addControl(row,presenterSelfTextArray[6], makePresenterListener, true);
		
		HTML line1 = new HTML("&nbsp;");
		line1.setStyleName("section-break");
		row.add(line1);
		
		addControl(row, presenterSelfTextArray[5], changeDisplayListener, true);
		addMoodControl(row, presenterSelfTextArray[4], moodListener, false);
		HTML line2 = new HTML("&nbsp;");
		line2.setStyleName("section-break");
		row.add(line2);
		if (ConferenceGlobals.recordingEnabled)
		{
			if (ClientModel.getClientModel().getRecordingModel().isRecordingActive())
			{
				addControl(row, ConferenceGlobals.getDisplayString("usermenu.stop.recording.label","Stop Recording"), recordListener, true);
			}
			else
			{
				addControl(row, ConferenceGlobals.getDisplayString("usermenu.start.recording.label","Start Recording"), recordListener, true);
			}
		}
		if(!ConferenceGlobals.isPresenterAVAudioDisabled())
		{
			addControl(row, presenterSelfTextArray[2], controlsProvider.getImage3ClickListener(), true);
		}
		addControl(row, presenterSelfTextArray[7], signoutListener, true);
		
	}
	
	private void attendeeOnSelf(VerticalPanel row) {
//		if("true".equalsIgnoreCase(ConferenceGlobals.getShowInviteLinks()))
		if (this.allowInvites)
		{
			addControl(row, attendeeSelfTextArray[0], sendInviteListener, true);
		}
		addControl(row, attendeeSelfTextArray[4], feedbackListener, true);
		HTML line1 = new HTML("&nbsp;");
		line1.setStyleName("section-break");
		row.add(line1);
		
		addControl(row, attendeeSelfTextArray[5], changeDisplayListener, true);
		addMoodControl(row, attendeeSelfTextArray[3], moodListener, false);
		
		HTML line2 = new HTML("&nbsp;");
		line2.setStyleName("section-break");
		row.add(line2);
		
		if(me.isAudioOn() || me.isVideoOn())
		{
			addControl(row, attendeeSelfTextArray[2], controlsProvider.getImage3ClickListener(), true);
		}else{
			addControl(row, attendeeSelfTextArray[2], (ClickListener)null, true);	
		}
		
		addControl(row, attendeeSelfTextArray[6], signoutListener, true);
	}
	
	protected	Widget	getLinks()
	{
		HorizontalPanel	panel = new HorizontalPanel();
		final UIRosterEntry otherUser = ((UserListEntry)userEntry).getUser();
		if (UIGlobals.isActivePresenter(me) && !me.getUserId().equals(otherUser.getUserId()))
		{
			Label removeLink = new Label(UIStrings.getRemoveLabel());
			removeLink.setStyleName("common-text");
			removeLink.addStyleName("common-anchor");
			removeLink.addStyleName("dm-hover-popup-link");
			removeLink.addStyleName("user-remove-control-link");
			removeLink.addClickListener(new ClickListener()
				{
					public	void	onClick(Widget sender)
					{
						String userName = otherUser.getDisplayName();
						ConfirmationDialog cd = new ConfirmationDialog(
								ConferenceGlobals.getDisplayString("remove.header","Remove")+" "+userName,
								ConferenceGlobals.getDisplayString("remove.desc","Are you sure you want to remove ")+userName,"default-message",
								new ConfirmationListener()
								{
									public	void	onOK()
									{
										userManager.removeUser(otherUser);
									}
									public	void	onCancel()
									{
										
									}
								});
						cd.drawDialog();
						hide();
					}
				});
			panel.add(removeLink);
			panel.setCellHorizontalAlignment(removeLink,HorizontalPanel.ALIGN_LEFT);
		}
		if (me.getUserId().equals(otherUser.getUserId()))
		{
			Label changePictureLink = new Label(UIStrings.getChangePictureLabel());
			changePictureLink.setStyleName("common-text");
			changePictureLink.addStyleName("common-anchor");
			changePictureLink.addStyleName("dm-hover-popup-link");
			changePictureLink.addStyleName("user-change-picture-control-link");
			changePictureLink.addClickListener(new ClickListener()
			{
				public	void	onClick(Widget sender)
				{
					ChangePictureDialog dlg = new ChangePictureDialog(userManager,me);
					dlg.drawDialog();
					hide();
				}
			});
			panel.add(changePictureLink);
			panel.setCellWidth(changePictureLink,"100%");
			panel.setCellHorizontalAlignment(changePictureLink,HorizontalPanel.ALIGN_RIGHT);
		}
		return	panel;
	}
	protected	Label	createLabel(String labelText, String styleName)
	{
		Label html = new Label(labelText);
		html.setStyleName("user-menu-item");
		if (styleName != null)
		{
			html.addStyleName(styleName);
		}
		return	html;
	}

	protected	void	writeHeader3()
	{
		HorizontalPanel	header = new HorizontalPanel();
		/*
		if(ConferenceGlobals.privateChatEnabled)
		{
			addControl(header,userEntry.getImage1Url(),controlsProvider.getImage1ClickListener());
			addControl(header,userEntry.getImage2Url(),controlsProvider.getImage2ClickListener());
		}
		addControl(header,userEntry.getImage3Url(),controlsProvider.getImage3ClickListener());
		addControl(header,userEntry.getImage4Url(),controlsProvider.getImage4ClickListener());*/
		Label userID = new Label(((UserListEntry)userEntry).getUser().getDisplayName());
		userID.setStyleName("user-menu-item");
		header.add(userID);
		
		/*Label email = new Label(me.getEmail());
		email.setStyleName("user-menu-item");
		email.addStyleName("common-anchor");
		header.add(email);*/
		
		header.setCellHorizontalAlignment(userID, HorizontalPanel.ALIGN_LEFT);
		//header.setCellHorizontalAlignment(email, HorizontalPanel.ALIGN_RIGHT);
		//header.setBorderWidth(1);
		headerPanel.add(header);
		
		header.setSize("100%", "100%");
		headerPanel.setCellWidth(header,"100%");
	}
	
	private	void	addAudioControl(VerticalPanel panel, String labelText1, int labelText2, ClickListener clickListener, boolean hideOnClick)
	{
		this.addControl(panel,labelText1+"  ("+labelText2+ " left)",clickListener,hideOnClick);
		/*
		HorizontalPanel imageNText = new HorizontalPanel();
		FocusPanel	focusPanel = new FocusPanel();
		
		Label lbl = new Label(labelText1);
		lbl.setStyleName("user-menu-item");
		imageNText.add(lbl);
		imageNText.setCellHorizontalAlignment(lbl, HorizontalPanel.ALIGN_RIGHT);
		
		FocusPanel fp = new FocusPanel();
		Label lbl1 = new Label("("+labelText2+ " left)");
		lbl1.setStyleName("user-menu-item");
		//lbl1.addStyleName("user-list-disabled");
		//hp.addStyleName("user-list-disabled");
		
		fp.add(lbl1);
		imageNText.add(fp);
		imageNText.setCellHorizontalAlignment(lbl1, HorizontalPanel.ALIGN_RIGHT);
		
		focusPanel.add(imageNText);
		if(null != clickListener)
		{
			focusPanel.addMouseListener(new HoverStyler(moodListener));
			focusPanel.setStyleName("user-list-enabled");
			focusPanel.addClickListener(clickListener);
		}else{
			focusPanel.setStyleName("user-list-disabled");
		}
		if(hideOnClick)
		{
			ClickListener clickListener2 = new ClickListener()
			{
				public	void	onClick(Widget w)
				{
					//Window.alert(" clicked on an item now hiding menu");
					hide();
				}
			};
			focusPanel.addClickListener(clickListener2);
		}
		
		//lbl1.removeStyleDependentName("user-list-hover");
		//lbl1.removeStyleName("user-list-hover td div");
		//hp.removeStyleDependentName("user-list-hover");
		panel.add(focusPanel);
		*/
	}
	
	private	void	addVideoControl(VerticalPanel panel, String labelText1, int labelText2, ClickListener clickListener, boolean hideOnClick)
	{
		this.addControl(panel,labelText1+"  ("+labelText2+ " left)",clickListener,hideOnClick);
		/*
		HorizontalPanel imageNText = new HorizontalPanel();
		FocusPanel	focusPanel = new FocusPanel();
		
		Label lbl = new Label(labelText1);
		lbl.setStyleName("user-menu-item");
		imageNText.add(lbl);
		imageNText.setCellHorizontalAlignment(lbl, HorizontalPanel.ALIGN_RIGHT);
		
		FocusPanel fp = new FocusPanel();
		Label lbl1 = new Label("("+labelText2+ " left)");
		lbl1.setStyleName("user-menu-item");
		//lbl1.addStyleName("user-list-disabled");
		//hp.addStyleName("user-list-disabled");
		
		fp.add(lbl1);
		imageNText.add(fp);
		imageNText.setCellHorizontalAlignment(lbl1, HorizontalPanel.ALIGN_RIGHT);
		
		focusPanel.add(imageNText);
		if(null != clickListener)
		{
			focusPanel.addMouseListener(new HoverStyler(moodListener));
			focusPanel.setStyleName("user-list-enabled");
			focusPanel.addClickListener(clickListener);
		}else{
			focusPanel.setStyleName("user-list-disabled");
		}
		if(hideOnClick)
		{
			ClickListener clickListener2 = new ClickListener()
			{
				public	void	onClick(Widget w)
				{
					//Window.alert(" clicked on an item now hiding menu");
					hide();
				}
			};
			focusPanel.addClickListener(clickListener2);
		}
		
		//lbl1.removeStyleDependentName("user-list-hover");
		//lbl1.removeStyleName("user-list-hover td div");
		//hp.removeStyleDependentName("user-list-hover");
		panel.add(focusPanel);
		*/
	}
	
	/**
	 * This will create a menu item with one label
	 * @param panel
	 * @param labelText
	 * @param clickListener
	 * @param hideOnClick
	 */
	private	void	addMoodControl(VerticalPanel panel, String labelText, ClickListener clickListener, boolean hideOnClick)
	{
		HorizontalPanel imageNText = new HorizontalPanel();
//		FocusPanel	focusPanel = new FocusPanel();
		Label lbl = new Label(labelText);
		lbl.setStyleName("user-menu-entry");
		imageNText.add(lbl);
		imageNText.setCellHorizontalAlignment(lbl, HorizontalPanel.ALIGN_LEFT);
		
		HTML lbl2 = new HTML("&raquo;");
		//lbl2.setStyleName("user-menu-item");
		imageNText.add(lbl2);
		imageNText.setCellHorizontalAlignment(lbl2, HorizontalPanel.ALIGN_RIGHT);
		imageNText.setCellWidth(lbl, "100%");
		imageNText.setCellHeight(lbl2, "100%");
		imageNText.setWidth("100%");
		//imageNText.setBorderWidth(1);
//		focusPanel.add(imageNText);
		
		if(null != clickListener)
		{
			HoverStyler hs = new HoverStyler(moodListener,imageNText,null);
			lbl.addMouseListener(hs);
			lbl.addStyleName("user-menu-entry-enabled");
			lbl.addClickListener(clickListener);
			lbl2.addMouseListener(hs);
			lbl2.addClickListener(clickListener);
		}else{
			lbl.addStyleName("user-menu-entry-disabled");
		}
		if(hideOnClick)
		{
			ClickListener clickListener2 = new ClickListener()
			{
				public	void	onClick(Widget w)
				{
					//Window.alert(" clicked on an item now hiding menu");
					hide();
				}
			};
			lbl.addClickListener(clickListener2);
		}
		
		panel.add(imageNText);
		panel.setCellWidth(imageNText, "100%");
	}
	
	
	/**
	 * This will create a menu item with one label
	 * @param panel
	 * @param labelText
	 * @param clickListener
	 * @param hideOnClick
	 */
	private	void	addControl(VerticalPanel panel, String labelText, ClickListener clickListener, boolean hideOnClick)
	{
//		HorizontalPanel imageNText = new HorizontalPanel();
//		FocusPanel	focusPanel = new FocusPanel();
		Label lbl = new Label(labelText);
//		lbl.setStyleName("user-menu-item");
		lbl.setStyleName("user-menu-entry");
//		imageNText.add(lbl);
//		imageNText.setCellWidth(lbl, "100%");
//		imageNText.setCellHorizontalAlignment(lbl, HorizontalPanel.ALIGN_LEFT);
		
//		focusPanel.add(imageNText);
		if(null != clickListener)
		{
			lbl.addMouseListener(new HoverStyler(moodListener));
			lbl.addStyleName("user-menu-entry-enabled");
			lbl.addClickListener(clickListener);
		}else{
			lbl.addStyleName("user-menu-entry-disabled");
		}
		if(hideOnClick)
		{
			ClickListener clickListener2 = new ClickListener()
			{
				public	void	onClick(Widget w)
				{
					//Window.alert(" clicked on an item now hiding menu");
					hide();
				}
			};
			lbl.addClickListener(clickListener2);
		}
		panel.add(lbl);
		panel.setCellWidth(lbl, "100%");
	}
	
	protected	Label	createTextHTML(String text, String styleName)
	{
		Label html = new Label(text);
		html.setStyleName("common-table-text");
		if (styleName != null)
		{
			html.addStyleName(styleName);
		}
		return	html;
	}
	
	/*public void onClick(Widget sender)
	{
		//Window.alert("clicked...");
	}*/

}

class MoodListener implements ClickListener{

	UserListEntryHoverPopup parentMenu = null;
	MoodControlPopup mcp = null;
	public MoodListener(UserListEntryHoverPopup parentMenu)
	{
		this.parentMenu = parentMenu;
	}
	
	public	void	onClick(Widget sender)
	{
		parentMenu.setSubMeuOpen(true);
		//Window.alert(" Just before creating the mood pop up");
		if(null == mcp)
		{
			mcp = new MoodControlPopup(parentMenu.me, parentMenu.userEntry, parentMenu.userManager, parentMenu);
		}
		//Window.alert("offset width = "+parentMenu.getOffsetWidth());
		int left = parentMenu.getAbsoluteLeft() + (parentMenu.getOffsetWidth()+3);
		int top = sender.getAbsoluteTop()+3;
		mcp.setPopupPosition(left,top);
		//mcp.setDialogPosition(left,top);
		//Window.alert(" Just before showing the mood pop up");
		mcp.show();
	}

}
