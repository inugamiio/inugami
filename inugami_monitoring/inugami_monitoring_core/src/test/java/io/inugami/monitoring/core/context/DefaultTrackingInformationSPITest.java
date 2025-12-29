package io.inugami.monitoring.core.context;

import io.inugami.commons.test.api.UuidLineMatcher;
import io.inugami.framework.api.monitoring.MdcService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static io.inugami.commons.test.UnitTestHelper.assertText;

@ExtendWith(MockitoExtension.class)
class DefaultTrackingInformationSPITest {
    // =================================================================================================================
    // ATTRIBUTES
    // =================================================================================================================
    public static final DefaultTrackingInformationSPI SERVICE = new DefaultTrackingInformationSPI();

    // =================================================================================================================
    // INIT
    // =================================================================================================================
    @BeforeEach
    public void init() {
        MdcService.getInstance().clear();
    }


    // =================================================================================================================
    // ATTRIBUTES
    // =================================================================================================================
    @Test
    void getInformation_nominal() {
        final var mdc = MdcService.getInstance();
        mdc.deviceIdentifier("615fcdae-b1c8-4ce8-b3b4-324445eccaba");
        mdc.correlationId("6f73b358-bfbe-42df-a7d0-3cf7bd517323");
        mdc.conversationId("8723cd94-f9d1-4cfb-b675-8cf2fef0b7fe");

        assertText(SERVICE.getInformation(),
                   """
                           {
                              "x-device-identifier" : "615fcdae-b1c8-4ce8-b3b4-324445eccaba",
                              "x-correlation-id" : "6f73b358-bfbe-42df-a7d0-3cf7bd517323",
                              "x-conversation-id" : "8723cd94-f9d1-4cfb-b675-8cf2fef0b7fe",
                              "x-b3-traceid" : "10298c78-f89e-48c4-a7d4-b57787d62220"
                            }
                           """,
                   UuidLineMatcher.of(4));
    }

    @Test
    void getInformation_withoutValues() {
        assertText(SERVICE.getInformation(),
                   """
                           {
                             "x-correlation-id" : "fba0aea9-0c67-42f7-92a6-a9a0a974a481",
                             "x-b3-traceid" : "8bac53b1-53c5-4e09-90c8-571dfd441d67"
                           }
                           """,
                   UuidLineMatcher.of(1,2));
    }
}