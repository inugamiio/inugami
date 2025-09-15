package io.inugami.logs.obfuscator.obfuscators;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class AccessControlExposedHeaderObfuscatorTest implements ObfuscatorTestUtils {
    private static final String VALUE = "Access-Control-Expose-Headers: x-device-identifier, x-correlation-id, x-b3-traceid";

    @Test
    void accept_nominal() {
        AccessControlExposedHeaderObfuscator obfuscator = new AccessControlExposedHeaderObfuscator();
        assertThat(obfuscator.accept(buildEvent(VALUE))).isTrue();
    }

    @Test
    void obfuscate_nominal() {
        AccessControlExposedHeaderObfuscator obfuscator = new AccessControlExposedHeaderObfuscator();
        assertThat(obfuscator.obfuscate(buildEvent(VALUE))).isEqualTo("Access-Control-Expose-Headers: xxxxx");
        assertThat(obfuscator.obfuscate(buildEvent("Access-Control-Expose-Headers= x-device-identifier, x-correlation-id, x-b3-traceid"))).isEqualTo("Access-Control-Expose-Headers= xxxxx");
        assertThat(obfuscator.obfuscate(buildEvent("Access-Control-Expose-Headers : x-device-identifier, x-correlation-id, x-b3-traceid"))).isEqualTo("Access-Control-Expose-Headers : xxxxx");
    }
}