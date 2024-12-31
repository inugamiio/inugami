package io.inugami.commons.test.api;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class NumberLineMatcherTest {
    @Test
    void accept_nominal() {
        final NumberLineMatcher matcher = buildMatcher();
        assertThat(matcher.accept(1, null, null)).isTrue();
        assertThat(matcher.accept(0, null, null)).isFalse();
    }

    @Test
    void skip_nominal() {
        final NumberLineMatcher matcher = buildMatcher();
        assertThat(matcher.skip(0, null, null)).isFalse();
    }

    @Test
    void match_nominal() {
        final NumberLineMatcher matcher = buildMatcher();
        assertThat(matcher.match(0, "", null)).isFalse();
        assertThat(matcher.match(0, "12345", null)).isTrue();
        assertThat(matcher.match(0, "id: 12345", null)).isTrue();
    }

    private NumberLineMatcher buildMatcher() {
        return NumberLineMatcher.of(1);
    }
}