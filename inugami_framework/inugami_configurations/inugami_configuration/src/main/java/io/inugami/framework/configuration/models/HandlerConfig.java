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
package io.inugami.framework.configuration.models;

import io.inugami.framework.interfaces.configurtation.BehaviourComponents;
import lombok.*;

import java.io.Serializable;
import java.util.Map;

@SuppressWarnings({"java:S2160"})
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@EqualsAndHashCode
public class HandlerConfig implements Serializable , BehaviourComponents {

    // =================================================================================================================
    // ATTRIBUTES
    // =================================================================================================================
    private static final long                serialVersionUID = -717509522684920613L;
    private              Map<String, String> configs;
    private              String              type;
    private              String              name;
    private              String              className;
}
