package io.inugami.framework.interfaces.commons;

import io.inugami.framework.interfaces.exceptions.Asserts;
import io.inugami.framework.interfaces.exceptions.UncheckedException;
import io.inugami.framework.interfaces.models.JsonBuilder;
import io.inugami.framework.interfaces.monitoring.logger.ConsoleColors;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UnitTestHelperText {


    static void assertTextIntegration(final String value, final String path, final LineMatcher... matchers) {
        final String refJson = UnitTestHelperFile.readFileIntegration(path);
        assertText(value, refJson, matchers);
    }


    static void assertTextIntegration(final Object value, final String path, final LineMatcher... matchers) {
        final String refJson = UnitTestHelperFile.readFileIntegration(path);
        assertText(value, refJson, matchers);
    }


    static void assertTextRelative(final String value, final String path, final LineMatcher... matchers) {
        final String refJson = UnitTestHelperFile.readFileRelative(path);
        assertText(value, refJson, matchers);
    }


    static void assertTextRelative(final Object value, final String path, final LineMatcher... matchers) {
        final String refJson = UnitTestHelperFile.readFileRelative(path);
        assertText(value, refJson, matchers);
    }


    static void assertText(final Object value, final String jsonRef, final LineMatcher... matchers) {
        if (jsonRef == null) {
            Asserts.assertNull("json must be null", value);
        } else {
            Asserts.assertNotNull("json mustn't be null", value);
            final String json = UnitTestHelperJson.convertToJson(value);
            assertText(json, jsonRef, matchers);
        }
    }

    static void assertText(final String json, final String jsonRef, final LineMatcher... matchers) {
        Asserts.assertNotNull("json ref mustn't be null", jsonRef);
        Asserts.assertNotNull("json mustn't be null", json);
        final String[] jsonValue = json.split("\n");
        final String[] refLines  = jsonRef.split("\n");

        try {
            if (jsonValue.length != refLines.length) {
                log.error(renderDiff(jsonValue, refLines));
            }
            Asserts.assertTrue(
                    String.format("reference and json have not same size : %s,%s", jsonValue.length, refLines.length),
                    jsonValue.length == refLines.length);
        } catch (final Throwable e) {
            throw e;
        }


        for (int i = 0; i < refLines.length; i++) {
            final String      valueLine = jsonValue[i].trim();
            final String      refLine   = refLines[i].trim();
            final LineMatcher matcher   = resolveMatcher(i, valueLine, refLine, matchers);

            if (matcher.skip(i, valueLine, refLine)) {
                continue;
            }
            if (!matcher.match(i, valueLine, refLine)) {
                log.error(renderDiff(jsonValue, refLines));
                throw new AssertTextException(
                        String.format("[%s][%s] %s != %s", matcher.getClass(),
                                      i + 1, jsonValue[i].trim(), refLines[i].trim()));
            }

        }
    }

    private static LineMatcher resolveMatcher(final int index,
                                              final String valueLine,
                                              final String refLine,
                                              final LineMatcher[] matchers) {
        LineMatcher result = null;
        if (matchers != null) {
            for (final LineMatcher matcher : matchers) {
                if (matcher.accept(index, valueLine, refLine)) {
                    result = matcher;
                    break;
                }
            }
        }
        return result == null ? new DefaultLineMatcher() : result;
    }

    public static class AssertTextException extends UncheckedException {
        public AssertTextException(final String message) {
            super(message);
        }
    }
    // =========================================================================
    // RENDERING
    // =========================================================================

    private static String renderDiff(final String[] jsonValue, final String[] refLines) {
        final JsonBuilder writer = new JsonBuilder();
        writer.line();
        writer.write("Current:").line().write(ConsoleColors.createLine("-", 80)).line();
        writer.write(String.join("\n", jsonValue)).line();
        writer.write(ConsoleColors.createLine("-", 80));

        writer.line().line();
        final int    currentNbLine    = jsonValue.length;
        final int    refNbLines       = refLines.length;
        final int    nbLines          = jsonValue.length > refLines.length ? jsonValue.length : refLines.length;
        final int    lineColumn       = String.valueOf(nbLines).length() + 2;
        final int    maxCurrentColumn = computeMaxColumnSize(jsonValue);
        final int    maxRefColumn     = computeMaxColumnSize(refLines);
        final String lineDeco         = " |";
        final String diffMiddleOk     = "     ";
        final String diffMiddleKO     = " <-> ";
        final int    fullSize         = lineColumn + maxCurrentColumn + diffMiddleOk.length() + maxRefColumn;

        writer.write(ConsoleColors.createLine("=", fullSize)).line();
        writer.write(ConsoleColors.createLine(" ", lineColumn)).write(lineDeco);
        writer.write(writeCenter("Current", maxCurrentColumn));
        writer.write(diffMiddleOk);
        writer.write(writeCenter("Reference", maxRefColumn));
        writer.line();
        writer.write(ConsoleColors.createLine("=", fullSize)).line();

        for (int i = 0; i < nbLines; i++) {
            final String  currentLine = i >= currentNbLine ? UnitTestHelperCommon.EMPTY : jsonValue[i];
            final String  refLine     = i >= refNbLines ? UnitTestHelperCommon.EMPTY : refLines[i];
            final boolean diff        = !currentLine.trim().equals(refLine.trim());
            if (diff) {
                writer.write(ConsoleColors.RED);
            }
            writer.write(i).write(ConsoleColors.createLine(" ", lineColumn - String.valueOf(i).length()));
            writer.write(lineDeco);
            writer.write(currentLine).write(ConsoleColors.createLine(" ", maxCurrentColumn - currentLine.length()));
            writer.write(diff ? diffMiddleKO : diffMiddleOk);
            writer.write(refLine);
            if (diff) {
                writer.write(ConsoleColors.RESET);
            }
            writer.line();
        }

        return writer.toString();
    }


    private static int computeMaxColumnSize(final String[] lines) {
        int result = 20;
        if (lines != null) {
            for (final String line : lines) {
                if (line.length() > result) {
                    result = line.length();
                }
            }
        }
        return result;
    }

    private static String writeCenter(final String title, final int columnSize) {
        final StringBuilder result = new StringBuilder();
        if (columnSize < title.length()) {
            result.append(title.substring(0, columnSize));
        } else {
            final int titleOffset = title.length() / 2;

            result.append(ConsoleColors.createLine(" ", titleOffset));
            result.append(title);
            result.append(ConsoleColors.createLine(" ", columnSize - (titleOffset + title.length())));
        }
        return result.toString();
    }

    public static LineMatcher[] buildSkipLines(final int... skipLines) {
        if (skipLines == null) {
            return null;
        }
        final List<LineMatcher> result = new ArrayList<>();
        for (final int line : skipLines) {
            result.add(SkipLineMatcher.of(line));
        }
        return result.toArray(new LineMatcher[]{});
    }

}
