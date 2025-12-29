package io.inugami.monitoring.springboot.partnerlog.feign;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import io.inugami.framework.api.monitoring.MdcService;
import io.inugami.framework.api.tools.RunSafeUtils;
import io.inugami.framework.interfaces.monitoring.logger.Loggers;
import io.inugami.framework.interfaces.monitoring.models.IoInfoDTO;
import io.inugami.monitoring.core.context.MonitoringContext;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Optional;

@SuppressWarnings({"java:S1181", "java:S108", "java:S2629"})
@Slf4j
@Getter
@RequiredArgsConstructor
public class FeignPartnerRequestInterceptor implements RequestInterceptor {

    private final MonitoringContext monitoringContext;

    @Override
    public void apply(final RequestTemplate requestTemplate) {
        RunSafeUtils.runSafeVoid(() -> {
            final Map<String, String> trackingInfo = monitoringContext.getTrackingInformation();
            for (final Map.Entry<String, String> entry : Optional.ofNullable(trackingInfo)
                                                                 .orElse(Map.of())
                                                                 .entrySet()) {
                requestTemplate.header(entry.getKey(), entry.getValue());
            }
        }, log);

        RunSafeUtils.runSafeVoid(() -> {
            MdcService.getInstance().partnerRemove();

            final IoInfoDTO info = FeignCommon.buildInfo(requestTemplate);
            MdcService.getInstance().ioinfoPartner(info);

            Loggers.PARTNERLOG.info(info.toString());
            requestTemplate.header(FeignCommon.X_DATE, String.valueOf(System.currentTimeMillis()));
        }, log);
    }
}
