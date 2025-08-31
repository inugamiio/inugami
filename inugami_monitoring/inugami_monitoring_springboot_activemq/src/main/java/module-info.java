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
module io.inugami.monitoring.springboot.activemq {
    requires io.inugami.framework.api;
    requires io.inugami.framework.interfaces;
    requires spring.beans;
    requires spring.core;
    requires spring.context;
    requires spring.boot.autoconfigure;
    requires static lombok;
    requires org.slf4j;
    requires com.fasterxml.jackson.databind;
    requires spring.jms;
    requires jakarta.messaging;
    requires activemq.client;
    requires spring.oxm;
    requires jakarta.xml.bind;
    requires java.naming;
}