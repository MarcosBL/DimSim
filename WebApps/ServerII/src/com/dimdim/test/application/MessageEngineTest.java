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
 * Copyright (c) 2007 Dimdim Inc. All Rights Reserved.              	  *
 *                                                                        *
 *                                                                        *
 * This code is licensed under the DimDim License                         *
 * For details please visit http://www.dimdim.com/license                 *
 *                                                                        *
 **************************************************************************
 */

package com.dimdim.test.application;

import	java.util.Vector;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */

public class MessageEngineTest
{
	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		TestMeeting meeting1 = new TestMeeting(0);
		Vector v = new Vector();
		
		for (int i=0; i<10; i++)
		{
			TestParticipant participant = new TestParticipant(i);
			meeting1.addParticipant(participant);
			v.addElement(participant);
		}
		int	size = v.size();
		for (int j=0; j<100; j++)
		{
			for (int k=0; k<size; k++)
			{
				TestParticipant participant = (TestParticipant)v.elementAt(k);
				participant.run();
			}
		}
	}
}
