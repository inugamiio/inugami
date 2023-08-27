package io.inugami.core.alertings.dynamic.entities;

import io.inugami.api.dao.ClonableObject;
import io.inugami.api.dao.Identifiable;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "CORE_PROVIDER_SOURCE")
public class ProviderSource implements Identifiable<Long>, ClonableObject<ProviderSource> {

    private static final long serialVersionUID = 4041172715089117695L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    @NotNull
    @NotEmpty
    @EqualsAndHashCode.Include
    private String provider;

    @EqualsAndHashCode.Include
    private String cronExpression;

    private String eventName;

    @EqualsAndHashCode.Include
    @Column(name = "dataset_from")
    private String from;

    @EqualsAndHashCode.Include
    private String to;

    @Lob
    @EqualsAndHashCode.Include
    private String query;


    @Override
    public ProviderSource cloneObject() {
        return toBuilder().build();
    }


    @Override
    public boolean isUidSet() {
        return uid != null;
    }


}
