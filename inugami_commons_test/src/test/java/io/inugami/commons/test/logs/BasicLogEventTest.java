package io.inugami.commons.test.logs;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class BasicLogEventTest {

    @Test
    public void builder_nominal() {
        final BasicLogEvent log = BasicLogEvent.builder()
                                               .loggerName("IOLOG")
                                               .message("hello the world")
                                               .level("info")
                                               .mdc(Map.ofEntries(
                                                       Map.entry("app", "inugami")
                                               ))
                                               .build();

        assertThat(log.toString()).isEqualTo("BasicLogEvent(loggerName=IOLOG, message=hello the world, level=info, mdc={app=inugami})");
        assertThat(log).isEqualTo(log.toBuilder().build());
        assertThat(log.hashCode()).isEqualTo(log.toBuilder().build().hashCode());
    }
}