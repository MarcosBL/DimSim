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
 *	File Name  : AVWindow.java
 *  Created On : Jun 30, 2006
 *  Created By : Saurav Mohapatra
 **************************************************************************
 */

package com.dimdim.conference.ui.user.client;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.ComplexPanel;

import pl.rmalinowski.gwt2swf.client.ui.SWFCallableWidget;
import pl.rmalinowski.gwt2swf.client.ui.SWFParams;

//import com.dimdim.conference.ui.common.client.util.DmFlashWidget2;

/**
 * @author Saurav Mohapatra
 * 
 */
public class AVWindow extends ComplexPanel
{
//	protected	UIRosterEntry	me;
	protected	Element	outer;
	protected	SWFCallableWidget	movie;
//	protected	DmFlashWidget2	movie;
	
	protected	String	movieUrl;
	protected	int	width;
	protected	int	height;
	protected	String	outerNodeName;
	protected	String	flashWidgetNodeName;
	protected	String		currentMovieId;
	
	public AVWindow(String outerNodeName, String flashWidgetNodeName,
				String movieUrl, int width, int height)
	{
//		Window.alert("AVWindow::AVWindow");
		
		this.outerNodeName = outerNodeName;
		this.flashWidgetNodeName = flashWidgetNodeName;
		this.movieUrl = movieUrl;
		this.width = width;
		this.height = height;
		outer = DOM.createDiv();
		setElement(outer);
		currentMovieId = flashWidgetNodeName;
//		DOM.setAttribute(outer,"id",outerNodeName);
		
		addMovie(movieUrl, width, height);
	}
	public	boolean	stopAV(String movieNodeId)
	{
		if (this.movie != null)
		{
//			this.myListEntryPanel.clearMovie();
			try
			{
//				movie.stop(UIConstants.AUDIO_BROADCASTER_PLAYER_MOVIE_ID);
//				return	movie.stop(currentMovieId);
				movie.call("stopBroadcastingOrReceiving");
				return	true;
			}
			catch(Exception e)
			{
//				Window.alert(e.getMessage());
			}
		}
		return	false;
	}
	protected	void	addMovie(String movieUrl, int width, int height)
	{
		if (this.movie != null)
		{
			super.remove(movie);
		}
//		currentMovieId = UIConstants.VIDEO_BROADCASTER_PLAYER_MOVIE_ID;//"m"+ConferenceGlobals.getClientGUID();
//		movie = new DmFlashWidget2(currentMovieId+"_id",
//				currentMovieId,
//				width+"",height+"",movieUrl,"white");
		
		SWFParams wbWidgetParams = new SWFParams(movieUrl,width,height);
		movie = new SWFCallableWidget(wbWidgetParams);
		
		movie.addStyleName("discussion-av-top-margin");
		super.add(movie,outer);
//		movie.show();
	}
	protected	void	startMovie()
	{
//		if (this.movie != null)
//		{
//			this.movie.show();
//		}
	}
//	protected	void	resizeMovie(int width, int height)
//	{
//		this.width = width;
//		this.height = height;
//	}
	public	void	restartAsRecord()
	{
		if (this.movie != null)
		{
//			Window.alert("Calling restartAsRecord");
			this.movie.call("restartAsRecord");
		}
	}
	public	void	restartAsLive()
	{
		if (this.movie != null)
		{
//			Window.alert("Calling restartAsLive");
			this.movie.call("restartAsLive");
		}
	}
}
