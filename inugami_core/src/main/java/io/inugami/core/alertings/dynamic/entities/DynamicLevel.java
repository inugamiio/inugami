package io.inugami.core.alertings.dynamic.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import io.inugami.api.dao.ClonableObject;
import io.inugami.api.dao.Identifiable;

@Entity
@Table(name = "CORE_DYNAMIC_LEVELS")
public class DynamicLevel implements Identifiable<Long>, ClonableObject<DynamicLevel> {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final long        serialVersionUID = -1342514302269125330L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long                     uid;
    
    private String                   name;
    
    private int                      activationDelais;
    
    @OneToMany(cascade = CascadeType.ALL, targetEntity = DynamicLevelValues.class, fetch = FetchType.EAGER)
    private List<DynamicLevelValues> data;
    
    private transient boolean        dynamic;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public DynamicLevel() {
    }
    
    public DynamicLevel(final Long uid, final String name, final List<DynamicLevelValues> data, final boolean dynamic) {
        super();
        this.uid = uid;
        this.name = name;
        this.data = data;
        this.dynamic = dynamic;
    }
    
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
    
    @Override
    public DynamicLevel cloneObject() {
        final List<DynamicLevelValues> newData = new ArrayList<>();
        if (data != null) {
            data.stream().map(DynamicLevelValues::cloneObject).forEach(newData::add);
        }
        return new DynamicLevel(uid, name, data, dynamic);
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
    
    public int getActivationDelais() {
        return activationDelais;
    }
    
    public void setActivationDelais(final int activationDelais) {
        this.activationDelais = activationDelais;
    }
    
}
