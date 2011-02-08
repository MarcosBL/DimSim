package com.dimdim.conference.application.core;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ResourceBundle;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;

public class OtherCallbacksHandler {

	private static HttpClient httpClient = new HttpClient();
	
	public static void meetingClosed(String dimdimId, String roomId,
			String confKey, String meetingId)
	{
		String url = null;
		try{
			ResourceBundle rb = ResourceBundle.getBundle("resources.dimdim");
			url = rb.getString("dimdim.otherCallBackUrl" );	
		}catch (Exception e) {
			e.printStackTrace();
		}
		String httpArguments = "dimdimId="+dimdimId+"&roomId="+roomId+"&confKey="+confKey+"&meetingId="+meetingId;
		if(null != url && url.length() > 0)
		{
			String returnVal = getURL_String(httpClient, url, httpArguments);
			System.out.println("return of other call back = "+returnVal);
		}
	}
	
	private	static synchronized	String	getURL_String(HttpClient client, String url, String args)
	{
		return getURL_String(client, url, args, 25000);
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
		
//		System.out.println("Getting URL:"+url);
//		int size = v.size();
//		NameValuePair[] data = new NameValuePair[size];
//		for (int i=0; i<size; i++)
//		{
//			data[i] = (NameValuePair)v.elementAt(i);
//		}
		//System.out.println("!@#@!#@#@ Posting url:"+url);
        method = new GetMethod(url+"?"+args);
//        method.setRequestBody(args);
//        method.addParameter("meetingID", args);
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
//            	System.out.println("Received buffer:"+str);
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
        	he.printStackTrace();
        }
        catch (IOException ioe)
        {
        	ioe.printStackTrace();
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
        return	responseBody;

}
}
