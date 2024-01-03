package io.inugami.api.exceptions;

import io.inugami.api.tools.unit.test.UnitTestData;
import io.inugami.interfaces.exceptions.DefaultWarning;
import io.inugami.interfaces.exceptions.Warning;
import io.inugami.interfaces.monitoring.logger.LogInfoDTO;
import org.junit.jupiter.api.Test;

import static io.inugami.api.tools.unit.test.UnitTestHelper.assertTextRelative;

class WarningTest {

    private static final Warning WARNING = DefaultWarning.builder()
                                                         .warningCode("WARN-1")
                                                         .message("token will expired")
                                                         .addMessageDetail("token : {0}", UnitTestData.UID)
                                                         .warningType("security")
                                                         .category("GET")
                                                         .domain("user")
                                                         .subDomain("search")
                                                         .build();

    private static final Warning WARNING_EMPTY = new Warning() {

        @Override
        public Warning getCurrentWaring() {
            return null;
        }
    };


    @Test
    void toMap_nominal() {
        assertTextRelative(WARNING.toMap(), "api/exceptions/warning/toMap_nominal.json");
        assertTextRelative(WARNING_EMPTY.toMap(), "api/exceptions/warning/toMap_empty.json");
    }

    @Test
    void getters_nominal() {
        assertTextRelative(LogInfoDTO.builder()
                                     .with(Warning.WARNING_CODE, WARNING.getWarningCode())
                                     .with(Warning.WARNING_MESSAGE, WARNING.getMessage())
                                     .with(Warning.WARNING_MESSAGE_DETAIL, WARNING.getMessageDetail())
                                     .with(Warning.WARNING_TYPE, WARNING.getWarningType())
                                     .with(Warning.WARNING_CATEGORY, WARNING.getCategory())
                                     .with(Warning.WARNING_DOMAIN, WARNING.getDomain())
                                     .with(Warning.WARNING_SUB_DOMAIN, WARNING.getSubDomain())
                                     .build()
                                     .toString()
                , "api/exceptions/warning/getters_nominal.txt");


        assertTextRelative(LogInfoDTO.builder()
                                     .with(Warning.WARNING_CODE, WARNING_EMPTY.getWarningCode())
                                     .with(Warning.WARNING_MESSAGE, WARNING_EMPTY.getMessage())
                                     .with(Warning.WARNING_MESSAGE_DETAIL, WARNING_EMPTY.getMessageDetail())
                                     .with(Warning.WARNING_TYPE, WARNING_EMPTY.getWarningType())
                                     .with(Warning.WARNING_CATEGORY, WARNING_EMPTY.getCategory())
                                     .with(Warning.WARNING_DOMAIN, WARNING_EMPTY.getDomain())
                                     .with(Warning.WARNING_SUB_DOMAIN, WARNING_EMPTY.getSubDomain())
                                     .build()
                                     .toString()
                , "api/exceptions/warning/getters_empty.txt");

    }

    @Test
    void keysSet_nominal() {
        assertTextRelative(WARNING.keysSet(), "api/exceptions/warning/keysSet_nominal.json");
        assertTextRelative(WARNING_EMPTY.keysSet(), "api/exceptions/warning/keysSet_nominal.json");
    }

    @Test
    void toBuilder_nominal() {
        assertTextRelative(WARNING.toBuilder().build().toMap(), "api/exceptions/warning/toMap_nominal.json");
        assertTextRelative(WARNING_EMPTY.toBuilder().build().toMap(), "api/exceptions/warning/toBuilder_empty.json");
    }
}