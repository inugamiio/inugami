package io.inugami.monitoring.springboot.partnerlog.feign;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import io.inugami.api.loggers.Loggers;
import io.inugami.api.monitoring.MdcService;
import io.inugami.api.monitoring.models.IoInfoDTO;
import io.inugami.monitoring.core.context.MonitoringContext;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@Getter
@RequiredArgsConstructor
public class FeignPartnerRequestInterceptor implements RequestInterceptor {

    private final MonitoringContext monitoringContext;

    @Override
    public void apply(final RequestTemplate requestTemplate) {
        final IoInfoDTO info = FeignCommon.buildInfo(requestTemplate);
        MdcService.getInstance().ioinfoPartner(info);

        Loggers.PARTNERLOG.info(info.toString());
        requestTemplate.header(FeignCommon.X_DATE, String.valueOf(System.currentTimeMillis()));

        final Map<String, String> trackingInfo = monitoringContext.getTrackingInformation();
        if (trackingInfo != null) {
            for (final Map.Entry<String, String> entry : trackingInfo.entrySet()) {
                requestTemplate.header(entry.getKey(), entry.getValue());
            }
        }
    }
}
