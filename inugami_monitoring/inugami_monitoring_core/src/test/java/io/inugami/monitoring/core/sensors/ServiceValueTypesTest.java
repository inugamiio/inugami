package io.inugami.monitoring.core.sensors;

import io.inugami.commons.test.UnitTestHelper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class ServiceValueTypesTest {
    @Test
    void assertEnum() {
        UnitTestHelper.assertEnum(ServiceValueTypes.class,
                                  """
                                          {
                                            "HITS" : {
                                              "keywork" : "hits"
                                            },
                                            "DONE" : {
                                              "keywork" : "done"
                                            },
                                            "ERROR" : {
                                              "keywork" : "error"
                                            },
                                            "RESPONSE_TIME" : {
                                              "keywork" : "responseTime"
                                            }
                                          }
                                          """);
    }

    @Test
    void getKeyword_nominal() {
        assertThat(ServiceValueTypes.DONE.getKeywork()).isEqualTo("done");
    }
}