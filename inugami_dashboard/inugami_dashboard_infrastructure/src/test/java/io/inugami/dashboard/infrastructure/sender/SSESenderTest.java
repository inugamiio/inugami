package io.inugami.dashboard.infrastructure.sender;

import io.inugami.commons.test.UnitTestHelper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class SSESenderTest {
    //==================================================================================================================
    // ATTRIBUTES
    //==================================================================================================================
    @InjectMocks
    private SSESender sender;


    //==================================================================================================================
    // SEND
    //==================================================================================================================
    @Test
    void onEventDone_nominal() {
        UnitTestHelper.assertLogs(() -> sender.onEventDone(List.of()),
                                  SSESender.class,
                                  """
                                          [
                                              {
                                                  "loggerName":"io.inugami.dashboard.infrastructure.sender.SSESender",
                                                  "level":"INFO",
                                                  "mdc":{}
                                                  "message":"send SSE event ...."
                                              }
                                          ]
                                          """);
    }
}