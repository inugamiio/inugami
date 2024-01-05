package io.inugami.commons.test.dto;

import io.inugami.commons.test.api.SkipLineMatcher;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AssertLogContextTest {

    @Test
    void addClass_withNull() {
        assertThat(AssertLogContext.builder().addClass(null).build().getClasses()).isEmpty();
    }

    @Test
    void addPattern_nominal() {
        assertThat(AssertLogContext.builder().addPattern(null).build().getPatterns()).isEmpty();
        assertThat(AssertLogContext.builder().addPattern("value").build().getPatterns().get(0)).isEqualTo("value");
    }

    @Test
    void cleanMdcEnabled_nominal() {
        assertThat(AssertLogContext.builder().cleanMdcEnabled().build().getCleanMdc()).isTrue();
    }

    @Test
    void unitTest_nominal() {
        assertThat(AssertLogContext.builder().unitTest().build().getIntegrationTest()).isFalse();
        assertThat(AssertLogContext.builder().integrationTestEnabled().build().getIntegrationTest()).isTrue();
    }

    @Test
    void addLineMatcher_nominal() {
        assertThat(AssertLogContext.builder().addLineMatcher(SkipLineMatcher.of(1)).build().getLineMatchers()).isNotEmpty();
        assertThat(AssertLogContext.builder().addLineMatcher(null).build().getLineMatchers()).isEmpty();
    }
}