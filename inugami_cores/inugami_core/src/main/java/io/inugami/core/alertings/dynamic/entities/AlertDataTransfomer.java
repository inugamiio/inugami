package io.inugami.core.alertings.dynamic.entities;

import io.inugami.api.dao.ClonableObject;
import io.inugami.api.dao.Identifiable;
import lombok.*;

import javax.persistence.*;

@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "CORE_ALERT_DATA_TRANSFORMER")
public class AlertDataTransfomer implements Identifiable<Long>, ClonableObject<AlertDataTransfomer> {

    
    private static final long serialVersionUID = 8658816619910512607L;

    @Id
    @ToString.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    @ToString.Include
    @EqualsAndHashCode.Include
    private String name;

    @Lob
    @EqualsAndHashCode.Include
    private String script;


    @Override
    public AlertDataTransfomer cloneObject() {
        return toBuilder().build();
    }

    @Override
    public boolean isUidSet() {
        return uid != null;
    }
}
