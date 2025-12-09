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

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
@Slf4j
@EnableConfigurationProperties
@ActiveProfiles("test")
@ContextConfiguration(
        initializers = {
                InugamiInitializer.class
        })
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
        classes ={
                SpringBootIntegrationTestConfiguration.class,
                InugamiDashboardApplication.class
        } )
public class SpringBootIntegrationTest {
    @LocalServerPort
    protected int port;


    protected void waiting(final Long delay){
        try {
            log.info("waiting {}",delay);
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            //nothing to do
        }
    }

}
