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
package io.inugami.core.cdi.scheduler;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * SchedulerSystem
 * 
 * @author patrick_guillerm
 * @since 19 janv. 2018
 */
@Named
@ApplicationScoped
public class SchedulerSystem implements Serializable {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final long  serialVersionUID = -6448640249214014985L;
    
    @Inject
    private Event<SystemEvent> event;
    
    // =========================================================================
    // METHODS
    // =========================================================================
    public void fireSchedulerEvent() {
        event.fire(new SystemEvent());
    }
}
