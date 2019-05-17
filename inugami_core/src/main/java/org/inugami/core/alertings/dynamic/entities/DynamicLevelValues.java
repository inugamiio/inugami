package org.inugami.core.alertings.dynamic.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.inugami.api.dao.ClonableObject;
import org.inugami.api.dao.Identifiable;

@Entity
@Table(name = "CORE_DYNAMIC_LEVELS_VALUES")
public class DynamicLevelValues implements Identifiable<Long>, ClonableObject<DynamicLevelValues> {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final long serialVersionUID = -796912928682428059L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long              uid;
    
    @Min(0)
    @Max(23)
    @NotNull
    private Integer           hour;
    
    @NotNull
    private Double            level;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public DynamicLevelValues() {
    }
    
    public DynamicLevelValues(final Integer hour, final Double level) {
        super();
        this.hour = hour;
        this.level = level;
    }
    
    public DynamicLevelValues(final Long uid, final Integer hour, final Double level) {
        super();
        this.uid = uid;
        this.hour = hour;
        this.level = level;
    }
    
    @Override
    public DynamicLevelValues cloneObject() {
        return new DynamicLevelValues(uid, hour, level);
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
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + ((hour == null) ? 0 : hour.hashCode());
        result = (prime * result) + ((level == null) ? 0 : level.hashCode());
        return result;
    }
    
    @Override
    public boolean equals(final Object obj) {
        boolean result = this == obj;
        
        if (!result && (obj != null) && (obj instanceof DynamicLevelValues)) {
            final DynamicLevelValues other = (DynamicLevelValues) obj;
            
            //@formatter:off
            result = (hour == null)  ? other.getHour() == null  : hour.equals(other.getHour()) 
                  && (level == null) ? other.getLevel() == null : level.equals(other.getLevel());
            //@formatter:on
            
        }
        
        return result;
    }
    
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("DynamicLevelValues [uid=");
        builder.append(uid);
        builder.append(", hour=");
        builder.append(hour);
        builder.append(", level=");
        builder.append(level);
        builder.append("]");
        return builder.toString();
    }
    
    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    public Integer getHour() {
        return hour;
    }
    
    public void setHour(final Integer hour) {
        this.hour = hour;
    }
    
    public Double getLevel() {
        return level;
    }
    
    public void setLevel(final Double level) {
        this.level = level;
    }
    
}
