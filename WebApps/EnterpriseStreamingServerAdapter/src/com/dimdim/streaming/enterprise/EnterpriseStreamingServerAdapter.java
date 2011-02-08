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

package com.dimdim.streaming.enterprise;

import java.io.File;
import java.io.FileInputStream;
import java.util.PropertyResourceBundle;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.HashMap;
import java.util.ResourceBundle;

import com.dimdim.streaming.StreamingServerAdapter;
import com.dimdim.streaming.StreamingServerAdapterProvider;
import com.dimdim.streaming.StreamingServerConstants;
import com.dimdim.streaming.StreamingServer;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * Only if the resource directory path is provided, the on line refresh will
 * be available. Java resource bundles are not reloaded upon change in the
 * resource file on the disk.
 */

public class EnterpriseStreamingServerAdapter implements StreamingServerAdapter
{
	protected	String	webappLocalPath;
	protected	String	streamingPropertiesFilePath;
	protected	long	lastUpdateTime = 0;
	
//	protected	int		numberOfServers;
	protected	String	name;
	protected	int		numberOfServers;
	protected	HashMap	streamingServersMap;
//	protected	Vector	streamingServers = new Vector();
	
//	protected	int	nextServer = 0;
	
	protected	final	String	SAStreamingServeAdapterResourceName = "resources.streaming";
	
	public	EnterpriseStreamingServerAdapter()
	{
//		this.streamingServers = new Vector();
		
		//	Initialize the adapter from the properties file. That is essentially
		//	read the file and create the number of servers.
//		ResourceBundle rb = ResourceBundle.getBundle("resources.streaming");
	}
	public	void	setWebappLocalPath(String path)
	{
		this.webappLocalPath = path;
		try
		{
			String webinfDir  = (new File(webappLocalPath, "WEB-INF")).getAbsolutePath();
			String classesDir  = (new File( webinfDir, "classes")).getAbsolutePath();
			String resourcesDirectory  = (new File( classesDir, "resources")).getAbsolutePath();
			streamingPropertiesFilePath = (new File( resourcesDirectory, "streaming.properties")).getAbsolutePath();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	protected	void	refreshAdapter()
	{
		File f = new File(this.streamingPropertiesFilePath);
		if (this.lastUpdateTime == 0)
		{
			readStreamingServerConfig(f);
		}
		else
		{
			if (f.lastModified() > this.lastUpdateTime)
			{
				readStreamingServerConfig(f);
			}
		}
	}
	protected	synchronized	void	readStreamingServerConfig(File f)
	{
		try
		{
			this.streamingServersMap = new HashMap();
			this.streamingServersMap.put(StreamingServerConstants.ANY_STREAM, new Vector());
			this.streamingServersMap.put(StreamingServerConstants.AUDIO_STREAM, new Vector());
			this.streamingServersMap.put(StreamingServerConstants.AUDIO_VIDEO_STREAM, new Vector());
			this.streamingServersMap.put(StreamingServerConstants.WHITEBOARD_STREAM, new Vector());
			this.streamingServersMap.put(StreamingServerConstants.SCREEN_SHARE_STREAM, new Vector());
			this.streamingServersMap.put(StreamingServerConstants.ANY_STREAM+"_last", new Integer(0));
			this.streamingServersMap.put(StreamingServerConstants.AUDIO_STREAM+"_last", new Integer(0));
			this.streamingServersMap.put(StreamingServerConstants.AUDIO_VIDEO_STREAM+"_last", new Integer(0));
			this.streamingServersMap.put(StreamingServerConstants.WHITEBOARD_STREAM+"_last", new Integer(0));
			this.streamingServersMap.put(StreamingServerConstants.SCREEN_SHARE_STREAM+"_last", new Integer(0));
			
			FileInputStream fis = new FileInputStream(f);
			PropertyResourceBundle prb = new PropertyResourceBundle(fis);
			this.createServers(prb);
			this.lastUpdateTime = f.lastModified();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	protected	void	createServers(ResourceBundle rb)
	{
		try
		{
//			this.streamingServers = new Vector();
			numberOfServers = 0;
			for (int i=0; i<2000; i++)
			{
				String index = (i+1)+"";
				String rtmpUrl = rb.getString("streaming_server."+index+".rtmp_url");
				String rtmptUrl = rb.getString("streaming_server."+index+".rtmpt_url");
				String avApplicationStreamsDirectory = "";
				try
				{
					avApplicationStreamsDirectory = rb.getString("streaming_server."+index+".av_application_streams_directory");
				}
				catch(Exception e2)
				{
					avApplicationStreamsDirectory = "";
				}
				String maxStreamsStr = rb.getString("streaming_server."+index+".max_number_of_streams");
				int maxStreams = Integer.parseInt(maxStreamsStr);
				String supportedStreamTypesStr = rb.getString("streaming_server."+index+".supported_stream_types");
				Vector v = this.parseSupportedStreamTypes(supportedStreamTypesStr);
				
				System.out.println("Streaming Server: "+index);
				System.out.println("		rtmp url: "+rtmpUrl);
				System.out.println("		rtmpt url: "+rtmptUrl);
				System.out.println("		max streams: "+maxStreams);
				System.out.println("		supported stream types: "+v);
				
				EnterpriseStreamingServer sass = new EnterpriseStreamingServer(rtmpUrl,rtmptUrl,
						avApplicationStreamsDirectory,maxStreams,v);
				int types = v.size();
				for (int j=0; j<types; j++)
				{
					String	type = (String)v.elementAt(j);
					Vector v2 = (Vector)this.streamingServersMap.get(type);
					if (v2 != null)
					{
						v2.addElement(sass);
					}
					else
					{
						System.out.println("Unknown stream type ************* : "+type);
					}
				}
				numberOfServers++;
//				this.streamingServers.addElement(sass);
			}
		}
		catch(Exception e)
		{
		}
	}
	protected	Vector	parseSupportedStreamTypes(String str)
	{
		Vector v = new Vector();
		if (str != null && str.length() >0)
		{
			StringTokenizer parser = new StringTokenizer(str,",");
			while (parser.hasMoreTokens())
			{
				v.addElement(parser.nextToken());
			}
		}
		return	v;
	}
	public String getAdapterType()
	{
		return RED5;
	}
	public int getNumberOfServers()
	{
//		int num = this.streamingServers.size();
//		if (num == 0)
//		{
//			this.refreshAdapter();
//		}
//		return num;
		return	numberOfServers;
	}
	public StreamingServer getAvailableServer()
	{
		return	this.getAvailableServer(StreamingServerConstants.ANY_STREAM);
	}
	public StreamingServer getAvailableServer(String streamType)
	{
//		this.nextServer++;
		this.refreshAdapter();
		Vector v = (Vector)this.streamingServersMap.get(streamType);
		Integer last = (Integer)this.streamingServersMap.get(streamType+"_last");
		if (v == null || last == null || v.size() == 0)
		{
			return	null;
		}
		int	l = last.intValue();
		l++;
		if (l >= v.size())
		{
			l = 0;
		}
		StreamingServer server = (StreamingServer)v.elementAt(l);
		this.streamingServersMap.put(streamType+"_last", new Integer(l));
//		int	size = this.streamingServers.size();
//		if (size > 0)
//		{
//			for (int i=0; i<size; i++)
//			{
//				if (this.nextServer >= size)
//				{
//					this.nextServer = 0;
//				}
//				server = (StreamingServer)this.streamingServers.elementAt(this.nextServer);
//				this.nextServer++;
//				if (server.supportsStreamType(streamType))
//				{
//					break;
//				}
//				else
//				{
//					server = null;
//				}
//			}
//		}
		
		return server;
	}
	/*
	public StreamingServer getAvailableServer()
	{
		StreamingServer server = null;
		int	size = this.streamingServers.size();
		for (int i=0; i<size; i++)
		{
			server = (StreamingServer)this.streamingServers.elementAt(i);
			if (server.isStreamAvailable())
			{
				break;
			}
			else
			{
				server = null;
			}
		}
		return server;
	}
	*/
	public String getName()
	{
		return name;
	}
}
