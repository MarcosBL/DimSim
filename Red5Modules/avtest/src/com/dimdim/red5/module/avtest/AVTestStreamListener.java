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


import java.io.File;
import java.util.Vector;

import org.red5.server.api.stream.IStreamCapableConnection;
import org.red5.server.api.stream.IStreamService;

public class AVTestStreamListener implements IStreamService {

	protected	String	appName;
	protected	String	streamStorageDirectory;
	protected	Vector	streamNames;
	
	public AVTestStreamListener(String appName, String streamStorageDirectory){
		this.appName = appName;
		this.streamStorageDirectory = streamStorageDirectory;
		this.streamNames = new Vector();
	}
	public void closeStream() {
		System.out.println("####### AVTestStreamListener Close stream #######");		
	}

	public int createStream() {
		System.out.println("########### AVTestStreamListener Create stream ##########");
		return 0;
	}

	public void deleteStream(int streamId) {
		System.out.println("######### AVTestStreamListener Delete Stream ############");
	}

	public void deleteStream(IStreamCapableConnection streamConn, int streamId) {
		System.out.println("###### AVTestStreamListener Delete stream with connection ########");
	}

	public void pause(boolean pausePlayback, int position) {
		System.out.println("#### AVTestStreamListener pause ### " + pausePlayback + " position " + position);
	}

	public void play(Boolean dontStop) {
		System.out.println("##### AVTestStreamListener play #### : dontStop " + dontStop);
		
	}

	public void play(String streamName) {
		System.out.println("##### AVTestStreamListener play #### : streamName " + streamName);
		
	}

	public void play(String streamName, int startPosition) {
		System.out.println("##### AVTestStreamListener play #### : streamName " + streamName + " position : " + startPosition);
		
	}

	public void play(String streamName, int startPosition, int length) {
		System.out.println("##### AVTestStreamListener play #### : streamName " + streamName + " position : " + startPosition + " length " + length);
		
	}

	public void play(String streamName, int startPosition, int length, boolean flushPlaylist) {
		System.out.println("##### AVTestStreamListener play #### : streamName " + streamName + " position : " + startPosition + " length " + length + " flushplaylist " + flushPlaylist);
	}

	public void publish(String streamName) {
		System.out.println("### AVTestStreamListener publish One Parameter ### streamName : " + streamName);
		this.streamNames.add(streamName);
		
	}
	
	public void publish(Boolean dontStop) {
		System.out.println("### AVTestStreamListener publish ### dontStop : " + dontStop);		
	}

	public void publish(String streamName, String mode) {
		System.out.println("### AVTestStreamListener publish Two parameters ### streamName : " + streamName + " mode : " + mode);
		this.streamNames.add(streamName);
	}

	public void receiveAudio(boolean receive) {
		System.out.println("### AVTestStreamListener receive audio ### receive : " + receive);
		
	}

	public void receiveVideo(boolean receive) {
		System.out.println("### AVTestStreamListener receive video ### receive : " + receive);
		
	}

	public void releaseStream(String streamName) {
		System.out.println("### AVTestStreamListener release stream ### streamName : " + streamName);
		
	}

	public void seek(int position) {
		System.out.println("### AVTestStreamListener seek ### position : " + position);
		
	}
	
	public	void	cleanupOldRecording()
	{
		int size = this.streamNames.size();
		if (size > 0)
		{
			for (int i=0; i<size; i++)
			{
				String streamName = (String)this.streamNames.get(i);
				try
				{
					File f = new File(this.streamStorageDirectory,streamName+".flv");
					if (f.exists())
					{
						if (f.delete())
						{
							System.out.println("Deleted file: "+f.getCanonicalPath());
						}
						else
						{
							System.out.println("ERROR while deleting file:"+f.getCanonicalPath());
						}
					}
					else
					{
						System.out.println("The recording file for stream: "+f.getCanonicalPath()+" doesn't exist.");
					}
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		}
		else
		{
			System.out.println("No recording to delete");
		}
	}

}
