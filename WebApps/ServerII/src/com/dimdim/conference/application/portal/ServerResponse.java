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

package com.dimdim.conference.application.portal;

import java.util.Vector;



/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * This object is the server counterpart of the ui object. It is responsible
 * for formulating the json buffer as expected and required by ui.
 * 
 * This object is a copy of the server response object in the portal webapp.
 * The hardcoding is unfortunate neccesity right now to avoid dependancies.
 * If given sufficient time a common json and model package could be
 * developed that could serve both webapps. This is difficult because the
 * requirements of the two webapps are quite different. Portal for db
 * storage and authentication checks, whereas conference webapp for the
 * meeting runtime.
 */

public class ServerResponse
{
	protected	boolean		success;
	protected	int			code;
	protected	String		dataType;
	protected	String		objClass;
	protected	String		dataString;
	protected	Vector		array;
	
	public	ServerResponse(boolean success, int code, Message object)
	{
		this.success = success;
		this.code = code;
		this.dataType = "object";
		this.objClass = object.getObjectClass();
		this.dataString = object.toJson();
	}
	
	public	ServerResponse(boolean success, int code, JsonSerializable object)
	{
		this.success = success;
		this.code = code;
		this.dataType = "object";
		this.objClass = object.getObjectClass();
		this.dataString = object.toJson();
	}
	
	public	String	toJson()
	{
		StringBuffer buf = new StringBuffer();
		
		buf.append("{success:\"");
		buf.append(""+success);
		buf.append("\",code:\"");
		buf.append(""+code);
		buf.append("\",dataType:\"");
		buf.append(this.dataType);
		buf.append("\",objClass:\"");
		buf.append(this.objClass);
		buf.append("\",data:");
		if (this.dataType.equals("object"))
		{
			buf.append(this.dataString);
		}
		else
		{
			buf.append("\"");
			buf.append(this.dataString);
			buf.append("\"");
		}
		buf.append("}");
		
		return	buf.toString();
	}
	public int getCode()
	{
		return code;
	}
	public void setCode(int code)
	{
		this.code = code;
	}
	public String getDataString()
	{
		return dataString;
	}
	public void setDataString(String dataString)
	{
		this.dataString = dataString;
	}
	public String getDataType()
	{
		return dataType;
	}
	public void setDataType(String dataType)
	{
		this.dataType = dataType;
	}
	public boolean isSuccess()
	{
		return success;
	}
	public void setSuccess(boolean success)
	{
		this.success = success;
	}
}
