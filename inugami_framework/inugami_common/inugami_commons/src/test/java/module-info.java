import io.inugami.framework.interfaces.marshalling.jaxb.JaxbClassRegister;

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
module io.inugami.framework.commons.testing {

    requires io.inugami.framework.interfaces;
    requires io.inugami.framework.api;

    requires jakarta.servlet;
    requires lombok;
    requires org.slf4j;
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.dataformat.yaml;
    requires org.apache.commons.io;
    requires commons.lang;
    requires cglib;
    requires java.xml;
    requires jakarta.xml.bind;
    requires com.fasterxml.jackson.datatype.jdk8;
    requires com.fasterxml.jackson.datatype.jsr310;
    requires com.fasterxml.jackson.module.paramnames;
    requires io.inugami.framework.commons;
    requires io.inugami.commons.test;
    requires org.junit.jupiter.api;
    requires org.assertj.core;
    requires org.mockito;
    requires ch.qos.logback.core;
    requires ch.qos.logback.classic;
    requires net.bytebuddy;


    opens io.inugami.framework.commons.testing.data;
    opens io.inugami.framework.commons.testing.marshaling;
    opens io.inugami.framework.commons.testing.providers;
    opens io.inugami.framework.commons.testing.security;
    opens io.inugami.framework.commons.testing.threads;

    uses io.inugami.framework.interfaces.marshalling.jaxb.JaxbClassRegister;
    provides JaxbClassRegister with io.inugami.framework.commons.testing.marshaling.TestJaxbClassRegister;
}
