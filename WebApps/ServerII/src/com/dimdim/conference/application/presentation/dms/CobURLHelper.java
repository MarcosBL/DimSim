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

public class CobURLHelper
{
	private static HttpClient httpClient = new HttpClient();
	private	static	int	timeoutValue = 5000;
	
	protected	static	int	getURLCallTimeOut()
	{
		if (timeoutValue == 5000)
		{
			int	value = 10000;
			try
			{
				String s = ConferenceConsoleConstants.getResourceKeyValue("dimdim.serverToServerCobCallTimeout", "10000");
				value = Integer.parseInt(s);
			}
			catch(Exception e)
			{
				value = 10000;
			}
			timeoutValue = value;
		}
		return	timeoutValue;
	}
	public static String callCacheCob(String args) throws Exception
	{
		String dmsUrl = "http://"+ConferenceConsoleConstants.getResourceKeyValue("dimdim.dmsCobAddressInternalAddress", "");
		String jsonResponse = getURL_String(httpClient, dmsUrl+"/cobrowsing/createURLResource",args, getURLCallTimeOut());
		System.out.println("json = "+jsonResponse);

		JSONObject jsonObj;
		try {
			jsonObj = new JSONObject(jsonResponse);
		
			if("true".equalsIgnoreCase(jsonObj.getString("result")) )
			{
				return "true";
			}
		} catch (Exception e) {
			System.out.println("exception while caching url... "+e.getMessage());
			throw e;
		}
		return "false";
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
