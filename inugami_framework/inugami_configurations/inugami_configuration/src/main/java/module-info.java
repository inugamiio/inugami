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
module io.inugami.framework.configurations.configuration {
    requires lombok;
    requires org.slf4j;

    requires io.inugami.framework.interfaces;
    requires io.inugami.framework.api;
    requires io.inugami.framework.commons;

    exports io.inugami.framework.configuration.exceptions;
    exports io.inugami.framework.configuration.models;
    exports io.inugami.framework.configuration.models.app;
    exports io.inugami.framework.configuration.models.components;
    exports io.inugami.framework.configuration.models.front;
    exports io.inugami.framework.configuration.models.plugins;
    exports io.inugami.framework.configuration.services;
    exports io.inugami.framework.configuration.services.functions;
    exports io.inugami.framework.configuration.services.mapping;
    exports io.inugami.framework.configuration.services.resolver;
    exports io.inugami.framework.configuration.services.resolver.strategies;
    exports io.inugami.framework.configuration.services.validators;

}
