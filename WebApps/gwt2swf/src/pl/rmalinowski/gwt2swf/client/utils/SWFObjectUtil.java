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

package pl.rmalinowski.gwt2swf.client.utils;

import com.google.gwt.core.client.JavaScriptObject;

public class SWFObjectUtil {

	private static native JavaScriptObject _getPlayerVersion() /*-{
		return $wnd.deconcept.SWFObjectUtil.getPlayerVersion();
	}-*/;

	private static PlayerVersion playerVersion = null;

	/**
	 * 
	 * @return flash player version
	 */
	public static PlayerVersion getPlayerVersion() {
		if (playerVersion == null) {
			JavaScriptObject o = _getPlayerVersion();
			playerVersion = new PlayerVersion();
			playerVersion.deserialize(o);
		}
		return playerVersion;
	}

	/**
	 * check if current player version is valid
	 * @param neededVersion 
	 * @return 
	 */
	public static boolean isVersionIsValid(PlayerVersion neededVersion) {
		return getPlayerVersion().versionIsValid(neededVersion);
	}
	
	/**
	 * check if flash player is installed
	 * @return true if flash player is installed false if is not
	 */
	public static boolean isFlashPlayerInstalled() {
		return !SWFObjectUtil.getPlayerVersion().toString().equals("0.0.0");
	}

}
