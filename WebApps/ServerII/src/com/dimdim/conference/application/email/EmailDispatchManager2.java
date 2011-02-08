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

package com.dimdim.conference.application.email;

import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;

import com.dimdim.conference.application.core.ActiveConference;
import com.dimdim.conference.model.IConferenceParticipant;
import com.dimdim.conference.model.InvitationEmail;
import com.dimdim.conference.model.JoinInvitationEmail;
import com.dimdim.conference.model.FeedbackEmail;
import	com.dimdim.conference.model.ConferenceInfo;
import com.dimdim.conference.ConferenceConsoleConstants;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * This object takes the responsibility of dispatching all the invitation
 * emails. It accepts the invitation object and the list of recipients.
 * It formats the invitation text for each recipient as required and sends
 * out the emails using the MailSender object.
 * 
 * The dispatcher's email auth credentials are read from a resource file.
 */
public class EmailDispatchManager2
{
	protected	static	EmailDispatchManager2	theManager	=	null;
	
	public	static	EmailDispatchManager2	getManager()
	{
		if (EmailDispatchManager2.theManager == null)
		{
			EmailDispatchManager2.createManager();
		}
		return	EmailDispatchManager2.theManager;
	}
	
	protected	static	synchronized	void	createManager()
	{
		if (EmailDispatchManager2.theManager == null)
		{
			try
			{
				EmailDispatchManager2.theManager = new EmailDispatchManager2();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	
	protected	String	emailServer;
	protected	String	emailUser;
	protected	String	emailPassword;
	protected	String	emailSender;
	
//	protected	EmailDispatchWorker		worker;
//	protected	Thread		workerThread;
	
	protected	EmailDispatchManager2()	throws	Exception
	{
//		this.worker = new EmailDispatchWorker(this.emailServer,
//				this.emailUser, this.emailPassword, this.emailSender);
	}
	public	void	dispatch(InvitationEmail jie,
			ActiveConference conf, IConferenceParticipant sender, Locale locale, String userType)
	{
		try
		{
//			ResourceBundle erb = ResourceBundle.getBundle("resources.DimDimConference");
			ResourceBundle erb = ConferenceConsoleConstants.getDimdimConferenceProperties();
			
			this.emailServer = erb.getString("email.server");
			this.emailUser = erb.getString("email.user");
			this.emailPassword = erb.getString("email.password");
			this.emailSender = erb.getString("email.sender");
			
//			System.out.println("emailServer:"+emailServer);
//			System.out.println("emailUser:"+emailUser);
//			System.out.println("emailPassword:"+emailPassword);
//			System.out.println("emailSender:"+emailSender);
			
			EmailDispatchWorker2 worker = new EmailDispatchWorker2(this.emailServer,
				this.emailUser, this.emailPassword, this.emailSender, jie, conf, sender, locale, userType);
			Thread t = new Thread(worker);
			t.start();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		/*
		System.out.println("Dispatching Email:"+jie.getSubject());
		this.worker.addEmail(jie);
		if (workerThread == null || !workerThread.isAlive())
		{
			workerThread = new Thread(this.worker);
			workerThread.start();
		}
		*/
	}
	public	void	dispatch(FeedbackEmail fe,
			ActiveConference conf, IConferenceParticipant sender,Locale locale, String userType)
	{
		try
		{
			ResourceBundle erb = ConferenceConsoleConstants.getDimdimConferenceProperties();
			
			this.emailServer = erb.getString("email.server");
			this.emailUser = erb.getString("email.user");
			this.emailPassword = erb.getString("email.password");
			//this.emailSender = erb.getString("email.sender");
			this.emailSender = fe.getSender();
			System.out.println("emailServer:"+emailServer);
			System.out.println("emailUser:"+emailUser);
			System.out.println("emailPassword:"+emailPassword);
			System.out.println("emailSender:"+emailSender);
			
			EmailDispatchWorker2 worker = new EmailDispatchWorker2(this.emailServer,
				this.emailUser, this.emailPassword, this.emailSender, fe, conf, sender, locale, userType);
			Thread t = new Thread(worker);
			t.start();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	/*
	public	static	void	main(String[] args)
	{
    	if(args.length < 3)
    	{
    		System.err.println("java MailSender to(comma seperated emails) <conference name> message");
    		return;
    	}
    	
//    	String from = args[0]; //"invitations@communiva.com";
    	String to = args[0]; //"saurav.mohapatra@communiva.com, mohaps@gmail.com, prakash.khot@gmail.com";
//    	String server = args[2]; //"smtp.atlarge.net";
//    	String user = args[3]; //"invitation.COMMUNIVA";
//    	String pass = args[4];
    	String conferenceName = args[1];
    	String message = args[2];
		
    	int count = 1;
    	if (args.length > 3)
    	{
    		count = Integer.parseInt(args[3]);
    	}
    	ConferenceInfo ci = new ConferenceInfo(conferenceName, "testKey", 4, 4,
    			new Date(),(new Date()).toString(),"Jayant Pandit","Jayant.Pandit@communiva.com");
    	
    	JoinInvitationEmail jie = new JoinInvitationEmail(ci,to,message);
    	
    	for (int i=0; i<count; i++)
    	{
    		EmailDispatchManager2.getManager().dispatch(jie);
	    	try { Thread.sleep(30000); } catch(Exception e) { }
    	}
	}
	*/
}
