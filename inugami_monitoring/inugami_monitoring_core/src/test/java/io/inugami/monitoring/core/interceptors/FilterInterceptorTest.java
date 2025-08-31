package io.inugami.monitoring.core.interceptors;

import io.inugami.framework.api.monitoring.MdcService;
import io.inugami.framework.api.processors.DefaultConfigHandler;
import io.inugami.framework.interfaces.exceptions.DefaultErrorCode;
import io.inugami.framework.interfaces.monitoring.ErrorResult;
import io.inugami.framework.interfaces.monitoring.JavaRestMethodResolver;
import io.inugami.framework.interfaces.monitoring.JavaRestMethodTracker;
import io.inugami.monitoring.api.exceptions.ExceptionResolver;
import io.inugami.monitoring.api.resolvers.Interceptable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
class FilterInterceptorTest {
    @Mock
    private JavaRestMethodResolver javaRestMethodResolver;

    @Mock
    private JavaRestMethodTracker javaRestMethodTracker;

    @Mock
    private Interceptable interceptable;

    @Mock
    private ExceptionResolver exceptionResolver;


    @Test
    void resolveError_withoutError_shouldReturnNull() {
        final FilterInterceptor filter = buildFilter();
        assertThat(filter.resolveError(null, null)).isNull();
    }

    @Test
    void resolveError_witErrorCodeInMdc_shouldResolveError() {
        MdcService.getInstance()
                  .clear()
                  .errorCode(DefaultErrorCode.buildUndefineError());

        final FilterInterceptor filter = buildFilter();
        final ErrorResult       error  = filter.resolveError(null, null);
        assertThat(error).isNotNull();
        assertThat(error.getErrorCode()).isEqualTo("err-undefine");
        assertThat(error.getHttpCode()).isEqualTo(500);
        assertThat(error.getErrorType()).isEqualTo("technical");
    }

    private FilterInterceptor buildFilter() {
        return FilterInterceptor.builder()
                                .javaRestMethodResolvers(List.of(javaRestMethodResolver))
                                .javaRestMethodTrackers(List.of(javaRestMethodTracker))
                                .exceptionResolver(List.of(exceptionResolver))
                                .interceptableResolver(List.of(interceptable))
                                .configuration(new DefaultConfigHandler())
                                .purgeCacheStrategy(new DefaultFilterInterceptorCachePurgeStrategy())
                                .build();
    }

}

