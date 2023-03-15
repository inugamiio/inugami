package io.inugami.api.exceptions;

import org.junit.jupiter.api.Test;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class DefaultWarningTest {


    @Test
    public void addDetail_nominal_shouldAddDetail(){
        assertThat(WarningErrorCode.REQUEST_REQUIRE.addDetail("simple message {0}" , "hello").toString())
                .isEqualTo("DefaultWarning(warningCode=HTTP-1, message=request is require, messageDetail=simple message hello, warningType=functional, category=null)");
    }


    private static enum WarningErrorCode implements  Warning{
        REQUEST_REQUIRE(DefaultWarning.builder()
                                .warningCode("HTTP-1")
                                .warningType("functional")
                                .message("request is require")
        );

        private final Warning warning;

        private WarningErrorCode(DefaultWarning.DefaultWarningBuilder builder) {
            warning = builder.build();
        }

        @Override
        public Warning getCurrentWaring() {
            return warning;
        }
    }
}