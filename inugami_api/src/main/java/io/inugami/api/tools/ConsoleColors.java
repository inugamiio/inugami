/* --------------------------------------------------------------------
 *  Inugami
 * --------------------------------------------------------------------
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package io.inugami.api.tools;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ConsoleColors {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    public enum State {
        ERROR,
        WARN,
        SUCCESS,
        UNDEFINE
    }

    // Reset
    public static final String RESET     = "\033[0m";
    public static final String CLEAR_ALL = "\033c";

    // Regular Colors
    public static final String BLACK  = "\033[0;30m";
    public static final String RED    = "\033[0;31m";
    public static final String GREEN  = "\033[0;32m";
    public static final String YELLOW = "\033[0;33m";
    public static final String BLUE   = "\033[0;34m";
    public static final String PURPLE = "\033[0;35m";
    public static final String CYAN   = "\033[0;36m";
    public static final String WHITE  = "\033[0;37m";

    // Bold
    public static final String BLACK_BOLD  = "\033[1;30m";
    public static final String RED_BOLD    = "\033[1;31m";
    public static final String GREEN_BOLD  = "\033[1;32m";
    public static final String YELLOW_BOLD = "\033[1;33m";
    public static final String BLUE_BOLD   = "\033[1;34m";
    public static final String PURPLE_BOLD = "\033[1;35m";
    public static final String CYAN_BOLD   = "\033[1;36m";
    public static final String WHITE_BOLD  = "\033[1;37m";

    // Underline
    public static final String BLACK_UNDERLINED  = "\033[4;30m";
    public static final String RED_UNDERLINED    = "\033[4;31m";
    public static final String GREEN_UNDERLINED  = "\033[4;32m";
    public static final String YELLOW_UNDERLINED = "\033[4;33m";
    public static final String BLUE_UNDERLINED   = "\033[4;34m";
    public static final String PURPLE_UNDERLINED = "\033[4;35m";
    public static final String CYAN_UNDERLINED   = "\033[4;36m";
    public static final String WHITE_UNDERLINED  = "\033[4;37m";

    // Background
    public static final String BLACK_BACKGROUND  = "\033[40m";
    public static final String RED_BACKGROUND    = "\033[41m";
    public static final String GREEN_BACKGROUND  = "\033[42m";
    public static final String YELLOW_BACKGROUND = "\033[43m";
    public static final String BLUE_BACKGROUND   = "\033[44m";
    public static final String PURPLE_BACKGROUND = "\033[45m";
    public static final String CYAN_BACKGROUND   = "\033[46m";
    public static final String WHITE_BACKGROUND  = "\033[47m";

    // High Intensity
    public static final String BLACK_BRIGHT  = "\033[0;90m";
    public static final String RED_BRIGHT    = "\033[0;91m";
    public static final String GREEN_BRIGHT  = "\033[0;92m";
    public static final String YELLOW_BRIGHT = "\033[0;93m";
    public static final String BLUE_BRIGHT   = "\033[0;94m";
    public static final String PURPLE_BRIGHT = "\033[0;95m";
    public static final String CYAN_BRIGHT   = "\033[0;96m";
    public static final String WHITE_BRIGHT  = "\033[0;97m";

    // Bold High Intensity
    public static final String BLACK_BOLD_BRIGHT  = "\033[1;90m";
    public static final String RED_BOLD_BRIGHT    = "\033[1;91m";
    public static final String GREEN_BOLD_BRIGHT  = "\033[1;92m";
    public static final String YELLOW_BOLD_BRIGHT = "\033[1;93m";
    public static final String BLUE_BOLD_BRIGHT   = "\033[1;94m";
    public static final String PURPLE_BOLD_BRIGHT = "\033[1;95m";
    public static final String CYAN_BOLD_BRIGHT   = "\033[1;96m";
    public static final String WHITE_BOLD_BRIGHT  = "\033[1;97m";

    // High Intensity backgrounds
    public static final String BLACK_BACKGROUND_BRIGHT  = "\033[0;100m";
    public static final String RED_BACKGROUND_BRIGHT    = "\033[0;101m";
    public static final String GREEN_BACKGROUND_BRIGHT  = "\033[0;102m";
    public static final String YELLOW_BACKGROUND_BRIGHT = "\033[0;103m";
    public static final String BLUE_BACKGROUND_BRIGHT   = "\033[0;104m";
    public static final String PURPLE_BACKGROUND_BRIGHT = "\033[0;105m";
    public static final String CYAN_BACKGROUND_BRIGHT   = "\033[0;106m";
    public static final String WHITE_BACKGROUND_BRIGHT  = "\033[0;107m";


    // =========================================================================
    // API
    // =========================================================================
    public static String renderState(final State state) {
        return renderState(state, null);
    }

    public static String renderState(final State state, final String message) {
        final StringBuilder result     = new StringBuilder();
        final State         localState = state == null ? State.UNDEFINE : state;

        switch (localState) {
            case ERROR:
                result.append(ConsoleColors.RED_BOLD);
                result.append("X\t");
                result.append(message == null ? "" : message);
                result.append(ConsoleColors.RESET);
                break;
            case SUCCESS:
                result.append(ConsoleColors.GREEN);
                result.append("✔\t");
                result.append(message == null ? "" : message);
                result.append(ConsoleColors.RESET);
                break;
            case WARN:
                result.append(ConsoleColors.YELLOW_BOLD);
                result.append("~\t");
                result.append(message == null ? "" : message);
                result.append(ConsoleColors.RESET);
                break;
        }

        return result.toString();
    }


    public static String createLine(final String deco, final int size) {
        final StringBuilder result = new StringBuilder();
        for (int i = size - 1; i >= 0; i--) {
            result.append(deco);
        }
        return result.toString();
    }
}
