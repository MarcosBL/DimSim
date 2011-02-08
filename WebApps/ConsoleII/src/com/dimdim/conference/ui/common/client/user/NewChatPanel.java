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

package com.dimdim.conference.ui.common.client.user;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import org.gwtwidgets.client.ui.PNGImage;

import com.dimdim.conference.ui.common.client.UIStrings;
import com.dimdim.conference.ui.common.client.util.DebugPanel;
import com.dimdim.conference.ui.json.client.UIChatEntry;
import com.dimdim.conference.ui.json.client.UIPopoutPanelData;
import com.dimdim.conference.ui.json.client.UIRosterEntry;
import com.dimdim.conference.ui.model.client.ChatListener;
import com.dimdim.conference.ui.model.client.ClientModel;
import com.dimdim.conference.ui.model.client.ConferenceGlobals;
import com.dimdim.conference.ui.model.client.PopoutPanelProxy;
import com.dimdim.conference.ui.model.client.PopoutSupportingPanel;
import com.dimdim.conference.ui.model.client.RosterModel;
import com.dimdim.ui.common.client.data.UIDataDictionary;
import com.dimdim.ui.common.client.data.UIDataDictionaryManager;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusListener;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.KeyboardListenerAdapter;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MouseListenerAdapter;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * This chat panel itself contains only the messages scroll box, chat message
 * entry text area and the instructions line. This panel is used for the
 * all to all discussions chat and through a popup one on one chats. The chat
 * panel itself does not require header and footer. It also does not support
 * popout. That is also 
 */
public class NewChatPanel extends Composite implements FocusListener,
		ChatListener, PopoutSupportingPanel
{
	protected	UIRosterEntry	me;
	protected	UIRosterEntry	other;
	protected	boolean	active = false;
	protected	long	lastActivityTime = 0;
	protected	int	heightIndex = 3;
	
//	protected	DockPanel	outer = new DockPanel();
	protected	VerticalPanel	outer = new VerticalPanel();
//	private	DockPanel	body = new DockPanel();
	
	protected	HorizontalPanel	instructionPanel;
	protected	HorizontalPanel	pane = new HorizontalPanel();
	protected	VerticalPanel	chatMessages = new VerticalPanel();
	protected	TextArea	sendText = null;
	protected	ScrollPanel	scrollPanel;
	
	protected	String	toId;
	
	protected	int		maxMessages = 200;
	protected	int		trimMessages = 25;
	
//	protected	int	scrollMax = 195;
	// modified to fix the chat scroll issue.
	protected int scrollMax = 2048;
	protected	KeyboardListener	keyboardListener;
	protected	NewChatUnreadMessageListener	unreadMessaeListener;
	
	protected	RosterModel		rosterModel;
	protected	boolean		hasMessage	=	false;
	protected	boolean		textBoxHasFocus = false;
	protected	boolean		minimized	=	false;
	protected	boolean		hasUnreadMessage	=	false;
	protected  EmoticonsPopup ePopUP = null;
	
	//	Both must be true to begin with for the console to allow creation
	//	of the chat panel.
	
	protected	boolean		myChatEnabled = true;
	protected	boolean		otherChatEnabled = true;
	protected	String		spaceSequence = "DIMDIM_WBR_NON_IE8203";
	
	private static String[]  listNamesSorted = null;
	
	protected  static  HashMap emoticonsMap = null;// = new HashMap();    
	//private static HashMap tempMap = null;
//	String wordBreak = "DIMDIM_LTwbr>";
	int maxAllowedLen = 24;
	
	/**
	 * Same chat panel is used for global as well as personal chats. Global
	 * chat is simply identified by using 'other' argument as null.
	 */
	public	NewChatPanel(UIRosterEntry me, UIRosterEntry other)
	{
		this.me = me;
		this.other = other;
		if (other != null)
		{
			this.toId = other.getUserId();
		}
		this.lastActivityTime = System.currentTimeMillis();
		if (ConferenceGlobals.isBrowserIE())
		{
			spaceSequence = "DIMDIM_LTwbr>";
		}
		
		//	Add the central scroll panel that will hold the messages.
		
		scrollPanel = new ScrollPanel();
		scrollPanel.add(this.chatMessages);
		scrollPanel.setStyleName("dm-chat-message-area");
		
		//	A small and short instructions / message area.
		
		instructionPanel = new HorizontalPanel();
		instructionPanel.setStyleName("chat-instruction-panel");
		instructionPanel.setWidth("248px");
		
		//in public chat add powered by dimdim logo else have the help text
		if(null == toId)
		{
			HorizontalPanel hp = new HorizontalPanel();
			
			HorizontalPanel tempSpacer = new HorizontalPanel();
			tempSpacer.setWidth("10px");
			tempSpacer.add(new Label("  "));
			hp.add(tempSpacer);
			hp.setCellHorizontalAlignment(tempSpacer, HorizontalPanel.ALIGN_LEFT);
			hp.setCellVerticalAlignment(tempSpacer, VerticalPanel.ALIGN_MIDDLE);
			
			PNGImage image = new PNGImage("images/logo_powered.png", 8, 14);
			hp.add(image);
			//instructionPanel.setCellWidth(image,"100%");
			hp.setCellHorizontalAlignment(image, HorizontalPanel.ALIGN_LEFT);
			hp.setCellVerticalAlignment(image, VerticalPanel.ALIGN_MIDDLE);
			
			//hp.setBorderWidth(1);
			HTML instruction = new HTML("Powered By <a href='#'><u> Dimdim </u></a>");
			instruction.addClickListener(new ClickListener()
			{
				public void onClick(Widget sender)
				{
					openDimdimWebSite();
				}
			});
			instruction.setStyleName("poweredby-text");
			hp.add(instruction);
			//instructionPanel.setCellWidth(instruction,"100%");
			hp.setCellHorizontalAlignment(instruction, HorizontalPanel.ALIGN_LEFT);
			hp.setCellVerticalAlignment(instruction, VerticalPanel.ALIGN_MIDDLE);
			
			
			
			
			instructionPanel.add(hp);
			//instructionPanel.setCellWidth(instruction,"100%");
			instructionPanel.setCellHorizontalAlignment(hp,HorizontalPanel.ALIGN_LEFT);
			instructionPanel.setCellVerticalAlignment(hp,VerticalPanel.ALIGN_MIDDLE);
			
		}else{
			Label instruction = new Label(UIStrings.getChatPanelInstruction());
			instruction.setStyleName("chat-instruction");
			instructionPanel.add(instruction);
			//instructionPanel.setCellWidth(instruction,"100%");
			instructionPanel.setCellHorizontalAlignment(instruction,HorizontalPanel.ALIGN_LEFT);
			instructionPanel.setCellVerticalAlignment(instruction,VerticalPanel.ALIGN_MIDDLE);
		}
				
		Label emoticon = new Label(UIStrings.getChatPanelEmoticonInstruction());
		emoticon.setStyleName("chat-emoticon-lable");
		instructionPanel.add(emoticon);
		//instructionPanel.setCellWidth(emoticon,"30%");
		instructionPanel.setCellHorizontalAlignment(emoticon,HorizontalPanel.ALIGN_RIGHT);
		instructionPanel.setCellVerticalAlignment(emoticon,VerticalPanel.ALIGN_MIDDLE);
				
		//	Add the text area that the users will type their messages in.
		
		sendText = new TextArea();
		sendText.setText("");
		if(null == toId)
		{
			sendText.setText(UIStrings.getChatPanelInstruction());
			sendText.setStyleName("chat-instruction");
		}
		//if (ConferenceGlobals.isBrowserIE())
		//{
			sendText.setVisibleLines(2);
		//}
		//else
		//{
		//	sendText.setVisibleLines(1);
		//}
		sendText.setStyleName("chat-text-area");

		keyboardListener = new KeyboardListenerAdapter()
		{
			public void onKeyDown(Widget sender, char keyCode, int modifiers)
			{
			}
			public void onKeyUp(Widget sender, char keyCode, int modifiers)
			{
				if( keyCode == KeyboardListener.KEY_ENTER )
				{
					sendChatMessage();
				}
			}
		};
		sendText.addKeyboardListener(keyboardListener);
		sendText.addFocusListener(this);
		
		//	Assemble the overall chat panel.
		
		initWidget(pane);
		pane.setWidth("100%");
		pane.add(outer);
		outer.setWidth("100%");
		
		outer.add(scrollPanel);
		scrollPanel.addStyleName("dm-chat-message-area-pane");
		
		outer.add(this.instructionPanel);
		outer.setCellWidth(this.instructionPanel,"100%");
		outer.setCellHorizontalAlignment(this.instructionPanel,HorizontalPanel.ALIGN_LEFT);
		
		outer.add(this.sendText);
		outer.setCellWidth(this.sendText,"100%");
		outer.setCellHorizontalAlignment(this.sendText,HorizontalPanel.ALIGN_CENTER);
		this.sendText.setStyleName("dm-chat-text-area");
		
	
		this.rosterModel = ClientModel.getClientModel().getRosterModel();

		
//		Window.alert("created popup..");
		//setting up emoticons popup panel
		ePopUP = new EmoticonsPopup(sendText);
		emoticon.addMouseListener(new MouseListenerAdapter()
		{
			public	void onMouseEnter(Widget sender)
			{
				int left = sender.getAbsoluteLeft()- 5;
				int top = sender.getAbsoluteTop() - 75;				
				ePopUP.setPopupPosition(left,top);
				ePopUP.showHoverPopup();
				ePopUP.popupVisible();
			}
		});
		
		
		if(emoticonsMap == null)
		{
			emoticonsMap = new HashMap();
			prepareEmoticonsList();
//			this is to handle :) and :( also			
		}
	}
	
	private	void	prepareEmoticonsList()
	{
//		Window.alert("prepareEmoticonsList");
		try
		{
			UIDataDictionary emoticons = UIDataDictionaryManager.getManager().getDictionary("global_string", "emoticons");
			if (emoticons != null)
			{
//			Dictionary emoticons = Dictionary.getDictionary("emoticons");
				String s = emoticons.getStringValue("num");
				int count = (new Integer(s)).intValue();
				for (int i=0; i<count; i++)
				{
					String name = emoticons.getStringValue("t"+i+"_name");
					String value = emoticons.getStringValue("t"+i+"_value");
					emoticonsMap.put(name, value);
				}
			}
			listNamesSorted = getSortedList(emoticonsMap.keySet());
		}
		catch(Exception e)
		{
//			Window.alert(e.getMessage());
		}
	}
	public	long	getLastActivityTime()
	{
		return	this.lastActivityTime;
	}
	public	UIRosterEntry	getMe()
	{
		return	this.me;
	}
	public	UIRosterEntry	getOther()
	{
		return	this.other;
	}
	public void sendChatMessage()
	{
		String tempS = this.sendText.getText();
		//Window.alert("--"+tempS+"--");
		//this is to block the empty strings from getting in the chat messages
		if (tempS.trim().length() > 0)
		{
			// added a function to limit the string to 800 characters including space...
			String modifiedChatMessage = checklength(tempS);
			this.showAndSendChatMessage(modifiedChatMessage);
		}else{
			this.sendText.setText("");
		}
	}
//	 added a function to limit the string to 800 characters including space...
	private String checklength(String tempS)
	{
		String toModifyChat = tempS;
		String modifiedChatMessage;
		if(toModifyChat.length() > 800)
		{
			int start = 0;
			int end = 799;
			String substr = toModifyChat.substring(start, end);
			modifiedChatMessage = substr;
//			Window.alert("String length" + toModifyChat.length());
//			Window.alert("inside if loop " + modifiedChatMessage);
		}
		else
		{
//			Window.alert("inside else loop " + toModifyChat);
//			modifiedChatMessage = toModifyChat.substring(0,toModifyChat.length()-2);
			modifiedChatMessage = toModifyChat.replaceAll("\n", "");
		}
		
		return modifiedChatMessage;
	}
	protected	void	showAndSendChatMessage(String tempS)
	{
		String tempString = this.getParsedChatMessage(tempS);
		String s1 = encodeBase64(tempString);
		//Window.alert("--"+s1+"--");
		//for the same user when message is displayedhandling emoticons also
		//coz now getParsedChatMessage does not handle emoticons
		String emoticonsParsedMsg = handleEmoticons(tempString);
		if (tempString.length() > 0)
		{
			emoticonsParsedMsg = emoticonsParsedMsg.replaceAll("<", "&lt;");
			emoticonsParsedMsg = this.decodeEmoticonSpecialChars(emoticonsParsedMsg);
			this.addHtmlHTextToTextBox("<b>me:</b> "+emoticonsParsedMsg,true);
			if(emoticonsParsedMsg.indexOf("DimdimChatTraceLog")==0)
			{
				emoticonsParsedMsg = "DimdimChatTraceLog : Send by Browser-";
				emoticonsParsedMsg += Long.toString(System.currentTimeMillis());
			}
			
			this.sendText.setText("");
			this.lastActivityTime = System.currentTimeMillis();
			
			String s = encodeChatSpecialChars(s1);
			if (this.isInConsole())
			{
				this.rosterModel.sendChatMessage(toId,s);
			}
			else
			{
				this.sendMessageToConsolePanel(s1);
			}
		}
	}
	
	
	public	void	onChatMessage(UIChatEntry chatEntry)
	{
		if (chatEntry.getMessageText() != null)
		{
			if ((!chatEntry.isMessagePrivate() && this.other == null) ||
					(chatEntry.isMessagePrivate() && this.other != null &&
						this.other.getUserId().equals(chatEntry.getSenderId())))
			{
				this.receiveChatMessage(chatEntry.getSenderId(),
						chatEntry.getSenderName(), chatEntry.getMessageText());
				this.lastActivityTime = System.currentTimeMillis();
			}
		}
	}
	public void receiveChatMessage(String id, String name, String message)
	{
		//if(ConferenceGlobals.publicChatEnabled)
		//{
			if(message.indexOf("DimdimChatTraceLog")==0)
			{
				message+=", Received by Browser-"+Long.toString(System.currentTimeMillis());
			}
			//Window.alert("Received: "+message);
			String tempMessage = decodeChatSpecialChars(message);
			String receieveMessage = decodeBase64(tempMessage);
			//emoticons handled while a message is recieved
			//coz diff html tags used for diff browsers
			receieveMessage = handleEmoticons(receieveMessage);
			//Window.alert("Received Parsed: "+receieveMessage);
			receieveMessage = receieveMessage.replaceAll("<", "&lt;");
			receieveMessage = this.decodeEmoticonSpecialChars(receieveMessage);
			
			String s = "<b>"+name+":</b> "+receieveMessage;
			this.addHtmlHTextToTextBox(s,false);
		//}
	}
	private void receiveChatMessage2(String id, String name, String message)
	{
		//if(ConferenceGlobals.publicChatEnabled)
		//{
		if("system".equalsIgnoreCase(name))
		{
			message = message.replaceAll("<", "&lt;");
			
			String s = "<b>"+name+":</b> "+message;
			this.addHtmlHTextToTextBox(s,false);
		}else if (!id.equals(this.me.getUserId()))
			{
				message = message.replaceAll("<", "&lt;");
				
				String s = "<b>"+name+":</b> "+message;
				this.addHtmlHTextToTextBox(s,false);
			}
		//}
	}
	public void receiveWelcomeMessage(String message)
	{	if(ConferenceGlobals.publicChatEnabled)
		{
			this.addHtmlHTextToTextBox(this.getParsedChatMessage(message),true);
		}
	}

//	public String encodeReceiveChatQuote(String message)
//	{
//		return message.replaceAll("\"","DIMDIM_QUOTE");
//	}
//	
//	public String parseReceiveChatQuote(String message)
//	{
//		
//		return message.replaceAll("DIMDIM_QUOTE","\"");
//	}

	public String encodeChatSpecialChars(String message)
	{	
		String parsedString;
		if(message.indexOf("\"") != -1)
			parsedString = message.replaceAll("\"","DIMDIM_QUOTE");
		else if(message.indexOf("#")!= -1)
			parsedString = message.replaceAll("#","DIMDIM_HASH");
		else if(message.indexOf("&")!= -1)
			parsedString = message.replaceAll("&","DIMDIM_AMP");
		else if(message.indexOf("%")!= -1)
			parsedString = message.replaceAll("%","DIMDIM_PERCENTAGE");
		else if(message.indexOf("+")!= -1){
			//here + is part of regular expression
			//so replacing it as a char with % then replacing with a string
			parsedString = message.replace('+','%');
			parsedString = parsedString.replaceAll("%","DIMDIM_PLUS");
		}
		else if(message.indexOf("\\")!= -1){
			//here \ is part of regular expression
			//so replacing it as a char with % then replacing with a string
			parsedString = message.replace('\\','%');
			parsedString = parsedString.replaceAll("%","DIMDIM_BACK_SLASH");
		}
		else
			parsedString = message;
		
		return parsedString; 
	}

	private String decodeChatSpecialChars(String message)
	{	
		String parsedString;
		
		if(message.indexOf("DIMDIM_QUOTE")!= -1)
			parsedString = message.replaceAll("DIMDIM_QUOTE","\"");
		else if(message.indexOf("DIMDIM_HASH")!= -1)
			parsedString = message.replaceAll("DIMDIM_HASH","#");
		else if(message.indexOf("DIMDIM_AMP")!= -1)
			parsedString = message.replaceAll("DIMDIM_AMP","&");
		else if(message.indexOf("DIMDIM_PERCENTAGE")!= -1)
			parsedString = message.replaceAll("DIMDIM_PERCENTAGE","%");
		else if(message.indexOf("DIMDIM_PLUS")!= -1)
			parsedString = message.replaceAll("DIMDIM_PLUS","+");
		else if(message.indexOf("DIMDIM_BACK_SLASH")!= -1)
			parsedString = message.replaceAll("DIMDIM_BACK_SLASH","&#92;");
		else
			parsedString = message;
		
		return parsedString; 
	}
	private String decodeEmoticonSpecialChars(String message)
	{
		String s1 = message.replaceAll("DIMDIM_LTspan", "<span");
		String s2 = s1.replaceAll("DIMDIM_LT/span", "</span");
		String s3 = s2.replaceAll("DIMDIM_LTa", "<a");
		String s4 = s3.replaceAll("DIMDIM_LT/a", "</a");
		String s5 = s4.replaceAll("DIMDIM_LTimg", "<img");
		
		String s6 = s5.replaceAll("DIMDIM_LTw", "<w");
		String s7 = s6.replaceAll("DIMDIM_WBR_NON_IE8203", "&#8203");
//		String s = "";
//		if(!ConferenceGlobals.isBrowserIE())
//		{
//			s = s6.replaceAll("<wbr>", "&#8203;");;
//		}else{
//			s = s6;
//		}
		return	s7;
	}
	private	void	addHtmlHTextToTextBox(String text, boolean host)
	{

		text = decodeEmoticonSpecialChars(text);
		bufferMessage(text,host);
		int	lineNum = 0;
		{
			//	Add the first section to the box and trim the string.
			HTML html = new HTML(text);
			html.setWordWrap(true);
			html.setStyleName("chat-message");
			if (lineNum == 0)
			{
				html.addStyleName("chat-message-first-line");
			}
			
			addText(html,host);
		}
	}
	private	String	getStringSection(String text, int maxLength)
	{
		if (text.length() < maxLength)
		{
			return	text;
		}
		else
		{
			//	Attempt to tokenize the string in words.
			String section = this.getWordGroup(text, maxLength);
			if (section.length() == 0)
			{
				section = text.substring(0,maxLength);
			}
			return	section;
		}
	}
	private	String	getWordGroup(String text, int maxLength)
	{
		String s = text;
		String wordGroup = "";
		int nextSpace = s.indexOf(" ");
		if (nextSpace > 0)
		{
			while (nextSpace > 0)
			{
				//	If the next word is longer than the maximum
				if (nextSpace >= maxLength)
				{
					return	wordGroup;
				}
				else
				{
					wordGroup = wordGroup +" "+ s.substring(0,nextSpace);
					s = s.substring(nextSpace+1);
					nextSpace = s.indexOf(" ");
				}
			}
		}
		//	Check the last section.
		if (s.length() < maxLength)
		{
			wordGroup = wordGroup+ " " + s;
		}
//		Window.alert("Word group: "+wordGroup);
		return	wordGroup;
	}
	private void addText(Widget lbl,boolean host)
	{
		HorizontalPanel hp = new HorizontalPanel();
		hp.add(lbl);
		if (chatMessages.getWidgetCount() > maxMessages)
		{
			for (int i=trimMessages; i>= 0; i--)
			{
				chatMessages.remove(i);
			}
		}
		this.chatMessages.add(hp);
		this.chatMessages.setCellWidth(hp, "100%");
		if (host)
		{
			hp.addStyleName("host-chat-message");
		}
		else
		{
			hp.addStyleName("remote-chat-message");
		}
		
		if (!this.textBoxHasFocus)
		{	
				if (this.unreadMessaeListener != null)
				{
					this.unreadMessaeListener.hasNewUnreadMessage();
				}
		}  
		else
		{
				if (this.unreadMessaeListener != null)
				{
					this.unreadMessaeListener.seenUnreadMessage();
				}
		}
		
		
		int scrollPosition = scrollPanel.getScrollPosition();
		DebugPanel.getDebugPanel().addDebugMessage("Chat Panel scroll position:"+scrollPosition);
		if (scrollPosition < 0)
		{
			scrollPosition = 0;
		}
		/*if(scrollPosition < scrollMax)
		{
			scrollMax += scrollPosition;
		}*/
//		Window.alert("Scroll position " + scrollPosition );
//		Window.alert("Scroll Max " + scrollMax);
		scrollMax += scrollPosition;
		scrollPanel.setScrollPosition(scrollMax);
	}
	public NewChatUnreadMessageListener getUnreadMessaeListener()
	{
		return unreadMessaeListener;
	}
	public void setUnreadMessaeListener(
			NewChatUnreadMessageListener unreadMessaeListener)
	{
		this.unreadMessaeListener = unreadMessaeListener;
	}

	public	void	setScrollPanelHeight(int height)
	{
		this.scrollPanel.setHeight(height+"px");
	}
	public	void	setContainerHeight(int height)
	{
		
	}
	public void onFocus(Widget w)
	{
		this.textBoxHasFocus = true;
		//this means this is pubic chat
		if(null == toId)
		{
			if(UIStrings.getChatPanelInstruction().equals(this.sendText.getText())){
				this.sendText.setText("");
				this.sendText.removeStyleName("chat-instruction");
			}
		}
		this.unreadMessaeListener.seenUnreadMessage();
	}
	public void onLostFocus(Widget sender)
	{
		this.textBoxHasFocus = false;
	}
	public	void	userLeft()
	{
		this.sendText.setEnabled(false);
	}
	public	void	currentUserChatPermissionRevoked()
	{
		if (this.myChatEnabled)
		{
			this.receiveChatMessage2(null,"system",UIStrings.getChatPermissionRevokedMessage1());
			this.myChatEnabled = false;
			this.sendText.setEnabled(false);
		}
		else
		{
			//	Redundant call. Should never happen, but just a safety check.
		}
	}
	public	void	currentUserChatPermissionGranted()
	{
		if (this.myChatEnabled)
		{
			//	Redundant call. Should never happen, but just a safety check.
		}
		else
		{
			this.receiveChatMessage2(null,"system",UIStrings.getChatPermissionGrantedMessage1());
			this.myChatEnabled = true;
			this.sendText.setEnabled(true);
		}
	}
	public	void	otherUserChatPermissionRevoked()
	{
		if (this.otherChatEnabled)
		{
			this.receiveChatMessage2(null,"system",UIStrings.getChatPermissionRovokedMessage2()+this.other.getDisplayName());
			this.otherChatEnabled = false;
			this.sendText.setEnabled(false);
		}
		else
		{
			//	Redundant call. Should never happen, but just a safety check.
		}
	}
	public	void	otherUserChatPermissionGranted()
	{
		if (this.otherChatEnabled)
		{
			//	Redundant call. Should never happen, but just a safety check.
		}
		else
		{
			this.receiveChatMessage2(null,"system",UIStrings.getChatPermissionGrantedMessage2()+this.other.getDisplayName());
			this.otherChatEnabled = true;
			this.sendText.setEnabled(true);
		}
	}
	public	void	onShow()
	{
		this.sendText.setFocus(true);
	}
	
	public String getParsedChatMessage(String chatMessage)
	{
		try
		{
//			String parsedChatMessage = "";
			String hyperLinkParsedMessage = "";
			hyperLinkParsedMessage = this.handleHyperlinkAndLongWords(chatMessage);
//			if(hyperLinkParsedMessage.length() > 0)
//			{
//				String emoticonsParsedMessage = "";				
//				emoticonsParsedMessage = hyperLinkParsedMessage;
//				if(emoticonsParsedMessage.length() > 0)
//				{
//					String longWordParsedMessage = "";
//					longWordParsedMessage = hyperLinkParsedMessage;//this.handleLongWords(emoticonsParsedMessage);
//					
//					if(longWordParsedMessage.length() > 0)
//					{
//						parsedChatMessage = longWordParsedMessage;
//					}
//				}
//			}
			if(hyperLinkParsedMessage.length() > 0)
			{
				return hyperLinkParsedMessage;
			}
			else
			{
				return chatMessage;
			}
		}
		catch(Exception e)
		{
			return chatMessage;
		}
	}
	
	public Vector tokenizeString(String chatMessage)
	{
		Vector st = null;
		String tempMsg = chatMessage;
		
		tempMsg.trim();
		
		if(tempMsg.length() > 0)
		{
			st = new Vector();
			int index = 0;
			int index1 = 0;
			while (true)
			{
				index1 = tempMsg.indexOf(' ', index);
				if(index1 != -1)
				{
					if(index1 > index)
					{
						String buff = tempMsg.substring(index, index1);
						st.add(buff);
						index = index1+1;
					}
					else
					{
						index = index1+1;
					}
				}				
				else
				{
					String buff = tempMsg.substring(index);
					st.add(buff);
					break;
				}
			}
		}
		
		return st;
	}
	private	String	makeLinkBuffer(String realLink, String displayStringSegment)
	{
		String tmpBuff = "";
		//tmpBuff +="<a href=\"";
		tmpBuff +="DIMDIM_LTa href=\"";
		tmpBuff +=realLink;
		//tmpBuff +="\">"+buf+"</a>";
		tmpBuff +="\" target=_blank>"+displayStringSegment+"DIMDIM_LT/a>";
		
		return	tmpBuff;
	}
	private String handleLink1(String message, String prefix, boolean addHttp)
	{
		String parsedChatMessage = "";
		String	realLink = message;
		if (addHttp)
		{
			realLink = "http://"+message;
		}
//		String buf = "";
//		if(message.indexOf(prefix) == 0)
//		{
			int buffLen = message.length();
			if(buffLen > maxAllowedLen)
			{
				int len = 0;
				while(len < buffLen)
				{
					if((buffLen - len) > maxAllowedLen)
					{
						String buf = message.substring(len,len+maxAllowedLen);
						String tmpBuff = this.makeLinkBuffer(realLink, buf);
						parsedChatMessage +=tmpBuff+this.spaceSequence;
						
						len+=maxAllowedLen;
						continue;
					}
					else
					{
						String buf =message.substring(len,buffLen);
						String tmpBuff = this.makeLinkBuffer(realLink, buf);
						parsedChatMessage +=tmpBuff+" ";
														
						break;
					}						
				}
			}
			else
			{
				parsedChatMessage += this.makeLinkBuffer(realLink,message);
			}
			/*
			*/
//			Window.alert("parsedChatMessage = "+parsedChatMessage);
//		}
		return parsedChatMessage;
	}
	private	String	handleLinkWithoutBreaks(String message, String prefix, boolean addHttp)
	{
		String parsedChatMessage = "";
		String	realLink = message;
		if (addHttp)
		{
			realLink = "http://"+message;
		}
		String buf = "";
		
		int buffLen = message.length();
		String tmpBuff = "";
		//tmpBuff +="<a href=\"";
		//tmpBuff +="DIMDIM_LTa href=\"";
		tmpBuff +=realLink;
		//Window.alert("message = "+message);
		if(buffLen > maxAllowedLen)
		{
			int len = 0;
			while(len < buffLen)
			{
				if((buffLen - len) > maxAllowedLen)
				{
					buf = buf + message.substring(len,len+maxAllowedLen)+spaceSequence;
					len+=maxAllowedLen;
					continue;
				}
				else
				{
					buf = buf + message.substring(len,buffLen);
					break;
				}						
			}
		}
		else
		{
			buf = message;
		}
		//tmpBuff +="\" target=_blank>"+buf+"DIMDIM_LT/a>";	
		parsedChatMessage = makeLinkBuffer(realLink, buf);
		
		//Window.alert("after message = "+parsedChatMessage);
		return parsedChatMessage;
	}
	public String handleHyperlinkAndLongWords(String chatMessage)
	{
		
		try
		{		
			String tempMsg = chatMessage;
			String parsedChatMessage = "";
			tempMsg.trim();
			
			//StringTokenizer st = new StringTokenizer(tempMsg);
			//Window.alert("chatMessage = "+chatMessage);
			Vector		st = null;
			st = tokenizeString(chatMessage);
			if(st == null)
				return chatMessage;
						
			for(int i = 0; i < st.size(); i++)
			{
				String buff = (String)st.get(i);

				//Window.alert("buff = "+buff);
				if(buff.indexOf("http://") == 0)
				{
					parsedChatMessage = parsedChatMessage+handleLinkWithoutBreaks(buff, "http://", false);
				}else if(buff.indexOf("https://") == 0)
				{ 
					parsedChatMessage = parsedChatMessage+handleLinkWithoutBreaks(buff, "https://", false);
				}else if(buff.indexOf("www.") == 0)
				{
					parsedChatMessage = parsedChatMessage+handleLinkWithoutBreaks(buff, "www.", true);
				}
				else if (buff.length() > this.maxAllowedLen)
				{
					parsedChatMessage += this.handleLongWord(buff);
				}
				/*else 
				if((buff.indexOf(".com") != -1) || (buff.indexOf(".net") != -1) || (buff.indexOf(".org") != -1) || (buff.indexOf(".us") != -1) || (buff.indexOf(".biz") != -1) || (buff.indexOf(".info") != -1) || (buff.indexOf(".tv") != -1) || (buff.indexOf(".cc") != -1) || (buff.indexOf(".ws") != -1) || (buff.indexOf(".ca") != -1) || (buff.indexOf(".de") != -1) || (buff.indexOf(".jp") != -1) || (buff.indexOf(".co.uk") != -1) || (buff.indexOf(".org.uk") != -1) || (buff.indexOf(".co.nz") != -1) || (buff.indexOf(".net.nz") != -1) || (buff.indexOf(".org.nz") != -1) || (buff.indexOf(".mobi") != -1) || (buff.indexOf(".eu") != -1) || (buff.indexOf(".pro") != -1)) 
				{
					int buffLen = buff.length();
					if(buffLen > maxAllowedLen)
					{
						int len = 0;
						while(len < buffLen)
						{
							if((buffLen - len) > maxAllowedLen)
							{
								String buf = buff.substring(len,len+maxAllowedLen);
								
								String tmpBuff = "";
								//tmpBuff +="<a href=\"";
								tmpBuff +="DIMDIM_LTa href=http://";
								tmpBuff +=buff;
								//tmpBuff +="\">"+buf+"</a>";
								tmpBuff +=" target=_blank>"+buf+"DIMDIM_LT/a>";
								
								parsedChatMessage +=tmpBuff+" ";
								
								len+=maxAllowedLen;
								continue;
							}
							else
							{
								String buf =buff.substring(len,buffLen);
								
								String tmpBuff = "";
								//tmpBuff +="<a href=\"";
								tmpBuff +="DIMDIM_LTa href=http://";
								tmpBuff +=buff;
								//tmpBuff +="\">"+buf+"</a>";
								tmpBuff +=" target=_blank>"+buf+"DIMDIM_LT/a>";
								
								parsedChatMessage +=tmpBuff+" ";
																
								break;
							}						
						}
					}
					else
					{
						String buf = buff;
						
						String tmpBuff = "";
						//tmpBuff +="<a href=\"";
						tmpBuff +="DIMDIM_LTa href=http://";
						tmpBuff +=buff;
						//tmpBuff +="\">"+buf+"</a>";
						tmpBuff +=" target=_blank>"+buf+"DIMDIM_LT/a>";
						
						parsedChatMessage +=tmpBuff+" ";
					}	
				}*/
				else
				{
					parsedChatMessage +=buff+" ";
				}
			}
			
			if(parsedChatMessage.length() > 0)
			{
				return parsedChatMessage;
			}
			else
			{
				return chatMessage;
			}
		}
		catch(Exception e)
		{
			return chatMessage;
		}		
	}
	
	
	public String handleEmoticons(String chatMessage){
		
		String parsedChatMessage = "";
		String tempMsg = chatMessage;
		String tmpBuff = null;	
		//Window.alert("DILIP chatMessage = "+chatMessage);
		StringBuffer sb = new StringBuffer();
		
		//Window.alert("DILIP emoticonsMap = "+emoticonsMap); 
		if(emoticonsMap == null || emoticonsMap.isEmpty())
		{
			prepareEmoticonsList();			
		}
		if(listNamesSorted == null || listNamesSorted.length == 0){
			listNamesSorted = getSortedList(emoticonsMap.keySet());
		}
		tempMsg.trim();
		parsedChatMessage = tempMsg;
		for (int loopCntr = 0; loopCntr < listNamesSorted.length; loopCntr++) {
			String name = new String(listNamesSorted[loopCntr]);
			//Window.alert("name = "+name);
			String value = (String)emoticonsMap.get(name);
			if(ConferenceGlobals.isBrowserIE())
			{	
				tmpBuff = "DIMDIM_LTspan style=DIMDIM_QUOTEdisplay:inline-block;width:" + 16 + "px;height:" + 16 + "px;filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(src='" + value + "', sizingMethod='none' )DIMDIM_QUOTE>DIMDIM_LT/span>";
			}
			else if (ConferenceGlobals.isBrowserFirefox())
			{
				tmpBuff = "DIMDIM_LTimg class=chat-image src="+value+">";
			}
			else if (ConferenceGlobals.isBrowserSafari())
			{
				tmpBuff = "DIMDIM_LTimg class=chat-image src="+value+">";
			}
			int i = 0;
			
			while(tempMsg.indexOf(name) != -1){
				i = tempMsg.indexOf(name);
				parsedChatMessage = tempMsg.substring(0, i);
				parsedChatMessage = parsedChatMessage+tmpBuff;
				parsedChatMessage = parsedChatMessage+tempMsg.substring(i+name.length());
				tempMsg = parsedChatMessage;
			}
			parsedChatMessage = tempMsg;

		}
		
	    return parsedChatMessage;
	}
	
	private String[] getSortedList(Set keyValues) {
		String[] tempList = null;
		String nameCurr = null;
		String temp = null;
		int i = 0;
		int maxLength = keyValues.size();
		
		tempList = new String[maxLength];
		for (Iterator iterator1 = keyValues.iterator(); iterator1.hasNext();) {
			nameCurr = new String((String) iterator1.next());
			tempList[i++] = nameCurr;
		}
		//sorting based on the name length
		for(i = 0; i< maxLength;  i++){
			for(int j = maxLength-1; j > i; j--){
				if(tempList[j-1].length() < tempList[j].length()){
					temp = tempList[j-1];
					tempList[j-1] = tempList[j];
					tempList[j] = temp;
				}
			}
		}
		/*for (int j = 0; j < tempList.length; j++) {
			Window.alert(tempList[j]);
		}*/
		return tempList;
	}

	private String handleLongWord(String chatMessage)
	{
		try
		{	
			String parsedChatMessage = "";
//			String tempMsg = chatMessage;
//			tempMsg.trim();

//			Vector		st = null;
//			st = tokenizeString(chatMessage);
//			if(st == null)
//				return chatMessage;

//			for(int i = 0; i < st.size(); i++)
//			{
				String buff = chatMessage;//(String)st.get(i);
				
//				if((buff.indexOf("href=http:") == -1) && (buff.indexOf("src=")== -1) && (buff.indexOf("target=_blank")== -1))
//				if((buff.indexOf("href=http:") != -1) || (buff.indexOf("src=") != -1))
//				{
					int buffLen = buff.length();
					if(buffLen > maxAllowedLen)
					{
						int len = 0;
						while(len < buffLen)
						{
							if((buffLen - len) > maxAllowedLen)
							{
								parsedChatMessage +=buff.substring(len,len+maxAllowedLen)+spaceSequence;
								len+=maxAllowedLen;
								continue;
							}
							else
							{
								parsedChatMessage +=buff.substring(len,buffLen)+" ";
								break;
							}						
						}
					}
					else
					{
						parsedChatMessage +=buff+" ";
					}
//				}
//				else
//				{
//					parsedChatMessage +=buff+ " ";
//				}
//			}
			
			if(parsedChatMessage.length() > 0)
			{
				return parsedChatMessage;
			}
			else
			{
				return chatMessage;
			}
		}
		catch(Exception e)
		{
			return chatMessage;
		}
	}
	
//	public String getHyperlink(String msg)
//	{
//		return null;
//	}
	
	/**
	 * POPOUT SUPPORT.
	 * 
	 * If the panel is in a popout then the send needs to send the typed
	 * message to the corresponding panel in the console using the popout
	 * panel proxy.
	 * 
	 * As required by the popout
	 * needs to be able to export and import the messages.
	 */
	
	protected	boolean		inConsole = true;
	protected	boolean		inPopout = false;
	protected	boolean		consolePanelPoppedOut = false;
	
	protected	PopoutPanelProxy	popoutPanelProxy;
	
	protected	Vector	bufferedMessages = new Vector();
	
	public	void	bufferMessage(String s, boolean host)
	{
		if (this.inConsole)
		{
			String b = host?"T":"F";
			String c = b+s;
			if (bufferedMessages.size() > 100)
			{
				bufferedMessages.remove(0);
			}
			bufferedMessages.addElement(c);
		}
	}
	public String getPanelData()
	{
		int size = bufferedMessages.size();
		StringBuffer buf = new StringBuffer();
		for (int i=0; i<size; i++)
		{
			String s = (String)bufferedMessages.elementAt(i);
			if (i>0)
			{
				buf.append(" ");
			}
			buf.append(this.encodeBase64(s));
		}
		return buf.toString();
	}
	public String getPanelId()
	{
		return this.toId;
	}
	public boolean isInConsole()
	{
		return this.inConsole;
	}
	public boolean isInPopout()
	{
		return this.inPopout;
	}
	public void panelInConsole()
	{
		this.inConsole = true;
		this.inPopout = false;
	}
	public void panelInPopout()
	{
		this.inConsole = false;
		this.inPopout = true;
	}
	public void panelPopedOut()
	{
		//	No specific action is required here. The container panel set
		//	will be responsible for setting the panel invisible.
	}
	public void panelPoppedIn()
	{
		//	No specific action is required here. The container panel set
		//	will be responsible for setting the panel visible.
	}
	/**
	 * This method should be called on the panel only when it is in a popout
	 * window.
	 */
	public void readPanelData(String dataText)
	{
		if (dataText != null && dataText.length() > 0)
		{
			Vector v = this.tokenizeString(dataText);
			int size = v.size();
			for (int i=0; i<size; i++)
			{
				String s = (String)v.elementAt(i);
				String s2 = this.decodeBase64(s);
				
				boolean b = s2.startsWith("T");
				this.addHtmlHTextToTextBox(s2.substring(1),b);
			}
		}
	}
	public void setPopoutPanelProxy(PopoutPanelProxy popoutPanelProxy)
	{
		this.popoutPanelProxy = popoutPanelProxy;
	}
	public void receiveMessageFromPopout(UIPopoutPanelData msg)
	{
		if (msg != null && msg.getDataText() != null)
		{
			this.receiveMessageFromPopoutPanel(msg.getDataText());
		}
	}
	/**
	 * This method creates a popout panel data object and passes on to the
	 * panel proxy to be sent to the panel in console. The panel in console
	 * is expected to send it to other attendees whether the 
	 * @param msg
	 */
	public	void	sendMessageToConsolePanel(String msg)
	{
		this.popoutPanelProxy.sendMessageToConsole(this.getPanelId(), msg);
	}
	public	void	receiveMessageFromPopoutPanel(String msg)
	{
		this.showAndSendChatMessage(decodeBase64(msg));
	}
	private native void openDimdimWebSite()/*-{
		$wnd.openDimdimWebSite();
	}-*/;
	private	native	String	encodeBase64(String s) /*-{
		return $wnd.Base64.encode(s);
	}-*/;
	private	native	String	decodeBase64(String s) /*-{
		return $wnd.Base64.decode(s);
	}-*/;

}

