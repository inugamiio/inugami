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
package io.inugami.framework.interfaces.models.search;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;

/**
 * @since 2025-12-28
 */
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@Setter
@Getter
public class SearchRequestDefaultDTO implements SearchRequest, Serializable {
    private static final long                      serialVersionUID = -8435181778683183759L;
    private              Integer                   page;
    private              Integer                   pageSize;
    private              String                    sortFields;
    private              SortOrder                 sortOrder;
    private              Collection<String>        createdBy;
    private              Collection<LocalDateTime> createdDate;
    private              Collection<String>        lastModifiedBy;
    private              Collection<LocalDateTime> lastModifiedDate;
}
