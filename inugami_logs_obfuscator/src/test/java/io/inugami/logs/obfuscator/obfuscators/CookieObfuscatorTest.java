package io.inugami.logs.obfuscator.obfuscators;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class CookieObfuscatorTest implements ObfuscatorTestUtils {
    private static final String VALUE = "Cookie: x-device-identifier, x-correlation-id, x-b3-traceid";

    @Test
    void accept_nominal() {
        CookieObfuscator obfuscator = new CookieObfuscator();
        assertThat(obfuscator.accept(buildEvent(VALUE))).isTrue();
    }

    @Test
    void obfuscate_nominal() {
        CookieObfuscator obfuscator = new CookieObfuscator();
        assertThat(obfuscator.obfuscate(buildEvent(VALUE))).isEqualTo("Cookie: xxxxx");
        assertThat(obfuscator.obfuscate(buildEvent("Cookie= x-device-identifier, x-correlation-id, x-b3-traceid"))).isEqualTo("Cookie= xxxxx");
        assertThat(obfuscator.obfuscate(buildEvent("Cookie : x-device-identifier, x-correlation-id, x-b3-traceid"))).isEqualTo("Cookie : xxxxx");
    }
}