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

package com.dimdim.conference.ui.common.client.resource;

import com.google.gwt.user.client.ui.Widget;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */

public interface ResourcePresentationMovieContainer
{
	/**
	 * Old method for in panel movie presentation.
	 * 
	 * @param swfUrl
	 * @param width
	 * @param height
	 */
//	public	void	showPresentation(String swfUrl, int width, int height);
	
	/**
	 * New method for in frame presentation.
	 * 
	 * @param swfUrl
	 * @param width
	 * @param height
	 */
	/*
	public	void	showPresentation(String resourceId, String mediaId,
			String pptName, String pptUrl,
			int numberOfSlides, int lastSlideIndex,
			int width, int height, String annotation);
	
	public	void	stopPresentation();
	
	public	void	setAnnotationStatus(String annotation);
	*/
}
