package org.inugami.core.alertings.dynamic;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.inugami.api.dao.Identifiable;

@Entity
@Table(name = "CORE_DYNAMIC_LEVELS")
public class DynamicLevel implements Identifiable<Long> {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final long        serialVersionUID = -1342514302269125330L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long                     uid;
    
    private String                   name;
    
    @OneToMany(cascade = CascadeType.ALL, targetEntity = DynamicLevelValues.class, fetch = FetchType.EAGER)
    private List<DynamicLevelValues> data;
    
    private transient boolean        dynamic;
    
    // =========================================================================
    // OVERRIDES
    // =========================================================================
    @Override
    public Long getUid() {
        return uid;
    }
    
    @Override
    public void setUid(final Long uid) {
        this.uid = uid;
    }
    
    @Override
    public boolean isUidSet() {
        return uid != null;
    }
    
    // =========================================================================
    // GETTERS & SETTERS
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
        
        if (!result && (obj != null) && (obj instanceof DynamicLevel)) {
            final DynamicLevel other = (DynamicLevel) obj;
            //@formatter:off
            result = name==null?other.getName()==null : name.equals(other.getName());
            //@formatter:on
        }
        
        return result;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public List<DynamicLevelValues> getData() {
        return data;
    }
    
    public void setData(final List<DynamicLevelValues> data) {
        this.data = data;
        dynamic = !((data == null) || data.isEmpty() || (data.size() == 1));
    }
    
    public boolean isDynamic() {
        return dynamic;
    }
    
}
