package io.inugami.framework.api.exceptions.warnings;

import io.inugami.framework.api.exceptions.WarningContext;
import io.inugami.framework.api.tools.unit.test.UnitTestHelper;
import io.inugami.framework.interfaces.exceptions.DefaultWarning;
import io.inugami.framework.interfaces.exceptions.Warning;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static io.inugami.framework.api.exceptions.warnings.WarningEmpty.warningEmpty;
import static io.inugami.framework.api.exceptions.warnings.WarningEmpty.warningNotEmpty;
import static io.inugami.framework.api.tools.unit.test.UnitTestHelper.assertText;


class WarningEmptyTest {
    //==================================================================================================================
    // ATTRIBUTES
    //==================================================================================================================
    public static final String NOMINAL_LIST = """
            [ {
              "warningCode" : "ENGINE-0_0",
              "warningMessage" : "workspace not defined",
              "warningMessageDetail" : "workspace required",
              "warningType" : "security",
              "warningCategory" : "category",
              "warningDomain" : "SECU",
              "warningSubDomain" : "user"
            }, {
              "warningCode" : "ENGINE-0_0",
              "warningMessage" : "workspace not defined",
              "warningMessageDetail" : "workspace required",
              "warningType" : "security",
              "warningCategory" : "category",
              "warningDomain" : "SECU",
              "warningSubDomain" : "user"
            } ]
            """;
    public static final String NOMINAL      = """
            [ {
              "warningCode" : "ENGINE-0_0",
              "warningMessage" : "workspace not defined",
              "warningMessageDetail" : "workspace required",
              "warningType" : "security",
              "warningCategory" : "category",
              "warningDomain" : "SECU",
              "warningSubDomain" : "user"
            } ]
            """;

    //==================================================================================================================
    // INIT
    //==================================================================================================================
    @BeforeEach
    public void init() {
        WarningContext.getInstance().clear();
    }

    @Test
    void utilityClas() {
        UnitTestHelper.assertUtilityClassLombok(WarningEmpty.class);
    }

    //==================================================================================================================
    // STRING
    //==================================================================================================================
    @Test
    void warningEmpty_string_nominal() {
        final String nullValue = null;
        warningEmpty(EngineWarning.WORKSPACE_UNDEFINED, nullValue, "workspace required", "15");
        warningEmpty(EngineWarning.WORKSPACE_UNDEFINED, "", "workspace required", "15");
        assertText(WarningContext.getInstance().getWarnings(),
                   NOMINAL_LIST);
    }

    @Test
    void warningNotEmpty_string_nominal() {
        warningNotEmpty(EngineWarning.WORKSPACE_UNDEFINED, "some value", "workspace required", "15");
        assertText(WarningContext.getInstance().getWarnings(),
                   NOMINAL);
    }

    //==================================================================================================================
    // COLLECTION
    //==================================================================================================================
    @Test
    void warningEmpty_collection_nominal() {
        final List<String> nullValue = null;
        warningEmpty(EngineWarning.WORKSPACE_UNDEFINED, nullValue, "workspace required", "15");
        warningEmpty(EngineWarning.WORKSPACE_UNDEFINED, List.of(), "workspace required", "15");
        assertText(WarningContext.getInstance().getWarnings(),
                   NOMINAL_LIST);
    }

    @Test
    void warningNotEmpty_collection_nominal() {
        warningNotEmpty(EngineWarning.WORKSPACE_UNDEFINED, List.of("some value"), "workspace required", "15");
        assertText(WarningContext.getInstance().getWarnings(),
                   NOMINAL);
    }


    //==================================================================================================================
    // ARRAYS
    //==================================================================================================================
    @Test
    void warningEmpty_array_nominal() {
        final String[] nullValue = null;
        warningEmpty(EngineWarning.WORKSPACE_UNDEFINED, nullValue, "workspace required", "15");
        warningEmpty(EngineWarning.WORKSPACE_UNDEFINED, new String[]{}, "workspace required", "15");
        assertText(WarningContext.getInstance().getWarnings(),
                   NOMINAL_LIST);
    }

    @Test
    void warningNotEmpty_array_nominal() {
        warningNotEmpty(EngineWarning.WORKSPACE_UNDEFINED, new String[]{"some value"}, "workspace required", "15");
        assertText(WarningContext.getInstance().getWarnings(),
                   NOMINAL);
    }

    //==================================================================================================================
    // MAP
    //==================================================================================================================
    @Test
    void warningEmpty_map_nominal() {
        final Map<String, String> nullValue = null;
        warningEmpty(EngineWarning.WORKSPACE_UNDEFINED, nullValue, "workspace required", "15");
        warningEmpty(EngineWarning.WORKSPACE_UNDEFINED, Map.of(), "workspace required", "15");
        assertText(WarningContext.getInstance().getWarnings(),
                   NOMINAL_LIST);
    }

    @Test
    void warningNotEmpty_map_nominal() {
        final Map<String, String> value = Map.of("key", "value");
        warningNotEmpty(EngineWarning.WORKSPACE_UNDEFINED, value, "workspace required", "15");
        assertText(WarningContext.getInstance().getWarnings(),
                   NOMINAL);
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