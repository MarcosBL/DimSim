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

package com.dimdim.data.application;

import	java.util.HashMap;
import	java.util.Locale;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * This dictionary provider must be implemented by the components which use
 * the data manager to transfer dictionaries which are not under the locale
 * manager to UI.
 * 
 * Essentially any table of values is a data dictionary as long as the ui is
 * concerned. The table returned by the provider is provided to the ui as a
 * json buffer in same format as the one returned by the locale manager.
 * 
 * The implementer of the provider is responsible for accounting for the
 * current locale if required by the dictionary under consideration.
 */

public interface UIDataDictionaryProvider
{
	
	public	HashMap	getUIDataDictionary(String component, String name, Locale locale);
	
}
