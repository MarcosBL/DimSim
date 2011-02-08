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

package com.dimdim.conference.ui.json.client;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.Window;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */
public class UIResourceObject	extends	UIObject
{
	
	public	static	final	String	ANNOTATION_ON	=	"ann_on";
	public	static	final	String	ANNOTATION_OFF	=	"ann_off";
	
	public static final String RESOURCE_TYPE_DESKTOP = "resource.screen";
	public static final String RESOURCE_TYPE_WHITEBOARD = "resource.whiteboard";
	public static final String RESOURCE_TYPE_COBROWSE = "resource.cobrowse";
	public static final String RESOURCE_TYPE_PRESENTATION = "resource.ppt";
	
	/**
	 * Constant flags for supported resource types.
	 */
	
//	public static final String RESOURCE_TYPE_PRESENTATION = "resource.ppt";
//	public static final String RESOURCE_TYPE_POLL = "resource.poll";
//	public static final String RESOURCE_TYPE_WHITEBOARD = "resource.whiteboard";
//	public static final String RESOURCE_TYPE_SCREEN_SHARE = "resource.screen";
//	public static final String RESOURCE_TYPE_VIDEO = "resource.video";
//	public static final String RESOURCE_TYPE_AUDIO = "resource.audio";
	
	protected	static	String	KEY_URL = "url";
	protected	static	String	KEY_RESOURCE_ID = "resourceId";
	protected	static	String	KEY_RESOURCE_NAME = "resourceName";
	protected	static	String	KEY_RESOURCE_TYPE = "resourceType";
	protected	static	String	KEY_MEDIA_ID = "mediaId";
	protected	static	String	KEY_SLIDE = "slide";
	protected	static	String	KEY_WIDTH = "width";
	protected	static	String	KEY_HEIGHT = "height";
	protected	static	String	KEY_LAST_SLIDE_INDEX = "lastSlideIndex";
	protected	static	String	KEY_OWNER_ID = "ownerId";
	protected	static	String	KEY_APP_HANDLE = "appHandle";
	protected	static	String	KEY_ANNOTATION_HANDLE = "annotation";
	
	protected	final	String	resourceId;
	protected	String	resourceName = "";
	protected	String	resourceType = "";
	protected	String	mediaId = "";
	protected	Integer	slide;
	protected	Integer	width;
	protected	Integer	height;
	protected	final	String	ownerId;
	protected	String	appHandle = "";
	protected	String	annotation = ANNOTATION_OFF;
	protected	int		lastSlideIndex = 0;
	
	String url = null;
	
	public UIResourceObject()
	{
		this.resourceId = "x";
		this.mediaId = "x";
		this.slide = new Integer(0);
		this.ownerId = "x";
	}
	public UIResourceObject(String resourceIdx, String resourceNamex,
			String resourceTypex, String mediaIdx, Integer slidex,
			Integer widthx, Integer heightx,
			String ownerIdx, String appHandlex, String annotation, Integer lastSlideIndexT)
	{
		this.resourceId = resourceIdx;
		//this.resourceName = resourceNamex;
		this.resourceType = resourceTypex;
		//Window.alert("the resourceTypex  = "+resourceTypex);
		//Window.alert("the resource name = "+resourceNamex);
		//Window.alert("the resource name after decoding = "+decodeBase64(resourceNamex));
		if("resource.ppt".equals(resourceTypex))
		{
		    this.resourceName = decodeBase64(resourceNamex);
		}else{
		    this.resourceName = resourceNamex;
		}
		this.mediaId = mediaIdx;
		this.slide = slidex;
		this.width = widthx;
		this.height = heightx;
		this.ownerId = ownerIdx;
		this.appHandle = appHandlex;
		this.annotation = annotation;
		this.lastSlideIndex = lastSlideIndexT.intValue();
	}
	public	static	UIResourceObject	parseJsonObject(JSONObject pceJson)
	{
		//Window.alert("inside to parseJsonObject JSONObject = "+pceJson);
		String resourceIdT = pceJson.get(KEY_RESOURCE_ID).isString().stringValue();
		String resourceNameT = pceJson.get(KEY_RESOURCE_NAME).isString().stringValue();
		String resourceTypeT = pceJson.get(KEY_RESOURCE_TYPE).isString().stringValue();
		String mediaIdT = pceJson.get(KEY_MEDIA_ID).isString().stringValue();
		Integer slideT = new Integer(pceJson.get(KEY_SLIDE).isString().stringValue());
		Integer widthT = new Integer(pceJson.get(KEY_WIDTH).isString().stringValue());
		Integer heightT = new Integer(pceJson.get(KEY_HEIGHT).isString().stringValue());
		Integer lastSlideIndexT = new Integer(pceJson.get(KEY_LAST_SLIDE_INDEX).isString().stringValue());
		String ownerIdT = pceJson.get(KEY_OWNER_ID).isString().stringValue();
		String appHandleT = pceJson.get(KEY_APP_HANDLE).isString().stringValue();
		String annotation = pceJson.get(KEY_ANNOTATION_HANDLE).isString().stringValue();
		
		String url ="";
		if(null != pceJson.get(KEY_URL))
		{
			url = pceJson.get(KEY_URL).isString().stringValue();
		}
		
		UIResourceObject ro = new UIResourceObject(resourceIdT, resourceNameT,
				resourceTypeT, mediaIdT, slideT, widthT, heightT,
				ownerIdT, appHandleT, annotation, lastSlideIndexT);
		ro.setUrl(url);
		
		//Window.alert("inside to parseJsonObject "+ro);
		
		return	ro;
	}
	public	void	refreshWithChanges(UIResourceObject changes)
	{
		this.setResourceName(changes.getResourceName());
		this.setResourceType(changes.getResourceType());
		this.setMediaId(changes.getMediaId());
		this.setSlide(changes.getSlide());
		this.setWidth(changes.getWidth());
		this.setHeight(changes.getHeight());
//		this.setOwnerId(changes.getOwnerId());
		this.setAppHandle(changes.getAppHandle());
	}
	
	public String getResourceId()
	{
		return this.resourceId;
	}
//	public void setResourceId(String resourceId)
//	{
//		this.resourceId = resourceId;
//	}
	public String getResourceName()
	{
		return this.resourceName;
	}
	public void setResourceName(String resourceName)
	{
		this.resourceName = resourceName;
	}
	public String getResourceType()
	{
		return this.resourceType;
	}
	public void setResourceType(String resourceType)
	{
		this.resourceType = resourceType;
	}	
	public String getMediaId()
	{
		return this.mediaId;
	}
	public void setMediaId(String mediaId)
	{
		this.mediaId = mediaId;
	}
	
	//	Following methods allow various specific resource viewer to
	//	access the parameters directly.
	
	public Integer getSlide()
	{
		return this.slide;
	}
	public void setSlide(Integer slide)
	{
		this.slide = slide;
	}
	public Integer getHeight()
	{
		return height;
	}
	public void setHeight(Integer height)
	{
		this.height = height;
	}
	public Integer getWidth()
	{
		return width;
	}
	public void setWidth(Integer width)
	{
		this.width = width;
	}
	/*
	public void setRtmpURL(String rtmpURL)
	{
		this.rtmpURL = rtmpURL;
	}
	//	Desktop Streaming Parameters.
	public	String	getRtmpURL()
	{
		return	this.rtmpURL;
	}
	*/
	public	String	getStreamName()
	{
		return	this.resourceId;
	}
	public	String	getStreamTitle()
	{
		return	this.resourceName;
	}
	
	//	Powerpoint Presentation Parameters
	public	String	getPresentationId()
	{
		return	this.mediaId;
	}
	public	String	getPresentationTitle()
	{
		return	this.resourceName;
	}
	public	Integer	getSlideCount()
	{
		return	this.slide;
	}
	public String getAppHandle()
	{
		return appHandle;
	}
	public void setAppHandle(String appHandle)
	{
		this.appHandle = appHandle;
	}
	public String getOwnerId()
	{
		return ownerId;
	}
	public String getAnnotation()
	{
		return annotation;
	}
	public void setAnnotation(String annotation)
	{
		this.annotation = annotation;
	}
	public int getLastSlideIndex()
	{
		return lastSlideIndex;
	}
	public void setLastSlideIndex(int lastSlideIndex)
	{
		this.lastSlideIndex = lastSlideIndex;
	}
	public	String	toJson()
	{
		StringBuffer buf = new StringBuffer("");
		String resName = resourceName;
		if("resource.ppt".equals(resourceType))
		{
		    resName = encodeBase64(resourceName);
		}
		buf.append("{");
		buf.append("objClass:\""); buf.append("ResourceObject"); buf.append("\",");
		buf.append("resourceId:\""); buf.append(resourceId); buf.append("\",");
		buf.append("url:\""); buf.append(url); buf.append("\",");
		buf.append("resourceName:\""); buf.append(resName); buf.append("\",");
		buf.append("resourceType:\""); buf.append(resourceType); buf.append("\",");
		buf.append("mediaId:\""); buf.append(mediaId); buf.append("\",");
		buf.append("slide:\""); buf.append(slide); buf.append("\",");
		buf.append("width:\""); buf.append(width); buf.append("\",");
		buf.append("height:\""); buf.append(height); buf.append("\",");
		buf.append("lastSlideIndex:\""); buf.append(lastSlideIndex+""); buf.append("\",");
		buf.append("ownerId:\""); buf.append(ownerId); buf.append("\",");
		buf.append("appHandle:\""); buf.append(appHandle); buf.append("\",");
		buf.append("annotation:\""); buf.append(annotation); buf.append("\",");
		buf.append("data:\""); buf.append("dummy"); buf.append("\"");
		buf.append("}");
		
		//Window.alert("inside to json "+buf.toString());
		return	buf.toString();
	}
	public String toString()
	{
		return	toJson();
	}
	protected	native	String	encodeBase64(String s) /*-{
		return $wnd.Base64.encode(s);
	}-*/;
	private	native	String	decodeBase64(String s) /*-{
		return $wnd.Base64.decode(s);
	}-*/;
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
}
