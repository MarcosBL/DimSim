
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

//import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;

public class SWFCallableWidget extends SWFWidget{

	public SWFCallableWidget() {
		super();
	}

	public SWFCallableWidget(SWFParams desc) {
		super(desc);
	}
	
    public SWFCallableWidget(SWFParams params, String id, String divId, String innerElementText)
    {
    	super(params,id,divId,innerElementText);
    }
//	To działa prawidłowo 
	private native int _call(Element flashObject,String methodName)/*-{
		if (flashObject[methodName])
		{
 	 		flashObject[methodName]();
 	 		return 1;
		}
		else
		{
			return 0;
		}
	}-*/;
	
	private native int _call_p1(Element flashObject,String methodName,int p1)/*-{
		if (flashObject[methodName])
		{
	 	 	flashObject[methodName](p1);
	 	 	return 1;
		}
		else
		{
			return 0;
		}
	}-*/;
		
	//To dzilaa prawidlowo
	private native void  _callForFlashInfo(Element flashObject) /*-{
		var flash = flashObject;
		var flashInfo = flash.getFlashInfo();
		var str = "";
		for(var key in flashInfo) {
			str += key + ": " + flashInfo[key] + "\n";
		}
		$wnd.alert(str);

	}-*/;

	//	Returns 0 if the flash object does not have the said method.
	
	public int call(String methodName) {
		
		//_callForFlashInfo(DOM.getElementById(super.getSwfId()));
		
		return	_call(DOM.getElementById(super.getSwfId()), methodName);
//		_call(DOM.getElementById(super.getSwfId()), "getFlashInfo");
	}
	
	//	Returns 0 if the flash object does not have the said method.
	
	public int callP1(String methodName, int p1) {
		
		//_callForFlashInfo(DOM.getElementById(super.getSwfId()));
		
		return	_call_p1(DOM.getElementById(super.getSwfId()), methodName, p1);
//		_call(DOM.getElementById(super.getSwfId()), "getFlashInfo");
	}
}
