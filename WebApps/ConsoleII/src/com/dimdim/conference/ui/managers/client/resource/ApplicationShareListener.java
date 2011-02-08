package com.dimdim.conference.ui.managers.client.resource;

//import com.dimdim.conference.ui.common.client.UIConstants;
import com.dimdim.conference.ui.common.client.util.DebugPanel;
import com.dimdim.conference.ui.json.client.JSONurlReader;
import com.dimdim.conference.ui.json.client.UIResourceObject;
import com.dimdim.conference.ui.model.client.AnalyticsConstants;
import com.dimdim.conference.ui.model.client.ClientModel;
import com.dimdim.conference.ui.model.client.CommandURLFactory;
import com.dimdim.conference.ui.model.client.ConferenceGlobals;
import com.dimdim.conference.ui.publisher.client.ApplicationShareInterface;
import com.dimdim.conference.ui.publisher.client.WaitAndContinueData;
import com.google.gwt.user.client.ResponseTextHandler;
//import com.google.gwt.user.client.Window;

public class ApplicationShareListener implements ApplicationShareInterface, ResponseTextHandler
{
	CommandURLFactory urlFactory = new CommandURLFactory();
	protected	ResourceSharingController	controller;
	protected	boolean		shareRunning = false;
	
	public ApplicationShareListener(ResourceSharingController controller)
	{
		this.controller = controller;
	}
    
	public	void	error(WaitAndContinueData data)
	{
		DebugPanel.getDebugPanel().addDebugMessage("Error from the publisher interface::"+data.toString());
		int pubState = data.getInt1();
		String message = ConferenceGlobals.getDisplayString("publisher_error.header","Desktop Share Error")+" "+
			ConferenceGlobals.getDisplayString("publisher_error."+pubState,"Unknown Error");
		this.controller.pubResponseReceived(true, "Error", message);
		if (this.shareRunning)
		{
	        ConferenceGlobals.setCurrentSharedResource(null);
	        String stopShareUrl = urlFactory.getStopSharingURL(data.getResourceId(), data.getRtmpStreamId(), "x",data.getUserId(), data.getRecordingFlags());
			AnalyticsConstants.reportDesktopShareStopped();
	        executeCommand(stopShareUrl);
		}
	}
    public void start(WaitAndContinueData data)
    {
		DebugPanel.getDebugPanel().addDebugMessage("Raising share started event::"+data.toString());
		UIResourceObject res = ClientModel.getClientModel().
			getResourceModel().findResourceObject(data.getResourceId());
		if (res != null)
		{
			ConferenceGlobals.setCurrentSharedResource(res);
			String startShareUrl = urlFactory.getStartSharingURL(data.getResourceId(), data.getRtmpStreamId(), "x",data.getUserId(), data.getRecordingFlags());
			this.shareRunning = true;
			AnalyticsConstants.reportDesktopShareStarted();
			executeCommand(startShareUrl);
		}
		else
		{
			DebugPanel.getDebugPanel().addDebugMessage("Error in share start listener. No valid resource.");
		}
		this.controller.pubResponseReceived(false, "Desktop Sharing Started", "Desktop Sharing Started");
    }
    public void stop(WaitAndContinueData data)
    {
	//Window.alert("inside  stop method call of application share listener...data="+data);
    	if( null != data)
    	{
	        ConferenceGlobals.setCurrentSharedResource(null);
	        String stopShareUrl = urlFactory.getStopSharingURL(data.getResourceId(), data.getRtmpStreamId(), "x",data.getUserId(), data.getRecordingFlags());
			AnalyticsConstants.reportDesktopShareStopped();
	        executeCommand(stopShareUrl);
    	}
		else
		{
			DebugPanel.getDebugPanel().addDebugMessage("Error in share stop listener. No data available.");
		}
    	this.controller.pubResponseReceived(false, "Desktop Sharing Stopped", "Desktop Sharing Stopped");
    }
    protected	void	executeCommand(String url)
	{
		JSONurlReader reader = new JSONurlReader(url,this);
		reader.doReadURL();
	}
    public void onCompletion(String arg0)
    {
    }
}
