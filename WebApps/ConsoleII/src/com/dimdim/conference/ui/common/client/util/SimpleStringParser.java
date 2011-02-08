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

import java.util.Vector;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */

public class SimpleStringParser
{
	public	SimpleStringParser()
	{
	}
	public Vector tokenizeString(String str)
	{
		Vector st = null;
		String tempMsg = str;
		
		tempMsg.trim();
		
		if(tempMsg.length() > 0)
		{
			st = new Vector();
			int index = 0;
			int index1 = 0;
			while (true)
			{
				index1 = tempMsg.indexOf(' ', index);
				if(index1 != -1)
				{
					if(index1 > index)
					{
						String buff = tempMsg.substring(index, index1);
						st.add(buff);
						index = index1+1;
					}
					else
					{
						index = index1+1;
					}
				}				
				else
				{
					String buff = tempMsg.substring(index);
					st.add(buff);
					break;
				}
			}
		}
		
		return st;
	}
}
