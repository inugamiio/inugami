package io.inugami.commons.test.api;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class RegexLineMatcherTest {

    @Test
    public void accept_withoutPattern_shouldReturnFalse() {
        assertThat(RegexLineMatcher.builder().build().accept(0, null, null)).isFalse();
    }

    @Test
    public void skip_nominal_shouldReturnFalse() {
        assertThat(RegexLineMatcher.builder().build().skip(0, null, null)).isFalse();
    }

    @Test
    public void match_nominal_shouldMatch() {
        assertThat(RegexLineMatcher.of("hello.*", 1).match(0, "value", null)).isFalse();
        assertThat(RegexLineMatcher.of("hello.*", 1).match(0, "hello the world", null)).isTrue();

    }
}