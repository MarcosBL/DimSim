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

package com.dimdim.conference.action.common;

import java.util.UUID;
import com.dimdim.conference.action.CommonDimDimAction;

public class GetNewUUID extends CommonDimDimAction
{
	
	protected	String		jsonBuffer = "{a:\"b\"}";
	
	public	String	execute()	throws	Exception
	{
		String	uuid = UUID.randomUUID().toString();
//		jsonBuffer = "{uuid:\""+uuid+"\" }";
		jsonBuffer = uuid;
		System.out.println(" New UUID:"+jsonBuffer);
		return SUCCESS;
	}
	public String getJsonBuffer()
	{
		return jsonBuffer;
	}
	public void setJsonBuffer(String jsonBuffer)
	{
		this.jsonBuffer = jsonBuffer;
	}
}
