package io.inugami.monitoring.api.resolvers;

import io.inugami.framework.interfaces.monitoring.dto.InterceptorContextDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class DefaultServiceNameResolverTest {
    // =================================================================================================================
    // ATTRIBUTES
    // =================================================================================================================
    @InjectMocks
    private DefaultServiceNameResolver resolver;

    @Test
    void resolve_nominal() {
        assertThat(resolver.resolve(InterceptorContextDto.builder().build())).isNull();

        assertThat(resolver.resolve(InterceptorContextDto.builder()
                                                         .url("ftp://myserver.com/api/service")
                                                         .build())).isNull();

        assertThat(resolver.resolve(InterceptorContextDto.builder()
                                                         .url("http://localhost:8080/api/users")
                                                         .build())).isEqualTo("api/users");

        assertThat(resolver.resolve(InterceptorContextDto.builder()
                                                         .url("http://127.0.0.1/path/to/resource/")
                                                         .build())).isEqualTo("path/to/resource/");

        assertThat(resolver.resolve(InterceptorContextDto.builder()
                                                         .url("https://domain.com/single")
                                                         .build())).isEqualTo("single");

        assertThat(resolver.resolve(InterceptorContextDto.builder()
                                                         .url("http://domain.com/")
                                                         .build())).isEmpty();
    }

}