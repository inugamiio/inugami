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
package io.inugami.core.services.sse;

import io.inugami.api.models.data.basic.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;

/**
 * SendSseEventTask
 *
 * @author patrick_guillerm
 * @since 12 sept. 2017
 */
@Builder(toBuilder = true)
@AllArgsConstructor
@ToString
@Getter
public class SendSseEvent implements Serializable {

    private static final long   serialVersionUID = 2093174535542339803L;
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private final        String cron;

    private final String channel;

    private final String event;

    private final JsonObject value;

    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public SendSseEvent(final JsonObject value, final String event, final String cron, final String channel) {
        super();
        this.channel = channel;
        this.event = event;
        this.cron = cron;
        this.value = value;
    }


}
