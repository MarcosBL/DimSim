package com.dimdim.conference.model;

import com.dimdim.conference.ConferenceConstants;
import com.dimdim.conference.application.core.ActiveConference;
import com.dimdim.messaging.IEvent;
import com.dimdim.messaging.IEventFilter;

public class ParticpantListFilter implements	IEventFilter{
	
	IConferenceParticipant user = null;
	ActiveConference conf = null;
	
	public	ParticpantListFilter(IConferenceParticipant user, ActiveConference conf)
	{
		this.user = user;
		this.conf = conf;
	}
	
	public	boolean	receiveEvent(IEvent event)
	{
		//System.out.println("%%%%%%%%%%%%%%%%5IN EVENT FILTER: event:"+event.toString());
		String featureId = event.getSource();
		String eventId = event.getType();
		if (featureId.equals(ConferenceConstants.FEATURE_ROSTER))
		{
			if(conf.getOrganizer().getId().equals(user.getId()) )
			{
				//System.out.println("IN EVENT FILTER (Since Organizer ):Allowing event: "+event.toString());
				return	true;
			}
			if(eventId.equals(ConferenceConstants.EVENT_PARTICIPANT_ROLE_CHANGE))
			{
				//System.out.println("IN EVENT FILTER:(Since this is of type role change )Allowing event: "+event.toString());
				return	true;
			}
			if(null != event.getEventData())
			{
				String eventCreatorId = event.getEventData().getCreatorId();
				if(eventCreatorId.equals(user.getId() ) || eventCreatorId.equals(conf.getOrganizer().getId() ))
				{
					//System.out.println("IN EVENT FILTER:(Since this is from organizer or current user)Allowing event: "+event.toString());
					return	true;
				}
			}
			if(eventId.equals(ConferenceConstants.EVENT_PARTICIPANTS_LIST))
			{
				//System.out.println("IN EVENT FILTER:Allowing event: (Since roster list event )"+event.toString());
				return	true;
			}
			
			//System.out.println("IN EVENT FILTER: Filtering out event:"+event.toString());
			return	false;
			
		}
		//System.out.println("IN EVENT FILTER:Allowing event: "+event.toString());
		return	true;
	}
}
