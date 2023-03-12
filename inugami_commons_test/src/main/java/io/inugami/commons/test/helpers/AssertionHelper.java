package io.inugami.commons.test.helpers;

import io.inugami.api.exceptions.Asserts;
import io.inugami.api.loggers.Loggers;
import io.inugami.commons.test.LineAssertion;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AssertionHelper {
    public static void assertJson(final Object value, final String jsonRef, final int... skipLines) {
        assertJson(value, jsonRef, LineAssertionHelper.buildSkipLine(skipLines));
    }

    public static void assertJson(final Object value, final String jsonRef, final LineAssertion... lineAssertions) {
        assertText(jsonRef, JsonHelper.convertToJson(value), lineAssertions);
    }

    public static void assertJson(final String jsonRef, final String json, final int... skipLines) {
        assertJson(jsonRef, json, LineAssertionHelper.buildSkipLine(skipLines));
    }

    public static void assertJson(final String jsonRef, final String json, final LineAssertion... lineAssertions) {
        assertText(jsonRef, json, lineAssertions);
    }

    public static void assertTextRelative(final String value, final String path, final int... skipLines) {
        assertTextRelative(value, path, LineAssertionHelper.buildSkipLine(skipLines));
    }

    public static void assertTextRelative(final String value, final String path, final LineAssertion... lineAssertions) {
        final String refJson = FileReaderHelper.loadRelativeFile(path);
        assertText(refJson, value, lineAssertions);
    }


    public static void assertTextRelative(final String path, final Object value, final int... skipLines) {
        assertTextRelative(path, value, LineAssertionHelper.buildSkipLine(skipLines));
    }

    public static void assertTextRelative(final String path, final Object value, final LineAssertion... lineAssertions) {
        final String refJson = FileReaderHelper.loadRelativeFile(path);
        assertText(refJson, value, lineAssertions);

    }

    public static void assertText(final String jsonRef, final Object value, final int... skipLines) {
        assertText(jsonRef, value, LineAssertionHelper.buildSkipLine(skipLines));
    }

    public static void assertText(final String jsonRef, final Object value, final LineAssertion... lineAssertions) {
        AssertionHelper.assertText(jsonRef, JsonHelper.convertToJson(value), lineAssertions);
    }

    public static void assertText(final String jsonRef, final String json, int... skipLines) {
        assertText(jsonRef, json, LineAssertionHelper.buildSkipLine(skipLines));
    }

    public static void assertText(final String jsonRef, final String json, LineAssertion... lineAssertions) {
        Asserts.assertNotNull("json ref mustn't be null", jsonRef);
        Asserts.assertNotNull("json mustn't be null", json);
        final String[] jsonValue = json.split("\n");
        final String[] refLines  = jsonRef.split("\n");

        try {
            if (jsonValue.length != refLines.length) {
                Loggers.DEBUG.error(RenderingHelper.renderDiff(jsonValue, refLines));
            }
            Asserts.assertTrue(
                    String.format("reference and json have not same size : %s,%s", jsonValue.length, refLines.length),
                    jsonValue.length == refLines.length);
        } catch (final Throwable e) {
            throw e;
        }


        for (int i = 0; i < refLines.length; i++) {
            final String        currentJsonValue = jsonValue[i].trim();
            final String        currentRefLine   = refLines[i].trim();
            final LineAssertion lineAssertion    = LineAssertionHelper.searchLineAssertion(i, currentJsonValue, lineAssertions);

            if (lineAssertion == null) {
                if (!currentJsonValue.equals(currentRefLine)) {
                    Loggers.DEBUG.error(RenderingHelper.renderDiff(jsonValue, refLines));
                    throw new RuntimeException(
                            String.format("[%s] %s != %s", i + 1, jsonValue[i].trim(), refLines[i].trim()));
                }
            } else {
                if (lineAssertion.isSkipped(currentJsonValue)) {
                    continue;
                } else if (!lineAssertion.isMatching(currentJsonValue, currentRefLine)) {
                    Loggers.DEBUG.error(RenderingHelper.renderDiff(jsonValue, refLines));
                    throw new RuntimeException(
                            String.format("[%s] %s != %s", i + 1, jsonValue[i].trim(), refLines[i].trim()));
                }
            }
        }
    }

}
