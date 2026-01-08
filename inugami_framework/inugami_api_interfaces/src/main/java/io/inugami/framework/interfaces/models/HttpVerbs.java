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
package io.inugami.framework.interfaces.models;

import lombok.experimental.UtilityClass;

/**
 * @since 2026-01-08
 */
@UtilityClass
public class HttpVerbs {
    public static final String GET     = "GET";
    public static final String POST    = "POST";
    public static final String PATCH   = "PATCH";
    public static final String PUT     = "PUT";
    public static final String DELETE  = "DELETE";
    public static final String HEAD    = "HEAD";
    public static final String CONNECT = "CONNECT";
    public static final String OPTIONS = "OPTIONS";
    public static final String TRACE   = "TRACE";
}
