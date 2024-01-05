package io.inugami.commons.test.api;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class LocalDateTimeLineMatcherTest {

    @Test
    void match() {
        final LocalDateLineMatcher matcher = new LocalDateLineMatcher(List.of(1));
        assertThat(matcher.match(1, LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME), "")).isTrue();
    }
}