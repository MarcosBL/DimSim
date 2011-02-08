
package com.dimdim.email.application;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringBufferInputStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.SendFailedException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.URLName;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.dimdim.email.model.ConferenceInfo;
import com.dimdim.email.model.IConferenceParticipant;
import com.sun.mail.smtp.SMTPSSLTransport;

public class Mailer2	{
	
    public static final String MAILER_RESOURCES = "resources.dimdim";
    public static final int DEFAULT_BUFFER_SIZE = 1024;
    public static final boolean DEFAULT_DEBUG_MAIL_SESSION = false;
    
    protected	ConferenceInfo	conferenceInfo = null;
    
    public static final String DEFAULT_PROTOCOL = "smtp";
    public static final int DEFAULT_PORT = 25;
    
    public static final String HOST_PARAM = "email.server";
    public static final String EMAIL_USER_KEY = "email.user";
    public static final String EMAIL_PASSWORD_KEY = "email.password";
    public static final String EMAIL_FROM_KEY = "email.sender";
    
    public static final String PROTOCOL_PARAM = "email.PROTOCOL";
    public static final String PORT_PARAM = "email.PORT";
    public static final String DEBUG_MAIL_SESSION_PARAM = "email.DEBUG_MAIL_SESSION";
    public static final String BUFFER_SIZE_PARAM = "email.BUFFER_SIZE";
    public static final String EMAIL_SUBJECT_KEY = "email.EMAIL_SUBJECT";
    public static final String EMAIL_FROM_PERSON_KEY  = "email.EMAIL_FROM_PERSON";
    public static final String EMAIL_REPLY_TO_KEY = "email.EMAIL_REPLY_TO";
    public static final String EMAIL_TO_KEY = "email.EMAIL_TO";
    public static final String EMAIL_CC_KEY = "email.EMAIL_CC";
    public static final String EMAIL_BCC_KEY = "email.EMAIL_BCC";
    public static final String EMAIL_CHARSET_KEY = "email.EMAIL_CHARSET";
    
    protected ResourceBundle mailerRes;
    
    protected Session session;
    protected Transport transport;
    protected String host;
    protected String user;
    protected String password;
    protected String protocol;
    protected int port;
    protected boolean debug;
    protected int bufferSize;
    protected String emailSubject;
    protected Address emailFrom;
    protected Address emailReplyTo[];
    protected Address emailTo[];
    protected Address emailCc[];
    protected String emailCharset;
    
    protected	ErrorHandler	errorHandler = new ErrorHandler();
    private String smtpSSL = "1";
    
    public	Mailer2(IErrorReporter conf, IConferenceParticipant sender, String target)	throws	Exception
    {
    	initialize(conf,sender,target);
    }
    protected	String	getInitParameter(String key)
    {
    	String value = null;
    	try
    	{
    		value = mailerRes.getString(key);
    	}
    	catch(Exception e)
    	{
    		
    	}
    	return	value;
    }
    protected	String	getBeanResourceAsString(String key, boolean required)
    	throws	Exception
    {
    	String	value = getInitParameter(key);
    	if (value == null && required)
    	{
    		throw	new	Exception();
    	}
    	return	value;
    }
    protected	Object	getBeanResourceAsStringOrStringArray(String s, boolean required)
    	throws	Exception
    {
    	return	getBeanResourceAsString(s,required);
    }
	protected void initialize(IErrorReporter conf, IConferenceParticipant sender,
			String target) throws Exception
	{
		try
		{
			mailerRes = EmailConstants.getDimdimProperties();
    	}
    	catch (MissingResourceException e)
    	{
            throw new Exception(e.getMessage());
        }

        // Get the host parameter
        host = getInitParameter(HOST_PARAM);
        // Get the protocol parameter
        protocol = getInitParameter(PROTOCOL_PARAM);
        if (protocol == null || protocol.length() == 0)
            protocol = DEFAULT_PROTOCOL;

        String sp = getInitParameter(PORT_PARAM);
        if (sp == null || sp.length() == 0)
            port = DEFAULT_PORT;
        else
        	port = Integer.parseInt(sp.trim());

        // Get the debug parameter
        debug = DEFAULT_DEBUG_MAIL_SESSION;
        String debugParam = getInitParameter(DEBUG_MAIL_SESSION_PARAM);
        if (debugParam != null)
            debug = Boolean.valueOf(debugParam).booleanValue();

        // Get the buffer size parameter
        bufferSize = DEFAULT_BUFFER_SIZE;
        String bufferSizeParam = getInitParameter(BUFFER_SIZE_PARAM);
        if (bufferSizeParam != null)
            try {
                bufferSize = Integer.parseInt(bufferSizeParam);
            } catch (NumberFormatException e) {
                errorHandler.fatalError("[INVALID_INIT_PARAM]", null,
                    BUFFER_SIZE_PARAM, bufferSizeParam, conf, sender, target);
            }

        // Get the e-mail subject
        emailSubject = getBeanResourceAsString(EMAIL_SUBJECT_KEY, true);
        user = getBeanResourceAsString(EMAIL_USER_KEY, true);
        password = getBeanResourceAsString(EMAIL_PASSWORD_KEY, true);

        // Get the e-mail from address
        String emailFromPerson
            = getBeanResourceAsString(EMAIL_FROM_PERSON_KEY, true);
        String emailFromAddress
            = getBeanResourceAsString(EMAIL_FROM_KEY, true).trim();
        
        emailFrom = getInternetAddress(emailFromAddress, emailFromPerson,conf,sender,target);

        // Get the e-mail replay-to addresses
        emailReplyTo = getAddressList(EMAIL_REPLY_TO_KEY,conf,sender,target);

        // Get the e-mail to addresses
        emailTo = getAddressList(EMAIL_TO_KEY,conf,sender,target);

        // Get the e-mail cc addresses
        emailCc = getAddressList(EMAIL_CC_KEY,conf,sender,target);

        // Get the e-mail bcc addresses
//        emailBcc = getAddressList(EMAIL_BCC_KEY);

        // Get the e-mail charset
        emailCharset = getBeanResourceAsString(EMAIL_CHARSET_KEY, true);
        
//      Get ssl enabled or not
        smtpSSL  = getInitParameter("dimdim.smtptype");
        
        URLName urlName = new URLName(protocol,host,port,"",user,password);

        // Create the session's properties
        Properties props = new Properties();
    	props.put("mail." + protocol + ".auth", "true");
        props.put("mail." + protocol + ".host", host);
        if (debug)
            props.put("mail.debug", "true");

        // Get the session object
        session = Session.getInstance(props, null);
        session.setDebug(debug);

        // Get the transport object
        //if not smtpssl get a plain connection
        if("2".equalsIgnoreCase(smtpSSL))
        {
            transport = new SMTPSSLTransport(session, urlName);
            
        }else{
            try {
                transport = session.getTransport(urlName);
            }catch (NoSuchProviderException e) {
                errorHandler.fatalError("[NO_PROTOCOL_PROVIDER]", e, protocol,conf,sender,target);
            }
        }
            
        if (transport == null)
            errorHandler.fatalError("[COULDNT_GET_TRANSPORT]", null, protocol,conf,sender,target);
    }

    /**
     * Gets an InternetAddress object
     */
    protected InternetAddress getInternetAddress(String emailAddress, String emailPerson,
    		IErrorReporter conf, IConferenceParticipant sender, String target) throws Exception
    {
        if (emailAddress.length() > 0)
        {
            if (emailPerson != null && emailPerson.length() > 0)
            {
                try
                {
                    return new InternetAddress(emailAddress, emailPerson);
                }
                catch (UnsupportedEncodingException e)
                {
                    errorHandler.fatalError("[UNSUPPORTED_ENCODING]", e,
                        emailPerson, emailAddress,conf,sender,target);
                }
            }
            else
            {
                try
                {
                    return new InternetAddress(emailAddress);
                }
                catch (AddressException e)
                {
                    errorHandler.fatalError("[WRONGLY_FORMATTED_ADDRESS]", e,
                        emailAddress,conf,sender,target);
                }
            }
        }
        return null;
    }

    /**
     * Gets a list of addresses
     */
    protected Address[] getAddressList(String key,
    		IErrorReporter conf, IConferenceParticipant sender,String target) throws Exception {
        Object obj = getBeanResourceAsStringOrStringArray(key, true);
        return this.getAddressListFromValue(obj,conf,sender,target);
    }
    protected Address[] getAddressListFromValue(Object obj,
    		IErrorReporter conf, IConferenceParticipant sender,String target) throws Exception {
        // Get the list as a String array
        String addressArray[] = null;
        if (obj instanceof String[])
            addressArray = (String[]) obj;
        else {
            Vector addressVector = new Vector();
            StringTokenizer stk = new StringTokenizer((String) obj,",");
            while (stk.hasMoreTokens()) {
                String token = stk.nextToken();
                addressVector.addElement(token);
            }
            addressArray = new String[addressVector.size()];
            for (int i = 0; i < addressArray.length; i++)
                addressArray[i] = (String) addressVector.elementAt(i);
        }

        // Create the array of InternetAddress objects
        int length = addressArray.length;
        if (length == 0)
            return null;
        Address list[] = new Address[length];
        for (int i = 0; i < length; i++)
            list[i] = getInternetAddress(addressArray[i], null,conf,sender,target);
        return list;
    }

    /**
     * Warning about some addresses
     */
    protected void addressWarning(String key, Address list[])
    {
        if (list != null && list.length > 0)
        {
            StringBuffer buf = new StringBuffer();
            for (int i = 0; i < list.length; i++)
                buf.append("  ").append(list[i].toString());
            errorHandler.warning(key, new String[] { buf.toString() });
        }
    }

//* PROCESSING

    /**
     * Converts a bean to text and tries to e-mail it.
     * Returns true if the operation is successful
     */
    public	boolean sendMessage(String recipient, String organizer, String subject, String messageText,
    		IErrorReporter conf, IConferenceParticipant sender, String target)
    	throws	Exception
    {
        // Bean-to-text mapping
        String text = null;
        String subjectUTF8 = null;
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            try {
            	byte[] ary = messageText.getBytes();
                out.write(ary,0,ary.length);
            } finally {
                out.close();
            }
            text = out.toString("utf-8");
        } catch (IOException e) {
            errorHandler.error("[COULDNT_MAP_BEAN_TO_TEXT]", e, conf, sender, target);
            return false;
        }

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            try {
            	byte[] ary = subject.getBytes();
                out.write(ary,0,ary.length);
            } finally {
                out.close();
            }
            subjectUTF8 = out.toString("utf-8");
        } catch (IOException e) {
            errorHandler.error("[COULDNT_MAP_BEAN_TO_TEXT]", e, conf, sender, target);
            return false;
        }
        
        // Build the message
        MimeMessage msg = new MimeMessage(session);
        try {
           /* if (emailSubject.length() > 0)
            {
           //     msg.setSubject(emailSubject+" "+subject);
                msg.setSubject(subject+" "+emailSubject);
            }
            else
            {*/
            	msg.setSubject(subjectUTF8, "UTF-8");
            //}
            if (emailFrom != null)
                msg.setFrom(emailFrom);
            if(conferenceInfo != null)
            	emailReplyTo[0] = new InternetAddress(conferenceInfo.getOrganizerEmail());
            if (emailReplyTo != null)
                msg.setReplyTo(emailReplyTo);
            
//            if (emailTo != null)
                msg.setRecipients(Message.RecipientType.TO, getAddressListFromValue(recipient,conf,sender,target));
            
            if (emailCc != null)
                msg.setRecipients(Message.RecipientType.CC, emailCc);
            /*
            if (emailCharset.length() > 0)
                msg.setText(text, emailCharset);
            else
                msg.setText(text);
            */
            msg.setContent(text,"text/html");
//            msg.addHeader("Sender",organizer);
            msg.setSentDate(new Date());
        } catch (MessagingException e) {
            errorHandler.error("[COULDNT_BUILD_MESSAGE]", e, conf, sender, target);
            return false;
        }

        synchronized (this) {
            if (!transport.isConnected())
                // Connect to the host
                try {
                    transport.connect(host,user,password);
                } catch (MessagingException e) {
                    errorHandler.error("[COULDNT_CONNECT]", e, host, conf, sender, target);
                    return false;
                }

            // Send the message
            try {
                Address addressList[] = msg.getAllRecipients();
                if (addressList != null && addressList.length > 0)
                    transport.sendMessage(msg, addressList);
                else {
                    errorHandler.warning("[NO_RECIPIENTS]");
                    return false;
                }
            } catch (MessagingException e) {
                boolean retryFlag = true;

                // Log the error
                synchronized (this) {
                    errorHandler.error("[COULDNT_SEND_MESSAGE]", e, conf, sender,target);
                    if (e instanceof SendFailedException) {
                        SendFailedException sfe = (SendFailedException) e;
                        Address[] invalid = sfe.getInvalidAddresses();
                        addressWarning("[INVALID_ADDRESSES]", invalid);
                        Address[] validUnsent = sfe.getValidUnsentAddresses();
                        addressWarning("[VALID_UNSENT_ADDRESSES]", validUnsent);
                        Address[] validSent = sfe.getValidSentAddresses();
                        addressWarning("[VALID_SENT_ADDRESSES]", validSent);
                        if (invalid != null && invalid.length > 0)
                            retryFlag = false; // there is no point to retry
                    }
                }

                // Retry with a new transport object. Don't log any new errors.
                if (retryFlag) {
                    Transport newTransport = null;
                    try {
                        // Try to resend the message using a new connection
                        newTransport = session.getTransport(protocol);
                        newTransport.connect(host,user,password);
                        newTransport.sendMessage(msg, msg.getAllRecipients());

                        // Try to close the old transport
                        try {
                            transport.close();
                        } catch (Exception t) {
                        }

                        // The new transport object worked
                        transport = newTransport;
                        return true;
                    } catch (Exception t) {
                        // The new transport failed too. Try to close it
                        if (newTransport != null)
                            try {
                                newTransport.close();
                            } catch (Exception t2) {
                            }
                    }
                }
                return false;
            }
        }
        return true;
    }
    
    /**
     * Converts a bean to text and tries to e-mail it.
     * Returns true if the operation is successful
     */
    public	boolean sendMessage(String recipient, String organizer, String subject, String messageText,
    		IErrorReporter conf, IConferenceParticipant sender, String target, String calendarEvent)
    	throws	Exception
    {
        // Bean-to-text mapping
        String text = null;
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            try {
            	byte[] ary = messageText.getBytes();
                out.write(ary,0,ary.length);
            } finally {
                out.close();
            }
            text = out.toString("utf-8");
        } catch (IOException e) {
            errorHandler.error("[COULDNT_MAP_BEAN_TO_TEXT]", e, conf, sender, target);
            return false;
        }

        // Build the message
        MimeMessage msg = new MimeMessage(session);
        try {
           /* if (emailSubject.length() > 0)
            {
           //     msg.setSubject(emailSubject+" "+subject);
                msg.setSubject(subject+" "+emailSubject);
            }
            else
            {*/
            	msg.setSubject(subject);
            //}
            if (emailFrom != null)
                msg.setFrom(emailFrom);
            if (emailReplyTo != null)
                msg.setReplyTo(emailReplyTo);
            
//            if (emailTo != null)
                msg.setRecipients(Message.RecipientType.TO, getAddressListFromValue(recipient,conf,sender,target));
            
            if (emailCc != null)
                msg.setRecipients(Message.RecipientType.CC, emailCc);
            /*
            if (emailCharset.length() > 0)
                msg.setText(text, emailCharset);
            else
                msg.setText(text);
            */
            
//          Create Multipart for message content with subtype "alternative".
            Multipart multipart = new MimeMultipart();

//          Add iCalendar attachment to Multipart.
            MimeBodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setDataHandler(new DataHandler(new StringDataSource(calendarEvent, "text/calendar", "meetingRequest" )));
            //messageBodyPart.setDataHandler()
            multipart.addBodyPart(messageBodyPart);
            
            // Add message body to Multipart.
            messageBodyPart = new MimeBodyPart();
            messageBodyPart.setDataHandler(new DataHandler(new StringDataSource(text, "text/html", "eventDescription")));
            messageBodyPart.setContentID("<eventDescriptionHTML>");
            multipart.addBodyPart(messageBodyPart);
            
            

            
            msg.setContent(multipart);
//            msg.addHeader("Sender",organizer);
            msg.setSentDate(new Date());
        } catch (MessagingException e) {
            errorHandler.error("[COULDNT_BUILD_MESSAGE]", e, conf, sender, target);
            return false;
        }

        synchronized (this) {
            if (!transport.isConnected())
                // Connect to the host
                try {
                    transport.connect(host,user,password);
                } catch (MessagingException e) {
                    errorHandler.error("[COULDNT_CONNECT]", e, host, conf, sender, target);
                    return false;
                }

            // Send the message
            try {
                Address addressList[] = msg.getAllRecipients();
                if (addressList != null && addressList.length > 0)
                    transport.sendMessage(msg, addressList);
                else {
                    errorHandler.warning("[NO_RECIPIENTS]");
                    return false;
                }
            } catch (MessagingException e) {
                boolean retryFlag = true;

                // Log the error
                synchronized (this) {
                    errorHandler.error("[COULDNT_SEND_MESSAGE]", e, conf, sender,target);
                    if (e instanceof SendFailedException) {
                        SendFailedException sfe = (SendFailedException) e;
                        Address[] invalid = sfe.getInvalidAddresses();
                        addressWarning("[INVALID_ADDRESSES]", invalid);
                        Address[] validUnsent = sfe.getValidUnsentAddresses();
                        addressWarning("[VALID_UNSENT_ADDRESSES]", validUnsent);
                        Address[] validSent = sfe.getValidSentAddresses();
                        addressWarning("[VALID_SENT_ADDRESSES]", validSent);
                        if (invalid != null && invalid.length > 0)
                            retryFlag = false; // there is no point to retry
                    }
                }

                // Retry with a new transport object. Don't log any new errors.
                if (retryFlag) {
                    Transport newTransport = null;
                    try {
                        // Try to resend the message using a new connection
                        newTransport = session.getTransport(protocol);
                        newTransport.connect(host,user,password);
                        newTransport.sendMessage(msg, msg.getAllRecipients());

                        // Try to close the old transport
                        try {
                            transport.close();
                        } catch (Exception t) {
                        }

                        // The new transport object worked
                        transport = newTransport;
                        return true;
                    } catch (Exception t) {
                        // The new transport failed too. Try to close it
                        if (newTransport != null)
                            try {
                                newTransport.close();
                            } catch (Exception t2) {
                            }
                    }
                }
                return false;
            }
        }
        return true;
    }

    private class StringDataSource implements DataSource {
	    private String contents ;
	    private String mimetype ;
	    private String name ;


	    public StringDataSource( String contents
	                           , String mimetype
	                           , String name
	                           ) {
	        this.contents = contents ;
	        this.mimetype = mimetype ;
	        this.name = name ;
	    }
	    
	    public String getContentType() {
	        return( mimetype ) ;
	    }
	    
	    public String getName() {
	        return( name ) ;
	    }

	    public InputStream getInputStream() {
	        return( new StringBufferInputStream( contents ) ) ;
	    }
	    
	    public OutputStream getOutputStream() {
	        throw new IllegalAccessError( "This datasource cannot be written to" ) ;
	    }
	}

	public ConferenceInfo getConferenceInfo() {
		return conferenceInfo;
	}
	public void setConferenceInfo(ConferenceInfo conferenceInfo) {
		this.conferenceInfo = conferenceInfo;
	}
}



