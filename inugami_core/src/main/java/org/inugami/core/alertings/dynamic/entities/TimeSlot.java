package org.inugami.core.alertings.dynamic.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.inugami.api.dao.Identifiable;

@Entity
@Table(name = "CORE_ACTIVATION_TIME_SLOT")
public class TimeSlot implements Identifiable<Long> {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final long serialVersionUID = -426400984660202399L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long              uid;
    
    @Column(name = "time_from")
    private String            from;
    
    private String            to;
    // =========================================================================
    // CONSTRUCTORS
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
    // OVERRIDES
    // =========================================================================
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + ((from == null) ? 0 : from.hashCode());
        result = (prime * result) + ((to == null) ? 0 : to.hashCode());
        return result;
    }
    
    @Override
    public boolean equals(final Object obj) {
        boolean result = this == obj;
        
        if (!result && (obj != null) && (obj instanceof TimeSlot)) {
            final TimeSlot other = (TimeSlot) obj;
            //@formatter:off
            result = (from==null)?other.getFrom() == null : from.equals(other.getFrom())
                  && (to == null)?other.getTo()   == null : to.equals(other.getTo());
            //@formatter:on
        }
        
        return result;
    }
    
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("TimeSlot [uid=");
        builder.append(uid);
        builder.append(", from=");
        builder.append(from);
        builder.append(", to=");
        builder.append(to);
        builder.append("]");
        return builder.toString();
    }
    
    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    public String getFrom() {
        return from;
    }
    
    public void setFrom(final String from) {
        this.from = from;
    }
    
    public String getTo() {
        return to;
    }
    
    public void setTo(final String to) {
        this.to = to;
    }
    
}
