package com.dimdim.conference.action.roster;

import com.dimdim.conference.action.ConferenceAction;
import com.dimdim.conference.model.IConference;
import com.dimdim.conference.model.IConferenceParticipant;
import com.dimdim.util.misc.Base64;

public class SetNameAction extends	ConferenceAction{
protected	String	name;
	
	public	SetNameAction()
	{
	}
	
	public	String	doWork()	throws	Exception
	{
		String	ret = SUCCESS;
		IConference conf = this.userSession.getConference();
		IConferenceParticipant user = this.userSession.getUser();
		
		try
		{
			if (name != null)
			{
				String decodedName = new String(Base64.decode(name));
				conf.getRosterManager().getRosterObject().updateDisplayName(user.getId(), decodedName);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			ret = ERROR;
		}
		
		return	ret;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
