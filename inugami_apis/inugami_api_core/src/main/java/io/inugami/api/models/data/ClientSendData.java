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
package io.inugami.api.models.data;

import io.inugami.interfaces.models.basic.JsonObject;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Pojo
 *
 * @author patrick_guillerm
 * @since 22 sept. 2016
 */
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Setter
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public final class ClientSendData {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    @ToString.Include
    @EqualsAndHashCode.Include
    private String           name;
    @ToString.Include
    @EqualsAndHashCode.Include
    private Long             time;
    private List<JsonObject> targets = new ArrayList<>();


    public void addTarget(final JsonObject target) {
        targets.add(target);
    }


}
