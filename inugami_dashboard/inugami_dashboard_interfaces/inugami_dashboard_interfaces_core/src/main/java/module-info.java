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
open module io.inugami.dashboard.interfaces.core {
    requires com.fasterxml.jackson.annotation;
    requires io.inugami.dashboard.api;
    requires io.inugami.dashboard.interfaces.api;
    requires io.inugami.framework.api;
    requires io.inugami.framework.interfaces;
    requires lombok;
    requires org.slf4j;
    requires spring.web;
    requires spring.context;
    requires org.mapstruct;
}
