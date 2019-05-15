package org.inugami.webapp.rest.alerts.models;

import java.io.Serializable;
import java.util.Map;

public class ProviderRestModel implements Serializable {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final long         serialVersionUID = 519746212332122237L;
    
    private final String              name;
    
    private final boolean             enable;
    
    private final Map<String, String> configurations;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public ProviderRestModel(final String name, final boolean enable, final Map<String, String> configurations) {
        super();
        this.name = name;
        this.enable = enable;
        this.configurations = configurations;
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
        
        if (!result && (obj != null) && (obj instanceof ProviderRestModel)) {
            final ProviderRestModel other = (ProviderRestModel) obj;
            result = name == null ? other.getName() == null : name.equals(other.getName());
        }
        
        return result;
    }
    
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("ProviderRestModel [name=");
        builder.append(name);
        builder.append(", enable=");
        builder.append(enable);
        builder.append("]");
        return builder.toString();
    }
    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    
    public String getName() {
        return name;
    }
    
    public boolean isEnable() {
        return enable;
    }
    
    public Map<String, String> getConfigurations() {
        return configurations;
    }
    
}
