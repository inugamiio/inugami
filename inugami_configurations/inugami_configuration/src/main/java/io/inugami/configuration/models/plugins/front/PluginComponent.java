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
package io.inugami.configuration.models.plugins.front;

import java.io.Serializable;

/**
 * PluginComponent
 * 
 * @author patrick_guillerm
 * @since 18 janv. 2017
 */
public class PluginComponent implements Serializable {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -7369458630345980269L;
    
    private String            className;
    
    private String            file;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public PluginComponent() {
        super();
    }
    
    public PluginComponent(final String className, final String file) {
        super();
        this.className = className;
        this.file = file;
    }
    
    // =========================================================================
    // OVERRIDES
    // =========================================================================
    @Override
    public int hashCode() {
        return (className == null) ? 0 : className.hashCode();
    }
    
    @Override
    public boolean equals(final Object obj) {
        boolean result = this == obj;
        
        if (!result && (obj != null) && (obj instanceof PluginComponent)) {
            final PluginComponent other = (PluginComponent) obj;
            result = className == null ? other.getClassName() == null : className.equals(other.getClassName());
        }
        
        return result;
    }
    
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("PluginComponent [className=");
        builder.append(className);
        builder.append(", file=");
        builder.append(file);
        builder.append("]");
        return builder.toString();
    }
    
    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    public String getClassName() {
        return className;
    }
    
    public void setClassName(final String className) {
        this.className = className;
    }
    
    public String getFile() {
        return file;
    }
    
    public void setFile(final String file) {
        this.file = file;
    }
    
}
