package io.inugami.commons.test.api;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class DefaultLineMatcherTest {

    @Test
    public void accept_nominal_shouldAccept() {
        assertThat(new DefaultLineMatcher().accept(0, null, null)).isTrue();
        assertThat(new DefaultLineMatcher().accept(0, "", "")).isTrue();
    }

    @Test
    public void skip_nominal_shouldNotAccept() {
        assertThat(new DefaultLineMatcher().skip(0, null, null)).isFalse();
        assertThat(new DefaultLineMatcher().skip(0, "", "")).isFalse();
    }

    @Test
    public void match_nominal_shouldMatchByEquals() {
        assertThat(new DefaultLineMatcher().match(0, "joe", "joe")).isTrue();
        assertThat(new DefaultLineMatcher().match(1, "joe", "joe")).isTrue();

        assertThat(new DefaultLineMatcher().match(0, "joe", "")).isFalse();
        assertThat(new DefaultLineMatcher().match(1, "", "joe")).isFalse();
    }
}