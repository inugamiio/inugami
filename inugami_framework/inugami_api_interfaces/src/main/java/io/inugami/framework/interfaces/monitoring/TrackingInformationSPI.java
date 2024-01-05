package io.inugami.framework.interfaces.monitoring;


import java.util.Map;

@FunctionalInterface
public interface TrackingInformationSPI {
    Map<String, String> getInformation();
}
