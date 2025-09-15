package io.inugami.monitoring.core.context;

import io.inugami.framework.interfaces.monitoring.TrackingInformationSPI;
import io.inugami.framework.interfaces.monitoring.models.Monitoring;
import io.inugami.framework.interfaces.spi.SpiLoader;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
final class MonitoringContextUtils {
    private static final List<TrackingInformationSPI> TRACKERS = SpiLoader.getInstance()
                                                                          .loadSpiServicesByPriority(TrackingInformationSPI.class);

    public static Map<String, String> getTrackingInformation(final Monitoring config) {
        Map<String, String> result = new LinkedHashMap<>();
        for (TrackingInformationSPI tracker : TRACKERS) {
            final Map<String, String> trackerInfo = tracker.getInformation();

            if (trackerInfo != null) {
                result.putAll(trackerInfo);
            }
        }

        return result;
    }
}
