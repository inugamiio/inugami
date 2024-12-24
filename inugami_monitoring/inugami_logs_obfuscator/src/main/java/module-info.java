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
module io.inugami.logs.obfuscator {
    requires jakarta.servlet;
    requires lombok;
    requires org.slf4j;
    requires ch.qos.logback.classic;
    requires ch.qos.logback.core;
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires io.inugami.framework.interfaces;
    requires io.inugami.framework.api;

    exports io.inugami.logs.obfuscator.api;
    exports io.inugami.logs.obfuscator.appender;
    exports io.inugami.logs.obfuscator.appender.writer;
    exports io.inugami.logs.obfuscator.encoder;
    exports io.inugami.logs.obfuscator.obfuscators;
    exports io.inugami.logs.obfuscator.tools;

    uses io.inugami.framework.interfaces.marshalling.ModuleRegisterSpi;
    uses io.inugami.framework.interfaces.marshalling.JacksonMarshallerSpi;
    uses io.inugami.framework.interfaces.monitoring.MdcServiceSpi;
    uses io.inugami.framework.interfaces.monitoring.MonitoringLoaderSpi;
    uses io.inugami.framework.interfaces.monitoring.logger.initializer.MdcInitializerSpi;
    uses io.inugami.framework.interfaces.monitoring.logger.mapper.LoggerMdcMappingSPI;
    uses io.inugami.framework.interfaces.listeners.ApplicationLifecycleSPI;
    uses io.inugami.framework.interfaces.tools.TemplateProviderSPI;
    uses io.inugami.logs.obfuscator.api.ObfuscatorSpi;
    uses io.inugami.framework.interfaces.configurtation.ConfigurationSpi;

    provides io.inugami.logs.obfuscator.api.ObfuscatorSpi with io.inugami.logs.obfuscator.obfuscators.JsonPasswordObfuscator, io.inugami.logs.obfuscator.obfuscators.BasicPasswordObfuscator, io.inugami.logs.obfuscator.obfuscators.XMLPasswordObfuscator, io.inugami.logs.obfuscator.obfuscators.JsonAuthorizationObfuscator, io.inugami.logs.obfuscator.obfuscators.BasicAuthorizationObfuscator, io.inugami.logs.obfuscator.obfuscators.AccessControlAllowHeaderObfuscator, io.inugami.logs.obfuscator.obfuscators.AccessControlExposedHeaderObfuscator, io.inugami.logs.obfuscator.obfuscators.CookieObfuscator;
}