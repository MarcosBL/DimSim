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

package com.dimdim.conference.ui.publisher.client;

//import com.dimdim.conference.ui.common.client.layout.DefaultCommonDialog;
import com.dimdim.conference.ui.common.client.util.DebugPanel;
//import com.dimdim.conference.ui.json.client.JSONurlReader;
import com.dimdim.conference.ui.json.client.UIResourceObject;
import com.dimdim.conference.ui.model.client.ClientModel;
//import com.dimdim.conference.ui.model.client.ConferenceGlobals;
import com.google.gwt.user.client.Element;
//import com.google.gwt.user.client.ResponseTextHandler;
import com.google.gwt.user.client.Timer;
//import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.PopupPanel;

/**
 *	The element passed to these methods must be the element of the OBJECT
 *	node.
 *
 *	dimdimHasFinished return codes:
 *
 *	0 - control still active.
 *	1 - user pressed ok.
 *	2 - user pressed cancel.
 *
 */
public class WaitAndContinueWaiter {
	
	protected	PopupPanel	waitPopup;
//	protected	PublisherInterfaceManager	pim;
	
//	TimeAndFocusSensitiveModalDialog tfd = new TimeAndFocusSensitiveModalDialog();
	
//	public WaitAndContinueWaiter()
//	{
//		this(null);
//	}
	public WaitAndContinueWaiter(PublisherInterfaceManager pim)
	{
//		this.pim = pim;
	}
	/*
	public  void	waitAndCotinueCreateAppShare(final WaitAndContinueUserListener listener,
			final Element elem, final WaitAndContinueData listenerData,
			final PublisherInterfaceManager pim, final boolean genericAppLaunch)
	{
		//Window.alert("inside waitAndCotinueCreateAppShare before creating a thread...");
		Timer t = new Timer()
		{
			private PublisherInterfaceManager	pim2 = pim;
			private WaitAndContinueData listenerData2 = new WaitAndContinueData(listenerData);
			String fileName = null;
			
			public	void	run()
			{
				//Window.alert("inside run method of run....");		
				int pubState = 0;
				try
				{
					if (genericAppLaunch)
					{
						if (elem == null)
						{
							pubState = getPublisherFinishStateLaunch();
						}
						else
						{
							pubState = getPublisherFinishStateLaunchFirefox(elem);
						}
					}
					else
					{
						if (elem == null)
						{
							//pubState = getPublisherFinishStateSelector();
						    	pubState = pim.isSharingActive();
							//Window.alert("pubState:"+pubState);
						}
						else
						{
							pubState = getPublisherFinishStateSelectorFirefox(elem);
						}
					}
				}
				catch(Exception e)
				{
					Window.alert(e.getMessage());
					pubState  = 2;
				}
				if (pubState != 0 && pubState != 1)
				{
					//	Popup has finished.
					this.cancel();
					pim2.setAppSelectorActive(false);
					DebugPanel.getDebugPanel().addDebugMessage("Exiting create app share wait with status:"+pubState);
					if (listener != null && listenerData2 != null)
					{
						int appHandle = 0;
						String appName = null;
						listenerData2.setBool1(false);
						listenerData2.setInt1(appHandle);
						listenerData2.setStr9(appName);
						if (pubState == 1)
						{
							//Window.alert("Return code(DimdimHasFinished):"+pubState+", window handle(DimdimGetResult):"+appHandle);
							try
							{
								if (elem == null)
								{
									appHandle = getAppHandleToShare();
									appName = getSharedResourceName(appHandle);
								}
								else
								{
									appHandle = getAppHandleToShareFirefox(elem);
									
									if (genericAppLaunch)
									{
										
										  fileName = listenerData.getStr9();
										  if ( fileName.lastIndexOf("\\") == -1)
										  {
											  fileName = fileName.substring(1, fileName.length()-1);
											  appName = fileName;// getSharedResourceNameFirefox(elem,appHandle);; 			
										  }
										  else	  
										  { appName = fileName.substring(fileName.lastIndexOf("\\")+1,fileName.lastIndexOf("."));}
										
									} else
									{
										appName = getSharedResourceNameFirefox(elem,appHandle);
									}
									
								}
							}
							catch(Exception e)
							{
								//Window.alert(e.getMessage());
							}
							DebugPanel.getDebugPanel().addDebugMessage("Received app handle:"+appHandle);
							DebugPanel.getDebugPanel().addDebugMessage("Received app name:"+appName);
							if (appHandle > 0)
							{
								listenerData2.setBool1(true);
								listenerData2.setInt1(appHandle);
								listenerData2.setStr9(appName);
							}
	//						rdh.continueAppShareCreation(appHandle,streamId,resourceId,resourceName,isImport);
						}
						else
						{
							listenerData2.setInt1(pubState);
						}
						listener.continueWork(listenerData2);
					}
					closePopup();
				}
			}
		};
		t.scheduleRepeating(500);
	}
	public  void	waitAndCotinuePPTUpload(final WaitAndContinueUserListener listener,
			final Element elem, final WaitAndContinueData listenerData,
			final PublisherInterfaceManager pim)
	{
		Timer t = new Timer()
		{
			private PublisherInterfaceManager	pim2 = pim;
			private WaitAndContinueData listenerData2 = new WaitAndContinueData(listenerData);
			public	void	run()
			{
				int pubState = 0;
				try
				{
					if (elem == null)
					{
						pubState = getPublisherFinishStatePPT();
					}
					else
					{
						pubState = getPublisherFinishStatePPTFirefox(elem);
					}
				}
				catch(Exception e)
				{
					//Window.alert(e.getMessage());
					pubState = 2;
				}
				if (pubState != 0)
				{
					//	Upload has finished or been cancelled.
					this.cancel();
					pim2.setPptUploadActive(false);
					DebugPanel.getDebugPanel().addDebugMessage("PPT Upload finished with status:"+pubState);
//					Window.alert("PPT Upload finished with status:"+pubState);
					if (listener != null)
					{
						if (pubState == 1)
						{
							listenerData2.setBool1(true);
						}
						else
						{
							listenerData2.setBool1(false);
						}
						listenerData2.setInt1(pubState);
						listener.continueWork(listenerData2);
//						rdh.continueCreateNewPresentation(presentationId,resourceId,resourceName,isImport);
					}
					closePopup();
				}
			}
		};
		t.scheduleRepeating(500);
	}
	
	public  void	waitAndCotinuePPTXUpload(final WaitAndContinueUserListener listener,
		final Element elem, final WaitAndContinueData listenerData,
		final PublisherInterfaceManager pim)
{
	Timer t = new Timer()
	{
		private PublisherInterfaceManager	pim2 = pim;
		private WaitAndContinueData listenerData2 = new WaitAndContinueData(listenerData);
		public	void	run()
		{
		    ConversionProgressCheckResponse response = null;
			try
			{
				if (elem == null)
				{
				    response = pim.isPptUploadActive();
				}
				else
				{
				    //pubState = getPublisherFinishStatePPTFirefox(elem);
				    
				}
				//Window.alert("inside listener... getTotalSlides = "+ response.toString());
			}
			catch(Exception e)
			{
				Window.alert(e.getMessage());
			    
			}
			if(response.getResult() != 0){
        			if (response.isActionComplete() || response.isActionCancelled())
        			{
        				//	Upload has finished or been cancelled.
        			    	//Window.alert("inside action complete or action canceled response = "+response.toString());
        				this.cancel();
        				pim2.setPptUploadActive(false);
        				DebugPanel.getDebugPanel().addDebugMessage("PPT Upload finished with status:"+response.toString());
        //				Window.alert("PPT Upload finished with status:"+pubState);
        				if (listener != null)
        				{
        				    //Window.alert("response.isActionComplete() = "+response.isActionComplete());
        					if (response.isActionComplete())
        					{
        						listenerData2.setBool1(true);
        					}
        					else
        					{
        						listenerData2.setBool1(false);
        					}
        					
        					listenerData2.setInt1(response.getResult());
        					executeCommand(listener, listenerData, response);
        					
        //					rdh.continueCreateNewPresentation(presentationId,resourceId,resourceName,isImport);
        				}
        				closePopup();
        			}
			}
		}
	};
	t.scheduleRepeating(500);
}
	*/
	/*
	protected	void	executeCommand(final WaitAndContinueUserListener listener, final WaitAndContinueData listenerData, final ConversionProgressCheckResponse response)
	{
		//this handler is used so that the console side change are made only after server side is done
		ResponseTextHandler respHandler = new ResponseTextHandler(){

			public void onCompletion(String responseText) {
			    Window.alert("on completion of ResponseTextHandler.. pptID="+listenerData.getStr3());
			    listener.continueWorkAfterDMS(listenerData.getStr3());
				
			}
			
		};
		
		String url = ConferenceGlobals.webappRoot+"DMSPresentaion.action?pptName="+"name.ppt"+
		"&pptID="+listenerData.getStr3()+"&noOfSlides="+response.getTotalSlides()+
		"&meetingKey="+listenerData.getStr5();
		JSONurlReader reader = new JSONurlReader(url, ConferenceGlobals.getConferenceKey(), respHandler);
		
		reader.doReadURL();
	}
	*/
	public  void	waitForScreenShareCompletion(final ApplicationShareInterface listener,
			final Element elem, final WaitAndContinueData listenerData,
			final PublisherInterfaceManager pim)//, final ApplicationWindowsListPanel appPanel)
	{
	    //Window.alert("inside .... wait for screen share completion listenerData ="+listenerData);
	    //tfd.setCaption("Wait for Share to Start...", false);
	    //tfd.show();
	    
		Timer t = new Timer()
		{
			private PublisherInterfaceManager	pim2 = pim;
			private WaitAndContinueData listenerData2 = new WaitAndContinueData(listenerData);
			boolean appPanelRefreshed = false;
			int countZero = 0;
			
			public	void	run()
			{
				int pubState = 0;
				try
				{
					pubState = PublisherInterfaceManager.getManager().isSharingActive();
					//Window.alert("screen share result = "+pubState);
				}
				catch(Exception e)
				{
					//Window.alert(e.getMessage());
					pubState = 2;
				}
				if(pubState == 0){
				    //tfd.show();
				    countZero += 1;
				    if(countZero > 20 ){
					pubState = 22;
				    }
				}
				
				if(pubState == 1 && listenerData.getInt1() >= 0 && !appPanelRefreshed){
				    //Window.alert("got 1 as pub state");
				    //tfd1.hide();
					if (listener != null)
					{
						DebugPanel.getDebugPanel().addDebugMessage("application share result from pub = "+pubState);
//					    UIResourceObject res = ClientModel.getClientModel().
//						getResourceModel().findResourceObjectByMediaID(String.valueOf(listenerData.getInt1()) );
					    
//					    if(null != res)
//					    {
//					    	listenerData2.setStr7(res.getResourceId());
//					    }
					    listenerData2.setBool1(true);
					    listenerData2.setAppHandle(listenerData.getAppHandle());
					    listenerData2.setInt1(listenerData.getInt1());
					    //listener.continueWork(listenerData2);
					    //ConferenceGlobals.setCurrentSharedResource(null);
					    //ResourceModel rm = ClientModel.getClientModel().getResourceModel();
					    //rm.setCurrentResourceUnselected();
					    //Window.alert("app share started....handle = "+listenerData.getInt1());
					    listener.start(listenerData2);
					    appPanelRefreshed = true;
					}
//					appPanel.closePopup();
				}
				if (pubState != 0 && pubState != 1)
				{
					//Window.alert("Exited the screen share wait with return code:"+pubState);
					//Window.alert("delete resource in wait and continue waiter sshare..");
					//Window.alert("inside .... app handle = "+appPanel.getSharedAppHandle());
					this.cancel();
					//tfd1.hide();
					pim2.setDesktopSharingActive(false);
//					if(pubState != 2){
//					    DefaultCommonDialog.showMessage(
//						ConferenceGlobals.getDisplayString("publisher_error.header","Application Share Error"), 
//						ConferenceGlobals.getDisplayString("publisher_error."+pubState,"Unknown Error"));
//					}
					
					if (listener != null )
					{
					    UIResourceObject res = null;//ClientModel.getClientModel().
//						getResourceModel().findResourceObjectByMediaID(String.valueOf(appPanel.getSharedAppHandle() ) );
//					    //try to get resource from resource model
					    if(null == res )
					    {
							//Window.alert("resource from app panel = null");
							res = ClientModel.getClientModel().
							getResourceModel().findResourceObject(listenerData.getResourceId());
					    }
					    if(null != res)
					    {
					    	listenerData2.setCode("SHARE_APPLICATION");
							listenerData2.setInt1(listenerData.getInt1());
							if(null != res)
							{
							    listenerData2.setResourceId(res.getResourceId());
							}
//							listener.stop(listenerData2);
					    }
						
						if(pubState != 2)
						{
							listenerData2.setInt1(pubState);
							listener.error(listenerData2);
						}
						else
						{
							listener.stop(listenerData2);
						}
						
					//	ResourceModel rm = ClientModel.getClientModel().getResourceModel();
					//	rm.setCurrentResourceUnselected();
					}
//					closePopup();
//					appPanel.closePopup();
				}
			}
		};
		t.scheduleRepeating(1000);
	}
//	protected	final	void	closePopup()
//	{
//		if (waitPopup != null)
//		{
//			waitPopup.hide();
//		}
//	}
	/**
		return elem.DimdimHasFinished();
	 * 
	 * @return
	 */
//	private native int getPublisherFinishStateSelector()/*-{
//		return $wnd.dimdimPublisherControl1.DimdimHasFinishedSelector();
//	}-*/;
//	private native int getPublisherFinishStateLaunch()/*-{
//		return $wnd.dimdimPublisherControl1.DimdimHasFinishedLaunch();
//	}-*/;
//	private native int getPublisherFinishStateSharing()/*-{
//		return $wnd.dimdimPublisherControl1.DimdimHasFinishedSharing();
//	}-*/;
//	private native int getPublisherFinishStatePPT()/*-{
//		return $wnd.dimdimPublisherControl1.DimdimHasFinishedPPT();
//	}-*/;
	
//	private native int getAppHandleToShare()/*-{
//		return $wnd.dimdimPublisherControl1.DimdimGetResult;
//	}-*/;
//	private native String getSharedResourceName(int appHandle)/*-{
//		return $wnd.dimdimPublisherControl1.GetSharedResourceName(0);
//	}-*/;
//	private native int getPublisherFinishStateSelectorFirefox(Element elem) /*-{
//		return	elem.DimdimHasFinishedSelector();
//    }-*/;
//	private native int getPublisherFinishStateLaunchFirefox(Element elem) /*-{
//		return	elem.DimdimHasFinishedLaunch();
//	}-*/;
//	private native int getPublisherFinishStateSharingFirefox(Element elem) /*-{
//		return	elem.DimdimHasFinishedSharing();
//	}-*/;
//	private native int getPublisherFinishStatePPTFirefox(Element elem) /*-{
//		return	elem.DimdimHasFinishedPPT();
//	}-*/;
	
//	private native int getAppHandleToShareFirefox(Element elem)/*-{
//		return elem.DimdimGetResult();
//	}-*/;
//	private native String getSharedResourceNameFirefox(Element elem,int appHandle)/*-{
//	return elem.GetSharedResourceName(0);
//	}-*/;

}
