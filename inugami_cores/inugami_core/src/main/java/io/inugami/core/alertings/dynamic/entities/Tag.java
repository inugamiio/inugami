package io.inugami.core.alertings.dynamic.entities;

import io.inugami.api.dao.ClonableObject;
import io.inugami.api.dao.Identifiable;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@ToString
@EqualsAndHashCode
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "CORE_TAGS")
public class Tag implements Identifiable<String>, ClonableObject<Tag> {

    private static final long serialVersionUID = -874109940237137230L;

    @NotNull
    @NotEmpty
    @Id
    private String name;


    @Override
    public Tag cloneObject() {
        return toBuilder().build();
    }

    @Override
    public String getUid() {
        return name;
    }

    @Override
    public void setUid(final String uid) {
        this.name = uid;

    }

    @Override
    public boolean isUidSet() {
        return (name != null) && !name.trim().isEmpty();
    }


}
