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
package org.inugami.core.alertings;

import org.inugami.api.models.Gav;
import org.inugami.api.models.events.GenericEvent;

/**
 * GavEvent
 * 
 * @author patrick_guillerm
 * @since 26 déc. 2017
 */
public class GavEvent {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private Gav          gav;
    
    private GenericEvent event;
    
    // =========================================================================
    // CONSTRUCTOR
    // =========================================================================
    public GavEvent(GenericEvent event, Gav gav) {
        this.event = event;
        this.gav = gav;
    }
    
    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    public Gav getGav() {
        return gav;
    }
    
    public void setGav(Gav gav) {
        this.gav = gav;
    }
    
    public GenericEvent getEvent() {
        return event;
    }
    
    public void setEvent(GenericEvent event) {
        this.event = event;
    }
}
