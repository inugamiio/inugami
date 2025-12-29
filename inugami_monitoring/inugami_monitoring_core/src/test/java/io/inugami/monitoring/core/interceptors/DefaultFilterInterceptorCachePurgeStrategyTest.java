package io.inugami.monitoring.core.interceptors;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static io.inugami.monitoring.core.interceptors.DefaultFilterInterceptorCachePurgeStrategy.MAX_ITEMS;
import static org.assertj.core.api.Assertions.assertThat;

class DefaultFilterInterceptorCachePurgeStrategyTest {

    @Test
    void shouldPurge_nominal() {
        final var interceptor = new DefaultFilterInterceptorCachePurgeStrategy();
        assertThat(interceptor.shouldPurge(null)).isFalse();
        assertThat(interceptor.shouldPurge(Map.of())).isFalse();

        final Map<String, Boolean> values = new HashMap<>();
        for (int i = MAX_ITEMS; i >= 0; i--) {
            values.put("" + i, Boolean.TRUE);
        }
        assertThat(interceptor.shouldPurge(values)).isTrue();
    }
}