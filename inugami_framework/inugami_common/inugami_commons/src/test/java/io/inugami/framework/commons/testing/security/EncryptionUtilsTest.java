package io.inugami.framework.commons.testing.security;

import io.inugami.framework.commons.security.EncryptionUtils;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EncryptionUtilsTest {
    // =========================================================================
    // TEST BASE 64
    // =========================================================================
    @Test
    void testBase64() {
        final EncryptionUtils utils = new EncryptionUtils();
        final String          token = utils.encodeBase64("foobar");
        assertThat(token).isNotNull();
        final String newValue = utils.decodeBase64(token);
        assertThat(newValue).isEqualTo("foobar");
    }

    // =========================================================================
    // TEST AES
    // =========================================================================
    @Test
    void testAES() throws Exception {
        final EncryptionUtils utils = new EncryptionUtils();

        final String password = "password";
        final String hash     = utils.encodeAES("password");
        assertThat(hash).isNotNull()
                        .isEqualTo("NU9VtiyCzXHMqEudCTioZA==");

        final String newPassword = utils.decodeAES(hash);
        assertThat(password).isEqualTo(newPassword);

    }

    // =========================================================================
    // TEST SHA1
    // =========================================================================
    @Test
    void testEncodeSha1() {
        final EncryptionUtils utils = new EncryptionUtils();
        final String          token = utils.encodeSha1("password");
        assertThat(token).isNotNull();
        assertThat(token).isNotEqualTo("password");
        assertThat(token).doesNotContain("=");
    }

}
