package io.inugami.core.alertings.dynamic.entities;

import io.inugami.api.dao.ClonableObject;
import io.inugami.api.dao.Identifiable;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@SuppressWarnings({"java:S3358"})
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "CORE_ACTIVATION_TIME")
public class ActivationTime implements Identifiable<Long>, ClonableObject<ActivationTime> {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final long serialVersionUID = 4659384989763563433L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    @ElementCollection
    @EqualsAndHashCode.Include
    private List<String> days;

    @EqualsAndHashCode.Include
    @OneToMany(cascade = CascadeType.ALL, targetEntity = TimeSlot.class, fetch = FetchType.EAGER)
    private List<TimeSlot> hours;

    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================


    public ActivationTime(final List<String> days, final List<TimeSlot> hours) {
        this(-1L, days, hours);
    }


    @Override
    public ActivationTime cloneObject() {
        return toBuilder().build();
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

}
