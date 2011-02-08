/*
 *    Copyright 2007 Rafal M.Malinowski
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *   
 */


package pl.rmalinowski.gwt2swf.client.ui;

import java.util.Map;

import pl.rmalinowski.gwt2swf.client.ui.exceptions.UnsupportedFlashPlayerVersionException;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * @author Rafal M.Malinowski
 * 
 */
public class SWFWidget extends Widget {

    private final SWFParams swfParams;

    private static int count = 0;

    private final static String divPrefix = "swfDivID_";

    private final String swfDivId;

    private final static String idPrefix = "swfID_";

    private final String swfId;

    private boolean isSWFInjected = false;

    public SWFWidget() {
        this(new SWFParams());
    }

    // var desc = this.@pl.rmalinowski.gwtswf.client.ui::swfParams;
    // alert($doc.getElementById(divId).innerHTML);
    protected native boolean injectSWF(String divId, String swf, String id,
            String w, String h, String ver, String c, String wmode,
            Map/* <String,String> */vars) /*-{       	  
                            	 
                            var so = new $wnd.deconcept.SWFObject(swf, id, w, h, ver,c);
                            if(vars!=null){
                           	iterator=vars.@java.util.Map::keySet()().@java.util.Set::iterator()();
                               var key,value;
                           	while(iterator.@java.util.Iterator::hasNext()()) {
                           		key=iterator.@java.util.Iterator::next()();
                           	    value=vars.@java.util.Map::get(Ljava/lang/Object;)(key);
                           	    so.addVariable(key,value);
                           	}
                            }
                            so.addParam("allowScriptAccess","always");
                            so.addParam("swLiveConnect","true");
                            if (wmode != "")
                            {
                            	so.addParam("wmode",wmode);
                         	}
                            return so.write(divId);
                    }-*/;

    public SWFWidget(String src, Integer width, Integer height) {
        this(new SWFParams(src, width, height));
    }

    public SWFWidget(String src, Integer width, Integer height, String bgcolor) {
        this(new SWFParams(src, width, height, bgcolor));
    }
    
    public SWFWidget(String src, int width, int height) {
        this(new SWFParams(src, width, height));
    }
   
    public SWFWidget(String src, String width, String height) {
        this(new SWFParams(src, width, height));
    }

    public SWFWidget(String src, int width, int height, String bgcolor) {
        this(new SWFParams(src, width, height, bgcolor));
    }

    public SWFWidget(String src, String width, String height, String bgcolor) {
        this(new SWFParams(src, width, height, bgcolor));
    }

    public SWFWidget(SWFParams params) {
    	this(params,null,null,null);
    }
    public SWFWidget(SWFParams params, String id, String divId, String innerElementText) {
        swfParams = params;
        if (id != null)
        {
        	swfId = id;
        }
        else
        {
            swfId = idPrefix + count;
        }
        if (divId != null)
        {
        	swfDivId = divId;
        }
        else
        {
            swfDivId = divPrefix + count;
        }
        ++count;
        Element element = DOM.createElement("div");
        DOM.setElementProperty(element, "id", swfDivId);
        if (innerElementText != null)
        {
        	DOM.setInnerText(element, innerElementText);
        }
        else
        {
        	DOM.setInnerText(element, swfParams
                .getInnerTextDivForFlashPlayerNotFound().replaceAll(
                        "\\$flashPlayer.version", params.getVersion().toString()));
        }
        setElement(element);
        // GWT.log("Created with id " + swfId, null);
    }

    /**
     * @throws UnsupportedFlashPlayerVersionException
     */
    protected void onLoad() {
        if (!isSWFInjected) {
            onBeforeSWFInjection();
            if (!injectSWF(getSwfDivId(), swfParams.getSrc(), getSwfId(),
                    swfParams.getWidth(), swfParams.getHeight(), swfParams
                            .getVersion().toString(), swfParams.getBgcolor(),
                            swfParams.getWmode(),
                    swfParams.getVars()))
                throw new UnsupportedFlashPlayerVersionException();
            isSWFInjected = true;
            onAfterSWFInjection();
        }
        super.onLoad();
    }

    /**
     * Override this method to catch information about swf injected.
     * The default implementation does nothing and need not be called by
     * subclasses.
     */
    protected void onAfterSWFInjection() {

    }
    
    /**
     * Override this method to catch information about swf injection.
     * The default implementation does nothing and need not be called by
     * subclasses.
     */
    protected void onBeforeSWFInjection() {

    }

    protected void onUnload() {
        super.onUnload();
    }

    /**
     * 
     * @throws UnsupportedFlashPlayerVersionException
     * @Deprecated use setVisible(true)
     */
    public void show() throws UnsupportedFlashPlayerVersionException {
        setVisible(true);
    }

    /**
     * 
     * @Deprecated use setVisible(false)
     */
    public void hide() {
        setVisible(false);
    }

    protected String getSwfDivId() {
        return swfDivId;
    }

    protected String getSwfId() {
        return swfId;
    }

    /**
     * @return the swfParams
     */
    public SWFParams getSwfParams() {
        return swfParams;
    }

    /**
     * Sets the swf object's height.
     * 
     * @param height
     *            the swf object's new height, in CSS units (e.g. "10px", "1em" ,
     *            "100%")
     */
    public void setHeight(String height) {
        if (isSWFInjected)
            throw getUnspportedSizeChangingException("height");
        super.setHeight(height);
    }

    /**
     * Sets the swf object's size, in pixels.
     * 
     * @param width
     *            the swf object's new width, in pixels
     * @param height
     *            the swf object's new height, in pixels
     */
    public void setPixelSize(int width, int height) {
        if (isSWFInjected)
            throw getUnspportedSizeChangingException("size");
        super.setPixelSize(width, height);
    }

    /**
     * Sets the swf object's size.
     * 
     * @param width
     *            the swf object's new width, in CSS units (e.g. "10px", "1em",
     *            "100%")
     * @param height
     *            the swf object's new height, in CSS units (e.g. "10px", "1em",
     *            "100%")
     */
    public void setSize(String width, String height) {
        if (isSWFInjected)
            throw getUnspportedSizeChangingException("size");
        super.setSize(width, height);
    }

    /**
     * Sets the swf object's width.
     * 
     * @param width
     *            the swf object's new width, in CSS units (e.g. "10px", "1em",
     *            "100%")
     */
    public void setWidth(String width) {
        if (isSWFInjected)
            throw getUnspportedSizeChangingException("width");
        super.setWidth(width);
    }

    private UnsupportedOperationException getUnspportedSizeChangingException(
            String msg) {
        return new UnsupportedOperationException("Seting " + msg
                + " of SWFWidget after it was generated"
                + " is not implemented yet. Please change it"
                + " before SWFWidget will be attached to RootPanel.");

    }

}
