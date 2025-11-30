package io.inugami.framework.api.tools;

import io.inugami.framework.api.tools.unit.test.UnitTestHelper;
import io.inugami.framework.interfaces.tools.StringTools;
import org.junit.jupiter.api.Test;

import java.util.regex.Pattern;

import static io.inugami.framework.interfaces.tools.StringTools.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class StringToolsTest {

    // =========================================================================
    // METHODS
    // =========================================================================
    @Test
    void assertUtilityClass() throws Exception {
        UnitTestHelper.assertUtilityClassLombok(StringTools.class);
    }

    @Test
    void testConvertToAscii() throws Exception {
        assertEquals("aAONzYSCLlssOoAEOE", StringTools.convertToAscii("àÁÒÑžÝŠÇŁłßØøÆŒ"));
    }

    @Test
    void replaceAll_nominal() throws Exception {
        assertThat(replaceAll(Pattern.compile("[a-zA-Z]+"), "abcd", "1")).isEqualTo("1");
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
