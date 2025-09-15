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

import java.util.Collection;

public interface SearchRequestFront {
    Integer getPage();

    void setPage(final Integer page);

    Integer getPageSize();

    void setPageSize(final Integer size);

    String getSortFields();

    SortOrder getSortOrder();

    // Audit
    Collection<String> getCreatedBy();

    Collection<String> getCreatedDate();

    Collection<String> getLastModifiedBy();

    Collection<String> getLastModifiedDate();

    Collection<Long> getVersion();
}
