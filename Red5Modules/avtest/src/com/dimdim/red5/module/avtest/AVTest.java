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
 * Copyright (c) 2008 Dimdim Inc. All Rights Reserved.                 	  *
 *                                                                        *
 *                                                                        *
 * This code is licensed under the Dimdim License                         *
 * For details please 									                  *
 * 	visit http://www.dimdim.com/opensource/dimdim_license.html			  *
 *                                                                        *
 **************************************************************************
 */

package com.dimdim.red5.module.avtest;

/**
 * @author Rajesh Dharmalingam
 * @email rajesh@dimdim.com
 *
 */

import java.io.IOException;
import java.util.HashMap;

import org.red5.server.adapter.ApplicationAdapter;
import org.red5.server.api.IClient;
import org.red5.server.api.IConnection;
import org.red5.server.api.IScope;
import org.red5.server.api.stream.IBroadcastStream;
import org.red5.server.api.stream.IPlayItem;
import org.red5.server.api.stream.IPlaylistSubscriberStream;
import org.red5.server.api.stream.IStreamAwareScopeHandler;
import org.red5.server.api.stream.IStreamListener;
import org.red5.server.api.stream.ISubscriberStream;

public class AVTest extends ApplicationAdapter implements IStreamAwareScopeHandler
{

	protected IScope appScope = null;
	protected	HashMap		streamListeners = new HashMap();
	protected String streamNameRecorded;
	
	public boolean appStart(IScope app) {
		System.out.println("######## AVTest App Start ########");
		if (!super.appStart(app)) {
			System.out.println("Unable to start the application");
			return false;
		}
		this.appScope = app;
		String appName = app.getName();
		String streamStoragePath = app.getContextPath();
		System.out.println("get context path is : " + streamStoragePath);
		
		AVTestStreamListener avtestStreamListener = new AVTestStreamListener(appName,streamStoragePath);		
		System.out.println("AVTest Stream Listener created in AppStart : " + avtestStreamListener.toString());
		
		this.streamListeners.put(appName, avtestStreamListener);
		
		return true;
	}

	public void appLeave(IClient client, IScope scope) {
		System.out.println("########### AVTest appLeave : " + client.getId()
				+ " of scope " + scope.getName());
		String appName = scope.getName();
		AVTestStreamListener streamListener = (AVTestStreamListener)this.streamListeners.remove(appName);
		streamListener.cleanupOldRecording();
	}

	public void appDisconnect(IConnection conn) {
		System.out.println("########### AVTest appDisconnect : "
				+ conn.getScope().getName());
		String appName = conn.getScope().getName();
		AVTestStreamListener avTestListener = (AVTestStreamListener) this.streamListeners.remove(appName);
		//stopRecordingShow(conn);
	}

	public boolean roomStart(IScope room) {
		System.out.println("######## AVTest Room start object called ########");		
		if (!super.roomStart(room)) {
			System.out.println("Unable to start Room.");
			return false;
		}
		this.appScope = room;		
		return true;
	}

	public void streamPublishStart(IBroadcastStream stream) {
		System.out.println("########### AVTest streamPublishStart ############");
		System.out.println("stream publish start: " + stream.getPublishedName());
	}

	public void streamBroadcastStart(IBroadcastStream stream) {
		System.out.println("########### AVTest streamBroadcastStart ############");
		String appName = stream.getScope().getName();		
		System.out.println("appName is : " + appName);
		streamNameRecorded = stream.getPublishedName();
		System.out.println("stream publish name is : " + stream.getPublishedName());
		System.out.println("stream name is : " + stream.getName());
		System.out.println("stream save name is : " + stream.getSaveFilename());
		AVTestStreamListener streamListener = (AVTestStreamListener)this.streamListeners.get(appName);
		stream.addStreamListener((IStreamListener) streamListener);
	}

	public void streamSubscriberStart(ISubscriberStream stream){
		System.out.println("########### AVTest streamSubscriberStart ############");
		System.out.println("Stream name : " + stream.getName());
		System.out.println("Stream id : " + stream.getStreamId());
		System.out.println("Stream class : " + stream.getClass());
		System.out.println("Stream scope : " + stream.getScope());
		System.out.println("Stream connection path : " + stream.getConnection().getPath());
		System.out.println("Stream scope's context path : " + stream.getScope().getContextPath());
		System.out.println("Stream scope's name : " + stream.getScope().getName());
		System.out.println("Stream scope's attribute names : " + stream.getScope().getAttributeNames().toString());
	}
	
	public void streamSubscriberClose(ISubscriberStream stream){
		System.out.println("########### AVTest streamSubscriberClose ############");
	}
	
	public void streamPlaylistItemPlay(IPlaylistSubscriberStream stream,IPlayItem item,boolean isLive){
		System.out.println("########### AVTest streamPlaylistItemPlay ############ and stream name is : " + stream.getStreamId());		
		System.out.println("########### AVTest streamPlaylistItemPlay ############ and stream name is : " + stream.getName());
		System.out.println("stream statistics : " + stream.getStatistics());
	}
	
	public boolean roomJoin(IClient client, IScope room) {
		System.out.println("##### AVTest Room Join ######");		
		if (!super.roomJoin(client, room)) {
			System.out.println("Room join is unsuccessful");
			return false;
		}
		return true;
	}

	public void roomDisconnect(IConnection conn) {
		System.out
				.println("############## AVTest room Disconnect function called ######");
	}

	
	public boolean connect(IConnection conn, IScope scope, Object[] params) {
		System.out.println("######## AVTest Just Connect Connection object ########");
		scope.setPersistent(false);	
		this.appScope = scope;
		//roomStart(scope);
		return true;
	}

	public boolean join(IClient client, IScope scope) {
		System.out.println("###### AVTest Join method called #######");
		roomJoin(client, scope);
		return true;
	}
	

}