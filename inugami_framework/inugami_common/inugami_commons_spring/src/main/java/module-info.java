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
open module io.inugami.framework.commons.spring {
    exports io.inugami.framework.commons.spring.mapstruct;
    exports io.inugami.framework.commons.spring.feature.interceptors;
    exports io.inugami.framework.commons.spring to spring.core;
    exports io.inugami.framework.commons.spring.configuration;
    requires jakarta.servlet;

    requires lombok;
    requires org.slf4j;
    requires spring.aop;
    requires spring.beans;
    requires spring.core;
    requires spring.context;
    requires spring.boot.autoconfigure;
    requires spring.web;
    requires org.mapstruct;
    requires org.aspectj.weaver;
    requires com.google.gson;

    requires io.inugami.framework.interfaces;
    requires io.inugami.framework.api;
    requires io.inugami.framework.commons;
    requires io.inugami.framework.configurations.configuration;
    requires org.zalando.problem;
    requires jakarta.annotation;
    requires spring.boot;

    uses io.inugami.framework.configuration.services.functions.ProviderAttributFunction;
}
