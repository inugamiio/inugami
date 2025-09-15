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
open module io.inugami.framework.interfaces {
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.datatype.jdk8;
    requires com.fasterxml.jackson.datatype.jsr310;
    requires com.fasterxml.jackson.module.paramnames;
    requires jakarta.servlet;
    requires jakarta.xml.bind;
    requires java.desktop;
    requires java.naming;
    requires java.xml;
    requires okhttp3;
    requires org.slf4j;
    requires spring.web;
    requires spring.webmvc;
    requires static lombok;

    exports io.inugami.framework.interfaces.alertings;
    exports io.inugami.framework.interfaces.concurrent;
    exports io.inugami.framework.interfaces.configurtation;
    exports io.inugami.framework.interfaces.connectors.config;
    exports io.inugami.framework.interfaces.connectors.exceptions;
    exports io.inugami.framework.interfaces.connectors;
    exports io.inugami.framework.interfaces.ctx;
    exports io.inugami.framework.interfaces.dao.event;
    exports io.inugami.framework.interfaces.dao;
    exports io.inugami.framework.interfaces.documentation;
    exports io.inugami.framework.interfaces.engine;
    exports io.inugami.framework.interfaces.exceptions.asserts;
    exports io.inugami.framework.interfaces.exceptions.connector;
    exports io.inugami.framework.interfaces.exceptions.services;
    exports io.inugami.framework.interfaces.exceptions;
    exports io.inugami.framework.interfaces.feature;
    exports io.inugami.framework.interfaces.files.zip;
    exports io.inugami.framework.interfaces.functionnals;
    exports io.inugami.framework.interfaces.handlers;
    exports io.inugami.framework.interfaces.listeners;
    exports io.inugami.framework.interfaces.mapping;
    exports io.inugami.framework.interfaces.marshalling.jaxb;
    exports io.inugami.framework.interfaces.marshalling.serializers;
    exports io.inugami.framework.interfaces.marshalling;
    exports io.inugami.framework.interfaces.metrics.dto;
    exports io.inugami.framework.interfaces.metrics;
    exports io.inugami.framework.interfaces.models.basic;
    exports io.inugami.framework.interfaces.models.crud;
    exports io.inugami.framework.interfaces.models.event;
    exports io.inugami.framework.interfaces.models.graphite;
    exports io.inugami.framework.interfaces.models.maven;
    exports io.inugami.framework.interfaces.models.number;
    exports io.inugami.framework.interfaces.models.search;
    exports io.inugami.framework.interfaces.models.tools;
    exports io.inugami.framework.interfaces.models;
    exports io.inugami.framework.interfaces.monitoring.core;
    exports io.inugami.framework.interfaces.monitoring.data;
    exports io.inugami.framework.interfaces.monitoring.dto;
    exports io.inugami.framework.interfaces.monitoring.interceptors;
    exports io.inugami.framework.interfaces.monitoring.logger.initializer;
    exports io.inugami.framework.interfaces.monitoring.logger.mapper;
    exports io.inugami.framework.interfaces.monitoring.logger;
    exports io.inugami.framework.interfaces.monitoring.models;
    exports io.inugami.framework.interfaces.monitoring.partner;
    exports io.inugami.framework.interfaces.monitoring.senders;
    exports io.inugami.framework.interfaces.monitoring.sensors;
    exports io.inugami.framework.interfaces.monitoring.spring.feign;
    exports io.inugami.framework.interfaces.monitoring.threads;
    exports io.inugami.framework.interfaces.monitoring;
    exports io.inugami.framework.interfaces.processors;
    exports io.inugami.framework.interfaces.providers;
    exports io.inugami.framework.interfaces.scan;
    exports io.inugami.framework.interfaces.spi;
    exports io.inugami.framework.interfaces.spring;
    exports io.inugami.framework.interfaces.rest;
    exports io.inugami.framework.interfaces.task;
    exports io.inugami.framework.interfaces.threads;
    exports io.inugami.framework.interfaces.tools.fifo;
    exports io.inugami.framework.interfaces.tools.reflection;
    exports io.inugami.framework.interfaces.tools.strategy;
    exports io.inugami.framework.interfaces.tools;
    exports io.inugami.framework.interfaces.exceptions.dto;

    uses io.inugami.framework.interfaces.configurtation.ConfigurationSpi;
    uses io.inugami.framework.interfaces.configurtation.ProviderAttributFunction;
    uses io.inugami.framework.interfaces.exceptions.ErrorCodeResolver;
    uses io.inugami.framework.interfaces.exceptions.ExceptionHandlerMapper;
    uses io.inugami.framework.interfaces.exceptions.ExceptionResolver;
    uses io.inugami.framework.interfaces.exceptions.WarningTracker;
    uses io.inugami.framework.interfaces.listeners.ApplicationLifecycleSPI;
    uses io.inugami.framework.interfaces.marshalling.JacksonMarshallerSpi;
    uses io.inugami.framework.interfaces.marshalling.ModuleRegisterSpi;
    uses io.inugami.framework.interfaces.marshalling.XmlJaxbMarshallerSpi;
    uses io.inugami.framework.interfaces.marshalling.jaxb.JaxbClassRegister;
    uses io.inugami.framework.interfaces.monitoring.FilterInterceptorCachePurgeStrategy;
    uses io.inugami.framework.interfaces.monitoring.Interceptable;
    uses io.inugami.framework.interfaces.monitoring.IoLogContentDisplayResolverSPI;
    uses io.inugami.framework.interfaces.monitoring.JavaRestMethodResolver;
    uses io.inugami.framework.interfaces.monitoring.JavaRestMethodTracker;
    uses io.inugami.framework.interfaces.monitoring.MdcCleanerSPI;
    uses io.inugami.framework.interfaces.monitoring.MdcServiceSpi;
    uses io.inugami.framework.interfaces.monitoring.MonitoringLoaderSpi;
    uses io.inugami.framework.interfaces.monitoring.Obfuscator;
    uses io.inugami.framework.interfaces.monitoring.ResponseListener;
    uses io.inugami.framework.interfaces.monitoring.ServiceNameResolver;
    uses io.inugami.framework.interfaces.monitoring.core.CorsHeadersSpi;
    uses io.inugami.framework.interfaces.monitoring.interceptors.MonitoringFilterInterceptor;
    uses io.inugami.framework.interfaces.monitoring.logger.initializer.MdcInitializerSpi;
    uses io.inugami.framework.interfaces.monitoring.logger.mapper.LoggerMdcMappingSPI;
    uses io.inugami.framework.interfaces.monitoring.spring.feign.FeignErrorCodeBuilderSpi;
    uses io.inugami.framework.interfaces.tools.StringDataCleaner;
    uses io.inugami.framework.interfaces.tools.TemplateProviderSPI;
    uses org.springframework.web.multipart.MultipartResolver;
    uses org.springframework.web.servlet.HandlerMapping;
    uses io.inugami.framework.interfaces.monitoring.ServicesSensorAggregator;
    uses io.inugami.framework.interfaces.exceptions.ProblemAdditionalFieldBuilder;
}