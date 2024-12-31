package io.inugami.commons.test;


import io.inugami.commons.test.api.SkipLineMatcher;
import io.inugami.commons.test.dto.AssertLogContext;
import io.inugami.framework.api.monitoring.MdcService;
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
                                                      .logs("""
                                                                    [
                                                                        {
                                                                            "loggerName":"io.inugami.commons.test.UnitTestHelperLogsTest$Service",
                                                                            "level":"INFO",
                                                                            "mdc":{
                                                                                "appSubService":"sayHello",
                                                                                "service":"Service"
                                                                            },
                                                                            "message":"hello"
                                                                        }
                                                                    ]
                                                                    """
                                                      )
                                                      .build());
    }

    @Test
    void assertLogs_directCall() {
        final Service service = new Service();

        UnitTestHelperLogs.assertLogs(service::sayHello,
                                      Service.class,
                                      """
                                              [
                                                  {
                                                      "loggerName":"io.inugami.commons.test.UnitTestHelperLogsTest$Service",
                                                      "level":"INFO",
                                                      "mdc":{
                                                          "appSubService":"sayHello",
                                                          "service":"Service"
                                                      },
                                                      "message":"hello"
                                                  }
                                              ]
                                              """);
    }


    @Test
    void assertLogs_withException() {
        final Service service = new Service();

        assertThrows("some error",
                     () -> UnitTestHelperLogs.assertLogs(service::shouldThrow,
                                                         Service.class,
                                                         """
                                                                 [
                                                                     {
                                                                         "loggerName":"io.inugami.commons.test.UnitTestHelperLogsTest$Service",
                                                                         "level":"ERROR",
                                                                         "mdc":{}
                                                                         "message":"sorry"
                                                                     }
                                                                 ]
                                                                 """));

        UnitTestHelperLogs.assertLogs(() -> {
                                          assertThrows("some error", service::shouldThrow);
                                      },
                                      Service.class,
                                      """
                                              [
                                                  {
                                                      "loggerName":"io.inugami.commons.test.UnitTestHelperLogsTest$Service",
                                                      "level":"ERROR",
                                                      "mdc":{}
                                                      "message":"sorry"
                                                  }
                                              ]
                                              """);


    }


    @Test
    void assertLogs_withMdc() {
        final Service service = new Service();

        MdcService.getInstance()
                  .clear()
                  .appService("service");
        UnitTestHelperLogs.assertLogs(AssertLogContext.builder()
                                                      .process(service::sayHello)
                                                      .addClass(Service.class)
                                                      .cleanMdcDisabled()
                                                      .logs("""
                                                                    [
                                                                        {
                                                                            "loggerName":"io.inugami.commons.test.UnitTestHelperLogsTest$Service",
                                                                            "level":"INFO",
                                                                            "mdc":{
                                                                                "appService":"service",
                                                                                "appSubService":"sayHello",
                                                                                "service":"Service"
                                                                            },
                                                                            "message":"hello"
                                                                        }
                                                                    ]
                                                                    """)
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
                                                      .logs("""
                                                                    [
                                                                        {
                                                                            "loggerName":"io.inugami.commons.test.UnitTestHelperLogsTest$Service",
                                                                            "level":"INFO",
                                                                            "mdc":{
                                                                                "appSubService":"sayHello",
                                                                                "service":"Service",
                                                                                "userId":"e744ca85-c135-4439-b0f3-16d05c60e027"
                                                                            },
                                                                            "message":"hello"
                                                                        }
                                                                    ]
                                                                    """)
                                                      .addLineMatcher(SkipLineMatcher.of(7))
                                                      .build());

        MdcService.getInstance().clear();
    }

    // =================================================================================================================
    // TOOLS
    // =================================================================================================================
    @Slf4j
    static class Service {

        void sayHello() {
            MdcService.getInstance().service(Service.class.getSimpleName());
            MdcService.getInstance().appSubService("sayHello");
            log.info("hello");
        }

        void shouldThrow() throws Exception {
            log.error("sorry");
            throw new Exception("some error");
        }

    }
}