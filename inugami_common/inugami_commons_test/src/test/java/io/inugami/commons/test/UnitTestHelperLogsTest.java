package io.inugami.commons.test;

import io.inugami.api.monitoring.MdcService;
import io.inugami.commons.test.api.SkipLineMatcher;
import io.inugami.commons.test.dto.AssertLogContext;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static io.inugami.commons.test.UnitTestHelper.assertThrows;


class UnitTestHelperLogsTest {

    // =================================================================================================================
    // TEST
    // =================================================================================================================
    @Test
    void assertLogs_nominal() {
        final Service service = new Service();

        UnitTestHelperLogs.assertLogs(AssertLogContext.builder()
                                                      .process(service::sayHello)
                                                      .addClass(Service.class)
                                                      .logs("{\n" +
                                                            "  \"level\" : \"INFO\",\n" +
                                                            "  \"loggerName\" : \"io.inugami.commons.test.UnitTestHelperLogsTest$Service\",\n" +
                                                            "  \"mdc\" : { },\n" +
                                                            "  \"message\" : \"hello\"\n" +
                                                            "}")
                                                      .build());
    }

    @Test
    void assertLogs_directCall() {
        final Service service = new Service();

        UnitTestHelperLogs.assertLogs(service::sayHello,
                                      Service.class,
                                      "{\n" +
                                      "  \"level\" : \"INFO\",\n" +
                                      "  \"loggerName\" : \"io.inugami.commons.test.UnitTestHelperLogsTest$Service\",\n" +
                                      "  \"mdc\" : { },\n" +
                                      "  \"message\" : \"hello\"\n" +
                                      "}");
    }


    @Test
    void assertLogs_withException() {
        final Service service = new Service();

        assertThrows("some error",
                     () -> UnitTestHelperLogs.assertLogs(service::shouldThrow,
                                                         Service.class,
                                                         "{\n" +
                                                         "  \"level\" : \"ERROR\",\n" +
                                                         "  \"loggerName\" : \"io.inugami.commons.test.UnitTestHelperLogsTest$Service\",\n" +
                                                         "  \"mdc\" : { },\n" +
                                                         "  \"message\" : \"sorry\"\n" +
                                                         "}"));

        UnitTestHelperLogs.assertLogs(() -> {
                                          assertThrows("some error", service::shouldThrow);
                                      },
                                      Service.class,
                                      "{\n" +
                                      "  \"level\" : \"ERROR\",\n" +
                                      "  \"loggerName\" : \"io.inugami.commons.test.UnitTestHelperLogsTest$Service\",\n" +
                                      "  \"mdc\" : { },\n" +
                                      "  \"message\" : \"sorry\"\n" +
                                      "}");

    }


    @Test
    void assertLogs_withMdc() {
        final Service service = new Service();

        MdcService.getInstance().clear();
        MdcService.getInstance().appService("service");
        UnitTestHelperLogs.assertLogs(AssertLogContext.builder()
                                                      .process(service::sayHello)
                                                      .addClass(Service.class)
                                                      .cleanMdcDisabled()
                                                      .logs("{\n" +
                                                            "  \"level\" : \"INFO\",\n" +
                                                            "  \"loggerName\" : \"io.inugami.commons.test.UnitTestHelperLogsTest$Service\",\n" +
                                                            "  \"mdc\" : {\n" +
                                                            "    \"appService\" : \"service\"\n" +
                                                            "  },\n" +
                                                            "  \"message\" : \"hello\"\n" +
                                                            "}")
                                                      .build());

        MdcService.getInstance().clear();
    }

    @Test
    void assertLogs_withPath() {
        final Service service = new Service();

        UnitTestHelperLogs.assertLogs(AssertLogContext.builder()
                                                      .process(service::sayHello)
                                                      .addClass(Service.class)
                                                      .path("test/unitTestHelperLogsTest/assertLogs_withPath.json")
                                                      .build());

    }

    @Test
    void assertLogs_withPathOnIntegrationTest() {
        final Service service = new Service();

        UnitTestHelperLogs.assertLogs(AssertLogContext.builder()
                                                      .process(service::sayHello)
                                                      .addClass(Service.class)
                                                      .integrationTestEnabled()
                                                      .path("test/unitTestHelperLogsTest/assertLogs_withPathOnIntegrationTest.json")
                                                      .build());

    }

    @Test
    void assertLogs_withLineMatcher() {
        final Service service = new Service();

        MdcService.getInstance().clear();
        MdcService.getInstance().userId(UUID.randomUUID().toString());
        UnitTestHelperLogs.assertLogs(AssertLogContext.builder()
                                                      .process(service::sayHello)
                                                      .addClass(Service.class)
                                                      .cleanMdcDisabled()
                                                      .logs("{\n" +
                                                            "  \"level\" : \"INFO\",\n" +
                                                            "  \"loggerName\" : \"io.inugami.commons.test.UnitTestHelperLogsTest$Service\",\n" +
                                                            "  \"mdc\" : {\n" +
                                                            "    \"userId\" : \"831ddb11-b8e3-4e35-8459-6e5f6a82a150\"\n" +
                                                            "  },\n" +
                                                            "  \"message\" : \"hello\"\n" +
                                                            "}")
                                                      .addLineMatcher(SkipLineMatcher.of(4))
                                                      .build());

        MdcService.getInstance().clear();
    }

    // =================================================================================================================
    // TOOLS
    // =================================================================================================================
    @Slf4j
    static class Service {

        void sayHello() {
            log.info("hello");
        }

        void shouldThrow() throws Exception {
            log.error("sorry");
            throw new Exception("some error");
        }

    }
}