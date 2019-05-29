package org.inugami.webapp.rest.models;

import com.google.common.base.Objects;

import java.io.Serializable;

public class DataProviderRestModel implements Serializable {


    private static final long serialVersionUID = 5867341458582220123L;

    private final String name;

    private final float         timeout;

    public DataProviderRestModel(String name, float timeout) {
        this.name = name;
        this.timeout = timeout;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DataProviderRestModel that = (DataProviderRestModel) o;
        return Float.compare(that.timeout, timeout) == 0 &&
                Objects.equal(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name, timeout);
    }

    public String getName() {
        return name;
    }

    public float getTimeout() {
        return timeout;
    }

    @Override
    public String toString() {
        return "{" +
                "name:'" + name + '\'' +
                ", timeout:" + timeout +
                '}';
    }
}
