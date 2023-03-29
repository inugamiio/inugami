package io.inugami.monitoring.core.interceptors;

import io.inugami.api.exceptions.DefaultErrorCode;
import io.inugami.api.monitoring.JavaRestMethodResolver;
import io.inugami.api.monitoring.JavaRestMethodTracker;
import io.inugami.api.monitoring.MdcService;
import io.inugami.api.monitoring.exceptions.ErrorResult;
import io.inugami.monitoring.api.exceptions.ExceptionResolver;
import io.inugami.monitoring.api.resolvers.Interceptable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class FilterInterceptorTest {
    @Mock
    private JavaRestMethodResolver javaRestMethodResolver;

    @Mock
    private JavaRestMethodTracker javaRestMethodTracker;

    @Mock
    private Interceptable interceptable;

    @Mock
    private ExceptionResolver exceptionResolver;


    @Test
    public void resolveError_withoutError_shouldReturnNull() {
        FilterInterceptor filter = buildFilter();
        assertThat(filter.resolveError(null)).isNull();
    }

    @Test
    public void resolveError_witErrorCodeInMdc_shouldResolveError() {
        MdcService.getInstance()
                  .clear()
                  .errorCode(DefaultErrorCode.buildUndefineError());

        FilterInterceptor filter = buildFilter();
        final ErrorResult error  = filter.resolveError(null);
        assertThat(error).isNotNull();
        assertThat(error.getErrorCode()).isEqualTo("err-undefine");
        assertThat(error.getHttpCode()).isEqualTo(500);
        assertThat(error.getErrorType()).isEqualTo("technical");
    }

    private FilterInterceptor buildFilter() {
        return new FilterInterceptor(
                List.of(javaRestMethodResolver),
                List.of(javaRestMethodTracker),
                List.of(interceptable),
                List.of(exceptionResolver)
        );
    }

}

