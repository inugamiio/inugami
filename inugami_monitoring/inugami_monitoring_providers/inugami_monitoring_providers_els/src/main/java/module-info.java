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
open module io.inugami.monitoring.providers.els {
    requires com.fasterxml.jackson.databind;
    requires io.inugami.framework.api;
    requires io.inugami.framework.commons;
    requires io.inugami.framework.interfaces;
    requires io.inugami.monitoring.api;
    requires org.slf4j;
    requires static lombok;
}