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
module io.inugami.monitoring.api {

    requires io.inugami.framework.api;
    requires io.inugami.framework.interfaces;
    requires lombok;
    requires org.slf4j;

    exports io.inugami.monitoring.api.dto;
    exports io.inugami.monitoring.api.exceptions;
    exports io.inugami.monitoring.api.obfuscators;
    exports io.inugami.monitoring.api.resolvers;
    exports io.inugami.monitoring.api.tools;
    uses io.inugami.monitoring.api.obfuscators.Obfuscator;
}