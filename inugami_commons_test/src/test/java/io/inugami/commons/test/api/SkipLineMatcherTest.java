package io.inugami.commons.test.api;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class SkipLineMatcherTest {

    @Test
    public void accept_nominal_shouldAcceptDefinedLines() {
        assertThat(SkipLineMatcher.of(1).accept(1, null, null)).isTrue();
        assertThat(SkipLineMatcher.of(1).accept(2, null, null)).isFalse();
    }

    @Test
    public void skip_nominal_returnTrue() {
        assertThat(SkipLineMatcher.of(1).skip(0, null, null)).isTrue();
        assertThat(SkipLineMatcher.of(1).skip(1, null, null)).isTrue();
    }

    @Test
    public void match_nominal_returnTrue() {
        assertThat(SkipLineMatcher.of(1).match(0, null, null)).isTrue();
        assertThat(SkipLineMatcher.of(1).match(1, null, null)).isTrue();
    }

    @Test
    public void builder_nominal() {
        assertThat(SkipLineMatcher.builder()
                                  .line(1)
                                  .build()
                                  .toBuilder()
                                  .build()
                                  .accept(1, null, null)).isTrue();
    }
}