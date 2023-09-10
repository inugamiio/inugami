package io.inugami.monitoring.core.context;

import io.inugami.api.monitoring.MdcService;
import io.inugami.api.monitoring.TrackingInformationSPI;
import io.inugami.api.monitoring.models.IHeaders;

import java.util.LinkedHashMap;
import java.util.Map;

import static io.inugami.api.functionnals.FunctionalUtils.applyIfNotNull;

public class DefaultTrackingInformationSPI implements TrackingInformationSPI {

    @Override
    public Map<String, String> getInformation(final IHeaders headers) {
        Map<String, String> result = new LinkedHashMap<>();


        applyIfNotNull(MdcService.getInstance().getMdc(MdcService.MDCKeys.deviceIdentifier),
                       value -> result.put(headers.getDeviceIdentifier(), value));

        applyIfNotNull(MdcService.getInstance().getMdc(MdcService.MDCKeys.correlation_id),
                       value -> result.put(headers.getCorrelationId(), value));

        applyIfNotNull(MdcService.getInstance().getMdc(MdcService.MDCKeys.conversation_id),
                       value -> result.put(headers.getConversationId(), value));


        String traceId = MdcService.getInstance().getMdc(MdcService.MDCKeys.request_id);
        if (traceId == null) {
            traceId = MdcService.getInstance().getMdc(MdcService.MDCKeys.traceId);
        }
        applyIfNotNull(traceId, value -> result.put(headers.getRequestId(), value));

        return result;
    }
}
