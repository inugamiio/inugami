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
open module io.inugami.dashboard.infrastructure {

    exports io.inugami.dashboard.infrastructure.database;
    exports io.inugami.dashboard.infrastructure.internal.schduler;
    exports io.inugami.dashboard.infrastructure.sender;

    requires io.inugami.dashboard.api;
    requires io.inugami.framework.commons.components;
    requires io.inugami.framework.commons;
    requires io.inugami.framework.configurations.configuration;
    requires io.inugami.framework.interfaces;
    requires jakarta.annotation;
    requires java.sql;
    requires lombok;
    requires org.mapstruct;
    requires org.slf4j;
    requires spring.boot;
    requires spring.context;
    requires com.hazelcast.core;
    requires org.jspecify;
}
