import io.inugami.framework.interfaces.exceptions.ProblemAdditionalFieldBuilder;
import io.inugami.framework.interfaces.monitoring.spring.feign.FeignErrorCodeBuilderSpi;

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
open module io.inugami.monitoring.springboot {
    requires com.fasterxml.jackson.databind;
    requires feign.core;
    requires feign.jackson;
    requires feign.okhttp;
    requires io.inugami.framework.api;
    requires io.inugami.framework.commons.spring;
    requires io.inugami.framework.interfaces;
    requires io.inugami.monitoring.core;
    requires jakarta.annotation;
    requires jakarta.servlet;
    requires org.mapstruct;
    requires org.slf4j;
    requires org.zalando.problem;
    requires spring.beans;
    requires spring.boot.actuator;
    requires spring.boot.autoconfigure;
    requires spring.boot;
    requires spring.context;
    requires spring.core;
    requires spring.web;
    requires static lombok;
    requires spring.webmvc;

    exports io.inugami.monitoring.springboot.actuator.feature;
    exports io.inugami.monitoring.springboot.actuator;
    exports io.inugami.monitoring.springboot.config;
    exports io.inugami.monitoring.springboot.exception;
    exports io.inugami.monitoring.springboot.filter;
    exports io.inugami.monitoring.springboot.partnerlog.feign;
    exports io.inugami.monitoring.springboot.request;

    uses ProblemAdditionalFieldBuilder;
    uses io.inugami.framework.interfaces.exceptions.ErrorCodeResolver;
    uses io.inugami.framework.interfaces.monitoring.JavaRestMethodTracker;
    uses FeignErrorCodeBuilderSpi;
    uses org.springframework.web.multipart.MultipartResolver;
    uses org.springframework.web.servlet.HandlerMapping;

    provides io.inugami.framework.interfaces.exceptions.ErrorCodeResolver with io.inugami.monitoring.springboot.exception.SpringDefaultErrorCodeResolver, io.inugami.monitoring.springboot.exception.FeignErrorCodeResolver;
    provides io.inugami.framework.interfaces.monitoring.JavaRestMethodTracker with io.inugami.monitoring.springboot.request.SpringRestMethodTracker;
    provides FeignErrorCodeBuilderSpi with io.inugami.monitoring.springboot.exception.DefaultFeignErrorCodeBuilderSpi;
}