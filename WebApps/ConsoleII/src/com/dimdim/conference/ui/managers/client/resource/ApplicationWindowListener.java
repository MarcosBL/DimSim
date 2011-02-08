/*
 **************************************************************************
 *                                                                        *
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
package com.dimdim.conference.ui.managers.client.resource;

import com.dimdim.conference.ui.publisher.client.ApplicationWindowItem;
import com.dimdim.conference.ui.publisher.client.ApplicationWindowsListPanelListener;

/**
 * This class implements action that needs to be taken when a user clicks
 * on a row in the application table
 * @author Dilip Kumar
 * @email dilip@dimdim.com
 * 
 */
public class ApplicationWindowListener implements	
ApplicationWindowsListPanelListener
{

    ResourceSharingController resourceSharingController = null;

//    private ApplicationWindowListener(ResourceSharingController resShareController)
//    {
//    	this.resourceSharingController = resShareController;
//    }

    public void applicationWindowSelected(ApplicationWindowItem appItem)
    {

	//toggling the state of appItem share status
	//Window.alert("inside listener clicked on appItem = "+appItem.getAppName()+appItem.getAppHandle() +" appItem.isShared() = "+appItem.isShared());
//	if(appItem.isShared())
//	{
//	    //Window.alert("this is shared so calling to stop share..");
//	    resourceSharingController.stopDTPAppShare();
//	    appItem.setShared(false);
//	}
//	else{
//	    //Window.alert("this is not shared so calling to share..");
//	    //resourceSharingController.continueWorkAfterAppSelection(appItem.getAppHandle(),	appItem.getAppName());
//	    
//	    //only if not desktop
//	    if(appItem.getAppHandle() > 0)
//	    {
//		WaitAndContinueData listenerData = getWaitAndContinueData(appItem);
//		resourceSharingController.continueWork(listenerData);
//	    appItem.setShared(true);
//	    }else{
//		resourceSharingController.startShare(ResourceSharingController.DESKTOP, getDsktopResource());
//	    }
//	    UIResourceObject res = new UIResourceObject();
//	    res.setMediaId(String.valueOf(appItem.getAppHandle()));
//	    res.setResourceName(appItem.getAppName());
//	    res.setAppHandle(String.valueOf(appItem.getAppHandle()));
//	    resourceSharingController.startShare(ResourceSharingController.SHARE_APPLICATION, res, true);
//	}


    }
//    private UIResourceObject getDsktopResource()
//    {
//	return ResourceGlobals.getResourceGlobals().getDesktopResource();
//    }
}
