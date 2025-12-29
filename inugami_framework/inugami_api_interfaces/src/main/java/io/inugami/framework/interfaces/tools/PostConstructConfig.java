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
package io.inugami.framework.interfaces.tools;

import io.inugami.framework.interfaces.configurtation.ConfigHandler;
import io.inugami.framework.interfaces.models.maven.ManifestInfo;

/**
 * Used for SPI implementations or Inugami plugin, the <strong>NamedComponent</strong> allows to retrieve the
 * component name.
 *
 * @author patrick_guillerm
 * @since 2025-10-11
 */
public interface PostConstructConfig {
    default void postConstruct(final ConfigHandler<String, String> configuration, final ManifestInfo manifest) {
    }
}
