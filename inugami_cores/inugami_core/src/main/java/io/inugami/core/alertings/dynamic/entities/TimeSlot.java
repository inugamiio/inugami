package io.inugami.core.alertings.dynamic.entities;

import io.inugami.api.dao.ClonableObject;
import io.inugami.api.dao.Identifiable;
import lombok.*;

import javax.persistence.*;

@ToString
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "CORE_ACTIVATION_TIME_SLOT")
public class TimeSlot implements Identifiable<Long>, ClonableObject<TimeSlot> {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final long serialVersionUID = -426400984660202399L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    @EqualsAndHashCode.Include
    @Column(name = "time_from")
    private String from;
    @EqualsAndHashCode.Include
    private String to;

    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public TimeSlot(final String from, final String to) {
        this(-1L, from, to);
    }


    @Override
    public TimeSlot cloneObject() {
        return toBuilder().build();
    }


    @Override
    public boolean isUidSet() {
        return uid != null;
    }

    @Override
    public String uidFieldName() {
        return "uid";
    }
}
