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

package com.dimdim.conference.ui.common.client.util;

import java.util.Iterator;
import java.util.Vector;

import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.ClickListener;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * This simple utility allows all the checkboxes in the to toggle their
 * state. This could be used by all the control panels that have and support
 * a list of check boxes.
 */

public class RadioButtonList implements ClickListener
{
	protected	Vector	radioButtons = new Vector();
	protected	boolean	radioButtonsEnabled = true;
	
	public	RadioButtonList()
	{
		
	}
	public	void	addRadioButton(RadioButton checkBox)
	{
		this.radioButtons.addElement(checkBox);
	}
	public	void	removeRadioButton(RadioButton checkBox)
	{
		this.radioButtons.remove(checkBox);
	}
	public	void	removeRadioButton(int i)
	{
		this.radioButtons.remove(i);
	}
	public	void	onClick(Widget sender)
	{
		radioButtonsEnabled = !radioButtonsEnabled;
		int size = this.radioButtons.size();
		for (int i=0; i<size; i++)
		{
			RadioButton cb = (RadioButton)(this.radioButtons.elementAt(i));
			cb.setChecked(radioButtonsEnabled);
		}
	}
}
