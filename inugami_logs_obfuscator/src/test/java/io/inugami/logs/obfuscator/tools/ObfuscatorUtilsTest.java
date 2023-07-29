package io.inugami.logs.obfuscator.tools;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ObfuscatorUtilsTest {
    
    // =========================================================================
    // CONTAINS
    // =========================================================================
    @Test
    void contains_nominal() {
        assertThat(ObfuscatorUtils.contains("hello the world", "world")).isTrue();
        assertThat(ObfuscatorUtils.contains("hello the world", "joe")).isFalse();
    }
}