/* --------------------------------------------------------------------
 *  Inugami  
 * --------------------------------------------------------------------
 * 
 * This program is free software: you can redistribute it and/or modify  
 * it under the terms of the GNU General Public License as published by  
 * the Free Software Foundation, version 3.
 *
 * This program is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU 
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License 
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package io.inugami.core.alertings.senders.teams.icons;

/**
 * TeamIconCpu
 * 
 * @author patrick_guillerm
 * @since 21 août 2018
 */
public class TeamIconResolverDevices implements TeamIconResolver {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private final String iconAndroid;
    
    private final String iconIOS;
    
    private final String iconChrome;
    
    private final String iconFirefox;
    
    private final String iconOpera;
    
    private final String iconEdge;
    
    private final String iconSafari;
    
    private final String iconLinux;
    
    private final String iconWindows;
    
    // =========================================================================
    // METHODS
    // =========================================================================
    public TeamIconResolverDevices() {
        iconAndroid = readImage("META-INF/icons/android.png");
        iconIOS = readImage("META-INF/icons/apple.png");
        iconChrome = readImage("META-INF/icons/chrome.png");
        iconFirefox = readImage("META-INF/icons/firefox.png");
        iconOpera = readImage("META-INF/icons/opera.png");
        iconEdge = readImage("META-INF/icons/edge.png");
        iconSafari = readImage("META-INF/icons/safari.png");
        iconLinux = readImage("META-INF/icons/linux.png");
        iconWindows = readImage("META-INF/icons/microsoft.png");
        
    }
    // =========================================================================
    // METHODS
    // =========================================================================
    
    @Override
    public TeamIcon resolve(final String level) {
        final String lowerCase = level.toLowerCase();
        String icon = null;
        
        if (lowerCase.contains("android")) {
            icon = iconAndroid;
        }
        else if (lowerCase.contains("ios") || lowerCase.contains("apple") || lowerCase.contains("mac")
                 || lowerCase.contains("osx")) {
            icon = iconIOS;
        }
        else if (lowerCase.contains("chrome") || lowerCase.contains("google")) {
            icon = iconChrome;
        }
        else if (lowerCase.contains("firefox")) {
            icon = iconFirefox;
        }
        else if (lowerCase.contains("opera")) {
            icon = iconOpera;
        }
        else if (lowerCase.contains("safari")) {
            icon = iconSafari;
        }
        else if (lowerCase.contains("ie") || lowerCase.contains("edge")) {
            icon = iconEdge;
        }
        else if (lowerCase.contains("windows") || lowerCase.contains("microsoft")) {
            icon = iconWindows;
        }
        else if (lowerCase.contains("linux") || lowerCase.contains("unix")) {
            icon = iconLinux;
        }
        
        return icon == null ? null : new TeamIcon(0, icon);
    }
    
}
