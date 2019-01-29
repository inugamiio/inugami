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
package org.inugami.configuration.models.plugins.front;

import java.io.Serializable;

/**
 * Router
 * 
 * @author patrick_guillerm
 * @since 18 janv. 2017
 */
public class Route implements Serializable {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 8978168463668527556L;
    
    private String            path;
    
    private String            component;
    
    // =========================================================================
    // OVERRIDES
    // =========================================================================
    public Route() {
    }
    
    public Route(final String path, final String component) {
        this.path = path;
        this.component = component;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + ((component == null) ? 0 : component.hashCode());
        result = (prime * result) + ((path == null) ? 0 : path.hashCode());
        return result;
    }
    
    @Override
    public boolean equals(final Object obj) {
        boolean result = this == obj;
        
        if (!result && (obj != null) && (obj instanceof Route)) {
            final Route other = (Route) obj;
            //@formatter:off
            result =   (path==null      ? other.getPath()==null      :path.equals(other.getPath()))
                     &&(component==null ? other.getComponent()==null :component.equals(other.getComponent()));
            //@formatter:on
        }
        return result;
    }
    
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("Router [path=");
        builder.append(path);
        builder.append(", component=");
        builder.append(component);
        builder.append("]");
        return builder.toString();
    }
    
    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    
    public String getPath() {
        return path;
    }
    
    public void setPath(final String path) {
        this.path = path;
    }
    
    public String getComponent() {
        return component;
    }
    
    public void setComponent(final String component) {
        this.component = component;
    }
    
}
