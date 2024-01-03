package io.inugami.api.exceptions.asserts;

import io.inugami.interfaces.exceptions.DefaultErrorCode;
import io.inugami.interfaces.exceptions.ErrorCode;
import org.junit.jupiter.api.Test;

import java.util.regex.Pattern;

import static io.inugami.api.exceptions.Asserts.*;
import static io.inugami.api.tools.unit.test.UnitTestData.OTHER;
import static io.inugami.api.tools.unit.test.UnitTestHelper.assertThrows;

class AssertRegexTest {
    private static final ErrorCode ERROR_CODE    = DefaultErrorCode.buildUndefineError();
    private static final String    ERROR_MESSAGE = "error";
    public static final  String    VALUE         = "value";
    public static final  String    REGEX         = ".*value.*";
    public static final  Pattern   PATTERN       = Pattern.compile(REGEX);

    @Test
    void assertRegexMatch_withError() {
        assertRegexMatch(PATTERN, VALUE);
        assertRegexMatch(REGEX, VALUE);
        assertRegexMatch(ERROR_MESSAGE, REGEX, VALUE);
        assertRegexMatch(ERROR_MESSAGE, PATTERN, VALUE);
        assertRegexMatch(() -> ERROR_MESSAGE, REGEX, VALUE);
        assertRegexMatch(() -> ERROR_MESSAGE, PATTERN, VALUE);
        assertRegexMatch(ERROR_CODE, REGEX, VALUE);
        assertRegexMatch(ERROR_CODE, PATTERN, VALUE);

        assertThrows("current value XXX doesn't match with .*value.*", () -> assertRegexMatch(PATTERN, OTHER));
        assertThrows("current value XXX doesn't match with .*value.*", () -> assertRegexMatch(REGEX, OTHER));
        assertThrows(ERROR_MESSAGE, () -> assertRegexMatch(ERROR_MESSAGE, REGEX, OTHER));
        assertThrows(ERROR_MESSAGE, () -> assertRegexMatch(ERROR_MESSAGE, PATTERN, OTHER));
        assertThrows(ERROR_MESSAGE, () -> assertRegexMatch(() -> ERROR_MESSAGE, REGEX, OTHER));
        assertThrows(ERROR_MESSAGE, () -> assertRegexMatch(() -> ERROR_MESSAGE, PATTERN, OTHER));
        assertThrows(ERROR_CODE, () -> assertRegexMatch(ERROR_CODE, REGEX, OTHER));
        assertThrows(ERROR_CODE, () -> assertRegexMatch(ERROR_CODE, REGEX, OTHER));
    }

    @Test
    void assertRegexNotMatch_withError() {
        assertRegexNotMatch(REGEX, OTHER);
        assertRegexNotMatch(ERROR_MESSAGE, REGEX, OTHER);
        assertRegexNotMatch(() -> ERROR_MESSAGE, REGEX, OTHER);
        assertRegexNotMatch(ERROR_CODE, REGEX, OTHER);

        assertRegexNotMatch(PATTERN, OTHER);
        assertRegexNotMatch(ERROR_MESSAGE, PATTERN, OTHER);
        assertRegexNotMatch(() -> ERROR_MESSAGE, PATTERN, OTHER);
        assertRegexNotMatch(ERROR_CODE, PATTERN, OTHER);

        assertThrows("current value value should not match with .*value.*", () -> assertRegexNotMatch(REGEX, VALUE));
        assertThrows(ERROR_MESSAGE, () -> assertRegexNotMatch(ERROR_MESSAGE, REGEX, VALUE));
        assertThrows(ERROR_MESSAGE, () -> assertRegexNotMatch(() -> ERROR_MESSAGE, REGEX, VALUE));
        assertThrows(ERROR_CODE, () -> assertRegexNotMatch(ERROR_CODE, REGEX, VALUE));

        assertThrows("current value value should not match with .*value.*", () -> assertRegexNotMatch(PATTERN, VALUE));
        assertThrows(ERROR_MESSAGE, () -> assertRegexNotMatch(ERROR_MESSAGE, PATTERN, VALUE));
        assertThrows(ERROR_MESSAGE, () -> assertRegexNotMatch(() -> ERROR_MESSAGE, PATTERN, VALUE));
        assertThrows(ERROR_CODE, () -> assertRegexNotMatch(ERROR_CODE, PATTERN, VALUE));
    }

    @Test
    void assertRegexFind_withError() {
        assertRegexFind(REGEX, VALUE);
        assertRegexFind(ERROR_MESSAGE, REGEX, VALUE);
        assertRegexFind(() -> ERROR_MESSAGE, REGEX, VALUE);
        assertRegexFind(ERROR_CODE, REGEX, VALUE);

        assertRegexFind(PATTERN, VALUE);
        assertRegexFind(ERROR_MESSAGE, PATTERN, VALUE);
        assertRegexFind(() -> ERROR_MESSAGE, PATTERN, VALUE);
        assertRegexFind(ERROR_CODE, PATTERN, VALUE);

        assertThrows("current value XXX doesn't find any result with .*value.*", () -> assertRegexFind(REGEX, OTHER));
        assertThrows(ERROR_MESSAGE, () -> assertRegexFind(ERROR_MESSAGE, REGEX, OTHER));
        assertThrows(ERROR_MESSAGE, () -> assertRegexFind(() -> ERROR_MESSAGE, REGEX, OTHER));
        assertThrows(ERROR_CODE, () -> assertRegexFind(ERROR_CODE, REGEX, OTHER));

        assertThrows("current value XXX doesn't find any result with .*value.*", () -> assertRegexFind(PATTERN, OTHER));
        assertThrows(ERROR_MESSAGE, () -> assertRegexFind(ERROR_MESSAGE, PATTERN, OTHER));
        assertThrows(ERROR_MESSAGE, () -> assertRegexFind(() -> ERROR_MESSAGE, PATTERN, OTHER));
        assertThrows(ERROR_CODE, () -> assertRegexFind(ERROR_CODE, PATTERN, OTHER));
    }

    @Test
    void assertRegexNotFind_withError() {
        assertRegexNotFind(REGEX, OTHER);
        assertRegexNotFind(ERROR_MESSAGE, REGEX, OTHER);
        assertRegexNotFind(() -> ERROR_MESSAGE, REGEX, OTHER);
        assertRegexNotFind(ERROR_CODE, REGEX, OTHER);

        assertRegexNotFind(PATTERN, OTHER);
        assertRegexNotFind(ERROR_MESSAGE, PATTERN, OTHER);
        assertRegexNotFind(() -> ERROR_MESSAGE, PATTERN, OTHER);
        assertRegexNotFind(ERROR_CODE, PATTERN, OTHER);

        assertThrows("current value value should have any result with .*value.*", () -> assertRegexNotFind(REGEX, VALUE));
        assertThrows(ERROR_MESSAGE, () -> assertRegexNotFind(ERROR_MESSAGE, REGEX, VALUE));
        assertThrows(ERROR_MESSAGE, () -> assertRegexNotFind(() -> ERROR_MESSAGE, REGEX, VALUE));
        assertThrows(ERROR_CODE, () -> assertRegexNotFind(ERROR_CODE, REGEX, VALUE));

        assertThrows("current value value should have any result with .*value.*", () -> assertRegexNotFind(PATTERN, VALUE));
        assertThrows(ERROR_MESSAGE, () -> assertRegexNotFind(ERROR_MESSAGE, PATTERN, VALUE));
        assertThrows(ERROR_MESSAGE, () -> assertRegexNotFind(() -> ERROR_MESSAGE, PATTERN, VALUE));
        assertThrows(ERROR_CODE, () -> assertRegexNotFind(ERROR_CODE, PATTERN, VALUE));
    }

}