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

package com.dimdim.conference.ui.common.client.list;

import pl.rmalinowski.gwt2swf.client.ui.SWFCallableWidget;
import pl.rmalinowski.gwt2swf.client.ui.SWFParams;

import com.dimdim.conference.ui.common.client.util.FixedLengthLabel;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

//import com.google.gwt.user.client.Window;

/**
 * The subtext panels are used for mood messges. The fixed message panel is
 * used for messages that need to remain in the user's view at all times,
 * such as 'You are now broadcasting the audio'.
 * 
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 *
 */
public class ListEntryPanel	extends	Composite implements ListEntryChangeListener, MouseListener, ListEntryShareListener
{
	protected	ListPanel	listPanel;
	
	protected	HorizontalPanel	rootPanel = new HorizontalPanel();
	protected	FocusPanel	focusPanel = new FocusPanel();
	protected	DockPanel	basePanel = new DockPanel();
	protected	HTML		basePanelFiller = new HTML("&nbsp;");
	
	protected	HorizontalPanel	image1Panel = new HorizontalPanel();
	protected	HorizontalPanel	namePanel = new HorizontalPanel();
	protected	HorizontalPanel	image2Panel = new HorizontalPanel();
	protected	HorizontalPanel	image3Panel = new HorizontalPanel();
	protected	HorizontalPanel	image4Panel = new HorizontalPanel();
	protected	HorizontalPanel	image5Panel = new HorizontalPanel();
	
	protected	HorizontalPanel	movie1Panel = new HorizontalPanel();
	protected	HorizontalPanel	movie2Panel = new HorizontalPanel();
	
	protected	Image	image1;
	protected	FixedLengthLabel	nameLabel;
	protected	Image	image2;
	protected	Image	image3;
	protected	Image	image4;
	protected	Image	image5;
	
	protected	SWFCallableWidget	movie1 = null;
	protected	SWFCallableWidget	movie2 = null;
	
	protected	ListEntryPropertiesProvider	popertiesProvider;
	protected	ListEntryControlsProvider	controlsProvider;
	
    protected	ListEntry		listEntry;
	protected	ListEntryPropertiesProvider		listEntryPropertiesProvider;
	protected	ListEntryControlsProvider		listEntryControlsProvider;
	
	protected	boolean		panelVisible = false;
	protected	int			containerIndex;
	String backgroundStyle = null;
	String hoverStyle = "list-entry-panel-hover";
	String hoverSharingStyle = "list-entry-panel-share-hover";
	String hoverSharingStopStyle = "list-entry-panel-stop-hover";
	String shareStyle = "list-entry-panel-share";

	private boolean isSharing;
	
	/**
	 * The panel constructor does not check for movies because the list entries
	 * never have the information is never known at this time.
	 * @param listEntry
	 */
	public ListEntryPanel(ListEntry listEntry)
	{
		initWidget(rootPanel);
		
		this.rootPanel.setStyleName("list-entry-panel");
		
		//this.basePanel.setBorderWidth(1);
		
		this.listEntry = listEntry;
		this.listEntryControlsProvider = listEntry.getListEntryControlsProvider();
		this.listEntryPropertiesProvider = listEntry.getListEntryPropertiesProvider();
		this.listEntryControlsProvider.setListEntryPanel(this);
		
		this.nameLabel = new FixedLengthLabel("", this.listEntryPropertiesProvider.getNameLabelWidth());
		this.nameLabel.setWordWrap(false);
		this.nameLabel.setStyleName("common-text");
		this.nameLabel.addStyleName(this.listEntryPropertiesProvider.getNameLabelStyle());
		
		backgroundStyle = listEntryPropertiesProvider.getListEntryPanelBackgroundStyle();
		if (backgroundStyle != null)
		{
			this.addStyleName(backgroundStyle);
		}
		
		this.image1Panel.setStyleName("list-entry-panel-image-panel");
		this.basePanel.add(this.image1Panel,DockPanel.WEST);
		this.basePanel.setCellHorizontalAlignment(this.image1Panel,HorizontalPanel.ALIGN_LEFT);
		this.basePanel.setCellVerticalAlignment(this.image1Panel,VerticalPanel.ALIGN_MIDDLE);
		
		
		image1 = this.setImage(this.basePanel,image1,listEntry.getImage1Url(),
				this.listEntryControlsProvider.getImage1ClickListener(),image1Panel,
				listEntry.getImage1Tooltip());
		
		this.basePanel.add(this.nameLabel,DockPanel.WEST);
		this.basePanel.setCellVerticalAlignment(this.nameLabel, VerticalPanel.ALIGN_MIDDLE);
		
		this.nameLabel.setText(listEntry.getName());
		
		this.nameLabel.setTitle(listEntry.getName());	
		//Window.alert("adding click listener");
		ClickListener cl = this.listEntryControlsProvider.getNameLabelClickListener();
		if (cl != null)
		{
			//this.focusPanel.addStyleName("anchor-cursor");
			this.focusPanel.addClickListener(cl);
		}
			
		ClickListener ml = this.listEntryControlsProvider.getNameLabelMouseListener();
		if (ml != null)
		{
			this.focusPanel.addClickListener(ml);
		}
		
		//Window.alert("name = "+nameLabel.getText());
		//if(this.listEntry instanceof ResourceListEntry)
		//{
			//Window.alert("adding mouse listener");
			this.focusPanel.addMouseListener(this);
		//}
		
		this.image2Panel.setStyleName("list-entry-panel-image-panel");
		this.basePanel.add(this.image2Panel,DockPanel.WEST);
		this.basePanel.setCellHorizontalAlignment(this.image2Panel,HorizontalPanel.ALIGN_LEFT);
		this.basePanel.setCellVerticalAlignment(this.image2Panel,VerticalPanel.ALIGN_MIDDLE);
		
		image2 = this.setImage(this.basePanel,image2,listEntry.getImage2Url(),
				this.listEntryControlsProvider.getImage2ClickListener(),image2Panel,
				listEntry.getImage2Tooltip());
		
		this.image3Panel.setStyleName("list-entry-panel-image-panel");
		this.basePanel.add(this.image3Panel,DockPanel.WEST);
		this.basePanel.setCellHorizontalAlignment(this.image3Panel,HorizontalPanel.ALIGN_LEFT);
		this.basePanel.setCellVerticalAlignment(this.image3Panel,VerticalPanel.ALIGN_MIDDLE);
		
		image3 = this.setImage(this.basePanel,image3,listEntry.getImage3Url(),
		this.listEntryControlsProvider.getImage3ClickListener(),image3Panel,
		listEntry.getImage3Tooltip());

		image4 = this.setImage(this.basePanel,image4,listEntry.getImage4Url(),
				ml,null,
				listEntry.getImage4Tooltip(), true);
		
		//this.basePanel.setCellHorizontalAlignment(image4, DockPanel.WEST);
		
		image5 = this.setImage(this.basePanel,image5,listEntry.getImage5Url(),
				this.listEntryControlsProvider.getImage5ClickListener(),null,
				listEntry.getImage5Tooltip());
		
		this.basePanel.add(this.basePanelFiller,DockPanel.CENTER);
		this.basePanel.setCellWidth(this.basePanelFiller,"100%");
		
		listEntry.setChangeListener(this);
		listEntry.setShareListener(this);
		
		this.focusPanel.add(this.basePanel);
		this.rootPanel.add(this.focusPanel);
		this.rootPanel.setCellHorizontalAlignment(this.focusPanel,HorizontalPanel.ALIGN_LEFT);
		this.rootPanel.setCellVerticalAlignment(this.focusPanel,VerticalPanel.ALIGN_MIDDLE);
		this.focusPanel.setTitle(listEntry.getName());	
	}
	public ListPanel getListPanel()
	{
		return listPanel;
	}
	public void setListPanel(ListPanel listPanel)
	{
		this.listPanel = listPanel;
	}
	public int getContainerIndex()
	{
		return containerIndex;
	}
	public void setContainerIndex(int containerIndex)
	{
		this.containerIndex = containerIndex;
	}
	public boolean isPanelVisible()
	{
		return panelVisible;
	}
	public void setPanelVisible(boolean panelVisible)
	{
		this.panelVisible = panelVisible;
	}
	/**
	 * This method always clears out the current image if one already exists.
	 * 
	 * @param imagePanel
	 * @param imageUrl
	 */
//	private	Image	setImage(DockPanel imagePanel, Image image,
//			Image imageUrl, ClickListener clickListener, String tooltip)
//	{
//		return	this.setImage(imagePanel,image,imageUrl,clickListener,null,tooltip, false);
//	}
	

	/**
	 * This uses a new signature which takes a param based on which the iamge will be aligned east or not
	 * @param imagePanel
	 * @param image
	 * @param imageUrl
	 * @param clickListener
	 * @param subPanel
	 * @param tooltip
	 * @return
	 */
	protected	Image	setImage(DockPanel imagePanel, Image image,
			Image imageUrl, ClickListener clickListener, HorizontalPanel subPanel, String tooltip)
	{
		return	this.setImage(imagePanel,image,imageUrl,clickListener,subPanel,tooltip, false);
	}
	
	protected	Image	setImage(DockPanel imagePanel, Image image,
			Image imageUrl, ClickListener clickListener, HorizontalPanel subPanel, String tooltip, boolean rightAlign)
	{
		Image image2 = image;
		if (image != null)
		{
			//Window.alert("prev image = is not null");
			if (subPanel != null)
			{
				//Window.alert("subpanel = is not null so removing..");
				subPanel.remove(image);
				image2 = null;
			}else if(imagePanel != null){
				imagePanel.remove(image);
				image2 = null;
			}

			
		}
		if (imageUrl != null )
		{
			/*
			if (imageUrl.endsWith("xxx"))
			{
				Image image = new Image(imageUrl);
				image.addStyleName("list-entry-panel-image");
				imagePanel.add(image,DockPanel.WEST);
				imagePanel.setCellHorizontalAlignment(image,HorizontalPanel.ALIGN_CENTER);
				imagePanel.setCellVerticalAlignment(image,VerticalPanel.ALIGN_MIDDLE);
				if (clickListener != null)
				{
					image.addClickListener(clickListener);
				}
			}
			else
			*/
//			else
			{
				image2 = imageUrl;
				image2.addStyleName("list-entry-panel-image");
				if (subPanel != null)
				{
					subPanel.add(image2);
					subPanel.setCellHorizontalAlignment(image2,HorizontalPanel.ALIGN_CENTER);
					subPanel.setCellVerticalAlignment(image2,VerticalPanel.ALIGN_MIDDLE);
				}
				else
				{
					if(rightAlign)
					{
						imagePanel.add(image2,DockPanel.EAST);
						imagePanel.setCellHorizontalAlignment(image2,HorizontalPanel.ALIGN_RIGHT);
						imagePanel.setCellVerticalAlignment(image2,VerticalPanel.ALIGN_MIDDLE);
					}else
					{
						imagePanel.add(image2,DockPanel.WEST);
						imagePanel.setCellHorizontalAlignment(image2,HorizontalPanel.ALIGN_CENTER);
						imagePanel.setCellVerticalAlignment(image2,VerticalPanel.ALIGN_MIDDLE);
					}
				}
				if (clickListener != null)
				{
					image2.addClickListener(clickListener);
					//image2.addStyleName("anchor-cursor");
				}
				if (tooltip != null)
				{
					image2.setTitle(tooltip);
				}
			}
		}
		return	image2;
	}
	public void displayRankChanged(int previousDisplayRank, int newDisplayRank)
	{
		this.listPanel.moveEntryPanelSection(this,previousDisplayRank,newDisplayRank);
		//changing the background
		this.removeStyleName("host-list-entry-panel-background");
		this.removeStyleName("active-presenter-list-entry-panel-background");
		this.removeStyleName("self-list-entry-panel-background");
		this.removeStyleName("user-list-entry-panel-background");
		backgroundStyle = listEntryPropertiesProvider.getListEntryPanelBackgroundStyle();
		if (backgroundStyle != null)
		{
			this.addStyleName(backgroundStyle);
		}
	}
	public FixedLengthLabel getNameLabel()
	{
		return nameLabel;
	}
	public ListEntry	getListEntry()
	{
		return	this.listEntry;
	}
	public void image1UrlChanged(Image newUrl)
	{
		image1 = this.setImage(this.basePanel,image1,newUrl,
			this.listEntryControlsProvider.getImage1ClickListener(),image1Panel,
			listEntry.getImage1Tooltip());
	}
	public void image2UrlChanged(Image newUrl)
	{
		image2 = this.setImage(this.basePanel,image2,newUrl,
				this.listEntryControlsProvider.getImage2ClickListener(),image2Panel,
				listEntry.getImage2Tooltip());
	}
	public void image3UrlChanged(Image newUrl)
	{
		image3 = this.setImage(this.basePanel,image3,newUrl,
				this.listEntryControlsProvider.getImage3ClickListener(),image3Panel,
				listEntry.getImage3Tooltip());
	}
	
	/*public void image3UrlChanged(Image newUrl, boolean alignRight)
	{
		//Window.alert("image 5 url changed..alignRight = "+alignRight+" name = "+this.nameLabel.getText());//+"image5 = "+image5+" newUrl = "+newUrl);
		image3 = this.setImage(this.basePanel,image3,newUrl,
				this.listEntryControlsProvider.getImage3ClickListener(),image3Panel,
				listEntry.getImage3Tooltip(), alignRight);
	}*/
	
	public void image4UrlChanged(Image newUrl)
	{
		image4 = this.setImage(this.basePanel,image4,newUrl,
				this.listEntryControlsProvider.getImage4ClickListener(),null,
				listEntry.getImage4Tooltip());
	}
	public void image5UrlChanged(Image newUrl)
	{
		//Window.alert("image 5 url changed..name = "+this.nameLabel.getText()+" prev image = "+image5+" newimage = "+newUrl);
		image5 = this.setImage(this.basePanel,image5,newUrl,
				this.listEntryControlsProvider.getImage5ClickListener(),null,
				listEntry.getImage5Tooltip());
	}
	
	public void image5UrlChanged(Image newUrl, boolean alignRight)
	{
		//Window.alert("image 5 url changed..name = "+this.nameLabel.getText()+"prev image = "+image5+" new image = "+newUrl);
		image5 = this.setImage(this.basePanel,image5,newUrl,
				this.listEntryControlsProvider.getImage5ClickListener(),null,
				listEntry.getImage5Tooltip(), alignRight);
	}
	
	public void movieModel1Changed(ListEntryMovieModel oldMovie, ListEntryMovieModel newMovie)
	{
		if (this.movie1 != null)
		{
			try
			{
				this.movie1.call("stopBroadcastingOrReceiving");
			}
			catch(Exception e)
			{
//				Window.alert("Exception while removing attendee player: "+
//						this.nameLabel.getText()+", audio player id:"+this.movie1.getName()+
//						", exception:"+e.getMessage());
			}
			try
			{
				this.basePanel.remove(movie1);
			}
			catch(Exception e)
			{
				//	Just in case 
			}
		}
		this.movie1 = null;
		if (newMovie != null)
		{
			try
			{
				SWFParams wbWidgetParams = new SWFParams(newMovie.getSwfUrl(),newMovie.getWidth(),newMovie.getHeight(),newMovie.getColor());
				wbWidgetParams.setWmode("transparent");
				this.movie1 = new SWFCallableWidget(wbWidgetParams);
//				this.movie1 = new DmFlashWidget2(newMovie.getId(),newMovie.getName(),newMovie.getHeight()+"",
//					newMovie.getWidth()+"",newMovie.getSwfUrl(),newMovie.getColor());
				this.basePanel.add(this.movie1,DockPanel.EAST);
//				this.movie1.show();
			}
			catch(Exception e)
			{
//				Window.alert("Exception while adding attendee player: "+
//				this.nameLabel.getText()+", audio player id:"+this.movie1.getName()+
//				", exception:"+e.getMessage());
				
//				this.movie1 = null;
			}
		}
	}
	
	public void onStartShare() {
		//Window.alert("removing bkground styl "+backgroundStyle +" adding hover style "+hoverStyle);
		//Window.alert("on start share.."+listEntry.getName());
		removeAllStyles();
		this.addStyleName(shareStyle);
		isSharing = true;
		
	}
	public void onStopShare() {
		//Window.alert("adding bkground styl "+backgroundStyle +" removing hover style "+hoverStyle);
		//Window.alert("on stop share.."+listEntry.getName());
		removeAllStyles();
		if(null != backgroundStyle)
		{
			this.addStyleName(backgroundStyle);
		}
		isSharing = false;
	}
	
	public void movieModel2Changed(ListEntryMovieModel oldMovie, ListEntryMovieModel newMovie)
	{
	}
	public void nameChanged(String newName)
	{
		this.nameLabel.setText(newName);
		this.nameLabel.setTitle(newName);
	}
	
	public void onMouseDown(Widget sender, int x, int y) {
		// TODO Auto-generated method stub
		
	}
	public void onMouseEnter(Widget sender) {
		//Window.alert("removing bkground styl "+backgroundStyle +" adding hover style "+hoverStyle);
		removeAllStyles();
		if(isSharing)
		{
			this.addStyleName(hoverSharingStyle);
		}else{
			this.addStyleName(hoverSharingStopStyle);
		}
		
	}
	public void onMouseLeave(Widget sender) {
		removeAllStyles();
		if(isSharing)
		{
			this.addStyleName(shareStyle);
		}else{
			if(null != backgroundStyle)
			{
				this.addStyleName(backgroundStyle);
			}
		}
		
	}
	
	private void removeAllStyles(){
		if(null != backgroundStyle)
		{
			this.removeStyleName(backgroundStyle);
		}
		this.removeStyleName(shareStyle);
		this.removeStyleName(hoverStyle);
		this.removeStyleName(hoverSharingStopStyle);
		this.removeStyleName(hoverSharingStyle);
	}
	
	public void onMouseMove(Widget sender, int x, int y) {
		// TODO Auto-generated method stub
		
	}
	public void onMouseUp(Widget sender, int x, int y) {
		// TODO Auto-generated method stub
		
	}
}

