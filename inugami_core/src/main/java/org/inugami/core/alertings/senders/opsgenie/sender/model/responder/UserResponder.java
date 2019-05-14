package org.inugami.core.alertings.senders.opsgenie.sender.model.responder;

import com.google.common.base.Objects;

import java.io.Serializable;

public class UserResponder implements Responder, Serializable {

    private static final long serialVersionUID = 2343911678395547818L;

    private ResponderType               type;

    private String                      username;

    private String                      id;


    public ResponderType getType() {
        return type;
    }

    public void setType(ResponderType type) {
        this.type = type;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
        this.id = null;
    }

    public String getId() {
        return id;

    }

    public void setId(String id) {
        this.id = id;
        this.username = null;
    }

    @Override
    public String toString() {
        return "UserResponder{" +
                "type=" + type +
                ", username='" + username + '\'' +
                ", id='" + id + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserResponder that = (UserResponder) o;
        return type == that.type &&
                Objects.equal(username, that.username) &&
                Objects.equal(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(type, username, id);
    }
}
