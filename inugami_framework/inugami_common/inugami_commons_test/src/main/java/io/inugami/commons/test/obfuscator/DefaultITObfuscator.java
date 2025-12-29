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
package io.inugami.commons.test.obfuscator;

import io.inugami.framework.interfaces.models.JsonBuilder;
import io.inugami.framework.interfaces.monitoring.logger.BasicLogEvent;
import io.inugami.framework.interfaces.monitoring.logger.ConsoleColors;
import io.inugami.framework.interfaces.monitoring.logger.LogEventDto;
import io.inugami.framework.interfaces.tools.MapUtils;
import io.inugami.logs.obfuscator.obfuscators.AbstractTermObfuscator;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;


public class DefaultITObfuscator extends AbstractTermObfuscator {

    public static final  String  TERM           =
            "user-agent|\thost|datetime|duration|connection|x-b3-traceid|x-correlation-id|accept-encoding|content-length|appUrl";
    private static final Pattern REGEX          = initializeRegex();
    public static final  int     MARGIN         = 2;
    public static final  String  EMPTY          = "";
    public static final  String  SEPARATOR      = "|";
    public static final  String  SPACE          = " ";
    public static final  String  LOG_DECO       = "=";
    public static final  String  ATTR_SEPARATOR = " : ";

    private static Pattern initializeRegex() {
        final StringBuilder regex = new StringBuilder();
        regex.append("(?:").append(TERM).append(")");
        regex.append("(?:\\s*[").append(DEFAULT_DELIMITERS).append("]\\s*)");
        regex.append("((?:[^\\n]+)|(?:.*))");
        return Pattern.compile(regex.toString(), Pattern.CASE_INSENSITIVE);
    }

    @Override
    public boolean accept(final LogEventDto event) {
        return true;
    }

    @Override
    protected String getTerm() {
        return TERM;
    }

    @Override
    protected Pattern getRegex() {
        return REGEX;
    }

    public static String renderLogs(final Collection<BasicLogEvent> logs) {
        final JsonBuilder               result          = new JsonBuilder();
        final Collection<BasicLogEvent> data            = Optional.ofNullable(logs).orElse(List.of());
        int                             loggerLevelSize = 0;
        for (BasicLogEvent log : data) {
            int iLoggerLevelSize = Optional.ofNullable(log.getLevel()).map(String::length).orElse(0);
            if (iLoggerLevelSize > loggerLevelSize) {
                loggerLevelSize = iLoggerLevelSize;
            }
        }

        for (BasicLogEvent log : data) {
            result.write(ConsoleColors.createLine(LOG_DECO, 80)).line();
            final var loggerName = or(log.getLoggerName(), EMPTY);
            result.write(or(log.getLevel(), EMPTY));
            result.write(ConsoleColors.createLine(SPACE, (loggerLevelSize + MARGIN) - loggerName.length()));
            result.write(SEPARATOR).write(SPACE);
            result.write(loggerName);
            result.line();

            result.write("MDC :").line();
            result.write("-----").line();
            final Map<String, Serializable> mdc     = MapUtils.initMapAndSort(log.getMdc());
            int                             keySize = computeKeySize(mdc);
            for (Map.Entry<String, Serializable> entry : mdc.entrySet()) {
                result.tab()
                      .write(entry.getKey())
                      .write(ConsoleColors.createLine(SPACE, (keySize - entry.getKey().length())))
                      .write(ATTR_SEPARATOR)
                      .write(entry.getValue())
                      .line();
            }
            result.line();

            result.write("MESSAGE :").line();
            result.write("---------").line();
            result.write(log.getMessage()).line();
            result.write(ConsoleColors.createLine("-", 40)).line();
        }

        return result.toString();
    }

    private static int computeKeySize(final Map<String, Serializable> mdc) {
        int result = 0;
        for (String key : mdc.keySet()) {
            if (key.length() > result) {
                result = key.length();
            }
        }
        return 0;
    }

    private static <T> T or(final T value, final T ref) {
        return Optional.ofNullable(value).orElse(ref);
    }
}
