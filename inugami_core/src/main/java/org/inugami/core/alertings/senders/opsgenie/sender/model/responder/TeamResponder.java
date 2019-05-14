package org.inugami.core.alertings.senders.opsgenie.sender.model.responder;

import com.google.common.base.Objects;

import java.io.Serializable;

public class TeamResponder implements Responder, Serializable {
    private static final long serialVersionUID = -1474824053820961800L;

    private ResponderType               type;

    private String                      name;

    private String                      id;

    public ResponderType getType() {
        return type;
    }

    public void setType(ResponderType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        this.id = null;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
        this.name = null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TeamResponder that = (TeamResponder) o;
        return type == that.type &&
                Objects.equal(name, that.name) &&
                Objects.equal(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(type, name, id);
    }

    @Override
    public String toString() {
        return "TeamResponder{" +
                "type=" + type +
                ", name='" + name + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
