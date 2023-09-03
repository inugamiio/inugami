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

public abstract class AbstractTermObfuscator implements ObfuscatorSpi {
    private final boolean enabled = enabled();

    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    @Override
    public boolean isEnabled() {
        return enabled;
    }

    static Pattern buildRegex(final String term) {
        return Pattern.compile(new StringBuilder()
                                       .append("(?:")
                                       .append(term)
                                       .append(")")
                                       .append("(?:\\s*[")
                                       .append("=|:")
                                       .append("]\\s*)")
                                       .append("(.*)")
                                       .toString(),
                               Pattern.CASE_INSENSITIVE);
    }

    // =========================================================================
    // API
    // =========================================================================
    @Override
    public boolean accept(final LogEventDto event) {
        return contains(event.getMessage(), getTerm(), getTerm().toUpperCase(), getTerm().toLowerCase());
    }


    @Override
    public String obfuscate(final LogEventDto event) {
        return replaceAll(event.getMessage(), getRegex(), value -> MASK);
    }

    protected abstract String getTerm();

    protected abstract Pattern getRegex();
}
