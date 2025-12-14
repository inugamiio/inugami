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
package io.inugami.monitoring.springboot.spring;

import io.inugami.framework.api.tools.PortGenerator;
import io.restassured.RestAssured;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

@Slf4j
public class InugamiInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    public static final int SERVER_PORT = PortGenerator.generateFor("server.port");

    @Override
    public void initialize(final ConfigurableApplicationContext applicationContext) {
        System.setProperty("server.port", String.valueOf(SERVER_PORT));
        RestAssured.baseURI = "http://localhost";
        RestAssured.port    = SERVER_PORT;
    }
}
