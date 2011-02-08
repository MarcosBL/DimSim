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
import org.red5.server.api.IScope;
import org.red5.server.api.so.ISharedObject;

public class WhiteboardPages {

	protected String prefix;
	protected ISharedObject pages;
//	protected HashMap whiteboards;
	private MeetingWhiteboard meetingWhiteboard;
	
	public WhiteboardPages(String prefix, String name, boolean isPersistent,
			String storageDir, ISharedObject sharedObject,
			MeetingWhiteboard meetingWhiteboard)
	{
		this.meetingWhiteboard = meetingWhiteboard;
		this.prefix = prefix;
		pages = meetingWhiteboard.getWhiteboardApp().getSharedObject(meetingWhiteboard.roomScope, name, isPersistent);	
		if (pages == null)
		{
			meetingWhiteboard.getWhiteboardApp().createSharedObject(meetingWhiteboard.roomScope, name, isPersistent);
			pages = meetingWhiteboard.getWhiteboardApp().getSharedObject(meetingWhiteboard.roomScope, name, isPersistent);
		}
//		this.whiteboards = whiteboards;
	}
	
//	public WhiteboardPages(String prefix, ISharedObject pages, Whiteboard whiteboardApp)
//	{
//		this.pages = pages;
//		this.prefix = prefix;
//		this.whiteboardApp = whiteboardApp;
//		
//		System.out.println("Reading existing whiteboard pages object, Number of pages:"+getL()+", Current page:"+getC());
//	}
	protected ISharedObject getPagesSO()
	{
		return this.pages;
	}
	protected ISharedObject getSO()
	{
		return this.pages;
	}
	public int getL()
	{
		if(this.pages.getAttribute("l") != null)
		{
			Integer i = (Integer)this.pages.getAttribute("l");
			return i.intValue();	
		}
		else
		{
			this.setL(1);
		}
		return 1;
	}
	public void setL(int l)
	{
		this.pages.setAttribute("l", new Integer(l));		
	}
	public int getC()
	{
		if(this.pages.getAttribute("c") !=null)
		{
			Integer c = (Integer)this.pages.getAttribute("c");
			return c.intValue();	
		}
		else
		{
			this.setC(0);
		}
		return 0;
	}
	public void setC(int c)
	{
		this.pages.setAttribute("c", new Integer(c));
	}
	public int getPtrX()
	{
		if(this.pages.getAttribute("ptr_x") !=null)
		{
			Integer ptrX = (Integer)this.pages.getAttribute("ptr_x");
			return ptrX.intValue();	
		}
		else
		{
			this.setPtrX(-1000);
		}
		return -1000;
	}
	public void setPtrX(int ptrX)
	{
		this.pages.setAttribute("ptr_x", new Integer(ptrX));
	}
	public int getPtrY()
	{
		if(this.pages.getAttribute("ptr_y") !=null)
		{
			Integer ptrY = (Integer)this.pages.getAttribute("ptr_y");
			return ptrY.intValue();	
		}
		else
		{
			this.setPtrY(-1000);
		}
		return -1000;
	}
	public void setPtrY(int ptrY)
	{
		this.pages.setAttribute("ptr_y", new Integer(ptrY));
	}
	public String getLS()
	{
		if(this.pages.getAttribute("ls") !=null)
		{
			String ls = (String)this.pages.getAttribute("ls");
			return ls;	
		}
		else
		{
			this.setLS("u");
		}
		return "u";
	}
	public void setLS(String ls)
	{
		this.pages.setAttribute("ls", ls);
	}
	public WhiteboardPage createPage(String prefix, IScope pageScope,
			boolean isPersistent, ISharedObject sharedObject, int pageIndex)
	{
		String pageName = prefix + ".p" + pageIndex;
		WhiteboardPage page = new WhiteboardPage(pageName, isPersistent,
				sharedObject.getPath(), this.meetingWhiteboard);
		this.setL(pageIndex + 1);
		return page;
	}
}

