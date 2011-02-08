package com.dimdim.email.application;

import com.dimdim.email.model.EmailAttemptResult;
import com.dimdim.email.model.IConferenceParticipant;

public interface IErrorReporter {

	public	void	reportError(EmailAttemptResult result, IConferenceParticipant user);
	
}
