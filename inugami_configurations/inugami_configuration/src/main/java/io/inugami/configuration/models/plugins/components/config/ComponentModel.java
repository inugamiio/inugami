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
package io.inugami.configuration.models.plugins.components.config;

import java.util.List;

import io.inugami.api.models.data.basic.JsonObject;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * ComponentModel
 * 
 * @author patrickguillerm
 * @since 30 août 2018
 */
@XStreamAlias("component")
public class ComponentModel implements JsonObject {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final long          serialVersionUID = -4365933262773809816L;
    
    @XStreamAsAttribute
    private String                     name;
    
    private List<ComponentViewModel>   views;
    
    private ComponentDescription       descriptions;
    
    private List<ComponentFieldsModel> models;
    
    private List<ComponentEvent>       events;
    
    // =========================================================================
    // OVERRIDES
    // =========================================================================
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }
    
    @Override
    public boolean equals(Object obj) {
        boolean result = this == obj;
        
        if (!result && obj != null && obj instanceof ComponentModel) {
            final ComponentModel other = (ComponentModel) obj;
            result = name == null ? other.getName() == null : name.equals(other.getName());
        }
        
        return result;
    }
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(this.getClass().getSimpleName());
        builder.append(convertToJson());
        return builder.toString();
    }
    
    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public List<ComponentViewModel> getViews() {
        return views;
    }
    
    public void setViews(List<ComponentViewModel> views) {
        this.views = views;
    }
    
    public ComponentDescription getDescriptions() {
        return descriptions;
    }
    
    public void setDescription(ComponentDescription descriptions) {
        this.descriptions = descriptions;
    }
    
    public List<ComponentFieldsModel> getModels() {
        return models;
    }
    
    public void setModels(List<ComponentFieldsModel> models) {
        this.models = models;
    }
    
    public List<ComponentEvent> getEvents() {
        return events;
    }
    
    public void setEvents(List<ComponentEvent> events) {
        this.events = events;
    }
    
}
