package io.inugami.commons.test.api;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UuidLineMatcherTest {

    @Test
    void accept_nominal() {
        final UuidLineMatcher matcher = buildMatcher();
        assertThat(matcher.accept(1, null, null)).isTrue();
        assertThat(matcher.accept(0, null, null)).isFalse();
    }

    @Test
    void skip_nominal() {
        final UuidLineMatcher matcher = buildMatcher();
        assertThat(matcher.skip(0, null, null)).isFalse();
    }

    @Test
    void match_nominal() {
        final UuidLineMatcher matcher = buildMatcher();
        assertThat(matcher.match(0, "", null)).isFalse();
        assertThat(matcher.match(0, "6f2bd193-965d-40b2-ba8a-4a85418078ab", null)).isTrue();
    }

    private UuidLineMatcher buildMatcher() {
        return UuidLineMatcher.of(1);
    }
}