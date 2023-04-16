package io.inugami.commons.test.logs;

import io.inugami.api.loggers.Loggers;
import io.inugami.api.monitoring.MdcService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static io.inugami.commons.test.UnitTestHelper.assertTextRelative;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Slf4j
public class LogTestAppenderTest {


    @Test
    public void register_nominal() {
        final List<BasicLogEvent> logs     = new ArrayList<>();
        final LogListener         listener = new DefaultLogListener(".*", logs::add);
        LogTestAppender.register(listener);
        process();
        LogTestAppender.removeListener(listener);
        assertTextRelative(logs, "test/logs/logTestAppender/register_nominal.json");
    }

    @Test
    public void register_withJustOneLogger_shouldHandlerOnlyThisLogger() {
        final List<BasicLogEvent> logs     = new ArrayList<>();
        final LogListener         listener = new DefaultLogListener(Loggers.DEBUGLOG_NAME, logs::add);
        LogTestAppender.register(listener);
        process();
        LogTestAppender.removeListener(listener);
        assertThat(LogTestAppender.LISTENERS.isEmpty());
        assertTextRelative(logs, "test/logs/logTestAppender/register_withJustOneLogger_shouldHandlerOnlyThisLogger.json");
    }

    @Test
    public void register_withClass_shouldHandlerOnlyThisLogger() {
        final List<BasicLogEvent> logs     = new ArrayList<>();
        final LogListener         listener = new DefaultLogListener(LogTestAppenderTest.class, logs::add);
        LogTestAppender.register(listener);
        process();
        LogTestAppender.removeListener(listener);
        assertTextRelative(logs, "test/logs/logTestAppender/register_withClass_shouldHandlerOnlyThisLogger.json");
    }

    private static void process() {
        MdcService.getInstance().clear()
                  .appMethod("register_nominal");
        log.info("hello the world");
        log.info("some value : {}", "Hello");
        Loggers.DEBUG.info("debug information");
        MdcService.getInstance().clear();
    }
}