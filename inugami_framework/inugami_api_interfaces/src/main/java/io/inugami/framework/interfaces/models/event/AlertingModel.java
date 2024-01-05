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
package io.inugami.framework.interfaces.models.event;

import io.inugami.framework.interfaces.models.ClonableObject;
import lombok.*;

import java.io.Serializable;

/**
 * AlertingModel
 *
 * @author patrick_guillerm
 * @since 20 déc. 2017
 */
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Setter
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
// alerting
public final class AlertingModel implements Serializable, ClonableObject<AlertingModel> {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final long serialVersionUID = 2557187994399108457L;

    @EqualsAndHashCode.Include
    private String name;
    private String provider;
    private String message;
    private String level;
    private String condition;
    private String function;


    @Override
    public AlertingModel cloneObj() {
        return toBuilder().build();
    }
}
