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

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;

import org.red5.io.utils.ObjectMap;
import org.red5.server.api.IClient;
import org.red5.server.api.IConnection;
import org.red5.server.api.IScope;
import org.red5.server.api.so.ISharedObject;

public class MeetingWhiteboard
{
	protected	String		prefix;
	protected	String		name;
	protected	boolean			isPersistent;
	protected	WhiteboardPages	pages;
	protected	WhiteboardPage	currentPage;
	protected	int				currentPageIndex;
	protected	Whiteboard		whiteboardApp;
	protected	IScope			roomScope;
	protected	ISharedObject	roomSharedObject;
	protected	long			lastConnectTime = System.currentTimeMillis();
	
	public	MeetingWhiteboard(Whiteboard whiteboardApp, IScope roomScope)
	{
		this.whiteboardApp = whiteboardApp;
		this.isPersistent = whiteboardApp.isPersistent;
		this.roomScope = roomScope;
		this.name = roomScope.getName();
		
		this.roomScope = roomScope;
	    Object handler = new com.dimdim.red5.module.whiteboard.SOHandler(this);
	    roomScope.registerServiceHandler("wb.board", handler);
		initialize(roomScope);
	}
	public	String	getName()
	{
		return this.name;
	}
	public	Whiteboard	getWhiteboardApp()
	{
		return	this.whiteboardApp;
	}
	protected void initialize(IScope room)
	{
		String meetingName = room.getName();
		System.out.println("meeting name is " + meetingName);
		
//		MeetingWhiteboard meetingWB = new MeetingWhiteboard(this);		
//		room.setAttribute(meetingName, meetingWB);
//		this.whiteboards.put(meetingName, meetingWB);
		if (whiteboardApp.getSharedObject(room, room.getName(), isPersistent) == null)
		{
			whiteboardApp.createSharedObject(room, room.getName(), isPersistent);
		}
		
		roomSharedObject = whiteboardApp.getSharedObject(room, room.getName(), isPersistent);
		
		this.name = room.getName();
		this.prefix = "wb.board";
		this.currentPageIndex = 0;
		String pagesSOName = prefix + ".pages";
//		String cacheSOName = prefix + ".cache";
		
//		cache = whiteboardApp.getSharedObject(room, cacheSOName,	isPersistent);
//		System.out.println("Cache got the shared object : " + cacheSOName);
//		
//		if (cache == null)
//		{							
//			createSharedObject(room, cacheSOName, isPersistent);
//			cache = this.getSharedObject(room, cacheSOName, isPersistent);
//			sharedObject.setAttribute( cacheSOName, cache );
//			cache.setAttribute( "0", new Integer(0));
//			cache.setAttribute( "0_undo", new Integer(""));
//		} 
		
//		System.out.println("check the boolean value for pagesSOName : " + sharedObject.hasAttribute(pagesSOName));
		
		ISharedObject pagesSO = whiteboardApp.getSharedObject(room, pagesSOName, isPersistent);
		System.out.println("getSharedObject(room, pagesSOName, isPersistent) : " + pagesSO);
		if (pagesSO == null)
		{
			//	New whiteboard.
			System.out.println("Creating new whiteboard ---------------");
			pages = new WhiteboardPages(prefix, pagesSOName, isPersistent,room.getPath(), roomSharedObject,this);
//			sharedObject.setAttribute(pagesSOName, pages);
			pages.setL(1);
			pages.setC(0);
			pages.setLS("u");
			pages.setPtrX(-1000);
			pages.setPtrY(-1000);
			createPage(room, isPersistent, roomSharedObject, 0, 0, 0);
		}
		else
		{
			System.out.println("Reading old whiteboard ---------------");
			pages = new WhiteboardPages(prefix, pagesSOName, isPersistent,room.getPath(), roomSharedObject,this);				
//			sharedObject.setAttribute(pagesSOName, pages);
			this.currentPageIndex = pages.getC();
			this.changePage(room, isPersistent, roomSharedObject,this.currentPageIndex);
//			pages.setL(pages.getL());
//			pages.setC(pages.getC());
			pages.setLS("u");
			pages.setPtrX(-1000);
			pages.setPtrY(-1000);
		}
	}
	protected	void	createShape(Integer shapeType, Object shapeData)
	{
		try
		{
			LinkedHashMap map;
			int count = this.currentPage.getCount();
			int	len	= this.currentPage.getLen();
			System.out.println("Creating new shape, id:-----------------:"+count+", len:"+len);
			synchronized (this.currentPage)
			{
				this.currentPage.setProperty(count+"", shapeType);
				this.currentPage.setProperty(count+".2", new Integer(count));
				map = (LinkedHashMap) shapeData;
//				System.out.println("set values map");
				Set set = map.keySet();
				Iterator it = set.iterator();
				while(it.hasNext())
				{
					Object key = it.next();
					ObjectMap singleValueMap = (ObjectMap)map.get(key);
					Object iValue = singleValueMap.get("i");
					Object vValue = singleValueMap.get("v");
//					System.out.println("iValue is : " + iValue.toString() + " vValue is : " + vValue.toString());
					this.currentPage.setProperty(count+"."+iValue, vValue);
				}
				this.currentPage.setCount(++count);
				this.currentPage.setLen(++len);
		
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	

	protected void changePage(IScope appScope, boolean isPersistent,ISharedObject sharedObject, int pageIndex) {
		
			synchronized (this.pages)
			{
				System.out.println("page index is : " + pageIndex + " and pages.getL is : " + this.pages.getL());
				if (pageIndex >= this.pages.getL())
				{
					pageIndex = this.pages.getL();
					currentPageIndex = pageIndex;
					this.createPage(appScope, isPersistent, sharedObject,pageIndex, 0, 0);
				}
				else
				{
					currentPageIndex = pageIndex;
					String pageName = prefix + ".p" + pageIndex;
					currentPage = new WhiteboardPage(pageName,isPersistent,sharedObject.getPath(),this);
				}
//				System.out.println("Current Page: " + currentPageIndex + ":" + currentPage);
				pages.setC(currentPageIndex);
			}
		}
	protected void changePage(int pageIndex)
	{
//		sharedObject = getSharedObject(this.appScope, this.appScope.getName(), isPersistent);
		this.changePage(this.roomScope,this.isPersistent,roomSharedObject,pageIndex);
		/*
		synchronized (this.pages)
		{
			if (pageIndex >= this.pages.getL())
			{
				pageIndex = this.pages.getL();					
				this.createPage(this.appScope, isPersistent, sharedObject, pageIndex, 0, 0);
			}
			else
			{
				currentPageIndex = pageIndex;
				String pageName = prefix+".p"+pageIndex;					
				currentPage = new WhiteboardPage(pageName, isPersistent, sharedObject.getPath(), this);
			}
			pages.setC(pageIndex);
		}
		*/
	}
		
		
		protected void createPage(IScope roomScope, boolean isPersistent,
				ISharedObject sharedObject, int pageIndex, int count, int len) {

			currentPage = pages.createPage(prefix, roomScope, isPersistent,sharedObject, pageIndex);
//			System.out.println("this.prefix+.p+pageIndex is : " + this.prefix+".p"+pageIndex);			
//			sharedObject.setAttribute(this.prefix+".p"+pageIndex,currentPage);
			currentPage.setCount(count);
			currentPage.setLen(len);
			currentPageIndex = pageIndex;
//			cache.setAttribute(pageIndex + "", new Integer(0));
		}

			
		protected void clearCurrentPage()
		{
			System.out.println("### clear current page of main whiteboard called");
				synchronized (this.currentPage)
				{
//					this.cache.setAttribute(this.currentPageIndex+"", new Integer(0));
					
					this.currentPage.setCount(0);
					this.currentPage.setLen(0);
					this.currentPage.getSO().removeAttributes();
				}
		}
		
//		public	void	clearAllPages(IScope scope,
//				boolean	isPersistent, ISharedObject sharedObject)
//		{
//			synchronized (this)
//			{				
//				int numberOfPages = this.pages.getL();
//				for (int i=0; i<numberOfPages; i++)
//				{
//					String pageName = prefix+".p"+i;
////					System.out.println("pageName is : " + pageName);
//					sharedObject = this.getSharedObject(appScope, pageName, isPersistent);
//					this.createPage(scope, isPersistent, sharedObject, 0, 0, 0);
//					this.cache.setAttribute(i+"i", new Integer(0));
//					this.currentPage.setCount(0);
//					this.currentPage.setLen(0);
//					this.currentPage.getSO().removeAttributes();
//					this.currentPage.getSO().release();
//				}
//			}
//		}

		protected	void	redepthShape(int shapeId)
		{
//			System.out.println("Whiteboard redeptShape called");
			synchronized (this.currentPage)
			{
				int count = this.currentPage.getCount();
				this.currentPage.setProperty(shapeId+".2", new Integer(count));
				this.currentPage.setCount(++count);
			}
		}

		protected	void	updateShape(String shapeId, Object shapeData)
		{
//			System.out.println("update shape of wb called...");
			LinkedHashMap map = new LinkedHashMap();
			synchronized (this.currentPage)
			{
				map = (LinkedHashMap) shapeData;
				Set set = map.keySet();
				Iterator it = set.iterator();
				while(it.hasNext())
				{
					Object key = it.next();			
					ObjectMap singleValueMap = (ObjectMap)map.get(key);
					Object iValue = singleValueMap.get("i");
					Object vValue = singleValueMap.get("v");
//					System.out.println("iValue is : " + iValue.toString() + " vValue is : " + vValue.toString());
					this.currentPage.setProperty(shapeId+"."+iValue, vValue);
				}
			}		
		}
		
		protected	void	deleteShape(String shapeId)
		{
//			System.out.println("delete shape of wb called");
			synchronized (this.currentPage)
			{
//				System.out.println("Whiteboard::deleteShape:deleting shape:"+shapeId);
				this.currentPage.deleteShape(shapeId);
			}
		}
		
		protected	void	updateShapes(Object shapeData, Double xdiff, Double ydiff)
		{
//			System.out.println("updateshapes of wb called");
			LinkedHashMap map = new LinkedHashMap();
			synchronized (this.currentPage)
			{
						map = (LinkedHashMap) shapeData;						
						int size = map.size();
//						System.out.println("size of the linked hash map is : " + size);
						for(int i=0; i < size; i++)
						{
							String shapeId = map.get(i+"").toString();
							String xPropId = shapeId+".0";
							String yPropId = shapeId+".1";
							
							Object currentX = this.currentPage.getProperty(xPropId);
							Object currentY = this.currentPage.getProperty(yPropId);
							
							Double xTemp = (Double)currentX;
							Double yTemp = (Double)currentY;
							Double newX = new Double(xTemp.doubleValue() + xdiff.doubleValue());
							Double newY = new Double(yTemp.doubleValue() + ydiff.doubleValue());
							
							this.currentPage.setProperty(xPropId, newX);
							this.currentPage.setProperty(yPropId, newY);
						}
			}
		}

		public	void	deleteShapes(Object shapeIds)
		{
			synchronized (this.currentPage)
			{
				String[] shapeArray = shapeIds.toString().split(",");
				
				int size = shapeArray.length;
				for(int i=0;i<size;i++){
					
					if(i == size-1){
						String[] shapeId = shapeArray[i].split("=");
						String[] shapeSubId = shapeId[1].split("}");
						this.currentPage.deleteShape(shapeSubId[0]);
					}
					else{
						String[] shapeId = shapeArray[i].split("=");
						this.currentPage.deleteShape(shapeId[1]);
					}
					
				}
			}

		}
		
		
//	public boolean roomConnect(IConnection conn, Object[] params)
//	{
//		System.out.println("RoomConnect called for : "
//				+ conn.getScope().getName());

//		if (!super.roomConnect(conn, params)) {
//			System.out.println("Unable to connect from "
//					+ conn.getScope().getName() + " for the application");
//			return false;
//		}
//		return true;
//	}

//	public boolean roomJoin(IClient client, IScope scope) {
//		System.out.println("RoomJoined Called for client: " + client.getId()
//				+ "of scope " + scope.getName());
//		return true;
//	}
	
	
//	public void appLeave(IClient client, IScope scope) {
//		System.out.println("AppLeave called for client : " + client.getId()
//				+ " of scope " + scope.getName());
//		closeWhiteboardSession(scope);
//	}

	public void roomLeave(IClient client, IScope room) {
//		System.out.println("Roomleave Called for : " + client.getId() + " , "
//				+ scope.getName());
	}

	public void roomDisconnect(IConnection conn) {
//		System.out.println("RoomDisconnect Called for : "
//				+ conn.getScope().getName());
	}

	public void appDisconnect(IConnection conn) {
//		System.out.println("appDisconnect Called for : "
//				+ conn.getScope().getName());
	}

	public void appStop(IScope app) {
		System.out.println("AppStop Called for : " + app.getName());
	}

	protected void createWhiteboardSession(IScope appScope) {
		System.out.println("### Creating an instance for the meeting whiteboard");
//		String wbName = appScope.getName();		
//		MeetingWhiteboard wb = new MeetingWhiteboard(this);
//		this.whiteboards.put(wbName, wb);		
	}

//	protected void closeWhiteboardSession(IScope scope) {
//		ISharedObject sharedObject = this.getSharedObject(scope, scope.getName(), isPersistent);
//		String wbName = scope.getName();
//		ISharedObject wb = (ISharedObject) sharedObject.getAttribute(wbName);
//		wb = null;
//	}


//	public Whiteboard getWhiteboardInst() {
//		return whiteboardInst;
//	}

//	public ISharedObject getSharedObject() {
//		return sharedObject;
//	}

//	public HashMap getWhiteboards() {
//		return whiteboards;
//	}
	
	public	void	lock()
	{
		pages.setLS("l");
	}
	public	void	unlock()
	{
		pages.setLS("u");
	}
	public	void	movePtrX(Object[] params)
	{
//		System.out.println("modePtrX function called:"+params.length);
//		for(int i=0;i<params.length;i++){
//			System.out.println("param"+i+" is : " + params[i] + " and params[i]'s class is : " + params[i].getClass());	
//		}
		if (params.length >0)
		{
			try
			{
				String s = params[0].toString();
				int	x = Integer.parseInt(s);
				pages.setPtrX(x);
			}
			catch(Exception e)
			{
				
			}
		}
	}
	public	void	movePtrY(Object[] params)
	{
//		System.out.println("modePtrY function called:"+(new Date()).toString()+":"+params.length);
//		for(int i=0;i<params.length;i++){
//			System.out.println("param "+i+" is : " + params[i] + " and params[i]'s class is : " + params[i].getClass());	
//		}
		if (params.length >0)
		{
			try
			{
				String s = params[0].toString();
				int	y = Integer.parseInt(s);
				pages.setPtrY(y);
			}
			catch(Exception e)
			{
				
			}
		}
	}
	public	synchronized	void	clientConnected()
	{
		this.lastConnectTime = System.currentTimeMillis();
	}
	public	boolean	isActive()
	{
		return	(System.currentTimeMillis() - this.lastConnectTime)<(5*60*60*1000);
	}
}
