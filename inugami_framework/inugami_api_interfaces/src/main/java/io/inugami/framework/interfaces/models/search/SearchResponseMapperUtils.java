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

import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * @since 2025-12-29
 */
@UtilityClass
public class SearchResponseMapperUtils {

    public static <I, O> SearchResponse<O> convert(final SearchResponse<I> response, final Function<I, O> mapper) {
        if (response == null) {
            return SearchResponse.<O>builder()
                                 .data(List.of())
                                 .next(false)
                                 .previous(false)
                                 .totalPages(0)
                                 .nbFoundItems(0)
                                 .build();
        }
        final var data = Optional.ofNullable(response.getData()).orElse(List.of());

        return SearchResponse.<O>builder()
                             .data(data.stream().map(mapper::apply).toList())
                             .page(response.getPage())
                             .pageSize(response.getPageSize())
                             .totalPages(response.getTotalPages())
                             .sortFields(response.getSortFields())
                             .sortOrder(response.getSortOrder())
                             .nbFoundItems(response.getNbFoundItems())
                             .previous(response.getPrevious())
                             .next(response.getNext())
                             .filters(response.getFilters())
                             .build();
    }
}
