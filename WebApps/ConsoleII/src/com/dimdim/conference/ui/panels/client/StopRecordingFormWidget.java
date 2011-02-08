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
 * Part of the DimDim V 2.0 Codebase (http://www.dimdim.com)	          *
 *                                                                        *
 * Copyright (c) 2008 Dimdim Inc. All Rights Reserved.              	  *
 *                                                                        *
 *                                                                        *
 * This code is licensed under the DimDim License                         *
 * For details please visit http://www.dimdim.com/license                 *
 *                                                                        *
 **************************************************************************
 */

package com.dimdim.conference.ui.panels.client;


import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.Window;

import com.dimdim.conference.ui.common.client.UIGlobals;
import com.dimdim.conference.ui.common.client.util.FlashCallbackHandler;
import com.dimdim.conference.ui.common.client.util.FlashStreamHandler;
import com.dimdim.conference.ui.json.client.ResponseAndEventReader;
import com.dimdim.conference.ui.json.client.UIPopoutPanelData;
import com.dimdim.conference.ui.json.client.UIPresentationControlEvent;
import com.dimdim.conference.ui.json.client.UIRosterEntry;
import com.dimdim.conference.ui.json.client.UIResourceObject;
import com.dimdim.conference.ui.model.client.ClientModel;
import com.dimdim.conference.ui.model.client.ConferenceGlobals;
import com.dimdim.conference.ui.model.client.PopoutPanelProxy;
import com.dimdim.conference.ui.model.client.PopoutSupportingPanel;
import com.dimdim.conference.ui.model.client.PPTSharingModel;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * This widget will ask the host following questions.
 * 
 * a. Do you want to stop the recording, it cannot be extended.
 * b. Do you want to save the recording.
 * c. Do you want to upload the recording to biz tv.
 * d. Enter title, description and additional tags, optional.
 * 
 * The layout will be simple. Two check boxes and a few input fields.
 * Overall confirmation is yes, no. Once ok is selected the box will
 * give appropriate message and close after two seconds.
 */

public class StopRecordingFormWidget extends	VerticalPanel
{
	protected	Label		uploadComment = new Label();
	protected	Label		uploadQuestion = new Label();
	protected	Label		titleLabel = new Label();
	protected	TextBox		titleTextBox = new TextBox();
	protected	Label		descriptionLabel = new Label();
	protected	TextBox		descriptionTextBox = new TextBox();
	protected	Label		categoryLabel = new Label();
	protected	TextBox		categoryTextBox = new TextBox();
	protected	Label		keywordsLabel = new Label();
	protected	TextBox		keywordsTextBox = new TextBox();
	
	public	StopRecordingFormWidget(String title, String description, String category, String keywords)
	{
		this.uploadQuestion.setText(ConferenceGlobals.getDisplayString("record.question2","Do you want to upload this recording to blip.tv?"));
		this.titleLabel.setText(ConferenceGlobals.getDisplayString("record.title","Title"));
		this.titleTextBox.setText(title);
		this.descriptionLabel.setText(ConferenceGlobals.getDisplayString("record.description","Description"));
		this.descriptionTextBox.setText(description);
		this.categoryLabel.setText(ConferenceGlobals.getDisplayString("record.category","Category"));
		this.categoryTextBox.setText(category);
		this.keywordsLabel.setText(ConferenceGlobals.getDisplayString("record.keywords","Keywords"));
		this.keywordsTextBox.setText(keywords);
		
		this.addField(this.uploadQuestion, null, "100%", "");
//		this.addField(this.titleLabel, this.titleTextBox, "30%", "70%");
//		this.addField(this.descriptionLabel, this.descriptionTextBox, "30%", "70%");
//		this.addField(this.categoryLabel, this.categoryTextBox, "30%", "70%");
//		this.addField(this.keywordsLabel, this.keywordsTextBox, "30%", "70%");
		this.addField(this.titleTextBox, this.descriptionTextBox, "50%", "50%");
		this.addField(this.titleLabel, this.descriptionLabel, "50%", "50%");
		this.addField(this.categoryTextBox, this.keywordsTextBox, "30%", "50%");
		this.addField(this.categoryLabel, this.keywordsLabel, "50%", "50%");
	}
	private	void	addField(Widget w1, Widget w2, String w1Width, String w2Width)
	{
		HorizontalPanel hp1 = new HorizontalPanel();
		w1.setStyleName("common-text");
		hp1.add(w1);
		hp1.setCellWidth(w1, w1Width);
		if (w2 != null)
		{
			w2.setStyleName("common-text");
			hp1.add(w2);
			hp1.setCellWidth(w2, w2Width);
		}
		this.add(hp1);
		this.setCellWidth(hp1, "100%");
	}
	public	String	getTitleText()
	{
		String s = this.titleTextBox.getText();
		if (s.length() > 0)
		{
			s = this.encodeBase64(s);
		}
		return	s;
	}
	public	String	getDescriptionText()
	{
		String s = this.descriptionTextBox.getText();
		if (s.length() > 0)
		{
			s = this.encodeBase64(s);
		}
		return	s;
	}
	public	String	getCategoryText()
	{
		String s = this.categoryTextBox.getText();
		if (s.length() > 0)
		{
			s = this.encodeBase64(s);
		}
		return	s;
	}
	public	String	getKeywordsText()
	{
		String s = this.keywordsTextBox.getText();
		if (s.length() > 0)
		{
			s = this.encodeBase64(s);
		}
		return	s;
	}
	private	native	String	encodeBase64(String s) /*-{
		return $wnd.Base64.encode(s);
	}-*/;
}

