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
    requires ch.qos.logback.classic;
    requires ch.qos.logback.core;
    requires com.fasterxml.jackson.annotation;
    requires tools.jackson.core;
    requires tools.jackson.databind;
    requires tools.jackson.dataformat.yaml;
    requires tools.jackson.datatype.javatime;
    requires io.inugami.framework.api;
    requires io.inugami.framework.interfaces;
    requires io.inugami.logs.obfuscator;
    requires lombok;
    requires net.bytebuddy;
    requires org.apache.commons.io;
    requires org.assertj.core;
    requires org.junit.jupiter.api;
    requires org.mockito;
    requires org.slf4j;
    requires org.jspecify;
    requires annotations;
    exports io.inugami.commons.test.api;
    exports io.inugami.commons.test.mock;
    exports io.inugami.commons.test.dto;
    exports io.inugami.commons.test.logs;
    exports io.inugami.commons.test;

    uses io.inugami.framework.interfaces.configurtation.ConfigurationSpi;
    uses io.inugami.framework.interfaces.marshalling.JacksonMarshallerSpi;
    uses io.inugami.framework.interfaces.marshalling.ModuleRegisterSpi;
    uses io.inugami.framework.interfaces.monitoring.MdcServiceSpi;
    uses io.inugami.framework.interfaces.monitoring.MonitoringLoaderSpi;
    uses io.inugami.framework.interfaces.monitoring.logger.initializer.MdcInitializerSpi;
    uses io.inugami.framework.interfaces.monitoring.logger.mapper.LoggerMdcMappingSPI;

}
