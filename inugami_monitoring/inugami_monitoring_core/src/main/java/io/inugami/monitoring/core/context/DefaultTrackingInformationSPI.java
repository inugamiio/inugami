package io.inugami.monitoring.core.context;


import io.inugami.framework.api.monitoring.MdcService;
import io.inugami.framework.interfaces.monitoring.TrackingInformationSPI;
import io.inugami.framework.interfaces.monitoring.logger.MDCKeys;
import io.inugami.framework.interfaces.monitoring.models.Headers;

import java.util.LinkedHashMap;
import java.util.Map;

import static io.inugami.framework.interfaces.functionnals.FunctionalUtils.applyIfNotNull;


public class DefaultTrackingInformationSPI implements TrackingInformationSPI {

    @Override
    public Map<String, String> getInformation() {
        final Map<String, String> result = new LinkedHashMap<>();
        final var                 mdc    = MdcService.getInstance();

        applyIfNotNull(mdc.getMdc(MDCKeys.deviceIdentifier),
                       value -> result.put(Headers.X_DEVICE_IDENTIFIER, value));

        result.put(Headers.X_CORRELATION_ID, mdc.correlationId());

        applyIfNotNull(mdc.getMdc(MDCKeys.conversation_id),
                       value -> result.put(Headers.X_CONVERSATION_ID, value));

        result.put(Headers.X_B_3_TRACEID, mdc.traceId());

        return result;
    }
}
