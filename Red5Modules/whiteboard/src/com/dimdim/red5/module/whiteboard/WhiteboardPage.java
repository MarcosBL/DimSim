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
 * Part of the DimDim V 2.0 Codebase (http://www.dimdim.com)	          *
 *                                                                        *
 * Copyright (c) 2008 Dimdim Inc. All Rights Reserved.                 	  *
 *                                                                        *
 *                                                                        *
 * This code is licensed under the Dimdim License                         *
 * For details please 									                  *
 * 	visit http://www.dimdim.com/opensource/dimdim_license.html			  *
 *                                                                        *
 **************************************************************************
 */

package com.dimdim.red5.module.whiteboard;

/**
 * @author Rajesh Dharmalingam
 * @email rajesh@dimdim.com
 *
 */

import java.util.HashMap;
import java.util.Set;

import org.red5.server.api.so.ISharedObject;

public class WhiteboardPage {

	protected ISharedObject page;

//	protected HashMap whiteboards;
	

	public WhiteboardPage(String pageName, boolean isPersistent,
			String storageDir, MeetingWhiteboard meetingWhiteboard)
	{
//		this.whiteboards = whiteboards;
		page = meetingWhiteboard.getWhiteboardApp().getSharedObject(meetingWhiteboard.roomScope, pageName, isPersistent);
		if (page == null)
		{
			meetingWhiteboard.getWhiteboardApp().createSharedObject(meetingWhiteboard.roomScope, pageName, isPersistent);		
			page = meetingWhiteboard.getWhiteboardApp().getSharedObject(meetingWhiteboard.roomScope, pageName, isPersistent);
		}
	}

//	public WhiteboardPage(ISharedObject page) {
//		this.page = page;
//	}
	protected ISharedObject getPageSO()
	{
		return this.page;
	}
	protected ISharedObject getSO()
	{
		return this.page;
	}
	protected void setPageSO(ISharedObject page)
	{
		this.page = page;
	}
	public int getCount()
	{
		if(this.page.getAttribute("count") != null)
		{
			Integer count = (Integer) this.page.getAttribute("count");		
			return count.intValue();	
		}
		else
		{
			this.setCount(0);
		}
		return 0;
	}

	public void setCount(int l)
	{
		this.page.setAttribute("count", new Integer(l));
	}
	public int getLen()
	{
		if(this.page.getAttribute("len") !=null)
		{
			Integer l = (Integer) this.page.getAttribute("len");
			return l.intValue();	
		}
		else
		{
			this.setLen(1);
		}
		return 1;
	}
	public void setLen(int len)
	{		
		this.page.setAttribute("len", new Integer(len));
	}
	public Object getProperty(String name)
	{
		return this.page.getAttribute(name);
	}
	public void setProperty(String name, Object Data)
	{
		this.page.setAttribute(name, Data);
	}
	public void deleteShape(String shapeId)
	{
		Set names = this.page.getAttributeNames();
		String[] array = (String[])names.toArray(new String[names.size()]);
		String shapePropertyKey = shapeId;
//		System.out.println("shapePropertyKey is : " + shapePropertyKey);
		int size = names.size();
//		System.out.println("size of the names list is : " + size);
		for (int i=0;i<size;i++)
		{
			if(array[i].startsWith(shapePropertyKey))
				this.page.removeAttribute(array[i]);
		}
	}
}
