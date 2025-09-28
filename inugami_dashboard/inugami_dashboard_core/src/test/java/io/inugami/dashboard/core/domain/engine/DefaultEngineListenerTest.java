package io.inugami.dashboard.core.domain.engine;


import io.inugami.dashboard.api.domain.engine.dto.EnginePluginEventResultDTO;
import io.inugami.dashboard.api.domain.engine.dto.EnginePluginResultDTO;
import io.inugami.dashboard.api.domain.engine.dto.EngineResultDTO;
import io.inugami.framework.interfaces.exceptions.DefaultErrorCode;
import io.inugami.framework.interfaces.models.engine.Status;
import io.inugami.framework.interfaces.models.maven.Gav;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

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
}