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
 * This test simply creates 100 meetings with 1000 participants each. It then
 * broadcasts 10000 messages to each meeting at 20 ms interval. Measuring the
 * time taken for the broadcast. It should not be significantly higher than the
 * wait time.
 */

public class MessageEngineBroadcastTimeTest
{
	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		Vector mv = new Vector();
		
		long t1 = System.currentTimeMillis();
		for (int i=0; i<100; i++)
		{
			TestMeeting meeting1 = new TestMeeting(i);
			mv.addElement(meeting1);
			for (int j =0; j<1000; j++)
			{
				TestParticipant participant = new TestParticipant(j);
				meeting1.addParticipant(participant);
			}
		}
		long t2 = System.currentTimeMillis();
		int	size = mv.size();
		for (int k=0; k<10; k++)
		{
			System.out.println("Event publication round: "+k);
			for (int l=0; l<size; l++)
			{
				TestMeeting meeting1 = (TestMeeting)mv.elementAt(l);
				TestEvent event = new TestEvent(k);
//				meeting1.messageEngine.dispatchEvent(event);
				for (int m=0; m<1000; m++)
				{
					meeting1.messageEngine.dispatchEventToReceiver(event, m+"");
				}
			}
			try
			{
				Thread.sleep(20);
			}
			catch(Throwable t)
			{
				
			}
		}
		long waitTime = 10*20;
		long t3 = System.currentTimeMillis();
		System.out.println("Time for creating the objects:"+(t2-t1));
		System.out.println("Time for broadcasting events:"+((t3-t2)-waitTime));
		try
		{
			Thread.sleep(60000);
		}
		catch(Exception e)
		{
			
		}
	}
}
