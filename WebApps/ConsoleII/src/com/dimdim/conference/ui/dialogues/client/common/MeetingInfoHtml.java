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

package com.dimdim.conference.ui.dialogues.client.common;

import pl.rmalinowski.gwt2swf.client.ui.SWFParams;
import pl.rmalinowski.gwt2swf.client.ui.SWFWidget;

import com.dimdim.conference.ui.common.client.UIStrings;
import com.dimdim.conference.ui.common.client.util.CopyToClipBoard;
import com.dimdim.conference.ui.json.client.UIRosterEntry;
import com.dimdim.conference.ui.model.client.ClientModel;
import com.dimdim.conference.ui.model.client.ConferenceGlobals;
import com.dimdim.conference.ui.model.client.UIResources;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * A simple example of an 'about' dialog box.
 */
public class MeetingInfoHtml extends FlexTable
{
	TextBox joinUrl = null;
	
	public MeetingInfoHtml()
	{
		UIResources  uiResources = UIResources.getUIResources();
//		FlexTable	table = new FlexTable();
		int i = 0;
		String	subject = uiResources.getConferenceInfoAndDecode64("subject");
		String	key = uiResources.getConferenceInfo("key");
		UIRosterEntry host = ClientModel.getClientModel().getRosterModel().getCurrentHost();
		String	organizerName = host.getDisplayName();
		//String	organizerName = uiResources.getConferenceInfo("organizerName");
		//organizerName = UIRosterEntry.decodeBase64(organizerName);
		if (organizerName.length() > 50)
		{
			organizerName = organizerName.substring(0, 50);
		}
		String	organizerEmail = uiResources.getConferenceInfo("organizerEmail");
		String	startDate = uiResources.getConferenceInfo("startDate");
		String	joinURL = uiResources.getConferenceInfo("joinURL");
		//String	mailToTag = "<a href=\"mailto:"+organizerEmail+
		//	"?subject="+subject+"\">"+organizerName+"</a>";
		
		this.setWidget(i, 0, createLabel(UIStrings.getMeetingInfoSubjectLabel()));
		this.setWidget(i++, 1, createTextHTML(subject));
		this.setWidget(i, 0, createLabel(UIStrings.getMeetingInfoKeyLabel()));
		this.setWidget(i++, 1, createTextHTML(key));
		this.setWidget(i, 0, createLabel(UIStrings.getMeetingInfoOrganizerLabel()));
		this.setWidget(i++, 1, createTextHTML(organizerName));
		this.setWidget(i, 0, createLabel(UIStrings.getMeetingInfoStartTimeLabel()));
		this.setWidget(i++, 1, createTextHTML(startDate));
		
		if(ConferenceGlobals.showPhoneInfo)
		{
			//this.setWidget(i, 0, createLabel(UIStrings.getTollFreeInfoLabel()));
			//this.setWidget(i++, 1, createTextHTML(ConferenceGlobals.tollFree));
			if(ConferenceGlobals.internToll.length() > 0)
			{
				this.setWidget(i, 0, createLabel(UIStrings.getTollInfoLabel()));
				this.setWidget(i++, 1, createTextHTML(ConferenceGlobals.internToll));
			}
			//this.setWidget(i, 0, createLabel(UIStrings.getInternTollFreeLabel()));
			//this.setWidget(i++, 1, createTextHTML(ConferenceGlobals.internTollFree));
			//this.setWidget(i, 0, createLabel(UIStrings.getInternTollLabel()));
			//this.setWidget(i++, 1, createTextHTML(ConferenceGlobals.internToll));
			if(ConferenceGlobals.attendeePasscode.length() > 0)
			{
				this.setWidget(i, 0, createLabel(UIStrings.getAttendePasscodeLabel()));
				this.setWidget(i++, 1, createTextHTML(ConferenceGlobals.attendeePasscode));
			}
			//this.setWidget(i, 0, createLabel(UIStrings.getModeratorPasscodeLabel()));
			//this.setWidget(i++, 1, createTextHTML(ConferenceGlobals.moderatorPassCode));
		}
		this.setWidget(i, 0, createLabel(UIStrings.getMeetingInfoJoinURLLabel()));
		//if (joinURL.length() > 78)
		//{
		//	joinUrl = new TextBox();
		//	joinUrl.setName("join_URL_text");
		//	joinUrl.setText(joinURL);
		//	joinUrl.setStyleName("common-text");
		//	joinUrl.addStyleName("meeting-info-text");
		//	joinUrl.addStyleName("meeting-info-text-box");
		//	this.setWidget(i++, 1, joinUrl);
		//}
		//else
		//{
		//FocusPanel fp = new FocusPanel();
		//CopyToClipBoard copyListener = new CopyToClipBoard(joinURL);
		//fp.addClickListener(copyListener);
		//HTML htmlTextBox = createTextAreaHTML(joinURL);
		VerticalPanel vp = new VerticalPanel();
		//vp.add(htmlTextBox);
		String fullMovieUrl = ConferenceGlobals.baseWebappURL+"html/layout2/clipbdMeetInfo.swf";
		
		SWFParams wbWidgetParams = new SWFParams(fullMovieUrl,"334", "18", "#ffffff");
		wbWidgetParams.setWmode("");
		wbWidgetParams.addVar("roomName", key);
		wbWidgetParams.addVar("meetingUrl", joinURL);
		if(ConferenceGlobals.isBrowserSafari())
		{
			wbWidgetParams.setWmode("opaque");
		}
		
		SWFWidget movieWidget = new SWFWidget(wbWidgetParams);
		vp.add(movieWidget);
		Label help = new Label(ConferenceGlobals.getDisplayString("console.copy.url.tooltip","Click on the URL to copy it to Clicpboard."));
		help.setStyleName("common-text");
		help.addStyleName("meeting-info-text");
		vp.add(help);
		//vp.setWidth("350px");
		//vp.setBorderWidth(1);
		//fp.add(vp);
		
		this.setWidget(i++, 1, vp);
		
		/*String fullMovieUrl = ConferenceGlobals.baseWebappURL+"html/layout2/clipbdMeetInfo.swf";
		
		SWFParams wbWidgetParams = new SWFParams(fullMovieUrl,"100", "10", "#ff0000");
		wbWidgetParams.setWmode("");
		wbWidgetParams.addVar("roomName", key);
		wbWidgetParams.addVar("meetingUrl", joinURL);
		if(ConferenceGlobals.isBrowserSafari())
		{
			wbWidgetParams.setWmode("opaque");
		}
		
		SWFWidget movieWidget = new SWFWidget(wbWidgetParams);
		this.setWidget(i++, 1, movieWidget);*/
	
		//}
	}
	protected	HTML	createLabel(String labelText)
	{
		HTML html = new HTML();
		html.setHTML("<span>"+labelText+": </span>");
		html.setStyleName("common-text");
		html.addStyleName("meeting-info-label");
		return	html;
	}
	protected	HTML	createTextHTML(String text)
	{
		HTML html = new HTML();
		String textWithBreaks = getTextWithBreaks(text);
		//String textWithBreaks = text;
		html.setHTML("<span>"+textWithBreaks+"</span>");
		html.setStyleName("common-text");
		html.addStyleName("meeting-info-text");
		return	html;
	}
	
	private String getTextWithBreaks(String text) {
		String[] words = text.split(" ");
		StringBuffer returnString = new StringBuffer();
		String singleWord = "";
		String singleWordWithBreaks = "";
		
		String htmlWordBreak = "<wbr>";
		
		int maxWordLength = 20;
		
		if (ConferenceGlobals.isBrowserSafari())
		{
			htmlWordBreak = "&#8203;";
		}
		for (int i = 0; i < words.length; i++) {
			singleWord = words[i];
			if(singleWord.length() > maxWordLength)
			{
				int len = 0;
				int buffLen = singleWord.length();
				while(len < buffLen)
				{
					if((buffLen - len) > maxWordLength)
					{
						singleWordWithBreaks +=singleWord.substring(len,len+maxWordLength)+htmlWordBreak;
						len+=maxWordLength;
						continue;
					}
					else
					{
						singleWordWithBreaks +=singleWord.substring(len,buffLen);
						break;
					}	
				}
			}else{
				singleWordWithBreaks = singleWord;
			}
			returnString.append(singleWordWithBreaks);
			//returnString.append(" ");
			singleWordWithBreaks = "";
		}
		//Window.alert("returning "+returnString.toString());
		return returnString.toString();
	}
	protected	HTML	createTextAreaHTML(String text)
	{
		HTML html = new HTML();
		html.setHTML("<input type='text' readonly=true' name='texttocopy' id='texttocopy' class='common-text meeting-info-text-box'" +
				" size='78' value='"+text+"'/>"
				);
		html.setStyleName("common-text");
		//html.addStyleName("meeting-info-text");
		//html.addStyleName("meeting-info-text-box");
		return	html;
	}
	
}
