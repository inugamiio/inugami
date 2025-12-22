package io.inugami.monitoring.api.obfuscators;

import io.inugami.commons.test.UnitTestHelper;
import org.junit.jupiter.api.Test;

import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ObfuscatorToolsTest {

    @Test
    void assertUtilityClass(){
        UnitTestHelper.assertUtilityClassLombok(ObfuscatorTools.class);
    }

    @Test
    void testApplyObfuscators_WithNull() {
        assertThat(ObfuscatorTools.applyObfuscators(null)).isNull();
    }

    @Test
    void testApplyObfuscators_WithData() {
        String input = "mySecretData";
        String result = ObfuscatorTools.applyObfuscators(input);
        assertNotNull(result);
    }

    @Test
    void testBuildJsonFieldPattern_Default() {
        Pattern pattern = ObfuscatorTools.buildJsonFieldPattern("password");
        assertNotNull(pattern);
        assertTrue(pattern.matcher("\"password\":\"secret\"").find());
        assertFalse(pattern.matcher("\"user\":\"secret\"").find());
    }

    @Test
    void testBuildJsonFieldPattern_WithContent() {
        Pattern pattern = ObfuscatorTools.buildJsonFieldPattern("id", "[0-9]+");
        assertTrue(pattern.matcher("\"id\":\"12345\"").find());
        assertFalse(pattern.matcher("\"id\":\"abc\"").find());
    }

    @Test
    void testBuildJsonFieldPattern_WithoutQuotes() {
        Pattern pattern = ObfuscatorTools.buildJsonFieldPattern("active", "true", false);
        assertTrue(pattern.matcher("\"active\":true").find());
        assertFalse(pattern.matcher("\"active\":\"true\"").find());
    }

}