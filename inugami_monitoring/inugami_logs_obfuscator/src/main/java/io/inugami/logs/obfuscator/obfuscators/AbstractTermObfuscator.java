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

import io.inugami.framework.api.tools.RunSafeUtils;
import io.inugami.framework.interfaces.monitoring.logger.LogEventDto;
import io.inugami.framework.interfaces.monitoring.logger.ObfuscatorSpi;
import lombok.extern.slf4j.Slf4j;

import java.util.regex.Pattern;

import static io.inugami.framework.interfaces.monitoring.logger.ObfuscatorUtils.*;

@Slf4j
public abstract class AbstractTermObfuscator implements ObfuscatorSpi {
    private final boolean enabled = enabled();

    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public static Pattern buildRegex(final String term) {
        return RunSafeUtils.runSafe(() -> buildRegexFullLine(term, DEFAULT_DELIMITERS), log);
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
        return replaceAll(event.getMessage(), getRegex(), this::obfuscateValue);
    }

    protected String obfuscateValue(String value) {
        return MASK;
    }

    protected abstract String getTerm();

    protected abstract Pattern getRegex();
}
