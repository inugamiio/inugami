package io.inugami.monitoring.core.context;

import io.inugami.framework.interfaces.monitoring.TrackingInformationSPI;
import io.inugami.framework.interfaces.monitoring.models.Monitoring;
import io.inugami.framework.interfaces.spi.SpiLoader;
import lombok.experimental.UtilityClass;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static io.inugami.framework.interfaces.functionnals.FunctionalUtils.applyIfNotNull;

@SuppressWarnings({"java:S1172"})
@UtilityClass
public class MonitoringContextUtils {
    private static final List<TrackingInformationSPI> TRACKERS = SpiLoader.getInstance()
                                                                          .loadSpiServicesByPriority(TrackingInformationSPI.class);

    public static Map<String, String> getTrackingInformation(final Monitoring config) {
        Map<String, String> result = new LinkedHashMap<>();
        for (TrackingInformationSPI tracker : TRACKERS) {
            final Map<String, String> trackerInfo = tracker.getInformation();
            applyIfNotNull(trackerInfo, result::putAll);
        }
        return result;
    }
}
