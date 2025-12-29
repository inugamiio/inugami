package io.inugami.monitoring.core.obfuscators;

import org.junit.jupiter.api.Test;

import static io.inugami.commons.test.UnitTestHelper.assertText;
import static org.assertj.core.api.Assertions.assertThat;

class PasswordObfuscatorTest {

    // =================================================================================================================
    // TEST
    // =================================================================================================================
    @Test
    void accept_nominal() {
        final var obfuscator = buildObfuscator();
        assertThat(obfuscator.accept(null)).isFalse();
        assertThat(obfuscator.accept("")).isFalse();

        assertThat(obfuscator.accept("password")).isTrue();
        assertThat(obfuscator.accept("PASSWORD")).isTrue();
    }


    @Test
    void clean_nominal() {
        final var obfuscator = buildObfuscator();
        assertText(obfuscator.clean("""
                                            "password": "123456789"   
                                            """),
                   """
                           "password":"xxxxx"
                           """);
    }

    // =================================================================================================================
    // TOOLS
    // =================================================================================================================
    PasswordObfuscator buildObfuscator() {
        return new PasswordObfuscator();
    }
}