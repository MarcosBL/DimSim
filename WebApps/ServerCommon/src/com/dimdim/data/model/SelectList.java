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

package com.dimdim.data.model;

import	java.util.Vector;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */

public class SelectList implements JsonSerializable
{
	protected	String	name;
	protected	Vector	options;
	
	public	SelectList(String name)
	{
		this.name = name;
		this.options = new Vector();
	}
	public	void	addOption(String name, String value)
	{
		this.options.addElement(new SelectListOption(name,value));
	}
	public String getObjectClass()
	{
		return "SelectList";
	}
	public String toJson()
	{
		StringBuffer buf = new StringBuffer();
		
		int size = this.options.size();
		buf.append("{name:\"");
		buf.append(name);
		buf.append("\",options:");
		buf.append("[");
		for (int i=0; i<size; i++)
		{
			if (i>0)
			{
				buf.append(",");
			}
			buf.append(((JsonSerializable)this.options.elementAt(i)).toJson());
		}
		buf.append("]");
		buf.append("}");
		
		return buf.toString();
	}
	
}
