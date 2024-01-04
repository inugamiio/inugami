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
    requires com.fasterxml.jackson.annotation;
    requires jakarta.servlet;
    requires lombok;
    requires org.slf4j;
    requires java.desktop;

    exports io.inugami.interfaces.alertings;
    exports io.inugami.interfaces.concurrent;
    exports io.inugami.interfaces.configurtation;
    exports io.inugami.interfaces.ctx;
    exports io.inugami.interfaces.dao;
    exports io.inugami.interfaces.dao.event;
    exports io.inugami.interfaces.documentation;
    exports io.inugami.interfaces.exceptions;
    exports io.inugami.interfaces.exceptions.connector;
    exports io.inugami.interfaces.exceptions.services;
    exports io.inugami.interfaces.feature;
    exports io.inugami.interfaces.functionnals;
    exports io.inugami.interfaces.handlers;
    exports io.inugami.interfaces.listeners;
    exports io.inugami.interfaces.mapping;
    exports io.inugami.interfaces.marshalling;
    exports io.inugami.interfaces.metrics;
    exports io.inugami.interfaces.models;
    exports io.inugami.interfaces.models.basic;
    exports io.inugami.interfaces.models.event;
    exports io.inugami.interfaces.models.maven;
    exports io.inugami.interfaces.models.number;
    exports io.inugami.interfaces.models.tools;
    exports io.inugami.interfaces.monitoring;
    exports io.inugami.interfaces.monitoring.core;
    exports io.inugami.interfaces.monitoring.data;
    exports io.inugami.interfaces.monitoring.models;
    exports io.inugami.interfaces.monitoring.interceptors;
    exports io.inugami.interfaces.monitoring.logger;
    exports io.inugami.interfaces.monitoring.logger.initializer;
    exports io.inugami.interfaces.monitoring.logger.mapper;
    exports io.inugami.interfaces.monitoring.partner;
    exports io.inugami.interfaces.monitoring.senders;
    exports io.inugami.interfaces.monitoring.sensors;
    exports io.inugami.interfaces.monitoring.threads;
    exports io.inugami.interfaces.processors;
    exports io.inugami.interfaces.providers;
    exports io.inugami.interfaces.spi;
    exports io.inugami.interfaces.spring;
    exports io.inugami.interfaces.tools;
    exports io.inugami.interfaces.tools.fifo;
    exports io.inugami.interfaces.tools.reflection;
    exports io.inugami.interfaces.tools.strategy;
    exports io.inugami.interfaces.task;
}