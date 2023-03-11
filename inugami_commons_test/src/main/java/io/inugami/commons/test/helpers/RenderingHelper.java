package io.inugami.commons.test.helpers;

import io.inugami.api.models.JsonBuilder;
import io.inugami.api.tools.ConsoleColors;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RenderingHelper {

    public static String renderDiff(final String[] jsonValue, final String[] refLines) {
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
            final String  currentLine = i >= currentNbLine ? "" : jsonValue[i];
            final String  refLine     = i >= refNbLines ? "" : refLines[i];
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
}
