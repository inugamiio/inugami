package io.inugami.monitoring.springboot;

import io.inugami.framework.interfaces.monitoring.data.RequestData;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PropertiesInterceptableTest {

    @Test
    void isInterceptable_nominal() {
        final var interceptable = buildInterceptable("/some/uri.*");
        assertThat(interceptable.isInterceptable(RequestData.builder()
                                                            .uri("/some/uri")
                                                            .build()))
                .isTrue();
        assertThat(interceptable.isInterceptable(RequestData.builder()
                                                            .uri("/other/uri")
                                                            .build()))
                .isFalse();
    }

    @Test
    void isInterceptable_withoutSkip() {
        final var interceptable = buildInterceptable(null);
        assertThat(interceptable.isInterceptable(RequestData.builder()
                                                            .uri("/some/uri")
                                                            .build()))
                .isTrue();
        assertThat(interceptable.isInterceptable(RequestData.builder()
                                                            .uri("/other/uri")
                                                            .build()))
                .isTrue();
    }


    PropertiesInterceptable buildInterceptable(final String pattern) {
        final var result = new PropertiesInterceptable();
        result.setSkipUrl(pattern);
        result.init();
        return result;
    }

}