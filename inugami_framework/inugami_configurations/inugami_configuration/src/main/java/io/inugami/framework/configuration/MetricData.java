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
package io.inugami.framework.configuration;


import io.inugami.framework.configuration.models.ProviderConfig;
import lombok.*;

/**
 * Pojo
 *
 * @author rachid_nidsaid
 * @since 22 sept. 2016
 */
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class MetricData {

    // =================================================================================================================
    // ATTRIBUTES
    // =================================================================================================================
    @EqualsAndHashCode.Include
    @ToString.Include
    protected String         name;
    @ToString.Include
    protected String         request;
    protected ProviderConfig config;
    protected Long           lastCallTime;
    @ToString.Include
    protected String         from;
    @ToString.Include
    protected String         until;
    @ToString.Include
    protected String         groupName;
}
