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
package io.inugami.interfaces.marshalling;

import io.inugami.interfaces.models.basic.JsonSerializerSpi;
import io.inugami.interfaces.models.basic.JsonSerializerSpiException;
import io.inugami.interfaces.monitoring.logger.Loggers;
import io.inugami.interfaces.spi.SpiLoader;

import java.io.Serializable;


/**
 * JsonObject
 *
 * @author patrick_guillerm
 * @since 12 janv. 2017
 */
public interface JsonObjectToJson extends Serializable {

    default String convertToJson() {
        try {
            final JsonSerializerSpi serializer = SpiLoader.getInstance().loadSpiSingleService(JsonSerializerSpi.class);
            return serializer.serialize(this);
        } catch (JsonSerializerSpiException e) {
            Loggers.DEBUG.error(e.getMessage(), e);
            return null;
        }
    }


}
