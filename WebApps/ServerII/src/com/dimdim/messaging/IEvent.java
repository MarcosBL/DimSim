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

package com.dimdim.messaging;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * The event interface provides type, source and originator for the use by
 * message engine. These parameters are arbitrary and may be interpreted
 * differently by each user of the message engine.
 */
public interface IEvent	//	extends	Comparable
{
	public	static	final	int		PRIORITY_0 = 0;
	public	static	final	int		PRIORITY_1 = 1;
	public	static	final	int		PRIORITY_2 = 2;
	
	public	String		getType();
	
	public	String		getSource();
	
//	public	String		getTarget();
	
//	public	String		getOriginator();
	
	public	int		getIndex();
	
	public	void	setIndex(int i);
	
	//	Number between 1 - 100. The available events will be sequenced in
	//	the priority order for sending to the final recipient.
	
	public	int		getPriority();
	
	public	String	toJson();
	
	public	long	getCreationTime();
	
	public	IEventData	getEventData();
	
	public	String		getInitiatorId();
	
}
