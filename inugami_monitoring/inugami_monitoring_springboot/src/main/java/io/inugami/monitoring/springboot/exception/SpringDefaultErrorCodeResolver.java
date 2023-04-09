package io.inugami.monitoring.springboot.exception;

import io.inugami.api.exceptions.DefaultErrorCode;
import io.inugami.api.exceptions.ErrorCode;
import io.inugami.api.exceptions.ErrorCodeResolver;
import io.inugami.api.exceptions.ExceptionWithErrorCode;
import io.inugami.api.spi.SpiPriority;

@SpiPriority(SpiPriority.LOWER_PRIORITY)
public class SpringDefaultErrorCodeResolver implements ErrorCodeResolver {

    @Override
    public ErrorCode resolve(final Throwable exception) {
        ErrorCode result = null;
        if (exception instanceof ExceptionWithErrorCode) {
            result = ((ExceptionWithErrorCode) exception).getErrorCode();
        } else {
            result = DefaultErrorCode.builder()
                                     .message(exception.getMessage())
                                     .build();
        }
        return result;
    }
}
