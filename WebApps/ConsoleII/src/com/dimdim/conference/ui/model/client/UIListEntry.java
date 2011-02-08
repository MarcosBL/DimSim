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

package com.dimdim.conference.ui.model.client;

import java.lang.Comparable;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * Each list entry is required to comprise a single image on left and label
 * text in the rest of the area.
 * 
 * The rank is expected to be used to move the users in the visible list
 * up and down. The list is to be ordered always with 0 at top and the rest
 * below. The rank can be set arbitrarily the controller must not validate
 * the rank but simply use it. If multiple n are found they can be arranged
 * in any order between the closed n- and n+.
 */

public class UIListEntry implements Comparable
{
	protected	String	id;
	protected	String	labelText;
	protected	String	subText;	//	This text is put below the main text.
	protected	boolean	enforceVisible = false;
	
	protected	String	type;
	protected	String	type2;
	protected	String	type3;
	
	protected	int		rank	=	100;
	
	private	UIListEntry(String id, String labelText)
	{
		this.id = id;
		this.labelText = labelText;
	}
	public UIListEntry(String id, String labelText, String subText,
				String type, String type2, String type3)
	{
		this.id = id;
		this.labelText = labelText;
		this.subText = subText;
		this.type = type;
		this.type2 = type2;
		this.type3 = type3;
	}
	/**
	 * This method is used to refresh current objects from changes within
	 * events. This is required because the list entry panels already have
	 * a handle on the list entry objects. Deleting and recreating the
	 * panels themselves would create flickers in the display. Hence the
	 * actual display change is kept to a minimum by the list entry manager
	 * by updating only the required labels and images as applicable.
	 * @param entry
	 */
	public	void	refreshEntry(UIListEntry entry)
	{
		this.labelText = entry.getLabelText();
		this.subText = entry.getSubText();
		this.type = entry.getType();
		this.type2 = entry.getType2();
		this.type3 = entry.getType3();
	}
	public	int	compareTo(Object o)
	{
		UIListEntry e = (UIListEntry)o;
		
		return	id.compareTo(e.getId());
	}
	public	boolean	equals(Object o)
	{
		return	(this.compareTo(o) == 0);
	}
	public	String		getId()
	{
		return	id;
	}
	public String getLabelText()
	{
		return labelText;
	}
	public void setLabelText(String labelText)
	{
		this.labelText = labelText;
	}
	public String getSubText()
	{
		return subText;
	}
	public void setSubText(String subText)
	{
		this.subText = subText;
	}
	public String getType()
	{
		return type;
	}
	public void setType(String type)
	{
		this.type = type;
	}
	public String getType2()
	{
		return type2;
	}
	public void setType2(String type2)
	{
		this.type2 = type2;
	}
	public String getType3()
	{
		return type3;
	}
	public void setType3(String type3)
	{
		this.type3 = type3;
	}
	public boolean isEnforceVisible()
	{
		return enforceVisible;
	}
	public void setEnforceVisible(boolean enforceVisible)
	{
		this.enforceVisible = enforceVisible;
	}
}
