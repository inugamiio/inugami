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
package io.inugami.framework.interfaces.models.referential;

import io.inugami.framework.interfaces.models.basic.AuditDTO;
import io.inugami.framework.interfaces.models.basic.Auditable;
import lombok.*;

import java.util.Collection;

/**
 * @since 2026-01-16
 */
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ReferentialDTO implements Auditable {
    private static final long                       serialVersionUID = -6461615870865459099L;
    private              String                     id;
    @EqualsAndHashCode.Include
    @ToString.Include
    private              String                     label;
    private              String                     value;
    private              String                     description;
    @ToString.Include
    @EqualsAndHashCode.Include
    private              ReferentialTypeDTO         type;
    private              Boolean                    active;
    private              Collection<ReferentialDTO> children;
    private              AuditDTO                   audit;
}
