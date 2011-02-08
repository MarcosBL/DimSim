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
 *								                                          *
 * Copyright (c) 2006 Communiva Inc. All Rights Reserved.	              *
 *								                                          *
 *								                                          *
 * This code is licensed under the DimDim License                         *
 * For details please visit http://www.dimdim.com/license		          *
 *									                                      *
 **************************************************************************
 */

package com.dimdim.conference.ui.common.client;

import java.util.HashMap;

import com.dimdim.conference.ui.common.client.data.UIParams;
import com.dimdim.conference.ui.model.client.ConferenceGlobals;
import com.google.gwt.user.client.ui.Label;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 */
public class LayoutGlobals
{
	//	A global table for use by different components as required.
	
	protected	static	final	HashMap	table = new HashMap();
	
	protected	static	Label	workspaceHeaderLabel;
	
	public static Label getWorkspaceHeaderLabel()
	{
		return workspaceHeaderLabel;
	}
	public static void setWorkspaceHeaderLabel(Label workspaceHeaderLabel)
	{
		LayoutGlobals.workspaceHeaderLabel = workspaceHeaderLabel;
	}
	
	public	HashMap	getTable()
	{
		return	LayoutGlobals.table;
	}
	
	public	static	int	getCommonDialogBoxPopupPositionX()
	{
		return	161;
	}
	public	static	int	getCommonDialogBoxPopupPositionY()
	{
		return	155;
	}
	public	static	String	getSelectedListEntryPanelStyleName(String entryType)
	{
		return	"";
	}
	
	/**
	 * There are two available slots for the av floats. This applies only on the
	 * attendee side at present.
	 */
	protected	static	boolean		avFloatSlot1Free = true;
	protected	static	int			avFloatSlot1Top = 0;
	protected	static	int			avFloatSlot1Left = 0;
	protected	static	boolean		avFloatSlot2Free = true;
	protected	static	int			avFloatSlot2Top = 0;
	protected	static	int			avFloatSlot2Left = 0;
	protected	static	boolean		avFloatsLayoutVertical = true;
	
	public	static	boolean	isAVFloatsLayoutVertical()
	{
		return	LayoutGlobals.avFloatsLayoutVertical;
	}
	public	static	void	setAVFloatsLayout(boolean vertical)
	{
		LayoutGlobals.avFloatsLayoutVertical = vertical;
	}
	public	static	int		getAvailableAVFloatSlot()
	{
		int i = 1; 
		if (LayoutGlobals.avFloatSlot1Free)
		{
			LayoutGlobals.avFloatSlot1Free = false;
		}
		else
		{
			LayoutGlobals.avFloatSlot2Free = false;
			i = 2;
		}
		return	i;
	}
	public	static	void	setAVFloatSlotAvailable(int i)
	{
		if (i == 1)
		{
			LayoutGlobals.avFloatSlot1Free = true;
		}
		else
		{
			LayoutGlobals.avFloatSlot2Free = true;
		}
	}
	public	static	int		getAVFloatTop(int positionId)
	{
		int allowance = LayoutGlobals.getAVPlayerFloatPanelHeight("1");
		if (positionId == 2)
		{
			if (LayoutGlobals.avFloatsLayoutVertical)
			{
				if (ConferenceGlobals.isMeetingVideoChat())
				{
					allowance += (LayoutGlobals.getAVPlayerFloatPanelHeight("1")+2);
				}
				else
				{
					allowance += (LayoutGlobals.getAudioBroadcasterFloatPanelHeight()+2);
				}
			}
			else
			{
				//	Nothing the top position remains same. So no allowance change.
			}
		}
		int top = ConferenceGlobals.getContentHeight() - (allowance+2);
		int footerAllowance = UIParams.getUIParams().getBrowserParamIntValue("av_box_footer_allowance", 0);
		top = top - footerAllowance;
		
		return	top;
	}
	public	static	int		getAVFloatLeft(int positionId)
	{
		int left = 5;
		if (positionId != 1)
		{
			if (!LayoutGlobals.avFloatsLayoutVertical)
			{
				left += UIGlobals.getAudioBroadcasterWidth()+5;
			}
		}
		return	left;
	}
	public	static	int		getPrivateChatLeft(int chatIndex)
	{
		int	allowance = 0;
		if (!LayoutGlobals.avFloatsLayoutVertical)
		{
			if (!LayoutGlobals.avFloatSlot1Free && !LayoutGlobals.avFloatSlot2Free)
			{
				allowance = 250;
			}
		}
		return	allowance+(chatIndex*257);
	}
	public	static	int	getAVPlayerFloatPanelHeight(String sizeFactor)
	{
		return	UIGlobals.getAVBroadcasterHeight(sizeFactor)+24;
	}
	public	static	int	getAudioBroadcasterFloatPanelHeight()
	{
		return	UIGlobals.getAudioBroadcasterHeight()+24;
	}
	public	static	boolean	isWhiteboardSupported()
	{
		String s = UIGlobals.getStreamingUrlsTable().getWhiteboardRtmpUrl();
		return	(s != null && s.length() > 0);
	}
	public static int getAvFloatSlot1Left()
	{
		return avFloatSlot1Left;
	}
	public static void setAvFloatSlot1Left(int avFloatSlot1Left)
	{
		LayoutGlobals.avFloatSlot1Left = avFloatSlot1Left;
	}
	public static int getAvFloatSlot1Top()
	{
		return avFloatSlot1Top;
	}
	public static void setAvFloatSlot1Top(int avFloatSlot1Top)
	{
		LayoutGlobals.avFloatSlot1Top = avFloatSlot1Top;
	}
	public static int getAvFloatSlot2Left()
	{
		return avFloatSlot2Left;
	}
	public static void setAvFloatSlot2Left(int avFloatSlot2Left)
	{
		LayoutGlobals.avFloatSlot2Left = avFloatSlot2Left;
	}
	public static int getAvFloatSlot2Top()
	{
		return avFloatSlot2Top;
	}
	public static void setAvFloatSlot2Top(int avFloatSlot2Top)
	{
		LayoutGlobals.avFloatSlot2Top = avFloatSlot2Top;
	}
}
