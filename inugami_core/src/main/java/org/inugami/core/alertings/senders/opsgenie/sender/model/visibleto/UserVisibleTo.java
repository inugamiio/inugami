package org.inugami.core.alertings.senders.opsgenie.sender.model.visibleto;

import com.google.common.base.Objects;

import java.io.Serializable;

public class UserVisibleTo implements VisibleTo, Serializable {

    private static final long serialVersionUID = -4217815011879567179L;
    private VisibleToType               type;

    private String                      username;
    private String                      id;


    public VisibleToType getType() {
        return type;
    }

    public void setType(VisibleToType type) {
        this.type = type;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "UserVisibleTo{" +
                "type='" + type + '\'' +
                ", username='" + username + '\'' +
                ", id='" + id + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserVisibleTo that = (UserVisibleTo) o;
        return Objects.equal(type, that.type) &&
                Objects.equal(username, that.username) &&
                Objects.equal(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(type, username, id);
    }
}
