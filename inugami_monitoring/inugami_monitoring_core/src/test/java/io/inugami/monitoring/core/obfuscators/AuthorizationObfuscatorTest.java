package io.inugami.monitoring.core.obfuscators;

import org.junit.jupiter.api.Test;

import static io.inugami.commons.test.UnitTestHelper.assertText;
import static org.assertj.core.api.Assertions.assertThat;

class AuthorizationObfuscatorTest {

    // =================================================================================================================
    // TEST
    // =================================================================================================================
    @Test
    void accept_nominal() {
        final var obfuscator = buildObfuscator();
        assertThat(obfuscator.accept(null)).isFalse();
        assertThat(obfuscator.accept("")).isFalse();

        assertThat(obfuscator.accept("AUTHORIZATION")).isTrue();
        assertThat(obfuscator.accept("authorization")).isTrue();
    }


    @Test
    void clean_nominal() {
        final var obfuscator = buildObfuscator();
        assertText(obfuscator.clean("""
                                            "authorization": "Basic 123456789"   
                                            """),
                   """
                           "authorization":"xxxxx"
                           """);
    }

    // =================================================================================================================
    // TOOLS
    // =================================================================================================================
    AuthorizationObfuscator buildObfuscator() {
        return new AuthorizationObfuscator();
    }
}