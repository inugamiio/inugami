package io.inugami.dashboard.webapp.domain.administration;/* --------------------------------------------------------------------
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

import io.inugami.commons.test.api.LocalDateTimeLineMatcher;
import io.inugami.commons.test.api.SkipLineMatcher;
import io.inugami.dashboard.interfaces.domain.administration.PingRestClient;
import io.inugami.dashboard.webapp.SpringBootIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static io.inugami.commons.test.UnitTestHelper.assertText;

public class PingIT extends SpringBootIntegrationTest {
    //==================================================================================================================
    // ATTRIBUTES
    //==================================================================================================================
    private @Autowired PingRestClient pingRestClient;

    //==================================================================================================================
    // TEST
    //==================================================================================================================
    @Test
    void ping_nominal() {
        assertText(pingRestClient.ping(),
                   """
                           {
                             "applicationName" : "inugami",
                             "now" : "2025-12-09T03:50:56.100681298"
                           }
                           """,
                   LocalDateTimeLineMatcher.of(2));
    }
}
