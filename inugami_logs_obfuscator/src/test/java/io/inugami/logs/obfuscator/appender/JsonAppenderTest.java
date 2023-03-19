package io.inugami.logs.obfuscator.appender;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.*;

public class JsonAppenderTest {
    private final static Logger LOGGER = LoggerFactory.getLogger(JsonAppenderTest.class);

    @Test
    public void test_appender(){
        LOGGER.debug("debug information");
        LOGGER.info("hello");
        logError();
        LOGGER.warn("some warning");
    }

    private void logError() {
        LOGGER.error("oups");
        LOGGER.error("other error");

        anotherMethod();
    }

    private void anotherMethod() {
        LOGGER.info("action from another method");
    }
}