package io.inugami.logs.obfuscator.obfuscators;

import io.inugami.logs.obfuscator.api.ObfuscatorSpi;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class JsonPasswordObfuscatorTest implements ObfuscatorTestUtils {

    @Test
    void accept_withPassword_shouldAccept() {
        ObfuscatorSpi obfuscator = new JsonPasswordObfuscator();
        assertThat(obfuscator.accept(buildEvent(" \"password\":\"qwertz\""))).isTrue();
    }

    @Test
    void obfuscate_withPassword_shouldObfuscate() {
        ObfuscatorSpi obfuscator = new JsonPasswordObfuscator();
        assertThat(obfuscator.obfuscate(buildEvent(" \"password\":\"qwertz\""))).isEqualTo(" \"password\":\"xxxxx\"");
    }
}