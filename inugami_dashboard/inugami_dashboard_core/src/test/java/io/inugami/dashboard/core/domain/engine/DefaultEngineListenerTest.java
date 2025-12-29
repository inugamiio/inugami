package io.inugami.dashboard.core.domain.engine;


import io.inugami.commons.test.UnitTestData;
import io.inugami.dashboard.api.domain.engine.dto.EnginePluginEventResultDTO;
import io.inugami.dashboard.api.domain.engine.dto.EnginePluginResultDTO;
import io.inugami.dashboard.api.domain.engine.dto.EngineResultDTO;
import io.inugami.framework.configuration.models.plugins.Plugin;
import io.inugami.framework.interfaces.exceptions.DefaultErrorCode;
import io.inugami.framework.interfaces.exceptions.UncheckedException;
import io.inugami.framework.interfaces.models.engine.Status;
import io.inugami.framework.interfaces.models.event.SimpleEvent;
import io.inugami.framework.interfaces.models.maven.Gav;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static io.inugami.commons.test.UnitTestHelper.assertLogs;
import static io.inugami.commons.test.UnitTestHelper.assertText;

@ExtendWith(MockitoExtension.class)
class DefaultEngineListenerTest {
    //==================================================================================================================
    // ATTRIBUTES
    //==================================================================================================================
    @InjectMocks
    private DefaultEngineListener listener;

    //==================================================================================================================
    // ON DONE
    //==================================================================================================================
    @Test
    void onDone_nominal() {
        assertLogs(() -> listener.onDone(EngineResultDTO.builder()
                                                        .status(Status.SUCCESS)
                                                        .start(UnitTestData.DATE_TIME.minusMinutes(5))
                                                        .end(UnitTestData.DATE_TIME)
                                                        .build()),
                   DefaultEngineListener.class,
                   """
                           [
                               {
                                   "loggerName":"io.inugami.dashboard.core.domain.engine.DefaultEngineListener",
                                   "level":"INFO",
                                   "mdc":{}
                                   "message":"successful engine running (starting:2023-06-01T11:55 |finish : 2023-06-01T12:00)"
                               }
                           ]
                           """);

        assertLogs(() -> listener.onDone(EngineResultDTO.builder()
                                                        .status(Status.ERROR)
                                                        .start(UnitTestData.DATE_TIME.minusMinutes(5))
                                                        .end(UnitTestData.DATE_TIME)
                                                        .build()),
                   DefaultEngineListener.class,
                   """
                           [
                                 {
                                     "loggerName":"io.inugami.dashboard.core.domain.engine.DefaultEngineListener",
                                     "level":"ERROR",
                                     "mdc":{}
                                     "message":[
                                         "error on engine running (starting:2023-06-01T11:55 |finish : 2023-06-01T12:00) ",
                                         "status:	ERROR"
                                     ]
                                 }
                             ]
                           """);
    }

    @Test
    void onEventDone_nominal() {
        assertLogs(() -> listener.onEventDone(Plugin.builder()
                                                    .gav(buildGav())
                                                    .build(),
                                              SimpleEvent.builder().name("inu-test").build(),
                                              EnginePluginEventResultDTO.builder()
                                                                        .status(Status.SUCCESS)
                                                                        .build()),
                   DefaultEngineListener.class,
                   """
                           [
                               {
                                   "loggerName":"io.inugami.dashboard.core.domain.engine.DefaultEngineListener",
                                   "level":"DEBUG",
                                   "mdc":{}
                                   "message":"successful event running (io.inugami:inugami_api:3.3.0:jar:inu-test)"
                               }
                           ]
                           """);

        assertLogs(() -> listener.onEventDone(Plugin.builder()
                                                    .gav(buildGav())
                                                    .build(),
                                              SimpleEvent.builder().name("inu-test").build(),
                                              EnginePluginEventResultDTO.builder()
                                                                        .status(Status.ERROR)
                                                                        .error(new UncheckedException("sorry"))
                                                                        .errorCode(DefaultErrorCode.buildUndefineError())
                                                                        .build()),
                   DefaultEngineListener.class,
                   """
                           [
                               {
                                   "loggerName":"io.inugami.dashboard.core.domain.engine.DefaultEngineListener",
                                   "level":"DEBUG",
                                   "mdc":{}
                                   "message":[
                                       "error on event running (starting:io.inugami:inugami_api:3.3.0:jar |finish : inu-test) ",
                                       "status:	ERROR",
                                       "message:	null",
                                       "errorCode:	err-undefine",
                                       "error:	sorry"
                                   ]
                               }
                           ]
                           """);

    }

    @Test
    void buildErrorInfo_nominal() {
        final EngineResultDTO data = EngineResultDTO.builder()
                                                    .start(LocalDateTime.of(2025, 9, 28, 14, 17, 25))
                                                    .end(LocalDateTime.of(2025, 9, 28, 14, 25, 14))
                                                    .status(Status.ERROR)
                                                    .plugins(EnginePluginResultDTO.builder()
                                                                                  .gav(Gav.builder()
                                                                                          .groupId("io.inugami")
                                                                                          .artifactId("plugin-test")
                                                                                          .version("1.0.0")
                                                                                          .build())
                                                                                  .status(Status.ERROR)
                                                                                  .events(EnginePluginEventResultDTO.builder()
                                                                                                                    .name("count_calls")
                                                                                                                    .status(Status.ERROR)
                                                                                                                    .errorCode(DefaultErrorCode.buildUndefineError())
                                                                                                                    .build())
                                                                                  .build())
                                                    .build();

        assertText(listener.buildErrorInfo(data),
                   """
                           {
                             "data" : {
                               "status" : "ERROR",
                               "io.inugami:plugin-test:1.0.0" : {
                                 "data" : {
                                   "status" : "ERROR",
                                   "count_calls" : {
                                     "data" : {
                                       "status" : "ERROR",
                                       "errorCode" : "err-undefine"
                                     }
                                   }
                                 }
                               }
                             }
                           }
                           """);
    }

    private Gav buildGav() {
        return Gav.builder()
                  .groupId("io.inugami")
                  .artifactId("inugami_api")
                  .version("3.3.0")
                  .qualifier("jar")
                  .build()
                  .toBuilder()
                  .build();
    }

}