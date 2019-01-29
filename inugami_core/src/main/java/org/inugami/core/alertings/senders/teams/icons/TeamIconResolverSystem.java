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
package org.inugami.core.alertings.senders.teams.icons;

/**
 * TeamIconCpu
 * 
 * @author patrick_guillerm
 * @since 21 août 2018
 */
public class TeamIconResolverSystem implements TeamIconResolver {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private final String iconCpu;
    
    private final String iconMemory;
    
    private final String iconSpeed;
    
    private final String iconSocket;
    
    private final String iconDatabase;
    
    private final String iconNetwork;
    
    private final String iconSecurity;
    
    // =========================================================================
    // METHODS
    // =========================================================================
    public TeamIconResolverSystem() {
        iconCpu = readImage("META-INF/icons/cpu.png");
        iconMemory = readImage("META-INF/icons/memory.png");
        iconSpeed = readImage("META-INF/icons/chronometer.png");
        iconSocket = readImage("META-INF/icons/socket.png");
        iconDatabase = readImage("META-INF/icons/database.png");
        iconNetwork = readImage("META-INF/icons/network.png");
        iconSecurity = readImage("META-INF/icons/security.png");
        
    }
    // =========================================================================
    // METHODS
    // =========================================================================
    
    @Override
    public TeamIcon resolve(final String level) {
        final String lowerCase = level.toLowerCase();
        String icon = null;
        
        if (lowerCase.contains("cpu")) {
            icon = iconCpu;
        }
        else if (lowerCase.contains("memory")) {
            icon = iconMemory;
        }
        else if (lowerCase.contains("speed") || lowerCase.contains("time")) {
            icon = iconSpeed;
        }
        else if (lowerCase.contains("socket")) {
            icon = iconSocket;
        }
        else if (lowerCase.contains("database") || lowerCase.contains("sgbd")) {
            icon = iconDatabase;
        }
        else if (lowerCase.contains("network")) {
            icon = iconNetwork;
        }
        else if (lowerCase.contains("security") || lowerCase.contains("firewall")) {
            icon = iconSecurity;
        }
        
        return icon == null ? null : new TeamIcon(0, icon);
    }
    
}
