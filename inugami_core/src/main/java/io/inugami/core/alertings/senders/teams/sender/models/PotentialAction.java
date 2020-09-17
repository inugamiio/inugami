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
package io.inugami.core.alertings.senders.teams.sender.models;

import flexjson.JSON;

/**
 * PotentialAction
 * 
 * @author patrick_guillerm
 * @since 21 août 2018
 */
public class PotentialAction {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    @JSON(name = "@type")
    private String type;
    
    private String name;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public PotentialAction() {
    }
    
    public PotentialAction(final String type, final String name) {
        super();
        this.type = type;
        this.name = name;
    }
    
    // =========================================================================
    // OVERRIDES
    // =========================================================================
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + ((name == null) ? 0 : name.hashCode());
        return result;
    }
    
    @Override
    public boolean equals(final Object obj) {
        boolean result = this == obj;
        
        if (!result && (obj != null) && (obj instanceof PotentialAction)) {
            final PotentialAction other = (PotentialAction) obj;
            result = name == null ? other.getName() == null : name.equals(other.getName());
        }
        
        return result;
    }
    
    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    public String getName() {
        return name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(final String type) {
        this.type = type;
    }
    
}
