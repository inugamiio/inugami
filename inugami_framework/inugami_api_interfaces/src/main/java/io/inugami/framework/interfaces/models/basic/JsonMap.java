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
package io.inugami.framework.interfaces.models.basic;


import lombok.*;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * JsonMap
 *
 * @author patrick_guillerm
 * @since 10 oct. 2017
 */
@ToString
@EqualsAndHashCode
@Setter
@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class JsonMap<K extends Serializable, V extends Dto<?>> implements Dto<JsonMap<K, V>> {
    private static final long serialVersionUID = -6379031016086770674L;

    private Map<K, V> data;


    @Override
    public JsonMap<K, V> cloneObj() {
        final Map<K, V> newMap = new HashMap<>();
        if (data != null) {
            for (final Map.Entry<K, V> item : newMap.entrySet()) {
                final V itemData = item.getValue();
                if (itemData != null) {
                    newMap.put(item.getKey(), (V) itemData.cloneObj());
                }
            }
        }
        return new JsonMap<>(newMap);
    }

}
