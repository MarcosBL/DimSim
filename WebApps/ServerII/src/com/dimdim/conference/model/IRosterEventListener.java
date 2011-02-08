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
 *								                                          *
 * Copyright (c) 2006 Communiva Inc. All Rights Reserved.	              *
 *								                                          *
 *								                                          *
 * This code is licensed under the DimDim License                         *
 * For details please visit http://www.dimdim.com/license		          *
 *									                                      *
 **************************************************************************
 */
/*
 **************************************************************************
 *	File Name  : IRosterEventListener.java
 *  Created On : Apr 11, 2006
 *  Created By : Saurav Mohapatra
 **************************************************************************
*/
 
package com.dimdim.conference.model;

/**
 * @author Saurav Mohapatra
 * @email  Saurav.Mohapatra@communiva.com
 */
public interface IRosterEventListener
{
	public void onUserJoined(IRoster roster, IRosterEntry user);
	public void onUserLeft(IRoster roster,IRosterEntry user);
	public void onUserChanged(IRoster roster, IRosterEntry user);
}
