package com.dimdim.conference.ui.common.client.util;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ImageNTextWidget extends Composite
{
	protected	HorizontalPanel	basePanel = new HorizontalPanel();
	//protected	HorizontalPanel	labelPanel = new HorizontalPanel();
	
	protected	Image	image;
	protected	ClickListener listener;
	protected	Label	line1;
	boolean imageOnRight = false;
	
	public	ImageNTextWidget(String label1, Image image)
	{
		initWidget(basePanel);
		this.line1 = new Label(label1);
		//this.basePanel.setStyleName("anchor-cursor");
		//basePanel.setBorderWidth(1);
		this.basePanel.add(this.line1);
		this.line1.setStyleName("console-workspace-top-link");
		this.line1.addStyleName("text-margin");
		//this.line1.addStyleName("anchor-cursor");
		this.basePanel.setCellHorizontalAlignment(this.line1,HorizontalPanel.ALIGN_LEFT);
		this.basePanel.setCellVerticalAlignment(this.line1,VerticalPanel.ALIGN_MIDDLE);
		basePanel.setCellWidth(line1, "100%");
		
		this.image = image;
		this.basePanel.add(image);
		this.image.setStyleName("image-margin");
		this.image.addStyleName("anchor-cursor");
		basePanel.setCellWidth(image, "100%");
		this.basePanel.setCellHorizontalAlignment(image,HorizontalPanel.ALIGN_RIGHT);
		this.basePanel.setCellVerticalAlignment(image,VerticalPanel.ALIGN_MIDDLE);
		
		
	}
	
	public	ImageNTextWidget(String label1, Image image, boolean imageOnRight)
	{
		initWidget(basePanel);
		this.imageOnRight = imageOnRight;
		this.line1 = new Label(label1);
		//this.basePanel.setStyleName("console-top-panel-link");
		
		
		this.line1.setStyleName("console-workspace-top-link");
		this.line1.addStyleName("text-margin");
		//this.line1.addStyleName("anchor-cursor");
		
		this.image = image;
		this.image.setStyleName("image-margin");
		this.image.addStyleName("anchor-cursor");
		if(imageOnRight)
		{
			this.basePanel.add(this.line1);
			//basePanel.setCellWidth(line1, "100%");
			
			this.basePanel.add(image);
			//basePanel.setCellWidth(image, "100%");
			
			/*this.basePanel.setCellHorizontalAlignment(this.line1,HorizontalPanel.ALIGN_LEFT);
			this.basePanel.setCellVerticalAlignment(this.line1,VerticalPanel.ALIGN_MIDDLE);
			this.basePanel.setCellHorizontalAlignment(image,HorizontalPanel.ALIGN_RIGHT);
			this.basePanel.setCellVerticalAlignment(image,VerticalPanel.ALIGN_MIDDLE);	*/
		}else{
			this.basePanel.add(image);
			//basePanel.setCellWidth(image, "100%");
			
			this.basePanel.add(this.line1);
			//basePanel.setCellWidth(line1, "100%");
			
			/*this.basePanel.setCellHorizontalAlignment(this.line1,HorizontalPanel.ALIGN_RIGHT);
			this.basePanel.setCellVerticalAlignment(this.line1,VerticalPanel.ALIGN_MIDDLE);
			this.basePanel.setCellHorizontalAlignment(image,HorizontalPanel.ALIGN_LEFT);
			this.basePanel.setCellVerticalAlignment(image,VerticalPanel.ALIGN_MIDDLE);*/
		}
		
	}
	
	public	ImageNTextWidget(Label label1, Image image, boolean imageOnRight)
	{
		initWidget(basePanel);
		this.imageOnRight = imageOnRight;
		this.line1 = label1;
		//this.basePanel.setStyleName("console-top-panel-link");
		
		
		this.line1.setStyleName("console-workspace-top-link");
		this.line1.addStyleName("text-margin");
		this.line1.addStyleName("anchor-cursor");
				
		this.image = image;
		this.image.setStyleName("image-margin");
		this.image.addStyleName("anchor-cursor");
		if(imageOnRight)
		{
			this.basePanel.add(this.line1);
			//basePanel.setCellWidth(line1, "100%");
			
			this.basePanel.add(image);
			//basePanel.setCellWidth(image, "100%");
		}else{
			this.basePanel.add(image);
			//basePanel.setCellWidth(image, "100%");
			
			this.basePanel.add(this.line1);
			//basePanel.setCellWidth(line1, "100%");
		}
		
	}
	
	public	ImageNTextWidget(String label1, Image image, ClickListener listener)
	{
		this(label1, image);
		if(null != listener)
		{
			this.listener = listener;
			this.line1.addClickListener(listener);
			this.image.addClickListener(listener);
		}
	}
	
	public	ImageNTextWidget(String label1, Image image, ClickListener listener, boolean imageOnRight)
	{
		this(label1, image, imageOnRight);
		if(null != listener)
		{
			this.listener = listener;
			this.line1.addClickListener(listener);
			this.image.addClickListener(listener);
			this.image.addStyleName("anchor-cursor");
		}
	}
	
	public	ImageNTextWidget(Label label1, Image image, ClickListener listener, boolean imageOnRight)
	{
		this(label1, image, imageOnRight);
		if(null != listener)
		{
			this.line1.addClickListener(listener);
			this.image.addClickListener(listener);
		}
	}
	
	public Label getLabel()
	{
		return	this.line1;
	}
	
	public void setText(String text)
	{
		this.line1.setText(text);
	}

	public Image getImage() {
		return image;
	}
	
	public void addClickListener(ClickListener listener)
	{
		if(null != listener)
		{
			this.listener = listener;
			this.line1.addClickListener(listener);
			this.image.addClickListener(listener);
			this.image.addStyleName("anchor-cursor");
		}
	}
	
	public void removeClickListener(ClickListener listener)
	{
		if(null != listener)
		{
			this.listener = null;
			this.line1.removeClickListener(listener);
			this.image.removeClickListener(listener);
			this.image.removeStyleName("anchor-cursor");
		}
	}

	public void setImage(Image image) {
		basePanel.remove(this.image);
		this.image = image;
		this.basePanel.add(image);
		if (this.listener != null)
		{
			this.image.addClickListener(this.listener);
			this.image.addStyleName("anchor-cursor");
		}
	}
}
