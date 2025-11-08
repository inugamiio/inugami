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
package io.inugami.dashboard.infrastructure.sender;

import io.inugami.dashboard.api.domain.engine.dto.EventDoneDTO;
import io.inugami.dashboard.api.domain.sender.ISSESender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Slf4j
@Service
public class SSESender implements ISSESender {
    //==================================================================================================================
    // ATTRIBUTES
    //==================================================================================================================


    //==================================================================================================================
    // SEND
    //==================================================================================================================
    @Override
    public void onEventDone(final Collection<EventDoneDTO> eventsResult) {
        //TODO send SSE event
        log.info("send SSE event ....");
    }
}
