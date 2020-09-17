package io.inugami.webapp.rest.models;

import com.google.common.base.Objects;
import io.inugami.api.processors.ConfigHandler;

import java.io.Serializable;

public class DataProviderRestModel implements Serializable {


    private static final long serialVersionUID = 5867341458582220123L;

    private final String                            name;

    private final float                             timeout;

    private final ConfigHandler<String,String>      config;

    public DataProviderRestModel(String name, float timeout,ConfigHandler<String,String> config ) {
        this.name = name;
        this.timeout = timeout;
        this.config = config;
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

    public ConfigHandler<String,String> getConfig(){ return config;}

    @Override
    public String toString() {
        return "{" +
                "name:'" + name + '\'' +
                ", timeout:" + timeout +
                '}';
    }
}
