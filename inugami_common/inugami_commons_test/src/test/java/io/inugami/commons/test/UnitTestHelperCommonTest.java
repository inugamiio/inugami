package io.inugami.commons.test;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class UnitTestHelperCommonTest {

    @Test
    void cleanWindowsChar() {
        assertThat(UnitTestHelperCommon.cleanWindowsChar("\r")).isEmpty();
    }
}