package com.dimdim.conference.ui.common.client.common;

import java.util.Vector;

import com.dimdim.conference.ui.common.client.UIStrings;
import com.dimdim.conference.ui.common.client.common.TimeAndFocusSensitiveModalDialog;
import com.dimdim.conference.ui.common.client.util.FixedLengthLabel;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class TimeSensitiveInfo extends TimeAndFocusSensitiveModalDialog
{
    
    protected	VerticalPanel	basePanel = new VerticalPanel();
    protected DialogBox dlg = new DialogBox();
    //String caption = "Info...";
    String message = "";
    
    protected	String			dialogName = null;
    protected	Vector			footerButtons = null;
//	protected	DockPanel		outer;
	protected	VerticalPanel		vp;
	protected	String				closeButtonText = UIStrings.getCloseLabel();
	protected	FixedLengthLabel		messageLabel = new FixedLengthLabel("",15);
//	protected	boolean			addLogoImage = true;
	
	
	
	public TimeSensitiveInfo(String message, int showTime, boolean considerMouse)
	{
		super(showTime, considerMouse);
		initialize(message);
	}
	
    public TimeSensitiveInfo(String message){
	
    	super();
    	initialize(message);
    }

	private void initialize(String message) {
		this.message = message;
		dialogName = "default-message";
		
		pane.setStyleName("dm-send-invitation-panel");
		pane.add(basePanel);
		//pane.addStyleName("dialog-z-index");
		//Window.alert("before getting panel...");
		//Widget c = new Label(message);
		Widget c = getContent();
		basePanel.add(c);
		basePanel.setStyleName("dm-send-invitation-panel");
		this.basePanel.setCellWidth(c,"100%");
		//basePanel.add(getContent());
	}

    public	VerticalPanel	getContent()
	{
		//Window.alert("inside get content");
		vp = new VerticalPanel();
		//RoundedPanel rp = new RoundedPanel(vp);
		//vp.add(rp);
		
		vp.setStyleName("common-dialog-outer-panel");
		//vp.setBorderWidth(1);
		vp.addStyleName("common-dialog-rounded-corner-panel");
		//rp.setStyleName("common-dialog-rounded-corner-panel");
		//Window.alert("inside getting panel....0");
//		outer = new DockPanel();
//		outer.setStyleName("common-dialog-outer-panel");
//		if (this.addLogoImage)
//		{
//			outer.add(new Image(LOGO_IMAGE), DockPanel.WEST);
//		}
		DockPanel buttonPanel = new DockPanel();
		
		
		//Window.alert("inside getting panel....1");
		buttonPanel.setSpacing(0);
		HTML filler1 = new HTML("&nbsp;");
		buttonPanel.add(filler1,DockPanel.EAST);
		//buttonPanel.setStyleName("dm-send-invitation-panel");
		
		//Window.alert("inside getting panel....1");
		this.messageLabel.setStyleName("common-text");
		this.messageLabel.addStyleName("dialog-message-label");
		buttonPanel.add(this.messageLabel,DockPanel.WEST);
		buttonPanel.setCellVerticalAlignment(this.messageLabel,VerticalPanel.ALIGN_MIDDLE);
		buttonPanel.setCellWidth(this.messageLabel,"100%");
		
		Widget c = new Label(message);
		c.setStyleName("common-text");
		//Window.alert("inside getting panel....2");
		if (this.dialogName != null)
		{
			//	Create a width adjustment panel.
			String widthStyle = this.dialogName+"-dialog-width";
			String heightStyle1 = this.dialogName+"-dialog-height-one";
			String heightStyle2 = this.dialogName+"-dialog-height-two";
			String contentWidthStyle = this.dialogName+"-dialog-content";
			
			c.addStyleName(contentWidthStyle);
			HorizontalPanel upperPanel = new HorizontalPanel();
			
			HTML upperLeftBar = new HTML("&nbsp;");
			upperLeftBar.setStyleName(heightStyle1);
			upperPanel.add(upperLeftBar);
			upperPanel.add(c);
			upperPanel.setCellWidth(c,"100%");
			upperPanel.setCellVerticalAlignment(c,VerticalPanel.ALIGN_MIDDLE);
			
			HorizontalPanel lowerPanel = new HorizontalPanel();
			lowerPanel.setStyleName(widthStyle);
			
			HTML lowerLeftBar = new HTML("&nbsp;");
			lowerLeftBar.setStyleName(heightStyle2);
			lowerPanel.add(lowerLeftBar);
			lowerPanel.add(buttonPanel);
			lowerPanel.setCellWidth(buttonPanel,"100%");
			lowerPanel.setCellHorizontalAlignment(buttonPanel,HorizontalPanel.ALIGN_RIGHT);
			lowerPanel.setCellVerticalAlignment(buttonPanel,VerticalPanel.ALIGN_MIDDLE);
			
			vp.add(upperPanel);
			vp.add(lowerPanel);
			
			//Window.alert("inside getting panel....3");
		}
		else
		{
			vp.add(c);
			vp.setCellWidth(c,"100%");
			
			vp.add(buttonPanel);
			vp.setCellWidth(buttonPanel,"100%");
			vp.setCellHorizontalAlignment(buttonPanel,HorizontalPanel.ALIGN_RIGHT);
			//Window.alert("inside getting panel....4");
		}

		return vp;
	}

}
