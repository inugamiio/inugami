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
module io.inugami.dashboard.webapp {
    exports io.inugami.dashboard.webapp;

    requires com.fasterxml.jackson.annotation;
    requires io.inugami.dashboard.api;
    requires io.inugami.dashboard.core;
    requires io.inugami.dashboard.infrastructure;
    requires io.inugami.dashboard.interfaces.api;
    requires io.inugami.dashboard.interfaces.core;
    requires io.inugami.framework.commons.spring;
    requires io.inugami.framework.interfaces;
    requires io.inugami.framework.api;
    requires io.inugami.framework.commons;
    requires io.inugami.framework.configurations.configuration;

    requires spring.context;
    requires spring.boot.autoconfigure;
    requires spring.boot;
    requires jakarta.servlet;
    requires lombok;
    requires org.slf4j;
    requires spring.aop;
    requires spring.beans;
    requires spring.core;
    requires spring.web;
    requires org.mapstruct;
    requires org.aspectj.weaver;
    requires java.sql;
    opens io.inugami.dashboard.webapp to spring.core;
}
