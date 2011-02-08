/**
 *	The error handler needs to report the errors back to the presenter
 *	attempting to dispatch the emails. Depending on the errors the
 *	presenter may be able to retry successfully.
 */

package com.dimdim.conference.application.email;
import java.util.Date;

import com.dimdim.conference.ConferenceConstants;
import com.dimdim.conference.application.core.ActiveConference;
import com.dimdim.conference.model.IConferenceParticipant;
import com.dimdim.conference.model.Event;
import com.dimdim.conference.model.EmailAttemptResult;

public class ErrorHandler
{
	public	ErrorHandler()
	{
		
	}
	public	void	fatalError(String a, Exception e, String b,
			ActiveConference conf, IConferenceParticipant sender, String target)
	{
		this.reportError(a,e,b,null,conf,sender,target);
	}
	public	void	fatalError(String a, Exception e, String b, String c,
			ActiveConference conf, IConferenceParticipant sender, String target)
	{
		this.reportError(a,e,b,c,conf,sender,target);
	}
	public	void	warning(String a, String[] ary)
	{
	}
	public	void	warning(String s)
	{
	}
	public	void	error(String a, Exception e,
			ActiveConference conf, IConferenceParticipant sender, String target)
	{
		this.reportError(a,e,null,null,conf,sender,target);
	}
	public	void	error(String a, Exception e, String b,
			ActiveConference conf, IConferenceParticipant sender, String target)
	{
		this.reportError(a,e,b,null,conf,sender,target);
	}
	private	void	reportError(String a, Exception e, String b, String c,
			ActiveConference conf, IConferenceParticipant sender, String target)
	{
		System.out.println("Email dispath errot:"+a+","+e+","+b+","+c+","+conf+","+sender);
		StringBuffer buf = new StringBuffer();
		if (a != null)
		{
			buf.append(a); buf.append(" ");
		}
		if (e != null)
		{
			buf.append(e.getMessage()); buf.append(" ");
			e.printStackTrace();
		}
		if (b != null)
		{
			buf.append(b); buf.append(" ");
		}
		if (c != null)
		{
			buf.append(c); buf.append(" ");
		}
		String message = buf.toString();
		if(null != sender)
		{
		    EmailAttemptResult ear = new EmailAttemptResult(sender.getId(),target,message);
		    this.sendEventToPresenter(conf,
				ConferenceConstants.EVENT_INVITATION_EMAIL_DISPATCH_ERROR,
				ear,sender);
		}
	}
	public	void	emailSent(ActiveConference conf, IConferenceParticipant sender)
	{
		this.sendEventToPresenter(conf,
				ConferenceConstants.EVENT_INVITATION_EMAIL_DISPATCH_SUCCESS,
				new EmailAttemptResult(),sender);
	}
	private	void	sendEventToPresenter(ActiveConference conf,
			String eventCode, EmailAttemptResult ear, IConferenceParticipant sender)
	{
		if (conf != null && sender != null)
		{
			Event event = new Event(ConferenceConstants.FEATURE_ROSTER,
					eventCode, new Date(), ConferenceConstants.RESPONSE_OK, ear);
			
			conf.getClientEventPublisher().dispatchEventTo(event,sender);
		}
		else
		{
			System.out.println("The conference or sender not available to send failure event:"+
				ear.toJson());
		}
	}
}
