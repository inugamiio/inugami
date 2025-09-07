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
open module io.inugami.framework.commons {
    requires cglib;
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.dataformat.yaml;
    requires com.fasterxml.jackson.datatype.jdk8;
    requires com.fasterxml.jackson.datatype.jsr310;
    requires com.fasterxml.jackson.module.paramnames;
    requires commons.lang;
    requires io.inugami.framework.api;
    requires io.inugami.framework.interfaces;
    requires jakarta.servlet;
    requires jakarta.xml.bind;
    requires java.xml;
    requires lombok;
    requires org.apache.commons.io;
    requires org.slf4j;

    exports io.inugami.framework.commons.data;
    exports io.inugami.framework.commons.files;
    exports io.inugami.framework.commons.marshaling.jaxb;
    exports io.inugami.framework.commons.marshaling;
    exports io.inugami.framework.commons.messages;
    exports io.inugami.framework.commons.providers;
    exports io.inugami.framework.commons.security;
    exports io.inugami.framework.commons.spi;
    exports io.inugami.framework.commons.threads;
    exports io.inugami.framework.commons.tools;
    exports io.inugami.framework.commons.writer;

    uses io.inugami.framework.interfaces.marshalling.XmlJaxbMarshallerSpi;
    uses io.inugami.framework.interfaces.marshalling.jaxb.JaxbAdapterSpi;
    uses io.inugami.framework.interfaces.marshalling.jaxb.JaxbClassRegister;
    uses io.inugami.framework.interfaces.monitoring.MonitoringInitializer;

    provides io.inugami.framework.interfaces.marshalling.XmlJaxbMarshallerSpi with io.inugami.framework.commons.marshaling.DefaultXmlJaxbMarshallerSpi;
    provides io.inugami.framework.interfaces.marshalling.jaxb.JaxbAdapterSpi with io.inugami.framework.commons.marshaling.jaxb.LocalDateTimeAdapter, io.inugami.framework.commons.marshaling.jaxb.LocalDateAdapter;
    provides io.inugami.framework.interfaces.marshalling.jaxb.JaxbClassRegister with io.inugami.framework.commons.marshaling.jaxb.DefaultJaxbClassRegister;
}
