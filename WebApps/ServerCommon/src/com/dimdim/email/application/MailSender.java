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

package com.dimdim.email.application;

import java.util.*;
import java.io.*;
import javax.mail.*;
import com.sun.mail.smtp.*;
import javax.mail.internet.*;
import javax.activation.*;

/**
 * @author Saurav Mohapatra
 *
 */
public class MailSender
{
//	private String to = "";
	private String from = "";
	private String smtpServer = "";
	private String smtpUser = null;
	private String smtpPass = null;

	private SMTPTransport bus = null;
	private Session session = null;
	
	public MailSender(String from, String to, String smtpServer,String user, String pass) throws Exception
	{
//		this.to = to;
		this.from = from;
		this.smtpServer = smtpServer;
		this.smtpUser = user;
		this.smtpPass = pass;
		
		connect();
	}
	public void connect() throws Exception
	{
		 // Create properties for the Session
        Properties props = new Properties();

        // If using static Transport.send(),
        // need to specify the mail server here
        props.put("mail.smtp.host", smtpServer);
        if(smtpUser != null)
        {
        	props.put("mail.smtp.auth", "true");
        	props.put("mail.smtp.user",smtpUser);
        	props.put("mail.from",from);
        }
        // To see what is going on behind the scene
        props.put("mail.debug", "true");

        // Get a session
        session = Session.getInstance(props);
        //      Get a Transport object to send e-mail
        bus = (SMTPTransport)session.getTransport("smtp");
        
        this.open();
	}
	public void open()	throws	Exception
	{
        // Connect only once here
        // Transport.send() disconnects after each send
        // Usually, no username and password is required for SMTP
        if(smtpUser == null)
        {
        	bus.connect();
        }
        else
        {
        	System.out.println("Connecting with password...");
        	bus.connect(smtpServer, smtpUser, smtpPass);
        }
	}
	public boolean isConnected()
	{
		return this.bus.isConnected();
	}
	public void sendTextMail(String to, String fromName, String subject, String body) throws Exception
	{
		 // Instantiate a message
        Message msg = new MimeMessage(session);

        // Set message attributes
        msg.setFrom(new InternetAddress(from));
        msg.addHeader("Sender",fromName);
        InternetAddress[] address = InternetAddress.parse(to, false);
        // Parse comma/space-separated list. Cut some slack.
		msg.setRecipients(Message.RecipientType.TO,
                            address);

        msg.setSubject(subject);
        msg.setSentDate(new Date());

        // Set message content and send
        setTextContent(msg,body);
        msg.saveChanges();
        bus.sendMessage(msg, address);


	}
	private void setTextContent(Message msg, String body) throws Exception
	{
        msg.setContent(body, "text/plain");

	}
	public void sendHTMLMail(String to, String fromName, String subject, String body) throws Exception
	{
		 // Instantiate a message
        Message msg = new MimeMessage(session);

        // Set message attributes
        msg.setFrom(new InternetAddress(from));
        msg.addHeader("Sender",fromName);
        InternetAddress[] address = InternetAddress.parse(to, false);
		        // Parse comma/space-separated list. Cut some slack.
				msg.setRecipients(Message.RecipientType.TO,
                            address);

        msg.setSubject(subject);
        msg.setSentDate(new Date());

        // Set message content and send
        setHTMLContent(msg,body);
        msg.saveChanges();
        bus.sendMessage(msg, address);


	}

    // Set a single part html content.
    // Sending data of any type is similar.
    public static void setHTMLContent(Message msg, String html) throws MessagingException {



        // HTMLDataSource is an inner class
        msg.setDataHandler(new DataHandler(new HTMLDataSource(html)));
    }



    /*
     * Inner class to act as a JAF datasource to send HTML e-mail content
     */
    static class HTMLDataSource implements DataSource {
        private String html;

        public HTMLDataSource(String htmlString) {
            html = htmlString;
        }

        // Return html string in an InputStream.
        // A new stream must be returned each time.
        public InputStream getInputStream() throws IOException {
            if (html == null) throw new IOException("Null HTML");
            return new ByteArrayInputStream(html.getBytes());
        }

        public OutputStream getOutputStream() throws IOException {
            throw new IOException("This DataHandler cannot write HTML");
        }

        public String getContentType() {
            return "text/html";
        }

        public String getName() {
            return "JAF text/html dataSource to send e-mail only";
        }
    }

    public void close()
    {
    	try
    	{
    		bus.close();
    	}
    	catch(Exception ex)
    	{

    	}
    }

    public static void main(String[] args) throws Exception
    {


    	if(args.length < 5)
    	{
    		System.err.println("java MailSender from(email) to(comma seperated emails) server user password");
    		return;
    	}
    	
    	
    	String from = args[0]; //"saurav.mohapatra@communiva.com";
    	String to = args[1]; //"saurav.mohapatra@communiva.com, mohaps@gmail.com, prakash.khot@gmail.com";
    	String server = args[2]; //"smtp.atlarge.net";
    	String user = args[3]; //"mohsa01.COMMUNIVA";
    	String pass = args[4];
    	
    	int	count = 1;
    	if (args.length > 5)
    	{
    		count = Integer.parseInt(args[5]);
    	}

    	MailSender sender = new MailSender(from,to,server,user,pass);
    	
    	for (int i=0; i<count; i++)
    	{
	    	/*System.out.println("Sending text mail...");
	    	sender.sendTextMail("dimdim Test Mail","Hi Saurav,\n How Are You\n regards\n Saurav");*/
	    	System.out.println("Sending html mail...");
	    	sender.sendHTMLMail(to,from,"dimdim HTML Test Mail","<html><head><title>Wow</title></head><body><h1>Hello</h1><p>this is a test mail and if you click on this <a href=\"http://www.google.com\">link</a> you should go to <b>Google</b></body></html>");
	    	
	    	try { Thread.sleep(2000); } catch(Exception e) { }
    	}
    	
    	sender.close();
    }

}
