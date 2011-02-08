package com.dimdim.conference.model;

public interface ICobrowseManager {
	
	public	void	startCobrowse(IConferenceParticipant presenter,
			String resourceId);

	public	void	stopCobrowse(IConferenceParticipant presenter,
			String resourceId);

	public	void	navigate(IConferenceParticipant presenter,
			String resourceId);
	
	public	void	rename(IConferenceParticipant presenter,
			String resourceId, String newName);

	public	void	scroll(IConferenceParticipant presenter,
			String resourceId, String horScroll, String verScroll);
	
	public	void	lock(IConferenceParticipant presenter,
			IResourceObject ro);
	
	public	void	unlock(IConferenceParticipant presenter,
			IResourceObject ro);
}
