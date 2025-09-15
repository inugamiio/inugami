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
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@ToString(onlyExplicitlyIncluded = true)
@Setter
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class SearchResponse<T> implements Serializable {
    @ToString.Include
    private int                       page;
    @ToString.Include
    private int                       pageSize;
    private int                       totalPages;
    @ToString.Include
    private String                    sortFields;
    @ToString.Include
    private SortOrder                 sortOrder;
    @ToString.Include
    private long                      nbFoundItems;
    private Boolean                   previous;
    private Boolean                   next;
    private Map<String, Serializable> filters;
    private List<T>                   data;

    public boolean hasContent() {
        return data != null && !data.isEmpty();
    }

    public boolean hasPrevious() {
        return previous == null ? false : previous.booleanValue();
    }

    public boolean hasNext() {
        return next == null ? false : next.booleanValue();
    }

    public void processOverData(final Consumer<T> consumer) {
        if (hasContent() && consumer != null) {
            data.forEach(consumer::accept);
        }
    }
}
