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

package com.dimdim.data.model;

import java.util.Vector;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * This object is the server counterpart of the ui object. It is responsible
 * for formulating the json buffer as expected and required by ui.
 */

public class ServerResponse
{
	public	static	final	String	ServerResponseDataType_Object	=	"object";
	public	static	final	String	ServerResponseDataType_Array	=	"array";
	
	protected	boolean		success;
	protected	int			code;
	protected	String		dataType;
	protected	String		objClass;
	protected	String		dataString;
	protected	Vector		array;
	
	public ServerResponse(boolean success, int code, String objClass, Vector array)
	{
		this.success = success;
		this.code = code;
		this.dataType = ServerResponseDataType_Array;
		this.objClass = objClass;//((JsonSerializable)array.elementAt(0)).getObjectClass();
		this.array = array;
	}
	public	ServerResponse(boolean success, int code, JsonSerializable object)
	{
		this.success = success;
		this.code = code;
		this.dataType = ServerResponseDataType_Object;
		this.objClass = object.getObjectClass();
		this.dataString = object.toJson();
	}
	/**
	 * This constructor accepts string for data which must be a json string
	 * that represents a single object.
	 * 
	 * e.g. dataString = {a:"b",c:"d"}
	 * 
	 * The datatype can be 'object' or 'array'. The objClass parameter must
	 * provide the true type of the object serialized as the dataString.
	 */
	public	ServerResponse(boolean success, int code, String objClass, String dataString)
	{
		this.success = success;
		this.code = code;
		this.dataType = ServerResponseDataType_Object;
		this.objClass = objClass;
		this.dataString = dataString;
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
		if (this.dataType.equals(ServerResponseDataType_Object))
		{
			buf.append(this.dataString);
		}
		else if (this.dataType.equals(ServerResponseDataType_Array))
		{
			int size = this.array.size();
			buf.append("[");
			for (int i=0;i<size; i++)
			{
				if (i>0)
				{
					buf.append(",");
				}
				buf.append((String)this.array.elementAt(i));
			}
			buf.append("]");
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
