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

import io.inugami.dashboard.api.domain.administration.IPingService;
import io.inugami.dashboard.api.domain.administration.exception.AdministrationErrors;
import io.inugami.dashboard.interfaces.domain.administration.PingRestClient;
import io.inugami.dashboard.interfaces.domain.administration.dto.PingDTO;
import io.inugami.dashboard.interfaces.core.domain.administration.mapper.PingDTORestMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import static io.inugami.framework.interfaces.exceptions.Asserts.assertNotNull;

@RequiredArgsConstructor
@RestController
public class PingRestController implements PingRestClient {
    //==================================================================================================================
    // ATTRIBUTES
    //==================================================================================================================
    private final IPingService      pingService;
    private final PingDTORestMapper pingDTORestMapper;

    //==================================================================================================================
    // READ
    //==================================================================================================================
    @Override
    public PingDTO ping() {
        return pingDTORestMapper.convertToRestDTO(pingService.ping());
    }

    @Override
    public PingDTO pingTest() {
        assertNotNull(AdministrationErrors.UNDEFINED, null);
        return null;
    }
}
