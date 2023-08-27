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
package io.inugami.configuration.models.plugins;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * Resources
 * 
 * @author patrick_guillerm
 * @since 22 déc. 2016
 */
public class Resource implements Serializable {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -4852393784977428286L;
    
    @XStreamAsAttribute
    private final String      path;
    
    @XStreamAsAttribute
    private final String      name;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public Resource(final String path, final String name) {
        super();
        this.path = path == null ? "" : path;
        this.name = name;
        
    }
    
    // =========================================================================
    // OVERRIDES
    // =========================================================================
    @Override
    public int hashCode() {
        return getFullPath().hashCode();
    }
    
    @Override
    public boolean equals(final Object obj) {
        boolean result = this == obj;
        
        if (!result && (obj != null) && (obj instanceof Resource)) {
            result = getFullPath().equals(((Resource) obj).getFullPath());
        }
        return result;
    }
    
    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    public String getPath() {
        return path;
    }
    
    public String getName() {
        return name;
    }
    
    public String getFullPath() {
        return path + name;
    }
    
}
