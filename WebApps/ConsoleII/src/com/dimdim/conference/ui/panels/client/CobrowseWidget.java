package com.dimdim.conference.ui.panels.client;

import org.gwtwidgets.client.ui.PNGImage;

import com.dimdim.conference.ui.common.client.UIImages;
import com.dimdim.conference.ui.common.client.UIStrings;
import com.dimdim.conference.ui.common.client.util.CommonUserInformationDialog;
import com.dimdim.conference.ui.json.client.UIRosterEntry;
import com.dimdim.conference.ui.managers.client.resource.ResourceSharingController;
import com.dimdim.conference.ui.model.client.ClientModel;
import com.dimdim.conference.ui.model.client.CommandExecProgressListener;
import com.dimdim.conference.ui.model.client.ConferenceGlobals;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusListener;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.KeyboardListenerAdapter;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class CobrowseWidget extends Composite implements ClickListener{

	protected	ResourceSharingController	rsc = null;
//	protected	HorizontalPanel		basePanel = new HorizontalPanel();
	protected	HorizontalPanel		basePanel = new HorizontalPanel();
	protected	HorizontalPanel		syncImagePanel = new HorizontalPanel();
	
	TextBox urlTextBox = new TextBox();
	Button addButton = new Button();
	PNGImage sync = null;
	PNGImage lockImage = null;
	PNGImage unLockImage = null;
	Image prevButton = null;
	Image nextButton = null;
	UIRosterEntry currentUser = null;
	boolean isLocked = false;
	boolean textBoxHasFocus = false;
	public CobrowseWidget(final ResourceSharingController rsc, final UIRosterEntry currentUser)
	{
		//Window.alert("inside constructor");
		this.rsc = rsc;
		this.currentUser = currentUser;
		initWidget(basePanel);
		syncImagePanel.setSize("100%", "100%");
		fillContent();

	}
	private void fillContent()
	{
		sync = new PNGImage("images/sync.png", 20, 20);
		sync.addStyleName("padding-5");
		sync.addStyleName("anchor-cursor");
		sync.setTitle("Sync to the previous state");
		sync.addClickListener(this);
		
		lockImage = new PNGImage("images/unlock.png", 20, 20);
		lockImage.addStyleName("padding-5");
		lockImage.addStyleName("anchor-cursor");
		lockImage.setTitle("Lock");
		lockImage.addClickListener(this);
		
		unLockImage = new PNGImage("images/lock.png", 20, 20);
		unLockImage.addStyleName("padding-5");
		unLockImage.addStyleName("anchor-cursor");
		unLockImage.setTitle("Un-Lock");
		unLockImage.addClickListener(this);
		
		if(this.currentUser.isActivePresenter())
		{
			syncImagePanel.add(lockImage);
		}else{
			syncImagePanel.add(sync);

		}
		
		
		//if (ClientModel.getClientModel().getRosterModel().getCurrentUser().isActivePresenter())
		//{
		addButton.setText("Add");
		addButton.setStyleName("add_btns");
		addButton.setTitle("Click to Co-Browse");
		//button.setStyleName("dm-popup-close-button");
		addButton.addStyleName("padding-5");
		addButton.addStyleName("anchor-cursor");
		addButton.addClickListener(this);
		
		urlTextBox.setStyleName("common-text");
		urlTextBox.addStyleName("padding-5");
		urlTextBox.addFocusListener(new FocusListener(){

			public void onFocus(Widget arg0) {
				textBoxHasFocus = true;
				
			}

			public void onLostFocus(Widget arg0) {
				textBoxHasFocus = false;
				
			}
			
		});
		
		KeyboardListenerAdapter keyboardListener = new KeyboardListenerAdapter()
		{
			public void onKeyDown(Widget sender, char keyCode, int modifiers)
			{
			}
			public void onKeyUp(Widget sender, char keyCode, int modifiers)
			{
				if( keyCode == KeyboardListener.KEY_ENTER )
				{
					String url = urlTextBox.getText();
					if(null != url && url.length() > 0)
					{
						onClick(addButton);
					}
				}
			}
		};
		urlTextBox.addKeyboardListener(keyboardListener);
		
		
		prevButton = UIImages.getImageBundle(UIImages.defaultSkin).getPrev();
		nextButton = UIImages.getImageBundle(UIImages.defaultSkin).getNext();
		prevButton.addStyleName("padding-5");
		nextButton.addStyleName("padding-5");
		prevButton.addStyleName("anchor-cursor");
		nextButton.addStyleName("anchor-cursor");
		prevButton.setTitle("Go to the previous state");
		nextButton.setTitle("Go to the next state");
		prevButton.addClickListener(this);
		nextButton.addClickListener(this);
		
		basePanel.add(prevButton);
		basePanel.add(nextButton);
		basePanel.setCellVerticalAlignment(prevButton, HorizontalPanel.ALIGN_MIDDLE);
		basePanel.setCellVerticalAlignment(nextButton, HorizontalPanel.ALIGN_MIDDLE);
		
		basePanel.add(urlTextBox);
		basePanel.add(addButton);
		basePanel.setCellVerticalAlignment(urlTextBox, HorizontalPanel.ALIGN_MIDDLE);
		basePanel.setCellVerticalAlignment(addButton, HorizontalPanel.ALIGN_MIDDLE);
		
		basePanel.add(syncImagePanel);
		basePanel.setCellVerticalAlignment(syncImagePanel, HorizontalPanel.ALIGN_MIDDLE);
		//}
		roleChanged(ClientModel.getClientModel().getRosterModel().getCurrentUser().isActivePresenter());
	}
	public void onClick(Widget sender) {
		//Window.alert("inside clicked..."+tb.getText());
		if(sender == addButton)
		{
			String url = urlTextBox.getText();
			if(null != url && url.length() > 0)
			{
				if(url.startsWith("http://"))
				{
					
				}else{
					url = "http://"+url;
				}
				
				rsc.addCobRes(url, new CobCommandExecListener());
			}else{
				Window.alert("Please give a url");
			}
		}else if(sender == lockImage || sender == unLockImage)
		{
			//Window.alert("inside sync clicked");
			if(this.currentUser.isActivePresenter())
			{
				if(sender == lockImage)
				{
					//Window.alert("locking it");
					rsc.lock(true);
					setLockIcon(true);
				}else if(sender == unLockImage){
					//Window.alert("un-locking it");
					rsc.lock(false);
					setLockIcon(false);
				}
			}
		}else if(sender == sync) {
			rsc.syncCobrowse();
		}
		else if(sender == prevButton)
		{
			//Window.alert("inside prev");
			rsc.navigateTo("back");
		}
		else if(sender == nextButton)
		{
			//Window.alert("inside next clicked");
			rsc.navigateTo("forward");
		}
	}

	public	void	roleChanged(boolean isActivePresenter)
	{
		//Window.alert("inside cob widget activePresenter = "+isActivePresenter);
		if(isActivePresenter)
		{
			urlTextBox.setVisible(true);
			addButton.setVisible(true);
			prevButton.setVisible(true);
			nextButton.setVisible(true);
			addSyncImage(lockImage);
			
		}else{
			urlTextBox.setVisible(false);
			addButton.setVisible(false);
			prevButton.setVisible(false);
			nextButton.setVisible(false);
			addSyncImage(sync);
		}
		isLocked = false;
	}
	private void addSyncImage(PNGImage imageToAdd) {
		try{
			syncImagePanel.remove(lockImage);
			syncImagePanel.remove(unLockImage);
			syncImagePanel.remove(sync);
		}catch (Exception e) {
			
		}
		syncImagePanel.add(imageToAdd);
	}
	public boolean isLocked() {
		return isLocked;
	}
	public void setLocked(boolean isLocked) {
		this.isLocked = isLocked;
	}
	
	public void setLockIcon(boolean isLocked)
	{
		//Window.alert("inside cob widget..."+isLocked);
		if(this.currentUser.isActivePresenter())
		{
			if(isLocked)
			{
				addSyncImage(unLockImage);
				this.isLocked = true;
			}else{
				addSyncImage(lockImage);
				this.isLocked = false;
			}
		}
	}
	public boolean setTextBoxValue(String url) {
		//Only change the text box content if it has focus
		if(!textBoxHasFocus)
		{
			//if the text boxes is content is not same as parameter then only set it
			if(!urlTextBox.getText().equals(url))
			{
				urlTextBox.setText(url);
				urlTextBox.setTitle(url);
				rsc.changeCobrowseName(url);
				return true;
				
			}
		}
		return false;
	}
	private native String getLocation() /*-{
		return	escape($wnd.location);
	}-*/;
	
	class CobCommandExecListener implements CommandExecProgressListener
	{
		String waitAMoment = ConferenceGlobals.getDisplayString("stopping.desktop.desc","This may take a moment. Please wait.");
		CommonUserInformationDialog cuid = CommonUserInformationDialog.getCommonUserInformationDialog();
		
		String errorMessage = "Could not add the resource";
		String errorTitle = "Error";
		String progressTitle = "Info";
		public void commandExecError(String message) {
			//Window.alert("commandExecError message = "+message);
			if (cuid == null)
			{
				cuid = CommonUserInformationDialog.getCommonUserInformationDialog(errorTitle, errorMessage);
		    	cuid.drawDialog();
			}
			else
			{
				cuid.setMessage(errorTitle, errorMessage);
			}
	    	cuid.setButtonLabels(UIStrings.getOKLabel(),UIStrings.getCancelLabel());
			cuid.hideOKButton();
			cuid.hideCancelButton();			
			
		}
		public void commandExecSuccess(String message) {
			//Window.alert("commandExecSuccess message = "+message);
			cuid.hide();
			
		}
		public void setProgressMessage(String message) {
			//Window.alert("setProgressMessage message = "+message);
			if (cuid == null)
			{
				cuid = CommonUserInformationDialog.getCommonUserInformationDialog(progressTitle, message+" "+waitAMoment);
		    	cuid.drawDialog();
			}
			else
			{
				cuid.setMessage(progressTitle, message+" "+waitAMoment);
			}
	    	cuid.setButtonLabels(UIStrings.getOKLabel(),UIStrings.getCancelLabel());
			cuid.hideOKButton();
			cuid.hideCancelButton();
			
		}
	}
}
