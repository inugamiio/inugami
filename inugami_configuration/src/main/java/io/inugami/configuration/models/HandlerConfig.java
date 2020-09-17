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
package io.inugami.configuration.models;

import io.inugami.api.processors.ClassBehavior;
import io.inugami.api.processors.Config;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("handler")
public class HandlerConfig extends ClassBehavior {
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final long serialVersionUID = -717509522684920613L;
    
    @XStreamAsAttribute
    private String            type;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public HandlerConfig() {
        super();
    }
    
    public HandlerConfig(final String name, final String className, final Config... configs) {
        super(name, className, configs);
    }
    
    public HandlerConfig(final String name, final String className, final String type, final Config... configs) {
        super(name, className, configs);
        this.type = type;
    }
    
    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    public String getType() {
        return type;
    }
    
    public void setType(final String type) {
        this.type = type;
    }
}
