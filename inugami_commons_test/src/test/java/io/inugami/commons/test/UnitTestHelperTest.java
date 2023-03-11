package io.inugami.commons.test;

import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;


public class UnitTestHelperTest {

    @Test
    public void assertTextRelative_withSkipLine_shouldPass() {
        Map<String, String> values = new LinkedHashMap<>();
        values.put("title", "hello");
        values.put("uid", UUID.randomUUID().toString());
        values.put("name", "joe");

        UnitTestHelper.assertTextRelative(values, "assertTextRelative_withSkipLine_shouldPass.json", DefaultLineAssertion.skipLine(2));
        UnitTestHelper.assertTextRelativeSkipLine(values, "assertTextRelative_withSkipLine_shouldPass.json",2);

    }
}