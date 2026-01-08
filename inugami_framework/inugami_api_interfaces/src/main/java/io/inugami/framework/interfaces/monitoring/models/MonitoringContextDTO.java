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

import java.time.Clock;

/**
 * @since 2026-01-08
 */
@ToString(onlyExplicitlyIncluded = true)
@Setter
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class MonitoringContextDTO {
    @Builder.Default
    @ToString.Include
    private CurrentApplicationDTO currentApplication = CurrentApplicationDTO.builder().build();
    @Builder.Default
    private Clock                 clock = Clock.systemUTC();
}
