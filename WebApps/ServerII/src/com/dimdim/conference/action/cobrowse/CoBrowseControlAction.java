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
 
package com.dimdim.conference.action.cobrowse;

import com.dimdim.conference.ConferenceConstants;
import com.dimdim.conference.action.ConferenceAction;
import com.dimdim.conference.model.IConference;
import com.dimdim.conference.model.IConferenceParticipant;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 *
 */
public class CoBrowseControlAction		extends	ConferenceAction
{
	protected	String	cmd;
	protected	String	ri;		//	resourceId
	protected	String	si;		//	streamId
	protected	String	horScroll;
	protected	String	verScroll;
	protected	String	newName;
	
	
	public	CoBrowseControlAction()
	{
	}
	public	String	doWork()	throws	Exception
	{
		String	ret = SUCCESS;
		IConference conf = this.userSession.getConference();
		IConferenceParticipant user = this.userSession.getUser();
		
		try
		{
			System.out.println("Cobrowser control event: -"+cmd+"--"+si+"--"+ri);
			conf.getResourceManager().handleCobrowseControlMessage(user, cmd, ri, horScroll, verScroll, newName);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			ret = ERROR;
		}
		return	ret;
	}
	public String getCmd()
	{
		return cmd;
	}
	public void setCmd(String cmd)
	{
		this.cmd = cmd;
	}
	public String getRi()
	{
		return ri;
	}
	public void setRi(String ri)
	{
		this.ri = ri;
	}
	public String getSi()
	{
		return si;
	}
	public void setSi(String si)
	{
		this.si = si;
	}
	public String getHorScroll() {
		return horScroll;
	}
	public void setHorScroll(String horScroll) {
		this.horScroll = horScroll;
	}
	public String getVerScroll() {
		return verScroll;
	}
	public void setVerScroll(String verScroll) {
		this.verScroll = verScroll;
	}
	public String getNewName() {
		return newName;
	}
	public void setNewName(String newName) {
		this.newName = newName;
	}
}
