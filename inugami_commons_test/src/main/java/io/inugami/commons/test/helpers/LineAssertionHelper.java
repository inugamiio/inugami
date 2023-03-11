package io.inugami.commons.test.helpers;

import io.inugami.commons.test.DefaultLineAssertion;
import io.inugami.commons.test.LineAssertion;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class LineAssertionHelper {

    public static LineAssertion searchLineAssertion(final int lineNumber,
                                                     final String line,
                                                     final LineAssertion[] lineAssertions) {
        if (lineAssertions != null) {
            for (LineAssertion lineAssertion : lineAssertions) {
                if (lineAssertion.accept(lineNumber, line)) {
                    return lineAssertion;
                }
            }
        }
        return null;
    }
    public static LineAssertion[] buildSkipLine(final int[] skipLines) {
        final List<LineAssertion> result = new ArrayList<>();
        if (skipLines != null) {
            for (int line : skipLines) {
                result.add(DefaultLineAssertion.skipLine(line));
            }
        }
        return result.toArray(new LineAssertion[]{});
    }

}
