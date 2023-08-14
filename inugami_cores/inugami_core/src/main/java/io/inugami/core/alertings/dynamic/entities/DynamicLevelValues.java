package io.inugami.core.alertings.dynamic.entities;

import io.inugami.api.dao.ClonableObject;
import io.inugami.api.dao.Identifiable;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "CORE_DYNAMIC_LEVELS_VALUES")
public class DynamicLevelValues implements Identifiable<Long>, ClonableObject<DynamicLevelValues> {

    private static final long serialVersionUID = -796912928682428059L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    @Min(0)
    @Max(23)
    @NotNull
    @EqualsAndHashCode.Include
    private Integer hour;

    @NotNull
    @EqualsAndHashCode.Include
    private Double level;


    @Override
    public DynamicLevelValues cloneObject() {
        return toBuilder().build();
    }


    @Override
    public boolean isUidSet() {
        return uid != null;
    }

}
