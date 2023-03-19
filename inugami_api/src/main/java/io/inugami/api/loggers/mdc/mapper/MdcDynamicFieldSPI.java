package io.inugami.api.loggers.mdc.mapper;

import java.io.Serializable;
import java.util.Map;

public interface MdcDynamicFieldSPI {
    Map<String, Serializable> generate();
}
