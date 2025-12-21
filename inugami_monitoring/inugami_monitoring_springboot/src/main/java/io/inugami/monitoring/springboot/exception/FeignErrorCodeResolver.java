/* --------------------------------------------------------------------
 *  Inugami
 * --------------------------------------------------------------------
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package io.inugami.monitoring.springboot.exception;

import feign.FeignException;
import io.inugami.framework.api.exceptions.ExceptionUtils;
import io.inugami.framework.api.listeners.DefaultApplicationLifecycleSPI;
import io.inugami.framework.api.monitoring.MdcService;
import io.inugami.framework.interfaces.exceptions.DefaultErrorCode;
import io.inugami.framework.interfaces.exceptions.ErrorCode;
import io.inugami.framework.interfaces.exceptions.ErrorCodeResolver;
import io.inugami.framework.interfaces.exceptions.ExceptionWithErrorCode;
import io.inugami.framework.interfaces.listeners.ApplicationLifecycleSPI;
import io.inugami.framework.interfaces.monitoring.spring.feign.FeignErrorCodeBuilderSpi;
import io.inugami.framework.interfaces.spi.SpiLoader;

import java.util.List;
import java.util.Optional;

public class FeignErrorCodeResolver implements ErrorCodeResolver, ApplicationLifecycleSPI {
    // =================================================================================================================
    // ATTRIBUTES
    // =================================================================================================================
    private List<FeignErrorCodeBuilderSpi> feignErrorCodeBuilderSpi;

    // =================================================================================================================
    // CONSTRUCTOR
    // =================================================================================================================
    public FeignErrorCodeResolver() {
        initErrorCodeBuilder();
        DefaultApplicationLifecycleSPI.register(this);
    }

    @Override
    public void onContextRefreshed(final Object event) {
        initErrorCodeBuilder();
    }

    private void initErrorCodeBuilder() {
        feignErrorCodeBuilderSpi = SpiLoader.getInstance().loadSpiServicesByPriority(FeignErrorCodeBuilderSpi.class);
    }


    // =================================================================================================================
    // RESOLVE
    // =================================================================================================================
    @Override
    public ErrorCode resolve(final Throwable exception) {
        ErrorCode result = null;
        if (exception instanceof FeignException feignException) {
            result = buildError(feignException);
        } else if (exception instanceof ExceptionWithErrorCode exceptionErrorCode) {
            result = exceptionErrorCode.getErrorCode();
        }
        return result;
    }

    private ErrorCode buildError(final FeignException exception) {
        final String partner = MdcService.getInstance().partner();
        final int    status  = exception.status();

        final DefaultErrorCode.DefaultErrorCodeBuilder error        = DefaultErrorCode.buildUndefineErrorCode();
        final FeignErrorCodeBuilderSpi                 errorBuilder = resolveErrorBuilder(partner);

        final Throwable cause = ExceptionUtils.searchCause(4, exception);
        error.errorCode(errorBuilder.buildErrorCode(partner, exception))
             .statusCode(status)
             .message(exception.getMessage())
             .messageDetail(Optional.ofNullable(cause).map(Throwable::getMessage).orElse(null));

        return error.build();
    }

    private FeignErrorCodeBuilderSpi resolveErrorBuilder(final String partner) {
        for (final FeignErrorCodeBuilderSpi instance : feignErrorCodeBuilderSpi) {
            if (instance.accept(partner)) {
                return instance;
            }
        }
        // but not possible
        return new DefaultFeignErrorCodeBuilderSpi();
    }


}
