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
module inugami.api.interfaces {
    requires jakarta.servlet;
    requires lombok;
    requires org.slf4j;
    requires java.desktop;
    requires com.fasterxml.jackson.annotation;

    exports io.inugami.framework.interfaces.alertings;
    exports io.inugami.framework.interfaces.concurrent;
    exports io.inugami.framework.interfaces.configurtation;
    exports io.inugami.framework.interfaces.ctx;
    exports io.inugami.framework.interfaces.dao;
    exports io.inugami.framework.interfaces.dao.event;
    exports io.inugami.framework.interfaces.documentation;
    exports io.inugami.framework.interfaces.exceptions;
    exports io.inugami.framework.interfaces.exceptions.connector;
    exports io.inugami.framework.interfaces.exceptions.services;
    exports io.inugami.framework.interfaces.feature;
    exports io.inugami.framework.interfaces.functionnals;
    exports io.inugami.framework.interfaces.handlers;
    exports io.inugami.framework.interfaces.listeners;
    exports io.inugami.framework.interfaces.mapping;
    exports io.inugami.framework.interfaces.marshalling;
    exports io.inugami.framework.interfaces.metrics;
    exports io.inugami.framework.interfaces.models;
    exports io.inugami.framework.interfaces.models.basic;
    exports io.inugami.framework.interfaces.models.event;
    exports io.inugami.framework.interfaces.models.maven;
    exports io.inugami.framework.interfaces.models.number;
    exports io.inugami.framework.interfaces.models.tools;
    exports io.inugami.framework.interfaces.monitoring;
    exports io.inugami.framework.interfaces.monitoring.core;
    exports io.inugami.framework.interfaces.monitoring.data;
    exports io.inugami.framework.interfaces.monitoring.models;
    exports io.inugami.framework.interfaces.monitoring.interceptors;
    exports io.inugami.framework.interfaces.monitoring.logger;
    exports io.inugami.framework.interfaces.monitoring.logger.initializer;
    exports io.inugami.framework.interfaces.monitoring.logger.mapper;
    exports io.inugami.framework.interfaces.monitoring.partner;
    exports io.inugami.framework.interfaces.monitoring.senders;
    exports io.inugami.framework.interfaces.monitoring.sensors;
    exports io.inugami.framework.interfaces.monitoring.threads;
    exports io.inugami.framework.interfaces.processors;
    exports io.inugami.framework.interfaces.providers;
    exports io.inugami.framework.interfaces.spi;
    exports io.inugami.framework.interfaces.spring;
    exports io.inugami.framework.interfaces.tools;
    exports io.inugami.framework.interfaces.tools.fifo;
    exports io.inugami.framework.interfaces.tools.reflection;
    exports io.inugami.framework.interfaces.tools.strategy;
    exports io.inugami.framework.interfaces.task;
}