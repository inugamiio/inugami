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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Supplier;

@UtilityClass
public class SearchFiltersUtils {
    public static final String CREATED_BY         = "createdBy";
    public static final String CREATED_DATE       = "createdDate";
    public static final String LAST_MODIFIED_BY   = "lastModifiedBy";
    public static final String LAST_MODIFIED_DATE = "lastModifiedDate";
    public static final String VERSION            = "version";

    protected static final List<QueryFilterDTO<?>> DEFAULT_FILTERS = List.of(
            QueryFilterDTO.<String>builder()
                          .field(CREATED_BY)
                          .matchType(MatchType.CONTAINS)
                          .build(),
            QueryFilterDTO.<String>builder()
                          .field(CREATED_DATE)
                          .matchType(MatchType.BETWEEN)
                          .build(),
            QueryFilterDTO.<String>builder()
                          .field(LAST_MODIFIED_BY)
                          .matchType(MatchType.CONTAINS)
                          .build(),
            QueryFilterDTO.<String>builder()
                          .field(LAST_MODIFIED_DATE)
                          .matchType(MatchType.BETWEEN)
                          .build(),
            QueryFilterDTO.<String>builder()
                          .field(VERSION)
                          .matchType(MatchType.BETWEEN)
                          .build()
    );

    public static List<QueryFilterDTO<?>> buildFilters(final QueryFilterDTO<?>... filters) {
        final List<QueryFilterDTO<?>> result = new ArrayList<>();
        if (filters.length > 0) {
            result.addAll(Arrays.asList(filters));
        }
        result.addAll(DEFAULT_FILTERS);
        return result;
    }

    public static Collection<LocalDate> convertToLocalDate(final Object dto,
                                                           final Supplier<Collection<String>> supplier) {
        if (dto == null || supplier == null) {
            return null;
        }
        return Optional.ofNullable(supplier.get())
                       .orElse(List.of())
                       .stream()
                       .map(date -> LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE))
                       .toList();

    }

    public static LocalDate convertToLocalDateSingle(final Object dto,
                                                     final Supplier<String> supplier) {
        if (dto == null || supplier == null) {
            return null;
        }
        return Optional.ofNullable(supplier.get())
                       .map(date -> LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE))
                       .orElse(null);
    }

    public static Collection<LocalDateTime> convertToLocalDateTime(final Object dto,
                                                                   final Supplier<Collection<String>> supplier) {
        if (dto == null || supplier == null) {
            return null;
        }
        return Optional.ofNullable(supplier.get())
                       .orElse(List.of())
                       .stream()
                       .map(date -> LocalDateTime.parse(date, DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                       .toList();

    }

    public static LocalDateTime convertToLocalDateTimeSingle(final Object dto,
                                                     final Supplier<String> supplier) {
        if (dto == null || supplier == null) {
            return null;
        }
        return Optional.ofNullable(supplier.get())
                       .map(date -> LocalDateTime.parse(date, DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                       .orElse(null);
    }
}
