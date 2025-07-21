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
module io.inugami.monitoring.springboot {
    requires spring.beans;
    requires spring.core;
    requires spring.context;
    requires spring.boot.autoconfigure;
    requires spring.web;
    requires org.mapstruct;
    requires io.inugami.monitoring.core;
    requires io.inugami.framework.api;
    requires jakarta.annotation;
    requires jakarta.servlet;


    exports io.inugami.monitoring.springboot.actuator;
    exports io.inugami.monitoring.springboot.actuator.feature;
    exports io.inugami.monitoring.springboot.api;
    exports io.inugami.monitoring.springboot.config;
    exports io.inugami.monitoring.springboot.exception;
    exports io.inugami.monitoring.springboot.filter;
    exports io.inugami.monitoring.springboot.partnerlog.feign;
    exports io.inugami.monitoring.springboot.request;
}