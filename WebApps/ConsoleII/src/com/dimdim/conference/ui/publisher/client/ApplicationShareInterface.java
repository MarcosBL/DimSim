package com.dimdim.conference.ui.publisher.client;

import com.dimdim.conference.ui.json.client.UIResourceObject;


public interface ApplicationShareInterface
{
	
	public	void	error(WaitAndContinueData data);
	
    public	void	start(WaitAndContinueData data);
    
    public	void	stop(WaitAndContinueData data);
    
}