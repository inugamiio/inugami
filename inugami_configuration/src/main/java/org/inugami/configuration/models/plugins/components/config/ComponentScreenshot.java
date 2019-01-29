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
package org.inugami.configuration.models.plugins.components.config;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * ComponentScreenshot
 * 
 * @author patrickguillerm
 * @since 31 août 2018
 */
@XStreamAlias("screenshot")
public class ComponentScreenshot {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    @XStreamAsAttribute
    private String path;
    
    // =========================================================================
    // OVERRIDES
    // =========================================================================
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((path == null) ? 0 : path.hashCode());
        return result;
    }
    
    @Override
    public boolean equals(Object obj) {
        boolean result = this == obj;
        
        if (!result && obj != null && obj instanceof ComponentScreenshot) {
            final ComponentScreenshot other = (ComponentScreenshot) obj;
            result = path == null ? other.getPath() == null : path.equals(other.getPath());
        }
        
        return result;
    }
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("ComponentScreenshot [path=");
        builder.append(path);
        builder.append("]");
        return builder.toString();
    }
    
    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    public String getPath() {
        return path;
    }
    
    public void setPath(String path) {
        this.path = path;
    }
}
