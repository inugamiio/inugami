package io.inugami.monitoring.core.context;

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
                             "x-conversation-id" : "8723cd94-f9d1-4cfb-b675-8cf2fef0b7fe"
                           }
                           """);
    }

    @Test
    void getInformation_withoutValues() {
        assertText(SERVICE.getInformation(),
                   "{ }");
    }
}