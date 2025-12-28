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
open module io.inugami.framework.commons.spring.data {
    requires spring.core;
    requires io.inugami.framework.interfaces;
    requires static lombok;
    requires com.querydsl.core;
    requires com.querydsl.jpa;
    requires spring.data.jpa;
    requires spring.data.commons;

    exports io.inugami.framework.commons.spring.data.utils;

}
