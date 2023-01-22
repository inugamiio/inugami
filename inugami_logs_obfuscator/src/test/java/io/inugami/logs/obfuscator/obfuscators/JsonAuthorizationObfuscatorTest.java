package io.inugami.logs.obfuscator.obfuscators;

import io.inugami.logs.obfuscator.api.ObfuscatorSpi;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class JsonAuthorizationObfuscatorTest implements ObfuscatorTestUtils {

    @Test
    public void accept_withPassword_shouldAccept() {
        ObfuscatorSpi obfuscator = new JsonAuthorizationObfuscator();
        assertThat(obfuscator.accept(buildEvent(" \"Authorization\":\"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c\""))).isTrue();
    }

    @Test
    public void obfuscate_withPassword_shouldObfuscate() {
        ObfuscatorSpi obfuscator = new JsonAuthorizationObfuscator();
        assertThat(obfuscator.obfuscate(buildEvent(" \"Authorization\":\"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c\"")))
                .isEqualTo(" \"Authorization\":\"xxxxxsw5c\"");
    }
}