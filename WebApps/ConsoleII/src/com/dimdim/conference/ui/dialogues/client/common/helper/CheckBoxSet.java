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

import java.util.Vector;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * This set enforces that within the given group of checkboxes only a fixed
 * number will be enabled at a time.
 * 
 * 1.	The set itself will not change the state of the checkboxes, this is
 * 		strictly a helper to manager ui.
 * 2.	The set is constructed only with the max enabled number. This can be
 * 		changed during the lifetime of the set, 0 is allowed to mean that the
 * 		whole set of boxes are simply disabled.
 * 3.	Through the click listeners the set will keep track of the enabled
 * 		boxes. When the maximum number of boxes are checked, the set will
 * 		disable the other unchecked boxes.
 * 4.	When at least of the checked boxes is unchecked, the other unchecked
 * 		boxes will be enabled.
 * 5.	The set will also accept an optional label to put a message about
 * 		why the checkboxes are disabled.
 */

public class CheckBoxSet implements ClickListener
{
	protected	int		maxChecked;
	protected	int		currentlyChecked;
	protected	Vector	boxes;
	protected	Label	messageLabel;
	
	public	CheckBoxSet(int maxChecked, Label messageLabel)
	{
		this.maxChecked = maxChecked;
		this.currentlyChecked = 0;
		this.messageLabel = messageLabel;
		this.boxes = new Vector();
	}
	public	void	addCheckBox(CheckBox cb)
	{
		if (!this.boxes.contains(cb))
		{
			this.boxes.addElement(cb);
			if (cb.isChecked())
			{
				incrementCheckedCount();
			}
			else
			{
				this.setSpaceAvailability();
			}
			cb.addClickListener(this);
		}
	}
	public	void	removeCheckBox(CheckBox cb)
	{
		if (this.boxes.contains(cb))
		{
			this.boxes.remove(cb);
			if (cb.isChecked())
			{
				decrementCheckedCount();
			}
			cb.removeClickListener(this);
		}
	}
	public	void	onClick(Widget w)
	{
		CheckBox cb = (CheckBox)w;
		if (cb.isChecked())
		{
			this.incrementCheckedCount();
		}
		else
		{
			this.decrementCheckedCount();
		}
	}
	public	boolean	hasSpace()
	{
		return (this.currentlyChecked < this.maxChecked);
	}
	public	int	getCurrentlyChecked()
	{
		return	this.currentlyChecked;
	}
	protected	void	setSpaceAvailability()
	{
		if (this.currentlyChecked < this.maxChecked)
		{
			this.setAllEnabled(true);
		}
		else
		{
			this.setAllEnabled(false);
		}
	}
	protected	void	setAllEnabled(boolean b)
	{
		int	sz = this.boxes.size();
		for (int i=0; i<sz; i++)
		{
			CheckBox cb = (CheckBox)this.boxes.elementAt(i);
			if (!cb.isChecked())
			{
				cb.setEnabled(b);
			}
		}
	}
	protected	void	incrementCheckedCount()
	{
		this.currentlyChecked++;
		this.setSpaceAvailability();
	}
	protected	void	decrementCheckedCount()
	{
		this.currentlyChecked--;
		this.setSpaceAvailability();
	}
	public int getMaxChecked()
	{
		return maxChecked;
	}
	public void setMaxChecked(int maxChecked)
	{
		this.maxChecked = maxChecked;
	}
	public Label getMessageLabel()
	{
		return messageLabel;
	}
	public void setMessageLabel(Label messageLabel)
	{
		this.messageLabel = messageLabel;
	}
}
