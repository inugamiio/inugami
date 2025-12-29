package io.inugami.commons.test.obfuscator;

import io.inugami.framework.interfaces.monitoring.logger.BasicLogEvent;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static io.inugami.commons.test.UnitTestHelper.assertText;
import static io.inugami.commons.test.obfuscator.DefaultITObfuscator.renderLogs;

class DefaultITObfuscatorTest {

    @Test
    void renderLogs_nominal() {
        assertText(renderLogs(List.of(
                           BasicLogEvent.builder()
                                        .loggerName("IOLOG")
                                        .level("INFO")
                                        .mdc(Map.of("user-agent", "test",
                                                    "content-type", "application/json; charset=UTF-8"))
                                        .message("Hello the world")
                                        .build(),
                           BasicLogEvent.builder()
                                        .loggerName("IOLOG")
                                        .level("ERROR")
                                        .mdc(Map.of("user-agent", "test",
                                                    "content-type", "application/json; charset=UTF-8"))
                                        .message("Something else")
                                        .build()
                                     )),
                   """
                           ================================================================================
                           INFO  | IOLOG
                           MDC :
                           -----
                           	content-type : application/json; charset=UTF-8
                           	user-agent : test
                           
                           MESSAGE :
                           ---------
                           Hello the world
                           ----------------------------------------
                           ================================================================================
                           ERROR  | IOLOG
                           MDC :
                           -----
                           	content-type : application/json; charset=UTF-8
                           	user-agent : test
                           
                           MESSAGE :
                           ---------
                           Something else
                           ----------------------------------------
                           """);
    }
}