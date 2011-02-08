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

package com.dimdim.conference.ui.envcheck.client.layout;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * With the 3.5 version this object is no longer a layout object. The layout and
 * functionality is being seperated. On start, succes and failure this object will
 * add and remove movies and labels as required in the root 
 */

public class CheckPanel	//extends	Composite
{
//	protected	HorizontalPanel	basePanel	=	new	HorizontalPanel();
//	protected	HorizontalPanel	insidePanel	=	new	HorizontalPanel();
	
//	protected	Image			checkImage;
//	protected	HorizontalPanel	imagePanel	=	new	HorizontalPanel();
//	protected	Label			checkText;
//	protected	HorizontalPanel	textPanel	=	new	HorizontalPanel();
	
	protected	Widget		movie;
	
	protected	String		progressImageDivId;
	protected	String		resultTextDivId;
	protected	String		resultImageSucessId;
	//protected	String		resultImageFailureId;
	
//	protected	String		testDescriptionText;
	
	public CheckPanel(int checkIndex, String checkName)
	{
		this.progressImageDivId = checkName+"_check_image";
		this.resultTextDivId = checkName+"_check_message";
		this.resultImageSucessId = checkName + "check_mark_done";
		//this.resultImageFailureId = checkName + "check_mark_error";
	}
	public	void	setCheckInProgress(String message)
	{
		this.clearMovie();
		this.setMessageText(message);
	}
	public	void	setCheckSucceeded(String message)
	{
	    	//Window.alert("setCheckSucceeded message... "+resultTextDivId);
		this.clearMovie();
		RootPanel.get(resultImageSucessId).setStyleName("mark_done");
		//RootPanel.get(resultImageFailureId).setStyleName("Hide");
		
		//Window.alert("setCheckSucceeded message... "+message);
		this.setMessageText(message);
	}
	
	public	void	setCheckFailed(String message)
	{
		this.clearMovie();
		//Window.alert("setCheckFailed message... "+message);
		//RootPanel.get(resultImageFailureId).setStyleName("Show");
		RootPanel.get(resultImageSucessId).setStyleName("mark_error");
		this.setMessageText(message);
	}
	
	public	void	setCheckNotApplicable()
	{
		this.clearMovie();
		//Window.alert("setCheckNotApplicable");
		//RootPanel.get(resultImageFailureId).setStyleName("Hide");
		//RootPanel.get(resultImageSucessId).setStyleName("Hide");
		//this.setMessageText("This check is not applicable....");
	}
	
	public	void	setMovie(Widget movie)
	{
		this.movie = movie;
		RootPanel.get(resultTextDivId).add(movie);
	}
	private	void	clearMovie()
	{
		RootPanel.get(this.resultTextDivId).clear();
		this.movie = null;
//		if (this.movie != null)
//		{
//			RootPanel.get(resultTextDivId).remove(movie);
//		}
	}
	private	void	setMessageText(String text)
	{
	    //Window.alert("result div id ="+resultTextDivId);
	    //Window.alert("div element = "+RootPanel.get(this.resultTextDivId));
		RootPanel.get(this.resultTextDivId).clear();
		RootPanel.get(this.resultTextDivId).add(new Label(""));
		HTML lbl = new HTML(text);
		lbl.setStyleName("console-label");
		RootPanel.get(this.resultTextDivId).add(lbl);
	}
}
