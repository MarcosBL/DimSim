package com.dimdim.conference.ui.common.client.user;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.gwtwidgets.client.ui.PNGImage;

import com.dimdim.conference.ui.common.client.UIGlobals;
import com.dimdim.ui.common.client.data.UIDataDictionary;
import com.dimdim.ui.common.client.data.UIDataDictionaryManager;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FocusListener;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.HoverPopupPanel;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.PopupListener;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class EmoticonsPopup extends HoverPopupPanel
implements MouseListener, FocusListener, ClickListener, PopupListener {

	protected	FocusPanel	pane = new FocusPanel();
	protected	VerticalPanel	basePanel = new VerticalPanel();
	protected   Grid imageGrid = null;
	protected   TextArea chatTextArea = null;
	protected	int		showTime = UIGlobals.getHoverInitialShowTime();
	protected	Timer	timer;
	protected	boolean	hasFocus	=	false;
	protected 	HashMap emoticonsMap = null;
	
	public  EmoticonsPopup(TextArea chatTextArea) {
		super();
		//this.setStyleName("dm-hover-popup");
		
		int noRows = 3;
		int noCol = 3;
		int currentEmotionIndex = 0;
		Set tmpSet = null;
		String emotion = null;
		PNGImage emotionImage = null;
		
		this.chatTextArea = chatTextArea;
		//pane.setSize("100%","100%");
		//this.setStyleName("chat-emoticon-popup");
		this.add(pane);
		this.addPopupListener(this);
		pane.addMouseListener(this);
		pane.addFocusListener(this);

		//DockPanel outer = new DockPanel();
		//outer.setSize("100%","100%");
		//outer.setStyleName("dm-hover-popup-body");
		//outer.setVisible(true);
		//pane.add(outer);
		
		//Window.alert("DILIP added outer to pane ");
		
		//this.setStyleName("chat-emoticon-popup");
		
		//basePanel.setSize("100%","100%");
		//basePanel.setStyleName("chat-emoticon-popup");
		pane.add(basePanel);
		basePanel.setVisible(true);
		//basePanel.setStyleName("dm-hover-popup-body");

		
		//Label testLable = new Label("test");
		//this.pane.add(testLable);
		//pane.add(testLable);
		//testLable.setVisible(true);
		
		//Window.alert("DILIP added basepanel to outer ");
		imageGrid = new Grid(noRows, noCol);
		imageGrid.setVisible(true);
		//imageGrid.setSize("100%","100%");
		//imageGrid.setBorderWidth(1);
		imageGrid.setCellSpacing(1);
		//imageGrid.setCellPadding(1);
		imageGrid.setStyleName("chat-emoticon-popup");
//		imageGrid.getCellFormatter().setStyleName(0, 0, "chat-emoticon");
//		imageGrid.getCellFormatter().setStyleName(0, 1, "chat-emoticon");
//		imageGrid.getCellFormatter().setStyleName(0, 2, "chat-emoticon");
		//imageGrid.setStyleName();
		//Window.alert("calling prepareEmoticonsList ");
		prepareEmoticonsList();
		tmpSet = this.emoticonsMap.keySet();
		for (Iterator iter = tmpSet.iterator(); iter.hasNext();) {
			emotion = (String) iter.next();
			//Window.alert("emotion = "+emotion);
			emotionImage = getEmoticon(emotion);
			//Window.alert("emotionImage = "+emotionImage);
			HorizontalPanel tempPanel = new HorizontalPanel();
			tempPanel.add(emotionImage);
			//tempPanel.setStyleName("chat-emoticon-cell");
			tempPanel.setCellHorizontalAlignment(emotionImage, HorizontalPanel.ALIGN_CENTER);
			tempPanel.setCellVerticalAlignment(emotionImage, VerticalPanel.ALIGN_MIDDLE);
			//tempPanel.setBorderWidth(1);
			//tempPanel.setSize("120%","120%");
			
			FocusPanel tempFocusPanel = new FocusPanel();
			tempFocusPanel.add(tempPanel);
			tempPanel.setSize("100%","100%");
			tempFocusPanel.setStyleName("chat-emoticon-cell");
			tempFocusPanel.addMouseListener(new MouseListener() {
				public void onMouseDown(Widget sender, int arg1, int arg2) {
					// TODO Auto-generated method stub
					
				}
				public void onMouseEnter(Widget sender) {
					sender.setStyleName("chat-emoticon-cell-highlight");
					
				}
				public void onMouseLeave(Widget sender) {
					sender.setStyleName("chat-emoticon-cell");
					
				}
				public void onMouseMove(Widget sender, int arg1, int arg2) {
					// TODO Auto-generated method stub
					
				}
				public void onMouseUp(Widget sender, int arg1, int arg2) {
					// TODO Auto-generated method stub
					
				}
			});
			
			imageGrid.setWidget(currentEmotionIndex/3, currentEmotionIndex%3, tempFocusPanel);
			
			currentEmotionIndex++;
		}//end of for
		//imageGrid.getCellFormatter().setWidth(0, 2, "256px");
		this.basePanel.add(imageGrid);
		//this.basePanel.setCellWidth(imageGrid, "100%");
		//Window.alert("DILIP 2");
	}
	
	private	void prepareEmoticonsList()
	{
		//Window.alert("prepareEmoticonsList");
		if(emoticonsMap != null)
			return;

		emoticonsMap = new HashMap();
		try
		{
			UIDataDictionary emoticons = UIDataDictionaryManager.getManager().getDictionary("global_string", "emoticons");
			String s = emoticons.getStringValue("num");
			int count = (new Integer(s)).intValue();
			for (int i=0; i<count; i++)
			{
				String name = emoticons.getStringValue("t"+i+"_name");
				String value = emoticons.getStringValue("t"+i+"_value");
				if(!emoticonsMap.containsValue(value)){
					emoticonsMap.put(name, value);
				}
				
			}
			
			/*Dictionary emoticons = Dictionary.getDictionary("emoticons");
			String s = emoticons.get("num");
			int count = (new Integer(s)).intValue();
			for (int i=0; i<count; i++)
			{
				String name = emoticons.get("t"+i+"_name");
				String value = emoticons.get("t"+i+"_value");
				if(!emoticonsMap.containsValue(value)){
					emoticonsMap.put(name, value);
				}
			}*/
		}
		catch(Exception e)
		{
//			Window.alert(e.getMessage());
		}
	}
	
	protected	PNGImage	getEmoticon(final String emotion)
	{
		String emotionImgLoc = (String)emoticonsMap.get(emotion);
		//Window.alert("TASGXDG = "+emotionImgLoc);
		PNGImage emotionImage = new PNGImage(emotionImgLoc,16,16);
		int startIndex = -1;
		int endIndex = -1;
		startIndex = emotionImgLoc.lastIndexOf('\\');
		if(startIndex == -1)
			startIndex = emotionImgLoc.lastIndexOf('/');
		
		endIndex = emotionImgLoc.lastIndexOf('.');
		String tooltip = emotionImgLoc.substring(startIndex+1, endIndex);
		emotionImage.setTitle(tooltip);

		//emotionImage.setStyleName("chat-emoticon");
		emotionImage.addClickListener(new ClickListener(){
			public	void	onClick(Widget w)
			{
				sendEmotion(emotion);
				closePopup();
			}
		});
		
		/*emotionImage.addMouseListener(new MouseListener() {
			public void onMouseDown(Widget sender, int arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			public void onMouseEnter(Widget sender) {
				sender.removeStyleName("chat-emoticon-cell");
				sender.setStyleName("chat-emoticon-cell-highlight");
				
			}
			public void onMouseLeave(Widget sender) {
				sender.removeStyleName("chat-emoticon-cell-highlight");
				sender.setStyleName("chat-emoticon-cell");
				
			}
			public void onMouseMove(Widget sender, int arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			public void onMouseUp(Widget sender, int arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
		});*/
	
		//Window.alert("TASGXDG = "+emotionImage);
		return	emotionImage;
	}
	
	protected	void	sendEmotion(String emotion)
	{
		//Window.alert("DILIP clicked on this "+ emotion);
		//Window.alert("DILIP chatTextarea  = "+chatTextArea);
		String temp  = null;
		if(null != chatTextArea.getText()){
			//Window.alert("DILIP chat text area not null");
			temp = chatTextArea.getText()+emotion;
		}
		else
			temp = emotion;
		
		chatTextArea.setText(temp);
		//Window.alert("DILIP chat temp = "+temp);
		chatTextArea.setCursorPos(temp.length());
		chatTextArea.setFocus(true);
		hideHoverPopup();
	}

	public void onFocus(Widget sender)
	{
		onMouseEnter(sender);
	}
	public void onLostFocus(Widget sender)
	{
		onMouseLeave(sender);
	}
	public void onMouseDown(Widget sender, int x, int y)
	{
	}
	public void onMouseEnter(Widget sender)
	{
		if (this.timer != null)
		{
			this.timer.cancel();
			this.timer = null;
		}
		this.hasFocus = true;
		this.showTime = UIGlobals.getHoverPostMouseOutShowTime();
	}
	public void closePopup()
	{
		if (this.timer != null)
		{
			this.timer.cancel();
			this.timer = null;
		}
		if (!this.hasFocus)
		{
			timer = new Timer()
			{
				public void run()
				{
					timer = null;
					hideHoverPopup();
				}
			};
			//Window.alert("DILIP scheduled to hide");
			timer.schedule(this.showTime);
		}
	}
	public void onMouseLeave(Widget sender)
	{
		//Window.alert("DILIP MOSE LEAVE...");
		this.hasFocus = false;
		closePopup();
	}
	public void onMouseMove(Widget sender, int x, int y)
	{
	}
	public void onMouseUp(Widget sender, int x, int y)
	{
	}
	public void onClick(Widget sender)
	{
	}
	public	void	onPopupClosed(PopupPanel popup, boolean autoClosed)
	{
		if (this.timer != null)
		{
			this.timer.cancel();
			this.timer = null;
		}
	}
	public int getShowTime()
	{
		return showTime;
	}
	public void setShowTime(int showTime)
	{
		this.showTime = showTime;
	}
	public	boolean	supportsRefresh()
	{
		return	false;
	}
	public	void	hideHoverPopup()
	{
		this.showTime = UIGlobals.getHoverInitialShowTime();
		if (this.timer != null)
		{
			this.timer.cancel();
			this.timer = null;
		}
		super.hideHoverPopup();
	}
	
	public	void	popupVisible()
	{
		//Window.alert("DILIP #!@#!@# popupvisible method");
		this.timer = new Timer()
		{
			public void run()
			{
				timer = null;
				hideHoverPopup();
			}
		};
		this.timer.schedule(this.showTime);
	}
	
}
