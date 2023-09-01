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
package io.inugami.logs.obfuscator.obfuscators;

import io.inugami.logs.obfuscator.api.LogEventDto;
import io.inugami.logs.obfuscator.api.ObfuscatorSpi;

import java.util.regex.Pattern;

import static io.inugami.logs.obfuscator.tools.ObfuscatorUtils.MASK;
import static io.inugami.logs.obfuscator.tools.ObfuscatorUtils.replaceAll;

@SuppressWarnings({"java:S2068"})
public class AccessControlAllowHeaderObfuscator implements ObfuscatorSpi {


    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    public static final  String  TERM  = "Access-Control-Allow-Headers";
    private static final Pattern REGEX = Pattern.compile(new StringBuilder()
                                                                 .append("(?:")
                                                                 .append(TERM)
                                                                 .append(")")
                                                                 .append("(?:\\s*[")
                                                                 .append("=|:")
                                                                 .append("]\\s*)")
                                                                 .append("(.*)")
                                                                 .toString(),
                                                         Pattern.CASE_INSENSITIVE);

    private final boolean enabled = enabled();

    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    @Override
    public boolean isEnabled() {
        return enabled;
    }

    // =========================================================================
    // API
    // =========================================================================
    @Override
    public boolean accept(final LogEventDto event) {
        return contains(event.getMessage(), TERM, TERM.toUpperCase(), TERM.toLowerCase());
    }

    @Override
    public String obfuscate(final LogEventDto event) {
        return replaceAll(event.getMessage(), REGEX, value -> MASK);
    }


}
