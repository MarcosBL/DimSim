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

package com.dimdim.conference.ui.envcheck.client.command;

import com.google.gwt.user.client.ui.HTML;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */

public class BaseFlashHtml	extends	HTML
{
	public	BaseFlashHtml(String swfUrl, int width, int height)
	{
		super("<table width=\"100%\" height=\"100%\" cellspacing=\"0\" border=\"0\" cellpadding=\"0\" valign=\"top\" align=\"center\"><tr align=\"center\" valign=\"top\"><td align=\"center\" valign=\"top\">"
		+ "<object classid=\"clsid:d27cdb6e-ae6d-11cf-96b8-444553540000\" codebase=\"http://fpdownload.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=7,0,0,0\" width=\""
		+ width
		+ "\" height=\""
		+ height
		+ "\" id=\"dimdimPPTViewer\" align=\"center\">"
		+ "<param name=\"allowScriptAccess\" value=\"sameDomain\" />"
		+ "<param name=\"movie\" value=\""
		+ swfUrl
		+ "\" />"
		+ "<param name=\"quality\" value=\"high\" />"
		+ "<param name=\"wmode\" value=\"transparent\" />"
		+ "<param name=\"bgcolor\" value=\"#ffffff\" />"
		+ "<embed src=\""
		+ swfUrl
		+ "\" quality=\"high\" bgcolor=\"#ffffff\" width=\""
		+ width
		+ "\" height=\""
		+ height
		+ "\" name=\"dimdimPPTViewer\" wmode=\"transparent\" align=\"center\" allowScriptAccess=\"sameDomain\" type=\"application/x-shockwave-flash\" pluginspage=\"http://www.macromedia.com/go/getflashplayer\" />"
		+ "</object></td></tr></table>");
	}
}
