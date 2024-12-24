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
module io.inugami.framework.api {

    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.datatype.jdk8;
    requires com.fasterxml.jackson.datatype.jsr310;
    requires com.fasterxml.jackson.module.paramnames;
    requires com.fasterxml.jackson.dataformat.yaml;
    requires io.inugami.framework.interfaces;
    requires jakarta.servlet;
    requires lombok;
    requires org.slf4j;
    requires java.desktop;
    requires okhttp3;
    requires kotlin.stdlib;

    exports io.inugami.framework.api.configurtation;
    exports io.inugami.framework.api.listeners;
    exports io.inugami.framework.api.loggers.mdc.initializer;
    exports io.inugami.framework.api.loggers.mdc.mapper;
    exports io.inugami.framework.api.marshalling;
    exports io.inugami.framework.api.models.data;
    exports io.inugami.framework.api.monitoring;
    exports io.inugami.framework.api.monitoring.cors;
    exports io.inugami.framework.api.monitoring.logs;
    exports io.inugami.framework.api.processors;
    exports io.inugami.framework.api.processors.fifo;
    exports io.inugami.framework.api.providers;
    exports io.inugami.framework.api.spi;
    exports io.inugami.framework.api.tools;
    exports io.inugami.framework.api.connectors;

    uses io.inugami.framework.interfaces.configurtation.ConfigurationSpi;
    uses io.inugami.framework.interfaces.marshalling.ModuleRegisterSpi;
    uses io.inugami.framework.interfaces.marshalling.JacksonMarshallerSpi;
    uses io.inugami.framework.interfaces.monitoring.MdcServiceSpi;
    uses io.inugami.framework.interfaces.monitoring.MonitoringLoaderSpi;
    uses io.inugami.framework.interfaces.monitoring.logger.initializer.MdcInitializerSpi;
    uses io.inugami.framework.interfaces.monitoring.logger.mapper.LoggerMdcMappingSPI;
    uses io.inugami.framework.interfaces.listeners.ApplicationLifecycleSPI;
    uses io.inugami.framework.interfaces.tools.TemplateProviderSPI;

    provides io.inugami.framework.interfaces.monitoring.MdcServiceSpi with io.inugami.framework.api.monitoring.MdcService;
    provides io.inugami.framework.interfaces.monitoring.logger.mapper.LoggerMdcMappingSPI with io.inugami.framework.api.loggers.mdc.mapper.MdcMapperDefaultBooleanValue, io.inugami.framework.api.loggers.mdc.mapper.MdcMapperDefaultDoubleValue, io.inugami.framework.api.loggers.mdc.mapper.MdcMapperDefaultIntValue, io.inugami.framework.api.loggers.mdc.mapper.MdcMapperDefaultLongValue, io.inugami.framework.api.loggers.mdc.mapper.MdcMapperDefault;
    provides io.inugami.framework.interfaces.configurtation.ConfigurationSpi with io.inugami.framework.api.configurtation.DefaultConfigurationSpi;
}
