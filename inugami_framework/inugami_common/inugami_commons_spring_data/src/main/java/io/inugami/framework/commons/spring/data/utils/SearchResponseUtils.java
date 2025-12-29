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
package io.inugami.framework.commons.spring.data.utils;

import io.inugami.framework.interfaces.models.search.SearchRequest;
import io.inugami.framework.interfaces.models.search.SearchResponse;
import io.inugami.framework.interfaces.models.search.SortOrder;
import lombok.experimental.UtilityClass;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static io.inugami.framework.interfaces.functionnals.FunctionalUtils.applyIfNotNull;

/**
 * @since 2025-12-28
 */
@SuppressWarnings({"java:S2293"})
@UtilityClass
public class SearchResponseUtils {
    public static PageRequest buildPageRequest(final SearchRequest searchRequest, final String defaultField) {
        final var request = Optional.ofNullable(searchRequest);
        return PageRequest.of(request.map(SearchRequest::getPage).orElse(0),
                              request.map(SearchRequest::getPageSize).orElse(20),
                              SortOrder.DESC == request.map(SearchRequest::getSortOrder).orElse(SortOrder.ASC)
                              ? Sort.Direction.DESC
                              : Sort.Direction.ASC,
                              request.map(SearchRequest::getSortFields)
                                     .orElse(defaultField));
    }

    public static <E> Page<E> buildEmptyPage(final PageRequest pageRequest) {
        return new PageImpl<E>(List.of(), pageRequest, 0);
    }

    public static <T, E> SearchResponse<T> buildSearchResponse(final Page<E> data,
                                                               final Function<E, T> mapper) {
        final List<T>     currentData = new ArrayList<>();
        final Iterator<E> iterator    = data.iterator();
        while (iterator.hasNext()) {
            final E item = iterator.next();
            applyIfNotNull(mapper.apply(item), currentData::add);
        }

        return SearchResponse.<T>builder()
                             .page(data.getNumber())
                             .pageSize(data.getSize())
                             .next(data.hasNext())
                             .previous(data.hasPrevious())
                             .nbFoundItems(data.getTotalElements())
                             .totalPages(data.getTotalPages())
                             .data(currentData)
                             .build();
    }
}
