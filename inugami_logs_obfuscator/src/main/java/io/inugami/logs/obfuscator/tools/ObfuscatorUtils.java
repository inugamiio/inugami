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
package io.inugami.logs.obfuscator.tools;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static io.inugami.logs.obfuscator.api.Constants.MASK;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ObfuscatorUtils {


    // =========================================================================
    // CONTAINS
    // =========================================================================
    public static boolean contains(final String content, String... values) {
        if (content == null || values == null) {
            return false;
        }

        boolean result = false;

        for (String value : values) {
            result = content.contains(value);
            if (result) {
                break;
            }
        }

        return result;
    }

    public static boolean containsAll(final String content, String... values) {
        if (content == null || values == null) {
            return false;
        }

        boolean result = false;

        for (String value : values) {
            result = content.contains(value);
            if (!result) {
                break;
            }
        }

        return result;
    }

    // =========================================================================
    // BUILD REGEX
    // =========================================================================
    public static Pattern buildRegex(final String term, final String delimiter) {
        final StringBuilder regex = new StringBuilder();
        regex.append("(?:")
             .append(term)
             .append(")");
        regex.append("(?:\\s*[")
             .append(delimiter)
             .append("]\\s*)");
        regex.append("([^\\s]+)");
        return Pattern.compile(regex.toString(), Pattern.CASE_INSENSITIVE);
    }

    public static Pattern buildJsonRegex(final String field) {
        final StringBuilder regex = new StringBuilder();
        regex.append("(?:\"");
        regex.append(field);
        regex.append("\")(?:\\s*:\\s*)(?:\")([^\"]+)(?:\")");
        return Pattern.compile(regex.toString(), Pattern.CASE_INSENSITIVE);
    }

    public static Pattern buildXmlRegex(final String field) {
        final StringBuilder regex = new StringBuilder();
        regex.append("(?:[<])(?:(");
        regex.append(field);
        regex.append(")([^>]*)[>])([^<]*)([<][^>]+[>])");
        return Pattern.compile(regex.toString(), Pattern.CASE_INSENSITIVE);
    }
    // =========================================================================
    // REPLACE ALL
    // =========================================================================
    public static String replaceAll(final String message,
                                    final Pattern pattern,
                                    final Function<String, String> obfuscateFunction) {
        if (message == null || pattern == null || obfuscateFunction == null) {
            return message;
        }


        int                 cursor  = 0;
        final Matcher       matcher = pattern.matcher(message);
        final StringBuilder buffer  = new StringBuilder();
        while (matcher.find(cursor)) {
            final MatchResult matchResult = matcher.toMatchResult();
            buffer.append(message.substring(cursor, matchResult.start(1)));

            final String content = message.substring(matchResult.start(1), matchResult.end(1));
            buffer.append(obfuscateFunction.apply(content));

            cursor = matchResult.end(1);
        }

        if (cursor == 0) {
            return message;
        }
        else if (cursor > 0 && cursor < message.length() - 1) {
            buffer.append(message.substring(cursor));
        }

        return buffer.toString();
    }

    public static String keepLastChars(final String value, final int nbChars){
        String result = "";
        if(value!=null && value.length()> nbChars){
            result = value.substring(value.length()-(nbChars+1));
        }
        return MASK + result;
    }

    public static String replaceAllWithGroup(final String message,
                                    final Pattern pattern,
                                    final Function<List<String>, String> obfuscateFunction) {
        if (message == null || pattern == null || obfuscateFunction == null) {
            return message;
        }


        int                 cursor  = 0;
        final Matcher       matcher = pattern.matcher(message);
        final StringBuilder buffer  = new StringBuilder();
        while (matcher.find(cursor)) {
            final MatchResult matchResult = matcher.toMatchResult();
            List<String> groups = new ArrayList<>();
            for(int i = 0; i< matchResult.groupCount(); i++){
                groups.add(matchResult.group(i));
            }
            buffer.append(obfuscateFunction.apply(groups));

            cursor = matchResult.end();
        }

        if (cursor == 0) {
            return message;
        }
        else if (cursor > 0 && cursor < message.length() - 1) {
            buffer.append(message.substring(cursor));
        }

        return buffer.toString();
    }
}