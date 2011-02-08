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

public class PlayerVersion {
    int major, minor, rev;
    
    public native void deserialize(JavaScriptObject o) /*-{
      //$wnd.alert(" major: " + o.major + " minor: " + o.minor + " rev: " + o.rev +  "");
      this.@pl.rmalinowski.gwt2swf.client.utils.PlayerVersion::setMajor(I)(o.major);
      this.@pl.rmalinowski.gwt2swf.client.utils.PlayerVersion::setMinor(I)(o.minor);
      this.@pl.rmalinowski.gwt2swf.client.utils.PlayerVersion::setRev(I)(o.rev);
    	
    }-*/;

    public PlayerVersion(int[] version) {
        int l = version.length >= 3 ? 3 : version.length;
        switch (l) {
        case 3:
            rev = version[2];
            minor = version[1];
            major = version[0];
            break;
        case 2:
            minor = version[1];
            major = version[0];
            break;
        case 1:
            major = version[0];
            break;
        }
    }

    public PlayerVersion(int major, int minor, int rev) {
        this(major, minor);
        this.rev = rev;
    }

    public PlayerVersion(int major, int minor) {
        this(major);
        this.minor = minor;
    }

    public PlayerVersion(int major) {
        this.major = major;
    }

    public PlayerVersion() {
    }

    public int getMajor() {
        return major;
    }

    public void setMajor(int major) {
        this.major = major;
    }

    public int getMinor() {
        return minor;
    }

    public void setMinor(int minor) {
        this.minor = minor;
    }

    public int getRev() {
        return rev;
    }

    public void setRev(int rev) {
        this.rev = rev;
    }

    public String toString() {
        return getMajor() + "." + getMinor() + "." + getRev();
    }

    public boolean versionIsValid(PlayerVersion fv) {
        if (this.major < fv.getMajor())
            return false;
        if (this.major > fv.getMajor())
            return true;
        if (this.minor < fv.getMinor())
            return false;
        if (this.minor > fv.getMinor())
            return true;
        if (this.rev < fv.getRev())
            return false;
        return true;
    }

}
