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

import io.inugami.framework.interfaces.models.search.MatchType;
import io.inugami.framework.interfaces.models.search.QueryFilterDTO;
import lombok.experimental.UtilityClass;

import java.util.List;

import static io.inugami.framework.interfaces.models.search.SearchFiltersUtils.buildFilters;
@UtilityClass
public class AlertingFilters {

    public static final String UID         = "uid";
    public static final String NAME        = "name";
    public static final String DESCRIPTION = "description";
    public static final String PROVIDER    = "provider";
    public static final String MESSAGE     = "message";
    public static final String LEVEL       = "level";

    public static final List<QueryFilterDTO<?>> FILTERS = buildFilters(
            QueryFilterDTO.<String>builder()
                          .field(UID)
                          .matchType(MatchType.IN)
                          .build(),
            QueryFilterDTO.<String>builder()
                          .field(NAME)
                          .matchType(MatchType.CONTAINS)
                          .build(),
            QueryFilterDTO.<String>builder()
                          .field(DESCRIPTION)
                          .matchType(MatchType.CONTAINS)
                          .build(),
            QueryFilterDTO.<String>builder()
                          .field(PROVIDER)
                          .matchType(MatchType.IN)
                          .build(),
            QueryFilterDTO.<String>builder()
                          .field(MESSAGE)
                          .matchType(MatchType.CONTAINS)
                          .build(),
            QueryFilterDTO.<String>builder()
                          .field(LEVEL)
                          .matchType(MatchType.IN)
                          .build()
    );
}
