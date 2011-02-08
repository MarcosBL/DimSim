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
 * Part of the DimDim V 4.1 Codebase (http://www.dimdim.com)	          *
 *                                                                        *
 * Copyright (c) 2008 Communiva Inc. All Rights Reserved.                 *
 *                                                                        *
 *                                                                        *
 * This code is licensed under the DimDim License                         *
 * For details please visit http://www.dimdim.com/license                 *
 *                                                                        *
 **************************************************************************
 */

package com.dimdim.conference.ui.common.client.util;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */
public interface DimdimPopup
{
	public	static	int		VideoBroadcaster	=	0;
	public	static	int		AudioBroadcaster	=	1;
	public	static	int		VideoPlayer		=	2;
	public	static	int		PrivateChatBox	=	3;
	
	public	String	getPopupId();
	
	public	int		getPopupType();
	
	public	int		getPopupWidth();
	
	public	int		getPopupHeight();
	
	public	void	setPopupPosition(int left, int top);
}

