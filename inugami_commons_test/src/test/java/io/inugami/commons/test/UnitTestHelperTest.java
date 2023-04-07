package io.inugami.commons.test;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static io.inugami.commons.test.UnitTestHelper.assertTextRelatif;
import static io.inugami.commons.test.UnitTestHelper.assertThrowsError;

public class UnitTestHelperTest {


    @Test
    public void assertText_withLocalDateTime_shouldSerializeAsString() {
        final LocalDateTime date = LocalDateTime.of(2023, 4, 2, 20, 27, 0);
        assertTextRelatif(date, "assertText_withLocalDateTime_shouldSerializeAsString.json");
    }

    @Test
    public void assertThrowsError_withException_shouldMatch() {

        assertThrowsError(NullPointerException.class, "some error", () -> {
            throw new NullPointerException("some error");
        });
    }
}