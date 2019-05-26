package org.inugami.core.alertings.dynamic;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.inugami.api.dao.Identifiable;

@Entity
@Table(name = "CORE_TAGS")
public class Tag implements Identifiable<String> {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final long serialVersionUID = -874109940237137230L;
    
    @NotNull
    @NotEmpty
    @Id
    private String            name;
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    
    public Tag() {
    }
    
    //@formatter:off
    public Tag(@NotNull @NotEmpty final String name) {
        this.name = name;
        //@formatter:on
    }
    
    // =========================================================================
    // OVERRIDES
    // =========================================================================
    @Override
    public String getUid() {
        return name;
    }
    
    @Override
    public void setUid(final String uid) {
        this.name = uid;
        
    }
    
    @Override
    public boolean isUidSet() {
        return (name != null) && !name.trim().isEmpty();
    }
    
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
        
        if (!result && (obj != null) && (obj instanceof Tag)) {
            final Tag other = (Tag) obj;
            result = name == null ? other.getName() == null : name.equals(other.getName());
        }
        
        return result;
    }
    
    @Override
    public String toString() {
        return name;
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
    
}
