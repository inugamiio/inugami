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
 * MenuLink
 * 
 * @author patrick_guillerm
 * @since 19 janv. 2017
 */
public class MenuLink implements Serializable {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 8016174687361342718L;
    
    private String            path;
    
    private String            title;
    
    private String            styleClass;
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    
    public MenuLink() {
        super();
    }
    
    public MenuLink(final String path, final String title, final String styleClass) {
        super();
        this.path = path;
        this.title = title;
        this.styleClass = styleClass;
    }
    
    // =========================================================================
    // OVERRIDES
    // =========================================================================
    // TODO: hashcode/equls/toString
    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    public String getPath() {
        return path;
    }
    
    public void setPath(final String path) {
        this.path = path;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(final String title) {
        this.title = title;
    }
    
    public String getStyleClass() {
        return styleClass;
    }
    
    public void setStyleClass(final String styleClass) {
        this.styleClass = styleClass;
    }
}
