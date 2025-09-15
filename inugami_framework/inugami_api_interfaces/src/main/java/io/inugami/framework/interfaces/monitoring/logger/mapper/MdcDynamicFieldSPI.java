package io.inugami.framework.interfaces.monitoring.logger.mapper;

import java.io.Serializable;
import java.util.Map;

public interface MdcDynamicFieldSPI {
    Map<String, Serializable> generate();
}
