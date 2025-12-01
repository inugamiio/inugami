package io.inugami.framework.interfaces.tools;

import io.inugami.framework.interfaces.testing.commons.UnitTestHelper;
import org.junit.jupiter.api.Test;

import java.util.regex.Pattern;

import static io.inugami.framework.interfaces.tools.StringTools.*;
import static org.assertj.core.api.Assertions.assertThat;

class StringToolsTest {
    // =========================================================================
    // METHODS
    // =========================================================================
    @Test
    void initializeStrategies_nominal() {
        assertThat(StringTools.initializeStrategies()).isNotEmpty();
    }

    @Test
    void assertUtilityClass() throws Exception {
        UnitTestHelper.assertUtilityClassLombok(StringTools.class);
    }

    @Test
    void testConvertToAscii() throws Exception {
        assertThat(StringTools.convertToAscii("àÁÒÑžÝŠÇŁłßØøÆŒ")).isEqualTo("aAONzYSCLlssOoAEOE");
        assertThat(StringTools.convertToAscii(null)).isNull();
    }

    @Test
    void replaceAll_nominal() throws Exception {
        assertThat(replaceAll(Pattern.compile("[a-zA-Z]+"), "abcd", "1")).isEqualTo("1");

        assertThat(replaceAll(Pattern.compile("[0-9]+"), "abcd", "X")).isEqualTo("abcd");
    }

    @Test
    void containsChars_nominal() throws Exception {
        assertThat(containsChars("abcd", "a")).isTrue();
        assertThat(containsChars("abcd", "z")).isFalse();
        assertThat(containsChars(null, "z")).isFalse();
    }

    @Test
    void format_nominal() throws Exception {
        assertThat(format("abcd {0}", "a")).isEqualTo("abcd a");
    }
}