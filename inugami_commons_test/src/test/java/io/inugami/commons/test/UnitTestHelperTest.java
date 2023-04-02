package io.inugami.commons.test;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static io.inugami.commons.test.UnitTestHelper.assertText;
import static io.inugami.commons.test.UnitTestHelper.assertTextRelatif;
import static org.junit.jupiter.api.Assertions.*;

public class UnitTestHelperTest {


    @Test
    public void assertText_withLocalDateTime_shouldSerializeAsString() {
        LocalDateTime date = LocalDateTime.of(2023, 4, 2, 20, 27, 0);
        assertTextRelatif(date, "assertText_withLocalDateTime_shouldSerializeAsString.json");
    }
}