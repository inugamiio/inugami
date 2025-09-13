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
package io.inugami.dashboard.core.domain.administration;

import io.inugami.dashboard.api.administration.IPingService;
import io.inugami.dashboard.api.administration.dto.PingDTO;
import io.inugami.dashboard.api.administration.exception.AdministrationErrors;
import io.inugami.dashboard.core.configuration.InugamiConfiguration;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDateTime;

import static io.inugami.framework.interfaces.exceptions.Asserts.assertNotNull;

@RequiredArgsConstructor
@Service
public class PingService implements IPingService {
    //==================================================================================================================
    // ATTRIBUTES
    //==================================================================================================================
    private final Clock clock;

    private final InugamiConfiguration properties;

    //==================================================================================================================
    // API
    //==================================================================================================================
    @Override
    public PingDTO ping() {
        return PingDTO.builder()
                      .now(LocalDateTime.now(clock))
                      .applicationName(properties.getApplication().getName())
                      .build();
    }

    @Override
    public void pingTest() {
        assertNotNull(AdministrationErrors.UNDEFINED, null);
    }
}
