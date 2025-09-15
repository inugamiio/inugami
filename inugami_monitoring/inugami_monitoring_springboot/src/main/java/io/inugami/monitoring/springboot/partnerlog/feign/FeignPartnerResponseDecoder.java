package io.inugami.monitoring.springboot.partnerlog.feign;

import feign.FeignException;
import feign.Response;
import feign.codec.DecodeException;
import feign.codec.Decoder;
import io.inugami.framework.api.monitoring.MdcService;
import io.inugami.framework.interfaces.monitoring.logger.Loggers;
import io.inugami.framework.interfaces.monitoring.models.IoInfoDTO;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.lang.reflect.Type;

@SuppressWarnings({"java:S2629"})
@Builder
@RequiredArgsConstructor
public class FeignPartnerResponseDecoder implements Decoder {
    private final Decoder decoder;

    @Override
    public Object decode(final Response response, final Type type) throws IOException, DecodeException, FeignException {
        final long now      = System.currentTimeMillis();
        final long callDate = FeignCommon.resolveCallDate(response);

        Response  wrappedResponse = FeignCommon.wrapResponse(response);
        IoInfoDTO ioInfo          = FeignCommon.buildInfo(wrappedResponse, now - callDate);

        MdcService.getInstance().ioinfoPartner(ioInfo);

        if (ioInfo.getStatus() >= 400) {
            Loggers.PARTNERLOG.error(ioInfo.toString());
        } else {
            Loggers.PARTNERLOG.info(ioInfo.toString());
        }
        
        MdcService.getInstance().partnerRemove();

        return decoder.decode(wrappedResponse, type);
    }


}
