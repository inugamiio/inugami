package io.inugami.logs.obfuscator.obfuscators;

import io.inugami.logs.obfuscator.api.ObfuscatorSpi;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class XMLPasswordObfuscatorTest implements ObfuscatorTestUtils {

    @Test
    void accept_withPassword_shouldAccept() {
        ObfuscatorSpi obfuscator = new XMLPasswordObfuscator();
        assertThat(obfuscator.accept(buildEvent("<password>qwertzui</password>"))).isTrue();
    }

    @Test
    void obfuscate_withPassword_shouldObfuscate() {
        ObfuscatorSpi obfuscator = new XMLPasswordObfuscator();
        assertThat(obfuscator.obfuscate(buildEvent("<password type=\"basic\">qwertzui</password>"))).isEqualTo("<password type=\"basic\">xxxxx<password>");
        assertThat(obfuscator.obfuscate(buildEvent("<password>qwertzui</password>"))).isEqualTo("<password>xxxxx<password>");
    }
}