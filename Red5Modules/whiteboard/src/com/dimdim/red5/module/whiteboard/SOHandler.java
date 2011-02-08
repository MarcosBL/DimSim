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

//import org.red5.server.adapter.ApplicationAdapter;

public class SOHandler //extends ApplicationAdapter 
{
	
	protected MeetingWhiteboard meetingWhiteboard;
	
	public SOHandler(MeetingWhiteboard meetingWhiteboard)
	{
		this.meetingWhiteboard = meetingWhiteboard;
	}
	
	public void s(Object[] method)
	{
//		System.out.println("SOHandler s - change page method called 1");	
		changePage(meetingWhiteboard,method);
	}
	
	public void changePage(MeetingWhiteboard wb, Object[] method)
	{
//		System.out.println("### changePage called ####");		
			Integer pageIndex = (Integer) method[0];
			wb.changePage(pageIndex.intValue());
	}
	
	public void c(String method)
	{
//		System.out.println("SOHandler c - clear current page method called 1");
//		Whiteboard wb = this.meetingWhiteboard.getWhiteboardInst();
		meetingWhiteboard.clearCurrentPage();
	}

	public void r(Object[] method)
	{
//		System.out.println("SOHandler r - redepth method called 1");
//		Whiteboard wb = this.meetingWhiteboard.getWhiteboardInst();
		redepthShape(meetingWhiteboard, method);
	}
	
	public void redepthShape(MeetingWhiteboard wb, Object[] params)
	{
//		System.out.println("redepth fun called");
		int shapeId = ((Integer)params[0]).intValue();
		wb.redepthShape(shapeId);

	}

	public void u(Object[] method)
	{
//		System.out.println("SOHandler u - update method called 1");
//		Whiteboard wb = this.meetingWhiteboard.getWhiteboardInst();
		updateShape(meetingWhiteboard,method);
	}
	
	public void updateShape(MeetingWhiteboard wb, Object[] params)
	{
//		System.out.println("updateShapes function called");
//		for(int i=0;i<params.length;i++){
//			System.out.println("param's i is : " + params[i] + " and params[i]'s class is : " + params[i].getClass());	
//		}
		String shapeId = (String)params[0];			
		Object shapeData = params[1];
		wb.updateShape(shapeId, shapeData);			
	}

	public void p(Object[] method)
	{
//		System.out.println("SOHandler p method called 1");
/*		for ( int i=0; i<method.length; i++){
			System.out.println("param's i is : " + method[i] + " params[i].getClass() is : " + method[i].getClass());
		}
*/
//		Whiteboard wb = meetingWhiteboard.getWhiteboardInst();
		createShape(meetingWhiteboard,method);
	}
	
	protected	void	createShape(MeetingWhiteboard meetingWhiteboard, Object[] params)
	{
//		System.out.println("#### Create shape called ####");
		Integer shapeType = (Integer) params[0];
		Object shapeData = params[1];
		meetingWhiteboard.createShape(shapeType,shapeData);
	}
	
	public void d(Object[] method)
	{
//		System.out.println("SOHandler d - delete method called 1");
//		Whiteboard wb = this.meetingWhiteboard.getWhiteboardInst();
		deleteShape(meetingWhiteboard,method[0]);
	}
	
	public void deleteShape(MeetingWhiteboard wb, Object params)
	{
//		System.out.println("delete shape called");
		String shapeId = (String)params;
//		System.out.println("shapeId is : " + shapeId);
		wb.deleteShape(shapeId);
	}
	
	public void bd(Object[] method)
	{
//		System.out.println("SOHandler bd - batch delete method called 1");
//		Whiteboard wb = this.meetingWhiteboard.getWhiteboardInst();
		batchDelete(meetingWhiteboard,method[0]);	
	}
	
	public void batchDelete(MeetingWhiteboard wb, Object params)
	{
//		System.out.println("batchDelete fun called");
//		System.out.println("shape ids of batch delete is : " + params.toString());
		wb.deleteShapes(params);
	}
	
	public void bu(Object[] method)
	{
//		System.out.println("SOHandler bu - batch update method called 1");
//		Whiteboard wb = this.meetingWhiteboard.getWhiteboardInst();
		batchUpdate(meetingWhiteboard,method);
	}
	
	public void batchUpdate(MeetingWhiteboard wb, Object[] params)
	{
//		System.out.println("batch update function called");
		Double shapeXdiff = (Double)params[1];
		Double shapeYdiff = (Double)params[2];
		wb.updateShapes(params[0], shapeXdiff, shapeYdiff);
	}
	
//	public void cp(Object[] method)
//	{
////		System.out.println("SOHandler cp - clear pages method called 1");
////		Whiteboard wb = this.meetingWhiteboard.getWhiteboardInst();
//		clearWhiteboard(meetingWhiteboard,method);
//	}
	
//	public void clearWhiteboard(MeetingWhiteboard wb, Object[] params)
//	{
////		System.out.println("clearWhiteboard called ");		
//		wb.clearAllPages(wb.getScope(), false, wb.getSharedObject());
//		wb.changePage(wb.appScope, wb.isPersistent, wb.getSharedObject(), 0);
//	}
	
	public void connect(Object[] method)
	{
		System.out.println("SOHandler connect method called "+method);
//		System.out.println("method length is : " + method.length);
//		System.out.println("mehod name is : " + method.toString());
	}
	public void ll()
	{
		this.meetingWhiteboard.lock();
	}
	public void lu()
	{
		this.meetingWhiteboard.unlock();
	}
	public void mx(Object[] method)
	{
		this.meetingWhiteboard.movePtrX(method);
	}
	public void my(Object[] method)
	{
		this.meetingWhiteboard.movePtrY(method);
	}
}
