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
 * TeamIcon
 * 
 * @author patrick_guillerm
 * @since 21 août 2018
 */
public class TeamIcon {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private final int    priority;
    
    private final String icon;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public TeamIcon(final int priority, final String icon) {
        super();
        this.priority = priority;
        this.icon = icon;
    }
    
    // =========================================================================
    // OVERRIDES
    // =========================================================================
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + ((icon == null) ? 0 : icon.hashCode());
        return result;
    }
    
    @Override
    public boolean equals(final Object obj) {
        boolean result = this == obj;
        
        if (!result && (obj != null) && (obj instanceof TeamIcon)) {
            final TeamIcon other = (TeamIcon) obj;
            result = icon == null ? other.getIcon() == null : icon.equals(other.getIcon());
        }
        
        return result;
    }
    
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("TeamIcon [priority=");
        builder.append(priority);
        builder.append(", icon=");
        builder.append(icon);
        builder.append("]");
        return builder.toString();
    }
    
    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    public int getPriority() {
        return priority;
    }
    
    public String getIcon() {
        return icon;
    }
}
