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
package io.inugami.dashboard.webapp;

import io.inugami.framework.api.tools.PortGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.IOException;

@Slf4j
@Configuration
public class InugamiInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    public static final int SERVER_PORT = PortGenerator.generateFor("server.port");

    @Override
    public void initialize(final ConfigurableApplicationContext applicationContext) {
        try {
            System.setProperty("inugami-home", new File("./src/test_it/resources/workspace").getCanonicalPath());
            System.setProperty("server.port", String.valueOf(SERVER_PORT));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
