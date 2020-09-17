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

import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.sse.OutboundSseEvent;

import io.inugami.core.model.system.UserConnection;
import org.jboss.resteasy.plugins.providers.sse.SseEventOutputImpl;

/**
 * SseSocket
 * 
 * @author patrickguillerm
 * @since 4 oct. 2018
 */
public class SseSocket extends SseEventOutputImpl {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private final UserConnection userConnexion;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public SseSocket(final MessageBodyWriter<OutboundSseEvent> writer, final UserConnection userConnexion) {
        super(writer);
        this.userConnexion = userConnexion;
    }
    
    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    public UserConnection getUserConnexion() {
        return userConnexion;
    }
}
