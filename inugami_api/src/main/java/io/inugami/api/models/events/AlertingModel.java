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
package io.inugami.api.models.events;

import java.io.Serializable;
import java.util.Optional;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * AlertingModel
 * 
 * @author patrick_guillerm
 * @since 20 déc. 2017
 */
@XStreamAlias("alerting")
public class AlertingModel implements Serializable {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final long serialVersionUID = 2557187994399108457L;
    
    @XStreamAsAttribute
    private String            name;
    
    @XStreamAsAttribute
    private String            provider;
    
    @XStreamAsAttribute
    private String            message;
    
    @XStreamAsAttribute
    private String            level;
    
    @XStreamAsAttribute
    private String            condition;
    
    @XStreamAsAttribute
    protected String          function;
    
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
        
        if (!result && (obj != null) && (obj instanceof AlertingModel)) {
            final AlertingModel other = (AlertingModel) obj;
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
    
    public String getProvider() {
        return provider;
    }
    
    public void setProvider(final String provider) {
        this.provider = provider;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(final String message) {
        this.message = message;
    }
    
    public String getLevel() {
        return level;
    }
    
    public void setLevel(final String level) {
        this.level = level;
    }
    
    public String getCondition() {
        return condition;
    }
    
    public void setCondition(final String condition) {
        this.condition = condition;
    }
    
    public Optional<String> grabFunction() {
        return Optional.ofNullable(function);
    }
    
    public String getFunction() {
        return function;
    }
    
    public void setFunction(final String function) {
        this.function = function;
    }
    
}
