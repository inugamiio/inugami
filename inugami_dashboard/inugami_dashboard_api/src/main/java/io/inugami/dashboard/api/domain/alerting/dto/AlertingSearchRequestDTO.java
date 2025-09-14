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
package io.inugami.dashboard.api.domain.alerting.dto;

import io.inugami.framework.interfaces.models.search.SearchRequest;
import io.inugami.framework.interfaces.models.search.SortOrder;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Collection;

@ToString
@Setter
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class AlertingSearchRequestDTO implements SearchRequest {

    // SearchRequest
    private Integer                   page;
    private Integer                   pageSize;
    private String                    sortFields;
    private SortOrder                 sortOrder;
    @Singular("createdBy")
    private Collection<String>        createdBy;
    @Singular("createdDate")
    private Collection<LocalDateTime> createdDate;
    @Singular("lastModifiedBy")
    private Collection<String>        lastModifiedBy;
    @Singular("LastModifiedDate")
    private Collection<LocalDateTime> LastModifiedDate;
    @Singular("version")
    private Collection<Long>          version;

    // AlertingSearchRequest
    @Singular("uid")
    private Collection<String> uid;
    @Singular("name")
    private Collection<String> name;
    @Singular("description")
    private Collection<String> description;
    @Singular("provider")
    private Collection<String> provider;
    @Singular("message")
    private Collection<String> message;
    @Singular("level")
    private Collection<String> level;
}
