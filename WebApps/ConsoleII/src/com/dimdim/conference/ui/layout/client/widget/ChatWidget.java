package com.dimdim.conference.ui.layout.client.widget;

import com.dimdim.conference.ui.common.client.UIImages;
import com.dimdim.conference.ui.layout.client.main.NewLayout;
import com.dimdim.conference.ui.user.client.DiscussionWidget;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ChatWidget  extends Composite //implements ClickListener
{
/*
	private   WorkspacePanel workSpace = null;
	protected HorizontalPanel basePanel = new HorizontalPanel();
	protected VerticalPanel verticalPanel = new VerticalPanel();
	protected HorizontalPanel chatPanel = new HorizontalPanel();
	//ImageNTextWidget chatLink = null;
	Label chatLink = null;
	//RoundedPanel roundedChat = null;
	Label chatLabel = null;
	
	DiscussionWidget chatWidget = null;
	private NewLayout consoleLayout;
	
	public ChatWidget(WorkspacePanel workSpace, NewLayout consoleLayout){
		this.workSpace = workSpace;
		this.consoleLayout = consoleLayout;
		
		initWidget(basePanel);
		
		//basePanel.setBorderWidth(1);
		fillContent();
		
	}
	
	private void fillContent(){
		
		/*HorizontalPanel h2 = new HorizontalPanel();
		h2.setStyleName("console-middle-left-panel-float");
		h2.add(new HTML("&nbsp;&nbsp;&nbsp;"));
		basePanel.add(h2);
		h2.setWidth("10px");*--/
		
		chatWidget = new DiscussionWidget(workSpace.getCurrentUser());
		chatWidget.setSize("100%", "100%");
		chatPanel.add(chatWidget);
		//chatPanel.setCellWidth(chatWidget, "100%");
		chatPanel.setCellHorizontalAlignment(chatWidget, HorizontalPanel.ALIGN_CENTER);
		//chatPanel.setBorderWidth(1);
		//chatPanel.setStyleName("rp");
		FocusPanel otherLinksPanel = new FocusPanel();
		
		chatLink = new Label("Public Chat");
		chatLink.setStyleName("common-text");
		chatLink.addStyleName("common-bold-text");
		
		//chatLink = new ImageNTextWidget("Hide Chat", UIImages.getImageBundle(UIImages.defaultSkin).getHideChat(), this);
		//chatLabel = chatLink.getLabel();
		otherLinksPanel.add(chatLink);
		/*HorizontalPanel topLinksPanel = new HorizontalPanel();
		topLinksPanel.add(chatLink);
		topLinksPanel.setStyleName("console-collab-chat-header");
		topLinksPanel.setCellHorizontalAlignment(chatLink, HorizontalPanel.ALIGN_RIGHT);*--/
		
		//verticalPanel.add(topLinksPanel);
		
		verticalPanel.add(chatWidget);
		//otherLinksPanel.setCellHorizontalAlignment(chatWidget, HorizontalPanel.ALIGN_RIGHT);
		//otherLinksPanel.setCellVerticalAlignment(chatWidget, HorizontalPanel.ALIGN_TOP);
		otherLinksPanel.setWidth("215px");
		otherLinksPanel.addStyleName("anchor-cursor");
		otherLinksPanel.addClickListener(this);
		//Window.alert("before adding chat widget..");
		consoleLayout.addWidgetToID("chat_pod_header", otherLinksPanel);
		consoleLayout.addWidgetToID("chat_pod_content", verticalPanel);
		
		addChatWidget();
		//verticalPanel.add(chatPanel);
		//verticalPanel.setCellHorizontalAlignment(chatPanel, HorizontalPanel.ALIGN_RIGHT);
		
		//roundedChat = new RoundedPanel(verticalPanel);
		//roundedChat.setSize("100%", "100%");
		
		//basePanel.add(roundedChat);
		
	}
	
	public void addChatWidget() {
		consoleLayout.setIDVisibility("main_chat_td", true);
		//verticalPa)nel.setVisible(true);
		//verticalPanel.add(chatPanel);
		//verticalPanel.setCellHorizontalAlignment(chatPanel, HorizontalPanel.ALIGN_RIGHT);
		//if(null != workSpace.resourcePlayerPanel)
		 //{
		//	workSpace.resourcePlayerPanel.removeStyleName("console-resourcePlayer-fullwidth");
		//	workSpace.resourcePlayerPanel.addStyleName("console-resourcePlayer-width");
		//}
		workSpace.hideChatLink();
	}
	
	private void removeChatWidget() {
		//verticalPanel.setVisible(false);
		//verticalPanel.remove(chatPanel);
		//basePanel.setVisible(false);
		consoleLayout.setIDVisibility("main_chat_td", false);
		workSpace.showChatLink();
	}
	
	public void onClick(Widget sender) {
		//if(chatLabel == sender || sender == chatLink.getImage())
		//{
			removeChatWidget();
		//}
	}
	
	public void panelResized(int width, int height)
	{
		if((height-10) > 0)
		{
			chatPanel.setSize(width+"px", (height-10)+"px");
		}
		if((height-20) > 0)
		{
			chatWidget.resizeWidget((width-10), (height-20));
		}
	}
	*/
}
