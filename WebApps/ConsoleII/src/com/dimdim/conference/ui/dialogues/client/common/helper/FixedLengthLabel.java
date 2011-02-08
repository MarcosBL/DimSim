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

package com.dimdim.conference.ui.dialogues.client.common.helper;

import com.google.gwt.user.client.ui.Label;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * This class creates a label with fixed length. If the given string is
 * longer than the specified max, the string is truncated in the length
 * and the full string is attached as a popup through focus listener.
 */

public class FixedLengthLabel extends Label
{
	protected	int	maxLength;
	protected	String	fullText;
	
	public FixedLengthLabel(String text, int ml)
	{
		super(text);
		this.fullText = text;
		this.maxLength = ml;
		this.setText(text);
	}
	protected	static	String	trimText(String text, int mL)
	{
		String s = text;
		if (s.length() >= mL)
		{
			s = text.substring(0,mL);
			s += "...";
		}
		return	s;
	}
	public	void	setText(String text)
	{
		this.fullText = text;
		super.setText(trimText(text,this.maxLength));
	}
	public int getMaxLength()
	{
		return maxLength;
	}
	public void setMaxLength(int maxLength)
	{
		this.maxLength = maxLength;
		super.setText(trimText(fullText,this.maxLength));
	}
}
