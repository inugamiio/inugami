package io.inugami.commons.test;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class TestHelpersErrorCodesTest {


    @Test
    public void getCurrentErrorCode_nominal() {
        assertThat(TestHelpersErrorCodes.MUST_BE_EQUALS.getCurrentErrorCode().getMessage()).isEqualTo("values must be equals");
        assertThat(TestHelpersErrorCodes.MUST_BE_NOT_NULL.getCurrentErrorCode().getMessage()).isEqualTo("value mustn't be null");
        assertThat(TestHelpersErrorCodes.MUST_BE_EQUALS.getCurrentErrorCode().getMessage()).isEqualTo("values must be equals");
    }
}