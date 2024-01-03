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
package io.inugami.interfaces.providers;

import io.inugami.interfaces.models.basic.JsonObject;

/**
 * The <strong>ProviderWriter</strong> is designed to create writer providers.
 * These will be able to write a <strong>JsonObject</strong> to datasource.
 *
 * @author patrick_guillerm
 * @since 28 mai 2018
 */
public interface ProviderWriter {
    void write(JsonObject data);
}
