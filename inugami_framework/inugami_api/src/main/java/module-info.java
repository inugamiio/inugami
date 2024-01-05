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
module inugami.api {
    exports io.inugami.framework.api.configurtation;
    exports io.inugami.framework.api.constants;
    exports io.inugami.framework.api.listeners;
    exports io.inugami.framework.api.loggers.mdc.initializer;
    exports io.inugami.framework.api.loggers.mdc.mapper;
    exports io.inugami.framework.api.marshalling;


    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.databind;
    requires inugami.api.interfaces;
    requires jakarta.servlet;
    requires lombok;
    requires org.slf4j;
    requires java.desktop;

}
