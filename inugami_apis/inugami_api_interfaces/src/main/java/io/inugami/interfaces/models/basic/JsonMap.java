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
package io.inugami.interfaces.models.basic;


import io.inugami.interfaces.models.JsonBuilder;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * JsonMap
 *
 * @author patrick_guillerm
 * @since 10 oct. 2017
 */
public class JsonMap<K extends Serializable, V extends JsonObject> implements JsonObject {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final long serialVersionUID = -6379031016086770674L;

    private final Map<K, V> data;
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================

    public JsonMap(final Map<K, V> data) {
        super();
        this.data = data;
    }

    // =========================================================================
    // METHODS
    // =========================================================================
    @Override
    public String toString() {
        return convertToJson();
    }

    // =========================================================================
    // OVERRIDES
    // =========================================================================
    @Override
    public String convertToJson() {
        final JsonBuilder json = new JsonBuilder();
        if (data == null) {
            json.valueNull();
        } else {
            json.openObject();

            int cursor = 0;
            for (final Map.Entry<K, V> entry : data.entrySet()) {
                if (cursor != 0) {
                    json.addSeparator();
                }
                if (entry.getValue() == null) {
                    json.valueNull();
                } else {
                    json.addField(String.valueOf(entry.getKey()));
                    json.write(entry.getValue().convertToJson());
                }
                cursor++;
            }

            json.closeObject();
        }
        return json.toString();
    }

    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    public Map<K, V> getData() {
        return data;
    }

    @Override
    public JsonObject cloneObj() {
        final Map<K, V> newMap = new HashMap<>();
        if (data != null) {
            for (final Map.Entry<K, V> item : newMap.entrySet()) {
                final JsonObject itemData = item.getValue();
                if (itemData != null) {
                    newMap.put(item.getKey(), (V) itemData.cloneObj());
                }
            }
        }
        return new JsonMap<>(newMap);
    }

}
