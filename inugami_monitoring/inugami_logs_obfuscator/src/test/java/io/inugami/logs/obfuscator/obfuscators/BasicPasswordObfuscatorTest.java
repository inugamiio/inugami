package io.inugami.logs.obfuscator.obfuscators;

import io.inugami.logs.obfuscator.api.ObfuscatorSpi;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class BasicPasswordObfuscatorTest implements ObfuscatorTestUtils {

    @Test
    void accept_withPassword_shouldAccept() {
        ObfuscatorSpi obfuscator = new BasicPasswordObfuscator();
        assertThat(obfuscator.accept(buildEvent("password=hello"))).isTrue();
    }

    @Test
    void obfuscate_withPassword_shouldObfuscate() {
        ObfuscatorSpi obfuscator = new BasicPasswordObfuscator();
        assertThat(obfuscator.obfuscate(buildEvent("password:hello"))).isEqualTo("password:xxxxx");
        assertThat(obfuscator.obfuscate(buildEvent("password : hello"))).isEqualTo("password : xxxxx");
        assertThat(obfuscator.obfuscate(buildEvent("password=hello"))).isEqualTo("password=xxxxx");
        assertThat(obfuscator.obfuscate(buildEvent("password = hello"))).isEqualTo("password = xxxxx");
    }
}