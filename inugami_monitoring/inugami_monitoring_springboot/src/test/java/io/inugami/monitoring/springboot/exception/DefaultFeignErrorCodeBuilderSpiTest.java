package io.inugami.monitoring.springboot.exception;

import feign.FeignException;
import io.inugami.framework.interfaces.exceptions.UncheckedException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class DefaultFeignErrorCodeBuilderSpiTest {

    // =================================================================================================================
    // ATTRIBUTES
    // =================================================================================================================
    public static final UncheckedException EXCEPTION       = new UncheckedException("sorry");
    public static final FeignException     FEIGN_EXCEPTION = new MyFeignException(404, "sorry");
    public static final String             PARTNER         = "partner";

    @InjectMocks
    private DefaultFeignErrorCodeBuilderSpi errorCodeBuilder;

    // =================================================================================================================
    // accept
    // =================================================================================================================
    @Test
    void accept_nominal() {
        assertThat(errorCodeBuilder.accept(null)).isTrue();
        assertThat(errorCodeBuilder.accept(PARTNER)).isTrue();
    }

    // =================================================================================================================
    // buildErrorCode
    // =================================================================================================================
    @Test
    void buildErrorCode_nominal() {
        assertThat(errorCodeBuilder.buildErrorCode(null, EXCEPTION)).isEqualTo(DefaultFeignErrorCodeBuilderSpi.UNDEFINED);
        assertThat(errorCodeBuilder.buildErrorCode(PARTNER, EXCEPTION)).isEqualTo("partner-500");
        assertThat(errorCodeBuilder.buildErrorCode(PARTNER, FEIGN_EXCEPTION)).isEqualTo("partner-404");
    }


    private static class MyFeignException extends FeignException {
        protected MyFeignException(final int status, final String message) {
            super(status, message);
        }
    }
}