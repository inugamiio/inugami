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
package io.inugami.framework.commons.spring.configuration;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@EqualsAndHashCode
@ToString
@Builder(toBuilder = true)
@Getter
public final class SpringConfigBinding {
    private final String springKey;
    private final String inugamiKey;
    private final String defaultValue;

    static SpringConfigBinding fromKey(final String key) {
        return new SpringConfigBinding(key, key, null);
    }

    static SpringConfigBinding fromKey(final String key, final String defaultValue) {
        return new SpringConfigBinding(key, key, defaultValue);
    }
}
