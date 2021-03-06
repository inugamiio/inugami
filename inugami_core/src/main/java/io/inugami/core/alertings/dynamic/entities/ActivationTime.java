package io.inugami.core.alertings.dynamic.entities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
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
@Table(name = "CORE_ACTIVATION_TIME")
public class ActivationTime implements Identifiable<Long>, ClonableObject<ActivationTime> {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final long serialVersionUID = 4659384989763563433L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long              uid;
    
    @ElementCollection
    private List<String>      days;
    
    @OneToMany(cascade = CascadeType.ALL, targetEntity = TimeSlot.class, fetch = FetchType.EAGER)
    private List<TimeSlot>    hours;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public ActivationTime() {
    }
    
    public ActivationTime(final List<String> days, final List<TimeSlot> hours) {
        this(-1L, days, hours);
    }
    
    public ActivationTime(final Long uid, final List<String> days, final List<TimeSlot> hours) {
        super();
        this.uid = uid;
        this.days = days;
        this.hours = hours;
    }
    
    @Override
    public ActivationTime cloneObject() {
        
        final List<String> newDays = days == null ? null : new ArrayList<>(days);
        //@formatter:off
        final List<TimeSlot> newHours = Optional.ofNullable(hours)
                                                .orElse(Collections.emptyList())
                                                .stream()
                                                .map(TimeSlot::cloneObject)
                                                .collect(Collectors.toList());
        //@formatter:on
        return new ActivationTime(uid, newDays, newHours);
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
        result = (prime * result) + ((days == null) ? 0 : days.hashCode());
        result = (prime * result) + ((hours == null) ? 0 : hours.hashCode());
        return result;
    }
    
    @Override
    public boolean equals(final Object obj) {
        boolean result = this == obj;
        
        if (!result && (obj != null) && (obj instanceof ActivationTime)) {
            final ActivationTime other = (ActivationTime) obj;
            
            //@formatter:off
            result = (days == null)  ? other.getDays() == null  : days.equals(other.getDays()) 
                  && (hours == null) ? other.getHours() == null : hours.equals(other.getHours());
            //@formatter:on
            
        }
        
        return result;
    }
    
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("ActivationTime [uid=");
        builder.append(uid);
        builder.append(", days=");
        builder.append(days);
        builder.append(", hours=");
        builder.append(hours);
        builder.append("]");
        return builder.toString();
    }
    
    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    
    public List<String> getDays() {
        return days;
    }
    
    public void setDays(final List<String> days) {
        this.days = days;
    }
    
    public List<TimeSlot> getHours() {
        return hours;
    }
    
    public void setHours(final List<TimeSlot> hours) {
        this.hours = hours;
    }
    
}
