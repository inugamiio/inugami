package io.inugami.monitoring.springboot.partnerlog.feign;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

class FeignBodyTest {

    public static final String INUGAMI = "Inugami";

    @Test
    void should_manage_body_correctly() throws IOException {
        byte[] content = INUGAMI.getBytes(StandardCharsets.UTF_8);
        FeignBody feignBody = FeignBody.builder()
                                       .body(content)
                                       .build();


        assertThat(feignBody.length()).isEqualTo(content.length);
        assertThat(feignBody.isRepeatable()).isTrue();

        try (InputStream is = feignBody.asInputStream()) {
            assertThat(is).isNotNull();
            assertThat(new String(is.readAllBytes(), StandardCharsets.UTF_8)).isEqualTo(INUGAMI);
        }


        try (Reader reader = feignBody.asReader(StandardCharsets.UTF_8)) {
            assertThat(reader).isNotNull();
            char[] charBuffer = new char[content.length];
            reader.read(charBuffer);
            assertThat(new String(charBuffer)).isEqualTo(INUGAMI);
        }

        assertThatCode(feignBody::close).doesNotThrowAnyException();
    }

    @Test
    void should_support_to_builder() {
        FeignBody original = new FeignBody("Initial".getBytes());


        FeignBody copy = original.toBuilder()
                                 .body("Updated".getBytes())
                                 .build();

        assertThat(new String(copy.getBody())).isEqualTo("Updated");
        assertThat(new String(original.getBody())).isEqualTo("Initial");
    }
}