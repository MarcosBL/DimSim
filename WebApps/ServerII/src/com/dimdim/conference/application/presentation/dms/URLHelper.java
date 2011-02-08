package com.dimdim.conference.application.presentation.dms;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dimdim.conference.ConferenceConsoleConstants;
import com.dimdim.conference.ConferenceConstants;
import com.dimdim.conference.application.core.ActiveConference;
import com.dimdim.conference.model.IConference;
import com.dimdim.conference.model.Presentation;
import com.dimdim.util.misc.Base64;
import com.dimdim.util.misc.FileUtil;

import com.dimdim.mailbox.LocalDMSManager;

public class URLHelper
{
	private static HttpClient httpClient = new HttpClient();
//	private static String dmsUrl = null;
	private	static	int	timeoutValue = 2000;
	
	protected	static	int	getURLCallTimeOut()
	{
		if (timeoutValue == 2000)
		{
			int	value = 4000;
			try
			{
				String s = ConferenceConsoleConstants.getResourceKeyValue("dimdim.serverToServerCallTimeout", "4000");
				value = Integer.parseInt(s);
			}
			catch(Exception e)
			{
				value = 4000;
			}
			timeoutValue = value;
		}
		return	timeoutValue;
	}
	//	MeetingID in this call is the conferenceKey in DCS
	public static ArrayList getPresentations(String meetingID)
	{
		String	preloadedPresentationsSupported = ConferenceConsoleConstants.getResourceKeyValue("dms.preloadedPresentationsSupported", "false");
		if (preloadedPresentationsSupported.equals("true"))
		{
			return	URLHelper.ReadPresentationsList(meetingID,URLHelper.getPresentationsListRemote(meetingID));
		}
		else
		{
			System.out.println("Preloaded presentations not supported");
			return	new	ArrayList();
		}
	}
//	private static String getPresentationsListLocal(String meetingID)
//	{
//		return	LocalDMSManager.getLocalDMSManager().getPreloadedPresentations(meetingID, null);
//	}
	private static String getPresentationsListRemote(String meetingID)
	{
		String dmsServerResponse = null;
		try
		{
			String action = "listDocuments";
			dmsServerResponse = getURL_String(httpClient,
					"http://"+ConferenceConsoleConstants.getDMServerInternalAddress()+"/"+action, "meetingID="
					+ConferenceConsoleConstants.getInstallationId()+ConferenceConsoleConstants.getInstallationPrefix()+meetingID);
		}
		catch (Throwable e)
		{
			System.out.println("exception while getting presentations  ");
			//e.printStackTrace();
		}
		return	dmsServerResponse;
	}
	private static ArrayList ReadPresentationsList(String meetingID, String dmsServerResponse)
	{
		ArrayList listPresentations = new ArrayList();
		
//		String action = "listDocuments";
		Presentation presentation = null;
		JSONArray pptArray = null;
		
		try
		{
//			String dmsServerResponse = getURL_String(httpClient,
//					"http://"+ConferenceConsoleConstants.getDMServerInternalAddress()+"/"+action, "meetingID="
//					+ConferenceConsoleConstants.getInstallationId()+ConferenceConsoleConstants.getInstallationPrefix()+meetingID);
//				System.out.println("get meetings response:"+conferenceServerResponse);
				//conferenceServerResponse = "{result:\"True\",method:\"listPresentations\"}";
			if (dmsServerResponse != null && dmsServerResponse.startsWith("{"))
			{
				JSONObject jsonObj=new JSONObject(dmsServerResponse);
				System.out.println("jsonObj = "+jsonObj);
				Object objPptArray = jsonObj.get("docs");
				if(objPptArray instanceof JSONArray)
				{
					pptArray = (JSONArray)objPptArray;
				}
				if(null != pptArray)
				{
					for (int i = 0; i < pptArray.length(); i++)
					{
						JSONObject singlePPT = pptArray.getJSONObject(i);
						System.out.println("single ppt = "+singlePPT);
						presentation = new Presentation();
						presentation.setMeetingId(meetingID);
						String fileName = singlePPT.getString("docName");
						byte[] bytes = fileName.getBytes();
						String base64EncodedFileName = Base64.encodeBytes(bytes, 0, bytes.length,Base64.DONT_BREAK_LINES);
						presentation.setOriginalFileName(base64EncodedFileName);
						presentation.setSlideCount(singlePPT.getInt("noOfPages"));
						presentation.setPresentationId(singlePPT.getString("docID"));
						
						presentation.setWidth(singlePPT.getInt("width"));
						presentation.setHeight(singlePPT.getInt("height"));
						listPresentations.add(presentation);
					}
				}
			}
		}
		catch (Throwable e)
		{
			System.out.println("exception while getting presentations  ");
			//e.printStackTrace();
		}
		
		return listPresentations;
	}
	public static boolean closeMeetingOnDMS(String confKey)
	{
		String action = "closeMeeting";
		boolean returnValue = true;
		
		String response = getURL_String(httpClient,
				"http://"+ConferenceConsoleConstants.getDMServerInternalAddress()+"/"+action,
				"meetingID="+ConferenceConsoleConstants.getInstallationId()+
				ConferenceConsoleConstants.getInstallationPrefix()+confKey);
		System.out.println("the response on closing meeting on DMS is "+response);
		
		String cobUrl = "http://"+ConferenceConsoleConstants.getResourceKeyValue("dimdim.dmsCobAddressInternalAddress", "");
		String cobEnabled = ConferenceConsoleConstants.getResourceKeyValue("dimdim.show.cob", "true");
		
		String cobCloseUrl = cobUrl+"/cobrowsing/closeMeeting"; 
		String args = "dimdimID="+ConferenceConsoleConstants.getInstallationId()+
						ConferenceConsoleConstants.getInstallationPrefix()+confKey+"&roomID="+confKey;
		if("true".equalsIgnoreCase(cobEnabled))
		{
			response = getURL_String(httpClient, cobCloseUrl, args);
			System.out.println("the response on closing meeting on COB is "+response);
		}
		return returnValue;
	}
	public static DMSResourceSet getResourceSet(IConference conf, String desktopResName, String whiteboardResName
			, String coBrowseResName)
	{
//		String action = "listAttachedResources";
//		String meetingID = conf.getConfig().getConferenceKey();
		//here the resource names are listed
		
		//String desktopResName = "Share Desktop";
		//String whiteboardResName = "Share Whiteboard";
		String resourceNames[] = {"desktop", "whiteboard"};
		String resourceNiceNames[] = { desktopResName, whiteboardResName};
		//String cobResourceId = "asdf1234";
		//String resourceNames[] = {"desktop"};
		//here other settings which can over-ride these settings are mentioned so this array size must be same as resourceName
		String resourceTypes[] = { ConferenceConstants.RESOURCE_TYPE_SCREEN_SHARE, ConferenceConstants.RESOURCE_TYPE_WHITEBOARD};
		boolean settingsFromPortal[] = {conf.isPublisherEnabled(), conf.isWhiteboardEnabled()};
		boolean status = false;
		DMSResourceSet returnValue = new DMSResourceSet();
		try
		{
			String response = null;//getURL_String(httpClient,
//					"http://"+ConferenceConsoleConstants.getDMServerInternalAddress()+"/"+action, meetingID);
			if (response != null && response.startsWith("{"))
			{
				JSONObject jsonObj = new JSONObject(response);
				for (int i = 0; i < resourceNames.length; i++)
				{
					status = jsonObj.getBoolean(resourceNames[i]);
					System.out.println("from dms the resource name = "+resourceNames[i] +" is enabled = "+status);
					returnValue.addResource(resourceNiceNames[i],
//							ConferenceConstants.RESOURCE_TYPE_SCREEN_SHARE,
							resourceTypes[i],
							(status && settingsFromPortal[i]) );
				}
			}
			else
			{
			    if(conf.isPublisherEnabled())
			    {
			    	returnValue.addResource(desktopResName,
						ConferenceConstants.RESOURCE_TYPE_SCREEN_SHARE, true);
			    }
				if (conf.isWhiteboardEnabled())
				{
					returnValue.addResource(whiteboardResName,
							ConferenceConstants.RESOURCE_TYPE_WHITEBOARD, true);
				}
				
				
			}
		}
		catch (Throwable e)
		{
			System.out.println("exception while getting desktop enabled ");
//			returnValue.addResource("desktop",
//					ConferenceConstants.RESOURCE_TYPE_SCREEN_SHARE, true);
		    if(conf.isPublisherEnabled())
		    {
		    	returnValue.addResource(desktopResName,
					ConferenceConstants.RESOURCE_TYPE_SCREEN_SHARE, true);
		    }
			if (conf.isWhiteboardEnabled())
			{
				returnValue.addResource(whiteboardResName,
						ConferenceConstants.RESOURCE_TYPE_WHITEBOARD, true);
			}
//			e.printStackTrace();
		}
		
		if(conf.isFeatureCob())
		{
			/*
			String cobUrl = "http://"+ConferenceConsoleConstants.getResourceKeyValue("dimdim.dmsCobAddressInternalAddress", "");
			String bookmarkAction = ConferenceConsoleConstants.getServerAddress()
										+"/"
										+ConferenceConsoleConstants.getWebappName()
										+"/"
										+"BookmarkCobResource.action";
			String args = "dimdimID="+ConferenceConsoleConstants.getInstallationId()+
			ConferenceConsoleConstants.getInstallationPrefix()+conf.getConferenceInfo().getKey()
			+"&roomID="+conf.getConferenceInfo().getKey()+"&bookmarkAction="+bookmarkAction+"&cflag="+System.currentTimeMillis();
		
			String jsonResponse = getURL_String(httpClient, cobUrl+"/cobrowsing/listURLResources", args, getURLCallTimeOut());
			System.out.println("json = "+jsonResponse);
			
			JSONObject jsonObj;
			try {
				if(null != jsonResponse)
				{
					jsonObj = new JSONObject(jsonResponse);
					if("true".equalsIgnoreCase(jsonObj.getString("result")) )
					{
						JSONArray cobUrlsArray = jsonObj.getJSONArray("resources");
						if(null != cobUrlsArray)
						{
							for (int i = 0; i < cobUrlsArray.length(); i++)
							{
								JSONObject singleCahceUrl = cobUrlsArray.getJSONObject(i);
								//only add if it is pre-loaded
								if("preloaded".equalsIgnoreCase(singleCahceUrl.getString("resourceType")))
								{
									System.out.println("single cacheUrl = "+singleCahceUrl);
									returnValue.addResource(singleCahceUrl.getString("resourceURL"), 
										ConferenceConstants.RESOURCE_TYPE_COBROWSE, true, singleCahceUrl.getString("resourceID"));
								}
							}
						}
					}
				}
			} catch (JSONException e) {
				System.out.println("exception while getting pre-loaded urls... "+e.getMessage());
			}
			*/
		}
		//returnValue.addResource(coBrowseResName,ConferenceConstants.RESOURCE_TYPE_COBROWSE, true, cobResourceId);
		
		return returnValue;
	}
	
//	public static String callCacheCob(String args) throws JSONException
//	{
//		dmsUrl = "http://"+ConferenceConsoleConstants.getResourceKeyValue("dimdim.dmsCobAddressInternalAddress", "");
//		String jsonResponse = getURL_String(httpClient, dmsUrl+"/cobrowsing/createURLResource",args, getURLCallTimeOut());
//		System.out.println("json = "+jsonResponse);
//
//		JSONObject jsonObj;
//		try {
//			jsonObj = new JSONObject(jsonResponse);
//		
//			if("true".equalsIgnoreCase(jsonObj.getString("result")) )
//			{
//				return "true";
//			}
//		} catch (JSONException e) {
//			System.out.println("exception while caching url... "+e.getMessage());
//			throw e;
//		}
//		return "false";
//	}
	
	//	MeetingID in this call is the conferenceKey in DCS, userID is ignored.
	public static String getLogoUrl(String meetingID, String userId)
	{
		String u = userId;
		if (u == null)
		{
			u = meetingID;
		}
		String localDMSMailbox = ConferenceConsoleConstants.getResourceKeyValue("dms.localMailboxDirectory", "");
		if (LocalDMSManager.isDMSLocal() && localDMSMailbox != null && localDMSMailbox.length() > 0)
		{
			return	URLHelper.getLogoUrlLocal(meetingID, u);
		}
		else
		{
			return	URLHelper.getLogoUrlRemote(meetingID, u);
		}
	}
	private static String getLogoUrlLocal(String meetingID, String userId)
	{
		String url = ConferenceConsoleConstants.getDefaultLogo();
		String fileName = LocalDMSManager.getLocalDMSManager().getLogoUrl(meetingID, meetingID);
		System.out.println("Local DMS Manager url:"+fileName);
		if (fileName != null && fileName.length() > 0)
		{
			url = "http://"+ConferenceConsoleConstants.getDMServerMboxExternalAddress()+"/"+fileName;
		}
		System.out.println("Local DMS URL:"+url);
		return	url;
	}
	private static String getLogoUrlRemote(String meetingID, String userId)
	{
		String action = "listStaticFiles";
		boolean flag = true;
		String returnValue = "";
		try
		{
			if(null == meetingID)
			{
				return ConferenceConsoleConstants.getDefaultLogo();
			}
			String installationId = ConferenceConsoleConstants.getResourceKeyValue("dimdim.installationId","_default");
			String installationSeperator = ConferenceConsoleConstants.getResourceKeyValue("dimdim.installationPrefix","____");
			String args = "dimdimId="+installationId+installationSeperator+userId+"&roomId="+meetingID;
			String response = getURL_String(httpClient,
					"http://"+ConferenceConsoleConstants.getDMServerMboxInternalAddress()+"/mbox/"+action, args);
			System.out.println("inside getLogUrl response = "+response);
			JSONObject jsonObj=new JSONObject(response);
			flag =  jsonObj.getBoolean("result");
			if(flag){
				JSONArray privFilesArray = jsonObj.getJSONArray("files");
				int len = privFilesArray.length();
				for(int i = 0; i < len ; i++){
					JSONObject fileNameObj = privFilesArray.getJSONObject(i);
					String fileName = fileNameObj.getString("fileName");
					System.out.println("filename from dms = "+fileName);
					if(fileName.startsWith("dimdim_logo")){
						String pathName = jsonObj.getString("path");
						returnValue = "http://"+ConferenceConsoleConstants.getDMServerMboxExternalAddress()+"/" + pathName + "/"+fileName;
						break;
					}
						
				}
				
			}else{
				returnValue = ConferenceConsoleConstants.getDefaultLogo();
			}
			if(returnValue.length() == 0){
				returnValue = ConferenceConsoleConstants.getDefaultLogo();
			}
		}catch (Throwable e) {
			System.out.println("exception while getting default logo so reverting to conf server default");
//			e.printStackTrace();
			returnValue = ConferenceConsoleConstants.getDefaultLogo();
		}
		
		System.out.println("inside getLogUrl returnValue = "+returnValue);
		
		return returnValue;
	}
	
	
	private	static synchronized	String	getURL_String(HttpClient client, String url, String args)
	{
		return getURL_String(client, url, args, getURLCallTimeOut());
	}
		/**
		 * This method uses the HttpClient to connect to the conference server. Reads
		 * the return and 
		 * 
		 * @param url
		 * @param arguments
		 * @return
		 */
		private	static synchronized	String	getURL_String(HttpClient client, String url, String args, int timeout)
		{
			GetMethod method = null;
			
			System.out.println("Getting URL:"+url);
//			int size = v.size();
//			NameValuePair[] data = new NameValuePair[size];
//			for (int i=0; i<size; i++)
//			{
//				data[i] = (NameValuePair)v.elementAt(i);
//			}
			//System.out.println("!@#@!#@#@ Posting url:"+url);
	        method = new GetMethod(url+"?"+args);
//	        method.setRequestBody(args);
//	        method.addParameter("meetingID", args);
	        //method.setFollowRedirects(true);
	        
	        //execute the method
	        System.out.println(" Calling url:"+url+"?"+args);
	        String responseBody = null;
	        StringBuffer buf = new StringBuffer();
	        try
	        {
	        	client.setTimeout(timeout);
	            client.executeMethod(method);
	            InputStream is = method.getResponseBodyAsStream();
	            BufferedInputStream bis = new BufferedInputStream(is);
	            byte[] ary = new byte[256];
	            int len = 0;
	            while ((len=bis.read(ary,0,256))>0)
	            {
	            	String str = new String(ary,0,len);
//	            	System.out.println("Received buffer:"+str);
	            	buf.append(str);
	            }
	            try
	            {
	            	bis.close(); is.close();
	            }
	            catch(Exception e)	{	}
	            responseBody = buf.toString();
	        }
	        catch (HttpException he)
	        {
	        	//he.printStackTrace();
	        	System.out.println("exception getURL_String "+he.getMessage());
	        }
	        catch (IOException ioe)
	        {
	        	//ioe.printStackTrace();
	        	System.out.println("exception getURL_String "+ioe.getMessage());
	        }
	        catch(Throwable ee)
	        {
	        	//	Ignore the exceptions in cleanup.
	        }
	        //clean up the connection resources
	        try
	        {
	        	method.releaseConnection();
	        }
	        catch(Exception ee)
	        {
	        	//	Ignore the exceptions in cleanup.
	        }
			System.out.println("Got URL ---:"+url);
	        return	responseBody;
	
	}
}
