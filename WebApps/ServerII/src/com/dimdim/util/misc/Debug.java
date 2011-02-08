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
 * Copyright (c) 2008 Dimdim Inc. All Rights Reserved.              	  *
 *                                                                        *
 *                                                                        *
 * This code is licensed under the DimDim License                         *
 * For details please visit http://www.dimdim.com/license                 *
 *                                                                        *
 **************************************************************************
 */

package com.dimdim.util.misc;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 */

public	class	Debug
{
	private	static	boolean	debugOn	=	false;
	
	public	static	boolean	debugEnabled()
	{
		return debugOn;
	}
	
	public	static	synchronized	void	printDebug(Object o)
	{
		if(debugOn)
		{
			System.err.println(o);
		}
	}
	//usage: printDebug(">> IN (0)","someFunc");
	//usage: printDebug(">> IN (1)","someFunc");
	//usage: printDebug("<< OUT (0)","someFunc");
	public	static	synchronized	void	printDebug(String inOut, String methodName)
	{
		if(debugOn)
		{
			System.err.println(inOut + " - "+System.currentTimeMillis()+" - "+Thread.currentThread().getId()+" - "+methodName);
		}
	}
}
