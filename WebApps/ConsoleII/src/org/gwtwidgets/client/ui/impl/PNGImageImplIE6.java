/*
 * Copyright 2006 Robert Hanson <iamroberthanson AT gmail.com>
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.gwtwidgets.client.ui.impl;

import org.gwtwidgets.client.ui.PNGImage;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;


public class PNGImageImplIE6 extends PNGImageImpl
{
    private String url;
    private boolean isPng;

    
    public Element createElement (String url, int width, int height)
    {
        this.url = url;
    
        if (url.endsWith(".png") || url.endsWith(".PNG")) {
            isPng = true;
        }
        else {
            isPng = false;
        }
        
        if (isPng) {
            Element div = DOM.createDiv();
            DOM.setInnerHTML(div, "<span style=\"display:inline-block;width:" + width + "px;height:" + height + "px;filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(src='" + url + "', sizingMethod='none')\"></span>");

            return DOM.getFirstChild(div);
        }
        else {
            return super.createElement(url, width, height);
        }
    }

    public String getUrl (PNGImage image)
    {
        if (isPng) {
            return url;
        }
        else {
            return super.getUrl(image);
        }
    }

    
}
