package io.inugami.logs.obfuscator.tools;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ObfuscatorUtilsTest {

    public static final String HELLO_THE_WORLD = "hello the world";
    public static final String JOE = "joe";

    // =========================================================================
    // CONTAINS
    // =========================================================================
    @Test
    void contains_nominal() {
        assertThat(ObfuscatorUtils.contains(HELLO_THE_WORLD, "world")).isTrue();
        assertThat(ObfuscatorUtils.contains(HELLO_THE_WORLD, JOE)).isFalse();
    }



    // =========================================================================
    // keepLastChars / keepFirstChars
    // =========================================================================
    @Test
    void keepLastChars_nominal(){
        assertThat(ObfuscatorUtils.keepLastChars(HELLO_THE_WORLD, 3)).isEqualTo("xxxxxorld");
        assertThat(ObfuscatorUtils.keepLastChars(JOE, 3)).isEqualTo("xxxxx");
        assertThat(ObfuscatorUtils.keepLastChars(null, 3)).isEqualTo("xxxxx");
    }

    @Test
    void keepFirstChars_nominal(){
        assertThat(ObfuscatorUtils.keepFirstChars(HELLO_THE_WORLD, 3)).isEqualTo("helxxxxx");
        assertThat(ObfuscatorUtils.keepFirstChars(JOE, 3)).isEqualTo("xxxxx");
        assertThat(ObfuscatorUtils.keepFirstChars(null, 3)).isEqualTo("xxxxx");
    }
}