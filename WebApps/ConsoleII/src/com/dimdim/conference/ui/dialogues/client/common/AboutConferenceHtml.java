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

package com.dimdim.conference.ui.dialogues.client.common;

import com.dimdim.conference.ui.common.client.UIStrings;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Image;

/**
 * @author Saurav Mohapatra
 *
 */
public class AboutConferenceHtml	extends	FlexTable
{
	public	AboutConferenceHtml()
	{
//		UIResources  uiResources = UIResources.getUIResources();
		//Window.alert("test about box html");
		String	aboutBoxLogo   = UIStrings.getAboutLogo();
		Image logo = new Image();
		logo.setUrl(aboutBoxLogo);
		
		FlexTable productInfo = new FlexTable();
		productInfo.setWidget(0,0,logo);		
		setWidget(0,10,productInfo);
	}
	
}
