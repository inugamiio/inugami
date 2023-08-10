package io.inugami.logs.obfuscator.obfuscators;

import io.inugami.logs.obfuscator.api.ObfuscatorSpi;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class BasicAuthorizationObfuscatorTest implements ObfuscatorTestUtils {

    @Test
    void accept_withPassword_shouldAccept() {
        ObfuscatorSpi obfuscator = new BasicAuthorizationObfuscator();
        assertThat(obfuscator.accept(buildEvent("Authorization=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0N"))).isTrue();
    }

    @Test
    void obfuscate_withPassword_shouldObfuscate() {
        ObfuscatorSpi obfuscator = new BasicAuthorizationObfuscator();
        assertThat(obfuscator.obfuscate(buildEvent("Authorization=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0N"))).isEqualTo("Authorization=xxxxxjM0N");
        assertThat(obfuscator.obfuscate(buildEvent("Authorization = eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0N"))).isEqualTo("Authorization = xxxxxjM0N");
        assertThat(obfuscator.obfuscate(buildEvent("Authorization : eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0N"))).isEqualTo("Authorization : xxxxxjM0N");
    }
}