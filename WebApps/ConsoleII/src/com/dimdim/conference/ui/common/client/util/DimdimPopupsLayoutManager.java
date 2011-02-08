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
 * Part of the DimDim V 4.1 Codebase (http://www.dimdim.com)	          *
 *                                                                        *
 * Copyright (c) 2008 Communiva Inc. All Rights Reserved.                 *
 *                                                                        *
 *                                                                        *
 * This code is licensed under the DimDim License                         *
 * For details please visit http://www.dimdim.com/license                 *
 *                                                                        *
 **************************************************************************
 */

package com.dimdim.conference.ui.common.client.util;

import java.util.Vector;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.WindowResizeListener;
import com.dimdim.conference.ui.common.client.util.DebugPanel;
import com.dimdim.conference.ui.common.client.data.UIParams;
import com.dimdim.conference.ui.model.client.ClientModel;
import com.dimdim.conference.ui.json.client.UIRosterEntry;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */
public class DimdimPopupsLayoutManager
{
	protected	static	DimdimPopupsLayoutManager	theManager;
	
	public	static	DimdimPopupsLayoutManager	getManager(UIRosterEntry me)
	{
		if (DimdimPopupsLayoutManager.theManager == null)
		{
			DimdimPopupsLayoutManager.theManager = new DimdimPopupsLayoutManager(me);
		}
		return	DimdimPopupsLayoutManager.theManager;
	}
	
	//	There are maximum 7 slots at bottom left of the screen :
	//	--
	//	1  3
	//	0  2  4  5  6
	//	------------------------------------------
	//	The vector contains a simple Integer with values denoting --
	//	-1 not available, 0 - free, 1 - occupied
	//
	//	Height calculations:
	//
	//	Height taken up by the items panel: 176 px. (Each type panel 26px)
	//	Height taken up by the users panel: 26*(number of user,max 5)+50.
	
	protected	int[]	availableSlots = new int[7];
	protected	int[]	leftPoints = new int[7];
	protected	int[]	topPoints = new int[7];
	protected	int[]	widths = new int[7];
	protected	int[]	heights = new int[7];
	
	protected	int		lastKnownWidth = 0;
	protected	int		lastKnownHeight = 0;
	
	protected	int		itemsBoxHeight = 176;
	protected	int		usersBoxHeight = 75;
	
	protected	UIRosterEntry	me;
	protected	DimdimPopup	myBroadcaster;
	protected	Vector		videoPlayers;
	protected	Vector		chatBoxes;
	protected	int 		footerAllowance;
	
	private	PopupsResizeListener	resizeListener;
	
	public	DimdimPopupsLayoutManager(UIRosterEntry me)
	{
		this.me = me;
		this.initializeSlots();
		videoPlayers = new Vector();
		chatBoxes = new Vector();
		resizeListener = new PopupsResizeListener();
		Window.addWindowResizeListener(resizeListener);
	}
	private	void	initializeSlots()
	{
		availableSlots[0] = 0;
		availableSlots[1] = 0;
		availableSlots[2] = 0;
		availableSlots[3] = 0;
		availableSlots[4] = 0;
		availableSlots[5] = 0;
		availableSlots[6] = 0;
		footerAllowance = UIParams.getUIParams().getBrowserParamIntValue("av_box_footer_allowance", 0);
	}
	public	void	doResize(int width, int height)
	{
		this.lastKnownWidth = width;
		this.lastKnownHeight = height;
		this.repositionPopups();
	}
	public	void	repositionPopups()
	{
		if (this.lastKnownWidth == 0)
		{
			this.lastKnownWidth = Window.getClientWidth();
		}
		if (this.lastKnownHeight == 0)
		{
			this.lastKnownHeight = Window.getClientHeight();
		}
		//	Setup the available slots arrangement.
		this.analyzeScreen();
		//	Now move the popups around.
		this.rearrangePopups();
	}
	private	void	analyzeScreen()
	{
		this.initializeSlots();
		
		int u = ClientModel.getClientModel().getRosterModel().getNumberOfParticipants();
		if (u > 5)
		{
			u = 5;
		}
		this.usersBoxHeight = 26*u+50;
		
		int heightTaken = this.itemsBoxHeight + this.footerAllowance + 30;
		if (me.isActivePresenter())
		{
			heightTaken += usersBoxHeight;
		}
		if ((this.lastKnownHeight-heightTaken) < 500)
		{
			this.availableSlots[1] = -1;
			this.availableSlots[3] = -1;
		}
//		this.leftPoints[0] = 5;
//		this.leftPoints[1] = 5;
//		this.leftPoints[2] = 5+UIGlobals.getAVBroadcasterWidth("1");
//		this.leftPoints[3] = 5+UIGlobals.getAVBroadcasterWidth("1");
//		this.leftPoints[4] = 5+UIGlobals.getAVBroadcasterWidth("1")+248;
//		this.leftPoints[5] = 5+UIGlobals.getAVBroadcasterWidth("1")+248+248;
//		this.leftPoints[6] = 5+UIGlobals.getAVBroadcasterWidth("1")+248+248+248;
		
//		this.topPoints[0] = this.lastKnownHeight - (5+UIGlobals.getAVBroadcasterHeight("1"));
//		this.topPoints[1] = this.lastKnownHeight - (5+UIGlobals.getAVBroadcasterHeight("1")+5+UIGlobals.getAVBroadcasterHeight("1"));
//		this.topPoints[2] = this.lastKnownHeight - (5+UIGlobals.getAVBroadcasterHeight("1"));
//		this.topPoints[3] = this.lastKnownHeight - (5+UIGlobals.getAVBroadcasterHeight("1")+5+UIGlobals.getAVBroadcasterHeight("1"));
//		this.topPoints[4] = this.lastKnownHeight - 225;
//		this.topPoints[5] = this.lastKnownHeight - 225;
//		this.topPoints[6] = this.lastKnownHeight - 225;
	}
	public	void	dimdimPopupOpened(DimdimPopup popup)
	{
		int type = popup.getPopupType();
		if (type == DimdimPopup.AudioBroadcaster || type == DimdimPopup.VideoBroadcaster)
		{
			this.myBroadcaster = popup;
		}
		else if (type == DimdimPopup.VideoPlayer)
		{
			this.removePopup(this.videoPlayers, popup);
			this.videoPlayers.addElement(popup);
		}
		else
		{
			this.removePopup(this.chatBoxes, popup);
			this.chatBoxes.addElement(popup);
		}
		this.repositionPopups();
	}
	public	void	dimdimPopupClosed(DimdimPopup popup)
	{
		int type = popup.getPopupType();
		if (type == DimdimPopup.AudioBroadcaster || type == DimdimPopup.VideoBroadcaster)
		{
			this.myBroadcaster = null;
		}
		else if (type == DimdimPopup.VideoPlayer)
		{
			this.removePopup(this.videoPlayers, popup);
		}
		else
		{
			this.removePopup(this.chatBoxes, popup);
		}
		this.repositionPopups();
	}
	private	void	removePopup(Vector v, DimdimPopup p)
	{
		int size = v.size();
		for (int i=0; i<size; i++)
		{
			DimdimPopup pi = (DimdimPopup)v.elementAt(i);
			if (pi.getPopupId().equals(p.getPopupId()))
			{
				v.removeElementAt(i);
				break;
			}
		}
	}
	//	If my broadcaster is there it always goes to the bottom left.
	private	void	rearrangePopups()
	{
		int	position = 0;
		if (this.myBroadcaster != null)
		{
			position = this.getNextAvailableSlot(position);
			this.positionPopup(position, this.myBroadcaster);
		}
		int	size = this.videoPlayers.size();
		for (int i=0; i<size; i++)
		{
			position = this.getNextAvailableSlot(position);
			DimdimPopup popup = (DimdimPopup)this.videoPlayers.elementAt(i);
			this.positionPopup(position, popup);
		}
		size = this.chatBoxes.size();
		for (int i=0; i<size; i++)
		{
			position = this.getNextAvailableSlot(position);
			if (position == 1)
			{
				position = 2;
			}
			else if (position == 3)
			{
				position = 4;
			}
			DimdimPopup popup = (DimdimPopup)this.chatBoxes.elementAt(i);
			this.positionPopup(position, popup);
		}
	}
	
	private	int	getNextAvailableSlot(int index)
	{
		for (int i=index; i<7; i++)
		{
			if (this.availableSlots[i] == 0)
			{
				return	i;
			}
		}
		return	-1;
	}
	private	boolean	positionPopup(int position, DimdimPopup popup)
	{
		DebugPanel.getDebugPanel().addDebugMessage("Positioning popup at: "+position);
		if (position >= 0 && position < 7)
		{
			this.widths[position] = popup.getPopupWidth();
			this.heights[position] = popup.getPopupHeight();
			
			int left = this.getLeftValue(position,popup);
			int top = this.getTopValue(position,popup);
			DebugPanel.getDebugPanel().addDebugMessage("Positioning popup at:"+position+", left:"+left+", top:"+top);
			popup.setPopupPosition(left, top);
			this.availableSlots[position] = 1;
		}
		return	true;
	}
	private	int	getLeftValue(int position, DimdimPopup popup)
	{
		if (position == 0 || position == 1)
		{
			this.leftPoints[position] = 5;
		}
		else if (position == 2)
		{
			this.leftPoints[position] = this.leftPoints[0] + this.widths[0] + 8;
		}
		else if (position == 3)
		{
			this.leftPoints[position] = this.leftPoints[1] + this.widths[1] + 8;
		}
		else if (position == 4)
		{
			this.leftPoints[position] = this.leftPoints[2] + this.widths[2] + 8;
		}
		else if (position == 5)
		{
			this.leftPoints[position] = this.leftPoints[4] + this.widths[4] + 4;
		}
		else if (position == 6)
		{
			this.leftPoints[position] = this.leftPoints[5] + this.widths[5] + 4;
		}
		return	this.leftPoints[position];
	}
	private	int	getTopValue(int position, DimdimPopup popup)
	{
		if (position == 0 || position == 2 || position >= 3)
		{
			this.topPoints[position] = this.lastKnownHeight - (popup.getPopupHeight() + 2 + this.footerAllowance);
		}
		else if (position == 1)
		{
			this.topPoints[position] = this.topPoints[0] - (popup.getPopupHeight()+4);
		}
		else if (position == 3)
		{
			this.topPoints[position] = this.topPoints[2] - (popup.getPopupHeight()+4);
		}
		return	this.topPoints[position];
	}
	
	class	PopupsResizeListener	implements	WindowResizeListener
	{
		PopupsResizeListener()
		{
		}
		public	void	onWindowResized(int w, int h)
		{
			PopupsResizeCommand command = new PopupsResizeCommand(w,h);
			DeferredCommand.addCommand(command);
		}
	}
	
	class	PopupsResizeCommand	implements	Command
	{
		int	width;
		int	height;
		public	PopupsResizeCommand(int w, int h)
		{
			this.width = w;
			this.height = h;
		}
		public	void	execute()
		{
			doResize(width,height);
		}
	}
}
