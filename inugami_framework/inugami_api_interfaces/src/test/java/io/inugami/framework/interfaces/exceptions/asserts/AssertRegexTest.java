package io.inugami.framework.interfaces.exceptions.asserts;

import io.inugami.framework.interfaces.commons.UnitTestData;
import io.inugami.framework.interfaces.commons.UnitTestHelper;
import io.inugami.framework.interfaces.exceptions.Asserts;
import io.inugami.framework.interfaces.exceptions.DefaultErrorCode;
import io.inugami.framework.interfaces.exceptions.ErrorCode;
import org.junit.jupiter.api.Test;

import java.util.regex.Pattern;

class AssertRegexTest {
    private static final ErrorCode ERROR_CODE    = DefaultErrorCode.buildUndefineError();
    private static final String    ERROR_MESSAGE = "error";
    public static final  String    VALUE         = "value";
    public static final  String    REGEX         = ".*value.*";
    public static final  Pattern   PATTERN       = Pattern.compile(REGEX);

    @Test
    void assertRegexMatch_withError() {
        Asserts.assertRegexMatch(PATTERN, VALUE);
        Asserts.assertRegexMatch(REGEX, VALUE);
        Asserts.assertRegexMatch(ERROR_MESSAGE, REGEX, VALUE);
        Asserts.assertRegexMatch(ERROR_MESSAGE, PATTERN, VALUE);
        Asserts.assertRegexMatch(() -> ERROR_MESSAGE, REGEX, VALUE);
        Asserts.assertRegexMatch(() -> ERROR_MESSAGE, PATTERN, VALUE);
        Asserts.assertRegexMatch(ERROR_CODE, REGEX, VALUE);
        Asserts.assertRegexMatch(ERROR_CODE, PATTERN, VALUE);

        UnitTestHelper.assertThrows("current value XXX doesn't match with .*value.*", () -> Asserts.assertRegexMatch(PATTERN, UnitTestData.OTHER));
        UnitTestHelper.assertThrows("current value XXX doesn't match with .*value.*", () -> Asserts.assertRegexMatch(REGEX, UnitTestData.OTHER));
        UnitTestHelper.assertThrows(ERROR_MESSAGE, () -> Asserts.assertRegexMatch(ERROR_MESSAGE, REGEX, UnitTestData.OTHER));
        UnitTestHelper.assertThrows(ERROR_MESSAGE, () -> Asserts.assertRegexMatch(ERROR_MESSAGE, PATTERN, UnitTestData.OTHER));
        UnitTestHelper.assertThrows(ERROR_MESSAGE, () -> Asserts.assertRegexMatch(() -> ERROR_MESSAGE, REGEX, UnitTestData.OTHER));
        UnitTestHelper.assertThrows(ERROR_MESSAGE, () -> Asserts.assertRegexMatch(() -> ERROR_MESSAGE, PATTERN, UnitTestData.OTHER));
        UnitTestHelper.assertThrows(ERROR_CODE, () -> Asserts.assertRegexMatch(ERROR_CODE, REGEX, UnitTestData.OTHER));
        UnitTestHelper.assertThrows(ERROR_CODE, () -> Asserts.assertRegexMatch(ERROR_CODE, REGEX, UnitTestData.OTHER));
    }

    @Test
    void assertRegexNotMatch_withError() {
        Asserts.assertRegexNotMatch(REGEX, UnitTestData.OTHER);
        Asserts.assertRegexNotMatch(ERROR_MESSAGE, REGEX, UnitTestData.OTHER);
        Asserts.assertRegexNotMatch(() -> ERROR_MESSAGE, REGEX, UnitTestData.OTHER);
        Asserts.assertRegexNotMatch(ERROR_CODE, REGEX, UnitTestData.OTHER);

        Asserts.assertRegexNotMatch(PATTERN, UnitTestData.OTHER);
        Asserts.assertRegexNotMatch(ERROR_MESSAGE, PATTERN, UnitTestData.OTHER);
        Asserts.assertRegexNotMatch(() -> ERROR_MESSAGE, PATTERN, UnitTestData.OTHER);
        Asserts.assertRegexNotMatch(ERROR_CODE, PATTERN, UnitTestData.OTHER);

        UnitTestHelper.assertThrows("current value value should not match with .*value.*", () -> Asserts.assertRegexNotMatch(REGEX, VALUE));
        UnitTestHelper.assertThrows(ERROR_MESSAGE, () -> Asserts.assertRegexNotMatch(ERROR_MESSAGE, REGEX, VALUE));
        UnitTestHelper.assertThrows(ERROR_MESSAGE, () -> Asserts.assertRegexNotMatch(() -> ERROR_MESSAGE, REGEX, VALUE));
        UnitTestHelper.assertThrows(ERROR_CODE, () -> Asserts.assertRegexNotMatch(ERROR_CODE, REGEX, VALUE));

        UnitTestHelper.assertThrows("current value value should not match with .*value.*", () -> Asserts.assertRegexNotMatch(PATTERN, VALUE));
        UnitTestHelper.assertThrows(ERROR_MESSAGE, () -> Asserts.assertRegexNotMatch(ERROR_MESSAGE, PATTERN, VALUE));
        UnitTestHelper.assertThrows(ERROR_MESSAGE, () -> Asserts.assertRegexNotMatch(() -> ERROR_MESSAGE, PATTERN, VALUE));
        UnitTestHelper.assertThrows(ERROR_CODE, () -> Asserts.assertRegexNotMatch(ERROR_CODE, PATTERN, VALUE));
    }

    @Test
    void assertRegexFind_withError() {
        Asserts.assertRegexFind(REGEX, VALUE);
        Asserts.assertRegexFind(ERROR_MESSAGE, REGEX, VALUE);
        Asserts.assertRegexFind(() -> ERROR_MESSAGE, REGEX, VALUE);
        Asserts.assertRegexFind(ERROR_CODE, REGEX, VALUE);

        Asserts.assertRegexFind(PATTERN, VALUE);
        Asserts.assertRegexFind(ERROR_MESSAGE, PATTERN, VALUE);
        Asserts.assertRegexFind(() -> ERROR_MESSAGE, PATTERN, VALUE);
        Asserts.assertRegexFind(ERROR_CODE, PATTERN, VALUE);

        UnitTestHelper.assertThrows("current value XXX doesn't find any result with .*value.*", () -> Asserts.assertRegexFind(REGEX, UnitTestData.OTHER));
        UnitTestHelper.assertThrows(ERROR_MESSAGE, () -> Asserts.assertRegexFind(ERROR_MESSAGE, REGEX, UnitTestData.OTHER));
        UnitTestHelper.assertThrows(ERROR_MESSAGE, () -> Asserts.assertRegexFind(() -> ERROR_MESSAGE, REGEX, UnitTestData.OTHER));
        UnitTestHelper.assertThrows(ERROR_CODE, () -> Asserts.assertRegexFind(ERROR_CODE, REGEX, UnitTestData.OTHER));

        UnitTestHelper.assertThrows("current value XXX doesn't find any result with .*value.*", () -> Asserts.assertRegexFind(PATTERN, UnitTestData.OTHER));
        UnitTestHelper.assertThrows(ERROR_MESSAGE, () -> Asserts.assertRegexFind(ERROR_MESSAGE, PATTERN, UnitTestData.OTHER));
        UnitTestHelper.assertThrows(ERROR_MESSAGE, () -> Asserts.assertRegexFind(() -> ERROR_MESSAGE, PATTERN, UnitTestData.OTHER));
        UnitTestHelper.assertThrows(ERROR_CODE, () -> Asserts.assertRegexFind(ERROR_CODE, PATTERN, UnitTestData.OTHER));
    }

    @Test
    void assertRegexNotFind_withError() {
        Asserts.assertRegexNotFind(REGEX, UnitTestData.OTHER);
        Asserts.assertRegexNotFind(ERROR_MESSAGE, REGEX, UnitTestData.OTHER);
        Asserts.assertRegexNotFind(() -> ERROR_MESSAGE, REGEX, UnitTestData.OTHER);
        Asserts.assertRegexNotFind(ERROR_CODE, REGEX, UnitTestData.OTHER);

        Asserts.assertRegexNotFind(PATTERN, UnitTestData.OTHER);
        Asserts.assertRegexNotFind(ERROR_MESSAGE, PATTERN, UnitTestData.OTHER);
        Asserts.assertRegexNotFind(() -> ERROR_MESSAGE, PATTERN, UnitTestData.OTHER);
        Asserts.assertRegexNotFind(ERROR_CODE, PATTERN, UnitTestData.OTHER);

        UnitTestHelper.assertThrows("current value value should have any result with .*value.*", () -> Asserts.assertRegexNotFind(REGEX, VALUE));
        UnitTestHelper.assertThrows(ERROR_MESSAGE, () -> Asserts.assertRegexNotFind(ERROR_MESSAGE, REGEX, VALUE));
        UnitTestHelper.assertThrows(ERROR_MESSAGE, () -> Asserts.assertRegexNotFind(() -> ERROR_MESSAGE, REGEX, VALUE));
        UnitTestHelper.assertThrows(ERROR_CODE, () -> Asserts.assertRegexNotFind(ERROR_CODE, REGEX, VALUE));

        UnitTestHelper.assertThrows("current value value should have any result with .*value.*", () -> Asserts.assertRegexNotFind(PATTERN, VALUE));
        UnitTestHelper.assertThrows(ERROR_MESSAGE, () -> Asserts.assertRegexNotFind(ERROR_MESSAGE, PATTERN, VALUE));
        UnitTestHelper.assertThrows(ERROR_MESSAGE, () -> Asserts.assertRegexNotFind(() -> ERROR_MESSAGE, PATTERN, VALUE));
        UnitTestHelper.assertThrows(ERROR_CODE, () -> Asserts.assertRegexNotFind(ERROR_CODE, PATTERN, VALUE));
    }

}