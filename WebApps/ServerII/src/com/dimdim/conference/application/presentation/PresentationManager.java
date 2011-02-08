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

package com.dimdim.conference.application.presentation;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.SortedMap;
import java.util.TreeMap;

import com.dimdim.conference.application.presentation.dms.URLHelper;
import com.dimdim.conference.model.Presentation;
import com.dimdim.conference.ConferenceConsoleConstants;


/**
 * @author Saurav Mohapatra
 * @email Saurav.Mohapatra@communiva.com
 * 
 */
public class PresentationManager 
{
	public	static String	SystemDefaultOwnerId = "SYSTEM";
	public	static String	PreloadedDefaultOwnerId = "PRELOADED";
//	public	static String GlobalSampleConference = "global-meeting";
//	public	static String PreloadedItemsConference = "preloaded";
	protected static HashMap presentationManagers = new HashMap();
//	protected	static	String	presentationRootDirPath = null;
	
	private String meetingId = null;
	private SortedMap presentations = new TreeMap();
	
	public	synchronized	static	void	initializePresentationManager()
	{
//		if (PresentationManager.presentationRootDirPath == null)
//		{
//			PresentationManager.presentationManagers = new HashMap();
//			PresentationManager.presentationRootDirPath = ConferenceConsoleConstants.getPresentationStorageRoot();
//		}
	}
	/**
	 * The key is invalid if the directory points to a directory outside the dimdim
	 * installation and if it is one of the reserved key words.
	 * @param key
	 * @throws Exception
	 */
	public	static	void	validateKey(String key)	throws	Exception
	{
//		File rootDir = new File(PresentationManager.presentationRootDirPath,key);
//		if(!rootDir.getCanonicalPath().startsWith(PresentationManager.presentationRootDirPath)
//				|| rootDir.getCanonicalPath().equals(PresentationManager.presentationRootDirPath)
//				/*|| key.equals(PresentationManager.GlobalSampleConference)
//				|| key.equals(PresentationManager.PreloadedItemsConference)*/)
//		{
//			throw new	Exception("Invalid meeting key");
//		}
	}
	public static PresentationManager getPresentationManager(String meetingId)
	{
		PresentationManager pm = (PresentationManager)presentationManagers.get(meetingId);
		if (pm == null)
		{
			return	PresentationManager.addPresentationManager(meetingId, "IGNORE_DMS");
		}
		return	pm;
	}
	public synchronized	static PresentationManager addPresentationManager(String meetingId)
	{
		//return	PresentationManager.addPresentationManager(meetingId, "IGNORE_DMS");
		return	PresentationManager.addPresentationManager(meetingId, "");
	}
	public synchronized	static PresentationManager addPresentationManager(String meetingId,
			String rootDir)
	{
		synchronized(presentationManagers)
		{
			PresentationManager pmgr = (PresentationManager)presentationManagers.get(meetingId);
			if (pmgr == null)
			{
				pmgr = new PresentationManager(meetingId);
				if (!rootDir.equals("IGNORE_DMS"))
				{
					pmgr.init();
				}
				System.out.println("Presentation Manager For ["+meetingId+"] initialized with root dir : "+rootDir);
				
				presentationManagers.put(meetingId,pmgr);
			}
			return pmgr;
		}
	}
	private void init()
	{
		initPresentationTable();
	}
	private void initPresentationTable()
	{
		presentations.clear();
		this.loadPresentationTable();
	}
	private void loadPresentationTable()
	{
		ArrayList listPresentations = URLHelper.getPresentations(meetingId);
		Presentation presentation = null;
		
		for (Iterator iter = listPresentations.iterator(); iter.hasNext();)
		{
			presentation = (Presentation) iter.next();
			String presentationId = presentation.getPresentationId();
			if (!presentations.containsKey(presentationId))
			{
				//PresentationDetails details = loadPresentationDetails(presentationId);
				presentations.put(presentationId, presentation);
				presentation.setSealed(true);
				presentation.setMeetingId(meetingId);
				System.out.println("Presentation ID :: "+presentationId+" loaded as : "+presentation);
			}
		}
	}
	public static void removePresentationManager(String confKey, boolean deleteFiles)
	{
		synchronized(presentationManagers)
		{
			PresentationManager pmgr = (PresentationManager)presentationManagers.get(confKey);
			if(pmgr != null)
			{
				presentationManagers.remove(confKey);
				try
				{
					boolean result = URLHelper.closeMeetingOnDMS(confKey);
					System.out.println("inside DMS manager result on closing = "+result);
				}
				catch(Exception ex)
				{
					System.err.println("Directory for meeting id "+confKey+" could not be removed.Error = "+ex.getMessage());
					ex.printStackTrace();
				}
			}
		}
	}
	private PresentationManager(String meetingId)
	{
		this.meetingId=meetingId;
	}
	public Presentation[] getPresentations() 
	{
	//		ArrayList<Presentation> aList = new ArrayList<Presentation>();
		ArrayList aList = new ArrayList();
		Iterator iter = presentations.keySet().iterator();
		while(iter.hasNext())
		{
			String presentationId = (String)iter.next();
			Presentation presentation = getPresentation(presentationId);
			if(presentation.isSealed())
			{
				aList.add(presentation);
			}
		}
		
		Presentation[] ret = new Presentation[aList.size()];
		for(int i = 0; i < ret.length; i++)
		{
			ret[i] = (Presentation)aList.get(i);
		}
		return ret;
	}
	public Presentation getPresentation(String presentationId)
	{
		return (Presentation)presentations.get(presentationId);
	}
	public	void	sealDMSPresentation(String pptID, String pptName, int noOfSlides, int width, int height)
	{
		synchronized (presentationManagers)
		{
			System.out.println("adding ppt through ppt manager...");
			Presentation presentation = newPresentation(pptID, pptName);
			System.out.println("added ppt.. ");
			presentation.setSlideCount(noOfSlides);
			presentation.setWidth(width);
			presentation.setHeight(height);
			
			sealPresentation(presentation.getResourceId(), presentation.getPresentationId());
			System.out.println("sealed ppt.. ");
		}
	}
	public synchronized	Presentation newPresentation(String presentationId, String originalFileName)
	{
		deletePresentation(presentationId);
		Presentation presentation = new Presentation();
		presentation.setMeetingId(meetingId);
		presentation.setPresentationId(presentationId);
		presentation.setOriginalFileName(originalFileName);	
		presentation.resetDefaults();
		presentations.put(presentationId, presentation);
		return presentation;
	}
	public synchronized	void sealPresentation(String resourceId,String presentationId)
	{
		Presentation presentation = getPresentation(presentationId);
		if(presentation != null)
		{
			presentation.setResourceId(resourceId);
			presentation.setSealed(true);
		}
	}
	private void deletePresentation(String presentationId)
	{
		Presentation presentation = (Presentation)presentations.get(presentationId);
		if(presentation != null)
		{
			presentations.remove(presentationId);
		}
	}
}
