package io.inugami.logs.obfuscator.obfuscators;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class AccessControlAllowHeaderObfuscatorTest implements ObfuscatorTestUtils {
    private static final String VALUE = "Access-Control-Allow-Headers: x-device-identifier, x-correlation-id, x-b3-traceid";

    @Test
    void accept_nominal() {
        AccessControlAllowHeaderObfuscator obfuscator = new AccessControlAllowHeaderObfuscator();
        assertThat(obfuscator.accept(buildEvent(VALUE))).isTrue();
    }

    @Test
    void obfuscate_nominal() {
        AccessControlAllowHeaderObfuscator obfuscator = new AccessControlAllowHeaderObfuscator();
        assertThat(obfuscator.obfuscate(buildEvent(VALUE))).isEqualTo("Access-Control-Allow-Headers: xxxxx");
        assertThat(obfuscator.obfuscate(buildEvent("Access-Control-Allow-Headers= x-device-identifier, x-correlation-id, x-b3-traceid"))).isEqualTo("Access-Control-Allow-Headers= xxxxx");
        assertThat(obfuscator.obfuscate(buildEvent("Access-Control-Allow-Headers : x-device-identifier, x-correlation-id, x-b3-traceid"))).isEqualTo("Access-Control-Allow-Headers : xxxxx");
    }
}