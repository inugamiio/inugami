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
open module io.inugami.framework.commons.components {

    requires annotations;
    requires com.fasterxml.jackson.annotation;
    requires tools.jackson.core;
    requires tools.jackson.databind;
    requires tools.jackson.dataformat.yaml;
    requires tools.jackson.datatype.javatime;
    requires io.inugami.framework.api;
    requires io.inugami.framework.commons;
    requires io.inugami.framework.interfaces;
    requires lombok;
    requires org.slf4j;
    requires java.management;

    exports io.inugami.framework.commons.components.providers.system;

}
