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
module inugami.monitoring.logs.obfuscator {
    requires jakarta.servlet;
    requires lombok;
    requires org.slf4j;
    requires ch.qos.logback.classic;
    requires ch.qos.logback.core;
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires inugami.framework.api.interfaces;
    requires inugami.framework.api;


    uses io.inugami.framework.interfaces.marshalling.ModuleRegisterSpi;
    uses io.inugami.framework.interfaces.marshalling.JacksonMarshallerSpi;
    uses io.inugami.framework.interfaces.monitoring.MdcServiceSpi;
    uses io.inugami.framework.interfaces.monitoring.MonitoringLoaderSpi;
    uses io.inugami.framework.interfaces.monitoring.logger.initializer.MdcInitializerSpi;
    uses io.inugami.framework.interfaces.monitoring.logger.mapper.LoggerMdcMappingSPI;
    uses io.inugami.framework.interfaces.listeners.ApplicationLifecycleSPI;
    uses io.inugami.framework.interfaces.tools.TemplateProviderSPI;
}