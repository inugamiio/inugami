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
package io.inugami.api.models.data.basic;

import io.inugami.api.spi.SpiLoader;
import lombok.*;

/**
 * RawJsonObject
 *
 * @author patrick_guillerm
 * @since 18 mai 2018
 */
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Setter
@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public final class RawJsonObject implements JsonObject {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final long serialVersionUID = -7034224735029402485L;

    private transient Object data;

    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================

    public static JsonObject buildFromObject(final Object data) {
        final RawJsonObject result = new RawJsonObject();
        result.setData(data);
        return result;
    }

    public static JsonObject buildFromJson(final String data) {
        final RawJsonObject     result         = new RawJsonObject();
        final JsonSerializerSpi jsonSerializer = SpiLoader.getInstance().loadSpiSingleService(JsonSerializerSpi.class);

        result.setData(jsonSerializer.deserialize(data));
        return result;
    }


}
