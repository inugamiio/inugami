package org.inugami.core.alertings.senders.opsgenie.sender.model.visibleto;

import com.google.common.base.Objects;

import java.io.Serializable;

public class TeamVisibleTo implements VisibleTo, Serializable {
    private static final long serialVersionUID = 3257681849412611664L;

    private VisibleToType                      type;

    private String                      name;

    private String                      id;

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("TeamVisibleTo [type=");
        builder.append(type);
        builder.append(", name=");
        builder.append(name);
        builder.append(",id=");
        builder.append(id);
        builder.append("]");
        return builder.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TeamVisibleTo that = (TeamVisibleTo) o;
        return Objects.equal(type, that.type) &&
                Objects.equal(name, that.name) &&
                Objects.equal(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(type, name, id);
    }

    public void setId(String id) {
        this.id = id;
    }

    public VisibleToType getType() {
        return type;
    }

    public void setType(VisibleToType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
