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

package com.dimdim.ui.common.client.json;

import java.util.ArrayList;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */

public class ServerResponse
{
	public	static	int		DATA_TYPE_STRING	=	1;
	public	static	int		DATA_TYPE_OBJECT	=	2;
	public	static	int		DATA_TYPE_ARRAY	=	3;
	
	//	A simple flag to indicate if the command sent was a success or failure.
	//	The code in intended for more detailed information regarding the status
	//	of the server request.
	
	private	boolean		success;
	private	int			code;
	
	//	Data type
	
	private	int			dataType;
	
	//	Data returned by the server action. This may be a simple message or
	//	a data object that is used by other objects such as forms. It is upto
	//	individual command dispatcher to receive and interpret the data.
	
	private	UIObject	dataObject;
	
	private	String		dataString;
	
	private	ArrayList	dataArray;
	
	public	ServerResponse(boolean success, int code, String str)
	{
		this.success = success;
		this.code = code;
		this.dataType = ServerResponse.DATA_TYPE_STRING;
		this.dataString = str;
	}
	public	ServerResponse(boolean success, int code, UIObject obj)
	{
		this.success = success;
		this.code = code;
		this.dataType = ServerResponse.DATA_TYPE_OBJECT;
		this.dataObject = obj;
	}
	public	ServerResponse(boolean success, int code, ArrayList array)
	{
		this.success = success;
		this.code = code;
		this.dataType = ServerResponse.DATA_TYPE_ARRAY;
		this.dataArray = array;
	}
	public ArrayList getDataArray()
	{
		return dataArray;
	}
	public void setDataArray(ArrayList dataArray)
	{
		this.dataArray = dataArray;
	}
	public UIObject getDataObject()
	{
		return dataObject;
	}
	public void setDataObject(UIObject dataObject)
	{
		this.dataObject = dataObject;
	}
	public String getDataString()
	{
		return dataString;
	}
	public void setDataString(String dataString)
	{
		this.dataString = dataString;
	}
	public int getDataType()
	{
		return dataType;
	}
	public void setDataType(int dataType)
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
	public int getCode()
	{
		return code;
	}
	public void setCode(int code)
	{
		this.code = code;
	}
}

