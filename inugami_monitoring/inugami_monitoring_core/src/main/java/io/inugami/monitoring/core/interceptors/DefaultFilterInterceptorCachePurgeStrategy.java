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
package io.inugami.monitoring.core.interceptors;


import io.inugami.framework.interfaces.monitoring.FilterInterceptorCachePurgeStrategy;

import java.util.Map;

public class DefaultFilterInterceptorCachePurgeStrategy implements FilterInterceptorCachePurgeStrategy {

    private static final int MAX_ITEMS = 20000;

    @Override
    public boolean shouldPurge(final Map<String, Boolean> values) {
        return (values == null ? 0 : values.size()) >= MAX_ITEMS;
    }
}
