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
package io.inugami.framework.interfaces.monitoring.models;

import lombok.*;

import java.io.Serializable;

/**
 * @since 2026-01-07
 */
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Setter
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class CurrentApplicationDTO implements Serializable {
    private static final long   serialVersionUID = 860434663268503615L;
    private              String env;
    private              String asset;
    private              String hostname;
    private              String instanceName;
    private              String instanceNumber;
    @ToString.Include
    @EqualsAndHashCode.Include
    private              String groupId;
    @ToString.Include
    @EqualsAndHashCode.Include
    private              String artifactId;
    @ToString.Include
    @EqualsAndHashCode.Include
    private              String version;
    private              String commitId;
    private              String commitDate;
}
