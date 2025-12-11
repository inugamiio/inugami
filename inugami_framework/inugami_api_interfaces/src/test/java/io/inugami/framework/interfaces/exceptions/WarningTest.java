package io.inugami.framework.interfaces.exceptions;

import org.junit.jupiter.api.Test;

import static io.inugami.framework.interfaces.testing.commons.UnitTestHelper.assertText;

class WarningTest {

    @Test
    void KEYS_SET_nomninal() {
        assertText(Warning.KEYS_SET,
                   """
                           [ "warningCode", "warningMessage", "warningMessageDetail", "warningType", "warningCategory", "warningDomain", "warningSubDomain" ]
                           """);
        assertText(EngineWarning.WORKSPACE_UNDEFINED.keysSet(),
                   """
                           [ "warningCode", "warningMessage", "warningMessageDetail", "warningType", "warningCategory", "warningDomain", "warningSubDomain" ]
                           """);
    }

    @Test
    void toMap_nominal() {
        assertText(EngineWarning.WORKSPACE_UNDEFINED.toMap(),
                   """
                           {
                             "warningCode" : "ENGINE-0_0",
                             "warningMessage" : "workspace not defined",
                             "warningMessageDetail" : "please check your configuration",
                             "warningType" : "security",
                             "warningCategory" : "category",
                             "warningDomain" : "SECU",
                             "warningSubDomain" : "user"
                           }
                           """);
    }

    @Test
    void getSubDomain_nominal() {
        assertText(EngineWarning.WORKSPACE_UNDEFINED.getSubDomain(),
                   """
                           user
                           """);
    }

    @Test
    void getDomain_nominal() {
        assertText(EngineWarning.WORKSPACE_UNDEFINED.getDomain(),
                   """
                           SECU
                           """);
    }

    @Test
    void getCategory_nominal() {
        assertText(EngineWarning.WORKSPACE_UNDEFINED.getCategory(),
                   """
                           category
                           """);
    }

    @Test
    void addDetail_nominal() {
        assertText(EngineWarning.WORKSPACE_UNDEFINED.addDetail("value", "15").toMap(),
                   """
                           {
                             "warningCode" : "ENGINE-0_0",
                             "warningMessage" : "workspace not defined",
                             "warningMessageDetail" : "value",
                             "warningType" : "security",
                             "warningCategory" : "category",
                             "warningDomain" : "SECU",
                             "warningSubDomain" : "user"
                           }
                           """);
    }

    @Test
    void getWarningType_nominal() {
        assertText(EngineWarning.WORKSPACE_UNDEFINED.getWarningType(),
                   """
                           security
                           """);
    }

    @Test
    void getMessageDetail_nominal() {
        assertText(EngineWarning.WORKSPACE_UNDEFINED.getMessageDetail(),
                   """
                           please check your configuration
                           """);
    }

    @Test
    void getMessage_nominal() {
        assertText(EngineWarning.WORKSPACE_UNDEFINED.getMessage(),
                   """
                           workspace not defined
                           """);
    }

    @Test
    void getWarningCode_nominal() {
        assertText(EngineWarning.WORKSPACE_UNDEFINED.getWarningCode(),
                   """
                           ENGINE-0_0
                           """);
    }

    //==================================================================================================================
    // DATA
    //==================================================================================================================
    private enum EngineWarning implements Warning {
        WORKSPACE_UNDEFINED(DefaultWarning.builder().warningCode("ENGINE-0_0")
                                          .message("workspace not defined")
                                          .messageDetail("please check your configuration")
                                          .domain("SECU")
                                          .subDomain("user")
                                          .category("category")
                                          .warningType("security"));

        private final Warning warning;

        EngineWarning(final DefaultWarning.DefaultWarningBuilder builder) {
            warning = builder.build();
        }

        @Override
        public Warning getCurrentWaring() {
            return warning;
        }
    }
}