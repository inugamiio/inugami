package io.inugami.logs.obfuscator.appender;

import io.inugami.framework.api.monitoring.MdcService;
import io.inugami.framework.interfaces.exceptions.DefaultErrorCode;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings({"java:S2699"})
class JsonAppenderTest {
    private final static Logger LOGGER = LoggerFactory.getLogger(JsonAppenderTest.class);

    @Test
    void test_appender() {

        LOGGER.debug("debug information");
        LOGGER.info("hello");
        logError();
        LOGGER.warn("some warning");
    }

    private void logError() {
        MdcService.getInstance().errorCode(
                DefaultErrorCode.buildUndefineErrorCode()
                                .category("user")
                                .statusCode(500)
                                .errorCode("ERR-0001")
                                .message("some error occurs")
                                .field("user.name")
                                .addMessageDetail("detail {0}", "some message")
                                .retryable(true)
                                .rollback(true)
                                .exploitationError()
                                .build()
        );

        LOGGER.error("oups");

        MdcService.getInstance()
                  .duration(250L)
                  .quantity(25)
                  .size(125)
                  .price(32.50);
        LOGGER.error("other error");

        anotherMethod();
    }

    private void anotherMethod() {
        LOGGER.info("action from another method");
    }
}