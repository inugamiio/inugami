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
open module io.inugami.commons.test {

    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.datatype.jdk8;
    requires com.fasterxml.jackson.datatype.jsr310;
    requires com.fasterxml.jackson.module.paramnames;
    requires com.fasterxml.jackson.dataformat.yaml;

    requires io.inugami.framework.interfaces;
    requires io.inugami.framework.api;
    requires lombok;
    requires org.junit.jupiter.api;
    requires org.slf4j;
    requires org.apache.commons.io;
    requires org.assertj.core;
    requires org.mockito;
    requires ch.qos.logback.core;
    requires ch.qos.logback.classic;
    requires net.bytebuddy;

    exports io.inugami.commons.test.api;
    exports io.inugami.commons.test.dto;
    exports io.inugami.commons.test.logs;
    exports io.inugami.commons.test;


    uses io.inugami.framework.interfaces.configurtation.ConfigurationSpi;
    uses io.inugami.framework.interfaces.marshalling.ModuleRegisterSpi;
    uses io.inugami.framework.interfaces.marshalling.JacksonMarshallerSpi;
    uses io.inugami.framework.interfaces.monitoring.MdcServiceSpi;
    uses io.inugami.framework.interfaces.monitoring.MonitoringLoaderSpi;
    uses io.inugami.framework.interfaces.monitoring.logger.initializer.MdcInitializerSpi;
    uses io.inugami.framework.interfaces.monitoring.logger.mapper.LoggerMdcMappingSPI;
}
