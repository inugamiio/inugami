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
package io.inugami.api.exceptions;

import io.inugami.interfaces.exceptions.Warning;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CurrentWarningContext {
    private final List<Warning> warnings = new ArrayList<>();

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CurrentWarningContext{");
        sb.append("warnings=")
          .append(warnings);
        sb.append('}');
        return sb.toString();
    }

    public List<Warning> getWarnings() {
        return Collections.unmodifiableList(warnings);
    }

    public CurrentWarningContext addWarnings(Warning... warnings) {
        this.warnings.addAll(Arrays.asList(warnings));
        return this;
    }

    public CurrentWarningContext setWarnings(final List<Warning> warnings) {
        this.warnings.clear();
        if (warnings != null) {
            this.warnings.addAll(warnings);
        }
        return this;
    }
}
