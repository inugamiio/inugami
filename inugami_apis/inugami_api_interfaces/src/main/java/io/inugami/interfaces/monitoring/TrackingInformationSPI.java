package io.inugami.interfaces.monitoring;


import java.util.Map;

@FunctionalInterface
public interface TrackingInformationSPI {
    Map<String, String> getInformation();
}
