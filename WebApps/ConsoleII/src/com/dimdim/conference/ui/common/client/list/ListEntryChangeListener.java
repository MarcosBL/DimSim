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

package com.dimdim.conference.ui.common.client.list;

import com.google.gwt.user.client.ui.Image;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * This change listener is for the view elements to manage the display of
 * the list entry subject to the entry parameters.
 */

public interface ListEntryChangeListener
{
	public	void	displayRankChanged(int previousDisplayRank, int newDisplayRank);
	
	public	void	image1UrlChanged(Image newUrl);
	
	public	void	nameChanged(String newName);
	
	public	void	image2UrlChanged(Image newUrl);
	
	public	void	image3UrlChanged(Image newUrl);
	
	//public	void	image3UrlChanged(Image newUrl, boolean rightAlign);
	
	public	void	image4UrlChanged(Image newUrl);
	
	public	void	image5UrlChanged(Image newUrl);
	
	public	void	image5UrlChanged(Image newUrl, boolean rightAlign);
	
	public	void	movieModel1Changed(ListEntryMovieModel oldMovie, ListEntryMovieModel newMovie);
	
	public	void	movieModel2Changed(ListEntryMovieModel oldMovie, ListEntryMovieModel newMovie);
}
