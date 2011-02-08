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

package com.dimdim.conference.install;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * This utility is for use by the installer during an upgrade. It is expected
 * to take care of any and all actions required at the end of an upgrade.
 * 
 * 100_alpha_091006 to 100_alpha_091906 -
 * 
 * The short cuts html files and welcome page html were added to the product.
 * Server name and port number in these file need to be replaced by the
 * real values from the DimDimConference.properties file on the user's
 * system from the previous installation.
 */

public class PostUpgradeActions
{
	
	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		try
		{
//			ServerNamePortNumberSetup snpns = new ServerNamePortNumberSetup();
//			snpns.doWork();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
}
