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
package io.inugami.dashboard.interfaces.core.domain.administration;

import io.inugami.commons.test.UnitTestData;
import io.inugami.commons.test.mock.MockContext;
import io.inugami.commons.test.mock.MockGenerator;
import io.inugami.commons.test.mock.MockOpenApiContext;
import io.inugami.dashboard.api.domain.administration.IPingService;
import io.inugami.dashboard.api.domain.administration.dto.PingDTO;
import io.inugami.dashboard.interfaces.core.domain.administration.mapper.InugamiInterfaceAdministationConfiguration;
import io.inugami.dashboard.interfaces.domain.administration.PingRestClient;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static io.inugami.commons.test.UnitTestHelper.assertText;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PingRestControllerTest {
    //==================================================================================================================
    // ATTRIBUTES
    //==================================================================================================================
    private static final String       PING_FOLDER = "io/inugami/dashboard/interfaces/domain/administration/pingRestClient/ping";

    @Mock
    private IPingService pingService;

    @AfterAll
    public static void generateOpenApi() {
        MockGenerator.generateOpenApiDocumentation(MockOpenApiContext.builder()
                                                                     .restClientClass(PingRestClient.class)
                                                                     .folders(List.of(PING_FOLDER))
                                                                     .build());
    }

    //==================================================================================================================
    // TEST
    //==================================================================================================================
    @Test
    void ping_nominal() {
        when(pingService.ping()).thenReturn(PingDTO.builder()
                                                   .applicationName("inugami")
                                                   .now(UnitTestData.DATE_TIME)
                                                   .build());
        final var response = controller().ping();
        assertText(response,
                   """
                           {
                             "applicationName" : "inugami",
                             "now" : "2023-06-01T12:00:00"
                           }
                           """);

        MockGenerator.generate(MockContext.builder()
                                          .folder(PING_FOLDER)
                                          .get("/administration/ping")
                                          .addRequestHeaderTracking()
                                          .addResponseHeaderTracking()
                                          .statusSuccess()
                                          .responsePayload(response)
                                          .build());
    }

    //==================================================================================================================
    // TOOLS
    //==================================================================================================================
    private PingRestController controller() {
        return PingRestController.builder()
                                 .pingService(pingService)
                                 .pingDTORestMapper(new InugamiInterfaceAdministationConfiguration().pingDTORestMapper())
                                 .build();
    }

}