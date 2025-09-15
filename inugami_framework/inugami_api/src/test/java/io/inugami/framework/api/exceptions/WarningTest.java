package io.inugami.framework.api.exceptions;

import io.inugami.framework.api.tools.unit.test.UnitTestData;
import io.inugami.framework.api.tools.unit.test.UnitTestHelper;
import io.inugami.framework.interfaces.exceptions.DefaultWarning;
import io.inugami.framework.interfaces.exceptions.Warning;
import io.inugami.framework.interfaces.monitoring.logger.LogInfoDTO;
import org.junit.jupiter.api.Test;

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
        UnitTestHelper.assertTextRelative(WARNING.toMap(), "io/inugami/framework/api/warning/toMap_nominal.json");
        UnitTestHelper.assertTextRelative(WARNING_EMPTY.toMap(), "io/inugami/framework/api/warning/toMap_empty.json");
    }

    @Test
    void getters_nominal() {
        UnitTestHelper.assertTextRelative(LogInfoDTO.builder()
                                                    .with(Warning.WARNING_CODE, WARNING.getWarningCode())
                                                    .with(Warning.WARNING_MESSAGE, WARNING.getMessage())
                                                    .with(Warning.WARNING_MESSAGE_DETAIL, WARNING.getMessageDetail())
                                                    .with(Warning.WARNING_TYPE, WARNING.getWarningType())
                                                    .with(Warning.WARNING_CATEGORY, WARNING.getCategory())
                                                    .with(Warning.WARNING_DOMAIN, WARNING.getDomain())
                                                    .with(Warning.WARNING_SUB_DOMAIN, WARNING.getSubDomain())
                                                    .build()
                                                    .toString()
                , "io/inugami/framework/api/warning/getters_nominal.txt");


        UnitTestHelper.assertTextRelative(LogInfoDTO.builder()
                                                    .with(Warning.WARNING_CODE, WARNING_EMPTY.getWarningCode())
                                                    .with(Warning.WARNING_MESSAGE, WARNING_EMPTY.getMessage())
                                                    .with(Warning.WARNING_MESSAGE_DETAIL, WARNING_EMPTY.getMessageDetail())
                                                    .with(Warning.WARNING_TYPE, WARNING_EMPTY.getWarningType())
                                                    .with(Warning.WARNING_CATEGORY, WARNING_EMPTY.getCategory())
                                                    .with(Warning.WARNING_DOMAIN, WARNING_EMPTY.getDomain())
                                                    .with(Warning.WARNING_SUB_DOMAIN, WARNING_EMPTY.getSubDomain())
                                                    .build()
                                                    .toString()
                , "io/inugami/framework/api/warning/getters_empty.txt");

    }

    @Test
    void keysSet_nominal() {
        UnitTestHelper.assertTextRelative(WARNING.keysSet(), "io/inugami/framework/api/warning/keysSet_nominal.json");
        UnitTestHelper.assertTextRelative(WARNING_EMPTY.keysSet(), "io/inugami/framework/api/warning/keysSet_nominal.json");
    }

    @Test
    void toBuilder_nominal() {
        UnitTestHelper.assertTextRelative(WARNING.toBuilder()
                                                 .build()
                                                 .toMap(), "io/inugami/framework/api/warning/toMap_nominal.json");
        UnitTestHelper.assertTextRelative(WARNING_EMPTY.toBuilder()
                                                       .build()
                                                       .toMap(), "io/inugami/framework/api/warning/toBuilder_empty.json");
    }
}