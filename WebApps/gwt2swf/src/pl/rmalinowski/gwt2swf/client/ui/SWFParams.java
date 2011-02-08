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

import java.util.HashMap;
import java.util.Map;

import pl.rmalinowski.gwt2swf.client.utils.PlayerVersion;

//http://www.adobe.com/cfusion/knowledgebase/index.cfm?id=tn_12701
public class SWFParams {
    // swf, id, w, h, ver, c, useExpressInstall, quality, xiRedirectUrl,
    // redirectUrl, detectKey
    public static final String DEFAULT_BGCOLOR = "ffffff";

    public static final String DEFAULT_INNER_DIV_TEXT_FOR_FLASH_PLAYER_NOT_FOUND = "Here should be a swf movieclip. "
            + "You probably don't have FlashPlayer installed or have a version lower than $flashPlayer.version.";

    private String innerTextDivForFlashPlayerNotFound = DEFAULT_INNER_DIV_TEXT_FOR_FLASH_PLAYER_NOT_FOUND;

    String src;

    String width;

    String height;
    
    String wmode = "opaque";

    PlayerVersion version = new PlayerVersion(7, 0, 14); // = "7.0.14";

    String bgcolor = DEFAULT_BGCOLOR;

    /**
     */
    Map<java.lang.String,java.lang.String> vars = new HashMap<java.lang.String,java.lang.String>();

    /**
     */
    Map<java.lang.String, java.lang.String> params = new HashMap<java.lang.String, java.lang.String>();

    String quality;

    String xiRedirectUrl, redirectUrl, detectKey;

    public SWFParams() {
    }

    public SWFParams(String src, int width, int height) {
        this(src, width, height, DEFAULT_BGCOLOR);
    }

    public SWFParams(String src, Integer width, Integer height) {
        this(src, width.intValue(), height.intValue(), DEFAULT_BGCOLOR);
    }

    public SWFParams(String src, String width, String height) {
        this(src, width, height, DEFAULT_BGCOLOR);
    }

    public SWFParams(String src, int width, int height, String bgcolor) {
        super();
        setSrc(src);
        setPixelSize(width, height);
        setBgcolor(bgcolor);
    }

    public SWFParams(String src, Integer width, Integer height, String bgcolor) {
        this(src, width.intValue(), height.intValue(), bgcolor);
    }

    public SWFParams(String src, String width, String height, String bgcolor) {
        super();
        setSrc(src);
        setWidth(width);
        setHeight(height);
        setBgcolor(bgcolor);
    }

    public String getBgcolor() {
        return bgcolor;
    }

    public void setBgcolor(String bgcolor) {
        if (bgcolor == null)
            throw new NullPointerException();
        this.bgcolor = bgcolor;
    }

    public String getDetectKey() {
        return detectKey;
    }

    public void setDetectKey(String detectKey) {
        this.detectKey = detectKey;
    }

    public String getHeight() {
        return height;
    }

    /**
     * @return vars
     */
    public Map<java.lang.String, java.lang.String> getParams() {
        return params;
    }

    /**
     */
    public void setParams(Map<java.lang.String, java.lang.String> params) {
        this.params = params;
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    /**
     * @return vars
     */
    public Map<java.lang.String,java.lang.String> getVars() {
        return (vars == null) ? vars = new HashMap<java.lang.String,java.lang.String>() : vars;
    }

    /**
     */
    public void setVars(Map<java.lang.String,java.lang.String> vars) {
        this.vars = vars;
    }

    public void addVar(String key, String value) {
        getVars().put(key, value);
    }

    public PlayerVersion getVersion() {
        return version;
    }

    public void setVersion(PlayerVersion version) {
        this.version = version;
    }

    public String getWidth() {
        return width;
    }

    public String getXiRedirectUrl() {
        return xiRedirectUrl;
    }

    public void setXiRedirectUrl(String xiRedirectUrl) {
        this.xiRedirectUrl = xiRedirectUrl;
    }

    public String getWmode()
	{
		return wmode;
	}

	public void setWmode(String wmode)
	{
		this.wmode = wmode;
	}

	/**
     * Sets the swf object's height.
     * 
     * @param height
     *            the swf object's new height, in CSS units (e.g. "10px", "2em" ,
     *            "100%")
     * @throws RuntimeException
     *             if height < 0
     */
    public void setHeight(String height) {
        if (!(parseLength(height.trim().toLowerCase()) >= 0))
            throw new RuntimeException("CSS heights should not be negative");
        this.height = height;
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
        if (width >= 0) {
            setWidth(width + "px");
        }
        if (height >= 0) {
            setHeight(height + "px");
        }
    }

    /**
     * Sets the swf object's size.
     * 
     * @param width
     *            the swf object's new width, in CSS units (e.g. "10px", "2em",
     *            "100%")
     * @param height
     *            the swf object's new height, in CSS units (e.g. "10px", "2em",
     *            "100%")
     */
    public void setSize(String width, String height) {
        setWidth(width);
        setHeight(height);
    }

    /**
     * Sets the swf object's width.
     * 
     * @param width
     *            the swf object's new width, in CSS units (e.g. "10px", "2em",
     *            "100%")
     * @throws RuntimeException
     *             if width < 0
     */
    public void setWidth(String width) {
        if (!(parseLength(width.trim().toLowerCase()) >= 0))
            throw new RuntimeException("CSS widths should not be negative");
        this.width = width;
    }

    /**
     * @return the innerTextDivForFlashPlayerNotFound
     */
    public String getInnerTextDivForFlashPlayerNotFound() {
        return innerTextDivForFlashPlayerNotFound;
    }

    /**
     * @param innerTextDivForFlashPlayerNotFound the innerTextDivForFlashPlayerNotFound to set
     */
    public void setInnerTextDivForFlashPlayerNotFound(
            String innerTextDivForFlashPlayerNotFound) {
        this.innerTextDivForFlashPlayerNotFound = innerTextDivForFlashPlayerNotFound;
    }
    
    private native double parseLength(String s) /*-{      
    return parseFloat(s);      
}-*/;
}
